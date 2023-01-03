using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GenMap : MonoBehaviour
{

    public int length = 10;
    public int width = 10;

    public int q;
    public int g;

    public Transform floor;
    public GameObject wall;

    
    
    


    void Start()
    {

        floor.localScale += new Vector3(width - floor.localScale.x,1-floor.localScale.y,length-floor.localScale.z);
        floor.position += new Vector3(((float)width-1)/2 - floor.position.x,0,((float)length-1)/2 - floor.position.z);
        

        Mapper m = new Mapper(length,width,q,g);

        
        m.bubble(1,20,5,0.8);
        m.merge(2,0.3);
        m.smooth(3);

        int[,] arr = m.getIntArr(); 
        

        for(int x = 0; x< width;x++){
            for(int k = 0; k<length;k++){

                if(arr[x,k] == 1){
                    
                    GameObject nt = Instantiate(wall,new Vector3(x,1,k),Quaternion.Euler(0,0,0));
                }            

            }
        }




    }

    





}
