/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.viamais.serial.SerialComLeitura;

/**
 *
 * @author Bruno
 */
public class Gerador_Mensagens {

    public int bytes2Int(byte b0, byte b1) {
        byte[] bytes = {b0, b1, 0, 0};
        int f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

        return f;
    }

    public Gerador_Mensagens() {
    }

    /*private void enviar_msg(byte[] msg)
     {
     serialEscrita.HabilitarEscrita();
     serialEscrita.EnviarByte(msg);
     }
     */
    public Mensagem msg_1_1(int id_sonda_aux) {
        byte codigo = 1;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);

        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3]};

        //MONTA MSG
        set_lenght(msg);

        return new Mensagem(msg, 1);

        //ENVIA MSG
        //enviar_msg(msg);

        //System.out.println(id_sonda[3]);

    }

    public Mensagem msg_1_1_Resp(int mais_aux) {
        //CONVERTE ENTRADAS
        byte[] mais = ByteBuffer.allocate(4).putInt(mais_aux).array();

        //MONTA CORPO DAS MSG
        byte msg[] = {0, mais[3]};

        //MONTA MSG
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg); 

        return new Mensagem(msg, 0);
    }

    public Mensagem msg_1_2_1(int id_sonda_aux, boolean sensor[]) {
        byte codigo = 2;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte hab = this.boolean2Byte(sensor);

        System.out.println(bytes2Int(hab, (byte) 0));

        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], 1, hab};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg);    
        return new Mensagem(msg, 0);
    }

    public Mensagem msg_1_2_2(int id_sonda_aux, int id_sensor_aux, int const_A_aux, int const_B_aux) {
        byte codigo = 2;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte id_sensor = this.boolean2Byte(id_sensor2Boolean(id_sensor_aux));

        byte[] const_A = int2ByteArray(const_A_aux);
        byte[] const_B = int2ByteArray(const_B_aux);


        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], 2, id_sensor, const_A[2], const_A[3], const_B[2], const_B[3]};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg);       
        return new Mensagem(msg, 2);
    }

    public Mensagem msg_1_2_3(int id_sonda_aux, int id_sensor_aux, int tipo_aux) {
        byte codigo = 2;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte id_sensor = this.boolean2Byte(id_sensor2Boolean(id_sensor_aux));

        byte[] tipo = int2ByteArray(tipo_aux);



        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], 3, id_sensor, tipo[3]};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg);   
        return new Mensagem(msg, codigo);
    }

    public Mensagem msg_1_2_4(int id_sonda_aux, int id_sensor_aux) {
        byte codigo = 2;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte id_sensor = this.boolean2Byte(id_sensor2Boolean(id_sensor_aux));


        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], 4, id_sensor};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg);  
        return new Mensagem(msg, codigo);
    }

    public Mensagem msg_1_3(int id_sonda_aux, int id_sonda_novo_aux) {
        byte codigo = 4;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte[] id_sonda_novo = int2ByteArray(id_sonda_novo_aux);


        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], id_sonda_novo[3]};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg); 
        return new Mensagem(msg, codigo);
    }

    public Mensagem msg_1_4(int id_sonda_aux, long data_aux) {
        byte codigo = 5;

        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte[] data = long2ByteArray(data_aux);

        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], data[4], data[5], data[6], data[7]};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg);
        return new Mensagem(msg, 0);
    }

    public Mensagem msg_1_5(int id_sonda_aux, int intervalo_aux) {
        byte codigo = 6;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte[] intervalo = int2ByteArray(intervalo_aux);


        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], intervalo[2], intervalo[3]};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg); 
        return new Mensagem(msg, codigo);
    }

    public Mensagem msg_1_6(int id_sonda_aux, int option_aux) {
        byte codigo = 7;
        //CONVERTE ENTRADAS
        byte[] id_sonda = int2ByteArray(id_sonda_aux);
        byte[] option = int2ByteArray(option_aux);


        //MONTA CORPO DAS MSG
        byte msg[] = {0, codigo, id_sonda[3], option[3]};
        set_lenght(msg);

        //ENVIA MSG
        //enviar_msg(msg); 
        return new Mensagem(msg, codigo);
    }

    public static byte[] long2ByteArray(long value) {
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    public byte boolean2Byte(boolean[] sensores_status) {
        String s = new String();
        for (int j = 0; j < sensores_status.length; j++) {
            if (sensores_status[j]) {
                s = s.concat("1");
            } else {
                s = s.concat("0");
            }
        }
        byte numberByte = (byte) Integer.parseInt(s, 2); //so mode 2
        //System.out.println(numberByte);
        return numberByte;
    }

    public boolean[] id_sensor2Boolean(int num) {
        boolean[] retorno = {false, false, false, false, false, false, false, false};
        retorno[num - 1] = true;

        return retorno;
    }

    public static byte[] int2ByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public void set_lenght(byte[] msg) {
        byte[] msg_leght = ByteBuffer.allocate(4).putInt(msg.length - 1).array();
        msg[0] = msg_leght[3];
    }
}
