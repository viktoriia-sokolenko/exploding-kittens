# BVA Analysis for Abstract **Card** Class

#### Important Note

`Card` is an abstract class wrapping a non-null `CardType` and declaring an abstract `play(Player p)` to be implemented by each subtype. We'll test:

0. **Constructor** – rejects `null` type.
1. **`getCardType()`** – returns the assigned `CardType`.
2. **`equals(Object o)`** / **`hashCode()`** – equality and hash based solely on `cardType`.
3. **`play(Player p)`** – abstract method contract that subclasses can implement.

---

## Method 0: ```public Card(CardType type)```

### Step 1–3 Results

|            | Input                | Output                                                                                             |
| ---------- | -------------------- | -------------------------------------------------------------------------------------------------- |
| **Step 1** | CardType enum        | assigns card to the type                                                                           |
| **Step 2** | CardType enum        | `NullPointerException`, Card Object                                                                |
| **Step 3** | valid types, null    | `new TestCard(CardType.ATTACK)` <br> `new TestCard(CardType.DEFUSE)` <br> … <br> `new TestCard(CardType.EXPLODING_KITTEN)` |

### Step 4

|               | Input             | Output                                       | Implemented? | Test name                                             |
| ------------- | ----------------- | -------------------------------------------- |--------------|-------------------------------------------------------|
| Test Case 1   | type `null`       | throws `NullPointerException`                | ✅            | `constructor_withNullType_throwsNullPointerException` |
| Test Case 2   | type `CardType.EXPLODING_KITTEN`     | returns non-null `Card`; `getCardType()==EXPLODING_KITTEN` | ✅ | `constructor_withValidCardType_createsCard`           |

---  

## Method 1: ```public CardType getCardType()```

### Step 1–3 Results

|            | Input        | Output                         |
| ---------- | ------------ | ------------------------------ |
| **Step 1** | Card         | the CardType belonging to Card |
| **Step 2** | Card Object  | CardType Object                |
| **Step 3** | Card Object  | CardType Object                |

### Step 4

|              | Input             | Output            | Implemented? | Test name                     |
| ------------ | ----------------- | ----------------- |--------------| ----------------------------- |
| Test Case 1  | Card created with `CardType.ATTACK`          | returns `CardType.ATTACK`       | ✅            | `getType_returnsAssignedType` |

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
| **Step 3** | comparisons:                       | 1. `normalCard.equals(normalCard)` <br> 2. `normalCard.equals(null)` <br> 3. `normalCard.equals("stringObject")` <br> 4. `normalCard.equals(anotherNormalCard)` <br> 5. `normalCard.equals(explodingKittenCard)` |

### Step 4 – equals()

| Test Case                              | System under test        | Expected behavior | Implemented? | Test name                            |
| -------------------------------------- | ------------------------ | ----------------- |--------------| ------------------------------------ |
| 1. `card.equals(card)`                 | `equals(this)`           | `true`            | ✅            | `equals_compareWithItself_returnsTrue`            |
| 2. `card1.equals(card2)` (same type)   | `equals(otherSameType)`  | `true`            | ✅            | `equals_compareWithSameTypeCard_returnsTrue`        |
| 3. `card.equals(null)`                 | `equals(null)`           | `false`           | ✅            | `equals_compareWithNull_returnsFalse`           |
| 4. `card.equals("stringObject")`       | `equals(differentClass)` | `false`           | ✅            | `equals_compareWithDifferentClass_returnsFalse` |
| 5. `card1.equals(card2)` (different types) | `equals(differentType)`      | `false`           | ✅            | `hashCode_differentCards_mayReturnDifferentHashCode` (covers different types)  |

### Step 4 – hashCode()

| Test Case                               | System under test | Expected behavior                                           | Implemented? | Test name                       |
| --------------------------------------- | ----------------- | ----------------------------------------------------------- |--------------| ------------------------------- |
| Test Case 1 | `hashCode()` | `card1.hashCode() == card2.hashCode()` (same type) | ✅ | `hashCode_sameCards_returnsSameHashCode`   |
| Test Case 2 | `hashCode()` | `card1.hashCode()` vs `card2.hashCode()` likely different (different types) | ✅ | `hashCode_differentCards_mayReturnDifferentHashCode` |

---

## Method 3: ```public abstract void play(Player player)```
> Abstract in `Card`; concrete subtypes override to implement effects.

### Step 1–3 Results

|            | Input                                                                 | Input 2        | Output                                                                                                                                                                              |
| ---------- | --------------------------------------------------------------------- | -------------- |-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Step 1** | takes in a player and allows them to perform the action of the card   | different card | none (delegates to subclass override)                                                                                                                                               |
| **Step 2** | Player object and Case (Optional)                                     | Card Object    | void, Exception                                                                                                                                                                     |
| **Step 3** | Player Object, Null                                                   | Card Object    | TestCard implementation, `NullPointerException` <br> 1. `player = null` <br> 2. `player = validMockPlayer` |

### Step 4

|              | System under test      | Expected behavior                                    | Implemented? | Test name                                        |
| ------------ | ---------------------- | ---------------------------------------------------- |--------------| ------------------------------------------------ |
| Test Case 1  | `testCard.play(null)`           | throws `NullPointerException`                       | ✅            | `play_nullPlayer_throwsNullPointerException`     |
| Test Case 2  | `testCard.play(mockPlayer)`   | method executes without throwing (abstract contract works) | ✅ | `play_validPlayer_doesNotThrowException`          |

#### Important Note for Method 3:
**Specific card behavior testing is out of scope** for this abstract Card class BVA. Tests for actual game mechanics should be implemented in separate test classes:
- `AttackCardTest.java` - for attack card damage and win condition checks
- `DrawTwoCardTest.java` - for drawing two cards functionality
- `SkipTurnCardTest.java` - for turn skipping mechanics
- `DefuseCardTest.java` - for bomb defusing behavior
- `NukeCardTest.java` - for nuke effects

This BVA focuses only on testing that the abstract method contract works and can be implemented by subclasses.
