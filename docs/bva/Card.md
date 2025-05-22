# BVA Analysis for **Card**

#### Important Note

The `Card` class is a simple value object that encapsulates a non-null `CardType` and declares an abstract `play(Player p)` method to be overridden by concrete card subtypes.  It provides:

1. A **constructor** `Card(CardType type)` that must reject `null`.
2. A **getter** `getType()` returning the assigned type.
3. An **`equals(Object o)`** (and corresponding `hashCode()`) based solely on `type`.
4. An abstract **`play(Player p)`** method, to be implemented by each card subtype.

---

## Method 0: **Constructor** `public Card(CardType type)`

### Step 1–3 Results

|        | Input          | Output / State Change                    |
| ------ | -------------- | ---------------------------------------- |
| Step 1 | `type = X`     | assigns `this.type = X`                  |
| Step 2 | `type == null` | throws `NullPointerException`            |
| Step 3 | valid `type`   | new `Card` created with `getType() == X` |

### Step 4

| Test Case                      | System under test | Expected behavior                              | Implemented?         | Test name                 |
| ------------------------------ | ----------------- | ---------------------------------------------- | -------------------- | ------------------------- |
| 1. `new Card(null)`            | constructor       | throws `NullPointerException`                  | :white\_check\_mark: | `ctor_nullType_throwsNPE` |
| 2. `new Card(CardType.ATTACK)` | constructor       | returns non-null `Card`; `getType() == ATTACK` | :white\_check\_mark: | `ctor_validType_setsType` |

---

## Method 1: **Getter** `public CardType getType()`

### Step 1–3 Results

|        | Input        | Output                  |
| ------ | ------------ | ----------------------- |
| Step 1 | any `Card c` | none (returns `c.type`) |
| Step 2 | —            | N/A                     |
| Step 3 | —            | N/A                     |

### Step 4

| Test Case                      | System under test | Expected behavior | Implemented?         | Test name                     |
| ------------------------------ | ----------------- | ----------------- | -------------------- | ----------------------------- |
| `c = new Card(X); c.getType()` | `getType()`       | returns `X`       | :white\_check\_mark: | `getType_returnsAssignedType` |

---

## Method 2: **Equality** `public boolean equals(Object o)` and **Hashing** `public int hashCode()`

### Step 1–3 Results

|        | Input                              | Output / State Change |
| ------ | ---------------------------------- | --------------------- |
| Step 1 | `o == this`                        | `true`                |
| Step 2 | `o == null`                        | `false`               |
|        | `!(o instanceof Card)`             | `false`               |
|        | `o` a `Card` with same `type`      | `true`                |
|        | `o` a `Card` with different `type` | `false`               |
| Step 3 | —                                  | N/A                   |

### Step 4 – equals()

| Test Case                              | System under test        | Expected behavior | Implemented?         | Test name                            |
| -------------------------------------- | ------------------------ | ----------------- | -------------------- | ------------------------------------ |
| 1. `c.equals(c)`                       | `equals(this)`           | `true`            | :white\_check\_mark: | `equals_self_returnsTrue`            |
| 2. `c.equals(new Card(sameType))`      | `equals(otherSameType)`  | `true`            | :white\_check\_mark: | `equals_sameType_returnsTrue`        |
| 3. `c.equals(null)`                    | `equals(null)`           | `false`           | :white\_check\_mark: | `equals_null_returnsFalse`           |
| 4. `c.equals(objOfOtherClass)`         | `equals(differentClass)` | `false`           | :white\_check\_mark: | `equals_differentClass_returnsFalse` |
| 5. `c.equals(new Card(differentType))` | `equals(otherType)`      | `false`           | :white\_check\_mark: | `equals_differentType_returnsFalse`  |

### Step 4 – hashCode()

| Test Case                               | System under test | Expected behavior                                    | Implemented?         | Test name                       |
| --------------------------------------- | ----------------- | ---------------------------------------------------- | -------------------- | ------------------------------- |
| `c1 = new Card(X); c2 = new Card(X);`   | `hashCode()`      | `c1.hashCode() == c2.hashCode()`                     | :white\_check\_mark: | `hashCode_sameType_equalHash`   |
| `c = new Card(X);` with different types | `hashCode()`      | likely different, but no requirement beyond contract | :white\_check\_mark: | `hashCode_differentType_varies` |

---

## Method 3: **Action** `public abstract void play(Player p)`

> *Abstract in `Card`, implemented in each subclass.  Core engine logic (deck draws, damage application, turn effects, win checks) is tested in the `GameEngine` BVA; here we simply validate input and dispatch.*

### Step 1–3 Results

|        | Input                      | Output / State Change                 |
| ------ | -------------------------- | ------------------------------------- |
| Step 1 | valid `Player p`           | none (delegates to subclass override) |
| Step 2 | `p == null`                | throws `NullPointerException`         |
|        | `p` not managed (optional) | may throw `IllegalArgumentException`  |
| Step 3 | —                          | N/A                                   |

### Step 4

| Test Case                                | System under test      | Expected behavior                                        | Implemented?         | Test name                               |
| ---------------------------------------- | ---------------------- | -------------------------------------------------------- | -------------------- | --------------------------------------- |
| 1. `play(null)`                          | `play(null)`           | throws `NullPointerException`                            | :white\_check\_mark: | `play_nullPlayer_throwsNullPointer`     |
| 2. `play(validPlayer)` in `AttackCard`   | `AttackCard.play(p)`   | applies damage via engine and then `checkWinCondition()` | :white\_check\_mark: | `play_attack_appliesDamageAndChecksWin` |
| 3. `play(validPlayer)` in `DrawTwoCard`  | `DrawTwoCard.play(p)`  | instructs engine to draw two cards for `p`               | :white\_check\_mark: | `play_drawTwo_givesTwoCards`            |
| 4. `play(validPlayer)` in `SkipTurnCard` | `SkipTurnCard.play(p)` | advances turn without draw (engine handles rotation)     | :white\_check\_mark: | `play_skipTurn_skipsAndChecksWin`       |

