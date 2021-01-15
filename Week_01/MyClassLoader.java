import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoader extends  ClassLoader{
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classInfo=null;
        try {
            classInfo = loadClassInfo(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, classInfo, 0, classInfo.length);
    }

    private byte[] loadClassInfo(String str) throws IOException {
        byte[] xlassBytes = getContext(str.replace(".", File.pathSeparator) + ".xlass");
        return xlassBytes;
    }

    private byte[] getContext(String path) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nextValue;
        while ((nextValue = inputStream.read()) != -1) {
            baos.write(nextValue);
        }
        return decode(baos.toByteArray());
    }

    private byte[] decode(byte[] xlass) {
        for (int i = 0; i < xlass.length; i++) {
            xlass[i] = (byte) (255 - xlass[i]);
        }
        return xlass;
    }

    public static void main(String[] args) {
        try {
            Class<?> myClass = new MyClassLoader().findClass("Hello");
            Method declaredMethod = myClass.getDeclaredMethod("hello");
            declaredMethod.invoke(myClass.newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

