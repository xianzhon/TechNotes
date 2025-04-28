import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

public class DemoBreakInnerClassSingleton {
    public static void breakSingletonWithReflection() throws Exception {
        InnerClassSingleton instance1 = InnerClassSingleton.getInstance();

        // 通过反射获取构造方法
        Constructor<InnerClassSingleton> constructor = InnerClassSingleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);  // 突破私有限制

        // 创建第二个实例
        InnerClassSingleton instance2 = constructor.newInstance();

        System.out.println(instance1 == instance2);  // 输出false，单例被破坏
    }

    static void breakLazyInitWhenFixReflectionBug() throws Exception{
        // 通过反射获取构造方法
        Constructor<InnerClassSingleton> constructor = InnerClassSingleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);  // 突破私有限制

        // 创建第二个实例
        // Note: since the constructor accesses the static instance, so it will trigger the initializaion of inner singleton instance
        InnerClassSingleton instance2 = constructor.newInstance();
        System.out.println(instance2);
    }

    static void breakWithDeserilization() throws IOException, ClassNotFoundException {
        InnerClassSingleton instance = InnerClassSingleton.getInstance();

        // 序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("singleton.ser"));
        oos.writeObject(instance);

        // 反序列化
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("singleton.ser"));
        InnerClassSingleton newInstance = (InnerClassSingleton) ois.readObject();

        System.out.println(instance == newInstance);  // 输出false
    }

    public static void main(String[] args) throws Exception {
        // breakSingletonWithReflection();
        // breakLazyInitWhenFixReflectionBug();
        breakWithDeserilization();
    }
}

/*
Conclusion:
    1. By default, the static holder singleton pattern has reflection security issue, meaning we can create a second instance by reflection.
    2. If we add prevention logic in the constructor, it will lose the lazy initialization feature, because we are refering to the static inner class
        and that would trigger the singleton object's initialization (if we didn't call getInstance() before).
    3. By default, the static holder singleton pattern also has deserialization security issue, meaning we can create a second instance by deserilization.
    4. We can fix the deserilization security issue by adding some prevention logic.
*/

