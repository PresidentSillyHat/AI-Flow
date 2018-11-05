package aiflow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Derek Wallace
 */
public class FlowDrawer extends JFrame{
    private static BufferedImage drawer;
    private static JPanel boardPanel;
    private static FlowDrawer inst=null;
    public static int height;
    public static int width;

    //using static to ease repainting, personal preference
    public static FlowDrawer getInstance() throws IOException{
        if (inst==null){
            inst = new FlowDrawer();}
        inst.repaint();	//updates board when needed
        return inst;
    }
    
    //window stuff
    public FlowDrawer() throws IOException{	//The GUI Frame
        super("AI FlowFree with Derek and Dillon");
 
        drawer=new BufferedImage(1500,1000,BufferedImage.TYPE_INT_ARGB);

        //this.setLayout(new GridBagLayout());
        boardPanel=new JPanel(){
        
            @Override
            protected void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                g.drawImage(drawer, 0, 0, null);	
                
            }
        };
        boardPanel.setSize(1500, 1000);
        this.add(boardPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500,1250);	//Good size for window bc of board
        this.setVisible(true);
    
    }
    
    //pass the initial numberedMap to GUI so it can draw layout
    public static void setBoard(int [][] newMap) throws IOException{

        int h=newMap.length,w=newMap[0].length;
        int tileHeight=1000/h, tileWidth=1250/w,x=0,y=0;
        height=h;width=w;
        
        Graphics g=drawer.getGraphics();
        for(int i=0;i<h;i++){
            for(int j=0;j<w;j++){
            //logic to auto color tiles
                x=j*tileWidth;
                y=i*tileHeight;
                g.setColor(new Color(0,0,0)); 
                g.fillRect(x, y, tileWidth, tileHeight); //make all blackground black
                switch (newMap[i][j]) {
                    case 0:
                        g.setColor(Color.black); //wall
                        break;
                    case 1:
                        g.setColor(Color.blue); //blue
                        break;
                    case 2:
                        g.setColor(Color.cyan); //cyan
                        break;
                    case 3:
                        g.setColor(Color.white); //white
                        break;
                    case 4:
                        g.setColor(Color.red); //red
                        break;
                    case 5:
                        g.setColor(Color.pink); //pink
                        break;
                    case 6:
                        g.setColor(Color.MAGENTA); //magenta
                        break;
                    case 7:
                        g.setColor(Color.orange); //orange
                        break;
                    case 8:
                        g.setColor(Color.green); //green
                        break;
                    case 9:
                        g.setColor(Color.yellow); //yellow
                        break;
                    case 10:
                        g.setColor(new Color(60,179,113)); //seafoam
                        break;
                    case 11:
                        g.setColor(new Color(128,0,128)); //purple
                        break;
                    default:
                        g.setColor(new Color(128,128,128));   //grey
                        break;
                }

                g.fillOval(x, y, tileWidth, tileHeight);
                g.setColor(new Color(128,128,128));
                g.drawRect(x, y, tileWidth, tileHeight); //outline for visibility
            }
        }
        FlowDrawer.getInstance();	//Repaint board to show changes
        g.dispose();	//Free up space from Graphics g
        
    }
    
    //update board, format is numberedMap[x0][y0]
    public static void updateBoard(int x0, int y0,int type) throws IOException{
        
        int tileHeight=1000/height, tileWidth=1250/width,x,y;
        Graphics g=drawer.getGraphics();        
        x=x0*tileWidth;
        y=y0*tileHeight;
        switch (type) {
            case 0:
                g.setColor(Color.black); //wall
                break;
            case 1:
                g.setColor(Color.blue); //blue
                break;
            case 2:
                g.setColor(Color.cyan); //cyan
                break;
            case 3:
                g.setColor(Color.white); //white
                break;
            case 4:
                g.setColor(Color.red); //red
                break;
            case 5:
                g.setColor(Color.pink); //pink
                break;
            case 6:
                g.setColor(Color.MAGENTA); //magenta
                break;
            case 7:
                g.setColor(Color.orange); //orange
                break;
            case 8:
                g.setColor(Color.green); //green
                break;
            case 9:
                g.setColor(Color.yellow); //yellow
                break;
            case 10:
                g.setColor(new Color(60,179,113)); //seafoam
                break;
            case 11:
                g.setColor(new Color(128,0,128)); //purple
                break;
            default:
                g.setColor(new Color(128,128,128));   //grey
                break;
        }

        g.fillRect(x+tileWidth/4, y+tileHeight/4, tileWidth/2, tileHeight/2);
        g.setColor(new Color(128,128,128));
        g.drawRect(x, y, tileWidth, tileHeight); //outline for visibility

        FlowDrawer.getInstance();	//Repaint board to show changes
        g.dispose();	//Free up space from Graphics g

    }    
}
