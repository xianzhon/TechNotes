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

    // 问题3: 如何更好扩展让订阅者可以订阅多个事件?
    public void receiveNews(String news) {
        // ....
    }

}

// 职责: 负责管理订阅者和发布消息
class TvStation {
    private final List<User> users = new ArrayList<>();

    public void register(User user) {
        this.users.add(user);
    }

    public void onInfoUpdate(String info) {
        // broadcast the info to each observer
        users.forEach(u -> u.receiveInfo(info));
    }

}

// 职责: 负责产生消息
class WeatherStation {
    private final TvStation tvStation; // 负责管理订阅者，以及广播消息

    public WeatherStation(TvStation tvStation) {
        this.tvStation = tvStation;
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
            tvStation.onInfoUpdate(info);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class ObserverV2 {
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

        TvStation tvStation = new TvStation();
        WeatherStation station = new WeatherStation(tvStation);

        tvStation.register(u1);
        tvStation.register(u2);

        station.start();
    }
}
