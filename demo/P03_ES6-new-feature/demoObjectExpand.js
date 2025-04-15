let person = {
    name: "admin",
    age: 12,
    wife: {
        name:"迪丽热巴"
    }
}

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