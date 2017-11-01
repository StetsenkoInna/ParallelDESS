/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PetriObj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JTextArea;
//import points2DVisual_withJchartLib.points2DVisual.DoublePoint;

/**
 * This class provides constructing Petri objective model.<br>
 * List of Petri-objects contains Petri-objects with links between them.<br>
 * For creating Petri-object use class PetriSim. For linking Petri-objects use
 * combining places and passing tokens.<br>
 * Method DoT() of class PetriSim provides programming the passing tokens from
 * the transition of one Petri-object to the place of other.
 *
 * @author Инна
 */
public class PetriObjModel implements Serializable {

    private ArrayList<PetriSim> listObj = new ArrayList<PetriSim>();
    private double t;
    private boolean isProtokolPrint = true;
    private boolean isStatistica = true;

    /**
     * Constructs model with given list of Petri objects
     *
     * @param List list of Petri objects
     */
    public PetriObjModel(ArrayList<PetriSim> List) {
        listObj = List;
    }

    /**
     * Set need in protocol
     *
     * @param b is true if protocol is needed
     */
    public void setIsProtokol(boolean b) {
        isProtokolPrint = b;
    }

    /**
     * Set need in statistics
     *
     * @param b is true if statistics is 
     */
    public void setIsStatistica(boolean b) {
        isStatistica = b;
    }

    /**
     *
     * @return the list of Petri objects of model
     */
    public ArrayList<PetriSim> getListObj() {
        return listObj;
    }

    /**
     * Set list of Petri objects
     *
     * @param List list of Petri objects
     */
    public void setListObj(ArrayList<PetriSim> List) {
        listObj = List;
    }

    /**
     * Simulating from zero time until the time equal time modeling
     *
     * @param timeModeling time modeling
     */
    public void go(double timeModeling) {
        
       PetriSim.setTimeMod(timeModeling);   //3.12.2015
        
        t = 0;
        double min;
      
        for (PetriSim e :  sortObj(listObj)) { //виправлено 9.11.2015
            e.start();
        }
        if (isProtokolPrint == true) {
            for (PetriSim e : listObj) {
                e.printMark();
            }
        }
        ArrayList<PetriSim> K = new ArrayList<PetriSim>();
        Random r = new Random();

        while (t < timeModeling) {

            K.clear();

            min = listObj.get(0).getTimeMin();  //пошук найближчої події

            for (PetriSim e : listObj) {
                if (e.getTimeMin() < min) {
                    min = e.getTimeMin();
                }
            }
            /*  if(min_t<t){ // added 24.06.2013   !!!!Подумать...при отрицательных задержках висит!!!!
             JOptionPane.showMessageDialog(null, "Negative time delay was generated! Check parameters, please/");
             return;
            
             }*/
            if (isStatistica == true) {
                for (PetriSim e : listObj) {
                    e.doStatistica((min - t) / min); //статистика за час "дельта т", для спільних позицій потрібно статистику збирати тільки один раз!!!

                }
            }

            t = min; // просування часу

            PetriSim.setTimeCurr(t); // просування часу //3.12.2015
            
            if (isProtokolPrint == true) {
                System.out.println(" Просування часу time = " + t + "\n");
            }
            if (t <= timeModeling) {

                for (PetriSim e : listObj) {
                    if (t == e.getTimeMin()) // розв'язання конфлікту об'єктів рівноймовірнісним способом
                    {
                        K.add(e);                           //список конфліктних обєктів
                    }
                }
                int num;
                int max;
                if (isProtokolPrint == true) {
                    System.out.println(" Список конфліктних обєктів  " + "\n");
                    for (int ii = 0; ii < K.size(); ii++) {
                        System.out.println(" K [ " + ii + "  ] = " + K.get(ii).getName() + "\n");
                    }
                }

                if (K.size() > 1) { //вибір обєкта, що запускається
                    max = K.size();
                    sortObj(K);
                    for (int i = 1; i < K.size(); i++) { //System.out.println("  "+K.get(i).getPriority()+"  "+K.get(i-1).getPriority());
                        if (K.get(i).getPriority() < K.get(i - 1).getPriority()) {
                            max = i - 1;
                            //System.out.println("max=  "+max);
                            break;
                        }

                    }
                    if (max == 0) {
                        num = 0;
                    } else {
                        num = r.nextInt(max);
                    }
                } else {
                    num = 0;
                }

                if (isProtokolPrint == true) {
                    System.out.println(" Обраний об'єкт  " + K.get(num).getName() + "\n" + " NextEvent " + "\n");
                }

                for (PetriSim e: listObj) {
                    if (e.getNumObj() == K.get(num).getNumObj()) {
                        if (isProtokolPrint == true) {
                            System.out.println(" time =   " + t + "   Подія '" + e.getEventMin().getName() + "'\n"
                                    + "                       відбувається для об'єкту   " + e.getName() + "\n");
                        }
                        e.doT();
                        e.stepEvent();

                    }

                }
                if (isProtokolPrint == true) {
                    System.out.println("Вихід маркерів з переходів:");
                    for (PetriSim e : listObj) //ДРУК поточного маркірування
                    {
                        e.printMark();
                    }
                }
                for (PetriSim e : sortObj(listObj)) {
                    //можливо змінились умови для інших обєктів
                    e.start(); //вхід маркерів в переходи Петрі-об'єкта

                }
                if (isProtokolPrint == true) {
                    System.out.println("Вхід маркерів в переходи:");
                    for (PetriSim e : listObj){ //ДРУК поточного маркірування
                    
                        e.printMark();
                    }
                }
            }
        }
    }

    public void go(double timeModeling, JTextArea area) { //виведення протоколу подій та результатів моделювання у об"єкт класу JTextArea
        area.setText(" Events protocol ");
        PetriSim.setTimeMod(timeModeling);   //3.12.2015
        t = 0;
        double min;
        for (PetriSim e : sortObj(listObj)) { //виправлено 9.11.2015
            e.start();
        }
        if (isProtokolPrint == true) {
            for (PetriSim e : listObj) {
                e.printMark();
            }
        }
        ArrayList<PetriSim> K = new ArrayList<PetriSim>();
        Random r = new Random();

        while (t < timeModeling) {

            K.clear();

            min = Double.MAX_VALUE;  //пошук найближчої події

            for (PetriSim e : listObj) {
                if (e.getTimeMin() < min) {
                    min = e.getTimeMin();
                }
            }
            if (isStatistica == true) {
                for (PetriSim e : listObj) {
                    if (min > 0) {
                        e.doStatistica((min - t) / min); //статистика за час "дельта т", для спільних позицій потрібно статистику збирати тільки один раз!!!
                    }
                }
            }

            t = min; // просування часу

          PetriSim.setTimeCurr(t); // просування часу //3.12.2015
            
            if (isProtokolPrint == true) {
                area.append(" \n Просування часу time = " + t + "\n");
            }
            if (t <= timeModeling) {

                for (PetriSim e : listObj) {
                    if (t == e.getTimeMin()) // розв'язання конфлікту об'єктів рівноймовірнісним способом
                    {
                        K.add(e);                           //список конфліктних обєктів
                    }
                }
                int num;
                int max;
                if (isProtokolPrint == true) {
                    area.append("  Список конфліктних обєктів  " + "\n");
                    for (int ii = 0; ii < K.size(); ii++) {
                        area.append("  K [ " + ii + "  ] = " + K.get(ii).getName() + "\n");
                    }
                }

                if (K.size() > 1) //вибір обєкта, що запускається
                {
                    max = K.size();
                    sortObj(K);
                    for (int i = 1; i < K.size(); i++) { //System.out.println("  "+K.get(i).getPriority()+"  "+K.get(i-1).getPriority());
                        if (K.get(i).getPriority() < K.get(i - 1).getPriority()) {
                            max = i - 1;
                           
                            break;
                        }

                    }
                    if (max == 0) {
                        num = 0;
                    } else {
                        num = r.nextInt(max);
                    }
                } else {
                    num = 0;
                }

                if (isProtokolPrint == true) {
                    area.append(" Обраний об'єкт  " + K.get(num).getName() + "\n" + " NextEvent " + "\n");
                }

                for (int i = 0; i < listObj.size(); i++) {
                    if (listObj.get(i).getNumObj() == K.get(num).getNumObj()) {
                        if (isProtokolPrint == true) {
                            area.append(" time =   " + t + "   Подія '" + listObj.get(i).getEventMin().getName() + "'\n"
                                    + "                       відбувається для об'єкту   " + listObj.get(i).getName() + "\n");
                        }
                        listObj.get(i).doT();
                        listObj.get(i).stepEvent();

                    }

                }
                if (isProtokolPrint == true) {
                    area.append("Вихід маркерів з переходів:");
                    for (PetriSim e : listObj) //ДРУК поточного маркірування
                    {
                        e.printMark(area);
                    }
                }
                for (PetriSim e : sortObj(listObj)) {
                    //можливо змінились умови для інших обєктів
                    e.start(); //вхід маркерів в переходи Петрі-об'єкта

                }
                if (isProtokolPrint == true) {
                    area.append("Вхід маркерів в переходи:");
                    for (PetriSim e : listObj) //ДРУК поточного маркірування
                    {
                        e.printMark(area);
                    }
                }
            }
        }
        area.append("\n Результати моделювання: \n");

        for (PetriSim e : listObj) {
            area.append("\n Петрі-об'єкт " + e.getName());
            area.append("\n Середні значення кількості маркерів у позиціях : ");
            for (PetriP P : e.getListPositionsForStatistica()) {
                area.append("\n  Позиція '" + P.getName() + "'  " + Double.toString(P.getMean()));
            }
            area.append("\n Середні значення кількості активних каналів переходів : ");
            for (PetriT T : e.getNet().getListT()) {
                area.append("\n Перехід '" + T.getName() + "'  " + Double.toString(T.getMean()));
            }
        }
    }

    private static ArrayList<PetriSim> sortObj(ArrayList<PetriSim> TT) {
        PetriSim aT;
        if (TT.size() > 1) {

            for (int i = 0; i < (TT.size() - 1); i++) {
                int max = i;
                for (int j = i + 1; j < TT.size(); j++) {

                    if (TT.get(j).getPriority() > TT.get(i).getPriority()) {
                        max = j;
                    }
                }

                if (max > i) {
                    aT = TT.get(max);
                    TT.set(max, TT.get(i));
                    TT.set(i, aT);
                }
            }
            // System.out.println("Значення найбільшого пріоритету = "+TT.get(0).getPriority()+this.getName()+TT.get(0).getProbability());
        }

        //    System.out.println("SORT"+TT.get(0));
        return TT;
    }
    /*     
     public void LinkObjectsByPlace(PetriSim one, int numberone, PetriSim other, int numberother)
     {
     one.getNet().getListP()[numberone] = other.getNet().getListP()[numberother];   //Тут здається все добре виходить
    
     }
   
    
     public void LinkObjectsByTransition(PetriSim one,int numberone, PetriSim other, int numberothe)
     {
            
     //Тут потрібно надавати можливість корегування програмного коду методу DoT() підкласу класу PetriSim.
     //Досить просто самостійно перевизначити цей метод в програмному коді підкласу. Але створити інтрефейс для цього, мабуть, важко.
     }
     */
}
