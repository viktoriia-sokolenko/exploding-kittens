## Hand class BVA

### Method under test: `isEmpty()`

|             | System under test                | Expected output | Implemented?       | Test name                               |
|-------------|----------------------------------|-----------------|--------------------|-----------------------------------------|
| Test Case 1 | Hand `[]`                        | `true`          | :white_check_mark: | isEmpty_onEmptyHand_returnsTrue         |
| Test Case 2 | Hand `[mockCard]`                | `false`         | :white_check_mark: | isEmpty_withOneCardInHand_returnsFalse  |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]` | `false`         | :white_check_mark: | isEmpty_withTwoCardsInHand_returnsFalse |

### Method under test: `containsCardType(CardType cardType)`

|             | System under test                                   | Expected output | Implemented?       | Test name                                             |
|-------------|-----------------------------------------------------|-----------------|--------------------|-------------------------------------------------------|
| Test Case 1 | Hand `[]`, cardType `NUKE`                          | `false`         | :white_check_mark: | containsCardType_onEmptyHand_returnsFalse             |
| Test Case 2 | Hand `[ATTACK]`, cardType `ATTACK`                  | `true`          | :white_check_mark: | containsCardType_withCardInHand_returnsTrue           |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, cardType `DEFUSE` | `false`         | :white_check_mark: | containsCardType_withTwoOtherCardsInHand_returnsFalse |
| Test Case 4 | Hand `[SKIP, NORMAL, NORMAL]`, cardType `NORMAL`    | `true`          | :white_check_mark: | containsCardType_withDuplicatesInHand_returnsTrue     |

### Method under test: `getNumberOfCards()`

|             | System under test                | Expected output | Implemented?       | Test name                                                       |
|-------------|----------------------------------|-----------------|--------------------|-----------------------------------------------------------------|
| Test Case 1 | Hand `[]`                        | 0               | :white_check_mark: | getNumberOfCards_onEmptyHand_returnsZero                        |
| Test Case 2 | Hand `[ATTACK]`                  | 1               | :white_check_mark: | getNumberOfCards_withOneCardInHand_returnsOne                   |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]` | 2               | :white_check_mark: | getNumberOfCards_withTwoCardsInHand_returnsTwo                  |
| Test Case 4 | Hand `[SKIP, NORMAL, NORMAL]`    | 3               | :white_check_mark: | getNumberOfCards_withThreeCardsInHandAndDuplicates_returnsThree |

### Method under test: `addCard(Card card)`

|             | System under test (pre-state)                           | Expected output / state transition               | Implemented?       | Test name                                       |
|-------------|---------------------------------------------------------|--------------------------------------------------|--------------------|-------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                            | `NullPointerException` ("Card can not be null")  | :white_check_mark: | addCard_withNullCard_throwsNullPointerException |
| Test Case 2 | Hand `[]`, card `FAVOR`                                 | Hand `[FAVOR]`                                   | :white_check_mark: | addCard_toEmptyHand_insertsCard                 |
| Test Case 3 | Hand `[ATTACK]`, card `SKIP`                            | Hand `[ATTACK, SKIP]`                            | :white_check_mark: | addCard_toHandWithOneCard_insertsCard           |
| Test Case 4 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `SEE_THE_FUTURE` | Hand `[SEE_THE_FUTURE, SHUFFLE, SEE_THE_FUTURE]` | :white_check_mark: | addCard_toHandWithSameCard_insertsDuplicateCard |

### Method under test: `removeCard(Card card)`

|             | System under test (pre-state)                           | Expected output / state transition                                    | Implemented?       | Test name                                                   |
|-------------|---------------------------------------------------------|-----------------------------------------------------------------------|--------------------|-------------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                            | `NullPointerException`  ("Card can not be null")                      | :white_check_mark: | removeCard_withNullCard_throwsNullPointerException          |
| Test Case 2 | Hand `[]`, card `FAVOR`                                 | `IllegalStateException` (“Hand empty - can not remove card”)          | :white_check_mark: | removeCard_withEmptyHand_throwsIllegalStateException        |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `DEFUSE`         | `IllegalArgumentException` (“Card not in hand - can not remove card”) | :white_check_mark: | removeCard_withCardNotInHand_throwsIllegalArgumentException |
| Test Case 4 | Hand `[ATTACK]`, card `ATTACK`                          | Hand `[]`                                                             | :white_check_mark: | removeCard_withOneCardInHand_emptiesHand                    |
| Test Case 5 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `SEE_THE_FUTURE` | Hand `[SHUFFLE]`                                                      | :white_check_mark: | removeCard_withTwoCardsInHand_removesHand                   |
| Test Case 6 | Hand `[SKIP, NORMAL, NORMAL]`, card `NORMAL`            | Hand `[SKIP, NORMAL]`                                                 | :white_check_mark: | removeCard_withDuplicateCardsInHand_removesOnlyOneCard      |

### Method under test: `getCountOfCardType(CardType cardType)`

|             | System under test                                              | Expected output | Implemented?       | Test name                                                 |
|-------------|----------------------------------------------------------------|-----------------|--------------------|-----------------------------------------------------------|
| Test Case 1 | Hand `[]`, cardType `SKIP`                                     | 0               | :white_check_mark: | getCountOfCardType_withEmptyHand_returnsZero              |
| Test Case 2 | Hand `[ATTACK]`, cardType `ATTACK`                             | 1               | :white_check_mark: | getCountOfCardType_withCardInHand_returnsOne              |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, cardType `DEFUSE`            | 0               | :white_check_mark: | getCountOfCardType_withCardNotInHand_returnsZero          |
| Test Case 4 | Hand `[SKIP, NORMAL, NORMAL]`, cardType `NORMAL`               | 2               | :white_check_mark: | getCountOfCardType_withTwoDuplicateCardsInHand_returnsTwo |
| Test Case 5 | Hand `[FAVOR, DEFUSE, FAVOR, ATTACK, FAVOR]`, cardType `FAVOR` | 3               |                    |