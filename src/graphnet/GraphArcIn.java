/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphnet;

import PetriObj.ArcIn;
import graphpresentation.GraphArc;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Инна
 */
public class GraphArcIn extends GraphArc implements Serializable {

    private static ArrayList<GraphArcIn> graphArcInList = new ArrayList<GraphArcIn>(); // added by Olha 24.09.12, cjrrect by Inna 28.11.2012
    private ArcIn tie;
  
    public GraphArcIn() { // додано Олею 28.09.12 для створення тимчасової дуги (тільки для промальовки)  
      super();
        tie = new ArcIn();
        //System.out.println("GraphArcIn  "+ tie.getNameP()+"  "+tie.getNumP()+"  "+tie.getNameT()+"  "+tie.getNumT());
   }
        
    public GraphArcIn(ArcIn tiein){
        tie = tiein;
   
    }
    public ArcIn getArcIn()
    {
    return tie;
    }

    @Override
    public void setPetriElements() {
        tie.setQuantity(1);
        tie.setInf(false);
        tie.setNumP(super.getBeginElement().getNumber());
        tie.setNumT(super.getEndElement().getNumber());
        tie.setNameP(super.getBeginElement().getName());
        tie.setNameT(super.getEndElement().getName());
      /* System.out.println("GraphTIE IN : setPetriElements "+super.getBeginElement().getName()+  "  "+ super.getBeginElement().getNumber()+
                    super.getEndElement().getName()+"  "+super.getEndElement().getNumber()     
                );*/
     
        addElementToArrayList(); //// added by Olha 24.09.12
    }

    @Override
    public void addElementToArrayList() {  // added by Olha 24.09.12
        if (graphArcInList == null) {
            graphArcInList = new ArrayList();
        }
        graphArcInList.add(this);
    }

    @Override
    public void drawGraphElement(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g;
        if (tie.getIsInf()) {
            Stroke drawingStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0);
            g2.setStroke(drawingStroke);
            g2.draw(this.getGraphElement());
            drawArrowHead(g2);
        } else {
            g2.setStroke(new BasicStroke());
            g2.draw(this.getGraphElement());
            drawArrowHead(g2);
        }
        if (tie.getQuantity() != 1 || tie.kIsParam()) {
            String quantityString = tie.kIsParam() // added by Katya 08.12.2016
                ? tie.getKParamName()
                : Integer.toString(tie.getQuantity());
            this.getAvgLine().setLocation((this.getGraphElement().getX1() + this.getGraphElement().getX2()) / 2, (this.getGraphElement().getY1() + this.getGraphElement().getY2()) / 2);
            g2.drawLine((int) this.getAvgLine().getX() + 5, (int) this.getAvgLine().getY() - 5, (int) this.getAvgLine().getX() - 5, (int) this.getAvgLine().getY() + 5);
            g2.drawString(quantityString, (float) this.getAvgLine().getX(), (float) this.getAvgLine().getY() - 7);
        }
    }
    
    public static ArrayList<GraphArcIn> getGraphArcInList() {
        return graphArcInList;
    }
    public static ArrayList<ArcIn> getArcInList() {  // added by Inna 1.11.2012
        
        ArrayList<ArcIn> arrayArcIn = new ArrayList <>();
        for (GraphArcIn e: graphArcInList)
            arrayArcIn.add(e.getArcIn());
        return arrayArcIn;
    }
    

    public static void setNullArcInList() {
        graphArcInList.clear();
    }
  
    @Override
    public int getQuantity(){
            return tie.getQuantity();
        }
    @Override
   public void setQuantity(int i){
            tie.setQuantity(i);
        }
    @Override
   public boolean getIsInf(){
            return tie.getIsInf();
        }
    @Override
   public void setInf(boolean i){
            tie.setInf(i);
        } 
    public static void addGraphArcInList(List<GraphArcIn> tieIn){ // added by Olha 14/11/2012
        for (GraphArcIn ti:tieIn){
            graphArcInList.add(ti);
        }
    }
    
    
    
}
