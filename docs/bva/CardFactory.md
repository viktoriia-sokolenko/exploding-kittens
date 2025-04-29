## CardFactory BVA

### Method under test: `createCard(CardType type)`

||System under test|Expected output|Implemented?|
|---|---|---|---|
|Test Case 1|`type == null`|`NullPointerException`||
|Test Case 2|`type == CardType.NORMAL`|Returns instance of `NormalCard` (e.g., `instanceof NormalCard == true`)||
|Test Case 3|`type == CardType.EXPLODING_KITTEN`|Returns instance of `ExplodingKittenCard`||
|Test Case 4|Any valid type called **repeatedly**|Each call returns a **new** object (reference inequality)||
|Test Case 5|Unrecognized `type` (only if enum isn’t exhaustive)|`IllegalArgumentException` (or equivalent)| |

### Method under test: `createCards(CardType type, int numCards)`

||System under test|Expected output / state transition|Implemented?|
|---|---|---|---|
|Test Case 1|`numCards <= 0`|`IllegalArgumentException` (or `IncorrectNumberOfCardsException`)||
|Test Case 2|`type == null`, `numCards == 1`|`NullPointerException`||
|Test Case 3|Valid `type`, `numCards == 1`|List size `== 1`; element passes `instanceof` check||
|Test Case 4|Valid `type`, `numCards > 1`|List size `== numCards`; **all** elements non-null & each a new instance||
|Test Case 5|Sequential calls: `createCards(type, n)` then `createCards(type, m)`|Second call unaffected by first; list sizes correct (no shared state)| |
