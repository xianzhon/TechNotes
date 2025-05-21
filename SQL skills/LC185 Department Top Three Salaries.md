
该题掌握程度：
- #熟练✓

## 1. 题目
题目链接：[185. 部门工资前三高的所有员工 - 力扣（LeetCode）](https://leetcode.cn/problems/department-top-three-salaries/description/)

Table: `Employee`

```
+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| id           | int     |
| name         | varchar |
| salary       | int     |
| departmentId | int     |
+--------------+---------+
id is the primary key (column with unique values) for this table.
departmentId is a foreign key (reference column) of the ID from the Department table.
Each row of this table indicates the ID, name, and salary of an employee. It also contains the ID of their department.
```



Table: `Department`

```
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
+-------------+---------+
id is the primary key (column with unique values) for this table.
Each row of this table indicates the ID of a department and its name.
```



A company's executives are interested in seeing who earns the most money in each of the company's departments. A **high earner** in a department is an employee who has a salary in the **top three unique** salaries for that department.

Write a solution to find the employees who are **high earners** in each of the departments.

Return the result table **in any order**.

The result format is in the following example.



**Example 1:**

```
Input:
Employee table:
+----+-------+--------+--------------+
| id | name  | salary | departmentId |
+----+-------+--------+--------------+
| 1  | Joe   | 85000  | 1            |
| 2  | Henry | 80000  | 2            |
| 3  | Sam   | 60000  | 2            |
| 4  | Max   | 90000  | 1            |
| 5  | Janet | 69000  | 1            |
| 6  | Randy | 85000  | 1            |
| 7  | Will  | 70000  | 1            |
+----+-------+--------+--------------+
Department table:
+----+-------+
| id | name  |
+----+-------+
| 1  | IT    |
| 2  | Sales |
+----+-------+
Output:
+------------+----------+--------+
| Department | Employee | Salary |
+------------+----------+--------+
| IT         | Max      | 90000  |
| IT         | Joe      | 85000  |
| IT         | Randy    | 85000  |
| IT         | Will     | 70000  |
| Sales      | Henry    | 80000  |
| Sales      | Sam      | 60000  |
+------------+----------+--------+
Explanation:
In the IT department:
- Max earns the highest unique salary
- Both Randy and Joe earn the second-highest unique salary
- Will earns the third-highest unique salary

In the Sales department:
- Henry earns the highest salary
- Sam earns the second-highest salary
- There is no third-highest salary as there are only two employees
```



**Constraints:**

- There are no employees with the **exact** same name, salary *and* department.



Schema:

```sql
Create table If Not Exists Employee (id int, name varchar(255), salary int, departmentId int)
Create table If Not Exists Department (id int, name varchar(255))

Truncate table Employee
insert into Employee (id, name, salary, departmentId) values ('1', 'Joe', '85000', '1')
insert into Employee (id, name, salary, departmentId) values ('2', 'Henry', '80000', '2')
insert into Employee (id, name, salary, departmentId) values ('3', 'Sam', '60000', '2')
insert into Employee (id, name, salary, departmentId) values ('4', 'Max', '90000', '1')
insert into Employee (id, name, salary, departmentId) values ('5', 'Janet', '69000', '1')
insert into Employee (id, name, salary, departmentId) values ('6', 'Randy', '85000', '1')
insert into Employee (id, name, salary, departmentId) values ('7', 'Will', '70000', '1')

Truncate table Department
insert into Department (id, name) values ('1', 'IT')
insert into Department (id, name) values ('2', 'Sales')
```



## 2. 最佳思路

- 先思考按 departmentId 进行分组，然后找到每个组里面前 3 的，通过示例可以看到，要求不同金额的前 3 ，金额相同的也要输出来，所以要用 dense_rank 去编号
- 然后在外层，对 rnk 进行筛选，只保留前 3 的
- 然后要输出 department name，可以 inner join department 表即可



## 3. 最佳代码

MySQL & Postgres.

```sql
select d.name as Department, t.name as Employee, Salary
from (
    select id, name, salary, departmentId,
    dense_rank() over (partition by departmentId order by salary desc) rnk from employee
) t
inner join department d on d.id = t.departmentId
where t.rnk <= 3;
```

## 其他解法 - timeout

思路：公司里前 3 高的薪水意味着有不超过 3 个工资比这些值大。
```sql
select e1.Name as 'Employee', e1.Salary
from Employee e1
where
(
    select count(distinct e2.Salary)
    from Employee e2
    where e2.Salary > e1.Salary and e2.departmentId = e1.departmentId
) < 3;
```

然后，join department 去拿 name.
```sql
select d.name 'Department', e1.name 'Employee', e1.Salary
from Employee e1
inner join department d on d.id = e1.departmentId
where
(
    select count(distinct e2.Salary)
    from Employee e2
    where e2.Salary > e1.Salary and e1.departmentId = e2.departmentId
) < 3;

```
## 考点

窗口函数。dense_rank的用法。