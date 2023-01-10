import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final Socket clientSocket;
        final BufferedReader br;
        final PrintWriter out;
        final Scanner scan = new Scanner(System.in);
        final InputStreamReader reader;
        final OutputStreamWriter writer;

        try{
            clientSocket = new Socket("127.0.0.1", 5000);

            reader = new InputStreamReader(clientSocket.getInputStream());
            writer = new OutputStreamWriter(clientSocket.getOutputStream());

            out = new PrintWriter(writer);
            br = new BufferedReader(reader);

            Thread sender = new Thread(new Runnable() {
                String text;
                @Override
                public void run() {
                    while (true){
                        text = scan.nextLine();
                        out.write(text);
                        out.flush();
                    }
                }
            });
            sender.start();
            Thread receiver = new Thread(new Runnable() {
                String text;
                @Override
                public void run() {
                    try {
                        text = br.readLine();
                        while (text != null){
                            System.out.println("Server : "+ text);
                            text = br.readLine();
                        }
                        System.out.println("Server out of service");
                        out.close();
                        clientSocket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
