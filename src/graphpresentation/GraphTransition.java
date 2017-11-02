/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphpresentation;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Ольга
 */
public class GraphTransition extends GraphElement{
    private static final int HEIGHT = 50;
    private static final int WIDTH = 5;
    private Rectangle2D graphElement=new Rectangle2D.Double(0,0, WIDTH, HEIGHT);;


    public GraphTransition(){
        
    }
  
    @Override
    public void drawGraphElement(Graphics2D g2) {
        g2.draw(graphElement);
        g2.fill(graphElement);
   }
   

    @Override
    public void setNewCoordinates(Point2D p){
        graphElement.setFrame(p.getX() - WIDTH / 2, p.getY() - HEIGHT / 2, WIDTH, HEIGHT);
    }

   
    @Override
    public boolean isGraphElement(Point2D p){
        if (graphElement.contains(p) || new Line2D.Double(new Point2D.Double(graphElement.getMaxX(),graphElement.getMinY()),new Point2D.Double(graphElement.getMinX(),graphElement.getMaxY())).ptSegDist(p)<WIDTH*2) {
            return true;
        }
        return false;
    }

  
    @Override
    public Point2D getGraphElementCenter(){
        return new Point2D.Double(graphElement.getX() + WIDTH / 2, graphElement.getY() + HEIGHT / 2);
    }

   
    @Override
    public String getType() {
        return graphElement.getClass().toString();
    }

    
    @Override
    public  int getBorder() {
        return WIDTH;
    }


    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public Rectangle2D getGraphElement() {
        return graphElement;
    }

    public void setGraphElement(Rectangle2D graphElement) {
        this.graphElement = graphElement;
    }
    
    
}
