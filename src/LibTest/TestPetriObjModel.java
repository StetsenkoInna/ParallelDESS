/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LibTest;

//import PetriObj.PetriObjModel;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriObjModel;
import PetriObj.PetriP;
import PetriObj.PetriSim;


import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author Инна
 */
public class TestPetriObjModel {  //Результати співпадають з аналітичними обрахунками!!!!
      
   
    public static void main(String[] args) throws ExceptionInvalidNetStructure {
         
     
     // цей фрагмент для запуску імітації моделі з заданною мережею Петрі на інтервалі часу timeModeling  
         // PetriObjModel model = getModelForTestParallel(512);
        int numObj=2;
        PetriObjModel model = getModelSMOgroupForTestParallel(numObj,10);;
        model.setIsProtokol(true);
          double timeModeling = 1000;
         
         
         model.go(timeModeling);
       //  model.go(timeModeling); //з паралельною бібліотечкою не працює!!! пошкоджені методи
        //Цей фрагмент для виведення результатів моделювання getModel() на консоль  
      /*    System.out.println("Mean value of queue");
        for (int j = 1; j < 5; j++) {
        System.out.println(model.getListObj().get(j).getNet().getListP()[0].getMean());
        }
        System.out.println("Mean value of channel worked");
        for (int j = 1; j < 4; j++) {
        System.out.println(1.0 - model.getListObj().get(j).getNet().getListP()[1].getMean());
        }
        System.out.println(2.0 - model.getListObj().get(4).getNet().getListP()[1].getMean());
        for(PetriSim e: model.getListObj()){
        e.printMark();
        }
        //Цей фрагмент для виведення результатів моделювання getModelForTestParallel() на консоль
          model.getListObj().stream().forEach((e) -> {
              e.printMark();
        });*/
          model.getListObj().stream().forEach((e) -> {
            
            if (e.getName().equals("SMOwithoutQueue")) {
                 for(int j=0;j<e.getNet().getListP().length/2;j++)
                    System.out.println("mean queue in SMO "+(e.getNumObj()-1)+"  "+e.getNet().getListP()[2*j].getMean());
                
            }
        });
          
          
      } 
      
     // метод для конструювання моделі масового обслуговування з 4 СМО 
      
      public static PetriObjModel getModel() throws ExceptionInvalidNetStructure{
          ArrayList<PetriSim> list = new ArrayList<>();
          list.add(new PetriSim(libnet.NetLibrary.CreateNetGenerator(2.0)));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(1, 0.6)));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(1, 0.3)));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(1, 0.4)));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(2, 0.1)));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetFork(0.15, 0.13, 0.3)));
      //перевірка зв'язків
     //     System.out.println(list.get(0).getNet().getListP()[1].getName() + " == " + list.get(1).getNet().getListP()[0].getName());
     //     System.out.println(list.get(1).getNet().getListP()[2].getName() + " == " + list.get(5).getNet().getListP()[0].getName());

          list.get(0).getNet().getListP()[1] = list.get(1).getNet().getListP()[0]; //gen = > SMO1
          list.get(1).getNet().getListP()[2] = list.get(5).getNet().getListP()[0]; //SMO1 = > fork

          list.get(5).getNet().getListP()[1] = list.get(2).getNet().getListP()[0]; //fork =>SMO2
          list.get(5).getNet().getListP()[2] = list.get(3).getNet().getListP()[0]; //fork =>SMO3
          list.get(5).getNet().getListP()[3] = list.get(4).getNet().getListP()[0]; //fork =>SMO4

          list.get(2).getNet().getListP()[2] = list.get(1).getNet().getListP()[0]; //SMO2 => SMO1
          list.get(3).getNet().getListP()[2] = list.get(1).getNet().getListP()[0];//SMO3 => SMO1
          list.get(4).getNet().getListP()[2] = list.get(1).getNet().getListP()[0];//SMO4 => SMO1
          
       // list.get(5).setPriority(1); чому цей пріоритет сильно впливає на результат? 
          
          PetriObjModel model = new PetriObjModel(list);
          return model;
      }
     public static PetriObjModel getModelForTestParallel(int numObj) throws ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<PetriSim>();
        int numSMO = numObj - 1;
        list.add(new PetriSim(libnet.NetLibrary.CreateNetGenerator(2.0)));
        for (int i = 0; i < numSMO; i++) {
           
            list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(1, 1.0))); //   SMO1,SMO2,SMO3...         
        }
        list.get(0).getNet().getListP()[1] = list.get(1).getNet().getListP()[0]; //gen = > SMO1
        if (numSMO > 1) {
            for (int i = 2; i <= numSMO; i++) {
                list.get(i-1).getNet().getListP()[2] = list.get(i).getNet().getListP()[0]; //SMO1 = > SMO2, SMO2 = > SMO3,...
            }
        }
        PetriObjModel model = new PetriObjModel(list);
        return model;
    }
    
    
     public static PetriObjModel getModelSMOgroupForTestParallel(int numGroups, int numInGroup) throws ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        int numSMO = numGroups - 1;
        
        list.add(new PetriSim(libnet.NetLibrary.CreateNetGenerator(2.0)));
        for (int i = 0; i < numSMO; i++) {
           
            list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOgroup(numInGroup,1, 1.0,"group_"+i))); //   group1,group2,group3...         
        }
        list.get(0).getNet().getListP()[1] = list.get(1).getNet().getListP()[0]; //gen = > group1
        list.get(0).addOutT(list.get(0).getNet().getListT()[0]);
        list.get(1).addInT( list.get(1).getNet().getListT()[0]);
        list.get(0).setNextObj(list.get(1));
      //  list.get(0).setAccess(true); // першим починає працювати
        list.get(1).setPreviousObj( list.get(0));
       // list.get(1).setAccess(false); //очікувати подію від попереднього об єкта
        if (numSMO > 1) {
            for (int i = 2; i <= numSMO; i++) {
                int last = list.get(i-1).getNet().getListP().length-1;
               
             //   list.get(i-1).getNet().getListP()[last] = list.get(i).getNet().getListP()[0]; //group1 = > group2, group2 = > group3,...
                list.get(i).getNet().getListP()[0] = list.get(i-1).getNet().getListP()[last]; //group1 = > group2, group2 = > group3,...
              //  list.get(i-1).getListPositionsForStatistica().remove(list.get(i-1).getNet().getListP()[last]); // так не виходить! корегування списку розицій для статистики
               
                int lastT = list.get(i-1).getNet().getListT().length-1;
                list.get(i-1).addOutT(list.get(i-1).getNet().getListT()[lastT]);
                list.get(i).addInT(list.get(i).getNet().getListT()[0]);
                list.get(i-1).setNextObj(list.get(i));
                list.get(i).setPreviousObj(list.get(i-1));
                
              //  list.get(i).setAccess(false);//очікувати подію від попереднього об єкта
            }
    }
       
        //корегування списку розицій для статистики
        for (int i = 1; i <= numSMO; i++) {
            ArrayList<PetriP> positionForStatistics = new ArrayList<>();
            PetriP[] listP = new PetriP[list.get(i).getNet().getListP().length];
            listP = list.get(i).getNet().getListP();
            for(int j=0;j<listP.length-1;j++){ //окрім останньої, наприклад
                    positionForStatistics.add(listP[j]);
            }
            
            list.get(i).setListPositionsForStatistica(positionForStatistics);
        }
        
        PetriObjModel model = new PetriObjModel(list);
        return model;
    }
     
    public static PetriObjModel getModel(Object synchron) throws ExceptionInvalidNetStructure{
          ArrayList<PetriSim> list = new ArrayList<>();
          list.add(new PetriSim(libnet.NetLibrary.CreateNetGenerator(2.0)));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(1, 0.6," 'SMO1'")));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(1, 0.3," 'SMO2'")));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(1, 0.4," 'SMO3'")));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetSMOwithoutQueue(2, 0.1," 'SMO4'")));
          list.add(new PetriSim(libnet.NetLibrary.CreateNetFork(0.15, 0.13, 0.3)));
     // перевірка зв'язків
       //   System.out.println(list.get(0).getNet().getListP()[1].getName() + " == " + list.get(1).getNet().getListP()[0].getName());
       //  System.out.println(list.get(1).getNet().getListP()[2].getName() + " == " + list.get(5).getNet().getListP()[0].getName());

          list.get(0).getNet().getListP()[1] = list.get(1).getNet().getListP()[0]; //gen = > SMO1
          list.get(1).getNet().getListP()[2] = list.get(5).getNet().getListP()[0]; //SMO1 = > fork

          list.get(5).getNet().getListP()[1] = list.get(2).getNet().getListP()[0]; //fork =>SMO2
          list.get(5).getNet().getListP()[2] = list.get(3).getNet().getListP()[0]; //fork =>SMO3
          list.get(5).getNet().getListP()[3] = list.get(4).getNet().getListP()[0]; //fork =>SMO4

          list.get(2).getNet().getListP()[2] = list.get(1).getNet().getListP()[0]; //SMO2 => SMO1
          list.get(3).getNet().getListP()[2] = list.get(1).getNet().getListP()[0];//SMO3 => SMO1
          list.get(4).getNet().getListP()[2] = list.get(1).getNet().getListP()[0];//SMO4 => SMO1
          
       // list.get(5).setPriority(1); чому цей пріоритет сильно впливає на результат? 
          
          PetriObjModel model = new PetriObjModel(list);
          return model;
      }
    
    
    
     
}
