# BVA Analysis for BURY-CARD

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input         | Output                                                    |
|--------|---------------|-----------------------------------------------------------|
| Step 1 | Bury Card     | BuryCardEffect class that implements CardEffect interface |
| Step 2 | Bury Object   | BuryEffect Object                                         |
| Step 3 | Bury instance | BuryEffect Instance                                       |

### Step 4:

|             | System under test | Expected output / state transition                                      | Implemented?       | Test name                                  |
|-------------|-------------------|-------------------------------------------------------------------------|--------------------|--------------------------------------------|
| Test Case 1 | BuryCard          | returns non-null (to indicate that a new BuryEffect Object was created) | :white_check_mark: | createEffect_buryCard_returnsNonNullEffect |

#### **Note**:

Within the BuryCard class, there is also an BuryEffect instance within that is used for executing.

# BVA Analysis for BURY-CARD-EFFECT

## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output/ State Change                                      |
|--------|----------------------|-----------------------------------------------------------|
| Step 1 | GameContext          | Calls context.buryCardImplementation()                    |
| Step 2 | GameContext Object   | Boolean (since the method above should be called)         |
| Step 3 | GameContext instance | True (must be true that buryCardImplementation is called) |

### Step 4:

|             | System under test | Expected output / state transition         | Implemented?       | Test name                                          |
|-------------|-------------------|--------------------------------------------|--------------------|----------------------------------------------------|
| Test Case 1 | gameContext       | calls gameContext.buryCardImplementation() | :white_check_mark: | execute_buryCardEffect_callsburyCardImplementation |