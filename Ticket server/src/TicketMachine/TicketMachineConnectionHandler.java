package TicketMachine;


import CentralServer.Attraction;
import ConfigReader.ConfigReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;

public class TicketMachineConnectionHandler {
    private String machineId;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public TicketMachineConnectionHandler(String machineId) {
        this.machineId = machineId;
    }

    private void startConnection() throws IOException {
        ConfigReader reader = new ConfigReader();
        String[] config = reader.getConfig();

        this.socket = new Socket(config[1],Integer.parseInt(config[0]));
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void closeEverything(){

        try{
            startConnection();
            if(this.bufferedReader!=null){
                this.bufferedReader.close();
            }
            if(this.bufferedWriter != null){
                this.bufferedWriter.close();
            }
            if(this.socket != null){
                this.socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Map<String, Attraction> sendGetOffersRequest(){
        try{
            startConnection();
            bufferedWriter.write("GET-OFFERS:"+this.machineId+":"+"null");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            String response="";
            ;

            var gson = new Gson();


            response = bufferedReader.readLine();


            if(response != ""){
                Type type = new TypeToken<Map<String,Attraction>>(){}.getType();

                Map<String, Attraction> responseParsed = gson.fromJson(response, type);
                System.out.println();
                responseParsed.keySet().forEach(item ->{
                    System.out.println(responseParsed.get(item).getTicketsMap());
                });


                closeEverything();
                return responseParsed;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean sendBuyTicketRequest(String attractionId){
        try{
            startConnection();
            bufferedWriter.write("BUY:"+this.machineId+":"+attractionId);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String response ="";

            response = bufferedReader.readLine();
            closeEverything();
            System.out.println(response);
            if(Integer.parseInt(response)!=0){
                return true;
            }else{
                return false;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }


}
