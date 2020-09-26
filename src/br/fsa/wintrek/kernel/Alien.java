package br.fsa.wintrek.kernel;

public class Alien {
	private int x;
	private int y;
	private int life;
	
	public Alien(int x, int y, int life) {
		this.x = x;
		this.y = y;
		this.life = life;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getLife() {
		return this.life;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void damage(int damage) {
		this.life -= damage;
	}
}
