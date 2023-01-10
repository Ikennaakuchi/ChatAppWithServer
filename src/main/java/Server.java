import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        final Socket clientSocket;
        final InputStreamReader reader;
        final OutputStreamWriter writer;
        final BufferedReader br;
        final PrintWriter out;
        final Scanner scan = new Scanner(System.in);
        final ServerSocket serverSocket;

        try{
            serverSocket = new ServerSocket(5000);
            clientSocket = serverSocket.accept();

            reader = new InputStreamReader(clientSocket.getInputStream());
            writer = new OutputStreamWriter(clientSocket.getOutputStream());

            out = new PrintWriter(writer);
            br = new BufferedReader(reader);

            Thread sender = new Thread(new Runnable() {
                String text;
                @Override
                public void run() {
                    while(true){
                        text = scan.nextLine();
                        out.write(text);
//                        out.println(text);
                        out.flush();
                    }
                }
            });
            sender.start();
            Thread receive = new Thread(new Runnable() {
                String text;
                @Override
                public void run() {
                    try {
                        text = br.readLine();
                        while (text != null){
                            System.out.println("Client : " + text);
                            text = br.readLine();
                        }
                        System.out.println("Client Disconnected");
                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
