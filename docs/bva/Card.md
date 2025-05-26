# BVA Analysis for **Card**

#### Important Note

`Card` is a value object wrapping a non-null `CardType` and declaring an abstract `play(Player p)` to be implemented by each subtype.  We’ll test:

0. **Constructor** – rejects `null` type.
1. **`getType()`** – returns the assigned `CardType`.
2. **`equals(Object o)`** / **`hashCode()`** – equality and hash based solely on `type`.
3. **`play(Player p)`** – validates `p` and delegates to subclass logic.

---

## Method 0: ```public Card(CardType type)```

### Step 1–3 Results

|            | Input                | Output                                                                                             |
| ---------- | -------------------- | -------------------------------------------------------------------------------------------------- |
| **Step 1** | CardType enum        | assigns card to the type                                                                           |
| **Step 2** | CardType enum        | `NullPointerException`, Card Object                                                                |
| **Step 3** | valid types, null    | `new Card(CardType.ATTACK)` <br> `new Card(CardType.DEFUSE)` <br> … <br> `new Card(CardType.NUKE)` |

### Step 4

|               | Input             | Output                                       | Implemented? | Test name                                             |
| ------------- | ----------------- | -------------------------------------------- |--------------|-------------------------------------------------------|
| Test Case 1   | type `null`       | throws `NullPointerException`                |              | `constructor_withNullType_throwsNullPointerException` |
| Test Case 2   | type `ATTACK`     | returns non-null `Card`; `getType()==ATTACK` |              | `constructor_withValidCardType_createsCard`           |

---  

## Method 1: ```public CardType getType()```

### Step 1–3 Results

|            | Input        | Output                         |
| ---------- | ------------ | ------------------------------ |
| **Step 1** | Card         | the CardType belonging to Card |
| **Step 2** | Card Object  | CardType Object                |
| **Step 3** | Card Object  | CardType Object                |

### Step 4

|              | Input             | Output            | Implemented? | Test name                     |
| ------------ | ----------------- | ----------------- |--------------| ----------------------------- |
| Test Case 1  | card `X`          | returns `X`       |              | `getType_returnsAssignedType` |

---

## Method 2: ```public boolean equals(Object o)``` and ```public int hashCode()``` (Equal and HashCode)
#### Important Note:
These methods are used for testing purposes, and some of the Java methods are used so that we can ensure and override the equals method of the object and its hashCode. This is something that allows methods like `AssertEqual` and determines whether two objects are part of the Card class. Which tests if two objects are equal. This BVA was created specially since it's beyond what we've learned in class.

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
| 1. `card.equals(c)`                       | `equals(this)`           | `true`            |              | `equals_self_returnsTrue`            |
| 2. `card.equals(new Card(sameType))`      | `equals(otherSameType)`  | `true`            |              | `equals_sameType_returnsTrue`        |
| 3. `card.equals(null)`                    | `equals(null)`           | `false`           |              | `equals_null_returnsFalse`           |
| 4. `card.equals(objOfOtherClass)`         | `equals(differentClass)` | `false`           |              | `equals_differentClass_returnsFalse` |
| 5. `card.equals(new Card(differentType))` | `equals(otherType)`      | `false`           |              | `equals_differentType_returnsFalse`  |

### Step 4 – hashCode()

| Test Case                               | System under test | Expected behavior                                           | Implemented? | Test name                       |
| --------------------------------------- | ----------------- | ----------------------------------------------------------- |--------------| ------------------------------- |
| `card1=new Card(X); card2=new Card(X)`  | `hashCode()`      | `card1.hashCode()==card2.hashCode()`                        |              | `hashCode_sameType_equalHash`   |
| `new Card(X).hashCode() vs new Card(Y)` | `hashCode()`      | likely different, but no strict requirement beyond contract |              | `hashCode_differentType_varies` |

---

## Method 3: ```public abstract void play(Player player)```
> Abstract in `Card`; concrete subtypes override to implement effects.

### Step 1–3 Results

|            | Input                                                                 | Input 2        |Output                                                                           |
| ---------- | --------------------------------------------------------------------- | -------------- |-------------------------------------------------------------------------------- |
| **Step 1** | takes in a player and allows them to perform the action of the card   | different card |none (delegates to subclass override)                                            |
| **Step 2** | Player object and Case (Optional)                                     | Card Object    |void, Exception                                                                  |
| **Step 3** | Player Object, Null, Optional: In Game or Not in Game (valid Player)  | Card Object    |All Cards, <be>, `NullPointerException`, `IllegalArgumentException` (optional) <br> 1. `player = null` <br> 2. `player = validPlayerInGame` <br> 3. `player = validPlayerNotInGame` |

### Step 4

|              | System under test      | Expected behavior                                    | Implemented? | Test name                                        |
| ------------ | ---------------------- | ---------------------------------------------------- |--------------| ------------------------------------------------ |
| Test Case 1  | player `null`           | throws `NullPointerException`                       |              | `play_nullPlayer_throwsNullPointerException`     |
| Test Case 2  | `AttackCard.play(p)`   | applies damage + `checkWinCondition()`               |              | `play_attack_appliesDamageAndChecksWin`          |
| Test Case 3  | `DrawTwoCard.play(p)`  | instructs engine to draw two cards for `p`           |              | `play_drawTwo_givesTwoCards`                     |
| Test Case 4  | `SkipTurnCard.play(p)` | advances turn without draw (engine handles rotation) |              | `play_skipTurn_skipsAndChecksWin`                 |
