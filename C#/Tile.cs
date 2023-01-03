using System.Collections.Generic;
using System;

public class Tile
{

    public double value;
    public double threshold;
    public Dictionary<string, int> position;
    
    public Tile(double v, int r, int c){

        position = new Dictionary<string, int>();

        value = v;

        position.Add("r",r);
        position.Add("c",c);


    }

     public override string ToString(){
        return value.ToString();

     }

     public double getRoundedThreshold(int p){

        return Math.Round(threshold,p);


     }

     public double getRoundedValue(int p){

        return Math.Round(value,p);


     }

     public int getBinaryValue(){
        
        if(value>= threshold){
            return 1;

        }

        return 0;
     }

     public void setThreshold(double d){
        threshold = d;

     }



}
