/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author henrique
 */
public class MapGenerator {

    public Utilities utl;

    public Tile[][] map;
    public int lConstant;
    public int wConstant;
    public int qConstant;
    public int gConstant;

    public int cConstant;
    public int sConstant;

    public MapGenerator(int length, int width, int q, int g) {
        utl = new Utilities();
        map = new Tile[length][width];
        qConstant = q;
        gConstant = g;
        lConstant = length - 1;
        wConstant = width - 1;

        genMap();
    }

    public void genMap() {

        for (int x = 0; x < map.length; x++) {
            for (int k = 0; k < map[x].length; k++) {

                map[x][k] = new Tile(Math.random(), x, k);
                map[x][k].setThreshold(calculateThreshold(x, k));

            }
        }

    }
    
    /*

    public double calculateThreshold(int r, int c) {

        double x = c - ((double) lConstant / 2);
        double y = r - ((double) wConstant / 2);

        x = Math.pow(x, qConstant);
        y = Math.pow(y, gConstant);

        return -getEffector() * (x + y) + 1;

    }

    public double calculateThreshold(int l, int w, int r, int c) {
        double x = c - ((double) (l - 1) / 2);
        double y = r - ((double) (w - 1) / 2);

        x = Math.pow(x, qConstant);
        y = Math.pow(y, gConstant);

        return -getEffector(l, w) * (x + y) + 1;

    }
    
    */
    
    public  double calculateThreshold(int r, int  c){
    
      double x_comp = Math.pow(c - (double)wConstant/2,2)/Math.pow((double)wConstant/2,2);
      double y_comp = Math.pow(r - (double)lConstant/2,2)/Math.pow((double)lConstant/2,2);
      
      double val = 1-x_comp-y_comp;
      
      return val < 0 ? 0:Math.sqrt(val);
    }
    
    public  double calculateThreshold(int l, int w, int r, int  c){
    
      double x_comp = Math.pow(c- (double)w /2,2)/Math.pow((double)w /2,2);
      double y_comp = Math.pow(r- (double)l /2,2)/Math.pow((double)l /2,2);
            
      double val = 1-x_comp-y_comp;
      
      return val < 0 ? 0:Math.sqrt(val);

    }

    public void merge(double mergeWeight) {

        Tile[][] newMap = new Tile[map.length][map[0].length];

        for (int x = 0; x < map.length; x++) {
            for (int k = 0; k < map[x].length; k++) {
                ArrayList<Tile> tar = getNeighbours(x, k);

                double sum = map[x][k].value;

                for (int n = 0; n < tar.size(); n++) {
                    sum += tar.get(n).value;
                }

                sum /= tar.size();

                Tile oldTile = map[x][k];
                double newVal = (oldTile.value * (1 - mergeWeight)) + (mergeWeight * sum);
                newMap[x][k] = new Tile(newVal, x, k);
                newMap[x][k].setThreshold(oldTile.threshold);

            }
        }

        map = newMap;

    }

    public void merge(int n, double mergeWeight) {

        for (int x = 0; x < n; x++) {
            merge(mergeWeight);
        }

    }

    public void smooth(int n) {

        for (int x = 0; x < n; x++) {
            smooth();
        }

    }

    public void smooth() {

        for (int x = 0; x < map.length; x++) {
            for (int k = 0; k < map[x].length; k++) {
                ArrayList<Tile> tar = getNeighbours(x, k);

                int c = 0;

                for (int j = 0; j < tar.size(); j++) {

                    if (map[x][k].getBinaryValue() != tar.get(j).getBinaryValue()) {
                        c++;
                    }

                }

                if (c > (determineMaxNeighbours(x, k)/2)+1) {

                    if (map[x][k].getBinaryValue() == 1) {
                        map[x][k].value = map[x][k].threshold - 0.000001;
                    } else {
                        map[x][k].value = map[x][k].threshold + 0.000001;
                    }

                }

            }
        }

    }

    public int determineMaxNeighbours(int r, int c) {

        int max = 8;

        if (r == 0) {
            max--;
        }
        if (c == 0) {
            max--;
        }
        if (r == 0 && c == 0) {
            max--;
        }
        if (r == map.length - 1) {
            max--;
        }
        if (c == map[r].length - 1) {
            max--;
        }
        if (r == map.length - 1 && c == map[r].length - 1) {
            max--;
        }
        if (r == 0 && c == map[r].length - 1) {
            max--;
        }
        if (r == map.length - 1 && c == 0) {
            max--;
        }

        return max;

    }

    public void quadrize() {

        double[][] newCave = quadrize(lConstant + 1, wConstant + 1);

        //System.out.println(utl.getDoubleArrStr(3, newCave));

        for (int x = 0; x < newCave.length; x++) {
            for (int k = 0; k < newCave[x].length; k++) {

                double newThreshold = (map[x][k].threshold + newCave[x][k]) / 2;
                map[x][k].setThreshold(newThreshold);

            }
        }

    }

    public double[][] quadrize(int l, int w) {
        ArrayList<double[][]> dar = new ArrayList<double[][]>();

        double[][] thresholds = new double[w][l];

        if (l < 4 || w < 4) {

            for (int x = 0; x < thresholds.length; x++) {
                for (int k = 0; k < thresholds[x].length; k++) {
                    thresholds[x][k] = -1;

                }
            }

        } else {

            for (int x = 0; x < thresholds.length; x++) {
                for (int k = 0; k < thresholds[x].length; k++) {

                    thresholds[x][k] = calculateThreshold(l, w, x, k);

                }

            }

            int lDivider = l / 2;
            int wDivider = w / 2;

            int lBounds1 = l - lDivider;
            int lBounds2 = l - lBounds1;

            int wBounds1 = w - wDivider;
            int wBounds2 = w - wBounds1;

            double[][] q1 = quadrize(lBounds1, wBounds1);
            double[][] q2 = quadrize(lBounds1, wBounds2);
            double[][] q3 = quadrize(lBounds2, wBounds1);
            double[][] q4 = quadrize(lBounds2, wBounds2);
            /*

            System.out.println("Q1: " + lBounds1 + " " + wBounds1);
            System.out.println("Q2: " + lBounds1 + " " + wBounds2);
            System.out.println("Q3: " + lBounds2 + " " + wBounds1);
            System.out.println("Q4: " + lBounds2 + " " + wBounds2);
             */

            double[][] newThresholds = new double[w][l];

            int r_offset = 0;
            int c_offset = 0;

            for (int x = 0; x < q1.length; x++) {
                for (int k = 0; k < q1[x].length; k++) {

                    newThresholds[x + r_offset][k + c_offset] = q1[x][k];

                }
            }

            r_offset = wBounds1;
            c_offset = 0;

            for (int x = 0; x < q2.length; x++) {
                for (int k = 0; k < q2[x].length; k++) {

                    newThresholds[x + r_offset][k + c_offset] = q2[x][k];

                }
            }

            r_offset = 0;
            c_offset = lBounds1;
            for (int x = 0; x < q3.length; x++) {
                for (int k = 0; k < q3[x].length; k++) {

                    newThresholds[x + r_offset][k + c_offset] = q3[x][k];

                }
            }

            r_offset = wBounds1;
            c_offset = lBounds1;

            for (int x = 0; x < q4.length; x++) {
                for (int k = 0; k < q4[x].length; k++) {

                    newThresholds[x + r_offset][k + c_offset] = q4[x][k];

                }
            }

            //System.out.println(utl.getDoubleArrStr(3,newThresholds));
            for (int x = 0; x < newThresholds.length; x++) {
                for (int k = 0; k < newThresholds[x].length; k++) {

                    if (newThresholds[x][k] != -1) {
                        thresholds[x][k] = (thresholds[x][k] + newThresholds[x][k]) / 2;
                    }

                }
            }

        }

        return thresholds;

    }
    
    public void generateBubble(int min, int max,double weight){
    
      int x_diameter = min + (int)(Math.random() * ((max - min) + 1));
      int y_diameter = min + (int)(Math.random() * ((max - min) + 1));
      
      int top = 0 + (int)(Math.random() * ((lConstant - y_diameter) + 1));
      int left = 0 + (int)(Math.random() * ((lConstant - x_diameter) + 1));
      
      System.out.println("xd: " + x_diameter);
      System.out.println("yd: " + y_diameter);
            
      for(int i = 0; i<y_diameter;i++){
         for(int k = 0; k<x_diameter;k++){
         
            double val = (map[i][k].threshold * (1-weight)) + ((1-calculateThreshold(y_diameter,x_diameter,i,k))*weight);
         
            map[i+top][k+left].setThreshold(val );
            //System.out.println(val);
         
         }
      
      }
    
    }
    
    
    public void bubble(int min,int max,int n,double weight){
      
      for(int i= 0; i<n; i++){
      
         generateBubble(1,Math.min(min,max),weight);
         
      
      }
    
    }
    
    
    
    
    

    public String getThresholdStr(int p) {
        String ns = "";

        for (int x = 0; x < map.length; x++) {
            for (int k = 0; k < map[x].length; k++) {
                String cs = Double.toString(map[x][k].getRoundedThreshold(p));

                while (cs.length() < p + 2) {
                    cs += "0";

                }

                ns += cs + ", ";

            }
            ns += "\n";
        }

        return ns;

    }

    public String getBinaryStr() {

        String ns = "";

        for (int x = 0; x < map.length; x++) {
            for (int k = 0; k < map[x].length; k++) {

                ns += map[x][k].getBinaryValue() + ", ";

            }
            ns += "\n";
        }

        return ns;

    }

    public String getValueStr(int p) {
        String ns = "";

        for (int x = 0; x < map.length; x++) {
            for (int k = 0; k < map[x].length; k++) {
                String cs = Double.toString(map[x][k].getRoundedValue(p));

                while (cs.length() < p + 2) {
                    cs += "0";

                }

                ns += cs + ", ";

            }
            ns += "\n";
        }

        return ns;

    }

    public String getVisualStr() {

        String ns = "";

        for (int x = 0; x < map.length; x++) {
            for (int k = 0; k < map[x].length; k++) {
               
                if (map[x][k].getBinaryValue() == 1) {
                    ns += "@, ";

                } else {
                    ns += "/, ";
                }

            }
            ns += "\n";
        }

        return ns;

    }

    public ArrayList<Tile> getNeighbours(int r, int c) {

        ArrayList<Tile> nar = new ArrayList<Tile>();
        if (r < 0 || c < 0 || r > map.length - 1 || c > map[0].length - 1) {
            System.out.println("Invalid input");
            return null;
        }

        if (r > 0) {//up
            nar.add(map[r - 1][c]);

        }
        if (r < map.length - 1) {//down
            nar.add(map[r + 1][c]);
        }
        if (c > 0) {//left
            nar.add(map[r][c - 1]);
        }
        if (c < map[r].length - 1) {//right
            nar.add(map[r][c + 1]);
        }
        if (r > 0 && c < map[r].length - 1) {//TR
            nar.add(map[r - 1][c + 1]);
        }
        if (r > 0 && c > 0) {//TL
            nar.add(map[r - 1][c - 1]);
        }
        if (r < map.length - 1 && c < map[r].length - 1) {//BR
            nar.add(map[r + 1][c + 1]);
        }
        if (r < map.length - 1 && c > 0) {//BL
            nar.add(map[r + 1][c - 1]);
        }

        return nar;

    }

    public double getEffector() {

        double leftDenom = Math.pow(-lConstant, qConstant) * Math.pow(2, gConstant);
        double rightDenom = Math.pow(-wConstant, gConstant) * Math.pow(2, qConstant);

        return Math.pow(2, qConstant + gConstant) / (leftDenom + rightDenom);
    }

    public double getEffector(int l, int w) {
        double leftDenom = Math.pow(-(l - 1), qConstant) * Math.pow(2, gConstant);
        double rightDenom = Math.pow(-(w - 1), gConstant) * Math.pow(2, qConstant);

        return Math.pow(2, qConstant + gConstant) / (leftDenom + rightDenom);
    }

}
