package Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andrew
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utilities.Monitor;
import Utilities.UDPServer;
import Utilities.UDPClient;

public class EchoUDPServer 
{
    public static void main(String [] args)
    {
        Monitor <UDPClient> mon = new Monitor();
        UDPServer Server = new UDPServer(20000, mon);
        
        Thread thr = new Thread(Server);
        
        thr.start();
        
        UDPClient Client = null;
        try 
        {Client = mon.pop();}
        catch (InterruptedException ex) 
        {Logger.getLogger(EchoUDPServer.class.getName()).log(Level.SEVERE, null, ex);}
        
        System.out.println("Got Client");
        String MSG;
        try {
            MSG = Client.recieve();
            
            Client.send("RECEIVE: " + MSG);
        } catch (IOException ex) {
            Logger.getLogger(EchoUDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Client.Disconnect();
        
        if(thr.isAlive())
        {
            Server.stop();
            try 
            {thr.join();}
            catch (InterruptedException ex) 
            {Logger.getLogger(EchoUDPServer.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }
}
