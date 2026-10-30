# SM Auto Interview Weakness Study Guide

*Async JavaScript, Async Python, FastAPI, React and Practical Full-Stack Engineering, for a fresher who wants to reason through problems independently.*

---

## How to use this guide

- **Read for understanding, then close the file and re-explain aloud.** An interview answer you can only recognize is not yet an answer you can give.
- **Attempt every exercise before opening the answer key** (Part 9).
- **Run the code.** Every example is meant to run. Where something cannot run as-is (for example it needs a real server), the text says so.
- Code was written for **Node.js 18+** (Node 20/22 recommended; `fetch`, `AbortController`, `AbortSignal.timeout`) and **Python 3.11+** (3.12 recommended). Version-specific features are flagged with the minimum version.

### Labels used throughout

| Label | Meaning |
|---|---|
| **[Confirmed]** | Something you told me happened or that you explicitly struggled with. |
| **[Inferred]** | A realistic practice scenario I constructed. It is *not* a claim that SM Auto asked it. |
| **[Assess]** | A topic not yet shown to be a weakness. Test yourself before spending time. |

### Contents

1. Interview Performance Analysis
2. JavaScript Fundamentals and Asynchronous Programming
3. Python Asynchronous Programming
4. FastAPI Practical Handbook
5. React and Frontend Integration
6. Scenario-Based Interview Questions (32)
7. Full-Stack Mini Project
8. Private Deployment and Working in a Small IT Team
9. Practice Exercises and Answer Key (20)
10. Mock Interview Bank
11. Revision Notes and Progress Tracker
12. **Start Here: First 3 Study Sessions**

---

# Part 1: Interview Performance Analysis

## 1.1 Interview context (as you described it)

- **Company:** SM Auto, an automotive component manufacturer with a small internal IT/software team.
- **What the team builds:** internal applications for company management and business operations.
- **Deployment:** possibly local servers, private servers, or a private network, because some data is sensitive.
- **Stack mentioned:** JavaScript, Python (especially FastAPI), React, `async/await`, Promises, synchronous versus asynchronous API behavior.
- **Question style:** scenario-based, not purely theoretical.
- **Team expectations:** full lifecycle ownership (plan, build, test, deploy, debug, maintain); adaptable in a small team; minimal reliance on AI tools because of privacy and policy.
- **Your own assessment:** you could talk about your resume and projects, but struggled with several JavaScript and Python questions. Performance felt average.

This guide does not speculate about how the interviewers judged you or whether you will be selected. It only uses what you reported to decide what to study.

## 1.2 Classification of topics

### Confirmed weaknesses [Confirmed]
You said these were areas you struggled with, so they get the most time:

| Area | Specific gaps to close |
|---|---|
| JavaScript async | Promise states, `async/await`, event loop, `Promise.all` vs `allSettled`, partial failure, timeouts |
| Python async | `asyncio`, `gather(return_exceptions=True)`, blocking vs non-blocking, timeouts, tasks |
| Multi-API failure handling | The three-API question: one fails, two succeed |

### Likely knowledge gaps [Inferred]
Your brief lists these as things you want to improve, and the interview style (scenario-based, FastAPI-heavy) makes them probable gaps. They are hypotheses, not verdicts:

- FastAPI `def` versus `async def`, and what blocks the event loop.
- Calling external APIs from FastAPI with `httpx.AsyncClient`, per-service timeouts.
- React effects: fetch cancellation, stale responses, partial-result UI.
- Debugging an integration problem without AI assistance.

### Areas not yet assessed [Assess]
Do a quick self-test (Part 11 checklist) before investing time:

- JavaScript core: closures, `this`, prototypes, array methods, destructuring.
- Pydantic validation and dependency injection depth.
- CORS and authentication basics.
- Logging, backups, monitoring on a private server.
- Docker for deployment.
- Your Java/Spring Boot strengths: these probably transfer well (see 1.4).

## 1.3 What a small internal IT team might expect from a fresher

This is general reasoning about small internal teams, not knowledge of SM Auto's policies.

1. **End-to-end ownership.** With three to six engineers, no one else will write the frontend for your API. Expect to read a requirement, ask questions, design a table and endpoint, build the screen, test, deploy, and answer the phone when it breaks.
2. **Judgment over trivia.** Internal tools are judged by whether the shop-floor or office users can finish their work. Scenario questions test whether you protect that outcome (for example, showing partial data with a warning instead of a blank page).
3. **Debugging without a crutch.** If AI use is discouraged, they want to see a method: reproduce, narrow, read logs, form hypotheses, test one change at a time.
4. **Data sensitivity awareness.** Do not paste internal data into external tools, do not put secrets in code, understand who can access what.
5. **Communication.** Short status updates, asking clarifying questions early, writing down decisions.
6. **Ability to learn a stack quickly.** You are strong in Java/Spring Boot. The signal they want is that you can transfer concepts (thread pools to the event loop, `@Async`/`CompletableFuture` to Promises, Bean Validation to Pydantic) rather than start from zero.

## 1.4 Using your Java knowledge as a bridge

| You know (Java/Spring) | Equivalent idea |
|---|---|
| `CompletableFuture.allOf(...)` | `Promise.all` / `asyncio.gather` |
| `CompletableFuture.handle()/exceptionally()` | `Promise.allSettled` / `gather(return_exceptions=True)` |
| `Future.get(timeout)` | `AbortSignal.timeout`, `asyncio.timeout` / `wait_for` |
| `@RestController` + `@RequestBody` + Bean Validation | FastAPI route + Pydantic model |
| `@Autowired` / constructor injection | FastAPI `Depends(...)` |
| `RestTemplate` / `WebClient` | `fetch` / `httpx.AsyncClient` |
| Servlet filter / `HandlerInterceptor` | FastAPI middleware |
| `application.properties` + env vars | `.env` + `pydantic-settings` |
| Tomcat thread-per-request (blocking) | Event loop (single thread, cooperative) |
| `@CrossOrigin` / `WebMvcConfigurer` CORS | FastAPI `CORSMiddleware` |

**Big conceptual shift:** in Spring MVC each request typically gets its own thread, so blocking a thread only hurts that request (until the pool is exhausted). In Node and in Python `asyncio`, **one thread runs the event loop for everybody**, so blocking it stalls every request. This difference explains many of the interview questions.

## 1.5 Prioritized learning checklist

Work top to bottom. Items 1 to 4 are the highest value.

- [ ] 1. Explain Promise states and the event loop execution order, with a trace.
- [ ] 2. Explain and code `Promise.all` vs `allSettled` including the three-API scenario.
- [ ] 3. Explain and code `asyncio.gather` with and without `return_exceptions=True`.
- [ ] 4. Explain `def` vs `async def` in FastAPI and what blocks the loop.
- [ ] 5. Implement per-service timeouts in both languages and explain cancellation.
- [ ] 6. Build the FastAPI aggregator (Part 4) and test it.
- [ ] 7. Build the React consumer with loading/partial/error states and stale-response protection (Part 5).
- [ ] 8. Practice 10 scenario answers aloud (Part 6).
- [ ] 9. Do the mini project (Part 7) end to end.
- [ ] 10. Learn the private deployment checklist (Part 8).

---

# Part 2: JavaScript Fundamentals and Asynchronous Programming

> **Version notes:** `AbortSignal.timeout()` needs Node 17.3+ / modern browsers. `AbortSignal.any()` needs Node 20.3+ / recent browsers. `Promise.any` needs Node 15+. Everything else is older. Save examples as `.js` files and run with `node file.js`.

## 2.1 Synchronous vs asynchronous execution and the call stack

**Simple definition.** *Synchronous* code runs line by line, and each line finishes before the next starts. *Asynchronous* code starts an operation (a timer, a network request) and lets the program continue; the result arrives later and is handled by a callback or Promise.

**Why it matters.** JavaScript has **one main thread**. If you do slow synchronous work (a huge loop, `JSON.parse` of a giant file), nothing else runs: no clicks, no timers, no other requests (in Node). Waiting for I/O must therefore be asynchronous.

**How it works.** The **call stack** holds the functions currently executing. When a function calls another, it is pushed; when it returns, it is popped. Async operations are handed to the runtime (browser Web APIs, or libuv in Node), which does the waiting outside the stack, then queues a callback to run when the stack is empty.

```js
function c() { console.log('c'); }
function b() { c(); console.log('b'); }
function a() { b(); console.log('a'); }
a();
// Output: c, b, a   (stack: a -> b -> c, then unwinds)
```

**Blocking example (do not do this on a server):**

```js
const start = Date.now();
setTimeout(() => console.log('timer fired after', Date.now() - start, 'ms'), 100);
while (Date.now() - start < 1000) {}   // busy loop blocks everything for 1 second
// The 100 ms timer fires only after ~1000 ms because the stack was never empty.
```

**Common mistakes**
- Thinking `setTimeout(fn, 0)` runs immediately. It runs after the current stack and all microtasks.
- Thinking `async` functions run on another thread. They do not; they just pause and resume on the same thread.

**Interview Q:** *"Is JavaScript single-threaded? Then how does it handle many requests?"*
**Strong answer:** "The JavaScript code runs on one thread, but I/O is delegated to the runtime, which uses the OS and, in Node, a thread pool for some operations. When I/O completes, a callback is queued onto the event loop. So one thread can juggle thousands of waiting connections as long as no single callback does heavy CPU work."

**Tricky follow-ups:** What happens to a Node server during a 5-second synchronous loop? (Every request waits.) How would you fix CPU-heavy work? (Worker threads, a child process, or a separate service.)

**Exercise 2.1.** Predict the output, then run it:

```js
console.log('A');
setTimeout(() => console.log('B'), 0);
console.log('C');
```
*Solution:* `A, C, B`. `B` is a macrotask that waits for the stack to empty.

---

## 2.2 The event loop: call stack, microtasks, macrotasks

**Simple definition.** The event loop is the mechanism that repeatedly checks "is the call stack empty?" and if so, takes the next queued task and runs it.

**Two queues matter for interviews:**

| Queue | Examples | When it runs |
|---|---|---|
| **Microtask queue** | `.then/.catch/.finally` callbacks, code after `await`, `queueMicrotask` | After the current script/task finishes, **drained completely** before the next macrotask |
| **Macrotask queue** | `setTimeout`, `setInterval`, I/O callbacks, UI events | One per loop turn |

(Node has extra phases such as `setImmediate` and `process.nextTick`, which runs even before promise microtasks. You rarely need these, but know they exist.)

**The rule:** run the current sync code to completion, then run **all** microtasks (including new ones queued while draining), then take **one** macrotask, then repeat.

**Runnable trace:**

```js
console.log('1: sync start');
setTimeout(() => console.log('2: setTimeout 0 (macrotask)'), 0);
Promise.resolve().then(() => console.log('3: promise.then (microtask)'));
queueMicrotask(() => console.log('4: queueMicrotask'));
(async () => {
  console.log('5: inside async fn, before await (still sync)');
  await null;
  console.log('6: after await (microtask)');
})();
console.log('7: sync end');
```

**Actual output (verified):**
```
1: sync start
5: inside async fn, before await (still sync)
7: sync end
3: promise.then (microtask)
4: queueMicrotask
6: after await (microtask)
2: setTimeout 0 (macrotask)
```

**Step-by-step:**
1. `1` prints (sync). The timer is handed to the runtime. Two microtasks are queued.
2. The async function runs **synchronously until its first `await`**, so `5` prints. The rest of it is queued as a microtask.
3. `7` prints. The script ends, the stack is empty.
4. The microtask queue drains in order of queuing: `3`, `4`, `6`.
5. Only now is the timer's macrotask run: `2`.

**Common mistakes**
- Believing code *before* the first `await` is deferred. It is not.
- Believing `setTimeout(..., 0)` beats a resolved Promise. It never does.
- Infinite microtask loops (a `.then` that always queues another) **starve** timers and rendering.

**Interview Q:** *"What is the difference between microtasks and macrotasks?"*
**Strong answer:** "After each macrotask, the engine drains the entire microtask queue before the next macrotask. Promise callbacks and post-`await` code are microtasks, timers and I/O callbacks are macrotasks. That is why a resolved promise's `.then` runs before a `setTimeout(0)`."

**Tricky follow-up:** What does this print?

```js
Promise.resolve().then(() => {
  console.log('a');
  Promise.resolve().then(() => console.log('b'));
});
setTimeout(() => console.log('c'), 0);
```
*Answer:* `a, b, c`. The nested microtask is queued while draining, so it also runs before the timer.

**Exercise 2.2.** Without running, predict the output:

```js
console.log(1);
setTimeout(() => console.log(2), 0);
Promise.resolve().then(() => console.log(3));
(async () => { console.log(4); await Promise.resolve(); console.log(5); })();
console.log(6);
```
*Solution:* `1, 4, 6, 3, 5, 2`. Sync: 1, 4 (async fn runs to its `await`), 6. Microtasks in queue order: `.then` (3), then the async continuation (5). Then the timer (2).

---

## 2.3 Promises and Promise states

**Simple definition.** A Promise is an object representing a value that is not available yet. It is in exactly one of three states:

| State | Meaning | Can change? |
|---|---|---|
| **pending** | Operation in progress | to fulfilled or rejected |
| **fulfilled** | Finished with a value | No (settled) |
| **rejected** | Failed with a reason | No (settled) |

"Settled" means fulfilled or rejected. A settled promise never changes state or value.

**Why it matters.** Promises give you composable, chainable async results with a single error path, instead of nested callbacks.

**How it works.** `.then(onFulfilled, onRejected)` returns a **new** promise. Whatever the handler returns becomes the new promise's value (and if it returns a promise, the chain waits for it). If a handler throws, the new promise rejects. A `.catch(fn)` is shorthand for `.then(undefined, fn)`.

```js
const p = new Promise((resolve, reject) => {
  setTimeout(() => resolve(42), 100);       // executor runs immediately and synchronously
});

p.then((v) => { console.log('got', v); return v * 2; })
 .then((v) => { console.log('doubled', v); throw new Error('boom'); })
 .catch((e) => { console.log('caught', e.message); return 'recovered'; })
 .finally(() => console.log('cleanup (no argument, value passes through)'))
 .then((v) => console.log('after catch:', v));
// got 42 / doubled 84 / caught boom / cleanup ... / after catch: recovered
```

**Common mistakes**
- Forgetting to `return` inside `.then`, so the next step receives `undefined` and does not wait.
- The "promise constructor anti-pattern": wrapping something that already returns a promise in `new Promise(...)`.
- Not handling a rejection. An unhandled rejection crashes Node 15+ by default (`unhandledRejection`).
- Thinking `.catch` only catches errors from the line directly above. It catches rejections from **anything earlier in the chain**.

**Interview Q:** *"What are the states of a Promise, and what does `.then` return?"*
**Strong answer:** "Pending, fulfilled, rejected. Once settled it is immutable. `.then` always returns a new promise, which is how chaining works. If the handler returns a value, the new promise fulfills with it; if the handler throws or returns a rejected promise, it rejects."

**Tricky follow-up:** What is the difference between `.then(a).catch(b)` and `.then(a, b)`? (In the first, `b` also handles errors thrown inside `a`; in the second it does not.)

**Exercise 2.3.** Write `delay(ms, value)` returning a promise that fulfills with `value` after `ms`.
```js
const delay = (ms, value) => new Promise((resolve) => setTimeout(() => resolve(value), ms));
delay(100, 'hi').then(console.log);
```

---

## 2.4 `async` and `await`

**Simple definition.** `async` marks a function that always returns a Promise. `await` pauses *that function* until a promise settles, returning its value or throwing its rejection. It is syntax over promises, not a different mechanism.

**Why it matters.** It lets async code read like synchronous code and use normal `try/catch`.

**How it works.** At `await`, the function is suspended and the thread is free to run other things. When the promise settles, the continuation is queued as a microtask.

```js
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function getUser() {
  await sleep(200);
  return { id: 1, name: 'Asha' };
}

async function main() {
  try {
    const user = await getUser();
    console.log(user);
  } catch (err) {
    console.error('failed', err);
  }
}
main();
```

### The most common performance bug: accidental sequential awaits

```js
// SLOW: ~600 ms. Each call waits for the previous one, but they are independent.
const a = await callA();   // 200 ms
const b = await callB();   // 200 ms
const c = await callC();   // 200 ms

// FAST: ~200 ms. Start all three, then wait.
const [a2, b2, c2] = await Promise.all([callA(), callB(), callC()]);
```



Another trap: `await` inside `forEach` does not wait.
```js
items.forEach(async (item) => { await save(item); });   // forEach ignores the returned promises
console.log('done?');  // runs BEFORE the saves finish

// Correct, sequential:
for (const item of items) await save(item);
// Correct, concurrent:
await Promise.all(items.map(save));
```

**Common mistakes**
- Sequential awaits for independent work.
- `await` in `forEach`.
- Forgetting `await` (you get a pending Promise object instead of data, and errors escape the `try/catch`).
- `return promise` vs `return await promise` inside `try/catch`: without `await`, a rejection bypasses the local `catch`.
- Using `await` at the top level of a CommonJS file (allowed in ES modules).

**Interview Q:** *"Does `async/await` make code run in parallel?"*
**Strong answer:** "No. It makes waiting non-blocking, but one `await` after another is sequential. To run independent operations concurrently I start the promises first, then await them together with `Promise.all` or `allSettled`. Also JavaScript is still single-threaded; concurrency here means overlapping *waiting*, not parallel computation."

**Exercise 2.4.** This code takes ~900 ms. Make it ~300 ms.
```js
async function load() {
  const u = await fakeFetch('users', 300);
  const o = await fakeFetch('orders', 300);
  const s = await fakeFetch('stock', 300);
  return { u, o, s };
}
```
*Solution:*
```js
async function load() {
  const [u, o, s] = await Promise.all([
    fakeFetch('users', 300), fakeFetch('orders', 300), fakeFetch('stock', 300),
  ]);
  return { u, o, s };
}
```
(Caveat to say aloud: this is only valid if the calls do not depend on each other.)

---

## 2.5 Promise combinators: `all`, `allSettled`, `race`, `any`

| Method | Resolves when | Rejects when | Result shape | Typical use |
|---|---|---|---|---|
| `Promise.all` | **all** fulfill | **first** rejection (immediately) | array of values, in input order | All pieces essential; fail fast |
| `Promise.allSettled` | **all** settle (never rejects) | never | array of `{status, value\|reason}` | Independent pieces; partial success |
| `Promise.race` | **first** settles (either way) | first settles with rejection | that one result | Timeout pattern, first response |
| `Promise.any` | **first fulfills** | **all** reject (`AggregateError`) | one value | Redundant mirrors/fallback servers |

**Critical details interviewers probe:**
- `Promise.all` rejecting does **not cancel** the other operations. They keep running; you simply stop waiting and ignore their results.
- Results of `all`/`allSettled` are in **input order**, not completion order.
- `Promise.race` with a never-resolving timeout leaves a timer alive unless you clear it.

**Runnable: `all` vs `allSettled` on the three-API scenario [Confirmed topic]**

```js
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function fakeApi(name, ms, fail = false) {
  await sleep(ms);
  if (fail) throw new Error(`${name} failed`);
  return { name, data: `${name}-data` };
}

async function withAll() {
  try {
    const r = await Promise.all([
      fakeApi('users', 100), fakeApi('orders', 150, true), fakeApi('stock', 200),
    ]);
    console.log('all ok', r);
  } catch (e) {
    console.log('Promise.all rejected:', e.message);   // the two good results are lost
  }
}

async function withSettled() {
  const names = ['users', 'orders', 'stock'];
  const results = await Promise.allSettled([
    fakeApi('users', 100), fakeApi('orders', 150, true), fakeApi('stock', 200),
  ]);
  const out = { data: {}, errors: {} };
  results.forEach((r, i) => {
    if (r.status === 'fulfilled') out.data[names[i]] = r.value;
    else out.errors[names[i]] = r.reason.message;
  });
  console.log(JSON.stringify(out));
}

(async () => { await withAll(); await withSettled(); })();
```

**Verified output:**
```
Promise.all rejected: orders failed
{"data":{"users":{"name":"users","data":"users-data"},"stock":{"name":"stock","data":"stock-data"}},"errors":{"orders":"orders failed"}}
```

**Line-by-line notes**
- `fakeApi` simulates a service: waits, then either throws or returns.
- In `withAll`, the first rejection (`orders` at 150 ms) jumps straight to `catch`. The `users` result is discarded even though it succeeded. `stock` is still running in the background.
- In `withSettled`, we wait for **all three**, then inspect each outcome by `status`. We rely on **input order** to map results back to names (`names[i]`).

**Other combinators in action**

```js
// race: first one wins, good or bad
Promise.race([delay(100, 'slow'), delay(50, 'fast')]).then(console.log);   // 'fast'

// any: first SUCCESS wins; failures are ignored until all fail
Promise.any([Promise.reject(new Error('a')), delay(80, 'mirror-b')]).then(console.log); // 'mirror-b'
Promise.any([Promise.reject(new Error('a')), Promise.reject(new Error('b'))])
  .catch((e) => console.log(e.constructor.name, e.errors.length));   // AggregateError 2
```

**Common mistakes**
- Using `Promise.all` when partial results are acceptable, so one flaky widget blanks the page.
- Using `allSettled` when all pieces are essential, then forgetting to check for failures and rendering a half-broken screen as if it were fine.
- Assuming `race` ignores rejections (it does not; use `any` for "first success").

**Interview Q:** *"When would you use `Promise.all` versus `Promise.allSettled`?"*
**Strong answer:** "`all` when the result is only meaningful if every part succeeded, for example computing an invoice total from three required sources. It fails fast, which also saves waiting. `allSettled` when the parts are independent and a partial result is useful, like a dashboard where one widget failing should not hide the others. With `allSettled` I must check each result's status and surface which parts are missing, so the UI does not mislead the user."

**Tricky follow-ups**
- "Does `Promise.all` cancel the other requests?" (No; you need `AbortController`.)
- "What does `Promise.all([])` return?" (A promise fulfilling with `[]`.)
- "What does `Promise.race([])` do?" (Stays pending forever.)

**Exercise 2.5.** Implement `firstSuccess(promises)` without using `Promise.any`.
```js
function firstSuccess(promises) {
  return new Promise((resolve, reject) => {
    const errors = [];
    let rejected = 0;
    promises.forEach((p, i) =>
      Promise.resolve(p).then(resolve, (e) => {
        errors[i] = e;
        if (++rejected === promises.length) reject(new AggregateError(errors, 'All failed'));
      })
    );
  });
}
```
*Explanation:* `resolve` can be called by whichever promise succeeds first; later calls are ignored because a promise settles once. We reject only when every input has rejected.

---

## 2.6 Error handling and `fetch()` HTTP errors

**Key fact:** `fetch()` only **rejects on network-level failures** (DNS failure, connection refused, abort). A **404 or 500 response is a *fulfilled* promise** with `res.ok === false`. Forgetting this is one of the most common real bugs.

```js
async function getJson(url, { timeoutMs = 3000 } = {}) {
  const res = await fetch(url, { signal: AbortSignal.timeout(timeoutMs) });
  if (!res.ok) throw new Error(`HTTP ${res.status} for ${url}`);   // convert HTTP errors to exceptions
  return res.json();   // may also throw if the body is not valid JSON
}
```

**Verified behavior (against a local test server):** a good URL returns `{ ok: true }`; a URL returning 500 throws `HTTP 500 for ...`.

**Distinguish error types in a `catch`:**

```js
try {
  const data = await getJson('http://localhost:3000/api/orders');
} catch (err) {
  if (err.name === 'TimeoutError') { /* took too long */ }
  else if (err.name === 'AbortError') { /* cancelled by us */ }
  else if (err instanceof TypeError) { /* typically network failure in fetch */ }
  else { /* HTTP error we threw, or JSON parse error */ }
}
```

**`try/catch` vs `.catch()`:** equivalent for promises. Use `try/catch` inside `async` functions; use `.catch` when chaining. Avoid mixing both in the same flow without a reason. Never swallow errors silently (`catch (e) {}`); at least log with context.

**Interview Q:** *"I call `fetch` and the server returns 404; does my `catch` run?"*
**Strong answer:** "No. `fetch` resolves for any HTTP response. I have to check `res.ok` or `res.status` and throw myself. The promise rejects only for network failures or aborts."

**Exercise 2.6.** Write `fetchOrFallback(url, fallback)` that returns `fallback` on any failure and logs the reason.
```js
async function fetchOrFallback(url, fallback, timeoutMs = 2000) {
  try {
    const res = await fetch(url, { signal: AbortSignal.timeout(timeoutMs) });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return await res.json();   // `await` here so JSON errors are caught by this try/catch
  } catch (e) {
    console.error(`fetchOrFallback(${url}) failed: ${e.name}: ${e.message}`);
    return fallback;
  }
}
```
*Note:* `return await` inside `try` is deliberate. Without `await`, a rejection from `res.json()` would bypass the `catch`.

---

## 2.7 Timeouts, cancellation, retries

### Timeouts and cancellation with `AbortController`

A timeout that only stops *waiting* leaves the request running. Real cancellation uses an **AbortSignal**, which `fetch` honors by aborting the underlying request.

```js
// Manual control (works in older runtimes too)
const controller = new AbortController();
const timer = setTimeout(() => controller.abort(), 2000);
try {
  const res = await fetch(url, { signal: controller.signal });
  /* ... */
} finally {
  clearTimeout(timer);   // always clear, or the timer keeps the process alive
}

// Modern shorthand: signal fires after 2s with a TimeoutError
await fetch(url, { signal: AbortSignal.timeout(2000) });

// Combine a user-cancel signal and a timeout (Node 20.3+)
const signal = AbortSignal.any([userSignal, AbortSignal.timeout(2000)]);
```

### Racing a timeout (when you cannot pass a signal)

```js
function withTimeout(promise, ms, label = 'operation') {
  let timer;
  const timeout = new Promise((_, reject) => {
    timer = setTimeout(() => reject(new Error(`${label} timed out after ${ms}ms`)), ms);
  });
  return Promise.race([promise, timeout]).finally(() => clearTimeout(timer));
}
```
**Limitation to state in interviews:** this only stops *waiting*. The underlying work continues in the background (and can still cause side effects, such as a payment being created). Prefer `AbortSignal` when the API supports it.

### Retries: only with care

```js
async function retry(fn, { retries = 2, baseMs = 100 } = {}) {
  let lastErr;
  for (let attempt = 0; attempt <= retries; attempt++) {
    try {
      return await fn(attempt);
    } catch (e) {
      lastErr = e;
      if (attempt === retries) break;
      const delay = baseMs * 2 ** attempt + Math.random() * 50;   // exponential backoff + jitter
      await new Promise((r) => setTimeout(r, delay));
    }
  }
  throw lastErr;
}
```
**Verified:** a function failing twice then succeeding returns `ok after 3`.

**Retry rules you must be able to state:**
1. **Idempotency.** Retry GETs freely. Retrying a POST that creates an order can create duplicates unless the server supports an idempotency key.
2. **Retry only retryable failures:** network errors, timeouts, 502/503/504, maybe 429 (respecting `Retry-After`). **Never** retry 400/401/403/404/422: it will not help.
3. **Limits and backoff.** Cap attempts; add exponential backoff with jitter; otherwise you create a retry storm against a service that is already struggling.
4. **Total deadline.** Retries must fit inside the overall time budget (for a 2-second endpoint you usually cannot afford any).

**Interview Q:** *"Your fetch timed out. Should you retry?"*
**Strong answer:** "It depends. If the request is idempotent and I still have time budget, one retry with a short backoff might help. If it is a non-idempotent write, a timeout does not tell me whether the server processed it, so retrying blindly could duplicate it; I would need an idempotency key or a status check. And if the whole endpoint has a 2-second budget, I would rather return a partial result than retry."

**Exercise 2.7.** Write `fetchWithRetry(url)` that retries only network errors and 5xx, up to 2 retries.
```js
async function fetchWithRetry(url, { retries = 2, timeoutMs = 1500 } = {}) {
  for (let attempt = 0; ; attempt++) {
    try {
      const res = await fetch(url, { signal: AbortSignal.timeout(timeoutMs) });
      if (res.status >= 500 && attempt < retries) throw new Error(`HTTP ${res.status}`);  // retryable
      if (!res.ok) throw Object.assign(new Error(`HTTP ${res.status}`), { fatal: true });  // 4xx or final 5xx
      return await res.json();
    } catch (e) {
      if (e.fatal || attempt >= retries) throw e;
      await new Promise((r) => setTimeout(r, 100 * 2 ** attempt));
    }
  }
}
```

---

## 2.8 The three-API scenario in JavaScript (complete treatment) [Confirmed]

> **The question you remember:** "If you make asynchronous calls to three APIs and one fails while the other two succeed, how would you handle it?"

### A structured way to answer (say this aloud)

1. **Clarify the requirement.** "Are all three required for the feature to make sense, or can the page work with two?"
2. **Make the calls concurrently**, since they are independent.
3. **Pick the combinator by the requirement:** `allSettled` if partial is acceptable, `all` if all are essential.
4. **Add timeouts**, so a slow API cannot hold the response hostage.
5. **Report honestly:** return which parts succeeded, which failed and why, so the caller or UI can show a warning.
6. **Log** the failure with enough context (service, status, duration) for debugging.

### Complete runnable aggregator with deadline

Requirement: respond within about 2 seconds; one API takes 10 seconds, two take 200 ms.


Production grade api aggregartor 
```js
// deadline.js  (Node 20.3+ because of AbortSignal.any)
function abortableSleep(ms, signal) {
  return new Promise((resolve, reject) => {
    if (signal?.aborted) return reject(signal.reason);
    const t = setTimeout(resolve, ms);
    signal?.addEventListener('abort', () => { clearTimeout(t); reject(signal.reason); }, { once: true });
  });
}

// Simulated service. A real one would call fetch(url, { signal }).
async function fakeService(name, ms, { fail = false, signal } = {}) {
  await abortableSleep(ms, signal);
  if (fail) throw new Error(`${name} returned 500`);
  return { service: name, value: name.toUpperCase() };
}

async function aggregate({ perServiceMs = 2000, overallMs = 2000 } = {}) {
  const overall = AbortSignal.timeout(overallMs);          // one deadline for the whole request
  const calls = {
    users:  (s) => fakeService('users', 200, { signal: s }),
    orders: (s) => fakeService('orders', 10000, { signal: s }),   // very slow
    stock:  (s) => fakeService('stock', 200, { signal: s }),
  };
  const entries = Object.entries(calls);

  const settled = await Promise.allSettled(
    entries.map(([, fn]) =>
      fn(AbortSignal.any([overall, AbortSignal.timeout(perServiceMs)]))   // whichever fires first
    )
  );

  const data = {}, errors = {};
  settled.forEach((r, i) => {
    const name = entries[i][0];
    if (r.status === 'fulfilled') data[name] = r.value;
    else errors[name] = r.reason?.name === 'TimeoutError' ? 'timeout' : r.reason.message;
  });
  return { data, errors, partial: Object.keys(errors).length > 0 };
}

const t0 = Date.now();
aggregate({ perServiceMs: 1500, overallMs: 2000 }).then((r) => {
  console.log(JSON.stringify(r), 'elapsed ms ~', Math.round((Date.now() - t0) / 100) * 100);
});
```

**Verified output:**
```
{"data":{"users":{...},"stock":{...}},"errors":{"orders":"timeout"},"partial":true} elapsed ms ~ 1500
```

**Explanation of the important lines**
- `AbortSignal.timeout(overallMs)` creates a signal that aborts with a `TimeoutError` after the budget.
- `AbortSignal.any([...])` merges the overall deadline with the per-service limit, so each call stops at whichever is earlier.
- `abortableSleep` stands in for a real network call. With real `fetch`, passing `signal` makes the browser/Node **actually cancel the request** and free its socket. That is the answer to "what happens to unfinished tasks?"
- `allSettled` never rejects, so one failure cannot break the aggregate.
- We map results back to names via the entries array, relying on input order.

### The extended questions, answered

| Question | Answer |
|---|---|
| One API takes 10 s, two take 200 ms. | Without a timeout, `allSettled` waits 10 s; `all` would also wait (unless another fails). Add per-service timeouts so the slow one is abandoned at, say, 1.5 s. |
| Endpoint must respond within 2 s. | Use a total deadline smaller than 2 s (leave headroom for your own processing and network, e.g. 1.5 s for downstream calls), then respond with whatever finished. |
| How to implement timeouts? | `AbortSignal.timeout` per call (preferred, truly cancels), or `Promise.race` with a timer (only stops waiting). |
| How to return partial results? | `allSettled`, then build `{ data, errors, partial: true }`. Optionally use HTTP 200 with a `warnings` field, or 207-style semantics if your team uses them; document the contract. |
| What happens to unfinished tasks? | `Promise.all` rejecting and `Promise.race` winning do **not** stop the others. Only abort signals do. Un-cancelled requests waste sockets and server load, and can still cause side effects. |
| Cancellation and cleanup? | Pass signals into every call; clear timers in `finally`; on the server, abort downstream calls if the client disconnects. |
| When fail completely instead of partial? | When the data is only meaningful as a whole (financial totals, a transaction that needs all inputs), when stale or missing data could cause a wrong business decision, or when the missing part is the primary thing the user asked for. Return an error (for example 502/503/504) with a clear message. |
| What if all three are essential? | Use `Promise.all` (fail fast) plus abort signals so the remaining requests are cancelled when one fails, and return a clear failure. Optionally retry idempotent calls within the time budget. |

### Fail-fast with cancellation when all three are essential

```js
async function allEssential(callFns, overallMs = 2000) {
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(new Error('overall deadline')), overallMs);
  try {
    return await Promise.all(callFns.map((fn) => fn(controller.signal)));
  } catch (e) {
    controller.abort(e);   // cancel the siblings that are still running
    throw e;
  } finally {
    clearTimeout(timer);
  }
}
```
*Explanation:* if any call rejects, `Promise.all` rejects, we abort the shared controller so sibling requests are cancelled instead of left running, then rethrow so the caller sees the failure.

**Common weak answers and why**
- "Use try/catch." Does not address concurrency or partial results.
- "Use `Promise.all`." Loses the two successful results and does not cancel anything.
- "Retry until it works." No limit, no idempotency thinking, no deadline.

**Exercise 2.8.** Extend `aggregate` to retry a failed *idempotent* service once if time remains. (Hint: check `overall.aborted` before retrying; do not retry on `TimeoutError` caused by the overall signal.)

---

## 2.9 Other JavaScript fundamentals worth a quick self-test [Assess]

You did not say these were weak. Spend five minutes each; if you stumble, deepen them.

| Topic | One-line must-know | Quick check |
|---|---|---|
| `var/let/const` | `var` is function-scoped and hoisted; `let/const` are block-scoped with a temporal dead zone | What does `for (var i=0;i<3;i++) setTimeout(()=>console.log(i))` print? (`3 3 3`; use `let` to get `0 1 2`) |
| Closures | A function remembers variables from where it was created | Write a `counter()` factory |
| `this` | Determined by *how* a function is called; arrow functions capture `this` lexically | Why does `setTimeout(obj.method, 0)` lose `this`? |
| Equality | `===` avoids coercion | `[] == false`? (`true`; avoid `==`) |
| Array methods | `map/filter/reduce/find/some/every` return new values, do not mutate (except `sort`, `reverse`, `splice`, `push`) | Group orders by status with `reduce` |
| Destructuring/spread | Copy and extract concisely; spread is a **shallow** copy | Why is `{...obj}` not a deep clone? |
| Modules | `import/export` (ESM) vs `require` (CommonJS) | Which one needs `"type": "module"`? |

**Interview Q (closure + async trap):** *"What does this print and why?"*
```js
for (var i = 0; i < 3; i++) setTimeout(() => console.log(i), 0);
```
**Answer:** `3 3 3`. `var` has one shared binding; by the time the timers run the loop has finished with `i === 3`. With `let`, each iteration gets its own binding, giving `0 1 2`.

---

# Part 3: Python Asynchronous Programming

> **Version notes:** `asyncio.run` needs 3.7+. `asyncio.to_thread` needs **3.9+**. `asyncio.timeout()`, `asyncio.TaskGroup` and `except*` need **3.11+**. `asyncio.wait_for` works on all versions. This guide assumes **Python 3.11+** (3.12 was used to verify the examples). Run examples with `python file.py`. Everything uses only the standard library in this Part.

## 3.1 Synchronous vs asynchronous Python; coroutines vs tasks

**Simple definition.** Synchronous Python runs one statement after another and blocks while waiting. Asynchronous Python (`asyncio`) runs an **event loop** on one thread; coroutines voluntarily **yield control at `await`**, so other coroutines can run while one waits for I/O.

**Why it matters.** FastAPI, HTTPX and many internal tools are built on this model. Misusing it (blocking the loop) turns a fast service into a slow one for *all* users at once.

**Core vocabulary**

| Term | Meaning |
|---|---|
| **Coroutine function** | `async def f(): ...` |
| **Coroutine object** | What calling `f()` returns. **It has not started running.** |
| **`await`** | Run this coroutine/awaitable and suspend me until it finishes; lets the loop run others meanwhile |
| **Task** | A coroutine **scheduled** on the loop via `asyncio.create_task(coro)`. It starts running soon, concurrently with your code |
| **Event loop** | Runs tasks, switching only at `await` points (cooperative multitasking) |
| **Future** | Low-level "result will appear later" object; Tasks are Futures |

**Coroutine vs task, the key distinction.**
```python
coro = fetch("a", 1)                       # coroutine object, NOTHING is running yet
r = await coro                              # runs it now, and you wait for it

task = asyncio.create_task(fetch("a", 1))   # scheduled NOW; runs concurrently
...do other work...
r = await task                              # collect result later
```
`await a(); await b()` is **sequential**. To overlap them, wrap in tasks or use `gather`.

**Runnable: sequential vs concurrent**

```python
import asyncio, time

async def fetch(name, delay):
    await asyncio.sleep(delay)          # non-blocking wait; lets other tasks run
    return f"{name}-data"

async def sequential():
    t = time.perf_counter()
    a = await fetch("a", 0.2); b = await fetch("b", 0.2); c = await fetch("c", 0.2)
    print("sequential", round(time.perf_counter() - t, 1))      # 0.6

async def concurrent():
    t = time.perf_counter()
    a, b, c = await asyncio.gather(fetch("a", 0.2), fetch("b", 0.2), fetch("c", 0.2))
    print("concurrent", round(time.perf_counter() - t, 1))      # 0.2

asyncio.run(sequential())
asyncio.run(concurrent())
```

**Common mistakes**
- Calling `fetch("a", 1)` **without `await`**: you get a coroutine object and a `RuntimeWarning: coroutine ... was never awaited`.
- Using `await` in a plain `def` (syntax error). `await` is only legal inside `async def`.
- Calling `asyncio.run()` from inside an already running loop (for example inside FastAPI or Jupyter). Use `await` instead.
- `create_task` without keeping a reference: tasks can be garbage-collected mid-flight. Store them in a set/list or use a `TaskGroup`.

**Interview Q:** *"What is the difference between a coroutine and a task?"*
**Strong answer:** "A coroutine is just the object produced by calling an `async def` function; it does nothing until it is awaited or scheduled. A task wraps a coroutine and schedules it on the event loop immediately, so it makes progress concurrently while I do other things. I use tasks, or `gather`, when I want several coroutines to overlap."

**Exercise 3.1.** What is the output and total time?
```python
async def main():
    await asyncio.gather(fetch("a", 1), fetch("b", 1))
```
*Solution:* about 1 second, not 2, because the two waits overlap.

---

## 3.2 Concurrency vs parallelism; I/O-bound vs CPU-bound

| Concept | Meaning | Python tool |
|---|---|---|
| **Concurrency** | Many tasks *in progress* at once, interleaved (often on one thread) | `asyncio` |
| **Parallelism** | Many tasks *literally executing at the same instant* on multiple cores | `multiprocessing`, `ProcessPoolExecutor` (or free-threaded builds, experimental) |

**Why `asyncio` helps only for I/O-bound work.** If a task spends 95% of its time waiting for a network reply, the loop can run other tasks during that wait. If a task spends its time *computing* (parsing big files, image processing, heavy loops), `await` points never come, and the loop is stuck.

**The GIL, briefly.** CPython's Global Interpreter Lock means only one thread executes Python bytecode at a time. Threads still help for **blocking I/O** (the GIL is released while waiting) but not for CPU-bound Python code. For CPU-bound work use **processes**.

| Workload | Best tool |
|---|---|
| Many HTTP calls, DB calls with async driver | `asyncio` |
| A blocking library you cannot change (old SDK, `requests`, file I/O) | `asyncio.to_thread` / threads |
| Heavy computation | `ProcessPoolExecutor` / a separate worker service |

**Interview Q:** *"Will `async def` make my CPU-heavy function faster?"*
**Strong answer:** "No. `async def` only enables cooperative switching at `await`. A CPU-bound function never awaits, so it holds the event loop and blocks every other task. I would move it to a process pool or a background worker."

---

## 3.3 Blocking vs non-blocking: why `async def` alone does not make code asynchronous

An `async def` function that calls a blocking function such as `time.sleep(5)`, `requests.get(...)`, or a heavy loop **freezes the whole event loop** while it runs.

**Runnable demonstration:** a "ticker" prints every 0.1 s while another coroutine runs.

```python
import asyncio, time

async def ticker(t0):
    for _ in range(3):
        print("  tick", round(time.perf_counter() - t0, 1))
        await asyncio.sleep(0.1)

async def bad():
    time.sleep(0.5)            # BLOCKS the event loop (nobody else can run)

async def good():
    await asyncio.sleep(0.5)   # yields; others run meanwhile

async def main():
    for name, fn in (("bad", bad), ("good", good)):
        t0 = time.perf_counter(); print(name)
        await asyncio.gather(ticker(t0), fn())

asyncio.run(main())
```

**Verified output:**
```
bad
  tick 0.0
  tick 0.5      <- the ticker was frozen for 0.5 s
  tick 0.6
good
  tick 0.0
  tick 0.1
  tick 0.2      <- ticks continue on schedule
```

**How to fix blocking code**

| Situation | Fix |
|---|---|
| `time.sleep(n)` | `await asyncio.sleep(n)` |
| `requests.get()` | `httpx.AsyncClient` / `aiohttp` |
| Blocking third-party function | `await asyncio.to_thread(func, *args)` (3.9+) |
| Blocking DB driver | Async driver, or `to_thread` |
| CPU-heavy | `loop.run_in_executor(ProcessPoolExecutor(), ...)` or a separate worker |

```python
import asyncio, time

def blocking_report():           # imagine a legacy library call
    time.sleep(0.1)
    return "report"

async def main():
    result = await asyncio.to_thread(blocking_report)   # runs in a worker thread; loop stays free
    print(result)

asyncio.run(main())
```
**Limitation:** `to_thread` helps because the thread waits on I/O. It does **not** speed up CPU-bound Python (GIL) and threads are limited in number, so do not launch thousands.

**Interview Q:** *"I wrote `async def get_data(): return requests.get(url).json()`. What is wrong?"*
**Strong answer:** "`requests` is blocking. Inside an `async def` it blocks the event loop thread, so no other request can be handled while it waits. I would either use an async client such as `httpx.AsyncClient`, or declare the function with plain `def` so FastAPI runs it in its thread pool, or wrap it with `asyncio.to_thread`."

**Debug tip:** run with `asyncio.run(main(), debug=True)` (or `PYTHONASYNCIODEBUG=1`) and asyncio logs callbacks that block for more than 100 ms.

---

## 3.4 `asyncio.gather` with and without `return_exceptions=True` [Confirmed topic]

**Simple definition.** `gather(*awaitables)` runs them concurrently and returns their results **in the order you passed them**.

**Default behavior (`return_exceptions=False`)**
- The **first exception raised propagates** out of `await gather(...)`.
- The **other awaitables are NOT cancelled**; they keep running in the background (their results/exceptions are not returned to you).
- You lose the results of the ones that succeeded.

**With `return_exceptions=True`**
- `gather` waits for **all** to finish.
- Exceptions are **returned in the result list as values** instead of being raised.
- You must check each item yourself.

```python
import asyncio

class ApiError(Exception):
    pass

async def api(name, delay, fail=False):
    await asyncio.sleep(delay)
    if fail:
        raise ApiError(f"{name} failed")
    return {"service": name, "value": name.upper()}

async def main():
    # 1) default: first exception propagates; good results lost
    try:
        await asyncio.gather(api("users", .1), api("orders", .15, True), api("stock", .2))
    except ApiError as e:
        print("gather (default) raised:", e)

    # 2) return_exceptions=True: get everything, sort it out yourself
    names = ["users", "orders", "stock"]
    results = await asyncio.gather(
        api("users", .1), api("orders", .15, True), api("stock", .2),
        return_exceptions=True,
    )
    data, errors = {}, {}
    for name, r in zip(names, results):              # results are in input order
        if isinstance(r, BaseException):             # how to tell an exception from a value
            errors[name] = str(r)
        else:
            data[name] = r
    print(data, errors)

asyncio.run(main())
```

**Verified output:**
```
gather (default) raised: orders failed
{'users': {'service': 'users', 'value': 'USERS'}, 'stock': {'service': 'stock', 'value': 'STOCK'}} {'orders': 'orders failed'}
```

**Line-by-line notes**
- `isinstance(r, BaseException)` is the check. (Use `BaseException`, not `Exception`: with `return_exceptions=True`, a `CancelledError` can also appear in results, and it is a `BaseException` since Python 3.8.)
- `zip(names, results)` is safe because order is preserved.
- Beware the pitfall: if you forget the check, you may treat an exception object as valid data.

**Which to choose**

| Need | Use |
|---|---|
| All results required; fail fast | default `gather` (plus cancel siblings yourself, or use `TaskGroup`) |
| Independent pieces; partial success | `gather(..., return_exceptions=True)` |
| Structured concurrency; cancel siblings automatically on failure (3.11+) | `asyncio.TaskGroup` |

**Common mistakes**
- Assuming default `gather` cancels the others on failure. It does not.
- Not checking result types when `return_exceptions=True`.
- Not applying any timeout, so one hanging call hangs the entire gather.

**Interview Q:** *"What does `return_exceptions=True` do?"*
**Strong answer:** "It makes `gather` wait for every awaitable and return exceptions as items in the result list rather than raising the first one. That lets me build a partial result: I loop over the results, treat `BaseException` instances as failures and everything else as successes. Without it, the first exception propagates immediately, I lose the successful results, and the other tasks keep running unobserved."

**Tricky follow-up:** *"If two of the three fail, does the default `gather` show you both errors?"* (No, only the first one raised. The second exception is never retrieved by you; with `return_exceptions=True` you get both.)

**Exercise 3.4.** Write `split_results(names, results)` returning `(successes_dict, errors_dict)`.
```python
def split_results(names, results):
    ok, err = {}, {}
    for n, r in zip(names, results):
        (err if isinstance(r, BaseException) else ok)[n] = str(r) if isinstance(r, BaseException) else r
    return ok, err
```

---

## 3.5 Tasks, `wait`, `wait_for`, timeouts, cancellation

### `create_task`
Schedules a coroutine to run concurrently. Keep a reference.

```python
task = asyncio.create_task(fetch("a", 1))
# ... do other work ...
result = await task
```

### `asyncio.wait_for(awaitable, timeout)` (all versions)
Waits up to `timeout` seconds; on timeout it **cancels the inner awaitable** and raises `asyncio.TimeoutError` (an alias of built-in `TimeoutError` from 3.11).

```python
try:
    await asyncio.wait_for(asyncio.sleep(5), timeout=0.1)
except asyncio.TimeoutError:
    print("timed out")
```

### `asyncio.timeout()` (3.11+): preferred for scopes
A context manager that cancels whatever is running inside the block when time runs out, then raises `TimeoutError` at the `async with`. Good for covering multiple awaits with one budget.

```python
async with asyncio.timeout(2.0):
    a = await call_a()
    b = await call_b()      # both share the 2 s budget
```

### `asyncio.wait(tasks, timeout=..., return_when=...)`
Does **not** raise on timeout and does **not** cancel. It returns two sets: `done` and `pending`. You decide what to do with pending tasks (typically cancel them). It needs **Task objects** (not bare coroutines, which is an error in modern Python).
`return_when` can be `ALL_COMPLETED` (default), `FIRST_COMPLETED`, or `FIRST_EXCEPTION`.

### Cancellation
`task.cancel()` requests cancellation: a **`CancelledError` is raised inside the task at its next `await`**. Rules:
1. A task may **catch `CancelledError` to clean up, but must re-raise it**. Swallowing it breaks timeouts and shutdown.
2. `cancel()` is a *request*; await the task (or `gather(..., return_exceptions=True)`) to be sure it actually finished.
3. `CancelledError` derives from `BaseException` since 3.8, so a bare `except Exception` will not catch it (good). A bare `except:` will (bad).

### Comparison

| Tool | On timeout | Cancels the work? | Raises? | Min version |
|---|---|---|---|---|
| `wait_for` | stops waiting | **yes** | `TimeoutError` | any |
| `timeout()` ctx | stops block | **yes** | `TimeoutError` | 3.11 |
| `wait(timeout=)` | returns `(done, pending)` | **no** (you must) | no | any |
| `gather` alone | nothing | no | n/a | any |

**Interview Q:** *"What happens to a task when `wait_for` times out?"*
**Strong answer:** "`wait_for` cancels the inner awaitable by raising `CancelledError` into it at its next await, and then raises `TimeoutError` to me. If the inner code swallows `CancelledError` or runs blocking code, cancellation does not take effect promptly. With `asyncio.wait`, by contrast, nothing is cancelled; I get `done` and `pending` sets and have to cancel pending ones myself."

**Exercise 3.5.** Write `first_n_or_timeout(coros, n, timeout)`.
*(Solution in Part 9, PY-6.)*

---

## 3.6 `TaskGroup` and structured concurrency (3.11+)

`asyncio.TaskGroup` runs tasks together and guarantees none outlive the `async with` block. If any task fails, the **others are cancelled** and the errors are raised together as an `ExceptionGroup`, which you handle with `except*`.

```python
import asyncio

async def ok(n, d): await asyncio.sleep(d); return n
async def bad():    await asyncio.sleep(.1); raise ValueError("boom")
async def slow():
    try:
        await asyncio.sleep(5)
    except asyncio.CancelledError:
        print("slow cancelled by TaskGroup")
        raise

async def main():
    try:
        async with asyncio.TaskGroup() as tg:
            tg.create_task(ok("a", .05))
            tg.create_task(bad())
            tg.create_task(slow())
    except* ValueError as eg:
        print("caught", [str(e) for e in eg.exceptions])

asyncio.run(main())
```

**Verified output:**
```
slow cancelled by TaskGroup
caught ['boom']
```

**When to use:** all pieces essential and you want automatic sibling cancellation. **When not to:** you want partial results; a TaskGroup fails the group on the first error. In that case wrap each coroutine in a function that catches its own exception and returns a result object, or use `gather(return_exceptions=True)`.

---

## 3.7 The three-API scenario in Python (complete treatment) [Confirmed]

> Same question as Part 2.8. Requirement: respond within 2 seconds; one service takes 10 s, two take 200 ms.

### Approach A: `asyncio.wait` with an overall deadline (maximum control)

```python
# aggregate.py   (Python 3.11+)
import asyncio, time

async def svc(name, delay, fail=False):
    try:
        await asyncio.sleep(delay)                   # stands in for an HTTP call
        if fail:
            raise RuntimeError(f"{name} returned 500")
        return name.upper()
    except asyncio.CancelledError:
        print(f"  [{name}] cancelled, cleaning up")  # close connections, release resources here
        raise                                        # ALWAYS re-raise

async def call_with_timeout(name, delay, limit, fail=False):
    try:
        async with asyncio.timeout(limit):           # per-service budget (3.11+)
            return await svc(name, delay, fail)
    except TimeoutError:
        raise TimeoutError(f"{name} timed out after {limit}s") from None

async def aggregate(overall=2.0, per=1.5):
    specs = {"users": (.2, False), "orders": (10, False), "stock": (.2, False)}
    tasks = {n: asyncio.create_task(call_with_timeout(n, d, per, f))
             for n, (d, f) in specs.items()}

    done, pending = await asyncio.wait(tasks.values(), timeout=overall)   # overall deadline
    for p in pending:
        p.cancel()                                   # do not leave stragglers running
    if pending:
        await asyncio.gather(*pending, return_exceptions=True)            # wait for cancellation

    data, errors = {}, {}
    for n, t in tasks.items():
        if t in pending:        errors[n] = "overall deadline"
        elif t.cancelled():     errors[n] = "cancelled"
        elif t.exception():     errors[n] = str(t.exception())
        else:                   data[n] = t.result()
    return {"data": data, "errors": errors, "partial": bool(errors)}

t = time.perf_counter()
print(asyncio.run(aggregate()), round(time.perf_counter() - t, 1))
```

**Verified output:**
```
  [orders] cancelled, cleaning up
{'data': {'users': 'USERS', 'stock': 'STOCK'}, 'errors': {'orders': 'orders timed out after 1.5s'}, 'partial': True} 1.5
```

**Explain each important piece**
- `asyncio.timeout(limit)` gives each service its own budget. When it expires, the inner `await` receives `CancelledError`, which the context manager converts to `TimeoutError`.
- We convert it to a readable message and use `from None` to hide the noisy internal chain.
- `asyncio.wait(..., timeout=overall)` enforces a ceiling for the *whole* endpoint. Since `per < overall` here, the per-service timeout fires first (1.5 s); the overall deadline is the safety net if you had many services or retries.
- After `wait`, any `pending` tasks are explicitly cancelled, then awaited so cancellation completes before we return. **This is the "what happens to unfinished tasks" answer: you must cancel and await them; otherwise they keep running after the response is sent.**
- Results are classified from task state: `cancelled()`, `exception()`, `result()`. Calling `exception()` on a cancelled task raises, hence the order of checks.

### Approach B: `gather(return_exceptions=True)` + `wait_for` (simplest)

```python
async def safe_call(name, coro, timeout):
    try:
        return name, await asyncio.wait_for(coro, timeout), None
    except Exception as e:                           # includes TimeoutError; not CancelledError
        return name, None, f"{type(e).__name__}: {e}"

async def aggregate_simple():
    results = await asyncio.gather(
        safe_call("users",  svc("users", .2), 1.5),
        safe_call("orders", svc("orders", 10), 1.5),
        safe_call("stock",  svc("stock", .2), 1.5),
    )
    data   = {n: v for n, v, e in results if e is None}
    errors = {n: e for n, v, e in results if e is not None}
    return {"data": data, "errors": errors, "partial": bool(errors)}
```
Because every call catches its own errors, `gather` never raises, so `return_exceptions` is not even needed. This is a very readable pattern for interviews.

### The extended questions answered for Python

| Question | Answer |
|---|---|
| One API takes 10 s, two take 200 ms | Wrap each call in `wait_for`/`timeout`. Without it the slow one holds `gather` for 10 s. |
| Endpoint must respond in 2 s | Per-service timeouts below the overall deadline, plus an overall `wait(timeout=...)` or an outer `asyncio.timeout`. Leave headroom for your own processing. |
| Partial results | `gather(return_exceptions=True)` or per-call try/except; return `data`, `errors`, `partial`. |
| Unfinished tasks | `wait_for`/`timeout` cancel the inner work. With `wait`, **you** cancel pending tasks and await them. |
| Cancellation and cleanup | Catch `CancelledError` only to clean up, then re-raise. Use `async with` / `try...finally` to close clients and release resources. |
| When to fail completely | Data is only meaningful as a whole, wrong/missing data could cause a bad business decision, or the missing piece is the primary requested resource. Then use `TaskGroup`/default `gather` and return an error (e.g. 502/503/504). |
| All three essential | `TaskGroup` (cancels siblings on first failure) inside an `asyncio.timeout`, then map the `ExceptionGroup` to a clear error response. |

### All essential, fail fast with cleanup (3.11+)

```python
async def all_essential(overall=2.0):
    try:
        async with asyncio.timeout(overall):
            async with asyncio.TaskGroup() as tg:
                t1 = tg.create_task(svc("users", .2))
                t2 = tg.create_task(svc("orders", .3))
                t3 = tg.create_task(svc("stock", .2))
        return [t1.result(), t2.result(), t3.result()]
    except* TimeoutError:
        raise RuntimeError("upstream deadline exceeded")
    except* Exception as eg:
        raise RuntimeError(f"required upstream failed: {[str(e) for e in eg.exceptions]}")
```

**Common weak answers:** "Use try/except" (no concurrency or partial thinking); "gather cancels the rest" (false); "just retry" (no limits or idempotency).

---

## 3.8 Python-specific traps

- **Forgetting `await`:** returns a coroutine object; code appears to "do nothing".
- **Blocking calls inside `async def`:** freezes everyone (3.3).
- **Swallowing `CancelledError`:** timeouts then never work.
- **`asyncio.run` inside a running loop:** raises `RuntimeError`; just `await`.
- **Shared mutable state across tasks:** no preemption means fewer races than threads, but any `await` between read and write is a switch point:
```python
# Race: another task can run between the read and the write
v = counter; await asyncio.sleep(0); counter = v + 1
```
Use `asyncio.Lock` if you must read-modify-write across an `await`.
- **Unbounded concurrency:** launching 10,000 requests at once. Use `asyncio.Semaphore(20)` to limit parallel calls:
```python
sem = asyncio.Semaphore(20)
async def limited(coro_fn, *a):
    async with sem:
        return await coro_fn(*a)
```

**Interview Q:** *"How would you limit to 10 concurrent API calls?"* → `Semaphore(10)` as above.

---

# Part 4: FastAPI Practical Handbook

> **Requirements:** Python 3.11+ (uses `asyncio.timeout`). Packages: `fastapi`, `uvicorn`, `httpx`, `pytest`. All code in this Part was run and the tests pass (7 passed).

## 4.1 FastAPI essentials you must be able to explain

**Simple definition.** FastAPI is a Python web framework built on **Starlette** (the async web toolkit) and **Pydantic** (data validation). You declare routes with decorators; type hints drive request parsing, validation, serialization and automatic OpenAPI docs at `/docs`.

| Spring Boot | FastAPI |
|---|---|
| `@GetMapping("/x/{id}")` | `@app.get("/x/{id}")` |
| `@PathVariable Long id` | `id: int` (function parameter name matches the path) |
| `@RequestParam` | other simple parameters, e.g. `strict: bool = False` become query parameters |
| `@RequestBody @Valid Dto` | parameter typed as a Pydantic model |
| Bean Validation (`@Min`, `@Pattern`) | `Field(ge=1, pattern=...)` |
| `ResponseEntity` / `@ResponseStatus` | `status_code=201`, `HTTPException`, `response_model` |
| Constructor injection | `Depends(...)` |
| `Filter` / interceptor | middleware |
| Actuator, Swagger via springdoc | built in `/docs` and `/openapi.json` |

**Where parameters come from (decided by the declaration):**
- Name appears in the path (`/customers/{customer_id}`) → **path parameter**.
- Simple type (`int`, `str`, `bool`) not in path → **query parameter**.
- Pydantic model → **JSON request body**.
- `Path(gt=0)`, `Query(...)`, `Header(...)` add constraints and docs.

**Validation failures** produce an automatic **422 Unprocessable Entity** with a precise list of errors (field location, message). You get this for free.

### `def` vs `async def` endpoints (a likely interview topic)

| | `def` endpoint | `async def` endpoint |
|---|---|---|
| Where it runs | FastAPI runs it in a **thread pool** (worker threads) | Directly **on the event loop** thread |
| Blocking calls inside (`requests`, `time.sleep`, blocking DB driver) | Fine: only that worker thread waits (pool is limited, default about 40 threads) | **Harmful: freezes the loop for every request** |
| `await` inside | Not allowed | Allowed |
| Best for | Blocking libraries | Async libraries (`httpx.AsyncClient`, async DB driver) and many concurrent I/O calls |

**Rule of thumb to say aloud:** "If I `await` something inside, I use `async def`. If I call blocking code and cannot change it, I use plain `def` (or `await asyncio.to_thread(...)`). I never call blocking code directly inside `async def`."

**Common mistakes**
- `async def` endpoint calling `requests.get` or a blocking ORM session: looks fine with one user, collapses under load.
- Doing CPU-heavy work in any endpoint without offloading: blocks the loop (in `async def`) or exhausts the thread pool (in `def`).
- Creating a new `httpx.AsyncClient` for every request (no connection reuse).
- Returning ORM objects or raw dicts with secret fields instead of using `response_model`.

**Interview Q:** *"What is the difference between `def` and `async def` in FastAPI?"*
**Strong answer:** "With `async def`, FastAPI runs the function on the event loop, so I must only await non-blocking operations; blocking inside it stalls all requests. With plain `def`, FastAPI automatically runs it in a thread pool, so blocking calls only occupy one worker thread. I choose `async def` when I use async clients and want high concurrency for I/O; I choose `def` when I rely on blocking libraries."

---

## 4.2 The project: a dashboard aggregator

**Scenario [Inferred, a practice scenario built from the three-API question, not a claim about SM Auto's systems].** An internal "customer dashboard" needs data from three internal services: *profile*, *orders*, and *stock*. The endpoint must respond quickly, call the three services **concurrently**, apply a **per-service timeout**, and return **partial results** when allowed.

### File structure

```
aggregator/
├── requirements.txt
├── mock_upstreams.py        # fake services so you can practice offline
├── app/
│   ├── __init__.py
│   ├── config.py            # settings from environment variables
│   ├── schemas.py           # Pydantic request/response models
│   ├── services.py          # one-service call with timeout + error mapping
│   └── main.py              # app, middleware, routes, dependencies
└── tests/
    ├── __init__.py
    └── test_api.py
```

### Install and run

```bash
python -m venv .venv
source .venv/bin/activate          # Windows: .venv\Scripts\activate
pip install -r requirements.txt

# Terminal 1: fake upstream services on port 9000
uvicorn mock_upstreams:app --port 9000

# Terminal 2: the aggregator on port 8000
uvicorn app.main:app --reload --port 8000

# Try it
curl http://localhost:8000/api/customers/1/dashboard
# Interactive docs: http://localhost:8000/docs
```

**Make failures happen (stop the mock, then restart it):**
```bash
# Linux/macOS
FAIL_SERVICES=stock SLOW_SERVICES=orders uvicorn mock_upstreams:app --port 9000
# Windows PowerShell
$env:FAIL_SERVICES="stock"; $env:SLOW_SERVICES="orders"; uvicorn mock_upstreams:app --port 9000
```

**Verified result with one slow (orders) and one failing (stock) service:**
```json
{"customer_id":1,"partial":true,
 "data":{"profile":{"service":"profile","customer_id":1,"value":"profile-data"}},
 "errors":{"orders":"timeout after 1.5s","stock":"upstream returned HTTP 500"},
 "elapsed_ms":1503}
```
HTTP 200 in about 1.5 s: the slow service was cut off at its 1.5 s budget while the others ran concurrently, instead of the response taking 10 s.

---

## 4.3 The code, file by file

**`requirements.txt`**

```text
fastapi>=0.110
uvicorn[standard]>=0.29
httpx>=0.27
pytest>=8.0
```

**`app/config.py`**

```python
import os
from dataclasses import dataclass


@dataclass(frozen=True)
class Settings:
    """Read configuration from environment variables (never hard-code URLs or secrets)."""
    upstream_base_url: str = os.getenv("UPSTREAM_BASE_URL", "http://127.0.0.1:9000")
    per_service_timeout_s: float = float(os.getenv("PER_SERVICE_TIMEOUT_S", "1.5"))
    allowed_origins: tuple[str, ...] = tuple(
        o for o in os.getenv("ALLOWED_ORIGINS", "http://localhost:5173").split(",") if o
    )


def get_settings() -> Settings:
    return Settings()
```

**`app/schemas.py`**

```python
from typing import Any
from pydantic import BaseModel, Field


class DashboardResponse(BaseModel):
    customer_id: int
    partial: bool = Field(description="True when at least one upstream service failed")
    data: dict[str, Any]
    errors: dict[str, str]
    elapsed_ms: int


class InquiryCreate(BaseModel):
    customer_name: str = Field(min_length=2, max_length=100)
    part_number: str = Field(pattern=r"^[A-Z]{2}-\d{4}$", examples=["BR-1042"])
    quantity: int = Field(ge=1, le=10_000)
    notes: str | None = Field(default=None, max_length=500)


class InquiryOut(InquiryCreate):
    id: int
```

**`app/services.py`**

```python
import asyncio
import logging
import time
from dataclasses import dataclass
from typing import Any

import httpx

log = logging.getLogger("aggregator.services")


@dataclass
class ServiceResult:
    name: str
    ok: bool
    data: Any = None
    error: str | None = None
    duration_ms: int = 0


async def call_service(client: httpx.AsyncClient, name: str, path: str, timeout_s: float) -> ServiceResult:
    """Call ONE upstream service. Never raises (except cancellation), always returns a ServiceResult."""
    start = time.perf_counter()
    try:
        # httpx timeouts apply per phase (connect/read/...), not to the whole call.
        # asyncio.timeout gives a hard TOTAL budget (Python 3.11+).
        async with asyncio.timeout(timeout_s):
            resp = await client.get(path)
        resp.raise_for_status()                      # turns 4xx/5xx into httpx.HTTPStatusError
        return ServiceResult(name, True, data=resp.json(), duration_ms=_ms(start))
    except TimeoutError:
        err = f"timeout after {timeout_s}s"
    except httpx.HTTPStatusError as e:
        err = f"upstream returned HTTP {e.response.status_code}"
    except httpx.RequestError as e:                  # connection refused, DNS, etc.
        err = f"network error: {type(e).__name__}"
    except ValueError:                               # resp.json() on invalid JSON
        err = "invalid JSON from upstream"
    log.warning("service=%s failed: %s (%d ms)", name, err, _ms(start))
    return ServiceResult(name, False, error=err, duration_ms=_ms(start))


def _ms(start: float) -> int:
    return int((time.perf_counter() - start) * 1000)
```

**`app/main.py`**

```python
import asyncio
import itertools
import logging
import time
import uuid
from contextlib import asynccontextmanager

import httpx
from fastapi import Depends, FastAPI, HTTPException, Path, Query, Request
from fastapi.middleware.cors import CORSMiddleware

from .config import Settings, get_settings
from .schemas import DashboardResponse, InquiryCreate, InquiryOut
from .services import call_service

logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(name)s %(message)s")
log = logging.getLogger("aggregator")


@asynccontextmanager
async def lifespan(app: FastAPI):
    settings = get_settings()
    # ONE shared client: reuses connections. Creating a client per request is wasteful.
    app.state.client = httpx.AsyncClient(base_url=settings.upstream_base_url, timeout=httpx.Timeout(5.0, connect=1.0))
    yield
    await app.state.client.aclose()                  # clean shutdown


app = FastAPI(title="Aggregator", lifespan=lifespan)
app.add_middleware(
    CORSMiddleware,
    allow_origins=list(get_settings().allowed_origins),   # explicit origins, not "*"
    allow_methods=["GET", "POST"],
    allow_headers=["Content-Type"],
)


@app.middleware("http")
async def request_logging(request: Request, call_next):
    rid = request.headers.get("x-request-id", uuid.uuid4().hex[:8])
    start = time.perf_counter()
    response = await call_next(request)
    log.info("rid=%s %s %s -> %d (%d ms)", rid, request.method, request.url.path,
             response.status_code, (time.perf_counter() - start) * 1000)
    response.headers["x-request-id"] = rid
    return response


def get_client(request: Request) -> httpx.AsyncClient:
    return request.app.state.client                  # replaced with a fake in tests


SERVICES = {"profile": "/profile/{id}", "orders": "/orders/{id}", "stock": "/stock/{id}"}


@app.get("/api/customers/{customer_id}/dashboard", response_model=DashboardResponse)
async def dashboard(
    customer_id: int = Path(gt=0),
    strict: bool = Query(False, description="If true, any upstream failure fails the whole request"),
    client: httpx.AsyncClient = Depends(get_client),
    settings: Settings = Depends(get_settings),
):
    start = time.perf_counter()
    # call_service never raises, so gather never raises either: all three run concurrently.
    results = await asyncio.gather(*(
        call_service(client, name, tpl.format(id=customer_id), settings.per_service_timeout_s)
        for name, tpl in SERVICES.items()
    ))
    data = {r.name: r.data for r in results if r.ok}
    errors = {r.name: r.error for r in results if not r.ok}

    if not data:                                      # nothing worked: do not pretend success
        raise HTTPException(503, detail={"message": "all upstream services failed", "errors": errors})
    if strict and errors:                             # all pieces essential
        raise HTTPException(502, detail={"message": "required upstream failed", "errors": errors})

    return DashboardResponse(customer_id=customer_id, partial=bool(errors), data=data,
                             errors=errors, elapsed_ms=int((time.perf_counter() - start) * 1000))


_inquiries: dict[int, InquiryOut] = {}               # in-memory store, for demo only
_ids = itertools.count(1)


@app.post("/api/inquiries", response_model=InquiryOut, status_code=201)
async def create_inquiry(body: InquiryCreate):
    # Pydantic already validated the body; invalid input gets an automatic 422.
    item = InquiryOut(id=next(_ids), **body.model_dump())
    _inquiries[item.id] = item
    return item


@app.get("/api/inquiries/{inquiry_id}", response_model=InquiryOut)
async def get_inquiry(inquiry_id: int):
    if inquiry_id not in _inquiries:
        raise HTTPException(404, "inquiry not found")
    return _inquiries[inquiry_id]
```

**`mock_upstreams.py`**

```python
"""Fake upstream services for local practice.

Run:  uvicorn mock_upstreams:app --port 9000
Make one misbehave via environment variables (set them BEFORE starting the mock):
    FAIL_SERVICES=stock        -> /stock/* returns HTTP 500
    SLOW_SERVICES=orders       -> /orders/* takes 10 seconds
You can also pass ?delay=... or ?fail=true directly to a mock endpoint.
"""
import asyncio
import os

from fastapi import FastAPI, HTTPException

app = FastAPI()
FAIL = set(filter(None, os.getenv("FAIL_SERVICES", "").split(",")))
SLOW = set(filter(None, os.getenv("SLOW_SERVICES", "").split(",")))


async def respond(name: str, id: int, delay: float, fail: bool):
    await asyncio.sleep(10 if name in SLOW else delay)
    if fail or name in FAIL:
        raise HTTPException(500, f"{name} exploded")
    return {"service": name, "customer_id": id, "value": f"{name}-data"}


@app.get("/profile/{id}")
async def profile(id: int, delay: float = 0.2, fail: bool = False):
    return await respond("profile", id, delay, fail)


@app.get("/orders/{id}")
async def orders(id: int, delay: float = 0.2, fail: bool = False):
    return await respond("orders", id, delay, fail)


@app.get("/stock/{id}")
async def stock(id: int, delay: float = 0.2, fail: bool = False):
    return await respond("stock", id, delay, fail)
```

## 4.4 Why the code is written this way

| Decision | Reason (say this in an interview) |
|---|---|
| `call_service` **never raises**; it returns a `ServiceResult` | Each failure is converted to data at the boundary, so `asyncio.gather` can never be broken by one service and we need no `return_exceptions` juggling. |
| `asyncio.timeout(timeout_s)` around the request | `httpx.Timeout` limits individual **phases** (connect, read, write, pool wait). A server that trickles bytes slowly can keep a read timeout from firing. `asyncio.timeout` is a hard **total** budget. |
| Exceptions mapped to short strings | The client gets a useful reason; internal details and stack traces stay in logs, not in responses. |
| `resp.raise_for_status()` | `httpx`, like `fetch`, does **not** raise on 4xx/5xx by default. This converts them to `HTTPStatusError`. |
| One shared `AsyncClient` created in `lifespan` | Connection pooling and keep-alive; closed on shutdown. Created per request it would open new connections each time. |
| `Depends(get_client)` | Tests replace the dependency with a fake client, so no network is needed. Same idea as injecting a mock bean in Spring. |
| `gather(*(...))` over the three calls | The three calls overlap, so total time is about the slowest one, not the sum. |
| `if not data: 503` | If nothing succeeded, returning `200 {partial: true, data: {}}` would hide an outage. |
| `strict` query flag → `502` | Models "all three essential": one failure fails the request. 502 means "bad gateway: an upstream failed"; 504 would be a pure upstream timeout. |
| `partial` + `errors` fields | The client can render a warning ("Orders unavailable") instead of silently missing data. |
| Explicit CORS origins | `"*"` is avoided; only the known frontend origin is allowed. |
| Request-ID middleware | Lets you correlate a user complaint with log lines. |

### HTTP status code choices

| Situation | Code | Note |
|---|---|---|
| All good | 200 | |
| Some services failed, partial allowed | 200 with `partial: true` | Some teams prefer 207 or a warnings header; whichever you pick, **document the contract** |
| Created a resource | 201 | `POST /api/inquiries` |
| Validation error | 422 | Automatic from FastAPI |
| Not found | 404 | |
| Upstream failed and result unusable | 502 | |
| No upstream available / overloaded | 503 | |
| Upstream too slow and result unusable | 504 | |

### Which operations are truly asynchronous, and which could block the loop

| Operation | Async or blocking? | Notes |
|---|---|---|
| `await client.get(...)` (`httpx.AsyncClient`) | **Async** | Yields while waiting for the network |
| `await asyncio.sleep(n)` | **Async** | |
| `await asyncio.gather(...)` | **Async** | Runs awaitables concurrently |
| `requests.get(...)` | **Blocking** | Never in `async def` |
| `time.sleep(n)` | **Blocking** | |
| Synchronous DB driver / SQLAlchemy sync session | **Blocking** | Use a plain `def` endpoint, `to_thread`, or an async driver |
| Reading a large file with `open().read()` | **Blocking** (briefly) | Fine for small files; for large ones use `to_thread` |
| `pydantic` validation of small bodies | CPU, microseconds | Fine |
| `json.loads` of a 200 MB body, image/PDF processing, heavy loops | **CPU-bound blocking** | Offload to a process pool or a worker |
| `logging` to a file/console | Blocking but tiny | Fine at normal volume |

### Demonstration of blocking the loop (add to `app/main.py` to experiment)

```python
import time

@app.get("/demo/blocking-bad")
async def blocking_bad():
    time.sleep(3)              # BLOCKS the event loop: every other request waits
    return {"ok": True}

@app.get("/demo/blocking-ok")
def blocking_ok():             # plain def: FastAPI runs it in the thread pool
    time.sleep(3)
    return {"ok": True}
```
Experiment: start the server, open two terminals and run `curl localhost:8000/demo/blocking-bad` twice at the same time, then `curl localhost:8000/health` (add a trivial `/health`). During `blocking-bad`, `/health` hangs; during `blocking-ok`, it responds instantly. (This is a good thing to demonstrate in an interview discussion, and good to try yourself once rather than memorize.)

---

## 4.4a The tests

**`tests/test_api.py`**

```python
import asyncio

import httpx
import pytest
from fastapi.testclient import TestClient

from app.main import app, get_client


def make_client(handlers: dict):
    """Build a fake upstream client. handlers maps path prefix -> async function(request) -> Response."""
    async def route(request: httpx.Request) -> httpx.Response:
        for prefix, fn in handlers.items():
            if request.url.path.startswith(prefix):
                return await fn(request)
        return httpx.Response(404)
    return httpx.AsyncClient(transport=httpx.MockTransport(route), base_url="http://upstream")


async def ok(request):
    return httpx.Response(200, json={"path": request.url.path})

async def boom(request):
    return httpx.Response(500)

async def hang(request):
    await asyncio.sleep(10)
    return httpx.Response(200, json={})


@pytest.fixture
def http():
    # TestClient triggers the real lifespan; we then override the dependency with a fake.
    with TestClient(app) as c:
        yield c
    app.dependency_overrides.clear()


def use(handlers):
    app.dependency_overrides[get_client] = lambda: make_client(handlers)


def test_all_success(http):
    use({"/profile": ok, "/orders": ok, "/stock": ok})
    r = http.get("/api/customers/7/dashboard")
    assert r.status_code == 200
    body = r.json()
    assert body["partial"] is False and set(body["data"]) == {"profile", "orders", "stock"}


def test_partial_success_when_one_fails(http):
    use({"/profile": ok, "/orders": boom, "/stock": ok})
    body = http.get("/api/customers/7/dashboard").json()
    assert body["partial"] is True
    assert set(body["data"]) == {"profile", "stock"}
    assert "HTTP 500" in body["errors"]["orders"]


def test_slow_service_times_out_without_blocking_others(http):
    use({"/profile": ok, "/orders": hang, "/stock": ok})
    import time
    t = time.perf_counter()
    body = http.get("/api/customers/7/dashboard").json()
    assert time.perf_counter() - t < 3                  # per-service timeout (1.5s) bounded the request
    assert "timeout" in body["errors"]["orders"]


def test_all_fail_returns_503(http):
    use({"/profile": boom, "/orders": boom, "/stock": boom})
    assert http.get("/api/customers/7/dashboard").status_code == 503


def test_strict_mode_fails_on_any_error(http):
    use({"/profile": ok, "/orders": boom, "/stock": ok})
    assert http.get("/api/customers/7/dashboard?strict=true").status_code == 502


def test_path_validation(http):
    assert http.get("/api/customers/0/dashboard").status_code == 422
    assert http.get("/api/customers/abc/dashboard").status_code == 422


def test_inquiry_validation(http):
    good = {"customer_name": "Asha", "part_number": "BR-1042", "quantity": 5}
    assert http.post("/api/inquiries", json=good).status_code == 201
    assert http.post("/api/inquiries", json={**good, "part_number": "bad"}).status_code == 422
    assert http.post("/api/inquiries", json={**good, "quantity": 0}).status_code == 422
```


Run with: `pytest -q` (from the `aggregator/` folder). **Verified: 7 passed.**

## 4.5 Logging, testing and debugging

### Logging
- Use the `logging` module, not `print`; include **service name, duration, error type, request id**.
- Log at the right level: `info` for requests, `warning` for handled upstream failures, `error`/`exception` for unexpected bugs (`log.exception` includes the stack trace).
- **Never log secrets, tokens, passwords or full personal/business payloads.** In sensitive internal systems, log identifiers, not contents.

### Testing approach used here
- **Unit-level API tests with `TestClient`**: fast, no network.
- **Fake upstreams with `httpx.MockTransport`**: a handler decides the response, can `await asyncio.sleep` to simulate slowness. No extra library is required.
- **`app.dependency_overrides`**: swaps `get_client` for the fake; always cleared afterwards (the fixture does this).
- **Scenarios covered:** all succeed, one fails (partial), one hangs (timeout), all fail (503), strict mode (502), path validation (422), body validation (422/201).

**Minimal test pattern to memorize:**
```python
def test_partial(http):
    use({"/profile": ok, "/orders": boom, "/stock": ok})   # arrange fake upstreams
    body = http.get("/api/customers/7/dashboard").json()   # act
    assert body["partial"] is True                         # assert behaviour, not implementation
    assert set(body["data"]) == {"profile", "stock"}
```

### Debugging a failing endpoint without AI (method)
1. **Reproduce** with `curl` or `/docs`; note exact URL, body, status code.
2. **Read the response body**: 422 tells you the failing field; 500 means check server logs.
3. **Read the server log and traceback**, bottom up: the last frame in *your* code is usually the culprit.
4. **Narrow down:** call the upstream service directly with `curl`. Is the upstream the problem or your code?
5. **Check the usual suspects:** wrong URL/env variable, missing `await` (response contains a coroutine object, or "coroutine was never awaited" warning), blocking call in `async def`, wrong `response_model`, CORS (appears only in the browser).
6. **Add a log line or a failing test** that captures the bug; fix; keep the test.

### Dependency injection and application structure (quick reference)

```python
# Dependency with cleanup (like a request-scoped bean)
async def get_db():
    db = await open_connection()
    try:
        yield db               # value injected into the endpoint
    finally:
        await db.close()       # runs after the response

@app.get("/items")
async def items(db = Depends(get_db)): ...
```
- **Routers:** split endpoints by area with `APIRouter(prefix="/api/orders")` and `app.include_router(...)`, like splitting controllers.
- **Layers:** keep route functions thin; put logic in `services/`, data access in `repositories/`, models in `schemas/`.
- **Settings:** read from environment (the guide uses a dataclass; `pydantic-settings` is a common upgrade).

### Calling an external API from FastAPI: checklist
- [ ] Async client (`httpx.AsyncClient`) shared via lifespan
- [ ] Explicit timeout (total, not only per-phase)
- [ ] `raise_for_status()` or manual status handling
- [ ] Failure mapped to a meaningful status code and message
- [ ] Retries only for idempotent calls, with limit and backoff, inside the time budget
- [ ] No secrets in logs; secrets in environment variables
- [ ] Test with a fake transport

**Interview Q:** *"Your endpoint calls three services. How do you stop it hanging when one is down?"*
**Strong answer:** "I run the calls concurrently with `asyncio.gather`, give each a hard timeout with `asyncio.timeout`, and make each call convert its own failures into a result object. If the requirement allows it I return partial data with a `partial` flag and an error per service; if all are essential I fail fast with 502/504. I test the timeout path with a fake transport that sleeps, and I log service name, duration and error."

---


# Python is skiped for now but it will continued after finishing the other parts 
  start from part 3 part 1 snd 2 is complete 

# Part 5: React and Frontend Integration

> **Requirements:** Node 18+ (20 recommended). React 18, Vite 5. **Verified:** the app builds with `vite build` and the 5 component tests below pass with Vitest 2. It was not exercised in a real browser during writing, so do your own manual run (steps below).

## 5.1 React concepts you need for this interview

| Concept | One-line explanation | Common trap |
|---|---|---|
| **Component** | A function returning UI (JSX) | Defining a component inside another component (it is recreated every render) |
| **Props** | Read-only inputs to a component | Mutating props |
| **State (`useState`)** | Data that, when changed, triggers a re-render | Mutating state directly (`arr.push`) instead of creating a new value |
| **Effect (`useEffect`)** | Code that synchronizes with something outside React (network, timers, subscriptions) | Missing or wrong dependency array; missing cleanup |
| **Dependency array** | Re-run the effect when these values change | `[]` hides updates; omitting it runs every render |
| **Cleanup function** | Returned from an effect; runs before the next run and on unmount | Not aborting requests, leaving timers running |
| **Controlled input** | Value held in state, updated by `onChange` | Mixing controlled/uncontrolled |
| **Keys** | Stable identity for list items | Using array index when the list reorders |
| **Custom hook** | A function starting with `use` that reuses stateful logic | Calling hooks conditionally |

**Rendering model in one paragraph.** React calls your component function to get UI. When state or props change, it calls the function again and updates the DOM with the differences. An effect runs *after* render. **State updates are asynchronous batches:** `setCount(count + 1)` twice in a row adds only 1; use `setCount(c => c + 1)` when the new value depends on the old.

**React 18 StrictMode (development only)** mounts, unmounts and remounts components once to expose missing cleanup. If your effect fires twice in development, that is intentional. The fix is correct cleanup (abort the request), not removing StrictMode.

## 5.2 Design of the consumer

**What the UI must handle (the five states):**

| State | Meaning | UI |
|---|---|---|
| **loading** | Request in flight, no data yet | "Loading…" (`role="status"`) |
| **success** | `partial: false`, has data | Show everything |
| **partial** | `partial: true`, some data | Show what exists plus a visible warning naming the missing parts and a Retry button |
| **empty** | Request worked but there is nothing to show | Friendly "No data found" |
| **error** | Request failed (network, timeout, 503, 422) | Clear message and a Retry button |

**Key design decisions**
1. **Logic in a custom hook (`useDashboard`)**, display in the component. Easier to test and reuse.
2. **AbortController in the effect cleanup** gives *stale-response protection*: when `customerId` changes quickly (1 then 2), the request for 1 is aborted and can never overwrite the newer data. The alternative for APIs that cannot be aborted is an `ignore` flag set in cleanup.
3. **Frontend timeout (5 s)** longer than the backend's 1.5 s per-service budget, so the backend gets to answer with a partial result first.
4. **Aborts caused by cleanup are not shown as errors.**
5. **Partial results are never silent**: the warning banner states which sections are missing.
6. **Form submit guarded against double-click** (`submitting` flag), because duplicate POSTs create duplicate records.
7. **Client validation is for fast feedback only; the server validates again** and its 422 errors are mapped back onto fields.

## 5.3 Setup and run

```bash
npm create vite@latest dashboard-ui -- --template react   # or copy the files below into a new folder
cd dashboard-ui
npm install
npm run dev          # http://localhost:5173
```
Start the backend from Part 4 on port 8000 first. **CORS:** the backend allows the origin `http://localhost:5173` by default (`ALLOWED_ORIGINS` env var). If you see a CORS error in the browser console, the page origin does not match the allow-list. Set `VITE_API_BASE` in a `.env` file if the API is not on `http://localhost:8000`.

## 5.4 The code

**`package.json`**

```json
{
  "name": "dashboard-ui",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build"
  },
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1"
  },
  "devDependencies": {
    "@testing-library/dom": "^10.4.2",
    "@testing-library/jest-dom": "^6.9.1",
    "@testing-library/react": "^16.3.3",
    "@vitejs/plugin-react": "^4.3.0",
    "jsdom": "^25.0.1",
    "vite": "^5.4.0",
    "vitest": "^2.1.9"
  }
}
```

**`vite.config.js`**

```js
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: { port: 5173 },
  test: { environment: "jsdom", globals: true, setupFiles: ["./vitest.setup.js"] },
});
```

**`index.html`**

```html
<!doctype html>
<html lang="en">
  <head><meta charset="UTF-8" /><title>Dashboard</title></head>
  <body><div id="root"></div><script type="module" src="/src/main.jsx"></script></body>
</html>
```

**`src/main.jsx`**

```jsx
import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App";

createRoot(document.getElementById("root")).render(<React.StrictMode><App /></React.StrictMode>);
```

**`src/api.js`**

```js
// Base URL comes from the environment (Vite exposes VITE_* variables). Never hard-code per-environment URLs.
export const API_BASE = import.meta.env?.VITE_API_BASE ?? "http://localhost:8000";

export class ApiError extends Error {
  constructor(message, { status, detail } = {}) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.detail = detail;
  }
}

// fetch wrapper: converts HTTP errors into exceptions and passes the abort signal through.
export async function apiFetch(path, { signal, ...options } = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: { "Content-Type": "application/json", ...(options.headers ?? {}) },
    signal,
  });
  let body = null;
  try { body = await res.json(); } catch { /* empty or non-JSON body */ }
  if (!res.ok) {
    throw new ApiError(`Request failed with status ${res.status}`, { status: res.status, detail: body?.detail });
  }
  return body;
}
```

**`src/useDashboard.js`**

```js
import { useCallback, useEffect, useState } from "react";
import { apiFetch } from "./api";

const FRONTEND_TIMEOUT_MS = 5000;

// status: "loading" | "success" | "partial" | "empty" | "error"
export function useDashboard(customerId) {
  const [state, setState] = useState({ status: "loading", data: null, errors: {}, error: null });
  const [reloadKey, setReloadKey] = useState(0);
  const reload = useCallback(() => setReloadKey((k) => k + 1), []);

  useEffect(() => {
    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(new DOMException("timeout", "TimeoutError")), FRONTEND_TIMEOUT_MS);
    setState((s) => ({ ...s, status: "loading", error: null }));    // keep old data visible while reloading

    apiFetch(`/api/customers/${customerId}/dashboard`, { signal: controller.signal })
      .then((body) => {
        const hasData = Object.keys(body.data).length > 0;
        setState({
          status: !hasData ? "empty" : body.partial ? "partial" : "success",
          data: body.data,
          errors: body.errors,
          error: null,
        });
      })
      .catch((err) => {
        // Aborts triggered by cleanup (user navigated / id changed) are NOT errors; ignore them.
        if (err.name === "AbortError" && controller.signal.reason?.name !== "TimeoutError") return;
        const message = controller.signal.reason?.name === "TimeoutError"
          ? "The server took too long to respond."
          : err.status === 503 ? "All data sources are unavailable right now."
          : err.status === 422 ? "Invalid customer id."
          : "Something went wrong. Please try again.";
        setState({ status: "error", data: null, errors: {}, error: message });
      })
      .finally(() => clearTimeout(timer));

    // Cleanup runs when customerId changes or the component unmounts:
    // abort the in-flight request so a slow OLD response can never overwrite a newer one.
    return () => { clearTimeout(timer); controller.abort(); };
  }, [customerId, reloadKey]);

  return { ...state, reload };
}
```

**`src/Dashboard.jsx`**

```jsx
import { useDashboard } from "./useDashboard";

const LABELS = { profile: "Profile", orders: "Orders", stock: "Stock" };

export default function Dashboard({ customerId }) {
  const { status, data, errors, error, reload } = useDashboard(customerId);

  if (status === "loading" && !data) return <p role="status">Loading…</p>;

  if (status === "error") {
    return (
      <div role="alert">
        <p>{error}</p>
        <button onClick={reload}>Retry</button>
      </div>
    );
  }

  if (status === "empty") return <p>No data found for this customer.</p>;

  return (
    <section aria-busy={status === "loading"}>
      {status === "partial" && (
        <div role="alert" style={{ background: "#fff4e5", padding: 8 }}>
          Some information is unavailable: {Object.keys(errors).map((k) => LABELS[k] ?? k).join(", ")}.{" "}
          <button onClick={reload}>Retry</button>
        </div>
      )}
      {Object.entries(LABELS).map(([key, label]) => (
        <article key={key}>
          <h3>{label}</h3>
          {data[key] ? <pre>{JSON.stringify(data[key], null, 2)}</pre> : <p>Unavailable ({errors[key] ?? "no data"})</p>}
        </article>
      ))}
    </section>
  );
}
```

**`src/InquiryForm.jsx`**

```jsx
import { useState } from "react";
import { apiFetch } from "./api";

const EMPTY = { customer_name: "", part_number: "", quantity: "1", notes: "" };

function validate(f) {
  const e = {};
  if (f.customer_name.trim().length < 2) e.customer_name = "Enter at least 2 characters.";
  if (!/^[A-Z]{2}-\d{4}$/.test(f.part_number)) e.part_number = "Format: two capital letters, dash, four digits (BR-1042).";
  const q = Number(f.quantity);
  if (!Number.isInteger(q) || q < 1 || q > 10000) e.quantity = "Quantity must be a whole number from 1 to 10000.";
  return e;
}

export default function InquiryForm() {
  const [form, setForm] = useState(EMPTY);
  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState(null);

  const onChange = (e) => setForm((f) => ({ ...f, [e.target.name]: e.target.value }));

  async function onSubmit(e) {
    e.preventDefault();                          // stop the browser's full-page form submit
    if (submitting) return;                      // guard against double clicks (duplicate POSTs)
    const clientErrors = validate(form);         // fast feedback; the SERVER still validates (never trust the client)
    setErrors(clientErrors);
    if (Object.keys(clientErrors).length) return;

    setSubmitting(true); setResult(null);
    try {
      const saved = await apiFetch("/api/inquiries", {
        method: "POST",
        body: JSON.stringify({ ...form, quantity: Number(form.quantity), notes: form.notes || null }),
      });
      setResult({ ok: true, message: `Saved inquiry #${saved.id}` });
      setForm(EMPTY);
    } catch (err) {
      if (err.status === 422 && Array.isArray(err.detail)) {
        // Map FastAPI's error list ({loc: ["body", "quantity"], msg}) onto the fields
        const serverErrors = {};
        err.detail.forEach((d) => { serverErrors[d.loc[d.loc.length - 1]] = d.msg; });
        setErrors(serverErrors);
      } else {
        setResult({ ok: false, message: "Could not save. Please try again." });
      }
    } finally {
      setSubmitting(false);
    }
  }

  const field = (name, label, props = {}) => (
    <label style={{ display: "block", marginBottom: 8 }}>
      {label}
      <input name={name} value={form[name]} onChange={onChange} {...props} />
      {errors[name] && <span role="alert" style={{ color: "crimson" }}> {errors[name]}</span>}
    </label>
  );

  return (
    <form onSubmit={onSubmit} noValidate>
      {field("customer_name", "Customer name ")}
      {field("part_number", "Part number ", { placeholder: "BR-1042" })}
      {field("quantity", "Quantity ", { inputMode: "numeric" })}
      {field("notes", "Notes ")}
      <button type="submit" disabled={submitting}>{submitting ? "Saving…" : "Submit inquiry"}</button>
      {result && <p role="status" style={{ color: result.ok ? "green" : "crimson" }}>{result.message}</p>}
    </form>
  );
}
```

**`src/App.jsx`**

```jsx
import { useState } from "react";
import Dashboard from "./Dashboard";
import InquiryForm from "./InquiryForm";

export default function App() {
  const [customerId, setCustomerId] = useState(1);
  return (
    <main style={{ maxWidth: 720, margin: "2rem auto", fontFamily: "sans-serif" }}>
      <h1>Customer dashboard</h1>
      <label>Customer id{" "}
        <input type="number" min="1" value={customerId}
               onChange={(e) => setCustomerId(Number(e.target.value) || 1)} />
      </label>
      <Dashboard customerId={customerId} />
      <h2>New inquiry</h2>
      <InquiryForm />
    </main>
  );
}
```

**`vitest.setup.js`**

```js
import "@testing-library/jest-dom/vitest";
```

**`src/Dashboard.test.jsx`**

```jsx
import { render, screen, waitFor } from "@testing-library/react";
import { afterEach, describe, expect, it, vi } from "vitest";
import Dashboard from "./Dashboard";

const json = (status, body) => Promise.resolve({ ok: status < 400, status, json: () => Promise.resolve(body) });

afterEach(() => vi.restoreAllMocks());

describe("Dashboard states", () => {
  it("shows success", async () => {
    vi.stubGlobal("fetch", vi.fn(() => json(200, { partial: false, data: { profile: { a: 1 }, orders: {}, stock: {} }, errors: {} })));
    render(<Dashboard customerId={1} />);
    expect(screen.getByRole("status")).toHaveTextContent("Loading");
    await waitFor(() => expect(screen.getByText("Profile")).toBeInTheDocument());
    expect(screen.queryByRole("alert")).toBeNull();
  });

  it("shows partial warning when one service failed", async () => {
    vi.stubGlobal("fetch", vi.fn(() => json(200, { partial: true, data: { profile: {} }, errors: { orders: "timeout after 1.5s" } })));
    render(<Dashboard customerId={1} />);
    await waitFor(() => expect(screen.getByRole("alert")).toHaveTextContent("Orders"));
  });

  it("shows empty state", async () => {
    vi.stubGlobal("fetch", vi.fn(() => json(200, { partial: false, data: {}, errors: {} })));
    render(<Dashboard customerId={1} />);
    await waitFor(() => expect(screen.getByText(/No data found/)).toBeInTheDocument());
  });

  it("shows error on 503", async () => {
    vi.stubGlobal("fetch", vi.fn(() => json(503, { detail: { message: "down" } })));
    render(<Dashboard customerId={1} />);
    await waitFor(() => expect(screen.getByRole("alert")).toHaveTextContent("unavailable"));
  });

  it("ignores a stale slow response after the id changes", async () => {
    let resolveOld;
    const fetchMock = vi.fn((url, { signal }) => {
      if (url.includes("/customers/1/")) {
        return new Promise((resolve, reject) => {
          resolveOld = () => resolve({ ok: true, status: 200, json: () => Promise.resolve({ partial: false, data: { profile: { who: "OLD" } }, errors: {} }) });
          signal.addEventListener("abort", () => reject(signal.reason));
        });
      }
      return json(200, { partial: false, data: { profile: { who: "NEW" } }, errors: {} });
    });
    vi.stubGlobal("fetch", fetchMock);
    const { rerender } = render(<Dashboard customerId={1} />);
    rerender(<Dashboard customerId={2} />);
    await waitFor(() => expect(screen.getByText(/NEW/)).toBeInTheDocument());
    resolveOld?.();                                  // the old response arrives late...
    await new Promise((r) => setTimeout(r, 20));
    expect(screen.queryByText(/OLD/)).toBeNull();    // ...and must not overwrite the newer data
  });
});
```


Run tests: `npx vitest run` (**5 passed**: success, partial, empty, 503 error, stale response ignored).

## The useEffect runs when customerId or reloadKey changes:

Create an AbortController. Its signal lets you cancel the request.
Start a 5 second timer that aborts the request with a TimeoutError (frontend timeout).
Set status to loading but keep the old data (...s), so the screen doesn’t flash blank.
Call the API, then decide the status:
no keys in body.data → empty
body.partial is true → partial (the back  end says some service failed)
otherwise → success
In .catch:
If the abort came from cleanup (user changed the id or left the page), it is AbortError, so ignore it. That is not a real error.
If it was our timeout, show “took too long”.
Otherwise map status codes: 503 → “All data sources unavailable”, 422 → “Invalid customer id”, else a generic message.
.finally clears the timer.
The returned cleanup function runs before the next effect and on unmount. It aborts the in-flight request. This prevents a race condition: you load customer 1 (slow), switch to customer 2 (fast), then the slow response for 1 arrives and overwrites customer 2’s data. Aborting makes that impossible.

## 5.5 Walkthrough of the important parts

**`useDashboard` step by step**
1. First render: `status: "loading"`.
2. The effect runs after render: creates an `AbortController` and a 5-second timer, sets status to loading, and calls the API.
3. On success, the status becomes `empty`, `partial` or `success` based on the response.
4. On failure, aborts caused by cleanup are ignored; everything else becomes a friendly message chosen by status code or timeout.
5. **Cleanup:** when `customerId` changes or the component unmounts, the old request is aborted and the timer cleared.
6. `reload()` bumps `reloadKey`, which is in the dependency array, re-running the effect for a retry.

**Why the stale-response test matters.** The test starts a slow request for customer 1, switches to customer 2 (fast), then lets the slow one "arrive". Because cleanup aborted it, the old data never appears. Without cleanup, whichever response arrives *last* wins, which is a classic race condition (the user sees customer 2's id with customer 1's data).

## 5.6 Debugging frontend–backend integration without AI

| Symptom | Likely cause | How to check |
|---|---|---|
| Console: "blocked by CORS policy" | Backend does not allow the page's origin; or backend crashed before adding headers | Network tab: response headers should include `access-control-allow-origin`. Check the **exact** origin (scheme, host, port) |
| Preflight (`OPTIONS`) fails | Custom headers or methods not allowed | Allow `Content-Type`, the methods you use |
| Request never shows in Network tab | Wrong URL/env var, or an effect never ran | `console.log(API_BASE)`; check dependency array |
| Effect fires twice in dev | StrictMode | Normal; ensure cleanup is correct |
| Data flickers or shows the wrong item | Race condition / stale response | Add abort/ignore in cleanup |
| `422` on submit | Payload shape or types wrong (e.g., quantity as a string) | Read the response body: `loc` shows the field |
| `TypeError: Failed to fetch` | Backend down, wrong port, mixed content (https page calling http), or CORS | Try `curl` to the API; see Network tab status |
| `Cannot read properties of undefined` | Rendering before data exists, or API shape changed | Guard on `status`/`data`; `console.log` the actual response |
| State not updating | Mutated state in place | Always create new arrays/objects |
| Infinite requests | Effect depends on an object/array recreated each render | Depend on primitive values, or memoize |

**Method:** open DevTools, Network tab; click the failing request; read **status**, **response body**, **request payload**, **headers**. Then reproduce the same request with `curl`. If `curl` works but the browser fails, suspect CORS or request headers. If both fail, it is the backend.

**Authentication basics for this frontend (conceptual).** Internal apps commonly use either session cookies (set by the backend, `credentials: "include"` on fetch, CORS must then allow credentials with an explicit origin) or a bearer token sent in the `Authorization` header. Either way the **backend must enforce authorization**; hiding a button in React is not security.

**Interview Q:** *"Your React component shows data for the wrong user after quickly switching. Why and how do you fix it?"*
**Strong answer:** "That is a race condition: responses can arrive out of order and the last response to arrive overwrites state, even if it belongs to an older request. I fix it by cancelling the previous request in the effect cleanup with an AbortController, or ignoring its result with a flag, so only the latest request can set state. I would add a test where the first response is delayed."

**Exercise 5.1** (answer key RX-1): add a "Refresh every 30 seconds" option to the dashboard with correct cleanup.

---

# Part 6: Scenario-Based Interview Questions (32)

> **Honesty note.** Only **Q9** paraphrases a question you remember from the interview **[Confirmed]**. Every other question is a **realistic practice question [Inferred]** based on the topics you reported. Do not treat them as questions SM Auto actually asked.
>
> **How to practice:** read only the question. Answer aloud for 2 minutes. Then compare with "Structured approach" and "Sample answer". Questions get harder: Level 1 (Q1-8) fundamentals, Level 2 (Q9-16) core scenarios, Level 3 (Q17-24) production thinking, Level 4 (Q25-32) design and trade-offs.

**Template for each question:** Testing → Approach → Sample answer → Code → Follow-ups → Weak answers.

---

## Level 1: Fundamentals in scenarios

### Q1 [Inferred] What is the output, and why?
```js
console.log('A');
setTimeout(() => console.log('B'), 0);
Promise.resolve().then(() => console.log('C'));
console.log('D');
```
- **Testing:** event loop, microtask vs macrotask.
- **Approach:** (1) run all sync code; (2) drain microtasks; (3) run one macrotask.
- **Sample answer:** "`A, D, C, B`. `A` and `D` are synchronous. The `.then` callback is a microtask and runs as soon as the stack is empty. The `setTimeout` callback is a macrotask, so it runs afterwards even with a 0 ms delay."
- **Follow-ups:** What changes if there is an `await` inside an async function before the log? What if the `.then` queues another `.then`?
- **Weak answers:** "`A, B, C, D` because they are in order" (ignores async); "`A, D, B, C` because timer is 0" (misses microtask priority).

### Q2 [Inferred] A colleague says "a Promise is just a callback." Respond.
- **Testing:** understanding of Promise states and chaining.
- **Approach:** compare: single settlement, composability, error propagation.
- **Sample answer:** "A Promise represents a future value in one of three states: pending, fulfilled or rejected, and it settles only once. Unlike a raw callback, I can chain with `.then`, which returns a new promise, propagate errors to one `.catch`, and combine several with `all` or `allSettled`. Callbacks can be called many times or never, and error handling is manual."
- **Follow-ups:** What does `.then` return? What happens if I throw inside `.then`?
- **Weak:** reciting states without explaining why they help.

### Q3 [Inferred] `fetch('/api/orders/99')` returns 404. Does your `catch` run?
- **Testing:** fetch semantics.
- **Sample answer:** "No. `fetch` fulfills for any HTTP response and rejects only for network failures or aborts. I check `res.ok`, throw an error with the status, and parse JSON only after that."
```js
const res = await fetch(url);
if (!res.ok) throw new Error(`HTTP ${res.status}`);
const data = await res.json();
```
- **Follow-ups:** How do you also add a timeout? How do you distinguish a 404 from a network error in the `catch`?
- **Weak:** "Yes, errors go to catch" (the most common wrong belief).

### Q4 [Inferred] This takes 900 ms. Why, and how do you make it faster?
```js
const user = await getUser(id);       // 300 ms
const orders = await getOrders(id);   // 300 ms
const stock = await getStock();       // 300 ms
```
- **Testing:** accidental sequential awaits.
- **Approach:** ask whether the calls depend on each other.
- **Sample answer:** "Each `await` pauses until that call finishes, so three independent calls run one after another: about 900 ms. If none needs another's result I start them together with `Promise.all` and the total is about 300 ms. If `getOrders` needs `user.id`, that one must stay sequential, and I would parallelize only the independent ones."
- **Follow-ups:** What if one fails? (Use `allSettled` if partial is acceptable.) What about 500 calls? (Limit concurrency, Q18.)
- **Weak:** "Use async/await" (it is already used).

### Q5 [Inferred] Python: your function runs but nothing happens, and you see "coroutine ... was never awaited". Why?
- **Testing:** coroutine vs task.
- **Sample answer:** "Calling an `async def` function returns a coroutine object; it does not run. I must `await` it, or schedule it with `asyncio.create_task`/`gather`. The warning appears because the coroutine object was created and discarded. The fix is `await send_email(...)`, or `create_task` if I really want it fire-and-forget (and then keep a reference and handle its exceptions, see Q31)."
- **Follow-ups:** What is the difference between `await f()` and `create_task(f())`?
- **Weak:** "Python async is buggy."

### Q6 [Inferred] When do you use `def` and when `async def` for a FastAPI endpoint?
- **Testing:** thread pool vs event loop.
- **Sample answer:** "`async def` runs on the event loop, so everything inside must be non-blocking: I use it with `httpx.AsyncClient` and async DB drivers. Plain `def` is run in a thread pool by FastAPI, so blocking libraries are fine. The danger is a blocking call inside `async def`, which freezes the loop for all users."
- **Follow-ups:** How would you call a blocking function from an `async def` endpoint? (`await asyncio.to_thread(fn)`.) How many threads does the pool have? (Default limit is on the order of 40; know that it is finite.)
- **Weak:** "`async def` is always faster."

### Q7 [Inferred] Your React component calls the API in `useEffect` and the network tab shows requests firing endlessly. What do you check?
- **Testing:** dependency array understanding.
- **Approach:** the effect re-runs when a dependency changes identity.
- **Sample answer:** "Usually the effect sets state that is also in its dependency array, or the dependency is an object/array recreated each render. A missing array also reruns after every render. I depend on primitives like `customerId`, move constant objects outside the component or memoize them, and make sure the effect does not set the value it depends on."
```js
useEffect(() => { fetchData(id); }, [id]);   // runs when id changes only
```
- **Follow-ups:** Why might it run twice in development? (StrictMode.) What does an empty array mean?
- **Weak:** "Put `[]` everywhere" (hides stale data bugs).

### Q8 [Inferred] A user submits `quantity: -5` to your FastAPI `POST /inquiries`. Who validates and what is returned?
- **Testing:** Pydantic, 422, trust boundaries.
- **Sample answer:** "FastAPI validates the body against my Pydantic model before my function runs. With `quantity: int = Field(ge=1)` the request is rejected with a `422` containing the field location and message. I also validate in React for fast feedback, but the server is the authority because clients can be bypassed. Business rules that need data (for example 'part number exists') are checked in the service layer and return 404 or 409/422 as appropriate."
- **Follow-ups:** What status for a duplicate record? (409.) Where do you check "stock must be sufficient"?
- **Weak:** "Validate in React only."

---

## Level 2: Core scenarios

### Q9 [Confirmed topic] "If you make asynchronous calls to three APIs and one fails while the other two succeed, how would you handle it?"
- **Testing:** concurrency, error isolation, requirement thinking, communication.
- **Structured approach:** (1) ask whether all three are essential; (2) call concurrently; (3) choose `allSettled`/`gather(return_exceptions=True)` for partial, `all`/`TaskGroup` for essential; (4) add timeouts; (5) report which parts failed; (6) log.
- **Sample answer:** "First I would clarify whether the feature is meaningful without all three. If a partial result is acceptable, for example a dashboard, I start the three calls together and collect outcomes without letting one failure reject the whole thing: in JavaScript `Promise.allSettled`, in Python `asyncio.gather(..., return_exceptions=True)`. I check each outcome's status, return the successful data plus an `errors` object and a `partial: true` flag, and show a warning in the UI. I give each call a timeout so a hung API cannot block the response, and log the failing service with its error and duration. If all three are essential, for example computing an invoice, I would fail fast with `Promise.all` or a `TaskGroup`, cancel the remaining calls, and return a clear error."
- **Code:** Part 2.5/2.8 (JS), Part 3.4/3.7 (Python), Part 4 (FastAPI).
- **Follow-ups:** Does `Promise.all` cancel the others? How do you tell success from failure in `gather` results?
- **Weak answers:** "try/catch" alone; "`Promise.all`" (loses two good results); "retry until it works".

### Q10 [Inferred] One API takes 10 seconds; two take 200 ms. The endpoint must answer within 2 seconds.
- **Testing:** timeouts, deadlines, partial results, cleanup.
- **Approach:** per-call timeout < overall deadline; concurrency; return partial; cancel stragglers.
- **Sample answer:** "I run all three concurrently and give each a hard timeout, say 1.5 s, under the 2 s SLA so I have time to build the response. The two fast calls return in 200 ms; the slow one is cut off and reported as `timeout`. I return 200 with `partial: true` if partial data is acceptable. Importantly, timing out must actually cancel the request: `AbortSignal.timeout` in `fetch`, `asyncio.timeout`/`wait_for` in Python, otherwise the slow call keeps consuming a connection."
- **Code:** Part 2.8 and Part 3.7 (verified at ~1.5 s with partial data).
- **Follow-ups:** Why not set the timeout to exactly 2 s? (No time left to respond.) What if the slow one is the main data? (Fail with 504.)
- **Weak:** "Increase the timeout" or "make the server faster" without handling the slow dependency.

### Q11 [Inferred] Two features: (a) a dashboard showing 3 widgets, (b) an invoice total built from 3 sources. Which combinator for each?
- **Sample answer:** "(a) `allSettled`, because widgets are independent and showing two is better than none; I label the missing one. (b) `all`, because a total computed from missing inputs is wrong, so it must fail rather than guess. The principle: partial results are appropriate only when the requirements allow them and when the missing data cannot cause a wrong decision."
- **Follow-ups:** What if the invoice source is slow? (Timeout and fail with 504, possibly retry idempotent reads.) 
- **Weak:** one answer for both.

### Q12 [Inferred] Your FastAPI endpoint uses `requests.get(...)` inside `async def`. Under load, all endpoints get slow. Why?
- **Testing:** blocking in the event loop.
- **Sample answer:** "`requests` is blocking. Inside `async def` it runs on the event loop thread, so while one request waits for the network the loop cannot serve anything else, including health checks. Fixes: use `httpx.AsyncClient` and `await`, or make the endpoint a plain `def` so it runs in the thread pool, or `await asyncio.to_thread(...)` for libraries I cannot replace. I would confirm by running asyncio debug mode or timing a trivial `/health` endpoint during a slow call."
- **Code:** Part 3.3 demonstration.
- **Follow-ups:** Is a blocking call in a plain `def` endpoint a problem? (Less, but it uses a pool thread; the pool is finite.)
- **Weak:** "Add more workers" (hides the cause).

### Q13 [Inferred] You applied a timeout in Python. What happens to the unfinished task?
- **Testing:** cancellation semantics.
- **Sample answer:** "With `asyncio.wait_for` or `asyncio.timeout`, the inner awaitable is cancelled: a `CancelledError` is raised into it at its next `await`, then I get `TimeoutError`. With `asyncio.wait(timeout=...)` nothing is cancelled; I receive `done` and `pending` sets and must cancel pending tasks and await them. Code that catches `CancelledError` should clean up and **re-raise**. Blocking code inside a task cannot be cancelled until it returns."
- **Code:** Part 3.7 Approach A.
- **Follow-ups:** What if a task ignores cancellation? What if a thread started with `to_thread` is running? (Thread cannot be killed; the `await` stops but the thread continues.)
- **Weak:** "The timeout kills it" (oversimplified).

### Q14 [Inferred] Design the React states for the dashboard that calls your partial-result API.
- **Sample answer:** "Five: loading, success, partial, empty, error. Partial shows the available sections and a visible warning naming what is missing with a retry. Empty means the call worked but there is nothing to display. Error covers network, timeout, 5xx, with a retry. I keep the existing data visible while reloading when possible, and I make sure loading and error states are announced accessibly (`role="status"`, `role="alert"`)."
- **Code:** Part 5.
- **Follow-ups:** How do you test each state? (Mock `fetch`.) How do you avoid flashing the spinner?
- **Weak:** only a `loading` flag.

### Q15 [Inferred] The user clicks customer 1, then quickly customer 2. They see customer 2's name with customer 1's orders. Why?
- **Testing:** race condition and stale responses.
- **Sample answer:** "The request for customer 1 was slower and arrived after the one for customer 2, overwriting state. The fix is to cancel or ignore outdated requests in the effect cleanup: an `AbortController` aborted on cleanup, or an `ignore` flag. I would add a test with a delayed first response."
```js
useEffect(() => {
  const c = new AbortController();
  fetch(`/api/customers/${id}`, { signal: c.signal }).then(r => r.json()).then(setData).catch(e => { if (e.name !== 'AbortError') setError(e); });
  return () => c.abort();
}, [id]);
```
- **Follow-ups:** What if the API is not abortable? (Use an `ignore` flag.) How does a data-fetching library help?
- **Weak:** "Add a delay" or "disable the buttons".

### Q16 [Inferred] The page works in Postman but fails in the browser with a CORS error. Explain.
- **Testing:** CORS understanding.
- **Sample answer:** "CORS is a browser rule: a page from one origin (scheme+host+port) can read responses from another origin only if the server's response includes the right `Access-Control-Allow-Origin` header. Postman and curl do not enforce it. The fix is on the server: allow the specific frontend origin (not `*` when using credentials). For non-simple requests (JSON content type, custom headers) the browser first sends an `OPTIONS` preflight that the server must answer. In development a proxy (Vite dev server proxy) avoids CORS entirely."
```python
app.add_middleware(CORSMiddleware, allow_origins=["http://localhost:5173"],
                   allow_methods=["GET", "POST"], allow_headers=["Content-Type"])
```
- **Follow-ups:** Is CORS a security feature for the server? (It protects users' browsers; it does not authenticate clients.) Why does a 500 sometimes look like a CORS error? (Error response lacking the headers.)
- **Weak:** "Disable CORS in the browser" or `allow_origins=["*"]` with no thought.

---

## Level 3: Production thinking

### Q17 [Inferred] A `POST /orders` call times out. Should the client retry?
- **Testing:** idempotency, retry policy.
- **Sample answer:** "A timeout does not tell me whether the server processed it. If I blindly retry, I may create a duplicate order. For safe retries the endpoint needs idempotency: the client sends an idempotency key, or I use a natural unique constraint, so a repeat returns the original result. Retries must be limited, use exponential backoff with jitter, apply only to retryable failures (network errors, timeouts, 502/503/504), never to 4xx, and fit inside the overall time budget. For idempotent GETs, one retry is usually fine."
- **Follow-ups:** How does a unique constraint help? What does the server return for a repeated key? 
- **Weak:** "Retry 3 times" with no thought about duplicates.

### Q18 [Inferred] You must call an internal API for 500 records. How?
- **Testing:** bounded concurrency.
- **Approach:** sequential is slow, `Promise.all` on 500 may overload the API or exhaust connections; use a limit.
- **Sample answer:** "I use a concurrency limit, say 5 to 10 at once, matching what the service can handle, and collect results with per-item error handling so one failure does not abort the batch."
```js
async function mapLimit(items, limit, fn) {
  const results = new Array(items.length);
  let next = 0;
  async function worker() {
    while (true) {
      const i = next++;
      if (i >= items.length) return;
      results[i] = await fn(items[i], i);
    }
  }
  await Promise.all(Array.from({ length: Math.min(limit, items.length) }, worker));
  return results;
}
```
```python
sem = asyncio.Semaphore(10)
async def limited(i):
    async with sem:
        return await call(i)
results = await asyncio.gather(*(limited(i) for i in ids), return_exceptions=True)
```
**Verified:** 20 items with limit 5 gave a peak concurrency of exactly 5 in both languages.
- **Follow-ups:** What if the API returns 429? (Respect `Retry-After`, back off.) Is there a batch endpoint?
- **Weak:** `Promise.all` of everything.

### Q19 [Inferred] "My loop saved all records, but the success message appears before saving finishes." Find the bug.
```js
items.forEach(async (item) => { await save(item); });
console.log('All saved!');
```
- **Sample answer:** "`forEach` ignores the promises returned by its async callback, so it does not wait. Use `for...of` with `await` to save sequentially, or `await Promise.all(items.map(save))` to save concurrently (with a limit for large batches, and `allSettled` if I want to know which failed)."
- **Follow-ups:** When should it be sequential? (Order matters, or each save depends on the previous, or protecting a rate limit.)
- **Weak:** "Add `await` before `forEach`" (does nothing).

### Q20 [Inferred] A FastAPI endpoint generates a large Excel report (CPU heavy, 20 s). Other endpoints become unresponsive. What do you do?
- **Testing:** CPU-bound vs I/O-bound, architecture.
- **Sample answer:** "CPU-bound work holds the loop in an `async def`, or occupies a thread pool slot in a `def`; and threads do not give CPU parallelism because of the GIL. For something taking 20 s I would not do it inside the request at all: accept the request, create a job record, run it in a worker process (a process pool or a separate worker), return `202 Accepted` with a job id, and let the UI poll for status or download when ready. For smaller CPU tasks `ProcessPoolExecutor` via `loop.run_in_executor` is enough."
- **Follow-ups:** What does the job table need? (id, status, created, result path, error.) What happens to jobs if the server restarts? (Persist state; decide on re-run/cleanup.)
- **Weak:** "Make the function `async`."

### Q21 [Inferred] Users report the dashboard is slow, but only around 9 a.m. How do you investigate?
- **Testing:** systematic production debugging.
- **Approach (say it as steps):** clarify, reproduce or find evidence, narrow by layer, test hypotheses, fix, verify, prevent.
- **Sample answer:** "I would first define 'slow': which endpoint, how slow, for whom. From logs I look at request duration by endpoint and per-upstream durations around 9 a.m. (this is why I log `service` and `duration_ms`). Hypotheses: the peak causes upstream slowness, connection pool exhaustion, database load from another job such as a nightly or start-of-shift report, or a blocked event loop. I test each: compare upstream durations, check pool timeouts in logs, check for a scheduled job at that time, check DB slow queries and indexes. Then fix the cause (cache, index, timeout/bulkhead, reschedule the batch job) and verify with the same measurements. I would not guess or restart repeatedly without evidence."
- **Follow-ups:** What if there are no logs of durations? (Add them first, then wait for the next occurrence; meanwhile reproduce with a load test.)
- **Weak:** "Add more servers" or "restart it".

### Q22 [Inferred] You see `httpx.PoolTimeout` errors in the logs. What does it mean?
- **Sample answer:** "All connections in the client's pool were in use and a request waited too long for a free one. Causes: too many concurrent calls without a limit, slow upstreams holding connections, or creating clients incorrectly. Fixes: limit concurrency with a semaphore, set realistic timeouts so slow calls release connections, tune pool limits (`httpx.Limits`), share one client, and fix the slow upstream. Raising limits alone just moves the problem."
- **Follow-ups:** Why not create a client per request? (No connection reuse; overhead; can leak.)
- **Weak:** "Increase the timeout" without understanding.

### Q23 [Inferred] A user double-clicks "Submit" and two identical inquiries appear. Fix it end to end.
- **Testing:** defense in depth.
- **Sample answer:** "Layer 1, the UI: disable the button while submitting and ignore re-entrant submits. Layer 2, the server: use an idempotency key or a unique constraint on the business key so duplicates are rejected or return the existing record (409 or the original 201). Layer 3, the database: a real unique index, because application checks race. The UI fix alone is not enough: retries, two tabs and network re-sends bypass it."
- **Follow-ups:** Why is 'check then insert' unsafe? (Two requests can both pass the check; the constraint is the only safe guard.)
- **Weak:** UI-only fix.

### Q24 [Inferred] Python: you launch a background task with `asyncio.create_task(job())` and later discover it failed silently. Why, and what do you do?
- **Testing:** task lifecycle, exceptions.
- **Sample answer:** "An exception inside a task that nobody awaits is only reported when the task is garbage-collected ('Task exception was never retrieved'), and without a stored reference the task may even be collected early. I keep a strong reference in a set, add a done callback that logs the exception, and for important work use a proper job mechanism with status."
```python
background = set()
def spawn(coro, name):
    task = asyncio.create_task(coro, name=name)
    background.add(task)
    def done(t):
        background.discard(t)
        if not t.cancelled() and (exc := t.exception()):
            log.error("background task %s failed: %r", name, exc)
    task.add_done_callback(done)
    return task
```
**Verified:** the failure is logged as `background task nightly-report failed: ValueError('report failed')`.
- **Follow-ups:** What does FastAPI's `BackgroundTasks` do and not do? (Runs after the response in the same process; no retries, lost on restart.)
- **Weak:** "Wrap in try/except" at the call site (the exception does not occur there).

---

## Level 4: Design, security and trade-offs

### Q25 [Inferred] Users upload a 50,000-row CSV from the ERP to be imported. Design it.
- **Sample answer:** "I would validate the file type and size first, then not process synchronously in the request. Store the upload, create an import job (`status: queued`), return `202` with a job id. A worker processes in chunks inside transactions, validating each row, collecting row-level errors rather than failing everything unless the business requires all-or-nothing. The UI polls job status and shows counts and an error report for download. Re-running must be safe (idempotent upserts keyed on a business id). Log counts, not row contents, given the data sensitivity."
- **Follow-ups:** All-or-nothing versus partial import: how decide? What about the file being edited and re-uploaded?
- **Weak:** read everything into memory in the handler.

### Q26 [Inferred] A client closes the browser while your endpoint is awaiting three upstream calls. What happens?
- **Sample answer:** "It depends on the stack. In many ASGI setups the handler may keep running until it completes unless the framework cancels it when the client disconnects, so I should not assume cancellation. For long operations I can check `await request.is_disconnected()` periodically and stop early, and ensure upstream calls carry timeouts so abandoned work ends. For writes, I must design so partial completion is safe (transaction, idempotency). I would test the behavior in my environment rather than assume."
- **Follow-ups:** Why does it matter for side effects like sending emails? 
- **Weak:** "It stops automatically."

### Q27 [Inferred] How would you protect internal endpoints in a private network app?
- **Sample answer:** "Private network is not a substitute for authentication: any compromised machine or curious insider on the LAN can call the API. I require login (corporate SSO if available, otherwise a session or short-lived token), hash passwords properly if I manage them (bcrypt/argon2, never plain text or fast hashes), enforce **authorization** per endpoint and per record (roles; a user must not read another department's data by changing an id in the URL), use HTTPS even internally, rate-limit login, and log access to sensitive actions. Secrets come from environment/secret store, not source control."
- **Follow-ups:** Authentication vs authorization? JWT vs session cookie trade-offs? (Cookie: easier revocation, CSRF concerns; JWT: stateless, harder to revoke, store carefully.)
- **Weak:** "It is internal, so no auth is needed."

### Q28 [Inferred] The app works on your laptop. After deploying to a company server, the React app cannot reach the API. Debug.
- **Sample answer:** "Likely candidates: the frontend still points to `localhost:8000` (which now means the *user's* machine, not the server); wrong port or firewall rule; the API bound to `127.0.0.1` instead of `0.0.0.0`; CORS origin mismatch with the new hostname; mixed content if the page is HTTPS and the API is HTTP; reverse proxy path not forwarded. I check from the user's machine: can I `curl` the API URL? From the server: is the process listening (`ss -ltnp`)? Then browser Network tab for status and CORS headers. The fix: configure the API base URL per environment (build-time variable or same-origin reverse proxy), and set allowed origins."
- **Follow-ups:** Why is a reverse proxy (Nginx) useful here? (Single origin, TLS, no CORS.)
- **Weak:** "Works on my machine."

### Q29 [Inferred] You notice the production database password is committed in the repository. What do you do?
- **Sample answer:** "Treat it as compromised. Tell the responsible person immediately; rotate the password first (removing it from git history does not un-leak it), update the deployment's configuration via environment variables or a secrets store, remove the secret from the code and from history if policy requires (and know that clones may still hold it), check access logs for misuse, add a pre-commit secret scanner and `.gitignore` for `.env`, and write down what happened. I would not try to quietly hide it."
- **Follow-ups:** What about a key in a front-end bundle? (Anything shipped to the browser is public.)
- **Weak:** "Delete the file and push."

### Q30 [Inferred] A user reports "the stock shown is yesterday's." No error anywhere. How do you debug without AI help?
- **Sample answer:** "Reproduce with the user's exact customer id. Compare the API's JSON with the source system directly: if the API is stale, look for caching (HTTP cache headers, server-side cache TTL, browser cache, CDN or proxy), a stale replica, a failing sync job that stopped; if the API is right and the UI is wrong, look at frontend caching or stale state. Check timestamps in the response, add a `fetched_at` field if missing, check the job scheduler logs. Fix the cause and add monitoring or an alert for staleness."
- **Follow-ups:** Which layers can cache? (Browser, proxy, server, DB read replica.)
- **Weak:** "Clear the cache" without finding where.

### Q31 [Inferred] A teammate wants to make everything `async`. Argue for and against.
- **Sample answer:** "For: high concurrency for I/O-heavy endpoints, fewer threads, natural fit with `httpx` and async drivers. Against: it is contagious (everything that awaits must be async), blocking libraries become dangerous, debugging is harder, and CPU-bound work gets no benefit. A small internal tool with a handful of users and a blocking ORM may be perfectly served by plain `def` endpoints in the thread pool. I would use `async def` where I fan out to external services or hold many connections, and keep the rest simple. I decide based on measured needs, and I would write the guideline down for the team."
- **Follow-ups:** What measurement would change your mind? (Latency under expected load; thread pool saturation.)
- **Weak:** "Async is modern, so always."

### Q32 [Inferred] Your manager (non-technical) asks why the dashboard sometimes shows "Orders unavailable". Explain and propose improvements.
- **Sample answer:** "The dashboard collects data from three systems at once. If one is slow or down, we deliberately show the rest instead of a blank page, and we tell the user which part is missing. It happens when the orders system takes longer than 1.5 seconds or returns an error. I will look at the logs to see how often and when; options are asking the orders team to investigate, caching the last good value with a clear 'as of' time, and adding an alert if failures exceed a threshold. I will report back with numbers by a specific day."
- **Testing:** communication, ownership, trade-off explanation.
- **Follow-ups:** What would you cache and what would you never cache? How would you decide the acceptable failure rate?
- **Weak:** jargon-heavy answer, or blaming the other team without data.

---

## Pattern cheat sheet for Part 6

| If the question mentions… | Think… |
|---|---|
| "one fails, others succeed" | `allSettled` / `gather(return_exceptions=True)`, partial flag, requirement check |
| "must respond within N seconds" | per-call timeouts under the deadline, cancellation, partial |
| "slow for everyone" | event loop blocked, pool exhaustion |
| "wrong data shown" | race condition, caching, stale state |
| "duplicate records" | idempotency, unique constraint, disable button |
| "works locally, fails deployed" | URLs, bind address, firewall, CORS, env vars |
| "retry" | idempotency, limits, backoff, deadline |
| "CPU heavy / long running" | background job, process pool, 202 + polling |
| "secret / security" | rotate, env vars, least privilege, authorization not just authentication |

---

# Part 7: Full-Stack Mini Project

## 7.1 Project: "Parts Inquiry Desk" (internal web app)

> **Honesty note.** This is a practice project invented for learning. It uses the interview facts only loosely (internal business app, FastAPI + React, multiple backend services, private network). It does not describe SM Auto's real systems.

### Problem statement
A small manufacturing company's sales staff need an internal web page where they can:
1. **Look up a customer** and see a dashboard with data from three internal services (profile, orders, stock). The dashboard must stay usable if one service is slow or down.
2. **Submit a parts inquiry** (customer name, part number, quantity, notes) that is stored in a database, protected against accidental duplicates.

### Scope (kept small on purpose)
Technologies: React + Vite, FastAPI, httpx, SQLite (standard library `sqlite3`), pytest, Vitest. **Nothing else.** No ORM, no message queue, no Docker required (Docker is optional in 7.9).

### Built on earlier parts
| Piece | Where it was built |
|---|---|
| Dashboard aggregator, timeouts, partial results | Part 4 (reused unchanged) |
| React dashboard hook and component | Part 5 (reused) |
| Inquiry form | Part 5 (small changes below) |
| SQLite persistence, idempotency, API key | **New in this part** |

## 7.2 Folder structure

```
parts-inquiry-desk/
├── backend/
│   ├── requirements.txt
│   ├── mock_upstreams.py          # stands in for the three internal services
│   ├── app/
│   │   ├── __init__.py
│   │   ├── config.py
│   │   ├── schemas.py
│   │   ├── services.py            # per-service call + timeout
│   │   ├── db.py                  # NEW: sqlite connection + schema
│   │   ├── auth.py                # NEW: API key dependency
│   │   ├── inquiries.py           # NEW: inquiry router (plain def, blocking sqlite)
│   │   └── main.py                # app, CORS, middleware, dashboard route
│   └── tests/
│       ├── test_api.py            # dashboard tests (Part 4)
│       └── test_inquiries.py      # NEW
└── frontend/
    ├── package.json  vite.config.js  index.html
    └── src/ api.js  useDashboard.js  Dashboard.jsx  InquiryForm.jsx  App.jsx  main.jsx
```

## 7.3 API contract

**Authentication (this lab):** header `X-API-Key: <shared key>` on `/api/inquiries*`. (See the limitation in 7.8: a key inside browser code is not secret.)

| Method & path | Request | Success | Errors |
|---|---|---|---|
| `GET /api/customers/{id}/dashboard?strict=false` | `id` positive integer | `200` `{customer_id, partial, data{profile?,orders?,stock?}, errors{service: reason}, elapsed_ms}` | `422` bad id; `503` all services failed; `502` if `strict=true` and any failed |
| `POST /api/inquiries` | JSON `{customer_name 2-100 chars, part_number "AA-1234", quantity 1-10000, notes? ≤500}`; optional header `Idempotency-Key` (≤64 chars) | `201` created `{id, ...}`; `200` the original record if the key was already used | `401` bad/missing key; `422` validation (field list) |
| `GET /api/inquiries/{id}` | | `200` record | `401`, `404` |

**Error body format:** FastAPI returns `{"detail": ...}`. For `422`, `detail` is a list of `{loc, msg, type}`; for 502/503 it is `{message, errors}`.

## 7.4 Implementation steps (do them in this order)

1. Start from the Part 4 backend; run its tests (6 dashboard-related tests pass).
2. Add `db.py`, then `inquiries.py`, `auth.py`; remove the in-memory inquiry endpoints from `main.py`; include the router.
3. Delete `test_inquiry_validation` from `tests/test_api.py` (the in-memory endpoint it tested no longer exists), add `tests/test_inquiries.py`, and make all tests pass (**12 passed** in the verified version; **13** after you add the pagination test from exercise RX-4).
4. Run backend + mock upstreams; use `/docs` to try the endpoints.
5. Build the React app from Part 5. Add the two headers to the form (7.6).
6. Test the five dashboard states manually by restarting the mock with `FAIL_SERVICES` / `SLOW_SERVICES`.
7. Run through the testing checklist (7.7) and the debugging exercises (7.9).
8. Practice the private deployment steps (Part 8) on a second machine or VM if possible.

## 7.5 Important new backend code

Set the key before running or testing: `export APP_API_KEY="choose-a-long-random-value"` (PowerShell: `$env:APP_API_KEY="..."`).

**`app/db.py`**

```python
import os
import sqlite3
from contextlib import contextmanager

DB_PATH = os.getenv("DB_PATH", "inquiries.db")

SCHEMA = """
CREATE TABLE IF NOT EXISTS inquiries (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name   TEXT    NOT NULL,
    part_number     TEXT    NOT NULL,
    quantity        INTEGER NOT NULL CHECK (quantity BETWEEN 1 AND 10000),
    notes           TEXT,
    idempotency_key TEXT    UNIQUE,                       -- the real guard against duplicates
    created_at      TEXT    NOT NULL DEFAULT (datetime('now'))
);
"""


def init_db(path: str | None = None) -> None:
    with connect(path) as conn:
        conn.executescript(SCHEMA)


@contextmanager
def connect(path: str | None = None):
    conn = sqlite3.connect(path or DB_PATH)
    conn.row_factory = sqlite3.Row
    try:
        yield conn
        conn.commit()                 # commit only if the block succeeded
    except Exception:
        conn.rollback()               # otherwise undo partial work
        raise
    finally:
        conn.close()
```

**`app/auth.py`**

```python
import hmac
import os

from fastapi import Header, HTTPException


def require_api_key(x_api_key: str | None = Header(default=None)) -> None:
    """Very simple shared-key check for a small internal tool.
    Real apps usually add per-user login and roles on top (authorization)."""
    expected = os.getenv("APP_API_KEY")
    if not expected:                                              # fail closed: misconfiguration must not open the API
        raise HTTPException(500, "server auth is not configured")
    if x_api_key is None or not hmac.compare_digest(x_api_key, expected):   # constant-time comparison
        raise HTTPException(401, "invalid or missing API key")
```

**`app/inquiries.py`**

```python
import sqlite3

from fastapi import APIRouter, Depends, Header, HTTPException, Response

from . import db
from .auth import require_api_key
from .schemas import InquiryCreate, InquiryOut

router = APIRouter(prefix="/api/inquiries", dependencies=[Depends(require_api_key)])


def _row_to_out(row: sqlite3.Row) -> InquiryOut:
    return InquiryOut(id=row["id"], customer_name=row["customer_name"], part_number=row["part_number"],
                      quantity=row["quantity"], notes=row["notes"])


# Plain `def`: sqlite3 is a BLOCKING driver, so FastAPI runs this in its thread pool
# instead of freezing the event loop.
@router.post("", response_model=InquiryOut, status_code=201)
def create_inquiry(body: InquiryCreate, response: Response,
                   idempotency_key: str | None = Header(default=None, max_length=64)):
    with db.connect() as conn:
        if idempotency_key:
            existing = conn.execute("SELECT * FROM inquiries WHERE idempotency_key = ?", (idempotency_key,)).fetchone()
            if existing:                                  # same key seen before: return the original, create nothing
                response.status_code = 200
                return _row_to_out(existing)
        try:
            cur = conn.execute(
                "INSERT INTO inquiries (customer_name, part_number, quantity, notes, idempotency_key) "
                "VALUES (?, ?, ?, ?, ?)",                 # parameters (?) prevent SQL injection: never build SQL with f-strings
                (body.customer_name, body.part_number, body.quantity, body.notes, idempotency_key),
            )
        except sqlite3.IntegrityError:                    # two simultaneous requests with the same key: the UNIQUE index wins
            row = conn.execute("SELECT * FROM inquiries WHERE idempotency_key = ?", (idempotency_key,)).fetchone()
            response.status_code = 200
            return _row_to_out(row)
        row = conn.execute("SELECT * FROM inquiries WHERE id = ?", (cur.lastrowid,)).fetchone()
        return _row_to_out(row)


@router.get("/{inquiry_id}", response_model=InquiryOut)
def get_inquiry(inquiry_id: int):
    with db.connect() as conn:
        row = conn.execute("SELECT * FROM inquiries WHERE id = ?", (inquiry_id,)).fetchone()
    if row is None:
        raise HTTPException(404, "inquiry not found")
    return _row_to_out(row)
```


**`app/main.py` changes (compared to Part 4)**
- Remove the in-memory `_inquiries` dictionary and its two routes.
- Add `from . import db` and `from .inquiries import router as inquiries_router`.
- In `lifespan`, call `db.init_db()` before creating the client.
- At the bottom: `app.include_router(inquiries_router)`.

**Why these choices (be ready to explain)**

| Choice | Reason |
|---|---|
| `def` (not `async def`) for inquiry routes | `sqlite3` is blocking. Plain `def` runs in the thread pool, so it cannot freeze the event loop. |
| Parameterized SQL (`?`) | Prevents SQL injection; a test proves a malicious name is stored as plain text. |
| `UNIQUE` on `idempotency_key` | A check-then-insert is racy; only the database constraint is a true guard. The `IntegrityError` branch handles the race. |
| `CHECK (quantity BETWEEN 1 AND 10000)` | Defense in depth: even a buggy code path cannot store bad data. |
| `connect()` commits or rolls back | A failed request never leaves half-written data. |
| `hmac.compare_digest` | Constant-time comparison avoids timing leaks. |
| Auth fails closed if `APP_API_KEY` is unset | Misconfiguration must not silently open the API. |
| 200 vs 201 for repeated key | Tells the client "this already existed" while giving the same body. |

**Known limits of SQLite (state them if asked):** one writer at a time, fine for a small internal tool; for many concurrent writers or multi-server deployment move to MySQL/PostgreSQL (you already know MySQL from your background).

### Tests added

**`tests/test_inquiries.py`**

```python
import os

import pytest
from fastapi.testclient import TestClient

os.environ["APP_API_KEY"] = "test-key"

from app import db  # noqa: E402
from app.main import app  # noqa: E402

GOOD = {"customer_name": "Asha", "part_number": "BR-1042", "quantity": 5}
H = {"X-API-Key": "test-key"}


@pytest.fixture
def client(tmp_path, monkeypatch):
    monkeypatch.setattr(db, "DB_PATH", str(tmp_path / "test.db"))
    with TestClient(app) as c:
        yield c


def test_requires_api_key(client):
    assert client.post("/api/inquiries", json=GOOD).status_code == 401
    assert client.post("/api/inquiries", json=GOOD, headers={"X-API-Key": "wrong"}).status_code == 401


def test_create_and_read(client):
    r = client.post("/api/inquiries", json=GOOD, headers=H)
    assert r.status_code == 201
    got = client.get(f"/api/inquiries/{r.json()['id']}", headers=H)
    assert got.status_code == 200 and got.json()["part_number"] == "BR-1042"


def test_validation(client):
    assert client.post("/api/inquiries", json={**GOOD, "quantity": 0}, headers=H).status_code == 422


def test_idempotency_key_prevents_duplicates(client):
    h = {**H, "Idempotency-Key": "abc-123"}
    first = client.post("/api/inquiries", json=GOOD, headers=h)
    second = client.post("/api/inquiries", json=GOOD, headers=h)
    assert first.status_code == 201 and second.status_code == 200
    assert first.json()["id"] == second.json()["id"]


def test_sql_injection_attempt_is_just_data(client):
    evil = {**GOOD, "customer_name": "x'); DROP TABLE inquiries;--"}
    assert client.post("/api/inquiries", json=evil, headers=H).status_code == 201
    assert client.post("/api/inquiries", json=GOOD, headers=H).status_code == 201   # table still exists


def test_not_found(client):
    assert client.get("/api/inquiries/999", headers=H).status_code == 404
```


Run: `pytest -q` from `backend/`. **Verified: 12 passed** (6 dashboard tests + 6 inquiry tests).

## 7.6 Frontend changes

In `InquiryForm.jsx`, generate **one idempotency key per submission attempt**, reuse it if the user retries after a network failure, and renew it after success:

```jsx
import { useRef } from "react";

// crypto.randomUUID only exists in "secure contexts" (https or localhost).
// On http://192.168.x.x (a plain-http LAN address) it is undefined: use a fallback.
const newKey = () => (globalThis.crypto?.randomUUID?.() ?? `${Date.now()}-${Math.random().toString(16).slice(2)}`);

// inside the component:
const keyRef = useRef(newKey());

// in onSubmit's apiFetch call:
const saved = await apiFetch("/api/inquiries", {
  method: "POST",
  headers: { "Idempotency-Key": keyRef.current, "X-API-Key": import.meta.env.VITE_DEV_API_KEY },
  body: JSON.stringify({ ...form, quantity: Number(form.quantity), notes: form.notes || null }),
});
keyRef.current = newKey();     // success: the next submission is a NEW inquiry
```
Add `VITE_DEV_API_KEY=...` to `frontend/.env.local` for local practice only.

## 7.7 Error-handling strategy

| Failure | Where handled | User sees | Logged |
|---|---|---|---|
| One upstream slow/failing | `call_service` returns a failed result | Dashboard with a "unavailable: Orders" warning | warning with service, duration, reason |
| All upstreams failing | `503` from the route | Error panel + Retry | warning per service |
| Invalid input | Pydantic `422` | Field-level messages | nothing (normal) |
| Duplicate submit | UI guard + idempotency key + unique index | Single record, no error | none |
| Bad/missing key | `401` | "Not authorized" | info (watch for spikes) |
| Unexpected exception | FastAPI `500` | Generic "Something went wrong" | `log.exception` with stack trace |
| Browser can't reach API | `fetch` rejects with `TypeError` | "Server unreachable" + Retry | browser console |

**Principles:** never leak stack traces or internal URLs to users; always return a stable JSON error shape; log enough to debug (request id, service, duration), never secrets or full payloads.

## 7.8 Security and configuration considerations

- **Secrets in environment variables**, never in code or git. `.env` files are gitignored. Provide `.env.example` with fake values.
- **The shared API key is not safe inside browser code.** Anything in the bundle (including `VITE_*` variables) is public to every user who opens DevTools. The key here exists to practice the pattern and to protect scripts and server-to-server calls. For human users you need **per-user login** (SSO or session/token), with **authorization** (roles; users must only see data they are allowed to see). Stretch goal: add login and a `users` table with hashed passwords (`argon2`/`bcrypt`).
- **CORS:** explicit allowed origins from config (`ALLOWED_ORIGINS`), no wildcard.
- **Input validation** at the edge (Pydantic) plus DB constraints.
- **SQL:** parameters only.
- **HTTPS** even on the LAN if sensitive data crosses the network (a reverse proxy can terminate TLS).
- **Logging:** ids and statuses, not contents of inquiries.
- **Backups:** the SQLite file is the data; copy it with `sqlite3 inquiries.db ".backup backup.db"` (a consistent copy while running), store off-machine, and test a restore.
- **Dependencies:** pin versions in `requirements.txt` for production; update deliberately.

## 7.9 Testing checklist

**Backend (automated)**
- [ ] All three upstreams succeed
- [ ] One upstream returns 500 → partial
- [ ] One upstream hangs → timeout, response in under ~2 s, partial
- [ ] All fail → 503
- [ ] `strict=true` with one failure → 502
- [ ] Invalid customer id → 422
- [ ] Inquiry validation failures → 422
- [ ] Missing/wrong key → 401
- [ ] Duplicate idempotency key → one row
- [ ] SQL-injection-looking input stored safely

**Frontend (automated + manual)**
- [ ] Loading → success; partial banner; empty; error with retry (Vitest covers these)
- [ ] Rapid id change never shows stale data (Vitest covers this)
- [ ] Form: client errors; server 422 mapped to fields; double click creates one record
- [ ] Manual: restart the mock with `FAIL_SERVICES=stock`, then with `SLOW_SERVICES=orders`, and watch the UI

**Non-functional (manual)**
- [ ] Stop the backend; UI shows a friendly error, not a blank page
- [ ] Open DevTools Network: verify no secrets other than the lab key appear in URLs
- [ ] Restart the backend: data persists

## 7.10 Debugging exercises (find and fix; solutions in Part 9, answer key DBG-1 to DBG-5)

1. **DBG-1 (JS):** The dashboard never shows the partial warning even when the backend returns `partial: true`. (A seeded bug: status computed with `body.partial ? "success" : ...` reversed.)
2. **DBG-2 (Python):** The endpoint returns in 9 seconds when one upstream is slow, ignoring the 1.5 s budget. (Seeded bug: `call_service` creates `httpx.Timeout(...)` only for the read phase and no `asyncio.timeout`; a trickling server never trips it.)
3. **DBG-3 (React):** The dashboard flickers between two customers. (Seeded bug: effect has no cleanup.)
4. **DBG-4 (Python):** Inquiries created twice. (Seeded bug: the idempotency key check happens but there is no UNIQUE constraint, and two parallel requests both pass.)
5. **DBG-5 (deployment):** After deployment, the UI shows "Server unreachable". (Seeded bug: `API_BASE` still `localhost:8000`; also API bound to `127.0.0.1`.)

## 7.11 Deployment considerations for a private server (summary; details in Part 8)

- Build the frontend (`npm run build`) and serve `dist/` from a web server (Nginx) or from FastAPI's static files.
- Run the backend with `uvicorn` under a process manager (systemd, or Docker with a restart policy), bound to `127.0.0.1` behind the reverse proxy, which listens on the LAN address.
- Put frontend and API on the **same origin** through the proxy (`/` → static files, `/api` → uvicorn) so there is no CORS to configure.
- Set `APP_API_KEY`, `UPSTREAM_BASE_URL`, `ALLOWED_ORIGINS`, `DB_PATH` via the service's environment file with restrictive permissions.
- Logs to a file with rotation, plus a `/health` endpoint for checks.
- Back up the database on a schedule; test restore.

---

# Part 8: Private Deployment and Working in a Small IT Team

> **What this Part is and is not.** From the interview you only know that internal apps may run on local servers, private servers or a private network. This Part teaches general, widely used practice. **It does not assume SM Auto's actual infrastructure, operating system, policies or tools.** The configuration files below are **templates to adapt**; the Python and Node code elsewhere in this guide was executed, but the Nginx, systemd and firewall snippets here were **not run in the environment where this guide was written**, so test them on a practice machine first.

## 8.1 Localhost vs LAN vs private server

| Level | Who can reach it | Typical address | Used for | Watch out for |
|---|---|---|---|---|
| **localhost** | Only your own machine | `127.0.0.1` / `localhost` | Development | "Works on my machine" |
| **LAN (a PC or server on the office network)** | Anyone on the same network who can route to it | `192.168.x.x` / `10.x.x.x` / hostname | Small internal tools | No HTTPS by default, firewall, DHCP address changes, anyone on LAN can try it |
| **Private server (managed machine/VM, maybe domain name, reverse proxy, TLS, backups)** | Authorized users inside the network (maybe via VPN) | `https://tools.company.local` | Production-style internal apps | Patching, monitoring, access control, ownership |

**The key trap:** `localhost` always means *the machine running the code*. A React app served from a server but configured with `API_BASE = "http://localhost:8000"` makes each **user's browser** look for the API on **the user's own computer**. This is the single most common "works locally, fails deployed" cause.

**Bind address:** `uvicorn --host 127.0.0.1` accepts only local connections (good behind a reverse proxy). `--host 0.0.0.0` listens on all network interfaces (reachable from the LAN, so the firewall matters).

## 8.2 Frontend/backend communication in a private network

Two common setups:

**A. Same origin through a reverse proxy (recommended).** The browser talks only to `https://tools.company.local`. The proxy serves the React build at `/` and forwards `/api/` to uvicorn on `127.0.0.1:8000`. No CORS configuration is needed because the origin is the same, there is one TLS certificate, and the API port is not exposed.

```nginx
# TEMPLATE (not executed in this guide's environment). /etc/nginx/conf.d/inquiry-desk.conf
server {
    listen 443 ssl;
    server_name tools.company.local;

    ssl_certificate     /etc/ssl/certs/inquiry-desk.crt;      # internal CA or self-signed, per company policy
    ssl_certificate_key /etc/ssl/private/inquiry-desk.key;

    root /var/www/inquiry-desk/dist;                           # output of `npm run build`
    index index.html;

    location /api/ {
        proxy_pass http://127.0.0.1:8000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 10s;                                # do not let hung upstreams hold connections forever
    }

    location / {
        try_files $uri /index.html;                            # SPA fallback for client-side routes
    }
}
```

**B. Separate origins** (`http://app-host:5173` for UI, `http://api-host:8000` for API). Then CORS must allow the exact UI origin, and the frontend needs the API base URL at build time (`VITE_API_BASE`). Works, but more to misconfigure.

**Debugging the connection (from the user's machine, then the server):**
```bash
curl -i https://tools.company.local/api/customers/1/dashboard      # from a client machine
curl -i http://127.0.0.1:8000/health                               # on the server, bypassing the proxy
ss -ltnp | grep 8000                                               # Linux: is something listening, on which address?
```
If the server call works but the client call fails, suspect the proxy, firewall, DNS/hostname, or TLS. If both fail, it is the app.

## 8.3 Environment variables and secrets

**Rules**
1. No secrets (passwords, API keys, tokens, DB credentials) in source code or git. Ever.
2. Configuration that differs per environment (URLs, ports, origins, DB path) comes from environment variables or a config file **outside** the repository.
3. Frontend variables (`VITE_*`) are baked into the public JavaScript bundle: **never put secrets there**.
4. Restrict file permissions on env files (readable only by the service user).
5. Commit `.env.example` with fake values so teammates know what to set.
6. Rotate a secret if it was exposed; removing it from git history is not enough.

```ini
# /etc/inquiry-desk/env   (TEMPLATE; chmod 600, owned by the service user)
UPSTREAM_BASE_URL=http://orders-internal:9000
PER_SERVICE_TIMEOUT_S=1.5
ALLOWED_ORIGINS=https://tools.company.local
DB_PATH=/var/lib/inquiry-desk/inquiries.db
APP_API_KEY=<generated long random value, not committed>
```

```ini
# /etc/systemd/system/inquiry-desk.service   (TEMPLATE, Linux with systemd)
[Unit]
Description=Inquiry Desk API
After=network.target

[Service]
User=inquirydesk
WorkingDirectory=/opt/inquiry-desk/backend
EnvironmentFile=/etc/inquiry-desk/env
ExecStart=/opt/inquiry-desk/.venv/bin/uvicorn app.main:app --host 127.0.0.1 --port 8000
Restart=on-failure

[Install]
WantedBy=multi-user.target
```
```bash
sudo systemctl daemon-reload
sudo systemctl enable --now inquiry-desk
journalctl -u inquiry-desk -f          # follow logs
```
(Windows servers and Docker use different mechanisms: Windows Service/NSSM/Task Scheduler, or `docker run --restart unless-stopped --env-file ...`. Ask the team what they use rather than assuming.)

## 8.4 Authentication and authorization basics

| Term | Question it answers | Example |
|---|---|---|
| **Authentication** | *Who are you?* | Login with username/password, SSO, token |
| **Authorization** | *What may you do?* | Sales can read inquiries; only managers can delete |

**Approaches commonly used for internal apps**
- **Corporate SSO** (for example the company's existing identity system) when available: best, because there are no new passwords to manage.
- **Session cookie:** server stores the session; the cookie is `HttpOnly`, `Secure`, `SameSite`. Easy to revoke; needs CSRF thought for state-changing requests.
- **Token (JWT) in the `Authorization: Bearer` header:** stateless; harder to revoke; keep lifetimes short; do not store sensitive tokens where scripts can read them without understanding the XSS trade-off.
- **Shared API key:** only for scripts and service-to-service calls, never as the user login.

**Non-negotiables**
- Store passwords only as salted hashes from `argon2` or `bcrypt`; never plain text, never fast hashes like plain SHA-256.
- Check authorization **on the server for every request**, including object-level checks ("does this user own/may this user see record 42?"). Changing an id in the URL must not expose someone else's data (this is called IDOR).
- The private network is not a security boundary you can rely on alone.
- Rate-limit or lock out repeated failed logins.
- Keep an audit log for sensitive actions (who changed what and when).

**Interview Q:** *"It's an internal tool on a private network. Do we still need login?"* → See Q27 in Part 6.

## 8.5 Database access and backups

- **Least privilege:** the application's DB user gets only the permissions it needs (`SELECT/INSERT/UPDATE` on specific tables), not root/admin.
- **Do not expose the database port** to the whole LAN; allow only the app server.
- **Migrations:** schema changes via versioned scripts (Flyway/Liquibase in Java land; Alembic for SQLAlchemy), not manual edits on production.
- **Backups:** a backup you have never restored is a hope, not a backup.

| Database | Consistent backup example | Notes |
|---|---|---|
| SQLite | `sqlite3 inquiries.db ".backup /backups/inquiries-$(date +%F).db"` | Safe while the app is running; do not just copy a live file |
| MySQL | `mysqldump --single-transaction -u backup_user -p dbname > dbname-$(date +%F).sql` | `--single-transaction` gives a consistent snapshot for InnoDB tables |
| MongoDB | `mongodump --db dbname --out /backups/...` | |

**Backup checklist:** scheduled (cron/Task Scheduler), stored on a **different machine or disk**, encrypted or access-restricted (it contains the same sensitive data), retention policy (for example daily for 14 days, weekly for 3 months), **restore tested periodically**, and one named person responsible.

## 8.6 Logs, monitoring, troubleshooting, maintenance

**Logging**
- Structured, timestamped lines with request id, endpoint, status, duration, upstream name.
- Levels: `INFO` normal events, `WARNING` handled problems (an upstream timeout), `ERROR` unexpected failures with stack trace.
- **Do not log** passwords, tokens, full request bodies with personal or commercial data.
- Rotate logs so the disk does not fill (a full disk is a classic outage): `logrotate` on Linux, or the logging module's `RotatingFileHandler`.

**Minimal monitoring (small team friendly)**
- `/health` endpoint (cheap, returns 200) checked by a script or monitoring tool every minute.
- Disk space, memory and service-up checks.
- Alert (email/chat) when the health check fails or error rate spikes.
- Track a simple trend: p95 response time and upstream failure counts per day.

**Maintenance routine**
- Weekly: look at errors, disk, backup success.
- Monthly: apply OS and dependency security updates on a staging copy first; renew certificates before expiry (note the date!).
- Per release: tag the version in git, record what changed, keep the previous version ready for **rollback**.

## 8.7 Investigating a bug when AI tools are unavailable

You may be expected to work this way every day. Use a method, not guesswork.

1. **Understand the report.** What did the user do, what did they expect, what happened, when, which account/data? Ask for a screenshot or exact message.
2. **Reproduce.** Same steps, same data, same environment if possible. If you cannot reproduce, gather evidence (logs, timestamps) instead.
3. **Locate the layer.** Browser (DevTools console and Network)? Proxy? API (logs)? Upstream service? Database?
   - Browser OK but wrong data → compare API response with the database.
   - API returns 500 → read the server traceback.
   - API OK but UI wrong → inspect state and rendering.
4. **Make one hypothesis at a time** and test it. Change one thing, observe, revert if it did not help. Keep notes.
5. **Read the error message and traceback carefully, bottom up.** The last frame in your own code is usually where to look.
6. **Use the primary documentation** (official docs, `--help`, `/docs` OpenAPI page, MDN, Python docs) and the code itself; search the exact error text; read the library's source when needed.
7. **Shrink the problem.** Write a ten-line script or a failing test that shows the bug.
8. **Fix, add a regression test, verify** in the same way you reproduced it.
9. **Write it down** (ticket or short note: cause, fix, how to detect it next time).

**Checklist of cheap tools:** `curl -i`, browser Network tab, `console.log`/`print` with labels, a debugger (VS Code breakpoints; `breakpoint()` in Python), `git log` and `git diff` ("what changed since it last worked?" is often the fastest question), `git bisect` for finding the commit that broke it.

**Working within an AI-restricted environment (communication tip).** If asked in an interview, say something like: "I understand the data is sensitive and the policy is to use AI minimally. I am comfortable working from documentation, logs and tests, and I would use approved tools only as the team allows; I would never paste internal data into external tools." Do not claim you never use AI if that is not true; say how you work and that you will follow policy.

## 8.8 Communicating progress, clarifying requirements, taking ownership

**Clarifying a requirement (questions to ask before coding)**
- Who uses it, how many, how often? What does "done" look like?
- What happens when data is missing or a service is down: block, or show partial?
- What data is sensitive? Who may see what?
- How fast must it be? What volume of data?
- Is there an existing system or spreadsheet this replaces? What must be migrated?
- Who approves changes, and how is it deployed?

**Status update template (short, regular)**
> **Done:** inquiry form saves to the database; duplicate-submit protection tested.
> **Next:** dashboard partial-failure display; target Thursday.
> **Blocked/Risk:** need the stock service's test URL; without it I will use a mock and risk rework.
> **Need from you:** confirm whether a partial dashboard is acceptable when orders is down.

**Ownership behaviors small teams value**
- Tell someone early when you are stuck (after a reasonable, time-boxed attempt such as 30–60 minutes of real effort), and bring what you tried.
- Own the full path: code, test, deploy, watch logs after release, handle the first bug report.
- Write down decisions and runbooks ("how to restart the service", "how to restore the backup") so the team does not depend on memory.
- Prefer boring, maintainable solutions to clever ones; the next person maintaining it may be you in six months or a colleague.
- Say "I do not know yet; here is how I will find out", rather than guessing.

**Interview Q:** *"Describe a time you owned something end to end."* Use your Bug Tracker or Inquiry Management System. Structure: **situation → what you owned → key decisions and trade-offs → problem you debugged → result → what you would improve.** Prepare one real debugging story per project (for example a transaction, validation or deployment issue you actually hit). Do not invent a story you cannot defend with details.

## 8.9 Examples relevant to internal business applications

| Example app | Likely concerns |
|---|---|
| **Inquiry / quotation tracker** | Duplicate prevention, validation, audit trail, backups |
| **Production or stock dashboard** | Several data sources, partial results, "as of" timestamps, caching |
| **Maintenance request tool** | Roles (operator vs supervisor), attachments, notifications |
| **Report generator (Excel/PDF)** | Long-running jobs, background processing, file storage, access control |
| **Employee/asset records** | Sensitive data, least privilege, audit logs, retention |

---

# Part 9: Practice Exercises and Answer Key

**How to use.** Attempt each exercise on your own, in a real file, and run it. Only then check the answer key (starts at **9.4**). The solutions in the key were executed and checked with assertions (JavaScript with Node 22, Python with 3.12, React with Vitest) unless a note says otherwise. If your solution differs but passes the stated checks, that is fine; compare trade-offs.

**Difficulty:** ★ small, ★★ medium, ★★★ harder.

## 9.1 JavaScript exercises (8)

**JS-1 ★ Predict the output.** Write the order of the logs *before* running.
```js
console.log('1');
setTimeout(() => console.log('2'), 0);
Promise.resolve().then(() => { console.log('3'); setTimeout(() => console.log('4'), 0); });
(async () => { console.log('5'); await new Promise((r) => setTimeout(r, 0)); console.log('6'); })();
Promise.reject(new Error('x')).catch(() => console.log('7'));
console.log('8');
```

**JS-2 ★★ `withTimeout(promise, ms, label)`.** Reject with `"<label> timed out after <ms>ms"` if the promise is not settled in time. It must not leave a timer running when the promise settles first. Explain what it can **not** do (hint: cancellation).

**JS-3 ★★ Implement `allSettledLite(items)`** that behaves like `Promise.allSettled` without using it. It must accept non-promise values.

**JS-4 ★★ Implement `promisify(fn)`** for error-first callback functions: `fn(arg1, ..., (err, result) => ...)`.

**JS-5 ★★ `fetchAll(urls, getJson)`** returns `{ ok: {url: data}, failed: {url: message} }` and never rejects because of a single failing URL.

**JS-6 ★★★ `retryIf(fn, { retries, baseMs, shouldRetry })`** with exponential backoff and jitter. A 404 must **not** be retried, a 503 should be. Count the attempts to prove it.

**JS-7 ★★★ `latestOnly(fn)`** wraps an async function so that, when called several times quickly, only the **most recent** call is reported as fresh and earlier ones are reported as stale (a building block against race conditions in search boxes).

**JS-8 ★★★ Generic `aggregate(calls, { perMs, overallMs })`** where `calls` is an object of functions that take an `AbortSignal`. Return `{ data, errors, partial }`. A hung call must be cancelled at `perMs`, and nothing may continue after `overallMs`. Test with: one fast success, one failure, one very slow call.

## 9.2 Python / FastAPI exercises (8)

**PY-1 ★ Sequential → concurrent.** Three `await fetch(...)` calls of 0.1 s each take 0.3 s. Rewrite to finish in about 0.1 s and prove it with `time.perf_counter()`.

**PY-2 ★★ Classify `gather` results.** Run three calls under per-call timeouts with `return_exceptions=True`. Produce `{name: "ok" | "timeout" | "error"}`.

**PY-3 ★★ Fix blocking code.** `async def` function calls `blocking_io()` (which does `time.sleep(0.15)`). Prove with a background "ticker" that the loop stays responsive after your fix.

**PY-4 ★★ Async retry decorator** `@retry(times, base, retry_on)` with exponential backoff and jitter that retries only the exception types listed.

**PY-5 ★★ `bounded_gather(coro_fns, limit)`** running at most `limit` at a time, preserving input order, returning exceptions as values. Prove the maximum concurrency.

**PY-6 ★★★ `first_n_or_timeout(coros, n, timeout)`.** Return the first `n` **successful** results in completion order, or whatever succeeded when `timeout` expires. Failed coroutines are ignored. All unfinished work must be cancelled and awaited before returning.

**PY-7 ★★ FastAPI validation.** `POST /orders` with `part_number` (pattern `AA-1234`), `quantity` (1..10000) and optional `due_in_days` (0..365, default 7). Return 201. Write tests proving invalid input yields 422 and that the error names both bad fields.

**PY-8 ★★★ FastAPI two-service endpoint.** `GET /summary` calls services `a` and `b` concurrently via an injected `httpx.AsyncClient`, 0.2 s timeout each, returning `{data, errors}`. Test with a `MockTransport` where `b` is very slow; the whole request must finish well under the slow service's delay.

## 9.3 React / full-stack exercises (4)

**RX-1 ★★ Auto-refresh.** Write `useInterval(callback, ms, enabled)` that always calls the latest callback and leaves no timer behind after unmount. Use it to refresh the dashboard every 30 seconds. Test with fake timers.

**RX-2 ★★★ Debounced search hook.** `useDebouncedSearch(query)` waits 300 ms after typing stops, ignores queries shorter than 2 characters, cancels in-flight requests, encodes the query in the URL, and exposes `idle/loading/success/empty/error`. Test that five quick keystrokes produce one request.

**RX-3 ★★ Form + server errors.** Make the inquiry form show the **server's** 422 messages next to the right field and prevent a second request on double click. Write a test.

**RX-4 ★★★ Full-stack list view.** Add `GET /api/inquiries?limit=&offset=` (capped page size, newest first, returns `total`) and a React table with Previous/Next, plus loading, empty and error states with a working Retry.

## 9.4 Debugging exercises (intentionally incorrect code)

For each, find the bug **before** reading the fix.

**DBG-1 (JS, Part 7).** The partial warning never shows.
```js
setState({
  status: !hasData ? "empty" : body.partial ? "success" : "partial",
  data: body.data, errors: body.errors, error: null,
});
```

**DBG-2 (Python, Part 7).** One upstream trickles bytes (a byte every second), the request takes ~9 s despite a "1.5 s timeout".
```python
client = httpx.AsyncClient(timeout=httpx.Timeout(1.5))
async def call_service(name, path):
    resp = await client.get(path)
    resp.raise_for_status()
    return resp.json()
```

**DBG-3 (React, Part 7).** The dashboard sometimes shows the previous customer's data.
```jsx
useEffect(() => {
  fetch(`/api/customers/${id}/dashboard`).then((r) => r.json()).then(setData);
}, [id]);
```

**DBG-4 (Python/SQL, Part 7).** Duplicate inquiries appear when two requests arrive together.
```python
existing = conn.execute("SELECT id FROM inquiries WHERE idempotency_key = ?", (key,)).fetchone()
if not existing:
    conn.execute("INSERT INTO inquiries (..., idempotency_key) VALUES (..., ?)", (..., key))
# table definition: idempotency_key TEXT   -- no UNIQUE
```

**DBG-5 (Deployment, Part 7).** After deployment the UI says "Server unreachable".
```js
export const API_BASE = "http://localhost:8000";      // in the React app
```
```bash
uvicorn app.main:app --host 127.0.0.1 --port 8000     # on the server
```
*(Two separate causes: find both, and say which command shows each.)*

**DBG-6 (JS).** "All saved!" appears before saving finishes, and one failed save is silently lost.
```js
async function saveAll(items) {
  items.forEach(async (item) => {
    try { await save(item); } catch (e) {}
  });
  console.log('All saved!');
}
```

**DBG-7 (Python).** `TypeError: 'coroutine' object is not subscriptable`, and the service freezes under load.
```python
async def fetch_data(i):
    await asyncio.sleep(0.1)
    return {"total": i * 10}

async def get_report(i):
    time.sleep(2)                 # "simulate work"
    data = fetch_data(i)
    return data["total"]
```

**DBG-8 (React).** The page freezes with "Too many re-renders" or the network tab shows endless requests.
```jsx
function Orders() {
  const [orders, setOrders] = useState([]);
  const options = { limit: 10 };
  useEffect(() => {
    fetch(`/api/orders?limit=${options.limit}`).then((r) => r.json()).then(setOrders);
  }, [options]);
  return <List items={orders} />;
}
```

---

# 9.5 Answer Key

> Try first. Seriously. The goal is reasoning, not recognition.

## JavaScript answers

**JS-1 answer.** `1, 5, 8, 3, 7, 2, 6, 4` **(verified).**
- Sync phase: `1`; the async function runs until its first `await` so `5`; `8`.
- Microtasks in queue order: the `.then` (`3`, which schedules a *new* timer), then the rejection handler (`7`).
- Timers in the order they were created: the first `setTimeout` (`2`); the timer inside the async function resolves its promise (`6` runs as a microtask right after); the timer created inside the `.then` (`4`) comes last.

**JS-2 answer.**
```js
function withTimeout(promise, ms, label = 'operation') {
  let timer;
  const timeout = new Promise((_, reject) => { timer = setTimeout(() => reject(new Error(`${label} timed out after ${ms}ms`)), ms); });
  return Promise.race([promise, timeout]).finally(() => clearTimeout(timer));
}
```
Limitation: it only stops *waiting*. The original operation is still running (a request in flight, a payment being created). For real cancellation pass an `AbortSignal` to `fetch`.

**JS-3 answer.**
```js
function allSettledLite(items) {
  return Promise.all(Array.from(items, (p) =>
    Promise.resolve(p).then((value) => ({ status: 'fulfilled', value }), (reason) => ({ status: 'rejected', reason }))));
}
```
`Promise.resolve(p)` wraps plain values, `.then(onOk, onErr)` converts both outcomes into fulfilled objects, so `Promise.all` never rejects.

**JS-4 answer.**
```js
function promisify(fn) {
  return (...args) => new Promise((resolve, reject) => fn(...args, (err, result) => (err ? reject(err) : resolve(result))));
}
```
(Node also ships `util.promisify`.)

**JS-5 answer.**
```js
async function fetchAll(urls, getJson) {
  const settled = await Promise.allSettled(urls.map((u) => getJson(u)));
  const ok = {}, failed = {};
  settled.forEach((r, i) => { r.status === 'fulfilled' ? (ok[urls[i]] = r.value) : (failed[urls[i]] = r.reason.message); });
  return { ok, failed };
}
```
`allSettled` + index mapping back to the URL.

**JS-6 answer.**
```js
async function retryIf(fn, { retries = 2, baseMs = 5, shouldRetry = () => true } = {}) {
  for (let attempt = 0; ; attempt++) {
    try { return await fn(attempt); }
    catch (e) {
      if (attempt >= retries || !shouldRetry(e)) throw e;
      await sleep(baseMs * 2 ** attempt + Math.random() * 5);
    }
  }
}
```
Verified: with `shouldRetry = e => e.status >= 500`, a 503 succeeds on the third attempt, and a 404 is attempted exactly once.

**JS-7 answer.**
```js
function latestOnly(fn) {
  let latest = 0;
  return async (...args) => {
    const id = ++latest;
    const result = await fn(...args);
    return id === latest ? { stale: false, result } : { stale: true };
  };
}
```
A counter identifies the latest call; older calls still finish but are reported `stale`, so the caller discards them. In React the same idea is done with an `AbortController` in the effect cleanup.

**JS-8 answer.**
```js
async function aggregate(calls, { perMs = 1500, overallMs = 2000 } = {}) {
  const overall = AbortSignal.timeout(overallMs);
  const entries = Object.entries(calls);
  const settled = await Promise.allSettled(entries.map(([, fn]) => fn(AbortSignal.any([overall, AbortSignal.timeout(perMs)]))));
  const data = {}, errors = {};
  settled.forEach((r, i) => {
    const name = entries[i][0];
    if (r.status === 'fulfilled') data[name] = r.value;
    else errors[name] = r.reason?.name === 'TimeoutError' ? 'timeout' : String(r.reason?.message ?? r.reason);
  });
  return { data, errors, partial: Object.keys(errors).length > 0 };
}
const abortable = (ms, value, signal, fail) => new Promise((resolve, reject) => {
  const t = setTimeout(() => (fail ? reject(new Error(fail)) : resolve(value)), ms);
  signal.addEventListener('abort', () => { clearTimeout(t); reject(signal.reason); }, { once: true });
});
```
Test harness used (verified): `users` succeeds in 20 ms, `stock` rejects with `HTTP 500`, `orders` would take 5 s. Result: `{ data: { users: 'U' }, errors: { orders: 'timeout', stock: 'HTTP 500' }, partial: true }`. Each call receives a signal combining the per-call and overall limits, and each listens for `abort` to stop its work and clear its timer (with `fetch`, passing the signal does this for you).

## Python / FastAPI answers

(Imports used by the solutions: `asyncio, functools, time, random, httpx`, `FastAPI, Depends`, `TestClient`, `BaseModel, Field`.)

**PY-1 answer.**
```python
async def py1():
    t = time.perf_counter()
    r = await asyncio.gather(fetch("a", .1), fetch("b", .1), fetch("c", .1))
    assert r == ["a", "b", "c"] and time.perf_counter() - t < .2
```

**PY-2 answer.**
```python
async def py2():
    async def limited(coro, s):
        async with asyncio.timeout(s): return await coro
    names = ["x", "y", "z"]
    res = await asyncio.gather(limited(fetch("x", .01), .1), limited(fetch("y", 1), .05), limited(fetch("z", .01, True), .1), return_exceptions=True)
    kinds = {n: ("timeout" if isinstance(r, TimeoutError) else "error" if isinstance(r, BaseException) else "ok") for n, r in zip(names, res)}
    assert kinds == {"x": "ok", "y": "timeout", "z": "error"}, kinds
```
The `fetch` helper is the same as in the earlier examples (`await asyncio.sleep(d)`, optional failure). `TimeoutError` is checked first because both outcomes are `BaseException` instances.

**PY-3 answer.**
```python
def blocking_io(): time.sleep(.15); return "report"
async def py3():
    ticks = 0
    async def ticker():
        nonlocal ticks
        while True: await asyncio.sleep(.02); ticks += 1
    t = asyncio.create_task(ticker())
    assert await asyncio.to_thread(blocking_io) == "report"
    t.cancel()
    assert ticks >= 5, ticks              # the loop kept running while the thread blocked
```
`to_thread` (3.9+) runs the blocking call in a worker thread; the ticker proves the event loop kept running (at least 5 ticks during the 0.15 s block).

**PY-4 answer.**
```python
def retry(times=2, base=.01, retry_on=(ConnectionError, TimeoutError)):
    def deco(fn):
        @functools.wraps(fn)
        async def wrapper(*a, **kw):
            for attempt in range(times + 1):
                try: return await fn(*a, **kw)
                except retry_on:
                    if attempt == times: raise
                    await asyncio.sleep(base * 2 ** attempt + random.random() * base)
        return wrapper
    return deco
async def py4():
    n = 0
    @retry()
    async def flaky():
        nonlocal n; n += 1
        if n < 3: raise ConnectionError("down")
        return "ok"
    assert await flaky() == "ok" and n == 3
    m = 0
    @retry()
    async def bad():
        nonlocal m; m += 1; raise ValueError("not retryable")
    try: await bad()
    except ValueError: pass
    assert m == 1
```
Only `ConnectionError`/`TimeoutError` are retried by default; a `ValueError` is raised immediately (verified: attempted once).

**PY-5 answer.**
```python
async def bounded_gather(coro_fns, limit):
    sem = asyncio.Semaphore(limit)
    async def run(fn):
        async with sem: return await fn()
    return await asyncio.gather(*(run(f) for f in coro_fns), return_exceptions=True)
async def py5():
    active = peak = 0
    async def work(i):
        nonlocal active, peak
        active += 1; peak = max(peak, active); await asyncio.sleep(.01); active -= 1
        if i == 3: raise ValueError("bad item")
        return i
    res = await bounded_gather([functools.partial(work, i) for i in range(10)], 3)
    assert peak == 3 and res[0] == 0 and isinstance(res[3], ValueError) and len(res) == 10
```
Verified: with 10 items and limit 3, peak concurrency is exactly 3, results keep input order, and the failing item appears as a `ValueError` in the list.

**PY-6 answer.**
```python
async def first_n_or_timeout(coros, n, timeout):
    tasks = [asyncio.ensure_future(c) for c in coros]
    results = []
    try:
        async with asyncio.timeout(timeout):
            for fut in asyncio.as_completed(tasks):
                try: results.append(await fut)
                except Exception: continue            # ignore failures, keep waiting for successes
                if len(results) == n: break
    except TimeoutError:
        pass                                           # return what we have
    finally:
        for t in tasks:
            if not t.done(): t.cancel()
        await asyncio.gather(*tasks, return_exceptions=True)   # let cancellation finish
    return results
async def py6():
    r = await first_n_or_timeout([fetch("a", .3), fetch("b", .01), fetch("c", .02, True), fetch("d", .03)], 2, .2)
    assert r == ["b", "d"], r
    r2 = await first_n_or_timeout([fetch("a", 1), fetch("b", .01)], 2, .1)
    assert r2 == ["b"], r2
```
Key points: `as_completed` yields in completion order; the `finally` cancels unfinished tasks and **awaits** them so nothing keeps running after we return; the timeout returns partial results instead of raising. Verified: from `a` (0.3 s), `b` (0.01 s), `c` (fails), `d` (0.03 s) with `n=2, timeout=0.2` the result is `["b", "d"]`.

**PY-7 answer.**
```python
app7 = FastAPI()
class OrderIn(BaseModel):
    part_number: str = Field(pattern=r"^[A-Z]{2}-\d{4}$")
    quantity: int = Field(ge=1, le=10_000)
    due_in_days: int = Field(default=7, ge=0, le=365)
@app7.post("/orders", status_code=201)
def create_order(o: OrderIn): return {"id": 1, **o.model_dump()}
def py7():
    c = TestClient(app7)
    assert c.post("/orders", json={"part_number": "BR-1042", "quantity": 3}).status_code == 201
    r = c.post("/orders", json={"part_number": "nope", "quantity": 0})
    assert r.status_code == 422 and {e["loc"][-1] for e in r.json()["detail"]} == {"part_number", "quantity"}
```
The 422 body lists each failing field in `detail[*].loc`; the test confirms both `part_number` and `quantity` are named.

**PY-8 answer.**
```python
app8 = FastAPI()
def get_client(): raise RuntimeError("override me")
@app8.get("/summary")
async def summary(client: httpx.AsyncClient = Depends(get_client)):
    async def one(name):
        try:
            async with asyncio.timeout(.2):
                r = await client.get(f"/{name}"); r.raise_for_status(); return name, r.json(), None
        except TimeoutError: return name, None, "timeout"
        except httpx.HTTPError as e: return name, None, type(e).__name__
    res = await asyncio.gather(one("a"), one("b"))
    return {"data": {n: d for n, d, e in res if e is None}, "errors": {n: e for n, d, e in res if e}}
def py8():
    async def handler(req):
        if req.url.path == "/b": await asyncio.sleep(2)
        return httpx.Response(200, json={"p": req.url.path})
    app8.dependency_overrides[get_client] = lambda: httpx.AsyncClient(transport=httpx.MockTransport(handler), base_url="http://x")
    t = time.perf_counter(); body = TestClient(app8).get("/summary").json()
    assert body == {"data": {"a": {"p": "/a"}}, "errors": {"b": "timeout"}} and time.perf_counter() - t < 1, body
```
Verified: with service `b` sleeping 2 s, the call returns in well under 1 s with `b` reported as `timeout`.

## React / full-stack answers

**RX-1 answer** (`src/useAutoRefresh.js`):
```jsx
import { useEffect, useRef } from "react";

export function useInterval(callback, ms, enabled = true) {
  const saved = useRef(callback);
  useEffect(() => { saved.current = callback; }, [callback]);   // latest callback, no stale closure

  useEffect(() => {
    if (!enabled) return undefined;
    const id = setInterval(() => saved.current(), ms);
    return () => clearInterval(id);                              // no leaked timers
  }, [ms, enabled]);
}
```
Use it in the dashboard: `useInterval(reload, 30_000)`. Verified with fake timers: three ticks in 3 s, none after unmount.

**RX-2 answer** (`src/useDebouncedSearch.js`):
```jsx
import { useEffect, useState } from "react";
import { apiFetch } from "./api";

export function useDebouncedSearch(query, delayMs = 300) {
  const [state, setState] = useState({ status: "idle", results: [], error: null });

  useEffect(() => {
    const q = query.trim();
    if (q.length < 2) { setState({ status: "idle", results: [], error: null }); return undefined; }

    const controller = new AbortController();
    const timer = setTimeout(() => {
      setState((s) => ({ ...s, status: "loading" }));
      apiFetch(`/api/search?q=${encodeURIComponent(q)}`, { signal: controller.signal })
        .then((results) => setState({ status: results.length ? "success" : "empty", results, error: null }))
        .catch((err) => { if (err.name !== "AbortError") setState({ status: "error", results: [], error: "Search failed." }); });
    }, delayMs);

    return () => { clearTimeout(timer); controller.abort(); };
  }, [query, delayMs]);

  return state;
}
```
Verified: four rapid re-renders with `brake` as the final query produce **one** `fetch`, with `q=brake`. (`/api/search` is hypothetical; you would add it to the backend.)

**RX-3 answer.** The `InquiryForm` from Part 5 already contains the logic (disable-while-submitting guard and mapping `detail[*].loc` to fields). The test (verified):
```jsx
it("shows the server message on the right field and blocks double submit", async () => {
  const fetchMock = vi.fn(() => Promise.resolve({
    ok: false, status: 422,
    json: () => Promise.resolve({ detail: [{ loc: ["body", "part_number"], msg: "Part number not recognised" }] }),
  }));
  vi.stubGlobal("fetch", fetchMock);
  render(<InquiryForm />);
  fireEvent.change(screen.getByLabelText(/Customer name/), { target: { value: "Asha", name: "customer_name" } });
  fireEvent.change(screen.getByLabelText(/Part number/), { target: { value: "BR-1042", name: "part_number" } });
  fireEvent.click(screen.getByRole("button", { name: /Submit/ }));
  fireEvent.click(screen.getByRole("button", { name: /Saving|Submit/ }));   // second click
  await waitFor(() => expect(screen.getByText(/not recognised/)).toBeInTheDocument());
  expect(fetchMock).toHaveBeenCalledTimes(1);
});
```

**RX-4 answer.** Backend (added to `app/inquiries.py`, verified with a pagination test, 13 backend tests pass in total):
```python
@router.get("", response_model=dict)
def list_inquiries(limit: int = 20, offset: int = 0):
    limit = max(1, min(limit, 100))                 # cap page size
    offset = max(0, offset)
    with db.connect() as conn:
        total = conn.execute("SELECT COUNT(*) FROM inquiries").fetchone()[0]
        rows = conn.execute("SELECT * FROM inquiries ORDER BY id DESC LIMIT ? OFFSET ?", (limit, offset)).fetchall()
    return {"total": total, "limit": limit, "offset": offset, "items": [_row_to_out(r).model_dump() for r in rows]}
```
Test:
```python
def test_list_pagination(client):
    for i in range(5):
        client.post("/api/inquiries", json={**GOOD, "quantity": i + 1}, headers=H)
    page = client.get("/api/inquiries?limit=2&offset=0", headers=H).json()
    assert page["total"] == 5 and len(page["items"]) == 2 and page["items"][0]["quantity"] == 5
    assert len(client.get("/api/inquiries?limit=2&offset=4", headers=H).json()["items"]) == 1
    assert client.get("/api/inquiries?limit=100000", headers=H).json()["limit"] == 100
```
Frontend (`src/InquiryTable.jsx`, compile-checked; not browser-tested):
```jsx
import { useEffect, useState } from "react";
import { apiFetch } from "./api";

const PAGE = 10;

export default function InquiryTable() {
  const [offset, setOffset] = useState(0);
  const [reloadKey, setReloadKey] = useState(0);   // bump to force a reload (setOffset(sameValue) does nothing)
  const [state, setState] = useState({ status: "loading", page: null });

  useEffect(() => {
    const c = new AbortController();
    setState((s) => ({ ...s, status: "loading" }));
    apiFetch(`/api/inquiries?limit=${PAGE}&offset=${offset}`, {
      signal: c.signal,
      headers: { "X-API-Key": import.meta.env.VITE_DEV_API_KEY },
    })
      .then((page) => setState({ status: page.items.length ? "success" : "empty", page }))
      .catch((e) => { if (e.name !== "AbortError") setState({ status: "error", page: null }); });
    return () => c.abort();
  }, [offset, reloadKey]);

  if (state.status === "error") return <p role="alert">Could not load inquiries. <button onClick={() => setReloadKey((k) => k + 1)}>Retry</button></p>;
  if (state.status === "loading" && !state.page) return <p role="status">Loading…</p>;
  if (state.status === "empty") return <p>No inquiries yet.</p>;

  const { items, total } = state.page;
  return (
    <>
      <table>
        <thead><tr><th>#</th><th>Customer</th><th>Part</th><th>Qty</th></tr></thead>
        <tbody>{items.map((i) => <tr key={i.id}><td>{i.id}</td><td>{i.customer_name}</td><td>{i.part_number}</td><td>{i.quantity}</td></tr>)}</tbody>
      </table>
      <button disabled={offset === 0} onClick={() => setOffset((o) => Math.max(0, o - PAGE))}>Previous</button>
      <button disabled={offset + PAGE >= total} onClick={() => setOffset((o) => o + PAGE)}>Next</button>
      <span> {offset + 1}–{Math.min(offset + PAGE, total)} of {total}</span>
    </>
  );
}
```
Design notes to say aloud: page size is capped on the server; the "empty" state can also appear when you navigate past the last page; `OFFSET` pagination gets slow on very large tables (keyset pagination is the alternative); the retry bug to avoid is `setOffset(o => o)`, which does nothing because the value does not change.

## Debugging answers

**DBG-1.** The ternary is reversed: `partial: true` produces `"success"`. Fix:
```js
status: !hasData ? "empty" : body.partial ? "partial" : "success",
```
*Lesson:* a test per UI state (Part 5's `Dashboard.test.jsx`) catches this immediately.

**DBG-2.** `httpx.Timeout(1.5)` sets **per-phase** limits (connect, read, write, pool). A server that sends a chunk every second keeps resetting the read timer, so the call can last far longer than 1.5 s. Add a hard total budget:
```python
async def call_service(name, path):
    async with asyncio.timeout(1.5):          # total budget (3.11+)
        resp = await client.get(path)
    resp.raise_for_status()
    return resp.json()
```

**DBG-3.** No cleanup, so if the response for the old `id` arrives after the new one, it overwrites the state (a race condition). Fix with an abort:
```jsx
useEffect(() => {
  const c = new AbortController();
  fetch(`/api/customers/${id}/dashboard`, { signal: c.signal })
    .then((r) => r.json()).then(setData)
    .catch((e) => { if (e.name !== "AbortError") setError(e); });
  return () => c.abort();
}, [id]);
```
(Also add `r.ok` handling; see Part 2.6.)

**DBG-4.** Two requests can both see "no existing row" and both insert (check-then-insert race). The real guard is a database constraint:
```sql
idempotency_key TEXT UNIQUE
```
plus handling `sqlite3.IntegrityError` by returning the existing row (as `inquiries.py` does). Application-level checks alone are never enough.

**DBG-5.** Two causes:
1. `localhost` in the browser means the *user's* computer. Fix: configure `API_BASE` per environment (`VITE_API_BASE`) or use a same-origin reverse proxy with a relative path (`/api`). Diagnose with the browser Network tab (request goes to `localhost:8000`).
2. `--host 127.0.0.1` accepts only local connections, so other machines cannot reach the API directly. Behind a reverse proxy on the same machine this is correct; if clients connect directly, bind `0.0.0.0` (and open the firewall deliberately). Diagnose with `ss -ltnp | grep 8000` on the server and `curl http://SERVER:8000/health` from a client.

**DBG-6.** `forEach` ignores the promises its callback returns, so the log runs first; the empty `catch {}` hides failures. Fix (concurrent, with reporting):
```js
async function saveAll(items) {
  const results = await Promise.allSettled(items.map(save));
  const failed = results.map((r, i) => (r.status === "rejected" ? items[i] : null)).filter(Boolean);
  console.log(`Saved ${items.length - failed.length}/${items.length}`);
  if (failed.length) console.error("Failed items:", failed);
  return failed;
}
```
(Use a `for...of` loop with `await` if order or rate limits matter, and limit concurrency for large batches.)

**DBG-7.** Two bugs: `fetch_data(i)` returns a coroutine that was never awaited, and `time.sleep(2)` blocks the loop.
```python
async def get_report(i):
    await asyncio.sleep(2)            # non-blocking wait (or to_thread for real blocking work)
    data = await fetch_data(i)
    return data["total"]
```

**DBG-8.** `options` is a **new object on every render**, so the effect's dependency changes every time, the effect re-runs, `setOrders` triggers a render, and it loops forever. Depend on primitives or move constants out of the component:
```jsx
const LIMIT = 10;                       // outside the component, or use the primitive directly
useEffect(() => {
  const c = new AbortController();
  fetch(`/api/orders?limit=${LIMIT}`, { signal: c.signal }).then((r) => r.json()).then(setOrders);
  return () => c.abort();
}, []);                                  // or [limit] if limit comes from props/state
```

---

# Part 10: Mock Interview Bank

**How to use.** Ask a friend to act as interviewer, or record yourself and play it back. Use a timer. **No AI, no copy-paste from this guide** while practicing. Speak your reasoning aloud: interviewers in scenario-based rounds care about *how* you think.

All questions are **[Inferred] practice material**, not a list of what SM Auto asked.

## 10.1 Mock 1: 30-minute technical interview (concepts + one scenario)

| Time | Segment | Prompt | What a strong answer shows |
|---|---|---|---|
| 0–3 | Warm-up | "Walk me through your Inquiry Management System: your role and one decision you made." | Ownership, clear structure, a real trade-off |
| 3–8 | JS event loop | Q1 (Part 6) output prediction, then "why does the Promise callback run first?" | Microtask vs macrotask, trace out loud |
| 8–13 | Promises | "Difference between `Promise.all` and `Promise.allSettled`; when to use each?" Follow-up: "Does `all` cancel the rest?" | Requirement-driven choice, cancellation knowledge |
| 13–18 | Python async | "What does `async def` do? Will it speed up CPU-bound code?" Follow-up: "What goes wrong with `requests` inside `async def`?" | Blocking vs non-blocking, thread pool/`to_thread` |
| 18–25 | **Scenario** | **Q9 (three APIs, one fails)** then "Now one takes 10 s and the endpoint must reply in 2 s." | Clarifying question first, concurrency, timeouts, partial result contract, cleanup |
| 25–28 | FastAPI/React | "`def` vs `async def`?" and "Why might a React effect fire twice in development?" | Thread pool vs loop; StrictMode and cleanup |
| 28–30 | Your questions | You ask two good questions (see below) | Interest in team practice |

**Good questions to ask the interviewer** (genuine, not scripted): "How does the team handle deployments and rollbacks for internal apps?" "What does a typical first project look like for a new developer?" "How are bugs reported and prioritized by the business users?"

**Self-scoring (0–2 each):** clarified the requirement before answering; gave the structure aloud; code or pseudo-code correct; named a trade-off; handled the follow-up without panic.

## 10.2 Mock 2: 60-minute practical coding interview

**Setup:** an editor, a terminal, docs allowed (official documentation only, mirroring a no-AI environment). Talk while you code.

| Time | Task | Requirement |
|---|---|---|
| 0–5 | Read and clarify | You receive the task below. Ask at least three clarifying questions. |
| 5–20 | **Part A: JavaScript** | `getDashboard(fetchers, {perMs, overallMs})` described below |
| 20–40 | **Part B: FastAPI** | Endpoint calling two services concurrently with timeouts, plus 2 tests |
| 40–52 | **Part C: React** | Component showing loading/success/partial/error with stale-response protection |
| 52–60 | Review | Explain what you would do with more time; deployment and logging notes |

**Task statement (hand this over):**
> An internal page shows a customer's **profile** and **recent orders** from two internal services. Either may be slow or down. The page should still show whatever is available within about 2 seconds, and clearly tell the user what is missing.

**Part A prompt.** *Write `getDashboard(fetchers, opts)` where `fetchers` is `{ profile: (signal) => Promise, orders: (signal) => Promise }`. Return `{ data, errors, partial }`. Enforce `perMs` per call and cancel anything slow.* (Reference: Part 2.8 and Exercise JS-8.)

**Part B prompt.** *Create `GET /customers/{id}/summary` in FastAPI that calls `/profile/{id}` and `/orders/{id}` on an upstream through an injected `httpx.AsyncClient`. 0.5 s timeout each; partial results allowed; 503 if both fail. Write two tests: one partial, one both-failing.* (Reference: Part 4, Exercise PY-8.)

**Part C prompt.** *Write a React hook and component that calls your endpoint. Show loading, success, partial (with a warning naming what is missing), and error with retry. Changing the customer id quickly must never show stale data.* (Reference: Part 5.)

**Expected solution outline (interviewer copy)**

| Part | Must-haves | Strong extras |
|---|---|---|
| A | Concurrent start; `allSettled` or per-call catch; abort/timeout per call; results mapped to names; `partial` flag | `AbortSignal.any` with overall deadline; distinguishes timeout from HTTP error; unit test with fake fetchers |
| B | `async def`; shared/injected client; concurrent `gather`; total timeout (`asyncio.timeout`) per call; correct status codes (200 partial, 503 both fail); `raise_for_status` | Logging with duration; `dependency_overrides` in tests; response model |
| C | `useEffect` with cleanup/abort; five states; partial banner naming the missing section; retry | `role="alert"`; test for stale response; keeping old data visible during reload |

**Time-pressure strategy to practice**
1. Write the **simplest correct version first** (even if sequential), get it working, then improve concurrency. A working simple answer beats an unfinished clever one.
2. Say your plan before typing: "I will do X, then Y, and add timeouts last."
3. When stuck for more than a few minutes, say what you tried and what you suspect; then try the smallest experiment (a `console.log`, a `curl`).
4. Reserve the last five minutes to run tests and state limitations honestly.

## 10.3 Mock 3: scenario-based interview on API failures and async programming (45 minutes)

Interviewer: ask each scenario, let the candidate answer fully, then use the probes. Candidate: structure every answer as **clarify → approach → code or pseudocode → failure cases → test/monitor.**

| # | Scenario | Probes |
|---|---|---|
| 1 | "Three APIs, one fails, two succeed." (Q9) | `all` vs `allSettled`; identifying failures in Python's `gather`; response shape |
| 2 | "One API takes 10 s; the endpoint must reply in 2 s." (Q10) | Per-call vs overall timeouts; what happens to the slow task; HTTP status |
| 3 | "A payment API times out. Do you retry?" (Q17) | Idempotency key; unknown outcome; backoff and limits |
| 4 | "Your service must call an external API 500 times." (Q18) | Concurrency limit; 429 handling; partial failure report |
| 5 | "After a release, the whole API gets slow, not just one endpoint." (Q12/Q20) | Blocked event loop; how to prove it; fix options |
| 6 | "The dashboard shows data from the previous customer." (Q15) | Race condition; abort in cleanup; test for it |
| 7 | "It works locally but fails on the company server." (Q28) | `localhost`, bind address, firewall, CORS, env vars; method of checking each |
| 8 | "A background job fails silently." (Q24) | Task reference and done callback; logging; monitoring |

**Closing question for the candidate:** "Pick one answer you gave that you would improve, and how." (Tests self-awareness and honesty.)

## 10.4 Interviewer rubric (use for any mock)

Score each category 1 to 4. **1 = not shown, 2 = partially, 3 = solid, 4 = strong.**

| Category | 1 | 2 | 3 | 4 |
|---|---|---|---|---|
| **Correctness** | Wrong concepts or code does not work | Right idea, notable bugs | Works for the main path | Works including failure paths; verified by running |
| **Reasoning** | Jumps to code, no structure | Some structure | Clarifies, compares options, chooses with reasons | Explains trade-offs and states assumptions and limits |
| **Edge cases** | None considered | Mentions one (e.g., null) | Timeouts, failures, empty data handled | Concurrency, cancellation, retries/idempotency, security considered |
| **Code quality** | Hard to read, duplicated | Readable but messy | Clear names, small functions, error handling | Idiomatic, minimal, easy to test and maintain |
| **Testing** | No tests or plan | Says "I would test" | Writes at least one meaningful test | Tests failure and timing paths, uses fakes/mocks appropriately |
| **Communication** | Silent or rambling | Understandable | Explains while coding, answers directly | Concise, checks understanding, admits unknowns and proposes how to find out |
| **Practical engineering** | Ignores deployment, logs, data sensitivity | Mentions logging | Logs, config, and basic security considered | Operational thinking: monitoring, rollback, backups, handoff |

**Interpreting your own results (for self-study, not a prediction of any hiring outcome):** after each mock, list the **lowest two categories** and make them the focus of your next study session. Repeat the same mock a week later and compare.

---

# Part 11: Revision Notes and Progress Tracker

## 11.1 Concise revision notes

**JavaScript async**
- One thread; the event loop runs sync code → drains **all microtasks** (promise callbacks, code after `await`) → one macrotask (timer, I/O) → repeat.
- Promise: pending → fulfilled/rejected, settles once. `.then` returns a new promise.
- `async` function returns a promise; `await` pauses that function only. Code before the first `await` runs synchronously.
- Independent calls: start together (`Promise.all`/`allSettled`). Sequential awaits = sum of times.
- `fetch` rejects only on network failure/abort; check `res.ok`.
- Timeouts: `AbortSignal.timeout` cancels the request; `Promise.race` only stops waiting.
- `Promise.all` fail-fast, does **not** cancel others.

**Python async**
- `async def` call → coroutine object (does nothing until awaited/scheduled). Task = scheduled coroutine.
- Concurrency ≠ parallelism. `asyncio` helps I/O-bound work; CPU-bound needs processes.
- Blocking call in `async def` freezes the loop. Fix: async library, plain `def`, or `asyncio.to_thread`.
- `gather` default: first exception propagates, others keep running; `return_exceptions=True`: all results, exceptions as values (check `isinstance(r, BaseException)`).
- `wait_for` / `timeout()` cancel inner work; `wait(timeout=)` does not. Re-raise `CancelledError`.
- `TaskGroup` (3.11+): cancels siblings on failure, errors as `ExceptionGroup` (`except*`).

**FastAPI**
- Types drive parsing: path, query, body (Pydantic). Invalid input → automatic 422.
- `def` → thread pool; `async def` → event loop (never block it).
- Shared `httpx.AsyncClient` via lifespan; total timeout via `asyncio.timeout`; `raise_for_status()`.
- `Depends` for injection; override in tests. Use `response_model`.
- CORS: explicit origins. Middleware for request id/logging.

**React**
- Effect = sync with outside world; cleanup runs before re-run and on unmount.
- Abort in cleanup to prevent stale responses; five UI states; partial warnings visible.
- Depend on primitives; never mutate state; use functional updates when new state depends on old.

**Engineering**
- Partial results only when requirements allow and wrong data cannot cause harm.
- Retries: idempotency + limits + backoff + deadline.
- Secrets in env, never in git or frontend bundles. Authentication ≠ authorization.
- Debug by method: reproduce → locate layer → one hypothesis at a time → fix + test + note.

## 11.2 Comparison and decision tables

**Promise combinators**

| | Waits for | Rejects when | Use for |
|---|---|---|---|
| `all` | all fulfill | first rejection | all essential |
| `allSettled` | all settle | never | independent, partial OK |
| `race` | first to settle | first settles as rejection | timeout pattern |
| `any` | first fulfillment | all reject | redundant sources |

**Python equivalents and timeouts**

| Need | Tool | Cancels work? |
|---|---|---|
| All, fail fast | `gather()` / `TaskGroup` | gather: no; TaskGroup: yes |
| All, partial OK | `gather(return_exceptions=True)` | no |
| Hard time limit for one call | `wait_for` / `timeout()` | yes |
| Overall deadline, manual control | `wait(timeout=)` + cancel pending | only if you do it |
| Limit parallelism | `Semaphore` | n/a |
| Blocking function | `to_thread` | thread cannot be killed |
| CPU-heavy | process pool / worker | n/a |

**Which concurrency tool?**

| Workload | Tool |
|---|---|
| Many network calls | `asyncio` |
| Blocking library, I/O | threads / `to_thread` / plain `def` endpoint |
| Heavy computation | processes / background worker |
| Long job (minutes) | job queue/worker + status endpoint |

**HTTP status quick map**

| Situation | Code |
|---|---|
| OK / created / idempotent repeat | 200 / 201 / 200 |
| Bad input | 400 or 422 |
| Not logged in / not allowed | 401 / 403 |
| Not found / conflict | 404 / 409 |
| Upstream failed / unavailable / too slow | 502 / 503 / 504 |
| Accepted for background processing | 202 |

**Partial vs fail-fast**

| Question | Partial result | Fail completely |
|---|---|---|
| Are parts independent? | Yes | No |
| Could missing data cause a wrong decision? | No | Yes |
| Can the UI clearly show what is missing? | Yes | n/a |
| Is the missing part the primary thing requested? | No | Yes |

## 11.3 Common interview traps

1. "`setTimeout(fn, 0)` runs immediately." (No: after sync code and microtasks.)
2. "`fetch` rejects on 404." (No.)
3. "`async/await` makes code parallel." (No: concurrency only if you start tasks together.)
4. "`Promise.all` cancels the other promises." (No.)
5. "`gather` cancels the others on error." (No, not by default.)
6. "`async def` makes a function non-blocking." (Only if it awaits non-blocking things.)
7. "Timeouts kill the work." (Only with real cancellation, and not for blocking code.)
8. "Retry on every failure." (Idempotency, limits, backoff, deadline.)
9. "`forEach` with `await` waits." (No.)
10. "`useEffect` with `[]` is always safe." (Hides stale data.)
11. "It is an internal app, so no security needed." (Authentication and authorization still matter.)
12. "Validate in the frontend." (Server is the authority.)
13. "`localhost` in the deployed frontend." (Points to the user's machine.)
14. "A partial result is always better than an error." (Only if requirements allow and it is not misleading.)
15. "`var` in loop + `setTimeout`" printing the final value.

## 11.4 "Can I explain this without notes?" checklist

Tick only when you have explained it **aloud, without looking**, and a friend (or your recording) agrees it is clear.

**JavaScript**
- [ ] Event loop order with a trace (sync → microtasks → macrotask)
- [ ] Promise states and what `.then` returns
- [ ] `async/await` is syntax over promises; code before first `await` is synchronous
- [ ] Sequential vs concurrent awaits and when each is right
- [ ] `all` vs `allSettled` vs `race` vs `any`
- [ ] `fetch` and HTTP errors; `AbortController`; `AbortSignal.timeout`
- [ ] Retry rules (idempotency, backoff, limits)

**Python**
- [ ] Coroutine vs task; `await` vs `create_task`
- [ ] Concurrency vs parallelism; I/O-bound vs CPU-bound; GIL in one sentence
- [ ] Why `async def` does not remove blocking
- [ ] `gather` with/without `return_exceptions`; how to classify results
- [ ] `wait_for`, `timeout()`, `wait()` differences; cancellation and re-raising
- [ ] `TaskGroup` and when not to use it

**FastAPI**
- [ ] `def` vs `async def` and the thread pool
- [ ] Pydantic validation → 422; response models
- [ ] Calling several services concurrently with timeouts and partial results
- [ ] Dependency injection and testing with overrides
- [ ] CORS in two sentences

**React**
- [ ] `useEffect` dependencies and cleanup
- [ ] Stale response/race condition and fix
- [ ] Five UI states for an API call
- [ ] Form validation + server error mapping

**Engineering**
- [ ] The three-API question in under two minutes, in both languages
- [ ] Localhost vs LAN vs private server; why `localhost` breaks deployed apps
- [ ] Secrets/environment variables; authentication vs authorization
- [ ] A step-by-step debugging method
- [ ] How you would ask clarifying questions and report progress

## 11.5 Progress tracker

Copy this table and fill it in after each study session. **Confidence:** 1 (cannot explain) to 5 (can teach it). **Practice completed:** exercise ids finished. **Mistakes:** what you got wrong (be specific). **Revision date:** schedule 1 day, 3 days, 7 days, 14 days later.

| Topic | Confidence (1-5) | Practice completed | Mistakes | Revision date |
|---|---|---|---|---|
| Event loop and execution order | | JS-1 | | |
| Promises and chaining | | JS-3, JS-4 | | |
| async/await patterns | | JS-5 | | |
| `all` / `allSettled` / `race` / `any` | | JS-3, JS-5 | | |
| Timeouts, abort, retries (JS) | | JS-2, JS-6, JS-8 | | |
| Race conditions (JS) | | JS-7 | | |
| Python coroutines and tasks | | PY-1 | | |
| Blocking vs non-blocking | | PY-3, DBG-7 | | |
| `gather` and `return_exceptions` | | PY-2, PY-5 | | |
| Timeouts and cancellation (Python) | | PY-4, PY-6 | | |
| FastAPI validation | | PY-7 | | |
| FastAPI concurrent services | | PY-8 | | |
| React effects and cleanup | | RX-1, DBG-3, DBG-8 | | |
| React states and stale responses | | RX-2 | | |
| Forms and server errors | | RX-3 | | |
| Full-stack list/pagination | | RX-4 | | |
| Private deployment | | DBG-5 | | |
| Security and secrets | | Part 8 review | | |
| Scenario questions Q1–Q32 | | Q__ of 32 | | |
| Mock interviews | | Mock 1 / 2 / 3 | | |

## 11.6 Final readiness assessment (self-assessment only)

This is a tool for **you** to judge your own preparation. It cannot predict any interviewer's decision or whether you will be selected.

Answer honestly. Give yourself one point per "yes":

1. I can explain the event loop order with a trace for a new example, not only the ones in this guide.
2. I can write the three-API aggregator in JavaScript from scratch, with timeouts, in 15 minutes.
3. I can write the same in Python from scratch in 15 minutes.
4. I can state, without hesitation, what happens to unfinished tasks in each approach.
5. I can explain why a blocking call in `async def` hurts all users, and demonstrate it.
6. I have built and run the FastAPI aggregator and its tests myself, and I understand every line.
7. I have built the React consumer and can explain the stale-response fix.
8. I have scored at least "3" on every rubric category in one full Mock 2 session.
9. I can debug a failing integration using logs, curl and the browser Network tab, without AI.
10. I can describe a private-network deployment, including secrets, backups and logs.
11. I can talk through two of my own projects with concrete debugging stories.
12. I can say "I do not know yet; here is how I would find out" naturally.

| Score | Meaning for your study plan |
|---|---|
| 0–4 | Return to the First 3 Study Sessions below; focus on building and running code, not rereading |
| 5–8 | Solid foundation; target your lowest rubric categories; repeat mocks |
| 9–12 | Keep refreshing with spaced revision; practice speaking answers concisely |

---

# Start Here: First 3 Study Sessions

These are the highest-value sessions for the weaknesses you reported. Each is about **2 to 3 hours**. Do them in order, ideally on separate days, and do **not** skip the "explain aloud" step.

## Session 1: The async core (JavaScript and the three-API question)
**Goal:** be able to explain the event loop and answer the three-API question in JavaScript, with code you wrote yourself.

1. **(30 min) Read** Parts 2.1 to 2.5. Run `order.js` (the trace in 2.2) and change one line at a time to see the output shift.
2. **(20 min) Do** exercises JS-1, JS-3 without looking at the key.
3. **(40 min) Build** the `all` vs `allSettled` demo from 2.5 from memory in a new file; then add a fourth API that is very slow.
4. **(30 min) Add** a per-call timeout with `AbortSignal.timeout` (2.7/2.8) and make the whole thing return in about 1.5 s.
5. **(20 min) Explain aloud** Q9 (Part 6) in two minutes, record it, and listen back. Note filler, missing steps.
6. **Self-check:** can you say what `Promise.all` does to the other requests? If not, reread 2.5.

## Session 2: Python asyncio and FastAPI
**Goal:** the same answer in Python, plus the `def` vs `async def` understanding the team likely cares about.

1. **(30 min) Read** Parts 3.1 to 3.5. Run the blocking-vs-non-blocking ticker demo (3.3) and watch the loop freeze.
2. **(30 min) Rebuild** the `gather(return_exceptions=True)` classifier (3.4) from memory; then do PY-2 and PY-3.
3. **(30 min) Read** Part 3.7 and rewrite Approach B (the simple `wait_for` version) yourself.
4. **(50 min) Set up** the Part 4 project, run it with the mock upstreams and the `FAIL_SERVICES` / `SLOW_SERVICES` switches, run `pytest`.
5. **(20 min) Try** the blocking demo endpoints (4.4) with two `curl` windows to feel `async def` + `time.sleep` versus `def`.
6. **Self-check:** explain `def` vs `async def` in FastAPI in 30 seconds, and what happens to a task when `wait_for` times out.

## Session 3: React integration, debugging and interview delivery
**Goal:** connect frontend and backend, handle partial results and race conditions, and practice speaking.

1. **(40 min) Build** the Part 5 frontend, point it at your running backend, and exercise all five states by changing the mock's behavior.
2. **(30 min) Read** 5.5 and the debugging table; then do DBG-3 and DBG-8 (React) and DBG-5 (deployment).
3. **(20 min) Do** RX-1 or RX-2 (pick one) with a test.
4. **(30 min) Practice aloud** Q10, Q15 and Q16 (timeouts, stale response, CORS) with a timer, two minutes each.
5. **(30 min) Do** the 30-minute technical mock (10.1) with a friend or a recording.
6. **Plan next:** score yourself with the rubric (10.4), pick the two weakest categories, and schedule sessions for those. Then continue with Part 6 questions (a few each day), the mini project (Part 7) and Part 8.

**Do not** try to do everything at once. Revisit sessions 1 to 3 after one week and see what you can now do without notes. That gap is your real progress.

---

*End of guide.*
