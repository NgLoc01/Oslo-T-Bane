import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

class TBane{
    HashMap<String, ArrayList<String[]>> graph; //a stationID with a list of tuples too other stations and tunnels they share with each other 
    HashMap<String, Station> stations; //stationID gives correct station object 
    HashMap<String, Tunnel> tunnels; //tunnelID gives correct tunnel object 

    public TBane(){
        graph = new HashMap<>();
        stations = new HashMap<>();
        tunnels = new HashMap<>();
    }

    public void graphAddStation(String station){ //in the graph
        graph.put(station, new ArrayList<>());
    }

    public void graphAddTunnel(String from, String to, String tunnel){ //in the graph
        String[] val = new String[2];
        val[0] = to;
        val[1] = tunnel;

        graph.get(from).add(val);
    }

    public HashMap<String, ArrayList<String []>> getGraphHashMap(){
        return graph;
    }

    public HashMap<String, Station> getStationsHashMap(){ //used when reading tsv files
        return stations;
    }

    public HashMap<String, Tunnel> getTunnelsHashMap(){ //used when reading tsv files
        return tunnels;
    }


    public static void main(String[] args){
        TBane tbane = new TBane();

        //Tunnels - The tunnels has to be added into the tunnels hashmap first so i can be used when making stations 
        //Reading Tunnels.tsv
        try{
            Scanner filReader = new Scanner(new File("Tunnels.tsv"));
            while(filReader.hasNextLine()){
                String line = filReader.nextLine();
                String[] split = line.split(" ");
            
                String tnID = split[0];
                String tunnelName = split[1];
                String travelTime = split[2]; 

                //System.out.println(split[0]);
                //System.out.println(split[1]);
                //System.out.println(split[2] + "\n");

                Tunnel newTunnel = new Tunnel(tnID, tunnelName, travelTime); //make tunnel
                tbane.getTunnelsHashMap().put(tnID, newTunnel); //add a tunnel to the tunnel hashmap
            }
            filReader.close();

        }catch(FileNotFoundException e){
            System.out.println("file not found");
        }

        //Reading Stations.tsv
        try{
            Scanner filReader = new Scanner(new File("Stations.tsv"));
            while(filReader.hasNextLine()){
                String line = filReader.nextLine();
                String[] split = line.split(" ");

                String stnID = split[0];
                String stnName = split[1];

                tbane.graphAddStation(stnID); //add node (station) to graph hashmap

                //System.out.println(split[0]);
                //System.out.println(split[1] + "\n");
                
                Station newStation = new Station(stnID, stnName);
                tbane.getStationsHashMap().put(stnID, newStation);

                //all connected tunnels from a station 
                for (int i = 2; i < split.length; i++){
                    newStation.addTunnelStation(split[i]); //add all tunnel with their ids to spesifics station own list over tunnels that goes from that spesifc tunnel
                    if (tbane.getTunnelsHashMap().containsKey(split[i])){//if tunnel id exist in tunnel hashmap 
                        tbane.getTunnelsHashMap().get(split[i]).addStationtoTunnel(newStation); //add new station to spesific tunnel
                    }
                }
            
            }
            filReader.close();

        }catch(FileNotFoundException e){
            System.out.println("file not found");
        }
    }
}

//Node
class Station{
    ArrayList<String> tunnelIDList = new ArrayList<String>(); //list of all tunnels that extends from this station
    String stnID;
    String stnName;

    public Station(String id, String name){
        stnID = id;
        stnName = name;
    }

    public void addTunnelStation(String tnId){
        tunnelIDList.add(tnId);
    }
}


//Edge
class Tunnel{ //Egde in the graph between two nodes(stations), it only has an ID, name and travel time for the tunnel  
    ArrayList<Station> stations = new ArrayList<Station>(); //list of all stations that uses this tunnel
    String tnID;
    String tunnelName;
    int travelTime;

    public Tunnel(String id, String name, String time){
        tnID = id;
        tunnelName = name;
        travelTime = Integer.parseInt(time);
    }

    public void addStationtoTunnel(Station station){
        stations.add(station);
    }

}