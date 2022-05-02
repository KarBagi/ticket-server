package CentralServer;

import java.util.HashMap;
import java.util.Map;

public class Attraction {
    private String name;
    private Map<Integer,Boolean> ticketsMap;

    public Attraction(String name,Integer ticketAmount){
        this.name = name;
        this.ticketsMap = new HashMap<>();
        addTicketAmount(ticketAmount);
    }
    public Map<Integer,Boolean> getTicketsMap(){
        return this.ticketsMap;
    }

    public void addTicketAmount( Integer ticketsAmount){
        Integer lastTicketId = getLastTicketId();

        for(int i=1;i<=ticketsAmount;i++){
            this.ticketsMap.put(lastTicketId+i,true);
        }

    }
    public void removeTicketAmount(Integer ticketsAmount){
        Integer lastTicketId = getLastTicketId();

        for(int i=0; i>-ticketsAmount;i--){
            this.ticketsMap.remove(lastTicketId+i);
        }

    }

    public Integer getLastTicketId(){
        Integer max = 0;
        for(Map.Entry<Integer,Boolean> entry:this.ticketsMap.entrySet()){
            if(entry.getKey()>max){
                max=entry.getKey();
            }
        }
        return max;
    }

    public Integer buyTicket(){
        for(Map.Entry<Integer,Boolean> entry: this.ticketsMap.entrySet()){
            if(entry.getValue()){
                this.ticketsMap.put(entry.getKey(),false);
                return entry.getKey();
            }
        }
        return 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void clearTickets() {
        this.ticketsMap = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }
}
