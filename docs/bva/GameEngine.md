# Boundary‑Value Analysis (BVA) for `GameEngine`

#### Important Note

`GameEngine` orchestrates play around several collaborators.  In the constructor **all collaborators except the `UserInterface` may be `null`‑checked**, while runtime methods chiefly guard against *null* arguments and delegate to other domain objects.  Where a method relies on *text commands* or *deck size*, we treat those values as input parameters for BVA purposes.

---

## Method under test: **Constructor**

`public GameEngine(TurnManager tm, PlayerManager pm, Deck deck, UserInterface ui, CardFactory factory)`

### Step-1‑3-Results

|            | **turnManager**       | **playerManager**     | **deck**              | **userInterface**                | **cardFactory**       | Expected result / state change                                                      |
|------------| --------------------- | --------------------- | --------------------- | -------------------------------- | --------------------- | ----------------------------------------------------------------------------------- |
| **Step-1** | collaborator instance | collaborator instance | collaborator instance | collaborator instance (optional) | collaborator instance | fields assigned                                                                     |
| **Step-2** | **Cases**             | **Cases**             | **Cases**             | **Cases**                        | **Cases**             | **Effect**                                                                          |
| **Step-3** | `null`, valid         | `null`, valid         | `null`, valid         | `null`, valid                    | `null`, valid         | Any `null` among mandatory params →`NullPointerException`; otherwise object created |

### Step-4

| # | System under test                             | Expected output                                         | Implemented?                  | JUnitest name                                                 |
| - | --------------------------------------------- | ------------------------------------------------------- |-------------------------------|---------------------------------------------------------------|
| 1 | `new GameEngine(null, pm, deck, ui, factory)` | *NullPointerException* "turnManager must not be null"   | :white_check_mark:            | `constructor_withNullTurnManager_throwsNullPointerException`  |
| 2 | `new GameEngine(tm, null, deck, ui, factory)` | *NullPointerException* "playerManager must not be null" | :white_check_mark:            | `constructor_withNullPlayerManager_throwsNullPointerException` |
| 3 | `new GameEngine(tm, pm, null, ui, factory)`   | *NullPointerException* "deck must not be null"          | :white_check_mark:            | `constructor_withNullDeck_throwsNullPointerException`         |
| 4 | `new GameEngine(tm, pm, deck, ui, null)`      | *NullPointerException* "cardFactory must not be null"   | :white_check_mark:            | `constructor_withNullCardFactory_throwsNullPointerException`  |
| 5 | `new GameEngine(tm, pm, deck, null, factory)` | **No exception** (UI optional)                          | :white_check_mark:            | `constructor_withNullUI_allowsNullUI`                         |

---

## Method under test: **`playCard(Player player, Card card)`**

### Step-1‑3-Results

|            | **player**             | **card**          | Expected result / state change                                                                               |
|------------| ---------------------- | ----------------- | ------------------------------------------------------------------------------------------------------------ |
| **Step-1** | player invoking action | card being played | delegates to card‑specific logic                                                                             |
| **Step-2** | **Cases**              | **Cases**         | **Effect**                                                                                                   |
| **Step-3** | `null`, valid          | `null`, valid     | Any `null` →`NullPointerException`; otherwise card removed from hand, effect executed, `TurnManager` updated |

### Step-4

| # | System under test                            | Expected output                                                          | Implemented?       | JUnitest name                                      |
| - | -------------------------------------------- | ------------------------------------------------------------------------ |--------------------|----------------------------------------------------|
| 1 | `playCard(null, skipCard)`                   | *NullPointerException* "Player cannot be null"                           | :white_check_mark: | `playCard_withNullPlayer_throwsNullPointerException` |
| 2 | `playCard(player, null)`                     | *NullPointerException* "Card cannot be null"                             | :white_check_mark: | `playCard_withNullCard_throwsNullPointerException` |
| 3 | `playCard(player owns SKIP, SKIP)`           | `TurnManager.endTurnWithoutDraw()` invoked **once**; hand size decreased | :white_check_mark: | `playCard_playerHasCard_executesCardEffect`        |

---

## Method under test: **`startGame()`**

### Step-1‑3-Results

|            | Inpu1 (player count) | Inpu2 (deck size)       | Inpu3 (initial deal succeeds?) | Output / side‑effects                                     |
|------------|----------------------|-------------------------|--------------------------------| --------------------------------------------------------- |
| **Step-1** | number of players    | size of deck before deal | exceptions from collaborators  | game initialised & first turn started, or error displayed |
| **Step-2** | **Cases**            | **Cases**               | **Cases**                      | **Effect**                                                |
| **Step-3** | `2`, `5`             | sufficient, insufficient | normal, exception              | normal flow; error path displays via `UserInterface`      |

### Step-4

| # | System under test                                 | Expected output                            | Implemented?       | JUnitest name                                    |
| - |---------------------------------------------------| ------------------------------------------ |--------------------|--------------------------------------------------|
| 1 | happy‑path mocks (2 players, deck ≥ cards needed) | completes without exception                | :white_check_mark: | `startGame_withValidSetup_initializesAndRunsGame` |
| 2 | collaborator throws runtime exception             | `UserInterface.displayError()` called once | no                 | `startGame_withException_displaysError`          |

---

## Method under test: **`processCommand(String cmd, Player actor)`** *(private – tested via reflection)*

### Step-1‑3-Results

|            | **cmd** (raw string) | **actor**      | Output / state change                   |
|------------| -------------------- | -------------- | --------------------------------------- |
| **Step-1** | console command      | current player | varies by verb                          |
| **Step-2** | **Cases**            | **Cases**      | **Effect**                              |
| **Step-3** | `null/""/blank`      | valid          | error message "Please enter a command…" |
|            | `play` (no index)    | valid          | error message "Usage: play <index>"     |
|            | `draw`               | valid          | delegates to draw logic                 |
|            | `hand`               | valid          | displays hand                           |
|            | `status`             | valid          | displays status                         |
|            | `help`               | valid          | prints help                             |
|            | `quit`               | valid          | prints goodbye, ends game loop          |
|            | unknown verb         | valid          | prints unknown‑command error            |

### Step-4 *(subset – tests in place / planned)*

| # | Scenario                       | Expected output            | Implemented? | JUnitest name                                          |
| - | ------------------------------ | -------------------------- |----------|--------------------------------------------------------|
| 1 | `cmd = null`                   | error message via UI       | no       | `processCommand_withNullOrEmptyInput_displaysError`    |
| 2 | `cmd = "play"` (missing index) | error message via UI       | no       | `processCommand_withPlayCommand_callsHandlePlayCommand` |
| 3 | `cmd = "draw"` (deck ok)       | card drawn, turn ended     | no       | `processCommand_withDrawCommand_callsHandleDrawCommand` |
| 4 | `cmd = "status"`               | status information printed | no       | `processCommand_withStatusCommand_displaysGameStatus`  |
| 5 | `cmd = "invalidcommand"`       | unknown‑command error      | no       | `processCommand_withUnknownCommand_displaysError`      |

---

## Method under test: **`checkWinCondition()`**

### Step-1‑3 Results

|            | Input: Active players) | Output (game over text?) |
|------------|------------------------| ------------------------ |
| **Step-1** | integer 0…n            | console message or none  |
| **Step-2** | **Cases**              | **Effect**               |
| **Step-3** | `1`                    | prints "🎉 GAME OVER! …" |
|            | `0`                    | prints "💥 GAME OVER! …" |
|            | `2+`                   | continues silently       |

### Step-4

| # | System under test | Expected output                   | Implemented? | JUnitest name                                            |
| - |------------------| --------------------------------- |--------------|----------------------------------------------------------|
| 1 | activePlayers=-1 | victory message printed           | no           | `checkWinCondition_withOnePlayerLeft_endsGame`           |
| 2 | activePlayers=-0 | everyone‑exploded message printed | no           | `checkWinCondition_withNoPlayersLeft_endsGame`           |
| 3 | activePlayers=-3 | no game‑over message              | no           | `checkWinCondition_withMultiplePlayersLeft_continuesGame` |

---

## Method under test: **`createInitialDeck(CardFactory, int playerCount)`** *(static)*

### Step-1‑3 Results

|            | **playerCount** | Output (generated deck)                                                                                       |
|------------|-----------------|---------------------------------------------------------------------------------------------------------------|
| **Step-1** | integer 2-…5    | `List<Card>` sized appropriately                                                                              |
| **Step-2** | **Cases**       | **Deck composition**                                                                                          |
| **Step-3** | `2`,`3`,`4`,`5` | contains attack / skip / favor … cards; exactly `playerCoun–1` exploding kittens & `playerCount` defuse cards |

### Step-4

| # | System under test                 | Expected output                            | Implemented? | JUnitest name                                              |
| - | --------------------------------- | ------------------------------------------ |-------------|------------------------------------------------------------|
| 1 | `playerCount` parameterised 2‑5   | deck non‑empty and includes key card types | no          | `createInitialDeck_withValidPlayerCount_createsCorrectDeck` |
| 2 | `playerCount = 1` (below minimum) | *IllegalArgumentException*                 | no          | *planned*                                                  |
