package controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import javax.swing.JOptionPane;

import model.Command;
import model.DisconnectCommand;
import model.Shape;
import view.NetpaintGUI;

/**
 * The client side of Netpaint. This class displays the current canvas and
 * sends AddObjectCommands to the server.
 * 
 * @author Karyn Vo and Jennifer Tran
 */
public class Client{
	private String clientName; // user name of the client
	private NetpaintGUI netpaintFrame;

	private Socket server; // connection to server
	private ObjectOutputStream out; // output stream
	private ObjectInputStream in; // input stream

	/**
	 * This class reads and executes commands sent from the server.
	 * 
	 * @author Karyn Vo and Jennifer Tran
	 */
	private class ServerHandler implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					Command<Client> c = (Command<Client>) in.readObject();
					c.execute(Client.this);
				}
			} catch (SocketException e) {
				return;
			} catch (EOFException e) {
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Class constructor that prompts the client for the host address, port, and user name.
	 * A connection to the server, a thread to handle the server, and a netpaint canvas are opened.
	 */
	public Client() {
		String host = JOptionPane.showInputDialog("Host address: ");
		String port = JOptionPane.showInputDialog("Host port: ");
		clientName = JOptionPane.showInputDialog("User name: ");
		
		if(host == null || port == null || clientName == null)
			return;
		
		try {
			// Open a connection to the server
			server = new Socket(host, Integer.parseInt(port));
			in = new ObjectInputStream(server.getInputStream());
			out = new ObjectOutputStream(server.getOutputStream());

			// write out the name of this client
			out.writeObject(clientName);
			
			netpaintFrame = new NetpaintGUI(out);
			
			// add a listener that sends a disconnect command to when closing
			netpaintFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent arg0) {
					try {
						out.writeObject(new DisconnectCommand(clientName));
						out.close();
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			// start a thread for handling server events
			new Thread(new ServerHandler()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new Client.
	 * @param args - parameter for main method
	 */
	public static void main(String[] args){
		new Client();
	}

	/**
	 * Updates the Netpaint canvas with the updated list of shapes.
	 * 
	 * @param shapes	the list of shapes to display
	 */
	public void update(List<Shape> shapes) {
		netpaintFrame.update(shapes);
	}

}
