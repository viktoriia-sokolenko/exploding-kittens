# BVA Analysis for **GameManager**

#### Important Note

The `GameManager` sits between the **UI layer** and the **GameEngine**, validating UI inputs, calling engine methods, and updating the UI with either new state or error messages. It contains no core game logic.

Key methods:

1. **Constructor** – stores `GameEngine` and `UI`, then calls `bindUI()`.
2. **`bindUI()`** – registers UI callbacks (`onPlayCard`, `onNextTurn`) to its handler methods.
3. **`handlePlayCard(Player p, Card c)`** – called by UI, delegates to `engine.playCard(p,c)`, catches exceptions, and updates UI.
4. **`handleNextTurn()`** – called by UI, delegates to `engine.nextTurn()`, catches exceptions, and updates UI.

---

## Method 1: **Constructor**

`public GameManager(GameEngine engine, UI ui)`

### Step 1–3 Results

|            | Input                                                         | Output / State Change                                                                                        |
| ---------- | ------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| **Step 1** | `engine`, `ui`                                                | stores references; calls `bindUI()`                                                                          |
| **Step 2** | • `engine == null`  <br> • `ui == null`  <br> • both non-null | throws `NullPointerException` if either arg is null; otherwise proceeds to `bindUI()`                        |
| **Step 3** |                                                               | **(engine, ui)**: <br> 1. `(null, validUI)`  <br> 2. `(validEngine, null)`  <br> 3. `(validEngine, validUI)` |

### Step 4

| Test Case | System under test                       | Expected behavior                          | Implemented? | Test name                                           |
| --------- | --------------------------------------- | ------------------------------------------ |--------------| --------------------------------------------------- |
| 1         | `new GameManager(null, validUI)`        | throws `NullPointerException`              | no           | `constructor_nullEngine_throwsNullPointerException` |
| 2         | `new GameManager(validEngine, null)`    | throws `NullPointerException`              | no           | `constructor_nullUI_throwsNullPointerException`     |
| 3         | `new GameManager(validEngine, validUI)` | stores refs; calls `bindUI()` exactly once | no           | `constructor_validArgs_callsBindUIOnce`             |

---

## Method 2: **`private void bindUI()`**

> *Registers UI listeners via* `ui.onPlayCard(...)` *and* `ui.onNextTurn(...)`.

### Step 1–3 Results

|            | Input                                                                                        | Output / State Change                                                                                           |
| ---------- | -------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| **Step 1** | none                                                                                         | registers two listeners on the UI                                                                               |
| **Step 2** | • UI callbacks succeed <br> • `ui.onPlayCard(...)` throws <br> • `ui.onNextTurn(...)` throws | on exception: propagates (or logs) <br> otherwise no immediate invocation                                       |
| **Step 3** |                                                                                              | 1. both registrations succeed <br> 2. `onPlayCard` registration throws <br> 3. `onNextTurn` registration throws |

### Step 4

| Test Case | System under test        | Expected behavior                                                                             | Implemented? | Test name                                  |
| --------- | ------------------------ | --------------------------------------------------------------------------------------------- |--------------| ------------------------------------------ |
| 1         | after construction       | `ui.onPlayCard(...)` & `ui.onNextTurn(...)` each called exactly once, with non-null callbacks | no           | `bindUI_registersBothListenersExactlyOnce` |
| 2         | calling `bindUI()` again | registers additional listeners (idempotency not required in normal use)                       | no           | `bindUI_idempotentWhenCalledOnce`          |

---

## Method 3: **`public void handlePlayCard(Player p, Card c)`**

### Step 1–3 Results

|            | Input                                                                                                         | Output / State Change                                                                                                                                |
| ---------- | ------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Step 1** | `Player p`, `Card c`                                                                                          | none (delegates to `engine.playCard(p,c)`; catches exceptions; updates UI)                                                                           |
| **Step 2** | • `p == null`  <br> • `c == null`  <br> • `engine.playCard` throws `IllegalArgumentException`  <br> • success | on nulls or engine exception: `ui.showError(...)`; on success: `ui.refresh(engine.getState())`                                                       |
| **Step 3** |                                                                                                               | 1. `(null, validCard)`  <br> 2. `(validPlayer, null)`  <br> 3. `(validPlayer, validCard)` + IAE thrown  <br> 4. `(validPlayer, validCard)` + success |

### Step 4

| Test Case | System under test                        | Expected behavior                                                                                   | Implemented? | Test name                              |
| --------- | ---------------------------------------- | --------------------------------------------------------------------------------------------------- |--------------| -------------------------------------- |
| 1         | `handlePlayCard(null, validCard)`        | calls `ui.showError("Player cannot be null")`; no engine call or refresh                            | no           | `handlePlayCard_nullPlayer_showsError` |
| 2         | `handlePlayCard(validPlayer, null)`      | calls `ui.showError("Card cannot be null")`; no engine call or refresh                              | no           | `handlePlayCard_nullCard_showsError`   |
| 3         | engine throws `IllegalArgumentException` | calls `engine.playCard`, catches exception, then `ui.showError(exception.getMessage())`; no refresh | no           | `handlePlayCard_engineIAE_showsError`  |
| 4         | engine succeeds                          | calls `engine.playCard`, then `ui.refresh(engine.getState())`; no `ui.showError`                    | no           | `handlePlayCard_success_refreshesUI`   |

---

## Method 4: **`public void handleNextTurn()`**

### Step 1–3 Results

|            | Input                                                                                   | Output / State Change                                                                                                        |
| ---------- | --------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| **Step 1** | none                                                                                    | none (delegates to `engine.nextTurn()`; catches exceptions; updates UI)                                                      |
| **Step 2** | • engine not started → ISE  <br> • deck empty → `NoCardsToMoveException`  <br> • normal | on exception: `ui.showError(...)`; on success: `ui.refresh(engine.getState())`                                               |
| **Step 3** |                                                                                         | 1. before `startGame()`  <br> 2. after `startGame(...)` with empty deck  <br> 3. after `startGame(...)` with ≥1 card in deck |

### Step 4

| Test Case | System under test      | Expected behavior                                                               | Implemented? | Test name                              |
| --------- | ---------------------- | ------------------------------------------------------------------------------- |--------------| -------------------------------------- |
| 1         | before `startGame()`   | catches `IllegalStateException`; calls `ui.showError(...)`; no `refresh`        | no           | `handleNextTurn_beforeInit_showsError` |
| 2         | after init, deck empty | catches `NoCardsToMoveException`; calls `ui.showError(...)`; no `refresh`       | no           | `handleNextTurn_emptyDeck_showsError`  |
| 3         | after init, deck ≥1    | calls `engine.nextTurn()`, then `ui.refresh(engine.getState())`; no `showError` | no           | `handleNextTurn_success_refreshesUI`   |
