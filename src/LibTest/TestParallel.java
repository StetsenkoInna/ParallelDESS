/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LibTest;

import static LibTest.TestPetriObjModel.*;

import PetriObj.ExceptionInvalidNetStructure;

import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Inna
 */
public class TestParallel {
    public static void main(String[] args) throws ExceptionInvalidNetStructure {
        
        long startTime = System.nanoTime();
       
        ArrayList<Thread> threads = new ArrayList<>();
        double time= 100000;
        int numObj =8;
        
        
        PetriObjModel model = getModelSMOgroupForTestParallel(numObj,10); // sequence of  10SMO-groups and Generator
        System.out.println("  quantity of objects   "+model.getListObj().size()+
                 ", quantity of positions in one object "+model.getListObj().get(1).getListP().length);
        model.setTimeMod(time);
        PetriSim.setLimitArrayExtInputs(3);
//при великій кількості лімітованих подій в буфері  Generator ERROR: Wrong request of rollback з являється частіше і статистика хромає...
        model.setIsProtokol(true);

        model.getListObj().forEach((PetriSim e) -> {
            Thread petriObj = new Thread(e);
            threads.add(petriObj);
            petriObj.start();
        });

        threads.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(TestParallel.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
         printResultsForAllObj(model);
       // printResultsForLastObj(model);
        PetriSim e = model.getListObj().get(numObj-1);
        double mean=0.0;
        int n= 0;
        int frequency = 1;
     //   model.getListObj().stream().forEach((PetriSim e) -> {
       /*   for(int j=0; frequency*j<e.getArrayMean().size();j++){
             System.out.println(e.getArrayMark().get(frequency*j)+"\t"+e.getArrayTimeLocal().get(frequency*j)+"\t"+e.getArrayMean().get(frequency*j));
             mean +=e.getArrayMean().get(frequency*j);
             n++;
             
         }*/
         // System.out.println("======================="+mean/n);
        // System.out.println(e.getName()+" counter = "+e.getCounter());
     //   });
         
     
        long stopTime = System.nanoTime();
        System.out.println("-----------------------------");
        System.out.println("Total execution time: " + ((stopTime - startTime) / 1_000_000) + " ms.");
     } 
    
    public static void printResultsForAllObj(PetriObjModel model) {
        model.getListObj().stream().forEach((PetriSim e) -> {
            System.out.println("for SMO " + e.getName() + ":  tLocal " + e.getTimeLocal());
            if (e.getPreviousObj() != null) {
                System.out.println(" tExternalInput size " + e.getTimeExternalInput().size());
                if (!e.getTimeExternalInput().isEmpty()) {
                    System.out.println(" tExternalInput first " + e.getTimeExternalInput().get(0)
                            + " tExternalInput last " + e.getTimeExternalInput().get(e.getTimeExternalInput().size() - 1));
                }
                for (int j = 0; j < e.getNet().getListP().length / 2; j++) {
                    System.out.println("mean queue in SMO " + e.getName() + "  " + e.getNet().getListP()[2 * j].getMean()
                            + ", mark in position " + e.getNet().getListP()[2 * j].getMark());
                }
            }
            e.printState();
        });
    }
    public static void printResultsForLastObj(PetriObjModel model) {
        PetriSim e = model.getListObj().get(model.getListObj().size() - 1);
        System.out.println("for SMO " + e.getName() + ":  tLocal " + e.getTimeLocal()+", tMin "+e.getTimeMin());
        if (e.getPreviousObj() != null) {
            System.out.println(" tExternalInput size " + e.getTimeExternalInput().size());
            if (!e.getTimeExternalInput().isEmpty()) {
                System.out.println(" tExternalInput first " + e.getTimeExternalInput().get(0)
                        + " tExternalInput last " + e.getTimeExternalInput().get(e.getTimeExternalInput().size() - 1));
            }

            for (int j = 0; j < e.getNet().getListP().length / 2; j++) {
                System.out.println("mean queue in SMO " + e.getName() + "  " + e.getNet().getListP()[2 * j].getMean()
                        + ", mark in position " + e.getNet().getListP()[2 * j].getMark());
            }
        }
        e.printState();
    }

}
