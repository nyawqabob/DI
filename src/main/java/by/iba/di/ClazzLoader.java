package by.iba.di;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ClazzLoader extends ClassLoader {

    private static final String PACKAGE_NAME = "by.iba.di";
    private Map<String, Class> classTypes = new HashMap<>();

    public ClazzLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public synchronized Class<?> loadClass(String className) throws ClassNotFoundException{
        Class returnedClass;
        if (classTypes.containsKey(className)) {
            returnedClass = classTypes.get(className);
        } else {
            if (className.startsWith(PACKAGE_NAME)) {
                String file = className.replace('.', File.separatorChar) + ".class";
                try {
                    byte bytes[] = getClassBytes(file);
                    Class<?> clazz = defineClass(className, bytes, 0, bytes.length);
                    resolveClass(clazz);
                    if (clazz.getPackage() == null) {
                        String packageName = className.substring(0, className.lastIndexOf('.'));
                        definePackage(packageName, null, null, null, null, null, null, null);
                    }
                    returnedClass = clazz;
                    classTypes.put(className, returnedClass);
                } catch (IOException e) {
                    returnedClass = null;
                }
            } else {
                returnedClass = super.loadClass(className);
            }
        }
        return returnedClass;
    }

    private byte[] getClassBytes(String className) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(className);
        byte bytes[] = new byte[stream.available()];
        DataInputStream inputStream = new DataInputStream(stream);
        inputStream.readFully(bytes);
        inputStream.close();
        return bytes;
    }


}
