/**
 * UDP Chat Server Program
 * Act as a intermediate portal for chat between two clients
 * Listens on a UDP port until two cients make a connection
 * Receives a line of input from the first client and sends it to second client and vice versa
 * Ends connection when either one of client says "Goodbye"
 * Partner: Carlos Leyva
 * @author: Hein Thu
 * @version: 1.1
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

        byte[] receiveData = null;
        byte[] sendData = null;
        DatagramPacket receivePacket = null;
        DatagramPacket sendPacket = null;
        InetAddress firstIPAddress = null;
        InetAddress secondIPAddress = null;
        int firstPort = 0;
        int secondPort = 0;
        String sentence = "";
        boolean isFirst = false;


        while (!isFirst) {
            receiveData = new byte[1024];
            sendData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            sentence = new String(receivePacket.getData());

            firstIPAddress = receivePacket.getAddress();
            firstPort = receivePacket.getPort();

            if ((sentence.length() >= 9 && sentence.substring(0,9).equals("HELLO Red"))||(sentence.length() >= 10 && sentence.substring(0, 10).equals("HELLO Blue"))){
                sentence = "100";
                isFirst=true;
            }
            else
                sentence="300 Command not recognized";

            sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
            serverSocket.send(sendPacket);
        }


        while (isFirst) {
            receiveData = new byte[1024];
            sendData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            sentence = new String(receivePacket.getData());

            secondIPAddress = receivePacket.getAddress();
            secondPort = receivePacket.getPort();

            if ((sentence.length() >= 9 && sentence.substring(0,9).equals("HELLO Red"))||(sentence.length() >= 10 && sentence.substring(0, 10).equals("HELLO Blue"))){
                sentence = "200";
                isFirst=false;
            }
            else
                sentence="300 Command not recognized";

            sendData = sentence.getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, secondIPAddress, secondPort);
            serverSocket.send(sendPacket);
        }
        isFirst=true;
        sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
        serverSocket.send(sendPacket);


        while(true){
          receiveData = new byte[1024];
          sendData = new byte[1024];
          receivePacket = new DatagramPacket(receiveData, receiveData.length);
          serverSocket.receive(receivePacket);

          sentence = new String(receivePacket.getData());
          sendData = sentence.getBytes();

          if(sentence.length()>=7 && sentence.substring(0,7).equals("Goodbye")){
            sentence="Goodbye";
            sendPacket = new DatagramPacket(sendData, sendData.length, secondIPAddress, secondPort);
            serverSocket.send(sendPacket);
            sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
            serverSocket.send(sendPacket);
            break;
          }
          else if(isFirst){
            sendPacket = new DatagramPacket(sendData, sendData.length, secondIPAddress, secondPort);
            serverSocket.send(sendPacket);
            isFirst=false;
          }
          else{
            sendPacket = new DatagramPacket(sendData, sendData.length, firstIPAddress, firstPort);
            serverSocket.send(sendPacket);
            isFirst=true;
          }
        }

        serverSocket.close();
    }
}
