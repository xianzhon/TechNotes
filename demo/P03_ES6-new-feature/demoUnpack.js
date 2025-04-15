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
