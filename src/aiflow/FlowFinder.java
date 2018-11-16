package aiflow;

import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 *
 * @author Derek Wallace and Dillon Monday
 */
public class FlowFinder {

    public int[][] board;
    public Node[] map; //pick one or other
    public int width;
    public List<Node> Frontier=new ArrayList<>(); //Frontier
    public int counter=0;
    public int altColor=-1;
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
        
        int lockedT=0;
        while(lockedT<board.length*board[0].length){ //come up with better stop condition
            lockedT=0; //reset count
            for(int i=0;i<map.length;i++){
                //colorNode(convertNodeNum(map[i].number),3);
                //Thread.sleep(200);
                checkOptions(map[i]);
                if(map[i].color!=0)lockedT++;
                //colorNode(convertNodeNum(map[i].number),map[i].color);
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
            else if(current.color!=0)neighbored(current);
            else if(current.color==0)fakeCorner(current);
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
            else if(current.color!=0)neighbored(current);
            else if(current.color==0)fakeCorner(current);
        }
        else if(current.color!=0)neighbored(current);
        else if(current.color==0)fakeCorner(current);
        
    }
    
    
    /**
    * Corners are dependent so this helps enforce arc consistency
    * Because there is no zigzagging, both sides of a corner will be the same color
    * There is a special case for when sources are a corner
    **/
    public void realCorner(Node current, int type) throws  Exception{
    //type is orientation, 0 topleft, 1 topright, 2 botleft, 3 botright 
        switch(type){
            case 0:
                //if open corner
                if(current.isSource==false){
                    if(current.down.color==0 && current.right.color==0){current.color=altColor;current.down.color=altColor; current.right.color=altColor;altColor--;}
                    else if (current.down.color!=0){current.color=current.down.color;current.right.color=current.down.color;}
                    else {current.color=current.right.color;current.down.color=current.right.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.right.number),current.right.color);
                    colorNode(convertNodeNum(current.down.number),current.down.color);
                }
                else{neighbored(current);}
                break;
            case 1:
                if(current.isSource==false){
                    if(current.down.color==0 && current.left.color==0){current.color=altColor;current.down.color=altColor; current.left.color=altColor;altColor--;}
                    else if (current.down.color!=0){current.color=current.down.color;current.left.color=current.down.color;}
                    else {current.color=current.left.color;current.down.color=current.left.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.left.number),current.left.color);
                    colorNode(convertNodeNum(current.down.number),current.down.color);
                }
                else{neighbored(current);}
                break;
            case 2:
                if(current.isSource==false){
                    if(current.up.color==0 && current.right.color==0){current.color=altColor;current.up.color=altColor; current.right.color=altColor;altColor--;}
                    else if (current.up.color!=0){current.color=current.up.color;current.right.color=current.up.color;}
                    else {current.color=current.right.color;current.up.color=current.right.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.right.number),current.right.color);
                    colorNode(convertNodeNum(current.up.number),current.up.color);
                }
                else{neighbored(current);}
                break;
            case 3:
                if(current.isSource==false){
                    if(current.up.color==0 && current.left.color==0){current.color=altColor;current.up.color=altColor; current.left.color=altColor;altColor--;}
                    else if (current.up.color!=0){current.color=current.up.color;current.left.color=current.up.color;}
                    else {current.color=current.left.color;current.up.color=current.left.color;}
                    colorNode(convertNodeNum(current.number),current.color);
                    colorNode(convertNodeNum(current.left.number),current.left.color);
                    colorNode(convertNodeNum(current.up.number),current.up.color);
                }
                else{neighbored(current);}
                break;
            default:
                System.out.println("Unreachable");
                break;
                
        }
    }
    
    /** Detects possible inner corners
     *  A form of consistency
     * The inner corners are noticeable by having two adjacent sides be satisfied 
     * @param current is  possible inner corner
     * @throws java.io.IOException for drawing call
     * @throws java.lang.InterruptedException for sleep delay
     */
    public void fakeCorner(Node current) throws IOException, InterruptedException{
        if(satisfiedAssignment(current.left) && satisfiedAssignment(current.up)){
            if(!exists(current.down) && current.down.color!=0 && !exists(current.right) && current.right.color!=0){
                if(current.down.color==0 && current.right.color==0){current.color=altColor;current.down.color=altColor; current.right.color=altColor;altColor--;}
                else if (current.down.color!=0){current.color=current.down.color;current.right.color=current.down.color;}
                else {current.color=current.right.color;current.down.color=current.right.color;}
                colorNode(convertNodeNum(current.number),current.color);
                colorNode(convertNodeNum(current.right.number),current.right.color);
                colorNode(convertNodeNum(current.down.number),current.down.color);
            }
        }
        else if(satisfiedAssignment(current.left) && satisfiedAssignment(current.down)){
            if(current.up.color==0 && current.right.color==0){current.color=altColor;current.up.color=altColor; current.right.color=altColor;altColor--;}
            else if (current.up.color!=0){current.color=current.up.color;current.right.color=current.up.color;}
            else {current.color=current.right.color;current.up.color=current.right.color;}
            colorNode(convertNodeNum(current.number),current.color);
            colorNode(convertNodeNum(current.right.number),current.right.color);
            colorNode(convertNodeNum(current.up.number),current.up.color);       
        }
        else if(satisfiedAssignment(current.right) && satisfiedAssignment(current.up)){ 
            if(current.down.color==0 && current.left.color==0){current.color=altColor;current.down.color=altColor; current.left.color=altColor;altColor--;}
            else if (current.down.color!=0){current.color=current.down.color;current.left.color=current.down.color;}
            else {current.color=current.left.color;current.down.color=current.left.color;}
            colorNode(convertNodeNum(current.number),current.color);
            colorNode(convertNodeNum(current.left.number),current.left.color);
            colorNode(convertNodeNum(current.down.number),current.down.color);   
        }
        else if(satisfiedAssignment(current.right) && satisfiedAssignment(current.down)){
            if(current.up.color==0 && current.left.color==0){current.color=altColor;current.up.color=altColor; current.left.color=altColor;altColor--;}
            else if (current.up.color!=0){current.color=current.up.color;current.left.color=current.up.color;}
            else {current.color=current.left.color;current.up.color=current.left.color;}
            colorNode(convertNodeNum(current.number),current.color);
            colorNode(convertNodeNum(current.left.number),current.left.color);
            colorNode(convertNodeNum(current.up.number),current.up.color);
        }
    }
    
    /**
     * Checks solutions for nodes near colors but not cornered
     * Will only make a decision if their is only one possible option or enforcing merging of dependencies
     * @param cur is the colored node checking its available moves
     */
    public void neighbored(Node cur) throws Exception{
        int freeMoves=0;
        //free moves doesnt work for how neighbored tries
        boolean left=false,right=false,up=false,down=false;
        boolean leftM=false,rightM=false,upM=false,downM=false;
        //can't discount grey paths, 
        if(exists(cur.left) && (cur.left.color<=0 || (cur.color<0 && cur.left.color>0 && !satisfiedAssignment(cur.left)))){
            //undecided dependencies are <0, this prevents seeing opening at colored dependency
            if(!sameColor(cur,cur.left)){
                //if the move is also valid connection wise
                if(newValidAssignment(cur,cur.left))
                    freeMoves++;left=true;
            }
        }
        if(exists(cur.right) && (cur.right.color<=0 || (cur.color<0 && cur.right.color>0 && !satisfiedAssignment(cur.right)))){
            if(!sameColor(cur,cur.right)){
                if(newValidAssignment(cur,cur.right))
                    freeMoves++;right=true;
            }
        }
        if(exists(cur.up) && (cur.up.color<=0 || (cur.color<0 && cur.up.color>0 && !satisfiedAssignment(cur.up)))){
            if(!sameColor(cur,cur.up)){
                if(newValidAssignment(cur,cur.up))
                    freeMoves++;up=true;
            }
        }
        if(exists(cur.down) && (cur.down.color<=0 || (cur.color<0 && cur.down.color>0 && !satisfiedAssignment(cur.down)))){
            if(!sameColor(cur,cur.down)){
                if(newValidAssignment(cur,cur.down))
                    freeMoves++;down=true;
            }
        }
        
        if(cur.color==0){return;}//take out later
        
        switch(freeMoves){
            //don't merge middles of dependencies
            case 0:
                //if dependency is blocked in, merge
                int open=0;
                if(validAssignment(cur)){
                    if(cur.color<0){
                        if(exists(cur.left) && cur.left.color!=0){
                            if(validAssignment(cur.left) && newValidAssignment(cur,cur.left)){
                                open++; leftM=true;
                            }
                        }
                        if(exists(cur.up) && cur.up.color!=0){
                            if(validAssignment(cur.up)&& newValidAssignment(cur,cur.up)){
                                open++; upM=true;
                            }
                        }
                        if(exists(cur.right) && cur.right.color!=0){
                            if(validAssignment(cur.right)&& newValidAssignment(cur,cur.right)){
                                open++; rightM=true;
                            }
                        }

                        if(exists(cur.down) && cur.down.color!=0){
                            if(validAssignment(cur.down)&& newValidAssignment(cur,cur.down)){
                                open++; downM=true;
                            }
                        }
                    }
                }
                switch(open){
                    case 1:
                        if(leftM)mergeDependencies(Math.max(cur.color,cur.left.color),Math.min(cur.color, cur.left.color));
                        if(upM)mergeDependencies(Math.max(cur.color,cur.up.color),Math.min(cur.color, cur.up.color));
                        if(rightM)mergeDependencies(Math.max(cur.color,cur.right.color),Math.min(cur.color, cur.right.color));
                        if(downM)mergeDependencies(Math.max(cur.color,cur.down.color),Math.min(cur.color, cur.down.color));
                        break;
                    default:
                        //not enough info
                        break;
                }
                break;
            case 1:
                //only one move, check further
                
                //have to check that placing rules arent violated
                if(up && validAssignment(cur) && newValidAssignment(cur,cur.up)){
                    if(cur.color<0 && cur.up.color>0)mergeDependencies(Math.max(cur.color,cur.up.color),Math.min(cur.color, cur.up.color));
                    cur.up.color=cur.color;colorNode(convertNodeNum(cur.up.number),cur.up.color);
                }
                else if(left && validAssignment(cur) && newValidAssignment(cur,cur.left)){
                    if(cur.color<0 && cur.left.color>0)mergeDependencies(Math.max(cur.color,cur.left.color),Math.min(cur.color, cur.left.color));
                    cur.left.color=cur.color;colorNode(convertNodeNum(cur.left.number),cur.left.color);
                }
                else if(down && validAssignment(cur) && newValidAssignment(cur,cur.down)){
                    if(cur.color<0 && cur.down.color>0)mergeDependencies(Math.max(cur.color,cur.down.color),Math.min(cur.color, cur.down.color));
                    cur.down.color=cur.color;colorNode(convertNodeNum(cur.down.number),cur.down.color);
                }
                else if(right && validAssignment(cur)  && newValidAssignment(cur,cur.right)){
                    if(cur.color<0 && cur.right.color>0)mergeDependencies(Math.max(cur.color,cur.right.color),Math.min(cur.color, cur.right.color));
                    cur.right.color=cur.color;colorNode(convertNodeNum(cur.right.number),cur.right.color);
                }
                else{}//Not good
                
                break;
            case 2:
                //possible corner
//                colorNode(convertNodeNum(cur.number),3);
//                if(left)colorNode(convertNodeNum(cur.left.number),3);
//                if(right)colorNode(convertNodeNum(cur.right.number),3);
//                if(down)colorNode(convertNodeNum(cur.down.number),3);
//                if(up)colorNode(convertNodeNum(cur.up.number),3);
//                if(left)colorNode(convertNodeNum(cur.left.number),cur.left.color);
//                if(right)colorNode(convertNodeNum(cur.right.number),cur.right.color);
//                if(down)colorNode(convertNodeNum(cur.down.number),cur.down.color);
//                if(up)colorNode(convertNodeNum(cur.up.number),cur.up.color);
//                colorNode(convertNodeNum(cur.number),cur.color);
                
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
    
    //follow a path of the merging color and add all those nodes to list
    //change all the listed nodes to new color, then recolor
    public void mergeDependencies(int altColor,int merging) throws IOException, InterruptedException{
        //List<Node> mergeNodes=new ArrayList<>();
        for(int i=0;i<map.length;i++){
            if(map[i].color==merging){
                map[i].color=altColor;
                colorNode(convertNodeNum(map[i].number),map[i].color);
            }
        }
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
    public boolean satisfiedAssignment(Node cur){
        int connected=0;
        if(!exists(cur) || cur.color==0)return false; //assume dependencies are satisfied for now
        if(sameColor(cur,cur.left))connected++;
        if(sameColor(cur,cur.right))connected++;
        if(sameColor(cur,cur.down))connected++;
        if(sameColor(cur,cur.up))connected++;
        return (cur.isSource && connected==1) || (!cur.isSource && connected==2);
    }
    public boolean newValidAssignment(Node cur, Node neo){
        //check how many times the color connects to new node, prevents zigzag
        if(neo.color<0){    //check if valid if neo merged dependencies wit cur
            if((sameColor(cur,neo.up)||sameColor(neo,neo.up)) && (sameColor(cur,neo.left)||sameColor(neo,neo.left)) && (sameColor(cur,neo.up.left)||sameColor(neo,neo.up.left))){return false;}
            if((sameColor(cur,neo.up)||sameColor(neo,neo.up)) && (sameColor(cur,neo.right)||sameColor(neo,neo.right)) && (sameColor(cur,neo.up.right)||sameColor(neo,neo.up.right))){return false;}
            if((sameColor(cur,neo.down)||sameColor(neo,neo.down)) && (sameColor(cur,neo.left)||sameColor(neo,neo.left)) && (sameColor(cur,neo.down.left)||sameColor(neo,neo.down.left))){return false;}
            if((sameColor(cur,neo.down)||sameColor(neo,neo.down)) && (sameColor(cur,neo.right)||sameColor(neo,neo.right)) && (sameColor(cur,neo.down.right)||sameColor(neo,neo.down.right))){return false;}
            if(cur.color<0 && neo.color>0)return newValidAssignment(neo,cur);
        }
        
        if(sameColor(cur,neo.up) && sameColor(cur,neo.left) && sameColor(cur,neo.up.left)){return false;}
        if(sameColor(cur,neo.up) && sameColor(cur,neo.right) && sameColor(cur,neo.up.right)){return false;}
        if(sameColor(cur,neo.down) && sameColor(cur,neo.left) && sameColor(cur,neo.down.left)){return false;}
        if(sameColor(cur,neo.down) && sameColor(cur,neo.right) && sameColor(cur,neo.down.right)){return false;}

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
