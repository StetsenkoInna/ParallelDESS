/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphpresentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Ольга
 */
public class GraphPosition extends GraphElement {
    
    private static final int DIAMETER = 40;       // діаметр кола
 
    private Ellipse2D graphElement=new Ellipse2D.Double(0,0, DIAMETER, DIAMETER);  // координати розташування кола

   public  GraphPosition() {
    }

    @Override
    public void drawGraphElement(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3));
        g2.draw(graphElement);
        g2.setColor(Color.WHITE);
        g2.fill(graphElement);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.BLACK);
     
    }

    
    public static int getDIAMETER() {
        return DIAMETER;
    }

    
    @Override
    public void setNewCoordinates(Point2D p) {
            graphElement.setFrame(p.getX() - DIAMETER / 2, p.getY() - DIAMETER / 2, DIAMETER, DIAMETER);
    }

        
    @Override
    public boolean isGraphElement(Point2D p) {
        if (graphElement.contains(p)) {
            return true;
        }
        return false;
    }

    @Override
    public Point2D getGraphElementCenter() {
        return new Point2D.Double(graphElement.getX() + DIAMETER / 2, graphElement.getY() + DIAMETER / 2);
    }

  
    @Override
    public String getType() {
        return graphElement.getClass().toString();
    }

   
    @Override
    public  int getBorder() {
        return DIAMETER / 2;
    }
    

    public Ellipse2D getGraphElement() {
        return graphElement;
    }

    public void setGraphElement(Ellipse2D graphElement) {
        this.graphElement = graphElement;
    }
    
    
    
}
