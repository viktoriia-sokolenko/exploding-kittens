# BVA Analysis for SeeTheFutureCard

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input                     | Output                                                        |
|--------|---------------------------|---------------------------------------------------------------|
| Step 1 | See The Future Card       | SeeTheFutureEffect class that implements CardEffect interface |
| Step 2 | SeeTheFutureCard Object   | SeeTheFutureEffect Object                                     |
| Step 3 | SeeTheFutureCard instance | Non-null object                                               |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                                          |
|-------------|-------------------|------------------------------------|--------------------|----------------------------------------------------|
| Test Case 1 | SeeTheFutureCard  | returns non-null object            | :white_check_mark: | createEffect_seeTheFutureCard_returnsNonNullEffect |

# BVA Analysis for SeeTheFutureEffect
## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                               |
|--------|----------------------|----------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.viewTopTwoCardsFromDeck()                              |
| Step 2 | GameContext Object   | Boolean                                                              |
| Step 3 | GameContext instance | True (must be true that context.viewTopTwoCardsFromDeck() is called) |

### Step 4:

|             | System under test | Expected output / state transition          | Implemented?       | Test name                                               |
|-------------|-------------------|---------------------------------------------|--------------------|---------------------------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.viewTopTwoCardsFromDeck() | :white_check_mark: | execute_seeTheFutureEffect_callsViewTopTwoCardsFromDeck |
