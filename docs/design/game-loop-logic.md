This file should contain the rough algorithm for the main game loop.

# Exploding Kittens Game Loop Logic
## Main Game Loop Structure
The game runs in a continuous loop that follows these steps:
1. **Get Current Player**
    - Retrieve the active player for the current turn

2. **Player Elimination Check**
    - If current player is not in game:
        - Handle player elimination
        - Continue to next iteration

3. **Display Game State**
    - Show current game information:
        - Turn number/player indicator
        - Number of remaining players
        - Active player indices
        - Number of cards in deck
        - Current player's hand

4. **Process Player Input**
    - Get user input command
    - Available commands:
        - `play <card_type>` - Play a card from hand
        - `draw` - Draw a card from the deck
        - `hand` - Display current hand
        - `help` - Show available commands
        - `status` - Show game status
        - `quit` - End the game

5. **Win Condition Check**
    - Check if only one player remains
    - If true:
        - End game loop
        - Declare winner

    - If no players remain:
        - End game loop
        - Declare everyone lost

## Command Processing
### Play Command
- Format: `play <card_type>`
- Validates card type exists in player's hand
- Executes card effect
- Displays confirmation

### Draw Command
- Draws card from deck if not empty
- If Exploding Kitten:
    - Check for Defuse card
    - If has Defuse:
        - Use Defuse
        - Player chooses position to return Exploding Kitten

    - If no Defuse:
        - Player eliminated

- Advances to next player's turn

### Status Command
- Displays number of active players, number of cards in deck, and number of cards in current player's hand

### Help Command
- Shows available commands

## Game State Management
The loop maintains:
- Active player tracking
- Deck state
- Player hands
- Game running status
- Turn order

The loop continues until either:
- A winner is determined (last player standing)
- All players are eliminated
- Player issues quit command

This core loop structure ensures turn-based gameplay while handling all possible game states and player actions.
