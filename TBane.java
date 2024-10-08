import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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


    public void readTsvFiles(){//read the both Station.tsv and Tunnels.tsv to get all necessary data to recreate Oslo metro in hashmaps 
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

                if(split[0].charAt(0) != split[0].charAt(1)){ //Example"12TN1812 Majorstuen-Nationaltheatret 1000" transitionTunnel
                    Tunnel newTunnel = new Tunnel(tnID, tunnelName, travelTime, true); //make normal tunnel
                    getTunnelsHashMap().put(tnID, newTunnel); //add a tunnel to the tunnel hashmap
                }else{
                    Tunnel newTunnel = new Tunnel(tnID, tunnelName, travelTime, false); //make transitional tunnel
                    getTunnelsHashMap().put(tnID, newTunnel); //add a tunnel to the tunnel hashmap
                }             
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


    public void addEgdes(){//method to add edgdes(tunnles) between station in the graph hashmap
        //add edges to graph(tunnels between stations)
        Set<Map.Entry<String,ArrayList<String[]>>> graphHashMap = getGraphHashMap().entrySet(); //returns a Set of Map.Entry objects. Each Map.Entry object represents a key-value pair in the HashMap
        for(Map.Entry<String, ArrayList<String[]>> keyPair : graphHashMap){ //keyPair = [stationID, [list]], we iterate over alle entries in then graph HashMap
            
            ArrayList<String> tunnelIDList = getStationsHashMap().get(keyPair.getKey()).getTunnelIDList(); //get the list of extending tunnels from a spesific station, getKey gives a stationID 
            for(String tnID : tunnelIDList){

                ArrayList<Station> stationsList = getTunnelsHashMap().get(tnID).getStationsList(); //get list over all stations that uses a spesific tunnel
                for(Station station : stationsList){ //iterate over all stations that uses this spesific tunnel 
                    if(!(station.getstnID().equals(keyPair.getKey()) ) ){ //ensures that stations dont make an edge back too itself 
                        graphAddTunnel(keyPair.getKey(), station.getstnID(), tnID); //adds edge to the graph hashmap
                    }
                }
            }
        }
    }


    public void dijkstra(String[] startAndEnd){ //dijkstra algorithem to find the shortes path 
        if(startAndEnd == null){//Identical station was choosen two times or user wrote q to quit from user input 
            return;
        }

        String startStation = startAndEnd[0]; //we use the start station to get the optimal route to all other station in the metro network, keep in mind we one only one of those routes
        String desinationStation = startAndEnd[1]; //doesn't get used in the dijkstra 
        
        HashMap<String, Float> distance = new HashMap<>(); //empty map with ∞ as default
        for (String key : graph.keySet()) {  
            distance.put(key, Float.MAX_VALUE);
        }

        PriorityQueue<Station> queue = new PriorityQueue<>();
        getStationsHashMap().get(startStation).stationSetDist((float) 0); //set station's dist variable to 0
        queue.offer(getStationsHashMap().get(startStation)); //queue ← singleton priority queue containing s with priority 0

        distance.put(startStation, (float) 0); //dist[s] ← 0

        HashMap<String, String[]> allPaths = new HashMap<>();//dijkstra gives the shortes route from one node to all other nodes, we only need one route 
        allPaths.put(startStation, null);  

        while(!queue.isEmpty()){
            Station station = queue.poll();

            //get the ArrayList of tuple for spesific station in the graph hashmap
            //this is to iterate through all neighboring stations in the grapth to the station that just got taken from the priority queue
            for(String[] tuple : getGraphHashMap().get(station.getstnID())){ 
                String nextStationID = tuple[0]; 
                String tunnelID = tuple[1];
                Tunnel tunnel = getTunnelsHashMap().get(tunnelID);

                float c = distance.get(station.getstnID()) + getTunnelsHashMap().get(tunnelID).getTravelTime(); //c ← dist[u] + w(u, v), distance from a station + tunnels trave time 

                if(c < distance.get(nextStationID)){ //triggers only if its a shorter way from out of all neighboring stations to the station that just got taken from the priority queue 
                    distance.put(nextStationID, c);//dist[v] ← c

                    getStationsHashMap().get(nextStationID).stationSetDist(c); //insert(queue, v) v have ny priority 
                    queue.offer(getStationsHashMap().get(nextStationID)); //put the new shortes path back in the queue to check their neighboring stations for a shorter path

                    String[] stationInfo = new String[2];
                    stationInfo[0] = station.getstnID();
                    stationInfo[1] = tunnel.gettnID();

                    allPaths.put(nextStationID, stationInfo); //gives the shortest path from one node to all other nodes in the graph
                }
            }
        }
        printPath(allPaths, startStation, desinationStation);
    }


    public void printPath(HashMap<String, String[]> allPaths, String startStation, String desinationStation){//get all the optimal routes from dijkstra method and pick the one route we want, add station from the end to front, then print the stack from last in first out to get it in chronological order  
        int totalTime = 0;
        String current = desinationStation;
        Stack<String> stack = new Stack<String>(); //stack to reverse the path given to give it in right chronological order 

        //Traversing from our destination and backward to find the one path we want 
        while(current != null){ //we are working from end to start backward to find the only one path, the 
            if (allPaths.get(current) != null){

                stack.push("===[" + getStationsHashMap().get(current).getstnID() + " " + getStationsHashMap().get(current).getstnName() + "]===> ");
                totalTime += getTunnelsHashMap().get(allPaths.get(current)[1]).getTravelTime();;

                if(getStationsHashMap().get(current).getstnID().charAt(0) != allPaths.get(current)[0].charAt(0)){
                    stack.push("\nChange to Line:" + getStationsHashMap().get(current).getstnID().charAt(0));
                }
                
                current = allPaths.get(current)[0]; //allPaths.get(current)[0] is nextStationID

            }else{//start station will be the last added to the stack and the first on read
                stack.push("===[" + getStationsHashMap().get(current).getstnID() + " " + getStationsHashMap().get(startStation).getstnName() + "]===>");
                stack.push("Start at Line:" + getStationsHashMap().get(current).getstnID().charAt(0));
                stack.push("======================================");
                stack.push("            Optimal route:             ");
                stack.push("======================================");
                break;
            }
        }
        while (!stack.empty()){  //Printing out the path we want
            System.out.println(stack.pop()); 
        }
        System.out.println("\nTotal time: " + totalTime + "min");

    }


    public String[] askRoute(){//asks the depature and destination station from the user and sends the answerer to the dijkstra() method
        Scanner scan = new Scanner(System.in);
        String chosenLine = "";
        String start = "";
        String end = "";

        chosenLine = chooseLine(scan);
        if(chosenLine == null){
            clearTerminal();
            return null;
        }
        start = chooseStation(chosenLine, scan);
        if(start == null){
            clearTerminal();
            return null;
        }

        chosenLine = chooseLine(scan);
        if(chosenLine == null){
            clearTerminal();
            return null;
        }
        end = chooseStation(chosenLine, scan);
        if(end == null){
            clearTerminal();
            return null;
        }

        //Start and Destionation answere
        String[] startAndEnd = new String[2];
        startAndEnd[0] = start; 
        startAndEnd[1] = end;

        if(getStationsHashMap().get(start).getstnName().equals(getStationsHashMap().get(end).getstnName())){ //Identical station was choosen two times
            System.out.println("======================================");
            System.out.println("              Oslo Metro              ");
            System.out.println("======================================");
            System.out.println("You are already here");
            return null;
        }
        return startAndEnd;
    }


    public String chooseStation(String chosenLine, Scanner scan){ //used in askRoute() to choose a station from the user
        Boolean loop = true;
        String chosenStation = "";

        while (loop) {
            switch(chosenLine){
                case "1":
                    printLine('1');
                    break;
                case "2":
                    printLine('2');
                    break;
                case "3":
                    printLine('3');
                    break;
                case "4":
                    printLine('4');
                    break;
                case "5":
                    printLine('5');
                    break;
            }
            System.out.print("\nWrite departure station number: (q to quit): ");
            //Write destination station number
            String input = scan.nextLine();
            if(input.equals("q")){
                return null;
            }
            
            //add all station from the choosen line to a hashset so it can be checked for valied station input from that spesific line 
            Set<String> validStations = new HashSet<>();
            try{
                Scanner filReader = new Scanner(new File("Stations.tsv"));
                while(filReader.hasNextLine()){
                    String line = filReader.nextLine();
                    String[] split = line.split(" ");
    
                    if(split[0].charAt(0) == chosenLine.charAt(0)){
                        validStations.add(split[0].charAt(4) + ""+ split[0].charAt(5));
                    }
                }
                filReader.close();
            }catch(FileNotFoundException e){
                System.out.println("file not found");
            }


            if(input.length() == 1){ //treat 01 and 1 equally e.g
                input = "0" + input;
            }
            chosenStation = chosenLine + "STN" + input;


            if(validStations.contains(input)){//check if user input is a station that is valid on the choosen line 
                loop = false;
            }else{
                System.out.println("\n[INVALID INPUT, TRY AGAIN]");

                try { //make the message appear for a short time 
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e);;
                }
                clearTerminal();
            }
        }
        clearTerminal();
        return chosenStation;
    }


    public void printLine(char lineNum){ //used in chooseStation() to print all the station for a choosen line
        System.out.println("--------------------------------------");
        System.out.println("          Selected Line: " + lineNum);
        System.out.println("--------------------------------------");
        try{
            Scanner filReader = new Scanner(new File("Stations.tsv"));
            while(filReader.hasNextLine()){
                String line = filReader.nextLine();
                String[] split = line.split(" ");

                if(split[0].charAt(0) == lineNum){
                    System.out.println(""+split[0].charAt(4) + split[0].charAt(5) + " " + split[1]); 
                }
            }

            filReader.close();
        }catch(FileNotFoundException e){
            System.out.println("file not found");
        }
    }


    public String chooseLine(Scanner scan){//used in askRoute() to make the user choose the line where a spesific station is at
        Boolean loop = true;
        String chosenLine = null;

        while(loop){
            System.out.println("======================================");
            System.out.println("              Oslo Metro              ");
            System.out.println("======================================");
            System.out.println("Choose linje:");
            System.out.println("1. Frognerseteren ");
            System.out.println("2. Østerås ");
            System.out.println("3. Kolsås");
            System.out.println("4. Vestli / Bergkrystallen");
            System.out.println("5. Ringen / Sognsvann");

            System.out.print("\nWrite line number for the departure station: (q to quit): ");
            chosenLine = scan.nextLine();
            if(chosenLine.equals("q")){
                return null;
            }

            if(chosenLine.equals("1") || 
            chosenLine.equals("2") ||
            chosenLine.equals("3") ||
            chosenLine.equals("4") ||
            chosenLine.equals("5")
            ){
                loop = false;
            }else{
                System.out.println("\n[INVALID INPUT, TRY AGAIN]");

                try { //make the message appear for a short time 
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e);;
                }
                clearTerminal();
            }
        }
        clearTerminal();
        return chosenLine;
    }


    public void clearTerminal(){//function to clear the terminal to make the terminal more readabl 
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("window")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    
    public static void main(String[] args){
        //Build the tbane 
        TBane tbane = new TBane();
        tbane.readTsvFiles();
        tbane.addEgdes();

        //Find the shortes route
        tbane.dijkstra(tbane.askRoute());
    }
}