const p = console.log;

let name = 'admin'
let age = 20

//es6之前
let personOld = {
    name: name,
    age: age
}
p(personOld);

//es6  声明对象时的属性名与引用的变量名相同就可以省略
let person={
    name,
    age
}
p(person);

// if we changed the name of some field, we can mix the old and new way
let personNew = {
    familyName: name,
    age
}

p(personNew);