public class HeapNode{

    private String vertexString; 
    private String vertexParent; 
    private int[] vertex; //refers to the Numeric vertex on the grid: 0 index is x, 1 index is y
    private double pathDistance; //refers to h(n) : the true cost to travel to this node g(n) + heuristic distance of node to goal h(n)
    private double pathToStart; //refers to g(n)
    private int x; 
    private int y;


    //these nodes should be kept in the open list, generated upon expansion of a node in the graph. 


    public HeapNode(int x, int y, String parent, double startpath){
        this.pathToStart = startpath;
        this.vertexParent = parent; 
        this.vertexString = x + "," + y; 
        this.vertex = new int[2]; 
        vertex[0] = x; 
        vertex[1] = y;
        this.x = x;
        this.y = y; 
        
    }

    public int getX(){
        return this.x; 
    }

    public int getY(){
        return this.y; 
    }
    

    public double getStartDistance(){
        return this.pathToStart;
    }
    public void setStartDistance(double pathToStart){
        this.pathToStart = pathToStart;
    }

    public void setDistance(double distance){
        this.pathDistance = distance; 
    }

    public void setParent(String parent){
        this.vertexParent = parent;
    }

    public double getDistance(){
        return this.pathDistance; 
    }

    public int[] getVertexArray(){
        return this.vertex;  
    }

    public String getVertexString(){
        return this.vertexString;
    }
    
    public String getVertexParent(){
        return this.vertexParent;
    }

}