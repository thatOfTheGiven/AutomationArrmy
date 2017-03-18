/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lieutenant;

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
}
