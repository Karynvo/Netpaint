/**
 * This class implements the client side GUI that allows a user to draw and display drawn images.
 * @author Karyn Vo, Jennifer Tran.
 */

package view;

import model.AddObjectCommand;
import model.Image;
import model.Rectangle;
import model.Shape;
import model.Oval;
import model.Line;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import controller.Client;

public class NetpaintGUI extends JFrame {
	private static final long serialVersionUID = 7686336736079994065L;

	private Canvas canvasPanel;
	private Color currentColor;
	private String currentShapeString = "";
	private List<Shape> listOfShapes;

	private static ObjectOutputStream output;
	
	/**
	 * Class constructor that lays out the GUI.
	 * @param output - connects output stream to GUI
	 */
	public NetpaintGUI(ObjectOutputStream output) {
		this.output = output;
		
		this.setTitle("Netpaint Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set box layout
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		// Create canvas
		canvasPanel = new Canvas();
		canvasPanel.setPreferredSize(new Dimension(500, 1000));
		container.add(new JScrollPane(canvasPanel));

		// Create radio buttons
		container.add(createButtonPanel());

		// Create and set up the content pane.
		JComponent newContentPane = new ColorChooser();
		newContentPane.setOpaque(true); // content panes must be opaque

		// add stuff to JFrame
		this.setContentPane(newContentPane);
		this.add(container);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * This class represents the drawing canvas for the GUI.
	 * @author Karyn Vo, Jennifer Tran.
	 */
	private class Canvas extends JPanel {

		private int oldX, oldY, newX, newY;
		private boolean isDrawing;

		/**
		 * Class constructor that adds MouseListener and initializes private instance variables.
		 */
		public Canvas() {
			// The Panel listens to itself
			isDrawing = false;
			MouseListener listener = new ListenToMouse();
			MouseMotionListener motionListener = new ListenToMouse();

			this.addMouseMotionListener(motionListener);
			this.addMouseListener(listener);
			this.setBackground(Color.WHITE);

			listOfShapes = new ArrayList<Shape>();

		}

		/**
		 * This class represents the listener for the mouse.
		 * @author Karyn Vo, Jennifer Tran
		 */
		private class ListenToMouse implements MouseMotionListener,
				MouseListener {

			/**
			 * Unused method.
			 * @param evt
			 */
			public void mouseClicked(MouseEvent evt) {
			}

			/**
			 * Captures the x and y coordinates of the mouse when it moves.
			 * Repaints the shape for every mouse movement.
			 * @param evt - passed in when mouseMoved triggered
			 */
			public void mouseMoved(MouseEvent evt) {
				if (isDrawing) {
					newX = evt.getX();
					newY = evt.getY();
					repaint();
				}
			}

			/**
			 * If it is the first mousePressed event out of two clicks, a shape is drawn.
			 * Else, the old x and y coordinates are stored.
			 * @param evt - passed in when mousePressed is triggered
			 */
			public void mousePressed(MouseEvent evt) {
				if(!currentShapeString.equals(""))
					isDrawing = !isDrawing;
				if (isDrawing == true) {
					oldX = evt.getX();
					oldY = evt.getY();
				}
				if (isDrawing == false && !currentShapeString.equals("")) {
					Shape s = null;
					if(currentShapeString.equals("rectangle")){
						s = new Rectangle(Math.min(oldX, newX), Math
								.min(oldY, newY), currentColor, Math.abs(newX
								- oldX), Math.abs(newY - oldY));
					}
					else if(currentShapeString.equals("oval")){
						s = new Oval(Math.min(oldX, newX), Math.min(oldY,
								newY), currentColor, Math.abs(newX - oldX), Math
								.abs(newY - oldY));
					}
					else if(currentShapeString.equals("line")){
						s = new Line(oldX, oldY, currentColor, newX, newY);
					}
					else if(currentShapeString.equals("image")){
						s = new Image(oldX, oldY, currentColor, newX
								- oldX, newY - oldY);
					}
					listOfShapes.add(s);
					try{
						output.writeObject(new AddObjectCommand(s));
					}catch(Exception e){
						e.printStackTrace();
					}
					repaint();
				}
			}

			/**
			 * Unused method.
			 * @param evt
			 */
			public void mouseEntered(MouseEvent evt) {
			}

			/**
			 * Unused method.
			 * @param evt
			 */
			public void mouseReleased(MouseEvent evt) {
			}

			/**
			 * Unused method.
			 * @param evt
			 */
			public void mouseExited(MouseEvent evt) {
			}

			/**
			 * Unused method.
			 * @param evt
			 */
			public void mouseDragged(MouseEvent evt) {
			}
		} // end mouse listener

		/**
		 * Paints the selected shape and color onto the canvas.
		 * @param g - used to draw shapes
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			for (Shape s : listOfShapes) {
				g2.setColor(s.getColor());
				s.draw(g2);
			}
			if(isDrawing && !currentShapeString.equals("")){
				g2.setColor(currentColor);
				if (currentShapeString.equals("rectangle")) {
					Rectangle2D.Double rectangle = new Rectangle2D.Double(Math.min(
							oldX, newX), Math.min(oldY, newY),
							Math.abs(newX - oldX), Math.abs(newY - oldY));
					g2.fill(rectangle);
				} else if (currentShapeString.equals("oval")) {
					Ellipse2D.Double oval = new Ellipse2D.Double(Math.min(oldX,
							newX), Math.min(oldY, newY), Math.abs(newX - oldX),
							Math.abs(newY - oldY));
					g2.fill(oval);
				} else if (currentShapeString.equals("line")) {
					Line2D.Double line = new Line2D.Double(oldX, oldY, newX, newY);
					g2.draw(line);
				} else if (currentShapeString.equals("image")) {
					BufferedImage image = null;
					try {
						image = ImageIO.read(new File("doge.jpeg"));
						g2.drawImage(image, oldX, oldY, newX - oldX, newY - oldY,
								null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Creates the radio button panel to select a shape to draw.
	 * @return {@link JPanel} that contains the radio buttons
	 */
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JRadioButton rectangleButton = new JRadioButton("Rectangle");
		JRadioButton ovalButton = new JRadioButton("Oval");
		JRadioButton lineButton = new JRadioButton("Line");
		JRadioButton imageButton = new JRadioButton("Image");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rectangleButton);
		buttonGroup.add(ovalButton);
		buttonGroup.add(lineButton);
		buttonGroup.add(imageButton);

		buttonPanel.add(rectangleButton);
		buttonPanel.add(ovalButton);
		buttonPanel.add(lineButton);
		buttonPanel.add(imageButton);

		rectangleButton.addActionListener(new RadioButtonListener());
		ovalButton.addActionListener(new RadioButtonListener());
		lineButton.addActionListener(new RadioButtonListener());
		imageButton.addActionListener(new RadioButtonListener());

		return buttonPanel;
	}

	/**
	 * This class implements an ActionListener for a radio button.
	 * @author Karyn Vo, Jennifer Tran
	 */
	private class RadioButtonListener implements ActionListener {

		/**
		 * Gets the text of the radio button to determine the shape selected to draw.
		 * @param arg0 - passed in when actionPerformed triggered
		 */
		public void actionPerformed(ActionEvent arg0) {
			JRadioButton button = (JRadioButton) arg0.getSource();

			if (button.getText().equals("Rectangle"))
				currentShapeString = "rectangle";
			else if (button.getText().equals("Oval"))
				currentShapeString = "oval";
			else if (button.getText().equals("Line"))
				currentShapeString = "line";
			else if (button.getText().equals("Image"))
				currentShapeString = "image";
		}
	}

	/**
	 * This class implements ChangeListener and extends JPanel to create a JColorChooser panel.
	 * @author Karyn Vo, Jennifer Tran
	 */
	private class ColorChooser extends JPanel implements ChangeListener {

		private JColorChooser tcc;

		/**
		 * Class constructor that lays out JColorChooser in a JPanel. Adds ChangeListener
		 * to JPanel
		 */
		public ColorChooser() {
			super(new BorderLayout());
			tcc = new JColorChooser();
			tcc.getSelectionModel().addChangeListener(this);
			tcc.setBorder(BorderFactory
					.createTitledBorder("Choose Shape Color"));

			add(tcc, BorderLayout.PAGE_END);
		}

		/**
		 * Sets the current color selected.
		 * @param arg0 - passed in when stateChanged is triggered
		 */
		public void stateChanged(ChangeEvent arg0) {
			currentColor = tcc.getColor();
		}
	}

	/**
	 * Creates a new NetpaintGUI.
	 * @param args - parameter for main method
	 */
	public static void main(String[] args) {
		new NetpaintGUI(output);
	}

	
	/**
	 * Updates the Netpaint canvas with the updated list of shapes and repaints.
	 * 
	 * @param shapes	the list of shapes to display
	 */
	public void update(List<Shape> shapes) {
		listOfShapes = shapes;
		repaint();
	}
}
