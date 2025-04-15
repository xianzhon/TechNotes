// Creating a Symbol
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

// Well-known Symbol (custom iteration)
const myIterable = {
  [Symbol.iterator]: function* () {
    yield 1;
    yield 2;
  },
};
for (const num of myIterable) {
    console.log(num); // 1, 2
}
