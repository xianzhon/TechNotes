
## 问题
现在有一个log 表（id，name，optime，command）记录了谁(name) 在什么时间操作了什么 (command)。写一个 SQL，找出每个人最后一次操作的内容和时间。

## 答案

### 利用窗口函数 row_number()   ✅
按照 name 进行 partition，然后按 optime 降序排列。给分组中的每一行添加了一个行号。在外层，只选每组里面的第一行即可。
```sql
select name, optime, command 
from (select name, optime, command, row_number() over (partition by name order by optime desc) rn from log) t
where t.rn = 1;
```


### 利用窗口函数 dense_rank()  ❌
```sql
select name, optime, command 
from (select name, optime, command, dense_rank() over (partition by name order by optime desc) rk from log) t 
where t.rk = 1;
```
↑ 这个有 bug，按 optime 降序，当两行有相同的 name 和 optime 时，他们的 rk 是一样的。这样外层筛选到的结果会包含多条数据，这就不对了。

### 其他解法

#### join ❌

```sql
SELECT l.name, l.optime, l.command
FROM log l
JOIN (
    SELECT name, MAX(optime) AS max_optime
    FROM log
    GROUP BY name
) latest ON l.name = latest.name AND l.optime = latest.max_optime;  -- 子查询，这里可以跟上多个条件
```

缺点：对于 name 一样，如果它有两条相同的时间的操作记录，那么这个分组里面会输出两条

#### 自连接 ❌

```sql
SELECT l.name, l.optime, l.command
FROM log l
LEFT JOIN log l2 ON l.name = l2.name AND l.optime < l2.optime
WHERE l2.name IS NULL;
```

缺点：对于 name 一样，如果它有两条相同的时间的操作记录，那么这个分组里面会输出两条。

疑问：不过话说回来，如果 name 和 optime 分别都相同的两条记录，结果应该是返回一条呢，还是两条呢？
