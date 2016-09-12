# Pacman-Harry

### This is just a summary of the Pacman-Harry game created in my leisure time. 
-----------------------------------
##### *More details are explained in the pdf attached.*
##### *Two demo videos (brief explanations on the characters as well as Map 1 and Map 2) are available in my Youtube Channel*:
###### Map 1 - https://www.youtube.com/watch?annotation_id=annotation_3083261565&feature=iv&src_vid=EgJB08PtlUM&v=u96zMdYGFpU
###### Map 2 - https://www.youtube.com/watch?v=EgJB08PtlUM
-----------------------------------
Pacman-Harry is a Unix console text-based game. It is slightly different from the original Pacman game in terms of the _maze display_ and _the algorithms on the Ghosts' behaviours_. Besides, I incorporated __Portal__, which was inspired by the "Portal" video game series - https://en.wikipedia.org/wiki/Portal_(video_game), in this game. The famous __A* algorithm__ for pathfinding is also added in all of the Ghosts in order to increase the difficulty of this game. Moreover, one of the Maps that I designed in this game has a "**FLIP mode**" (it is briefly explained below and in details in the pdf).

Two Maps, **Map 1** and **Map 2**, are currently available in Pacman-Harry. <br />
Before the game starts, the player could select any Map for the first round as well as for the second round. During the game, the selected Map will be printed out consistently and continuously on the console screen. <br />
The first Map (Map 1) resembles the original Pacman maze. However, if you notice carefully, the maze blocks in Map 1 (the blocks above and below the "PACMAN"  heading block) are designed with my name "Harry". <br />
The second Map (Map 2) is a fairly small maze. Map 2 is a special map as this Map has a "**FLIP mode**". Map 2 will flip horizontally every 20 seconds. During the "**FLIP mode**", the _LEFT_ and _RIGHT_ direction for Pacman are both *reversed*. 

There are four Ghosts in Pacman-Harry. Each of the Ghosts has its own Artificial Intelligence (AI). <br />
**Ghost 1** has the ability to cleverly avoid Pacman if the Pacman (who has already eaten the Power Pellet) is trying to approach him. <br /> 
**Ghost 2** is capable of hunting down the Pacman if he enters into a dead-end maze blocks nearby. <br />
**Ghost 3** has the ability to shoot a Lazer onto Pacman during "Chase mode" in order to slow him down and ease the other Ghosts in attacking him. <br />
**Ghost 4** has the ability to destroy Portal(s) created by Pacman. <br />
All of the Ghosts use __A* algorithm__ (shortest pathfinding algorithm) in tracking the location of Pacman during the Ghosts' "Chase mode".

In Pacman-Harry, Pacman is capable of creating __Portal__ for teleporting between two places. In order for teleportation to happen, two different Portal Gates have to be created on different _wall blocks_ or _maze blocks_. Portal might comes into handy if the Ghosts are in "Chase mode"!!!

In Pacman-Harry, several colours are used: <br />
Pacman - _yellow_ <br />
Ghost 1 - _red_ <br />
Ghost 2 - _purple_ <br />
Ghost 3 - _green_ <br />
Ghost 4 - _cyan_ <br />
Portals - _blue_ <br />
Lazer shot by Ghost 3 - _colourful_ <br />
Wall Bricks, Maze Blocks, Pac-dots and Power Pellets - _white_

Cheat codes are available in the game (mentioned in the pdf).


#### REMARK: 
#### During the game, if a player wishes to input a command (for instance "a" to change the direction of Pacman to the left), ENTER key has to be pressed after the input in order for the game to read in the input command in the terminal.
