# ES6 New Features



## 1. 函数（箭头函数）

- what:  arrow function provides a shorter syntax to write function as an expression.  注意：java 中 lambda 是用单箭头，ES6 中是用双箭头

```javascript
function add(a, b) { return a + b; }
const add = (a, b) => a + b;
```

- why: 1. more concise syntax 语法更简洁了；  2. 解决了匿名函数 this 指向问题，有利于封装回调函数（箭头函数体内的`this`对象，就是定义**该函数时所在的作用域指向的对象**，而不是使用时所在的作用域指向的对象。）【普通函数，this 是跟调用的对象绑定的】

```javascript
// arrow function: this is decided where it is defined.
// 箭头函数体内的`this`对象，就是定义**该函数时所在的作用域指向的对象**，而不是使用时所在的作用域指向的对象

var age = 20;
let obj = {
    age: 35,
    print: () => {
        console.log(this.age);
    }
};

obj.print(); // print 20 in browser console. but print underfined in nodejs
```

For normal function, it behaves like:

```javascript
// node version: v16.20.2
// Normal function: 'this' always binded to the caller object
var name = 'window';  // this equals: window.name = 'foo' in browser console

var A = {
    name: 'A',
    sayHi: function() {
        console.log(this.name);
    }
};

A.sayHi();  // print A. because this binds to the caller object: A

var B = {
    name: 'B'
};

A.sayHi.call(B); // print B. same reason, the caller object is B.
A.sayHi.call(); // print window in browser console, but print undefined in nodejs
```

- where: anywhere a function expression is used, but not for object methods
- when: when you want cleaner code and avoid `this` binding issues.



## 2. template string

- what: 在字符串里面，允许插入变量，直接输出变量的内容。避免了字符串拼接，提高了可读性

```javascript
const name = 'Ross'
let msg = `hello ${name}`
console.log(msg); // output: hello Ross

let msg2 = `<h1>hello</h1>
<div>ross</div>`;

console.log(msg2);
```

- why: 1. 避免字符串拼接，提高可读性； 2. support to define a multiple line string (useful for html blocks).
- when: When building dynamic strings or multiline text (e.g., HTML templates).
- who: Developers working with dynamic content (e.g., APIs, UIs).



## 3. 解构表达式 - destructing assignment

- what: Unpack values from arrays / objects into variables.

```javascript
// destructing assignment

let arr = [1, 2, 3]
let [a, b, c] = arr  // Note: variables the left side must use same form as right side. array / object

console.log(a) // 1
console.log(b) // 2
console.log(c) // 3

let obj = {
    name: 'alice',
    age: 25
};

const {name, age} = obj;
console.log(name); // alice
console.log(age);  // 25
```



- why: simplify extracting data from structure (例如一个 obj 里面有很多属性，我们只需要里面的一个或少数几个属性). 
- when: when working with complex data (e.g. API responses, React props)
- where: function param, imports, state management

## 4. 异步 Async/Await

- why: 简化 promise 编程，让异步编程更容易理解，跟同步编程一样；避免回调地狱

```javascript
fetch(url)
  .then(response => response.json())
  .then(data => console.log(data));

// Async/Await
async function fetchData() {
  const response = await fetch(url);
  const data = await response.json();
  console.log(data);
}
```

- when: dealing with APIs, file I/O, DBs, or any async task.
- where: Frontend (AJAX call), backend (NodeJs)



## 5. class

- what: JS 的一个语法糖，还是基于 prototype inheritance. 原型链继承
- why: cleaner OOP syntax.
- when: create reusable components (React, Angular). building complex applications with inheritance.
- where: object-oriented Javascript applications

```javascript
// define class
class Person {
    constructor(name, age) {
        this.name = name;
        this.age = age;
    }

    sayHi() {
        console.log(`Hello from ${this.name}`);
    }
}

p = new Person('Ross', 33); // create an instance
p.sayHi() // print: Hello from Ross
```

demo the inheritance

```javascript
class Teacher extends Person { // note: the keyword extends is same as java
    constructor(name, age, school) { // note2: keyword constructor, different with java
        super(name, age);
        this.school = school;
    }
    sayHi() {
        console.log(`Hello from ${this.name} at ${this.school}`) // note3: can access extended var with this
    }
}

t = new Teacher('Jack', 28, 'SJTU')
t.sayHi()  // print: Hello from Jack at SJTU
```

## 6. 模块化 - import/export

- what: native support for modular code organization
- why: 更好组织代码；避免变量污染全局空间
- when: in a large-scale applications and want to improve maintainablity

```javascript
// math.js
export const add = (a, b) => a + b;

// app.js
import { add } from './math.js';

// Export
export default function greet() { return "Hello!"; }
// Import
import greet from './greet.js';
```





## 7. let and const

var 定义的变量，会自动提升作用域。而 let 和 const 就和 java 里的类型定义一样，作用域比较清晰明确，不能提前使用未经定义的变量。

```javascript
const p = console.log;

p(a); // print undefined
p(b); // ReferenceError: Cannot access 'b' before initialization
p(c);

var a = 1;
let b = 2;
const c = 3;

c = 4; // TypeError: Assignment to constant variable
```

- **what**: `let` defines block-scoped and reassignable variable. `const`: block-scoped & constant variable.
- **why**: Fixes issues with `var` (function-scoped, hoisting, accidental global leaks); Encourages better coding practices (immutability with `const`).
- **where**: Anywhere variables are needed (replaces `var` in modern JS).

```javascript
// let (reassignable)
let counter = 0;
counter = 1; // ✅ Works

// const (immutable binding)
const PI = 3.14;
PI = 3.14159; // ❌ TypeError: Assignment to constant variable.

// Block-scoping
if (true) {
  let blockVar = "I'm scoped here!";
  const blockConst = "Me too!";
}
console.log(blockVar); // ❌ ReferenceError
```

## 8. symbol - unique primitive

- what: java 中没有类似概念。基础类型，保证唯一。主要用作对象属性的 key 值。
- why：避免属性的名称冲突 (library)；实现私有属性
- where: Mostly in libraries/metaprogramming.

```javascript
const id = Symbol("uniqueId");
const id2 = Symbol("uniqueId");

// Using as an object key
const user = {
  name: "Alice",
  [id]: 12345, // Hidden from for...in, Object.keys()
  id2: 6789
};

console.log(user[id]); // 12345
console.log(user.id);  // print undefined

console.log(user.id2); // 6789
console.log(Object.keys(user)); // print ['name', 'id2'] ([symbol] is hidden)
```





## 5. 对象

### 5.1 Object 新加的方法 is/assign/keys/values/entries

类似于 java 8 中，Objects类中的静态方法: Objects.equals.

Takeaways:

- **`Object.is()`**: Safer than `===` for `NaN` and `-0`.
- **`Object.assign()`**: Shallow merge (avoid nested references).
- **`Object.keys/values/entries()`**: Convert objects to arrays for iteration.
- **`Object.fromEntries()`**: Rebuild objects from entries (e.g., after `filter`).

```javascript
console.log(Object.is(NaN, NaN)); // true (✅ Fixes NaN === NaN → false)
console.log(Object.is(-0, +0));  // false (✅ Fixes -0 === +0 → true)

p("\n-----Object.keys usage-----")
let obj = {a: 1, b: 2, c: 3};
p(Object.keys(obj)); // ["a", "b", "c"]

p("\n-----Object.values usage-----")
// Returns an array of an object’s own enumerable property `values`.
p(Object.values(obj)) // [1, 2, 3]

p("\n-----Object.entries usage-----")
// Returns an array of [key, value] pairs.
p(Object.entries(obj)) // [["a", 1], ["b", 2], ["c", 3]]

Object.entries(obj).forEach(([key, value]) => {
  p(`${key}: ${value}`);
});

p("\n-----Object.assign usage-----")
let obj1 = {a: 1, b: 2};
let obj2 = {b: 3, c: 4};
let obj3 = Object.assign(obj1, obj2);
p(obj3); // {a: 1, b: 3, c: 4}
```



`Object.fromEntries()` (Object from Entries)

- **what**: Transforms a list of `[key, value]` pairs **back into an object**.
- **why**: reverse operation of `Object.entires()`; Useful after array manipulations (e.g., `filter` → `Object.fromEntries`).
- **when**: Reconstruct objects from Map or arrays.
- **where**: Data normalization, API response handling.

```javascript
const entries = [["name", "Alice"], ["age", 25]];
const obj = Object.fromEntries(entries);
console.log(obj); // { name: "Alice", age: 25 }
```



### 5.2 对象声明简写

```javascript
let name = 'admin'
let age = 20

//es6之前
let personOld = {
    name: name,
    age: age
}
//es6  声明对象时的属性名与引用的变量名相同就可以省略
let person={
    name,
    age
}
```

### 5.3 对象扩展符 (…)

```javascript
// useage 1: 拷贝对象（一层深拷贝）
let person2 = {...person}

console.log(person2 === person); //false
console.log(person2); // Print { name: 'admin', age: 12, wife: { name: '迪丽热巴' } }

// usage 2: 合并对象
let o1 = {a: 1, b: 2}
let o2 = {b: 3, c: 4}
let o3 = {d: 5}
let o4 = {...o1, ...o2, ...o3}
console.log(o4) // {a: 1, b: 3, c: 4, d: 5}
```



## 6. 函数参数默认值

跟 Python 的函数默认值类似，java 中没有参数默认值，只能通过函数重载来实现类似的调用形式。

```javascript
function add(a, b=10) {
  return a + b;
}

console.log(add(1)); // 11
console.log(add(1, 2)); // 3
```

