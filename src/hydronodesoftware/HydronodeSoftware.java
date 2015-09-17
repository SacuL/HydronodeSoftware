/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hydronodesoftware;

import Interface.Painel_Inicial;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Bruno
 */
public class HydronodeSoftware {

    /**
     * @param args the command line arguments
     */
    
            static public int bytes2Int(byte b0, byte b1)
    {
        byte[] bytes = {b0,b1,0,0};
        int f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        return f;
    }
            
    public static void main(String[] args) {
        /*
        byte b0=-1;
        byte b1=0;
        
        System.out.println(bytes2Int(b0,b1));
        */
      
        System.out.println("-Hydronode Software-");
        
        System.out.println("Carregando Interface...");
        
        Painel_Inicial Inicial = new Painel_Inicial();
        Inicial.setVisible(true);
        
        System.out.println("Carregando Interface...OK!");
        
    }
}
