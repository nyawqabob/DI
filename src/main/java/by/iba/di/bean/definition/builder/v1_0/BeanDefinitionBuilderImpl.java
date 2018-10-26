package by.iba.di.bean.definition.builder.v1_0;

import by.iba.di.annotations.scopes.ScopeType;
import by.iba.di.bean.definition.BeanDefinition;
import by.iba.di.bean.definition.builder.BeanDefinitionBuilder;

public class BeanDefinitionBuilderImpl implements BeanDefinitionBuilder {

    private BeanDefinition beanDefinition;

    public BeanDefinitionBuilderImpl()
    {
        beanDefinition = new BeanDefinition();
    }

    @Override
    public BeanDefinitionBuilder setClassType(Class classType) {
        beanDefinition.setClassType(classType);
        return this;
    }

    @Override
    public BeanDefinitionBuilder setScope(ScopeType scope) {
        beanDefinition.setScope(scope);
        return this;
    }

    @Override
    public BeanDefinitionBuilder setBeanName(String beanName) {
        beanDefinition.setBeanName(beanName);

        return this;
    }

    @Override
    public BeanDefinition build() {
        return beanDefinition;
    }
}

