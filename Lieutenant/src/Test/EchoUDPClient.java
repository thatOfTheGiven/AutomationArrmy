/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utilities.UDPClient;

/**
 *
 * @author Andrew
 */
public class EchoUDPClient 
{
    public static void main(String[] ARGS)
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
            ex.printStackTrace();
            System.out.println("Failed to host exception.");
            System.exit(0);
        }
        scan.useDelimiter(System.getProperty("line.separator"));
        if(Client.Connect())
        {
            System.out.println("Send Message to host. (ends when you have a line just with '.')");
            String line = "";
            while(!line.equals("."))
            {
                line = line.equals("") ? "" : line + "\n";
                MSG += line;
                line = scan.next();
            }
            
            try 
            {
                Client.send(MSG);
                System.out.println(Client.recieve());
            } 
            catch (IOException ex) 
            {
                ex.printStackTrace();
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);}
            
            Client.Disconnect();
        }
        else
        {System.out.println("FAILED");}
     }
    
}
