package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  String loginKey = "loginID";
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	String msgStr = (String) msg;
	String sendMsg;
	System.out.println("Message received: " + msg + " from " + client.getInfo(loginKey) + ".");

	if ((msgStr).startsWith("#login")){
		if (client.getInfo(loginKey) == null) {
			String loginIDStr = msgStr.replace("#login ", ""); 
			client.setInfo(loginKey, loginIDStr);
			sendMsg = (client.getInfo(loginKey) + " has logged on.");
			System.out.println(sendMsg);
			this.sendToAllClients(sendMsg);
		} else {
			try {
				client.close();
			} catch (IOException e) {}
		}
	} else {
    
	sendMsg = client.getInfo(loginKey) + "> " + msgStr;
		
    this.sendToAllClients(sendMsg);
	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client has connected to the server.");
  }
  	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
  		System.out.println(client.getInfo(loginKey) + " has disconnected.");
  		super.clientDisconnected(client);
	}
  	@Override
  	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
  		System.out.println(client.getInfo(loginKey) + " has disconnected.");
  	}
  
  	
  
  //Class methods ***************************************************
  
}
//End of EchoServer class
