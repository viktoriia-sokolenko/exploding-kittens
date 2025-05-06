## Deck class BVA

**Important Note**: Whenever you see the keywords "non-empty list" or noticed that the states takes in a list of two
cards without specifying the Card Type (i.e `[card1, card2]`), that means that the test cases uses a Parameterized Test
that uses all the different Card Types into a list of two different Cards and performs _Parameterized Tests_ based on
it.

Also, unless specified that I am dealing with  `Duplicates` states, each cards listed below are from different
CardTypes.

### Method under test: `draw()`

|             | System under test                                                 | Expected output / state transition                              | Implemented?       |
|-------------|-------------------------------------------------------------------|-----------------------------------------------------------------|--------------------|
| Test Case 1 | Deck **empty** `[]`                                               | `NoSuchElementException` (“Deck is empty”)                      | :white_check_mark: |
| Test Case 2 | Deck has **exactly 1** card `[card1]`                             | Returns `card1`; deck `size = 0` []                             | :white_check_mark: |
| Test Case 3 | Deck has **> 1** cards `[card1, card2]`                           | Returns `card2`; deck `size` decrements by 1 [card1]            | :white_check_mark: |
| Test Case 4 | Deck has **> 1** cards and Duplicates `[card1, card2.1, card2.2]` | Returns `card2.2`; deck `size` decrements by 1 [card1, card2.2] | :white_check_mark: |

### Method under test: `peekTop()`

|             | System under test                                                 | Expected output / state transition         | Implemented?       |
|-------------|-------------------------------------------------------------------|--------------------------------------------|--------------------|
| Test Case 1 | Deck **empty** `[]`                                               | `NoSuchElementException` (“Deck is empty”) | :white_check_mark: |
| Test Case 2 | Deck has **exactly 1** card `[card1]`                             | Returns `card1`;                           | :white_check_mark: |
| Test Case 3 | Deck has **> 1** cards `[card1, card2]`                           | Returns `card2`;                           | :white_check_mark: |
| Test Case 4 | Deck has **> 1** cards and Duplicated `[card1, card2.1, card2.2]` | Returns `card2.2`;                         | :white_check_mark: |

### Method under test: `getCardAt(int index)`

|             | System under test                                      | Expected output / state transition | Implemented?       |
|-------------|--------------------------------------------------------|------------------------------------|--------------------|
| Test Case 1 | `index < 0` (empty list)     `[]`                      | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 2 | `index < 0` (non-empty list) `[card1]`                 | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 3 | `index == 0`  (empty list)  `[]`                       | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 4 | `index == 0` (non-empty list)  `[card1, card2]`        | `Returns `card1`                   | :white_check_mark: |
| Test Case 5 | `index == 1` (non-empty list)  `[card1, card2, card3]` | `Returns `card2`                   | :white_check_mark: |
| Test Case 6 | `index == size`  (non-empty list) `[card1, card2]`     | `IndexOutOfBoundsException`        |                    |
| Test Case 7 | `index > 0` (empty list)   `[]`                        | `IndexOutOfBoundsException`        | :white_check_mark: |
| Test Case 8 | `index > size` (non-empty list) `[card1, card2]`       | `IndexOutOfBoundsException`        |                    |

### Method under test: `getDeckSize()`

|             | System under test                                  | Expected output / state transition | Implemented?       |
|-------------|----------------------------------------------------|------------------------------------|--------------------|
| Test Case 1 | Deck **empty** `[]`                                | Returns `0`;                       | :white_check_mark: |
| Test Case 2 | Deck has **exactly 1** card `[card1]`              | Returns `1`;                       | :white_check_mark: |
| Test Case 3 | Deck has **> 1** cards `[card1, card2]`            | Returns `2`;                       | :white_check_mark: |
| Test Case 4 | Deck has **> 1** cards `[card1, card2.1, card2.2]` | Returns `3`;                       | :white_check_mark: |

### Method under test: `insertCardAt(Card card, Index index)`

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

### Method under test: `shuffleDeck(Random rand)`

|             | System under test               | Expected output                                                                           | Implemented?       |
|-------------|---------------------------------|-------------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | Deck size **0** `[]`            | Order remains unchanged (idempotent) `[]`                                                 | :white_check_mark: |
| Test Case 2 | Deck size **1** `[card1]`       | Order remains unchanged (idempotent) `[card1]`                                            |                    |
| Test Case 3 | Deck size **> 1**               | Order changes _or_ remains statistically different over many runs; deck content invariant |                    |
| Test Case 3 | Deck size **> 1** and duplicate | Order changes _or_ remains statistically different over many runs; deck content invariant |                    |

### Method under test: `giveCardToPlayer(Player p)`

|             | System under test (pre-state)                                                  | Expected output / state transition                                                                      | Implemented? |
|-------------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | `p == null`                                                                    | `NullPointerException`                                                                                  |              |
| Test Case 2 | Deck `size == 0`                                                               | `NoSuchElementException` (“Deck is empty”)                                                              |              |
| Test Case 3 | `p.hand.size() == Player.MAX_HAND`                                             | `IllegalStateException` (“Hand already full”)                                                           |              |
| Test Case 4 | Deck `size == 1`; player hand has space                                        | Top card moves to `p`; deck `size` becomes 0; `p.hand.size()` increments by 1                           |              |
| Test Case 5 | Deck `size > 1`; top card is **not** `EXPLODING_KITTEN`; player hand has space | Card transferred; deck `size--`; `p.hand.size()++`; method returns the card (or `void` per your design) |              |
