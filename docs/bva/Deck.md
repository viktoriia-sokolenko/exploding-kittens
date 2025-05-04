## Deck class BVA

### Method under test: `draw()`

|             | System under test                     | Expected output / state transition                 | Implemented?       |
|-------------|---------------------------------------|----------------------------------------------------|--------------------|
| Test Case 1 | Deck **empty** `[]`                     | `NoSuchElementException` (“Deck is empty”)         | :white_check_mark: |
| Test Case 2 | Deck has **exactly 1** card `[Card1]`   | Returns `Card1`; deck `size = 0` []                  | :white_check_mark: |
| Test Case 3 | Deck has **> 1** cards `[Card1, Card2]` | Returns `Card2`; deck `size` decrements by 1 [Card1] | :white_check_mark: |

### Method under test: `peekTop()`

|             | System under test                            | Expected output / state transition                           | Implemented? |
|-------------|----------------------------------------------|--------------------------------------------------------------|--------------|
| Test Case 1 | Deck **empty** `[]`                            | `IllegalOperationException` (“Cannot peak at an empty deck”) |              |
| Test Case 2 | Deck has **exactly 1** card `[Card1]`          | Returns `Card1`;                                               |              |
| Test Case 3 | Deck has **> 1** cards `[Card1, Card2]`        | Returns `Card2`;                                               |              |
| Test Case 4 | Deck has **> 1** cards `[Card1, Card2, Card3]` | Returns `Card3`;                                               |              |

### Method under test: `getDeckSize()`

|             | System under test                            | Expected output / state transition | Implemented?       |
|-------------|----------------------------------------------|------------------------------------|--------------------|
| Test Case 1 | Deck **empty** `[]`                            | Returns `0`;                         | :white_check_mark: |
| Test Case 2 | Deck has **exactly 1** card `[Card1]`          | Returns `1`;                         | :white_check_mark: |
| Test Case 3 | Deck has **> 1** cards `[Card1, Card2]`        | Returns `2`;                         |                    |
| Test Case 4 | Deck has **> 1** cards `[Card1, Card1]`        | Returns `2`;                         |                    |
| Test Case 5 | Deck has **> 1** cards `[Card1, Card2, Card3]` | Returns `3`;                         |                    |

### Method under test: `insertAt(int index, Card c)`

|             | System under test (pre-state)                                      | Expected output / state transition                   | Implemented?       |
|-------------|--------------------------------------------------------------------|------------------------------------------------------|--------------------|
| Test Case 1 | `index < 0` (empty list)     `[]`                                    | `IndexOutOfBoundsException`                          | :white_check_mark: |
| Test Case 2 | `index < 0` (non-empty list) `[Card1]`                               | `IndexOutOfBoundsException`                          | :white_check_mark: |
| Test Case 3 | `index == 0` (front of deck) (empty list)                          | Card inserted at top; deck `size` increments by 1    |                    |
| Test Case 4 | `index == 0` (front of deck) (non-empty list)                      | Card inserted at top; deck `size` increments by 1    |                    |
| Test Case 5 | `index == size` (exactly last position / bottom)  (non-empty list) | Card appended at bottom; deck `size` increments by 1 |                    |
| Test Case 6 | `index > size` (empty list)   `[]`                                   | `IndexOutOfBoundsException`                          | :white_check_mark: |
| Test Case 7 | `index > size` (non-empty list) `[Card1, Card2]`                     | `IndexOutOfBoundsException`                          | :white_check_mark: |
| Test Case 8 | `c == null` (empty list)    `[]`                                     | `NullPointerException`                               | :white_check_mark: |
| Test Case 9 | `c == null` (non-empty list) `[Card1, Card2]`                        | `NullPointerException`                               | :white_check_mark: |

NEED MORE CARDS CASES FOR THE DIFFERENT CARDS

- Card Cases:
- NORMAL,
- EXPLODING_KITTEN,
- DEFUSE,
- ATTACK,
- SKIP,
- FAVOR,
- SHUFFLE,
- SEE_THE_FUTURE,
- ALTER_THE_FUTURE,
- NUKE

### Method under test: `shuffle()`

|             | System under test       | Expected output                                                                           | Implemented? |
|-------------|-------------------------|-------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Deck size **0** `[]`      | Order remains unchanged (idempotent) `[]`                                                   |              |
| Test Case 2 | Deck size **1** `[Card1]` | Order remains unchanged (idempotent) `[Card1]`                                              |              |
| Test Case 3 | Deck size **> 1**       | Order changes _or_ remains statistically different over many runs; deck content invariant |              |

// Need to figure how shuffle should work

### Method under test: `giveCardToPlayer(Player p)`

|             | System under test (pre-state)                                                  | Expected output / state transition                                                                      | Implemented? |
|-------------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | `p == null`                                                                    | `NullPointerException`                                                                                  |              |
| Test Case 2 | Deck `size == 0`                                                               | `NoSuchElementException` (“Deck is empty”)                                                              |              |
| Test Case 3 | `p.hand.size() == Player.MAX_HAND`                                             | `IllegalStateException` (“Hand already full”)                                                           |              |
| Test Case 4 | Deck `size == 1`; player hand has space                                        | Top card moves to `p`; deck `size` becomes 0; `p.hand.size()` increments by 1                           |              |
| Test Case 5 | Deck `size > 1`; top card is **not** `EXPLODING_KITTEN`; player hand has space | Card transferred; deck `size--`; `p.hand.size()++`; method returns the card (or `void` per your design) |              |
