const p = console.log;

p(a); // print undefined
p(b); // ReferenceError: Cannot access 'b' before initialization
p(c);

var a = 1;
let b = 2;
const c = 3;

// c = 4; // TypeError: Assignment to constant variable

/*
 * summary: var variable will be promoted to global variable, and we can use it before declaration, but the value is undefined until it is declared.
 *
 * let & const: strictly follow the variable visibility as java.
 */
