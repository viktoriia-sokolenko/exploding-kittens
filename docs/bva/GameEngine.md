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

|        | Input                                                                                                                                                                                                | Output / State Change                                                                                                                                                                   |
| ------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | any `player`, any `card`                                                                                                                                                                             | none                                                                                                                                                                                    |
| Step 2 | • `player = null`<br>• `playerNotInGame`<br>• `card = null`<br>• `validPlayerInGame & validCard`                                                                                                     | **null inputs** → throws `NullPointerException`<br>**unknown player** → throws `IllegalArgumentException("Player not in game")`<br>**otherwise** → delegate to card logic               |
| Step 3 | 1. `(null, validCard)`<br>2. `(validPlayerInGame, null)`<br>3. `(playerNotInGame, validCard)`<br>4. `(validPlayerInGame, attackCardInstance)`<br>5. `(validPlayerInGame, unhandledCardTypeInstance)` | 1 → NPE<br>2 → NPE<br>3 → IAE("Player not in game")<br>4 → call `AttackCard.play(this, player)` then `checkWinCondition()`<br>5 → ISE("Unhandled card type") then `checkWinCondition()` |

### Step 4

| Test Case | System under test                      | Expected behavior                                                                        | Implemented? | Test name                                               |
| --------- | -------------------------------------- | ---------------------------------------------------------------------------------------- | ------------ | ------------------------------------------------------- |
| 1         | `playCard(null, validCard)`            | throws `NullPointerException`                                                            |              | `playCard_nullPlayer_throwsNullPointerException`        |
| 2         | `playCard(validPlayer, null)`          | throws `NullPointerException`                                                            |              | `playCard_nullCard_throwsNullPointerException`          |
| 3         | `playCard(playerNotInGame, validCard)` | throws `IllegalArgumentException("Player not in game")`                                  |              | `playCard_unknownPlayer_throwsIllegalArgumentException` |
| 4         | valid attack scenario                  | invokes `AttackCard.play(this,p)`; then `checkWinCondition()`                            |              | `playCard_attack_delegatesEffectAndChecksWinCondition`  |
| 5         | unhandled card type                    | throws `IllegalStateException("Unhandled card type")`; still calls `checkWinCondition()` |              | `playCard_unhandledType_throwsAndChecksWinCondition`    |

---

## Method 3: `public void nextTurn()`

### Step 1–3 Results

|        | Input                                                                                                                                                           | Output / State Change                                                                                                                                                                                        |
| ------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Step 1 | none                                                                                                                                                            | none                                                                                                                                                                                                         |
| Step 2 | • before init<br>• deck empty after init<br>• deck has ≥1 card after init                                                                                       | none                                                                                                                                                                                                         |
| Step 3 | 1. call `nextTurn()` before `startGame(...)`<br>2. after `startGame(2)`, deck emptied, then `nextTurn()`<br>3. after `startGame(3)`, deck ≥1, then `nextTurn()` | 1 → throws `IllegalStateException("GameEngine not initialized")`<br>2 → propagates `NoCardsToMoveException`; no `checkWinCondition()`<br>3 → calls `turnManager.endTurnAndDraw()` then `checkWinCondition()` |

### Step 4

| Test Case | System under test                     | Expected behavior                                                     | Implemented? | Test name                                                |
| --------- | ------------------------------------- | --------------------------------------------------------------------- | ------ | -------------------------------------------------------- |
| 1         | before `startGame(...)`               | throws `IllegalStateException("GameEngine not initialized")`          |        | `nextTurn_beforeInit_throwsIllegalStateException`        |
| 2         | after init, deck empty                | propagates `NoCardsToMoveException`; no `checkWinCondition()` call    |        | `nextTurn_emptyDeck_throwsNoCardsToMoveException`        |
| 3         | after init, deck ≥1, multiple players | calls `turnManager.endTurnAndDraw()` once; then `checkWinCondition()` |        | `nextTurn_validInvocation_advancesAndChecksWinCondition` |

---

## Method 4: `public void eliminatePlayer(Player p)`

### Step 1–3 Results

|        | Input                                                                                               | Output / State Change                                                                                                                                          |
| ------ | --------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | `Player p`                                                                                          | none                                                                                                                                                           |
| Step 2 | • `p = null`<br>• `p = playerNotInGame`<br>• `p = activePlayer`<br>• `p = alreadyRemovedPlayer`     | none                                                                                                                                                           |
| Step 3 | 1. `p = null`<br>2. `p = playerNotInGame`<br>3. `p = activePlayer`<br>4. `p = alreadyRemovedPlayer` | 1 → NPE<br>2 → IAE("Player not found")<br>3 → remove → `syncWith(...)` → `checkWinCondition()`<br>4 → ISE("Player already removed") then `checkWinCondition()` |

### Step 4

| Test Case | System under test                       | Expected behavior                                                                                            | Implemented? | Test name                                                      |
| --------- | --------------------------------------- | ------------------------------------------------------------------------------------------------------------ | ------------ | -------------------------------------------------------------- |
| 1         | `eliminatePlayer(null)`                 | throws `NullPointerException`                                                                                | no           | `eliminatePlayer_null_throwsNullPointerException`              |
| 2         | `eliminatePlayer(playerNotInGame)`      | throws `IllegalArgumentException("Player not found")`                                                        | no           | `eliminatePlayer_unknownPlayer_throwsIllegalArgumentException` |
| 3         | `eliminatePlayer(activePlayer)`         | calls: remove → sync → `checkWinCondition()`                                                                 | no           | `eliminatePlayer_active_removesSyncsAndChecksWinCondition`     |
| 4         | `eliminatePlayer(alreadyRemovedPlayer)` | throws `IllegalStateException("Player already removed")`; still calls `checkWinCondition()` (sees >1 remain) | no           | `eliminatePlayer_alreadyRemoved_throwsAndChecksWinCondition`   |







