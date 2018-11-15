package aiflow;

import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 *
 * @author Derek Wallace
 */
public class FlowFinder {

    public int[][] board;
    public Node[] map; //pick one or other
    public int width;
    public List<Node> Frontier=new ArrayList<>(); //Frontier
    public int counter=0;
    long starttime = System.currentTimeMillis();

    public FlowFinder(int[][] game,Node[] game2, boolean isDummy) throws Exception{
        board=game;
        map=game2;
        width=game[0].length;

        //get initial frontier
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[0].length;j++){
                //Add node to frontier if its a source
                if(board[i][j]>0){
                    int nodeNum=convertArrayNum(i,j); //pass y,x
                    boolean start = true;
                    for(int x = 0; x<Frontier.size();x++)

                        if(map[nodeNum].color == Frontier.get(x).color) {
                            start = false;
                        }
                        if (start) {
                            Frontier.add(map[nodeNum]);
                            map[nodeNum].isSource = true;

                        }
                        else
                        {
                            map[nodeNum].isDest = true;
                        }

                }
            }
        }
        if(isDummy){dummySolution();}
        else smartSolution();
    }

  public void smartSolution() throws Exception{
        
        //Rules:
        // Each square must be connected to two adjacent squares of same color
        // Sources must be connected to one adjacent square
        int lockedT=0;
        while(lockedT<board.length*board[0].length){ //come up with better stop condition
            lockedT=0; //reset count
            for(int i=0;i<map.length;i++){
                checkOptions(map[i]);
                if(map[i].color>0)lockedT++;
            }
            
        }
        System.out.println("Finished");
    }
    
    //Helpers for source
    public void checkOptions(Node current) throws Exception{
        //if on right side
        if(!exists(current.right)){
            //top right corner
            if(!exists(current.up)){
                realCorner(current, 1);
            }
            //bottom right corner
            else if(!exists(current.down)){
                realCorner(current, 3);
            }
            else if(current.color>0)neighbored(current);
        }
        //if on left side
        else if(!exists(current.left)){
            //top left corner
            if(!exists(current.up)){
                realCorner(current,0);
            }
            //bottom left corner
            else if(!exists(current.down)){
                realCorner(current,2);
            }
            else if(current.color>0)neighbored(current);
        }
        else if(current.color>0)neighbored(current);
        
    }
    
    
    /**
    * Corners are dependent so this helps enforce arc consistency
    * Because there is no zigzagging, both sides of a corner will be the same color
    * There is a special case for when sources are a corner
    **/
    public void realCorner(Node current, int type) throws IOException, InterruptedException{
    //type is orientation, 0 topleft, 1 topright, 2 botleft, 3 botright 
        switch(type){
            case 0:
                //if open corner
                if(current.isSource==false){
                    if(current.down.color==0 && current.right.color==0){current.color=15;current.down.color=15; current.right.color=15;}
                    else if (current.down.color>0){current.color=current.down.color;current.right.color=current.down.color;}
                    else {current.color=current.right.color;current.down.color=current.right.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.right.number),current.right.color);
                    colorNode(convertNodeNum(current.down.number),current.down.color);
                }
                break;
            case 1:
                if(current.isSource==false){
                    if(current.down.color==0 && current.left.color==0){current.color=15;current.down.color=15; current.left.color=15;}
                    else if (current.down.color>0){current.color=current.down.color;current.left.color=current.down.color;}
                    else {current.color=current.left.color;current.down.color=current.left.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.left.number),current.left.color);
                    colorNode(convertNodeNum(current.down.number),current.down.color);
                }
                break;
            case 2:
                if(current.isSource==false){
                    if(current.up.color==0 && current.right.color==0){current.color=15;current.up.color=15; current.right.color=15;}
                    else if (current.up.color>0){current.color=current.up.color;current.right.color=current.up.color;}
                    else {current.color=current.right.color;current.up.color=current.right.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.right.number),current.right.color);
                    colorNode(convertNodeNum(current.up.number),current.up.color);
                }
                break;
            case 3:
                if(current.isSource==false){
                    if(current.up.color==0 && current.left.color==0){current.color=15;current.up.color=15; current.left.color=15;}
                    else if (current.up.color>0){current.color=current.up.color;current.left.color=current.up.color;}
                    else {current.color=current.left.color;current.up.color=current.left.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.left.number),current.left.color);
                    colorNode(convertNodeNum(current.up.number),current.up.color);
                }
                break;
            default:
                System.out.println("Unreachable");
                break;
                
        }
    }
    
    /** Detects possible inner corners
     *  A form of consistency
     *  Node current is checked node, type is corner orientation
     */
    public void fakeCorner(Node current, int type){
        
    }
    
    /**
     * Checks solutions for nodes near colors but not cornered
     * Node cur is the node being checked
     */
    public void neighbored(Node cur) throws Exception{
        int freeMoves=0;
        //free moves doesnt work for how neighbored tries
        boolean left=false,right=false,up=false,down=false;
        if(exists(cur.left) && cur.left.color==0){freeMoves++;left=true;}
        if(exists(cur.right) && cur.right.color==0){freeMoves++;right=true;}
        if(exists(cur.up) && cur.up.color==0){freeMoves++;up=true;}
        if(exists(cur.down) && cur.down.color==0){freeMoves++;down=true;}
        
        if(cur.color==0){return;}//take out later
        
        switch(freeMoves){
            case 0:
                //System.out.println("locked in"); 
                break;
            case 1:
                //only one move, check further
                
                //have to check that placing rules arent violated
                if(up && validAssignment(cur)){cur.up.color=cur.color;colorNode(convertNodeNum(cur.up.number),cur.up.color);}
                else if(left && validAssignment(cur)){cur.left.color=cur.color;colorNode(convertNodeNum(cur.left.number),cur.left.color);}
                else if(down && validAssignment(cur)){cur.down.color=cur.color;colorNode(convertNodeNum(cur.down.number),cur.down.color);}
                else if(right && validAssignment(cur)){cur.right.color=cur.color;colorNode(convertNodeNum(cur.right.number),cur.right.color);}
                else{}//Not good
                //colorNode(convertNodeNum(cur.number),cur.color);
                break;
            case 2:
                //possible corner
                break;
            case 3:
                //Not enough info
                break;
            default:
                //no decision can be made yet
                break;
        }
    }
    public void forwardCheck(){
        
    }
    
    public boolean sameColor(Node cur, Node other){
        if (exists(other)){
            if(cur.color==other.color)return true;
        }
        return false;
    }
    public boolean exists(Node questioned){
        if(questioned==null)return false;
        return true;
    }
    public boolean validAssignment(Node cur){
        int connected=0; //keeps track of how many neighbors of same color
        if(sameColor(cur,cur.left))connected++;
        if(sameColor(cur,cur.right))connected++;
        if(sameColor(cur,cur.down))connected++;
        if(sameColor(cur,cur.up))connected++;
        if(cur.isSource){
            //can only have one neighbor of same color if source
            if (connected>0)return false;
        }
        else{
            if (connected>1)return false;
        }
        return true;
    }
    
    public void dummySolution() throws IOException, InterruptedException {

        for(int x = 0; x<Frontier.size();x++)
        {
            System.out.println(Frontier.get(x));
            }
     //depthFirstSearch(Frontier.get(0),0);
        pathCheck(0,true);
        System.out.println(counter);
        long endtime = starttime- System.currentTimeMillis();;
        System.out.println(endtime);
    }

    //for coloring flows
    public void colorNode(int[] coords,int type) throws IOException, InterruptedException{
        FlowDrawer.updateBoard(coords[0], coords[1], type); //turn color
        Thread.sleep(25);
    }

    public int[] convertNodeNum(int nodeNumber){
        int[] coords=new int[2];
        coords[0]=nodeNumber%width;
        coords[1]=nodeNumber/width;
        return coords;
    }
    public int convertArrayNum(int y, int x){
        int coords=0;
        coords=y*width +x;
        return coords;
    }
	//helper function that checks adjacent nodes to see if they are viable options for the current node, and returns the number of viable options
    public int getOptions(Node current, int x)
    {
        int options = 0;

        if(current.up != null && current.up.color ==0 && !current.up.expanded[x]||current.up != null &&current.up.isDest && current.up.color==current.color )
        {
            options ++;
        }
        if(current.left != null && current.left.color ==0&& !current.left.expanded[x]||current.left != null &&current.left.isDest && current.left.color==current.color )
        {
            options ++;
        }
        if(current.right != null && current.right.color ==0&& !current.right.expanded[x]||current.right != null &&current.right.isDest && current.right.color==current.color )
        {
            options++;
        }
        if(current.down != null && current.down.color ==0&& !current.down.expanded[x]||current.down != null &&current.down.isDest && current.down.color==current.color )
        {
            options++;
        }

        return options;
    }

 



        //a helper function to see if any of the adjacent nodes are the final goal
    public boolean goalCheck(Node source, Node next)
    {
        if(source.color == next.color && next.isDest)
        {
            return true;
        }
        else
            return false;
    }
		/* 
		recursive function for searching for the correct path using depth first search.
		*/
    public boolean pathCheck(int x, boolean viable)throws IOException, InterruptedException {
        {

            Node next;
            Node curr = Frontier.get(x);
            curr.expanded[x] = true;
            colorNode(convertNodeNum(curr.number), curr.color);
           // Thread.sleep(500);
            boolean flag = false;
            Stack<Node>  finalFrontier = new Stack();
            finalFrontier.push(curr);
            while (viable || x < Frontier.size()) {


                while(!flag)
                {
                    System.out.println(counter);
                    long endtime =  System.currentTimeMillis()-starttime;
                    System.out.println(endtime);

                    if(getOptions(curr,x)>0)
                    {
                        if(curr.left != null && (curr.left.color ==0 || curr.left.color ==curr.color)&&!curr.left.expanded[x])
                        {
                            if( goalCheck(curr, curr.left))
                            {
                                boolean path;
                                if(x <Frontier.size()-1) {
                                    path = pathCheck(x + 1, true);
                                }
                                else
                                {
                                    path = true;
                                }
                                if(path) {
                                    flag = true;
                                    while (!curr.isSource) {
                                        colorNode(convertNodeNum(curr.number), curr.color);
                                       // Thread.sleep(500);
                                        curr = curr.prev;
                                    }
                                    return true;
                                }
                                else
                                {
                                    while(!finalFrontier.isEmpty())
                                    {
                                       Node temp = finalFrontier.pop();
                                       if(!temp.isSource ||!temp.isDest)
                                       {
                                           temp.color = 0;
                                           counter++;
                                           colorNode(convertNodeNum(temp.number), temp.color);
                                           //Thread.sleep(500);
                                           break;
                                       }
                                    }
                                    finalFrontier.pop();
                                    curr.color = 0;
                                    counter++;
                                    colorNode(convertNodeNum(curr.number), curr.color);
                                    //Thread.sleep(500);
                                    curr  =curr.prev;
                                }
                            }
                            next = curr.left;
                            next.expanded[x] = true;
                            next.color = curr.color;
                            next.prev = curr;
                            finalFrontier.push(next);
                            curr = next;
                            counter++;
                            colorNode(convertNodeNum(curr.number), curr.color);
                            //Thread.sleep(500);
                        }
                        else if(curr.down != null && (curr.down.color ==0 || curr.down.color ==curr.color) &&!curr.down.expanded[x] )
                        {
                            if( goalCheck(curr, curr.down))
                            {
                                boolean path;
                                if(x <Frontier.size()-1) {
                                    path = pathCheck(x + 1, true);
                                }
                                else
                                {
                                    path = true;
                                }
                                if(path) {
                                    flag = true;
                                    while (!curr.isSource) {
                                        colorNode(convertNodeNum(curr.number), curr.color);
                                        //Thread.sleep(500);
                                        curr = curr.prev;
                                    }
                                    return true;
                                }
                                else
                                {
                                    finalFrontier.pop();
                                    curr.color = 0;
                                    colorNode(convertNodeNum(curr.number), curr.color);
                                    counter++;
                                   // Thread.sleep(500);
                                    curr  =curr.prev;
                                    break;
                                }
                            }
                            next = curr.down;
                            next.prev = curr;
                            next.color = curr.color;
                            next.expanded[x] = true;
                            finalFrontier.push(next);
                            curr = next;
                            counter++;
                            colorNode(convertNodeNum(curr.number), curr.color);
                            //Thread.sleep(500);

                        }
                        else if(curr.right != null && (curr.right.color ==0 || curr.right.color ==curr.color) &&!curr.right.expanded[x])
                        {
                            if( goalCheck(curr, curr.right))
                            {
                                boolean path;
                                if(x <Frontier.size()-1) {
                                    path = pathCheck(x + 1, true);
                                }
                                else
                                {
                                    path = true;
                                }
                                if(path) {
                                    flag = true;
                                    while (!curr.isSource) {
                                        colorNode(convertNodeNum(curr.number), curr.color);
                                       // Thread.sleep(500);
                                        curr = curr.prev;
                                    }
                                    return true;
                                }
                                else
                                {
                                    finalFrontier.pop();
                                    curr.color = 0;
                                    colorNode(convertNodeNum(curr.number), curr.color);
                                    //Thread.sleep(500);
                                    counter++;
                                    curr  =curr.prev;
                                    break;
                                }
                            }
                            next = curr.right;
                            next.color = curr.color;
                            next.prev = curr;
                            next.expanded[x] = true;
                            finalFrontier.push(next);
                            curr = next;
                            counter++;
                            colorNode(convertNodeNum(curr.number), curr.color);
                            //Thread.sleep(500);
                        }
                        else if(curr.up != null &&(curr.up.color ==0 || curr.up.color ==curr.color) &&!curr.up.expanded[x] )
                        {
                            if( goalCheck(curr, curr.up))
                            {
                                boolean path;
                                if(x <Frontier.size()-1) {
                                    path = pathCheck(x + 1, true);
                                }
                                else
                                {
                                    path = true;
                                }
                                if(path) {
                                    flag = true;
                                    while (!curr.isSource) {
                                        colorNode(convertNodeNum(curr.number), curr.color);
                                        //Thread.sleep(500);
                                        curr = curr.prev;
                                    }
                                    return true;
                                }
                                else
                                {
                                    finalFrontier.pop();
                                    curr.color = 0;
                                    colorNode(convertNodeNum(curr.number), curr.color);
                                    counter++;
                                    //Thread.sleep(500);
                                    curr =curr.prev;
                                    break;
                                }
                            }
                            next = curr.up;
                            next.prev = curr;
                            next.expanded[x] = true;
                            next.color = curr.color;
                            finalFrontier.push(next);
                            counter++;
                            curr = next;
                            colorNode(convertNodeNum(curr.number), curr.color);
                           // Thread.sleep(500);
                        }
                    }
                    else
                    {
                       if(finalFrontier.peek().isSource )
                       {
                           int size = width*width;
                           for(int y =0; y<size;y++)
                           {
                               if(x==0 && map[y]==curr) {

                               }
                               else {
                                   map[y].expanded[x] = false;
                               }
                           }
                           if(x!=0){return false;}
                           else
                           {
                                   if(curr == curr.prev.left)
                                   {
                                       curr.prev.left=null;
                                   }
                                   else if(curr == curr.prev.down)
                                   {
                                       curr.prev.down=null;
                                   }else if(curr == curr.prev.right)
                                   {
                                       curr.prev.right=null;
                                   }
                                   else if(curr == curr.prev.up)
                                   {
                                       curr.prev.up=null;
                                   }

                                    curr.color = 0;
                                    colorNode(convertNodeNum(curr.number),curr.color);
                                    curr = curr.prev;
                               counter++;
                               System.out.println(counter);
                                endtime = starttime- System.currentTimeMillis();
                               System.out.println(endtime);
                                    curr.expanded[x] = true;



                           }
                       }
                       else{
                        finalFrontier.pop();
                        curr.color = 0;
                           counter++;
                           colorNode(convertNodeNum(curr.number), curr.color);
                           //Thread.sleep(500);
                        curr = curr.prev;
                    }
                    }
                }
            }
            return viable;
        }
    }
}
