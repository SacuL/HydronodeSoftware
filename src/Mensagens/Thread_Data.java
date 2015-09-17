/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import Interface.Painel_Inicial;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;  
import javax.swing.JTextField;
  
public class Thread_Data extends Thread {  
  
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    Painel_Inicial pai;
    
    public Thread_Data(Painel_Inicial pai)
    {
        this.pai=pai;
    }
    
    @Override  
    public void run() {  
        String data;
        String Parte1;
        String Parte2;
        
        String Ano;
        String Mes;
        String Dia;
        
        String Hora;
        String Minutos;
        String Segundos;
        
        long segundos;
        
        while(true) {  
            
            
           date = new Date();
           segundos=date.getTime()*1000;
           pai.set_data_segundos(segundos);
           
           data=dateFormat.format(date).toString();
           Parte1=data.split(" ")[0];
           Parte2=data.split(" ")[1];
           
           Ano=Parte1.split("/")[0];
           Mes=Parte1.split("/")[1];
           Dia=Parte1.split("/")[2];
           
           Hora=Parte2.split(":")[0];
           Minutos=Parte2.split(":")[1];
           Segundos=Parte2.split(":")[2];
           
           //Panel.get_campo_data().setText(Dia+"/"+Mes+"/"+Ano);
           //Panel.get_campo_hora().setText(Hora+":"+Minutos+":"+Segundos);
           
           
           
           try {
                   Thread.sleep(1000);
               } catch (InterruptedException ex) {
                   Logger.getLogger(Thread_Data.class.getName()).log(Level.SEVERE, null, ex);
               }
           
           }
         
    }  
  
  
}  