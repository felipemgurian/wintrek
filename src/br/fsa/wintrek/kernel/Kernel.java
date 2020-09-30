package br.fsa.wintrek.kernel;

import java.util.ArrayList;
import java.util.Random;

public class Kernel {
	
	//CONSTANTS
	public static final int PLAYER = 1;
	public static final int ALIEN  = 1 << 1;
	public static final int STAR   = 1 << 2;
	public static final int BASE   = 1 << 3;
	
	public static final int LIMITALIENSPERQUADRANT = 2;
	public static final int LIMITSTARSPERQUADRANT  = 2;
	public static final int LIMITBASESPERQUADRANT  = 1;
	public static final int MAXPLAYERLIFE  = 500;
	public static final int MAXPHOTON  = 10;

	//VARIABLES
	Random random;
	int[][] world;
	public Alien[] aliens;
	int playerX       = 0;
	int playerY       = 0;
	int playerLife    = MAXPLAYERLIFE;
	int totalAliens   = 20;
	int totalStars    = 20;
	int totalBases    = 3;
	int totalEnergy   = 3000;
	int totalShield   = 0;
	int totalPhoton   = 10;
	int phaserPower   = 0;
	int timer         = 0;
	
	public Kernel() {
		this.world = new int[64][64];
		this.aliens = new Alien[totalAliens];
        random = new Random();
	}
	
    public void newGame() {

        startBoard();
        
		 new Thread(new Runnable() {
	            @Override
	            public void run() {
	             while(true) { 
	            	 try
	            	 {
	            	     Thread.sleep(1000);
	            	     timer++;
	            	 }
	            	 catch(InterruptedException ex)
	            	 {
	            	     Thread.currentThread().interrupt();
	            	 }
	             }
	            }
		  }).start();
		 
    }
	
    private void startBoard() {
    	addPlayer();
    	addAliens();
    	addStars();
    	addBases();
    }
	
    private void addPlayer() {
		this.world[playerX][playerY] |= PLAYER;
    }
    
	private void addAliens() {
	    int x = 0;
	    int y = 0;
	    
		int count = 0;
		
        x = random.nextInt(63);
        y = random.nextInt(63);
        
		while(true) {
			
			if(count == totalAliens) {
				break;
			}

			if(x != playerX && y != playerY && !hasAlien(x,y) && limitAlienQuadrant(x,y)) {
				int alienLife = (int) ((Math.random() * (300 - 150)) + 150);
				this.world[x][y] |= ALIEN;
				this.aliens[count] = new Alien(x, y, alienLife);
				count++;
				continue;
			}
			
	        x = random.nextInt(63);
	        y = random.nextInt(63);
		}
		
	}
	
	private void addStars() {
	   int x = 0;
	   int y = 0;
	    
		int count = 0;
		
        x = random.nextInt(63);
        y = random.nextInt(63);
        
		while(true) {
			
			if(count == totalStars) {
				break;
			}

			if(x != playerX && y != playerY && !hasAlien(x,y) && !hasStar(x,y) && limitStarQuadrant(x,y)) {
				this.world[x][y] |= STAR;
				count++;
				continue;
			}
			
	        x = random.nextInt(63);
	        y = random.nextInt(63);
		}
	}
	
	private void addBases() {
	   int x = 0;
	   int y = 0;
	    
		int count = 0;
		
        x = random.nextInt(63);
        y = random.nextInt(63);
        
		while(true) {
			
			if(count == totalBases) {
				break;
			}

			if(x != playerX && y != playerY && !hasAlien(x,y) && !hasStar(x,y) && !hasBase(x,y) && limitBaseQuadrant(x,y)) {
				this.world[x][y] |= BASE;
				count++;
				continue;
			}
			
	        x = random.nextInt(63);
	        y = random.nextInt(63);
		}
	}
	
	public boolean impulse(int angle, int dist) {
		int x = getXWithAngle(angle, dist) + this.playerX;
		int y = getYWithAngle(angle, dist) + this.playerY;
		
		int quadrantX = getQuadrantX(x);
		int quadrantY = getQuadrantY(y);
		
		int quadrantPlayerX = getQuadrantX(this.playerX);
		int quadrantPlayerY = getQuadrantY(this.playerY);

		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		if(x < 0 || x > 63 || y < 0 || y > 63 || quadrantX != quadrantPlayerX || quadrantY != quadrantPlayerY) return false;
		
		if(!hasAlien(x, y) && !hasStar(x, y) && !hasBase(x, y)) {
			this.world[this.playerX][this.playerY] = 0;
			this.world[x][y] |= PLAYER;
			this.playerX = x;
			this.playerY = y;
			this.timer += dist*86400;
			
			 if(playerX < 63 && hasBase((playerX+1), playerY)) { 
	    		 totalEnergy = 3000 - totalShield;
	    		 playerLife = MAXPLAYERLIFE;
	    		 totalPhoton = MAXPHOTON;
        		 System.out.println("Status restored!");

			 }
			 else if(playerX > 0 && hasBase((playerX-1), playerY)) { 
	    		 totalEnergy = 3000 - totalShield;
	    		 playerLife = MAXPLAYERLIFE;
	    		 totalPhoton = MAXPHOTON;
        		 System.out.println("Status restored!");

			 }
			 else if(playerY < 63 && hasBase(playerX, (playerY+1))) { 
	    		 totalEnergy = 3000 - totalShield;
	    		 playerLife = MAXPLAYERLIFE;
	    		 totalPhoton = MAXPHOTON;
        		 System.out.println("Status restored!");

			 }
			 else if(playerY > 0 && hasBase(playerX, (playerY-1))) { 
	    		 totalEnergy = 3000 - totalShield;
	    		 playerLife = MAXPLAYERLIFE;
	    		 totalPhoton = MAXPHOTON;
        		 System.out.println("Status restored!");

			 }
		 
			for(int i = firstX; i <= lastX; i++) {
		    	 for(int j = firstY; j <= lastY; j++) {
		    		if(hasAlien(i, j)) {

		    			phaserAttackAlien(i, j);
		    			
		    			int angleAlien = (int) ((Math.random() * (359 - 0)) + 0);
						int distAlien = 1;
						
						impulseAlien(i, j, angleAlien, distAlien);

		    		}
		    	 }
	    	 }
			
			return true;
		}
		
		return false;
	}
	public boolean impulseAlien(int alienX, int alienY, int angle, int dist) {
		int x = getXWithAngle(angle, dist) + alienX;
		int y = getYWithAngle(angle, dist) + alienY;
		
		int quadrantX = getQuadrantX(x);
		int quadrantY = getQuadrantY(y);
		
		int quadrantPlayerX = getQuadrantX(this.playerX);
		int quadrantPlayerY = getQuadrantY(this.playerY);

		
		if(x < 0 || x > 63 || y < 0 || y > 63 || quadrantX != quadrantPlayerX || quadrantY != quadrantPlayerY) return false;

		if(!hasAlien(x, y) && !hasStar(x, y) && !hasBase(x, y) && x != this.playerX && y != this.playerY) {
			
			this.world[x][y] |= ALIEN;
			this.world[alienX][alienY] = 0;
			
			for(int i = 0; i < this.aliens.length; i++) {
				if(this.aliens[i].getX() == alienX && this.aliens[i].getY() == alienY) {
					this.aliens[i].setX(x);
					this.aliens[i].setY(y);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	
	public boolean warp(int angle, int dist) {
		int quadrantX = getXWithAngle(angle, dist) + getQuadrantX(this.playerX);
		int quadrantY = getYWithAngle(angle, dist) + getQuadrantY(this.playerY);
		
		if(quadrantX < 0 || quadrantX > 7 || quadrantY < 0 || quadrantY > 7) return false;

		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		
		int firstX = lastX - 7;
		int firstY = lastY - 7;

		while(true) {
			int x = (int) ((Math.random() * (lastX - firstX)) + firstX);
			int y = (int) ((Math.random() * (lastY - firstY)) + firstY);
			
			if(!hasAlien(x, y) && !hasStar(x, y) && !hasBase(x, y)) {
				this.world[x][y] |= PLAYER;
				this.world[this.playerX][this.playerY] = 0;
				this.playerX = x;
				this.playerY = y;
				this.timer = this.timer + dist*86400;
				
				 if(playerX < 63 && hasBase((playerX+1), playerY)) { 
            		 totalEnergy = 3000 - totalShield;
            		 playerLife = MAXPLAYERLIFE;
            		 totalPhoton = MAXPHOTON;
            		 System.out.println("Status restored!");
				 }
        		 else if(playerX > 0 && hasBase((playerX-1), playerY)) { 
            		 totalEnergy = 3000 - totalShield;
            		 playerLife = MAXPLAYERLIFE;
            		 totalPhoton = MAXPHOTON;
            		 System.out.println("Status restored!");

				 }
        		 else if(playerY < 63 && hasBase(playerX, (playerY+1))) { 
            		 totalEnergy = 3000 - totalShield;
            		 playerLife = MAXPLAYERLIFE;
            		 totalPhoton = MAXPHOTON;
            		 System.out.println("Status restored!");

				 }
        		 else if(playerY > 0 && hasBase(playerX, (playerY-1))) { 
            		 totalEnergy = 3000 - totalShield;
            		 playerLife = MAXPLAYERLIFE;
            		 totalPhoton = MAXPHOTON;
            		 System.out.println("Status restored!");

				 }
				return true;
			}
		}
		
	}
	
	public boolean phaserAttack(int attackType, int damage) {
		if(getEnergy() < damage) {
			System.out.println("Not enougth energy to this action!");
			return false;
		}
		
		int quadrantX = (int) Math.ceil((this.playerX)/8);
		int quadrantY = (int) Math.ceil((this.playerY)/8);
				
		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		int[]  aliensIndexes = new int[LIMITALIENSPERQUADRANT];
		
		int aliensInArray = 0;
		
		for(int i = firstX; i <= lastX; i++) {
			for(int j = firstY; j <= lastY; j++) {
				
				for(int k = 0; k < this.aliens.length; k++) {
					if(this.aliens[k].getX() == i && this.aliens[k].getY() == j) {
						aliensIndexes[aliensInArray] = k;
						aliensInArray++;
					}
				}
			}
		}
		
		if(attackType == 0) {
			for(int i = 0; i < aliensInArray; i++) {
				int index = aliensIndexes[i];

				int x = this.aliens[index].getX();
				int y = this.aliens[index].getY();
				
				ArrayList<Coordinates> coordinatesToAlien = getLine(this.playerX, this.playerY, x, y);
				
				boolean hasStar = false;
				
				for(int j = 0; j < coordinatesToAlien.size(); j++) {
					if(hasStar(coordinatesToAlien.get(j).getX(), coordinatesToAlien.get(j).getY())) {
						hasStar = true;
						break;
					}
				}
				
				if(hasStar) {
					System.out.println();
					System.out.println("A Star in the way blocks the shoot!");
					System.out.println("Alien in pos "+y+"-"+x+" don't receive damage");
					continue;
				}
				
				this.aliens[index].damage(damage/aliensInArray);
				this.totalEnergy -= (damage/aliensInArray);
				System.out.println("Alien in pos "+y+"-"+x+" receive damage "+(damage/aliensInArray));
				System.out.println("Alien in pos "+y+"-"+x+" life is " + this.aliens[index].getLife());

				if(this.aliens[index].getLife() <= 0) {
					System.out.println("Alien in pos "+y+"-"+x+" was destroyed!");
					this.world[x][y] = 0;
					this.totalAliens--;
					this.aliens[index].setX(-1);
					this.aliens[index].setY(-1);
				}
			}
			return true;

		} else {
			
			int closestAlienIndex = aliensIndexes[0];
			
			for(int i = 1; i < aliensInArray; i++) {
				int index = aliensIndexes[i];
				
				int x = this.aliens[index].getX();
				int y = this.aliens[index].getY();
				
				int closX = this.aliens[closestAlienIndex].getX();
				int closY = this.aliens[closestAlienIndex].getY();

				if(x+y < closX + closY) {
					closestAlienIndex = index;
				}
			}
			
			int x = this.aliens[closestAlienIndex].getX();
			int y = this.aliens[closestAlienIndex].getY();
			
			ArrayList<Coordinates> coordinatesToAlien = getLine(this.playerX, this.playerY, x, y);
			
			boolean hasStar = false;
			
			for(int j = 0; j < coordinatesToAlien.size(); j++) {
								
				if(hasStar(coordinatesToAlien.get(j).getX(), coordinatesToAlien.get(j).getY())) {
					hasStar = true;
					continue;
				}
			}
			
			if(hasStar) {
				System.out.println();
				System.out.println("A Star in the way blocks the shoot!");
				System.out.println("Alien in pos "+y+"-"+x+" don't receive damage");
				return false;
			}
			
			this.aliens[closestAlienIndex].damage(damage);
			this.totalEnergy -= (damage/aliensInArray);
			System.out.println("Alien in pos "+y+"-"+x+" receive damage "+(damage));
			System.out.println("Alien in pos "+y+"-"+x+" life is " + this.aliens[closestAlienIndex].getLife());

			if(this.aliens[closestAlienIndex].getLife() <= 0) {
				System.out.println("Alien in pos "+y+"-"+x+" was destroyed!");
				this.world[x][y] = 0;
				this.totalAliens--;
				this.aliens[closestAlienIndex].setX(-1);
				this.aliens[closestAlienIndex].setY(-1);
			}
			
			
		}
		
		return false;
	}
	
	public boolean phaserAttackAlien(int alienX, int alienY) {
		
		int quadrantX = (int) Math.ceil((alienX)/8);
		int quadrantY = (int) Math.ceil((alienY)/8);
			
		int quadrantPlayerX = (int) Math.ceil((this.playerX)/8);
		int quadrantPlayerY = (int) Math.ceil((this.playerY)/8);
		
		if(quadrantX != quadrantPlayerX || quadrantY != quadrantPlayerY) return false;
		
		ArrayList<Coordinates> coordinatesToPlayer = getLine(alienX, alienY, this.playerX, this.playerY);
		
		boolean hasStar = false;
		
		for(int j = 0; j < coordinatesToPlayer.size(); j++) {
			if(hasStar(coordinatesToPlayer.get(j).getX(), coordinatesToPlayer.get(j).getY())) {
				hasStar = true;
				break;
			}
		}
		
		if(hasStar) return false;
		
		int damage = (int) ((Math.random() * (200 - 100)) + 100);
		
		if(this.totalShield > damage) {
			this.totalShield -= damage;
			
			System.out.println();
			System.out.println("Alien attack and cause: " + damage + " damage in shield!");
			System.out.println();
			return true;
			
		}else if(this.totalShield < damage) {
			damage -= this.totalShield;
			
			System.out.println();
			System.out.println("Alien attack and cause: " + this.totalShield + " damage in shield!");

			this.totalShield = 0;
			
			this.playerLife -= damage;
			System.out.println("Alien attack and cause: " + damage + " damage in player!");
			System.out.println();
			
			
			return true;

		}
		
		return false;
	}
	
	public boolean photonAttack(int angle, int dist) {
		int x = getXWithAngle(angle, dist) + this.playerX;
		int y = getYWithAngle(angle, dist) + this.playerY;
		
		int quadrantPlayerX = getQuadrantX(this.playerX);
		int quadrantPlayerY = getQuadrantY(this.playerY);

		int quadrantX = getQuadrantX(x);
		int quadrantY = getQuadrantY(y);
		
		if(quadrantX != quadrantPlayerX || quadrantY != quadrantPlayerY || x < 0 || x > 63 || y < 0 || y > 63) {
			System.out.println();
			System.out.println("You miss the shoot!");
			this.totalPhoton--;
			return false;
		}
		

		if(hasAlien(x, y)) {
			
			ArrayList<Coordinates> coordinatesToAlien = getLine(this.playerX, this.playerY, x, y);
			
			boolean hasStar = false;
			
			for(int j = 0; j < coordinatesToAlien.size(); j++) {
				if(hasStar(coordinatesToAlien.get(j).getX(), coordinatesToAlien.get(j).getY())) {
					hasStar = true;
					break;
				}
			}
			
			if(hasStar) {
				System.out.println();
				System.out.println("A Star in the way blocks the shoot!");
				System.out.println("Alien in pos "+y+"-"+x+" don't receive damage");
				this.totalPhoton--;
				return false;
			}
			
			for(int i = 0; i < this.aliens.length; i++) {
				if(this.aliens[i].getX() == x && this.aliens[i].getY() == y) {
					this.aliens[i].damage(this.aliens[i].getLife());
					this.totalPhoton--;
					
					if(this.aliens[i].getLife() <= 0) {
						this.world[x][y] = 0;
						this.totalAliens--;
						this.aliens[i].setX(-1);
						this.aliens[i].setY(-1);
						System.out.println("Alien in pos "+y+"-"+x+" was destroyed!");
					}
					
					return true;
				}
			}
		}
		this.totalPhoton--;
		System.out.println("You miss the shoot!");
		return false;
	}
	
	
	public void setOneQuadrant(int quadrantX, int quadrantY) {
		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		
		for(int i = lastX; i > lastX - 8; i--) {
			for(int j = lastY; j > lastY - 8; j--) {
				this.world[i][j] = 1;
			}
		}
	}
	
	public boolean increaseShield(int amount) {
		if(this.totalEnergy > amount) {
			this.totalShield += amount;
			this.totalEnergy -= amount;
			
			return true;
		}
		return false;
	}
	
	public boolean decreaseShield(int amount) {
		if(this.totalShield >= amount) {
			this.totalShield -= amount;
			this.totalEnergy += amount;
			
			return true;
		}
		return false;
	}
	
	//GETS
	public int[][] getWorld() {
		return this.world;
	}
	
	public int getTotalAliensQuadrant(int quadrantX, int quadrantY) {
		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		int aliens = 0;
		
		for(int i = firstX; i <= lastX; i++) {
			for(int j = firstY; j <= lastY; j++) {
				if(this.world[i][j] == (this.world[i][j] | ALIEN) ) {
					aliens++;
				}
			}
		}
		
		return aliens;
	}

	public int getTotalStarsQuadrant(int quadrantX, int quadrantY) {
		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		int stars = 0;
		
		for(int i = firstX; i <= lastX; i++) {
			for(int j = firstY; j <= lastY; j++) {
				if(this.world[i][j] == (this.world[i][j] | STAR) ) {
					stars++;
				}
			}
		}
		
		return stars;
	}
	
	public int getTotalBasesQuadrant(int quadrantX, int quadrantY) {
		int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		int firstX = lastX - 7;
		int firstY = lastY - 7;
		
		int bases = 0;
		
		for(int i = firstX; i <= lastX; i++) {
			for(int j = firstY; j <= lastY; j++) {
				if(this.world[i][j] == (this.world[i][j] | BASE) ) {
					bases++;
				}
			}
		}
		
		return bases;
	}
	
	public int getQuadrantX(int x) {
	 	int quadrantX = (int) Math.ceil((x)/8);
	 	return quadrantX;
	}
	
	public int getQuadrantY(int y) {
		int quadrantY = (int) Math.ceil((y)/8);
		return quadrantY;
	}
	
	public int getLastX(int quadrantX) {
		return ((quadrantX + 1) * 8) - 1;
	}
	
	public int getLastY(int quadrantY) {
		return ((quadrantY + 1) * 8) - 1;
	}
	
	public int getRealPlayerX(int x) {
        int quadrantX = getQuadrantX(this.playerX);
        int lastX = ((quadrantX + 1) * 8) - 1;
        int diff = 7 - x;
        int realPlayerX = lastX - diff;
		return realPlayerX;
	}
	
	public int getRealPlayerY(int y) {
        int quadrantY = getQuadrantY(this.playerY);
        int lastY = ((quadrantY + 1) * 8) - 1;
        int diff = 7 - y;
        int realPlayerY = lastY - diff;
		return realPlayerY;
	}
	
	public int getRealX(int x, int comparative) {
        int quadrantX = getQuadrantX(comparative);
        int lastX = ((quadrantX + 1) * 8) - 1;
        int diff = 7 - x;
        int realPlayerX = lastX - diff;
		return realPlayerX;
	}
	
	public int getRealY(int y, int comparative) {
        int quadrantY = getQuadrantY(comparative);
        int lastY = ((quadrantY + 1) * 8) - 1;
        int diff = 7 - y;
        int realPlayerY = lastY - diff;
		return realPlayerY;
	}
	
	public int getSectionX(int x) {
		int quadrantX = (int) Math.ceil((x)/8);
		int lastX = ((quadrantX + 1) * 8) - 1;
		
		int diff = lastX - x;
	
		int sectionX = 7 - diff;
		
		return sectionX;
	}
	
	public int getSectionY(int y) {
		int quadrantY = (int) Math.ceil((y)/8);
		int lastY = ((quadrantY + 1) * 8) - 1;
		
		int diff = lastY - y;
	
		int sectionY = 7 - diff;
		return sectionY;
	}
	
	public int getPlayerX() {
		return this.playerX;
	}
	
	public int getPlayerY() {
		return this.playerY;
	}
	
	public int getShield() {
		return this.totalShield;
	}
	
	public int getEnergy() {
		return this.totalEnergy;
	}
	
	public int getPhoton() {
		return this.totalPhoton;
	}
	
	public int getPhaserPower() {
		return this.phaserPower;
	}
	
	public int getAliens() {
		return this.totalAliens;
	}
	
	public int getTimer() {
		return this.timer;
	}
	
	public int getPlayerLife() {
		return this.playerLife;
	}
	
	public int getXWithAngle(int angle, int dist) {
		double alpha = Math.toRadians(angle);
		
		int x = (int) Math.round(-Math.sin(alpha) * dist);
				
		return x;
	}
	
	public int getYWithAngle(int angle, int dist) {
		double alpha = Math.toRadians(angle);
		
		int y = (int) Math.round(Math.cos(alpha) * dist);
				
		return y;
	}
	
	public ArrayList<Coordinates> getLine(int xi, int yi, int xf, int yf) {
		
		ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
		
		int dx = Math.abs(xf - xi);
        int dy = Math.abs(yf - yi);
        
        int sx = xi < xf ? 1 : -1;
        int sy = yi < yf ? 1 : -1; 
        
        double err = (dx>dy ? dx : -dy)/2;
        
        while (true) {
        	
        	coordinates.add(new Coordinates(xi, yi));
        	
        	if (xi == xf && yi == yf) break;
        	
        	double e2 = err;
        	
        	if (e2 > -dx) { err -= dy; xi += sx; }
        	if (e2 < dy) { err += dx; yi += sy; }
        }
        
        return coordinates;

	}
	
	//CHECKS
    public boolean hasAlien(int x, int y) {
    	if(this.world[x][y] == (this.world[x][y] | ALIEN)) return true;
    	return false;
    }
    
    public boolean hasPlayer(int x, int y) {
    	if(this.world[x][y] == (this.world[x][y] | PLAYER)) return true;
    	return false;
    }
    
    public boolean hasStar(int x, int y) {
    	if(this.world[x][y] == (this.world[x][y] | STAR)) return true;
    	return false;
    }
    
    public boolean hasBase(int x, int y) {
    	if(this.world[x][y] == (this.world[x][y] | BASE)) return true;
    	return false;
    }
    
    public boolean limitAlienQuadrant(int x, int y) {
    	int quadrantX = (int) Math.ceil((x)/8);
    	int quadrantY = (int) Math.ceil((y)/8);
    	
    	int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		
		int totalAliens = 0;

		for(int i = lastX; i > lastX - 8; i--) {
			for(int j = lastY; j > lastY - 8; j--) {
				if(this.world[i][j] == (this.world[i][j] | ALIEN) ) {
					totalAliens++;
				}
			}
		}
		
		if(totalAliens < LIMITALIENSPERQUADRANT) return true;

		return false;
    }
    
    public boolean limitBaseQuadrant(int x, int y) {
    	int quadrantX = (int) Math.ceil((x)/8);
    	int quadrantY = (int) Math.ceil((y)/8);
    	
    	int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY + 1) * 8) - 1;
		
		int totalBases = 0;

		for(int i = lastX; i > lastX - 8; i--) {
			for(int j = lastY; j > lastY - 8; j--) {

				if(this.world[i][j] == (this.world[i][j] | BASE) ) {
					totalBases++;
				}
				
			}
		}
		
		if(totalBases < LIMITBASESPERQUADRANT) return true;
    	
    	return false;
    }
    
    public boolean limitStarQuadrant(int x, int y) {
    	int quadrantX = (int) Math.ceil((x)/8);
    	int quadrantY = (int) Math.ceil((y)/8);
    	
    	int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY+ 1) * 8) - 1;
		
		int totalStars = 0;

		for(int i = lastX; i > lastX - 8; i--) {
			for(int j = lastY; j > lastY - 8; j--) {
				if(this.world[i][j] == (this.world[i][j] | STAR) ) {
					totalStars++;
				}
			}
		}
		
		if(totalStars < LIMITSTARSPERQUADRANT) return true;
    	
    	return false;
    }
    
    public boolean checkAlienQuadrant(int x, int y) {
    	int quadrantX = (int) Math.ceil((x)/8);
    	int quadrantY = (int) Math.ceil((y)/8);
    	
    	int lastX = ((quadrantX + 1) * 8) - 1;
		int lastY = ((quadrantY+ 1) * 8) - 1;
		
		for(int i = lastX; i > lastX - 8; i--) {
			for(int j = lastY; j > lastY - 8; j--) {
				if(this.world[i][j] == (this.world[i][j] | ALIEN) ) {
					return true;
				}
			}
		}
    	
    	return false;
    }
    
}
