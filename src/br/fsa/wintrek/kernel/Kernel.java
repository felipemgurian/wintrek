package br.fsa.wintrek.kernel;

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

	//VARIABLES
	Random random;
	int[][] world;
	int playerX       = 0;
	int playerY       = 0;
	int totalAliens   = 20;
	int totalStars    = 20;
	int totalBases    = 3;
	int totalEnergy   = 3000;
	int totalShield   = 0;
	int totalPhoton   = 10;
	int course        = 90;
	int timer         = 0;
	
	public Kernel() {
		this.world = new int[64][64];
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
				this.world[x][y] |= ALIEN;
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
	
	public void setCourse(int course) {
		if(course > 359) {
			course = course - 360;
		}

		this.course = course;
	}
	
	public boolean warpSimple(int x, int y) {
		if(!(x == getQuadrantX(this.playerX) && y == getQuadrantY(this.playerY)) && x >= 0 && x < 8 && y >= 0 && y < 8) {
			
			int playerSectionX = getSectionX(this.playerX);
			int playerSectionY = getSectionY(this.playerY);
			
			int diffX = 7 - playerSectionX;
			int diffY = 7 - playerSectionY;
			
			int lastX = ((x + 1) * 8) - 1;
			int lastY = ((y + 1) * 8) - 1;
			
			int firstX = lastX - 7;
			int firstY = lastY - 7;
			
			int newX = lastX - diffX;
			int newY = lastY - diffY;
			
			if(!hasAlien(newX, newY) && !hasBase(newX, newY) && !hasStar(newX, newY)) {
				this.world[newX][newY] |= PLAYER;
				this.world[this.playerX][this.playerY] = 0;
				this.playerX = newX;
				this.playerY = newY;
				
				return true;
			} else {
				for(int i = firstX; i <= lastX; i++) {
					for(int j = firstX; j <= firstY; j++) {
						
						if(!hasAlien(i, j) && !hasBase(i, j) && !hasStar(i, j)) {
							this.world[i][j] |= PLAYER;
							this.world[this.playerX][this.playerY] = 0;
							this.playerX = i;
							this.playerY = j;
							
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}
	
	public boolean impulseSimple(int x, int y) {
		if(!(x == getSectionX(this.playerX) && y == getSectionY(this.playerY)) && x >= 0 && x < 8 && y >= 0 && y < 8) {
			
			int realPlayerX = getRealPlayerX(x);
			int realPlayerY = getRealPlayerY(y);
			
			if(!hasAlien(realPlayerX, realPlayerY) && !hasStar(realPlayerX, realPlayerY) && !hasBase(realPlayerX, realPlayerY)) {
				this.world[realPlayerX][realPlayerY] |= PLAYER;
				this.world[this.playerX][this.playerY] = 0;
				this.playerX = realPlayerX;
				this.playerY = realPlayerY;
				
				return true;
			}
			return false;
		}
		return false;
	}
	
	public void impulse(int angle, int total) {
		//need implement, angle é o angulo, total é o total de impulsos
	}
	
	public void warp(int angle, int total) {
		//need implement,  angle é o angulo, total é o total de warp
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
	
	public int getAliens() {
		return this.totalAliens;
	}
	
	public int getTimer() {
		return this.timer;
	}
	
	public int getCourse() {
		return this.course;
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
    
}
