# BVA Analysis for Player

#### Important Note

I am using Parametrized Testing, so whenever I use `testCardType1` or `testCard1`, it means that the test runs for all
the card types or for cards with all the card types. Nonetheless, `testCardType2` and `testCard2` refer to **all**
possible card types, **except** `Exploding Kitten`, and cards with those types.

## Method under test: `getCardTypeCount(CardType cardType)`

### Step 1-3 Results

|        | Input 1                                                                                                      | Input 2                           | Output                                    |
|--------|--------------------------------------------------------------------------------------------------------------|-----------------------------------|-------------------------------------------|
| Step 1 | a card type                                                                                                  | the state of the hand             | number of cards of that type or exception |
| Step 2 | Cases                                                                                                        | Cases                             | Count or exception                        |
| Step 3 | null, NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | has 0, 1, >1 cards with such type | 0; 1; >1 or NullPointerException          |

### Step 4:

|             | System under test               | Expected output                                     | Implemented?       | Test name                                                    |
|-------------|---------------------------------|-----------------------------------------------------|--------------------|--------------------------------------------------------------|
| Test Case 1 | cardType `null`                 | `NullPointerException`  ("CardType cannot be null") | :white_check_mark: | getCardTypeCount_withNullCardType_throwsNullPointerException |
| Test Case 2 | Hand with **0** `testCardType1` | `0`                                                 | :white_check_mark: | getCardTypeCount_withCardNotInHand_returnsZero               |
| Test Case 3 | Hand with **1** `testCardType1` | `1`                                                 | :white_check_mark: | getCardTypeCount_withOneCardInHand_returnsOne                |
| Test Case 4 | Hand with **2** `testCardType1` | `2`                                                 | :white_check_mark: | getCardTypeCount_withTwoCardsInHand_returnsTwo               |

## Method under test: `drawCard(Deck deck)`

### Step 1-3 Results

|        | Input 1                                   | Input 2                                                                                                | Input 3                                    | Output 1                                                      | Output 2                              |
|--------|-------------------------------------------|--------------------------------------------------------------------------------------------------------|--------------------------------------------|---------------------------------------------------------------|---------------------------------------|
| Step 1 | the state of the deck list (empty or not) | card drawn from the deck                                                                               | the state of hand (has Defuse card or not) | the state of the hand (does hand has drawn card) or exception | is player in the game (yes/no answer) |
| Step 2 | Boolean                                   | Cases                                                                                                  | Boolean                                    | Boolean or exception                                          | Boolean                               |
| Step 3 | true, false                               | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | true, false                                | true, false, NoSuchElementException                           | true, false                           |

### Step 4:

|             | System under test                                                     | Expected output                                    | Implemented?       | Test name                                                             |
|-------------|-----------------------------------------------------------------------|----------------------------------------------------|--------------------|-----------------------------------------------------------------------|
| Test Case 1 | deck `[]`                                                             | `NoSuchElementException` (“Deck is empty”)         | :white_check_mark: | drawCard_withEmptyDeck_throwsNoSuchElementException                   |
| Test Case 2 | deck `non-empty`, card `testCard2`                                    | Hand `[...testCard2...]`                           | :white_check_mark: | drawCard_withNonEmptyDeck_addsCardToHand                              |
| Test Case 3 | deck `non-empty`, card `EXPLODING_KITTEN`, hand `[...DEFUSE...}`      | Hand `without DEFUSE card`,  Player is in the game | :white_check_mark: | drawExplodingKittenCard_withDefuseInHand_insertsDrawnCardBackIntoDeck |
| Test Case 4 | deck `non-empty`, card `EXPLODING_KITTEN`, hand `without DEFUSE card` | Player is not in the game                          | :white_check_mark: | drawExplodingKittenCard_withDefuseNotInHand_removesPlayer             |

## Method under test: `removeCardFromHand(Card card)`

### Step 1-3 Results

|        | Input 1                                    | Input 2                                                                                                | Input 3                              | Output                                                                                                |
|--------|--------------------------------------------|--------------------------------------------------------------------------------------------------------|--------------------------------------|-------------------------------------------------------------------------------------------------------|
| Step 1 | a card with a certain card type            | a card type                                                                                            | the state of the hand (empty or not) | the state of the hand or exception                                                                    |
| Step 2 | Cases                                      | Cases                                                                                                  | Boolean                              | Boolean or exception                                                                                  |
| Step 3 | null pointer, not in the hand; in the hand | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | true, false                          | Has card, does not have card or NullPointerException, IllegalArgumentException, IllegalStateException |

### Step 4:

|             | System under test                          | Expected output                                                      | Implemented?       | Test name                                                           |
|-------------|--------------------------------------------|----------------------------------------------------------------------|--------------------|---------------------------------------------------------------------|
| Test Case 1 | Card `null`, hand `non-empty`              | `NullPointerException`  ("Card cannot be null")                      | :white_check_mark: | removeCardFromHand_withNullCard_throwsNullPointerException          |
| Test Case 2 | Card `testCard1`, hand `[]`                | `IllegalStateException` (“Hand empty: can not remove card”)          | :white_check_mark: | removeCardFromHand_withEmptyHand_throwsIllegalStateException        |
| Test Case 3 | Card `testCard1`, hand `without testCard1` | `IllegalArgumentException` (“Card not in hand: can not remove card”) | :white_check_mark: | removeCardFromHand_withCardNotInHand_throwsIllegalArgumentException |
| Test Case 4 | Card `testCard2`, hand `[...testCard2...]` | hand `without testCard2`                                             | :white_check_mark: | removeCardFromHand_withCardInHand_removesCard                       |

## Method under test: `getNumberOfCards()`

### Step 1‑3 Results

|            | Input                             | Output            |
|------------|-----------------------------------|-------------------|
| **Step 1** | number of cards currently in hand | integer (`0 … n`) |
| **Step 2** | Count/Cases                       | **Count**         |
| **Step 3** | `0`, `5`, `15` cards              | `0`; `5`; `15`    |

### Step 4

|             | System under test      | Expected output | Implemented?        | Test name                                            |
|-------------|------------------------|-----------------|---------------------|------------------------------------------------------|
| Test Case 1 | Hand with **5** cards  | `5`             | :white_check_mark:  | `getNumberOfCards_fiveCardsInHand_returnsFive`       |
| Test Case 2 | Hand with **15** cards | `15`            | :white_check_mark:  | `getNumberOfCards_fifteenCardsInHand_returnsFifteen` |
| Test Case 3 | Hand with **0** cards  | `0`             | :white_check_mark:  | `getNumberOfCards_zeroCardsInHand_returnsZero`       |

---

## Method under test: `hasCardType(CardType cardType)`

### Step 1‑3 Results

|            | Input 1                        | Input 2                                  | Output                                  |
|------------|--------------------------------|------------------------------------------|-----------------------------------------|
| **Step 1** | a `CardType` value (or `null`) | whether the hand contains that card type | whether or not CardType is in hand      |
| **Step 2** | **Cases**                      | **Cases**                                | Boolean or Exception                    |
| **Step 3** | `null`; any `CardType`         | contains card; does not contain card     | `true`; `false`; `NullPointerException` |

### Step 4

|             | System under test                | Expected output                                    | Implemented?        | Test name                                                 |
|-------------|----------------------------------|----------------------------------------------------|---------------------|-----------------------------------------------------------|
| Test Case 1 | `cardType = null`                | `NullPointerException` ("CardType cannot be null") | :white_check_mark:  | `hasCardType_withNullCardType_throwsNullPointerException` |
| Test Case 2 | Hand **without** `testCardType1` | `false`                                            | :white_check_mark:  | `hasCardType_withCardNotInHand_returnsFalse`              |
| Test Case 3 | Hand **with** `testCardType1`    | `true`                                             | :white_check_mark:  | `hasCardType_withCardInHand_returnsTrue`                  |
| Test Case 4 | Hand **with** `DEFUSE`           | `true`                                             | :white_check_mark:  | `hasCardType_withDefuseCard_returnsTrue`                  |
| Test Case 5 | Hand **with** `EXPLODING_KITTEN` | `true`                                             | :white_check_mark:  | `hasCardType_withExplodingKitten_returnsTrue`             |

