# BVA Analysis for ATTACK-CARD

## Method 1: `public CardEffect createEffect()`

### Step 1-3 Results

|        | Input                     | Output                                                      |
|--------|---------------------------|-------------------------------------------------------------|
| Step 1 | Attack Card               | AttackCardEffect class that implements CardEffect interface |
| Step 2 | AttackCard Object         | AttackEffect Object                                         |
| Step 3 | SeeTheFutureCard instance | AttackEffect Instance                                       |

### Step 4:

|             | System under test | Expected output / state transition                                        | Implemented?       | Test name                                    |
|-------------|-------------------|---------------------------------------------------------------------------|--------------------|----------------------------------------------|
| Test Case 1 | AttackCard        | returns non-null (to indicate that a new AttackEffect Object was created) | :white_check_mark: | createEffect_attackCard_returnsNonNullEffect |

#### **Note**:
Within the AttackCard class, there is also an AttackEffect instance within that is used for executing.


# BVA Analysis for ATTACK-CARD-EFFECT
## Method 1: `public void execute(GameContext context)`

### Step 1-3 Results

|        | Input                | Output/ State Change                                               |
|--------|----------------------|--------------------------------------------------------------------|
| Step 1 | GameContext          | Calls context.endTurnWithoutDrawingForAttacks()                    |
| Step 2 | GameContext Object   | Boolean (since the method above should be called)                  |
| Step 3 | GameContext instance | True (must be true that endTurnWithoutDrawingForAttacks is called) |

### Step 4:

|             | System under test | Expected output / state transition                   | Implemented?       | Test name                                                  |
|-------------|-------------------|------------------------------------------------------|--------------------|------------------------------------------------------------|
| Test Case 1 | gameContext       | calls gameContext.endTurnWithoutDrawingForAttacks()  | :white_check_mark: | attackEffect_execute_callsEndTurnWithoutDrawingForAttacks  |