/**
 * This abstract class creates a serializable Shape object.
 * @author Karyn Vo, Jennifer Tran
 */

package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class Shape implements Serializable{
	private int x, y, arg3, arg4;
	private Color color;
	
	/**
	 * Class constructor that initializes private instance variables
	 *
	 * @param x - x coordinate of Shape
	 * @param y - y coordinate of Shape
	 * @param color - Color of Shape
	 * @param arg3 - width of Shape
	 * @param arg4 -height of Shape
	 * 
	 */
	public Shape(double x, double y, Color color, double arg3, double arg4){
		this.x = (int) x;
		this.y = (int) y;
		this.color = color;
		this.arg4 = (int) arg4;
		this.arg3 = (int) arg3;
	}
	
	/**
	 * Returns the x coordinate
	 * @return an int representing the x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the y coordinate
	 * @return an int representing the y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns the selected color
	 * @return a Color representing the color of the Shape
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Returns the 4th argument
	 * @return an int representing the 4th argument
	 */
	public int getArg4() {
		return arg4;
	}
	
	/**
	 * Returns the 3rd argument
	 * @return an int representing the 3rd argument
	 */
	public int getArg3() {
		return arg3;
	}
	
	/**
	 * This abstract method draws the Shape depending on which one it is
	 * @param g2 - a Graphics2D object to use Graphics2D method to draw
	 */
	public abstract void draw(Graphics2D g2);
}
