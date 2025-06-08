import java.util.*;
import java.util.function.*;

// ----------------------------------------------
// 抽取 event 接口，支持发布和订阅多种类型的 event
// ----------------------------------------------
interface Event {
    // 获取事件的事件
    long getTimestamp();

    // 获取事件源
    Object getSource();
}

// 之所以要有这个抽象类，是因为 timestamp 是在事件创建的时候，就已经确定的. 后续各个子类不需要再定义一遍
abstract class BaseEvent implements Event {
    protected long timestamp;

    public BaseEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}

class WeatherUpdateEvent extends BaseEvent {
    private final String info;

    public WeatherUpdateEvent(String info) {
        this.info = info;
    }

    @Override
    public Object getSource() {
        return info;
    }

    public String getInfo() {
        return info;
    }
}

// 扩展1: 早间新闻消息
class MorningNewsEvent extends BaseEvent {
    private String news;

    public MorningNewsEvent(String news) {
        this.news = news;
    }

    @Override
    public Object getSource() {
        return news;
    }
}

// ----------------------------------------------
// 扩展订阅逻辑，支持多种订阅来源，除了 User， Organization 也可以订阅
// 1. 只要实现了 EventListener 接口的类，都可以监听 Event
// ----------------------------------------------

interface EventListener {
    // 接收event后的处理逻辑
    void onEvent(Event event);
}


class User implements EventListener {
    private final String name;
    private Consumer<String> consumer;

    public User(String name, Consumer<String> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WeatherUpdateEvent) {
            consumer.accept(((WeatherUpdateEvent)event).getInfo());
        } else if (event instanceof MorningNewsEvent) {
            System.out.println(name + " received " + event.getSource().toString());
        }
    }
}

// 职责: 负责管理订阅者和发布消息
class TvStation {
    private final List<EventListener> eventListener = new ArrayList<>();

    public void register(EventListener eventListener) {
        this.eventListener.add(eventListener);
    }

    public void publish(Event event) {
        eventListener.forEach(u -> u.onEvent(event));
    }
}

// 职责: 负责产生消息
class WeatherStation {
    private final TvStation tvStation;

    public WeatherStation(TvStation tvStation) {
        this.tvStation = tvStation;
    }

    // 模拟气象产生的观测数据
    public String getInfo() {
        if (new Random().nextBoolean()) {
            return "晴天";
        }
        return "雨天";
    }

    public void start() {
        while (true) {
            String info = getInfo();
            // 气象局只需要通过总线(tvStation)将消息发送出去，它不用关心是谁订阅了该消息. 发给谁，是由总线决定的
            tvStation.publish(new WeatherUpdateEvent(info));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

// 类似地，可以添加早间新闻的发布类, 然后使用 TvStation 实例去发布
class MorningNewsReporter {
    private final TvStation tvStation;

    public MorningNewsReporter(TvStation tvStation) {
        this.tvStation = tvStation;
    }

    public String getNews() {
        return "News at " + new Date().toString();
    }

    public void start() {
        // generate news at 7am every morning
        while (true) {
            String news = getNews();
            tvStation.publish(new MorningNewsEvent(news));

            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class ObserverV3 {
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

        tvStation.register(u1);
        tvStation.register(u2);

        // weather
        new Thread(() -> {
            WeatherStation station = new WeatherStation(tvStation);
            station.start();
        }).start();

        // news
        new Thread(() -> {
            MorningNewsReporter reporter = new MorningNewsReporter(tvStation);
            reporter.start();
        }).start();
    }
}
