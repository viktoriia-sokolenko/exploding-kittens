# Boundary‑Value Analysis (BVA) for `GameEngine`

#### Important Note

`GameEngine` orchestrates play around several collaborators.
In the constructor **all collaborators except
the `UserInterface` may be `null`‑checked**, while runtime methods chiefly guard against *null* arguments and delegate
to other domain objects.
Where a method relies on *text commands* or *deck size*, we treat those values as input
parameters for BVA purposes.

---

## Method under test: **Constructor**

`public GameEngine(TurnManager tm, PlayerManager pm, Deck deck, UserInterface ui, CardFactory factory)`

#### Note

Given permission from Professor Zhang that GameEngine constructor can have all these parameters since it can't work
without them.
<br>
She also said that we can treat Constructor as a slightly different method than regular methods.
As a result, it does NOT
violate Uncle Bob's three-parameter max rule.

### Step 1-3 Results

|        | Input 1                    | Input 2                     | Input 3             | Input 4                          | Input 5                    | Expected result / state change                                                      |
|--------|----------------------------|-----------------------------|---------------------|----------------------------------|----------------------------|-------------------------------------------------------------------------------------|
| Step 1 | TurnManager Object         | PlayerManager Object        | Deck Object         | User Interface Object (optional) | CardFactory Object         | fields assigned                                                                     |
| Step 2 | Cases                      | Cases                       | Cases               | Cases                            | Cases                      | Assigns necessary fields or result in an Exception                                  |
| Step 3 | `null`, TurnManager Object | `null`, PlayerManger Object | `null`, Deck Object | `null`, User Interface Object    | `null`, CardFactory Object | Any `null` among mandatory params →`NullPointerException`; otherwise object created |

### Step 4:

|             | System under test                             | Expected output / state transition                      | Implemented?       | Test name                                                      |
|-------------|-----------------------------------------------|---------------------------------------------------------|--------------------|----------------------------------------------------------------|
| Test Case 1 | `new GameEngine(null, pm, deck, ui, factory)` | *NullPointerException* "turnManager must not be null"   | :white_check_mark: | `constructor_withNullTurnManager_throwsNullPointerException`   |
| Test Case 2 | `new GameEngine(tm, null, deck, ui, factory)` | *NullPointerException* "playerManager must not be null" | :white_check_mark: | `constructor_withNullPlayerManager_throwsNullPointerException` |
| Test Case 3 | `new GameEngine(tm, pm, null, ui, factory)`   | *NullPointerException* "deck must not be null"          | :white_check_mark: | `constructor_withNullDeck_throwsNullPointerException`          |
| Test Case 4 | `new GameEngine(tm, pm, deck, ui, null)`      | *NullPointerException* "cardFactory must not be null"   | :white_check_mark: | `constructor_withNullCardFactory_throwsNullPointerException`   |
| Test Case 5 | `new GameEngine(tm, pm, deck, null, factory)` | **No exception** (UI optional)                          | :white_check_mark: | `constructor_withNullUI_allowsNullUI`                          |

---

## Method under test: **`public void playCard(Player player, Card card)`**

### Step 1-3 Results

|        | Input 1                                 | Input 2                          | Expected result / state change                                                                               |
|--------|-----------------------------------------|----------------------------------|--------------------------------------------------------------------------------------------------------------|
| Step 1 | player invoking action -> Player Object | card being played -> Card Object | delegates to card‑specific logic                                                                             |
| Step 2 | **Cases**                               | **Cases**                        | Exception or **Effect** executed and TurnManager updated                                                     |
| Step 3 | `null`, Player Object                   | `null`, Card Object              | Any `null` →`NullPointerException`; otherwise card removed from hand, effect executed, `TurnManager` updated |

### Step 4:

|             | System under test                  | Expected output / state transition                                       | Implemented?       | Test name                                            |
|-------------|------------------------------------|--------------------------------------------------------------------------|--------------------|------------------------------------------------------|
| Test Case 1 | `playCard(null, skipCard)`         | *NullPointerException* "Player cannot be null"                           | :white_check_mark: | `playCard_withNullPlayer_throwsNullPointerException` |
| Test Case 2 | `playCard(player, null)`           | *NullPointerException* "Card cannot be null"                             | :white_check_mark: | `playCard_withNullCard_throwsNullPointerException`   |
| Test Case 3 | `playCard(player owns SKIP, SKIP)` | `TurnManager.endTurnWithoutDraw()` invoked **once**; hand size decreased | :white_check_mark: | `playCard_playerHasCard_executesCardEffect`          |

---

## Method under test: **`public void startGame()`**

### Step 1-3 Results

|        | Input 1           | Input 2 (deck size)      | Input 3 (initial deal succeeds?) | Expected result / state change                            |
|--------|-------------------|--------------------------|----------------------------------|-----------------------------------------------------------|
| Step 1 | number of players | size of deck before deal | exceptions from collaborators    | game initialised & first turn started, or error displayed |
| Step 2 | Counts            | **Cases**                | **Cases**                        | **Effect** -> game is initialized                         |
| Step 3 | `2`,`5`           | sufficient, insufficient | normal, exception                | normal flow; error path displays via `UserInterface`      |

#### Note

Recall that the number of players acceptable in the game is between 2–5.
Thus, it's not possible to have one or five players,
which PlayerManager already handles successfully :)

### Step 4:

|             | System under test                                 | Expected output / state transition         | Implemented?       | Test name                                         |
|-------------|---------------------------------------------------|--------------------------------------------|--------------------|---------------------------------------------------|
| Test Case 1 | happy‑path mocks (2 players, deck ≥ cards needed) | completes without exception                | :white_check_mark: | `startGame_withValidSetup_initializesAndRunsGame` |
| Test Case 2 | collaborator throws runtime exception             | `UserInterface.displayError()` called once | no                 | `startGame_withException_displaysError`           | |                                            |                    |                                                   |

---

## Method under test: **`public void processCommand(String input, Player currentPlayer)`**

### Step 1-3 Results

#### Note

The BVA column for this is slightly different compared to our other formats (but it's still very similar).
This is because for GameEngine, since the command values can range,
we thought it would be clearer and easier to understand when each of the inputs in Step 3 is separated like this.
:)

|        | Input 1                                  | Input 2                         | Output / state change                   |
|--------|------------------------------------------|---------------------------------|-----------------------------------------|
| Step 1 | console command string (raw string)      | current player -> Player Object | varies by verb                          |
| Step 2 | Cases (acceptable string command values) | Player Instance                 | **Effect**                              |
| Step 3 | `null/""/blank`                          | valid Player                    | error message "Please enter a command…" |
|        | `play` (no index)                        | valid Player                    | error message "Usage: play <index>"     |
|        | `draw`                                   | valid Player                    | delegates to draw logic                 |
|        | `hand`                                   | valid Player                    | displays hand                           |
|        | `status`                                 | valid Player                    | displays status                         |
|        | `help`                                   | valid Player                    | prints help                             |
|        | `quit`                                   | valid Player                    | prints goodbye, ends game loop          |
|        | unknown verb                             | valid Player                    | prints unknown‑command error            |

### Step 4:

|             | System under test              | Expected output / state transition | Implemented? | Test name                                               |
|-------------|--------------------------------|------------------------------------|--------------|---------------------------------------------------------|
| Test Case 1 | `cmd = null`                   | error message via UI               | no           | `processCommand_withNullOrEmptyInput_displaysError`     |
| Test Case 2 | `cmd = "play"` (missing index) | error message via UI               | no           | `processCommand_withPlayCommand_callsHandlePlayCommand` |
| Test Case 3 | `cmd = "draw"` (deck ok)       | card drawn, turn ended             | no           | `processCommand_withDrawCommand_callsHandleDrawCommand` |
| Test Case 4 | `cmd = "status"`               | status information printed         | no           | `processCommand_withStatusCommand_displaysGameStatus`   |
| Test Case 5 | `cmd = "invalidcommand"`       | unknown‑command error              | no           | `processCommand_withUnknownCommand_displaysError`       |

---

## Method under test: **`public void checkWinCondition()`**

### Step 1-3 Results

#### Note

The BVA column for this is slightly different compared to our other formats (but it's still very similar).
This is because for GameEngine, since the command values can range,
we thought it would be clearer and easier to understand when each of the inputs in Step 3 is separated like this.
:)

|        | Input                  | Output / state change                   |
|--------|------------------------|-----------------------------------------|
| Step 1 | Number of Players Left | Game Over Text: console message or none |
| Step 2 | Count                  | **Effect**                              |
| Step 3 | `1`                    | prints "🎉 GAME OVER! …"                |
|        | `0`                    | prints "💥 GAME OVER! …"                |
|        | `2+`                   | continues silently                      |

### Step 4:

|             | System under test                     | Expected output / state transition | Implemented? | Test name                                                 |
|-------------|---------------------------------------|------------------------------------|--------------|-----------------------------------------------------------|
| Test Case 1 | One Player Left                       | victory message printed            | no           | `checkWinCondition_withOnePlayerLeft_endsGame`            |
| Test Case 2 | No Players Left                       | everyone‑exploded message printed  | no           | `checkWinCondition_withNoPlayersLeft_endsGame`            |
| Test Case 3 | Three Players Left (Multiple Players) | no game‑over message               | no           | `checkWinCondition_withMultiplePlayersLeft_continuesGame` |

---

## Method under test: **`public static List<Card> createInitialDeck(CardFactory, int playerCount)`**

### Step 1-3 Results

|        | Input 1                    | Input 2                  | Output (generated deck)                                                                                       |
|--------|----------------------------|--------------------------|---------------------------------------------------------------------------------------------------------------|
| Step 1 | Player CountL integer 2-…5 | CardFactory Object       | `List<Card>` sized appropriately                                                                              |
| Step 2 | **Cases**                  | CardFactory Instance     | Collection: **Deck composition**                                                                              |
| Step 3 | `2`,`3`,`4`,`5`            | null, CardFactory Object | contains attack / skip / favor … cards; exactly `playerCoun–1` exploding kittens & `playerCount` defuse cards |

#### Note
Not possible to create Initial Deck to be empty

### Step 4:
|             | System under test                                                                       | Expected output / state transition                                                                                                   | Implemented? | Test name                                                         |
|-------------|-----------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|--------------|-------------------------------------------------------------------|
| Test Case 1 | `createInitialDeck(playerCount, factory)`, valid CardFactory instance, playerCount == 2 | Deck includes correct count of Attack, Skip, Favor, etc.; contains `playerCount` Defuse and `playerCount - 1` Exploding Kitten cards | no           | `createInitialDeck_withTwoPlayersAndFactory_createsCorrectDeck`   |
| Test Case 1 | `createInitialDeck(playerCount, factory)`, valid CardFactory instance, playerCount == 3 | Deck includes correct count of Attack, Skip, Favor, etc.; contains `playerCount` Defuse and `playerCount - 1` Exploding Kitten cards | no           | `createInitialDeck_withThreePlayersAndFactory_createsCorrectDeck` |
| Test Case 1 | `createInitialDeck(playerCount, factory)`, valid CardFactory instance, playerCount == 4 | Deck includes correct count of Attack, Skip, Favor, etc.; contains `playerCount` Defuse and `playerCount - 1` Exploding Kitten cards | no           | `createInitialDeck_withFourPlayersAndFactory_createsCorrectDeck`  |
| Test Case 1 | `createInitialDeck(playerCount, factory)`, valid CardFactory instance, playerCount == 5 | Deck includes correct count of Attack, Skip, Favor, etc.; contains `playerCount` Defuse and `playerCount - 1` Exploding Kitten cards | no           | `createInitialDeck_withFinvePlayersAndFactory_createsCorrectDeck` |
| Test Case 2 | `createInitialDeck(1, factory)`                                                         | Throws `IllegalArgumentException ("Player count must be at least 2")`                                                                | no           | `createInitialDeck_withPlayerCountBelowMinimum_throwsException`   |
| Test Case 6 | `createInitialDeck(3, null)`, cardFactoru == null, playerCount == 3                     | Throws `NullPointerException(Card Factory cannot be Null)`                                                                           | no           | `createInitialDeck_withNullFactory_throwsNullPointerException`    |