# SOFT2201-A3

## How to play
Run using the gradle command `gradle clean build run`.

You can execute the enemy cheat code by 
pressing the `E` key. You can also execute the 
projectile cheat code by pressing the `P` key.
These keys remove the green (fast) enemies and projectiles
respectively.

To change the difficulty, simply choose from the comboBox 
at the bottom of the screen.

To save and undo the game, press the `SAVE` and `UNDO` buttons
at the bottom of the screen as well.

## Features Implemented
The features implemented are:
* `Difficulty Level`
* `Score and Timer` and
* `Undo and Cheat`

## Designs Patterns Used
### Singleton Pattern (Creational)
The classes/files involved in this design pattern are:
* `Difficulty` (Singleton Participant)
* `GameWindow`
* `App` 

### Observer Pattern (Behavioural)
The classes/files involved in this design pattern are:
* `Subject` (Subject Participant)
* `Observer` (Observer Participant)
* `Score` (Concrete Subject)
* `Timer` (Concrete Subject)
* `ScoreObserver` (Concrete Observer)
* `TimerObserver` (Concrete Observer)
* `GameWindow`

### Memento Pattern (Behavioural)
The classes/files involved in this design pattern are:
* `Memento` (Memento Participant)
* `Caretaker` (Caretaker Participant)
* `GameEngine` (Originator)
* `GameWindow`
