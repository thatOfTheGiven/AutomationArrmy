package Test;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew
 */
public class JavaCommandResponds 
{
    
    public static void main (String [] args)
    {
        System.err.println("ERR MSG");
        System.out.println("STD MSG");
        
        while(true)
        {
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(JavaCommandResponds.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}
