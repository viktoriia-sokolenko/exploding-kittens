# BVA Analysis for **TurnManager**

#### Important Note

* The `TurnManager` now **owns** a `Deck` (injected via constructor) and manages a turn‐order queue of players. It never
  eliminates players—that’s `PlayerManager`’s job—but it handles all turn‐advancement operations and can draw cards when
  ending a turn.

---

## Method 1: `public void setPlayerManager(PlayerManager pm)`

### Step 1-3 Results

|        | Input                                        | Output / State Change                                      |
|--------|----------------------------------------------|------------------------------------------------------------|
| Step 1 | PlayerManager pm                             | Initializes turnQueue with players and sets current player |
| Step 2 | null, empty players list, valid players list | NPE, IAE, or successful initialization                     |
| Step 3 | null, [], [p1,p2,p3]                         | NPE, IAE("No players"), or success                         |

### Step 4:

##### Each-choice strategy (covering main scenarios)

|             | System under test                      | Expected output                                    | Implemented?       | Test name                                                             |
|-------------|----------------------------------------|----------------------------------------------------|--------------------|-----------------------------------------------------------------------|
| Test Case 1 | `setPlayerManager(null)`               | throws NullPointerException                        | :white_check_mark: | `setPlayerManager_withNullPlayerManager_throwsNullPointerException`   |
| Test Case 2 | `setPlayerManager(emptyPlayerManager)` | throws IllegalArgumentException("No players")      | :white_check_mark: | `setPlayerManager_withEmptyPlayerList_throwsIllegalArgumentException` |
| Test Case 3 | `setPlayerManager(validPlayerManager)` | initializes queue and sets first player as current | :white_check_mark: | `setPlayerManager_withValidPlayers_initializesCurrentPlayer`          |

## Method 2: `public Player getCurrentActivePlayer()`

### Step 1-3 Results

|        | Input                | Output / State Change                               |
|--------|----------------------|-----------------------------------------------------|
| Step 1 | None                 | Returns current active player                       |
| Step 2 | Before/after setup   | IllegalStateException or Player reference           |
| Step 3 | Not init, After init | ISE("not initialized") or reference to first player |

### Step 4:

##### Each-choice strategy

|             | System under test           | Expected output                                 | Implemented?       | Test name                                                        |
|-------------|-----------------------------|-------------------------------------------------|--------------------|------------------------------------------------------------------|
| Test Case 1 | Before initialization       | throws IllegalStateException("not initialized") | :white_check_mark: | `getCurrentActivePlayer_beforeSetup_throwsIllegalStateException` |
| Test Case 2 | After proper initialization | returns first player from queue                 | :white_check_mark: | `getCurrentActivePlayer_afterSetup_returnsFirstPlayer`           |

## Method 3: `public void endTurnWithoutDraw()`

### Step 1-3 Results

|        | Input                                  | Output / State Change                              |
|--------|----------------------------------------|----------------------------------------------------|
| Step 1 | Current game state                     | Advances turn or increments attack counter         |
| Step 2 | Empty queue, Normal turn, Under attack | ISE, advance turn, or increment counter            |
| Step 3 | [], [p1,p2], Under attack state        | ISE("No players"), advance turn, increment counter |

### Step 4:

##### Each-choice strategy

|             | System under test             | Expected output                            | Implemented?       | Test name                                                           |
|-------------|-------------------------------|--------------------------------------------|--------------------|---------------------------------------------------------------------|
| Test Case 1 | Before initialization         | throws IllegalStateException("No players") | :white_check_mark: | `endTurnWithoutDraw_beforeSetup_throwsIllegalStateException`        |
| Test Case 2 | Two players, not under attack | Advances to next player                    | :white_check_mark: | `endTurnWithoutDraw_withTwoPlayersNotAttacked_advancesToNextPlayer` |
| Test Case 3 | One player, not under attack  | Stays on same player                       | :white_check_mark: | `endTurnWithoutDraw_withOnePlayerNotAttacked_staysOnSamePlayer`     |
| Test Case 4 | Under attack                  | Increments turns taken                     | :white_check_mark: | `endTurnWithoutDraw_underAttack_callsIncrementTurnsTaken`           |

## Method 4: `public boolean isUnderAttack()`

### Step 1-3 Results

|        | Input                               | Output / State Change |
|--------|-------------------------------------|-----------------------|
| Step 1 | Required turns, turns taken         | Boolean result        |
| Step 2 | Various combinations of turn counts | true/false            |
| Step 3 | (1,0), (2,0), (2,1), (2,2), (3,3)   | false, true results   |

### Step 4:

##### All-combination strategy

|             | System under test                          | Expected output | Implemented?       | Test name                                            |
|-------------|--------------------------------------------|-----------------|--------------------|------------------------------------------------------|
| Test Case 1 | Default turn (required=1, taken=0)         | false           | :white_check_mark: | `isUnderAttack_defaultTurn_returnsFalse`             |
| Test Case 2 | Attack start (required=2, taken=0)         | true            | :white_check_mark: | `isUnderAttack_requiredTwoTakenZero_returnsTrue`     |
| Test Case 3 | Mid-attack (required=2, taken=1)           | true            | :white_check_mark: | `isUnderAttack_requiredTwoTakenOne_returnsTrue`      |
| Test Case 4 | Attack complete (required=2, taken=2)      | false           | :white_check_mark: | `isUnderAttack_requiredTwoTakenTwo_returnsFalse`     |
| Test Case 5 | Long attack complete (required=3, taken=3) | false           | :white_check_mark: | `isUnderAttack_requiredThreeTakenThree_returnsFalse` |
| Test Case 6 | Long attack ongoing (required=3, taken=1)  | true            | :white_check_mark: | `isUnderAttack_requiredThreeTakenOne_returnsTrue`    |

## Method 5: `public int getTurnsCountFor(Player player)`

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

|              | System Under Test                                                            | Expected Output                                 | Implemented?       | Test Name                                                   |
|--------------|------------------------------------------------------------------------------|-------------------------------------------------|--------------------|-------------------------------------------------------------|
| Test Case  1 | `player = null`, queue is empty                                              | `NullPointerException("Player Cannot be Null")` | :white_check_mark: | `getTurnsCountFor_nullPlayer_throwsNullPointerException`    |
| Test Case  2 | `player = player`, queue is `[]`                                             | `0`                                             | :white_check_mark: | `getTurnsCountFor_emptyQueue_returnsZero`                   |
| Test Case  3 | `player = player1`, queue is `[player1, player2]`                            | `1`                                             | :white_check_mark: | `getTurnsCountFor_playerInQueueWithTwo_returnsOne`          |
| Test Case  4 | `player = player3`, queue is `[player1, player2]`                            | `0`                                             | :white_check_mark: | `getTurnsCountFor_playerNotInQueueWithTwo_returnsZero`      |
| Test Case  5 | `player = player1`, queue is `[player1, player2, player1]`                   | `2`                                             | :white_check_mark: | `getTurnsCountFor_duplicatePlayerInQueueWithTwo_returnsTwo` |
| Test Case  6 | `player = player5`, queue is `[player1, player2, player3, player4, player5]` | `1`                                             | :white_check_mark: | `getTurnsCountFor_playerInQueueWithFive_retur nsOne`        |

# Method 6: `public void reverseOrder()`

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

| Test Case | System under test                 | Expected Output                                        | Implemented?       | Test name                                              |
|-----------|-----------------------------------|--------------------------------------------------------|--------------------|--------------------------------------------------------|
| 1         | `queue=[]`                        | throws `IllegalStateException("No players to manage")` | :white_check_mark: | `reverseOrder_emptyQueue_throwsIllegalStateException ` |
| 2         | `queue=[player1, player2]`        | `queue=[player2, player1]`                             | :white_check_mark: | `reverseOrder_withTwoPlayers_orderReverses `           |
| 3         | `queue=[player1,player2,player3]` | `queue=[player3,player2,player1]`                      | :white_check_mark: | `reverseOrder _withThreePlayers_orderReverses`         |

## Method 7: `public boolean isUnderAttack()`

### Step 1–3 Results

|        | Input 1                  | Input 2                                      | Expected output                                                       |
|--------|--------------------------|----------------------------------------------|-----------------------------------------------------------------------|
| Step 1 | Required number of turns | Number of turns the current player has taken | Returns `true` if player still has pending turns due to attack effect |
| Step 2 | Count                    | **Values of `currentPlayerTurnsTaken`**      | **Boolean result (true if attack in effect)**                         |
| Step 3 | `1`                      | `0`, `1`                                     | `false` (Not under attack: default turn, no extra turns)              |
|        | `2`                      | `0`                                          | `true` (Under attack: 2 required, 0 taken)                            |
|        | `2`                      | `1`                                          | `true` (Under attack: 2 required, 1 taken)                            |
|        | `2`                      | `2`                                          | `false` (Attack fulfilled: no more extra turns)                       |
|        | `3`                      | `3`                                          | `false` (All turns taken)                                             |
|        | `3`                      | `0`, `1`, `2`                                | `true` (Still has remaining turns to take)                            |

### Step 4

|             | System under test                               | Expected output | Implemented?       | Test name                                            |
|-------------|-------------------------------------------------|-----------------|--------------------|------------------------------------------------------|
| Test Case 1 | `requireTurns=1`, `currentPlayerTurnsTaken = 0` | `false`         | :white_check_mark: | `isUnderAttack_defaultTurn_returnsFalse`             |
| Test Case 2 | `requireTurns=2`, `currentPlayerTurnsTaken = 0` | `true`          | :white_check_mark: | `isUnderAttack_requiredTwoTakenZero_returnsTrue`     |
| Test Case 3 | `requireTurns=2`, `currentPlayerTurnsTaken = 1` | `true`          | :white_check_mark: | `isUnderAttack_requiredTwoTakenOne_returnsTrue`      |
| Test Case 4 | `requireTurns=2`, `currentPlayerTurnsTaken = 2` | `false`         | :white_check_mark: | `isUnderAttack_requiredTwoTakenTwo_returnsFalse`     |
| Test Case 5 | `requireTurns=3`, `currentPlayerTurnsTaken = 3` | `false`         | :white_check_mark: | `isUnderAttack_requiredThreeTakenThree_returnsFalse` |
| Test Case 6 | `requireTurns=3`, `currentPlayerTurnsTaken = 1` | `true`          | :white_check_mark: | `isUnderAttack_requiredThreeTakenOne_returnsTrue`    |

## Method 8: `public void incrementTurnsTaken()`

### Step 1–3 Results

|        | Input 1                           | Input 2                               | Expected Result / State Change                                                       |
|--------|-----------------------------------|---------------------------------------|--------------------------------------------------------------------------------------|
| Step 1 | Required turns before method call | Player turns taken before call amount | May increment counter or advance turn depending on values                            |
| Step 2 | Count                             | Count                                 | **Effect on state**                                                                  |
| Step 3 | `requiredTurns = 1`               | `0`                                   | Becomes `1 >= 1`, so: `advanceToNextPlayer()` called; values reset                   |
|        | `requiredTurns = 2`               | `0`                                   | Incremented to 1; no advancement; `requiredTurns = 2`, `currentPlayerTurnsTaken = 1` |
|        | `requiredTurns = 2`               | `1`                                   | Incremented to 2; advancement triggered; values reset                                |
|        | `requiredTurns = 3`               | `2`                                   | Incremented to 3; advancement triggered; values reset                                |

### Step 4

|             | System under test                               | Expected output / state change                                                     | Implemented?       | Test name                                                             |
|-------------|-------------------------------------------------|------------------------------------------------------------------------------------|--------------------|-----------------------------------------------------------------------|
| Test Case 1 | `requireTurns=1`, `currentPlayerTurnsTaken = 0` | `advanceToNextPlayer()` called, `requiredTurns = 1`, `currentPlayerTurnsTaken = 0` | :white_check_mark: | `incrementTurnsTaken_defaultTurn_advancesAndResets`                   |
| Test Case 2 | `requireTurns=2`, `currentPlayerTurnsTaken = 0` | `currentPlayerTurnsTaken = 1`; no advancement                                      | :white_check_mark: | `incrementTurnsTaken_partialAttackTurn_doesNotAdvance`                |
| Test Case 3 | `requireTurns=2`, `currentPlayerTurnsTaken = 1` | `advanceToNextPlayer()` called, `requiredTurns = 1`, `currentPlayerTurnsTaken = 0` | :white_check_mark: | `incrementTurnsTaken_finalAttackTurn_advancesAndResets`               |
| Test Case 4 | `requireTurns=3`, `currentPlayerTurnsTaken = 2` | `advanceToNextPlayer()` called, `requiredTurns = 1`, `currentPlayerTurnsTaken = 0` | :white_check_mark: | `incrementTurnsTaken_finalTurnOfMultipleTurnAttack_advancesAndResets` |
| Test Case 5 | `requireTurns=3`, `currentPlayerTurnsTaken = 1` | `currentPlayerTurnsTaken = 2`; no advancement                                      | :white_check_mark: | `incrementTurnsTaken_midAttack_doesNotAdvance`                        |

## Method 9: `public void setRequiredTurns(int requiredTurns)`

### Step 1–3 Results

|            | Input                          | Output / State Change                                                                    |
|------------|--------------------------------|------------------------------------------------------------------------------------------|
| **Step 1** | Amount of turns we want to set | set `requiredTurns` to be that number                                                    |
| **Step 2** | Count                          | Count or Exception                                                                       |
| **Step 3** | `-1`, `0`, `1`, `2`            | **-1** → throws `IllegalArgumentException("Required turns cannot be negative")`, 0, 1, 2 |

### Step 4

| Test Case | System under test      | Expected behavior                                                      | Implemented?       | Test name                                                      |
|-----------|------------------------|------------------------------------------------------------------------|--------------------|----------------------------------------------------------------|
| 1         | `setRequiredTurns(-1)` | throws `IllegalArgumentException("Required turns cannot be negative")` | :white_check_mark: | `setRequiredTurns_negativeOne_throwsIllegalArgumentException ` |
| 2         | `setRequiredTurns(0)`  | `requiredTurns == 0`                                                   | :white_check_mark: | `setRequiredTurns_zero_zeroRequiredTurns`                      |
| 3         | `setRequiredTurns(1)`  | `requiredTurns == 1`                                                   | :white_check_mark: | `setRequiredTurns_one_oneRequiredTurns`                        |
| 4         | `setRequiredTurns(2)`  | `requiredTurns == 2`                                                   | :white_check_mark: | `setRequiredTurns_two_twoRequiredTurns`                        |

## Method 10: `public void setCurrentPlayerTurnsTaken(int turnTaken)`

### Step 1–3 Results

|            | Input                          | Output / State Change                                                                                |
|------------|--------------------------------|------------------------------------------------------------------------------------------------------|
| **Step 1** | Amount of turns we want to set | set `turnTaken` to be that number                                                                    |
| **Step 2** | Count                          | Count or Exception                                                                                   |
| **Step 3** | `-1`, `0`, `1`, `2`            | **-1** → throws `IllegalArgumentException("Current player turns taken cannot be negative")`, 0, 1, 2 |

### Step 4

| Test Case | System under test                | Expected behavior                                                                  | Implemented?       | Test name                                                                |
|-----------|----------------------------------|------------------------------------------------------------------------------------|--------------------|--------------------------------------------------------------------------|
| 1         | `setCurrentPlayerTurnsTaken(-1)` | throws `IllegalArgumentException("Current player turns taken cannot be negative")` | :white_check_mark: | `setCurrentPlayerTurnsTaken_negativeOne_throwsIllegalArgumentException ` |
| 2         | `setCurrentPlayerTurnsTaken(0)`  | `requiredTurns == 0`                                                               | :white_check_mark: | `setCurrentPlayerTurnsTaken_zero_zeroTurnsTaken`                         |
| 3         | `setCurrentPlayerTurnsTaken(1)`  | `requiredTurns == 1`                                                               | :white_check_mark: | `setCurrentPlayerTurnsTaken_one_oneTurnsTaken`                           |
| 4         | `setCurrentPlayerTurnsTaken(2)`  | `requiredTurns == 2`                                                               | :white_check_mark: | `setCurrentPlayerTurnsTaken_two_twoTurnsTaken`                           |
