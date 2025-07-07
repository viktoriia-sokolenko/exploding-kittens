### Cards Implemented
- Exploding Kitten (Drawing this card eliminates a player from the game)
- Attack (End your turn without drawing, next player takes 2 turns)
- Defuse (Used automatically when you draw an Exploding Kitten to stay in the game)
- Favor (Pick another player, and they will choose one of their cards to give to you)
- Skip (End your turn without drawing a card)
- Shuffle (Shuffle the deck)
- See the Future (View the top two cards of the deck and their positions)
- Alter the Future (View the top three cards of the deck and pick the new order for those cards)
- Nuke (Move all Exploding Kittens to the top of the deck)
- Reverse (Reverse the order of play and end your turn without drawing a card)
- Swap Top and Bottom (Swap the top and bottom cards of the deck)
- Bury (Draw a card and pick a position in the deck to put that card in)
- Normal Cat

### Code Coverage
100% cyclomatic coverage.

### Mutation Testing
100% mutants are killed.

### Integration Testing
We have many integration tests in our Project. We also have an additional one for game setup feature that Viktoriia has worked on.

### Internalization
Our code allows user to choose between two languages (English and English (US)) in
the beginning of the game. Both languages have separate properties files, and new languages could be added easily, without changing the existing code.
None of the strings used in the code and the interface (except exceptions) are hard coded.