package model;

import controller.Server;

/**
 * Adds a shape to the server's list of shapes
 * 
 * @author Karyn Vo and Jennifer Tran
 */
public class AddObjectCommand extends Command<Server>{
	private static final long serialVersionUID = 8394654307009158284L;
	private Shape shape;
	
	/**
	 * Creates an AddObjectCommand with the given shape.
	 * 
	 * @param shape - shape to add to list
	 */
	public AddObjectCommand(Shape shape){
		this.shape = shape;
	}
	
	@Override
	public void execute(Server executeOn) {
		executeOn.addShape(shape);
	}

}
