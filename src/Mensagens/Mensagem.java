/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import net.viamais.serial.SerialComLeitura;

/**
 *
 * @author Bruno
 */
public class Mensagem {
    byte[] msg;    
    int codigo=0;
    
    //codigo=0 => Somente recebe ACK!
    //codigo=1 => 1ª Mensagem de Leitura de Dados
    //codigo=2 => recebe dados!
    
    public Mensagem(byte msg[])
    {
        this.msg=msg;
    }
    
    public Mensagem(byte msg[],int cod)
    {
        this.msg=msg;
        this.codigo=cod;
    }
    
    public byte[] get_msg()
    {
        return this.msg;
    }
}
