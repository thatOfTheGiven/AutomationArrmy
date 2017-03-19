/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrew
 */
public class UDPServer implements Runnable
{
    private final int Port;
    private final Monitor <UDPClient> Mon;
    private boolean end;
    
    public UDPServer(int Port, Monitor <UDPClient> Mon)  
    {
        this.Port=Port;
        this.Mon=Mon;
       
    }    
    
    @Override
    public void run() 
    {
        end = false;
        try 
        {
            DatagramSocket Socket = new DatagramSocket(Port);
            
            while(true)
            {
                byte[] buf = new byte[512];
            
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                Socket.receive(packet);
                
                DatagramSocket ShiftSocket = new DatagramSocket();
                UDPClient Client = new UDPClient(ShiftSocket, packet.getPort(), packet.getAddress());
                
                if(end)
                {
                    Client.send("Breakingdown");
                    break;
                }
                
                Client.send("Connected");
               
                Mon.push(Client);
                
            }
        } 
        catch (SocketException ex) 
        {Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);} 
        catch (IOException ex) 
        {Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);}    
    } 
    
    public void stop()
    {
        end=true;
        try 
        {
            UDPClient Client = new UDPClient("127.0.0.1", Port);
            Client.Connect();          
        } 
        catch (UnknownHostException ex) 
        {Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);}
    }
}
