/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;


import javax.swing.JButton;
import javax.swing.JLabel;  
import javax.swing.JTextField;
  
public class Thread_Contador extends Thread {  
  
    String hr;
    
    @Override  
    public void run() {  
       try {  
           int segundo = 0;  
           int hora = 0;  
           int minuto = 30;  
           while( minuto!=0 || segundo !=0 ) {  
               Thread.sleep(1000);  
                 segundo--;  
                     
                   if( segundo < 00 ){  
                       segundo = 59;  
                       minuto = minuto-1;  
                   }  
      
                   String timer = completaComZero(minuto) + ":" +  
                                  completaComZero(segundo);  
                   this.hr=timer;  
                   System.out.println(hr);
           }
       } catch (InterruptedException ex) {  
           ex.printStackTrace();  
       }  
    }  
  
    private String completaComZero(Integer i) {  
        String retorno = null;  
        if( i < 10 ) {  
            retorno = "0"+i;  
        } else {  
            retorno = i.toString();  
        }  
        return retorno;  
    }  
  
}  
