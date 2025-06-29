
# MySQL / 标准 SQL 内置函数大全

MySQL 和标准 SQL 提供了丰富的内置函数，可以分为几大类：聚合函数、数学函数、字符串函数、日期时间函数、流程控制函数、系统信息函数等。以下是主要内置函数及其作用的分类列表：

## 一、聚合函数（Aggregate Functions）

| 函数 | 作用 |
|------|------|
| `COUNT()` | 返回行数 |
| `SUM()` | 返回总和 |
| `AVG()` | 返回平均值 |
| `MIN()` | 返回最小值 |
| `MAX()` | 返回最大值 |
| `GROUP_CONCAT()` | 将组内值连接成字符串 |
| `STD()` / `STDDEV()` | 返回标准差 |
| `VARIANCE()` | 返回方差 |

## 二、数学函数

| 函数                        | 作用   |
| ------------------------- | ---- |
| `ABS()`                   | 绝对值  |
| `CEIL()` / `CEILING()`    | 向上取整 |
| `FLOOR()`                 | 向下取整 |
| `ROUND()`                 | 四舍五入 |
| `TRUNCATE()`              | 截断数字 |
| `MOD()`                   | 取模   |
| `POW()` / `POWER()`       | 幂运算  |
| `SQRT()`                  | 平方根  |
| `EXP()`                   | e的幂  |
| `LOG()` / `LOG10()`       | 对数   |
| `RAND()`                  | 随机数  |
| `SIN()`, `COS()`, `TAN()` | 三角函数 |
| `PI()`                    | 返回π值 |

## 三、字符串函数

| 函数                               | 作用        |
| -------------------------------- | --------- |
| `CONCAT()`                       | 连接字符串     |
| `CONCAT_WS()`                    | 用分隔符连接字符串 |
| `SUBSTRING()` / `SUBSTR()`       | 提取子串      |
| `LEFT()`                         | 返回左侧字符    |
| `RIGHT()`                        | 返回右侧字符    |
| `LENGTH()` / `CHAR_LENGTH()`     | 字符串长度     |
| `TRIM()` / `LTRIM()` / `RTRIM()` | 去除空格      |
| `UPPER()` / `UCASE()`            | 转为大写      |
| `LOWER()` / `LCASE()`            | 转为小写      |
| `REPLACE()`                      | 替换字符串     |
| `REVERSE()`                      | 反转字符串     |
| `STRCMP()`                       | 比较字符串     |
| `LOCATE()` / `INSTR()`           | 查找子串位置    |
| `LPAD()` / `RPAD()`              | 填充字符串     |
| `SPACE()`                        | 返回空格字符串   |
| `FORMAT()`                       | 格式化数字     |

## 四、日期时间函数

| 函数                         | 作用        |
| -------------------------- | --------- |
| `NOW()` / `SYSDATE()`      | 当前日期时间    |
| `CURDATE()`                | 当前日期      |
| `CURTIME()`                | 当前时间      |
| `DATE()`                   | 提取日期部分    |
| `TIME()`                   | 提取时间部分    |
| `YEAR()`                   | 提取年份      |
| `MONTH()`                  | 提取月份      |
| `DAY()` / `DAYOFMONTH()`   | 提取日       |
| `HOUR()`                   | 提取小时      |
| `MINUTE()`                 | 提取分钟      |
| `SECOND()`                 | 提取秒       |
| `DAYOFWEEK()`              | 星期几(1=周日) |
| `DAYOFYEAR()`              | 一年中的第几天   |
| `WEEK()`                   | 一年中的第几周   |
| `DATE_FORMAT()`            | 格式化日期     |
| `DATE_ADD()` / `ADDDATE()` | 日期加法      |
| `DATE_SUB()` / `SUBDATE()` | 日期减法      |
| `DATEDIFF()`               | 日期差(天数)   |
| `TIMEDIFF()`               | 时间差       |
| `TIMESTAMP()`              | 转为时间戳     |
| `FROM_UNIXTIME()`          | 时间戳转日期    |
| `UNIX_TIMESTAMP()`         | 日期转时间戳    |
memo:
- datediff 使用：`SELECT DATEDIFF('2023-05-20', '2023-05-15');  -- 返回 5`
- 用过将 date 转换成月份的字符串: `date_format(date_col, '%y-%m')`

## 五、流程控制函数

| 函数 | 作用 |
|------|------|
| `IF()` | 简单条件判断 |
| `IFNULL()` / `NULLIF()` | NULL值处理 |
| `CASE WHEN THEN ELSE END` | 复杂条件判断 |
| `COALESCE()` | 返回第一个非NULL值 |
- 用过：`ifnull (col, 0)` 如果 col 是 null 的话，输出 0. ref: [LC1158. Market Analysis I](LC1158.%20Market%20Analysis%20I.md)
## 六、窗口函数(MySQL 8.0+)

| 函数 | 作用 |
|------|------|
| `ROW_NUMBER()` | 行号 |
| `RANK()` | 排名(有间隔) |
| `DENSE_RANK()` | 排名(无间隔) |
| `NTILE()` | 分组排名 |
| `LEAD()` | 访问后续行 |
| `LAG()` | 访问前序行 |
| `FIRST_VALUE()` | 窗口第一行 |
| `LAST_VALUE()` | 窗口最后一行 |
| `NTH_VALUE()` | 窗口第N行 |

memo：注意 lead() 和  lag() 的用法。

## 七、JSON函数(MySQL 5.7+)

| 函数                | 作用            |
| ----------------- | ------------- |
| `JSON_EXTRACT()`  | 提取JSON值       |
| `JSON_OBJECT()`   | 创建JSON对象      |
| `JSON_ARRAY()`    | 创建JSON数组      |
| `JSON_CONTAINS()` | 检查JSON包含      |
| `JSON_SEARCH()`   | 搜索JSON路径      |
| json_pretty()     | print pretty  |
| json_unquote      | remove quotes |
实战 json:
```sql
create table wide_table (
    id int auto_increment primary key,
    data JSON
);

insert into wide_table (data) values
	(JSON_OBJECT('name', 'alice', 'age', 25, 'address', json_object('city', 'beijing', 'province', 'beijing'))),
	(JSON_OBJECT('name', 'jack', 'age', 35, 'address', json_object('city', 'shanghai', 'province', 'shanghai')));

-- query example 1: pretty print
select id, json_pretty(data) from wide_table;

-- query 2: extract column from json and nested json obj
select
	JSON_UNQUOTE(json_extract(data, '$.name')) as name,
	JSON_UNQUOTE(json_extract(data, '$.address.city')) as city
from wide_table;

-- query 3: filter with json fields
select
	JSON_UNQUOTE(json_extract(data, '$.name')) as name,
	JSON_UNQUOTE(json_extract(data, '$.address.city')) as city
from wide_table where JSON_UNQUOTE(json_extract(data, '$.address.city')) = 'shanghai';
--(use explain, we can see, the type of this sql is ALL, that means it will scan all rows)

-- add virtual column

alter table wide_table
	add column city varchar(30) as (JSON_UNQUOTE(json_extract(data, '$.address.city'))) virtual;
-- (有了 virtual column 之后，那么就可以直接 select city from wide_column了)

-- add index on virtual column
create index idx_city on wide_table (city);

-- query with virtual column (verify the index used, type='ref', ref=const)
explain select
	JSON_UNQUOTE(json_extract(data, '$.name')) as name,
	city
from wide_table where city = 'shanghai';
```
video lecture: [从10张表获取信息方案设计（200毫秒干到10毫秒，利用MySQL JSON特性优化千万级订单表）_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1bJ4m1N7kJ/?spm_id_from=333.337.search-card.all.click&vd_source=3b4e5325419175ea5ebf5c16e5e29278)


## 八、系统信息函数

| 函数                          | 作用      |
| --------------------------- | ------- |
| `VERSION()`                 | MySQL版本 |
| `DATABASE()`                | 当前数据库   |
| `USER()` / `CURRENT_USER()` | 当前用户    |
| `CONNECTION_ID()`           | 连接ID    |
| `LAST_INSERT_ID()`          | 最后插入ID  |

这些函数可以组合使用，构建强大的SQL查询语句。不同数据库系统(如Oracle, SQL Server, PostgreSQL)可能有不同的函数名称或实现，但核心功能相似。