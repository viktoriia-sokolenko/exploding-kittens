## Player class BVA

### Method under test: `drawCard(Deck deck)`

||System under test|Expected output / state transition|Implemented?|
|---|---|---|---|
|Test Case 1|`deck` is **empty** (`size = 0`)|`NoSuchElementException` (“Cannot draw from an empty deck”)||
|Test Case 2|`deck` has **exactly 1** card; player hand is empty|That single card moves to hand → hand `size = 1`; deck `size = 0`||
|Test Case 3|`deck` has **> 1** cards; player hand currently empty|Top card moves to hand → hand `size = 1`; deck `size` decrements by 1||
|Test Case 4|`deck` non-empty but `deck == null` (caller passed `null`)|`NullPointerException`||

### Method under test: `playCard(Card c)`

||System under test|Expected output / state transition|Implemented?|
|---|---|---|---|
|Test Case 1|Player hand **empty**|`IllegalStateException` (“Hand is empty – cannot play”)||
|Test Case 2|`c` not present in hand|`IllegalArgumentException` (“Card not in hand”)||
|Test Case 3|`c` **is** in hand and hand size is 1|Card leaves hand → hand `size = 0`; method returns `c` (or void, depending on design)||
|Test Case 4|Argument `c == null`|`NullPointerException`||

### Method under test: `hasDefuse()`

||System under test|Expected output|Implemented?|
|---|---|---|---|
|Test Case 1|Hand contains **≥ 1** “Defuse” card|`true`||
|Test Case 2|Hand contains **0** “Defuse” cards|`false`||

