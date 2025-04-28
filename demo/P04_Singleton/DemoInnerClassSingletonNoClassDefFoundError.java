import java.io.Serializable;
import javax.script.Invocable;

// NOTE: remove public decorator to test inside this file
class InnerClassSingleton implements Serializable {
    private InnerClassSingleton() {
        // NOTE: this is the code logic to prevent creating 2nd instance by reflection
        System.out.println("Adding below check will lose the lazy init feature?");
        if (SingletonHolder.INSTANCE != null) {
            throw new RuntimeException("不允许通过反射创建实例");
        }
    }

    // 静态内部类
    private static class SingletonHolder {
        private static final InnerClassSingleton INSTANCE = new InnerClassSingleton();

        static {
            boolean someCondition = true;
            if (someCondition) {
                throw new RuntimeException("Manually throw runtime exception");
            }
        }
    }

    public static InnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // NOTE: this is the code logic to prevent creating 2nd instance by deserilization
    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }

    public void foo() {
        System.out.println("Hello foo!");
    }

    public static void main(String[] args) {
    }
}

public class DemoInnerClassSingletonNoClassDefFoundError {
    public static void main(String[] args) {
        InnerClassSingleton instance = null;
        try {
            instance = InnerClassSingleton.getInstance();
        } catch (Throwable e) {
            // swallow the exception to see what will happen next
        }
        InnerClassSingleton.getInstance().foo(); // NoClassDefFoundError
    }
}
/* Output:

Adding below check will lose the lazy init feature?
Exception in thread "main" java.lang.NoClassDefFoundError: Could not initialize class InnerClassSingleton$SingletonHolder
        at InnerClassSingleton.getInstance(DemoInnerClassSingletonNoClassDefFoundError.java:27)
        at DemoInnerClassSingletonNoClassDefFoundError.main(DemoInnerClassSingletonNoClassDefFoundError.java:51)
Caused by: java.lang.ExceptionInInitializerError: Exception java.lang.RuntimeException: Manually throw runtime exception [in thread "main"]
        at InnerClassSingleton$SingletonHolder.<clinit>(DemoInnerClassSingletonNoClassDefFoundError.java:21)
        at InnerClassSingleton.getInstance(DemoInnerClassSingletonNoClassDefFoundError.java:27)
        at DemoInnerClassSingletonNoClassDefFoundError.main(DemoInnerClassSingletonNoClassDefFoundError.java:47)
*/
