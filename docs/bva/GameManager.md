# BVA Analysis for **GameManager**

#### Important Note

The `GameManager` serves as the bridge between the **UI layer** and the **GameEngine**, wiring user events (button clicks, menu selections) to engine calls and updating the UI with engine state or error messages.  It does not contain no core game logic, only validation of inputs, invocation of `GameEngine` methods, and UI updates.

Key methods:

1. **Constructor** – Accepts a `GameEngine` and a `UI` interface, then calls `bindUI()`.
2. **`bindUI()`** – Registers UI callbacks to controller handlers.
3. **`handlePlayCard(Player, Card)`** – Invoked by UI when a play‐card event occurs.
4. **`handleNextTurn()`** – Invoked by UI when a next‐turn event occurs.

---

## Method 1: **Constructor** `public GameManager(GameEngine engine, UI ui)`

### Step 1–3 Results

|        | Input                                                | Output / State Change                                                             |
| ------ | ---------------------------------------------------- | --------------------------------------------------------------------------------- |
| Step 1 | `engine`, `ui`                                       | none (stores references, calls `bindUI()`)                                        |
| Step 2 | Cases: `engine == null`, `ui == null`, both non-null | throws `NullPointerException` if any argument is null; otherwise calls `bindUI()` |
| Step 3 | Combine boundary cases                               |                                                                                   |

### Step 4

| Test Case   | System under test                      | Expected behavior                                | Implemented? | Test name                                           |
|-------------|----------------------------------------| ------------------------------------------------ |--------------| --------------------------------------------------- |
| Test Case 1 | `new GameManager(null, validUI)`       | throws `NullPointerException`                    | no           | `constructor_nullEngine_throwsNullPointerException` |
| Test Case 2 | `new GameManager(validEngine, null)`   | throws `NullPointerException`                    | no           | `constructor_nullUI_throwsNullPointerException`     |
| Test Case 3 | `new GameManager(validEngine, validUI)` | stores references; calls `bindUI()` exactly once | no           | `constructor_validArgs_callsBindUIOnce`             |

---

## Method 2: `private void bindUI()`


#### TODO: clarify this idea with the team: will need to update once I start working on the actual UI for the game

### Step 1–3 Results

|        | Input                          | Output / State Change                                                           |
| ------ | ------------------------------ | ------------------------------------------------------------------------------- |
| Step 1 | none                           | none (registers UI listeners via `ui.onPlayCard(...)` and `ui.onNextTurn(...)`) |
| Step 2 | UI methods may throw or accept | Should register two listeners; should not invoke them immediately               |
| Step 3 | —                              |                                                                                 |

### Step 4

| Test Case   | System under test                     | Expected behavior                                                                        | Implemented? | Test name                                  |
|-------------| ------------------------------------- | ---------------------------------------------------------------------------------------- |--------------| ------------------------------------------ |
| Test Case 1 | After construction                    | `ui.onPlayCard` called once with non-null callback; `ui.onNextTurn` called once likewise | no           | `bindUI_registersBothListenersExactlyOnce` |
| Test Case 2 | Calling `bindUI()` again (if exposed) | registers additional listeners, but duplicate registration is not expected in normal use | no           | `bindUI_idempotentWhenCalledOnce`          |

---

## Method 3: `public void handlePlayCard(Player p, Card c)`

### Step 1–3 Results

|        | Input                                                                             | Output / State Change                                                                                                   |
| ------ | --------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| Step 1 | `Player p`, `Card c`                                                              | none (delegates to `engine.playCard(p,c)`; catches exceptions; updates UI)                                              |
| Step 2 | Cases: `p` or `c` null; `engine.playCard` throws various exceptions; success path | on nulls: `ui.showError(...)`; on specific exceptions: `ui.showError(...)`; on success: `ui.refresh(engine.getState())` |
| Step 3 | Combine boundary inputs                                                           |                                                                                                                         |

### Step 4

| Test Case   | System under test                        | Expected behavior                                                                                                          | Implemented? | Test name                              |
|-------------| ---------------------------------------- | -------------------------------------------------------------------------------------------------------------------------- |--------------| -------------------------------------- |
| Test Case 1 | `handlePlayCard(null, validCard)`        | calls `ui.showError("Player cannot be null")` (or equivalent message); does **not** call `engine.playCard` or `ui.refresh` | no           | `handlePlayCard_nullPlayer_showsError` |
| Test Case 2 | `handlePlayCard(validPlayer, null)`      | calls `ui.showError("Card cannot be null")`; no engine call or refresh                                                     | no           | `handlePlayCard_nullCard_showsError`   |
| Test Case 3 | engine throws `IllegalArgumentException` | calls `engine.playCard`, catches exception, invokes `ui.showError(exception.getMessage())`; no `ui.refresh`                | no           | `handlePlayCard_engineIAE_showsError`  |
| Test Case 4 | engine succeeds                          | calls `engine.playCard`, then `ui.refresh(engine.getState())`; does not call `ui.showError`                                | no           | `handlePlayCard_success_refreshesUI`   |

---

## Method 4: `public void handleNextTurn()`

### Step 1–3 Results

|        | Input                                                                                | Output / State Change                                                          |
| ------ | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------ |
| Step 1 | none                                                                                 | none (delegates to `engine.nextTurn()`; catches exceptions; updates UI)        |
| Step 2 | Cases: engine not initialized; deck empty (`NoCardsToMoveException`); normal success | on exception: `ui.showError(...)`; on success: `ui.refresh(engine.getState())` |
| Step 3 | —                                                                                    |                                                                                |

### Step 4

| Test Case   | System under test      | Expected behavior                                                                                    | Implemented? | Test name                              |
|-------------| ---------------------- | ---------------------------------------------------------------------------------------------------- |--------------| -------------------------------------- |
| Test Case 1 | before `startGame()`   | calls `engine.nextTurn()`, catches `IllegalStateException`, calls `ui.showError(...)`, no `refresh`  | no           | `handleNextTurn_beforeInit_showsError` |
| Test Case 2 | after init, deck empty | calls `engine.nextTurn()`, catches `NoCardsToMoveException`, calls `ui.showError(...)`, no `refresh` | no           | `handleNextTurn_emptyDeck_showsError`  |
| Test Case 3 | normal case            | calls `engine.nextTurn()`, then `ui.refresh(engine.getState())`; no `showError`                      | no           | `handleNextTurn_success_refreshesUI`   |
