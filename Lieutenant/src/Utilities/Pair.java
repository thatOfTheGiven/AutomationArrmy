/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author Andrew
 * @param <L>
 * @param <R>
 */
public class Pair <L, R>
{
    public L Left  = null;
    public R Right = null;
    
    public Pair(L Left, R Right)
    {
        this.Left = Left;
        this.Right = Right;
    }
    
    public boolean equal(Pair Comp)
    {return Left.equals(Comp.Left) && Right.equals(Comp.Right);}
    
    @Override
    public String toString()
    {return Left.toString() + " " + Right.toString();}
}
