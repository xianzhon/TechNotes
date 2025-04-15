const p = console.log;

function add(a, b=10) {
  return a + b;
}

p(add(1)); // 11
p(add(1, 2)); // 3