import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 *  TCP/UDP Client class
 */
public class Client {
  
  // Constructor containing GUI code
  public Client()
  {
    JFrame frame = new JFrame();
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("TCP/UDP Client ");
    frame.setSize(300,250);
    frame.setLocationRelativeTo(null);
    
    JPanel panel1 = new JPanel();
    panel1.add(new JLabel("Server Address:"));
    JTextField addressField = new JTextField(15);
    addressField.setText("localhost");
    panel1.add(addressField);
    
    JPanel panel2 = new JPanel();
    panel2.add(new JLabel("Message:")); 
    JTextArea messageField = new JTextArea(4,23);
    JScrollPane scrollMessageField = new JScrollPane(messageField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    panel2.add(scrollMessageField);
    String[] pTypes = {"TCP", "UDP"};
    JComboBox<String> protocol = new JComboBox<String>(pTypes);
    panel2.add(protocol);
    
    JButton sendButton = new JButton( "Send Message to Server");
    sendButton.addActionListener(new ActionListener()
                                   {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String address = addressField.getText();
        String message = messageField.getText();
        String response = "";
        if (protocol.getSelectedItem().equals("TCP")) {
          response = sendTCP(address, message);
        } else if (protocol.getSelectedItem().equals("UDP")) {
          response = sendUDP(address, message);
        }
        JOptionPane.showMessageDialog(frame, response);
      }
    });
    
    frame.add(panel1, BorderLayout.NORTH);
    frame.add(panel2, BorderLayout.CENTER);
    frame.add(sendButton,BorderLayout.SOUTH);
    frame.setVisible(true);
  }
  
  // Method to send via TCP and receive server response
  private static String sendTCP(String address, String message) {
    String response = "";
    try {
      Socket socket = new Socket(address, 4000);
      DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToServer.writeBytes(message + '\n');
      String serverIP = socket.getInetAddress().getHostAddress();
      response = "From server ("+serverIP+") via TCP: "+inFromServer.readLine();
      socket.close();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return response;
  }
  
  // Method to send via UDP and receive server response
  private static String sendUDP(String address, String message) {
    String response = "";
    try {
      DatagramSocket socket = new DatagramSocket();
      byte[] sendData;
      byte[] receiveData = new byte[1024];
      String sentence = message;
      sendData = sentence.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(address), 4100);
      socket.send(sendPacket);
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      socket.receive(receivePacket);
      String serverIP = InetAddress.getByName(address).getHostAddress();
      response = "From server ("+serverIP+") via UDP: "+(new String(receivePacket.getData(), 0, receivePacket.getLength()));
      socket.close();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return response;
  }
  
  // main executable method
  public static void main(String[] args) {
    new Client();
  }
  
}