package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Command;
import model.DisconnectCommand;
import model.Shape;
import model.UpdateClientCommand;

/**
 * This class is the server side of Netpaint. The server communicates with clients.
 * Sends and receives commands, and holds the master list of shapes on the canvas.
 * 
 * @author Karyn Vo and Jennifer Tran
 *
 */
public class Server {
	private ServerSocket socket;

	private List<Shape> shapes;
	private HashMap<String, ObjectOutputStream> outputs;

	/**
	 * This thread reads and executes commands sent by a client.
	 * 
	 * @author Karyn Vo and Jennifer Tran
	 */
	private class ClientHandler implements Runnable {
		private ObjectInputStream input;

		/**
		 * Class constructor that initializes te client's ObjectInputStream.
		 * @param input	the client's ObjectInputStream
		 */
		public ClientHandler(ObjectInputStream input) {
			this.input = input;
		}

		@Override
		public void run() {
			try {
				while (true) {
					Command<Server> command = (Command<Server>) input
							.readObject();
					command.execute(Server.this);

					// terminate if client is disconnecting
					if (command instanceof DisconnectCommand) {
						input.close();
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This thread listens for and sets up connections to new clients.
	 *
	 * @author Karyn Vo and Jennifer Tran
	 */
	private class ClientAccepter implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					//accept a new client, get output & input streams
					Socket s = socket.accept();
					ObjectOutputStream output = new ObjectOutputStream(
							s.getOutputStream());
					ObjectInputStream input = new ObjectInputStream(
							s.getInputStream());

					//read client's name
					String clientName = (String) input.readObject();

					//map client name to output stream
					outputs.put(clientName, output);

					new Thread(new ClientHandler(input)).start();

					updateClients();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Class constructor that creates the master list of shapes and HashMap of clients and their output streams.
	 * The server socket is created and a thread to accept the client starts.
	 */
	public Server() {
		this.shapes = new ArrayList<Shape>();
		this.outputs = new HashMap<String, ObjectOutputStream>();

		try {
			socket = new ServerSocket(9001);
//			System.out.println("Server started on 9001");

			new Thread(new ClientAccepter()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a shape to the master list of shapes. Called by an AddObjectCommand.
	 * 
	 * @param shape	shape to add
	 */
	public void addShape(Shape shape) {
		shapes.add(shape);
		updateClients();
	}

	/**
	 * Writes an UpdateClientCommand to every connected user.
	 */
	public void updateClients() {
		UpdateClientCommand update = new UpdateClientCommand(shapes);
		try {
			for (ObjectOutputStream out : outputs.values())
				out.writeObject(update);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new Server.
	 * @param args - parameter for main method
	 */
	public static void main(String[] args) {
		new Server();
	}

	/**
	 * Disconnects a given user from the server gracefully.
	 * 
	 * @param clientName	user to disconnect
	 */
	public void disconnect(String clientName) {
		try {
			outputs.get(clientName).close();
			outputs.remove(clientName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
