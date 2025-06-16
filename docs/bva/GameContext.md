# BVA Analysis for GameContext

#### Important Note

I am using Parametrized Testing, so whenever I use `testCard`, it means that the test runs for cards with all the
available card types.

## Method 1: `public void endTurnWithoutDrawingForAttacks()`

### Step 1-3 Results

|        | Input                 | Output                                                   |
|--------|-----------------------|----------------------------------------------------------|
| Step 1 | Game Context          | calls `endTurnWithoutDrawingForAttacks` from TurnManager |
| Step 2 | Game Context Object   | calls `endTurnWithoutDrawingForAttacks` from TurnManager |
| Step 3 | Game Context Instance | calls `endTurnWithoutDrawingForAttacks` from TurnManager |

### Step 4:

|             | System under test         | Expected output / state transition                       | Implemented?       | Test name                                                        |
|-------------|---------------------------|----------------------------------------------------------|--------------------|------------------------------------------------------------------|
| Test Case 1 | Context fully initialized | calls `endTurnWithoutDrawingForAttacks` from TurnManager | :white_check_mark: | endTurnWithoutDrawingForAttacks_withFullContext_callsTurnManager |

## Method 2: `public void transferCardBetweenPlayers(Card card, Player playerGiver)`

### Step 1-3 Results

|        | Input 1                                                                                                | Input 2                                                     | Input 3                                   | Output 1                                                              | Output 2                                                       |
|--------|--------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|-------------------------------------------|-----------------------------------------------------------------------|----------------------------------------------------------------|
| Step 1 | `card` to be transferred                                                                               | State of player who gives `card` away (do they have `card`) | Player who receives `card`: currentPlayer | State of player who gives `card` away (do they no longer have `card`) | State of player who receives `card` (do they get a new `card`) |
| Step 2 | Cases                                                                                                  | Boolean                                                     | Player Object                             | Boolean or Exception                                                  | Boolean                                                        |
| Step 3 | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | true, false                                                 | currentPlayer                             | true, false or IllegalArgumentException                               | true, false                                                    |

### Step 4:

|             | System under test                                                     | Expected output / state transition                                      | Implemented?       | Test name                                                                   |
|-------------|-----------------------------------------------------------------------|-------------------------------------------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | Card `[testCard]`, playerGiver hand `without testCard`, currentPlayer | `IllegalArgumentException` (“Card not in hand: can not remove card”)    | :white_check_mark: | transferCardBetweenPlayers_withCardNotInHand_throwsIllegalArgumentException |
| Test Case 2 | Card `[testCard]`, playerGiver hand `[...testCard]`, currentPlayer    | playerGiver hand `without testCard`, currentPlayer hand `[...testCard]` | :white_check_mark: | transferCardBetweenPlayers_withCardInHand_transfersCard                     |

## Method 3: `public List<Card> viewTopTwoCardsFromDeck()`

### Step 1-3 Results

|        | Input                                                                                    | Output                                                        |
|--------|------------------------------------------------------------------------------------------|---------------------------------------------------------------|
| Step 1 | Deck of Cards                                                                            | Two Cards on top of deck, i.e last index from deck            |
| Step 2 | Collection                                                                               | Collections of 1-2 Card objects or Exception                  |
| Step 3 | Empty, Exactly 1 Element, Exactly 2 Elements, More than 2 Elements containing Duplicates | 1 Element, 2 Elements, Duplicates or `NoSuchElementException` |

### Step 4:

|             | System under test        | Expected output / state transition         | Implemented?       | Test name                                                                |
|-------------|--------------------------|--------------------------------------------|--------------------|--------------------------------------------------------------------------|
| Test Case 1 | Deck `[]`                | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: | viewTopTwoCardsFromDeck_emptyDeck_throwsNoSuchElementException           |
| Test Case 2 | Deck `[testCard]`        | Returns `[testCard]`;                      | :white_check_mark: | viewTopTwoCardsFromDeck_deckWithOneCard_returnsTheOnlyCard               |
| Test Case 3 | Deck `[NORMAL, FAVOR]`   | Returns `[NORMAL, FAVOR]`;                 | :white_check_mark: | viewTopTwoCardsFromDeck_deckWithTwoCards_returnsTwoLastCards             |
| Test Case 4 | Deck `[..., SKIP, SKIP]` | Returns `[SKIP, SKIP]`;                    | :white_check_mark: | viewTopTwoCardsFromDeck_deckWithThreeCardsAndDuplicate_returnsDuplicates |

## Method 4: `public void shuffleDeckFromDeck()`

### Step 1-3 Results

|        | Input                 | Output                        |
|--------|-----------------------|-------------------------------|
| Step 1 | Game Context          | calls `shuffleDeck` from Deck |
| Step 2 | Game Context Object   | calls `shuffleDeck` from Deck |
| Step 3 | Game Context Instance | calls `shuffleDeck` from Deck |

### Step 4:

|             | System under test         | Expected output / state transition | Implemented?       | Test name                                             |
|-------------|---------------------------|------------------------------------|--------------------|-------------------------------------------------------|
| Test Case 1 | Context fully initialized | calls `shuffleDeck` from Deck      | :white_check_mark: | shuffleDeckFromDeck_withFullContext_callShuffleDeck() |

## Method 9: `public void reverseOrderPreservingAttackState()`

### Step 1-3 Results

|        | Input                                                             | Output                                                                                                          |
|--------|-------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| Step 1 | Whether or not turnManager from GameContext is being under attack | May increment turns taken → then reverse turn order                                                             |
| Step 2 | Boolean                                                           | increment turns taken → then reverse turn order                                                                 |
| Step 3 | isUnderAttack= True, isUnderAttack=False, turnManager==null       | `incrementTurnsTake() called then `reverseOrder)` OR only `reverseOrder()` called, or don't do anything if null |

### Step 4:

|             | System under test                    | Expected output / state transition                                 | Implemented?       | Test name                                                           |
|-------------|--------------------------------------|--------------------------------------------------------------------|--------------------|---------------------------------------------------------------------|
| Test Case 1 | turnManager.isUnderAttack() == true  | `incrementTurnsTake() called then `reverseOrder)` from TurnManager | :white_check_mark: | reverseOrderPreservingAttackState_underAttack_incrementsAndReverses |
| Test Case 2 | turnManager.isUnderAttack() == false | only `reverseOrder()` called from TurnManager                      | :white_check_mark: | reverseOrderPreservingAttackState_notUnderAttack_onlyReverses       |
| Test Case 3 | turnManager == null                  | does nothing (doesn't call `reverseOrderPreservingAttackState`)    | :white_check_mark: | reverseOrderPreservingAttackState_nullTurnManager_doesNothing       |

## Method 10: `public void swapTopAndBottomDeckCards()`

### Step 1-3 Results

|        | Input                                                                                    | Output                                                     |
|--------|------------------------------------------------------------------------------------------|------------------------------------------------------------|
| Step 1 | Deck of cards                                                                            | Calls deck.swapTopAndBottom()                              |
| Step 2 | Collection                                                                               | Boolean                                                    |
| Step 3 | Empty, Exactly 1 Element, Exactly 2 Elements, More than 2 Elements containing Duplicates | True (must be true that deck.swapTopAndBottom() is called) |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                                                       |
|-------------|-------------------|------------------------------------|--------------------|-----------------------------------------------------------------|
| Test Case 1 | Deck              | deck.swapTopAndBottom() is called  | :white_check_mark: | swapTopAndBottomDeckCards_withFullContext_callsSwapTopAndBottom |