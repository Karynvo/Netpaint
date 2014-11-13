/**
 * Creates an Image object and extends Shape
 * @author Karyn Vo, Jennifer Tran
 */

package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image extends Shape{
	
	/**
	 * Calls super to initialize parameters
	 * @param x - x coordinate of Image
	 * @param y - y coordinate of Image
	 * @param color - Color of Image
	 * @param width - width of Image
	 * @param height -height of Image
	 * 
	 */
	public Image(double x, double y, Color color, double width, double height) {
		super(x, y, color, width, height);
	}

	/**
	 * Draws a buffered image using Graphics2D
	 * @param g2 - draws a buffered image
	 */
	@Override
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("doge.jpeg"));
			g2.drawImage(image, getX(), getY(), getArg3(),
					getArg4(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
