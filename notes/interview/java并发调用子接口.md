## 并发调用子接口

题目：请实现一个接口，它会调用下游的三个子接口，A、B、C分别拿到三个String。他们可以并发执行，在外层接口里将三个 String组合成一个 List返回，怎么做？

### 方法 1：CompletableFuture

- 这个接口非常适合这个任务，它简化了异步方法的调用。同时，方便组织多个异步任务，共同结束之后，再做一些操作。
- `CompletableFuture.supplyAsync` 接收一个 Supplier 对象，返回一个 CompletableFuture 对象
- `CompletableFuture.allOf`  可以组合多个异步任务，get 方法会等所有异步任务完成后才会返回。

### 方法 2：利用线程池 + Callable & Future接口

- 线程池：ExecutorService 接口有一个提交Callable任务的 `submit` 方法，它返回 `Future` 对象，然后调用它的 get 方法（阻塞）就可以拿到线程返回的结果了

### 完整代码
```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DemoConcurrentAPI {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DemoConcurrentAPI solu = new DemoConcurrentAPI();
        long tick1 = System.currentTimeMillis();
        System.out.println(solu.myAPI());
        long tick2 = System.currentTimeMillis();
        System.out.println(solu.myAPI2());
        long tick3 = System.currentTimeMillis();
        System.out.println("API 1 call takes: " + (tick2 - tick1)); // 4009ms
        System.out.println("API 2 call takes: " + (tick3 - tick2)); // 4009ms
    }

    // Approach 1: Use CompletableFuture to organize parallel tasks
    public List<String> myAPI() {
        // callA, callB, callC
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(this::callA);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(this::callB);
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(this::callC);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);
        try {
            allFutures.get(); // wait for all futures completed
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        List<String> ans = new ArrayList<>();
        try {
            ans.add(future1.get());
            ans.add(future2.get());
            ans.add(future3.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }

    // Approach 2, use ThreadPool + Future
    public List<String> myAPI2() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<String>> futures = new ArrayList<>();
        futures.add(executor.submit(this::callA));
        futures.add(executor.submit(this::callB));
        futures.add(executor.submit(this::callC));
        List<String> ans = futures.stream().map(e -> {
            try {
                return e.get();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());

        executor.shutdown();
        return ans;
    }

    // // Approach 3, use parallel stream. I didn't make it correct.
    // public List<String> myAPI3() {
    //     List<String> collect = Stream.of(this::callA, this::callB, this::callC)
    //             .parallel().map(e -> e.call())
    //             .collect(Collectors.toList());
    //     return null;
    // }

    public String callA() {
        sleep(3000);
        return "Hello A";
    }

    public String callB() {
        sleep(2000);
        return "Hello B";
    }

    public String callC() {
        sleep(4000);
        return "Hello C";
    }

    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```


