package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;
import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;



public class ServerConsole implements ChatIF{
	Scanner fromServerConsole;
	EchoServer sv;
	
	public ServerConsole(int port) {
			sv = new EchoServer(port);
		    
		    try 
		    {
		      sv.listen(); //Start listening for connections
		    } 
		    catch (Exception ex) 
		    {
		      System.out.println("ERROR - Could not listen for clients!");
		    }
		
	}
	
	@Override
	public void display(String message) {
		if(message.startsWith("#")) { //call if command is prompted, but don't send command to clients because that would be stupid
			handleServerCommands(message);
		} else { //If it's not a command then it is a message for all clients
		System.out.println("SERVER MESSAGE> "  + message);
		sv.sendToAllClients("SERVER MESSAGE> "  + message);
		}
	}
	
	public void handleServerCommands(String command) {
		 if(command.equals("#quit")) {
			 System.exit(0); //I hope this is graceful enough
		  } 
		  
		  else if (command.equals("#stop")) {
			  sv.stopListening();
		  } else if (command.equals("#close")) {
			  try {
				sv.stopListening();
				sv.close();
			} catch (IOException e) {}
			  
		  } else if (command.startsWith("#setport")) {
			  if ((!sv.isListening())&&(sv.getNumberOfClients() == 0)) {
				  try {
					  	sv.setPort((Integer.parseInt(command.replace("#setport ", ""))));
					 } catch (Exception e) 
					 	{
						 System.out.println("Server Error: Invalid Portname!");
					    }
			  }
		  } else if (command.equals("#start")) {
			  if(!sv.isListening()) {
				  try {
					sv.listen();
				  } catch (IOException e) {}
			  }
		  } else if (command.equals("#getport")) {
			  sv.getPort();
		  }
		  
		  else {
			  System.out.println("Server Error: "  + command + " is not a valid server command.");
		  }
	}
	
	//unused
	public EchoServer getsv() {
		return sv;
	}
	
	//mirrors the client console accept
	public void accept() 
	  {
	    try
	    {
	      fromServerConsole = new Scanner(System.in); 
	      String message;

	      while (true) 
	      {
	        message = fromServerConsole.nextLine();
	        display(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	
	
	  public static void main(String[] args) 
	  {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = EchoServer.DEFAULT_PORT; //Set port to 5555
	    }
	    //create a ServerConsole server
		ServerConsole srvr = new ServerConsole(port);
	   //start listening to a server client console
		srvr.accept();
	  }
}