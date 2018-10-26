package by.iba.di.entity;

import by.iba.di.annotations.Service;

@Service
public class Abc {

    public Integer a;
    public String b;
    public Character c;

    public Abc() {
    }

    public Abc(Integer a, String b, Character c){
        this.a =a;
        this.b= b;
        this.c=c;


    }
}
