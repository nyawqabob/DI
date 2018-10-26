package by.iba.di.entity;

import by.iba.di.annotations.Proxy;
import by.iba.di.annotations.Service;

@Service
@Proxy
public class Test {


    public String b;
    public String a;

    public String getA() {
        return a;
    }

    public Test(){


    }
    public Test(String a, String b){
        this.a = a;
        this.b=b;
    }
}
