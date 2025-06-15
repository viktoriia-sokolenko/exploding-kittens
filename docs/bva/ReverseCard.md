# BVA Analysis for ReverseCard
### Note to Self:
number of turns for player fir that state that player has turned is increased by one and if that's equals to the amount they need to take, advance to next player
player manager


## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input                | Output                                                   |
|--------|----------------------|----------------------------------------------------------|
| Step 1 | Reverse Card         | ReverseEffect class that implements CardEffect interface |
| Step 2 | ReverseCard Object   | ReverseEffect Object                                     |
| Step 3 | ReverseCard instance | Non-null object                                          |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?        | Test name                                     |
|-------------|-------------------|------------------------------------|---------------------|-----------------------------------------------|
| Test Case 1 | ReverseCard       | returns non-null object            | :white_check_mark:  | createEffect_reverseCard_returnsNonNullEffect |

# BVA Analysis for ReverseEffect
## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                    |
|--------|----------------------|-----------------------------------------------------------|
| Step 1 | GameContext          | Calls context.reverseOrder()                              |
| Step 2 | GameContext Object   | Boolean                                                   |
| Step 3 | GameContext instance | True (must be true that context.reverseOrder() is called) |

### Step 4:

|             | System under test | Expected output / state transition | Implemented? | Test name                          |
|-------------|-------------------|------------------------------------|--------------|------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.reverseOrder()   |              | execute_reverseEffect_reverseOrder |
