Paint Zone (PZ)
A Capture-the-Flag Paintball Minigame for Paper 1.19.2+

My goal for Paint Zone:

⸻

Description
Paint Zone is a fast-paced minigame where two teams, Red and Blue, battle using paintball guns while defending and capturing flags.
Players choose from three kits by default, join an arena, and compete in a fully automated match system with respawns, hit tracking through armor pieces,
and a fireworks finale for the winning team.

⸻

How the Game Works
	1.	Players use GUI items to select a kit and arena.
	2.	When at least 2 players join an arena, a 30-second bossbar countdown begins.
	3.	Teams are assigned randomly, and players are teleported to their team bases.
	4.	Players “die” after 4 hits (each hit removes one piece of dyed leather armor the color of the player's team).
	5.	Dead players enter spectator mode for 5 seconds before respawning at their base.
	6.	To capture a flag:
	•	Right-click the enemy flag
	•	Return to your base and right-click your own flag
	7.	First team to 3 captures wins the game.
	8.	Winners receive a short fireworks celebration before all players return to the Paint Zone lobby.

⸻

Kits
	•	Paintball Gun – 16 shots, 5-second reload.
	•	Sniper – 5 long-range shots, reloads 3 shots.
	•	Shotgun – Fires 5 pellets per shot in a spread, using 1 ammo per trigger.

All kit attributes are configurable in kits.yml.

⸻

Flags
	•	Flags are single wool blocks.
	•	Taking a flag removes the wool block, puts a matching wool block on the player’s head, and gives one to their inventory.
	•	Flag carriers cannot drop the flag.
	•	If the carrier dies, the flag instantly returns to its base.

⸻

Arenas
	•	Unlimited arenas supported.
	•	Each arena includes spawn points, flag locations, and a spectate location.
	•	Maximum players per team is configurable (default: 10).
	•	Minimum 2 players required to start.

Arena data is stored in arenas.yml.

⸻

Commands

Player Commands:
/pz – Teleports to the Paint Zone lobby and gives the selector items.

Admin Commands:
/pz admin set lobby
/pz admin arena <name> create
/pz admin arena <name> delete
/pz admin arena <name> set red spawn
/pz admin arena <name> set blue spawn
/pz admin arena <name> set spectate
/pz admin arena <name> set red flag
/pz admin arena <name> set blue flag
/pz admin setjoinitem
/pz admin set kitselectitem


⸻

Files
	•	config.yml – General settings, GUI items, custom kill messages.
	•	kits.yml – All kit definitions and gun stats.
	•	arenas.yml – Arena list and locations.

⸻

Additional Notes
	•	Leaving the world, disconnecting, or being kicked removes the player from the game.
	•	Custom kill messages are supported and colored by team.
	•	Selector items are undroppable, unmovable, and always in slots 0 and 1.
