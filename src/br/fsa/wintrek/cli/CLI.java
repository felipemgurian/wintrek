package br.fsa.wintrek.cli;

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
			  if(game.getTimer() > 3024000 || game.getPlayerLife() <= 0) {
				  System.out.println("GAME OVER!");
				  break;
			  }

			  System.out.print("> ");
			  String command = sc.next();
			  String temp;
			  int x;
			  int y;
			  int dist;
			  int angle;
			  int energy;
			  String attacktype;
			  int attacktypeint;
			  
			  switch(command) {
				
				  case "help":
					  System.out.println();
					  System.out.println("### COMMANDS ###");
					  System.out.println("help            - Show help commands.");
					  System.out.println("game            - Show game console.");
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
					  System.out.println("#COMPUTER COMMANDS");
					  System.out.println("scan            - Scan the current sector.");
					  System.out.println();
					  continue;
					  
				  case "game":
					  printGameScreen();
					  continue;
				  
				  case "warp":
				  	  System.out.print("Angle: ");
					  
					  temp = sc.next();
					  
					  try {
					       angle = Integer.parseInt(temp);
				      } catch (NumberFormatException nfe) {
					  	  System.out.println("Invalid Angle: 0-359");
				    	  continue;
				      }
					  
					  System.out.print("Distance: ");
					  
					  temp = sc.next();
					  
					  try {
					       dist = Integer.parseInt(temp);
				      } catch (NumberFormatException nfe) {
					  	  System.out.println("Invalid Distance: Number");
				    	  continue;
				      }
					  
					  boolean warp = game.warp(angle, dist);
					  
					  if(!warp) {
						  System.out.println("invalid coordinates!");
						  printGameScreen();
						  continue;
					  }

					  printGameScreen();
					  continue;
				  
				  case "impulse":
					  
					  System.out.print("Angle: ");
					  
					  temp = sc.next();
					  
					  try {
					       angle = Integer.parseInt(temp);
				      } catch (NumberFormatException nfe) {
					  	  System.out.println("Invalid Angle: 0-359");
				    	  continue;
				      }
					  
					  System.out.print("Distance: ");
					  
					  temp = sc.next();
					  
					  try {
					       dist = Integer.parseInt(temp);
				      } catch (NumberFormatException nfe) {
					  	  System.out.println("Invalid Distance: Number");
				    	  continue;
				      }
					  
					  
					  boolean impulse = game.impulse(angle, dist);
					  
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
					  
					  game.phaserAttack(attacktypeint, energy);
					  System.out.println();
					  printGameScreen();

					  continue;
				  case "photon":
				  	  System.out.print("Angle: ");
				  	  
					  temp = sc.next();
					  
					  try {
					       angle = Integer.parseInt(temp);
				      } catch (NumberFormatException nfe) {
					  	  System.out.println("Invalid Angle: 0-359");
				    	  continue;
				      }
					  
					  System.out.print("Distance: ");
					  
					  temp = sc.next();
					  
					  try {
					       dist = Integer.parseInt(temp);
				      } catch (NumberFormatException nfe) {
					  	  System.out.println("Invalid Distance: Number");
				    	  continue;
				      }
					  
					  
					  game.photonAttack(angle, dist);
					  System.out.println();
					  printGameScreen();
					  continue;
				  
				  case "scan":
					  scanCurrentSector();
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
		System.out.println("|  Quadrant:  "+game.getQuadrantY(game.getPlayerY()) + "-"+game.getQuadrantX(game.getPlayerX()) +"  |      _______________  |       _____________________________________________ |");
		
		//LINHA
		for(int i = firstX; i <= lastX; i++) {
			
			//STATUS
			
			if(i-firstX == 0)
				System.out.print("|  Section:   " + game.getSectionY(game.getPlayerY()) + "-" + game.getSectionX(game.getPlayerX()) + "  |");
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

				if(
					  (j == (game.getQuadrantY(game.getPlayerY()) + 1) && i - firstX == game.getQuadrantX(game.getPlayerX()) + 1) ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()) + 1) && i - firstX == game.getQuadrantX(game.getPlayerX()))     ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()))     && i - firstX == game.getQuadrantX(game.getPlayerX()) + 1) ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()) - 1) && i - firstX == game.getQuadrantX(game.getPlayerX()) - 1) ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()) - 1) && i - firstX == game.getQuadrantX(game.getPlayerX()))     ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()))     && i - firstX == game.getQuadrantX(game.getPlayerX()) - 1) ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()) + 1) && i - firstX == game.getQuadrantX(game.getPlayerX()) - 1) ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()) - 1) && i - firstX == game.getQuadrantX(game.getPlayerX()) + 1) ||
					  
					  (j == (game.getQuadrantY(game.getPlayerY()))     && i - firstX == game.getQuadrantX(game.getPlayerX()))     ||
					  
					  ((game.getQuadrantX(game.getPlayerX())) == 0     &&
							  (
									  j == (game.getQuadrantY(game.getPlayerY()))     || 
									  j == (game.getQuadrantY(game.getPlayerY()) + 1) || 
									  j == (game.getQuadrantY(game.getPlayerY()) - 1)
							  ) 
							                                           && i - firstX == game.getQuadrantX(game.getPlayerX()) + 7
					  )                                                                                                           ||
					  
					  ((game.getQuadrantX(game.getPlayerX())) == 7     &&
						  (
								  j == (game.getQuadrantY(game.getPlayerY()))     || 
								  j == (game.getQuadrantY(game.getPlayerY()) + 1) || 
								  j == (game.getQuadrantY(game.getPlayerY()) - 1)
						  ) 
						                                           && i - firstX == game.getQuadrantX(game.getPlayerX()) - 7
					  )                                                                                                           ||
					  
					  ((game.getQuadrantY(game.getPlayerY())) == 0     &&
						  (
								  i - firstX == (game.getQuadrantX(game.getPlayerX()))     || 
								  i - firstX == (game.getQuadrantX(game.getPlayerX()) + 1) || 
								  i - firstX == (game.getQuadrantX(game.getPlayerX()) - 1)
						  ) 
						                                           && j == game.getQuadrantY(game.getPlayerY()) + 7               
					  )                                                                                                           ||
					  
					  
					  ((game.getQuadrantY(game.getPlayerY())) == 7     &&
						  (
								  i - firstX == (game.getQuadrantX(game.getPlayerX()))     || 
								  i - firstX == (game.getQuadrantX(game.getPlayerX()) + 1) || 
								  i - firstX == (game.getQuadrantX(game.getPlayerX()) - 1)
						  ) 
						                                           && j == game.getQuadrantY(game.getPlayerY()) - 7               
					  )                                                                                                           
					  
				  ) {
					
					System.out.print(game.getTotalAliensQuadrant(i - firstX, j) + "" + game.getTotalBasesQuadrant(i - firstX, j) + game.getTotalStarsQuadrant(i - firstX, j) + ( j < 7 ? "   " : " "));
				
				} else {
					System.out.print("---" + ( j < 7 ? "   " : " "));
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
		System.out.println("|           90          |                                    |                                   |");
		System.out.println("|      135      45      |                                    |                                   |");
		System.out.println("|                       |                                    |                                   |");
		System.out.println("|   180             0   |                                    |                                   |");
		System.out.println("|                       |                                    |                                   |");
		System.out.println("|      225      315     |                                    |                                   |");
		System.out.println("|          270          |                                    |                                   |");
		System.out.println("|_______________________|____________________________________|___________________________________|");

		System.out.println();

		
	}
	
	public void scanCurrentSector() {
		
		int quadrantX = game.getQuadrantX(game.getPlayerX());
		int quadrantY = game.getQuadrantY(game.getPlayerY());
		
		int lastX = game.getLastX(quadrantX);
		int lastY = game.getLastY(quadrantY);
		
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		int totalAliens = game.getTotalAliensQuadrant(quadrantX, quadrantY);
		int totalBases  = game.getTotalBasesQuadrant(quadrantX, quadrantY);
		int totalStars  = game.getTotalStarsQuadrant(quadrantX, quadrantY);
		
		System.out.println();
		System.out.println("Quadrant("+quadrantY+", "+quadrantX+") Status:");
		System.out.println("Total Stars: " + totalStars);
		System.out.println("Total Bases: " + totalBases);
		System.out.println("Total Aliens: " + totalAliens);

		for(int i = firstX; i <= lastX; i++) {
			for(int j = firstY; j <= lastY; j++) {
				if(game.hasAlien(i,j)) {
					for(int k = 0; k < game.aliens.length; k++) {
						if(game.aliens[k].getX() == i && game.aliens[k].getY() == j) {
							
							System.out.println("Alien("+game.getSectionY(j)+", "+game.getSectionX(i)+") life: " + game.aliens[k].getLife());
							
						}
					}
				}
			}
		}
		System.out.println();

	}
	
}
