/**
 * Creates a Rectangle object and extends Shape
 * @author Karyn Vo, Jennifer Tran
 */

package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Shape{
	
	/**
	 * Calls super to initialize parameters
	 * @param x - x coordinate of Rectangle
	 * @param y - y coordinate of Rectangle
	 * @param color - Color of Rectangle
	 * @param width - width of Rectangle
	 * @param height -height of Rectangles
	 * 
	 */
	public Rectangle(double x, double y, Color color, double width, double height) {
		super(x, y, color, width, height);
	}

	/**
	 * Draws a 2D rectangle using Graphics 2D
	 * @param g2 - draws Rectangle2D
	 */
	@Override
	public void draw(Graphics2D g2) {
		g2.fill(new Rectangle2D.Double(getX(), getY(), getArg3(), getArg4()));
	}
}
