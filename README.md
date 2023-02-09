![CircleCI](https://circleci.com/gh/damianoravalico/jminesweeper.svg?style=shield)
![GitHub](https://img.shields.io/github/license/damianoravalico/jminesweeper)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![JUnit](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

# JMinesweeper

This repository contains the Java implementation of the famous
game "[Minesweeper](https://en.wikipedia.org/wiki/Minesweeper_(video_game))" for the exam of the course Software
Development Methods @ University of Trieste.

## Description of the game

Minesweeper is a logic puzzle video game. The game features a grid of clickable squares, with hidden mines scattered
around throughout the board. The objective is to clear the board without detonating any mines, with the help of clues
about the number of nearby mines in each cell.

### How to play

1. Click on a green tile to uncover it. Depending on the hidden value underneath the tile, there can be different
   outcomes.

    - If you dig out a number, its value indicates the total number of mines underneath the surrounding eight tiles
    - If you dig and there is no value, the eight surrounding tiles will also be dug, and so on if the dug tiles are
      empty
    - If you dig a mine, you have lost the game

2. Depending on the numbers that you dig, you might be sure that a green tile hides a mine. Put a flag on that tile to
   mark it as mined.
3. Repeat 1. until you've dug up all the safe tiles and marked all the mines!

Do not worry, the first click is always safe, e.g. you will not dig a mine on your first move.

## Build with

- [Java SDK 17](https://www.java.com/it/)
- [Gradle](https://gradle.org/)
- [IntelliJ IDEA IDE](https://www.jetbrains.com/idea/)
- [JUnit 5.9.1](https://junit.org/junit5/)

### Information about the implementation

For the realization of the game, the business part has been realized using TDD, through JUnit.
The game also implements a graphical interface, built with the Swing framework, using the MVC pattern, in a "classic"
version.

## Usage

Open a terminal and clone the repository ([git](https://git-scm.com/) required)

```
git clone https://github.com/damianoravalico/jminesweeper
```

Enter the folder just created

```
cd jminesweeper
```

Run the following command

```
./gradlew run
```

## Gameplay

![jminesweeper](https://media.giphy.com/media/laxylu2V2JubONIvUY/giphy.gif)

## Authors and acknowledgment

Project carried out by D. Ravalico and G. Cimador.

## License

Repository licensed with the MIT license. See the [LICENSE](LICENSE.md) for rights and limitations.
