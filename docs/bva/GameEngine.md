Here’s the fully refactored BVA for **GameEngine**, modeled on the Deck example—each Step 3 now lists concrete values or scenarios instead of “combine…”:

---

# BVA Analysis for **GameEngine**

#### Important Note

All core methods (`startGame`, `playCard`, `nextTurn`, `eliminatePlayer`) end by invoking `checkWinCondition()`. The engine is “not started” until **startGame(...)** is called.

---

## Method 0: **Constructor**

### Step 1–3 Results

|        | Input | Output / State Change                                                      |
| ------ | ----- | -------------------------------------------------------------------------- |
| Step 1 | none  | none (creates `deck`, `playerManager(deck)`, `turnManager(playerManager)`) |
| Step 2 | —     | N/A—no validation                                                          |
| Step 3 | —     | N/A                                                                        |

### Step 4

| Test Case | System under test  | Expected behavior                                             | Implemented? | Test name                         |
| --------- | ------------------ | ------------------------------------------------------------- |--------------| --------------------------------- |
| 1         | `new GameEngine()` | `deck` non-null; both managers injected with that same `deck` | no           | `ctor_initializesDeckAndManagers` |

---

## Method 1: `public void startGame(int numberOfPlayers) throws InvalidNumberofPlayersException`

### Step 1–3 Results

|        | Input             | Output / State Change                                                                                                                          |
| ------ | ----------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | `numberOfPlayers` | none (delegates to playerManager.addPlayers(), makePlayersDrawTheirInitialHands(), turnManager.setPlayerManager())                             |
| Step 2 | `<2`, `2–5`, `>5` | `<2` or `>5`: throws exception; otherwise proceeds                                                                                             |
| Step 3 |                   | **1** → throws<br> **2** → adds 2 players, deals hands, sets turn manager, then `checkWinCondition()`<br> **5** → same as 2<br> **6** → throws |

### Step 4

| Test Case | System under test | Expected behavior / state transition                                                  | Implemented? | Test name                                                        |
| --------- | ----------------- | ------------------------------------------------------------------------------------- |--------------| ---------------------------------------------------------------- |
| 1         | `startGame(1)`    | throws `InvalidNumberofPlayersException`                                              | no           | `startGame_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| 2         | `startGame(6)`    | throws `InvalidNumberofPlayersException`                                              | no           | `startGame_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| 3         | `startGame(2)`    | calls in order: add 2 players → deal hands → set turn manager → `checkWinCondition()` | no           | `startGame_twoPlayers_initializesManagersAndChecksWinCondition`  |
| 4         | `startGame(5)`    | same sequence with `n=5`                                                              | no           | `startGame_fivePlayers_initializesManagersAndChecksWinCondition` |

---

## Method 2: `public void playCard(Player p, Card c)`

### Step 1–3 Results

|        | Input 1: `Player p`                                       | Input 2: `Card c` | Step 3: Concrete test inputs                                                                                                                                                                                                          |
| ------ | --------------------------------------------------------- | ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | any `p`                                                   | any `c`           | (call signature)                                                                                                                                                                                                                      |
| Step 2 | • `p` or `c` null<br>• `p` not in game<br>• `c` unhandled |                   | (equivalence classes)                                                                                                                                                                                                                 |
| Step 3 |                                                           |                   | 1. `p=null`, `c=validAttackCard`  <br> 2. `p=validPlayerInGame`, `c=null`  <br> 3. `p=playerNotInGame`, `c=validAttackCard`  <br> 4. `p=validPlayerInGame`, `c=validAttackCard`  <br> 5. `p=validPlayerInGame`, `c=unhandledCardType` |

### Step 4

| Test Case | System under test                      | Expected behavior                                                                        | Implemented? | Test name                                               |
| --------- | -------------------------------------- | ---------------------------------------------------------------------------------------- |--------------| ------------------------------------------------------- |
| 1         | `playCard(null, validCard)`            | throws `NullPointerException`                                                            | no           | `playCard_nullPlayer_throwsNullPointerException`        |
| 2         | `playCard(validPlayer, null)`          | throws `NullPointerException`                                                            | no           | `playCard_nullCard_throwsNullPointerException`          |
| 3         | `playCard(playerNotInGame, validCard)` | throws `IllegalArgumentException("Player not in game")`                                  | no           | `playCard_unknownPlayer_throwsIllegalArgumentException` |
| 4         | valid attack scenario                  | invokes `AttackCard.play(this,p)`; then `checkWinCondition()`                            | no           | `playCard_attack_delegatesEffectAndChecksWinCondition`  |
| 5         | unhandled card type                    | throws `IllegalStateException("Unhandled card type")`; still calls `checkWinCondition()` | no           | `playCard_unhandledType_throwsAndChecksWinCondition`    |

---

## Method 3: `public void nextTurn() throws NoCardsToMoveException`

### Step 1–3 Results

|        | Input                                                                     | Step 3: Concrete scenarios                                                                                                                                                                                                                        |
| ------ | ------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | none                                                                      | (call signature)                                                                                                                                                                                                                                  |
| Step 2 | • before init<br>• deck empty after init<br>• deck has ≥1 card after init | (equivalence classes)                                                                                                                                                                                                                             |
| Step 3 |                                                                           | 1. **Before init**: call `nextTurn()` immediately after constructor <br> 2. **Empty deck**: after `startGame(2)` and remove all cards, call `nextTurn()` <br> 3. **Non-empty deck**: after `startGame(3)`, call `nextTurn()` when ≥1 card remains |

### Step 4

| Test Case | System under test                     | Expected behavior                                                     | Implemented? | Test name                                                |
| --------- | ------------------------------------- | --------------------------------------------------------------------- |--------------| -------------------------------------------------------- |
| 1         | before `startGame(...)`               | throws `IllegalStateException("GameEngine not initialized")`          | no           | `nextTurn_beforeInit_throwsIllegalStateException`        |
| 2         | after init, deck empty                | propagates `NoCardsToMoveException`; no `checkWinCondition()` call    | no           | `nextTurn_emptyDeck_propagatesNoCardsToMoveException`    |
| 3         | after init, deck ≥1, multiple players | calls `turnManager.endTurnAndDraw()` once; then `checkWinCondition()` | no           | `nextTurn_validInvocation_advancesAndChecksWinCondition` |

---

## Method 4: `public void eliminatePlayer(Player p)`

### Step 1–3 Results

|        | Input                                                                    | Step 3: Concrete test inputs                                                                                 |
| ------ | ------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ |
| Step 1 | `Player p`                                                               | (call signature)                                                                                             |
| Step 2 | • `p` null<br>• `p` not managed<br>• `p` active<br>• `p` already removed | (equivalence classes)                                                                                        |
| Step 3 |                                                                          | 1. `p = null`  <br> 2. `p = playerNotInGame`  <br> 3. `p = activePlayer`  <br> 4. `p = alreadyRemovedPlayer` |

### Step 4

| Test Case | System under test                       | Expected behavior                                                                                            | Implemented? | Test name                                                      |
| --------- | --------------------------------------- | ------------------------------------------------------------------------------------------------------------ |--------------| -------------------------------------------------------------- |
| 1         | `eliminatePlayer(null)`                 | throws `NullPointerException`                                                                                | no           | `eliminatePlayer_null_throwsNullPointerException`              |
| 2         | `eliminatePlayer(playerNotInGame)`      | throws `IllegalArgumentException("Player not found")`                                                        | no           | `eliminatePlayer_unknownPlayer_throwsIllegalArgumentException` |
| 3         | `eliminatePlayer(activePlayer)`         | calls: remove → sync → `checkWinCondition()`                                                                 | no           | `eliminatePlayer_active_removesSyncsAndChecksWinCondition`     |
| 4         | `eliminatePlayer(alreadyRemovedPlayer)` | throws `IllegalStateException("Player already removed")`; still calls `checkWinCondition()` (sees >1 remain) | no           | `eliminatePlayer_alreadyRemoved_throwsAndChecksWinCondition`   |

