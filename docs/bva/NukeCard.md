# BVA Analysis for NukeCard

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input             | Output                                                |
|--------|-------------------|-------------------------------------------------------|
| Step 1 | Nuke Card         | NukeEffect class that implements CardEffect interface |
| Step 2 | NukeCard Object   | NukeEffect Object                                     |
| Step 3 | NukeCard instance | Non-null object                                       |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                                  |
|-------------|-------------------|------------------------------------|--------------------|--------------------------------------------|
| Test Case 1 | NukeCard          | returns non-null object            | :white_check_mark: | createEffect_nukeCard_returnsNonNullEffect |

# BVA Analysis for NukeEffect

## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                                    |
|--------|----------------------|---------------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.moveAllExplodingKittensToTop()                              |
| Step 2 | GameContext Object   | Boolean                                                                   |
| Step 3 | GameContext instance | True (must be true that context.moveAllExplodingKittensToTop() is called) |

### Step 4:

|             | System under test | Expected output / state transition               | Implemented?       | Test name                                            |
|-------------|-------------------|--------------------------------------------------|--------------------|------------------------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.moveAllExplodingKittensToTop() | :white_check_mark: | execute_nukeEffect_callsMoveAllExplodingKittensToTop |