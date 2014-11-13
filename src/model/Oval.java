/**
 * Creates a Oval object and extends Shape
 * @author Karyn Vo, Jennifer Tran
 */

package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Oval extends Shape{
	
	/**
	 * Calls super to initialize parameters
	 * @param x - x coordinate of Oval
	 * @param y - y coordinate of Oval
	 * @param color - Color of Oval
	 * @param width - width of Oval
	 * @param height -height of Oval
	 * 
	 */
	public Oval(double x, double y, Color color, double width, double height) {
		super(x, y, color, width, height);
	}

	/**
	 * Draws a 2D ellipse using Graphics 2D
	 * @param g2 - draws Ellipse2D
	 */
	@Override
	public void draw(Graphics2D g2) {
		g2.fill(new Ellipse2D.Double(getX(), getY(), getArg3(), getArg4()));
	}
}
