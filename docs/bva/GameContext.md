# BVA Analysis for GameContext
#### Important Note
I am using Parametrized Testing, so whenever I use `testCard`, it means that the test runs for cards with all the available card types.

## Method 7: `public List<Card> viewTopTwoCardsFromDeck()`

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

## Method 8: `public void shuffleDeckFromDeck()`

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