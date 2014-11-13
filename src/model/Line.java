/**
 * Creates a Line object and extends Shape
 * @author Karyn Vo, Jennifer Tran
 */
package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Line extends Shape{

	/**
	 * Calls super to initialize parameters
	 * @param x - x coordinate of Line
	 * @param y - y coordinate of Line
	 * @param color - Color of Line
	 * @param x2 - second x coordinate of Line
	 * @param y2 - second y coordinate of Line
	 * 
	 */
	public Line(double x, double y, Color color, double x2, double y2) {
		super(x, y, color, x2, y2);
	}

	/**
	 * Draws a 2D line using Graphics 2D
	 * @param g2 - draws Line2D
	 */
	@Override
	public void draw(Graphics2D g2) {
		g2.draw(new Line2D.Double(getX(), getY(), getArg3(), getArg4()));
	}
}