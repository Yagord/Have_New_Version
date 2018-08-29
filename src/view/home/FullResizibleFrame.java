/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.home;

/**
 *
 * @author Pierre-Nicolas
 */
import java.awt.BorderLayout;   
import java.awt.Color;   
import java.awt.Cursor;   
import java.awt.Dimension;   
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;   
import java.awt.Point;   
import java.awt.Rectangle;      
import java.awt.Toolkit;   
import java.awt.event.MouseEvent;   
import java.awt.event.MouseListener;   
import java.awt.event.MouseMotionListener;   
import javax.swing.*;       
import javax.swing.border.LineBorder;   
   
   
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;      
import java.awt.Dimension;      
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;      
import java.awt.Point;      
import java.awt.Rectangle;         
import java.awt.Toolkit;      
import java.awt.event.MouseEvent;      
import java.awt.event.MouseListener;      
import java.awt.event.MouseMotionListener;      
import javax.swing.*;                
import javax.swing.border.LineBorder;
 
public class FullResizibleFrame extends JFrame implements MouseMotionListener, MouseListener      
{         
    private static final long serialVersionUID = 1L;        
    private Point start_drag;      
    private  Point start_loc; 
    private  Point precedent_loc;
    private  int precedent_width;
    private  int precedent_height;
    Toolkit toolkit =  Toolkit.getDefaultToolkit (); 
    private int minWidth;      
    private int minHeight; 
    private Point initialLocation;
    int cursorArea = 5;  
    Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();  
    private int DIFF_MIN_WIDTH = 30;
    private int DIFF_MIN_HEIGHT = 30;
     
    public FullResizibleFrame(Dimension initialDimension, Point initialLocation)      
    {      
        this.initialLocation = initialLocation;
        minWidth = (int)initialDimension.getWidth();
        minHeight = (int)initialDimension.getHeight();
        Init();      
    }      
           
    private void Init()       
    {      
        addMouseMotionListener(this);      
        addMouseListener(this);      
        this.setSize(minWidth, minHeight);    
         
        minWidth -= DIFF_MIN_WIDTH;
        minHeight -= DIFF_MIN_HEIGHT;
         
        setLocation(initialLocation);
        setUndecorated(true);             
    }  
 
    public static Point getScreenLocation(MouseEvent e, JFrame frame) 
    {
        Point cursor = e.getPoint();
        Point view_location = frame.getLocationOnScreen();
        return new Point((int) (view_location.getX() + cursor.getX()),
                (int) (view_location.getY() + cursor.getY()));
    }
     
    @Override    
    public void mouseDragged(MouseEvent e)       
    {      
        moveOrFullResizeFrame(e);      
    }      
      
    @Override    
    public void mouseMoved(MouseEvent e)       
    {      
        Point cursorLocation = e.getPoint();         
        int xPos = cursorLocation.x;         
        int yPos = cursorLocation.y;      
               
        if(xPos >= cursorArea && xPos <= getWidth()-cursorArea && yPos >= getHeight()-cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));      
        else if(xPos >= getWidth()-cursorArea && yPos >= cursorArea && yPos <= getHeight()-cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));      
        else if(xPos <= cursorArea && yPos >= cursorArea && yPos <= getHeight()-cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));      
        else if(xPos >= cursorArea && xPos <= getWidth()-cursorArea && yPos <= cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));      
        else if(xPos <= cursorArea && yPos <= cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));      
        else if(xPos >= getWidth() - cursorArea && yPos <= cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));      
        else if(xPos >= getWidth()-cursorArea && yPos >= getHeight()-cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));      
        else if(xPos <= cursorArea && yPos >= getHeight()-cursorArea)      
            setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));      
        else    
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));       
    }        
           
    @Override    
    public void mouseClicked(MouseEvent e)       
    {      
        Object sourceObject=e.getSource();      
        if(sourceObject instanceof JPanel)      
        {      
            if (e.getClickCount() == 2)       
            {     
                if(getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))      
                    headerDoubleClickResize();      
            }      
        }      
    }     
           
    private void moveOrFullResizeFrame(MouseEvent e)       
    {      
        Object sourceObject=e.getSource();   
        Point current = getScreenLocation(e, this);   
        Point offset = new Point((int)current.getX()- (int)start_drag.getX(), (int)current.getY()- (int)start_drag.getY());   
            
        if(sourceObject instanceof JPanel    
                && getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))    
            setLocation((int) (start_loc.getX() + offset.getX()), (int) (start_loc.getY() + offset.getY()));       
        else if(!getCursor().equals(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)))      
        {         
            int oldLocationX = (int)getLocation().getX();      
            int oldLocationY = (int)getLocation().getY();      
            int newLocationX = (int) (this.start_loc.getX() + offset.getX());      
            int newLocationY = (int) (this.start_loc.getY() + offset.getY());         
            boolean N_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));      
            boolean NE_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));      
            boolean NW_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));      
            boolean E_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));      
            boolean W_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));      
            boolean S_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));      
            boolean SW_Resize = getCursor().equals(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));      
            boolean setLocation = false;      
            int newWidth = e.getX();      
            int newHeight = e.getY();      
                   
            if(NE_Resize)         
            {           
                newHeight = getHeight() - (newLocationY - oldLocationY);       
                newLocationX = (int)getLocation().getX();      
                setLocation = true;      
            }      
            else if(E_Resize)      
                newHeight = getHeight();       
            else if(S_Resize)      
                newWidth = getWidth();                    
            else if(N_Resize)      
            {         
                newLocationX = (int)getLocation().getX();      
                newWidth = getWidth();      
                newHeight = getHeight() - (newLocationY - oldLocationY);      
                setLocation = true;      
            }      
            else if(NW_Resize)      
            {      
                newWidth = getWidth() - (newLocationX - oldLocationX);      
                newHeight = getHeight() - (newLocationY - oldLocationY);      
                setLocation =true;      
            }         
            else if(NE_Resize)        
            {           
                newHeight = getHeight() - (newLocationY - oldLocationY);      
                newLocationX = (int)getLocation().getX();        
            }      
            else if(SW_Resize)      
            {         
                newWidth = getWidth() - (newLocationX - oldLocationX);      
                newLocationY = (int)getLocation().getY();                     
                setLocation =true;      
            }      
            if(W_Resize)      
            {         
                newWidth = getWidth() - (newLocationX - oldLocationX);      
                newLocationY = (int)getLocation().getY();         
                newHeight = getHeight();      
                setLocation =true;      
            }      
                                   
            if(newWidth >= (int)toolkit.getScreenSize().getWidth() || newWidth <= minWidth)      
            {      
                newLocationX = oldLocationX;      
                newWidth = getWidth();      
            }      
                   
            if(newHeight >= (int)toolkit.getScreenSize().getHeight() - 30 || newHeight <= minHeight)      
            {      
                newLocationY = oldLocationY;      
                newHeight = getHeight();      
            }      
                   
            if(newWidth != getWidth() || newHeight != getHeight())      
            {      
                this.setSize(newWidth, newHeight);      
                           
                if(setLocation)      
                    this.setLocation(newLocationX, newLocationY);               
            }      
        }      
    }      
            
    private void headerDoubleClickResize()       
    {            
        if(getWidth() < screen.getWidth() || getHeight() < screen.getHeight())
        {
            this.setSize((int)screen.getWidth(),(int)screen.getHeight());  
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();      
            Dimension frameSize = this.getSize();     
            this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2); 
             
        }
        else  
        {   
            this.setSize(precedent_width, precedent_height); 
            this.setLocation(precedent_loc);
        }   
    }      
        
    @Override    
    public void mousePressed(MouseEvent e)       
    {      
        this.start_drag = getScreenLocation(e, this);      
        this.start_loc = this.getLocation();     
          
           
        if(getWidth() < screen.getWidth() || getHeight() < screen.getHeight())
        {
            precedent_loc = this.getLocation(); 
            precedent_width = getWidth();
            precedent_height = getHeight();
        }
    }      
           
    @Override    
    public void mouseEntered(MouseEvent e) {}      
      
    @Override    
    public void mouseExited(MouseEvent e) {}      
           
    @Override    
    public void mouseReleased(MouseEvent e) {}  
     
    public static void main(String[] args)         
    {           
        Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        int frameWidth = 300;
        int frameHeight = 300;          
           
        Point initialLocation = new Point((int)toolkit.getScreenSize().getWidth()/2 - frameWidth/2,       
                (int)toolkit.getScreenSize().getHeight()/2 - frameHeight/2);
        Dimension initialDimension = new Dimension(frameWidth, frameHeight);
        FullResizibleFrame fullResizibleFrame = new FullResizibleFrame(initialDimension, initialLocation);
         
        JPanel viewContainer=(JPanel)fullResizibleFrame.getContentPane();         
        viewContainer.setBackground(Color.LIGHT_GRAY);      
        viewContainer.setBorder(new LineBorder(Color.darkGray));      
               
        JPanel headerPanel=new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));     
        headerPanel.add(new JLabel("Double click or drag!"));   
        headerPanel.setPreferredSize(new Dimension(frameWidth, 20));      
        headerPanel.setBackground(Color.gray);      
        headerPanel.addMouseListener(fullResizibleFrame);
        headerPanel.addMouseMotionListener(fullResizibleFrame);
    
        viewContainer.add(headerPanel, BorderLayout.NORTH);    
         
        fullResizibleFrame.setVisible(true);
    }      
}
