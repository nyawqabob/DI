package by.iba.di.bean.factory.v1_0;

import by.iba.di.Scanner;
import by.iba.di.bean.BeansHandler;
import by.iba.di.bean.definition.BeanDefinitionsHandler;
import by.iba.di.bean.exception.BeanException;
import by.iba.di.bean.factory.BeanFactory;
import by.iba.di.bean.parameter_type.ParameterType;
import by.iba.di.bean.parameter_type.ParameterTypeHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class BeanFactoryImpl implements BeanFactory {
    private BeansHandler beansHandler;

    private static final Logger LOGGER = LogManager.getLogger(BeanFactoryImpl.class);

    public BeanFactoryImpl() {
        Scanner scanner = new Scanner();
        scanner.scan();
        Set<Class<?>> annotatedClasses = scanner.getAnnotatedClasses();
        beansHandler = BeansHandler.getInstance(annotatedClasses);
    }


    public <T> T getBean(String beanName) {
        return getBeanByParameterType(beanName);}

    public <T> T getBean(Class<T> requiredType) {
        return getBeanByParameterType(requiredType);
    }


    public <T> T getBean(Class<T> requiredType, Object... constructParams) {
        return getBeanByParameterType(requiredType, constructParams);
    }

    public <T> T getBean(String beanName, Object... constructParams) {
        return getBeanByParameterType(beanName, constructParams);
    }

    private <T> T getBeanByParameterType(Object parameter, Object... argumentParameters) {
        ParameterType parameterType = ParameterTypeHandler.chooseType(parameter);
        T returnedBean = null;
        switch (parameterType) {
            case BEAN_NAME:
                returnedBean = getObjectByStringParameter(parameter, argumentParameters);
                break;
            case CLASS:
                returnedBean = getObjectByClassParameter(parameter, argumentParameters);
                break;
        }
        return returnedBean;
    }

    private <T> T getObjectByStringParameter(Object beanName, Object... constructParams) {
        T returnedBean = null;
        for (Map.Entry<String, Object> bean : beansHandler.getSingletonBeans().entrySet()) {
            if (bean.getKey().equals(beanName)) {
                if (constructParams == null || constructParams.length == 0) {
                    returnedBean = (T) bean.getValue();
                    break;
                } else {
                    try {
                        returnedBean = (T) createNotSingletonObject(bean.getValue().getClass(), constructParams);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        throw new BeanException(e.getMessage(), e);
                    }
                }
            }
        }
        if (returnedBean == null) {
            if (isExistBeanDefinitionWithBeanName(beanName)) {
                try {
                    Class classType = BeanDefinitionsHandler.getInstance().getClassTypeByBeanDefinitionName((String) beanName);
                    returnedBean = (T) createNotSingletonObject(classType, constructParams);
                } catch (Exception e) {
                    throw new BeanException(e.getMessage(), e);
                }
            } else {
                LOGGER.info("Bean with class type " + beanName + " was not found");
            }

        }
        return returnedBean;
    }

    private <T> T getObjectByClassParameter(Object classType, Object... constructParams) {
        T returnedBean = null;
        for (Map.Entry<String, Object> bean : beansHandler.getSingletonBeans().entrySet()) {
            if (bean.getValue().getClass().equals(classType)) {
                if (constructParams == null || constructParams.length == 0) {
                    returnedBean = (T) bean.getValue();
                    break;
                } else {
                    try {
                        returnedBean = (T) createNotSingletonObject(bean.getValue().getClass(), constructParams);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        throw new BeanException(e.getMessage(), e);
                    }
                    break;
                }
            }
        }
        if (returnedBean == null) {
            if (isExistBeanDefinitionWithClassType(classType)) {
                try {
                    returnedBean = (T) createNotSingletonObject((Class) classType, constructParams);
                } catch (Exception e) {
                    throw new BeanException(e.getMessage(), e);
                }
            } else {
                LOGGER.info("Bean with class type " + classType + " was not found");
            }

        }
        return returnedBean;
    }

    private boolean isExistBeanDefinitionWithBeanName(Object beanName) {
        BeanDefinitionsHandler beanDefinitionsHandler = BeanDefinitionsHandler.getInstance();
        return beanDefinitionsHandler.isExistBeanDefinitionWithBeanName(beanName);
    }

    private boolean isExistBeanDefinitionWithClassType(Object classType) {
        BeanDefinitionsHandler beanDefinitionsHandler = BeanDefinitionsHandler.getInstance();
        return beanDefinitionsHandler.isExistBeanDefinitionWithClassType(classType);
    }

    private Object createNotSingletonObject(Class clazz, Object... constructParams) throws Exception {
        Class[] types = new Class[constructParams.length];
        for (int i = 0; i < constructParams.length; i++) {
            types[i] = constructParams[i].getClass();
        }
        ClassLoader classLoader = clazz.getClassLoader();
        return classLoader.loadClass(clazz.getName()).getDeclaredConstructor(types).newInstance(constructParams);
    }


}
