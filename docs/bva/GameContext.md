# BVA Analysis for GameContext

#### Important Note

I am using Parametrized Testing, so whenever I use `testCard`, it means that the test runs for cards with all the
available card types.

## Method 1: `public void endTurnWithoutDrawingForAttacks()`

### Step 1-3 Results

|        | Input                 | Output                                                                         |
|--------|-----------------------|--------------------------------------------------------------------------------|
| Step 1 | Game Context          | calls `endTurnWithoutDrawingForAttacks` from TurnManager                       |
| Step 2 | Game Context Object   | Boolean                                                                        |
| Step 3 | Game Context Instance | True (must be true that TurnManager.endTurnWithoutDrawingForAttacks is called) |

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

|        | Input                                                                                    | Output                                                                                                                             |
|--------|------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | Deck of Cards                                                                            | Calls deck.peekTopTwoCards and then passes those cards to userInterface.displayCardsFromDeck                                       |
| Step 2 | Collection                                                                               | Cases or Exception                                                                                                                 |
| Step 3 | Empty, Exactly 1 Element, Exactly 2 Elements, More than 2 Elements containing Duplicates | Calls deck.peekTopTwoCards and userInterface.displayCardsFromDeck or only deck.peekTopTwoCards and throws `NoSuchElementException` |

### Step 4:

|             | System under test        | Expected output / state transition                                                                                         | Implemented?       | Test name                                                                   |
|-------------|--------------------------|----------------------------------------------------------------------------------------------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | Deck `[]`                | `NoSuchElementException` (“Deck is empty”)                                                                                 | :white_check_mark: | viewTopTwoCardsFromDeck_emptyDeck_throwsNoSuchElementException              |
| Test Case 2 | Deck `[testCard]`        | Gets `[testCard]` from deck.peekTopTwoCards and then passes it to userInterface.displayCardsFromDeck with deck size 1      | :white_check_mark: | viewTopTwoCardsFromDeck_deckWithOneCard_callsPeekTopTwoCards                |
| Test Case 3 | Deck `[NORMAL, FAVOR]`   | Gets `[NORMAL, FAVOR]` from deck.peekTopTwoCards and then passes it to userInterface.displayCardsFromDeck with deck size 2 | :white_check_mark: | viewTopTwoCardsFromDeck_deckWithTwoCards_callsPeekTopTwoCards               |
| Test Case 4 | Deck `[..., SKIP, SKIP]` | Gets `[SKIP, SKIP]` from deck.peekTopTwoCards and then passes it to userInterface.displayCardsFromDeck with deck size 3    | :white_check_mark: | viewTopTwoCardsFromDeck_deckWithThreeCardsAndDuplicate_callsPeekTopTwoCards |

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

|             | System under test         | Expected output / state transition | Implemented?       | Test name                                             |
|-------------|---------------------------|------------------------------------|--------------------|-------------------------------------------------------|
| Test Case 1 | Context fully initialized | calls `shuffleDeck` from Deck      | :white_check_mark: | shuffleDeckFromDeck_withFullContext_callShuffleDeck() |

## Method 5: `public void rearrangeTopThreeCardsFromDeck()`

### Step 1-3 Results

|        | Input 1                                                                                                               | Input 2                                                                                | Output 1                                                       | 
|--------|-----------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|----------------------------------------------------------------|
| Step 1 | Deck of Cards                                                                                                         | User Input: indexOfNewTopCard, indexOfNewSecondCardFromTop, indexOfNewThirdCardFromTop | Calls Deck.rearrangeTopThreeCards with indices from user input |          
| Step 2 | Collection                                                                                                            | Array Indices                                                                          | Boolean                                                        |          
| Step 3 | Empty, Exactly 1 Element, Exactly 2 Elements, Exactly 3 Elements, More than 3 Elements, Element containing duplicates | `-1`, `0`, `size-3`, `size-2`, `size-1`, `size` size+1`                                | True (must be true that Deck.rearrangeTopThreeCards is called) |          

### Step 4:

|             | System under test                                          | Expected output / state transition                                                                                                | Implemented?       | Test name                                                                 |
|-------------|------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|--------------------|---------------------------------------------------------------------------|
| Test Case 1 | Deck `[card1]`, index `0`                                  | Only asks player for 1 index with min and max Indexes `0`, passes that index to Deck.rearrangeTopThreeCards                       | :white_check_mark: | rearrangeTopThreeCardsFromDeck_oneCardDeck_callsRearrangeTopThreeCards    |
| Test Case 2 | Deck `[card1, card2]`, indices `[1, 1]`                    | Only asks player for 2 indexes with minIndex `0` and maxIndex `1`, `IllegalArgumentException` (Duplicate indices are not allowed) | :white_check_mark: | rearrangeTopThreeCardsFromDeck_sameIndices_throwsIllegalArgumentException |
| Test Case 3 | Deck `[card1, card2, card3]`, indices `2, 0, 1`            | Asks player for 3 indexes with minIndex `0` and maxIndex `2`, passes those to Deck.rearrangeTopThreeCards                         | :white_check_mark: | rearrangeTopThreeCardsFromDeck_threeCards_callsRearrangeTopThreeCards     |
| Test Case 4 | Deck `[card1, card2, card3.1, card3.2]`, indices `1, 2, 3` | Asks player for 3 indexes with minIndex `1` and maxIndex `3`, passes those to Deck.rearrangeTopThreeCards                         | :white_check_mark: | rearrangeTopThreeCardsFromDeck_fourCards_callsRearrangeTopThreeCards      |

## Method 7: `public void buryCardImplementation()`

### Step 1-3 Results

|        | Input 1                  | Input 2                                  | Input 3             | Output                                                      |
|--------|--------------------------|------------------------------------------|---------------------|-------------------------------------------------------------|
| Step 1 | Top card drawn from deck | User input: index to insert the top card | the different cards | Calls `deck.insertCardAt(topCard, index)`                   |
| Step 2 | Count (deck size)        | Range: Index value (0 to deckSize)       | Card Object         | Card inserted at specified position                         |
| Step 3 | Deck size: `0`, `1`, `5` | `-1`, `0`, `1`, `deckSize`, `deckSize+1` | Card instance       | Invalid input rejected OR card inserted in correct position |

### Step 4:

#### Important Note

Each of the card below will be using paramaterized testing to ensure that all card works and goes through

|             | System under test              | Expected output / state transition                                      | Implemented?        | Test name                                                 |
|-------------|--------------------------------|-------------------------------------------------------------------------|---------------------|-----------------------------------------------------------|
| Test Case 1 | Deck = 1, `[allCardTypes]`     | Input `0`: inserts top card at the top (index 0)                        | :white_check_mark:  | buryCardImplementation_insertAtTop_insertsAtTop           |
| Test Case 2 | Deck Size = 5                  | Input `5`: inserts top card at bottom (index = size)                    | :white_check_mark:  | buryCardImplementation_insertAtBottom_insertsAtBottom     |
| Test Case 3 | Deck Size = 5                  | Input `-1`: out of bounds → triggers retry or validation                |                     | buryCardImplementation_invalidLowInput_retriesUntilValid  |
| Test Case 4 | Deck Size = 5                  | Input `6` (deck size + 1): out of bounds → triggers retry or validation |                     | buryCardImplementation_invalidHighInput_retriesUntilValid |
| Test Case 5 | Deck Size = 5                  | Input `2`: inserts card at index 2 (middle of deck)                     |                     | buryCardImplementation_insertAtMiddle_insertsAtMiddle     |
| Test Case 6 | Deck Size = 0  (empty, size 0) | Input `0`: allowed → card becomes only card in deck                     |                     | buryCardImplementation_insertIntoEmptyDeck_deckNowHasOne  |
