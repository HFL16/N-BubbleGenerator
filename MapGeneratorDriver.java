/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator;



public class MapGeneratorDriver{
    
    public static void main(String[] args){
        //Scanner sc = new Scanner(System.in);
        
        //sc.nextLine();
        
        //MapGenerator m = new MapGenerator(40,50,2,2); WORKS WELL
        MapGenerator m = new MapGenerator(40,50,2,2);
        m.bubble(20,20,10,0.8);
        m.merge(2,0.3);
        m.smooth(10);

        
        //System.out.println(m.getThresholdStr(3));
        //System.out.println(m.getValueStr(3));
        /*System.out.println("\n");
        System.out.println("ORIGINAL:");
        System.out.println(m.getVisualStr());
        
        System.out.println("\n\n\n");
        
        m.quadrize();
        System.out.println(m.getThresholdStr(3));
        System.out.println("QUADRIZED:");
        System.out.println(m.getVisualStr());
        
        
        System.out.println("\n\n\n");
        
        m.merge(10,0.3);
        System.out.println(m.getThresholdStr(3));
        System.out.println("MERGED:");
        System.out.println(m.getVisualStr());
        
        
        System.out.println("\n\n\n");
        
        m.smooth(3);
        System.out.println(m.getThresholdStr(3));
        System.out.println("SMOOTH:");
        
        */
        System.out.println(m.getVisualStr());
        

    }
    
}