# Common Table Expressions (CTEs) in MySQL

Common Table Expressions (CTEs) are a powerful SQL feature that allows you to define temporary result sets that exist only during query execution. They make complex queries more readable and maintainable.

## What is a CTE?

A CTE is a named temporary result set that you can reference within a SELECT, INSERT, UPDATE, or DELETE statement. The basic syntax is:

```sql
WITH cte_name AS (
    SELECT columns
    FROM tables
    WHERE conditions
)
SELECT * FROM cte_name;
```

## MySQL Versions Supporting CTEs

- **MySQL 8.0+**: Full support for CTEs (both non-recursive and recursive)
- **MySQL 5.7 and earlier**: Does not support CTEs

CTEs were introduced in MySQL 8.0, which was released in April 2018. If you're using an earlier version, you'll need to upgrade to use this feature.

## Types of CTEs

1. **Non-recursive CTEs**: Simple temporary result sets
   ```sql
   WITH department_stats AS (
       SELECT department_id, COUNT(*) as emp_count
       FROM employees
       GROUP BY department_id
   )
   SELECT * FROM department_stats WHERE emp_count > 5;
   ```

2. **Recursive CTEs**: Can reference themselves, useful for hierarchical data 【似乎很强大！】
   ```sql
   WITH RECURSIVE employee_hierarchy AS (
       -- Base case
       SELECT id, name, manager_id, 1 AS level
       FROM employees
       WHERE manager_id IS NULL

       UNION ALL

       -- Recursive case
       SELECT e.id, e.name, e.manager_id, eh.level + 1
       FROM employees e
       JOIN employee_hierarchy eh ON e.manager_id = eh.id
   )
   SELECT * FROM employee_hierarchy;
   ```

## Benefits of Using CTEs

1. Improved query readability and organization 【可读性提升了，类似于使用视图，将复杂的查询，当成一个简单的临时表】
2. Ability to reference the same subquery multiple times
3. Better alternative to complex nested subqueries
4. Essential for recursive queries (like organizational charts or tree structures)

If you're using MySQL 5.7 or earlier and need CTE functionality, you'll need to either upgrade to MySQL 8.0+ or find alternative approaches using temporary tables or derived tables.


# Common Table Expressions (CTEs) in MariaDB

MariaDB, the MySQL fork, also supports Common Table Expressions (CTEs), but with some version differences compared to MySQL.

## MariaDB Versions Supporting CTEs

- **MariaDB 10.2.2+**: Supports non-recursive CTEs (introduced in 2016)
- **MariaDB 10.3.0+**: Supports both non-recursive and recursive CTEs (released in 2018)

## Key Differences from MySQL

1. **Earlier Introduction**: MariaDB added CTE support before MySQL did (MySQL got CTEs in 8.0)
2. **Syntax Compatibility**: MariaDB's CTE implementation is largely compatible with MySQL 8.0's syntax
3. **Optimizer Differences**: MariaDB may handle some CTE queries differently than MySQL

## Example Usage in MariaDB

**Non-recursive CTE (10.2.2+):**
```sql
WITH department_summary AS (
    SELECT department, COUNT(*) as employee_count
    FROM employees
    GROUP BY department
)
SELECT * FROM department_summary WHERE employee_count > 10;
```

**Recursive CTE (10.3.0+):**
```sql
WITH RECURSIVE org_chart AS (
    -- Base case
    SELECT id, name, manager_id, 1 AS level
    FROM employees
    WHERE manager_id IS NULL

    UNION ALL

    -- Recursive case
    SELECT e.id, e.name, e.manager_id, oc.level + 1
    FROM employees e
    JOIN org_chart oc ON e.manager_id = oc.id
)
SELECT * FROM org_chart ORDER BY level, name;
```

## Current Recommendations

If you need CTEs in MariaDB:
- For production use, ensure you're running at least MariaDB 10.3 (better yet, 10.5+ for best performance)
- Recursive CTEs require 10.3.0 or later
- Always test CTE performance as MariaDB's optimizer may handle them differently than MySQL

MariaDB continues to improve CTE support in newer versions, with better optimization in 10.5+ and additional features in recent releases.

# Common Table Expressions (CTE) Support Across Major RDBMS

## Oracle
- **Supported since**: Oracle 9i (2002)
- **Recursive CTE**: Supported (using `CONNECT BY` prior to 9i, standard recursive CTEs since)
- **Syntax**:
  ```sql
  WITH cte_name AS (
      SELECT columns FROM table
  )
  SELECT * FROM cte_name;
  ```
- **Special features**:
  - Materialized CTEs (`WITH /*+ MATERIALIZE */`)
  - Inline CTEs (`WITH /*+ INLINE */`)

## PostgreSQL
- **Supported since**: PostgreSQL 8.4 (2009)
- **Recursive CTE**: Supported
- **Syntax**: Same standard syntax as others
- **Special features**:
  - Allows CTEs in UPDATE/DELETE statements
  - Supports `SEARCH` and `CYCLE` clauses for recursive CTEs
  - CTEs can be used with writable operations (UPDATE/INSERT in CTEs)

## Microsoft SQL Server
- **Supported since**: SQL Server 2005
- **Recursive CTE**: Supported
- **Syntax**: Same standard syntax
- **Special features**:
  - Supports `OPTION (MAXRECURSION n)` to control recursion depth
  - CTEs can be used in MERGE statements
  - Indexed views can reference CTEs

## IBM DB2
- **Supported since**: DB2 9.1 (2006)
- **Recursive CTE**: Supported
- **Special features**:
  - Advanced recursive features like `DEPTH FIRST`/`BREADTH FIRST`
  - Supports `CYCLE` detection

## SQLite
- **Supported since**: SQLite 3.8.3 (2014)
- **Recursive CTE**: Supported
- **Limitations**: Fewer optimization options compared to enterprise RDBMS

## Comparison Table

| RDBMS       | CTE Since | Recursive CTE | Special Features |
|-------------|-----------|---------------|------------------|
| MySQL       | 8.0 (2018)| Yes           | Limited optimization hints |
| MariaDB     | 10.2.2 (2016)| Since 10.3.0 | Similar to MySQL |
| Oracle      | 9i (2002) | Yes           | Materialization hints, advanced analytics |
| PostgreSQL  | 8.4 (2009)| Yes           | Writable CTEs, SEARCH/CYCLE |
| SQL Server  | 2005      | Yes           | MAXRECURSION, MERGE support |
| DB2         | 9.1 (2006)| Yes           | Depth/Breadth first search |
| SQLite      | 3.8.3 (2014)| Yes        | Basic implementation |

## Advanced Features Across RDBMS

1. **Multiple CTEs in single WITH clause**:
   ```sql
   WITH
     cte1 AS (SELECT ...),     【cte 之间用逗号分隔】
     cte2 AS (SELECT ...)      【最后一个 cte 跟主查询之间，没有逗号】
   SELECT ... FROM cte1 JOIN cte2...
   ```
   (Supported by all modern systems)

2. **Recursive CTE variations**:
   - PostgreSQL/DB2: `SEARCH DEPTH FIRST BY column`
   - Oracle: Alternative `CONNECT BY` syntax
   - SQL Server: `OPTION (MAXRECURSION 100)`

3. **Performance considerations**:
   - Some databases materialize CTEs by default (SQL Server)
   - Others merge them into the main query (PostgreSQL often does this)
   - Oracle and SQL Server provide hints to control this behavior

All major modern RDBMS now support CTEs, though with some variations in syntax and optimization approaches.



# Explanation of CTE using the 5W1H

---

### **1. WHAT is a CTE?**
A **Common Table Expression (CTE)** is a temporary named result set that exists only within the execution scope of a single SQL statement. It improves query readability and simplifies complex queries by breaking them into logical parts.

- **Example**:
  ```sql
  WITH Sales_CTE AS (
      SELECT ProductID, SUM(Quantity) AS TotalSales
      FROM Orders
      GROUP BY ProductID
  )
  SELECT * FROM Sales_CTE WHERE TotalSales > 100;
  ```

---

### **2. WHY use CTEs?**
CTEs are used to:
✔ **Improve readability** (better than nested subqueries)
✔ **Avoid repetition** (reuse the same subquery multiple times)
✔ **Handle recursive queries** (e.g., organizational hierarchies, tree structures)
✔ **Simplify complex joins and aggregations**

**Alternative without CTE (harder to read):**
```sql
SELECT * FROM (
    SELECT ProductID, SUM(Quantity) AS TotalSales
    FROM Orders
    GROUP BY ProductID
) AS TempTable WHERE TotalSales > 100;
```

---

### **3. WHEN were CTEs introduced?**
- **SQL Standard**: CTEs were part of **SQL:1999** (recursive CTEs in **SQL:2003**).
- **Database Support**:
  - **PostgreSQL**: Since **8.4 (2009)**
  - **SQL Server**: Since **2005**
  - **Oracle**: Since **9i (2002)**
  - **MySQL**: Since **8.0 (2018)**
  - **MariaDB**: Since **10.2.2 (2016)**

---

### **4. WHERE are CTEs used?**
- Inside **SELECT, INSERT, UPDATE, DELETE, or MERGE** statements.
- Mostly in **analytical queries, reporting, and hierarchical data processing**.
- **Not stored permanently** (unlike views or temp tables).

---

### **5. WHO uses CTEs?**
- **Database developers** (for cleaner SQL code)
- **Data analysts** (for complex aggregations)
- **BI professionals** (for recursive queries like org charts)

---

### **6. HOW do CTEs work?**
#### **Basic Syntax**
```sql
WITH CTE_Name AS (
    SELECT columns FROM table WHERE conditions
)
SELECT * FROM CTE_Name;
```

#### **Recursive CTE Example (Hierarchical Query)**
```sql
WITH RECURSIVE EmployeeHierarchy AS (
    -- Base case (top-level employees)
    SELECT ID, Name, ManagerID, 1 AS Level
    FROM Employees
    WHERE ManagerID IS NULL

    UNION ALL

    -- Recursive case (subordinates)
    SELECT e.ID, e.Name, e.ManagerID, eh.Level + 1
    FROM Employees e
    JOIN EmployeeHierarchy eh ON e.ManagerID = eh.ID
)
SELECT * FROM EmployeeHierarchy ORDER BY Level;
```

#### **Key Notes**
- **Recursive CTEs must use `UNION ALL`**.
- **MySQL/MariaDB require `RECURSIVE` keyword** (PostgreSQL/SQL Server allow omitting it).
- **Performance**: Some databases materialize CTEs (SQL Server), while others merge them (PostgreSQL).

---

### **Summary (5W1H Recap)**
| **Aspect**  | **Explanation** |
|-------------|----------------|
| **What** | Temporary result set for a single query. |
| **Why** | Improves readability, avoids repetition, handles recursion. |
| **When** | SQL:1999 standard, supported in most DBs since early 2000s. |
| **Where** | Inside SELECT/INSERT/UPDATE/DELETE, not stored permanently. |
| **Who** | DB devs, analysts, BI professionals. |
| **How** | `WITH CTE_Name AS (...) SELECT ... FROM CTE_Name`. |

CTEs are a **powerful SQL feature** that makes complex queries **easier to write and maintain**. 🚀