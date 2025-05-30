# Factory Design Pattern

总共分三种，复杂度依次增加：

- 简单工厂设计模式 (simple factory)
- 工厂方法设计模式 (factory method)
- 抽象工厂设计模式 (abstract factory)



工厂设计模式都是解决对象创建的使用场景的，例如，运行时在当前上下文无法直接 new 一个对象（通过反射当然可以，但是代码很丑陋，性能低下）。通过将对象的创建封装到一个工具类中，或者在子类中创建，那么就有了这 3 种工厂设计模式。



## Simple Factory

这个模式最简单，枚举/string + 工具类 + if-else/case。



简化版：

```java
class ProductFactory {
    public static Product createProduct(String type) {
        switch (type) {
            case "A": return new ProductA();
            case "B": return new ProductB();
            default: throw new IllegalArgumentException();
        }
    }
}
```

复杂版本：

```java
interface Product {
    void display();
}

class ConcreteProductA implements Product {
    public void display() {
        System.out.println("Product A");
    }
}

class ConcreteProductB implements Product {
    public void display() {
        System.out.println("Product B");
    }
}

class ProductFactory {
    public static Product createProduct(String type) {
        switch (type) {
            case "A": return new ConcreteProductA();
            case "B": return new ConcreteProductB();
            default: throw new IllegalArgumentException("Unknown product type");
        }
    }
}

// Client Code
public class Client {
    static void someBusinessFunction() {
        Product product = ProductFactory.createProduct("A");
        product.display(); // Output: "Product A"
    }

    public static void main(String[] args) {
        someBusinessFunction();
    }
}
```

- 优点：
	- 将对象的创建和使用解耦了，client 不需要知道具体的子类型；【实现简单，直接封装创建逻辑】【客户端无需知道具体的类型】
	- 集中化创建对象，方便维护；
	- 方便测试。

- 缺点：
	- 违反开闭原则（新增产品需要修改工厂类）
	- 工厂类的职责过重，因为他集中了所有产品的创建逻辑

- 使用场景：产品类型较少且不频繁扩展（如配置文件解析器、日志记录器）
	- 例如：支付方式，支付宝、微信



## Factory Method

将对象的创建下放到子类工厂中，在 client 代码中调用时，只需要使用父类工厂。



简化版：

```java
interface Factory {
    Product createProduct();
}
class FactoryA implements Factory {
    public Product createProduct() { return new ProductA(); }
}
```

复杂版：

```java
// Product Interface
interface Document {
    void open();
    void save();
}

// Concrete Products
class PdfDocument implements Document {
    public void open() { System.out.println("Opening PDF..."); }
    public void save() { System.out.println("Saving PDF..."); }
}

class WordDocument implements Document {
    public void open() { System.out.println("Opening Word..."); }
    public void save() { System.out.println("Saving Word..."); }
}

// Creator (Abstract Class)
abstract class Application {
    public abstract Document createDocument(); // Factory Method

    public void newDocument() {
        Document doc = createDocument();
        doc.open();
        doc.save();
    }
}

// Concrete Creators
class PdfApplication extends Application {
    public Document createDocument() {
        return new PdfDocument();
    }
}

class WordApplication extends Application {
    public Document createDocument() {
        return new WordDocument();
    }
}

// Client Code
public class Client {
    static void someFunction(Application app) {
        app.newDocument(); // Creates & uses a PDF
    }

    public static void main(String[] args) {
        Application app = new PdfApplication();
        someFunction(app);
    }
}
```

- 优点：
	- 符合开闭原则，新增产品，只用新加一个子类
	- 解耦客户端和具体的产品，客户端无需知道具体的类型
	- 支持多态性

- 缺点：
	- 每新增一个产品，就需要一个工厂子类（类数量膨胀）

- 使用场景：需要灵活扩展产品类型（如文档编辑器、游戏角色生成）

### 关联：java 8 中的 Supplier 接口

这个函数式接口，更像是一个简化版的工厂方法模式，实际的工厂则是一个匿名类（lambda 函数），这样能避免创建很多的 class。

```java
// java.util.function;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```

例如：

```java
void someBusinessLogic(Supplier<Product> factory) {
    Product = factory.get();
    // use product
}

// client code
someBusinessLogic(() -> {
    return new ProductA();
});
someBusinessLogic(() -> {
    return new ProductB();
});
```



## Abstract Factory

**核心思想**：创建多个相关产品的家族（如“现代风”家具 vs “复古风”家具）。



简化版：

```java
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}
class MacFactory implements GUIFactory { ... }
class WinFactory implements GUIFactory { ... }
```



复杂版：

```java
// Abstract Products
interface Button {
    void render();
}

interface Checkbox {
    void render();
}

// Concrete Products (Mac)
class MacButton implements Button {
    public void render() { System.out.println("Rendering Mac Button"); }
}

class MacCheckbox implements Checkbox {
    public void render() { System.out.println("Rendering Mac Checkbox"); }
}

// Concrete Products (Windows)
class WindowsButton implements Button {
    public void render() { System.out.println("Rendering Windows Button"); }
}

class WindowsCheckbox implements Checkbox {
    public void render() { System.out.println("Rendering Windows Checkbox"); }
}

// Abstract Factory
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

// Concrete Factories
class MacFactory implements GUIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}

class WindowsFactory implements GUIFactory {
    public Button createButton() { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}

// Client Code
class Application {
    private Button button;
    private Checkbox checkbox;

    public Application(GUIFactory factory) {
        button = factory.createButton();
        checkbox = factory.createCheckbox();
    }

    public void render() {
        button.render();
        checkbox.render();
    }
}

public class Client {
    public static void main(String[] args) {
        GUIFactory factory = new MacFactory(); // Or WindowsFactory
        Application app = new Application(factory);
        app.render();
    }
}
```



- 优点：
	- 能创建整个产品族，且方便切换产品族
	- 符合 open-close 原则

- 缺点：代码复杂度高

- 使用场景：创建一系列相关的产品，支持统一切换创建的对象类型。【需要创建一组相关或依赖的对象（如跨平台UI组件、数据库访问层）】



## **现实案例**

- **简单工厂**：JDK中的 `Calendar.getInstance(Timezone zone, Locale locale)`。具体的产品类：BuddhistCalendar、JapaneseImperialCalendar、GregorianCalendar
- **工厂方法**：Spring的 `BeanFactory`、Java集合框架的 `Iterator`。
- **抽象工厂**：Java Swing的 `LookAndFeel`（切换UI主题）。子工厂（BasicLookAndFeel）



## 总结

- **简单工厂**和**工厂方法**用得比较多，代码简单，思路也很简单。他们都是属于创建一种产品，工厂方法符合开闭原则，更加优雅一点，所以重点掌握。而且 java 8 的 `Supplier` 函数接口 + lambda 表达式，则省去了创建工厂类的繁琐代码。

- 相比之下，抽象工厂要复杂一点，只适用于需要创建多个相关的产品，且支持统一切换。



## 参考



- Deepseek
- [iluwatar/java-design-patterns: Design patterns implemented in Java](https://github.com/iluwatar/java-design-patterns)
- [Abstract Factory Pattern in Java: Mastering Object Creation Elegantly | Java Design Patterns](https://java-design-patterns.com/patterns/abstract-factory/)
