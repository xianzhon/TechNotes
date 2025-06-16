## 题目1
```
现在有一个图：
Node (1)------Node(3)
|               |
|               |
Node(2)-------Node(4)

需要你实现一个 clone 方法，能够deep clone 这个图：
Node (1')------Node(3')
|               |
|               |
Node(2')-------Node(4')

input: leader node Node(1)
output: cloned node Node(1')
```

## Solution 1
```java
// deep copy of the graph
Node clone(Node head) {
	return cloneHelper(head, new HashMap<>());
}

Node cloneHelper(Node head, Map<Node, Node> nodeMap) {
	if (nodeMap.containsKey(head)) {
		return nodeMap.get(head);
	}
	Node copy = new Node(head.value);
	nodeMap.put(head, copy);

	for (Node next : head.neighbors) {
		copy.neigbhors.add(cloneHelper(next));
	}
	return copy;
}
```


## Followup: a list of graph

```
// Follow up: clone a list of leader nodes
Node (1)------Node(3)
|               |
|               |
Node(2)-------Node(4)

Node (5)------Node(6)
|               |
|               |
Node(7)-------Node(8)

input: leaderNodes: [Node(1), Node(5)]
output: cloned leaders [Node(1'), Node(5')]
```


Solution:
```java
Node clone(List<Node> leaderNodes) {
	return leaderNodes.stream().map(leaderNode -> {
		return cloneHelper(leaderNode, new HashMap<>())
	}).collect(Collectors.toList());
}
```