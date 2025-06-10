# BVA Analysis for ShuffleCard

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input                | Output                                                 |
|--------|----------------------|--------------------------------------------------------|
| Step 1 | Shuffle Card         | ShuffleCard class that implements CardEffect interface |
| Step 2 | ShuffleCard Object   | ShuffleCard Object                                     |
| Step 3 | ShuffleCard instance | Non-null object                                        |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?        | Test name                                     |
|-------------|-------------------|------------------------------------|---------------------|-----------------------------------------------|
| Test Case 1 | ShuffleCard       | returns non-null object            | :white_check_mark:  | createEffect_shuffleCard_returnsNonNullEffect |

# BVA Analysis for shuffleEffect

## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                           |
|--------|----------------------|------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.shuffleDeckFromDeck()                              |
| Step 2 | GameContext Object   | Boolean                                                          |
| Step 3 | GameContext instance | True (must be true that context.shuffleDeckFromDeck() is called) |

### Step 4:

|             | System under test | Expected output / state transition      | Implemented? | Test name                                     |
|-------------|-------------------|-----------------------------------------|--------------|-----------------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.shuffleDeckFromDeck() |              | execute_shuffleEffect_callShuffleDeckFromDeck |
