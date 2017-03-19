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
import java.util.logging.Level;
import java.util.logging.Logger;
public class UDPClient 
{
    private InetAddress inetAddress = null;
    private int Port;
    private DatagramSocket Socket   = null;            
    
    
    public UDPClient(String Address, int Port) throws UnknownHostException
    {
        this.Port=Port;
        inetAddress = InetAddress.getByName(Address);
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
            
            if(received.equals("Connected"))
            {
                Socket.close();
                return false;
            }

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
    
    public String lissen(DatagramSocket socket) throws IOException
    {
        DatagramPacket packet;
        
        String Result   = "";
        String received = "";
        
        byte[] buf = new byte[512];
        
        do 
        {       
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
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
