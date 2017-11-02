/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LibTest;

import PetriObj.ExceptionInvalidNetStructure;
import graphpresentation.PetriNetsFrame;

/**
 *
 * @author Inna
 */
public class TestPaint {
     public static void main(String[] args) throws ExceptionInvalidNetStructure {
    
     Thread thread = new Thread(new Runnable(){

         @Override
         public void run() {
             new PetriNetsFrame().setVisible(true);
         }
         
     });
     thread.start();
     }
}
