import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

class TBane{
    private HashMap<String, ArrayList<String[]>> graph; //a stationID with a list of tuples too other stations and the tunnels they share with each other, {stationID, {[stationID, tunnelID], ...}} 
    private HashMap<String, Station> stations; //Station HashMap with station id and corresponding station, stationID gives correct station object 
    private HashMap<String, Tunnel> tunnels; //Tunnel Hashmap with tunnel id and corresponding tunnel, tunnelID gives correct tunnel object 

    public TBane(){
        graph = new HashMap<>();
        stations = new HashMap<>();
        tunnels = new HashMap<>();
    }

    public void graphAddStation(String station){ //in the graph
        getGraphHashMap().put(station, new ArrayList<>());
    }

    public void graphAddTunnel(String from, String to, String tunnel){ //in the graph
        String[] tuple = new String[2];
        tuple[0] = to;
        tuple[1] = tunnel;

        getGraphHashMap().get(from).add(tuple);
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

    public void readTsvFiles(){
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
                getTunnelsHashMap().put(tnID, newTunnel); //add a tunnel to the tunnel hashmap
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

                graphAddStation(stnID); //add node (station) to graph hashmap

                //System.out.println(split[0]);
                //System.out.println(split[1] + "\n");
                
                Station newStation = new Station(stnID, stnName);
                getStationsHashMap().put(stnID, newStation);

                //all connected tunnels from a station
                //here all tunnnels get added to right station and all stations to the right tunnels 
                for (int i = 2; i < split.length; i++){
                    newStation.addTunnelStation(split[i]); //add all tunnel with their ids to spesifics station own list over tunnels that goes from that spesifc statiom
                    getTunnelsHashMap().get(split[i]).addStationtoTunnel(newStation); //add new station to spesific tunnel
                }
            }
            filReader.close();

        }catch(FileNotFoundException e){
            System.out.println("file not found");
        }
    }

    public void addEgdes(){
        /* EXPLANATION: adding egdes 
        First, the graph hashmap is retrieved to iteration through. We iterater over all station ids and its list with tuples[another station, connecting tunnel]
            We adding for one station at a time, we get the station's tunnel list (all tunnel this station uses) 
                iterate over all tunnles for spesifc station 
                    get spesific tunnels own station list
                        iterate over the spesific tunnels own station list
                            now have all the information we need to add a egde
                                Each station in a tunnels internal station list uses the same tunnel as the station we curently iteration on
                                it therfore an egde and can add the egde to the grpah hashmap
                                    ["Station", {["diffrent station", "shared tunnel"], ["diffrent station", "shared tunnel"], ...}]
                
        Summed up, for every station in graph hashmap, get their tunnel list, then go trought each of those tunnels to their own station list,
        every station that is on a list shares a tunnel with the first station and have an egde                  
        
        TIME COMPLEXITY:
        You should think that the time complexecy would me O(n^2) but in reallity it not beacuse the graph is not complete
            Every station is not connected to every other station via tunnels  
        */

        //add edges to graph(tunnels between stations)
        Set<Map.Entry<String,ArrayList<String[]>>> graphHashMap = getGraphHashMap().entrySet(); //returns a Set of Map.Entry objects. Each Map.Entry object represents a key-value pair in the HashMap
        for(Map.Entry<String, ArrayList<String[]>> keyPair : graphHashMap){ //keyPair = [stationID, [list]], we iterate over alle entries in then graph HashMap

            ArrayList<String> tunnelIDList = getStationsHashMap().get(keyPair.getKey()).getTunnelIDList(); //get the list of extending tunnels from a spesific station, getKey gives a stationID 
            for(String tnID : tunnelIDList){

                ArrayList<Station> stationsList = getTunnelsHashMap().get(tnID).getStationsList(); //get list over all stations that uses a spesific tunnel
                for(Station station : stationsList){ //iterate over all stations that uses this spesific tunnel 
                    
                    if(!station.getstnID().equals(keyPair.getKey())){ //ensures that stations dont make an edge back too itself 
                        graphAddTunnel(keyPair.getKey(), station.getstnID(), tnID); //adds edge to the graph hashmap
                    }
                }
            }
        }
    }

    public void dijkstra(String startStation, String endStation){
        HashMap<String, Float> distance = new HashMap<>();
        PriorityQueue<Station> queue = new PriorityQueue<>();
        HashMap<String, String[]> path = new HashMap<>();  

        for (String key : graph.keySet()) {  //empty map with ∞ as default
            distance.put(key, Float.MAX_VALUE);
        }

        //while(!queue.isEmpty()){
        
        //}
    }


    public static void main(String[] args){
        TBane tbane = new TBane();
        tbane.readTsvFiles();
        tbane.addEgdes();
        //tbane.dijkstra(startStop, endStop)

    }

}

//Node
class Station{
    ArrayList<String> tunnelIDList = new ArrayList<String>(); //list of all tunnels that extends from this station
    String stnID;
    String stnName;
    float dist;

    public Station(String id, String name){
        stnID = id;
        stnName = name;
    }

    public void addTunnelStation(String tnId){
        tunnelIDList.add(tnId);
    }

    public ArrayList<String> getTunnelIDList(){
        return tunnelIDList;
    }

    public String getstnID(){
        return stnID;
    }

    public void stationSetDist(float setDist){
        dist = setDist;
    }
}


//Edge
class Tunnel{ //A tunnel is a egde in the graph and are between two stations, a tunnel has an ID, name and travel time  
    ArrayList<Station> stationsList = new ArrayList<Station>(); //list of all stations that uses this tunnel
    String tnID;
    String tunnelName;
    int travelTime;

    public Tunnel(String id, String name, String time){
        tnID = id;
        tunnelName = name;
        travelTime = Integer.parseInt(time);
    }

    public void addStationtoTunnel(Station station){
        stationsList.add(station);
    }

    public ArrayList<Station> getStationsList() {
        return stationsList;
    }

}