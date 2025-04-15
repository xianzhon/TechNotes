class Person {
    constructor(name, age) {
        this.name = name;
        this.age = age;
    }

    sayHi() {
        console.log(`Hello from ${this.name}`);
    }
}

p = new Person('Ross', 33)
p.sayHi() // print: Hello from Ross

class Teacher extends Person { // note: the keyword extends is same as java
    constructor(name, age, school) {
        super(name, age)
        this.school = school;
    }
    sayHi() {
        console.log(`Hello from ${this.name} at ${this.school}`)
    }
}

t = new Teacher('Jack', 28, 'SJTU')
t.sayHi()  // print: Hello from Jack at SJTU
