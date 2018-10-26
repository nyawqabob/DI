package by.iba.di.bean;

import by.iba.di.annotations.Proxy;
import by.iba.di.annotations.scopes.ScopeType;
import by.iba.di.bean.definition.BeanDefinition;
import by.iba.di.bean.definition.BeanDefinitionsHandler;
import by.iba.di.bean.exception.BeanException;
import by.iba.di.proxy.ByteBuddyProxyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BeansHandler {

    private Map<String, Object> singletonBeans = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LogManager.getLogger(BeansHandler.class);

    private static AtomicBoolean isCreated = new AtomicBoolean();
    private static Lock lock = new ReentrantLock();
    private static BeansHandler instance;

    private BeansHandler(Set<Class<?>> scannedClasses) {
        createSingletonBeans(scannedClasses);
    }

    public static BeansHandler getInstance(Set<Class<?>> scannedClasses) {
        if (!isCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new BeansHandler(scannedClasses);
                    isCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public Map<String, Object> getSingletonBeans() {
        return singletonBeans;
    }

    private void createSingletonBeans(Set<Class<?>> scannedClasses) {
        BeanDefinitionsHandler beanDefinitionsHandler = BeanDefinitionsHandler.getInstance(scannedClasses);
        Map<String, BeanDefinition> singletonBeanDefinitionsMap = filterBeanDefinitionsByScopeType(beanDefinitionsHandler.getBeanDefinitions(), ScopeType.SINGLETON);
        ByteBuddyProxyHandler byteBuddyProxyHandler = new ByteBuddyProxyHandler();

        for (Map.Entry<String, BeanDefinition> beanDefinition : singletonBeanDefinitionsMap.entrySet()) {
            try {
                Object object;
                if (beanDefinition.getValue().getClassType().isAnnotationPresent(Proxy.class)) {
                    object = byteBuddyProxyHandler.getProxiedObject(beanDefinition.getValue().getClassType());
                } else {
                    ClassLoader clazzLoader = beanDefinition.getValue().getClassType().getClassLoader();
                    object = clazzLoader.loadClass(beanDefinition.getValue().getClassType().getName()).newInstance();
                }
                singletonBeans.put(beanDefinition.getKey(), object);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw new BeanException(e.getMessage(), e);
            }
        }
    }

    private Map<String, BeanDefinition> filterBeanDefinitionsByScopeType(Map<String, BeanDefinition> beanDefinitions, ScopeType scopeType) {

        return beanDefinitions.entrySet().stream().filter(x -> x.getValue().
                getScope().equals(scopeType)).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
    }

}
