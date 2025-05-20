
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