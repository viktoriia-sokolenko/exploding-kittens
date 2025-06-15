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

|        | Input                 | Output                         |
|--------|-----------------------|--------------------------------|
| Step 1 | Game Context          | calls `shuffleDeck` from Deck  |
| Step 2 | Game Context Object   | calls `shuffleDeck` from Deck  |
| Step 3 | Game Context Instance | calls `shuffleDeck` from Deck  |

### Step 4:

|             | System under test         | Expected output / state transition | Implemented?       | Test name                                              |
|-------------|---------------------------|------------------------------------|--------------------|--------------------------------------------------------|
| Test Case 1 | Context fully initialized | calls `shuffleDeck` from Deck      | :white_check_mark: | shuffleDeckFromDeck_withFullContext_callShuffleDeck()  |

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
| Test Case 3 | Deck `[card1, card2, card3]`, indices `2, 3, 1`            | Asks player for 3 indexes with minIndex `0` and maxIndex `2`, passes those to Deck.rearrangeTopThreeCards                         | :white_check_mark: | rearrangeTopThreeCardsFromDeck_threeCards_callsRearrangeTopThreeCards     |
| Test Case 4 | Deck `[card1, card2, card3.1, card3.2]`, indices `1, 2, 3` | Asks player for 3 indexes with minIndex `1` and maxIndex `3`, passes those to Deck.rearrangeTopThreeCards                         | :white_check_mark: | rearrangeTopThreeCardsFromDeck_fourCards_callsRearrangeTopThreeCards      |