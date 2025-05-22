# BVA Analysis for **GameEngine**

#### Important Note

The `GameEngine` orchestrates core gameplay by coordinating `PlayerManager`, `TurnManager`, and the shared `Deck`.  In addition to initializing, playing cards, advancing turns, and eliminating players, it **checks for the win condition** (only one player remains) immediately after each turn advancement or elimination.

Key methods:

1. `startGame(int n)`
2. `playCard(Player p, Card c)`
3. `nextTurn()`
4. `eliminatePlayer(Player p)`

All four invoke `checkWinCondition()` at the end of their flow to detect and handle game completion.

---

## Method 1: `public void startGame(int n) throws InvalidNumberofPlayersException`

### Step 1–3 Results

|        | Input                               | Output / State Change                                                                                                                                                     |
| ------ | ----------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | `n`                                 | none (initializes `PlayerManager`, deals hands, sets up `TurnManager`)                                                                                                    |
| Step 2 | `n`: `<2`, `2–5`, `>5`              | `<2` or `>5`: throws `InvalidNumberofPlayersException`; otherwise:                                                                                                        |
|        |                                     | 1. `playerManager.addPlayers(n)`<br>2. `playerManager.makePlayersDrawTheirInitialHands()`<br>3. `turnManager.setPlayerManager(playerManager)`<br>4. `checkWinCondition()` |
| Step 3 | boundary values: `1`, `2`, `5`, `6` |                                                                                                                                                                           |

### Step 4

| Test Case   | System under test | Expected behavior / state transition                                                                                                                                               | Implemented?         | Test name                                                        |
|-------------| ----------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------- | ---------------------------------------------------------------- |
| Test Case 1 | `startGame(1)`    | throws `InvalidNumberofPlayersException`                                                                                                                                           | :white\_check\_mark: | `startGame_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| Test Case 2 | `startGame(6)`    | throws `InvalidNumberofPlayersException`                                                                                                                                           | :white\_check\_mark: | `startGame_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| Test Case 3 | `startGame(2)`    | calls in order:<br>1. `playerManager.addPlayers(2)`<br>2. `playerManager.makePlayersDrawTheirInitialHands()`<br>3. `turnManager.setPlayerManager(...)`<br>4. `checkWinCondition()` | :white\_check\_mark: | `startGame_twoPlayers_initializesManagersAndChecksWinCondition`  |
| Test Case 4 | `startGame(5)`    | same sequence with `n=5`                                                                                                                                                           | :white\_check\_mark: | `startGame_fivePlayers_initializesManagersAndChecksWinCondition` |

---

## Method 2: `public void playCard(Player p, Card c)`

### Step 1–3 Results

|        | Input 1: `Player p`                             | Input 2: `Card c` | Output / State Change                                                                 |
| ------ | ----------------------------------------------- | ----------------- | ------------------------------------------------------------------------------------- |
| Step 1 | `p`                                             | `c`               | none (dispatches `c.play(this, p)`)                                                   |
| Step 2 | `p` or `c` null; `p` not in game; `c` unhandled |                   | throws `NullPointerException`, `IllegalArgumentException`, or `IllegalStateException` |
| Step 3 | combine invalid and valid                       |                   | after dispatch, calls `checkWinCondition()`                                           |

### Step 4

#### TODO: Add test cases for other cards

| Test Case   | System under test                       | Expected behavior                                                                                                                        | Implemented?         | Test name                                               |
|-------------| --------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- | -------------------- | ------------------------------------------------------- |
| Test Case 1 | `playCard(null, validCard)`             | throws `NullPointerException`                                                                                                            | :white\_check\_mark: | `playCard_nullPlayer_throwsNullPointerException`        |
| Test Case 2 | `playCard(validPlayer, null)`           | throws `NullPointerException`                                                                                                            | :white\_check\_mark: | `playCard_nullCard_throwsNullPointerException`          |
| Test Case 3 | `playCard(playerNotManaged, validCard)` | throws `IllegalArgumentException("Player not in game")`                                                                                  | :white\_check\_mark: | `playCard_unknownPlayer_throwsIllegalArgumentException` |
| Test Case 4 | valid attack scenario                   | invokes `AttackCard.play(this,p)`; does **not** call sync here; then calls `checkWinCondition()`                                         | :white\_check\_mark: | `playCard_attack_delegatesEffectAndChecksWinCondition`  |
| Test Case 5 | unhandled card type                     | throws `IllegalStateException("Unhandled card type")`; no side-effects; still calls `checkWinCondition()` (which finds >1 player remain) | :white\_check\_mark: | `playCard_unhandledType_throwsAndChecksWinCondition`    |

---

## Method 3: `public void nextTurn() throws NoCardsToMoveException`

### Step 1–3 Results

|        | Input                           | Output / State Change                                                                                            |
| ------ | ------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| Step 1 | none                            | none (calls `turnManager.endTurnAndDraw()`)                                                                      |
| Step 2 | before init; deck empty; normal | before init: throws `IllegalStateException`; deck empty: propagates `NoCardsToMoveException`; otherwise advances |
| Step 3 | combine scenarios               | after successful advance, calls `checkWinCondition()`                                                            |

### Step 4

| Test Case   | System under test                    | Expected behavior                                                                                         | Implemented?         | Test name                                                |
|-------------| ------------------------------------ | --------------------------------------------------------------------------------------------------------- | -------------------- | -------------------------------------------------------- |
| Test Case 1 | before `startGame(...)`              | throws `IllegalStateException("GameEngine not initialized")`                                              | :white\_check\_mark: | `nextTurn_beforeInit_throwsIllegalStateException`        |
| Test Case 2 | after init, deck empty               | `turnManager.endTurnAndDraw()` throws `NoCardsToMoveException`; propagates; no `checkWinCondition()` call | :white\_check\_mark: | `nextTurn_emptyDeck_propagatesNoCardsToMoveException`    |
| Test Case 3 | after init, deck≥1, multiple players | calls `turnManager.endTurnAndDraw()` once; then calls `checkWinCondition()`                               | :white\_check\_mark: | `nextTurn_validInvocation_advancesAndChecksWinCondition` |

---

## Method 4: `public void eliminatePlayer(Player p)`

### Step 1–3 Results

|        | Input                                             | Output / State Change                                                                                           |
| ------ | ------------------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
| Step 1 | `Player p`                                        | none (delegates removal to `playerManager`, then `turnManager.syncWith(...)`)                                   |
| Step 2 | `p`: `null`; not managed; already removed; active | throws `NullPointerException`, `IllegalArgumentException`, or `IllegalStateException`; otherwise removes & sync |
| Step 3 | combine boundary and normal cases                 | after sync, calls `checkWinCondition()`                                                                         |

### Step 4

| Test Case   | System under test                       | Expected behavior                                                                                                           | Implemented?         | Test name                                                      |
|-------------| --------------------------------------- |-----------------------------------------------------------------------------------------------------------------------------| -------------------- | -------------------------------------------------------------- |
| Test Case 1 | `eliminatePlayer(null)`                 | throws `NullPointerException`                                                                                               | :white\_check\_mark: | `eliminatePlayer_null_throwsNullPointerException`              |
| Test Case 2 | `eliminatePlayer(playerNotManaged)`     | throws `IllegalArgumentException("Player not found")`                                                                       | :white\_check\_mark: | `eliminatePlayer_unknownPlayer_throwsIllegalArgumentException` |
| Test Case 3 | `eliminatePlayer(activePlayer)`         | calls in order:<br>1. `playerManager.removePlayerFromGame(p)`<br>2. `turnManager.syncWith(...)`<br>3. `checkWinCondition()` | :white\_check\_mark: | `eliminatePlayer_active_removesSyncsAndChecksWinCondition`     |
| Test Case 4 | `eliminatePlayer(alreadyRemovedPlayer)` | throws `IllegalStateException("Player already removed")`; still calls `checkWinCondition()` (which sees >1 remain)          | :white\_check\_mark: | `eliminatePlayer_alreadyRemoved_throwsAndChecksWinCondition`   |
