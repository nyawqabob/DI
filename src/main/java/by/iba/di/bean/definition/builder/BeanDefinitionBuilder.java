package by.iba.di.bean.definition.builder;

import by.iba.di.annotations.scopes.ScopeType;
import by.iba.di.bean.definition.BeanDefinition;

public interface BeanDefinitionBuilder {

    BeanDefinition build();

    BeanDefinitionBuilder setClassType(Class classType);

    BeanDefinitionBuilder setScope(ScopeType scope);

    BeanDefinitionBuilder setBeanName(String beanName);

}
