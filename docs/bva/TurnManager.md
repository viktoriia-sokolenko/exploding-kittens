# BVA Analysis for **TurnManager**

#### Important Note

* The `TurnManager` now **owns** a `Deck` (injected via constructor) and manages a turn‐order queue of players. It never
  eliminates players—that’s `PlayerManager`’s job—but it handles all turn‐advancement operations and can draw cards when
  ending a turn.

---

## Method 0: **Constructor**

### Step 1–3 Results

|            | Input                                        | Output / State Change                                                                                             |
|------------|----------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| **Step 1** | `deck`                                       | stores `deck`; `queue` and `current` remain unset                                                                 |
| **Step 2** | `deck == null`<br>`deck != null`             | throws NPE if null; otherwise proceeds                                                                            |
| **Step 3** | 1. `deck = null`  <br> 2. `deck = validDeck` | **1** → throws `NullPointerException("Deck is null")`<br>**2** → stores `deck`; `queue` and `current` still unset |

### Step 4

| Test Case | System under test       | Expected behavior                              | Implemented? | Test name                                  |
|-----------|-------------------------|------------------------------------------------|--------------|--------------------------------------------|
| 0.1       | `new TurnManager(null)` | throws `NullPointerException("Deck is null")`  | no           | `ctor_nullDeck_throwsNullPointerException` |
| 0.2       | `new TurnManager(deck)` | stores ref; queue/current remain uninitialized | no           | `ctor_validDeck_initializesState`          |

---

## Method 1: `public void setPlayerManager(PlayerManager pm)`

### Step 1–3 Results

|            | Input                                                                                                                       | Output / State Change                                                                                                                                                                          |
|------------|-----------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Step 1** | `PlayerManager pm`                                                                                                          | none (loads `pm.getPlayers()` into `queue`; sets `current`)                                                                                                                                    |
| **Step 2** | `pm == null`<br>`pm.getPlayers()` empty<br>`size=1`<br>`size>1`                                                             | throws NPE if `pm` null; IAE if list empty; otherwise initializes `queue` and `current`                                                                                                        |
| **Step 3** | 1. `pm = null`  <br> 2. `pm.getPlayers() = []`  <br> 3. `pm.getPlayers() = [p1]`  <br> 4. `pm.getPlayers() = [p1,p2,p3,p4]` | **1** → throws `NullPointerException`<br>**2** → throws `IllegalArgumentException("No players provided")`<br>**3** → `queue=[p1]`, `current=p1`<br>**4** → `queue=[p1,p2,p3,p4]`, `current=p1` |

### Step 4

| Test Case | System under test                 | Expected behavior                                        | Implemented? | Test name                                                   |
|-----------|-----------------------------------|----------------------------------------------------------|--------------|-------------------------------------------------------------|
| 1         | `setPlayerManager(null)`          | throws `NullPointerException`                            | no           | `setPlayerManager_null_throwsNullPointerException`          |
| 2         | `pm.getPlayers().isEmpty()`       | throws `IllegalArgumentException("No players provided")` | no           | `setPlayerManager_emptyList_throwsIllegalArgumentException` |
| 3         | `pm.getPlayers() = [p1]`          | `queue = [p1]`; `current == p1`                          | no           | `setPlayerManager_singlePlayer_initializesCorrectly`        |
| 4         | `pm.getPlayers() = [p1,p2,p3,p4]` | `queue = [p1,p2,p3,p4]`; `current == p1`                 | no           | `setPlayerManager_multiplePlayers_preservesOrder`           |

---

## Method 2: `public Player getCurrentActivePlayer()`

### Step 1–3 Results

|            | Input                                                                                                              | Output / State Change                                                                                                 |
|------------|--------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| **Step 1** | none                                                                                                               | returns `current`                                                                                                     |
| **Step 2** | before vs. after `setPlayerManager(...)`                                                                           | throws ISE if uninitialized; otherwise returns first element of `queue`                                               |
| **Step 3** | 1. before `setPlayerManager`  <br> 2. after `setPlayerManager([p1])`  <br> 3. after `setPlayerManager([p1,p2,p3])` | **1** → throws `IllegalStateException("TurnManager not initialized")`<br>**2** → returns `p1`<br>**3** → returns `p1` |

### Step 4

| Test Case | System under test                 | Expected behavior                                             | Implemented? | Test name                                               |
|-----------|-----------------------------------|---------------------------------------------------------------|--------------|---------------------------------------------------------|
| 1         | before any `setPlayerManager`     | throws `IllegalStateException("TurnManager not initialized")` | no           | `getCurrentActivePlayer_beforeSetup_throwsException`    |
| 2         | after `setPlayerManager([p1])`    | returns `p1`                                                  | no           | `getCurrentActivePlayer_singlePlayer_returnsThatPlayer` |
| 3         | after `setPlayerManager([p1,p2])` | returns `p1`                                                  | no           | `getCurrentActivePlayer_multiPlayers_initialFirst`      |

---

## Method 3: `public void endTurnAndDraw() throws NoCardsToMoveException`

### Step 1–3 Results

|            | Preconditions                                                                                                                                                                                                                                       | Output / State Change                                                                                                                                                                                                                                                                                                                                                      |
|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Step 1** | none                                                                                                                                                                                                                                                | removes `current` from front of `queue`, calls `deck.draw()`; re-adds player to back if >1 remain; updates `current`                                                                                                                                                                                                                                                       |
| **Step 2** | uninitialized; empty queue; deck empty with ≥1 player; deck≥1 & one player; deck≥1 & two players                                                                                                                                                    | throws ISE or NCME; otherwise rotates + draws                                                                                                                                                                                                                                                                                                                              |
| **Step 3** | 1. before `setPlayerManager`  <br> 2. after `setPlayerManager([])`  <br> 3. after `setPlayerManager([p1])`, `deck.size()==0`  <br> 4. after `setPlayerManager([p1])`, `deck.size()>=1`  <br> 5. after `setPlayerManager([p1,p2])`, `deck.size()>=2` | **1** → throws `IllegalStateException("TurnManager not initialized")`<br>**2** → throws `IllegalStateException("No players to manage")`<br>**3** → underlying `draw()` throws `NoCardsToMoveException`; `queue` unchanged<br>**4** → `p1` draws one card; `queue=[p1]`; `current=p1` (end‐of‐game)<br>**5** → `p1` draws one card, re-added; `queue=[p2,p1]`; `current=p2` |

### Step 4

| Test Case | System under test                                   | Expected behavior                                                      | Implemented? | Test name                                               |
|-----------|-----------------------------------------------------|------------------------------------------------------------------------|--------------|---------------------------------------------------------|
| 1         | before any `setPlayerManager`                       | throws `IllegalStateException("TurnManager not initialized")`          | no           | `endTurnAndDraw_beforeSetup_throwsException`            |
| 2         | after `setPlayerManager([])`                        | throws `IllegalStateException("No players to manage")`                 | no           | `endTurnAndDraw_noPlayers_throwsException`              |
| 3         | after `setPlayerManager([p1])`, `deck.size()==0`    | underlying `draw()` throws `NoCardsToMoveException`, queue unchanged   | no           | `endTurnAndDraw_emptyDeck_throwsNoCardsToMoveException` |
| 4         | after `setPlayerManager([p1])`, `deck.size()>=1`    | player draws 1 card; queue empty; `current` reflects end‐of‐game state | no           | `endTurnAndDraw_singlePlayer_drawsAndEndsGame`          |
| 5         | after `setPlayerManager([p1,p2])`, `deck.size()>=2` | `p1` draws+re-add; `current` advances to `p2`; queue = `[p2, p1]`      | no           | `endTurnAndDraw_twoPlayers_rotatesCorrectly`            |

---

## Method 4: `public void endTurnWithoutDraw()`

### Step 1–3 Results

|            | Preconditions                                                   | Output / State Change                                                                                                                                                                                    |
|------------|-----------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Step 1** | none                                                            | removes `current`; re-adds if >1 remain; updates `current`                                                                                                                                               |
| **Step 2** | uninitialized; empty queue; one player; ≥2 players              | throws ISE or rotates accordingly                                                                                                                                                                        |
| **Step 3** | 1. `queue=[]`  <br> 2. `queue=[p1]`  <br> 3. `queue=[p1,p2,p3]` | **1** → throws `IllegalStateException("No players to manage")`<br>**2** → removes `p1`; queue=\[]; `current=p1` (end‐of‐game)<br>**3** → rotates: removes `p1`, re-adds; queue=\[p2,p3,p1]; `current=p2` |

### Step 4

| Test Case | System under test  | Expected behavior                                         | Implemented? | Test name                                             |
|-----------|--------------------|-----------------------------------------------------------|--------------|-------------------------------------------------------|
| 1         | `queue=[]`         | throws `IllegalStateException("No players to manage")`    | no           | `endTurnWithoutDraw_noPlayers_throwsException`        |
| 2         | `queue=[p1]`       | removes `p1`; queue empty; `current` reflects end‐of‐game | no           | `endTurnWithoutDraw_singlePlayer_endsGame`            |
| 3         | `queue=[p1,p2,p3]` | rotates: removes `p1` + re-add to back; `current == p2`   | no           | `endTurnWithoutDraw_multiplePlayers_rotatesCorrectly` |

---

## Method 5: `public void endTurnWithoutDrawForAttacks()`

### Step 1–3 Results

|            | Input                                                                                                      | Output / State Change                                                             |
|------------|------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| **Step 1** | The current contents of the `turnQueue`                                                                    | Queue updated: current advances, next gets 2 turns                                |
| **Step 2** | Collection (Empty, Exactly 2 Element, Exactly 5 Elements (Max), Element containing duplicates)             | Next player receives 2 turns, player order preserved, or Exception                |
| **Step 3** | `[]`, `[player1, player2]`, `[player1, player2, player1]`, `[player1, player2, player3, player4, player5]` | `IllegalStateException("No players to manage")`, Correct advancement or exception |

##### Note:

* Exactly 2 Elements for turnQueue because PlayerManager have a requirement that there should be between 2 and 5 players
  in TurnManager.
* Don't need to test 5 Elements (two is more than enough) since the integration test for Game should allow testing
  multiple players and each doing their different turns and card

### Step 4

| Test Case | System under test                                                          | Expected behavior                                                                                         | Implemented?       | Test name                                                                   |
|-----------|----------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|--------------------|-----------------------------------------------------------------------------|
| 1         | `queue=[]`                                                                 | throws `IllegalStateException("No players to manage")`                                                    | :white_check_mark: | `endTurnWithoutDrawForAttacks_emptyQueue_throwsIllegalStateException `      |
| 2         | `queue=[player1, player2]`                                                 | removes `player1`; re-adds exactly one `player1`; `current == player2`, `player2` now have two turns more | :white_check_mark: | `endTurnWithoutDrawForAttacks_withTwoPlayers_incrementTurnForPlayerTwo `    |
| 3         | `queue=[player1,player2,player3]` player2 is the one that will play attack | removes `player2`; re-adds exactly one `player2`; `current == player3`, `player3` now have two turns more | :white_check_mark: | `endTurnWithoutDrawForAttacks_withThreePlayers_incrementTurnForPlayerThree` |

---

## Method 6: `public void addTurnForCurrentPlayer()`

### Step 1–3 Results

|            | Preconditions                                                | Output / State Change                                                                                                                             |
|------------|--------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| **Step 1** | none                                                         | inserts `current` at index 1                                                                                                                      |
| **Step 2** | uninitialized; empty queue; one player; ≥2 players           | throws ISE or duplicates accordingly                                                                                                              |
| **Step 3** | 1. `queue=[]`  <br> 2. `queue=[p1]`  <br> 3. `queue=[p1,p2]` | **1** → throws `IllegalStateException("No players to manage")`<br>**2** → queue=\[p1,p1]; `current=p1`<br>**3** → queue=\[p1,p1,p2]; `current=p1` |

### Step 4

| Test Case | System under test | Expected behavior                                      | Implemented? | Test name                                                 |
|-----------|-------------------|--------------------------------------------------------|--------------|-----------------------------------------------------------|
| 1         | `queue=[]`        | throws `IllegalStateException("No players to manage")` | no           | `addTurnForCurrentPlayer_noPlayers_throwsException`       |
| 2         | `queue=[p1]`      | transforms to `[p1,p1]`; `current == p1`               | no           | `addTurnForCurrentPlayer_singlePlayer_duplicatesNextTurn` |
| 3         | `queue=[p1,p2]`   | transforms to `[p1,p1,p2]`; `current == p1`            | no           | `addTurnForCurrentPlayer_multiplePlayers_insertsProperly` |

---

## Method 7: `public void syncWith(List<Player> activePlayers)`

### Step 1–3 Results

|            | Input                                                                                                                                                                           | Output / State Change                                                                                                                                                                                                                                       |
|------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Step 1** | `List<Player> activePlayers`                                                                                                                                                    | none (rebuilds `queue` from input; sets `current`)                                                                                                                                                                                                          |
| **Step 2** | `null`; empty list; contains old `current`; missing old `current`; reordered                                                                                                    | throws NPE or IAE; otherwise resets `queue` and `current`                                                                                                                                                                                                   |
| **Step 3** | 1. `activePlayers = null`  <br> 2. `activePlayers = []`  <br> 3. `activePlayers = [oldCurrent,p2,...]`  <br> 4. `activePlayers = [p2,p3]`  <br> 5. `activePlayers = [p3,p1,p2]` | **1** → throws `NullPointerException`<br>**2** → throws `IllegalArgumentException("No players provided")`<br>**3** → `queue=[oldCurrent,p2,...]`; `current=oldCurrent`<br>**4** → `queue=[p2,p3]`; `current=p2`<br>**5** → `queue=[p3,p1,p2]`; `current=p3` |

### Step 4

| Test Case | System under test              | Expected behavior                                        | Implemented? | Test name                                           |
|-----------|--------------------------------|----------------------------------------------------------|--------------|-----------------------------------------------------|
| 1         | `syncWith(null)`               | throws `NullPointerException`                            | no           | `syncWith_null_throwsNullPointerException`          |
| 2         | `syncWith([])`                 | throws `IllegalArgumentException("No players provided")` | no           | `syncWith_emptyList_throwsIllegalArgumentException` |
| 3         | `syncWith([oldCurrent,p2,p3])` | `queue` matches input; `current == oldCurrent`           | no           | `syncWith_includesCurrent_keepsOrderAndCurrent`     |
| 4         | `syncWith([p2,p3])`            | `queue = [p2,p3]`; `current == p2`                       | no           | `syncWith_excludesOldCurrent_setsNewCurrent`        |
| 5         | `syncWith([p3,p1,p2])`         | `queue = [p3,p1,p2]`; `current == p3`                    | no           | `syncWith_reordersQueue_updatesCurrent`             |

---

## Method 8: `public List<Player> getTurnOrder()`

### Step 1–3 Results

|            | Preconditions                                                                                                 | Output / State Change                                                                                                              |
|------------|---------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| **Step 1** | none                                                                                                          | returns snapshot of `queue`                                                                                                        |
| **Step 2** | before setup; after setup; after sync/end-turn                                                                | throws ISE or returns list                                                                                                         |
| **Step 3** | 1. before `setPlayerManager`  <br> 2. after `setPlayerManager([p1,p2])`  <br> 3. after `syncWith([p3,p1,p2])` | **1** → throws `IllegalStateException("TurnManager not initialized")`<br>**2** → returns `[p1,p2]`<br>**3** → returns `[p3,p1,p2]` |

### Step 4

| Test Case | System under test                    | Expected behavior                                             | Implemented? | Test name                                     |
|-----------|--------------------------------------|---------------------------------------------------------------|--------------|-----------------------------------------------|
| 1         | before any `setPlayerManager`        | throws `IllegalStateException("TurnManager not initialized")` | no           | `getTurnOrder_beforeSetup_throwsException`    |
| 2         | after `setPlayerManager([p1,p2,p3])` | returns `[p1,p2,p3]`                                          | no           | `getTurnOrder_afterSetup_returnsInitialOrder` |
| 3         | after `syncWith([p3,p1,p2])`         | returns `[p3,p1,p2]`                                          | no           | `getTurnOrder_afterMutations_reflectsQueue`   |

## Method 9: `public int getTurnsCountFor(Player player)`

#### Note:

This method is mainly used for Testing and not for game logic. :)

### Step 1–3 Results

|            | Input 1                                              | Input 2                                                                                                    | Output / State Change                                                            |
|------------|------------------------------------------------------|------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| **Step 1** | A player that wants to know how many turns they have | The current contents of the `turnQueue`                                                                    | Number of times the player appears in `turnQueue`                                |
| **Step 2** | `Player` Object                                      | Collection (Empty, Exactly 2 Element, Exactly 5 Elements (Max), Element containing duplicates)             | Integer or Exception                                                             |
| **Step 3** | `null`, Valid `Player`                               | `[]`, `[player1, player2]`, `[player1, player2, player1]`, `[player1, player2, player3, player4, player5]` | **null** → throws `NullPointerException("Player Cannot be Null")`, `0`, `1`, `2` |

##### Note:

Exactly 2 Elements for turnQueue because PlayerManager have a requirement that there should be between 2 and 5 players
in TurnManager.

### Step 4

|             | System Under Test                                                            | Expected Output                                 | Implemented?       | Test Name                                                   |
|-------------|------------------------------------------------------------------------------|-------------------------------------------------|--------------------|-------------------------------------------------------------|
| Test Case 1 | `player = null`, queue is empty                                              | `NullPointerException("Player Cannot be Null")` | :white_check_mark: | `getTurnsCountFor_nullPlayer_throwsNullPointerException`    |
| Test Case 2 | `player = player`, queue is `[]`                                             | `0`                                             | :white_check_mark: | `getTurnsCountFor_emptyQueue_returnsZero`                   |
| Test Case 3 | `player = player1`, queue is `[player1, player2]`                            | `1`                                             | :white_check_mark: | `getTurnsCountFor_playerInQueueWithTwo_returnsOne`          |
| Test Case 4 | `player = player3`, queue is `[player1, player2]`                            | `0`                                             | :white_check_mark: | `getTurnsCountFor_playerNotInQueueWithTwo_returnsZero`      |
| Test Case 5 | `player = player1`, queue is `[player1, player2, player1]`                   | `2`                                             | :white_check_mark: | `getTurnsCountFor_duplicatePlayerInQueueWithTwo_returnsTwo` |
| Test Case 6 | `player = player5`, queue is `[player1, player2, player3, player4, player5]` | `1`                                             | :white_check_mark: | `getTurnsCountFor_playerInQueueWithFive_retur nsOne`        |

# Method 10: `public void reverseOrder()`

### Step 1–3 Results

|            | Input                                                                                                      | Output / State Change                                                             |
|------------|------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| **Step 1** | The current contents of the `turnQueue`                                                                    | Queue reverses order                                                              |
| **Step 2** | Collection (Empty, Exactly 2 Element, Exactly 5 Elements (Max), Element containing duplicates)             | Reverse Order or Exception                                                        |
| **Step 3** | `[]`, `[player1, player2]`, `[player1, player2, player1]`, `[player1, player2, player3, player4, player5]` | `IllegalStateException("No players to manage")`, Correct advancement or exception |

##### Note:

* Exactly 2 Elements for turnQueue because PlayerManager have a requirement that there should be between 2 and 5 players
  in TurnManager.
* Don't need to test 5 Elements (two is more than enough) since the integration test for Game should allow testing
  multiple players and each doing their different turns and card

### Step 4

| Test Case | System under test                 | Expected behavior                                      | Implemented?       | Test name                                              |
|-----------|-----------------------------------|--------------------------------------------------------|--------------------|--------------------------------------------------------|
| 1         | `queue=[]`                        | throws `IllegalStateException("No players to manage")` | :white_check_mark: | `reverseOrder_emptyQueue_throwsIllegalStateException ` |
| 2         | `queue=[player1, player2]`        | `queue=[player2, player1]`                             | :white_check_mark: | `reverseOrder_withTwoPlayers_orderReverses `           |
| 3         | `queue=[player1,player2,player3]` | `queue=[player3,player2,player1]`                      | :white_check_mark: | `reverseOrder_withThreePlayers_orderReverses`          |

## Method 11: `public void setRequiredTurns(int requiredTurns)`

### Step 1–3 Results

|            | Input                          | Output / State Change                                                                    |
|------------|--------------------------------|------------------------------------------------------------------------------------------|
| **Step 1** | Amount of turns we want to set | set `requiredTurns` to be that number                                                    |
| **Step 2** | Count                          | Count or Exception                                                                       |
| **Step 3** | `-1`, `0`, `1`, `2`            | **-1** → throws `IllegalArgumentException("Required turns cannot be negative")`, 0, 1, 2 |

### Step 4

| Test Case | System under test      | Expected behavior                                                      | Implemented?        | Test name                                                      |
|-----------|------------------------|------------------------------------------------------------------------|---------------------|----------------------------------------------------------------|
| 1         | `setRequiredTurns(-1)` | throws `IllegalArgumentException("Required turns cannot be negative")` | :white_check_mark:  | `setRequiredTurns_negativeOne_throwsIllegalArgumentException ` |
| 2         | `setRequiredTurns(0)`  | `requiredTurns == 0`                                                   | no                  | `setRequiredTurns_zero_zeroRequiredTurns`                      |
| 3         | `setRequiredTurns(1)`  | `requiredTurns == 1`                                                   | no                  | `setRequiredTurns_one_oneRequiredTurns`                        |
| 4         | `setRequiredTurns(2)`  | `requiredTurns == 2`                                                   | no                  | `setRequiredTurns_two_twoRequiredTurns`                        |