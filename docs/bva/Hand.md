# BVA for Hand Class

Note: I am using Parametrized Testing so whenever I use testCard or testCardType, it means that the test runs for all the following card types or for cards with those card types: NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE. 
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
| Test Case 2 | Hand `[testCard]`                | `false`         | :white_check_mark: | isEmpty_withOneCardInHand_returnsFalse  |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]` | `false`         | :white_check_mark: | isEmpty_withTwoCardsInHand_returnsFalse |

## Method under test: `containsCardType(CardType cardType)`
### Step 1-3 Results
|        | Input 1                                                            | Input 2                                                                                                | (if more to consider for input)                                                             | Output          |
|--------|--------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|-----------------|
| Step 1 | a card type                                                        | a card type                                                                                            | the state of the list                                                                       | a yes/no answer |
| Step 2 | Cases                                                              | Cases                                                                                                  | Collection                                                                                  | Boolean         |
| Step 3 | null pointer, not in the list; in the list once; in the list twice | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | true, false     |
### Step 4:
|             | System under test                                   | Expected output                                     | Implemented?       | Test name                                                    |
|-------------|-----------------------------------------------------|-----------------------------------------------------|--------------------|--------------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, cardType `null`                    | `NullPointerException`  ("CardType cannot be null") | :white_check_mark: | containsCardType_withNullCardType_throwsNullPointerException |
| Test Case 2 | Hand `[]`, cardType `testCardType`                  | `false`                                             | :white_check_mark: | containsCardType_withEmptyHand_returnsFalse                  |
| Test Case 3 | Hand `[testCard]`, cardType `testCardType`          | `true`                                              | :white_check_mark: | containsCardType_withCardInHand_returnsTrue                  |
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
|             | System under test                              | Expected output | Implemented?       | Test name                                                       |
|-------------|------------------------------------------------|-----------------|--------------------|-----------------------------------------------------------------|
| Test Case 1 | Hand `[]`                                      | 0               | :white_check_mark: | getNumberOfCards_withEmptyHand_returnsZero                      |
| Test Case 2 | Hand `[testCard]`                              | 1               | :white_check_mark: | getNumberOfCards_withOneCardInHand_returnsOne                   |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`               | 2               | :white_check_mark: | getNumberOfCards_withTwoCardsInHand_returnsTwo                  |
| Test Case 4 | Hand `[SKIP, NORMAL, NORMAL]`                  | 3               | :white_check_mark: | getNumberOfCards_withThreeCardsInHandAndDuplicates_returnsThree |

## Method under test: `addCard(Card card)`
### Step 1-3 Results
|        | Input 1                                    | Input 2                                                                                                | (if more to consider for input)                                | Output                                                                                      |
|--------|--------------------------------------------|--------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| Step 1 | a card with a certain card type            | a card type                                                                                            | the state of the list                                          | the state of the list                                                                       |
| Step 2 | Cases                                      | Cases                                                                                                  | Collection                                                     | Collection                                                                                  |
| Step 3 | null pointer, not in the list; in the list | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element | empty list; list with 1 element; list with more than 1 element; contains duplicate elements |
### Step 4:
|             | System under test (pre-state)                           | Expected output / state transition               | Implemented?       | Test name                                       |
|-------------|---------------------------------------------------------|--------------------------------------------------|--------------------|-------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                            | `NullPointerException` ("Card cannot be null")   | :white_check_mark: | addCard_withNullCard_throwsNullPointerException |
| Test Case 2 | Hand `[]`, card `testCard`                              | Hand `[testCard]`                                | :white_check_mark: | addCard_toEmptyHand_insertsCard                 |
| Test Case 3 | Hand `[testCard]`, card `SKIP`                          | Hand `[testCard, SKIP]`                          | :white_check_mark: | addCard_toHandWithOneCard_insertsCard           |
| Test Case 4 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `SEE_THE_FUTURE` | Hand `[SEE_THE_FUTURE, SHUFFLE, SEE_THE_FUTURE]` | :white_check_mark: | addCard_toHandWithSameCard_insertsDuplicateCard |

## Method under test: `removeCard(Card card)`
### Step 1-3 Results
|        | Input 1                                                            | Input 2                                                                                                | (if more to consider for input)                                                             | Output                                                         |
|--------|--------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| Step 1 | a card with a certain card type                                    | a card type                                                                                            | the state of the list                                                                       | the state of the list                                          |
| Step 2 | Cases                                                              | Cases                                                                                                  | Collection                                                                                  | Collection                                                     |
| Step 3 | null pointer, not in the list; in the list once; in the list twice | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | empty list; list with 1 element; list with more than 1 element |
### Step 4:
|             | System under test (pre-state)                           | Expected output / state transition                                   | Implemented?       | Test name                                                   |
|-------------|---------------------------------------------------------|----------------------------------------------------------------------|--------------------|-------------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, card `null`                            | `NullPointerException`  ("Card cannot be null")                      | :white_check_mark: | removeCard_withNullCard_throwsNullPointerException          |
| Test Case 2 | Hand `[]`, card `testCard`                              | `IllegalStateException` (“Hand empty: can not remove card”)          | :white_check_mark: | removeCard_withEmptyHand_throwsIllegalStateException        |
| Test Case 3 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `DEFUSE`         | `IllegalArgumentException` (“Card not in hand: can not remove card”) | :white_check_mark: | removeCard_withCardNotInHand_throwsIllegalArgumentException |
| Test Case 4 | Hand `[testCard]`, card `testCard`                      | Hand `[]`                                                            | :white_check_mark: | removeCard_withOneCardInHand_emptiesHand                    |
| Test Case 5 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, card `SEE_THE_FUTURE` | Hand `[SHUFFLE]`                                                     | :white_check_mark: | removeCard_withTwoCardsInHand_removesCard                   |
| Test Case 6 | Hand `[SKIP, NORMAL, NORMAL]`, card `NORMAL`            | Hand `[SKIP, NORMAL]`                                                | :white_check_mark: | removeCard_withDuplicateCardsInHand_removesOnlyOneCard      |

## Method under test: `getCountOfCardType(CardType cardType)`
### Step 1-3 Results
|        | Input 1                                                                                         | Input 2                                                                                                | (if more to consider for input)                                                             | Output                       |
|--------|-------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|------------------------------|
| Step 1 | a card type                                                                                     | a card type                                                                                            | the state of the list                                                                       | number of cards of that type |
| Step 2 | Cases                                                                                           | Cases                                                                                                  | Collection                                                                                  | Count                        |
| Step 3 | null pointer, not in the list; in the list once; in the list twice; in the list more than twice | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | empty list; list with 1 element; list with more than 1 element; contains duplicate elements | 0; 1; >1                     |
### Step 4:
|             | System under test                                              | Expected output                                     | Implemented?       | Test name                                                      |
|-------------|----------------------------------------------------------------|-----------------------------------------------------|--------------------|----------------------------------------------------------------|
| Test Case 1 | Hand `[ATTACK]`, cardType `null`                               | `NullPointerException`  ("CardType cannot be null") | :white_check_mark: | getCountOfCardType_withNullCardType_throwsNullPointerException |
| Test Case 2 | Hand `[]`, cardType `testCardType`                             | 0                                                   | :white_check_mark: | getCountOfCardType_withEmptyHand_returnsZero                   |
| Test Case 3 | Hand `[testCard]`, cardType `testCardType`                     | 1                                                   | :white_check_mark: | getCountOfCardType_withCardInHand_returnsOne                   |
| Test Case 4 | Hand `[SEE_THE_FUTURE, SHUFFLE]`, cardType `DEFUSE`            | 0                                                   | :white_check_mark: | getCountOfCardType_withCardNotInHand_returnsZero               |
| Test Case 5 | Hand `[SKIP, NORMAL, NORMAL]`, cardType `NORMAL`               | 2                                                   | :white_check_mark: | getCountOfCardType_withTwoDuplicateCardsInHand_returnsTwo      |
| Test Case 6 | Hand `[FAVOR, FAVOR, DEFUSE, ATTACK, FAVOR]`, cardType `FAVOR` | 3                                                   | :white_check_mark: | getCountOfCardType_withThreeDuplicateCardsInHand_returnsThree  |