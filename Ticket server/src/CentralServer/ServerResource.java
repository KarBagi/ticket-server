package CentralServer;


import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerResource {
    public static ServerResource instance;

    private Map<String, Attraction> attractionTicketMap;

    private ServerResource(){
        this.attractionTicketMap = new HashMap<>();
        this.attractionTicketMap.put(UUID.randomUUID().toString(),new Attraction("Karuzela",10));
        this.attractionTicketMap.put(UUID.randomUUID().toString(),new Attraction("Autka",10));
        this.attractionTicketMap.put(UUID.randomUUID().toString(),new Attraction("Słoik",10));


    }

    synchronized public static ServerResource getInstance(){
        if(instance == null){
            instance = new ServerResource();
        }
        return instance;
    }



    synchronized private void clearTickets(String attractionId){
        this.attractionTicketMap.get(attractionId).clearTickets();
    }


    synchronized public String getAttractionMapJSON(){
        var gson  = new Gson();
        var json = gson.toJson(attractionTicketMap);
        System.out.println(json);
        return json;
    }

    synchronized public Integer buyTicket(String attractionId){
        if(this.attractionTicketMap.get(attractionId)!= null){
            Integer boughtTicket = this.attractionTicketMap.get(attractionId).buyTicket();
            return boughtTicket;

        }else{
            return 0;
        }

    }




        synchronized public String addAttraction(String attractionName,Integer ticketAmount){

        attractionTicketMap.put(UUID.randomUUID().toString(),new Attraction(attractionName,ticketAmount));
        return "Pomyślnie dodano atrakcję";

    }

    synchronized public String addAttractionTickets(String attractionId, Integer ticketsAmount){
        if(attractionTicketMap.keySet().contains(attractionId)){
            this.attractionTicketMap.get(attractionId).addTicketAmount(ticketsAmount);
            return "Pomyślnie dodano bilety";
        }else{
            return "Nie znaleziono atrakcji o takim id";
        }
    }

    synchronized public String removeAttractionTickets(String attractionId, Integer ticketsAmount){
        if(attractionTicketMap.keySet().contains(attractionId)){
            this.attractionTicketMap.get(attractionId).removeTicketAmount(ticketsAmount);
            return "Pomyślnie usunięto bilety";
        }else{
            return "Nie znaleziono atrakcji o takim id";
        }
    }

    synchronized public String clearAttractionTickets(String attractionId){
        if(attractionTicketMap.keySet().contains(attractionId)){
            clearTickets(attractionId);
            return "Pomyślnie wyczyszcono bilety";
        }else{
            return "Nie znaleziono atrakcji o takim id";
        }
    }

    synchronized public void updateAttraction(String attractionID, Integer ticketAmount, String attractionName) {
        attractionTicketMap.get(attractionID).setName(attractionName);
        Integer actualTicketAmount = attractionTicketMap.get(attractionID).getTicketsMap().keySet().size();
        if(ticketAmount>actualTicketAmount){
            attractionTicketMap.get(attractionID).addTicketAmount(ticketAmount-actualTicketAmount);
        }else if(ticketAmount<actualTicketAmount){
            attractionTicketMap.get(attractionID).removeTicketAmount(actualTicketAmount-ticketAmount);
        }
    }
    synchronized  public void removeAttraction(String attractionId){
        attractionTicketMap.remove(attractionId);
    }
}
