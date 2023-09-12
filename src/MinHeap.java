
public class MinHeap{

    private HeapNode[] heap; 
    private int size;
    private int capacity; 
    private static final int INITIAL_CAPACITY = 64; 

    public MinHeap(){
        this.heap = new HeapNode[INITIAL_CAPACITY]; 
        this.size = 0; 
        this.capacity = INITIAL_CAPACITY;
    }

    public int size(){
        return this.size;
    }

    private void resize(){
        capacity *= 2; 
        HeapNode[] temp = new HeapNode[capacity];
       
        for(int i = 0; i<size; i++){
            temp[i] = heap[i];
        }

        heap = temp; 

    }

    static private int parent(int pos){
        return (pos - 1) / 2; 
    }

    static private int leftChild(int pos){
        return pos * 2 + 1; 
    }

    static private int rightChild(int pos){
        return (pos + 1) * 2; 
    }

    private HeapNode getParent(int pos){
        return this.heap[parent(pos)]; 
    }

    private HeapNode getLeftChild(int pos) {
        try {
            return this.heap[ leftChild(pos)];
        } catch (Exception e) {
            return null; 
        }
    }

    private HeapNode getRightChild(int pos) {
        try {
            return this.heap[rightChild(pos)];
        } catch (Exception e) {
            return null; 
        }
    }
 
    public void insert(HeapNode node){

        
        int delta = this.size; 

        if(this.size == this.capacity){
            resize();
        }
        this.heap[this.size] = node; 
        this.size++;

        int parent = parent(delta); 
        

        while( delta > 0 && getParent(delta).getDistance() > this.heap[delta].getDistance()){ //swap up the tree to preserve min heap status

                HeapNode temp = this.heap[delta]; 
                this.heap[delta] = getParent(delta);
                this.heap[parent] = temp;
                delta = parent; 
                parent = parent(delta);

        }
    }

    public void print(){

        for(int i = 0; i< this.size ; i++ ){
              
              int[] parent = this.heap[i].getVertexArray();
            System.out.print( " PARENT : " + parent[0] + " , " + parent[1]   ); 

            System.out.print( " VALUE: " + this.heap[i].getDistance());


              if(leftChild(i) < this.size ){
                int[] leftchild =  this.heap[leftChild(i)].getVertexArray(); 
                System.out.print( " LEFT CHILD : " + leftchild[0] + " , " + leftchild[1]   ); 
              }
              
              if(rightChild(i) < this.size ){
                int[] rightchild =   this.heap[rightChild(i)].getVertexArray();
                System.out.print( " RIGHT CHILD : " + rightchild[0] + " , " + rightchild[1]   ); 
              }
    

                System.out.println(); 
        }


    }

    public HeapNode peek(){
        return this.heap[0];
    }

    public void remove(HeapNode h){

        for(int i = 0; i<this.size; i++){

            HeapNode current = heap[i];
            if(current.getVertexString().equals(h.getVertexString())){
                
                heap[i] = heap[size-1];
                heap[size-1] = null;
                this.size--;


                heapify(i);
                return;
            }

        }
        return; 
    }

    public boolean isEmpty(){
        if(this.size == 0){
            return true;
        }

        return false; 
    }

    private void heapify(int pos){

        int sizeh = this.size;

        if(sizeh > 0){
                int left = leftChild(pos);
                int right = rightChild(pos);
                int min; 

                if(left < sizeh && heap[pos].getDistance() > heap[left].getDistance()){
                    min = left; 
                }else{
                    min = pos; 
                }
                if(right < sizeh && heap[min].getDistance() > heap[right].getDistance()){
                    min = right; 
                }
                if(min != pos){

                    HeapNode temp = heap[pos];
                    heap[pos] = heap[min];
                    heap[min] = temp;
            
                    heapify(min);
                }

        }
    }

    public HeapNode popMin(){

        if(this.size == 0){
            return null; 
        }

        HeapNode temp = this.heap[0];
        this.heap[0] = this.heap[size-1];
        this.heap[size-1] = null;
        this.size--;

        heapify(0);

        return temp; 

    }
     
    public static void main(String[] args){
        MinHeap open = new MinHeap(); 
        HeapNode current = null;
        for(int i = 1; i<50; i++ ){
            current = new HeapNode(i,1, "0,0", 0);
            current.setDistance(100-i);
            open.insert(current );

            open.print();
            System.out.println("NEW ITERATION");
        }
       
       HeapNode temp = open.popMin(); 
        System.out.println("MIN DISTANCE : " + temp.getDistance());
        temp = open.popMin();
        
        System.out.println("MIN DISTANCE : " + temp.getDistance());
        open.print();
        
        return; 
    }

}