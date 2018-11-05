package aiflow;

import java.io.IOException;
/**
 *
 * @author Derek Wallace
 */
public class FlowFinder {
 
    public int[][] board;
    
    public FlowFinder(int[][] game, boolean isDummy) throws Exception{
        board=game;
        if(isDummy){dummySolution();}
        else smartSolution();
    }
    
    public void smartSolution(){
        
    }
    public void dummySolution(){
        int[][] tempSol=board;
        for(int i=0;i<tempSol.length;i++){
            for(int j=0;j<tempSol[0].length;j++){
                if(tempSol[i][j]>0){
                    //at a color
                }
            }
        }
    }
    
    //for coloring flows
    public void colorNode(int[] coords,int type) throws IOException, InterruptedException{
        FlowDrawer.updateBoard(coords[0], coords[1], type); //turn color
        
                
    }
}
