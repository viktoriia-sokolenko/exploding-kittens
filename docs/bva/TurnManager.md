Here's a lightly refactored version of your **TurnManager BVA** to match the updated flow where the `Deck` is now passed into the `TurnManager` via its constructor. This allows `TurnManager` to handle draw-based operations like `endTurnAndDraw()` without requiring indirect access through `PlayerManager`.

---

# BVA Analysis for **TurnManager**

#### Important Note

The `TurnManager` tracks and advances turn order using a queue of players and now receives a `Deck` via constructor. It no longer decides eliminations—those occur in `PlayerManager`. It provides turn advancement logic and synchronizes with remaining active players when needed.

Key responsibilities:

0. **Constructor** – Accepts a non-null `Deck`.
1. **`setPlayerManager(PlayerManager pm)`** – Initializes turn queue from `pm.getPlayers()`.
2. **`getCurrentActivePlayer()`** – Returns the player whose turn it is.
3. **`endTurnAndDraw()`**, **`endTurnWithoutDraw()`**, **`endTurnWithoutDrawForAttacks()`**, **`addTurnForCurrentPlayer()`** – Advance or manipulate the queue.
4. **`syncWith(List<Player> activePlayers)`** – Rebuilds internal queue to match active player list.
5. **`getTurnOrder()`** – Exposes current queue snapshot.

---

## Method 0: **Constructor**


### Step 1–3 Results

|        | Input  | Output / State Change                       |
| ------ | ------ | ------------------------------------------- |
| Step 1 | `deck` | stores `deck`; internal queue uninitialized |
| Step 2 | `null` | throws `NullPointerException`               |
| Step 3 | valid  | deck stored; queue is empty                 |

### Step 4

| Test Case     | System under test       | Expected behavior                             | Implemented? | Test name                                  |
| ------------- | ----------------------- | --------------------------------------------- |--------------| ------------------------------------------ |
| Test Case 0.1 | `new TurnManager(null)` | throws `NullPointerException("Deck is null")` | no           | `ctor_nullDeck_throwsNullPointerException` |
| Test Case 0.2 | `new TurnManager(deck)` | stores reference; queue not yet initialized   | no           | `ctor_validDeck_initializesState`          |

---

## Method 1: `public void setPlayerManager(PlayerManager pm)`

### Step 1–3 Results

|        | Input                                                 | Output / State Change                                   |
| ------ | ----------------------------------------------------- | ------------------------------------------------------- |
| Step 1 | `PlayerManager pm`                                    | none (loads `pm.getPlayers()` into queue, sets current) |
| Step 2 | `pm`: `null`, `pm.getPlayers()` empty, size=1, size>1 | Throws or initializes queue accordingly                 |
| Step 3 | Cover all variations                                  |                                                         |

### Step 4

| Test Case   | System under test           | Expected behavior                                        | Implemented? | Test name                                                   |
|-------------| --------------------------- | -------------------------------------------------------- |--------------| ----------------------------------------------------------- |
| Test Case 1 | `setPlayerManager(null)`    | throws `NullPointerException`                            | no           | `setPlayerManager_null_throwsNullPointerException`          |
| Test Case 2 | `pm.getPlayers().isEmpty()` | throws `IllegalArgumentException("No players provided")` | no           | `setPlayerManager_emptyList_throwsIllegalArgumentException` |
| Test Case 3 | size==1                     | queue=\[p1]; `getCurrentActivePlayer()` == p1            | no           | `setPlayerManager_singlePlayer_initializesCorrectly`        |
| Test Case 4 | size==4                     | queue preserves input order; current == first element    | no           | `setPlayerManager_multiplePlayers_preservesOrder`           |

---

## Method 2: `public Player getCurrentActivePlayer()`

### Step 1–3 Results

|        | Input                  | Output                          |
| ------ | ---------------------- | ------------------------------- |
| Step 1 | —                      | current active player           |
| Step 2 | before vs. after setup | throws or returns appropriately |
| Step 3 | call in each state     |                                 |

### Step 4

| Test Case   | System under test   | Expected behavior                                             | Implemented? | Test name                                               |
|-------------| ------------------- | ------------------------------------------------------------- |--------------| ------------------------------------------------------- |
| Test Case 1 | before setup        | throws `IllegalStateException("TurnManager not initialised")` | no           | `getCurrentActivePlayer_beforeSetup_throwsException`    |
| Test Case 2 | single-player setup | returns that player                                           | no           | `getCurrentActivePlayer_singlePlayer_returnsThatPlayer` |
| Test Case 3 | multi-player setup  | returns first element of provided list                        | no           | `getCurrentActivePlayer_multiPlayers_initialFirst`      |

---

## Method 3: `public void endTurnAndDraw() throws NoCardsToMoveException`

### Step 1–3 Results

|        | Preconditions                                             | Output / State Change                                                        |
| ------ | --------------------------------------------------------- | ---------------------------------------------------------------------------- |
| Step 1 | —                                                         | remove current, draw via player.drawCard(), re-add if needed, update current |
| Step 2 | before setup; empty queue; deck empty; deck≥1 & ≥1 player | throws or performs draw+rotate                                               |
| Step 3 | cover combinations                                        |                                                                              |

### Step 4

| Test Case   | System under test       | Expected behavior                                                                                        | Implemented? | Test name                                               |
|-------------| ----------------------- | -------------------------------------------------------------------------------------------------------- |--------------| ------------------------------------------------------- |
| Test Case 1 | before setup            | throws `IllegalStateException("TurnManager not initialised")`                                            | no           | `endTurnAndDraw_beforeSetup_throwsException`            |
| Test Case 2 | empty queue after setup | throws `IllegalStateException("No players to manage")`                                                   | no           | `endTurnAndDraw_noPlayers_throwsException`              |
| Test Case 3 | deck empty              | underlying draw throws `NoSuchElementException`; propagates as `NoCardsToMoveException`; queue unchanged | no           | `endTurnAndDraw_emptyDeck_throwsNoCardsToMoveException` |
| Test Case 4 | deck≥1 & single player  | player draws; queue empty; current reflects end-of-game                                                  | no           | `endTurnAndDraw_singlePlayer_drawsAndEndsGame`          |
| Test Case 5 | deck≥1 & two players    | first draws+re-add; current advances to second player                                                    | no           | `endTurnAndDraw_twoPlayers_rotatesCorrectly`            |

---

## Method 4: `public void endTurnWithoutDraw()`

### Step 1–3 Results

|        | Preconditions               | Output / State Change                               |
| ------ | --------------------------- | --------------------------------------------------- |
| Step 1 | —                           | remove current; re-add if >1 remain; update current |
| Step 2 | no players; one; >1 players | throws or rotates accordingly                       |
| Step 3 | test each scenario          |                                                     |

### Step 4

| Test Case   | System under test | Expected behavior                                      | Implemented? | Test name                                             |
|-------------| ----------------- | ------------------------------------------------------ |--------------| ----------------------------------------------------- |
| Test Case 1 | empty queue       | throws `IllegalStateException("No players to manage")` | no           | `endTurnWithoutDraw_noPlayers_throwsException`        |
| Test Case 2 | one player        | removes sole; queue empty; current reflects end        | no           | `endTurnWithoutDraw_singlePlayer_endsGame`            |
| Test Case 3 | three players     | rotates: first removed+re-added; current -> second     | no           | `endTurnWithoutDraw_multiplePlayers_rotatesCorrectly` |

---

## Method 5: `public void endTurnWithoutDrawForAttacks()`

### Step 1–3 Results

|        | Preconditions       | Output / State Change                                  |
| ------ | ------------------- | ------------------------------------------------------ |
| Step 1 | —                   | remove current; skip re-add duplicates; update current |
| Step 2 | no players; one; >1 | throws or performs attack-specific rotation            |
| Step 3 | test each scenario  |                                                        |

### Step 4

| Test Case   | System under test                    | Expected behavior                                      | Implemented? | Test name                                                        |
|-------------| ------------------------------------ | ------------------------------------------------------ |--------------| ---------------------------------------------------------------- |
| Test Case 1 | empty queue                          | throws `IllegalStateException("No players to manage")` | no           | `endTurnWithoutDrawForAttacks_noPlayers_throwsException`         |
| Test Case 2 | one player                           | removes sole; queue empty; current reflects end        | no           | `endTurnWithoutDrawForAttacks_singlePlayer_endsGame`             |
| Test Case 3 | three players, current repeats front | removes duplicates; re-adds once; current -> next      | no           | `endTurnWithoutDrawForAttacks_multiplePlayers_appliesAttackSkip` |

---

## Method 6: `public void addTurnForCurrentPlayer()`

### Step 1–3 Results

|        | Preconditions         | Output / State Change            |
| ------ | --------------------- | -------------------------------- |
| Step 1 | —                     | inserts current at index 1       |
| Step 2 | no players; ≥1 player | throws or duplicates accordingly |
| Step 3 | test both cases       |                                  |

### Step 4

| Test Case   | System under test | Expected behavior                                      | Implemented? | Test name                                                 |
|-------------| ----------------- | ------------------------------------------------------ |--------------| --------------------------------------------------------- |
| Test Case 1 | empty queue       | throws `IllegalStateException("No players to manage")` | no           | `addTurnForCurrentPlayer_noPlayers_throwsException`       |
| Test Case 2 | one player        | queue \[P,P]; current remains P                        | no           | `addTurnForCurrentPlayer_singlePlayer_duplicatesNextTurn` |
| Test Case 3 | two players       | queue \[P1,P1,P2]; current remains P1                  | no           | `addTurnForCurrentPlayer_multiplePlayers_insertsProperly` |

---

## Method 7: `public void syncWith(List<Player> activePlayers)`

### Step 1–3 Results

|        | Input                                                                                       | Output / State Change                                      |
| ------ | ------------------------------------------------------------------------------------------- | ---------------------------------------------------------- |
| Step 1 | `List<Player> activePlayers`                                                                | none (rebuilds queue from `activePlayers`, resets current) |
| Step 2 | Cases for `activePlayers`: `null`, empty, contains current, missing current, reordered list | Throws or rebuilds correctly                               |
| Step 3 | Combine boundary lists                                                                      |                                                            |

### Step 4

| Test Case   | System under test                 | Expected behavior                                               | Implemented? | Test name                                           |
|-------------| --------------------------------- | --------------------------------------------------------------- |--------------| --------------------------------------------------- |
| Test Case 1 | `syncWith(null)`                  | throws `NullPointerException`                                   | no           | `syncWith_null_throwsNullPointerException`          |
| Test Case 2 | `syncWith(emptyList)`             | throws `IllegalArgumentException("No players provided")`        | no           | `syncWith_emptyList_throwsIllegalArgumentException` |
| Test Case 3 | `syncWith(listWithCurrentFirst)`  | queue exactly matches input; current == first element           | no           | `syncWith_includesCurrent_keepsOrderAndCurrent`     |
| Test Case 4 | `syncWith(listMissingOldCurrent)` | queue rebuilt without old current; current == new first element | no           | `syncWith_excludesOldCurrent_setsNewCurrent`        |
| Test Case 5 | `syncWith(reorderedList)`         | queue matches new order; current updated to first               | no           | `syncWith_reordersQueue_updatesCurrent`             |

---

## Method 8: `public List<Player> getTurnOrder()`

### Step 1–3 Results

|        | Preconditions          | Output                          |
| ------ | ---------------------- | ------------------------------- |
| Step 1 | —                      | returns internal queue snapshot |
| Step 2 | before/after mutations | throws or returns accordingly   |
| Step 3 | call in each state     |                                 |

### Step 4

| Test Case   | System under test   | Expected behavior                                             | Implemented? | Test name                                     |
|-------------| ------------------- | ------------------------------------------------------------- |--------------| --------------------------------------------- |
| Test Case 1 | before setup        | throws `IllegalStateException("TurnManager not initialised")` | no           | `getTurnOrder_beforeSetup_throwsException`    |
| Test Case 2 | after setup         | returns list equal to initial `pm.getPlayers()`               | no           | `getTurnOrder_afterSetup_returnsInitialOrder` |
| Test Case 3 | after sync/reorders | returns queue reflecting latest `syncWith` or turn operations | no           | `getTurnOrder_afterMutations_reflectsQueue`   |

