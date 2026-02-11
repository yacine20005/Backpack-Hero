# Backpack Hero

A strategic roguelike focusing on inventory management, built with **Java** and strict **Object-Oriented Design** principles.

## Project Overview

This project is a Java implementation of the popular roguelike game *Backpack Hero*. Developed as part of an advanced **Object-Oriented Programming (OOP)** curriculum, it showcases a robust **Model-View-Controller (MVC)** architecture and adherence to rigorous clean code standards.

The core gameplay revolves around managing a limited inventory grid where items have physical shapes and interactions. Players must strategically organize their backpack to maximize combat efficiency while exploring a procedurally generated dungeon.

---

## Key Features

### Complex Inventory System
*   **Spatial Puzzle:** Items have unique shapes (tetromino-like) and must be rotated/translated to fit into the grid.
*   **Dynamic Expansion:** The backpack grid grows as the player levels up.

### Tactical Turn-Based Combat
*   **Energy Management:** Players spend energy points to use items (attack, block, heal).
*   **Predictive Enemy AI:** Enemy intentions are telegraphing, requiring players to react defensively or aggressively.
*   **Diverse Arsenal:** Weapons, shields, magical artifacts, and consumables.

### Dungeon Crawler Mechanics
*   **Procedural Generation:** Explore unique dungeon layouts with enemies, treasures, merchants, and healers.
*   **Progression System:** XP gain, leveling up, and permadeath mechanics.

---

## Technical Architecture

The project is engineered with a focus on maintainability, scalability, and code quality.

### Architecture: MVC Pattern
*   **Model:** Encapsulates all game logic, physics (collisions), and state (Hero, Dungeon, Combat).
*   **View:** Renders the game state using the **Zen** graphics library.
*   **Controller:** Handles user input and orchestrates the game loop.

### Code Quality Standards
*   **Encapsulation:** All fields are `private`; no global variables or states.
*   **Safety:** rigorous null-checks (`Objects.requireNonNull`) and input validation.
*   **No Duplication:** Heavy emphasis on code reuse and modularity.

---

## Build & Run

### Prerequisites
*   **Java JDK 21+**
*   **Apache Ant**

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/your-username/backpack-hero-java.git
    cd backpack-hero-java
    ```

2.  **Build the Project**
    Use the provided `build.xml` to compile and generate the JAR.
    ```bash
    ant jar
    ```

3.  **Run the Game**
    ```bash
    java -jar BackpackHero.jar
    ```

---

## Project Structure

```bash
src/fr/uge/backpackhero/
├── model/       # Game entities (Hero, Item, Enemy), logic, and physics
├── view/        # UI rendering and graphical components
├── controller/  # Input handling and game loop orchestration
└── Main.java    # Application entry point
```

---

*This project was developed for the L3 Computer Science curriculum at Université Gustave Eiffel.*
