# BVA Analysis for UserInterface

#### Important Note

For methods whose behaviour depends on user *input values* (e.g., `getNumberOfPlayers()`), we treat the textual console
input as the variable under test and identify numeric and non‑numeric boundary conditions. Display‑only methods (
`displayWelcome`, `displayHelp`, etc.) do not produce functional *outputs* beyond printing, so their BVAs focus on the
*state variations* that influence what is printed (e.g., empty vs. non‑empty hands).

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
