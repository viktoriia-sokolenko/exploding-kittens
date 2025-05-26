# BVA Analysis for **GameEngine**

#### Important Note

All core methods (`startGame`, `playCard`, `nextTurn`, `eliminatePlayer`) end by invoking `checkWinCondition()`. The engine is “not started” until **startGame(...)** is called.


## Method 1: ```public void startGame(int numberOfPlayers)```

### Step 1–3 Results

|        | Input             | Output / State Change                                                                                                                          |
| ------ | ----------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | `numberOfPlayers` | none (delegates to playerManager.addPlayers(), makePlayersDrawTheirInitialHands(), turnManager.setPlayerManager())                             |
| Step 2 | Intervals `(2,5)` | Exception, game proceeds                                                                                                                       |
| Step 3 | `<2`, `2–5`, `>5` | **1** → InvalidNumberofPlayersException<br> **2** → adds 2 players, deals hands, sets turn manager, then `checkWinCondition()`<br> **5** → same as 2<br> **6** → InvalidNumberofPlayersException |

### Step 4

| Test Case | System under test | Expected behavior / state transition                                                  | Implemented? | Test name                                                        |
| --------- | ----------------- | ------------------------------------------------------------------------------------- |--------------| ---------------------------------------------------------------- |
| 1         | `startGame(1)`    | throws `InvalidNumberofPlayersException`                                              |              | `startGame_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| 2         | `startGame(6)`    | throws `InvalidNumberofPlayersException`                                              |              | `startGame_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| 3         | `startGame(2)`    | calls in order: add 2 players → deal hands → set turn manager → `checkWinCondition()` |              | `startGame_twoPlayers_initializesManagersAndChecksWinCondition`  |
| 4         | `startGame(5)`    | calls in order: add 5 players → deal hands → set turn manager → `checkWinCondition()` |              | `startGame_fivePlayers_initializesManagersAndChecksWinCondition` |

---

## Method 2: `public void playCard(Player player, Card card)`

### Step 1–3 Results

|        | Input 1                                                                  | Input 2                            | Output                                                                |
| ------ | ------------------------------------------------------------------------ | ---------------------------------- | --------------------------------------------------------------------- |
| Step 1 | any `player`                                                             | any `card`                         |                                                                       |  
| Step 2 | Player Object, Case (Player in Game vs Not in Game)                      | Card Object                        |                                                                       |
| Step 3 | null, validPlayerInGame, playerNotInGame                                 | null, all the different Card types |                                                                       |

### Step 4

| Test Case | System under test                      | Expected behavior                                                                        | Implemented? | Test name                                               |
| --------- | -------------------------------------- | ---------------------------------------------------------------------------------------- |--------------| ------------------------------------------------------- |
| 1         | `playCard(null, validCard)`            | throws `NullPointerException`                                                            |              | `playCard_nullPlayer_throwsNullPointerException`        |
| 2         | `playCard(validPlayer, null)`          | throws `NullPointerException`                                                            |              | `playCard_nullCard_throwsNullPointerException`          |
| 3         | `playCard(playerNotInGame, validCard)` | throws `IllegalArgumentException("Player not in game")`                                  |              | `playCard_unknownPlayer_throwsIllegalArgumentException` |
| 4         | valid attack scenario                  | invokes `AttackCard.play(this,p)`; then `checkWinCondition()`                            |              | `playCard_attack_delegatesEffectAndChecksWinCondition`  |
| 5         | unhandled card type                    | throws `IllegalStateException("Unhandled card type")`; still calls `checkWinCondition()` |              | `playCard_unhandledType_throwsAndChecksWinCondition`    |

---

## Method 3: ```public void nextTurn()```

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
| 2         | after init, deck empty                | propagates `NoCardsToMoveException`; no `checkWinCondition()` call    | no           | `nextTurn_emptyDeck_throwsNoCardsToMoveException`        |
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

