package by.iba.di.bean.exception;

public class BeanException extends RuntimeException {

    public BeanException(String message, Throwable ex){
        super(message, ex);
    }
    public BeanException(String message){
        super(message);
    }

}
