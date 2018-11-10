package aiflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Derek Wallace
 */
public class FlowFinder {
 
    public int[][] board;
    public int locked=0;
    public Node[] map; //pick one or other
    public int width;
    public List<Node> Frontier=new ArrayList<>(); //Frontier
    public int extraColors=15;
    
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
                    Frontier.add(map[nodeNum]);
                    locked++;
                }
            }
        }
        if(isDummy){dummySolution();}
        else smartSolution();
    }
    
    public void smartSolution() throws Exception{
        // cycle through a frontier with current expanding nodes?
        // make arc consistent by removing color option for paths not taken
        // forward check: if no option for color, backtrack one round with restrictions?
        // If only one valid choice, make the choice
        
        //idea 2:
        // count all open paths on current node
        // check which open is closer by manhattan distance
        // connect prev and next
        // cycle to next frontier node
        // if no open path, backtrack with some heuristic? Last round?
        
        //SAT problem
        // if not source, has 2 neighbors of same color
        // Each color must be in a single path
        //PLACING RULES
        // Possible if color is connected
        // Cannot replace colors
        // Pick the path with smaller manhattan(?)
        
        //Rules:
        // Each square must be connected to two adjacent squares of same color
        // Sources must be connected to one adjacent square
        while(locked<board.length*board[0].length){
            for(int i=0;i<map.length;i++){
                checkOptions(map[i]);
            }
        }
        
    }
    
    //Helpers for source
    public void checkOptions(Node current) throws Exception{
        //if on right side
        if(current.right==null){
            //top right corner
            if(current.up==null){
                realCorner(current, 1);
            }
            //bottom right corner
            else if(current.down==null){
                realCorner(current, 3);
            }
            else if(current.down.color>0 || current.up.color>0 || current.left.color>0){neighbored(current);}
        }
        //if on left side
        else if(current.left==null){
            //top left corner
            if(current.up==null){
                realCorner(current,0);
            }
            //bottom left corner
            else if(current.down==null){
                realCorner(current,2);
            }
            else if(current.down.color>0 || current.up.color>0 || current.right.color>0){neighbored(current);}
        }
        //if on top, corners already checked
        else if(current.up==null){
            if(current.down.color>0 || current.right.color>0 || current.left.color>0){neighbored(current);}
        }
        //if on bottom
        else if(current.down==null){
            if(current.up.color>0 || current.right.color>0 || current.left.color>0){neighbored(current);}
        }
        
        
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
        boolean left=false,right=false,up=false,down=false;
        if(cur.left!=null && cur.left.color==0){freeMoves++;left=true;}
        if(cur.right!=null && cur.right.color==0){freeMoves++;right=true;}
        if(cur.up!=null && cur.up.color==0){freeMoves++;up=true;}
        if(cur.down!=null && cur.down.color==0){freeMoves++;down=true;}
        
        if(cur.color==0){return;}//take out later
        
        switch(freeMoves){
            case 0:
                //System.out.println("locked in"); 
                break;
            case 1:
                //only one move, check further
                if(up){cur.up.color=cur.color;colorNode(convertNodeNum(cur.up.number),cur.up.color);}
                else if(left){cur.left.color=cur.color;colorNode(convertNodeNum(cur.left.number),cur.left.color);}
                else if(down){cur.down.color=cur.color;colorNode(convertNodeNum(cur.down.number),cur.down.color);}
                else{cur.right.color=cur.color;colorNode(convertNodeNum(cur.right.number),cur.right.color);}
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
    
    public void dummySolution(){
        
    }
    
    //for coloring flows
    public void colorNode(int[] coords,int type) throws IOException, InterruptedException{
        FlowDrawer.updateBoard(coords[0], coords[1], type); //turn color  
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
}
