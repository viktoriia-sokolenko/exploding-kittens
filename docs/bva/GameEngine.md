# BVA Analysis for **GameEngine**

#### Important Note

The `GameEngine` now **owns** the shared `Deck`. In its constructor it:

1. **Creates** a new `Deck`.
2. **Injects** that `Deck` into a new `PlayerManager`.
3. **Injects** the `PlayerManager` into a new `TurnManager`.

After construction, the engine is in a “not started” state until `startGame(...)` is called. All core methods (`startGame`, `playCard`, `nextTurn`, `eliminatePlayer`) end by invoking `checkWinCondition()`.

---

## Method 0: **Constructor**


### Step 1–3 Results

|        | Input | Output / State Change                                                      |
| ------ | ----- | -------------------------------------------------------------------------- |
| Step 1 | none  | none (creates `deck`, `playerManager(deck)`, `turnManager(playerManager)`) |
| Step 2 | —     | N/A—no validation cases                                                    |
| Step 3 | —     | N/A                                                                        |

### Step 4

| Test Case   | System under test  | Expected behavior                                                                     | Implemented? | Test name                         |
| ----------- | ------------------ | ------------------------------------------------------------------------------------- |--------------| --------------------------------- |
| Test Case 1 | `new GameEngine()` | `deck` non-null; `playerManager` and `turnManager` constructed using that same `deck` | no            | `ctor_initializesDeckAndManagers` |

---

## Method 1: `public void startGame(int n) throws InvalidNumberofPlayersException`

### Step 1–3 Results

|        | Input                               | Output / State Change                                                                                                                      |
| ------ | ----------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| Step 1 | `n`                                 | none (delegates to `playerManager.addPlayers(n)`, `playerManager.makePlayersDrawTheirInitialHands()`, `turnManager.setPlayerManager(...)`) |
| Step 2 | `n`: `<2`, `2–5`, `>5`              | `<2` or `>5`: throws `InvalidNumberofPlayersException`; otherwise proceeds                                                                 |
| Step 3 | boundary values: `1`, `2`, `5`, `6` |                                                                                                                                            |

### Step 4

| Test Case   | System under test | Expected behavior / state transition                                                                                                                                               | Implemented? | Test name                                                        |
| ----------- | ----------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |--------------| ---------------------------------------------------------------- |
| Test Case 1 | `startGame(1)`    | throws `InvalidNumberofPlayersException`                                                                                                                                           | no            | `startGame_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| Test Case 2 | `startGame(6)`    | throws `InvalidNumberofPlayersException`                                                                                                                                           | no            | `startGame_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| Test Case 3 | `startGame(2)`    | calls in order:<br>1. `playerManager.addPlayers(2)`<br>2. `playerManager.makePlayersDrawTheirInitialHands()`<br>3. `turnManager.setPlayerManager(...)`<br>4. `checkWinCondition()` | no            | `startGame_twoPlayers_initializesManagersAndChecksWinCondition`  |
| Test Case 4 | `startGame(5)`    | same sequence with `n=5`                                                                                                                                                           | no            | `startGame_fivePlayers_initializesManagersAndChecksWinCondition` |

---

## Method 2: `public void playCard(Player p, Card c)`

### Step 1–3 Results

|        | Input 1: `Player p`                             | Input 2: `Card c` | Output / State Change                                                                 |
| ------ | ----------------------------------------------- | ----------------- | ------------------------------------------------------------------------------------- |
| Step 1 | `p`                                             | `c`               | none (calls `c.play(this, p)`)                                                        |
| Step 2 | `p` or `c` null; `p` not in game; `c` unhandled |                   | throws `NullPointerException`, `IllegalArgumentException`, or `IllegalStateException` |
| Step 3 | combine invalid and valid                       |                   | after dispatch, calls `checkWinCondition()`                                           |

### Step 4

| Test Case   | System under test                       | Expected behavior                                                                        | Implemented? | Test name                                               |
| ----------- | --------------------------------------- | ---------------------------------------------------------------------------------------- |--------------| ------------------------------------------------------- |
| Test Case 1 | `playCard(null, validCard)`             | throws `NullPointerException`                                                            | no            | `playCard_nullPlayer_throwsNullPointerException`        |
| Test Case 2 | `playCard(validPlayer, null)`           | throws `NullPointerException`                                                            | no            | `playCard_nullCard_throwsNullPointerException`          |
| Test Case 3 | `playCard(playerNotManaged, validCard)` | throws `IllegalArgumentException("Player not in game")`                                  | no            | `playCard_unknownPlayer_throwsIllegalArgumentException` |
| Test Case 4 | valid attack scenario                   | invokes `AttackCard.play(this,p)`; then calls `checkWinCondition()`                      | no            | `playCard_attack_delegatesEffectAndChecksWinCondition`  |
| Test Case 5 | unhandled card type                     | throws `IllegalStateException("Unhandled card type")`; still calls `checkWinCondition()` | no            | `playCard_unhandledType_throwsAndChecksWinCondition`    |

---

## Method 3: `public void nextTurn() throws NoCardsToMoveException`

### Step 1–3 Results

|        | Input                           | Output / State Change                                                                                            |
| ------ | ------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| Step 1 | none                            | none (calls `turnManager.endTurnAndDraw()`)                                                                      |
| Step 2 | before init; deck empty; normal | before init: throws `IllegalStateException`; deck empty: propagates `NoCardsToMoveException`; otherwise advances |
| Step 3 | combine scenarios               | after successful advance, calls `checkWinCondition()`                                                            |

### Step 4

| Test Case   | System under test                    | Expected behavior                                                                                         | Implemented? | Test name                                                |
| ----------- | ------------------------------------ | --------------------------------------------------------------------------------------------------------- |--------------| -------------------------------------------------------- |
| Test Case 1 | before `startGame(...)`              | throws `IllegalStateException("GameEngine not initialized")`                                              | no            | `nextTurn_beforeInit_throwsIllegalStateException`        |
| Test Case 2 | after init, deck empty               | `turnManager.endTurnAndDraw()` throws `NoCardsToMoveException`; propagates; no `checkWinCondition()` call | no            | `nextTurn_emptyDeck_propagatesNoCardsToMoveException`    |
| Test Case 3 | after init, deck≥1, multiple players | calls `turnManager.endTurnAndDraw()` once; then calls `checkWinCondition()`                               | no            | `nextTurn_validInvocation_advancesAndChecksWinCondition` |

---

## Method 4: `public void eliminatePlayer(Player p)`

### Step 1–3 Results

|        | Input                                             | Output / State Change                                                                                           |
| ------ | ------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Step 1 | `Player p`                                        | none (calls `playerManager.removePlayerFromGame(p)`, then `turnManager.syncWith(...)`)                          |
| Step 2 | `p`: `null`; not managed; already removed; active | throws `NullPointerException`, `IllegalArgumentException`, or `IllegalStateException`; otherwise removes & sync |
| Step 3 | combine boundary and normal cases                 | after sync, calls `checkWinCondition()`                                                                         |

### Step 4

| Test Case   | System under test                       | Expected behavior                                                                                                           | Implemented? | Test name                                                      |
| ----------- | --------------------------------------- | --------------------------------------------------------------------------------------------------------------------------- |--------------| -------------------------------------------------------------- |
| Test Case 1 | `eliminatePlayer(null)`                 | throws `NullPointerException`                                                                                               | no            | `eliminatePlayer_null_throwsNullPointerException`              |
| Test Case 2 | `eliminatePlayer(playerNotManaged)`     | throws `IllegalArgumentException("Player not found")`                                                                       | no            | `eliminatePlayer_unknownPlayer_throwsIllegalArgumentException` |
| Test Case 3 | `eliminatePlayer(activePlayer)`         | calls in order:<br>1. `playerManager.removePlayerFromGame(p)`<br>2. `turnManager.syncWith(...)`<br>3. `checkWinCondition()` | no            | `eliminatePlayer_active_removesSyncsAndChecksWinCondition`     |
| Test Case 4 | `eliminatePlayer(alreadyRemovedPlayer)` | throws `IllegalStateException("Player already removed")`; still calls `checkWinCondition()` (which sees >1 remain)          | no            | `eliminatePlayer_alreadyRemoved_throwsAndChecksWinCondition`   |
