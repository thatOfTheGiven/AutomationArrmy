/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities.Select;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import Utilities.Pair;
import Utilities.Monitor;
/**
 *
 * @author Andrew
 */
public class StreamSelect 
{
    private final HashMap<InputStream, Pair<StreamLissiner, Thread>> Streams= new HashMap();
    
    private final Monitor<Pair<InputStream,String>> Selectors = new Monitor();
    
    public void StreamSelect(InputStream Stream)
    {
        Add(Stream);
    }
    
    public void StreamSelect(InputStream [] Streams)
    {
        Add(Streams);
    }
    
    public void StreamSelect(ArrayList<InputStream> Streams)
    {
        Add(Streams);
    }
    
    public void Add(ArrayList<InputStream> Streams)
    {Add( (InputStream []) Streams.toArray());}
    
    
    public void Add(InputStream [] Streams)
    {
        for(InputStream Stream: Streams)
            Add(Stream);
    }
    
    public void Add(InputStream Stream)
    {        
        StreamLissiner Lissener = new StreamLissiner(Stream, Selectors);
        Thread thr = new Thread(Lissener);
        thr.start();
        Streams.put(Stream, new Pair(Lissener, thr));
    }
    
    public boolean IsOpen()
    {return !Streams.isEmpty();}
    
    public Pair<InputStream,String> next()
    {
        if(!IsOpen())
            return null;
        Pair<InputStream,String> StreamedData = null;
        try 
        {
            StreamedData = Selectors.shift();
            
            if(StreamedData.Right == null)
            {
                Streams.remove(StreamedData.Left);
                return next();
            }           
        } 
        catch (InterruptedException ex) 
        {Logger.getLogger(StreamSelect.class.getName()).log(Level.SEVERE, null, ex);}
        
        return StreamedData;
    }            
}


class StreamLissiner implements Runnable
{
    private final InputStream Stream;
    private final Monitor<Pair<InputStream,String>> out;
    
    public StreamLissiner(InputStream Stream, Monitor<Pair<InputStream,String>> out)
    {
        this.Stream = Stream;
        this.out = out;
    }
    
    @Override
    public void run()
    {
        try 
        {
            BufferedReader Reader = new BufferedReader( new InputStreamReader(Stream));
            String input;
            while((input = Reader.readLine()) != null)
            {out.push(new Pair(Stream, input));}
            
            out.push(new Pair(Stream, null));
        } 
        catch (IOException ex) 
        {Logger.getLogger(StreamLissiner.class.getName()).log(Level.SEVERE, null, ex);}
    }          
}

