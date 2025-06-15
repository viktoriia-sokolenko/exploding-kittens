# BVA Analysis for DECK

#### Important Note
Whenever you see `card1` without specifying its type, that means I am using Parametrized Testing, and the test runs for cards with all the available card types.

Whenever you see the keywords "non-empty list" or noticed that the states takes in a list of two
cards without specifying the Card Type (i.e `[card1, card2]`), that means that the test cases uses a Parameterized Test
that uses all the different Card Types into a list of two different Cards and performs _Parameterized Tests_ based on
it.
This Parameterized Testing is grouped as:

* `[CardType.NORMAL, CardType.ATTACK]`
* `[CardType.DEFUSE, CardType.SKIP]`
* `[CardType.FAVOR, CardType.EXPLODING_KITTEN]`
* `[CardType.SHUFFLE, CardType.ALTER_THE_FUTURE]`
* `[CardType.SEE_THE_FUTURE, CardType.NUKE]`

This is done like this because the main purpose of deck isn't too focused on the Card Types inside the deck but the
functionality of deck working with all the different types of cards. As a result, these Parameterized Testing ensures
that since Deck is a collection of Cards, no matter the Cards given in the collection, the operation still functions as
expected.

* In Other Words, in addition to deck being classified as a Collection (of Cards), if you'd like, it's also a case of
  the listed Card Types above

Also, unless specified that I am dealing with  `Duplicates` states, each card listed below is from different
CardTypes. When you see `card2.1` and `card2.2` that means that I am dealing with duplicate cards.

## Method 1: `public Card draw()`

### Step 1-3 Results

|        | Input                                                                                      | Output                                          |
|--------|--------------------------------------------------------------------------------------------|-------------------------------------------------|
| Step 1 | Deck of Cards                                                                              | Card drawn from deck and `size` decrements by 1 |
| Step 2 | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) | Card object or Exception                        |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  | Card object or `NoSuchElementException`         |

### Step 4:

|             | System under test                | Expected output / state transition         | Implemented?       | Test name                                                                  |
|-------------|----------------------------------|--------------------------------------------|--------------------|----------------------------------------------------------------------------|
| Test Case 1 | Deck `[]`                        | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: | draw_emptyDeck_throwsNoSuchElementException                                |
| Test Case 2 | Deck `[card1]`                   | Returns `card1`; Deck `[]`                 | :white_check_mark: | drawAndGetDeckSize_deckWithOneCard                                         |
| Test Case 3 | Deck `[card1, card2]`            | Returns `card2`; Deck `[card1]`            | :white_check_mark: | drawAndGetDeckSize_deckWithTwoCards                                        |
| Test Case 4 | Deck `[card1, card2.1, card2.2]` | Returns `card2.2`; Deck `[card1, card2.2]` | :white_check_mark: | drawAndGetDeckSize_deckWithThreeCardsAndDuplicate_returnsDeckWithTwoCards  |

## Method 2: `public Card peekTop()`

### Step 1-3 Results

|        | Input                                                                                      | Output                                        |
|--------|--------------------------------------------------------------------------------------------|-----------------------------------------------|
| Step 1 | Deck of Cards                                                                              | Card on top of deck, i.e last index from deck |
| Step 2 | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) | Card object or Exception                      |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  | Card object or `NoSuchElementException`       |

### Step 4:

|             | System under test                | Expected output / state transition         | Implemented?       | Test name                                              |
|-------------|----------------------------------|--------------------------------------------|--------------------|--------------------------------------------------------|
| Test Case 1 | Deck `[]`                        | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: | peekTop_emptyDeck_throwsNoSuchElementException         |
| Test Case 2 | Deck `[card1]`                   | Returns `card1`;                           | :white_check_mark: | peekTop_deckWithOneCard_returnsTheOnlyCard             |
| Test Case 3 | Deck `[card1, card2]`            | Returns `card2`;                           | :white_check_mark: | peekTop_deckWithTwoCards_returnsCardInIndexOne         |
| Test Case 4 | Deck `[card1, card2.1, card2.2]` | Returns `card2.2`;                         | :white_check_mark: | peekTop_deckWithThreeCardsAndDuplicate_returnsLastCard |

## Method 3: `public Card getCardAt(int index)`

### Step 1-3 Results

|        | Input 1                                          | Input 2                                                                                    | Output                                     |
|--------|--------------------------------------------------|--------------------------------------------------------------------------------------------|--------------------------------------------|
| Step 1 | An integer for the index                         | Deck of Cards                                                                              | Get the card at the specified Index        |
| Step 2 | Array Indices (Index are -1, 0, 1, size, size+1) | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) | Card object or Exception                   |
| Step 3 | `-1`, `0`, `1`, `size`, `size+1`                 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  | Card object or `IndexOutOfBoundsException` |

### Step 4:

|              | System under test                           | Expected output / state transition | Implemented?       | Test name                                                                  |
|--------------|---------------------------------------------|------------------------------------|--------------------|----------------------------------------------------------------------------|
| Test Case 1  | index `-1`, Deck `[]`                       | `IndexOutOfBoundsException`        | :white_check_mark: | getCardAt_emptyDeckWithIndexNegative_throwsIndexOutOfBoundsException       |
| Test Case 2  | index `-1`, Deck `[card1]`                  | `IndexOutOfBoundsException`        | :white_check_mark: | getCardAt_nonEmptyDeckWithIndexNegative_throwsIndexOutOfBoundsException    |
| Test Case 3  | index `0`, Deck `[]`                        | `IndexOutOfBoundsException`        | :white_check_mark: | getCardAt_emptyDeckWithIndexZero_throwsIndexOutOfBoundsException           |
| Test Case 4  | index `0`, Deck `[card1, card2]`            | Returns `card1`                    | :white_check_mark: | insertCardAtAndGetCardAt_nonEmptyDeckWithIndexZero                         |
| Test Case 5  | index `1`, Deck `[card1, card2, card3]`     | Returns `card2`                    | :white_check_mark: | insertCardAtAndGetCardAt_deckWithTwoCardsWithIndexOne                      |
| Test Case 6  | index `1`, Deck `[card1, card2.1, card2.2]` | Returns `card2.1`                  | :white_check_mark: | getCardAt_deckWithThreeCardsAndDuplicateWithIndexOne_returnsCardInIndexOne |
| Test Case 7  | index `2`, Deck `[card1, card2]`            | `IndexOutOfBoundsException`        | :white_check_mark: | getCardAt_deckWithTwoCardsWithIndexTwo_throwsIndexOutOfBoundsException     |
| Test Case 8  | index `3`, Deck `[card1, card2, card3]`     | `IndexOutOfBoundsException`        | :white_check_mark: | getCardAt_deckWithThreeCardsWithIndexThree_throwsIndexOutOfBoundsException |
| Test Case 9  | index `1`, Deck `[]`                        | `IndexOutOfBoundsException`        | :white_check_mark: | getCardAt_emptyDeckWithIndexOne_throwsIndexOutOfBoundsException            |
| Test Case 10 | index `4`, Deck `[card1, card2, card3]`     | `IndexOutOfBoundsException`        | :white_check_mark: | getCardAt_deckWithThreeCardsWithIndexFour_throwsIndexOutOfBoundsException  |

**Note**: Some of the test cases for this method have its own testing implemented as well as being implemented within
other method tests. These tests are labeled as methodOneAndMethodTwo.

## Method 4: `public int getDeckSize()`

### Step 1-3 Results

|        | Input 1                                                                                    | Output                             |
|--------|--------------------------------------------------------------------------------------------|------------------------------------|
| Step 1 | Deck of Cards                                                                              | The number of cards that deck have |
| Step 2 | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) | Integer                            |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  | `0`, `1`, `2`, `3`                 |

### Step 4:

|             | System under test                | Expected output / state transition | Implemented?       | Test name                                                  |
|-------------|----------------------------------|------------------------------------|--------------------|------------------------------------------------------------|
| Test Case 1 | Deck `[]`                        | Returns `0`;                       | :white_check_mark: | drawAndGetDeckSize_deckWithOneCard_returnsEmptyDeck        |
| Test Case 2 | Deck `[card1]`                   | Returns `1`;                       | :white_check_mark: | drawAndGetDeckSize_deckWithTwoCards_returnsDeckWithOneCard |
| Test Case 3 | Deck `[card1, card2]`            | Returns `2`;                       | :white_check_mark: | getDeckSize_deckWithTwoCards_ReturnsTwo                    |
| Test Case 4 | Deck `[card1, card2.1, card2.2]` | Returns `3`;                       | :white_check_mark: | getDeckSize_deckWithThreeCardsAndDuplicate_ReturnsThree    |

## Method 5: `public void insertCardAt(Card card, int index)`

### Step 1-3 Results

|        | Input 1                                          | Input 2                                                                                                      | Input 3                                                                                    | Output                                                                                                                                |
|--------|--------------------------------------------------|--------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | An integer for the index                         | Card that will be inserted,                                                                                  | Deck of Cards                                                                              | Card inserted into deck and `size` increments by 1                                                                                    |
| Step 2 | Array Indices (Index are -1, 0, 1, size, size+1) | Case                                                                                                         | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) | Collections or Exception                                                                                                              |
| Step 3 | `-1`, `0`, `1`, `size`, `size+1`                 | null, NORMAL, ATTACK, DEFUSE, SKIP, FAVOR, EXPLODING_KITTEN, SHUFFLE, ALTER_THE_FUTURE, SEE_THE_FUTURE, NUKE | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  | `[card]`, `[card, card1, card2]`, `[card, card1, card2]`, `[card1, card2, card]`, `IndexOutOfBoundsException`, `NullPointerException` |

### Step 4:

|              | System under test (pre-state)                               | Expected output / state transition | Implemented?       | Test name                                                                   |
|--------------|-------------------------------------------------------------|------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1  | index `-1`, card `NORMAL`,  Deck `[]`                       | `IndexOutOfBoundsException`        | :white_check_mark: | insertCardAt_emptyDeckWithIndexNegative_throwsIndexOutOfBoundsException     |
| Test Case 2  | index `-1`, card `NORMAL`, Deck `[NORMAL]`                  | `IndexOutOfBoundsException`        | :white_check_mark: | insertCardAt_nonEmptyDeckWithIndexNegative_throwsIndexOutOfBoundsException  |
| Test Case 3  | index `0`, card `EXPLODING_KITTEN`, Deck `[]`               | Deck `[EXPLODING_KITTEN]`          | :white_check_mark: | insertCardAt_emptyDeckWithIndexZero                                         |
| Test Case 4  | index `0`, card `NORMAL`, Deck `[card1, card2]`             | Deck `[NORMAL, card1, card2]`      | :white_check_mark: | insertCardAtAndGetCardAt_nonEmptyDeckWithIndexZero                          |
| Test Case 5  | index `1`, card `SKIP`, Deck `[SHUFFLE, NUKE]`              | Deck `[SHUFFLE, SKIP, NUKE]`       | :white_check_mark: | insertCardAtAndGetCardAt_deckWithTwoCardsWithIndexOne                       |
| Test Case 6  | index `2`, card `NORMAL`, Deck `[card1, card2]`             | Deck `[card1, card2, NORMAL]`      | :white_check_mark: | insertCardAt_deckWithTwoCardsWithIndexTwo                                   |
| Test Case 7  | index `1`, card `NORMAL`, Deck `[]`                         | `IndexOutOfBoundsException`        | :white_check_mark: | insertCardAt_emptyDeckWithIndexOne_throwsIndexOutOfBoundsException          |
| Test Case 8  | index `3`, card `NORMAL`, Deck `[EXPLODING_KITTEN, DEFUSE]` | `IndexOutOfBoundsException`        | :white_check_mark: | insertCardAt_deckWithTwoCardsWithIndexThree_throwsIndexOutOfBoundsException |
| Test Case 9  | index `0`, card `null`, `0`, Deck `[]`                      | `NullPointerException`             | :white_check_mark: | insertCardAt_emptyDeckAndInsertNullCard_throwsNullPointerException          |
| Test Case 10 | index `0`, card `null`, `0`, Deck `[NORMAL, FAVOR]`         | `NullPointerException`             | :white_check_mark: | insertCardAt_nonEmptyDeckAndInsertNullCard_throwsNullPointerException       |

**Note:** This is an important place where the note listed above comes in place. As seen, there are many cases that
Card can be, so on here, I have listed them as `card1, card2` as a way to showcase the different types,
but please do recall that Parameterized testing is used to ensure all Cards and their types are tested.

## Method 6: `public void shuffleDeck(Random rand)`

### Step 1-3 Results

|        | Input 1                       | Input 2                                                                                    | Output                                                                |
|--------|-------------------------------|--------------------------------------------------------------------------------------------|-----------------------------------------------------------------------|
| Step 1 | Random class that is a number | Deck of Cards                                                                              | Deck gets shuffled based on the randomized amount of runs given       |
| Step 2 | Random object or Null         | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) | Collection (cards within deck have been shuffled around) or Exception |
| Step 3 | null, Random object           | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  | Collection or `NullPointerException`                                  |

### Step 4:

|             | System under test                      | Expected output                                                                                                       | Implemented?       | Test name                                                     |
|-------------|----------------------------------------|-----------------------------------------------------------------------------------------------------------------------|--------------------|---------------------------------------------------------------|
| Test Case 1 | Rand `null`, Deck `[]`                 | `NullPointerException`                                                                                                | :white_check_mark: | shuffleDeck_emptyDeckAndNullRandom_throwsNullPointerException |
| Test Case 2 | Rand, Deck `[]`                        | Order remains unchanged (idempotent) `[]`                                                                             | :white_check_mark: | shuffleDeck_emptyDeck_orderRemainTheSame                      |
| Test Case 3 | Rand, Deck `[card1]`                   | Order remains unchanged (idempotent) `[card1]`                                                                        | :white_check_mark: | shuffleDeck_deckWithOneCard_orderRemainTheSame                |
| Test Case 4 | Rand, Deck `[card1, card2]`            | Order changes _or_ remains statistically different over many runs; deck content invariant `[card2, card1]`            | :white_check_mark: | shuffleDeck_deckWithTwoCards_orderFlipsPositions              |
| Test Case 5 | Rand, Deck `[card1, card2, card3]`     | Order changes _or_ remains statistically different over many runs; deck content invariant `[card3, card1, card2]`     | :white_check_mark: | shuffleDeck_deckWithThreeCards_orderChanges                   |
| Test Case 6 | Rand, Deck `[card1, card2.1, card2.2]` | Order changes _or_ remains statistically different over many runs; deck content invariant `[card2.1, card2.2, card1]` | :white_check_mark: | shuffleDeck_deckWithThreeCardsAndDuplicate_orderChanges       |

## Method 7: `public List<Card> peekTopTwoCards()`

### Step 1-3 Results

|        | Input                                                                                                 | Output                                                                                   |
|--------|-------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| Step 1 | Deck of Cards                                                                                         | Two Cards on top of deck, i.e last index from deck                                       |
| Step 2 | Collection (Empty, Exactly 1 Element, Exactly 2 Elements, More than 2 Elements containing Duplicates) | Collections of 1-2 Card objects or Exception                                             |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                             | `[card1]`, `[card1, card2]`, duplicates `[card2.1, card2.2]` or `NoSuchElementException` |

### Step 4:

|             | System under test                | Expected output / state transition         | Implemented?       | Test name                                                                |
|-------------|----------------------------------|--------------------------------------------|--------------------|--------------------------------------------------------------------------|
| Test Case 1 | Deck `[]`                        | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: | peekTopTwoCards_emptyDeck_throwsNoSuchElementException                   |
| Test Case 2 | Deck `[card1]`                   | Returns `[card1]`;                         | :white_check_mark: | peekTopTwoCards_deckWithOneCard_returnsTheOnlyCard                       |
| Test Case 3 | Deck `[card1, card2]`            | Returns `[card1, card2]`;                  | :white_check_mark: | peekTopTwoCards_deckWithTwoCards_returnsTwoLastCards                     |
| Test Case 4 | Deck `[card1, card2.1, card2.2]` | Returns `[card2.1, card2.2]`;              | :white_check_mark: | peekTopTwoCards_deckWithThreeCardsAndDuplicate_returnsLastDuplicateCards |

## Method 8: `public List<Card> peekTopThreeCards()`

### Step 1-3 Results

|        | Input                                                                                                                     | Output                                                                                                            |
|--------|---------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| Step 1 | Deck of Cards                                                                                                             | Three Cards on top of deck, i.e last index from deck                                                              |
| Step 2 | Collection (Empty, Exactly 1 Element, Exactly 2 Elements, Exactly 3 Elements, More than 3 Elements containing Duplicates) | Collections of 1-3 Card objects or Exception                                                                      |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, `[card1, card2, card3]`, duplicates `[card1, card2, card3, card4.1, card4.2]`          | `[card1]`, `[card1, card2]`, `[card2, card3]`, duplicates `[card3, card4.1, card4.2]` or `NoSuchElementException` |

### Step 4:

|             | System under test                              | Expected output / state transition         | Implemented?       | Test name                                                                 |
|-------------|------------------------------------------------|--------------------------------------------|--------------------|---------------------------------------------------------------------------|
| Test Case 1 | Deck `[]`                                      | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: | peekTopThreeCards_emptyDeck_throwsNoSuchElementException                  |
| Test Case 2 | Deck `[card1]`                                 | Returns `[card1]`;                         | :white_check_mark: | peekTopThreeCards_deckWithOneCard_returnsTheOnlyCard                      |
| Test Case 3 | Deck `[card1, card2]`                          | Returns `[card1, card2]`;                  | :white_check_mark: | peekTopThreeCards_deckWithTwoCards_returnsTwoLastCards                    |
| Test Case 3 | Deck `[card1, card2, card3]`                   | Returns `[card1, card2, card3]`;           | :white_check_mark: | peekTopThreeCards_deckWithThreeCards_returnsThreeLastCards                |
| Test Case 4 | Deck `[card1, card2, card3, card4.1, card4.2]` | Returns `[card3, card4.1, card4.2]`;       | :white_check_mark: | peekTopThreeCards_deckWithFourCardsAndDuplicate_returnsLastDuplicateCards |

## Method 7: `public void rearrangeTopThreeCards(List<int> newIndices)`

### Step 1-3 Results

|        | Input 1                                                                                                                                   | Input 2                                                         | Input 4                                                                                                                            | Output                                                                                                                                                                                                     |
|--------|-------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | List of new array indices for the card to be rearranged                                                                                   | newIndexForThirdCard, newIndexForSecondCard, newIndexForTopCard | Deck of Cards                                                                                                                      | Deck of Cards with top three cards rearranged                                                                                                                                                              |
| Step 2 | List of Array Indices                                                                                                                     | Array Indices                                                   | Collection (Empty, Exactly 1 Element, Exactly 2 Elements, Exactly 3 Elements, More than 3 Elements, Element containing duplicates) | Collection (Empty, Exactly 1 Element, Exactly 2 Elements, Exactly 3 Elements, More than 3 Elements, Element containing duplicates) or Exception                                                            |
| Step 3 | Collection (Empty, Exactly 1 Element, Exactly 2 Elements, Exactly 3 Elements, 3 elements containing Duplicates, more than three elements) | `-1`, `0`, `size-3`, `size-2`, `size-1`, `size` size+1`         | `[]`, `[card1]`, `[card1, card2]`, `[card1, card2, card3]`, duplicates `[card1, card2, card3, card4.1, card4.2]`                   | `[card1]`, Rearranged `[card1, card2]`, `[card1, card2, card3]`, duplicates `[card1, card2, card3, card4.1, card4.2]` or `NoSuchElementException`, `IndexOutOfBoundsException`, `IllegalArgumentException` |

### Step 4:

|              | System under test                                                 | Expected output / state transition                                            | Implemented?       | Test name                                                                                 |
|--------------|-------------------------------------------------------------------|-------------------------------------------------------------------------------|--------------------|-------------------------------------------------------------------------------------------|
| Test Case 1  | Deck `[]`, indices `[0, 1, 2]`                                    | `NoSuchElementException` (Deck is empty)                                      | :white_check_mark: | rearrangeTopThreeCards_emptyDeck_throwsNoSuchElementException                             |
| Test Case 2  | Deck `[card1]`, indices `[0, 1]`                                  | `IllegalArgumentException` (Number of indices is larger than the deck size)   | :white_check_mark: | rearrangeTopThreeCards_oneCardDeckWithTwoIndices_throwsIllegalArgumentException           |
| Test Case 3  | Deck `[card1, card2]`, indices `[0, 1, 2]`                        | `IllegalArgumentException` (Number of indices is larger than the deck size)   | :white_check_mark: | rearrangeTopThreeCards_TwoCardsThreeIndices_throwsIllegalArgumentException                |
| Test Case 4  | Deck `[card1, card2, card3]`, indices `[0, 1, 2, 3]`              | `IllegalArgumentException` (Number of indices is larger than the deck size)   | :white_check_mark: | rearrangeTopThreeCards_ThreeCardsFourIndices_throwsIllegalArgumentException               |
| Test Case 5  | Deck `[card1, card2]`, indices `-1, 1`                            | `IllegalArgumentException` (Negative indices are not allowed)                 | :white_check_mark: | rearrangeTopThreeCards_withNegativeFirstIndex_throwsIllegalArgumentException              |
| Test Case 6  | Deck `[card1, card2]`, indices `0, -1`                            | `IllegalArgumentException` (Negative indices are not allowed)                 | :white_check_mark: | rearrangeTopThreeCards_withNegativeSecondIndex_throwsIllegalArgumentException             |
| Test Case 8  | Deck `[card1, card2, card3]`, indices `3, 1, 0`                   | `IllegalArgumentException` (With deck size s, indices must be [s - 1, s - 3]) | :white_check_mark: | rearrangeTopThreeCards_ThreeCardsIndexThree_throwsIllegalArgumentException                |
| Test Case 9  | Deck `[card1, card2, card3, card4.1, card4.2]`, indices `3, 0, 2` | `IllegalArgumentException` (With deck size s, indices must be [s - 1, s - 3]) | :white_check_mark: | rearrangeTopThreeCards_FourCardsIndexZero_throwsIllegalArgumentException                  |
| Test Case 11 | Deck `[card1, card2, card3, card4.1, card4.2]`, indices `1, 2, 1` | `IllegalArgumentException` (Duplicate indices are not allowed)                | :white_check_mark: | rearrangeTopThreeCards_withSameFirstThirdIndex_throwsIllegalArgumentException             |
| Test Case 12 | Deck `[card1, card2]`, indices `0, 0`                             | `IllegalArgumentException` (Duplicate indices are not allowed)                | :white_check_mark: | rearrangeTopThreeCards_withSameFirstSecondIndex_throwsIllegalArgumentException            |
| Test Case 13 | Deck `[card1, card2, card3]`, indices `0, 2, 2`                   | `IllegalArgumentException` (Duplicate indices are not allowed)                | :white_check_mark: | rearrangeTopThreeCards_withSameSecondThirdIndex_throwsIllegalArgumentException            |
| Test Case 14 | Deck `[card1]`, indices `[0]`                                     | Deck `[card1]`                                                                | :white_check_mark: | rearrangeTopThreeCards_deckWithOneCard_orderRemainsTheSame                                |
| Test Case 15 | Deck `[card1, card2]`, indices `[0, 1]`                           | Deck `[card1, card2]`                                                         | :white_check_mark: | rearrangeTopThreeCards_deckWithTwoCardsAndSameIndices_orderRemainsTheSame                 |
| Test Case 16 | Deck `[card1, card2]`, indices `[1, 0]`                           | Deck `[card2, card1]`                                                         |                    | rearrangeTopThreeCards_deckWithTwoCardsAndReversedIndices_reversesCards                   |
| Test Case 17 | Deck `[card1, card2, card3]`, indices `0, 1, 2`                   | Deck `[card1, card2, card3]`                                                  |                    | rearrangeTopThreeCards_deckWithThreeCardsAndSameIndices_orderRemainsTheSame               |
| Test Case 18 | Deck `[card1, card2, card3]`, indices `2, 0, 1`                   | Deck `[card2, card3, card1]`                                                  |                    | rearrangeTopThreeCards_deckWithThreeCardsAndDifferentIndices_changesOrder                 |
| Test Case 19 | Deck `[card1, card2, card3, card4.1, card4.2]`, indices `1, 2, 3` | Deck `[card1, card2, card3, card4.1, card4.2]`                                |                    | rearrangeTopThreeCards_deckWithFourCardsAndDuplicateAndSameIndices_orderRemainsTheSame    |
| Test Case 20 | Deck `[card1, card2, card3, card4.1, card4.2]`, indices `1, 3, 2` | Deck `[card1, card2, card3, card4.2, card4.1]`                                |                    | rearrangeTopThreeCards_deckWithFourCardsAndDuplicateAndThreeDifferentIndices_changesOrder |
| Test Case 21 | Deck `[card1, card2, card3, card4.1, card4.2]`, indices `3, 2, 1` | Deck `[card1, card2, card4.1, card4.2, card3]`                                |                    | rearrangeTopThreeCards_deckWithFourCardsAndDuplicateTwoDifferentIndices_reversesCards     |

## Method under test: `giveCardToPlayer(Player p)`

|             | System under test (pre-state)                                                  | Expected output / state transition                                                                      | Implemented? |
|-------------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | `p == null`                                                                    | `NullPointerException`                                                                                  |              |
| Test Case 2 | Deck `size == 0`                                                               | `NoSuchElementException` (“Deck is empty”)                                                              |              |
| Test Case 3 | `p.hand.size() == Player.MAX_HAND`                                             | `IllegalStateException` (“Hand already full”)                                                           |              |
| Test Case 4 | Deck `size == 1`; player hand has space                                        | Top card moves to `p`; deck `size` becomes 0; `p.hand.size()` increments by 1                           |              |
| Test Case 5 | Deck `size > 1`; top card is **not** `EXPLODING_KITTEN`; player hand has space | Card transferred; deck `size--`; `p.hand.size()++`; method returns the card (or `void` per your design) |              |
