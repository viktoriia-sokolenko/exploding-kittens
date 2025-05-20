# BVA Analysis for CARD FACTORY

#### Important Note

The Card Factory is responsible for creating Card objects of various types. It has two main methods:
1. `createCard(CardType type)` - Creates a single card of the specified type
2. `createCards(CardType type, int numberOfCards)` - Creates multiple cards of the specified type

Each test case ensures that the Card Factory correctly handles all CardType enum values and validates inputs appropriately. All card types are tested: NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, ALTER_THE_FUTURE, SEE_THE_FUTURE, SHUFFLE, NUKE, and FAVOR.

## Method 1: `public Card createCard(CardType type)`

### Step 1-3 Results 

|        | Input                                                                                                                              | Output                                                         |
|--------|------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| Step 1 | Card Type                                                                                                                          | Card object of the specified type                              |
| Step 2 | Cases for Enum Values (null, and all CardType enum values)                                                                         | Card object or Exception                                       |
| Step 3 | `null`, `NORMAL`, `ATTACK`, `DEFUSE`, `SKIP`, `FAVOR`, `EXPLODING_KITTEN`, `SHUFFLE`, `ALTER_THE_FUTURE`, `SEE_THE_FUTURE`, `NUKE` | Card object of correct type or `NullPointerException` or `IllegalArgumentException` |

### Step 4:

|             | System under test                     | Expected output / state transition                                         | Implemented?       | Test name                                                    |
|-------------|--------------------------------------|--------------------------------------------------------------------------|--------------------|------------------------------------------------------------|
| Test Case 1 | `type == null`                       | `NullPointerException`                                                    | :white_check_mark: | createCard_withNullType_ThrowsNullPointerException          |
| Test Case 2 | `type == CardType.NORMAL`            | Returns instance of `NormalCard`                                          | :white_check_mark: | createCard_withNormalCardType_CreatesCard                   |
| Test Case 3 | `type == CardType.EXPLODING_KITTEN`  | Returns instance of `ExpoldingKittenCard`                                | :white_check_mark: | createCard_withExplodingKittenCardType_CreatesCard          |
| Test Case 4 | `type == CardType.DEFUSE`            | Returns instance of `DefuseCard`                                          | :white_check_mark: | createCard_withDefuseCardType_CreatesCard                   |
| Test Case 5 | `type == CardType.ATTACK`            | Returns instance of `AttackCard`                                          | :white_check_mark: | createCard_withAttackCardType_CreatesCard                   |
| Test Case 6 | `type == CardType.SKIP`              | Returns instance of `SkipCard`                                            | :white_check_mark: | createCard_withSkipCardType_CreatesCard                     |
| Test Case 7 | `type == CardType.ALTER_THE_FUTURE`  | Returns instance of `AlterTheFutureCard`                                 | :white_check_mark: | createCard_withAlterTheFutureCardType_CreatesCard           |
| Test Case 8 | `type == CardType.SEE_THE_FUTURE`    | Returns instance of `SeeTheFutureCard`                                   | :white_check_mark: | createCard_withSeeTheFutureCardType_CreatesCard             |
| Test Case 9 | `type == CardType.SHUFFLE`           | Returns instance of `ShuffleCard`                                         | :white_check_mark: | createCard_withShuffleCardType_CreatesCard                  |
| Test Case 10 | `type == CardType.NUKE`             | Returns instance of `NukeCard`                                            | :white_check_mark: | createCard_withNukeCardType_CreatesCard                     |
| Test Case 11 | `type == CardType.FAVOR`            | Returns instance of `FavorCard`                                           | :white_check_mark: | createCard_withFavorCardType_CreatesCard                    |
| Test Case 12 | Unrecognized `type`                 | `IllegalArgumentException` ("Unknown card type: " + type)                 | :white_check_mark: | createCard_withUnknownCardType_ThrowsIllegalArgumentException |
| Test Case 13 | Card types called repeatedly        | Each call returns a new object (reference inequality)                     | :white_check_mark: | createCard_returnsCorrectTypeForEachCardType                 |

**Note**: Each test case verifies that the card factory creates the correct type of card for each CardType enum value. The tests ensure that null inputs are properly handled with NullPointerException and that unknown card types throw IllegalArgumentException with the appropriate error message.

## Method 2: `public List<Card> createCards(CardType type, int numberOfCards)`

### Step 1-3 Results

|        | Input 1                                                                                                                                 | Input 2                  | Output                                                        |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------|----------------------------|-----------------------------------------------------------------|
| Step 1 | Card Type                                                                                                                               | Number of cards to create | List of Card objects of the specified type                     |
| Step 2 | Enum Values (null, and all CardType enum values)                                                                                        | Integers (-1, 0, 1, >1)   | List of Card objects or Exception                              |
| Step 3 | `null`, `NORMAL`, `ATTACK`, `DEFUSE`, `SKIP`, `FAVOR`, `EXPLODING_KITTEN`, `SHUFFLE`, `ALTER_THE_FUTURE`, `SEE_THE_FUTURE`, `NUKE`     | `-1`, `0`, `1`, `5`, `100` | List of Card objects or `NullPointerException` or `IllegalArgumentException` |

### Step 4:

|             | System under test                       | Expected output / state transition                                         | Implemented?       | Test name                                                                      |
|-------------|----------------------------------------|--------------------------------------------------------------------------|--------------------|--------------------------------------------------------------------------------|
| Test Case 1 | `numberOfCards == -1`, valid `type`    | `IllegalArgumentException` ("Number of cards must be above 0")            | :white_check_mark: | createCards_withNegativeCount_throwsIllegalArgumentException                   |
| Test Case 2 | `numberOfCards == 0`, valid `type`     | `IllegalArgumentException` ("Number of cards must be above 0")            | :white_check_mark: | createCards_withZeroCount_throwsIllegalArgumentException                       |
| Test Case 3 | `type == null`, `numberOfCards == 1`   | `NullPointerException`                                                    | :white_check_mark: | createCards_withNullTypeAndValidCount_throwsNullPointerException               |
| Test Case 4 | Valid `type`, `numberOfCards == 1`     | List size `== 1`; element is of correct card type                         | :white_check_mark: | createCards_withValidTypeAndCountOne_returnsListWithOneCard                    |
| Test Case 5 | Valid `type`, `numberOfCards == 5`     | List size `== 5`; all elements are of correct card type                   | :white_check_mark: | createCards_withValidTypeAndCountGreaterThanOne_returnsListWithCorrectNumberOfCards |
| Test Case 6 | Valid `type`, `numberOfCards == 5`     | All elements in list are unique instances (reference inequality)          | :white_check_mark: | createCards_withValidTypeAndCountGreaterThanOne_returnsListWithAllUniqueCards  |
| Test Case 7 | Sequential calls with different counts | Second call unaffected by first; list sizes correct (no shared state)     | :white_check_mark: | createCards_sequentialCalls_returnIndependentResults                           |
| Test Case 8 | Valid `type`, `numberOfCards == 100`   | List size `== 100`; all elements are of correct card type                 | :white_check_mark: | createCards_withLargeNumber_createsCorrectNumberOfCards                        |
