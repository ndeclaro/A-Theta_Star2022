import java.util.*;
import java.lang.Math;
import java.io.*;

public class DriverA{

    private static MinHeap openList; //nodes that can be expanded to
    private static HashMap<String, HeapNode> closedList; //tracks all visited nodes by their vertex String
    private static int[][] blockedcells; //cell coordinatess that are unusuable
    private static int[][] alreadyexpanded; //coordinates that can be reached by expansion
    //does not account for obstacles

    public static void main(String args[])
    {

        //Read in blocked list, rows/cols, and start/goal vertex from text file input arg

        String filePath = System.getProperty("user.dir") + File.separator + args[0];
        File inputFile = new File(filePath);
       // System.out.println(args[0])
       // System.out.println(System.getProperty("user.dir"));
        int[] start = new int[2];
        int[] goal = new int[2];
        int cols = 0;
        int rows = 0;

        try {

        Scanner readFile = new Scanner(inputFile);
        //start x y
        start[0] = readFile.nextInt();
        start[1] = readFile.nextInt();
        readFile.nextLine();
        //goal x y
        goal[0] = readFile.nextInt();
        goal[1] = readFile.nextInt();
        readFile.nextLine();
        //cols and rows
        cols = readFile.nextInt();
        rows = readFile.nextInt();
        readFile.nextLine();

        blockedcells = new int[rows+1][cols+1];
        alreadyexpanded = new int[rows+1][cols+1];

        while(readFile.hasNext() ){

            int x = readFile.nextInt();
            int y = readFile.nextInt();
            int blocked = readFile.nextInt();

            if(blocked == 1){
                blockedcells[y][x] = 1;
            }
            if(readFile.hasNext()) {
                readFile.nextLine();
            }

        }
            //System.out.print("Wonk");
        }catch(Exception e){
            System.out.println("File Not Found");
            e.printStackTrace();
        }

        blockInit(rows,cols); //blocks out the edge blocks

        closedList = new HashMap<String, HeapNode>();
        openList = new MinHeap();
        String startplace = start[0] + "," + start[1];
        HeapNode current = new HeapNode(start[0], start[1], startplace, 0);
            current.setDistance(generateHeuristic(goal, start));
        openList.insert(current);
        int[] currentVertex;
        int[] expansionVertex = new int[2];
        ArrayList<String> allowed = new ArrayList<String>();
        String goalplace = goal[0] + "," + goal[1];

        while(!openList.isEmpty()){ //while the current node string is not the goalstring, keep going

            current = openList.popMin(); //set current node to pop from it
            currentVertex = current.getVertexArray();
            closedList.put(current.getVertexString(), current);

            if(current.getVertexString().equals(goalplace)){
                break;
            }

            System.out.println("CURRENT POPPED MIN ROTATION" + current.getVertexString() + " VALUE " + current.getDistance());
            System.out.println("ARRAY " + current.getVertexArray()[0] + " " +current.getVertexArray()[1] );

            checkPaths(allowed, current.getVertexArray(), rows, cols);

            while(!allowed.isEmpty()){ //add potential expansions to openlist

                String[] newvertex = allowed.get(0).split(",");

                expansionVertex[0] = Integer.parseInt(newvertex[0]);
                expansionVertex[1] = Integer.parseInt(newvertex[1]);

                System.out.println("BEING REMOVED " + allowed.get(0));
                allowed.remove(0);
                String expansionVertexString = expansionVertex[0] + "," + expansionVertex[1];

                if(closedList.get(expansionVertexString) != null){ // if the node has been visited, do not add it to open
                   System.out.println("CLOSED LIST HAS " + expansionVertexString);
                    continue;
                }
                if(alreadyexpanded[expansionVertex[1]][expansionVertex[0]] == 1){
                    System.out.println("expanded list has " + expansionVertexString);
                    continue;
                }


                double hn = generateHeuristic(goal , expansionVertex);
                addPotentialExpansion(expansionVertex, current, hn); //this is an issue, as it will add a bunch of redundancies
                //per movement as it doesn't yet filter for those already in the binheap, but not yet expanded.

            }

        }

        System.out.println("SIZE OF CLOSED LIST " + closedList.size() );
        System.out.println("SIZE OF OPEN LIST " + openList.size());

        boolean flagPath = true;
        if(!current.getVertexString().equals(goalplace)){
            flagPath = false;
            System.out.println("NO VALID PATH");
            
        }

        try {

            File output = new File(args[1]);
            if (output.createNewFile()) {
                System.out.println("File created: " + output.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter pather = new FileWriter(output.getName());

            if(flagPath){
                while(!current.getVertexString().equals(startplace)){

                pather.write( current.getVertexString() + System.getProperty("line.separator"));
                current = closedList.get(current.getVertexParent());

                }  

                 pather.write( current.getVertexString() + System.getProperty("line.separator")); 
            }else{
                pather.write("NO VALID PATH");

            }

            pather.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return;
    }

    private static void addPotentialExpansion(int[] vertex, HeapNode parent, double heuristic){

        alreadyexpanded[vertex[1]][vertex[0]] = 1;
        HeapNode temp = new HeapNode(
            vertex[0],
            vertex[1],
            parent.getVertexString(),
            parent.getStartDistance() + generateDistAdded(parent.getVertexArray(),vertex));
        temp.setDistance( temp.getStartDistance() +  heuristic);

        openList.insert(temp);
        System.out.println("INSERTED : " + temp.getVertexString() + " WITH VALUE " + temp.getDistance());

    }

    private static void blockInit(int rows, int cols){

        for(int i = 0; i<blockedcells.length; i++){ //blocks left side
            blockedcells[i][0] = 1; //blocks left
            blockedcells[i][cols] = 1; //blocks right
            blockedcells[0][i] = 1; //blocks top
            blockedcells[rows][i] = 1; //blocks bottom
        }

    }

   private static void checkPaths(List<String> allowed, int[] vertex, int rows, int cols){ //checks all 8 surrounding positions for openlist consideration upon expansion

        int x = vertex[0];
        int y = vertex[1];

        int tempy = 0;
        int tempx = 0;

        //true if valid

        boolean right = x+1 <= cols; 
        boolean left = x-1 > 0;
        boolean top = y-1 > 0; 
        boolean bottom = y+1 <= rows;

       // System.out.println("X FOR PATH " + x + " Y FOR PATH "+ y);

    
            if( !gridblock(x, y-1, rows, cols) && right && top){ //topright
                
                
                tempx = x + 1;
                tempy = y - 1;

                
                
                allowed.add(tempx + "," + tempy);
            }
        
       
             if( !gridblock(x-1, y-1, rows, cols) && left && top ){ //topleft

                tempx = x-1;
                tempy = y-1;
                allowed.add(tempx + "," + tempy);
            }
        
            if( ( !gridblock(x-1, y-1, rows, cols) || !gridblock(x, y-1, rows,cols) ) && top){ //top

                tempx = x;
                tempy = y-1;
                allowed.add( tempx + "," + tempy);
            }
        
        
            if( (!gridblock(x, y-1, rows, cols) || !gridblock(x, y, rows, cols)) && right ) { //right
                tempx = x + 1;
                tempy = y;
                allowed.add(tempx + "," + tempy);
            }
        
        
            if( (!gridblock(x-1, y-1, rows,cols) || !gridblock(x-1, y, rows, cols)) && left){//left
                tempx = x-1;
                tempy = y;
                allowed.add(tempx + "," + tempy) ;
            }
        
       

            if( !gridblock(x-1, y, rows, cols) && bottom && left){//bottomleft
                tempx = x-1;
                tempy = y+1;
                allowed.add(tempx + "," + tempy);
            }
         
            if( !gridblock(x, y, rows, cols) && bottom && right ){ //bottomright
                tempx = x+1;
                tempy = y+1;
                allowed.add(tempx + "," + tempy);
            }
        
     
           if(( !gridblock(x, y, rows, cols) || gridblock(x-1, y, rows, cols)) && bottom){ //bottom
                tempx = x;
                tempy = y+1;
                allowed.add(tempx + "," + tempy);
            }
    

    }


    private static boolean gridblock(int x, int y, int rows, int cols){
    /*     if(x < 0 ){
            return true;
        }
        if(x > cols){
            return true;
        }
        if(y > rows){
            return true;
        }
        if(y < 0){
            return true;
        }
    */

        if(blockedcells[y][x] == 1){
            return true;
        }
        return false;
    }

    private static double generateDistAdded(int[] start, int[] coord){ //A* must follow either sqrt(2) or 1 plus.

        int diffx = coord[0] - start[0];
        int diffy = coord[1] - start[1];
        if( Math.abs(diffx) == 1 && Math.abs(diffy) == 1){ // at the diagonals the distance of traveling
            return Math.sqrt(2);
        }
        return 1;

    }

    private static double generateHeuristic(int[] goal, int[] coord){ // create straight line h(n) to create f(n), call prior to insertion

        int Sx = coord[0];
        int Sy = coord[1];
        int Gx = goal[0];
        int Gy = goal[1];
        int xdiff = Math.abs(Sx - Gx);
        int ydiff = Math.abs(Sy - Gy);
        int minimum = Math.min(xdiff, ydiff);
        int maximum = Math.max(xdiff, ydiff);
        double hvalue = Math.sqrt(2) * minimum + maximum - minimum;
        return hvalue;

    }



}
