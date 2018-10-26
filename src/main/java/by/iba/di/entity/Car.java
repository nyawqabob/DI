package by.iba.di.entity;


import by.iba.di.annotations.Autowired;
import by.iba.di.annotations.Proxy;
import by.iba.di.annotations.Service;

@Service
@Proxy
public class Car {

    public Wheel getWheel() {
        return wheel;
    }

    public void setWheel(Wheel wheel) {
        this.wheel = wheel;
    }

    @Autowired
    public Wheel wheel;

    public void beep() {
        System.out.println("Beep");
    }
}
