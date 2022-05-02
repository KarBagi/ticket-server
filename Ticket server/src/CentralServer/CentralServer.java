package CentralServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CentralServer {

    private ServerSocket serverSocket;

    public CentralServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }


    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                System.out.println("waiting for connection");
                Socket socket = serverSocket.accept();

                ServerConnectionHandler handler = new ServerConnectionHandler(socket);

                Thread thread = new Thread(handler);
                thread.start();
            }
        }catch (IOException e){

        }
    }


}
