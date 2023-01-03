/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator;

/**
 *
 * @author henrique
 */
public class Utilities {

    public String getDoubleArrStr(int places, double[][] dar) {
        String ns = "";

        for (int x = 0; x < dar.length; x++) {
            for (int k = 0; k < dar[x].length; k++) {
                String cs = Double.toString(getRounded(places,dar[x][k]));
                
                while (cs.length() < places + 2) {
                    cs += "0";

                }
                
                ns+=cs + ", ";
                
            }
            ns+= "\n";
        }
        
        return ns;

    }

    public double getRounded(int places, double d) {
        
        d*= Math.pow(10, places);
        int dint = (int)d;
        d = (double)dint/Math.pow(10,places);
        
        return d;

    }

}
