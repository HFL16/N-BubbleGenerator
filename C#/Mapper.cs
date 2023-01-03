
using System.Collections.Generic;
using System;

public class Mapper
{
    public Tile[,] map;
    public int lConstant;
    public int wConstant;
    public int qConstant;
    public int gConstant;

    public Mapper(int length, int width, int q, int g){

        map = new Tile[length, width];

        qConstant = q;
        gConstant = g;
        lConstant = length - 1;
        wConstant = width - 1;
        genMap();

    }

    public void genMap(){

        Random r = new Random();

        for(int x = 0; x< map.GetLength(0);x++){
            for(int k = 0; k<map.GetLength(1); k++){

                map[x,k] = new Tile(r.NextDouble(),x,k);
                map[x,k].setThreshold(calculateThreshold(x,k));

            }
        }
    }

    /*

    public double calculateThreshold(int r, int c){

        double x = c- ((double) lConstant / 2);
        double y = r- ((double) wConstant / 2);

        x = Math.Pow(x,qConstant);
        y = Math.Pow(y,gConstant);

        return -getEffector() * (x+y) + 1;


    }

    public double calculateThreshold(int l, int w, int r, int c){

        double x = c - ((double) (l - 1) / 2);
        double y = r - ((double) (w - 1) / 2);

        x = Math.Pow(x, qConstant);
        y = Math.Pow(y, gConstant);

        return -getEffector(l, w) * (x + y) + 1;

    }

    */

    public double calculateThreshold(int r, int c){

        double x_comp = Math.Pow(c  - (float)wConstant/2,2)/Math.Pow(wConstant/2,2);
        double y_comp = Math.Pow(r  - (float)lConstant/2,2)/Math.Pow(lConstant/2,2);

        double val = 1- x_comp - y_comp;

        return val <= 0 ? 0:Math.Sqrt(val);

    }

    public double calculateThreshold(int l, int w, int r, int c){

        double x_comp = Math.Pow(c  - (float)w/2,2)/Math.Pow(w/2,2);
        double y_comp = Math.Pow(r  - (float)l/2,2)/Math.Pow(l/2,2);

        double val = 1- x_comp - y_comp;

        return val <= 0 ? 0:Math.Sqrt(val);

    }




public void merge(int n, double mw){

    for(int x = 0;x<n;x++){
        merge(mw);
    }


}

 public void merge(double mergeWeight) {

    Tile[,] newMap = new Tile[map.GetLength(0), map.GetLength(1)];
    
    for (int x = 0; x < map.GetLength(0); x++) {
            for (int k = 0; k < map.GetLength(1); k++) {
                List<Tile> tar = getNeighbours(x, k);

                double sum = map[x,k].value;

                for(int n = 0; n<tar.Count;n++){
                    sum+= tar[n].value;
                }

                sum /= tar.Count;

                Tile oldTile = map[x,k];
                double newVal = (oldTile.value * (1 - mergeWeight)) + (mergeWeight * sum);
                newMap[x,k] = new Tile(newVal, x, k);
                newMap[x,k].setThreshold(oldTile.threshold);

            }
        }

        map = newMap;

 }

 public void smooth(int n){

    for(int x = 0;x<n;x++){
        smooth();
    }


}


 public void smooth(){

    for (int x = 0; x < map.GetLength(0); x++) {
            for (int k = 0; k < map.GetLength(1); k++) {
                List<Tile> tar = getNeighbours(x, k);

                int c = 0;

                for (int j = 0; j < tar.Count; j++) {

                    if (map[x,k].getBinaryValue() != tar[j].getBinaryValue()) {
                        c++;
                    }

                }

                if (c >  (determineMaxNeighbours(x, k) / 2) + 1) {

                    if (map[x,k].getBinaryValue() == 1) {
                        map[x,k].value = map[x,k].threshold - 0.000001;
                    } else {
                        map[x,k].value = map[x,k].threshold + 0.000001;
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
        if (r == 0 & c == 0) {
            max--;
        }
        if (r == map.GetLength(0) - 1) {
            max--;
        }
        if (c == map.GetLength(1) - 1) {
            max--;
        }
        if (r == map.GetLength(0) - 1 & c == map.GetLength(1) - 1) {
            max--;
        }
        if (r == 0 & c == map.GetLength(1) - 1) {
            max--;
        }
        if (r == map.GetLength(0) - 1& c == 0) {
            max--;
        }

        return max;

}

public void quadrize(){

    double[,] newCave = quadrize(lConstant + 1, wConstant + 1);

    for (int x = 0; x < newCave.GetLength(0); x++) {
        for (int k = 0; k < newCave.GetLength(1); k++) {

            double newThreshold = (map[x,k].threshold + newCave[x,k]) / 2;
            map[x,k].setThreshold(newThreshold);

        }
    }

}

public double[,] quadrize(int l, int w) {
        List<double[,]> dar = new List<double[,]>();

        double[,] thresholds = new double[w, l];

        if (l < 4 || w < 4) {

            for (int x = 0; x < thresholds.GetLength(0); x++) {
                for (int k = 0; k < thresholds.GetLength(1); k++) {
                    thresholds[x,k] = -1;

                }
            }

        } else {

            for (int x = 0; x < thresholds.GetLength(0); x++) {
                for (int k = 0; k < thresholds.GetLength(1); k++) {

                    thresholds[x,k] = calculateThreshold(l, w, x, k);

                }

            }

            int lDivider = l / 2;
            int wDivider = w / 2;

            int lBounds1 = l - lDivider;
            int lBounds2 = l - lBounds1;

            int wBounds1 = w - wDivider;
            int wBounds2 = w - wBounds1;

            double[,] q1 = quadrize(lBounds1, wBounds1);
            double[,] q2 = quadrize(lBounds1, wBounds2);
            double[,] q3 = quadrize(lBounds2, wBounds1);
            double[,] q4 = quadrize(lBounds2, wBounds2);
            /*

            System.out.println("Q1: " + lBounds1 + " " + wBounds1);
            System.out.println("Q2: " + lBounds1 + " " + wBounds2);
            System.out.println("Q3: " + lBounds2 + " " + wBounds1);
            System.out.println("Q4: " + lBounds2 + " " + wBounds2);
             */

            double[,] newThresholds = new double[w, l];

            int r_offset = 0;
            int c_offset = 0;

            for (int x = 0; x < q1.GetLength(0); x++) {
                for (int k = 0; k < q1.GetLength(1); k++) {

                    newThresholds[x + r_offset, k + c_offset] = q1[x,k];

                }
            }

            r_offset = wBounds1;
            c_offset = 0;

            for (int x = 0; x < q2.GetLength(0); x++) {
                for (int k = 0; k < q2.GetLength(1); k++) {

                    newThresholds[x + r_offset, k + c_offset] = q2[x,k];

                }
            }

            r_offset = 0;
            c_offset = lBounds1;
            for (int x = 0; x < q3.GetLength(0); x++) {
                for (int k = 0; k < q3.GetLength(1); k++) {

                    newThresholds[x + r_offset, k + c_offset] = q3[x,k];

                }
            }

            r_offset = wBounds1;
            c_offset = lBounds1;

            for (int x = 0; x < q4.GetLength(0); x++) {
                for (int k = 0; k < q4.GetLength(1); k++) {

                    newThresholds[x + r_offset, k + c_offset] = q4[x,k];

                }
            }

            //System.out.println(utl.getDoubleArrStr(3,newThresholds));
            for (int x = 0; x < newThresholds.GetLength(0); x++) {
                for (int k = 0; k < newThresholds.GetLength(1); k++) {

                    if (newThresholds[x,k] != -1) {
                        thresholds[x,k] = (thresholds[x,k] + newThresholds[x,k]) / 2;
                    }

                }
            }

        }

        return thresholds;

    }


public void generateBubble(int min, int max, double weight, Random rand){

    int x_diameter = min + (int)(rand.NextDouble()*((max-min)+1));
    int y_diameter = min + (int)(rand.NextDouble()*((max-min)+1));

    int top = 0 + (int)(rand.NextDouble() * ((lConstant - y_diameter) + 1));
    int left = 0 + (int)(rand.NextDouble() * ((wConstant - x_diameter) + 1));

    for(int i = 0; i<y_diameter;i++){
         for(int k = 0; k<x_diameter;k++){
         
            double val = (map[i,k].threshold * (1-weight)) + ((1-calculateThreshold(y_diameter,x_diameter,i,k))*weight);
         
            map[i+top,k+left].setThreshold(val );
            //System.out.println(val);
         
         }
      
      }


}


public  void bubble(int min, int max, int n, double weight){

    Random r = new Random();

    for(int i = 0; i< n;i++){

        generateBubble(min,max,weight,r);

    }


}



public string getThresholdStr(int p) {
    string ns = "";

    for (int x = 0; x < map.GetLength(0); x++) {
        for (int k = 0; k < map.GetLength(1); k++) {
            string cs = " " + map[x,k].getRoundedThreshold(p) + " ";

            while (cs.Length < p + 2) {
                cs += "0";

            }

            ns += cs + ", ";

        }
        ns += "\n";
    }

    return ns;

}

public string getBinaryStr() {

    string ns = "";

    for (int x = 0; x < map.GetLength(0); x++) {
        for (int k = 0; k < map.GetLength(1); k++) {

            ns += map[x,k].getBinaryValue() + ", ";

        }
        ns += "\n";
    }

    return ns;

}

public string getValueStr(int p) {
    string ns = "";

    for (int x = 0; x < map.GetLength(0); x++) {
        for (int k = 0; k < map.GetLength(1); k++) {
            string cs = "" + map[x,k].getRoundedValue(p) + "";

            while (cs.Length < p + 2) {
                cs += "0";

            }

            ns += cs + ", ";

        }
        ns += "\n";
    }

    return ns;

}

public string getVisualStr() {

    string ns = "";

    for (int x = 0; x < map.GetLength(0); x++) {
        for (int k = 0; k < map.GetLength(1); k++) {
            if (map[x,k].getBinaryValue() == 1) {
                ns += "@, ";

            } else {
                ns += "/, ";
            }

        }
        ns += "\n";
    }

    return ns;

}

public List<Tile> getNeighbours(int r, int c) {

        List<Tile> nar = new List<Tile>();
        if (r < 0 || c < 0 || r > map.GetLength(0) - 1 || c > map.GetLength(1) - 1) {
            
            return null;
        }

        if (r > 0) {//up
            nar.Add(map[r - 1, c]);

        }
        if (r < map.GetLength(0) - 1) {//down
            nar.Add(map[r + 1, c]);
        }
        if (c > 0) {//left
            nar.Add(map[r, c - 1]);
        }
        if (c < map.GetLength(1) - 1) {//right
            nar.Add(map[r, c + 1]);
        }
        if (r > 0 & c < map.GetLength(1) - 1) {//TR
            nar.Add(map[r - 1, c + 1]);
        }
        if (r > 0 & c > 0) {//TL
            nar.Add(map[r - 1 , c - 1]);
        }
        if (r < map.GetLength(0) - 1 & c < map.GetLength(1) - 1) {//BR
            nar.Add(map[r + 1 , c + 1]);
        }
        if (r < map.GetLength(0) - 1 & c > 0) {//BL
            nar.Add(map[r + 1 , c - 1]);
        }

        return nar;

    }

    public double getEffector() {

        double leftDenom = Math.Pow(-lConstant, qConstant) * Math.Pow(2, gConstant);
        double rightDenom = Math.Pow(-wConstant, gConstant) * Math.Pow(2, qConstant);

        return Math.Pow(2, qConstant + gConstant) / (leftDenom + rightDenom);
    }

    public double getEffector(int l, int w) {
        double leftDenom = Math.Pow(-(l - 1), qConstant) * Math.Pow(2, gConstant);
        double rightDenom = Math.Pow(-(w - 1), gConstant) * Math.Pow(2, qConstant);

        return Math.Pow(2, qConstant + gConstant) / (leftDenom + rightDenom);
    }

    public int[,] getIntArr(){

        int[,] iar = new int[map.GetLength(0),map.GetLength(1)];

        for(int x = 0; x<map.GetLength(0);x++){

            for(int k = 0;k<map.GetLength(1);k++){

                iar[x,k] = map[x,k].getBinaryValue();
                
            }


        }

        return iar;


    }




}

