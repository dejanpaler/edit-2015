package com.ev3.item;

public class Location {
	
	private int row;
	private int col;
	private direction dir;
	
	public Location(int r, int c, direction d)
	{
		this.row = r;
		this.col = c;
		this.dir = d;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return col;
	}
	
	public direction getDirection()
	{
		return dir;
	}
	
	 @Override public String toString()
	 {
		 String d = (dir == direction.up) ? "up" : "down";
		 
		 return Integer.toString(row) + " " + Integer.toString(col) + " " + d;
	 }
}
