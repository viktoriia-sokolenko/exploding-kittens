# BVA Analysis for **PlayerManager**

#### Important Note

The `PlayerManager` oversees player setup, hand allocation via an injected `Deck`, and active status tracking.  Its main responsibilities include:

0. **Constructor** – Accepts a non-null `Deck`.
1. **`addPlayers(int n)`** – Creates `n` new `Player` instances, initializes their hands and status.
2. **`makePlayersDrawTheirInitialHands()`** – Deals the starting hand to each player by calling `deck.draw()`.
3. **`getPlayers()`** – Returns the list of managed players.
4. **`getHands()`** – Returns a map of each player to their current hand of cards.
5. **`getPlayerStatus()`** – Returns a map of each player to a Boolean indicating active/eliminated status.
6. **`removePlayerFromGame(Player p)`** – Removes a player from management when they lose.

---

## Method 0: **Constructor**

| Test Case     | System under test              | Expected behavior                                         | Implemented? | Test name                                  |
| ------------- | ------------------------------ | --------------------------------------------------------- |------------| ------------------------------------------ |
| Test Case 0.1 | `new PlayerManager(null)`      | throws `NullPointerException("Deck cannot be null")`      | no         | `ctor_nullDeck_throwsNullPointerException` |
| Test Case 0.2 | `new PlayerManager(validDeck)` | stores deck reference; `players` and `status` start empty | no         | `ctor_validDeck_initializesEmptyManager`   |

---

## Method 1: `public void addPlayers(int numberOfPlayers)`

### Step 1–3 Results

|        | Input                                | Output / State Change                                                                       |
| ------ |--------------------------------------|---------------------------------------------------------------------------------------------|
| Step 1 | `numberOfPlayers`                    | none (creates and stores `numberOfPlayers` players, each with empty hand and status = true) |
| Step 2 | `numberOfPlayers`: `<2`, `2–5`, `>5` | Throws `InvalidNumberofPlayersException` for `<2` or `>5`; otherwise populates              |
| Step 3 | boundary values                      | `1`, `2`, `5`, `6`                                                                          |

### Step 4

| Test Case   | System under test | Expected behavior                                           | Implemented? | Test name                                                         |
| ----------- | ----------------- | ----------------------------------------------------------- |----------| ----------------------------------------------------------------- |
| Test Case 1 | `addPlayers(1)`   | throws `InvalidNumberofPlayersException`                    | no       | `addPlayers_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| Test Case 2 | `addPlayers(6)`   | throws `InvalidNumberofPlayersException`                    | no       | `addPlayers_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| Test Case 3 | `addPlayers(2)`   | `players.size()==2`; each hand empty; each status == `true` | no       | `addPlayers_validTwo_initializesTwoActivePlayers`                 |
| Test Case 4 | `addPlayers(5)`   | `players.size()==5`; initialization behaves as above        | no       | `addPlayers_validFive_initializesFiveActivePlayers`               |

---

## Method 2: `public void makePlayersDrawTheirInitialHands()`

### Step 1–3 Results

|        | Input                                                                                    | Output / State Change                                                                                                      |
| ------ |------------------------------------------------------------------------------------------| -------------------------------------------------------------------------------------------------------------------------- |
| Step 1 | none                                                                                     | none (each player draws `STARTING_HAND_SIZE` cards via `deck.draw()`)                                                      |
| Step 2 | Preconditions: `addPlayers(numberOfPlayers)` not called; `numberOfPlayers` players exist | Throws `IllegalStateException("Players not initialized")`; otherwise, each player's hand increases by `STARTING_HAND_SIZE` |
| Step 3 | Combine: before init; after init with 2, 5 players                                       |                                                                                                                            |

### Step 4

| Test Case       | System under test                                  | Expected behavior                                                                                   | Implemented? | Test name                                                                 |
| --------------- | -------------------------------------------------- | --------------------------------------------------------------------------------------------------- |-----------| ------------------------------------------------------------------------- |
| Test Case 1     | before `addPlayers(...)` called                    | throws `IllegalStateException("Players not initialized")`                                           | no        | `makePlayersDrawTheirInitialHands_beforeInit_throwsIllegalStateException` |
| **Test Case 2** | deck has fewer than `n * STARTING_HAND_SIZE` cards | first `NoSuchElementException` from `deck.draw()` propagates; no player's hand is modified          |           | `makePlayersDraw_insufficientDeck_throwsAndLeavesState`                   |
| Test Case 3     | after `addPlayers(2)` with sufficient deck         | each of 2 players’ hand size == `STARTING_HAND_SIZE`; deck size reduced by `2 * STARTING_HAND_SIZE` | no        | `makePlayersDraw_twoPlayers_dealsCorrectNumber`                           |
| Test Case 4     | after `addPlayers(5)` with sufficient deck         | each of 5 players’ hand size == `STARTING_HAND_SIZE`; deck size reduced by `5 * STARTING_HAND_SIZE` | no        | `makePlayersDraw_fivePlayers_dealsCorrectNumber`                          |

---

## Method 3: `public List<Player> getPlayers()`

### Step 1–3 Results

|        | Input                         | Output                           |
| ------ | ----------------------------- | -------------------------------- |
| Step 1 | none                          | returns internal list of players |
| Step 2 | Before vs. after `addPlayers` | empty list or populated list     |
| Step 3 | —                             | —                                |

### Step 4

| Test Case   | System under test        | Expected behavior                              | Implemented? | Test name                                          |
| ----------- | ------------------------ | ---------------------------------------------- |------------| -------------------------------------------------- |
| Test Case 1 | before `addPlayers(...)` | returns empty list                             | no         | `getPlayers_beforeInit_returnsEmptyList`           |
| Test Case 2 | after `addPlayers(3)`    | returns list of size 3 matching creation order | no         | `getPlayers_afterAdd_returnsCreatedPlayersInOrder` |

---

## Method 4: `public Map<Player, List<Card>> getHands()`

### Step 1–3 Results

|        | Input                          | Output                                     |
| ------ | ------------------------------ | ------------------------------------------ |
| Step 1 | none                           | returns map of player → current hand lists |
| Step 2 | Before dealing vs. after deals | empty-hand lists vs. non-empty             |
| Step 3 | —                              | —                                          |

### Step 4

| Test Case   | System under test                          | Expected output / state transition          | Implemented? | Test name                                             |
| ----------- | ------------------------------------------ | ------------------------------------------- |------------| ----------------------------------------------------- |
| Test Case 1 | before any hand dealt                      | each value in map is empty list             | no         | `getHands_beforeDeal_returnsEmptyHands`               |
| Test Case 2 | after `makePlayersDrawTheirInitialHands()` | each hand list size == `STARTING_HAND_SIZE` | no         | `getHands_afterInitialDeal_returnsHandsOfCorrectSize` |

---

## Method 5: `public Map<Player, Boolean> getPlayerStatus()`

### Step 1–3 Results

|        | Input                     | Output                                |
| ------ | ------------------------- | ------------------------------------- |
| Step 1 | none                      | returns map of player → active status |
| Step 2 | Before vs. after removals | all `true` vs. some `false`           |
| Step 3 | —                         | —                                     |

### Step 4

| Test Case   | System under test                     | Expected output                               | Implemented? | Test name                                          |
| ----------- | ------------------------------------- | --------------------------------------------- |------------| -------------------------------------------------- |
| Test Case 1 | before any removals                   | all values `true`                             | no         | `getPlayerStatus_initialAllActive`                 |
| Test Case 2 | after `removePlayerFromGame(playerX)` | status of `playerX` == `false`; others `true` | no         | `getPlayerStatus_afterRemoval_marksPlayerInactive` |

---

## Method 6: `public void removePlayerFromGame(Player p)`

### Step 1–3 Results

|        | Input                                             | Output / State Change                          |
| ------ | ------------------------------------------------- | ---------------------------------------------- |
| Step 1 | `Player p`                                        | none (updates `players` list and `status` map) |
| Step 2 | `p == null`; not managed; already removed; active | Throws appropriate NPE, IAE, or ISE            |
| Step 3 | combine boundary and normal cases                 | —                                              |

### Step 4

| Test Case   | System under test                            | Expected output                                                                  | Implemented? | Test name                                                           |
| ----------- | -------------------------------------------- | -------------------------------------------------------------------------------- |----------| ------------------------------------------------------------------- |
| Test Case 1 | `removePlayerFromGame(null)`                 | throws `NullPointerException`                                                    | no       | `removePlayerFromGame_null_throwsNullPointerException`              |
| Test Case 2 | `removePlayerFromGame(playerNotManaged)`     | throws `IllegalArgumentException("Player not found")`                            | no       | `removePlayerFromGame_unknownPlayer_throwsIllegalArgumentException` |
| Test Case 3 | `removePlayerFromGame(activePlayer)`         | removes from `players`; sets status `false`; `getHands()` no longer includes `p` | no       | `removePlayerFromGame_activePlayer_removesAndDeactivates`           |
| Test Case 4 | `removePlayerFromGame(playerAlreadyRemoved)` | throws `IllegalStateException("Player already removed")`                         | no       | `removePlayerFromGame_alreadyRemoved_throwsIllegalStateException`   |
