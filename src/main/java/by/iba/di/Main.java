package by.iba.di;

import by.iba.di.bean.factory.BeanFactory;
import by.iba.di.bean.factory.v1_0.BeanFactoryImpl;
import by.iba.di.entity.*;
//import by.iba.di.runner.DIRunner;


public class Main {

    private ClassLoader cl;

    public static void main(String[] args) {

        BeanFactory beanFactory = new BeanFactoryImpl();
        Object object = beanFactory.getBean(Main.class);
        Object secondObject = beanFactory.getBean(Car.class);
        Object thirdObject = beanFactory.getBean("by.iba.di.entity.Car");
        Wheel fourthObject = beanFactory.getBean(Wheel.class);
        Wheel anWheel = beanFactory.getBean(Wheel.class);
        Test fifthObject = beanFactory.getBean("by.iba.di.entity.Test", "A", "B");
        System.out.println(fifthObject.getA());
        Class classzz = fifthObject.getClass();
        Abc sixthObject = beanFactory.getBean(Abc.class, 10,"50",'1');
        ClassLoader cl = sixthObject.getClass().getClassLoader().getParent();
        Class clazz = fifthObject.getClass();
        Object objecfft = beanFactory.getBean("AFSf");
        Prototype pr = beanFactory.getBean(Prototype.class);
        Prototype secPr = beanFactory.getBean(Prototype.class);
        Prototype thirdPr = beanFactory.getBean(Prototype.class, "A", "B");
        fourthObject.sound();
        Class car = Car.class;


    }
}
