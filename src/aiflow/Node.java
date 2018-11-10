package aiflow;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Derek Wallace
 */
public class Node {
    public Node prev;
    public Node left;
    public Node right;
    public Node down;
    public Node up;
    public int number; //used for debugging
    public int color=0;
    public boolean isSource=false;
    public List<Integer> choices=new ArrayList<>(); //options for color
    
    public Node(int num){
        number=num;
    }
    public String toString(){
        return "Node number: "+number;
    }
    public int getNumber(){
        return number;
    }
    public boolean equalNode(Node other){
        return number==other.number;
    }
}
