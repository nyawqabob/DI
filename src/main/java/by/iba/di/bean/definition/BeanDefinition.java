package by.iba.di.bean.definition;

import by.iba.di.annotations.scopes.ScopeType;

public class BeanDefinition {

    private Class classType;

    private ScopeType scope;

    private String beanName;

    public Class getClassType() {
        return classType;
    }

    public ScopeType getScope() {
        return scope;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public void setScope(ScopeType scope) {
        this.scope = scope;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }


}
