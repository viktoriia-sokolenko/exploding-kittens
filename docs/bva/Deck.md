# BVA Analysis for Deck Class

#### Important Note

Whenever you see the keywords "non-empty list" or noticed that the states takes in a list of two
cards without specifying the Card Type (i.e `[card1, card2]`), that means that the test cases uses a Parameterized Test
that uses all the different Card Types into a list of two different Cards and performs _Parameterized Tests_ based on
it.
This Parameterized Testing are grouped as:

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

Also, unless specified that I am dealing with  `Duplicates` states, each cards listed below are from different
CardTypes. When you see `card2.1` and `card2.2` that means that I am dealing with duplicate cards.

## Method 1: `public Card draw()`

### Step 1-3 Results

|        | Input                                                                                      | (if more to consider for input) | Output                                          |
|--------|--------------------------------------------------------------------------------------------|---------------------------------|-------------------------------------------------|
| Step 1 | Deck of Cards                                                                              |                                 | Card drawn from deck and `size` decrements by 1 |
| Step 2 | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) |                                 | Card object or Exception                        |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  |                                 | Card object or `NoSuchElementException`         |

### Step 4:

|             | System under test           | Expected output / state transition                              | Implemented?       |
|-------------|-----------------------------|-----------------------------------------------------------------|--------------------|
| Test Case 1 | `[]`                        | `NoSuchElementException` (“Deck is empty”)                      | :white_check_mark: |
| Test Case 2 | `[card1]`                   | Returns `card1`; deck `size = 0` []                             | :white_check_mark: |
| Test Case 3 | `[card1, card2]`            | Returns `card2`; deck `size` decrements by 1 [card1]            | :white_check_mark: |
| Test Case 4 | `[card1, card2.1, card2.2]` | Returns `card2.2`; deck `size` decrements by 1 [card1, card2.2] | :white_check_mark: |

## Method 2: `public Card peekTop()`

### Step 1-3 Results

|        | Input                                                                                      | (if more to consider for input) | Output                                        |
|--------|--------------------------------------------------------------------------------------------|---------------------------------|-----------------------------------------------|
| Step 1 | Deck of Cards                                                                              |                                 | Card on top of deck, i.e last index from deck |
| Step 2 | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) |                                 | Card object or Exception                      |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  |                                 | Card object or `NoSuchElementException`       |

### Step 4:

|             | System under test           | Expected output / state transition         | Implemented?       |
|-------------|-----------------------------|--------------------------------------------|--------------------|
| Test Case 1 | `[]`                        | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: |
| Test Case 2 | `[card1]`                   | Returns `card1`;                           | :white_check_mark: |
| Test Case 3 | `[card1, card2]`            | Returns `card2`;                           | :white_check_mark: |
| Test Case 4 | `[card1, card2.1, card2.2]` | Returns `card2.2`;                         | :white_check_mark: |

## Method 3: `public Card getCardAt(int index)`

### Step 1-3 Results

|        | Input                                            | (if more to consider for input)                                                            | Output                                     |
|--------|--------------------------------------------------|--------------------------------------------------------------------------------------------|--------------------------------------------|
| Step 1 | An integer for the index                         | Deck of Cards                                                                              | Get the card at the specified Index        |
| Step 2 | Array Indices (Index are -1, 0, 1, size, size+1) | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) | Card object or Exception                   |
| Step 3 | `-1`, `0`, `1`, `size`, `size+1`                 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  | Card object or `IndexOutOfBoundsException` |

### Step 4:

|             | System under test            | Expected output / state transition | Implemented?       |
|-------------|------------------------------|------------------------------------|--------------------|
| Test Case 1 | `-1`, `[]`                   | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 2 | `-1`, `[card1]`              | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 3 | `0`, `[]`                    | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 4 | `0`, `[card1, card2]`        | Returns `card1`                    | :white_check_mark: |
| Test Case 5 | `1`, `[card1, card2, card3]` | Returns `card2`                    | :white_check_mark: |
| Test Case 6 | `2`, `[card1, card2]`        | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 7 | `3`, `[card1, card2, card3]` | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 8 | `1`, `[]`                    | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 9 | `4`, `[card1, card2, card3]` | `IndexOutOfBoundsException`        | :white_check_mark: |

**Note**: Some of the test cases for this method have its own testing implemented as well as being implemented within
other method tests. These test are labeled as methodOneAndMethodTwo.

## Method 4: `public int getDeckSize()`

### Step 1-3 Results

|        | Input                                                                                      | (if more to consider for input) | Output                             |
|--------|--------------------------------------------------------------------------------------------|---------------------------------|------------------------------------|
| Step 1 | Deck of Cards                                                                              |                                 | The number of cards that deck have |
| Step 2 | Collection (Empty, Exactly 1 Element, More than 1 Elements, Element containing duplicates) |                                 | Integer                            |
| Step 3 | `[]`, `[card1]`, `[card1, card2]`, duplicates `[card1, card2.1, card2.2]`                  |                                 | `0`, `1`, `2`, `3`                 |

### Step 4:

|             | System under test           | Expected output / state transition | Implemented?       |
|-------------|-----------------------------|------------------------------------|--------------------|
| Test Case 1 | `[]`                        | Returns `0`;                       | :white_check_mark: |
| Test Case 2 | `[card1]`                   | Returns `1`;                       | :white_check_mark: |
| Test Case 3 | `[card1, card2]`            | Returns `2`;                       | :white_check_mark: |
| Test Case 4 | `[card1, card2.1, card2.2]` | Returns `3`;                       | :white_check_mark: |

## Method under test: `insertCardAt(Card card, Index index)`

|             | System under test (pre-state)                                                       | Expected output / state transition                                          | Implemented?       |
|-------------|-------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|--------------------|
| Test Case 1 | `index < 0` (empty list)     `[]`                                                   | `IndexOutOfBoundsException`                                                 | :white_check_mark: |
| Test Case 2 | `index < 0` (non-empty list) `[card1]`                                              | `IndexOutOfBoundsException`                                                 | :white_check_mark: |
| Test Case 3 | `index == 0` (front of deck) (empty list)  `[]`                                     | Card inserted at top; deck `size` increments by 1 `[card]`                  | :white_check_mark: |
| Test Case 4 | `index == 0` (front of deck) (non-empty list)  `[card1, card2]`                     | Card inserted at top; deck `size` increments by 1 `[card, card1, card2]`    | :white_check_mark: |
| Test Case 4 | `index == 1`  (non-empty list)  `[card1, card2]`                                    | Card inserted at top; deck `size` increments by 1 `[card1, card, card2]`    | :white_check_mark: |
| Test Case 5 | `index == size` (exactly last position / bottom)  (non-empty list) `[card1, card2]` | Card appended at bottom; deck `size` increments by 1 `[card1, card2, card]` | :white_check_mark: |
| Test Case 6 | `index > size` (empty list)   `[]`                                                  | `IndexOutOfBoundsException`                                                 | :white_check_mark: |
| Test Case 7 | `index > size` (non-empty list) `[card1, card2]`                                    | `IndexOutOfBoundsException`                                                 | :white_check_mark: |
| Test Case 8 | `card == null` (empty list)    `[]`                                                 | `NullPointerException`                                                      | :white_check_mark: |
| Test Case 9 | `card == null` (non-empty list) `[card1, card2]`                                    | `NullPointerException`                                                      | :white_check_mark: |

## Method under test: `shuffleDeck(Random rand)`

|             | System under test                                           | Expected output                                                                                                       | Implemented?       |
|-------------|-------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | Deck size **0** `[]`                                        | Order remains unchanged (idempotent) `[]`                                                                             | :white_check_mark: |
| Test Case 2 | Deck size **1** `[card1]`                                   | Order remains unchanged (idempotent) `[card1]`                                                                        | :white_check_mark: |
| Test Case 3 | Deck size **> 1** [card1, card2]                            | Order changes _or_ remains statistically different over many runs; deck content invariant `[card2, card1]`            | :white_check_mark: |
| Test Case 3 | Deck size **> 1** [card1, card2, card3]                     | Order changes _or_ remains statistically different over many runs; deck content invariant `[card3, card1, card2]`     | :white_check_mark: |
| Test Case 3 | Deck size **> 1** and duplicate `[card1, card2.1, card2.2]` | Order changes _or_ remains statistically different over many runs; deck content invariant `[card2.1, card2.2, card1]` | :white_check_mark: |

## Method under test: `giveCardToPlayer(Player p)`

|             | System under test (pre-state)                                                  | Expected output / state transition                                                                      | Implemented? |
|-------------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | `p == null`                                                                    | `NullPointerException`                                                                                  |              |
| Test Case 2 | Deck `size == 0`                                                               | `NoSuchElementException` (“Deck is empty”)                                                              |              |
| Test Case 3 | `p.hand.size() == Player.MAX_HAND`                                             | `IllegalStateException` (“Hand already full”)                                                           |              |
| Test Case 4 | Deck `size == 1`; player hand has space                                        | Top card moves to `p`; deck `size` becomes 0; `p.hand.size()` increments by 1                           |              |
| Test Case 5 | Deck `size > 1`; top card is **not** `EXPLODING_KITTEN`; player hand has space | Card transferred; deck `size--`; `p.hand.size()++`; method returns the card (or `void` per your design) |              |
