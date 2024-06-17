import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

class TBane{
    HashMap<String, ArrayList<String[]>> graph; //en stasjonsID med liste av tupler til andre stasjoner og tunnelen de deler mellom seg  
    HashMap<String, Station> stations; //stasjonID gir riktig stasjonsobjekt 
    HashMap<String, Tunnel> tunnels; //tunnelID gir riktig tunnelobjekt 

    public TBane(){
        graph = new HashMap<>();
        stations = new HashMap<>();
        tunnels = new HashMap<>();
    }

    public void addStation(String station){ // in the graph
        graph.put(station, new ArrayList<>());
    }

    public void addTunnel(String from, String to, String tunnel){ //i grafen 
        String[] val = new String[2];
        val[0] = to;
        val[1] = tunnel;

        graph.get(from).add(val);
    }

    public HashMap<String, ArrayList<String []>> getGraph(){
        return graph;
    }

    public HashMap<String, Station> getStations(){ //bruker under innlesning 
        return stations;
    }

    public HashMap<String, Tunnel> getTunnels(){ //bruker under innlesning 
        return tunnels;
    }


    public static void main(String[] args){
        TBane tbane = new TBane();

        //Tunnels - The tunnels has to be added into the tunnels hashmap first so i can be used when making stations 
        try{
            Scanner filReader = new Scanner(new File("Tunnels.tsv"));
            while(filReader.hasNextLine()){
                String line = filReader.nextLine();
                System.out.println(line);
            }
            filReader.close();

        }catch(FileNotFoundException e){
            System.out.println("fant ikke fil");
        }

        System.out.println("//////////////");
        //Stations
        try{
            Scanner filReader = new Scanner(new File("Stations.tsv"));
            while(filReader.hasNextLine()){
                String line = filReader.nextLine();
                System.out.println(line);
            }
            filReader.close();

        }catch(FileNotFoundException e){
            System.out.println("fant ikke fil");
        }

        
    }
}

class Station{
    String stnID;
    String stnName;
    ArrayList<String> tnIDer = new ArrayList<String>(); //list of all tunnels that extends from this station

}

class Tunnel{ //Egde in the graph between two nodes(stations), it only has an ID, name and travel time for the tunnel  
    String tnID;
    String tunnelName;
    int travelTime;
}