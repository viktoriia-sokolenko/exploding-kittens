# BVA Analysis for **PlayerManager**

#### Important Note

The `PlayerManager` oversees player setup, hand allocation, and active status tracking.  Its main responsibilities include:

1. **`addPlayers(int n)`** – Creates `n` new `Player` instances, initializes their hands and status.
2. **`makePlayersDrawTheirInitialHands()`** – Deals the starting hand to each player.
3. **`getPlayers()`** – Returns the list of managed players.
4. **`getHands()`** – Returns a map of each player to their current hand of cards.
5. **`getPlayerStatus()`** – Returns a map of each player to a Boolean indicating active/eliminated status.
6. **`removePlayerFromGame(Player p)`** – Removes a player from management when they lose.

Each test case verifies input validation, correct initialization, and proper updates to player hands and status.

---

## Method 1: `public void addPlayers(int n)`

### Step 1-3 Results

|        | Input                                       | Output / State Change                                                                  |
| ------ | ------------------------------------------- | -------------------------------------------------------------------------------------- |
| Step 1 | `n`                                         | none (creates and stores `n` players, each with empty hand and status = active)        |
| Step 2 | Cases for `n`: `<2`, `2–5`, `>5`            | Throws `InvalidNumberofPlayersException` for `<2` or `>5`; otherwise populates players |
| Step 3 | Combine boundary counts: `1`, `2`, `5`, `6` |                                                                                        |

### Step 4:

|             | System under test | Expected output / state transition                                                 | Implemented?         | Test name                                                         |
| ----------- | ----------------- | ---------------------------------------------------------------------------------- | -------------------- | ----------------------------------------------------------------- |
| Test Case 1 | `addPlayers(1)`   | throws `InvalidNumberofPlayersException`                                           | :white\_check\_mark: | `addPlayers_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| Test Case 2 | `addPlayers(6)`   | throws `InvalidNumberofPlayersException`                                           | :white\_check\_mark: | `addPlayers_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| Test Case 3 | `addPlayers(2)`   | internal list size == 2; each player's hand is empty; each player's status == true | :white\_check\_mark: | `addPlayers_validTwo_initializesTwoActivePlayers`                 |
| Test Case 4 | `addPlayers(5)`   | internal list size == 5; initialization behaves as above                           | :white\_check\_mark: | `addPlayers_validFive_initializesFiveActivePlayers`               |

---

## Method 2: `public void makePlayersDrawTheirInitialHands()`

### Step 1-3 Results

|        | Input                                                        | Output / State Change                                                                                                 |
| ------ | ------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------- |
| Step 1 | none                                                         | none (each player draws the predetermined starting number of cards from `MainDeck`)                                   |
| Step 2 | Preconditions: `addPlayers(n)` not called; `n` players exist | Throws `IllegalStateException("Players not initialized")`; otherwise, each player's hand size increases appropriately |
| Step 3 | Combine: before init; after init with 2, 5 players           |                                                                                                                       |

### Step 4:

|             | System under test               | Expected output / state transition                                  | Implemented?         | Test name                                                                 |
| ----------- | ------------------------------- | ------------------------------------------------------------------- | -------------------- |---------------------------------------------------------------------------|
| Test Case 1 | before `addPlayers(...)` called | throws `IllegalStateException("Players not initialized")`           | :white\_check\_mark: | `makePlayersDrawTheirInitialHands_beforeInit_throwsIllegalStateException` |
| Test Case 2 | after `addPlayers(2)`           | each of 2 players’ hand size == starting hand count (e.g., 7 cards) | :white\_check\_mark: | `makePlayersDrawTheirInitialHands_twoPlayers_dealsCorrectNumber`          |
| Test Case 3 | after `addPlayers(5)`           | each of 5 players’ hand size == starting hand count                 | :white\_check\_mark: | `makePlayersDrawTheirInitialHands_fivePlayers_dealsCorrectNumber`         |

---

## Method 3: `public List<Player> getPlayers()`

### Step 1-3 Results

|        | Input                         | Output                           |
| ------ | ----------------------------- | -------------------------------- |
| Step 1 | none                          | returns internal list of players |
| Step 2 | Before vs. after `addPlayers` | empty list or populated list     |
| Step 3 | Call in each case             |                                  |

### Step 4:

|             | System under test        | Expected output / state transition                | Implemented?         | Test name                                          |
| ----------- | ------------------------ | ------------------------------------------------- | -------------------- | -------------------------------------------------- |
| Test Case 1 | before `addPlayers(...)` | returns empty list                                | :white\_check\_mark: | `getPlayers_beforeInit_returnsEmptyList`           |
| Test Case 2 | after `addPlayers(3)`    | returns list of size 3 matching order of creation | :white\_check\_mark: | `getPlayers_afterAdd_returnsCreatedPlayersInOrder` |

---

## Method 4: `public Map<Player, List<Card>> getHands()`

### Step 1-3 Results

|        | Input                          | Output                                    |
| ------ | ------------------------------ | ----------------------------------------- |
| Step 1 | none                           | returns map of player → current hand list |
| Step 2 | Before dealing vs. after deals | empty-hand lists vs. non-empty            |
| Step 3 | Call in each state             |                                           |

### Step 4:

|             | System under test                          | Expected output / state transition          | Implemented?         | Test name                                             |
| ----------- |--------------------------------------------| ------------------------------------------- | -------------------- | ----------------------------------------------------- |
| Test Case 1 | before any hand dealt                      | each value in map is empty list             | :white\_check\_mark: | `getHands_beforeDeal_returnsEmptyHands`               |
| Test Case 2 | after `makePlayersDrawTheirInitialHands()` | each value list size == starting hand count | :white\_check\_mark: | `getHands_afterInitialDeal_returnsHandsOfCorrectSize` |

---

## Method 5: `public Map<Player, Boolean> getPlayerStatus()`

### Step 1-3 Results

|        | Input                     | Output                                     |
| ------ | ------------------------- | ------------------------------------------ |
| Step 1 | none                      | returns map of player → active status flag |
| Step 2 | Before vs. after removals | all `true` vs. some `false`                |
| Step 3 | Call in each state        |                                            |

### Step 4:

|             | System under test                     | Expected output / state transition                   | Implemented?         | Test name                                          |
| ----------- | ------------------------------------- | ---------------------------------------------------- | -------------------- | -------------------------------------------------- |
| Test Case 1 | before any removals                   | all values `true`                                    | :white\_check\_mark: | `getPlayerStatus_initialAllActive`                 |
| Test Case 2 | after `removePlayerFromGame(playerX)` | status of `playerX` == `false`; others remain `true` | :white\_check\_mark: | `getPlayerStatus_afterRemoval_marksPlayerInactive` |

---

## Method 6: `public void removePlayerFromGame(Player p)`

### Step 1-3 Results

|        | Input                                                                | Output / State Change                       |
| ------ | -------------------------------------------------------------------- | ------------------------------------------- |
| Step 1 | `Player p`                                                           | none (updates internal list and status map) |
| Step 2 | Cases: `p == null`; `p` not managed; `p` active; `p` already removed | Throws or removes and updates appropriately |
| Step 3 | Combine boundary inputs                                              |                                             |

### Step 4:

|             | System under test                            | Expected output / state transition                                                              | Implemented?         | Test name                                                           |
| ----------- | -------------------------------------------- | ----------------------------------------------------------------------------------------------- | -------------------- | ------------------------------------------------------------------- |
| Test Case 1 | `removePlayerFromGame(null)`                 | throws `NullPointerException`                                                                   | :white\_check\_mark: | `removePlayerFromGame_null_throwsNullPointerException`              |
| Test Case 2 | `removePlayerFromGame(playerNotManaged)`     | throws `IllegalArgumentException("Player not found")`                                           | :white\_check\_mark: | `removePlayerFromGame_unknownPlayer_throwsIllegalArgumentException` |
| Test Case 3 | `removePlayerFromGame(activePlayer)`         | removes `p` from internal player list; sets status `false`; `getHands()` no longer contains `p` | :white\_check\_mark: | `removePlayerFromGame_activePlayer_removesAndDeactivates`           |
| Test Case 4 | `removePlayerFromGame(playerAlreadyRemoved)` | throws `IllegalStateException("Player already removed")`                                        | :white\_check\_mark: | `removePlayerFromGame_alreadyRemoved_throwsIllegalStateException`   |
