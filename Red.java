/**
*	Red Program
*	Connects to a Server
*	Receives input from keyboard to send to server.
*	Receives a response from the server and displays it.
*
*	@author: Carlos Leyva
*	Date: September 29, 2016
@	version: 2.1
*/

import java.io.*;
import java.net.*;

class Red {

  public static void main(String args[]) throws Exception{
		//instatiation of all variables that will be utilized such as the byte arrays for the message as well as the sockets for the client to port.
  
		BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IP = InetAddress.getByName("10.49.139.192");
		//IP Address of the computers we used. Will need to change we running on a different computer.
	
		byte[] sendData = null; 
		byte[] receiveData = null; 
		boolean isActive = true; 
		boolean redTurn = false; 
		String modifiedSentence = ""; 
		String message = "HELLO Red";
		//String message required to initiate the "100" and "200" messages

		DatagramPacket sendPacket = null; 
		DatagramPacket receivePacket = null; 
 
		int port = 9875; 
		//port number is different that of Blue because only one client can occupy one socket.


	
		try{ 
			clientSocket = new DatagramSocket(port); 
		} 
		catch(Exception e){ 
			System.out.println("Could not open receive port!"); 
			System.exit(0); 
		} 
		//Try-Catch connection from client to server socket


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
		
		while(true)
        {
			//If the red client is the first to input HELLO "name", then client will need to wait until there is a "200" client to begin chat
			DatagramPacket receivePacketServer = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacketServer);
			modifiedSentence = new String(receivePacketServer.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
			//will send message to server and then server will return "100" message to show that Red has been labelled 100

			if (modifiedSentence.trim().equals("200"))
			{
				break;
			}
			//If for some reason Blue happens to be the 200 client without a 100, then program will break.
            //begin chat if red is first
		}
	}
		else if (modifiedSentence.trim().equals("200"))
		{
			DatagramPacket receivePacketServer = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacketServer);
			modifiedSentence = new String(receivePacketServer.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
		}
		else if (modifiedSentence.length() >= 7 && modifiedSentence.substring(0,7).equals("Goodbye"))
		{
				break;
			//When the conversation is finished, one client will input "Goodbye" and will quit the program and then exit the socket.
		}
        
	}


    clientSocket.close();
  }
}