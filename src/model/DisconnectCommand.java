package model;

import controller.Server;

/**
 * This command is sent by a client that is disconnecting.
 * 
 * @author Karyn Vo and Jennifer Tran
 */
public class DisconnectCommand extends Command<Server>{
	private static final long serialVersionUID = -8557424886231888586L;
	private String clientName;
	
	/**
	 * Creates a disconnect command for the given client.
	 * 
	 * @param name - username of client to disconnect
	 */
	public DisconnectCommand(String name){
		clientName = name;
	}
	
	@Override
	public void execute(Server executeOn) {
		executeOn.disconnect(clientName);
	}

}
