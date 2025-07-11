# BVA Analysis for **PlayerManager**

## Method 0: **Constructor**

### Step 1–3 Results

|            | Input                    | Output / State Change                                                     |
|------------|--------------------------|---------------------------------------------------------------------------|
| **Step 1** | Deck                     | constructs PlayerManager instance or exception                            |
| **Step 2** | Boolean (is null or not) | stores `deck`; initializes empty `players` and `status` maps or exception |
| **Step 3** | true/false               | stores `deck`; `players` & `status` remain empty or NullPointerException  |

### Step 4

| Test Case   | System under test | Expected behavior                                    | Implemented?       | Test name                                             |
|-------------|-------------------|------------------------------------------------------|--------------------|-------------------------------------------------------|
| Test Case 1 | deck `null`       | throws `NullPointerException("Deck cannot be null")` | :white_check_mark: | `constructor_withNullDeck_throwsNullPointerException` |
| Test Case 2 | deck `non-null`   | stores deck ref; `players` & `status` start empty    | :white_check_mark: | `constructor_withValidDeck_initializesEmptyManager`   |

---

## Method 1: `public void addPlayers(int numberOfPlayers)`

### Step 1–3 Results

|            | Input             | Output / State Change                                                                               |
|------------|-------------------|-----------------------------------------------------------------------------------------------------|
| **Step 1** | `numberOfPlayers` | creates & stores `n` Player objects; each `hand = []`, `status = true`                              |
| **Step 2** | Cases             | populates `players`/`status` maps or exception                                                      |
| **Step 3** | `<2`; `2–5`; `>5` | `<2` or `>5`: throws `InvalidNumberofPlayersException`; otherwise populates `players`/`status` maps |

### Step 4

| Test Case   | System under test | Expected behavior                                                | Implemented?       | Test name                                                         |
|-------------|-------------------|------------------------------------------------------------------|--------------------|-------------------------------------------------------------------|
| Test Case 1 | `addPlayers(1)`   | throws `InvalidNumberofPlayersException`                         | :white_check_mark: | `addPlayers_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| Test Case 2 | `addPlayers(6)`   | throws `InvalidNumberofPlayersException`                         | :white_check_mark: | `addPlayers_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| Test Case 3 | `addPlayers(2)`   | `players.size()==2`; each `hand.isEmpty()`; each `status==true`  | :white_check_mark: | `addPlayers_withTwoPlayers_initializesBothActive`                 |
| Test Case 4 | `addPlayers(3)`   | `players.size()==3`;  each `hand.isEmpty()`; each `status==true` | :white_check_mark: | `addPlayers_withValidNumber_createsThatManyPlayers`               |

## Method 2: `public void removePlayerFromGame(Player player)`

### Step 1–3 Results

|        | Input                                                                       | Output / State Change                                                           |
|--------|-----------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| Step 1 | Player to be removed                                                        | updates `player` in `players` list or exception                                 |
| Step 2 | Cases                                                                       | sets player's status to false or exception                                      |
| Step 3 | 1. `player = null` 2. `player = playerNotInGame` 3. `player = activePlayer` | Sets player's status to false or NullPointerException, IllegalArgumentException |

### Step 4

| Test Case   | System under test        | Expected output                                               | Implemented?       | Test name                                                                 |
|-------------|--------------------------|---------------------------------------------------------------|--------------------|---------------------------------------------------------------------------|
| Test Case 1 | player `null`            | throws `NullPointerException`                                 | :white_check_mark: | `removePlayerFromGame_withNullPlayer_throwsNullPointerException`          |
| Test Case 2 | player `playerNotInGame` | throws `IllegalArgumentException("Player not found in game")` | :white_check_mark: | `removePlayerFromGame_withPlayerNotInGame_throwsIllegalArgumentException` |
| Test Case 3 | player `activePlayer`    | sets activePlayer's status `false`                            | :white_check_mark: | `removePlayerFromGame_withValidPlayer_marksPlayerInactive`                |

## Method 3: `public List<Player> getActivePlayers()`

### Step 1–3 Results

|            | Input                                 | Output / State Change                                                          |
|------------|---------------------------------------|--------------------------------------------------------------------------------|
| **Step 1** | none                                  | returns internal `List<Player>` with pnly those players whose status is active |
| **Step 2** | after `addPlayers` and `removePlayer` | Collection                                                                     |
| **Step 3** | 1. `addPlayers`  2.  `removePlayer`   | `size` equals addedPlayers-removedPlayers                                      |

### Step 4

| Test Case   | System under test                   | Expected behavior         | Implemented?       | Test name                                                 |
|-------------|-------------------------------------|---------------------------|--------------------|-----------------------------------------------------------|
| Test Case 1 | add `4` players, remove `2` players | return `2` active players | :white_check_mark: | `getActivePlayers_afterRemovals_returnsOnlyActivePlayers` |

## Method 4: `public Player getPlayerByIndex (int index)

### Step 1-3 Results

|        | Input 1                                          | Input 2                                   | Output                                |
|--------|--------------------------------------------------|-------------------------------------------|---------------------------------------|
| Step 1 | index corresponding to the player                | the state of the list players             | Get the player at specified index     |
| Step 2 | Array Indices (Index are -1, 0, 1, size, size+1) | Collection                                | Player Object or Index                |
| Step 3 | `-1`, `0`, `1`, `size`                           | empty list; list with more than 1 element | Player or `IndexOutOfBoundsException` |               |

### Step 4:

|             | System under test                        | Expected output             | Implemented?       | Test name                                                                   |
|-------------|------------------------------------------|-----------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | index `-1`, players `[player1, player2]` | `IndexOutOfBoundsException` | :white_check_mark: | getPlayerByIndex_withNegativeIndex_throwsIndexOutOfBoundsException          |
| Test Case 2 | index `1`, players `[]`                  | `IndexOutOfBoundsException` | :white_check_mark: | getPlayerByIndex_noPlayersWithPositiveIndex_throwsIndexOutOfBoundsException |
| Test Case 3 | index `2`, players `[player1, player2]`  | `IndexOutOfBoundsException` | :white_check_mark: | getPlayerByIndex_twoPlayersWithTwoIndex_throwsIndexOutOfBoundsException     |
| Test Case 4 | index `0`, players `[player1, player2]`  | `player1`                   | :white_check_mark: | getPlayerByIndex_twoPlayersWithZeroIndex_returnsFirstPlayer                 |
| Test Case 5 | index `1`, players `[player1, player2]`  | `player2`                   | :white_check_mark: | getPlayerByIndex_twoPlayersWithOneIndex_returnsSecondPlayer                 |
| Test Case 6 | index `3`, players `[player1, player2]`  | `IndexOutOfBoundsException` | :white_check_mark: | getPlayerByIndex_twoPlayersWithThreeIndex_throwsIndexOutOfBoundsException   |

## Method 5: `getNumberOfPlayers()`

### Step 1-3 Results

|        | Input                                                                                                  | Output                        |
|--------|--------------------------------------------------------------------------------------------------------|-------------------------------|
| Step 1 | the state of the players list                                                                          | number of players in the list |
| Step 2 | Collection                                                                                             | Count                         |
| Step 3 | empty list; list with 2 elements (min number of players); list with 5 elements (max number of players) | 0; 2, 5                       |

### Step 4:

|             | System under test                                       | Expected output | Implemented?       | Test name                                     |
|-------------|---------------------------------------------------------|-----------------|--------------------|-----------------------------------------------|
| Test Case 1 | players `[]`                                            | 0               | :white_check_mark: | getNumberOfPlayers_withNoPlayers_returnsZero  |
| Test Case 2 | players `[player1, player2]`                            | 2               | :white_check_mark: | getNumberOfPlayers_withMinPlayers_returnsTwo  |
| Test Case 3 | players `[player1, player2, player3, player4, player5]` | 5               | :white_check_mark: | getNumberOfPlayers_withMaxPlayers_returnsFive |

## Method 6: `public List<Player> getActivePlayers()`

### Step 1–3 Results

|            | Input                      | Output / State Change                  |
|------------|----------------------------|----------------------------------------|
| **Step 1** | internal players list      | List of only active players            |
| **Step 2** | Collection                 | Collection                             |
| **Step 3** | empty, partial, all active | empty list, partial list, or full list |

### Step 4

| Test Case   | System under test                       | Expected behavior       | Implemented?       | Test name                                                 |
|-------------|-----------------------------------------|-------------------------|--------------------|-----------------------------------------------------------|
| Test Case 1 | empty player list                       | return empty list       | :white_check_mark: | `getActivePlayers_withNoPlayers_returnsEmptyList`         |
| Test Case 2 | all players active                      | return all players      | :white_check_mark: | `getActivePlayers_allPlayersActive_returnsAllPlayers`     |
| Test Case 3 | add 4 players, remove 2 players         | return 2 active players | :white_check_mark: | `getActivePlayers_afterRemovals_returnsOnlyActivePlayers` |
| Test Case 4 | add max players (5), remove all but one | return 1 active player  | :white_check_mark: | `getActivePlayers_withOneActivePlayer_returnsOnePlayer`   |