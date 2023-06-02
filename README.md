# erG-Engine

erG-Engine (erb Game Engine):
A game engine with in-game world editor I made after taking an intro to java class.
Made using LibGDX, Box2d, and box2dlights.



Controls:

Press 1: Player mode;
Press 2: Builder mode;
3: Wireframe ON;
4: Wireframe OFF;
5: Lights OFF;
6: Lights ON (bug: lights a little low when turned back on);
ESC + Left-CTRL: quit

-Player mode: 
WASD movement;
Mouse to pan;
Hold shift to climb ladders

-Builder mode (the fun part):
WASD movement;
minus: zoom out;
plus: zoom in;
left/right: cycle through item pallet;
left-click: place item (item stays selected once placed);
right-click: select item;
up/down/left/right: move selected item;
O/L: move selected item up/down in z-axis;
comma/period: rotate selected item;
enter: place/deselect selected item;
backspace: delete selected item;
ESC + 0: save current world state

*note: Use a 'spawner' to place the location of the player. When the world is saved, a spawner is saved in the place of the player
