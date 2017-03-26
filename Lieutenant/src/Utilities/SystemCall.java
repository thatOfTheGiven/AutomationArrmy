/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import Utilities.Select.StreamSelect;

/**
 *
 * @author Andrew
 */
public class SystemCall implements Runnable
{
    private Monitor<String> STDout = null;
    private Monitor<String> ERRout = null;
    private final String CMD;

    private final ArrayList <String> Console = new ArrayList();
    private int ExitValue;
    private Process proc = null;
    
    
    public SystemCall(String CMD)
    {this.CMD=CMD;}
    
    
    public SystemCall(String CMD, Monitor<String> STDout)
    {
        this.CMD=CMD;
        this.STDout=STDout;
    }
    
    
    public SystemCall(String CMD, Monitor<String> STDout, Monitor<String> ERRout)
    {
        this.CMD=CMD;
        this.STDout=STDout;
        this.ERRout=ERRout;
    }
    
    
    @Override
    public void run()
    {
        try 
        {
            StreamSelect select = new StreamSelect();
            
            SetProccess();
            
            select.Add(proc.getInputStream());
            select.Add(proc.getErrorStream());
            
            Pair<InputStream, String> Line;
            while((Line = select.next()) != null)
            {
                String value = Line.Right;
                
                Console.add(value);
                long Seconds = System.currentTimeMillis();
                
                if(Line.Left == proc.getErrorStream())
                    value = "<ERR seconds=\"" + Seconds + "\">" + value + "</ERR>";
                else
                    value = "<STD seconds=\"" + Seconds + "\">" + value + "</STD>";
                
                if(value.contains("<ERR") && ERRout != null)
                    ERRout.push(value);
                else if(STDout != null)
                    STDout.push(value);
            }
            
            if(ERRout != null)
                ERRout.push(null);
            if(STDout != null)
                STDout.push(null);
            
            ExitValue = proc.exitValue();
        } 
        catch (IOException ex) 
        {Logger.getLogger(SystemCall.class.getName()).log(Level.SEVERE, null, ex);}
    }

    
    public void stop()
    {
        try 
        {WaitProccessing();}
        catch (IOException | InterruptedException ex) 
        {Logger.getLogger(SystemCall.class.getName()).log(Level.SEVERE, null, ex);}
        
        if(proc != null)
            proc.destroy();
    }
    
    
    private synchronized void SetProccess() throws IOException
    {
        Runtime Rt = Runtime.getRuntime();
        proc = Rt.exec(CMD);
        
        notifyAll();
    }
    
 
    public synchronized void WaitProccessing() throws IOException, InterruptedException
    {
        while( proc == null)
            wait();
    }
    
    
    public boolean successful()
    {return ExitValue == 0;}
    
    
    public String Console()
    {
        String Result = "";

        for(String Line: Console)
            if(!Result.equals(""))
                Result += "\n"+Line;
            else
                Result = Line;
        
        return Result;
    }
}