/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphnet;

import PetriObj.PetriT;
import graphpresentation.GraphTransition;
import java.awt.Graphics2D;
import java.io.Serializable;

/**
 *
 * @author Инна
 */
public class GraphPetriTransition extends GraphTransition implements Serializable {

    private PetriT transition;
    private int id;
    private static int simpleInd=0; // added by Inna 18.01.2013

    public GraphPetriTransition(PetriT T, int i) //додано Олею
    {
        transition = T;
        id=i;
    }

    public GraphPetriTransition(PetriT T) {
        transition = T;
        id = transition.getNumber(); // added by Katya 20.11.2016
    }
    public PetriT getPetriTransition()
    {
       return transition;
    }

    @Override
    public void drawGraphElement(Graphics2D g2) {
        super.drawGraphElement(g2);
        int font = 10;
        g2.drawString(transition.getName(), (float) this.getGraphElement().getCenterX() - transition.getName().length() * font / 2, (float) this.getGraphElement().getCenterY() - GraphPetriTransition.getHEIGHT()/ 2-GraphPetriTransition.getHEIGHT()/5);
        if(transition.getDistribution()!=null)
            g2.drawString("t=" + transition.getParametr()+"("+transition.getDistribution()+")", (float) this.getGraphElement().getCenterX() - Double.toString(transition.getParametr()).length() * font / 2, (float) this.getGraphElement().getCenterY() + GraphPetriTransition.getHEIGHT() / 2 + 20);
        else
            g2.drawString("t=" + transition.getParametr(), (float) this.getGraphElement().getCenterX() - Double.toString(transition.getParametr()).length() * font / 2, (float) this.getGraphElement().getCenterY() + GraphPetriTransition.getHEIGHT() / 2 + 20);
        g2.drawString("b=" + transition.getBuffer(), (float) this.getGraphElement().getCenterX() - Double.toString(transition.getBuffer()).length() * font / 2, (float) this.getGraphElement().getCenterY() + GraphPetriTransition.getHEIGHT() / 2 + 40);
    }

    @Override
    public String getType() { // added by Katya 23.10.2016
        return "GraphPetriTransition";
    }
    
    @Override
    public int getId(){
        return id;
    }

     @Override
     public String getName(){
           return this.getPetriTransition().getName();
       }
    @Override
       public int getNumber(){
           return this.getPetriTransition().getNumber();
       }
   
    public static String setSimpleName(){ // added by Inna 18.01.2013
          simpleInd++; 
          return "T"+simpleInd;
       }
    public static void setNullSimpleName(){ // added by Inna 18.01.2013
          simpleInd = 0; 
          
       }
     
}
