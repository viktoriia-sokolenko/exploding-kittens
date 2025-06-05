# BVA Analysis for SeeTheFutureCard

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input                     | Output                                                        |
|--------|---------------------------|---------------------------------------------------------------|
| Step 1 | See The Future Card       | SeeTheFutureEffect class that implements CardEffect interface |
| Step 2 | Object                    | Boolean (is SeeTheFutureEffect null)                          |
| Step 3 | SeeTheFutureCard instance | False                                                         |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                                          |
|-------------|-------------------|------------------------------------|--------------------|----------------------------------------------------|
| Test Case 1 | SeeTheFutureCard  | returns non-null                   | :white_check_mark: | createEffect_SeeTheFutureCard_returnsNonNullEffect |

# BVA Analysis for SeeTheFutureEffect
## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                  |
|--------|----------------------|-----------------------------------------|
| Step 1 | GameContext          | Calls context.viewTopTwoCardsFromDeck() |
| Step 2 | Object               | Boolean                                 |
| Step 3 | GameContext instance | True                                    |

### Step 4:

|             | System under test | Expected output / state transition      | Implemented?       | Test name                                       |
|-------------|-------------------|-----------------------------------------|--------------------|-------------------------------------------------|
| Test Case 1 | mockContext       | calls context.viewTopTwoCardsFromDeck() | :white_check_mark: | executeEffect_SeeTheFutureCard_callsGameContext |
