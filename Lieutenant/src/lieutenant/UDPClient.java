/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lieutenant;

/**
 *
 * @author Andrew
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UDPClient 
{
    private InetAddress inetAddress = null;
    private int Port;
    private DatagramSocket Socket   = null;            
    
    
    public void main(String[] ARGS)
    {
        Scanner scan = new Scanner(System.in);
        
        String Address;
        int Port;
        String MSG ="";
        
        System.out.print("What the address? ");
        Address=scan.next();
        
        System.out.println("What the port? ");
        Port=scan.nextInt();
        
        UDPClient Client = null;
        try
        {Client = new UDPClient(Address, Port);}
        catch (UnknownHostException ex)
        {
            System.out.println("Failed to host exception.");
            System.exit(0);
        }
        
        if(Client.Connect())
        {
            System.out.println("Send Message to host. (ends when you have a line just with'.'");
            String line = "";
            while(!line.equals("."))
            {
                line = line.equals("") ? "" : line + "\n";
                MSG += line;
                line += scan.next();
            }
            
            try 
            {
                Client.send(MSG);
                System.out.println(Client.recieve());
            } 
            catch (IOException ex) 
            {Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);}
            
            Client.Disconnect();
        }
     }
    
    
    
    public UDPClient(String Address, int Port) throws UnknownHostException
    {
        this.Port=Port;
        inetAddress = InetAddress.getByName(Address);
    }
    
    
    public UDPClient(DatagramSocket Socket, DatagramPacket packet)
    {
        this.Port=packet.getPort();
        inetAddress = packet.getAddress();
        this.Socket=Socket;
    }
    
    
    public boolean Connect()
    {
        DatagramPacket packet;
        byte[] buf = new byte[512];
       
        if(Socket != null && Socket.getLocalSocketAddress()!=null)
        {return true;}
        
        try { 
            Socket = new DatagramSocket();
            Socket.setSoTimeout(180000);
            
            packet = new DatagramPacket(buf, buf.length, 
                                inetAddress, Port);
            
            Socket.send(packet);
            
            packet = new DatagramPacket(buf, buf.length);
            Socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            
            if(!received.equals("Connected"))
            {
                Socket.close();
                return false;
            }
            
            Port = packet.getPort();

        } catch (java.net.SocketTimeoutException | SocketException | UnknownHostException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            Socket.close();
            return false;
        } catch (IOException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            Socket.close();
            return false;
        }
        
        return true;
    }
    
    public void Disconnect()
    {
        Socket.close();
        Socket=null;
    }
    

    
    public void send(String MSG) throws IOException
    {
        DatagramPacket packet;
        
        byte[] dataMSG = MSG.getBytes();
        int start = 0;
        
        do
        {
            byte[] buf = new byte[512];
            
            for(int i = 0; i < buf.length; i++)
            {
                if(start >= dataMSG.length)
                    break;
                
                
                buf[i] = dataMSG[start];
                start++;                 
            }
            
            packet = new DatagramPacket(buf, buf.length, 
                                inetAddress, Port);
            Socket.send(packet);
            
        } while(start < dataMSG.length);
        
        byte[] buf = new byte[512];
        packet = new DatagramPacket(buf, buf.length, 
                                inetAddress, Port);
        Socket.send(packet);
    }
    
    public String recieve() throws IOException
    {
        DatagramPacket packet;
        
        String Result   = "";
        String received;
        
        byte[] buf = new byte[512];
        
        do 
        {       
            packet = new DatagramPacket(buf, buf.length);
            try {
                Socket.receive(packet);
                received = new String(packet.getData(), 0, packet.getLength());
            } 
            catch (java.net.SocketTimeoutException ex)
            {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
            
            Result += received;
        } while(!received.equals(""));
        
         return Result;
    }
}
