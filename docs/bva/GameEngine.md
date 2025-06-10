# BVA Analysis for `GameEngine`

> BVA focuses on:
>
> 1. **Constructor** – defensive-null checks for mandatory collaborators.
> 2. **playCard(Player, Card)** – defensive-null checks and successful delegation to `CardManager`.

---

## Method 0: Constructor

### Steps 1-3 (summary)

|        | Input parameters                                                                                                              | Output / State Change                                                                                                                                              |
| ------ | ----------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **S1** | `turnManager`, `playerManager`, `deck`, `userInterface`                                                                       | none (constructor only assigns fields)                                                                                                                             |
| **S2** | • each non-UI param ∈ { null, non-null }<br>• `userInterface` ∈ { null, non-null }                                            | NPE with message for any *mandatory* null; otherwise instance created                                                                                              |
| **S3** | 1. `(null, PM, D, UI)`<br>2. `(TM, null, D, UI)`<br>3. `(TM, PM, null, UI)`<br>4. `(TM, PM, D, null)`<br>5. `(TM, PM, D, UI)` | 1 → NPE "turnManager must not be null"<br>2 → NPE "playerManager must not be null"<br>3 → NPE "deck must not be null"<br>4 → **OK** (UI is optional)<br>5 → **OK** |

### Step 4 – concrete tests

| # | System under test                  | Expected behaviour / state                 | Implemented? | JUnit test name                                                |
| - | ---------------------------------- | ------------------------------------------ | ------------ | -------------------------------------------------------------- |
| 1 | `new GameEngine(null, PM, D, UI)`  | throws `NullPointerException` with message | ✔︎           | `constructor_withNullTurnManager_throwsNullPointerException`   |
| 2 | `new GameEngine(TM, null, D, UI)`  | throws `NullPointerException` with message | ✔︎           | `constructor_withNullPlayerManager_throwsNullPointerException` |
| 3 | `new GameEngine(TM, PM, null, UI)` | throws `NullPointerException` with message | ✔︎           | `constructor_withNullDeck_throwsNullPointerException`          |
| 4 | `new GameEngine(TM, PM, D, null)`  | **no** exception                           | ✔︎           | `constructor_withNullUI_allowsNullUI`                          |
| 5 | `new GameEngine(TM, PM, D, UI)`    | instance created successfully              | (implicit)   | – (covered by test #4 setup)                                   |

---

## Method 1: `public void playCard(Player player, Card card)`

### Steps 1-3 (summary)

|        | Input pairs                                                                          | Output / State Change                                                                                                                                                            |
| ------ | ------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **S1** | any `player`, any `card`                                                             | none (method delegates)                                                                                                                                                          |
| **S2** | • `player ∈ { null, valid }` <br>• `card ∈ { null, valid }`                          | null player/card → `NullPointerException` with message<br>both valid → delegates to `CardManager.playCard(...)` which may mutate state; exceptions from `CardManager` propagate  |
| **S3** | 1. `(null, validCard)`<br>2. `(validPlayer, null)`<br>3. `(validPlayer, cardInHand)` | 1 → NPE "Player cannot be null"<br>2 → NPE "Card cannot be null"<br>3 → `cardManager.playCard(...)` called, card removed from hand, turn logic executed via manager interactions |

### Step 4 – concrete tests

| # | System under test                       | Expected behaviour                                                                       | Implemented? | JUnit test name                                                    |
| - | --------------------------------------- | ---------------------------------------------------------------------------------------- | ------------ | ------------------------------------------------------------------ |
| 1 | `playCard(null, skipCard)`              | throws `NullPointerException` ("Player cannot be null")                                  | ✔︎           | `playCard_withNullPlayer_throwsNullPointerException`               |
| 2 | `playCard(validPlayer, null)`           | throws `NullPointerException` ("Card cannot be null")                                    | ✔︎           | `playCard_withNullCard_throwsNullPointerException`                 |
| 3 | `playCard(validPlayer, skipCardInHand)` | • `cardManager.playCard(...)` invoked once<br>• card removed from player hand            | ✔︎           | `playCard_playerHasCard_executesCardEffect`                        |

---

## Gap analysis & suggestions

| Area                            | Current coverage                             | Suggested additions                                                                                                                   |
| ------------------------------- | -------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **Card possession rules**       | Only positive path tested (player owns card) | Add a test ensuring `playCard` fails or no-ops when the player **does not** possess the card, depending on `CardManager` requirements |
| **`CardManager` delegation**    | Verified for *one* card type (`SkipCard`)    | Use EasyMock to verify delegation for another representative card (e.g., `AttackCard`) or parameterise the test                       |
| **Thread-safety / re-entrancy** | Not in scope                                 | If the engine may be used concurrently, create multi-threaded tests around `playCard`                                                 |

