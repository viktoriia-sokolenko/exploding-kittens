This file should contain the **rough algorithm** for a single turn of game.

# Exploding Kittens – Single Turn Logic Explanation

This section explains the logic behind how a **single turn** is handled in the game, especially considering interactions
with special cards like **Attack**, **Skip**, and **Reverse**, and how turn counters are managed under attack states.

## Turn Structure (Logic from Turn Manager)

A player's turn generally involves:

1. **Optional Actions**:
    - The player may play any number of cards before drawing.
    - Card effects (like Favor, Shuffle, etc.) resolve immediately when played.

2. **Turn Completion**:
    - The turn **must end by drawing a card**, unless a card like **Skip** or **Attack** ends it early.

---

## Attack State Handling

When a player is **under attack**, they are required to take multiple consecutive turns.

- `turnManager.getRequiredTurns()` returns the total number of turns required.
- `turnManager.getCurrentPlayerTurnsTaken()` tracks how many of those the player has taken.

During a single logical turn:

- If the player is under attack and has not yet taken all required turns:
    - Each played card or drawn card **increments** `turnsTaken`.
    - The turn is not truly over until all required turns are completed.

---

## Reverse Card Special Case

- The `reverseOrderPreservingAttackState()` method:
    - First checks if the player is under attack.
    - If so, it **increments their `turnsTaken`** (as using Reverse counts as playing a card).
    - Then it reverses the turn order queue.
    - This ensures that the reverse happens **after** the current player finishes their turn(s) under attack.

---

## Ending a Turn

A turn is considered **fully complete** when:

- The player draws a card (unless avoided by Skip/Attack/Reverse).
- If under attack, the player has drawn or skipped the required number of times (`turnsTaken == requiredTurns`).

If not under attack:

- A single card draw or a Skip/Reverse/Attack ends the turn.

---

## Special Card Summary in a Turn

| Card                        | Ends Turn | Skips Drawing | Affects Attack State      | Notes                                  |
|-----------------------------|-----------|---------------|---------------------------|----------------------------------------|
| **Attack**                  | ✅         | ✅             | ✅ (passes to next)        | Adds 2 turns to next player            |
| **Skip**                    | ✅         | ✅             | ⚠️ (partial under attack) | Only ends 1 of 2 turns if under attack |
| **Reverse**                 | ✅         | ✅             | ✅                         | Reverses turn order & acts like Skip   |
| **Shuffle**                 | ❌         | ❌             | ❌                         | Pure utility, does not end turn        |
| **Favor, See/Alter Future** | ❌         | ❌             | ❌                         | Played during optional actions         |

### Important Note
All other cards not listed above, will use the basic `playCard Effect`. It's through the `Turn Manager` that handles all of this

---

## Summary

Your single-turn logic carefully checks for the **attack state**, allows players to take actions in between, and manages
turn progression with:

- `turnsTaken` and `requiredTurns`
- Card effects that can **alter or end the turn**
- Special treatment for cards like Reverse that must preserve the attack state before modifying the turn order.

