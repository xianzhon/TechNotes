## System design related
### after-thought / afterthought:
- meaning: think about or add something later.
- context: Why is scalability so hard? Because scalability cannot be an `after-thought`. It requires applications and platforms to be designed with scaling in mind, such that adding resources actually results in improving the performance or that if redundancy is introduced the system performance is not adversely affected.  (ref 1)

### heterogeneity
- meaning: the state of being diverse in content
- context: `Heterogeneity` means that some nodes will be able to process faster or store more data than other nodes in a system and algorithms that rely on uniformity either break down under these conditions or underutilize the newer resources.
  异质性意味着系统中某些节点比其他节点处理速度更快或存储的数据更多，而依赖于统一性的算法要么在这些条件下崩溃，要么不能充分利用较新的资源。
- context2: Also, since there can be heterogeneous machines in the clusters, some servers might hold more Vnodes than others. (background of consistent hashing with virtual nodes. meaning some server has better hardware, then it can take more responsibilities)
### pitfall
- meaning: a trap; hidden danger
- context: make sure that architects are aware of which tools they can use for under which conditions, and what the common `pitfalls` are.

### decommision
- meaning: destroyed
- context: Adding and removing nodes in any distributed system is quite common. Existing nodes can die and may need to be `decommissioned`.


### stackholder
- meaning: anyone who **cares about or is affected by** what you’re working on.
- "作为软件开发者，我的 stakeholders 主要包括产品经理、项目经理、设计师、测试团队和其他开发同事。  例如，我需要和产品经理确认需求细节，与设计师对齐 UI 实现，并配合测试团队修复问题。  此外，我也需要关注最终用户的反馈，确保开发的功能真正满足他们的需求。"

A **stakeholder** is anyone who **cares about or is affected by** what you’re working on. They could be people who:
- **Depend on your work** (e.g., your boss, customers).
- **Help you do your work** (e.g., teammates, designers).
- **Are impacted by your work** (e.g., users, company executives).









## References

1. https://www.allthingsdistributed.com/2006/03/a_word_on_scalability.html