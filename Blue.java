/**
*	Blue Program
*	Connects to a Server that will pass messages from one client to another
*	Will take user input to initiate a chat between two clients
*	Two clients that are being used are red and blue client files
*	
*
*	@author: Carlos Leyva
*	Date: September 29, 2016
@	version: 2.1
*/

import java.io.*;
import java.net.*;

class Blue {

  public static void main(String args[]) throws Exception{
		//instatiation of all variables that will be utilized such as the byte arrays for the message as well as the sockets for the client to port.
		
		BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IP = InetAddress.getByName("10.49.139.192");
		//IP Address of the computers we used. Will need to change we running on a different computer.
	
		byte[] sendData = null; 
		byte[] receiveData = null; 
		boolean isActive = true; 
		boolean blueTurn = false; 
		String modifiedSentence = ""; 
		String message = "HELLO Blue";
		//String message required to initiate the "100" and "200" messages
		
		DatagramPacket sendPacket = null; 
		DatagramPacket receivePacket = null; 
 
		int port = 9876; 
		//port number is different that of Red because only one client can occupy one socket.


		//Try-Catch connection from client to server socket
		try{ 
			clientSocket = new DatagramSocket(port); 
		} 
		catch(Exception e){ 
			System.out.println("Could not open receive port!"); 
			System.exit(0); 
		} 


    while(isActive){
		
		sendData = new byte[1024];
		receiveData = new byte[1024];
        //1024 byte arrays for messages between Red and Blue
		
		String sentence = fromUser.readLine();
        sendData = sentence.getBytes();
        //new send packet
		sendPacket = new DatagramPacket(sendData, sendData.length, IP, 9876);// packet that will be sent to the other client
		clientSocket.send(sendPacket);
		//new receive packet
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);

        modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);


    if(modifiedSentence.trim().equals("100")){
		//If the blue client is the first to input HELLO "name", then client will need to wait until there is a "200" client to begin chat
        while(true){ //the loop continues as long as the conversation keeps going
			DatagramPacket receivePacketServer = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacketServer);
            modifiedSentence = new String(receivePacketServer.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
			//will send message to server and then server will return "100" message to show that Blue has been labelled 100
			
			if (modifiedSentence.trim().equals("200"))
			{
				break;
			}
			//If for some reason Blue happens to be the 200 client without a 100, then program will break.
		}	

            //begin chat if blue is first
	}
		else if (modifiedSentence.trim().equals("200"))
		{
			DatagramPacket receivePacketServer = new DatagramPacket(receiveData, receiveData.length); //Receives data from the other client
            clientSocket.receive(receivePacketServer);
            modifiedSentence = new String(receivePacketServer.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
			
		}
		else if (modifiedSentence.length() >= 7 && modifiedSentence.substring(0,7).equals("Goodbye")) 
		{
			//When the conversation is finished, one client will input "Goodbye" and will quit the program and then exit the socket.
				break;
			
		}
        


    }

    clientSocket.close();
  }
}