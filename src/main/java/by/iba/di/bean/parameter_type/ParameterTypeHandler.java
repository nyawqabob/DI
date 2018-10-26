package by.iba.di.bean.parameter_type;

import by.iba.di.bean.exception.BeanException;

public class ParameterTypeHandler {

    private static ParameterType parameterType;

    public static ParameterType chooseType(Object object) {
        if (object.getClass().equals(String.class)) {
            parameterType = ParameterType.BEAN_NAME;
        }
        if (object.getClass().equals(Class.class)) {
            parameterType = ParameterType.CLASS;
        }
        if (parameterType == null) {
            throw new BeanException("Wrong type of class: " + object.getClass());
        }
        return parameterType;
    }
}
