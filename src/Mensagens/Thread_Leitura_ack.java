 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mensagens;

import Interface.Painel_Inicial;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.viamais.serial.SerialComLeitura;

/**
 *
 * @author Bruno
 */
public class Thread_Leitura_ack extends Thread {

    SerialComLeitura serialEscrita;
    public boolean ack_recebido;
    final int TEMPO_WAIT = 1;
    final int TEMPO_MAX = 15000;
    int tentativa = 0;
    final int TENTATIVA_MAX = 10;
    Mensagem msg;
    Painel_Inicial pai;

    public Thread_Leitura_ack(SerialComLeitura serialEscrita, Painel_Inicial Frame, Mensagem msg) {
        this.serialEscrita = serialEscrita;
        this.ack_recebido = false;
        this.pai = Frame;
        this.msg = msg;
    }

    public void enviar_msg(byte[] msg) {
        serialEscrita.HabilitarEscrita();
        serialEscrita.EnviarByte(msg);
    }

    public void run() {
        boolean sucesso = false;
        do {

            boolean esperando_ack = true;
            int Tempo = 0;

            serialEscrita.ObterIdDaPorta();

            serialEscrita.AbrirPorta();

            System.out.println("Enviando Mensagem...");

            this.pai.get_Status_label().setVisible(true);
            this.pai.get_Status_label().setText("Estabelecendo Conexão... Tentativa " + (tentativa + 1) + " de " + this.TENTATIVA_MAX);

            this.enviar_msg(msg.get_msg());

            System.out.println("Enviando Mensagem...OK!");

            serialEscrita.HabilitarLeitura();

            serialEscrita.LerDados();

            System.out.println("Iniciando Leitura...");


            this.pai.get_jProgressBar1().setMaximum(TEMPO_MAX);
            this.pai.get_jProgressBar1().setMinimum(0);

            while (esperando_ack && Tempo <= TEMPO_MAX) {

                this.pai.get_jProgressBar1().setValue(Tempo);
                if (serialEscrita.getbytes() != null) {
                    //System.out.println("ENTROU!");
                    byte auxiliar[] = serialEscrita.getbytes();

                    //if(auxiliar[1]!=0 || )
                    {

                        esperando_ack = false;
                        //System.out.println("==ACK Chegou==");
                        sucesso = true;
                        ack_recebido = true;
                        //if(this.msg.codigo==0 || this.msg.codigo==1)
                        int k = 0;
                        for (k = 0; k < auxiliar.length; k++) {
                            System.out.println("Byte " + k + ": " + auxiliar[k]);
                        }
                        serialEscrita.limparbytes();
                    }
                }

                //serialEscrita.byteslidos();
                try {
                    Thread.sleep(TEMPO_WAIT);
                } catch (InterruptedException ex) {
                }
                Tempo = Tempo + TEMPO_WAIT;
            }

            serialEscrita.FecharCom();

            if (sucesso == false) {
                System.out.println("--= ACK NÃO CHEGOU =-- ");
                this.pai.get_Status_label().setText("Falha ao Estabelecer Conexão...");
            }

            if (sucesso == true) {
                System.out.println("--= ACK CHEGOU =-- ");
                this.pai.get_jProgressBar1().setValue(TEMPO_MAX);

                switch (msg.codigo) {
                    case 0:
                        //iniciar Leiura
                        this.pai.get_Status_label().setText("Mensagem Enviada Com Sucesso!");
                        break;

                    case 1:
                        //iniciar Leiura
                        this.pai.get_Status_label().setText("Conexão Estabelecida!");
                        System.out.println("Iniciando Leitura...OK!");
                        System.out.println("Recebendo Mensagem de dados...");
                        Thread_Leitura_dados k = new Thread_Leitura_dados(this.serialEscrita, this.pai);
                        k.start();
                        this.pai.set_select_4();
                        break;
                }

                //System.out.println("Iniciando Leitura!");
            }
            tentativa++;
        } while (sucesso == false && tentativa < TENTATIVA_MAX);
    }
}
