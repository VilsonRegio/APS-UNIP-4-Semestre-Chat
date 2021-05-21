package chat;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Servidor 
{
    static ArrayList<String> listaUsuarios = new ArrayList<String>(); //Lista de usuarios conectados
    static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>(); // lista de destinatarios
    
    public static void main(String[] args) throws Exception 
    {
        System.out.println("Aguardando novos usuários...");
        ServerSocket ss = new ServerSocket(9086);
        while (true)
        {
            Socket usuario = ss.accept();
            System.out.println("Usuario conectado!");
            
            ManipuladorConversa conversa = new ManipuladorConversa(usuario);
            conversa.start();
        }
    }
}

class ManipuladorConversa extends Thread 
{
    Socket usuarioConversa; // ????
    BufferedReader entrada; //trabalhar com dados recebidos, enviados pelo cliente
    PrintWriter saida; //enviar dados para o cliente
    String nome; // nome do usuario
    
    public ManipuladorConversa (Socket usuarioConversa) throws IOException//?
    {
        this.usuarioConversa = usuarioConversa;
    }
    
    public void run()
    {
        try 
        {
            entrada = new BufferedReader(new InputStreamReader(usuarioConversa.getInputStream()));//Ler valores que foram enviados como entrada, e buscando valor de entrada
            saida = new PrintWriter(usuarioConversa.getOutputStream(), true);//Utilizamos para valores de saida
            
            int cont = 0;
            while(true) // verificar nomes existentes ou não / vazio ou não
            {
                if (cont > 0)
                {
                    saida.println("NOME EXISTENTE");
                }
                else
                {
                    saida.println("NOME EM BRANCO");
                }
                
                nome = entrada.readLine();
                
                if (nome == null)
                {
                    System.out.println("NOME NULO");
                    return;
                }
                
                if (!Servidor.listaUsuarios.contains(nome))// verificar se o nome já está sendo utilizado
                {
                    Servidor.listaUsuarios.add(nome);
                    break;
                }
                
                cont++;
            }
            
            saida.println("NOME ACEITO" + nome);
            Servidor.printWriters.add(saida);
            
            while(true)
            {
                String msg = entrada.readLine(); // a mensagem do usuario vindo pro servidor
                if (msg == null) // se a mensagem for nula
                {
                    return;
                }
                for (PrintWriter writer : Servidor.printWriters /*mudar nome da lista para destinatarios*/ )
                {
                    writer.println(nome + ": " + msg); // colocar nome de quem enviou a mensagem
                }
            }
        } 
        catch (Exception e) 
        {
            System.out.println("ERRO NO SERVIDOR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
