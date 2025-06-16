# BVA Analysis for SwapTopAndBottom

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input                         | Output                                                            |
|--------|-------------------------------|-------------------------------------------------------------------|
| Step 1 | Swap Top and Bottom Card      | SwapTopAndBottomEffect class that implements CardEffect interface |
| Step 2 | SwapTopAndBottomCard Object   | SwapTopAndBottomEffect Object                                     |
| Step 3 | SwapTopAndBottomCard instance | Non-null object                                                   |

### Step 4:

|             | System under test    | Expected output / state transition | Implemented?       | Test name                                              |
|-------------|----------------------|------------------------------------|--------------------|--------------------------------------------------------|
| Test Case 1 | SwapTopAndBottomCard | returns non-null object            | :white_check_mark: | createEffect_swapTopAndBottomCard_returnsNonNullEffect |

# BVA Analysis for SwapTopAndBottomEffect

## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output                                                                 |
|--------|----------------------|------------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.swapTopAndBottomDeckCards()                              |
| Step 2 | GameContext Object   | Boolean                                                                |
| Step 3 | GameContext instance | True (must be true that context.swapTopAndBottomDeckCards() is called) |

### Step 4:

|             | System under test | Expected output / state transition             | Implemented?       | Test name                                                       |
|-------------|-------------------|------------------------------------------------|--------------------|-----------------------------------------------------------------|
| Test Case 1 | mockContext       | calls mockContext.swapTopAndBottomDeckCards()  | :white_check_mark: | execute_SwapTopAndBottomEffect_callsSwapTopAndBottomDeckCards() |