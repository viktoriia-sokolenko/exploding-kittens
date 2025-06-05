# BVA Analysis for **PlayerManager**

#### Important Note

The `PlayerManager` oversees player setup, hand allocation via an injected `Deck`, and tracking active/eliminated status. Its main methods:

0. **Constructor** – takes a non-null `Deck`.
1. **`addPlayers(int n)`** – creates `n` new `Player` instances (status = active, empty hands).
2. **`makePlayersDrawTheirInitialHands()`** – deals each player `STARTING_HAND_SIZE` cards via `deck.draw()`.
3. **`getPlayers()`** – returns the list of players.
4. **`getHands()`** – returns a map from each player to their current hand.
5. **`getPlayerStatus()`** – returns a map from each player to a Boolean (active/eliminated).
6. **`removePlayerFromGame(Player p)`** – marks a player eliminated and removes them from the hands map.

---

## Method 0: **Constructor**

### Step 1–3 Results

|            | Input                                       | Output / State Change                                                            |
| ---------- | ------------------------------------------- | -------------------------------------------------------------------------------- |
| **Step 1** | `deck`                                      | none (stores `deck`; initializes empty `players` and `status` maps)              |
| **Step 2** | `deck == null`<br>`deck != null`            | throws `NullPointerException("Deck cannot be null")` if null; otherwise proceeds |
| **Step 3** | 1. `deck = null` <br> 2. `deck = validDeck` | **1** → throws NPE<br>**2** → stores `deck`; `players` & `status` remain empty   |

### Step 4

| Test Case | System under test              | Expected behavior                                    | Implemented? | Test name                                  |
| --------- | ------------------------------ | ---------------------------------------------------- | ------------ | ------------------------------------------ |
| 0.1       | `new PlayerManager(null)`      | throws `NullPointerException("Deck cannot be null")` | no           | `ctor_nullDeck_throwsNullPointerException` |
| 0.2       | `new PlayerManager(validDeck)` | stores deck ref; `players` & `status` start empty    | no           | `ctor_validDeck_initializesEmptyManager`   |

---

## Method 1: `public void addPlayers(int numberOfPlayers)`

### Step 1–3 Results

|            | Input                                      | Output / State Change                                                                                                                                                                       |
| ---------- | ------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Step 1** | `numberOfPlayers`                          | none (creates & stores `n` Player objects; each `hand = []`, `status = true`)                                                                                                               |
| **Step 2** | `<2`; `2–5`; `>5`                          | `<2` or `>5`: throws `InvalidNumberofPlayersException`; otherwise populates `players`/`status` maps                                                                                         |
| **Step 3** | 1. `1` <br> 2. `2` <br> 3. `5` <br> 4. `6` | **1** → throws `InvalidNumberofPlayersException`<br>**2** → creates 2 players with empty hands/status=true<br>**5** → creates 5 players<br>**6** → throws `InvalidNumberofPlayersException` |

### Step 4

| Test Case | System under test | Expected behavior                                               | Implemented? | Test name                                                         |
| --------- | ----------------- | --------------------------------------------------------------- | ------------ | ----------------------------------------------------------------- |
| 1         | `addPlayers(1)`   | throws `InvalidNumberofPlayersException`                        | no           | `addPlayers_tooFewPlayers_throwsInvalidNumberofPlayersException`  |
| 2         | `addPlayers(6)`   | throws `InvalidNumberofPlayersException`                        | no           | `addPlayers_tooManyPlayers_throwsInvalidNumberofPlayersException` |
| 3         | `addPlayers(2)`   | `players.size()==2`; each `hand.isEmpty()`; each `status==true` | no           | `addPlayers_validTwo_initializesTwoActivePlayers`                 |
| 4         | `addPlayers(5)`   | `players.size()==5`; same initialization as above               | no           | `addPlayers_validFive_initializesFiveActivePlayers`               |

---

## Method 2: `public void makePlayersDrawTheirInitialHands()`

### Step 1–3 Results

|            | Input                                                                                                                                                                                 | Output / State Change                                                                                                                                                                                |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Step 1** | none                                                                                                                                                                                  | none (for each player, calls `deck.draw()` `HAND_SIZE` times)                                                                                                                                        |
| **Step 2** | before `addPlayers(...)`; after valid `addPlayers`                                                                                                                                    | before init: throws `IllegalStateException("Players not initialized")`; after init: each hand grows, deck size decreases by `n * HAND_SIZE`                                                          |
| **Step 3** | 1. before init <br> 2. after `addPlayers(2)` with sufficient deck <br> 3. after `addPlayers(5)` with sufficient deck <br> 4. after `addPlayers(2)` but deck has `< 2×HAND_SIZE` cards | **1** → throws ISE<br>**2** → each of 2 hands size=`HAND_SIZE`; deck size reduced by `2×HAND_SIZE`<br>**3** → 5 hands dealt<br>**4** → first draw throws `NoSuchElementException`; no hands modified |

### Step 4

| Test Case | System under test                                | Expected behavior                                                               | Implemented? | Test name                                                                 |
| --------- | ------------------------------------------------ | ------------------------------------------------------------------------------- | ------------ | ------------------------------------------------------------------------- |
| 1         | before any `addPlayers(...)`                     | throws `IllegalStateException("Players not initialized")`                       | no           | `makePlayersDrawTheirInitialHands_beforeInit_throwsIllegalStateException` |
| 2         | after `addPlayers(2)`; deck size < `2*HAND_SIZE` | first `deck.draw()` throws `NoSuchElementException`; no hands are modified      | no           | `makePlayersDraw_insufficientDeck_throwsAndLeavesState`                   |
| 3         | after `addPlayers(2)`; deck size ≥ `2*HAND_SIZE` | each of 2 players’ hand size == `HAND_SIZE`; deck size reduced by `2*HAND_SIZE` | no           | `makePlayersDraw_twoPlayers_dealsCorrectNumber`                           |
| 4         | after `addPlayers(5)`; deck size ≥ `5*HAND_SIZE` | each of 5 players’ hand size == `HAND_SIZE`; deck size reduced by `5*HAND_SIZE` | no           | `makePlayersDraw_fivePlayers_dealsCorrectNumber`                          |

---

## Method 3: `public List<Player> getPlayers()`

### Step 1–3 Results

|            | Input                                                    | Output / State Change                                                       |
| ---------- | -------------------------------------------------------- | --------------------------------------------------------------------------- |
| **Step 1** | none                                                     | returns internal `List<Player>`                                             |
| **Step 2** | before vs. after `addPlayers`                            | empty list vs. populated list                                               |
| **Step 3** | 1. before any `addPlayers` <br> 2. after `addPlayers(3)` | **1** → returns `[]`<br>**2** → returns list of 3 players in creation order |

### Step 4

| Test Case | System under test        | Expected behavior                        | Implemented? | Test name                                          |
| --------- | ------------------------ | ---------------------------------------- | ------------ | -------------------------------------------------- |
| 1         | before `addPlayers(...)` | returns empty list                       | no           | `getPlayers_beforeInit_returnsEmptyList`           |
| 2         | after `addPlayers(3)`    | returns list of size 3 in creation order | no           | `getPlayers_afterAdd_returnsCreatedPlayersInOrder` |

---

## Method 4: `public Map<Player,List<Card>> getHands()`

### Step 1–3 Results

|            | Input                                                                 | Output / State Change                                                |
| ---------- | --------------------------------------------------------------------- | -------------------------------------------------------------------- |
| **Step 1** | none                                                                  | returns `Map<Player,List<Card>>`                                     |
| **Step 2** | before vs. after deal                                                 | each hand empty vs. non-empty                                        |
| **Step 3** | 1. before any deal <br> 2. after `makePlayersDrawTheirInitialHands()` | **1** → map values all `[]`<br>**2** → each list size == `HAND_SIZE` |

### Step 4

| Test Case | System under test  | Expected output                    | Implemented? | Test name                                             |
| --------- | ------------------ | ---------------------------------- | ------------ | ----------------------------------------------------- |
| 1         | before any deal    | each value list is empty           | no           | `getHands_beforeDeal_returnsEmptyHands`               |
| 2         | after initial deal | each hand list size == `HAND_SIZE` | no           | `getHands_afterInitialDeal_returnsHandsOfCorrectSize` |

---

## Method 5: `public Map<Player,Boolean> getPlayerStatus()`

### Step 1–3 Results

|            | Input                                                               | Output / State Change                                            |
| ---------- | ------------------------------------------------------------------- | ---------------------------------------------------------------- |
| **Step 1** | none                                                                | returns `Map<Player,Boolean>`                                    |
| **Step 2** | before vs. after removal                                            | all `true` vs. some `false`                                      |
| **Step 3** | 1. before any removal <br> 2. after `removePlayerFromGame(playerX)` | **1** → map all `true`<br>**2** → `playerX:false`, others `true` |

### Step 4

| Test Case | System under test                     | Expected output                            | Implemented? | Test name                                          |
| --------- | ------------------------------------- | ------------------------------------------ | ------------ | -------------------------------------------------- |
| 1         | before any removal                    | all statuses == `true`                     | no           | `getPlayerStatus_initialAllActive`                 |
| 2         | after `removePlayerFromGame(playerX)` | `playerX` status == `false`; others `true` | no           | `getPlayerStatus_afterRemoval_marksPlayerInactive` |

---

## Method 6: `public void removePlayerFromGame(Player p)`

### Step 1–3 Results

|            | Input                                                                                                      | Output / State Change                                                                                                                                                                                            |
| ---------- | ---------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Step 1** | `Player p`                                                                                                 | none (updates `players` list and `status` map)                                                                                                                                                                   |
| **Step 2** | `p == null`; not managed; active; already removed                                                          | throws NPE, IAE, or ISE as appropriate; otherwise removes and deactivates                                                                                                                                        |
| **Step 3** | 1. `p = null` <br> 2. `p = playerNotManaged` <br> 3. `p = activePlayer` <br> 4. `p = alreadyRemovedPlayer` | **1** → throws NPE<br>**2** → throws IAE("Player not found")<br>**3** → removes from `players`; sets `status=false`; `getHands()` no longer includes that player<br>**4** → throws ISE("Player already removed") |

### Step 4

| Test Case | System under test                            | Expected output                                                                          | Implemented? | Test name                                                           |
| --------- | -------------------------------------------- | ---------------------------------------------------------------------------------------- | ------------ | ------------------------------------------------------------------- |
| 1         | `removePlayerFromGame(null)`                 | throws `NullPointerException`                                                            | no           | `removePlayerFromGame_null_throwsNullPointerException`              |
| 2         | `removePlayerFromGame(playerNotManaged)`     | throws `IllegalArgumentException("Player not found")`                                    | no           | `removePlayerFromGame_unknownPlayer_throwsIllegalArgumentException` |
| 3         | `removePlayerFromGame(activePlayer)`         | removes from `players`; sets status `false`; `getHands()` no longer includes that player | no           | `removePlayerFromGame_activePlayer_removesAndDeactivates`           |
| 4         | `removePlayerFromGame(playerAlreadyRemoved)` | throws `IllegalStateException("Player already removed")`                                 | no           | `removePlayerFromGame_alreadyRemoved_throwsIllegalStateException`   |