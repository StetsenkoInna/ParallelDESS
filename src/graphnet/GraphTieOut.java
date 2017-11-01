/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphnet;

import PetriObj.TieOut;
import graphpresentation.GraphTie;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Инна
 */
public class GraphTieOut extends GraphTie implements Serializable {

    private static ArrayList<GraphTieOut> graphTieOutList = new ArrayList<GraphTieOut>();  // added by Olha 24.09.12, cjrrect by Inna 28.11.2012
    private TieOut tie;
    
   public GraphTieOut() { // додано Олею 28.09.12 для створення тимчасової дуги (тільки для промальовки) 
     super();
       tie = new TieOut();
       //System.out.println("GraphTieOut  "+ tie.getNameT()+"  "+tie.getNumT()+"  "+tie.getNameP()+"  "+tie.getNumP());
    }
    
     public GraphTieOut(TieOut tieout){
        tie = tieout;
   
    }
     public TieOut getTieOut()
    {
    return tie;
    }
    @Override
    public void setPetriElements() {
        tie.setQuantity(1);
        tie.setNumT(super.getBeginElement().getNumber());
        tie.setNameT(super.getBeginElement().getName());
        tie.setNumP(super.getEndElement().getNumber());
        tie.setNameP(super.getEndElement().getName());
    /*  System.out.println("GraphTIE OUT : setPetriElements "+super.getBeginElement().getName()+  "  "+ super.getBeginElement().getNumber()+
                    super.getEndElement().getName()+"  "+super.getEndElement().getNumber()     
                );*/
        addElementToArrayList(); //// added by Olha 24.09.12
    }

    @Override
    public void addElementToArrayList() {   // added by Olha 24.09.12
        if (graphTieOutList == null) {
            graphTieOutList = new ArrayList();
        }
        graphTieOutList.add(this);
    }

    @Override
    public void drawGraphElement(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g;
       /* if (inf) {   ///НЕ буває такого...
            Stroke drawingStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0);
            g2.setStroke(drawingStroke);
            g2.draw(this.getGraphElement());
            drawArrowHead(g2);
        } else*/
        {
        g2.setStroke(new BasicStroke());
        g2.draw(this.getGraphElement());
        drawArrowHead(g2);
        }
        if (tie.getQuantity() != 1) {
            this.getAvgLine().setLocation((this.getGraphElement().getX1() + this.getGraphElement().getX2()) / 2, (this.getGraphElement().getY1() + this.getGraphElement().getY2()) / 2);
            g2.drawLine((int) this.getAvgLine().getX() + 5, (int) this.getAvgLine().getY() - 5, (int) this.getAvgLine().getX() - 5, (int) this.getAvgLine().getY() + 5);
            g2.drawString(Integer.toString(tie.getQuantity()), (float) this.getAvgLine().getX(), (float) this.getAvgLine().getY() - 7);
        }
    }
 

    public static ArrayList<GraphTieOut> getGraphTieOutList() {
        return graphTieOutList;
    }
    
    public static ArrayList<TieOut> getTieOutList() {  // added by Inna 1.11.2012
        
        ArrayList<TieOut> arrayTieOut = new ArrayList();
        for (GraphTieOut e: graphTieOutList)
            arrayTieOut.add(e.getTieOut());
        return arrayTieOut;
    }
//    public static void setTieOutList(ArrayList<TieOut> TieOutList) {
//        TieOut.tieOutList = TieOutList;
//    }
    public static void setNullTieOutList() {
        graphTieOutList.clear();
    }
    public static void addGraphTieOutList(List<GraphTieOut> tieOut){ // added by Olha 14/11/2012
      for (GraphTieOut to:tieOut) {
          graphTieOutList.add(to);
      } 
    }
     
    
    
     @Override
    public int getQuantity(){  //потрібно для правильної роботи методу getQuantity() батьківського класу
            return tie.getQuantity();
        }
    @Override
   public void setQuantity(int i){
            tie.setQuantity(i);
        }
}
