package model;

import java.util.LinkedList;
import java.util.List;

import controller.Client;

/**
 * Updates a client with the current list of shapes.
 * 
 * @author Karyn Vo and Jennifer Tran
 */
public class UpdateClientCommand extends Command<Client>{
	private static final long serialVersionUID = 4222014184904080846L;
	private List<Shape> shapes;

	/**
	 * Creates a new UpdateClientCommand with the given list of shapes.
	 * 
	 * @param shapes - the list of shapes
	 */
	public UpdateClientCommand(List<Shape> shapes) {
		this.shapes = new LinkedList<Shape>(shapes);
	}

	@Override
	public void execute(Client executeOn) {
		executeOn.update(shapes);
	}

}
