/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.util.ArrayList;

/**
 *
 * @author Andrew
 * @param <K>
 */
public class Monitor <K>
{
    ArrayList<K> queue = new ArrayList();
    
    public synchronized void unshift(K input) 
    {
        queue.add(0, input);
        
        notifyAll();
    }
    
    public synchronized void push(K input) 
    {
        queue.add(input);
        
        notifyAll();
    }
    
    public synchronized K shift() throws InterruptedException 
    {
        while(queue.isEmpty())
            wait();
        
        K result = queue.get(0);
        queue.remove(0);
        
        return result;
    }
    
    public synchronized K pop() throws InterruptedException 
    {
        while(queue.isEmpty())
            wait();
        
        int last = queue.size()-1;
        K result = queue.get(last);
        queue.remove(last);
        
        return result;
    }
    
    public synchronized int size()
    {return queue.size();}
    
    public synchronized K get (int position)
    {return queue.get(position);}
    
    public synchronized K remove(int position)
    {
        K result = queue.get(position);
        queue.remove(position);
        
        return result;
    }
    
    public synchronized void insert(int position, K value)
    {queue.add(position, value);}
}
