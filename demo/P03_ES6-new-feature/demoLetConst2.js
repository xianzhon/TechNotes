const p = console.log;

if (1) {
    var a = 1;
    let b = 2;
    const c = 3;
}

p(a); // print 1
p(b); // ReferenceError: b is not defined

