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

    public void smartSolution(){
        // cycle through a frontier with current expanding nodes?
        // make arc consistent by removing color option for paths not taken
        // forward check: if no option for color, backtrack one round with restrictions?
        // If only one valid choice, make the choice

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
