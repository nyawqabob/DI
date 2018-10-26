package by.iba.di.bean.definition;

import by.iba.di.annotations.Autowired;
import by.iba.di.annotations.Proxy;
import by.iba.di.annotations.Scope;
import by.iba.di.annotations.scopes.ScopeType;
import by.iba.di.bean.definition.builder.BeanDefinitionBuilder;
import by.iba.di.bean.definition.builder.v1_0.BeanDefinitionBuilderImpl;
import by.iba.di.bean.exception.BeanException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BeanDefinitionsHandler {

    private Set<Class<?>> autowiredClasses = new HashSet<>();
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LogManager.getLogger(BeanDefinitionsHandler.class);

    private static AtomicBoolean isCreated = new AtomicBoolean();
    private static Lock lock = new ReentrantLock();
    private static BeanDefinitionsHandler instance;

    private BeanDefinitionsHandler(Set<Class<?>> scannedClasses) {
        createBeanDefinitions(scannedClasses);
    }

    public static BeanDefinitionsHandler getInstance(Set<Class<?>> scannedClasses) {
        if (!isCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new BeanDefinitionsHandler(scannedClasses);
                    isCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public static BeanDefinitionsHandler getInstance() {
        if (!isCreated.get()) {
            throw new BeanException("Firstly run DI");
        }
        return instance;
    }

    private void createBeanDefinitions(Set<Class<?>> scannedClasses) {
        for (Class clazz : scannedClasses) {
            BeanDefinitionBuilder beanDefinitionBuilder = new BeanDefinitionBuilderImpl();
            String beanName = clazz.getName();
            searchAutowiredFields(clazz);
            if (beanDefinitions.containsKey(beanName)) {
                LOGGER.error("Two equal beans");
                throw new BeanException("Two equal beans");
            }
            beanDefinitionBuilder.setClassType(clazz).setBeanName(beanName);
            if (clazz.isAnnotationPresent(Scope.class)) {
                Scope annotation = (Scope) clazz.getAnnotation(Scope.class);
                beanDefinitionBuilder.setScope(annotation.value());
            } else {
                beanDefinitionBuilder.setScope(ScopeType.SINGLETON);
            }

            BeanDefinition beanDefinition = beanDefinitionBuilder.build();
            if (!autowiredClasses.contains(clazz)) {
                beanDefinitions.put(beanName, beanDefinition);
            }
        }
        Set<Class<?>> setHelper = new HashSet<>(autowiredClasses);
        autowiredClasses.clear();
        if (!setHelper.isEmpty()) {
            createBeanDefinitions(setHelper);
        }
    }

    public boolean isExistBeanDefinitionWithClassType(Object classType) {
        boolean isExist = false;
        for (Map.Entry<String, BeanDefinition> beanDefinition : beanDefinitions.entrySet()) {
            if (beanDefinition.getValue().getClassType().equals(classType) && !beanDefinition.getValue().getClassType().isAnnotationPresent(Proxy.class)) {
                isExist = true;
            }
        }
        return isExist;
    }

    public boolean isExistBeanDefinitionWithBeanName(Object beanName) {
        boolean isExist = false;
        for (Map.Entry<String, BeanDefinition> beanDefinition : beanDefinitions.entrySet()) {
            if (beanDefinition.getKey().equals(beanName) && !beanDefinition.getValue().getClassType().isAnnotationPresent(Proxy.class)) {
                isExist = true;
            }
        }
        return isExist;
    }

    public Class getClassTypeByBeanDefinitionName(String beanName) {
        Class classType = null;
        for (Map.Entry<String, BeanDefinition> beanDefinition : beanDefinitions.entrySet()) {
            if (beanDefinition.getKey().equals(beanName)) {
                classType = beanDefinition.getValue().getClassType();
            }
        }
        return classType;
    }

    public Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }


    private void searchAutowiredFields(Class clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            String fieldTypeName = field.getType().getName();
            if (field.isAnnotationPresent(Autowired.class) && !beanDefinitions.containsKey(fieldTypeName)) {
                autowiredClasses.add(field.getType());
            }
        }
    }


}
