# SQL练习

以 Postgres (MySQL) 的语法练习为主。不过面试中 MySQL 问得比较多。

- 技巧1：写 SQL 的时候，按照 sql 语句实际的执行顺序来思考，会比较简单。先是 from 哪个表，然后是 where 条件，再是group by，having，然后是 select 什么字段，最后是 order by 哪些列。
- 技巧 2：可以考虑用 CTE (common table expression) 来提到 sql 的可读性，就当做是临时的视图吧（或者临时表）
- 技巧 3：窗口函数非常好用，比 group by 更加强大的工具
## SQL 语句的执行顺序
### 简单的 sql 语句 （单个 Select）

举例：`select col1, col2 from table where col1 > 5 group by col1 having count(*) > 1 order by col2 desc limit 10;`
或：
```sql
SELECT col1, col2
FROM table
WHERE col1 > 5
GROUP BY col1
HAVING COUNT(*) > 1
ORDER BY col2 DESC
LIMIT 10;
```

执行顺序是：
1. from table 确定数据源
2. where 过滤数据
3. group by 分组  (奇怪，这个顺序似乎不对？mysql 中，可以利用 select 列里面定义的别名，列已经使用过函数)
4. having 筛选分组
5. select 选择返回的列
6. order by 排序结果
7. limit 限制返回行数

那如果select 中有窗口函数，那是什么样呢？
答案： `FROM → WHERE → GROUP BY → HAVING → 窗口函数 → SELECT → DISTINCT → ORDER BY → LIMIT`

- 所以 order by 可以看到 select 中定义列名，也可以看到窗口函数中定义的别名

## 窗口函数

笔记：[SQL 窗口函数](SQL%20窗口函数.md)

通过上面分析出的执行顺序，窗口函数可以利用where，group by，having 处理之后的数据，不能看到 select 中定义的其他列名。

```sql
SELECT col1, RANK() OVER (ORDER BY col2) AS rnk
FROM table
ORDER BY rnk  -- 这里可以使用窗口函数别名
```

1. **窗口函数计算时机**：
    - 窗口函数是在 WHERE、GROUP BY 和 HAVING 之后计算的
    - 但在 SELECT 子句的其他表达式之前计算

2. **与普通聚合函数的区别**：
    - 普通聚合函数(如 SUM, COUNT)在 GROUP BY 阶段计算
    - 窗口聚合函数(如 SUM() OVER)在窗口函数阶段计算

3. **不能在 WHERE/HAVING 中使用窗口函数**：
    - 窗口函数结果不能用于 WHERE 或 HAVING 子句
    - 如果需要过滤窗口函数结果，必须使用子查询或 CTE

4. **性能考虑**：
    - 窗口函数通常会在内存中创建临时数据结构
    - 复杂的窗口函数可能影响查询性能
### 练习题目

- [LC176. Second Highest Salary](LC176.%20Second%20Highest%20Salary.md)  多种解法：子查询；`dense_rank` 窗口函数；自连接（跟一个值，非等值连接）
- [LC177. Nth Highest Salary](LC177.%20Nth%20Highest%20Salary.md)  `dense_rank` 窗口函数
- [LC185 Department Top Three Salaries](LC185%20Department%20Top%20Three%20Salaries.md) - dense_rank
- [LC184. Department Highest Salary](LC184.%20Department%20Highest%20Salary.md) - dense_rank
- [LC601. 体育馆的人流量](LC601.%20体育馆的人流量.md) - row_number() 且没有 partition 的窗口函数
- [LC1164. Product Price at a Given Date](LC1164.%20Product%20Price%20at%20a%20Given%20Date.md) - rank()分组, ifnull


## SQL 标准内置函数

- [SQL 内置函数列表](SQL%20内置函数列表.md)
- 举例： `ifnull(col, 0)`  equals to java `Map.getOrDefault(col, 0)`
- 日期间隔：datediff(date_big, date_small) = diff.  `SELECT DATEDIFF('2023-05-20', '2023-05-15');  -- 返回 5`
### 练习题目

- [LC584. Find Customer Referee](LC584.%20Find%20Customer%20Referee.md)  - ifnull
- [LC550. Game Play Analysis IV](LC550.%20Game%20Play%20Analysis%20IV.md) - left join，子查询


## 复杂的语法 (容易忘记)

### 1 case when
```sql
case when exists (
            select 1 from activity a
            where a.player_id = t.player_id
            and datediff(a.event_date, t.login) = 1
        )
then 1 else 0 end
```


### 2 if 函数
（常见的是放到 select 语句中）
```sql
COUNT(IF(state = 'approved', 1, NULL)) AS approved_count
SUM(IF(state = 'approved', amount, 0)) AS approved_total_amount
```


### 3 common table expression (CTE)
- 详细用法介绍： [CTE - common table expression 命名临时表](CTE%20-%20common%20table%20expression%20命名临时表.md)
- 例如，笔记：[LC1158. Market Analysis I](LC1158.%20Market%20Analysis%20I.md)
```sql
with t as (
    select buyer_id, count(*) as orders_in_2019
    from orders
    where year(order_date) = 2019
    group by buyer_id
)
select user_id as buyer_id, join_date, ifnull(orders_in_2019, 0) as orders_in_2019
from users u
left join t on u.user_id = t.buyer_id;
```