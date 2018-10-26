package by.iba.di.entity;

import by.iba.di.annotations.Scope;
import by.iba.di.annotations.Service;
import by.iba.di.annotations.scopes.ScopeType;

@Service
@Scope(ScopeType.PROTOTYPE)
public class Prototype {

    public String a;
    public String b;

    public Prototype() {

    }

    public Prototype(String a, String b) {
        this.a = a;
        this.b = b;
    }
}
