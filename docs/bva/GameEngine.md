# Boundary-Value Analysis (BVA) for `GameEngine`

> The BVA examines:
>
> 1. **Constructor** – defensive *null* checks on mandatory collaborators.
> 2. **`playCard(Player, Card)`** – defensive *null* checks and delegation to `CardManager`.

---

## Method 0 – Constructor

### Steps 1-3

| State  | **turnManager**   | **playerManager** | **deck**          | **userInterface** | Expected result / state change                                                                                              |
| ------ | ----------------- | ----------------- | ----------------- | ----------------- | --------------------------------------------------------------------------------------------------------------------------- |
| **S1** | any instance      | any instance      | any deck          | any UI            | Constructor only assigns fields                                                                                             |
| **S2** | { `null`, valid } | { `null`, valid } | { `null`, valid } | { `null`, valid } | Any mandatory parameter that is `null` → *NullPointerException* with a descriptive message; otherwise the object is created |
| **S3** | `null`            | valid             | valid             | valid             | *NullPointerException* – “turnManager must not be null”                                                                     |
|        | valid             | `null`            | valid             | valid             | *NullPointerException* – “playerManager must not be null”                                                                   |
|        | valid             | valid             | `null`            | valid             | *NullPointerException* – “deck must not be null”                                                                            |
|        | valid             | valid             | valid             | `null`            | No exception (userInterface is optional)                                                                                    |
|        | valid             | valid             | valid             | valid             | Instance created successfully                                                                                               |

### Step 4 – concrete tests

| # | System under test                                        | Expected behaviour                         | Implemented?   | JUnit test name                                            |
| - | -------------------------------------------------------- | ------------------------------------------ | -------------- | ---------------------------------------------------------- |
| 1 | `new GameEngine(null, playerManager, deck, ui)`          | throws *NullPointerException* with message | Yes            | `constructor_nullTurnManager_throwsNullPointerException`   |
| 2 | `new GameEngine(turnManager, null, deck, ui)`            | throws *NullPointerException* with message | Yes            | `constructor_nullPlayerManager_throwsNullPointerException` |
| 3 | `new GameEngine(turnManager, playerManager, null, ui)`   | throws *NullPointerException* with message | Yes            | `constructor_nullDeck_throwsNullPointerException`          |
| 4 | `new GameEngine(turnManager, playerManager, deck, null)` | No exception                               | Yes            | `constructor_nullUserInterface_allowed`                    |
| 5 | `new GameEngine(turnManager, playerManager, deck, ui)`   | Instance created successfully              | Yes (implicit) | –                                                          |

---

## Method 1 – `public void playCard(Player player, Card card)`

### Steps 1-3

| State  | **player**        | **card**          | Expected result / state change                                                                                                   |
| ------ | ----------------- | ----------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| **S1** | any player        | any card          | Method delegates to `CardManager`                                                                                                |
| **S2** | { `null`, valid } | { `null`, valid } | Either argument `null` → *NullPointerException*; both valid → delegate to `CardManager.playCard`, propagating further exceptions |
| **S3** | `null`            | valid             | *NullPointerException* – “player must not be null”                                                                               |
|        | valid             | `null`            | *NullPointerException* – “card must not be null”                                                                                 |
|        | valid             | card in hand      | `CardManager.playCard` invoked; card removed from hand; normal turn logic proceeds                                               |

### Step 4 – concrete tests

| # | System under test                       | Expected behaviour                                                 | Implemented? | JUnit test name                                  |
| - | --------------------------------------- | ------------------------------------------------------------------ | ------------ | ------------------------------------------------ |
| 1 | `playCard(null, skipCard)`              | throws *NullPointerException* – “player must not be null”          | Yes          | `playCard_nullPlayer_throwsNullPointerException` |
| 2 | `playCard(validPlayer, null)`           | throws *NullPointerException* – “card must not be null”            | Yes          | `playCard_nullCard_throwsNullPointerException`   |
| 3 | `playCard(validPlayer, skipCardInHand)` | `CardManager.playCard` invoked once; card removed from player hand | Yes          | `playCard_playerOwnsCard_executesEffect`         |

---

## Gap analysis & next steps

| Area                      | Current coverage                         | Recommended additions                                                         |
| ------------------------- | ---------------------------------------- | ----------------------------------------------------------------------------- |
| **Card-possession rules** | Only “player *does* own the card” path   | Add a test for when the player does **not** possess the card                  |
| **Delegation variety**    | Verified with one card type (`SkipCard`) | Add another representative card (e.g., `AttackCard`) or parameterise the test |
| **Concurrency**           | Not in scope                             | If concurrent use is possible, add multi-threaded tests around `playCard`     |