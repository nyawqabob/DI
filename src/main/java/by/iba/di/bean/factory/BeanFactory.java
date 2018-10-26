package by.iba.di.bean.factory;

public interface BeanFactory {

    <T> T getBean(String beanName);

    <T> T getBean(Class<T> requiredType);

    <T> T getBean(Class<T> requiredType, Object... constructParams);

    <T> T getBean(String beanName, Object... constructParams);

}
