package chat;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

public class Cliente 
{
    
    static JFrame janela = new JFrame("Chat"); //janela do chat
    static JTextArea mensagens = new JTextArea(22,40); //ver as mensagens
    static JTextField txfNovaMensagem = new JTextField(40); //digitar a mensagem
    static JButton btnEnviar = new JButton("Enviar"); //enviar mensagens
    static BufferedReader entrada; //receber informações do servidor
    static PrintWriter saida; //mandar informalções do servidor
    static JLabel usuarioAtual = new JLabel("");
    
    public Cliente() 
    {
        janela.setLayout(new FlowLayout()); //tipo de layout
        janela.add(usuarioAtual); // usuario que da janela
        janela.add(new JScrollPane(mensagens)); //add a area de mensagens
        janela.add(txfNovaMensagem); //add area de mensagem
        janela.add(btnEnviar); //add botao
        
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //opção de fechar
        janela.setSize(475, 500); //tamanho da janela
        janela.setVisible(true); //deixando janela visivel
        mensagens.setEditable(false); //não habilitando a edição
        txfNovaMensagem.setEditable(false); //habilitando o campo para escrever uma nova mensagem
        
        btnEnviar.addActionListener(new Listener()); // vincular o botao com o Listener
        txfNovaMensagem.addActionListener(new Listener()); // vincular o enter com o Listener
    }
    
    public void IniciarConversa() throws Exception
    {
        String IP = JOptionPane.showInputDialog(janela, "Endereço IP por favor: ", "Informação", JOptionPane.PLAIN_MESSAGE); //pedir o endereço do servidor
        Socket usuario = new Socket(IP, 9086); // criar uma instancia para a conexão
        
        entrada = new BufferedReader(new InputStreamReader(usuario.getInputStream())); // istanciar o objeto de entrada
        saida = new PrintWriter(usuario.getOutputStream(), true); // istanciar o objeto de saida
        
        while(true) // aguardar o envio de mensagems
        {
            String msg = entrada.readLine();// 
            
            if (msg.equals("NOME EM BRANCO"))
            {
                String nome = JOptionPane.showInputDialog(janela, "Nome do usuário", "Informação", JOptionPane.PLAIN_MESSAGE);// pedindo ao usuario o nome dele
                saida.println(nome);
            }
            else if (msg.equals("NOME EXISTENTE"))
            {
                String nome = JOptionPane.showInputDialog(janela, "Informe outro nome de usuário:", "Nome duplicado", JOptionPane.WARNING_MESSAGE);// digitar outro nome pois o nome ja foi utilizado
                saida.println(nome);
            }
            else if (msg.startsWith("NOME ACEITO"))
            {
                txfNovaMensagem.setEditable(true);// habilitando a digitação de novas mensagens
                usuarioAtual.setText("Você está logado como: " + msg.substring(11));
            }
            else
            {
                mensagens.append(msg + "\n"); // faz com que as mensagens recebidas qie nao forem dos ifs anteriores sejam inseridas na interface grafica
            }
        }
    }
    
    public static void main (String [] args) throws Exception
    {
        Cliente chat = new Cliente();
        chat.IniciarConversa();
    }
}

class Listener implements ActionListener
{
    @Override
    public void actionPerformed (ActionEvent e) //quando o botao for clicado 
    {
        Cliente.saida.println(Cliente.txfNovaMensagem.getText()); // mandar a mensagem digita para o servidor
        Cliente.txfNovaMensagem.setText(""); // limpar o campo de texto
    }
}
