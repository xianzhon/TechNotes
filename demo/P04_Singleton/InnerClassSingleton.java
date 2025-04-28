import java.io.Serializable;

public class InnerClassSingleton implements Serializable {
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
    }

    public static InnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // NOTE: this is the code logic to prevent creating 2nd instance by deserilization
    private Object readResolve() {
        return SingletonHolder.INSTANCE;
    }
}
