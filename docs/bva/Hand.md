# BVA for Hand Class

Note: I am using Parametrized Testing so whenever I use testCardType1 or testCard1, it means that the test runs for all the card types or for cards with all the card types. testCardType2 and testCard2 refer to all possible card types except Exploding Kitten and cards with those types.
## Method under test: `isEmpty()`
### Step 1-3 Results
|        | Input                                                                                       | Output          |
|--------|---------------------------------------------------------------------------------------------|-----------------|
| Step 1 | the state of the list                                                                       | a yes/no answer |
| Step 2 | Collection                                                                                  | Boolean         |
| Step 3 | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | true, false     |
### Step 4:
|             | System under test                | Expected output | Implemented?       | Test name                               |
|-------------|----------------------------------|-----------------|--------------------|-----------------------------------------|
| Test Case 1 | Hand `[]`                        | `true`          | :white_check_mark: | isEmpty_withEmptyHand_returnsTrue       |
| Test Case 2 | Hand `[testCard2]`               | `false`         | :white_check_mark: | isEmpty_withOneCardInHand_returnsFalse  |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]` | `false`         | :white_check_mark: | isEmpty_withTwoCardsInHand_returnsFalse |

## Method under test: `containsCardType(CardType cardType)`
### Step 1-3 Results
|        | Input 1                                                            | Input 2                                                                                                | (if more to consider for input)                                                             | Output                              |
|--------|--------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|-------------------------------------|
| Step 1 | a card type                                                        | a card type                                                                                            | the state of the list                                                                       | a yes/no answer or exception        |
| Step 2 | Cases                                                              | Cases                                                                                                  | Collection                                                                                  | Boolean or exception                |
| Step 3 | null pointer, not in the list; in the list once; in the list twice | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | true, false or NullPointerException |
### Step 4:
|             | System under test                                   | Expected output                                     | Implemented?       | Test name                                                    |
|-------------|-----------------------------------------------------|-----------------------------------------------------|--------------------|--------------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, cardType `null`                    | `NullPointerException`  ("CardType cannot be null") | :white_check_mark: | containsCardType_withNullCardType_throwsNullPointerException |
| Test Case 2 | Hand `[]`, cardType `testCardType1`                 | `false`                                             | :white_check_mark: | containsCardType_withEmptyHand_returnsFalse                  |
| Test Case 3 | Hand `[testCard2]`, cardType `testCardType2`        | `true`                                              | :white_check_mark: | containsCardType_withCardInHand_returnsTrue                  |
| Test Case 4 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, cardType `DEFUSE` | `false`                                             | :white_check_mark: | containsCardType_withTwoOtherCardsInHand_returnsFalse        |
| Test Case 5 | Hand `[SKIP, NORMAL, NORMAL]`, cardType `NORMAL`    | `true`                                              | :white_check_mark: | containsCardType_withDuplicatesInHand_returnsTrue            |

## Method under test: `getNumberOfCards()`
### Step 1-3 Results
|        | Input                                                                                       | Output                      |
|--------|---------------------------------------------------------------------------------------------|-----------------------------|
| Step 1 | the state of the list                                                                       | number of cards in the list |
| Step 2 | Collection                                                                                  | Count                       |
| Step 3 | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | 0; 1; >1                    |
### Step 4:
|             | System under test                | Expected output | Implemented?       | Test name                                                       |
|-------------|----------------------------------|-----------------|--------------------|-----------------------------------------------------------------|
| Test Case 1 | Hand `[]`                        | 0               | :white_check_mark: | getNumberOfCards_withEmptyHand_returnsZero                      |
| Test Case 2 | Hand `[testCard2]`               | 1               | :white_check_mark: | getNumberOfCards_withOneCardInHand_returnsOne                   |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]` | 2               | :white_check_mark: | getNumberOfCards_withTwoCardsInHand_returnsTwo                  |
| Test Case 4 | Hand `[SKIP, NORMAL, NORMAL]`    | 3               | :white_check_mark: | getNumberOfCards_withThreeCardsInHandAndDuplicates_returnsThree |

## Method under test: `addCard(Card card)`
### Step 1-3 Results
|        | Input 1                                    | Input 2                                                                                                | (if more to consider for input)                                | Output                                                                                                                                        |
|--------|--------------------------------------------|--------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | a card with a certain card type            | a card type                                                                                            | the state of the list                                          | the state of the list or exception                                                                                                            |
| Step 2 | Cases                                      | Cases                                                                                                  | Collection                                                     | Collection or exception                                                                                                                       |
| Step 3 | null pointer, not in the list; in the list | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element | empty list; list with 1 element; list with more than 1 element; contains duplicate elements or NullPointerException, IllegalArgumentException |
### Step 4:
|             | System under test (pre-state)                           | Expected output / state transition                                          | Implemented?       | Test name                                             |
|-------------|---------------------------------------------------------|-----------------------------------------------------------------------------|--------------------|-------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                            | `NullPointerException` ("Card cannot be null")                              | :white_check_mark: | addCard_withNullCard_throwsNullPointerException       |
| Test Case 2 | Hand `[]`, card `testCard2`                             | Hand `[testCard2]`                                                          | :white_check_mark: | addCard_toEmptyHand_insertsCard                       |
| Test Case 3 | Hand `[testCard2]`, card `SKIP`                         | Hand `[testCard2, SKIP]`                                                    | :white_check_mark: | addCard_toHandWithOneCard_insertsCard                 |
| Test Case 4 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `SEE_THE_FUTURE` | Hand `[SEE_THE_FUTURE, SHUFFLE, SEE_THE_FUTURE]`                            | :white_check_mark: | addCard_toHandWithSameCard_insertsDuplicateCard       |
| Test Case 5 | Hand `[testCard2]`, card `EXPLODING_KITTEN`             | `IllegalArgumentException` ("Exploding Kitten should not be added to Hand") | :white_check_mark: | addExplodingKittenCard_throwsIllegalArgumentException |

## Method under test: `removeCard(Card card)`
### Step 1-3 Results
|        | Input 1                                                            | Input 2                                                                                                | (if more to consider for input)                                                             | Output                                                                                                                                  |
|--------|--------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------|
| Step 1 | a card with a certain card type                                    | a card type                                                                                            | the state of the list                                                                       | the state of the list or exception                                                                                                      |
| Step 2 | Cases                                                              | Cases                                                                                                  | Collection                                                                                  | Collection or exception                                                                                                                 |
| Step 3 | null pointer, not in the list; in the list once; in the list twice | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | empty list; list with 1 element; list with more than 1 element or NullPointerException, IllegalArgumentException, IllegalStateException |
### Step 4:
|             | System under test (pre-state)                           | Expected output / state transition                                   | Implemented?       | Test name                                                   |
|-------------|---------------------------------------------------------|----------------------------------------------------------------------|--------------------|-------------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                            | `NullPointerException`  ("Card cannot be null")                      | :white_check_mark: | removeCard_withNullCard_throwsNullPointerException          |
| Test Case 2 | Hand `[]`, card `testCard1`                             | `IllegalStateException` (“Hand empty: can not remove card”)          | :white_check_mark: | removeCard_withEmptyHand_throwsIllegalStateException        |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `DEFUSE`         | `IllegalArgumentException` (“Card not in hand: can not remove card”) | :white_check_mark: | removeCard_withCardNotInHand_throwsIllegalArgumentException |
| Test Case 4 | Hand `[testCard2]`, card `testCard2`                    | Hand `[]`                                                            | :white_check_mark: | removeCard_withOneCardInHand_emptiesHand                    |
| Test Case 5 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `SEE_THE_FUTURE` | Hand `[SHUFFLE]`                                                     | :white_check_mark: | removeCard_withTwoCardsInHand_removesCard                   |
| Test Case 6 | Hand `[SKIP, NORMAL, NORMAL]`, card `NORMAL`            | Hand `[SKIP, NORMAL]`                                                | :white_check_mark: | removeCard_withDuplicateCardsInHand_removesOnlyOneCard      |

## Method under test: `getCountOfCardType(CardType cardType)`
### Step 1-3 Results
|        | Input 1                                                                                         | Input 2                                                                                                | (if more to consider for input)                                                             | Output                                    |
|--------|-------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|-------------------------------------------|
| Step 1 | a card type                                                                                     | a card type                                                                                            | the state of the list                                                                       | number of cards of that type or exception |
| Step 2 | Cases                                                                                           | Cases                                                                                                  | Collection                                                                                  | Count or exception                        |
| Step 3 | null pointer, not in the list; in the list once; in the list twice; in the list more than twice | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | 0; 1; >1 or NullPointerException          |
### Step 4:
|             | System under test                                              | Expected output                                     | Implemented?       | Test name                                                      |
|-------------|----------------------------------------------------------------|-----------------------------------------------------|--------------------|----------------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, cardType `null`                               | `NullPointerException`  ("CardType cannot be null") | :white_check_mark: | getCountOfCardType_withNullCardType_throwsNullPointerException |
| Test Case 2 | Hand `[]`, cardType `testCardType1`                            | 0                                                   | :white_check_mark: | getCountOfCardType_withEmptyHand_returnsZero                   |
| Test Case 3 | Hand `[testCard2]`, cardType `testCardType2`                   | 1                                                   | :white_check_mark: | getCountOfCardType_withCardInHand_returnsOne                   |
| Test Case 4 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, cardType `DEFUSE`            | 0                                                   | :white_check_mark: | getCountOfCardType_withCardNotInHand_returnsZero               |
| Test Case 5 | Hand `[SKIP, NORMAL, NORMAL]`, cardType `NORMAL`               | 2                                                   | :white_check_mark: | getCountOfCardType_withTwoDuplicateCardsInHand_returnsTwo      |
| Test Case 6 | Hand `[FAVOR, FAVOR, DEFUSE, ATTACK, FAVOR]`, cardType `FAVOR` | 3                                                   | :white_check_mark: | getCountOfCardType_withThreeDuplicateCardsInHand_returnsThree  |

## Method under test: `removeDefuseCard()`
### Step 1-3 Results
|        | Input                                                                                   | Output                                                                                                         |
|--------|-----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| Step 1 | the state of the list                                                                   | the state of the list or exception                                                                             |
| Step 2 | Collection                                                                              | Collection or exception                                                                                        |
| Step 3 | empty list; list without DEFUSE card; list with DEFUSE card; list with two DEFUSE cards | empty list; list without DEFUSE card; list with DEFUSE card or IllegalStateException, IllegalArgumentException |

### Step 4:
|             | System under test (pre-state)    | Expected output / state transition                                   | Implemented? | Test name |
|-------------|----------------------------------|----------------------------------------------------------------------|--------------|-----------|
| Test Case 1 | Hand `[]`                        | `IllegalStateException` (“Hand empty: can not remove card”)          |              |           |
| Test Case 2 | Hand `[SEE_THE_FUTURE]`          | `IllegalArgumentException` (“Card not in hand: can not remove card”) |              |           |
| Test Case 3 | Hand `[DEFUSE, FAVOR]`           | Hand `[FAVOR]`                                                       |              |           |
| Test Case 4 | Hand `[DEFUSE, SHUFFLE, DEFUSE]` | Hand `[DEFUSE, SHUFFLE]`                                             |              |           |