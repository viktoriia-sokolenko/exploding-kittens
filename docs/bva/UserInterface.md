# BVA Analysis for UserInterface

#### Important Note

For methods whose behaviour depends on user *input values* (e.g., `getNumberOfPlayers()`), we treat the textual console
input as the variable under test and identify numeric and non‑numeric boundary conditions. Display‑only methods (
`displayWelcome`, `displayHelp`, etc.) do not produce functional *outputs* beyond printing, so their BVAs focus on the
*state variations* that influence what is printed (e.g., empty vs. non‑empty hands).

#### Important Note
Whenever you see `card1` without specifying its type, that means I am using Parametrized Testing, and the test runs for cards with all the available card types.

Whenever you see the keywords "non-empty list" or noticed that the states takes in a list of two
cards without specifying the Card Type (i.e `[card1, card2]`), that means that the test cases uses a Parameterized Test
that uses all the different Card Types into a list of two different Cards and performs _Parameterized Tests_ based on
it.
This Parameterized Testing is grouped as:

* `[CardType.NORMAL, CardType.ATTACK]`
* `[CardType.DEFUSE, CardType.SKIP]`
* `[CardType.FAVOR, CardType.EXPLODING_KITTEN]`
* `[CardType.SHUFFLE, CardType.ALTER_THE_FUTURE]`
* `[CardType.SEE_THE_FUTURE, CardType.NUKE]`

This is done like this because the main purpose of deck isn't too focused on the Card Types inside the deck but the
functionality of deck working with all the different types of cards. As a result, these Parameterized Testing ensures
that since Deck is a collection of Cards, no matter the Cards given in the collection, the operation still functions as
expected.

* In Other Words, in addition to deck being classified as a Collection (of Cards), if you'd like, it's also a case of
  the listed Card Types above

Also, unless specified that I am dealing with  `Duplicates` states, each card listed below is from different
CardTypes. When you see `card2.1` and `card2.2` that means that I am dealing with duplicate cards.

---

## Method under test: `public int getNumberOfPlayers()`

The console prompt accepts **strings** that are parsed into integers. Valid inputs are the inclusive range **2-5**.

### Step-1‑3-Results

|        | Input                                                                                                                                   | Expected result / state change                                                        |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| Step 1 | text typed by the user, which is a raw console string                                                                                   | returns value (integer (2-5)) *or* error message                                      |
| Step 2 | **Cases**                                                                                                                               | **Cases**                                                                             |
| Step 3 | `"foo"` (non‑numeric)  <br>`"1"` (below min) <br>`"2"` (lower bound) <br>`"3"` (in range) <br>`"5"` (upper bound) <br>`"6"` (above max) | loops with error; loops with error; returns-2; returns-3; returns-5; loops with error |

### Step-4

|             | System under test                              | Expected output / state transition                                        | Implemented? | Test name                                              |
|-------------|------------------------------------------------|---------------------------------------------------------------------------|--------------|--------------------------------------------------------|
| Test Case 1 | `"3"`                                          | returns **3** on first attempt; **no** error printed                      | ✅            | `getNumberOfPlayers_validFirst_tryReturnsImmediately`  |
| Test Case 2 | `"foo"`, `"6"`, `"2"`                          | returns **2** after two invalid attempts; error message printed **twice** | ✅            | `getNumberOfPlayers_invalidThenValid_promptsUntilGood` |
| Test Case 3 | `"2"`                                          | returns **2** (lower bound)                                               | ❌            | *not yet implemented*                                  |
| Test Case 4 | `"5"`                                          | returns **5** (upper bound)                                               | ❌            | *not yet implemented*                                  |
| Test Case 5 | `"1"`, `"4"`, `"5"` (below‑min then mid‑range) | returns **5**; error message printed **two** times                        | ❌            | *not yet implemented*                                  |

---

## Method under test: `public void displayPlayerHand(Player player)`

The printed output varies with **hand size** and **card composition**.

### Step-1‑3-Results

|        | Input 1                                      | Input 2                                         | Expected result / state change                                  |
|--------|----------------------------------------------|-------------------------------------------------|-----------------------------------------------------------------|
| Step 1 | Number of Cards: integer `n` (0-–-hand-size) | Card Distribution multiset of `CardType` counts | Printed Hand listing: formatted list, or "(empty)" when `n-=-0` |
| Step 2 | **Cases**                                    | **Cases**                                       | **Printed result**                                              |
| Step 3 | `0`, `1`, `2+`                               | single type, multiple types                     | "(empty)"; single line; multiple lines                          |

### Step-4

|             | System under test                                      | Expected output / state transition                        | Implemented? | Test name                                       |
|-------------|--------------------------------------------------------|-----------------------------------------------------------|--------------|-------------------------------------------------|
| Test Case 1 | Hand size **0**                                        | prints "(empty)"                                          | ✅            | `displayPlayerHand_emptyHand_showsEmptyMessage` |
| Test Case 2 | Hand size **2**, both `SKIP`                           | prints indices **0** and **1**, each labelled `SKIP`      | ✅            | `displayPlayerHand_withThreeCards_listsAll`     |
| Test Case 3 | Hand size **1**, card type `DEFUSE`                    | prints exactly one line labelled `DEFUSE`                 | ❌            | *not yet implemented*                           |
| Test Case 4 | Hand size **3**, mixed types (`SKIP`,`ATTACK`,`FAVOR`) | prints three lines in correct order with matching indices | ❌            | *not yet implemented*                           |

---

## Method under test: `public void displayError(String message)`

### Step-1‑3-Results

|        | Input            | Expected result / state change          |
|--------|------------------|-----------------------------------------|
| Step 1 | arbitrary string | "Error: "-+-`message` printed to stderr |
| Step 2 | **Cases**        | **Printed result**                      |
| Step 3 | non‑empty, empty | prepended with "Error: "                |

### Step-4

|             | System under test    | Expected output / state transition | Implemented? | Test name                     |
|-------------|----------------------|------------------------------------|--------------|-------------------------------|
| Test Case 1 | message = "oops"     | stderr contains "Error: oops"      | ✅            | `displayError_printsToStderr` |
| Test Case 2 | message = "" (empty) | stderr contains "Error: "          | ❌            | *not yet implemented*         |

---

## Method under test: `public String getUserInput()`

### Step-1‑3-Results

|        | Input                            | Expected result / state change       |
|--------|----------------------------------|--------------------------------------|
| Step 1 | arbitrary line                   | same string without newline with ">" |
| Step 2 | **Cases**                        | Printed with ">"                     |
| Step 3 | "hello world" (non-empty), empty | "> hello world"                      |

### Step-4

|             | System under test         | Expected output / state transition          | Implemented? | Test name                          |
|-------------|---------------------------|---------------------------------------------|--------------|------------------------------------|
| Test Case 1 | input = "hello world"     | returns "hello world"; stdout contains "> " | ✅            | `getUserInput_readsLineAndPrompts` |
| Test Case 2 | input = "" (empty string) | returns ""; stdout contains "> "            | ❌            | *not yet implemented*              |

---

## Method under test: `public void displayCardPlayed(Card card)` & `public void displayDrawnCard(Card card)`

These methods share the same structure: they print a line referencing the card’s type.

### Step-1‑3-Results

|        | Input                  | Expected result / state change           |
|--------|------------------------|------------------------------------------|
| Step 1 | any `CardType`         | "You played: TYPE" *or* "You drew: TYPE" |
| Step 2 | **Cases**              | **Printed**                              |
| Step 3 | `SKIP`, `DEFUSE`, etc. | corresponding message                    |

### Step-4

|             | System under test         | Expected output / state transition   | Implemented? | Test name                                        |
|-------------|---------------------------|--------------------------------------|--------------|--------------------------------------------------|
| Test Case 1 | card type `SKIP`          | stdout contains "You played: SKIP"   | ✅            | `displayCardPlayed_showCorrectText`              |
| Test Case 2 | card type `SKIP` drawn    | stdout contains "You drew: SKIP"     | ✅            | `displayCardPlayed_andDrawnCard_showCorrectText` |
| Test Case 3 | card type `DEFUSE` played | stdout contains "You played: DEFUSE" | ❌            | *not yet implemented*                            |

## Method under test: `String getUserInput(String message)`

### Step 1-3 Results

|            | Input 1                                       | Input 2                               | Output                                        | Side‑effect                                               |
|------------|-----------------------------------------------|---------------------------------------|-----------------------------------------------|-----------------------------------------------------------|
| **Step 1** | message to be printed before asking for input | arbitrary line from console           | returned string (same string without newline) | print message and from new line prints "> " prompt        |
| **Step 2** | String                                        | String                                | String                                        | Boolean (does it prints message in new line and then "> " |
| **Step 3** | null, empty string, more than 1 character     | empty string, more than one character | empty string, more than one character         | yes/no                                                    |

### Step 4

|             | System under test                                        | Expected output                                           | Implemented?       | Test name                                                                   |
|-------------|----------------------------------------------------------|-----------------------------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | message `null`, input `"hello world"`                    | returns "hello world"; stdout contains "> "               | :white_check_mark: | `getUserInput_withNullMessageAndNonEmptyConsoleInput_returnsConsoleInput`   |
| Test Case 2 | message `""`, input `"hello world"`                      | returns "hello world"; stdout contains "> "               | :white_check_mark: | `getUserInput_withEmptyMessageAndNonEmptyConsoleInput_returnsConsoleInput`  |
| Test Case 3 | message `"message"`, input1 `""`, input2 `"hello world"` | returns ""; stdout repeats "> " and "message" twice       | :white_check_mark: | `getUserInput_withNonEmptyMessageAndEmptyConsoleInput_keepsAskingForInput`  |
| Test Case 4 | message `"message"`, input `"hello world"`               | returns "hello world"; stdout contains "> " and "message" | :white_check_mark: | `getUserInput_withValidMessageAndInput_returnsConsoleInputAndPrintsMessage` |

## Method under test: `int getNumericUserInput(String message)`

### Step 1-3 Results

|            | Input 1                                       | Input 2                                               | Output                                        | Side‑effect                                               |
|------------|-----------------------------------------------|-------------------------------------------------------|-----------------------------------------------|-----------------------------------------------------------|
| **Step 1** | message to be printed before asking for input | arbitrary line from console                           | returned string (same string without newline) | print message and from new line prints "> " prompt        |
| **Step 2** | String                                        | String                                                | String                                        | Boolean (does it prints message in new line and then "> " |
| **Step 3** | null, empty string, more than 1 character     | empty string, more than one character, is not integer | empty string, more than one character         | yes/no                                                    |

### Step 4

|             | System under test                                  | Expected output                                    | Implemented?       | Test name                                                                  |
|-------------|----------------------------------------------------|----------------------------------------------------|--------------------|----------------------------------------------------------------------------|
| Test Case 1 | message `null`, input `"1"`                        | returns `1`                                        | :white_check_mark: | `getNumericUserInput_withNullMessage_returnsConsoleInput`                  |
| Test Case 1 | message `""`, input `"0"`                          | returns 0; stdout contains "> " and "message"      | :white_check_mark: | `getNumericUserInput_withEmptyMessage_returnsConsoleInput`                 |
| Test Case 1 | message `""`, input1 `"hello world"`, input2 `"0"` | returns 0; stdout contains "> " twice              | :white_check_mark: | `getNumericUserInput_withNonNumericConsoleInput_keepsAskingForInput`       |
| Test Case 2 | message `"message"`, input1 `""`, input2 `"2"`     | returns 3; stdout repeats "> " and "message" twice | :white_check_mark: | `getNumericUserInput_withEmptyConsoleInput_keepsAskingForInput`            |
| Test Case 2 | message `"message"`, input `"2"`                   | returns 3; stdout contains "> " and "message"      | :white_check_mark: | `getNumericUserInput_withIntegerInput_returnsConsoleInputAndPrintsMessage` |

## Method under test: `void displayCardsFromDeck(List<Card> cards, int deckSize)`

### Step 1-3 Results

|            | Input 1                                                                                               | Input 2                                    | Output                                                                      |
|------------|-------------------------------------------------------------------------------------------------------|--------------------------------------------|-----------------------------------------------------------------------------|
| **Step 1** | cards to be printed to be viewed by the player                                                        | deck size to be used to print card indexes | prints ":Top of deck:" and card types and their indexes or throws exception |
| **Step 2** | Collection (Empty, Exactly 1 Element, Exactly 2 Elements, More than 2 Elements containing Duplicates) | Count                                      | String or exception                                                         |
| **Step 3** | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                             | `-1`, `0`, `1`, `>1`, `>cards.size()`      | List of cards printed or IllegalArgumentException                           |

### Step 4

|             | System under test                               | Expected output                                                                           | Implemented?       | Test name                                                                        |
|-------------|-------------------------------------------------|-------------------------------------------------------------------------------------------|--------------------|----------------------------------------------------------------------------------|
| Test Case 1 | cards `[]` deckSize `1`                         | prints `"No cards to view"`                                                               | :white_check_mark: | `displayCardsFromDeck_withEmptyCards_printsNoCardsMessage`                       |
| Test Case 2 | cards `[card1]`, deckSize `-1`                  | `IllegalArgumentException` (deckSize can not be negative)                                 | :white_check_mark: | `displayCardsFromDeck_withNegativeDeckSize_throwsIllegalArgumentException`       |
| Test Case 3 | cards `[card1]`, deckSize `0`                   | `IllegalArgumentException` (deckSize is less than number of cards to display)             | :white_check_mark: | `displayCardsFromDeck_withOneCardAndDeckSizeZero_throwsIllegalArgumentException` |
| Test Case 4 | cards `[card1]`, deckSize `1`                   | prints `":Top of deck:"`, `card1Type, index 0`                                            | :white_check_mark: | `displayCardsFromDeck_withOneCard_printCardTypeAndIndex`                         |
| Test Case 5 | cards `[card1, card2]`, deckSize `1`            | `IllegalArgumentException` (deckSize is less than number of cards to display)             |                    | `displayCardsFromDeck_withTwoCardsAndDeckSizeOne_throwsIllegalArgumentException` |
| Test Case 6 | cards `[card1, card2]`, deckSize `2`            | prints `":Top of deck:"`, `card1Type, index 1`, `card2Type, index 0`                      |                    | `displayCardsFromDeck_withTwoCards_printCardTypeAndIndex`                        |
| Test Case 7 | cards `[card1, card2.1, card2.2]`, deckSize `4` | prints `":Top of deck:"`, `card1Type, index 3`, `card2Type, index 2`, `card2Type, index1` |                    | `displayCardsFromDeck_withThreeCardsAndDuplicate_printCardTypeAndIndex`          |