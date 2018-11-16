package aiflow;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Derek Wallace
 */
public class AIflow {


    private static int width;
    private static int height;
    public static void main(String[] args) throws Exception {
        

        //numberedMap is used initially for setting up nodes and drawing

        String[] options=new String[2];
        options[0]="Smart";
        options[1]="Dummy";
        String choice="";
        String availableChars[] = {"5x5maze.txt","7x7maze.txt","8x8maze.txt","9x9maze.txt","10x10maze.txt","12x12maze.txt"};

        int choiceG = JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),
                    "Would you like to use the smart or dummy solution?",
                    "Flow Free solver",0,
                    JOptionPane.INFORMATION_MESSAGE,null,options,null
                );
                if(choiceG==0){
                    choice = (String) JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                    "Which puzzle size would you like to solve?",
                    "Choose puzzle",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    availableChars,
                    2);
                    try{
                        int[][] numberedMap=loadMap(choice);
                        Node[] map=linkNodes(numberedMap);
                        height=numberedMap.length;
                        width=numberedMap[0].length;

                        FlowDrawer.getInstance();
                        FlowDrawer.setBoard(numberedMap);
                        FlowFinder f=new FlowFinder(numberedMap,map,false);
                    }
                    catch(Exception e){System.out.println(e+" "+e.getStackTrace()[0].getLineNumber());}
                }
                else{
                    choice = (String) JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                    "Which puzzle size would you like to solve?",
                    "Choose puzzle",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    availableChars,
                    2);
                    try{
                        int[][] numberedMap=loadMap(choice);
                        Node[] map=linkNodes(numberedMap);
                        height=numberedMap.length;
                        width=numberedMap[0].length;

                        FlowDrawer.getInstance();
                        FlowDrawer.setBoard(numberedMap);
                        FlowFinder f=new FlowFinder(numberedMap,map,true);
                    }
                    catch(Exception e){System.out.println(e+" "+e.getStackTrace()[0].getLineNumber());}
                }



    }
    
    //read the textfile maze into strings without messy chars
    //could be probably be optimized for different fileRead type now bc of changes
    public static int[][] loadMap(String fileName) throws Exception{
        FileReader fr=new FileReader(fileName);
        int c;
        String str="";
        List<String> strA=new ArrayList<>(); //list of lines
        //process out newlines from midline
        while((c=fr.read())!=-1){
            System.out.print((char)c); //show map
            if(!(((char)c=='\n') || ((char)c=='\r'))){str=str+(char)c;}
            else if(((char)c=='\n' || (char)c=='\r')){
                if(strA.size()<1) {strA.add(str);str="";}
                else if(strA.size()>0 && strA.get(0).length()>str.length()){} //newline char found midline
                else {strA.add(str);str="";}
            }
            else System.out.println("Err: "+(char)c);
        }
        strA.add(str);
        System.out.println("\nWidth: "+str.length()+"\nHeight: "+strA.size());
        //now that size is known, fill info array
        int[][] map=fillMap(strA);
        
        return map;
    }
    
    //fill the array representing map with numbers
    public static int[][] fillMap(List<String> lines){
        int[][] map=new int[lines.size()][lines.get(0).length()];
        //find values of each spot
        for(int i=0;i<lines.size();i++){
            for(int j=0;j<lines.get(i).length();j++){
                char c=lines.get(i).charAt(j);
                switch (c) {
                    case '_': //empty
                        map[i][j]=0;
                        break;
                    case 'B': //Blue
                        map[i][j]=1;
                        break;
                    case 'A': //Cyan
                        map[i][j]=2;
                        break;
                    case 'W': //white
                        map[i][j]=3;
                        break;
                    case 'R': //red
                        map[i][j]=4;
                        break;
                    case 'P': //pink
                        map[i][j]=5;
                        break;
                    case 'D': //magenta
                        map[i][j]=6;
                        break;
                    case 'O': //orange
                        map[i][j]=7;
                        break;
                    case 'G': //Green
                        map[i][j]=8;
                        break;
                     case 'Y': //Yellow
                        map[i][j]=9;
                        break;
                     case 'K': //Seafoam
                        map[i][j]=10;
                        break;
                     case 'Q': //purple
                        map[i][j]=11;
                        break;
                    default:
                        map[i][j]=12; //shouldn't happen
                        break;
                }
            }
        }
        return map;
    }
    
    public static Node[] linkNodes(int[][] map){
        Node[] Maze=new Node[map.length*map[0].length];
        int nodeNum=0;
        //may i have some loops brother
        for(int i=0;i<Maze.length;i++){Maze[i]=new Node(i);}
        //connect left/right
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                
                if(nodeNum+1<Maze.length && (nodeNum+1)%(map[0].length)!=0){ 
                    Maze[nodeNum].right=Maze[nodeNum+1];
                    Maze[nodeNum+1].left=Maze[nodeNum];
                }
                if(map[i][j]>0){Maze[nodeNum].color=map[i][j];Maze[nodeNum].isSource=true;} //setting source colors
                nodeNum++;
            }
        }
        nodeNum=0;
        //connect up/down
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                //both spots aren't walls
                if(nodeNum+map[0].length<Maze.length){
                    Maze[nodeNum].down=Maze[nodeNum+map[0].length];
                    Maze[nodeNum+map[0].length].up=Maze[nodeNum];
                }

                nodeNum++;
            }
        }
        
        return Maze;
    }
    
}
