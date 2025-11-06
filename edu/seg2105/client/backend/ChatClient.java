// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, String loginID, ChatIF clientUI) 
    
		  
	throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    
    if (loginID != null) {
    	openConnection();
    	sendToServer(("#login " + loginID));
    } else {
    	clientUI.display("ERROR - No login ID specified.  Connection aborted. ");
    	System.exit(0); //Originally used quit() but it would give double disconnect messages
    }
    
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#")) {
    		handleCommand(message);
    	} else {
    		sendToServer(message);
    	}
      
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  public void handleCommand(String command) {
	  if(command.equals("#quit")) {
		  quit();
	  } 
	  
	  else if (command.equals("#logoff")) {
		  try
		    {
		      closeConnection();
		    }
		    catch(IOException e) {
		    	clientUI.display("Error: Could not log off!");
		    }
		  
	  } else if (command.startsWith("#sethost")) {
		  if (!isConnected()) {
			  try {
				  	setHost((command.replace("#sethost ", "")));
				 } catch (Exception e) 
				 	{
					 clientUI.display("Error: Invalid Hostname!");
				    }
		  } else {
			  clientUI.display("Error: You must not already be connected to a server!");
		  }
		  
	  } else if (command.startsWith("#setport")) {
		  if (!isConnected()) {
			  try {
				  	setPort((Integer.parseInt(command.replace("#setport ", ""))));
				 } catch (Exception e) 
				 	{
					 clientUI.display("Error: Invalid Portname!");
				    }
		  } else {
			  clientUI.display("Error: You must not already be connected to a server!");
		  }
		  
		  
	  } else if (command.equals("#login")) {
		  if (!isConnected()) {
			  try {
				  	openConnection();
				 } catch (Exception e) 
				 	{
					 clientUI.display("Error: Invalid Hostname!");
				    }
		  } else {
			  clientUI.display("Error: You must not already be connected to a server!");
		  }
		  
		  
	  } else if (command.equals("#gethost")) {
		  if (isConnected()) {
			  getHost();
		  } else {
			  clientUI.display("Error: Not connected to a server!");
		  }
		  
	  } else if (command.equals("#getport")) {
		  if (isConnected()) {
			  getPort();
		  } else {
			  clientUI.display("Error: Not connected to a server!");
		  }
	  } else {
		 try {
			sendToServer(command);
		 } catch (IOException e) 
		 	{
		      clientUI.display
		        ("Could not send false command message to server.  Terminating client.");
		      quit();
		    }
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
	/**
	 * Implements the hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  	@Override
	protected void connectionException(Exception exception) {
  		clientUI.display("The server has shut down");
  		quit();
	}
  	
  	/**
	 * Implements the hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  	@Override
	protected void connectionClosed() {
  		clientUI.display("Connection closed");
  		
	}
  
}
//End of ChatClient class
