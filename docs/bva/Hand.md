## Hand class BVA

### Method under test: `isEmpty()`

|             | System under test             | Expected output | Implemented?       | Test name                               |
|-------------|-------------------------------|-----------------|--------------------|-----------------------------------------|
| Test Case 1 | Hand `[]`                     | `true`          | :white_check_mark: | isEmpty_onEmptyHand_returnsTrue         |
| Test Case 2 | Hand `[mockCard]`             | `false`         | :white_check_mark: | isEmpty_withOneCardInHand_returnsFalse  |
| Test Case 3 | Hand `[mockCard1, mockCard2]` | `false`         | :white_check_mark: | isEmpty_withTwoCardsInHand_returnsFalse |

### Method under test: `containsCardType(CardType cardType)`

|             | System under test                                   | Expected output | Implemented? | Test name |
|-------------|-----------------------------------------------------|-----------------|--------------|-----------|
| Test Case 1 | Hand `[]`, cardType `NUKE`                          | `false`         |              |
| Test Case 2 | Hand `[ATTACK]`, cardType `ATTACK`                  | `true`          |              |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, cardType `DEFUSE` | `false`         |              |
| Test Case 4 | Hand `[SKIP, NORMAL, NORMAL]`, cardType `NORMAL`    | `true`          |              |

### Method under test: `addCard(Card card)`

|             | System under test (pre-state)                    | Expected output / state transition        | Implemented? | Test name |
|-------------|--------------------------------------------------|-------------------------------------------|--------------|-----------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                     | `NullPointerException`                    |              |
| Test Case 2 | Hand `[]`, card `FAVOR`                          | Hand `[FAVOR]`                            |              |
| Test Case 3 | Hand `[ATTACK]`, card `SKIP`                     | Hand `[ATTACK, SKIP]`                     |              |
| Test Case 4 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `SHUFFLE` | Hand `[SEE_THE_FUTURE, SHUFFLE, SHUFFLE]` |              |

### Method under test: `removeCard(Card card)`

|             | System under test (pre-state)                   | Expected output / state transition                                    | Implemented? | Test name |
|-------------|-------------------------------------------------|-----------------------------------------------------------------------|--------------|-----------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                    | `NullPointerException`                                                |              |
| Test Case 2 | Hand `[]`, card `FAVOR`                         | `IllegalStateException` (“Hand empty - can not remove card”)          |              |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `DEFUSE` | `IllegalArgumentException` (“Card not in hand - can not remove card”) |              |
| Test Case 4 | Hand `[ATTACK]`, card `ATTACK`                  | Returns card `ATTACK`, Hand `[]`                                      |              |
| Test Case 5 | Hand `[SKIP, NORMAL, NORMAL]`, card `NORMAL`    | Returns card `NORMAL`, Hand `[SKIP, NORMAL]`                          |              |

### Method under test: `getNumberOfCards()`

|             | System under test                | Expected output | Implemented? | Test name |
|-------------|----------------------------------|-----------------|--------------|-----------|
| Test Case 1 | Hand `[]`                        | 0               |              |
| Test Case 2 | Hand `[ATTACK]`                  | 1               |              |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]` | 2               |              |
| Test Case 4 | Hand `[SKIP, NORMAL, NORMAL]`    | 3               |              |