const p = console.log;

// Object.is: checks if two values are the same value
p(Object.is(0, -0)); // false.  Note: take care of this!!
p(-0 === +0); // true
p(Object.is(NaN, NaN)); // true
p(NaN === NaN); // false   note: strange!!

o1 = Object.create(null);
o2 = Object.create(null);
p(Object.is(o1, o2)) // false. two instances of Object.create(null) are not the same object. so it compares by reference.
p(o1 === o2);        // false

p("\n-----Object.keys usage-----")
let obj = {a: 1, b: 2, c: 3};
p(Object.keys(obj)); // ["a", "b", "c"]

p("\n-----Object.values usage-----")
p(Object.values(obj)) // [1, 2, 3]

p("\n-----Object.entries usage-----")
p(Object.entries(obj)) // [["a", 1], ["b", 2], ["c", 3]]

Object.entries(obj).forEach(([key, value]) => {
  p(`${key}: ${value}`);
});

p("\n-----Object.assign usage-----")
let obj1 = {a: 1, b: 2};
let obj2 = {b: 3, c: 4};
let obj3 = Object.assign(obj1, obj2);
p(obj3); // {a: 1, b: 3, c: 4}


p("\n-----Object.fromEntries usage-----")
const entries = [["name", "Alice"], ["age", 25]];
const newObj = Object.fromEntries(entries);
console.log(newObj); // { name: "Alice", age: 25 }