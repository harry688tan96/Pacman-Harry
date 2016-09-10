# pacman

### This is just a summary of the Pacman-Harry game created in my leisure time. 
### *More details are explained in the pdf attached.*

Pacman-Harry is a Unix console text-based game. It is slightly different from the original Pacman game in terms of the maze display and Ghosts' behaviours. Besides, I incorporated _Portal_, which was inspired by the "Portal" video game series - https://en.wikipedia.org/wiki/Portal_(video_game), in this game. The famous _A* algorithm_ for pathfinding is also added in all of the Ghosts in order to increase the difficulty of this game. Moreover, one of the Maps that I designed in this game has a "**FLIP mode**" (it is briefly explained below and in details in the pdf).

Two Maps, **Map 1** and **Map 2**, are currently available in Pacman-Harry.
Before the game starts, the player could select any Map for the first round as well as for the second round. During the game, the selected Map will be printed out consistently and continuously on the console screen.
The first Map (Map 1) resembles the original Pacman maze. However, if you notice carefully, the maze blocks in Map 1 (the blocks above and below the 
PACMAN"  heading block) are designed with my name "Harry". 
The second Map (Map 2) is a fairly small maze. Map 2 is a special map as this Map has a "**FLIP mode**". Map 2 will flip horizontally every 20 seconds. During the "FLIP mode", the LEFT and RIGHT direction for Pacman are both reversed. 

There are four Ghosts in Pacman-Harry. Each of the Ghosts has its own Artificial Intelligence (AI). 
**Ghost 1** has the ability to cleverly avoid Pacman if the Pacman (who has already eaten the Power Pellet) is trying to approach him. 
**Ghost 2** is capable of hunting down the Pacman if he enters into a dead-end maze blocks nearby. 
**Ghost 3** has the ability to shoot a Lazer onto Pacman during "Chase mode" in order to slow him down and ease the other Ghosts in attacking him. 
**Ghost 4** has the ability to destroy Portal(s) created by Pacman. 
All of the Ghosts use _A* algorithm_ (shortest pathfinding algorithm) in tracking the location of Pacman during the Ghosts' "Chase mode".

In Pacman-Harry, Pacman is capable of creating Portal for teleporting between two places. In order for teleportation to happen, two different Portal Gates have to be created on different wall blocks or maze blocks. Portal might comes into handy if the Ghosts are in "Chase mode"!!!

In Pacman-Harry, several colours are used:
Pacman - _yellow_ 
Ghost 1 - _red_
Ghost 2 - _purple_
Ghost 3 - _green_
Ghost 4 - _cyan_
Portals - _blue_
Lazer shot by Ghost 3 - _colourful_
Wall Bricks, Maze Blocks, Pac-dots and Power Pellets - _white_

Cheat codes are available in the game (mentioned in the pdf).


#### REMARK: 
#### During the game, if a player wishes to input a command (for instance "a" to change the direction of Pacman to the left), ENTER key has to be pressed after the input in order for the game to read in the input command in the terminal.
