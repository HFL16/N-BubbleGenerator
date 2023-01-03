/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator;

import java.util.Hashtable;



public class Tile {
    
    public double value;
    public double threshold;
    public Hashtable<Character,Integer> position;
    
    
    public Tile(double v,int r, int c){
        position = new Hashtable<Character,Integer>();
        
        value = v;
        
        position.put('r',r);
        position.put('c',c);
        
    }
    
    public String toString(){
        return Double.toString(value);
    }
    
    public double getRoundedThreshold(int p){
        
        double t = threshold;
        
        t*= Math.pow(10,p);
        int nt = (int)t;
        
        t = (double)nt/Math.pow(10, p);
        
        return t;
              
    }
    
    public double getRoundedValue(int p){
        
        double t = value;
        
        t*= Math.pow(10,p);
        int nt = (int)t;
        
        t = (double)nt/Math.pow(10, p);
        
        return t;
        
    
    }
            
    public int getBinaryValue(){
        if(value>=threshold)
            return 1;
        return 0;
    }
            
    
    
    
    public void setThreshold(double d){
        threshold = d;
    }
    
}
