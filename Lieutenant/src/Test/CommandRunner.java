/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Utilities.SystemCall;
import Utilities.Monitor;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Andrew
 */
public class CommandRunner 
{
    
    public static void main(String [] ARGS)
    {
        SystemCall Command;
        
        Monitor <String> STDmon = new Monitor();
        Monitor <String> ERRmon = new Monitor();
 /*       
        Command = new SystemCall("java -cp D:\\System\\Andrew\\Documents\\NetBeansProjects\\AutomationArrmy\\Lieutenant\\dist\\Lieutenant.jar Test.JavaCommandResponds", STDmon, ERRmon);
        Command.run();
        String Gotten;
        try {
            while((Gotten = STDmon.shift()) != null)
                System.out.println("STD:" + Gotten);
            
            while((Gotten = ERRmon.shift()) != null)
                System.out.println("ERR:" + Gotten);
        } catch (InterruptedException ex) {
            Logger.getLogger(CommandRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Command = new SystemCall("java -cp D:\\System\\Andrew\\Documents\\NetBeansProjects\\AutomationArrmy\\Lieutenant\\dist\\Lieutenant.jar Test.JavaCommandResponds", STDmon);
        Command.run();
        try {
            while((Gotten = STDmon.shift()) != null)
                System.out.println("STD:" + Gotten);
        } catch (InterruptedException ex) {
            Logger.getLogger(CommandRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
 */              
        Command = new SystemCall("java -cp D:\\System\\Andrew\\Documents\\NetBeansProjects\\AutomationArrmy\\Lieutenant\\dist\\Lieutenant.jar Test.JavaCommandResponds");
        Thread thr = new Thread(Command);
        thr.start();
        /*
        try {
    //        Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(CommandRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        if(thr.isAlive())
        {
            Command.stop();
            
            if(thr.isAlive())
                System.out.println("Still Alive");
            else
                System.out.println("Still Died");
        }
       
        
        
        System.out.println("\n\n");
        System.out.println(Command.Console());
        System.out.println(Command.successful());
    }    
}
