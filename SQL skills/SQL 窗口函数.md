SQL窗口函数（也称为**OLAP函数**）是一种强大的工具，用于在数据集的子集上执行计算，而不需要将数据分组为单独的行。这些函数提供了一种在保持行级别详细信息的同时进行聚合和其他复杂计算的方法。以下是一些常用的SQL窗口函数：

关键点：
- Aggregate functions take a group of rows with a common set of values (identified by the `GROUP BY` clause) and turn them into a number. E.g. SUM, MIN, MAX, and AVG.
- Similarly to row-level functions, window functions process rows one by one and produce `one output value for each input row`.
- 我：窗口函数并不会改变结果的行数，它只是增加了一列，每一行多了一列。 然后我们可以在外层对结果列二次加工（对列加工，或者增加 where 筛选条件）得到我们最终想要的结果。【窗口函数不影响 row 的数量】


1. **聚合窗口函数**：
   - `SUM() OVER(...)`: 计算窗口内值的总和。
   - `AVG() OVER(...)`: 计算窗口内值的平均值。
   - `MIN() OVER(...)`: 确定窗口内的最小值。
   - `MAX() OVER(...)`: 确定窗口内的最大值。
   - `COUNT() OVER(...)`: 计算窗口内的行数。
   举例：
   ![image-20250519180104637](https://i.hish.top:8/2025/05/19/180104.png)
对应的 SQL:
```sql
SELECT
SUM(clicks) OVER (PARTITION BY campaign) AS campaign_clicks
FROM campaigns

-- 对应的 self-join 的方式得到同样结果（但是丑陋，性能更低，不要这么做）
SELECT a.clicks
FROM campaigns c
INNER JOIN (
  SELECT campaign, SUM(clicks) AS clicks FROM campaigns GROUP BY campaign
) a
ON c.campaign = a.campaign
```

1. **排名窗口函数**：
   - `ROW_NUMBER() OVER(...)`: 为窗口内的每一行分配一个唯一的序号。
   - `RANK() OVER(...)`: 分配一个排名，相同值会获得相同的排名，但会留下间隙。
   - `DENSE_RANK() OVER(...)`: 类似于`RANK()`，但不留下间隙。
   - `NTILE(n) OVER(...)`: 将窗口内的行分成`n`个大致相等的组，并为每组分配一个编号。

3. **分析窗口函数**：
   - `LEAD(column, n, default) OVER(...)`: 返回当前行之后第`n`行的值。
   - `LAG(column, n, default) OVER(...)`: 返回当前行之前第`n`行的值。
   - `FIRST_VALUE(column) OVER(...)`: 返回窗口内第一行的值。
   - `LAST_VALUE(column) OVER(...)`: 返回窗口内最后一行的值。
   - `NTH_VALUE(column, n) OVER(...)`: 返回窗口内第`n`行的值。

4. **累积窗口函数**：
   - `CUME_DIST() OVER(...)`: 计算当前行在其窗口内的累积分布。
   - `PERCENT_RANK() OVER(...)`: 计算当前行的百分比排名。

在使用窗口函数时，通常需要定义`over()`子句，它可以包含`partition by`（用于将数据分成不同的部分或窗口）和`order by`（用于在每个窗口内对行进行排序）。

窗口函数的具体可用性和语法可能会根据不同的数据库系统（如 PostgreSQL, MySQL, SQL Server, Oracle 等）有所不同。

---
**问题：MySQL 和 postgres 对窗口函数的支持的情况**

截至 2023年4月，MySQL和PostgreSQL都支持窗口函数，但它们在某些方面的支持程度和性能优化可能有所不同。以下是对两者对窗口函数支持的概述：

### MySQL

- **支持情况**：MySQL从8.0版本开始支持窗口函数。
- **支持的窗口函数**：MySQL支持标准的窗口函数，如`ROW_NUMBER()`, `RANK()`, `DENSE_RANK()`, `NTILE()`, `LEAD()`, `LAG()`, `FIRST_VALUE()`, `LAST_VALUE()`, 以及聚合窗口函数如`SUM()`, `AVG()`, `MIN()`, `MAX()`, `COUNT()`等。
- **特性**：MySQL的窗口函数支持`PARTITION BY`和`ORDER BY`子句，以及窗口的范围和帧规范。
- **性能**：MySQL在窗口函数的性能方面进行了一些优化，但在处理大数据集时可能不如某些其他数据库系统高效。

### PostgreSQL

- **支持情况**：PostgreSQL对窗口函数的支持比MySQL更早，从8.4版本开始引入。
- **支持的窗口函数**：PostgreSQL提供了广泛的窗口函数支持，包括但不限于`ROW_NUMBER()`, `RANK()`, `DENSE_RANK()`, `NTILE()`, `LEAD()`, `LAG()`, `FIRST_VALUE()`, `LAST_VALUE()`，以及各种聚合窗口函数。此外，PostgreSQL还支持一些更高级的窗口功能，如`CUME_DIST()`, `PERCENT_RANK()`等。
- **特性**：PostgreSQL的窗口函数支持复杂的`PARTITION BY`、`ORDER BY`子句，以及窗口的范围和帧规范。它还允许在窗口函数中使用自定义的聚合函数。
- **性能**：PostgreSQL在窗口函数的实现上非常成熟，通常在处理大型数据集和复杂查询时表现出色。

### 总结

虽然MySQL和PostgreSQL都支持窗口函数，但PostgreSQL在这方面提供了更广泛的功能和可能更优化的性能。PostgreSQL通常被认为在复杂查询处理方面更为强大，特别是在涉及窗口函数的场景中。然而，具体选择哪个数据库系统还应考虑其他因素，如应用的特定需求、数据库的其他功能、生态系统和社区支持等。

---
非常不错的资料：【学完这两个就可以了】

- [SQL for the 21st Century: Analytic / Window Functions | GoodData --- 21 世纪的 SQL：分析/窗口函数 | GoodData](https://www.gooddata.com/blog/sql-21st-century-analytic-window-functions/)
- [What Are SQL Window Functions? | LearnSQL.com --- 什么是 SQL 窗口函数？ | LearnSQL.com](https://learnsql.com/blog/window-functions/)
## 练习

[query with window function row_number](query%20with%20window%20function%20row_number.md)