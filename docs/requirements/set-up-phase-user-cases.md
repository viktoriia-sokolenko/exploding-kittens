User Story:

As a player of Exploding Kittens, I want the game to be properly set up automatically when a new game starts, so that I can begin playing without manually organizing the deck and distributing cards.

Acceptance Criteria
✅ The game must not start unless there are 2 to 4 players.

✅ Each player must receive exactly 5 random cards from the deck.

✅ Each player must receive 1 Defuse card in addition to the 5 random cards.

✅ The number of Exploding Kitten cards added back into the deck must be one less than the number of players.

✅ The deck must be shuffled after adding the Exploding Kitten cards back in.

✅ The turn order must be initialized once setup is complete.

Use Case 1: Start New Game

Actor: Player

Preconditions:

The game application is launched. Deck instance is initialized with all 42 cards being face down.

Main Flow:

Player clicks “Start Game”.

System asks for the total number of players.

The player enters the number of players and each player's name.

System shuffles the deck.

System removes all Exploding Kitten cards.

System gives each player 1 Defuse card.

System deals 5 cards to each player.

System returns the correct number of Exploding Kitten cards to the deck (1 less than the number of players).

System shuffles the deck again.

System sets the initial turn order and indicates the name of the first player.

Alternate Flows:

5.a The system indicates that the number of players the user eneters is invalid (less than 2, more than 4)

6. Resumes at Step 2
 

Postconditions:

Each player has 6 cards (5 random + 1 Defuse).

The deck contains the correct number of Exploding Kittens.

The game is ready for the first turn.
