This file should contain the rough algorithm for the game set up phase.

# Exploding Kittens - Game Setup Phase (Rough Algorithm)

This document outlines the steps needed to initialize the Exploding Kittens game before actual gameplay begins.

---

## 🧩 Phase: Game Setup

### Step 1: Initialize Game Components
- Create and initialize:
  - `Deck` object
  - `Player` objects (prompt for number of players)
  - `CardFactory` or logic to create card instances

---

### Step 2: Build Card Pool
- Create instances of the following cards:
  - Exploding Kitten
  - Attack
  - Defuse
  - Favor
  - Skip
  - Shuffle
  - See the Future
  - Alter the Future
  - Nuke
  - Reverse
  - Swap Top and Bottom
  - Bury

> ✅ The number of each type should follow game rules (e.g., 1 fewer Exploding Kitten than number of players, some fixed number of Attacks, etc.)

---

### Step 3: Special Handling – Nuke Card
- If **Nuke** card is included in the deck:
  - Find all **Exploding Kitten** cards
  - Move them to the **top** of the deck (to explode players quickly)

---

### Step 4: Assign Initial Hands
- For each player:
  - Deal them:
    - **4 random cards** (excluding Exploding Kittens and extra Defuse cards)
    - **1 Defuse card** (guaranteed)
- Add **remaining Defuse** cards and **Exploding Kittens** to the deck
  - Exploding Kittens: Total = Number of Players - 1
  - Extra Defuse cards: Add to deck to be drawn later

---

### Step 5: Shuffle the Deck
- Shuffle the remaining cards in the deck (after removing cards for hands)

---

### Step 6: Initialize Player State
- Assign:
  - Player index
  - Hand (cards)
  - `isTurn?` → `false` for all but first player
- Set turn order (circular queue or linked list)

---

### Step 7: Prepare Game Loop
- Set current player to start
- Begin core game logic loop (handled in GameEngine)

---

## 🎴 Objects and Their Setup Roles

### `Card` Class
- Attributes:
  - Type (Enum/String)
  - Description
  - IsAssigned (boolean if it’s in a player hand)
- Method:
  - `play()`, `createEffect()`, etc.

### `Deck` Class
- Fields:
  - `List<Card> cards`
- Methods:
  - `shuffleDeck()`
  - `draw()`
  - `peekTop()`, `peekBottom()`
  - `insertCardAt()`
  - and more

### `Player` Class
- Fields:
  - `Hand hand`
  - `boolean activeStatus`
- Methods:
  - `drawCard()`
  - `addCardToHand()`
  - `removeCardFromHand()`
  - and more

### `Hand` Class
- Fields:
  - `Map CardType Count`
- Methods:
  - `addCard()`
  - `removeCard()`
  - and more

### `GameEngine` Class
- Method:
  - `playCard()`
  - `initializeGame()`
  - `runGameLoop()`
  - `createInitialDeck()`
  - `createNewGame()`
  - and More

---

## 🚀 Next Steps After Setup
- Move into game loop logic
- Use terminal-based UI for input/output
- Enable player actions (play, skip, etc.)
