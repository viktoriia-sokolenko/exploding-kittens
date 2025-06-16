# BVA Analysis for GameEngine

#### Important Note

`GameEngine` orchestrates play around several collaborators.
In the constructor **all collaborators except
the `UserInterface` may be `null`‑checked**, while runtime methods chiefly guard against *null* arguments and delegate
to other domain objects.
Where a method relies on *text commands* or *deck size*, we treat those values as input
parameters for BVA purposes.

---

## Method 1:

`public GameEngine(TurnManager, PlayerManager, Deck, UserInterface, CardFactory, SecureRandom, LocaleManager)`

### Step 1-3 Results

|        | Input                                   | Output                  |
|--------|-----------------------------------------|-------------------------|
| Step 1 | Required manager objects and components | Initialized GameEngine  |
| Step 2 | Objects                                 | GameEngine or Exception |
| Step 3 | null/valid for each required component  | Success or NPE          |

### Step 4:

|             | System under test    | Expected output / state transition | Implemented?       | Test name                                                    |
|-------------|----------------------|------------------------------------|--------------------|--------------------------------------------------------------|
| Test Case 1 | turnManager = null   | NullPointerException               | :white_check_mark: | constructor_withNullTurnManager_throwsNullPointerException   |
| Test Case 2 | playerManager = null | NullPointerException               | :white_check_mark: | constructor_withNullPlayerManager_throwsNullPointerException |
| Test Case 3 | deck = null          | NullPointerException               | :white_check_mark: | constructor_withNullDeck_throwsNullPointerException          |
| Test Case 4 | userInterface = null | Allows null UI                     | :white_check_mark: | constructor_withNullUI_allowsNullUI                          |
| Test Case 5 | cardFactory = null   | NullPointerException               | :white_check_mark: | constructor_withNullCardFactory_throwsNullPointerException   |

## Method 2: `public void playCard(Player player, Card card)`

### Step 1-3 Results

|        | Input 1    | Input 2    | Output               |
|--------|------------|------------|----------------------|
| Step 1 | Player     | Card       | Card effect executed |
| Step 2 | Object     | Object     | Boolean or Exception |
| Step 3 | null/valid | null/valid | Success or NPE       |

### Step 4:

|             | System under test     | Expected output / state transition | Implemented?       | Test name                                          |
|-------------|-----------------------|------------------------------------|--------------------|----------------------------------------------------|
| Test Case 1 | player = null         | NullPointerException               | :white_check_mark: | playCard_withNullPlayer_throwsNullPointerException |
| Test Case 2 | card = null           | NullPointerException               | :white_check_mark: | playCard_withNullCard_throwsNullPointerException   |
| Test Case 3 | valid player and card | Card effect executed               | :white_check_mark: | playCard_playerHasCard_executesCardEffect          |

## Method 3: `public void showAvailableCardTypes(Player player)`

### Step 1-3 Results

|        | Input 1            | Input 2    | Output                               |
|--------|--------------------|------------|--------------------------------------|
| Step 1 | Player             | Hand       | Formatted display of available cards |
| Step 2 | Object             | Object     | String display                       |
| Step 3 | null, empty, valid | valid Hand | Nothing/formatted output/exception   |

Note: Hand and Player goes together

### Step 4:

|             | System under test                | Expected output / state transition | Implemented?       | Test name                                                                   |
|-------------|----------------------------------|------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | player = null                    | NullPointerException               | :white_check_mark: | showAvailableCardTypes_withNullPlayer_throwsNullPointerException            |
| Test Case 2 | player with empty hand           | Nothing printed                    | :white_check_mark: | showAvailableCardTypes_withEmptyHand_printsNothing                          |
| Test Case 3 | player with one card type        | Single formatted card type         | :white_check_mark: | showAvailableCardTypes_withOneCardType_printsFormattedCardType              |
| Test Case 4 | player with multiple card types  | Comma-separated list               | :white_check_mark: | showAvailableCardTypes_withMultipleCardTypes_printsCommaSeparatedList       |
| Test Case 5 | player with underscore card type | Spaces replace underscores         | :white_check_mark: | showAvailableCardTypes_withUnderscoreCardType_replacesUnderscoresWithSpaces |
| Test Case 6 | player with mixed card types     | All types formatted correctly      | :white_check_mark: | showAvailableCardTypes_withMixedCardTypes_formatsAllCorrectly               |
| Test Case 7 | player with all card types       | Complete formatted list            | :white_check_mark: | showAvailableCardTypes_withAllCardTypes_printsCompleteList                  |

## Method 4: `public void handleDrawCommand(Player currentPlayer)`

### Step 1-3 Results

|        | Input       | Input 2          | Output                          |
|--------|-------------|------------------|---------------------------------|
| Step 1 | Player      | Deck state       | Card drawn or player eliminated |
| Step 2 | Object      | Collection       | State change or Exception       |
| Step 3 | null, valid | empty, has cards | Success/Elimination/Error       |

### Step 4:

|             | System under test                  | Expected output / state transition           | Implemented?       | Test name                                                                            |
|-------------|------------------------------------|----------------------------------------------|--------------------|--------------------------------------------------------------------------------------|
| Test Case 1 | player = null                      | NullPointerException                         | :white_check_mark: | handleDrawCommand_withNullPlayer_throwsNullPointerException                          |
| Test Case 2 | empty deck                         | Displays error and returns                   | :white_check_mark: | handleDrawCommand_withEmptyDeck_displaysErrorAndReturns                              |
| Test Case 3 | draws normal card                  | Adds card to hand and ends turn              | :white_check_mark: | handleDrawCommand_withNormalCard_addsCardToHandAndEndsTurn                           |
| Test Case 4 | draws exploding kitten, no defuse  | Player eliminated                            | :white_check_mark: | handleDrawCommand_withExplodingKittenAndNoDefuse_removesPlayer                       |
| Test Case 5 | draws exploding kitten, has defuse | Uses defuse, reinserts kitten, advances turn | :white_check_mark: | handleDrawCommand_withExplodingKittenAndDefuse_usesDefuseAndReinsertsAndAdvancesTurn |
| Test Case 6 | draws skip card                    | Adds card to hand and ends turn              | :white_check_mark: | handleDrawCommand_withSkipCard_addsCardToHandAndEndsTurn                             |
| Test Case 7 | draws defuse card                  | Adds card to hand and ends turn              | :white_check_mark: | handleDrawCommand_withDefuseCard_addsCardToHandAndEndsTurn                           |

## Method 5: `public void displayGameState(Player currentPlayer)`

### Step 1-3 Results

|        | Input          | Output                          |
|--------|----------------|---------------------------------|
| Step 1 | Game state     | Formatted display of game state |
| Step 2 | Objects        | String output                   |
| Step 3 | Various states | Correctly formatted output      |

### Step 4:

|             | System under test      | Expected output / state transition | Implemented?       | Test name                                                       |
|-------------|------------------------|------------------------------------|--------------------|-----------------------------------------------------------------|
| Test Case 1 | two players, full deck | Displays correct state             | :white_check_mark: | displayGameState_withTwoPlayersAndFullDeck_displaysCorrectState |
| Test Case 2 | one player remaining   | Displays correct state             | :white_check_mark: | displayGameState_withOnePlayerRemaining_displaysCorrectState    |
| Test Case 3 | five players remaining | Displays correct state             | :white_check_mark: | displayGameState_withFivePlayersRemaining_displaysCorrectState  |
| Test Case 4 | empty deck             | Displays zero cards                | :white_check_mark: | displayGameState_withEmptyDeck_displaysZeroCards                |
| Test Case 5 | any state              | Calls UI display player hand       | :white_check_mark: | displayGameState_ensuresUserInterfaceDisplayPlayerHandCalled    |
| Test Case 6 | large numbers          | Displays correctly                 | :white_check_mark: | displayGameState_withLargeNumbers_displaysCorrectly             |

## Method 6: `public void handleQuitCommand()`

### Step 1-3 Results

|        | Input | Output                                |
|--------|-------|---------------------------------------|
| Step 1 | None  | Game state change and display message |
| Step 2 | None  | Boolean and String                    |
| Step 3 | None  | Sets game running false and messages  |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                                 |
|-------------|-------------------|------------------------------------|--------------------|-------------------------------------------|
| Test Case 1 | Called            | Sets gameRunning to false          | :white_check_mark: | handleQuitCommand_setsGameRunningToFalse  |
| Test Case 2 | Called            | Displays thank you message         | :white_check_mark: | handleQuitCommand_displaysThankYouMessage |

## Method 7: `public void handlePlayerGetsEliminated()`

### Step 1-3 Results

|        | Input                 | Output                      |
|--------|-----------------------|-----------------------------|
| Step 1 | Active players list   | Turn order updated          |
| Step 2 | Collection            | State change                |
| Step 3 | Various player counts | Correctly synced turn order |

### Step 4:

|             | System under test     | Expected output / state transition | Implemented?       | Test name                                                      |
|-------------|-----------------------|------------------------------------|--------------------|----------------------------------------------------------------|
| Test Case 1 | Two active players    | Syncs correctly                    | :white_check_mark: | handlePlayerElimination_withTwoActivePlayers_syncsCorrectly    |
| Test Case 2 | One active player     | Syncs correctly                    | :white_check_mark: | handlePlayerElimination_withOneActivePlayer_syncsCorrectly     |
| Test Case 3 | Three active players  | Syncs correctly                    | :white_check_mark: | handlePlayerElimination_withThreeActivePlayers_syncsCorrectly  |
| Test Case 4 | Empty player list     | Still calls syncWith               | :white_check_mark: | handlePlayerElimination_withEmptyPlayerList_stillCallsSyncWith |
| Test Case 5 | Multiple eliminations | Works correctly                    | :white_check_mark: | handlePlayerElimination_calledMultipleTimes_worksCorrectly     |
| Test Case 6 | Player list           | Does not modify list               | :white_check_mark: | handlePlayerElimination_doesNotModifyPlayerList                |

## Method 8: `public void checkWinCondition()`

### Step 1-3 Results

|        | Input                | Output                          |
|--------|----------------------|---------------------------------|
| Step 1 | Active players count | Game state and win/lose message |
| Step 2 | Integer              | Boolean and String              |
| Step 3 | 0, 1, >1 players     | Game ends or continues          |

### Step 4:

|             | System under test    | Expected output / state transition | Implemented?       | Test name                                                  |
|-------------|----------------------|------------------------------------|--------------------|------------------------------------------------------------|
| Test Case 1 | Two active players   | Game continues running             | :white_check_mark: | checkWinCondition_withTwoActivePlayers_gameStillRunning    |
| Test Case 2 | Three active players | Game continues running             | :white_check_mark: | checkWinCondition_withThreeActivePlayers_gameStillRunning  |
| Test Case 3 | Five active players  | Game continues running             | :white_check_mark: | checkWinCondition_withFiveActivePlayers_gameStillRunning   |
| Test Case 4 | One active player    | Game ends with winner              | :white_check_mark: | checkWinCondition_withOneActivePlayer_gameEndsWithWinner   |
| Test Case 5 | No active players    | Game ends with no winner           | :white_check_mark: | checkWinCondition_withNoActivePlayers_gameEndsWithNoWinner |

## Method 9: `public void processCommand(String input, Player currentPlayer)`

### Step 1-3 Results

|        | Input 1          | Input 2        | Output                     |
|--------|------------------|----------------|----------------------------|
| Step 1 | Command string   | Current player | Command execution or error |
| Step 2 | String           | Player Object  | Action or Error message    |
| Step 3 | null/empty/valid | valid player   | Success or error message   |

### Step 4:

|              | System under test                       | Expected output / state transition | Implemented?       | Test name                                                                |
|--------------|-----------------------------------------|------------------------------------|--------------------|--------------------------------------------------------------------------|
| Test Case 1  | input = null                            | Displays error                     | :white_check_mark: | processCommand_withNullInput_displaysError                               |
| Test Case 2  | input = empty string                    | Displays error                     | :white_check_mark: | processCommand_withEmptyInput_displaysError                              |
| Test Case 3  | input = whitespace only                 | Displays error                     | :white_check_mark: | processCommand_withWhitespaceOnlyInput_displaysError                     |
| Test Case 4  | input = "play" with different cards     | Calls handlePlayCommand            | :white_check_mark: | processCommand_withPlayCommandForDifferentCards_callsHandlePlayCommand   |
| Test Case 5  | handlePlayCommand throws exception      | Displays error message             | :white_check_mark: | processCommand_whenHandlePlayCommandThrowsException_displaysErrorMessage |
| Test Case 6  | draw command throws exception           | Displays error message             | :white_check_mark: | processCommand_whenDrawThrowsException_catchesAndDisplaysError           |
| Test Case 7  | input = "help"                          | Displays help                      | :white_check_mark: | processCommand_withHelpCommand_displaysHelp                              |
| Test Case 8  | input = "hand"                          | Displays player hand               | :white_check_mark: | processCommand_withHandCommand_displaysPlayerHand                        |
| Test Case 9  | input = "quit"                          | Calls handleQuitCommand            | :white_check_mark: | processCommand_withQuitCommand_callsHandleQuitCommand                    |
| Test Case 10 | input = invalid commands                | Displays unknown command error     | :white_check_mark: | processCommand_withUnknownCommands_displaysUnknownCommandError           |
| Test Case 11 | input = multiple spaces in play command | Processes successfully             | :white_check_mark: | processCommand_withMultipleSpacesInPlayCommand_DoesntFail                |
| Test Case 12 | input = "status"                        | Displays game status               | :white_check_mark: | processCommand_withStatusCommand_displaysGameStatus                      |
| Test Case 13 | input = "draw"                          | Calls handleDrawCommand            | :white_check_mark: | processCommand_withDrawCommand_callsHandleDrawCommand                    |

## Method 10: `public static List<Card> createInitialDeck(CardFactory cardFactory, int numberOfPlayers)`

### Step 1-3 Results

|        | Input 1       | Input 2           | Output                |
|--------|---------------|-------------------|-----------------------|
| Step 1 | Card Factory  | Number of players | Initial deck of cards |
| Step 2 | Object        | Integer           | List<Card>            |
| Step 3 | valid factory | 2-5 players       | Correctly sized deck  |

### Step 4:

|             | System under test         | Expected output / state transition  | Implemented?       | Test name                                                              |
|-------------|---------------------------|-------------------------------------|--------------------|------------------------------------------------------------------------|
| Test Case 1 | Valid player counts (2-5) | Creates correct deck                | :white_check_mark: | createInitialDeck_withValidPlayerCount_createsCorrectDeck              |
| Test Case 2 | No normal cards needed    | Does not add normal cards           | :white_check_mark: | createInitialDeck_whenNoNormalCardsNeeded_doesNotAddNormalCards        |
| Test Case 3 | Normal cards needed       | Adds correct number of normal cards | :white_check_mark: | createInitialDeck_whenNormalCardsNeeded_addsCorrectNumberOfNormalCards |

## Method 11: `public static GameEngine createNewGame()`

### Step 1-3 Results

|        | Input | Output                     |
|--------|-------|----------------------------|
| Step 1 | None  | Initialized game engine    |
| Step 2 | None  | GameEngine object          |
| Step 3 | None  | Valid game engine instance |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                            |
|-------------|-------------------|------------------------------------|--------------------|--------------------------------------|
| Test Case 1 | Called            | Creates valid game engine          | :white_check_mark: | createNewGame_createsValidGameEngine |
