# BVA Analysis for SkipCard

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input             | Output                                                |
|--------|-------------------|-------------------------------------------------------|
| Step 1 | Skip Card         | SkipEffect class that implements CardEffect interface |
| Step 2 | SkipCard Object   | SkipEffect Object                                     |
| Step 3 | SkipCard instance | Non-null object                                       |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                                  |
|-------------|-------------------|------------------------------------|--------------------|--------------------------------------------|
| Test Case 1 | SkipCard          | returns non-null object            | :white_check_mark: | createEffect_skipCard_returnsNonNullEffect |

# BVA Analysis for SkipEffect

## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                             |
|--------|----------------------|--------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.endTurnWithoutDrawing()                              |
| Step 2 | GameContext Object   | Boolean                                                            |
| Step 3 | GameContext instance | True (must be true that context.endTurnWithoutDrawing() is called) |

### Step 4:

|             | System under test | Expected output / state transition        | Implemented?       | Test name                                     |
|-------------|-------------------|-------------------------------------------|--------------------|-----------------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.endTurnWithoutDrawing() | :white_check_mark: | execute_skipEffect_callsEndTurnWithoutDrawing |