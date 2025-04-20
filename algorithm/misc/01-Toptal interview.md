## 1. Problem: Count Missing XML Tags

Check online: https://www.youtube.com/watch?v=zBl3YPAe23o

![image-20250412104655967](https://i.hish.top:8/2025/04/12/104656.png)

### 1.1. Approach1

```java
public class CountMissingTag {
    int countMissingTags(String log) { // time: O(n), space: O(1
        int openCount = 0, missingCount = 0;
        final String OPEN = "<app>";
        for (int i = 0; i < log.length(); ) {
            // check next chunk
            String token = log.substring(i, i + OPEN.length());
            if (OPEN.equals(token)) {
                openCount++;
                i += OPEN.length();
            } else {
                if (openCount > 0) {
                    openCount--;
                } else {
                    missingCount++;
                }
                i += OPEN.length() + 1;
            }
        }
        return missingCount + openCount;
    }

    public static void main(String[] args) {
        CountMissingTag solu = new CountMissingTag();
        p(solu.countMissingTags("<app></app>") == 0);
        p(solu.countMissingTags("<app><app>") == 2);
        p(solu.countMissingTags("<app></app></app></app>") == 2);
        p(solu.countMissingTags("</app></app></app>") == 3);
        p(solu.countMissingTags("<app><app><app></app><app>") == 3);
    }

    static void p(Object o) {
        System.out.println(o);
    }
}
```


### 1.2. Approach2 - more clean

Convert the open-close tag to parantheses.

```java
public class CountMissingTag {

    int countMissingTags(String log) { // time: O(n), space: O(n)
        String simplifiedLog = log.replaceAll("<app>", "(").replaceAll("</app>", ")");
        int openCount = 0, missingCount = 0;
        for (int i = 0; i < simplifiedLog.length(); i++) {
            char ch = simplifiedLog.charAt(i);
            if (ch == '(') {
                openCount++;
            } else {
                if (openCount > 0) { // matched
                    openCount--;
                } else {
                    missingCount++;
                }
            }
        }
        return missingCount + openCount;
    }

    public static void main(String[] args) {
        CountMissingTag solu = new CountMissingTag();
        p(solu.countMissingTags("<app></app>") == 0);
        p(solu.countMissingTags("<app><app>") == 2);
        p(solu.countMissingTags("<app></app></app></app>") == 2);
        p(solu.countMissingTags("</app></app></app>") == 3);
        p(solu.countMissingTags("<app><app><app></app><app>") == 3);
    }

    static void p(Object o) {
        System.out.println(o);
    }
}
```

