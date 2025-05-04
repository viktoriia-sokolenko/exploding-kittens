## Deck class BVA

### Method under test: `draw()`

|             | System under test           | Expected output / state transition            | Implemented?       |
|-------------|-----------------------------|-----------------------------------------------|--------------------|
| Test Case 1 | Deck **empty**              | `NoSuchElementException` (“Deck is empty”)    | :white_check_mark: |
| Test Case 2 | Deck has **exactly 1** card | Returns that card; deck `size = 0`            | :white_check_mark: |
| Test Case 3 | Deck has **> 1** cards      | Returns top card; deck `size` decrements by 1 | :white_check_mark: |

### Method under test: `insertAt(int index, Card c)`

|             | System under test (pre-state)                    | Expected output / state transition                   | Implemented?       |
|-------------|--------------------------------------------------|------------------------------------------------------|--------------------|
| Test Case 1 | `index < 0`                                      | `IndexOutOfBoundsException`                          | :white_check_mark: |
| Test Case 2 | `index == 0` (front of deck)                     | Card inserted at top; deck `size` increments by 1    |                    |
| Test Case 3 | `index == size` (exactly last position / bottom) | Card appended at bottom; deck `size` increments by 1 |                    |
| Test Case 4 | `index > size`                                   | `IndexOutOfBoundsException`                          |                    |
| Test Case 5 | `c == null`                                      | `NullPointerException`                               |                    |

### Method under test: `shuffle()`

|             | System under test    | Expected output                                                                           | Implemented? |
|-------------|----------------------|-------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | Deck size **0 or 1** | Order remains unchanged (idempotent)                                                      |              |
| Test Case 2 | Deck size **> 1**    | Order changes _or_ remains statistically different over many runs; deck content invariant |              |

### Method under test: `giveCardToPlayer(Player p)`

|             | System under test (pre-state)                                                  | Expected output / state transition                                                                      | Implemented? |
|-------------|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | `p == null`                                                                    | `NullPointerException`                                                                                  |              |
| Test Case 2 | Deck `size == 0`                                                               | `NoSuchElementException` (“Deck is empty”)                                                              |              |
| Test Case 3 | `p.hand.size() == Player.MAX_HAND`                                             | `IllegalStateException` (“Hand already full”)                                                           |              |
| Test Case 4 | Deck `size == 1`; player hand has space                                        | Top card moves to `p`; deck `size` becomes 0; `p.hand.size()` increments by 1                           |              |
| Test Case 5 | Deck `size > 1`; top card is **not** `EXPLODING_KITTEN`; player hand has space | Card transferred; deck `size--`; `p.hand.size()++`; method returns the card (or `void` per your design) |              |
