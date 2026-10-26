# JavaScript Fundamentals Deep Dive (Interview Edition)

Seven topics that interviewers love, because each one hides a trap. For each topic: **concept, how it works, code, pitfalls, interview questions, practice**.

Run every snippet yourself in Node (`node file.js`) or the browser console. Reading is not enough; predict the output first, then run.

## Contents
1. `var` / `let` / `const`
2. Closures
3. `this`
4. Equality and type coercion
5. Array methods
6. Destructuring and spread
7. Modules (ESM vs CommonJS)
8. The closure + async trap (explained fully)
9. Rapid-fire interview Q&A
10. Practice exercises with answers

---

## 1. `var` / `let` / `const`

### 1.1 Scope

| Keyword | Scope | Hoisted? | Re-declare in same scope? | Re-assign? |
|---|---|---|---|---|
| `var` | **Function** (or global) | Yes, initialized to `undefined` | Yes | Yes |
| `let` | **Block** `{ }` | Yes, but in the Temporal Dead Zone (TDZ) | No | Yes |
| `const` | **Block** `{ }` | Yes, but in the TDZ | No | No (binding is fixed) |

```js
function demo() {
  if (true) {
    var a = 1;   // function-scoped, leaks out of the if block
    let b = 2;   // block-scoped
    const c = 3; // block-scoped
  }
  console.log(a); // 1
  console.log(b); // ReferenceError: b is not defined
}
```

### 1.2 Hoisting

JavaScript processes declarations **before** running code (during the creation phase of an execution context). What differs is the state of the variable afterwards.

```js
console.log(x); // undefined  (var is hoisted AND initialized to undefined)
var x = 5;

console.log(y); // ReferenceError: Cannot access 'y' before initialization
let y = 5;
```

The code above behaves as if `var x;` were moved to the top, while the assignment `x = 5` stays where it is.

**Function declarations** are hoisted completely (name and body). **Function expressions** follow the rule of the variable that holds them:

```js
greet();               // works
function greet() { console.log("hi"); }

hello();               // TypeError: hello is not a function (var hello is undefined)
var hello = function () { console.log("hello"); };

hey();                 // ReferenceError (TDZ)
const hey = () => console.log("hey");
```

### 1.3 Temporal Dead Zone (TDZ)

The TDZ is the time between entering a scope and the line where `let`/`const` is declared. Touching the variable in that window throws a `ReferenceError`.

```js
{
  // TDZ for `n` starts here
  console.log(typeof n); // ReferenceError (even typeof is not safe in the TDZ)
  let n = 1;             // TDZ ends here
}
```

`let` and `const` **are** hoisted (the engine knows about them at scope start). That is why this fails with a TDZ error rather than silently reading an outer variable:

```js
let v = "outer";
{
  console.log(v); // ReferenceError, not "outer": inner `v` is hoisted but in the TDZ
  let v = "inner";
}
```

### 1.4 `const` does not mean immutable

`const` fixes the **binding** (the variable cannot point somewhere else). It does not freeze the **value**.

```js
const user = { name: "Shyam" };
user.name = "Sham";      // allowed, mutating the object
user = {};               // TypeError: Assignment to constant variable

const arr = [1, 2];
arr.push(3);             // allowed
```

To prevent mutation use `Object.freeze(obj)`. It is **shallow**: nested objects stay mutable.

```js
const cfg = Object.freeze({ db: { host: "localhost" } });
cfg.db.host = "remote";  // still works, because db was not frozen
```

### 1.5 Global object behavior

```js
var a = 1;
let b = 2;
console.log(globalThis.a); // 1 in a browser script (var at top level attaches to window)
console.log(globalThis.b); // undefined
```
(In Node modules, top-level `var` is module-scoped, so this does not apply there.)

### 1.6 Best practice
1. Default to `const`.
2. Use `let` only when you must re-assign.
3. Avoid `var` in new code.

### Interview questions
- **Difference between `var`, `let`, `const`?** Scope, hoisting/TDZ, redeclaration, reassignment (table above).
- **Is `let` hoisted?** Yes, but it is uninitialized until its declaration line (TDZ).
- **Can you change a `const` array?** Yes, contents can change; the binding cannot.

---

## 2. Closures

### 2.1 Definition

A **closure** is a function bundled together with a reference to the variables of the scope where it was **created** (its lexical environment). The function keeps access to those variables even after the outer function has returned.

```js
function outer() {
  let count = 0;          // lives in outer's scope
  return function inner() {
    count++;              // inner "closes over" count
    return count;
  };
}

const next = outer();
console.log(next()); // 1
console.log(next()); // 2
// `outer` finished long ago, but `count` survives because `inner` still references it.
```

Key point: the closure captures the **variable** (a live reference), not a copy of its value at that moment.

### 2.2 The counter factory (a common coding question)

```js
function counter(start = 0, step = 1) {
  let value = start;
  return {
    increment() { value += step; return value; },
    decrement() { value -= step; return value; },
    reset()     { value = start; return value; },
    get()       { return value; },
  };
}

const c = counter(10, 5);
c.increment(); // 15
c.increment(); // 20
c.reset();     // 10
// `value` cannot be accessed from outside: real data privacy.
```

Each call to `counter()` creates a **new** scope, so two counters do not share state.

### 2.3 Practical uses

**Data privacy / encapsulation** (shown above).

**Function factories**
```js
const multiplyBy = (n) => (x) => x * n;
const double = multiplyBy(2);
double(7); // 14
```

**Memoization (cache)**
```js
function memoize(fn) {
  const cache = new Map();
  return (arg) => {
    if (cache.has(arg)) return cache.get(arg);
    const result = fn(arg);
    cache.set(arg, result);
    return result;
  };
}
const slowSquare = (n) => n * n;
const fastSquare = memoize(slowSquare);
```

**`once` (run a function a single time)**
```js
function once(fn) {
  let called = false, result;
  return (...args) => {
    if (!called) { called = true; result = fn(...args); }
    return result;
  };
}
```

**Debounce (very common in React search boxes)**
```js
function debounce(fn, delay) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}
```
`timer` persists between calls because of the closure.

**Callbacks and event handlers** rely on closures to remember the variables around them. In React, every render creates new closures, which is why "stale closure" bugs happen in `useEffect`/`setInterval` when you forget dependencies.

### 2.4 Classic pitfalls

**Loop with `var`** (see section 8).

**Memory leaks:** a closure keeps its captured variables alive. If a long-lived function (event listener, timer) captures a large object, that object is not garbage-collected until the function is released. Remove listeners and clear timers.

**Stale closure:**
```js
function makeLogger() {
  let msg = "first";
  const log = () => console.log(msg);
  msg = "second";   // closure sees the UPDATED variable
  return log;
}
makeLogger()(); // "second"
```

### Interview questions
- **What is a closure?** Function + its lexical environment; inner function retains access to outer variables after the outer returns.
- **Give a real use.** Private state, memoize, debounce, factories.
- **Do closures copy values?** No, they reference variables.

---

## 3. `this`

### 3.1 The core idea

`this` is **not** decided by where a function is written. It is decided by **how the function is called** (the call site). The exception is the arrow function, which has no `this` of its own and takes it from the surrounding scope where it was **defined**.

### 3.2 The five rules (check in this order)

| # | Rule | Call style | `this` is |
|---|---|---|---|
| 1 | `new` binding | `new Foo()` | the newly created object |
| 2 | Explicit binding | `fn.call(obj)`, `fn.apply(obj)`, `fn.bind(obj)()` | `obj` |
| 3 | Implicit binding | `obj.fn()` | `obj` (object left of the dot) |
| 4 | Default binding | `fn()` | `globalThis` (non-strict) or `undefined` (strict mode, modules, classes) |
| 5 | Arrow function | `() => {}` | `this` of the enclosing scope (cannot be changed by call/bind) |

### 3.3 Examples

```js
const user = {
  name: "Shyam",
  greet() { console.log("Hi, " + this.name); },
};

user.greet();          // "Hi, Shyam"  (implicit binding)

const fn = user.greet; // copy the function reference only
fn();                  // "Hi, undefined" (default binding: this is not user)
```

**Explicit binding**
```js
function intro(city, country) {
  console.log(`${this.name} from ${city}, ${country}`);
}
const p = { name: "Shyam" };

intro.call(p, "Pune", "India");     // arguments listed
intro.apply(p, ["Pune", "India"]);  // arguments in an array
const bound = intro.bind(p, "Pune");// returns a NEW function, does not call
bound("India");
```

**Arrow functions and lexical `this`**
```js
const team = {
  name: "Backend",
  members: ["A", "B"],
  show() {
    this.members.forEach((m) => {
      console.log(this.name, m); // arrow uses show()'s this -> team
    });
  },
};
team.show();
```
If the callback were a regular `function () {}`, its `this` would be `undefined` (strict) or global, and `this.name` would fail or be wrong.

**Do not use arrows as object methods:**
```js
const bad = {
  name: "x",
  hi: () => console.log(this.name), // `this` is the OUTER scope (module/global), not bad
};
```

### 3.4 Why does `setTimeout(obj.method, 0)` lose `this`?

```js
const obj = {
  name: "Obj",
  method() { console.log(this && this.name); },
};

setTimeout(obj.method, 0);
```

You pass only the **function reference**. `setTimeout` later calls it as a plain function, with no object before the dot, so rule 4 (default binding) applies. In a browser non-strict script, `this` is `window`, so `this.name` is `""` or `undefined`. In Node, `this` is the `Timeout` object, so `this.name` is `undefined`. In strict mode, it is `undefined` and `this.name` throws.

**Three fixes**
```js
setTimeout(() => obj.method(), 0);       // 1. wrap in an arrow (call site is obj.method())
setTimeout(obj.method.bind(obj), 0);     // 2. bind
// 3. define method as an arrow property when appropriate (class fields, see below)
```

### 3.5 `this` in classes

```js
class Counter {
  count = 0;
  inc() { this.count++; }            // normal method: depends on call site
  incArrow = () => { this.count++; } // class field arrow: always bound to the instance
}

const c = new Counter();
const { inc, incArrow } = c;
inc();       // TypeError: Cannot read properties of undefined (class bodies are strict)
incArrow();  // works
```

This is the origin of the `this.handleClick = this.handleClick.bind(this)` pattern in older React class components.

### 3.6 Quick decision guide
- Passing a method as a callback? Wrap it in an arrow or `bind` it.
- Need `this` from the surrounding code inside a callback? Use an arrow function.
- Defining object/prototype/class methods? Use regular method syntax.

### Interview questions
- **How is `this` determined?** By call site; arrows are lexical.
- **`call` vs `apply` vs `bind`?** call and apply invoke immediately (args list vs array); bind returns a new function with `this` fixed.
- **Can you rebind an arrow function's `this`?** No.

---

## 4. Equality and type coercion

### 4.1 `==` vs `===`

- `===` (strict): no type conversion. Different types means `false`.
- `==` (loose): converts types first using complicated rules.

```js
1 === "1"   // false
1 == "1"    // true  ("1" -> 1)
0 == false  // true
"" == false // true
null == undefined // true (special rule)
null == 0   // false
```

### 4.2 Why `[] == false` is `true`

Step by step (abstract equality algorithm):
1. One side is a boolean, so convert `false` to a number: `[] == 0`.
2. One side is an object, so convert it to a primitive with `toString()`: `[].toString()` is `""`, so `"" == 0`.
3. String vs number, so convert the string to a number: `0 == 0`.
4. `true`.

Even stranger: `![] == []` is `true`. This is why the rule is: **use `===`**.

### 4.3 Falsy values (memorize all 8)

`false`, `0`, `-0`, `0n` (BigInt zero), `""`, `null`, `undefined`, `NaN`

Everything else is truthy, including `[]`, `{}`, `"0"`, `"false"`, and `function(){}`.

```js
if ([]) console.log("truthy");  // prints
if ("0") console.log("truthy"); // prints
```

### 4.4 `NaN`, `Object.is`, `typeof`

```js
NaN === NaN            // false (the only value not equal to itself)
Number.isNaN(NaN)      // true (prefer over global isNaN, which coerces)
Object.is(NaN, NaN)    // true
Object.is(0, -0)       // false  (=== says true)

typeof null            // "object"  (historical bug)
typeof []              // "object"  -> use Array.isArray([])
typeof function(){}    // "function"
typeof undeclaredVar   // "undefined" (does not throw)
```

### 4.5 Objects compare by reference

```js
{} === {}        // false (two different objects)
const a = {}; const b = a;
a === b          // true  (same reference)
[1,2] === [1,2]  // false
```
To compare contents use a deep-equal helper or compare `JSON.stringify` (with limits).

### 4.6 Coercion with `+`

```js
1 + "2"   // "12"  (string concatenation wins)
"3" - 1   // 2     (minus forces numbers)
"5" * "2" // 10
[] + []   // ""
[] + {}   // "[object Object]"
true + 1  // 2
```

### 4.7 `||` vs `??` (practical and often asked)

```js
const port = 0;
port || 3000;   // 3000  (0 is falsy, so default replaces a valid value: bug)
port ?? 3000;   // 0     (?? only replaces null/undefined)
```
Use `??` for defaults, and `?.` for safe access: `user?.address?.city`.

### Interview questions
- **`==` vs `===`?** loose with coercion vs strict.
- **Why is `typeof null` "object"?** Legacy bug from the first JS implementation, kept for compatibility.
- **How to check for NaN?** `Number.isNaN(x)`.

---

## 5. Array methods

### 5.1 Mutating vs non-mutating

| Mutates the original | Returns new array/value (does NOT mutate) |
|---|---|
| `push`, `pop`, `shift`, `unshift` | `map`, `filter`, `slice`, `concat` |
| `splice`, `sort`, `reverse`, `fill` | `reduce`, `find`, `findIndex`, `some`, `every` |
| `copyWithin` | `flat`, `flatMap`, `includes`, `indexOf`, `join` |

Newer non-mutating twins (Node 20+, modern browsers): `toSorted()`, `toReversed()`, `toSpliced()`, `with(index, value)`.

### 5.2 The core methods

Sample data:
```js
const orders = [
  { id: 1, status: "paid",    amount: 500 },
  { id: 2, status: "pending", amount: 200 },
  { id: 3, status: "paid",    amount: 300 },
  { id: 4, status: "failed",  amount: 100 },
];
```

**`map`**: transform each item, same length.
```js
const ids = orders.map(o => o.id); // [1,2,3,4]
```

**`filter`**: keep items that pass a test.
```js
const paid = orders.filter(o => o.status === "paid");
```

**`reduce`**: fold the array into one value (number, object, array...).
```js
const total = orders.reduce((sum, o) => sum + o.amount, 0); // 1100
```
Always pass an initial value. `[].reduce((a, b) => a + b)` throws a TypeError.

**`find` / `findIndex`**: first match (or `undefined` / `-1`).
```js
const first = orders.find(o => o.amount > 250); // order 1
```

**`some` / `every`**: boolean checks.
```js
orders.some(o => o.status === "failed");   // true
orders.every(o => o.amount > 0);           // true
[].some(x => true);   // false
[].every(x => false); // true  (vacuous truth)
```

**`includes`**: membership test with `===`-like check (handles NaN).
```js
[1, 2, NaN].includes(NaN); // true  (indexOf would return -1)
```

**`flat` / `flatMap`**
```js
[[1,2],[3]].flat();                   // [1,2,3]
["a b", "c"].flatMap(s => s.split(" ")); // ["a","b","c"]
```

### 5.3 Group orders by status with `reduce` (the interview task)

```js
const grouped = orders.reduce((acc, order) => {
  (acc[order.status] ??= []).push(order);
  return acc;
}, {});

// {
//   paid:    [ {id:1,...}, {id:3,...} ],
//   pending: [ {id:2,...} ],
//   failed:  [ {id:4,...} ]
// }
```
Explained: `acc` starts as `{}`. For each order, create the array for that status if missing (`??=`), then push. Return `acc` every time (forgetting `return acc` is the most common bug).

Alternative without `reduce`: `Object.groupBy(orders, o => o.status)` (modern runtimes: Node 21+).

Group and sum:
```js
const totalByStatus = orders.reduce((acc, o) => {
  acc[o.status] = (acc[o.status] ?? 0) + o.amount;
  return acc;
}, {});
// { paid: 800, pending: 200, failed: 100 }
```

### 5.4 Chaining
```js
const paidTotal = orders
  .filter(o => o.status === "paid")
  .map(o => o.amount)
  .reduce((a, b) => a + b, 0); // 800
```

### 5.5 Sorting traps

```js
[10, 9, 1].sort();               // [1, 10, 9]  (default sort converts to strings!)
[10, 9, 1].sort((a, b) => a - b);// [1, 9, 10]  numeric ascending
[10, 9, 1].sort((a, b) => b - a);// descending
people.sort((a, b) => a.name.localeCompare(b.name)); // strings, locale-aware
```
`sort` **mutates**. To keep the original: `[...arr].sort(...)` or `arr.toSorted(...)`.

### 5.6 Other classic traps

```js
["1", "2", "3"].map(parseInt);  // [1, NaN, NaN]
// map passes (value, index, array); parseInt uses index as the radix.
["1", "2", "3"].map(Number);    // [1, 2, 3]

// forEach does not wait for async callbacks
arr.forEach(async (x) => { await save(x); }); // fires all, does not await them
for (const x of arr) { await save(x); }       // sequential
await Promise.all(arr.map(save));             // parallel
```

`forEach` cannot be stopped with `break`. Use `for...of`, `some`, or `every` if you need early exit.

### Interview questions
- **`map` vs `forEach`?** `map` returns a new array; `forEach` returns `undefined`, used for side effects.
- **Which methods mutate?** push/pop/shift/unshift/splice/sort/reverse/fill.
- **How to remove duplicates?** `[...new Set(arr)]`.

---

## 6. Destructuring and spread/rest

### 6.1 Destructuring

**Arrays (by position)**
```js
const [first, , third = 0, ...rest] = [1, 2, undefined, 4, 5];
// first=1, third=0 (default, because value was undefined), rest=[4,5]

let a = 1, b = 2;
[a, b] = [b, a];  // swap without a temp variable
```

**Objects (by name)**
```js
const user = { id: 7, name: "Shyam", address: { city: "Pune" } };

const { name, id } = user;
const { name: userName } = user;              // rename
const { role = "guest" } = user;              // default
const { address: { city } } = user;           // nested (note: `address` itself is not created)
const { id: uid, ...others } = user;          // rest collects remaining props
```

**In function parameters (very common in React)**
```js
function Card({ title, subtitle = "", onClick }) { /* ... */ }
const sum = ([a, b]) => a + b;
```

**Defaults apply only for `undefined`**, not `null`:
```js
const { x = 10 } = { x: null };  // x is null
const { y = 10 } = { y: undefined }; // y is 10
```

**Pitfall:** destructuring `null`/`undefined` throws.
```js
const { a } = null;     // TypeError
const { a } = obj ?? {}; // safe
```

**Statement starting with `{`:** wrap in parentheses when assigning to existing variables.
```js
let p, q;
({ p, q } = { p: 1, q: 2 });
```

### 6.2 Spread (`...` expands) and rest (`...` collects)

Same syntax, opposite role:
```js
// SPREAD (expanding)
const a = [1, 2], b = [3];
const merged = [...a, ...b];            // [1,2,3]
const copy   = { ...user, name: "Sham" }; // override a property
Math.max(...[3, 9, 4]);                 // 9

// REST (collecting)
function sum(...nums) { return nums.reduce((s, n) => s + n, 0); }
const [head, ...tail] = [1, 2, 3];
```

Later properties win in object spread: `{ ...defaults, ...overrides }`.

### 6.3 Spread is a SHALLOW copy

```js
const original = { name: "A", address: { city: "Pune" } };
const clone = { ...original };

clone.name = "B";            // original.name stays "A"  (top level copied)
clone.address.city = "Mumbai";
console.log(original.address.city); // "Mumbai"  (nested object is the SAME reference)
```

Only the first level is copied. Nested objects/arrays are shared by reference. Same for `[...arr]`, `Object.assign`, `slice`, `Array.from`.

**Deep clone options**
```js
const deep = structuredClone(original);       // modern, handles Date, Map, Set, cycles
const deep2 = JSON.parse(JSON.stringify(o));  // loses Dates (to strings), undefined, functions, Map/Set
```
`structuredClone` cannot clone functions.

**Immutable nested update (React state pattern)**
```js
const next = {
  ...state,
  address: { ...state.address, city: "Mumbai" },
};
```

### Interview questions
- **Spread vs rest?** Spread expands an iterable/object; rest gathers remaining items.
- **Why is `{...obj}` not a deep clone?** Copies only own enumerable props one level deep; nested references are shared.
- **How do you deep clone?** `structuredClone`.

---

## 7. Modules (ESM vs CommonJS)

### 7.1 Two systems

| | **CommonJS (CJS)** | **ES Modules (ESM)** |
|---|---|---|
| Syntax | `require()`, `module.exports` | `import`, `export` |
| Loading | Synchronous, at runtime | Static analysis, async-capable |
| Where `require`/`import` can sit | Anywhere (even inside `if`) | Top level (use `import()` for dynamic) |
| Exports | Copy of the exported value (snapshot) | **Live bindings** to the exporting module's variable |
| Top-level `await` | No | Yes |
| Default in Node | `.js` files unless configured | Needs `"type": "module"` or `.mjs` |
| `__dirname`, `__filename` | Available | Not available (use `import.meta.url`) |
| Browser support | No (needs bundler) | Native (`<script type="module">`) |
| Tree-shaking | Hard | Good (static structure) |

### 7.2 Syntax side by side

**CommonJS**
```js
// math.js
function add(a, b) { return a + b; }
module.exports = { add };          // or: exports.add = add;

// app.js
const { add } = require("./math");
```

**ESM**
```js
// math.mjs  (or math.js with "type": "module")
export function add(a, b) { return a + b; }  // named export
export default function sub(a, b) { return a - b; } // default export

// app.mjs
import sub, { add } from "./math.mjs";  // file extension required in Node ESM
import * as math from "./math.mjs";     // namespace import
import { add as plus } from "./math.mjs"; // rename
```

Default vs named:
- A module has **at most one** default export; importer chooses any name.
- Named exports must be imported with the exact name (or renamed with `as`).

### 7.3 Which one needs `"type": "module"`?

**ESM** needs it (in Node) when files use the `.js` extension:
```json
// package.json
{ "type": "module" }
```
Without it, Node treats `.js` as CommonJS and `import` gives `SyntaxError: Cannot use import statement outside a module`. Alternatives: name files `.mjs` for ESM, or `.cjs` for CommonJS inside an ESM project.

If you use `require` in a `"type": "module"` project you get `ReferenceError: require is not defined in ES module scope`.

### 7.4 Interop and differences that bite

```js
// Dynamic import works in BOTH systems and returns a Promise
const mod = await import("./math.mjs");

// __dirname replacement in ESM
import { fileURLToPath } from "node:url";
import path from "node:path";
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
```

- `import` declarations are **hoisted** (run before the rest of the file).
- Modules are **singletons**: evaluated once, then cached; every importer shares the same instance.
- ESM is in strict mode automatically.
- Circular dependencies: ESM live bindings handle them better, but you can still hit the TDZ.
- Recent Node versions allow `require()` of ESM in some cases; do not rely on it in interviews, state the classic rule.

### 7.5 Where you will meet this
- **React/Vite/Next.js:** ESM syntax everywhere (the bundler handles it).
- **Express/NestJS tutorials:** often CommonJS (`require`), or TypeScript compiling to either.
- **Node scripts/FastAPI frontends:** pick one system per project and stay consistent.

### Interview questions
- **ESM vs CJS?** Static vs dynamic loading, live bindings vs copies, async vs sync, top-level await.
- **Why is ESM better for bundlers?** Static imports allow tree-shaking.
- **`export default` vs named export?** One default per module; named can be many.

---

## 8. The closure + async trap (full explanation)

```js
for (var i = 0; i < 3; i++) setTimeout(() => console.log(i), 0);
```

**Output:** `3 3 3`

### Why, step by step
1. `var i` is **function-scoped**. There is **one** `i` for the whole loop (a single binding).
2. Each iteration schedules a callback with `setTimeout`. The callbacks are **closures** that capture the variable `i`, not its value at that time.
3. `setTimeout(..., 0)` does not run immediately. Callbacks go to the task queue, and the event loop runs them only **after the current synchronous code (the whole loop) finishes**.
4. When the loop ends, `i` is `3`. Now the three callbacks run, each reading the same `i`, so each prints `3`.

### Fix 1: `let` (per-iteration binding)
```js
for (let i = 0; i < 3; i++) setTimeout(() => console.log(i), 0);
// 0 1 2
```
With `let` in a `for` header, the engine creates a **new binding for each iteration** (copying the value forward). Each closure captures its own `i`.

### Fix 2: IIFE (the pre-ES6 way)
```js
for (var i = 0; i < 3; i++) {
  (function (j) {
    setTimeout(() => console.log(j), 0);
  })(i);
}
// 0 1 2   (j is a fresh parameter on each call, capturing the current value)
```

### Fix 3: pass an argument to `setTimeout`
```js
for (var i = 0; i < 3; i++) setTimeout((n) => console.log(n), 0, i);
```

### Fix 4: `bind` or a helper
```js
for (var i = 0; i < 3; i++) setTimeout(console.log.bind(console, i), 0);
```

### Related event-loop rule
Order of execution: synchronous code, then **microtasks** (Promise `.then`, `await` continuations), then **macrotasks** (`setTimeout`, I/O).
```js
console.log("A");
setTimeout(() => console.log("B"), 0);
Promise.resolve().then(() => console.log("C"));
console.log("D");
// A D C B
```

---

## 9. Rapid-fire interview Q&A

1. **Is JavaScript single-threaded?** Yes, one call stack; concurrency comes from the event loop with Web/Node APIs.
2. **What is hoisting?** Declarations are registered at scope creation; `var` is initialized to `undefined`, `let`/`const` are in the TDZ, function declarations are fully available.
3. **What is lexical scope?** Scope is determined by where code is written; inner functions can see outer variables.
4. **Pass by value or reference?** Primitives are copied by value; objects are passed as a copy of the reference.
5. **`null` vs `undefined`?** `undefined` is "not assigned"; `null` is an intentional "no value".
6. **What does `new` do?** Creates an object, links its prototype, calls the function with `this` set to it, returns it (unless the function returns another object).
7. **What is a pure function?** Same input gives same output, no side effects (no mutation of outside state).
8. **Arrow vs regular function?** Arrow: lexical `this`, no `arguments`, cannot be used with `new`.
9. **What is a Promise?** An object representing a future result: pending, fulfilled, or rejected.
10. **`Promise.all` vs `allSettled`?** `all` rejects on the first failure; `allSettled` waits for all and reports each outcome.



---

## 10. Practice exercises (answers at the bottom)

**Q1.** What is printed?
```js
console.log(a); var a = 1;
console.log(b); let b = 2;
```

**Q2.** Write `counter()` with `increment`, `decrement`, `get`, where two counters do not interfere.

**Q3.** What is printed?
```js
const o = { n: "o", f() { return () => this.n; } };
const g = o.f();
console.log(g());
```

**Q4.** Predict each:
```js
[] == ![];
null == 0;
null >= 0;
"b" + "a" + +"a" + "a";
```

**Q5.** Given `users = [{name:"A",age:20},{name:"B",age:30},{name:"C",age:20}]`, return an object grouping names by age: `{ 20: ["A","C"], 30: ["B"] }`.

**Q6.** Why does this print `[1, 10, 9]`, and how do you fix it?
```js
console.log([10, 9, 1].sort());
```

**Q7.** What is the output, and why?
```js
const a = { x: 1, nested: { y: 2 } };
const b = { ...a };
b.nested.y = 99;
console.log(a.nested.y);
```

**Q8.** Your project has `"type": "module"`. Convert `const fs = require("fs")` and `module.exports = { run }` to ESM.

**Q9.** Fix with two different methods:
```js
for (var i = 0; i < 3; i++) setTimeout(() => console.log(i), 100);
```

### Answers

**A1.** First `console.log(a)` prints `undefined`. Then `console.log(b)` throws `ReferenceError` (TDZ).

**A2.**
```js
function counter() {
  let n = 0;
  return {
    increment: () => ++n,
    decrement: () => --n,
    get: () => n,
  };
}
```

**A3.** `"o"`. `f` is called as `o.f()`, so `this` is `o`; the arrow inside captures that `this`.

**A4.**
- `[] == ![]` is `true` (`![]` is `false`, then `[] == false` as in section 4.2).
- `null == 0` is `false` (null only loosely equals undefined).
- `null >= 0` is `true` (relational operators convert null to 0; `==` has its own rule).
- `"b" + "a" + +"a" + "a"` is `"baNaNa"` (`+"a"` is `NaN`).

**A5.**
```js
const byAge = users.reduce((acc, u) => {
  (acc[u.age] ??= []).push(u.name);
  return acc;
}, {});
```

**A6.** Default `sort()` compares elements as strings (`"10" < "9"`). Fix: `[10, 9, 1].sort((a, b) => a - b)` gives `[1, 9, 10]`.

**A7.** `99`. Spread copies only the top level, so `b.nested` and `a.nested` point to the same object. Use `structuredClone(a)` for a deep copy.

**A8.**
```js
import fs from "node:fs";
export { run };           // or: export function run() { ... }
```

**A9.** Method 1: `for (let i = 0; i < 3; i++) setTimeout(() => console.log(i), 100);`
Method 2: `for (var i = 0; i < 3; i++) setTimeout((n) => console.log(n), 100, i);`
Both print `0 1 2`.

---

## Study plan (suggested)

| Day | Focus | Output |
|---|---|---|
| 1 | Sections 1-2 | Re-write counter, once, debounce from memory |
| 2 | Section 3 | Predict `this` in 10 snippets, then run them |
| 3 | Section 4 | Learn the falsy list; solve coercion puzzles |
| 4 | Section 5 | Do 10 `reduce` tasks (group, count, sum, flatten) |
| 5 | Sections 6-7 | Convert a small project between CJS and ESM |
| 6 | Sections 8-10 | Retake all practice questions without notes |

Rule of thumb for interviews: **say the rule first, then show a 3-line example, then mention the pitfall.** That structure sounds senior even from a fresher.
