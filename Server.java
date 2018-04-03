import java.io.*;
import java.net.*;

/*
 *  TCP/UDP Server class
 */
public class Server {
  
  // main (executable) method
  // launches TCP and UDP servers in separate Threads
  public static void main(String[] args) {
    TCPServerThread tcp = new TCPServerThread(4000);
    tcp.start();
    UDPServerThread udp = new UDPServerThread(4100);
    udp.start();
  }
  
}

/*
 *  TCP Server Thread class
 */
class TCPServerThread extends Thread {
  
  int port;
  
  // Constructor
  public TCPServerThread(int port) {
    this.port = port;
  }
  
  // TCP Server Thread execution
  public void run() {
    System.out.println("TCP server is up and running on port "+port+"...");
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while(true) 
      {
        Socket socket = serverSocket.accept();
        BufferedReader inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream outClient = new DataOutputStream(socket.getOutputStream());
        String message = inClient.readLine();
        String clientIP = socket.getInetAddress().getHostAddress();
        System.out.println("From client ("+clientIP+") via TCP: " + message);
        outClient.writeBytes(message + '\n');
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}

/*
 *  UDP Server Thread class
 */
class UDPServerThread extends Thread {
  
  int port;
  
  // Constructor
  public UDPServerThread(int port) {
    this.port = port;
  }
  
  // UDP Server Thread execution
  public void run() {
    System.out.println("UDP server is up and running on port "+port+"...");
    try {
      DatagramSocket serverSocket = new DatagramSocket(port);
      byte[] receiveData = new byte[1024];
      byte[] sendData;
      while(true)
      {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        String message = new String(receivePacket.getData(),0, receivePacket.getLength());
        String clientIP = receivePacket.getAddress().getHostAddress();
        System.out.println("From client ("+clientIP+") via UDP: " + message);
        sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
        serverSocket.send(sendPacket);
      }
      
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    
  }
  
}