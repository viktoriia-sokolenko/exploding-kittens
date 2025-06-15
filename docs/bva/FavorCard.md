# BVA Analysis for FavorCard

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input              | Output                                                 |
|--------|--------------------|--------------------------------------------------------|
| Step 1 | Favor Card         | FavorEffect class that implements CardEffect interface |
| Step 2 | FavorCard Object   | FavorEffect Object                                     |
| Step 3 | FavorCard instance | Non-null object                                        |

### Step 4:

|             | System under test | Expected output / state transition | Implemented?       | Test name                                   |
|-------------|-------------------|------------------------------------|--------------------|---------------------------------------------|
| Test Case 1 | FavorCard         | returns non-null object            | :white_check_mark: | createEffect_favorCard_returnsNonNullEffect |

# BVA Analysis for FavorEffect

## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                                  |
|--------|----------------------|-------------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.transferCardBetweenPlayers()                              |
| Step 2 | GameContext Object   | Boolean                                                                 |
| Step 3 | GameContext instance | True (must be true that context.transferCardBetweenPlayers() is called) |

### Step 4:

|             | System under test | Expected output / state transition             | Implemented?       | Test name                                           |
|-------------|-------------------|------------------------------------------------|--------------------|-----------------------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.transferCardBetweenPlayers() | :white_check_mark: | execute_favorEffect_callsTransferCardBetweenPlayers |