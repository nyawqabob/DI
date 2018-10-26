package by.iba.di.entity;


import by.iba.di.annotations.Autowired;

public class Wheel {

    @Autowired
    PartOfWheel partOfWheel;

    public void sound() {
        System.out.println("Sound");
    }
}
