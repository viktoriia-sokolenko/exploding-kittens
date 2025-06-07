# BVA Analysis for GameContext
#### Important Note
I am using Parametrized Testing, so whenever I use `testCard`, it means that the test runs for cards with all the available card types.

## Method: `public void transferCardBetweenPlayers(Card card, Player playerGiver)`

### Step 1-3 Results

|        | Input 1                                                                                                | Input 2                                                     | Input 3                                   | Output 1                                                              | Output 2                                                       |
|--------|--------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|-------------------------------------------|-----------------------------------------------------------------------|----------------------------------------------------------------|
| Step 1 | `card` to be transferred                                                                               | State of player who gives `card` away (do they have `card`) | Player who receives `card`: currentPlayer | State of player who gives `card` away (do they no longer have `card`) | State of player who receives `card` (do they get a new `card`) |
| Step 2 | Cases                                                                                                  | Boolean                                                     | Player Object                             | Boolean or Exception                                                  | Boolean                                                        |
| Step 3 | NORMAL, EXPLODING_KITTEN, DEFUSE, ATTACK, SKIP, FAVOR, SHUFFLE, SEE_THE_FUTURE, ALTER_THE_FUTURE, NUKE | true, false                                                 | currentPlayer                             | true, false or IllegalArgumentException                               | true, false                                                    |

### Step 4:

|             | System under test                                                     | Expected output / state transition                                      | Implemented?       | Test name                                                                   |
|-------------|-----------------------------------------------------------------------|-------------------------------------------------------------------------|--------------------|-----------------------------------------------------------------------------|
| Test Case 1 | Card `[testCard]`, playerGiver hand `without testCard`, currentPlayer | `IllegalArgumentException` (“Card not in hand: can not remove card”)    | :white_check_mark: | transferCardBetweenPlayers_withCardNotInHand_throwsIllegalArgumentException |
| Test Case 2 | Card `[testCard]`, playerGiver hand `[...testCard]`, currentPlayer    | playerGiver hand `without testCard`, currentPlayer hand `[...testCard]` | :white_check_mark: | transferCardBetweenPlayers_withCardInHand_transfersCard                     |