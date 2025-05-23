# BVA Analysis for **Card**

#### Important Note

`Card` is a value object wrapping a non-null `CardType` and declaring an abstract `play(Player p)` to be implemented by each subtype.  We’ll test:

0. **Constructor** – rejects `null` type.
1. **`getType()`** – returns the assigned `CardType`.
2. **`equals(Object o)`** / **`hashCode()`** – equality and hash based solely on `type`.
3. **`play(Player p)`** – validates `p` and delegates to subclass logic.

---

## Method 0: **Constructor**

`public Card(CardType type)`

### Step 1–3 Results

|            | Input          | Output / State Change                                                                              |
| ---------- | -------------- | -------------------------------------------------------------------------------------------------- |
| **Step 1** | `type = X`     | assigns `this.type = X`                                                                            |
| **Step 2** | `type == null` | throws `NullPointerException("type")`                                                              |
| **Step 3** | valid types    | `new Card(CardType.ATTACK)` <br> `new Card(CardType.DEFUSE)` <br> … <br> `new Card(CardType.NUKE)` |

### Step 4

| Test Case                      | System under test | Expected behavior                            | Implemented? | Test name                 |
| ------------------------------ | ----------------- | -------------------------------------------- |--------------| ------------------------- |
| 1. `new Card(null)`            | constructor       | throws `NullPointerException`                | no           | `ctor_nullType_throwsNPE` |
| 2. `new Card(CardType.ATTACK)` | constructor       | returns non-null `Card`; `getType()==ATTACK` | no           | `ctor_validType_setsType` |

---

## Method 1: **Getter**

`public CardType getType()`

### Step 1–3 Results

|            | Input        | Output           |
| ---------- | ------------ | ---------------- |
| **Step 1** | any `Card c` | returns `c.type` |
| **Step 2** | —            | N/A              |
| **Step 3** | —            | N/A              |

### Step 4

| Test Case                    | System under test | Expected behavior | Implemented? | Test name                     |
| ---------------------------- | ----------------- | ----------------- |--------------| ----------------------------- |
| `c=new Card(X); c.getType()` | `getType()`       | returns `X`       | no           | `getType_returnsAssignedType` |

---

## Method 2: **Equality & Hashing**

`public boolean equals(Object o)`
`public int hashCode()`

### Step 1–3 Results

|            | Input                              | Output / State Change                                                                                                                                  |
| ---------- | ---------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Step 1** | `o == this`                        | `true`                                                                                                                                                 |
| **Step 2** | `o == null`                        | `false`                                                                                                                                                |
|            | `!(o instanceof Card)`             | `false`                                                                                                                                                |
|            | `o` a `Card` with same `type`      | `true`                                                                                                                                                 |
|            | `o` a `Card` with different `type` | `false`                                                                                                                                                |
| **Step 3** | comparisons:                       | 1. `c.equals(c)` <br> 2. `c.equals(null)` <br> 3. `c.equals("foo")` <br> 4. `c.equals(new Card(sameType))` <br> 5. `c.equals(new Card(differentType))` |

### Step 4 – equals()

| Test Case                              | System under test        | Expected behavior | Implemented? | Test name                            |
| -------------------------------------- | ------------------------ | ----------------- |--------------| ------------------------------------ |
| 1. `c.equals(c)`                       | `equals(this)`           | `true`            | no           | `equals_self_returnsTrue`            |
| 2. `c.equals(new Card(sameType))`      | `equals(otherSameType)`  | `true`            | no           | `equals_sameType_returnsTrue`        |
| 3. `c.equals(null)`                    | `equals(null)`           | `false`           | no           | `equals_null_returnsFalse`           |
| 4. `c.equals(objOfOtherClass)`         | `equals(differentClass)` | `false`           | no           | `equals_differentClass_returnsFalse` |
| 5. `c.equals(new Card(differentType))` | `equals(otherType)`      | `false`           | no           | `equals_differentType_returnsFalse`  |

### Step 4 – hashCode()

| Test Case                               | System under test | Expected behavior                                           | Implemented? | Test name                       |
| --------------------------------------- | ----------------- | ----------------------------------------------------------- |--------------| ------------------------------- |
| `c1=new Card(X); c2=new Card(X)`        | `hashCode()`      | `c1.hashCode()==c2.hashCode()`                              | no           | `hashCode_sameType_equalHash`   |
| `new Card(X).hashCode() vs new Card(Y)` | `hashCode()`      | likely different, but no strict requirement beyond contract | no           | `hashCode_differentType_varies` |

---

## Method 3: **Action**

`public abstract void play(Player p)`

> Abstract in `Card`; concrete subtypes override to implement effects.

### Step 1–3 Results

|            | Input                      | Output / State Change                                                            |
| ---------- | -------------------------- | -------------------------------------------------------------------------------- |
| **Step 1** | valid `Player p`           | none (delegates to subclass override)                                            |
| **Step 2** | `p == null`                | throws `NullPointerException("p")`                                               |
|            | `p` not in game (optional) | may throw `IllegalArgumentException`                                             |
| **Step 3** | scenarios:                 | 1. `p = null` <br> 2. `p = validPlayerInGame` <br> 3. `p = validPlayerNotInGame` |

### Step 4

| Test Case                                | System under test      | Expected behavior                                    | Implemented? | Test name                               |
| ---------------------------------------- | ---------------------- | ---------------------------------------------------- |--------------| --------------------------------------- |
| 1. `play(null)`                          | `play(null)`           | throws `NullPointerException`                        | no           | `play_nullPlayer_throwsNullPointer`     |
| 2. `play(validPlayer)` in `AttackCard`   | `AttackCard.play(p)`   | applies damage + `checkWinCondition()`               | no           | `play_attack_appliesDamageAndChecksWin` |
| 3. `play(validPlayer)` in `DrawTwoCard`  | `DrawTwoCard.play(p)`  | instructs engine to draw two cards for `p`           | no           | `play_drawTwo_givesTwoCards`            |
| 4. `play(validPlayer)` in `SkipTurnCard` | `SkipTurnCard.play(p)` | advances turn without draw (engine handles rotation) | no           | `play_skipTurn_skipsAndChecksWin`       |
