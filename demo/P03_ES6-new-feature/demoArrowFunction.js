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


// -------------------
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


// --------------------------

let obj2 = {
    age: 35,
    print: function() {
        let nested = () => console.log(this.age);
        return nested;
    }
};

obj2.print()() // print 35
