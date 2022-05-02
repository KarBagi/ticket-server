package CentralServer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnectionHandler implements Runnable{

    public static ArrayList<ServerConnectionHandler> handlerArrayList = new ArrayList<>();
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public ServerConnectionHandler (Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            handlerArrayList.add(this);

        }catch (IOException e){
            closeEverything();
        }
    }

    public void closeEverything(){
        handlerArrayList.remove(this);
        try{
            if(this.bufferedWriter!=null){
                this.bufferedWriter.close();
            }
            if(this.bufferedReader!=null){
                this.bufferedWriter.close();
            }
            if(this.socket!=null) {
                this.socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String request;
        String[] splicedRequest;
        while(this.socket.isConnected()){
            try{
                request = bufferedReader.readLine();
                splicedRequest = request.split(":");
                String requestType = splicedRequest[0];
                String clientId = splicedRequest[1];
                String additionalInfo = splicedRequest[2];

                switch (requestType){
                    case"GET-OFFERS"->{
                        bufferedWriter.write(ServerResource.getInstance().getAttractionMapJSON());
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    case"BUY"->{
                        bufferedWriter.write(ServerResource.getInstance().buyTicket(additionalInfo).toString());
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    case"ADD-ATTRACTION-TICKETS"->{
                        String[] splicedAdditionalInfo = additionalInfo.split(";");
                        String attractionId = splicedAdditionalInfo[0];
                        String ticketsAmount = splicedAdditionalInfo[1];

                        ServerResource.getInstance().addAttractionTickets(attractionId,Integer.parseInt(ticketsAmount));
                        bufferedWriter.write("chuj");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    case"REMOVE-ATTRACTION-TICKETS"->{
                        String[] splicedAdditionalInfo = additionalInfo.split(";");
                        String attractionId = splicedAdditionalInfo[0];
                        String ticketsAmount = splicedAdditionalInfo[1];

                        ServerResource.getInstance().removeAttractionTickets(attractionId,Integer.parseInt(ticketsAmount));
                        bufferedWriter.write("");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    case"CLEAR-ATTRACTION-TICKETS"->{
                        ServerResource.getInstance().clearAttractionTickets(additionalInfo);
                        bufferedWriter.write("");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    case"ADD-ATTRACTION"->{
                        String[] splicedAdditionalInfo = additionalInfo.split(";");
                        String attractionName = splicedAdditionalInfo[0];
                        String ticketsAmount = splicedAdditionalInfo[1];

                        ServerResource.getInstance().addAttraction(attractionName,Integer.parseInt(ticketsAmount));
                    }
                    case"UPDATE-ATTRACTION"->{
                        String[] splicedAdditionalInfo = additionalInfo.split(";");
                        String attractionID = splicedAdditionalInfo[0];
                        Integer ticketAmount = Integer.parseInt(splicedAdditionalInfo[1]);
                        String attractionName= splicedAdditionalInfo[2];

                        ServerResource.getInstance().updateAttraction(attractionID,ticketAmount,attractionName);
                    }
                    case"REMOVE-ATTRACTION"->{
                        String attractionId = additionalInfo;
                        ServerResource.getInstance().removeAttraction(attractionId);
                    }
                }
            }catch (IOException e){
                closeEverything();
                break;
            }
        }

    }
}
