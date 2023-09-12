import java.util.*;
import java.lang.Math;
import java.io.*;

public class DriverTheta{

    private static MinHeap openList; //nodes that can be expanded to
    private static HashMap<String, HeapNode> closedList; //tracks all visited nodes by their vertex String
    private static int[][] blockedcells; //cell coordinatess that are unusuable
    private static int[][] alreadyexpanded; //coordinates that can be reached by expansion


    public static void main(String args[]){

        String filePath = System.getProperty("user.dir") + File.separator + args[0];
        File inputFile = new File(filePath);

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
                //System.out.println("BLOCKED " + "X " + x + "Y " +y);
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

        String goalplace = goal[0] + "," + goal[1];

        List<String> allowed = new ArrayList<String>();

        while(!openList.isEmpty()){
            current = openList.popMin();


            if(current.getVertexString().equals(goalplace)){
                break;
            }
            closedList.put(current.getVertexString(), current);
            getExpanded(allowed, current, goal, rows, cols);

        }

        
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

    private static void getExpanded(List<String> allowed, HeapNode parent, int[] goal, int rows, int cols ){

            checkPaths(allowed, parent.getVertexArray(), rows, cols);
            int[] expansionVertex = new int[2];
            HeapNode child;


            for(int i =0; i<allowed.size(); i++){
                String[] plusminus = allowed.get(i).split(",");

                expansionVertex[0] = Integer.parseInt(plusminus[0]);
                expansionVertex[1] = Integer.parseInt(plusminus[1]);

             //   System.out.println("EXPANSION VERTEX " + expansionVertex[0] + "," + expansionVertex[1]);
                child = new HeapNode(expansionVertex[0], expansionVertex[1], parent.getVertexString() , parent.getStartDistance() + generateLine(parent.getVertexArray(), expansionVertex));

                if(!closedList.containsKey(child.getVertexString())){
                    if(alreadyexpanded[expansionVertex[1]][expansionVertex[0]] != 1){
                        child.setStartDistance(Double.POSITIVE_INFINITY);
                        child.setParent(null);
                    }

                    updateVertex(parent, child, goal );
                    alreadyexpanded[expansionVertex[1]][expansionVertex[0]] = 1;

                }
            }

        allowed.clear();
        return;

    }

    private static void updateVertex(HeapNode parent, HeapNode child, int[] goal){
        HeapNode grandparent = closedList.get(parent.getVertexParent());
        double triangle;

        if(inLineOfSight(grandparent.getVertexArray(), child.getVertexArray())){
            triangle = grandparent.getStartDistance() + generateLine(child.getVertexArray(), grandparent.getVertexArray());
            if(  triangle < child.getStartDistance()){
                child.setStartDistance( triangle ); //set new g(n)
                child.setParent(grandparent.getVertexString()); //set parent

            // MUST INCLUDE A REMOVE SEQUENCE IN BINARYHEAP TO REMOVE NODES THAT ARE REDUNDANT

            if(alreadyexpanded[child.getY()][child.getX()] == 1){
                     openList.remove(child);
            }
            
            child.setDistance(child.getStartDistance()+generateHeuristic(goal, child.getVertexArray())); //set g(n) + h(n)
            openList.insert(child);
            }
        }else{
            triangle = parent.getStartDistance() + generateLine(child.getVertexArray(), parent.getVertexArray());
            if( triangle  < child.getStartDistance()){
                    child.setStartDistance( triangle );
                    child.setParent(parent.getVertexString());

            if(alreadyexpanded[child.getY()][child.getX()] == 1){
                     openList.remove(child);
            }
           

            child.setDistance(child.getStartDistance() + generateHeuristic(goal ,child.getVertexArray()));
            openList.insert(child);



            }
        }



    }

    private static boolean inLineOfSight(int[] initial, int[] end){

        int initx = initial[0];
        int endx = end[0];
        int inity = initial[1];
        int endy = end[1];

        int f = 0;

        int deltay = endy - inity;
        int deltax = endx - initx;

        int sy = 0;
        int sx = 0;

        if(deltay < 0){ //flip delta to positive
            deltay = deltay *-1;
            sy = -1;
        }else{
            sy = 1;
        }

        if(deltax < 0){
            deltax = deltax * -1;
            sx = -1;
        }else{
            sx = 1;
        }

        if(deltax >= deltay){ // if x is longer than y difference
            while(initx != endx){ //while they do not match

                f = f + deltay;

                if(f >= deltax){
                    if( gridblock(initx + ((sx - 1)/2), inity + ((sy - 1)/2) )){
                        return false;

                    }

                    inity += sy;
                    f -= deltax;
                }
                if(f != 0 && gridblock(initx + ((sx - 1)/2), inity + ((sy-1)/2))  ){
                    return false;
                }
                if(deltay == 0 && gridblock(initx + ((sx - 1)/2), inity) && gridblock(initx + ((sx - 1)/2), inity-1) ){
                    return false;
                }

                initx += sx;
            }
        }else{

            while(inity != endy){
                 f = f + deltax;

                 if(f >= deltay){
                    if( gridblock( initx + ((sx - 1)/2) , inity + ((sy - 1)/2))){
                        return false;
                    }
                    initx += sx;
                    f -= deltay;
                 }
                 if(f != 0 &&  gridblock( initx + ((sx - 1)/2) , inity + ((sy - 1)/2)) ){
                    return false;

                 }
                 if(deltax == 0 && gridblock(initx, inity + ((sy - 1)/2) ) && gridblock(initx-1, inity + ((sy - 1)/2) )  ){
                    return false;
                 }
                 inity += sy;

            }


        }

        return true;



    }


    private static void checkPaths(List<String> allowed, int[] vertex, int rows, int cols){ //checks all 8 surrounding positions for openlist consideration upon expansion

        int x = vertex[0];
        int y = vertex[1];

        int top = 0;
        int bottom = 0;
        int right = 0;
        int left = 0;

        int tempy = 0;
        int tempx = 0;

        if(x-1 < 0 ){
            left = 1;
        }
        if(x + 1 > cols){
            right = 1;
        }
        if(y+1 > rows){
            bottom = 1;
        }
        if(y-1 < 0){
            top = 1;
        }

       // System.out.println("X FOR PATH " + x + " Y FOR PATH "+ y);

        if(top != 1 && right != 1){
            if( blockedcells[y-1][x] != 1){ //topright
                tempx = x + 1;
                tempy = y - 1;

                allowed.add(tempx + "," + tempy);
                }
        }
        if(top != 1 && left != 1){
             if( blockedcells[y-1][x-1] != 1){ //topleft

                tempx = x-1;
                tempy = y-1;
                allowed.add(tempx + "," + tempy);
            }
        }
        if(top != 1){
            if( blockedcells[y-1][x-1] != 1 || blockedcells[y-1][x] != 1 ){ //top

                tempx = x;
                tempy = y-1;
                allowed.add( tempx + "," + tempy);
            }
        }
        if(right!= 1 ){
            if( blockedcells[y-1][x] != 1 || blockedcells[y][x] != 1){ //right
                tempx = x + 1;
                tempy = y;
                allowed.add(tempx + "," + tempy);
            }
        }
        if(left != 1){
            if( blockedcells[y-1][x-1] != 1 || blockedcells[y][x-1] != 1){//left
                tempx = x-1;
                tempy = y;
                allowed.add(tempx + "," + tempy) ;
            }
        }
        if(left != 1 && bottom != 1){

            if( blockedcells[y][x-1] != 1){//bottomleft
                tempx = x-1;
                tempy = y+1;
                allowed.add(tempx + "," + tempy);
            }
        }
        if(right != 1 && bottom != 1){
            if( blockedcells[y][x] != 1){ //bottomright
                tempx = x+1;
                tempy = y+1;
                allowed.add(tempx + "," + tempy);
            }
        }
        if(bottom != 1){
           if( blockedcells[y][x] != 1 || blockedcells[y][x-1] != 1){ //bottom
                tempx = x;
                tempy = y+1;
                allowed.add(tempx + "," + tempy);
            }
        }

    }

    private static boolean gridblock(int x, int y){
        
        if(blockedcells[y][x] == 1){
            return true;
        }
        return false;
    }

    private static void blockInit(int rows, int cols){

        for(int i = 0; i<blockedcells.length; i++){ //blocks left side
            blockedcells[i][0] = 1; //blocks left
            blockedcells[i][cols] = 1; //blocks right
            blockedcells[0][i] = 1; //blocks top
            blockedcells[rows][i] = 1; //blocks bottom
        }

    }

    private static double generateHeuristic(int[] goal, int[] coord){ // create straight line h(n) to create f(n), call prior to insertion
      int Sx = coord[0];
      int Sy = coord[1];
      int Gx = goal[0];
      int Gy = goal[1];
      return Math.hypot(Sx - Gx, Sy - Gy);

    }

    private static double generateLine(int[] coord, int[] goal){

        int x = coord[0];
        int y = coord[1];
        return Math.hypot(goal[0] - x, goal[1] - y);

    }

     private static double generateDistAdded(int[] start, int[] coord){ //A* must follow either sqrt(2) or 1 plus.

        int diffx = coord[0] - start[0];
        int diffy = coord[1] - start[1];
        if( Math.abs(diffx) == 1 && Math.abs(diffy) == 1){ // at the diagonals the distance of traveling
            return Math.sqrt(2);
        }
        return 1;

    }

}
