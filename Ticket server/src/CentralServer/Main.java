package CentralServer;



import ConfigReader.ConfigReader;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {


    public static void main(String[] args) throws IOException {


        ConfigReader reader = new ConfigReader();

        String[] config = reader.getConfig();

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(config[0]));
        CentralServer server = new CentralServer(serverSocket);

        server.startServer();
    }
}
