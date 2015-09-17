/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.viamais.serial;


import java.nio.ByteBuffer;

import gnu.io.CommPortIdentifier;

import gnu.io.SerialPort;

import gnu.io.SerialPortEvent;

import gnu.io.SerialPortEventListener;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;


public class SerialComLeitura implements Runnable, SerialPortEventListener {
    

    
private boolean ack; /*define quando a resposta é o ack ou a leitura de dados reais*/

private boolean ACK_RECEIVED;

public String Dadoslidos;

public int nodeBytes;

private int baudrate;

private int timeout;

private CommPortIdentifier cp;

private SerialPort porta;

private OutputStream saida;

private InputStream entrada;

private Thread threadLeitura;

private boolean IDPortaOK;

private boolean PortaOK;

private boolean Leitura;

private boolean Escrita;

private String Porta;

protected byte peso[];

private byte bytedado[];

public void limparbytes()
{
    this.bytedado=null;
}

public byte[] getbytes()
{
    return this.bytedado;
}




public void settimeout(int i)
{
    this.timeout=i;
}

public void setbaudrate(int i)
{
    this.baudrate=i;
}

public void setporta(String p){

    this.Porta=p;
    
}

public boolean getACK()
{
    return this.ACK_RECEIVED;
}

public void esperandoAck()
{
    this.ACK_RECEIVED=false;
    this.ack=true;
}

public void setPeso(byte[] peso){

        this.peso = peso;

}

public byte[] getPeso(){

        return peso;

}

public SerialComLeitura( String p , int b , int t ){

        this.Porta = p;

        this.baudrate = b;

        this.timeout = t;

}

public void HabilitarEscrita(){

        Escrita = true;

        Leitura = false;

}

public void HabilitarLeitura(){

        Escrita = false;

        Leitura = true;

}

public void ObterIdDaPorta(){

        try {

            cp = CommPortIdentifier.getPortIdentifier(Porta);

            if ( cp == null ) {

                System.out.println("Erro na porta");

                IDPortaOK = false;

                System.exit(1);

            }

            IDPortaOK = true;

        } catch (Exception e) {

            System.out.println("Erro obtendo ID da porta: " + e);

            IDPortaOK = false;

            System.exit(1);

        }

}

public void AbrirPorta(){

        try {

            porta = (SerialPort)cp.open("SerialComLeitura", timeout);

            PortaOK = true;

            //configurar parâmetros

            porta.setSerialPortParams(baudrate,

            porta.DATABITS_8,

            porta.STOPBITS_1,

            porta.PARITY_NONE);

            porta.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

        }catch(Exception e){

            PortaOK = false;

            System.out.println("Erro abrindo comunicação: " + e);

            System.exit(1);

        }

}

public void LerDados(){

        if (Escrita == false){

            try {

                entrada = porta.getInputStream();

            } catch (Exception e) {

                System.out.println("Erro de stream: " + e);

                System.exit(1);

            }

            try {

                porta.addEventListener(this);

            } catch (Exception e) {

                System.out.println("Erro de listener: " + e);

                System.exit(1);

            }

            porta.notifyOnDataAvailable(true);

            try {

                threadLeitura = new Thread(this);

                threadLeitura.start();

               run();

            } catch (Exception e) {

                System.out.println("Erro de Thred: " + e);

            }

        }

}

public void EnviarUmaString(String msg){

        if (Escrita==true) {

            try {

                saida = porta.getOutputStream();

                System.out.println("FLUXO OK!");

            } catch (Exception e) {

                System.out.println("Erro.STATUS: " + e );

            }

            try {

                System.out.println("Enviando mensagem para " + Porta );

                System.out.println("Enviando : " + msg );

                saida.write(msg.getBytes());

                Thread.sleep(100);

                saida.flush();

            } catch (Exception e) {

                System.out.println("Houve um erro durante o envio. ");

                System.out.println("STATUS: " + e );

                System.exit(1);

            }

        } else {

            System.exit(1);

        }

}

public void run(){

        try {

            Thread.sleep(5);

        } catch (Exception e) {

            System.out.println("Erro de Thred: " + e);

        }

}

public void byteslidos()
{
    this.bytedado=null;
}

public void serialEvent(SerialPortEvent ev){       

        ByteBuffer bufferLeitura = ByteBuffer.allocate(100);
        
        int novoDado = 0;
        this.bytedado = null;


        switch (ev.getEventType()) {

            case SerialPortEvent.BI:

            case SerialPortEvent.OE:

            case SerialPortEvent.FE:

            case SerialPortEvent.PE:

            case SerialPortEvent.CD:

            case SerialPortEvent.CTS:

            case SerialPortEvent.DSR:

            case SerialPortEvent.RI:

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:

            break;

            case SerialPortEvent.DATA_AVAILABLE:

                //Novo algoritmo de leitura.
                    
                while(novoDado != -1){

                    try{

                        novoDado = entrada.read();

                        if(novoDado == -1){

                            break;

                        }

                        if('\r' == (char)novoDado){

                            //bufferLeitura.append('\n');

                        }else{

                            bufferLeitura.put((byte) novoDado);

                        }

                    }catch(IOException ioe){

                        System.out.println("Erro de leitura serial: " + ioe);

                    }

                }

                byte[] leitura=bufferLeitura.array();
                byte[] leitura_aux= new byte[leitura[0]+1];
                
                for(int k=0;k<leitura[0]+1;k++)
                {
                    leitura_aux[k]=leitura[k];
                }
                
                
                setPeso(leitura_aux);

               /* System.out.println(getPeso()); */
                
                bytedado=(byte[])getPeso();//.getBytes();
        //{
            //int i;
            //for(i=0;i<bytedado.length;i++)
            //{
               /* if(i==0 && bytedado[0]==1 && ack)
                {
                    this.ACK_RECEIVED=true;
                }*/ 
           //System.out.println(bytedado[i]);
            //}
        //}
            break;

        }

}

public void FecharCom(){

            try {

                porta.close();

            } catch (Exception e) {

                System.out.println("Erro fechando porta: " + e);

                System.exit(0);

            }

}

public String obterPorta(){

        return Porta;

}

public int obterBaudrate(){

        return baudrate;

    }

public void EnviarByte(byte[] msg){
               
        if (Escrita==true) {

            try {

                saida = porta.getOutputStream();

                //System.out.println("FLUXO OK!");

            } catch (Exception e) {

                System.out.println("Erro.STATUS: " + e );

            }

            try {

                System.out.println("Enviando mensagem para " + Porta );

                //System.out.println("Enviando : " + msg[0]+" " +msg[1]+" " + msg[2] );

                saida.write(msg);

                Thread.sleep(100);

                saida.flush();

            } catch (Exception e) {

                System.out.println("Houve um erro durante o envio. ");

                System.out.println("STATUS: " + e );

                System.exit(1);

            }

        } else {

            System.exit(1);

        }

}


}