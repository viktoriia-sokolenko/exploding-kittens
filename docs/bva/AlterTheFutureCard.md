# BVA Analysis for AlterTheFutureCard

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input                       | Output                                                          |
|--------|-----------------------------|-----------------------------------------------------------------|
| Step 1 | Alter The Future Card       | AlterTheFutureEffect class that implements CardEffect interface |
| Step 2 | AlterTheFutureCard Object   | AlterTheFutureEffect Object                                     |
| Step 3 | AlterTheFutureCard instance | Non-null object                                                 |

### Step 4:

|             | System under test  | Expected output / state transition | Implemented?       | Test name                                            |
|-------------|--------------------|------------------------------------|--------------------|------------------------------------------------------|
| Test Case 1 | AlterTheFutureCard | returns non-null object            | :white_check_mark: | createEffect_alterTheFutureCard_returnsNonNullEffect |

# BVA Analysis for AlterTheFutureEffect
## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                                      |
|--------|----------------------|-----------------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.rearrangeTopThreeCardsFromDeck()                              |
| Step 2 | GameContext Object   | Boolean                                                                     |
| Step 3 | GameContext instance | True (must be true that context.rearrangeTopThreeCardsFromDeck() is called) |

### Step 4:

|             | System under test | Expected output / state transition                 | Implemented?       | Test name                                                        |
|-------------|-------------------|----------------------------------------------------|--------------------|------------------------------------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.rearrangeTopThreeCardsFromDeck() | :white_check_mark: | execute_alterTheFutureEffect_callsRearrangeTopThreeCardsFromDeck |