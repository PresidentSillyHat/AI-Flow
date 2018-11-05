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
    public Node[] map; //pick one or other
    public int width;
    public List<Node> Frontier=new ArrayList<>(); //Frontier
    
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
                }
            }
        }
        if(isDummy){dummySolution();}
        else smartSolution();
    }
    
    public void smartSolution(){
        // cycle through a frontier with current expanding nodes?
        // make arc consistent by removing color option for paths not taken
        // forward check: if no option for color, backtrack one round with restrictions?
        // If only one valid choice, make the choice
        
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
