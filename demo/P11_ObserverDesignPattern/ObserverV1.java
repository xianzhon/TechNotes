import java.util.*;
import java.util.function.*;

class User {
    private final String name;
    private Consumer<String> consumer;

    public User(String name, Consumer<String> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    public void receiveInfo(String info) {
        consumer.accept(info);
    }
}

class WeatherStation {
    private final List<User> users = new ArrayList<>();

    public void register(User user) {
        this.users.add(user);
    }

    public String getInfo() {
        if (new Random().nextBoolean()) {
            return "晴天";
        }
        return "雨天";
    }

    public void start() {
        while (true) {
            String info = getInfo();
            // how does user get this info? We can use observer pattern
            users.forEach(u -> u.receiveInfo(info));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class ObserverV1 {
    public static void main(String[] args) {
        User u1 = new User("Alice", (info) -> {
            if (info.equals("晴天")) {
                System.out.println("Alice 去打篮球");
            } else {
                System.out.println("Alice 在家打游戏");
            }
        });

        User u2 = new User("Ross", (info) -> {
            if (info.equals("晴天")) {
                System.out.println("Ross will go hiking");
            }
        });

        WeatherStation station = new WeatherStation();
        station.register(u1);
        station.register(u2);

        station.start();
    }
}
