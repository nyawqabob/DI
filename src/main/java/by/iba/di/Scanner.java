package by.iba.di;


import by.iba.di.annotations.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.Set;

public class Scanner {

    private static final Logger LOGGER = LogManager.getLogger(Scanner.class);
    private Set<Class<?>> annotatedClasses;

    public void scan() {
        String packAge = Scanner.class.getPackage().getName();
        LOGGER.info("Package of scanning is: " + packAge);
        Reflections reflections = new Reflections(packAge);
        annotatedClasses = reflections.getTypesAnnotatedWith(Service.class);
    }

    public Set<Class<?>> getAnnotatedClasses() {
        return annotatedClasses;
    }

}
