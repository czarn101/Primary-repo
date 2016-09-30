/**
 * UDP Chat Server Program
 * Act as a server for chat between two clients
 * Listens on a UDP port until two clients make a connection
 * Receives a line of input from the first client and sends it to second client and vice versa
 * Ends the chat when either one of clients says "Goodbye"
 * Partner: Carlos Leyva
 * @author: Hein Thu
 * @version: 1.0
 */

import java.io.*;
import java.net.*;

class ChatServer {
    public static void main(String args[]) throws Exception {

        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(9876);
        } catch (Exception e) {
            System.out.println("Failed to open UDP socket");
            System.exit(0);
        }
		
		
		//Declaring all the variables to be used in the program and initiating them with default values
        byte[] receiveData = null, sendData = null;
        DatagramPacket receivePacket = null, sendPacket = null;
        InetAddress firstIPAddress = null, secondIPAddress = null, incomingIPAddress= null;
        int firstPort = 0, secondPort = 0, incomingPort= 0;
        String sentence = "";
        boolean isFirst = false;

		//STAGE 1: GETTING THE FIRST CLIENT
        while (!isFirst) {//will loop until first client said "HELLO Red/Blue"
            receiveData = new byte[1024];
            sendData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            sentence = new String(receivePacket.getData());
            firstIPAddress = receivePacket.getAddress();
            firstPort = receivePacket.getPort();
			
            if ((sentence.trim().equals("HELLO Red"))||(sentence.trim().equals("HELLO Blue"))){
                sentence = "100";
                isFirst=true;
            }
            else// gives out error message if anything other than "HELLO Red/Blue" is received
                sentence="300 Command not recognized";

            sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
            serverSocket.send(sendPacket);
        }

		//STAGE 2: GETTING THE SECOND CLIENT
        while (isFirst) {//will loop until second client make contact with "HELLO Red/Blue"
            receiveData = new byte[1024];
            sendData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            sentence = new String(receivePacket.getData());
            secondIPAddress = receivePacket.getAddress();
            secondPort = receivePacket.getPort();

            if ((sentence.trim().equals("HELLO Red"))||(sentence.trim().equals("HELLO Blue"))){
                sentence = "200";
                isFirst=false;
            }
            else// gives out error message if anything other than "HELLO Red/Blue" is received
                sentence="300 Command not recognized";
				
            sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, secondIPAddress, secondPort);
            serverSocket.send(sendPacket);//sending "200" or an error message to second client
        }
        isFirst=true;
        sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
        serverSocket.send(sendPacket);//sending "200" to first client

		//STAGE 3: STARTING CHAT BETWEEN TWO CLIENTS
        while(true){
          receiveData = new byte[1024];
          sendData = new byte[1024];
          receivePacket = new DatagramPacket(receiveData, receiveData.length);
          serverSocket.receive(receivePacket);
          incomingIPAddress=receivePacket.getAddress();
		  incomingPort= receivePacket.getPort();
          sentence = new String(receivePacket.getData());
          sendData = sentence.getBytes();

          if(sentence.trim().equals("Goodbye")){//will end the loop if the sentence is "Goodbye"
            sentence="Goodbye";
            sendPacket = new DatagramPacket(sendData, sendData.length, secondIPAddress, secondPort);
            serverSocket.send(sendPacket);
            sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
            serverSocket.send(sendPacket);
            break;
          }
          else if(incomingIPAddress.equals(firstIPAddress)&& incomingPort==firstPort){//using the incoming IP address and port to know which client to direct the message to 
            sendPacket = new DatagramPacket(sendData, sendData.length, secondIPAddress, secondPort);
          }
          else{
            sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
          }
          serverSocket.send(sendPacket);
        }

        serverSocket.close();
    }
}
