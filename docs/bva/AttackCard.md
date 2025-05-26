# BVA Analysis for ATTACK-CARD

#### Important Note

The premise of this class is to do the action of ensuring that the player ends their turn without drawing
and force the next player to take two turns in a row. Therefore, I will use mocking as the player input.

## Method 1: ```public void play(Player player)```

### Step 1-3 Results

|        | Input                    | Output                                                              |
|--------|--------------------------|---------------------------------------------------------------------|
| Step 1 | valid player in the game | player doesn't draw and next player need to take two turns in a row |
| Step 2 | Player Object            | deck size remains the same, next player draws twice                 |
| Step 3 | null, Player object      | `NullPointerException` thrown if `player` is null                   |

#### Thoughts?: Please answer honestly

Should GameEngine or Player have an __attack boolean variable__? That way I can set to true once a `player` plays
this AttackCard. Thus, the next player in the game can have the boolean set to `true`. Once set to `true`, another class/method
controls it.

### Step 4:

|             | System under test | Expected output        | Implemented? |
|-------------|-------------------|------------------------|--------------|
| Test Case 1 | player `null`     | `NullPointerException` |              |
| Test Case 2 | player `player`   |                        |              |
| Test Case 3 |                   |                        |              |

