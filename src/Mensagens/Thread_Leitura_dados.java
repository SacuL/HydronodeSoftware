 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;


import Interface.Painel_Inicial;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import net.viamais.serial.SerialComLeitura;


/**
 *
 * @author Bruno
 */
public class Thread_Leitura_dados extends Thread {
    
    SerialComLeitura serialEscrita;
    public boolean ack_recebido;
    final int TEMPO_WAIT=1;
    final int TEMPO_MAX=40000;
    
    int tentativa=0;
    final int TENTATIVA_MAX=1;
    
    
    Mensagem msg;
    
    Painel_Inicial pai;
    
    
    public Thread_Leitura_dados(SerialComLeitura serialEscrita,Painel_Inicial Frame)
    {
        this.serialEscrita = serialEscrita;
        this.ack_recebido=false;
        this.pai=Frame;
        this.msg=null;
    }
    
        public Thread_Leitura_dados(SerialComLeitura serialEscrita,Painel_Inicial Frame,Mensagem msg)
    {
        this.serialEscrita = serialEscrita;
        this.ack_recebido=false;
        this.pai=Frame;
        this.msg=msg;
    }
    
    
    public void enviar_msg(byte[] msg)
    {
        serialEscrita.HabilitarEscrita();
        serialEscrita.EnviarByte(msg);
    }

    public int bytes2Int(byte b0, byte b1)
    {
        byte[] bytes = {b0,b1,0,0};
        int f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        return f;
    }
    
    public boolean[] byteArray2BitArray(byte[] bytes) {
    boolean[] bits = new boolean[bytes.length * 8];
    for (int i = 0; i < bytes.length * 8; i++) {
      if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0)
        bits[i] = true;
    }
    return bits;
  }
    
     public void run()
    { 
        boolean sucesso=false;
        
        boolean esperando_ack=true;
        
        int Tempo=0;

            serialEscrita.ObterIdDaPorta();

            serialEscrita.AbrirPorta();
            if(msg!=null)
            {
                System.out.println("Enviando Mensagem...");
                this.enviar_msg(msg.get_msg());
            }
            this.pai.get_Status_label().setVisible(true);
            this.pai.get_Status_label().setText("Estabelecendo Conexão... Tentativa "+(tentativa+1)+" de "+this.TENTATIVA_MAX);
            
            

            serialEscrita.HabilitarLeitura();

            serialEscrita.LerDados();

            System.out.println("Iniciando Leitura...");


            this.pai.get_jProgressBar1().setMaximum(TEMPO_MAX);
            this.pai.get_jProgressBar1().setMinimum(0);
        
            while(esperando_ack && Tempo<=TEMPO_MAX)
            {

                this.pai.get_jProgressBar1().setValue(Tempo);
                if(serialEscrita.getbytes()!=null)
                  {
                      //System.out.println("ENTROU!");
                      byte auxiliar[] = serialEscrita.getbytes();
                      
                      for(int das=0;das<auxiliar.length;das++)
                      {
                          System.out.println("Byte "+das+" ==> Valor: "+auxiliar[das]);
                      }
                      

                      int id=this.bytes2Int(auxiliar[1],(byte)0);
                      int valor_sensor[]={-1,-1,-1,-1,-1,-1,-1,-1};
                     
                      //System.out.println(auxiliar.length);
                      for(int l=7,k=0;l<=21;l=l+2,k++)
                      {
                        valor_sensor[k]=this.bytes2Int(auxiliar[l],auxiliar[l+1]);
                      }
                      byte[] aux={auxiliar[6]};
                      boolean[] bits;
                      bits=this.byteArray2BitArray(aux);
                      
                      /*for(int i=0;i<bits.length;i++)
                      {
                          System.out.println(bits[i]);
                      }*/
                      
                      
                      String valores_sensor[]={"-","-","-","-","-","-","-","-"};
                      
                      for(int l=0;l<valores_sensor.length;l++)
                      {
                          if(bits[l])
                          {
                              valores_sensor[l]=String.valueOf(valor_sensor[l]);
                          }
                      }
                      
                      
                      //if(auxiliar[1]!=0 || )
                        {
                            
                           DefaultTableModel model=((DefaultTableModel)pai.get_jTable().getModel());
                           model.addRow(new Object[]{id,valores_sensor[0],valores_sensor[1],valores_sensor[2],valores_sensor[3],
                           valores_sensor[4],valores_sensor[5],valores_sensor[6],valores_sensor[7]});
                           
                           esperando_ack=false;
                           //System.out.println("==ACK Chegou==");
                           sucesso=true;
                           ack_recebido=true;
                           serialEscrita.limparbytes();
                        }
                  }

                //serialEscrita.byteslidos();
                try {Thread.sleep(TEMPO_WAIT);} 
                catch (InterruptedException ex) {}
                Tempo=Tempo+TEMPO_WAIT;
            }

            serialEscrita.FecharCom();

            if(sucesso==false)
            {
                System.out.println("--= ACK NÃO CHEGOU =-- ");
                this.pai.get_Status_label().setText("Falha ao Estabelecer Conexão...");
                System.out.println("Iniciando Leitura...OK!");
            }

            if(sucesso==true)
            {
               System.out.println("--= ACK CHEGOU =-- ");
               this.pai.get_jProgressBar1().setValue(TEMPO_MAX);
               this.pai.get_Status_label().setText("Conexão Estabelecida!");
               System.out.println("Iniciando Leitura...OK!");
               
               //System.out.println("Iniciando Leitura!");
            }
    }
}
