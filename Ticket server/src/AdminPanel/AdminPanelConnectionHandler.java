package AdminPanel;

import ConfigReader.ConfigReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;

import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;
import CentralServer.Attraction;

public class AdminPanelConnectionHandler {

    private String adminId;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public AdminPanelConnectionHandler(String adminId){
        this.adminId = adminId;
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
            bufferedWriter.write("GET-OFFERS:"+this.adminId+":"+"null");
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

    public void sendRequest(String requestMessage) throws IOException {
        bufferedWriter.write(requestMessage);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void sentAddAttractionRequest(String attractionName, Integer ticketsAmount){
        try{
            startConnection();
            sendRequest("ADD-ATTRACTION:"+this.adminId+":"+attractionName+";"+ticketsAmount);

        }catch (IOException e){
            closeEverything();
        }
    }

    public void sendUpdateAttractionRequest(String selectedAttractionId,String name,Integer ticketAmount){
        try{
            startConnection();
            sendRequest("UPDATE-ATTRACTION:"+this.adminId+":"+selectedAttractionId+";"+ticketAmount+";"+name);

        }catch (IOException e){
            closeEverything();
        }

    }

    public void sendAddTicketsRequest(String attractionId, Integer ticketsAmount){
        try{
            startConnection();
            sendRequest("ADD-ATTRACTION-TICKETS:"+this.adminId+":"+attractionId+";"+ticketsAmount);

            String response = bufferedReader.readLine();
            System.out.println(response);
        }catch (IOException e){
            e.printStackTrace();
            closeEverything();
        }
    }



    public void sendRemoveTicketsRequest(String attractionId, Integer ticketsAmount){
        try{
            startConnection();
            sendRequest("REMOVE-ATTRACTION-TICKETS:"+this.adminId+":"+attractionId+";"+ticketsAmount);


            String response = bufferedReader.readLine();
            System.out.println(response);
        }catch (IOException e){
            e.printStackTrace();
            closeEverything();
        }
    }

    public void sendClearTicketsRequest(String attractionId){
        try{
            startConnection();
            sendRequest("CLEAR-ATTRACTION-TICKETS:"+this.adminId+":"+attractionId);


            String response = bufferedReader.readLine();
            System.out.println(response);
        }catch (IOException e){
            e.printStackTrace();
            closeEverything();
        }
    }


    public void sendRemoveAttractionRequest(String selectedAttractionId) {
        try{
            startConnection();
            sendRequest("REMOVE-ATTRACTION:"+this.adminId+":"+selectedAttractionId);
        }catch(IOException e ){
            closeEverything();
        }
    }
}
