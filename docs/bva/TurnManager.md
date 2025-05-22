# BVA Analysis for **TurnManager**

#### Important Note

The `TurnManager` maintains the play sequence and handles end-of-turn operations. It offers several methods to:

1. Establish the player order (`setPlayerManager`).
2. Query the active participant (`getCurrentActivePlayer`).
3. Advance play under different rules (`endTurnAndDraw`, `endTurnWithoutDraw`, `endTurnWithoutDrawForAttacks`).
4. Manipulate turn order (`addTurnForCurrentPlayer`, `makeCurrentPlayerLose`).
5. Inspect the queue (`getTurnOrder`).

Each test validates input handling, correct queue adjustments, and appropriate exception handling.

---

## Method 1: `public void setPlayerManager(PlayerManager pm)`

### Step 1-3 Results

|        | Input                                                                            | Output / State Change                               |
| ------ | -------------------------------------------------------------------------------- | --------------------------------------------------- |
| Step 1 | `PlayerManager pm`                                                               | none (initializes internal list and current player) |
| Step 2 | Cases for `pm`: `null`, non-null with zero players, one player, multiple players | Throws or populates turn list, sets active player   |
| Step 3 | Cover all input variations                                                       |                                                     |

### Step 4:

|             | System under test             | Expected output / state transition                                                          | Implemented?         | Test name                                                   |
| ----------- | ----------------------------- |---------------------------------------------------------------------------------------------| -------------------- | ----------------------------------------------------------- |
| Test Case 1 | `setPlayerManager(null)`      | throws `NullPointerException`                                                               | :white\_check\_mark: | `setPlayerManager_null_throwsNullPointerException`          |
| Test Case 2 | `pm.getPlayers().isEmpty()`   | throws `IllegalArgumentException("No players provided")`                                    | :white\_check\_mark: | `setPlayerManager_emptyList_throwsIllegalArgumentException` |
| Test Case 3 | `pm.getPlayers().size() == 1` | internal queue contains exactly that player; `getCurrentActivePlayer()` returns that player | :white\_check\_mark: | `setPlayerManager_singlePlayer_initializesCorrectly`        |
| Test Case 4 | `pm.getPlayers().size() == 4` | internal queue order matches input list; `getCurrentActivePlayer()` returns first element   | :white\_check\_mark: | `setPlayerManager_multiplePlayers_preservesInputOrder`      |

---

## Method 2: `public Player getCurrentActivePlayer()`

### Step 1-3 Results

|        | Input                                   | Output                        |
| ------ | --------------------------------------- | ----------------------------- |
| Step 1 | —                                       | returns current active player |
| Step 2 | Before initialization; after valid init | throws or returns accordingly |
| Step 3 | Call in each precondition               |                               |

### Step 4:

|             | System under test                            | Expected output / state transition                       | Implemented?         | Test name                                                        |
| ----------- | -------------------------------------------- | -------------------------------------------------------- | -------------------- |------------------------------------------------------------------|
| Test Case 1 | before `setPlayerManager(...)`               | throws `IllegalStateException("TurnManager not set up")` | :white\_check\_mark: | `getCurrentActivePlayer_beforeSetup_throwsIllegalStateException` |
| Test Case 2 | after setup with one player                  | returns that single player                               | :white\_check\_mark: | `getCurrentActivePlayer_singlePlayer_returnsThatPlayer`          |
| Test Case 3 | after setup with three players, no turns yet | returns the first player from provided list              | :white\_check\_mark: | `getCurrentActivePlayer_multiplePlayers_initialFirstPlayer`      |

---

## Method 3: `public void endTurnAndDraw() throws NoCardsToMoveException`

### Step 1-3 Results

|        | Preconditions                                                      | Output / State Change                                                             |
| ------ | ------------------------------------------------------------------ | --------------------------------------------------------------------------------- |
| Step 1 | —                                                                  | current player draws one card; may trigger special logic; advances to next player |
| Step 2 | `pm` not set; zero players; deck empty; deck non-empty & ≥1 player | Throws or performs draw and rotate                                                |
| Step 3 | Combine each boundary scenario                                     |                                                                                   |

### Step 4:

|             | System under test                          | Expected output / state transition                                                                                                 | Implemented?         | Test name                                                |
| ----------- | ------------------------------------------ |------------------------------------------------------------------------------------------------------------------------------------| -------------------- | -------------------------------------------------------- |
| Test Case 1 | before `setPlayerManager(...)`             | throws `IllegalStateException("TurnManager not initialized")`                                                                      | :white\_check\_mark: | `endTurnAndDraw_beforeSetup_throwsIllegalStateException` |
| Test Case 2 | after setup but `getTurnOrder().isEmpty()` | throws `IllegalStateException("No players to manage")`                                                                             | :white\_check\_mark: | `endTurnAndDraw_noPlayers_throwsIllegalStateException`   |
| Test Case 3 | deck is empty                              | underlying draw method throws `NoSuchElementException`; `endTurnAndDraw()` propagates as `NoCardsToMoveException`; queue unchanged | :white\_check\_mark: | `endTurnAndDraw_emptyDeck_throwsNoCardsToMoveException`  |
| Test Case 4 | deck ≥1 card, single player                | player draws one card; queue becomes empty or game-over state; `getCurrentActivePlayer()` reflects removal                         | :white\_check\_mark: | `endTurnAndDraw_singlePlayer_drawsAndEndsGame`           |
| Test Case 5 | deck ≥1 card, two players                  | first player draws and is re-added; `getCurrentActivePlayer()` advances to second player                                           | :white\_check\_mark: | `endTurnAndDraw_twoPlayers_rotatesToNextPlayer`          |

---

## Method 4: `public void endTurnWithoutDraw()`

### Step 1-3 Results

|        | Preconditions                      | Output / State Change                                                      |
| ------ | ---------------------------------- | -------------------------------------------------------------------------- |
| Step 1 | —                                  | removes current player from front; re-adds if more remain; updates current |
| Step 2 | no players; one player; ≥2 players | Throws or rotates accordingly                                              |
| Step 3 | Test each scenario                 |                                                                            |

### Step 4:

|             | System under test          | Expected output / state transition                                                | Implemented?         | Test name                                                  |
| ----------- | -------------------------- |-----------------------------------------------------------------------------------| -------------------- | ---------------------------------------------------------- |
| Test Case 1 | `getTurnOrder().isEmpty()` | throws `IllegalStateException("No players to manage")`                            | :white\_check\_mark: | `endTurnWithoutDraw_noPlayers_throwsIllegalStateException` |
| Test Case 2 | one player in queue        | removes sole player; queue empty; `getCurrentActivePlayer()` reflects end-of-game | :white\_check\_mark: | `endTurnWithoutDraw_singlePlayer_endsGame`                 |
| Test Case 3 | three players in queue     | removes first player; re-adds to end; `getCurrentActivePlayer()` moves to second  | :white\_check\_mark: | `endTurnWithoutDraw_multiplePlayers_rotatesCorrectly`      |

---

## Method 5: `public void endTurnWithoutDrawForAttacks()`

### Step 1-3 Results

|        | Preconditions                      | Output / State Change                                              |
| ------ | ---------------------------------- | ------------------------------------------------------------------ |
| Step 1 | —                                  | removes current; may avoid re-add if attack rules; updates current |
| Step 2 | no players; one player; ≥2 players | Throws or processes attack-specific rotation                       |
| Step 3 | Cover each situation               |                                                                    |

### Step 4:

|             | System under test                                     | Expected output / state transition                                                      | Implemented?         | Test name                                                            |
| ----------- | ----------------------------------------------------- |-----------------------------------------------------------------------------------------| -------------------- | -------------------------------------------------------------------- |
| Test Case 1 | empty queue                                           | throws `IllegalStateException("No players to manage")`                                  | :white\_check\_mark: | `endTurnWithoutDrawForAttacks_noPlayers_throwsIllegalStateException` |
| Test Case 2 | one player                                            | removes sole player; queue empty; `getCurrentActivePlayer()` reflects end-of-game       | :white\_check\_mark: | `endTurnWithoutDrawForAttacks_singlePlayer_endsGame`                 |
| Test Case 3 | three players, current repeats at front after removal | removes duplicates of current; re-adds once; `getCurrentActivePlayer()` moves correctly | :white\_check\_mark: | `endTurnWithoutDrawForAttacks_multiplePlayers_appliesAttackSkip`     |

---

## Method 6: `public void addTurnForCurrentPlayer()`

### Step 1-3 Results

|        | Preconditions                   | Output / State Change                   |
| ------ | ------------------------------- | --------------------------------------- |
| Step 1 | —                               | inserts current player as next in queue |
| Step 2 | no players; at least one player | Throws or duplicates turn accordingly   |
| Step 3 | Validate both cases             |                                         |

### Step 4:

|             | System under test | Expected output / state transition                           | Implemented?         | Test name                                                       |
| ----------- | ----------------- | ------------------------------------------------------------ | -------------------- | --------------------------------------------------------------- |
| Test Case 1 | empty queue       | throws `IllegalStateException("No players to manage")`       | :white\_check\_mark: | `addTurnForCurrentPlayer_noPlayers_throwsIllegalStateException` |
| Test Case 2 | one player        | queue before=\[P], after=\[P,P]; current remains P           | :white\_check\_mark: | `addTurnForCurrentPlayer_singlePlayer_duplicatesNextTurn`       |
| Test Case 3 | two players       | queue before=\[P1,P2], after=\[P1,P1,P2]; current remains P1 | :white\_check\_mark: | `addTurnForCurrentPlayer_multiplePlayers_insertsProperly`       |

---

## Method 7: `public void removeCurrentPlayerFromGame()`

### Step 1-3 Results

|        | Preconditions                      | Output / State Change                                                                           |
| ------ | ---------------------------------- | ----------------------------------------------------------------------------------------------- |
| Step 1 | —                                  | removes every occurrence of current player; informs managers of removal; updates current player |
| Step 2 | no players; one player; ≥2 players | Throws or removes and advances accordingly                                                      |
| Step 3 | Cover each scenario                |                                                                                                 |

### Step 4:

|             | System under test | Expected output / state transition                                                                                        | Implemented?         | Test name                                                           |
| ----------- | ----------------- |---------------------------------------------------------------------------------------------------------------------------| -------------------- |---------------------------------------------------------------------|
| Test Case 1 | empty queue       | throws `IllegalStateException("No players to manage")`                                                                    | :white\_check\_mark: | `removeCurrentPlayerFromGame_noPlayers_throwsIllegalStateException` |
| Test Case 2 | one player        | removes sole player; calls `playerManager.removePlayerFromGame(...)`; queue empty; `getCurrentActivePlayer()` signals end | :white\_check\_mark: | `removeCurrentPlayerFromGame_singlePlayer_clearsGame`               |
| Test Case 3 | three players     | removes all instances of current; calls removal on manager once; `getCurrentActivePlayer()` moves to new head             | :white\_check\_mark: | `removeCurrentPlayerFromGame_multiplePlayers_updatesQueue`          |

---
