# BVA Analysis for UserInterface

#### Important Note

For methods whose behaviour depends on user *input values* (e.g., `getNumberOfPlayers()`), we treat the textual console
input as the variable under test and identify numeric and non‑numeric boundary conditions. Display‑only methods (
`displayWelcome`, `displayHelp`, etc.) do not produce functional *outputs* beyond printing, so their BVAs focus on the
*state variations* that influence what is printed (e.g., empty vs. non‑empty hands).

I am using Parametrized Testing, so whenever I use `testCardType` or `testCard`, it means that the test runs for all the
possible card types or for cards with all the card types.

## Method under test: `public int getNumberOfPlayers()`

The console prompt accepts **strings** that are parsed into integers. Valid inputs are the inclusive range **2-5**.

### Step-1‑3-Results

|        | Input                                                                                                                                   | Expected result / state change                                                        |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|
| Step 1 | text typed by the user, which is a raw console string                                                                                   | returns value (integer (2-5)) *or* error message                                      |
| Step 2 | **Cases**                                                                                                                               | **Cases**                                                                             |
| Step 3 | `"foo"` (non‑numeric)  <br>`"1"` (below min) <br>`"2"` (lower bound) <br>`"3"` (in range) <br>`"5"` (upper bound) <br>`"6"` (above max) | loops with error; loops with error; returns-2; returns-3; returns-5; loops with error |

### Step-4

|             | System under test                 | Expected output                         | Implemented?       | Test name                                                |
|-------------|-----------------------------------|-----------------------------------------|--------------------|----------------------------------------------------------|
| Test Case 1 | Valid input "3"                   | Returns 3, no error message             | :white_check_mark: | `getNumberOfPlayers_validFirst_tryReturnsImmediately`    |
| Test Case 2 | Below minimum "1", then valid "2" | Error message, then accepts 2           | :white_check_mark: | `getNumberOfPlayers_belowMinimum_rejectsAndPrompts`      |
| Test Case 3 | Above maximum "6", then valid     | Error message, then accepts valid input | :white_check_mark: | `getNumberOfPlayers_aboveMaximum_rejectsAndPrompts`      |
| Test Case 4 | Invalid "foo", valid "2"          | Error message, then accepts 2           | :white_check_mark: | `getNumberOfPlayers_invalidThenValid_promptsUntilGood`   |
| Test Case 5 | Minimum boundary "2"              | Returns 2                               | :white_check_mark: | `getNumberOfPlayers_minimumBoundary_acceptsMinimumValue` |
| Test Case 6 | Maximum boundary "5"              | Returns 5                               | :white_check_mark: | `getNumberOfPlayers_maximumBoundary_acceptsMaximumValue` |

---

### Step 1-3 Results

|        | Input                         | Output                          |
|--------|-------------------------------|---------------------------------|
| Step 1 | Player with card collection   | Formatted display of cards      |
| Step 2 | Collection                    | String output                   |
| Step 3 | Empty, Single, Multiple cards | Empty message or formatted list |

### Step 4:

##### All-combination strategy

|             | System under test             | Expected output                    | Implemented?       | Test name                                              |
|-------------|-------------------------------|------------------------------------|--------------------|--------------------------------------------------------|
| Test Case 1 | Empty hand                    | "(empty)" message                  | :white_check_mark: | `displayPlayerHand_emptyHand_showsEmptyMessage`        |
| Test Case 2 | Single card                   | Single line without count          | :white_check_mark: | `displayPlayerHand_singleCard_showsCardWithoutCount`   |
| Test Case 3 | Multiple same type cards      | Shows cards with count             | :white_check_mark: | `displayPlayerHand_multipleCardsOfSameType_showsCount` |
| Test Case 4 | Multiple different cards      | Shows all cards individually       | :white_check_mark: | `displayPlayerHand_multipleDifferentCards_showsAll`    |
| Test Case 5 | Mixed cards with counts       | Shows correct counts for each type | :white_check_mark: | `displayPlayerHand_mixedCards_showsCorrectCounts`      |
| Test Case 6 | Hand with null count          | Handles null count properly        | :white_check_mark: | `displayPlayerHand_verifyNullCountHandling`            |
| Test Case 7 | Hand with specific null count | Prints correct hand                | :white_check_mark: | `displayPlayerHand_countIntegerNullHandling_specific`  |
| Test Case 8 | Hand with null card type      | Prints correct hand                | :white_check_mark: | `displayPlayerHand_nullCardType_printsCorrectHand`     |

---

## Method under test: `public void displayError(String message)`

### Step 1-3 Results

|        | Input          | Output                           |
|--------|----------------|----------------------------------|
| Step 1 | String message | Error message in stderr          |
| Step 2 | String         | String with "Error: " prefix     |
| Step 3 | non-empty      | Prefixed error message in stderr |

### Step 4:

##### Each-choice strategy

|             | System under test | Expected output               | Implemented?       | Test name                     |
|-------------|-------------------|-------------------------------|--------------------|-------------------------------|
| Test Case 1 | "oops" message    | stderr contains "Error: oops" | :white_check_mark: | `displayError_printsToStderr` |

---

## Method under test: `public String getUserInput()`

### Step 1-3 Results

|        | Input 1             | Input 2       | Output            |
|--------|---------------------|---------------|-------------------|
| Step 1 | Message             | Console input | Formatted input   |
| Step 2 | String              | String        | String            |
| Step 3 | null, "", "message" | any string    | Input with prompt |

### Step 4:

##### All-combination strategy

|             | System under test               | Expected output                         | Implemented?       | Test name                                                                   |
|-------------|---------------------------------|-----------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | Null message, non-empty input   | Returns input with prompt               | :white_check_mark: | `getUserInput_withNullMessageAndNonEmptyConsoleInput_returnsConsoleInput`   |
| Test Case 2 | Empty message, non-empty input  | Returns input with prompt               | :white_check_mark: | `getUserInput_withEmptyMessageAndNonEmptyConsoleInput_returnsConsoleInput`  |
| Test Case 3 | Message, empty then valid input | Keeps asking until valid input received | :white_check_mark: | `getUserInput_withNonEmptyMessageAndEmptyConsoleInput_keepsAskingForInput`  |
| Test Case 4 | Message, valid input            | Returns input with message and prompt   | :white_check_mark: | `getUserInput_withValidMessageAndInput_returnsConsoleInputAndPrintsMessage` |

---

## Method under test: `public void displayCardPlayed(Card card)`

These methods share the same structure: they print a line referencing the card’s type.

### Step 1-3 Results

|        | Input         | Output                               |
|--------|---------------|--------------------------------------|
| Step 1 | Card          | "You played/drew: TYPE" message      |
| Step 2 | CardType      | Formatted string output              |
| Step 3 | Any card type | Corresponding message with card type |

### Step 4:

##### Each-choice strategy

|             | System under test       | Expected output                        | Implemented?       | Test name                                        |
|-------------|-------------------------|----------------------------------------|--------------------|--------------------------------------------------|
| Test Case 1 | Card played             | Shows "You played: [card type]"        | :white_check_mark: | `displayCardPlayed_showCorrectText`              |
| Test Case 2 | Card drawn and played   | Shows correct message for both actions | :white_check_mark: | `displayCardPlayed_andDrawnCard_showCorrectText` |
| Test Case 3 | Card played with effect | Shows card type and effect             | :white_check_mark: | `displayCardPlayed_printsCardWithEffect`         |

## Method: `public void displayDrawnCard(Card card)`

### Step 1-3 Results

|        | Input                                 | Output                                     |
|--------|---------------------------------------|--------------------------------------------|
| Step 1 | Card object                           | Formatted message about drawn card         |
| Step 2 | CardType                              | String output (regular or special message) |
| Step 3 | EXPLODING_KITTEN, any other card type | Regular or special message                 |

### Step 4:

##### Each-choice strategy

|             | System under test            | Expected output                      | Implemented?       | Test name                                               |
|-------------|------------------------------|--------------------------------------|--------------------|---------------------------------------------------------|
| Test Case 1 | Card type EXPLODING_KITTEN   | Special message + "You drew: [card]" | :white_check_mark: | `displayDrawnCard_explodingKitten_printsSpecialMessage` |
| Test Case 2 | Regular card (non-exploding) | Standard "You drew: [card]" message  | :white_check_mark: | `displayCardPlayed_andDrawnCard_showCorrectText`        |

Notes:

- The method has special handling for EXPLODING_KITTEN cards, printing an additional warning message
- Regular cards only get the standard "You drew: [card]" message
- Test coverage verifies both the special case (EXPLODING_KITTEN) and regular case
- The formatting of the card name is handled by the `formatCardName` method
- Both test cases verify the exact output message format and content

## Method under test: `String getUserInput(String message)`

### Step 1-3 Results

|            | Input 1                                       | Input 2                               | Output                                        | Side‑effect                                               |
|------------|-----------------------------------------------|---------------------------------------|-----------------------------------------------|-----------------------------------------------------------|
| **Step 1** | message to be printed before asking for input | arbitrary line from console           | returned string (same string without newline) | print message and from new line prints "> " prompt        |
| **Step 2** | String                                        | String                                | String                                        | Boolean (does it prints message in new line and then "> " |
| **Step 3** | null, empty string, more than 1 character     | empty string, more than one character | empty string, more than one character         | yes/no                                                    |

### Step 4

|             | System under test                                        | Expected output                                           | Implemented?       | Test name                                                                   |
|-------------|----------------------------------------------------------|-----------------------------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | message `null`, input `"hello world"`                    | returns "hello world"; stdout contains "> "               | :white_check_mark: | `getUserInput_withNullMessageAndNonEmptyConsoleInput_returnsConsoleInput`   |
| Test Case 2 | message `""`, input `"hello world"`                      | returns "hello world"; stdout contains "> "               | :white_check_mark: | `getUserInput_withEmptyMessageAndNonEmptyConsoleInput_returnsConsoleInput`  |
| Test Case 3 | message `"message"`, input1 `""`, input2 `"hello world"` | returns ""; stdout repeats "> " and "message" twice       | :white_check_mark: | `getUserInput_withNonEmptyMessageAndEmptyConsoleInput_keepsAskingForInput`  |
| Test Case 4 | message `"message"`, input `"hello world"`               | returns "hello world"; stdout contains "> " and "message" | :white_check_mark: | `getUserInput_withValidMessageAndInput_returnsConsoleInputAndPrintsMessage` |

## Method under test: `int getNumericUserInput(String message, int max, int min)`

### Step 1-3 Results

|            | Input 1                                       | Input 2                                               | Output                                        | Side‑effect                                               |
|------------|-----------------------------------------------|-------------------------------------------------------|-----------------------------------------------|-----------------------------------------------------------|
| **Step 1** | message to be printed before asking for input | arbitrary line from console                           | returned string (same string without newline) | print message and from new line prints "> " prompt        |
| **Step 2** | String                                        | String                                                | String                                        | Boolean (does it prints message in new line and then "> " |
| **Step 3** | null, empty string, more than 1 character     | empty string, more than one character, is not integer | empty string, more than one character         | yes/no                                                    |

### Step 4

|             | System under test                                                    | Expected output                                     | Implemented?       | Test name                                                                  |
|-------------|----------------------------------------------------------------------|-----------------------------------------------------|--------------------|----------------------------------------------------------------------------|
| Test Case 1 | message `null`, input `"1"`, min `0`, max `1`                        | returns `1`                                         | :white_check_mark: | `getNumericUserInput_withNullMessage_returnsConsoleInput`                  |
| Test Case 2 | message `""`, input `"0"`, min `0`, max `2`                          | returns 0; stdout contains "> " and "message"       | :white_check_mark: | `getNumericUserInput_withEmptyMessage_returnsConsoleInput`                 |
| Test Case 3 | message `""`, input1 `"hello world"`, input2 `"0"`, min `0`, max `1` | returns 0; stdout contains "> " twice               | :white_check_mark: | `getNumericUserInput_withNonNumericConsoleInput_keepsAskingForInput`       |
| Test Case 4 | message `"message"`, input1 `""`, input2 `"2"`, min `0`, max `4`     | returns 3; stdout repeats "> " and "message" twice  | :white_check_mark: | `getNumericUserInput_withEmptyConsoleInput_keepsAskingForInput`            |
| Test Case 5 | message `"message"`, input `"2"`, min `1`, max `2`                   | returns 2; stdout contains "> " and "message"       | :white_check_mark: | `getNumericUserInput_withIntegerInput_returnsConsoleInputAndPrintsMessage` |
| Test Case 6 | message `"message"`, input1 `"3"`, input2 `"2"`, min `1`, max `2`    | returns 2; stdout contains "> " and "message" twice | :white_check_mark: | `getNumericUserInput_withInputMoreThanMax_keepsAskingForInput`             |
| Test Case 7 | message `"message"`, input1 `"0"`, input2 `"1"`, min `1`, max `4`    | returns 1; stdout contains "> " and "message" twice | :white_check_mark: | `getNumericUserInput_withInputLessThanMin_keepsAskingForInput`             |

## Method under test: `void displayCardsFromDeck(List<Card> cards, int deckSize)`

### Step 1-3 Results

|            | Input 1                                                                                  | Input 2                                    | Output                                                                      |
|------------|------------------------------------------------------------------------------------------|--------------------------------------------|-----------------------------------------------------------------------------|
| **Step 1** | cards to be printed to be viewed by the player                                           | deck size to be used to print card indexes | prints ":Top of deck:" and card types and their indexes or throws exception |
| **Step 2** | Collection                                                                               | Count                                      | String or exception                                                         |
| **Step 3** | Empty, Exactly 1 Element, Exactly 2 Elements, More than 2 Elements containing Duplicates | `-1`, `0`, `1`, `>1`, `>cards.size()`      | List of cards printed or IllegalArgumentException                           |

### Step 4

|             | System under test                                                          | Expected output                                                                           | Implemented?       | Test name                                                                        |
|-------------|----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|--------------------|----------------------------------------------------------------------------------|
| Test Case 1 | cards `[]` deckSize `1`                                                    | prints `"No cards to view"`                                                               | :white_check_mark: | `displayCardsFromDeck_withEmptyCards_printsNoCardsMessage`                       |
| Test Case 2 | cards `[testCard]`, deckSize `-1`                                          | `IllegalArgumentException` (deckSize can not be negative)                                 | :white_check_mark: | `displayCardsFromDeck_withNegativeDeckSize_throwsIllegalArgumentException`       |
| Test Case 3 | cards `[testCard]`, deckSize `0`                                           | `IllegalArgumentException` (deckSize is less than number of cards to display)             | :white_check_mark: | `displayCardsFromDeck_withOneCardAndDeckSizeZero_throwsIllegalArgumentException` |
| Test Case 4 | cards `[testCard]`, deckSize `1`                                           | prints `":Top of deck:"`, `card1Type, index 0`                                            | :white_check_mark: | `displayCardsFromDeck_withOneCard_printCardTypeAndIndex`                         |
| Test Case 5 | cards `[NORMAL, ALTER_THE_FUTURE]`, deckSize `1`                           | `IllegalArgumentException` (deckSize is less than number of cards to display)             | :white_check_mark: | `displayCardsFromDeck_withTwoCardsAndDeckSizeOne_throwsIllegalArgumentException` |
| Test Case 6 | cards `[NORMAL, ALTER_THE_FUTURE]`, deckSize `2`                           | prints `":Top of deck:"`, `card1Type, index 1`, `card2Type, index 0`                      | :white_check_mark: | `displayCardsFromDeck_withTwoCards_printCardTypeAndIndex`                        |
| Test Case 7 | cards `[SEE_THE_FUTURE, EXPLODING_KITTEN, EXPLODING_KITTEN]`, deckSize `4` | prints `":Top of deck:"`, `card1Type, index 3`, `card2Type, index 2`, `card2Type, index1` | :white_check_mark: | `displayCardsFromDeck_withThreeCardsAndDuplicate_printCardTypeAndIndex`          |