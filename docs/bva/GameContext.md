# BVA Analysis for GameContext

## Method 7: `public List<Card> viewTopTwoCardsFromDeck()`

### Step 1-3 Results

|        | Input                                                                                                 | Output                                                                                   |
|--------|-------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| Step 1 | Deck of Cards                                                                                         | Two Cards on top of deck, i.e last index from deck                                       |
| Step 2 | Collection (Empty, Exactly 1 Element, Exactly 2 Elements, More than 2 Elements containing Duplicates) | Collections of 1-2 Card objects or Exception                                             |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                             | `[card1]`, `[card1, card2]`, duplicates `[card2.1, card2.2]` or `NoSuchElementException` |

### Step 4:

|             | System under test                | Expected output / state transition         | Implemented?       | Test name                                                                        |
|-------------|----------------------------------|--------------------------------------------|--------------------|----------------------------------------------------------------------------------|
| Test Case 1 | Deck `[]`                        | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: | viewTopTwoCardsFromDeck_emptyDeck_throwsNoSuchElementException                   |
| Test Case 2 | Deck `[card1]`                   | Returns `[card1]`;                         | :white_check_mark: | viewTopTwoCardsFromDeck_deckWithOneCard_returnsTheOnlyCard                       |
| Test Case 3 | Deck `[card1, card2]`            | Returns `[card1, card2]`;                  |                    | viewTopTwoCardsFromDeck_deckWithTwoCards_returnsTwoLastCards                     |
| Test Case 4 | Deck `[card1, card2.1, card2.2]` | Returns `[card2.1, card2.2]`;              |                    | viewTopTwoCardsFromDeck_deckWithThreeCardsAndDuplicate_returnsLastDuplicateCards |