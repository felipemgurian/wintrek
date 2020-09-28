package br.fsa.wintrek.cli;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import br.fsa.wintrek.kernel.Kernel;

public class CLI {
	Kernel game;
	
	public CLI() {
		this.game = new Kernel();
		this.game.newGame();
	}
	
	public void init() {
		 
		  intro();
		  
		  Scanner sc = new Scanner(System.in);
		  
		  printGameScreen();
		  
		  System.out.println("press \"help\" to see available commands!");
		  
		  int round = 0;
		  while(true) {
			  if(game.getTimer() > 3024000) {
				  System.out.println("GAME OVER!");
				  break;
			  }
			  System.out.print("> ");
			  String command = sc.next();
			  int x;
			  int y;
			  int energy;
			  String attacktype;
			  int attacktypeint;
			  
			  switch(command) {
				  case "test":
					  
					  System.out.println();

					  System.out.print("Digite tipo de attack phaser 0 para wide dispersal, 1 para single: ");
					  attacktype = sc.next();
					  
					  System.out.print("Digite a quantidade de energia: ");
					  energy = sc.nextInt();
					  
					  attacktypeint = 0;
					  
					  if(attacktype == "0" || attacktype == "1") {
						  attacktypeint = Integer.parseInt(attacktype);
					  }
					  
					  if(attacktypeint == 0) {
						  System.out.println("Phaser attack - Wide Dispesal");
					  } else {
						  System.out.println("Phaser attack - Target");
					  }
					  
					  game.phaserAttackSimple(attacktypeint, energy);
					  System.out.println();
					  printGameScreen();

					  continue;
				
				  case "help":
					  System.out.println();
					  System.out.println("### COMMANDS ###");
					  System.out.println("help            - Show help commands.");
					  System.out.println("game            - Show game console.");
					  System.out.println("test            - Command to test new features.");
					  System.out.println();
					  System.out.println("#NAVIGATION COMMANDS");
					  System.out.println("warp            - Do a warp movement.");
					  System.out.println("impulse         - Do an impulse movement.");
					  System.out.println();
					  System.out.println("#SHIELD COMMANDS");
					  System.out.println("increase_shield - Transfer energy to shields.");
					  System.out.println("decrease_shield - Remove energy from shields.");
					  System.out.println();
					  System.out.println("#WEAPOM COMMANDS");
					  System.out.println("phaser          - Attack enemy with phaser.");
					  System.out.println("photon          - Attack enemy with photon.");

					  System.out.println();
					  continue;
					  
				  case "game":
					  printGameScreen();
					  continue;
				  
				  case "warp":
				  	  System.out.print("Coordinate x: ");
					  
					  x = sc.nextInt();
					  
					  System.out.print("Coordinate y: ");
					  
					  y = sc.nextInt();
					  
					  boolean warp = game.warpSimple(x, y);
					  
					  if(!warp) {
						  System.out.println("invalid coordinates!");
						  printGameScreen();
						  continue;
					  }

					  printGameScreen();
					  continue;
				  
				  case "impulse":
					  
					  System.out.print("Coordinate x: ");
					  
					  x = sc.nextInt();
					  
					  System.out.print("Coordinate y: ");
					  
					  y = sc.nextInt();
					  
					  boolean impulse = game.impulseSimple(x, y);
					  
					  if(!impulse) {
						  System.out.println("invalid coordinates");
						  printGameScreen();
						  continue;
					  }

					  printGameScreen();
					  continue;

				  case "increase_shield":
					  System.out.print("Energy used: ");
					  energy = sc.nextInt();
					  game.increaseShield(energy);
					  printGameScreen();
					  continue;
					  
				  case "decrease_shield":
					  System.out.print("Energy used: ");
					  energy = sc.nextInt();
					  game.decreaseShield(energy);
					  printGameScreen();
					  continue;
				  
				  case "phaser":
					  System.out.println();

					  System.out.print("Attack Type (0 = wide dispersal/ 1 = single target):  ");
					  attacktype = sc.next();
					  
					  System.out.print("Energy used: ");
					  energy = sc.nextInt();
					  
					  attacktypeint = 0;
					  
					  if(attacktype.equals("0") || attacktype.equals("1")) {
						  attacktypeint = Integer.parseInt(attacktype);
					  }
					  
					  if(attacktypeint == 0) {
						  System.out.println("Phaser attack - Wide Dispesal");
					  } else {
						  System.out.println("Phaser attack - Target");
					  }
					  
					  game.phaserAttackSimple(attacktypeint, energy);
					  System.out.println();
					  printGameScreen();

					  continue;
				  case "photon":
					  System.out.println();
				  	  System.out.print("Coordinate x: ");
					  
					  x = sc.nextInt();
					  
					  System.out.print("Coordinate y: ");
					  
					  y = sc.nextInt();
					  
					  game.photonAttackSimple(x,y);
					  System.out.println();
					  printGameScreen();
					  continue;
					  
				  default:
					  System.out.println("Unexpected command");
					  continue;
					  
			  }
		  }
	
	}
	
	public void intro() {
		  System.out.println("**************************************************************************************************");
		  System.out.println("*                                                                                                *");
		  System.out.println("*                                                                                                *");
		  System.out.println("*                                         FAENG WINTREK                                          *");
		  System.out.println("*                                                                                                *");
		  System.out.println("*                                                                                                *");
		  System.out.println("**************************************************************************************************");
		  System.out.println();
	}
	
	public void printAllWorld() {
		int[][] world = game.getWorld();
		
		System.out.println();
		System.out.println("     0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63");
		System.out.println();
		for(int i = 0; i < 64; i++) {
			
			if(i < 10) {
				System.out.print(" " + i + "  ");

			}else {
				System.out.print(i + "  ");
			}
			
			for(int j = 0; j < 64; j++) {
				if(world[i][j] < 10) {
					System.out.print(" " + world[i][j] + " ");
				}else {
					System.out.print(world[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();

		
	}
	
	public void printGameScreen() {
		int lastX = ((game.getQuadrantX(game.getPlayerX()) + 1) * 8) - 1;
		int lastY = ((game.getQuadrantY(game.getPlayerY()) + 1) * 8) - 1;
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		int[][] world = game.getWorld();
		
		System.out.println(" ________________________________________________________________________________________________");
		System.out.println("|                  |                                                                             |");
		System.out.println("|                  |                                    RADAR                                    |");
		System.out.println("|      STATUS      |_____________________________________________________________________________|");
		System.out.println("|                  |                       |                                                     |");
		System.out.println("|                  |      SHORT RANGE      |                     LONG RANGE                      |");
		System.out.println("|__________________|_______________________|_____________________________________________________|");
		System.out.println("|                  |                       |                                                     |");
		System.out.println("|                  |      0 1 2 3 4 5 6 7  |        0     1     2     3     4     5     6     7  |");
		System.out.println("|  Quadrant:  "+game.getQuadrantX(game.getPlayerX()) + "-"+game.getQuadrantY(game.getPlayerY()) +"  |      _______________  |       _____________________________________________ |");
		
		//LINHA
		for(int i = firstX; i <= lastX; i++) {
			
			//STATUS
			
			if(i-firstX == 0)
				System.out.print("|  Section:   " + game.getSectionX(game.getPlayerX()) + "-" + game.getSectionY(game.getPlayerY()) + "  |");
			else if(i-firstX == 1) {
				if(game.getEnergy() > 999) {
					System.out.print("|  Energy:   " + game.getEnergy() + "  |");
				} else if(game.getEnergy() > 99) {
					System.out.print("|  Energy:    " + game.getEnergy() + "  |");
				} else if(game.getEnergy() > 9) {
					System.out.print("|  Energy:     " + game.getEnergy() + "  |");
				} else {
					System.out.print("|  Energy:      " + game.getEnergy() + "  |");
				}
			} else if(i-firstX == 2) {
				if(game.getShield() > 999) {
					System.out.print("|  Shield:   " + game.getShield() + "  |");
				} else if(game.getShield() > 99) {
					System.out.print("|  Shield:    " + game.getShield() + "  |");
				} else if(game.getShield() > 9) {
					System.out.print("|  Shield:     " + game.getShield() + "  |");
				} else {
					System.out.print("|  Shield:      " + game.getShield() + "  |");
				}
				
			} else if(i-firstX == 3) {
				if(game.getPhoton() > 999) {
					System.out.print("|  Photon:   " + game.getPhoton() + "  |");
				} else if(game.getPhoton() > 99) {
					System.out.print("|  Photon:    " + game.getPhoton() + "  |");
				} else if(game.getPhoton() > 9) {
					System.out.print("|  Photon:     " + game.getPhoton() + "  |");
				} else {
					System.out.print("|  Photon:      " + game.getPhoton() + "  |");
				}
			}else if(i-firstX == 4) {
				//Verificar aqui, hasAlien é só para celula, precisa para quadrant
				System.out.print("|  Condition:   " + (game.checkAlienQuadrant(game.getPlayerX(), game.getPlayerY()) ? "R" : game.getEnergy() < 300 ? "Y" : (game.getEnergy() + game.getShield()) == 3000 ? "B" : "G" ) + "  |");
			
			}else if(i-firstX == 5) {
				if(game.getAliens() > 999) {
					System.out.print("|  Aliens:   " + game.getAliens() + "  |");
				} else if(game.getAliens() > 99) {
					System.out.print("|  Aliens:    " + game.getAliens() + "  |");
				} else if(game.getAliens() > 9) {
					System.out.print("|  Aliens:     " + game.getAliens() + "  |");
				} else {
					System.out.print("|  Aliens:      " + game.getAliens() + "  |");
				}
			}
			
			else if(i-firstX == 6) {
				System.out.print(
						(game.getTimer() > 999999 ? "|  Timer: "     : 
						 game.getTimer() > 99999  ? "|  Timer:  "    : 
						 game.getTimer() > 9999   ? "|  Timer:   "   :
						 game.getTimer() > 999    ? "|  Timer:    "  :
						 game.getTimer() > 99     ? "|  Timer:     " :
						 game.getTimer() > 9      ? "|  Timer:      ": "|  Timer:       ") + game.getTimer() + "  |");
			}
			
			else if(i-firstX == 7) {
				if(game.getPlayerLife() > 999) {
					System.out.print("|  Life:     " + game.getPlayerLife() + "  |");
				} else if(game.getPlayerLife() > 99) {
					System.out.print("|  Life:      " + game.getPlayerLife() + "  |");
				} else if(game.getPlayerLife() > 9) {
					System.out.print("|  Life:       " + game.getPlayerLife() + "  |");
				} else {
					System.out.print("|  Life:        " + game.getPlayerLife() + "  |");
				}
			}
			
			else {
				System.out.print("|                  |");
			}
			
			//Index Linha Short Range
			System.out.print(" " + (i - firstX) + "   |");
  
			for(int j = firstY; j <= lastY; j++) {
				System.out.print((world[i][j] == 1 ? "P" : world[i][j] == 2 ? "A" : world[i][j] == 4 ? "S" : world[i][j] == 8 ? "B" : " ") + " ");
			}
			//IMPLEMENTAR VERIFICAÇÃO APENAS PARA VIZINHOS
			//Index linha Long Range
			System.out.print(" |  " + (i - firstX) + "   |");
			for(int j = 0; j < 8; j++) {

				if(j < 7) {
					System.out.print(game.getTotalAliensQuadrant(i - firstX, j) + "" + game.getTotalBasesQuadrant(i - firstX, j) + game.getTotalStarsQuadrant(i - firstX, j) + "   ");
				} else {
					System.out.print(game.getTotalAliensQuadrant(i - firstX, j) + "" + game.getTotalBasesQuadrant(i - firstX, j) + game.getTotalStarsQuadrant(i - firstX, j) + " ");
				}
			}
			System.out.print("|");
			System.out.println();

		}
		System.out.println("|__________________|_______________________|_____________________________________________________|");
		System.out.println("|                       |                                    |                                   |");
		System.out.println("|       NAVIGATION      |                                    |                                   |");
		System.out.println("|_______________________|____________________________________|___________________________________|");
		System.out.println("|                       |                                    |                                   |");
		System.out.println("|           0           |                                    |                                   |");
		System.out.println("|      345      45      |                                    |                                   |");
		System.out.println("|                       |                                    |                                   |");
		System.out.println("|   270            90   |                                    |                                   |");
		System.out.println("|                       |                                    |                                   |");
		System.out.println("|      255     135      |                                    |                                   |");
		System.out.println("|          100          |                                    |                                   |");
		System.out.println("|_______________________|____________________________________|___________________________________|");

		System.out.println();

		
	}
	
	public void printLongWorld() {
		int[][] world = game.getWorld();
		
		System.out.print("      0     1     2     3     4     5     6     7");
		System.out.println();
		System.out.println("     _____________________________________________");
		for(int i = 0; i < 8; i++) {
			System.out.print(i + "   |");
			for(int j = 0; j < 8; j++) {
				System.out.print(game.getTotalAliensQuadrant(i, j) + "" + game.getTotalBasesQuadrant(i, j) + game.getTotalStarsQuadrant(i, j) + "   ");
			}
			System.out.println();
			
			if(i < 7) {
			 System.out.println("    |");
			}else {
				System.out.println();
			}
		}
		
	}
	
	public void printShortWorld(int quadrantX, int quadrantY) {
		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		int[][] world = game.getWorld();

		System.out.println();
		System.out.println("    0 1 2 3 4 5 6 7");
		System.out.println();
		for(int i = firstX; i <= lastX; i++) {
			System.out.print((i - firstX) + "   ");
			for(int j = firstY; j <= lastY; j++) {
				System.out.print(world[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void setOneQuadrant(int quadrantX, int quadrantY) {
		this.game.setOneQuadrant(quadrantX, quadrantY);
	}
	
}
