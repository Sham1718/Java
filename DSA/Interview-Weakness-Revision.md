# Interview Weakness Revision — Post-Interview Technical Repair

---

## 1. What This Document Is For

This is built from the **actual mistakes** in your Full Stack Developer Intern interview. It is not a roadmap. Each section targets one thing that went wrong and trains the exact skill: writing the query, writing the code, recognising the pattern, or explaining the answer out loud.

**Tags used throughout**

- **[MUST]** = expect this in interviews; be able to do it without hesitation.
- **[GOOD]** = strengthens an answer; know it if you have time.

**How to use this today (suggested order, about one sitting per block)**

1. SQL (section 2) — type every query yourself in MySQL/Postgres. Don't just read. 
2. Python dictionaries (section 3) — type every snippet, then close the file and redo the practice problems.
3. DSA recognition + concurrency (sections 4–5) — these are reasoning skills. Talk through them aloud.
4. Backend: API performance, indexes, middleware, HTTP (sections 6–9) — read, then explain out loud.
5. React (section 10), thinking under pressure (11), then speaking practice (12).
6. Finish with the checklist (13).

**Rule for practice sections:** questions come first, answers come later. Attempt before you scroll.

**Rule for every topic:** there is a "How I should explain this in an interview" box. Practise saying it in your own words. Don't memorise it word for word.

---

## 2. SQL Practical Query Patterns

### The two tables from the interview (used in all examples)

**customers**

| id | name  | city   |
|----|-------|--------|
| 1  | Asha  | Pune   |
| 2  | Bilal | Mumbai |
| 3  | Chen  | Pune   |
| 4  | Divya | Nashik |

**orders**
 -----------------------------------------
| id  | customer_id | amount | order_date |
|-----|-------------|--------|------------|
| 101 | 1           | 500    | 2026-01-05 |
| 102 | 1           | 300    | 2026-02-10 |
| 103 | 2           | 200    | 2026-02-11 |
| 104 | 3           | 700    | 2026-03-01 |
| 105 | 3           | 100    | 2026-03-09 |
| 106 | 3           | 50     | 2026-03-20 |

Divya (id 4) has **no orders**. That is what makes the trap questions work.

### The universal thinking routine (do this BEFORE typing SQL) **[MUST]**

1. **Output:** what does one row of the result represent? (one customer? one order? one category?)
2. **Tables:** which tables hold the columns I need?
3. **Row filter:** any condition on individual rows? -> `WHERE`
4. **Grouping:** is the answer "per something"? -> `GROUP BY` that something
5. **Group filter:** any condition on an aggregate ("more than once", "total > 1000")? -> `HAVING`
6. **Keep unmatched rows?** ("including those with none", "who have NO...") -> `LEFT JOIN`
7. **Order/limit:** `ORDER BY`, `LIMIT`

Say it aloud: *"One row per customer, so I group by customer. 'More than once' is a condition on a count, so HAVING."* That sentence alone earns marks.

**Logical execution order (memorise):**
`FROM/JOIN -> WHERE -> GROUP BY -> HAVING -> SELECT -> ORDER BY -> LIMIT`

This order explains why `WHERE` can't use `COUNT()` (groups don't exist yet) and why `HAVING` can.

---

### 2.1 GROUP BY **[MUST]**

**Explain simply:** collapses many rows into one row per distinct value of the grouping column(s), so aggregate functions (`COUNT, SUM, AVG, MIN, MAX`) compute per group.

**Example:** orders per customer

```sql
SELECT customer_id,
       COUNT(*)    AS order_count,
       SUM(amount) AS total_amount
FROM orders
GROUP BY customer_id;
```

| customer_id | order_count | total_amount |
|-------------|-------------|--------------|
| 1           | 2           | 800          |
| 2           | 1           | 200          |
| 3           | 3           | 850          |

**Syntax rule:** every column in `SELECT` must either be in `GROUP BY` or inside an aggregate function. (MySQL may allow violations depending on `ONLY_FULL_GROUP_BY`; Postgres never does. Don't rely on MySQL's leniency.)

**How to think:** "per X" in the question => `GROUP BY X`.

**Interview questions**

1. Show the number of orders per city (needs a join).
2. Show the highest order amount per customer_id.
3. Show each customer's name with their total spent. (Who do you group by, and why is grouping by `name` alone risky?)

**Solutions**

```sql
-- 1
SELECT c.city, COUNT(o.id) AS order_count
FROM customers c
JOIN orders o ON o.customer_id = c.id
GROUP BY c.city;

-- 2
SELECT customer_id, MAX(amount) AS max_amount
FROM orders
GROUP BY customer_id;

-- 3
SELECT c.id, c.name, SUM(o.amount) AS total_spent
FROM customers c
JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name;
```

Q3: two customers can share a name. Group by the **id** (and include name so it can be selected).

**Common mistakes**

- Selecting a non-grouped, non-aggregated column.
- Grouping by the wrong granularity (by name instead of id).
- Using `COUNT(*)` after a LEFT JOIN when you meant `COUNT(o.id)` (see 2.4).

**How I should explain this in an interview:** "GROUP BY splits rows into buckets, one per value of the column. Aggregates then compute per bucket. I pick the grouping column by asking: one result row per what?"

---

### 2.2 HAVING (and WHERE vs HAVING) **[MUST]**

**Explain simply:** `WHERE` filters **rows before** grouping. `HAVING` filters **groups after** aggregation.

| | WHERE | HAVING |
|---|---|---|
| Runs | before GROUP BY | after GROUP BY |
| Filters | individual rows | groups |
| Can use aggregates? | No | Yes |

**Example A — the interview question:** total amount for customers who ordered **more than once**

```sql
SELECT c.name,
       COUNT(o.id)  AS order_count,
       SUM(o.amount) AS total_amount
FROM customers c
JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name
HAVING COUNT(o.id) > 1;
```

Result: Asha (2, 800), Chen (3, 850). Bilal has one order so his group is removed. Divya has none so the inner join drops her.

**How I'd think about it (out loud):**
1. Output row = one customer.
2. Need names + amounts -> join customers and orders.
3. "Per customer" -> GROUP BY customer.
4. "More than once" = condition on COUNT -> HAVING.

**Using both WHERE and HAVING:** total for orders in 2026 only, customers with total > 600

```sql
SELECT customer_id, SUM(amount) AS total
FROM orders
WHERE order_date >= '2026-01-01'        -- row filter first
GROUP BY customer_id
HAVING SUM(amount) > 600;               -- group filter after
```

**Interview questions**

1. Customers with more than 2 orders.
2. Customers whose total spent is above 700.
3. Why does `WHERE COUNT(*) > 1` fail? Fix it.

**Solutions**

```sql
-- 1
SELECT customer_id, COUNT(*) AS n
FROM orders
GROUP BY customer_id
HAVING COUNT(*) > 2;

-- 2
SELECT customer_id, SUM(amount) AS total
FROM orders
GROUP BY customer_id
HAVING SUM(amount) > 700;

-- 3  WHERE runs before grouping; counts don't exist yet. Use HAVING.
```

**Common mistakes**

- Aggregate in WHERE.
- Putting a plain row condition (e.g. `city='Pune'`) in HAVING. It works in some DBs but is slower and unclear. Row conditions go in WHERE.
- Forgetting that HAVING needs GROUP BY (conceptually).

**How I should explain this in an interview:** "WHERE filters rows before they're grouped, so it can't see aggregates. HAVING filters after grouping, so it can. If a condition is on a single row, WHERE; on COUNT/SUM/AVG, HAVING."

---

### 2.3 JOIN (INNER JOIN) **[MUST]**

**Explain simply:** returns only rows where the join condition matches in **both** tables.

```sql
SELECT c.name, o.id AS order_id, o.amount
FROM customers c
INNER JOIN orders o ON o.customer_id = c.id;
```

Divya does not appear (no matching orders).

**Multi-table join example** (with `order_items` and `products`):

| order_items: order_id | product_id | qty | price |
|---|---|---|---|
| 101 | 1 | 2 | 150 |
| 101 | 2 | 1 | 200 |

```sql
SELECT c.name, p.name AS product, oi.qty * oi.price AS line_total
FROM customers c
JOIN orders o       ON o.customer_id = c.id
JOIN order_items oi ON oi.order_id   = o.id
JOIN products p     ON p.id          = oi.product_id;
```

**How to think:** follow the foreign-key chain: customer -> order -> item -> product. Write each `ON` as `child.fk = parent.id`.

**Interview questions**

1. Customer name and order amount for every order.
2. Customer names who placed at least one order, each name once.
3. Total amount per customer name (only customers with orders).

**Solutions**

```sql
-- 1
SELECT c.name, o.amount
FROM customers c JOIN orders o ON o.customer_id = c.id;

-- 2
SELECT DISTINCT c.name
FROM customers c JOIN orders o ON o.customer_id = c.id;
-- or: WHERE EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.id)

-- 3
SELECT c.id, c.name, SUM(o.amount) AS total
FROM customers c JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name;
```

**Common mistakes**

- Forgetting the `ON` condition -> cartesian product (every row x every row).
- Ambiguous column names (`id` exists in both tables) -> always use aliases.
- **Fan-out:** joining `orders` to `order_items` then `SUM(o.amount)` counts each order's amount once per item. Aggregate at the right level (see 2.5).

**How I should explain this in an interview:** "INNER JOIN keeps only rows with a match on both sides. I join along foreign keys and alias tables so columns are unambiguous."

---

### 2.4 LEFT JOIN + IS NULL (the anti-join) **[MUST]**

**Explain simply:** `LEFT JOIN` keeps **every row from the left table**. If there's no match on the right, right-side columns become `NULL`. Filtering `WHERE right.id IS NULL` keeps exactly the left rows that had **no match**.

```sql
SELECT c.name, o.id AS order_id
FROM customers c
LEFT JOIN orders o ON o.customer_id = c.id;
```

| name  | order_id |
|-------|----------|
| Asha  | 101      |
| Asha  | 102      |
| Bilal | 103      |
| Chen  | 104..106 |
| Divya | NULL     |

**Interview question B — customers with NO orders, by name:**

```sql
SELECT c.name
FROM customers c
LEFT JOIN orders o ON o.customer_id = c.id
WHERE o.id IS NULL
ORDER BY c.name;
```

**How I'd think about it (out loud):**
1. I want customers, so customers is the LEFT table.
2. I must keep customers even when no order exists -> LEFT JOIN.
3. Unmatched rows have NULLs in orders columns.
4. Keep only those: `WHERE o.id IS NULL` (use a column that can never be NULL in real rows, like the primary key).

**Two equivalent alternatives (know both)**

```sql
-- NOT EXISTS (safe and usually fast)
SELECT c.name FROM customers c
WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.id)
ORDER BY c.name;

-- NOT IN (DANGEROUS if the subquery can return NULL)
SELECT c.name FROM customers c
WHERE c.id NOT IN (SELECT customer_id FROM orders)
ORDER BY c.name;
```

**The NOT IN trap:** if `orders.customer_id` contains even one `NULL`, `NOT IN` returns **no rows** (comparison with NULL is unknown). `LEFT JOIN ... IS NULL` and `NOT EXISTS` don't have this problem.

**Count including zeros**

```sql
SELECT c.name, COUNT(o.id) AS order_count   -- COUNT(o.id) ignores NULL -> 0 for Divya
FROM customers c
LEFT JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name;
```

`COUNT(*)` would give Divya **1** (the row exists), which is wrong.

**Interview questions**

1. Customers who have never ordered.
2. Every customer with their order count, including zero.
3. Customers in Pune who have not ordered.

**Solutions**

```sql
-- 1  (above)

-- 2
SELECT c.name, COUNT(o.id) AS order_count
FROM customers c LEFT JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name ORDER BY c.name;

-- 3  city filter on the LEFT table goes in WHERE; the IS NULL also in WHERE
SELECT c.name
FROM customers c LEFT JOIN orders o ON o.customer_id = c.id
WHERE c.city = 'Pune' AND o.id IS NULL;
```

**Common mistakes**

- Putting a right-table filter in `WHERE` (e.g. `WHERE o.amount > 100`). That removes the NULL rows and silently turns your LEFT JOIN into an INNER JOIN. Put right-table conditions in the `ON` clause:
  `LEFT JOIN orders o ON o.customer_id = c.id AND o.amount > 100`
- `WHERE o.id = NULL` -> never true. Use `IS NULL`.
- Using `COUNT(*)` instead of `COUNT(o.id)` after a LEFT JOIN.
- Swapping the table order (putting orders on the left finds nothing useful).

**How I should explain this in an interview:** "LEFT JOIN keeps all rows from the left table and fills NULLs when there's no match. To find rows with no match, I LEFT JOIN and filter the right table's key with IS NULL. NOT EXISTS does the same, and I avoid NOT IN when NULLs are possible."

---

## DO FROM HERE
### 2.5 Aggregation (COUNT, SUM, AVG, MIN, MAX) and multi-table aggregation **[MUST]**

| Function | Notes |
|---|---|
| `COUNT(*)` | counts rows (including NULLs) |
| `COUNT(col)` | counts non-NULL values of col |
| `COUNT(DISTINCT col)` | counts unique non-NULL values |
| `SUM`, `AVG`, `MIN`, `MAX` | ignore NULLs |

**Subquery patterns you should be able to write**

```sql
-- Orders bigger than the overall average
SELECT * FROM orders
WHERE amount > (SELECT AVG(amount) FROM orders);

-- Customers whose total spend is above the average customer total
SELECT customer_id, SUM(amount) AS total
FROM orders
GROUP BY customer_id
HAVING SUM(amount) > (
    SELECT AVG(t.total)
    FROM (SELECT SUM(amount) AS total FROM orders GROUP BY customer_id) t
);

-- Highest-spending customer (simple)
SELECT customer_id, SUM(amount) AS total
FROM orders
GROUP BY customer_id
ORDER BY total DESC
LIMIT 1;
```

**Multi-table aggregation: avoid fan-out.** To get revenue per category:

```sql
SELECT p.category, SUM(oi.qty * oi.price) AS revenue
FROM order_items oi
JOIN products p ON p.id = oi.product_id
GROUP BY p.category;
```

Aggregate on the table that holds the measure (`order_items` here). If you join `orders` (one row per order) to `order_items` (many rows per order) and then `SUM(orders.amount)`, each order amount is repeated per item. Pre-aggregate in a subquery or sum from the detail table.

**Interview questions**

1. Average order amount per city.
2. Number of distinct customers who ordered in March 2026.
3. Customers with total > 500 AND at least 2 orders.

**Solutions**

```sql
-- 1
SELECT c.city, AVG(o.amount) AS avg_amount
FROM customers c JOIN orders o ON o.customer_id = c.id
GROUP BY c.city;

-- 2
SELECT COUNT(DISTINCT customer_id)
FROM orders
WHERE order_date >= '2026-03-01' AND order_date < '2026-04-01';

-- 3
SELECT customer_id, COUNT(*) AS n, SUM(amount) AS total
FROM orders
GROUP BY customer_id
HAVING COUNT(*) >= 2 AND SUM(amount) > 500;
```

**Common mistakes:** `AVG` of a column with NULLs (NULLs are skipped, which may or may not be what you want); summing after a fan-out join; using `BETWEEN` on dates/timestamps with an inclusive end (prefer `>= start AND < next_start`).

**How I should explain this in an interview:** "I first decide the output grain, then group at that grain, aggregate from the table that owns the measure, and filter groups with HAVING."

---

### 2.6 Common Interview Traps

| Trap | Fix |
|---|---|
| Aggregate in WHERE | Use HAVING |
| `= NULL` | Use `IS NULL` / `IS NOT NULL` |
| LEFT JOIN turned into INNER by a WHERE on the right table | Put the condition in `ON` |
| `COUNT(*)` after LEFT JOIN gives 1 for unmatched rows | `COUNT(right.id)` |
| `NOT IN` with NULLs | `NOT EXISTS` or LEFT JOIN + IS NULL |
| Group by name instead of id | Group by the unique key |
| Fan-out double-counting | Aggregate from the detail table / pre-aggregate |
| Selecting a non-grouped column | Add to GROUP BY or aggregate it |
| Forgetting `ORDER BY` when asked to "sort" | Add it (and `DESC` when needed) |

**Interview survival habit:** write the **skeleton first** and say it: `SELECT ... FROM ... JOIN ... ON ... WHERE ... GROUP BY ... HAVING ... ORDER BY ...`, then fill in. If you blank, write the join first, then the grouping.

---

### 2.7 Practice Questions (NO solutions here)

Schema (assume standard foreign keys):

```
customers(id, name, city, email)
orders(id, customer_id, amount, order_date)
order_items(id, order_id, product_id, qty, price)
products(id, name, category)
departments(id, name)
employees(id, name, dept_id, salary, manager_id)
```

1. Show each customer's name and number of orders, **including customers with zero orders**, sorted by name.
2. Show the total amount spent by each customer who has ordered **more than once**, highest total first.
3. List customers who have **never** placed an order, sorted by name. Write it **two ways** (LEFT JOIN and NOT EXISTS).
4. Show departments that have **no employees**.
5. Show departments whose **average salary is above 50,000**, with the average.
6. Show the **top 3 customers** by total spend (name and total).
7. List products that have **never been ordered**.
8. Show orders whose amount is **greater than the average order amount**.
9. Show employees who earn **more than the average salary of their own department**.
10. Show **total revenue per product category** (`qty * price`), highest first.
11. Show customers who placed orders in **both** January and February 2026.
12. Find **duplicate emails** in `customers` with how many times each appears.

### 2.8 Answer Key

```sql
-- 1
SELECT c.name, COUNT(o.id) AS order_count
FROM customers c LEFT JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name
ORDER BY c.name;

-- 2
SELECT c.name, SUM(o.amount) AS total
FROM customers c JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name
HAVING COUNT(o.id) > 1
ORDER BY total DESC;

-- 3 (a) LEFT JOIN
SELECT c.name FROM customers c
LEFT JOIN orders o ON o.customer_id = c.id
WHERE o.id IS NULL ORDER BY c.name;
-- 3 (b) NOT EXISTS
SELECT c.name FROM customers c
WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.id)
ORDER BY c.name;

-- 4
SELECT d.name FROM departments d
LEFT JOIN employees e ON e.dept_id = d.id
WHERE e.id IS NULL;

-- 5
SELECT d.name, AVG(e.salary) AS avg_salary
FROM departments d JOIN employees e ON e.dept_id = d.id
GROUP BY d.id, d.name
HAVING AVG(e.salary) > 50000;

-- 6
SELECT c.name, SUM(o.amount) AS total
FROM customers c JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name
ORDER BY total DESC
LIMIT 3;

-- 7
SELECT p.name FROM products p
LEFT JOIN order_items oi ON oi.product_id = p.id
WHERE oi.id IS NULL;

-- 8
SELECT * FROM orders
WHERE amount > (SELECT AVG(amount) FROM orders);

-- 9 (correlated subquery)
SELECT e.name, e.salary
FROM employees e
WHERE e.salary > (SELECT AVG(e2.salary) FROM employees e2 WHERE e2.dept_id = e.dept_id);

-- 10
SELECT p.category, SUM(oi.qty * oi.price) AS revenue
FROM order_items oi JOIN products p ON p.id = oi.product_id
GROUP BY p.category
ORDER BY revenue DESC;

-- 11 (one row per customer; count distinct months)
SELECT c.name
FROM customers c JOIN orders o ON o.customer_id = c.id
WHERE o.order_date >= '2026-01-01' AND o.order_date < '2026-03-01'
GROUP BY c.id, c.name
HAVING COUNT(DISTINCT MONTH(o.order_date)) = 2;
-- (Postgres: EXTRACT(MONTH FROM o.order_date))

-- 12
SELECT email, COUNT(*) AS n
FROM customers
GROUP BY email
HAVING COUNT(*) > 1;
```

**Self-check after the answer key:** for each problem you missed, write one sentence: "The output grain is ___, so I should have grouped by ___ / used ___ join."

---

## 3. Python Dictionary Coding

You said you only know "basic Python". Good news: interviews for this level use about **eight dictionary moves** repeatedly. Learn those and you can solve almost every question.

### Concepts

**[MUST] Core operations**

```python
d = {}                                   # empty dict
d = {"a": 1, "b": 2}                     # literal
d = dict(a=1, b=2)                       # keyword form

d["c"] = 3                               # add
d["a"] = 10                              # update (same syntax)
print(d["a"])                            # access -> KeyError if missing!
print(d.get("z"))                        # None if missing
print(d.get("z", 0))                     # default 0 if missing

"a" in d                                 # key exists? (checks KEYS, not values)
d.pop("a")                               # remove key, returns value
del d["b"]                               # remove key

d.keys()                                 # keys
d.values()                               # values
d.items()                                # (key, value) pairs

for k in d: ...                          # loops over KEYS
for k, v in d.items(): ...               # keys and values (most common)
```

**[MUST] The one-line idea behind 80% of problems**

```python
counts[x] = counts.get(x, 0) + 1
```
"Look up x, use 0 if it's new, add 1, store it back."

**[MUST] Other counting styles (know at least two)**

```python
# 1. get()
counts = {}
for x in nums:
    counts[x] = counts.get(x, 0) + 1

# 2. if/else (easiest to explain and to dry run)
for x in nums:
    if x in counts:
        counts[x] += 1
    else:
        counts[x] = 1

# 3. collections (good to know, mention after writing manually)
from collections import Counter, defaultdict
counts = Counter(nums)
groups = defaultdict(list)
```

**[MUST] Nested dictionaries**

```python
students = {
    "Asha":  {"math": 90, "sci": 80},
    "Bilal": {"math": 70, "sci": 95},
}
students["Asha"]["math"]            # 90
students["Asha"]["art"] = 85        # add nested key
for name, marks in students.items():
    print(name, sum(marks.values()))
```

**[MUST] Dictionary + list combinations**

```python
students = [
    {"name": "Asha", "marks": 90},
    {"name": "Bilal", "marks": 70},
]
for s in students:
    print(s["name"], s["marks"])

best = max(students, key=lambda s: s["marks"])
```

**[GOOD] Dictionary comprehension**

```python
squares = {n: n * n for n in range(5)}            # {0:0, 1:1, 2:4, 3:9, 4:16}
passed = {k: v for k, v in marks.items() if v >= 40}
```

### Patterns

**Pattern 1: Frequency counting.** Question words: "count", "how many times", "occurrences".
**Pattern 2: Seen/lookup.** Question words: "duplicate", "already seen", "pair that sums to". Store what you've seen, check in O(1).
**Pattern 3: Grouping.** Question words: "group by", "categorise". Key = group, value = list.
**Pattern 4: Max/min by value.** Question words: "most frequent", "highest".
**Pattern 5: Merge/compare.** Question words: "merge", "common keys".


## DO from here 
### Examples

```python
# Frequency of characters
def char_freq(s):
    freq = {}
    for ch in s:
        freq[ch] = freq.get(ch, 0) + 1
    return freq

# Most frequent element
def most_frequent(nums):
    freq = {}
    for x in nums:
        freq[x] = freq.get(x, 0) + 1
    best, best_count = None, 0
    for x, c in freq.items():
        if c > best_count:
            best, best_count = x, c
    return best

# Group words by length
def group_by_len(words):
    groups = {}
    for w in words:
        groups.setdefault(len(w), []).append(w)   # setdefault: create list if new key
    return groups

# Two Sum with a dictionary  (O(n))
def two_sum(nums, target):
    seen = {}                        # value -> index
    for i, x in enumerate(nums):
        need = target - x
        if need in seen:
            return [seen[need], i]
        seen[x] = i
    return []
```

### Dry Runs

**Dry run 1: `char_freq("banana")`**

| step | ch | freq after |
|---|---|---|
| 1 | b | {b:1} |
| 2 | a | {b:1, a:1} |
| 3 | n | {b:1, a:1, n:1} |
| 4 | a | {b:1, a:2, n:1} |
| 5 | n | {b:1, a:2, n:2} |
| 6 | a | {b:1, a:3, n:2} |

**Dry run 2: `two_sum([2, 7, 11, 15], 9)`**

| i | x | need = 9-x | need in seen? | seen after |
|---|---|---|---|---|
| 0 | 2 | 7 | no | {2:0} |
| 1 | 7 | 2 | **yes** -> return [0, 1] | |

Order matters: check **before** inserting the current number, so an element isn't paired with itself.

**Dry run 3: group_by_len(["hi","yo","sun","cat","a"])**

| w | groups after |
|---|---|
| hi | {2:[hi]} |
| yo | {2:[hi,yo]} |
| sun | {2:[hi,yo], 3:[sun]} |
| cat | {2:[hi,yo], 3:[sun,cat]} |
| a | {2:[hi,yo], 3:[sun,cat], 1:[a]} |

### Common Mistakes

1. `d["x"]` on a missing key -> `KeyError`. Use `get()` or check `in`.
2. `if value in d` when you meant a key (and `in` never checks values; use `in d.values()`).
3. Modifying a dict while looping over it -> `RuntimeError`. Loop over a copy: `for k in list(d):`.
4. Forgetting `.items()` and getting only keys: `for k, v in d:` fails.
5. Initialising `counts[x] += 1` without checking/defaulting.
6. Using a list as a key (unhashable). Use a tuple.
7. In max-by-value: `max(d)` returns the largest **key**. Use `max(d, key=d.get)` or `max(d.items(), key=lambda kv: kv[1])`.
8. Two Sum: inserting before checking; returning values instead of indices.
9. Assuming order: dicts keep insertion order in modern Python, but don't build logic on sorting unless you sort explicitly.

### How I should explain this in an interview

"A dictionary gives me O(1) average lookup by key. So whenever I need to count, remember what I've seen, or group items, I use a dict instead of a nested loop. For counting I use `get(x, 0) + 1`. For Two Sum I store each value with its index and check whether `target - x` has already been seen."

**If you blank:** say "Let me dry run a tiny input" and write `nums = [1, 2, 1]` and a dict on paper. The code usually follows.

### Practice Questions (NO solutions here)

Write each as a function. Test with your own inputs. Time yourself: 5 minutes each.

1. Count the frequency of each character in a string. `"hello"`
2. Count the frequency of each number in a list. `[1, 2, 2, 3, 3, 3]`
3. Return the duplicate elements of a list using a dictionary. `[1, 2, 3, 2, 4, 1]`
4. Return the most frequent element of a list. `[4, 5, 4, 6, 4, 5]`
5. Given `{"Asha": 82, "Bilal": 91, "Chen": 77}`, return the name with the highest marks, and the average of all marks.
6. Group a list of words by their first letter. `["apple", "bat", "ant", "ball", "cat"]`
7. Convert two lists into a dictionary. `keys = ["a","b","c"], values = [1,2,3]`
8. Merge two dictionaries; if a key exists in both, **add** the values. `{"a":1,"b":2}` and `{"b":3,"c":4}`
9. Find the common keys of two dictionaries.
10. Two Sum using a dictionary: return the indices of the two numbers that add up to the target. `[3, 2, 4], 6`

Bonus follow-ups interviewers like: *first non-repeating character*, *are two strings anagrams (compare frequency dicts)*, *invert a dictionary*, *sort dict by value*.

### Answer Key

```python
# 1
def char_freq(s):
    freq = {}
    for ch in s:
        freq[ch] = freq.get(ch, 0) + 1
    return freq
# char_freq("hello") -> {'h':1,'e':1,'l':2,'o':1}

# 2  same code, works on any hashable items
def num_freq(nums):
    freq = {}
    for n in nums:
        freq[n] = freq.get(n, 0) + 1
    return freq

# 3
def duplicates(nums):
    seen = {}
    dups = []
    for x in nums:
        if x in seen:
            if seen[x] == 1:          # add each duplicate only once
                dups.append(x)
            seen[x] += 1
        else:
            seen[x] = 1
    return dups
# [1,2,3,2,4,1] -> [2, 1]

# 4
def most_frequent(nums):
    freq = {}
    for x in nums:
        freq[x] = freq.get(x, 0) + 1
    return max(freq, key=freq.get)
# [4,5,4,6,4,5] -> 4

# 5
def top_student(marks):
    best_name = max(marks, key=marks.get)
    avg = sum(marks.values()) / len(marks)
    return best_name, avg
# ('Bilal', 83.33...)

# 6
def group_by_first_letter(words):
    groups = {}
    for w in words:
        groups.setdefault(w[0], []).append(w)
    return groups
# {'a':['apple','ant'],'b':['bat','ball'],'c':['cat']}

# 7
def lists_to_dict(keys, values):
    return dict(zip(keys, values))
# manual version:
def lists_to_dict_manual(keys, values):
    d = {}
    for i in range(len(keys)):
        d[keys[i]] = values[i]
    return d

# 8
def merge_add(d1, d2):
    result = dict(d1)                      # copy, don't mutate the input
    for k, v in d2.items():
        result[k] = result.get(k, 0) + v
    return result
# {'a':1,'b':5,'c':4}

# 9
def common_keys(d1, d2):
    return [k for k in d1 if k in d2]      # or: list(d1.keys() & d2.keys())

# 10
def two_sum(nums, target):
    seen = {}
    for i, x in enumerate(nums):
        if target - x in seen:
            return [seen[target - x], i]
        seen[x] = i
    return []
# [3,2,4], 6 -> [1, 2]
```

---

## 4. DSA Pattern Recognition

This section is **not** about relearning algorithms. It is about not picking the wrong one. In your interview the algorithm knowledge was not the problem; the **first 60 seconds** were.

> The mistake to fix: jumping to a technique (divide and conquer) before understanding what the question asks.

### How to Read a Problem **[MUST]**

Before you think about any algorithm, answer these six questions, out loud:

| # | Question | Egg-drop example |
|---|---|---|
| 1 | **What is the goal / output?** | The exact threshold floor (and maybe: fewest drops) |
| 2 | **What is given?** | 100 floors, some number of eggs |
| 3 | **What are the rules?** | Egg breaks if dropped from floor >= T; survives below T |
| 4 | **What are the constraints?** | How many eggs? Are unbroken eggs reusable? Worst case or average? |
| 5 | **What property can I exploit?** | Monotonic: if it breaks at floor k, it breaks at every floor above k |
| 6 | **What would the simplest correct solution be?** | Try floor 1, 2, 3... (linear) |

Then and only then: "What pattern matches this?"

**The 3-step clarify script (use real questions, not filler):**
1. "Let me restate it: ..." (one sentence)
2. "Can I confirm: ...?" (the one or two constraints that change the answer)
3. "Let me start with the simplest approach and improve it."

### Pattern Identification **[MUST]**

Match **signals in the question**, not the topic name.

| Signal in the question | Pattern | Why |
|---|---|---|
| Sorted array, "find position / first/last / threshold", monotonic yes/no | **Binary search** | Halve the search space each step |
| "Subarray/substring of size k", "longest/shortest window with condition" | **Sliding window** | Contiguous range; slide instead of recomputing |
| Sorted array + pair/triplet with a target; "reverse/palindrome" | **Two pointers** | Move from both ends/at different speeds |
| "Count", "duplicate", "seen before", "pair sum" in unsorted data | **Hashing (HashMap/Set)** | O(1) lookup replaces a nested loop |
| Problem is a smaller version of itself; tree/list traversal; "all combinations" | **Recursion** | Define base case + reduce |
| Split into independent halves, solve each, **combine** results | **Divide and conquer** | Merge sort, quick sort, max-subarray |
| "Maximum/minimum", locally best choice keeps working (intervals, coins with nice denominations) | **Greedy** | Pick best now; must justify it |
| Overlapping subproblems, "number of ways", "min cost", choices at each step | **Dynamic programming** | Cache subproblem answers |
| Connected things, shortest path, "levels", "neighbours", grids | **BFS/DFS (graph/tree)** | Traverse structure |
| Order of processing, "next greater", matching brackets | **Stack/Queue** | LIFO/FIFO behaviour |
| "Many independent items, many workers/threads/people" | **Parallelism / concurrency, not an algorithm** | Distribute tasks (section 5) |

**Quick decisions**

- Is the data **sorted or monotonic**? -> think binary search / two pointers first.
- Do I keep needing "have I seen this?" -> **hash**.
- Is the structure **contiguous** (subarray/substring)? -> **sliding window**.
- Do the subproblems **combine into one answer** (need a merge step)? -> **divide and conquer**.
- Are the pieces **independent and only need to be finished** (no combine)? -> **just distribute the work** (parallelism).

**Recognition cues for your known weak topics (no re-teaching):**

| Topic | The cue | The one idea |
|---|---|---|
| Linked list | "reverse", "middle", "cycle", "merge two lists", "remove nth from end" | Use `prev/curr/next` pointers; for middle/cycle use **slow and fast pointers** |
| Trees | "height", "sum", "path", "level by level" | Recursion on left/right for depth-like answers; BFS (queue) for levels |
| Merge sort | "stable, O(n log n), linked list sorting" | Split, sort halves, **merge** |
| Quick sort | "in-place, average O(n log n)" | Pick pivot, **partition**, recurse on sides |
| Sliding window | "longest substring without repeats", "max sum of k elements" | Expand right, shrink left while invalid; track state with a dict/set |
| HashMap/Two Sum | "pair", "complement", "anagram", "frequency" | Store what you've seen; look up `target - x` |
| Circular queue | "fixed size buffer", "wrap around" | `rear = (rear + 1) % capacity`; track `size` (or keep one slot empty) to tell full from empty |

### Egg/Threshold Problem **[MUST]**

**Problem (as usually asked):** A building has 100 floors. There is some threshold floor `T`: an egg dropped from floor `T` or higher breaks, below `T` it survives. Find `T`.

#### Step 1: Parse

- Output: the value `T`.
- Key property: outcomes are **monotonic**. Floors 1..T-1 -> survive, T..100 -> break.
- This is **not** "split the building into parts and combine results". Nothing needs combining. You are **searching** for a boundary.

#### Step 2: Clarify (ask these)

1. "How many eggs do I have?" (changes everything)
2. "Does an egg that survives a drop stay usable?"
3. "Do you want the minimum number of drops in the worst case, or just a correct method?"

#### Step 3: Reason toward a solution by egg count

| Eggs | Approach | Worst-case drops | Why |
|---|---|---|---|
| **Unlimited / many** | **Binary search** on floors: drop at 50; break -> search 1-49, survive -> search 51-100 | ~7 (since 2^7 = 128 >= 100) | A break costs nothing because you have spares |
| **1 egg** | **Linear from floor 1 upward** | up to 100 | If it breaks you have no egg left, so you can never skip a floor |
| **2 eggs** | **Jump in decreasing steps**; then linear within the last gap | **14** | See below |
| **k eggs, n floors** | **Dynamic programming** (breaks vs survives recurrence) | n/a | Good to know only; don't lead with it |

**Two eggs, worst case 14:**

- First egg jumps. If it breaks at a jump, the second egg goes linearly through the gap. Total drops = (jumps used) + (gap size).
- To make the worst case the same everywhere, make each following jump **one smaller** than the previous one: floors 14, 27, 39, 50, 60, 69, 77, 84, 90, 95, 99, 100.
- Why 14: the jumps are `x + (x-1) + (x-2) + ... + 1 = x(x+1)/2 >= 100` -> `x = 14`.
- Explain it simply: "Every time I use a drop on egg 1, the remaining linear gap for egg 2 shrinks by one. That keeps total worst-case drops constant."

**Interview-safe answer if you don't remember the 14:** "With two eggs binary search is unsafe because if the first egg breaks at 50, I'd have to go linearly through 49 floors with one egg. So I jump by fixed steps, say 10. Worst case is 10 jumps + 9 linear = 19. I can improve that by shrinking the step size each jump to equalise the worst case." That is a perfectly good answer and shows the reasoning.

#### Step 4: Code the simple parts

```python
# 1 egg: linear
def threshold_linear(breaks, n=100):        # breaks(floor) -> True/False
    for floor in range(1, n + 1):
        if breaks(floor):
            return floor
    return n + 1                             # never breaks

# Many eggs: binary search (first floor where it breaks)
def threshold_binary(breaks, n=100):
    lo, hi = 1, n + 1                        # answer in [lo, hi]; n+1 = never breaks
    while lo < hi:
        mid = (lo + hi) // 2
        if breaks(mid):
            hi = mid                         # threshold is mid or lower
        else:
            lo = mid + 1                     # threshold is above mid
    return lo
```

#### Common wrong approaches (what happened to you)

| Wrong move | Why it's wrong |
|---|---|
| "Divide and conquer: split 100 floors into two halves and solve each" | You don't solve both halves; one drop tells you which half holds the answer. That's **binary search**, which discards a half. D&C solves all parts and combines. |
| Jumping straight to DP | Overkill for 1 or many eggs; only needed for k eggs general. |
| Using binary search with 1 or 2 eggs without checking cost of breaking | A break destroys your resource. |
| Not asking how many eggs | The answer depends on it. |

#### How to recover when stuck on a problem like this

1. "Let me make sure I understand: I'm finding the boundary where eggs start breaking."
2. "Let me start with the naive way: drop from 1, 2, 3... That's 100 drops worst case."
3. "What structure can I exploit? If it breaks at floor k, it breaks above k. That's monotonic, so I can cut the search space."
4. "Constraint check: how many eggs do I have? That decides how aggressively I can cut."

### Common Wrong Approaches (general)

| Symptom | Likely cause | Fix |
|---|---|---|
| You say "divide and conquer" to everything | It's the pattern you practised most recently | Ask: "Do I need to combine sub-results?" If no, it isn't D&C |
| You start coding in 10 seconds | Fear of silence | Spend 60 seconds restating + clarifying; it's expected |
| You pick DP for "max/min" | Keyword matching | Try greedy/sorting first; DP needs overlapping subproblems |
| You forget the simplest solution | Hunting for the clever one | Always state brute force first, then optimise |
| You answer a question that wasn't asked | Missed the output | Restate the output before coding |
| Multiple people/threads = "split the array recursively" | Confusing parallel workers with recursive splitting | See section 5 |

**D&C vs parallel workers (the multithreading question you faced):**

- *Divide and conquer:* split a **problem** recursively until trivial, solve, **combine** (merge sort).
- *Parallel work distribution:* you have many **independent tasks** and several workers; give each worker a share. Nothing to combine except maybe counting results. Think "10 people processing 1,000 files", not "merge sort".

### Scenario Practice (NO solutions here)

For each: (1) write the output, (2) the given info, (3) the constraint that matters, (4) the pattern, (5) the brute force and the better approach. **Do not code until you've named the pattern.** Answers are at the end of this section.

1. A sorted list of 1 million employee IDs. Check whether ID 734,921 exists. Must be fast.
2. Given a list of integers and a target, return indices of two numbers that add to the target. The list is unsorted.
3. Find the length of the longest substring without repeating characters.
4. A log file has 10 million lines. Count how many times each IP address appears.
5. You have 3 eggs and 100 floors. What approach would you take, and how does it change from 1 egg and from unlimited eggs?
6. Given a linked list, find the middle node in one pass.
7. You're given 1,000 independent image files to resize and 8 CPU cores. How do you finish fastest?
8. Given an array of prices per day, find the maximum sum of any 3 consecutive days.
9. A deployment pipeline has a flag that flips from "working" to "broken" at some commit. Find the first broken commit among 10,000 commits.
10. Merge two already sorted arrays into one sorted array.

### Scenario Answers

1. **Binary search** (sorted, membership). O(log n). Mistake: linear scan or a set built from scratch.
2. **Hash map**: `seen[value] = index`, look up `target - x`. O(n). Brute force is O(n^2).
3. **Sliding window + set/dict**: expand right, shrink left while a duplicate is present, track max length.
4. **Hash map counting** (`counts[ip] += 1`). Memory concern: stream the file, don't load it all.
5. **3 eggs**: no simple formula; combine jumping and careful linear; general solution is DP (mention k eggs DP, but reason with 1 and 2 eggs first). With 1 egg: linear. Unlimited: binary search.
6. **Slow/fast pointers**: fast moves 2, slow moves 1; when fast ends, slow is at the middle.
7. **Not an algorithm problem.** Independent tasks -> **parallel processing**; images resizing is CPU-bound -> multiple processes/threads across cores (thread pool sized ~ number of cores). No combining needed.
8. **Fixed-size sliding window**: compute the first window sum, then add the new day and subtract the dropped day each step.
9. **Binary search** on a monotonic "good/bad" property (this is `git bisect`). ~14 checks.
10. **Two pointers** (the merge step of merge sort). O(n + m).

---

## 5. Multithreading & Concurrency

### Concepts

**[MUST] Process vs thread**

| | Process | Thread |
|---|---|---|
| Memory | Own separate memory | Shares memory with other threads of the same process |
| Creation cost | Heavy | Light |
| Communication | Harder (pipes, sockets, IPC) | Easy (shared variables), which also causes bugs |
| Crash isolation | One process crashing doesn't kill another | A thread can take down the whole process |

**[MUST] Concurrency vs parallelism**

- **Concurrency:** *dealing with* many tasks at once; tasks make progress by interleaving (one cook switching between dishes).
- **Parallelism:** *actually doing* many tasks at the same instant on multiple cores (several cooks, several stoves).
- You can have concurrency without parallelism (one core). Parallelism needs multiple cores.

**[MUST] CPU-bound vs I/O-bound**

| | CPU-bound | I/O-bound |
|---|---|---|
| Bottleneck | Computation (resizing images, encryption, number crunching) | Waiting (network, disk, database) |
| Best tool | Multiple **cores**: processes (Python) / threads on multiple cores (Java) | Many **threads or async**; they spend most time waiting anyway |
| Helped by more threads than cores? | No | Yes |

**Python note:** the GIL lets only one thread run Python bytecode at a time, so threads don't speed up CPU-bound work in CPython. Use `multiprocessing` for CPU-bound and `threading`/`asyncio` for I/O-bound. (Java threads run truly parallel on cores.)

**[MUST] Why use threads:** throughput (finish more work per unit time), responsiveness (UI/server not blocked), better use of idle CPU while waiting on I/O.

**[MUST] Shared resources, race conditions, thread safety**

- **Shared resource:** data that multiple threads read/write (a counter, a list, a file).
- **Race condition:** result depends on timing of threads. Classic: `count++` is three steps (read, add, write); two threads can read the same value and lose an update.
- **Thread-safe:** code that behaves correctly when called from multiple threads at once.

```java
// Race condition
class Counter {
    int count = 0;
    void inc() { count++; }          // NOT atomic
}
// 2 threads x 1000 increments -> often < 2000
```

**Fixes**

```java
// 1. synchronized (mutual exclusion)
synchronized void inc() { count++; }

// 2. Lock
private final ReentrantLock lock = new ReentrantLock();
void inc() {
    lock.lock();
    try { count++; } finally { lock.unlock(); }   // always unlock in finally
}

// 3. Atomic (best for simple counters)
AtomicInteger count = new AtomicInteger();
count.incrementAndGet();

// 4. Avoid sharing: give each thread its own data, combine at the end
```

**[MUST] Locks/mutex:** only one thread at a time in the critical section. Keep critical sections **small**. Too many locks -> contention; wrong lock order -> **deadlock** (A holds 1 wants 2, B holds 2 wants 1; fix by acquiring locks in a consistent order).

**[MUST] Thread pool:** creating a thread per task is costly and unbounded. A pool reuses a fixed number of worker threads and queues extra tasks.

```java
ExecutorService pool = Executors.newFixedThreadPool(10);
List<Future<Integer>> results = new ArrayList<>();

for (File f : files) {
    results.add(pool.submit(() -> process(f)));   // task distribution
}
for (Future<Integer> r : results) {
    total += r.get();                              // wait for results
}
pool.shutdown();
```

Sizing rule of thumb: CPU-bound -> about number of cores; I/O-bound -> more, because threads wait (tune by measuring).

**[GOOD] Producer/consumer:** producers put work into a **bounded queue**, consumers take it. The queue decouples speed and gives back-pressure.

```java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);

Runnable producer = () -> {
    for (int i = 0; i < 1000; i++) queue.put("job-" + i);   // blocks if full
};
Runnable consumer = () -> {
    while (true) { String job = queue.take(); handle(job); } // blocks if empty
};
```

Real-world version: API receives requests (producers) and pushes jobs to a queue (RabbitMQ/Kafka/SQS), workers (consumers) process them.


## write down this 
### Scenarios: how to choose **[MUST]**

| Situation | Best approach | Reason |
|---|---|---|
| Small, quick task; one at a time is fast enough | **Sequential** | Concurrency adds complexity; don't add it without a measured need |
| Tasks **depend** on each other (step 2 needs step 1) | **Sequential** (or pipeline) | Can't run independent |
| 1,000 files, each takes seconds of **CPU** | **Multiprocessing / thread pool sized to cores** | Needs real parallel compute |
| 100 independent API calls to third parties | **Multithreading or async** | Mostly waiting; overlap the waits |
| Server handling many simultaneous requests | **Thread pool / async event loop** | Each request mostly waits on DB/network |
| User clicks "export report" that takes 2 minutes | **Background job (async processing)** via queue | Don't block the HTTP request |
| Multiple threads update one counter/balance | **Synchronization** (lock/atomic/DB transaction) | Prevent lost updates |
| Processing spread across several machines | **Distributed processing** (queues, workers) | Beyond a single process |

**Vocabulary check:** *multithreading* = many threads in one process. *Multiprocessing* = many processes (true parallelism in Python). *Asynchronous* = don't wait idly; start a task and continue (event loop or callbacks). *Parallel* = simultaneous execution. They overlap, but don't treat them as synonyms.

**Scenario 1: "10 people processing 1,000 files"**

- Each file is independent. Split the work: each person takes 100 files (or pulls the next file from a shared pile).
- Why a shared pile (queue) is often better: people who finish early pick up more; no one sits idle. That's exactly a thread pool with a task queue.
- Only shared state is "which file is next" and "total count", so that is what needs synchronising.
- This is **not** divide and conquer: no recursive splitting, no merge step.

**Scenario 2: "Multiple requests arrive at the same time"**

- Server uses a thread pool (Tomcat/Spring: one thread per request). Each request is independent.
- Danger: shared mutable state in singleton beans (e.g. a non-thread-safe field in a `@Service`). Keep services **stateless**.
- Database handles concurrent writes with transactions and row locks, not your `synchronized`.

**Scenario 3: "Send 50 independent API requests"**

- Sequential: 50 x 200ms = 10s. Concurrent with a pool of ~10: about 1s. I/O-bound, so threads/async help.
- Consider rate limits of the third party and timeouts for each call.

### Interview Questions (try out loud, then read)

1. What is the difference between a process and a thread?
2. Concurrency vs parallelism?
3. What is a race condition? Give an example.
4. How do you fix a race condition? Name at least two ways.
5. Why use a thread pool instead of creating a thread per task?
6. CPU-bound vs I/O-bound: which benefits from more threads than cores?
7. You have 1 million independent tasks. How do you process them?
8. Two threads transfer money between the same two accounts. What can go wrong? (race, deadlock)
9. What is a deadlock, and how do you avoid it?
10. When would you choose *not* to use multithreading?

**Short answers**

1. Process = own memory; thread = shares memory with its process; threads are lighter but can corrupt shared data.
2. Concurrency = managing many tasks at once (interleaving); parallelism = executing at the same instant on multiple cores.
3. Two threads do `count++` at the same time and lose an update, because read-modify-write isn't atomic.
4. `synchronized`/locks, atomic variables, avoiding shared state, thread-safe collections, DB transactions.
5. Thread creation is expensive and unbounded threads exhaust memory/CPU; a pool reuses threads and queues work.
6. I/O-bound: threads spend time waiting, so others can use the CPU.
7. They're independent, so distribute via a pool/queue of workers (size by CPU vs I/O), process in batches, handle failures/retries, don't load everything into memory. If it's across machines: a message queue with worker consumers.
8. Race on balances (fix with locks/transactions) and deadlock if each locks the source account first (fix by locking in a fixed order, e.g. by account id).
9. Threads each hold a lock the other needs. Avoid with consistent lock ordering, lock timeouts, fewer/shorter locks.
10. When work is tiny, dependent/sequential, or the added complexity and bug risk outweigh the gain; measure first.

**How I should explain concurrency in an interview:** "First I check if the tasks are independent and whether they're CPU-bound or I/O-bound. Independent and CPU-bound -> spread across cores. Independent and waiting on network/DB -> threads or async. Shared mutable state -> synchronise or avoid sharing. If it's fast enough sequentially, I keep it simple."

---

## 6. Backend API Performance

### Systematic Debugging **[MUST]**

Your partial answers probably listed fixes ("add cache, add index"). Interviewers want to hear a **process** first.

> **MEASURE -> IDENTIFY BOTTLENECK -> OPTIMIZE -> MEASURE AGAIN**

Never optimise before measuring. Random fixes waste time and can make things worse.

**The mental model: where can the 5 seconds go?**

```
Client -> Network -> Load balancer -> App (your code) -> DB / Cache / External API -> back
```

| Step | Question | Tool |
|---|---|---|
| 1. Reproduce | Is it always slow or only sometimes, for some users, some inputs? | Request logs, repro with the same payload |
| 2. Break down time | How much is app code, DB, external calls, network, serialization? | Timers/logs, APM (tracing), slow-query log |
| 3. Find the biggest piece | Fix the largest contributor first (Amdahl's law) | Flame graph / trace spans |
| 4. Fix it | Pick the cheapest effective change | Below |
| 5. Re-measure | Did p95/p99 improve? Any regression? | Same load test |

**Always talk in percentiles:** average hides pain. Use **p50/p95/p99** latency. "Average 200ms but p99 6s" = some requests hit a slow path.

### Application layer **[MUST]**

| Problem | Symptom | Fix |
|---|---|---|
| Unnecessary processing | Work done that the response doesn't need | Remove, lazy-load, compute only what's needed |
| Inefficient algorithm | CPU high, time grows with data size (nested loops O(n^2)) | Better data structure (HashMap instead of list search), sort once, stream |
| Work inside loops | DB/API call per iteration | Batch, prefetch, cache |
| Excessive serialization | Big objects -> JSON is slow; entity graphs with lazy relations | Return DTOs with only needed fields; avoid serializing entire entities |
| Large payloads | Slow download time, high bandwidth | Pagination, field selection, compression (gzip), remove unused fields |
| Blocking calls | Thread waits on slow dependency | Timeouts, async, parallel calls |

### Database Optimization **[MUST]**

| Problem | How you notice | Fix |
|---|---|---|
| **Slow query** | Slow-query log, query time dominates the trace | `EXPLAIN`, add/adjust index, rewrite query |
| **Missing index** | Full table scan on a big table (`type: ALL`) | Add index on `WHERE`/`JOIN`/`ORDER BY` columns |
| **N+1 queries** | 1 query for a list + N queries for each item's relation (visible in logs as repeated similar SQL) | `JOIN FETCH` / `@EntityGraph` / batch fetching / fetch IDs then `IN (...)` |
| **Unnecessary joins** | Joining tables whose columns aren't used | Remove them, or denormalise/read model |
| **`SELECT *`** | Big rows, heavy I/O, blocks index-only scans | Select only needed columns (DTO projections) |
| **No pagination** | `findAll()` on a large table | `LIMIT/OFFSET` or keyset pagination (`WHERE id > last_id ORDER BY id LIMIT 50`) |
| **Connection pool exhausted** | Requests wait for a connection; threads blocked | Tune pool size (HikariCP), fix connection leaks, shorten transactions |
| **Long transactions/locks** | Other queries waiting | Smaller transactions, proper isolation, avoid work inside transactions |

**N+1 in Spring/JPA, explained:**

```java
List<Order> orders = orderRepo.findAll();            // 1 query
for (Order o : orders) {
    o.getCustomer().getName();                        // +1 query per order (lazy load)
}
// 1 + N queries. With 1000 orders = 1001 queries.

// Fix
@Query("select o from Order o join fetch o.customer")
List<Order> findAllWithCustomer();                    // 1 query
```

**Offset pagination gets slow on deep pages** (`OFFSET 100000` still scans the skipped rows). Keyset pagination doesn't.

### Indexes (overview, deep dive in section 7)

Speed reads on indexed columns; slow down writes and use storage. Verify with `EXPLAIN`.

### Caching **[MUST]**

**When caching helps:** data is read often, changes rarely, is expensive to compute/fetch, and slightly stale data is acceptable.

**What to cache:** product catalogues, config, user profile, computed reports, third-party API responses, session/token lookups.

**What not to cache:** highly personalised or rapidly changing data, sensitive data without care, anything where staleness is unacceptable (e.g. account balance during a transfer).

**Cache layers:** browser/CDN (HTTP caching headers) -> application cache (Redis, in-memory like Caffeine) -> database buffer cache.

**Redis conceptually:** an in-memory key-value store; very fast reads; supports TTL (expiry); shared across app instances (unlike an in-process map).

**Cache-aside pattern (most common):**

```
read(key):
    value = cache.get(key)
    if value is None:              # cache miss
        value = db.query(...)
        cache.set(key, value, ttl=300)
    return value

write(key):
    db.update(...)
    cache.delete(key)              # invalidate
```

**Invalidation basics:** TTL (simple; accepts staleness), explicit delete on write (fresher, more code), versioned keys. "There are only two hard things: cache invalidation and naming things."

**Problems to mention:** stale data, **cache stampede** (many requests miss at once and hit the DB; mitigate with locking/short TTL jitter), memory limits/eviction (LRU), extra complexity.

### API Design **[MUST]**

| Technique | What it fixes |
|---|---|
| **Pagination** (`?page=2&size=20`, or cursor) | Huge list responses |
| **Filtering / field selection** (`?status=ACTIVE&fields=id,name`) | Sending data the client doesn't need |
| **Limit max page size** (e.g. cap at 100) | Clients requesting 1,000,000 rows |
| **Batching** (one request for many ids: `GET /users?ids=1,2,3`) | N round trips for N items |
| **Avoid chatty APIs** | Client calling 10 endpoints to render one screen; create an aggregate endpoint |
| **Compression** (gzip) | Payload size |
| **HTTP caching** (`ETag`, `Cache-Control`) | Re-sending unchanged data |

### External Services **[MUST]**

Slow third-party API = **your latency is now theirs**.

| Technique | Why |
|---|---|
| **Timeouts** (connect + read) | Never wait forever; a hung dependency ties up your threads |
| **Retries with backoff + jitter** | Transient failures; retry only **idempotent** calls |
| **Circuit breaker** (Resilience4j) | Stop calling a failing service; fail fast; recover later |
| **Caching** their responses | Avoid repeated calls |
| **Parallel calls** | If you call 3 independent services, call them concurrently, not one after another |
| **Async processing** | If the user doesn't need the result immediately (send email, generate PDF), enqueue it and return `202 Accepted` |
| **Fallbacks** | Return default/partial data if the dependency is down |

### Backend Architecture **[GOOD]**

- **Synchronous** = caller waits for the result. Use when the client needs the answer now.
- **Asynchronous / background jobs** = accept the request, do the heavy work later, let the client poll or get a webhook. Use for reports, emails, image processing, bulk imports.
- **Queues** (RabbitMQ, Kafka, SQS): decouple producers and workers, absorb spikes, retry failed jobs.
- **Connection pools:** reuse DB connections instead of opening a new one per request (opening is expensive). Pool too small -> waiting; too big -> overloads the DB.
- **Horizontal scaling:** more app instances behind a load balancer (needs stateless services and shared cache/session store).
- **Read replicas:** send reads to replicas, writes to primary (replication lag trade-off).

### Monitoring **[MUST]**

| Signal | Answers |
|---|---|
| **Logs** | What happened for this request? (correlate with a request id) |
| **Metrics** | How is the system doing over time? Request rate, error rate, **latency p50/p95/p99**, CPU, memory, DB pool usage |
| **Tracing** (OpenTelemetry, Zipkin/Jaeger) | Where did *this request's* time go? (spans per DB call/external call) |
| **DB slow query log, `EXPLAIN`** | Which query is slow and why |
| **APM tools** (New Relic, Datadog, Spring Boot Actuator + Prometheus/Grafana) | All of the above combined |

### Scenario Questions **[MUST]**

**Scenario A: "An endpoint takes 5 seconds. How do you investigate?"**

- **First thought:** don't guess; measure where the time goes.
- **Investigation:** reproduce; check logs/tracing to split time into app / DB / external call / serialization; check if it's all requests or some inputs; check payload size; check recent deploys.
- **Likely bottlenecks:** slow or N+1 queries, a slow external API, fetching too much data, no pagination, blocking work in the request.
- **Solution:** fix the largest piece first (index/query rewrite, batch, cache, parallelise calls, move heavy work to background).
- **Tradeoffs:** caching adds staleness; async changes the API contract; indexes slow writes.
- **Say this:** "I'd start by measuring: add timing around DB, external calls, and serialization, or check traces. Then fix the biggest contributor and re-measure."

**Scenario B: "A database query takes 4 seconds. What do you do?"**

- **First thought:** look at the query plan.
- **Investigation:** run `EXPLAIN` / `EXPLAIN ANALYZE`; check for full table scan, rows examined vs rows returned, missing index, filesort/temp table, large joins, `SELECT *`.
- **Likely causes:** no/unused index, function on an indexed column, `LIKE '%x'`, big offset, joining big tables without indexed keys, stale statistics.
- **Solution:** add/adjust an index (often composite), rewrite the query, select fewer columns, paginate, pre-aggregate, cache.
- **Tradeoffs:** index storage and write cost; denormalisation risks inconsistency.
- **Then re-run EXPLAIN** to confirm the index is used and time drops.

**Scenario C: "The API is fast locally but slow in production. What do you check?"**

- **First thought:** what differs between the environments?
- **Check:** **data volume** (local has 100 rows, prod has millions; missing index), **network latency** to DB/third parties (different region), **concurrency** (many users, pool exhaustion, lock contention), **resource limits** (CPU/memory, GC pauses), **config differences** (pool sizes, logging level DEBUG, no caching), **cold vs warm** (first request, JIT), **external service behaviour**, **production-only middleware** (auth calls, API gateway).
- **Solution:** compare traces/metrics; replicate with production-like data and load tests.
- **Tradeoff:** a prod-like environment costs money, but it finds these issues early.

**Scenario D: "10,000 users hit the same endpoint. What could you change?"**

- **First thought:** is it the same data for everyone? Then caching is the biggest win.
- **Investigation:** what does the endpoint do; read-heavy or write-heavy; what's the current bottleneck (DB/CPU)?
- **Solutions:** cache the response (Redis / HTTP caching / CDN), scale horizontally behind a load balancer, index/optimise the query, add connection pooling, rate limit abusive clients, move non-critical work async, use read replicas.
- **Tradeoffs:** stale data, cache stampede, complexity, cost; rate limiting can reject legitimate users.
- **Test first:** load test (k6, JMeter) to find the actual limit before choosing.

**Scenario E: "A report endpoint times out."**

- Don't compute inside the request. Return `202 Accepted` with a job id, run the job in the background (queue + worker), store the result, let the client poll/download. Tradeoff: more moving parts and eventual delivery.

**Scenario F: "The list endpoint returns 20 MB of JSON."**

- Pagination, select only needed fields (DTO), compress, filter, cache. Tradeoff: clients need to handle multiple pages.

**How I should explain this in an interview:** "I don't start by guessing fixes. I measure where the time goes: app, database, or external calls, using logs, metrics, and tracing. Then I fix the biggest bottleneck, usually the database through indexes and query changes, or by caching and reducing payload, and I measure again. I also consider trade-offs like stale cache data or added write cost from indexes."

---

## 7. Database Indexes

### What an index does **[MUST]**

An index is a **separate sorted data structure** (usually a B-tree/B+tree) on one or more columns that lets the database find rows **without scanning the whole table**. Like a book's index: jump to the page instead of reading every page.

- Without index on `email`: `WHERE email = 'a@x.com'` checks all N rows -> **full table scan**, O(N).
- With index: walks the tree -> O(log N).

**Tradeoffs [MUST]**

- **Reads faster** on indexed columns (`WHERE`, `JOIN`, `ORDER BY`, `GROUP BY`).
- **Writes slower:** every `INSERT/UPDATE/DELETE` must also update every index.
- **Storage:** each index takes space.
- **Not free to have many:** more indexes = slower writes and a confused optimizer.
- **Low benefit** on small tables or columns with few distinct values.

### Composite indexes **[MUST]**

```sql
CREATE INDEX idx_orders_cust_date ON orders (customer_id, order_date);
```

- Follows the **leftmost prefix rule**: usable for `customer_id`, or `customer_id + order_date`, but **not** efficiently for `order_date` alone.
- Column order matters: put **equality** columns first, then range/sort columns.
- Helps: `WHERE customer_id = 5 AND order_date > '2026-01-01'`, and `WHERE customer_id = 5 ORDER BY order_date`.
- **Covering index:** if the index contains all columns the query needs, the database answers from the index alone (MySQL: `Extra: Using index`; Postgres: `Index Only Scan`).

### When indexes aren't useful (or aren't used) **[MUST]**

| Reason | Example |
|---|---|
| Function/expression on the column | `WHERE YEAR(order_date) = 2026`, `WHERE LOWER(email) = 'x'` (use a range, or a functional index) |
| **Leading wildcard** | `LIKE '%abc'` can't use a B-tree (`LIKE 'abc%'` can) |
| **Low selectivity** | Column like `gender` or `is_active` where most rows match; scanning is cheaper |
| Type mismatch / implicit cast | Comparing a string column to a number |
| Composite index, wrong column order | Filtering only on the 2nd column |
| **Small table** | Optimizer decides a scan is faster |
| `OR` across different columns | May skip indexes |
| **Stale statistics** | Optimizer has wrong row estimates (`ANALYZE TABLE` / `ANALYZE`) |
| Returning a large fraction of the table | Scan is cheaper than jumping through the index |
| `!=`, `NOT IN` on most values | Often scans |

### EXPLAIN **[MUST]**

`EXPLAIN` shows the **execution plan**: how the database *intends* to run the query (which tables, in what order, which indexes, estimated rows) **without running it** (in MySQL and Postgres, plain EXPLAIN doesn't execute).

**Key idea: an index existing does not mean it is used. EXPLAIN is how you verify.**

### Execution Plans

Words you must recognise:

| Term | Meaning |
|---|---|
| **Full table scan** | Reads every row (MySQL `type: ALL`; Postgres `Seq Scan`) |
| **Index scan** | Uses the index to find matching rows, then fetches them |
| **Index-only scan** | Answers from the index alone (covering index) |
| **Range scan** | Uses the index for a range (`>`, `BETWEEN`) |
| **Rows (estimate)** | How many rows the optimizer thinks it must examine |
| **Cost** | Optimizer's relative estimate of work |

### MySQL

```sql
EXPLAIN SELECT * FROM orders WHERE customer_id = 3;
```

Columns to read:

| Column | What you look for |
|---|---|
| **type** | Best to worst: `const` / `eq_ref` / `ref` -> `range` -> `index` -> **`ALL`** (full scan, the red flag) |
| **possible_keys** | Indexes the optimizer *could* use |
| **key** | The index it **actually chose**. `NULL` = no index used |
| **key_len** | How much of a composite index is used |
| **rows** | Estimated rows examined; lower is better |
| **filtered** | Percentage of examined rows kept |
| **Extra** | `Using index` (covering, good), `Using where`, `Using filesort` / `Using temporary` (extra work, often fixable) |

**How to verify an index is used (the interview answer):**

1. Run `EXPLAIN <your query>`.
2. Check the **`key`** column shows your index name, not `NULL`.
3. Check **`type`** isn't `ALL`, and **`rows`** is small.
4. For real timings use `EXPLAIN ANALYZE` (MySQL 8.0.18+), which actually runs the query and shows actual time and row counts.
5. Confirm the query got faster, not just that the index is used.

Also useful: `SHOW INDEX FROM orders;` to list indexes; `FORCE INDEX (idx)` to test (diagnosis, not a permanent fix); `ANALYZE TABLE orders;` to refresh statistics.

**Before/after example**

```sql
EXPLAIN SELECT * FROM orders WHERE customer_id = 3;
-- type: ALL, key: NULL, rows: 950000   -> full table scan

CREATE INDEX idx_orders_customer ON orders(customer_id);

EXPLAIN SELECT * FROM orders WHERE customer_id = 3;
-- type: ref, key: idx_orders_customer, rows: 3   -> index used
```

### PostgreSQL

```sql
EXPLAIN SELECT * FROM orders WHERE customer_id = 3;              -- plan only
EXPLAIN ANALYZE SELECT * FROM orders WHERE customer_id = 3;      -- runs it, shows actual time
EXPLAIN (ANALYZE, BUFFERS) SELECT ...;                           -- adds I/O info
```

| Plan node | Meaning |
|---|---|
| `Seq Scan` | Full table scan |
| `Index Scan` | Index used, fetches table rows |
| `Index Only Scan` | Covering index |
| `Bitmap Index Scan` + `Bitmap Heap Scan` | Index used for many matching rows |

Read: estimated vs actual rows (big gap = stale stats -> `ANALYZE orders;`), `actual time`, `Rows Removed by Filter`. You can also check usage counters: `pg_stat_user_indexes` (`idx_scan = 0` -> unused index candidate).

**Warning:** `EXPLAIN ANALYZE` executes the query. For `INSERT/UPDATE/DELETE`, wrap it in a transaction and roll back.

### Interview Questions

1. **How would you verify an index is actually being used?** -> Run `EXPLAIN`; check `key` (MySQL) or `Index Scan` (Postgres) rather than `ALL`/`Seq Scan`; use `EXPLAIN ANALYZE` for real timing; compare before/after.
2. **Why didn't the database use my index?** -> Function on the column, leading wildcard, low selectivity, wrong composite order, type mismatch, small table, stale statistics, optimizer estimated a scan cheaper.
3. **Why can an index sometimes make things slower?** -> Every write must update the index (slower inserts/updates/deletes), extra storage/memory pressure, and a poor index can lead the optimizer into bad plans or random I/O for big result sets.
4. **What is a composite index, and does order matter?** -> Index on several columns; order matters due to the leftmost prefix rule.
5. **What is a covering index?** -> An index containing all columns the query needs, so the table isn't touched.
6. **The table has an index on `email`, but `WHERE LOWER(email) = ...` is slow. Why?** -> The function on the column prevents use of the plain index; store normalised emails or create a functional index.
7. **Should I index every column?** -> No: write cost and storage; index what your queries filter/join/sort on.

**How I should explain this in an interview:** "An index is a sorted structure that lets the database avoid scanning the whole table. To confirm it's used I run EXPLAIN: in MySQL I look at the `key` and `type` columns, in Postgres I look for an Index Scan rather than a Seq Scan, and I use EXPLAIN ANALYZE to see real timings. If it's not used I check for functions on the column, leading wildcards, low selectivity, or the wrong column order in a composite index."

---

## 8. Middleware vs Service Layer

### Architecture **[MUST]**

```
Request
  -> Middleware / Filter / Interceptor   (cross-cutting, technical concerns)
  -> Controller                          (HTTP in/out)
  -> Service                             (business logic)
  -> Repository / DAO                    (data access)
  -> Database
```

| Layer | Belongs here | Does NOT belong here |
|---|---|---|
| **Middleware / Filter / Interceptor** (Spring: `Filter`, `HandlerInterceptor`; Express: `app.use`) | Things that apply to **many/all requests** regardless of business meaning: authentication (JWT check), request/response logging, CORS, rate limiting, request IDs, metrics, compression | Business rules, DB queries specific to a feature |
| **Controller** | Map URL + HTTP method, read params/body, basic validation (`@Valid`), call the service, choose HTTP status, return DTO | Business rules, SQL, heavy logic |
| **Service** | Business logic and rules, orchestration across repositories, transactions (`@Transactional`), authorisation based on business context ("only the order owner can cancel") | HTTP concerns (status codes, request objects) |
| **Repository** | Queries and persistence: `save`, `findBy...` | Business decisions |

**Rule of thumb:** *Does this apply to every request regardless of what it does?* -> middleware. *Does it depend on what the business operation means?* -> service.

### Request Logging **[MUST]**

**Question: "We need to log every request into the database. Middleware or service?"**

**Why middleware makes sense**

- Logging every request is **cross-cutting**: it applies to all endpoints, regardless of business logic.
- In one place (a filter), you capture method, path, status, duration, user id, without touching every controller/service.
- Services stay focused on business logic.

**But the real problem is "into the database"**

Writing one DB row **synchronously for every request**:

- Adds DB latency to **every** request (user waits for the log insert).
- Doubles the load on the DB: for N requests/sec you add N inserts/sec.
- If the DB is slow/down, **the API becomes slow/fails** because logging is on the critical path.
- Log table grows huge quickly (needs retention/partitioning).
- Contention with real business writes.

**Better approaches**

| Approach | Notes |
|---|---|
| **Write logs asynchronously**: the filter puts the log entry on an in-memory/bounded queue or message queue (Kafka/RabbitMQ); a background worker writes in **batches** | Request isn't delayed; batch inserts are far cheaper. Tradeoff: entries can be lost on a crash (acceptable for access logs; not for critical audit) |
| **Don't use the DB for access logs**: write to log files/stdout and ship to ELK/Loki/CloudWatch | Purpose-built for logs, scalable, searchable |
| **Sample** high-volume, low-value requests | Reduces volume |
| **Separate store/schema** for logs | Keeps the business DB healthy |

**What to log (and not):**

- Log: timestamp, request id/correlation id, method, path, status code, duration, user id (if authenticated), client IP, user agent, error type.
- **Don't log:** passwords, tokens, card numbers, full request bodies with personal data, anything regulated. Mask or omit.
- Avoid logging huge bodies; log size instead.

**Audit logging is different**

- Audit log = "who did what business action" (e.g. "admin X changed user Y's role"). It is about **business events** and must be **reliable**, so it often belongs in the **service layer**, in the **same transaction** as the change, so they commit or roll back together.

**A good interview answer:** "Request logging is cross-cutting, so I'd put it in a filter or interceptor. But I wouldn't insert into the database synchronously on each request; that puts DB latency on every call and makes logging a failure point. I'd push the log entry to a queue or buffer and write in batches asynchronously, or send logs to a logging system. For business audit events like 'role changed', I'd record them in the service layer within the same transaction."

### Scenarios **[MUST]** (answer first, then read)

1. Verify a JWT on every protected endpoint.
2. Log method, URL, status, and response time for every request.
3. Validate that an email field is a valid format and not blank.
4. Check that an order cannot be placed if stock is insufficient.
5. Only allow the order's owner (or an admin) to cancel that order.
6. Count requests per endpoint for metrics.
7. Record "admin deleted user 42" for compliance.
8. Insert an order and its order items together, atomically.
9. Enforce rate limiting of 100 requests/minute per IP.
10. Convert a `User` entity to a `UserResponse` DTO.

**Answers**

1. **Filter/middleware** (Spring Security filter chain): applies to all protected routes.
2. **Filter/interceptor**; the log write itself should be async (see above).
3. **Controller** (bean validation, `@Valid`) for format; service if it needs business rules (e.g. email already taken -> service + repository).
4. **Service** (business rule) using repository data.
5. **Service** (business authorisation depending on order data); the filter handles authentication ("who are you"), the service handles business-level authorisation ("may you do this to *this* order"). Role checks (`@PreAuthorize("hasRole('ADMIN')")`) can live at the controller/method-security level.
6. **Middleware/interceptor** or framework metrics (Micrometer/Actuator); cross-cutting.
7. **Service** (business audit event), same transaction, reliable.
8. **Service** with `@Transactional`; repositories do the inserts.
9. **Middleware / API gateway** (cross-cutting, applies before business logic).
10. **Service or a mapper**, returned via the controller; not the repository.

**How I should explain this in an interview:** "Middleware handles cross-cutting concerns that apply to every request: auth, logging, rate limiting. Controllers handle HTTP mapping. Services hold business rules and transactions. Repositories handle data access. If it's about how requests are handled generally it's middleware; if it depends on the business meaning it's service."

---

## 9. HTTP Methods

### The real reasoning **[MUST]**

**Don't say "GET is bad for sending data."** The correct principle:

> Methods describe **intent**. GET means "read a resource, with no side effects." Sending data is fine when it's *part of identifying what to read* (a search term, a filter, an id). It becomes a problem when the data is **sensitive, large, or the operation changes state**.

**Why GET isn't right for some data (the actual reasons)**

1. **URLs are visible and stored.** Query parameters appear in browser history, server and proxy access logs, analytics, the `Referer` header, bookmarks, and screenshots. Passwords, tokens, and personal data in a URL leak.
2. **Length limits.** URLs have practical limits (browsers/servers/proxies commonly cap at a few KB to ~8 KB). Large payloads don't fit. (Not a strict HTTP rule, but real in practice.)
3. **Semantics: GET must be safe (no side effects).** Browsers prefetch and crawlers follow GET links; caches and proxies may repeat/replay GET. A `GET /deleteUser?id=5` could be triggered by a crawler or an `<img>` tag (and is a CSRF risk).
4. **Caching:** GET responses are cacheable by default; a GET carrying sensitive parameters risks being cached/shared.
5. **Body:** GET with a body is not standardised. Many servers/clients/proxies ignore or reject it. Use POST for structured/large input.
6. **Encoding:** complex or binary data in a URL is awkward (needs URL-encoding); file uploads need a body (multipart).

**Property table**

| Method | Purpose | Request body | Safe (no side effects) | Idempotent | Cacheable | Typical success code |
|---|---|---|---|---|---|---|
| **GET** | Read | No (don't use one) | Yes | Yes | Yes | 200 |
| **POST** | Create / perform an action / process data | Yes | No | **No** | Rarely | 201 (created), 200, 202 |
| **PUT** | **Replace** a resource fully (or create at a known URI) | Yes | No | **Yes** | No | 200, 204 |
| **PATCH** | **Partially** update | Yes | No | Not guaranteed (depends on the operation) | No | 200, 204 |
| **DELETE** | Remove | Usually no | No | **Yes** | No | 204, 200 |

**Idempotent** = repeating the same request leaves the server in the same state as doing it once. `DELETE /orders/5` twice: the order is gone either way (the second may return 404, but the *state* is the same). `POST /orders` twice creates two orders. This matters for **retries**: safe to retry GET/PUT/DELETE automatically; retrying POST can duplicate (use an **idempotency key** for payments).

### GET **[MUST]**

- Use when: retrieving a resource or list, searching/filtering with short non-sensitive params, anything cacheable and bookmarkable.
- `GET /users/42`, `GET /products?category=shoes&sort=price&page=2`
- Never use for state changes or sensitive data.

### POST **[MUST]**

- Use when: creating a resource, submitting sensitive data (login), uploading files (multipart), large/complex input, actions that have side effects or aren't idempotent (`POST /orders/42/cancel`).
- Data travels in the **body**, not the URL. (Body isn't encrypted by itself; **HTTPS** encrypts both URL path/query and body in transit. The URL problem is about logs/history, not network sniffing.)
- Edge case: a complex **search** with huge filters may use `POST /search` because of URL size. That's a pragmatic exception, and you should say so.

### PUT **[MUST]**

- Replaces the whole resource. `PUT /users/42` with a full user object. Missing fields are treated as cleared/defaulted.
- Idempotent: sending the same PUT ten times gives the same result.

### PATCH **[MUST]**

- Partial update: `PATCH /users/42 {"phone": "999"}` changes only the phone.
- Not necessarily idempotent (e.g. `{"op": "increment", "field": "count"}`).

### DELETE **[MUST]**

- Removes a resource. Idempotent. Often returns `204 No Content`.
- Soft delete vs hard delete is a design choice.

### Scenarios **[MUST]**

| Scenario | Method | Why |
|---|---|---|
| Retrieve users list / one user | **GET** | Safe, cacheable, read-only (`GET /users`, `GET /users/7`) |
| **Login** | **POST** | Credentials must not appear in URL/logs; login creates a session/token (side effect) |
| **Upload a file** | **POST** (multipart/form-data) | Needs a body; too large/binary for a URL |
| **Search products** | **GET** (`/products?q=phone&minPrice=100`) | Read-only, bookmarkable, cacheable. Use POST only if the query is huge or sensitive |
| **Create an order** | **POST** | Creates a new resource; not idempotent (retry needs an idempotency key) |
| **Update profile (only phone changed)** | **PATCH** (or PUT if sending the whole profile) | Partial vs full |
| **Delete a record** | **DELETE** | Idempotent removal |
| Fetch user by **password reset token** | Prefer POST with token in body (or short-lived, one-time token over HTTPS) | Tokens in URLs get logged/leaked via Referer |
| Change password | **POST/PUT** (body) | Sensitive; never GET |
| "Mark notification as read" | **PATCH** (or POST to an action endpoint) | State change, never GET |

**Follow-up questions**

- *"Is POST more secure than GET?"* -> Not inherently; both are visible to whoever terminates TLS. POST keeps data out of URLs/logs/history/Referer. Security comes from HTTPS, auth, and validation.
- *"Can GET have a body?"* -> Technically allowed by the spec but semantics are undefined; many tools reject it. Avoid.
- *"PUT vs POST for creating?"* -> PUT when the client knows the resource URI and it's idempotent; POST when the server assigns the id.
- *"PUT vs PATCH?"* -> Replace vs partial update.

**How I should explain this in an interview:** "I choose the method by intent: GET for safe, cacheable reads, POST for creating or non-idempotent actions, PUT for full replacement, PATCH for partial updates, DELETE for removal. GET isn't bad for sending data in general; it's wrong when the data is sensitive, because URLs end up in logs and history, or when it's large, because of length limits, or when the operation changes state, because GET must be safe and can be prefetched or replayed."

---

## 10. React Performance

You answered `useMemo`, `useCallback`, `React.memo`. Correct direction. The depth interviewers want is **WHEN, WHY, and WHEN NOT**.

### Re-rendering **[MUST]**

**What causes a component to re-render**

1. Its **own state** changes (`setState`).
2. Its **parent re-renders** (by default, all children re-render too, even if their props didn't change).
3. A **context** value it consumes changes.
4. (Hooks like `useReducer`/external stores also trigger it via state updates.)

**What does NOT cause a re-render:** changing a plain variable, a `useRef` value, or mutating state directly without calling the setter.

**Important:** a re-render means React **calls your component function again** and compares the result with the previous output. It is not the same as a DOM update. Re-renders are usually cheap. Optimise only when you **measure** a problem (React DevTools Profiler, noticeable lag).

```jsx
function Parent() {
  const [count, setCount] = useState(0);
  return (
    <>
      <button onClick={() => setCount(count + 1)}>+</button>
      <ExpensiveChild />     {/* re-renders on every click even though it has no props */}
    </>
  );
}
```

**Unnecessary re-render:** a component re-renders but its output would be identical.

**Cheapest fixes come first (before any memo hooks):**

1. **Move state down.** Keep state in the smallest component that needs it.
2. **Lift content up / `children` pattern.** Pass expensive parts as `children` so they aren't recreated when the wrapper's state changes.
3. Split a big component into smaller ones.

```jsx
// Fix: state lives only where needed
function Counter() {
  const [count, setCount] = useState(0);
  return <button onClick={() => setCount(count + 1)}>{count}</button>;
}
function Parent() {
  return (<><Counter /><ExpensiveChild /></>);   // ExpensiveChild no longer re-renders
}
```

### React.memo **[MUST]**

Wraps a component so React **skips re-rendering it if its props are shallowly equal** to last time.

```jsx
const Row = React.memo(function Row({ item, onSelect }) {
  return <li onClick={() => onSelect(item.id)}>{item.name}</li>;
});
```

**Referential equality (the key concept):** objects, arrays, and functions are compared by **reference**, not content. A new `{}`, `[]` or `() => {}` created on each render is a **different reference**, so `React.memo` sees "changed props" and re-renders anyway.

```jsx
<Row item={item} onSelect={(id) => select(id)} />   // new function each render -> memo defeated
```

**Use it when:** component renders often with the **same props**, and its render is non-trivial (big lists, heavy children).
**Don't use it when:** the component is cheap, props change almost every time anyway, or you haven't measured. Memo has its own comparison cost.

### useMemo **[MUST]**

Caches a **computed value** between renders, recomputing only when dependencies change.

```jsx
const visible = useMemo(
  () => products.filter(p => p.name.includes(query)).sort(byPrice),
  [products, query]
);
```

**Two legitimate uses**

1. **Expensive calculation** (filtering/sorting thousands of items, heavy maths) that shouldn't rerun on unrelated re-renders.
2. **Stable reference** for an object/array passed to a `React.memo` child or used in another hook's dependency array.

**When not:** cheap calculations (`a + b`, small arrays). The memo bookkeeping can cost more than recalculating.

### useCallback **[MUST]**

Caches a **function reference** between renders.

```jsx
const handleSelect = useCallback((id) => setSelected(id), []);
<Row item={item} onSelect={handleSelect} />     // stable reference -> React.memo works
```

- `useCallback(fn, deps)` is essentially `useMemo(() => fn, deps)`.
- **Only useful** when the function is passed to a **memoised child** (`React.memo`) or used in a `useEffect` dependency array.
- **Useless on its own:** if the child isn't memoised, it re-renders anyway, so you've added overhead for nothing.

**useMemo vs useCallback (interview question):** `useMemo` caches the **result** of calling a function. `useCallback` caches the **function itself**.

### Other Optimizations **[MUST/GOOD]**

| Technique | What/why |
|---|---|
| **Keys in lists** **[MUST]** | Stable unique `key` (an id, not the array index when items can reorder/insert/delete) lets React match items correctly; bad keys cause wrong state and extra DOM work |
| **Virtualised lists** (`react-window`) **[GOOD]** | Render only visible rows of huge lists (thousands of items) |
| **Code splitting / lazy loading** **[MUST]** | `React.lazy(() => import('./Page'))` + `<Suspense>`; load a route/component only when needed -> smaller initial bundle |
| **Pagination / infinite scroll** **[MUST]** | Don't fetch or render everything at once |
| **Debouncing** **[MUST]** | For search-as-you-type: wait until the user pauses (e.g. 300ms) before calling the API or filtering |
| **Avoid unnecessary API calls** **[MUST]** | Cache responses (React Query/SWR), dedupe requests, correct `useEffect` dependencies |
| **Avoid inline heavy work in render** | Move to `useMemo`, or outside the component |
| **Avoid context for rapidly-changing values** **[GOOD]** | Every consumer re-renders on change; split contexts or use a state library |
| **Image optimisation** **[GOOD]** | Lazy load, correct sizes, modern formats |
| **Production build, bundle analysis** **[GOOD]** | Remove unused libraries |

**Debounce example**

```jsx
function useDebounce(value, delay = 300) {
  const [debounced, setDebounced] = useState(value);
  useEffect(() => {
    const t = setTimeout(() => setDebounced(value), delay);
    return () => clearTimeout(t);       // cancel the previous timer on each keystroke
  }, [value, delay]);
  return debounced;
}

const debouncedQuery = useDebounce(query);
useEffect(() => { fetchProducts(debouncedQuery); }, [debouncedQuery]);
```

### Common Traps **[MUST]**

1. **`useMemo`/`useCallback` everywhere.** They add cost and complexity. Use them for a measured problem or a clear need (expensive computation or stable reference to a memoised child).
2. **`useCallback` without `React.memo`** on the child: does nothing useful.
3. **`React.memo` with inline objects/functions** in props: memo is defeated.
4. **Using the array index as `key`** in dynamic lists.
5. **Wrong/missing dependency arrays** -> stale values or infinite loops.
6. **Mutating state** (`arr.push(x); setArr(arr)`) -> same reference, React may not re-render. Create a new array/object.
7. **Optimising before measuring.** Use the React Profiler first.
8. **Fetching in a loop / on every keystroke** with no debounce or caching.
9. "Re-render = slow": not true; only expensive or frequent re-renders matter.

### Scenarios (think, then read the answers)

1. A list of 5,000 products re-renders fully every time the user types in a search box. What do you do?
2. A child component receives `onClick={() => ...}` and is wrapped in `React.memo` but still re-renders. Why?
3. A dashboard page takes 6 seconds to load first time. What do you try?
4. A search box calls the API on every keystroke. Fix it.
5. A parent holds `count` state and renders a very heavy `<Chart />` that doesn't use `count`. What are your options, in order?

**Answers**

1. Debounce the search input; **move the search state** so only the list header re-renders or memoise the filtered result with `useMemo`; render rows with `React.memo` + stable keys; virtualise the list (`react-window`) and/or paginate.
2. The inline arrow function is a new reference every render, so shallow comparison sees changed props. Wrap the handler in `useCallback` (and make sure other props are stable).
3. Measure (Network tab, Lighthouse, Profiler). Code-split routes with `React.lazy`, lazy-load heavy components/charts, paginate/limit API data, cache API calls, compress/optimise images, avoid loading unused libraries.
4. Debounce (300ms), cancel stale requests (`AbortController`), cache results, require a minimum query length.
5. (a) Move `count` state down into its own component; (b) pass `<Chart />` as `children`; (c) wrap `Chart` in `React.memo`; (d) memoise its props if they're objects/functions.

**How I should explain this in an interview:** "A component re-renders when its state changes, its parent re-renders, or a context it uses changes. Re-renders are usually cheap, so I profile first. I start with structural fixes like moving state down. Then `React.memo` skips re-rendering when props are shallowly equal, `useMemo` caches an expensive result or stabilises an object reference, and `useCallback` stabilises a function reference, which only matters if a memoised child or an effect dependency uses it. I avoid using them everywhere since they add overhead."

---

## 11. Thinking Under Pressure

### HOW TO THINK WHEN YOU DON'T IMMEDIATELY KNOW THE ANSWER

Your pattern from the interview: *you knew the concepts but jumped to an unrelated technique, started solving before fully understanding, or gave a partially correct answer.* The fix is a **process**, not confidence.

| Step | What to do | Example phrase |
|---|---|---|
| 1. **Pause** | 3 seconds of silence is fine. Breathe. | "Give me a moment to think about this." |
| 2. **Restate** | Say the problem in your own words (catches misreadings) | "So I need to find the floor where the egg starts breaking." |
| 3. **Clarify assumptions** | Ask 1-2 questions that *change* the solution | "How many eggs do I have?" / "Is the data sorted?" / "Is this CPU-bound or waiting on I/O?" |
| 4. **Break it down** | Input, output, constraints, sub-steps | "The input is ..., the output is ..." |
| 5. **Think aloud** | Your reasoning is what's being graded | "My first thought is X, but that might fail if ..." |
| 6. **Identify constraints** | Size, time, memory, concurrency | "If n is a million, O(n^2) is too slow." |
| 7. **Start simple** | Brute force first, it proves you understand the problem | "The simple way is ..., that's O(n^2)." |
| 8. **Improve it** | Find the bottleneck, apply the right tool | "The repeated lookup is the slow part, a hash map fixes it." |
| 9. **Explain tradeoffs** | Time vs space, complexity vs benefit | "This uses extra memory but cuts time to O(n)." |
| 10. **If stuck, say what you know** | Show the partial map instead of freezing | "I know the threshold is monotonic, so I can cut the space; I'm not sure yet how to handle the 2-egg limit." |

**The anti-panic rule:** never jump to a technique *name*. Jump to the *problem's structure* (monotonic? contiguous? independent tasks? need to combine?).

### Example dialogues

**Example 1: Egg drop**

**BAD RESPONSE**
> Interviewer: "You have 100 floors and an egg. Find the floor where it breaks."
> You: "Um... divide and conquer? I'd split it into two halves and solve each..."
> *(Silence. You're committed to the wrong approach.)*

**BETTER RESPONSE**
> You: "Let me restate: there's a threshold floor, and eggs break at or above it. I want to find that floor. Can I ask how many eggs I have, and whether a surviving egg can be reused?"
> Interviewer: "Just one egg, reusable if it survives."
> You: "Then I can't risk a break, because if it breaks I have nothing left. So I'd start at floor 1 and go up one at a time. Worst case 100 drops. If I had many eggs, I could binary search because the outcome is monotonic. With two eggs, I'd jump in steps and then go linear in the last gap."

**Example 2: Multithreading scenario**

**BAD RESPONSE**
> Interviewer: "You have 1,000 files and 10 workers. How would you process them?"
> You: "I'd do divide and conquer, split the array into halves recursively..."

**BETTER RESPONSE**
> You: "The files are independent, so there's nothing to combine except maybe counting results. I'd hand each worker a share, or better, put the files in a queue and have each worker take the next file when free, so nobody sits idle. In code that's a thread pool with 10 workers. I'd check whether the work is CPU-bound or I/O-bound, since that affects the pool size. The shared state is just the queue and a counter, so that's where I'd need synchronisation."

**Example 3: A concept you only half remember**

**BAD RESPONSE**
> Interviewer: "How do you check that an index is being used?"
> You: "I think... you check the table? Or maybe the indexes tab?" *(guessing)*

**BETTER RESPONSE**
> You: "I know there's a command that shows the query plan: `EXPLAIN`. I'm confident it shows whether the database chooses an index or scans the whole table. In MySQL I'd look at the `key` column. I don't remember every column exactly, but I'd also use `EXPLAIN ANALYZE` to get actual timings."

*(Stating what you know, honestly marking the gap, and still moving forward, is stronger than guessing.)*

**Example 4: A question with a trap (GET vs POST)**

**BAD RESPONSE**
> "GET is bad for sending data, so always use POST."

**BETTER RESPONSE**
> "GET is for reads and is fine with small, non-sensitive parameters like a search term. I'd avoid it when data is sensitive, since URLs get logged and stored in history, when the payload is large, or when the action changes state, since GET should be safe and can be prefetched or replayed."

**Example 5: You realise mid-answer you were wrong**

> "Actually, let me correct that. I said X, but thinking it through, Y is more accurate because ..."

Self-correction reads as strength, not weakness.

### Techniques that are not memorised scripts

- **Keep a fixed opening ritual**: *restate -> one clarifying question -> simplest approach.* The wording changes; the structure doesn't.
- **Talk in trade-offs:** "It depends on X; if X, then A; otherwise B."
- **Name the category first:** "This is a database question about ..." / "This looks like a search problem ..." helps you choose the right toolbox.
- **Ask for a hint the right way:** "Am I right to consider it as a ... problem?" is better than silence.
- **Don't fake knowledge.** "I haven't used that directly, but I'd approach it like this..." is fine.
- **Time-box silence:** if you're stuck for 30 seconds, say what you've ruled out and what you're trying.
- **After your answer, check:** "Does that cover what you were looking for, or should I go deeper on something?"

### Your personal pre-answer checklist (write it on paper for the next interview)

```
1. What is the OUTPUT?
2. What is GIVEN?
3. What CONSTRAINT changes the answer?
4. What is the SIMPLEST solution?
5. What is the BOTTLENECK in it?
6. Which PATTERN fits the bottleneck (not the topic)?
```

---

## 12. Interview Speaking Practice

**How to use:** set a 90-second timer per question. Answer **out loud**, ideally recording yourself. Use structure: **definition -> how it works -> example -> trade-off/when not**. After answering, compare with the "hit these points" list below each group.

### SQL

1. Explain LEFT JOIN. *Hit:* keeps all left rows, NULLs on no match, example of "customers with no orders".
2. Why use HAVING instead of WHERE? *Hit:* WHERE before grouping (rows), HAVING after (aggregates), example.
3. How do you find records with no matching records? *Hit:* LEFT JOIN + IS NULL, NOT EXISTS, NOT IN NULL trap.
4. Walk me through writing: "total amount for customers who ordered more than once". *Hit:* output grain, join, group by customer id, HAVING COUNT > 1.
5. Difference between `COUNT(*)` and `COUNT(column)`.

### Python

6. How do you count the frequency of elements using a dictionary? *Hit:* `get(x, 0) + 1`, dry run small example.
7. How do you solve Two Sum with a dictionary, and why is it faster than two loops? *Hit:* store value->index, check complement, O(n) vs O(n^2), check before insert.
8. What does `d.get(key)` do differently from `d[key]`?

### DSA / Problem solving

9. How would you approach the egg-drop problem? *Hit:* clarify eggs, monotonic, binary search with many eggs, linear with one, stepping with two.
10. How do you decide between divide and conquer and just splitting work across workers? *Hit:* combine step vs independent tasks.
11. Given a new problem, what do you do in the first minute? *Hit:* restate, output, constraints, brute force, pattern.
12. What is a closure? *Hit:* a function that remembers variables from the scope where it was created, even after that scope has finished; example of a counter factory; used for data privacy, callbacks, memoisation.

### Concurrency

13. What is the difference between concurrency and parallelism?
14. How would you process 1 million independent tasks? *Hit:* independence, worker pool + queue, CPU vs I/O-bound, batching, failure/retry, memory, scaling out with a message queue.
15. What is a race condition and how do you prevent it?
16. When would you not use threads?

### Backend

17. How would you optimise a slow API? *Hit:* measure -> identify bottleneck (app/DB/external) -> fix (index, N+1, pagination, cache, async) -> measure again; mention p95/p99.
18. A DB query takes 4 seconds. What do you do? *Hit:* EXPLAIN, index, rewrite, select fewer columns, paginate.
19. How do you know an index is being used? *Hit:* EXPLAIN (`key`, `type`), Seq Scan vs Index Scan, EXPLAIN ANALYZE.
20. Why might the database not use my index? *Hit:* function on column, leading wildcard, low selectivity, composite order, stats.
21. What's the difference between middleware and service layer? *Hit:* cross-cutting vs business logic; flow.
22. Where would you put request logging, and what's the catch? *Hit:* middleware/filter, but write asynchronously/batch, don't log secrets; audit events in service.
23. Why is GET not suitable for sensitive data? *Hit:* URL in logs/history/Referer, length limits, GET is safe/idempotent.
24. Explain PUT vs PATCH vs POST.
25. What is idempotency and why does it matter for retries?
26. When does caching help, and what can go wrong? *Hit:* read-heavy, rarely changing; stale data, invalidation, stampede.

### React

27. What causes a React component to re-render?
28. What is the difference between `useMemo` and `useCallback`? *Hit:* value vs function; both for stable references or expensive work.
29. When would you not use `useMemo`/`useCallback`/`React.memo`?
30. How would you optimise a slow search box with a large list? *Hit:* debounce, memoised list rows, virtualisation, pagination.

### Project

31. **Explain your Legal Document Intelligence project.** Practise this structure (2 minutes):
    1. **Problem:** what pain does it solve (finding answers in long legal documents)?
    2. **Approach:** RAG flow: how documents are processed and chunked, embedded with SentenceTransformers, indexed in FAISS, retrieved for a query, and how T5 / TextRank produce the summary or answer.
    3. **Your part and decisions:** why FAISS, why FastAPI, why those chunk sizes or models. Be ready to defend each choice.
    4. **Challenges:** something that went wrong and how you fixed it.
    5. **Limits and next steps:** what you'd improve (evaluation, better models, hallucination control).
    Be ready for follow-ups: *How do you evaluate retrieval quality? What happens if no relevant chunk exists? How would it scale?*
32. Explain one backend project (for example the microservices issue tracker): services, how they communicate, how you handled auth (JWT), and what you'd change.

**Self-review after recording:** Did I (a) restate, (b) give a structure, (c) give an example, (d) mention a trade-off, (e) avoid jumping to random techniques? Re-record any answer that missed two or more.

---

## 13. Final Weakness Checklist

Tick only when you can **do it without looking**.

### SQL
- [ ] I can say the routine: output grain -> join -> group -> HAVING -> order.
- [ ] I can write "customers with more than one order + total" from scratch.
- [ ] I can write "customers with no orders" using LEFT JOIN + IS NULL **and** NOT EXISTS.
- [ ] I can explain why `COUNT(o.id)` (not `COUNT(*)`) is used after a LEFT JOIN.
- [ ] I know why a WHERE on the right table breaks a LEFT JOIN.
- [ ] I solved all 12 practice problems, then compared with the key.

### Python dictionaries
- [ ] I can write frequency counting with `get()` and with if/else.
- [ ] I can write Two Sum, duplicates, most frequent, grouping, merge, common keys from scratch.
- [ ] I know `items()`, `get()` with default, `in` (keys only).
- [ ] I can do a dry run on paper for one of them.

### DSA recognition
- [ ] I ask the six reading questions before choosing an approach.
- [ ] I can explain D&C vs parallel workers vs binary search in my own words.
- [ ] I can explain the egg problem for 1, 2, and many eggs.
- [ ] I can name the pattern for each of the 10 scenarios before coding.

### Concurrency
- [ ] Process vs thread, concurrency vs parallelism, CPU-bound vs I/O-bound.
- [ ] Race condition example + two fixes.
- [ ] Thread pool and task queue explained with the "10 people, 1,000 files" story.
- [ ] I can pick sequential / threads / processes / async / background job for a scenario.

### Backend performance
- [ ] I say "measure -> bottleneck -> optimise -> measure again" first.
- [ ] I can name at least five database and three API-design fixes.
- [ ] I can explain N+1 and show the fix.
- [ ] I can explain when caching helps and its risks.
- [ ] I can answer all four interviewer scenarios (5-second endpoint, 4-second query, fast locally/slow in prod, 10,000 users).

### Indexes
- [ ] I can explain what an index does and its trade-offs.
- [ ] I can verify usage with EXPLAIN (MySQL `key`/`type`; Postgres Seq Scan vs Index Scan) and EXPLAIN ANALYZE.
- [ ] I can list at least five reasons an index isn't used.

### Architecture
- [ ] I can draw the Request -> Filter -> Controller -> Service -> Repository -> DB flow and say what belongs where.
- [ ] I can explain the request-logging answer **including** why synchronous DB writes are a problem and the async alternative.
- [ ] I can separate request logging from audit logging.

### HTTP
- [ ] I can explain *why* GET isn't right for sensitive/large/state-changing data without saying "GET is bad".
- [ ] I can state safe/idempotent for each method and why idempotency matters for retries.

### React
- [ ] I can list what causes re-renders.
- [ ] I can explain referential equality and why inline functions break `React.memo`.
- [ ] I can explain when **not** to use `useMemo`/`useCallback`.
- [ ] I can mention code splitting, keys, debouncing, pagination.

### Communication
- [ ] I practised the pause -> restate -> clarify -> simplest solution routine aloud.
- [ ] I recorded and reviewed at least 10 speaking questions.
- [ ] I can explain my Legal Document project in 2 minutes with decisions and trade-offs.

---

*End of document. If you miss an item, go back to that section, redo the practice problems without looking, and say the "How I should explain this in an interview" box in your own words.*
