题目：现在有一个二维的 `List<List<Integer>> nums`，如何求最大值 (用 java 8 的 Stream API)？

```java
        List<List<Integer>> nums = Arrays.asList(Arrays.asList(1, 5, 7, 2), Arrays.asList(3, 5, 10, 2, 4));
        // Approach 1
        int maxNum = nums.stream()
                .flatMap(e -> e.stream())
                .max(Integer::compareTo)  // returns Optional<Integer>
                .get();
        System.out.println(maxNum);

        // Approach 2
        int maxNum2 = nums.stream()
                .flatMapToInt(e -> e.stream().mapToInt(Integer::intValue))
                .max()   // return OptionalInt
                .getAsInt();
        System.out.println(maxNum2);

```
