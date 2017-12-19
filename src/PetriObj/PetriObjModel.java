package PetriObj;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class provides constructing Petri objective model.<br>
 * List of Petri-objects contains Petri-objects with links between them.<br>
 * For creating Petri-object use class PetriSim. For linking Petri-objects use
 * combining places and passing tokens.<br>
 * Method DoT() of class PetriSim provides programming the passing tokens from
 * the transition of one Petri-object to the place of other.
 *
 * @author Inna V. Stetsenko
 */
public class PetriObjModel implements Serializable {

    private ArrayList<PetriSim> listObj = new ArrayList<>();
    private double t;
    private boolean isProtokolPrint = true;
    private boolean isStatistica = true;
    
    private ModelingTimeState timeState;

    /**
     * Constructs model with given list of Petri objects
     *
     * @param listObj list of Petri objects
     */
    public PetriObjModel(ArrayList<PetriSim> listObj) {
        this(listObj, new ModelingTimeState());
    }
    
    public PetriObjModel(ArrayList<PetriSim> listObj, ModelingTimeState timeState) {
        this.listObj = listObj;
        this.timeState = timeState;
        
        this.listObj.forEach(sim -> sim.setTimeState(timeState));
    }

    /**
     * Set need in protokol
     *
     * @param b is true if protokol is needed
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
     * @return the timeMod
     */
    public double getTimeMod() {
        return timeState.getModelingTime();
    }

    /**
     * @param timeMod the timeMod to set
     */
    public void setTimeMod(double timeMod) {
        timeState.setModelingTime(timeMod);
    }

    public double getTimeMinLocal() {
        double min = Double.MAX_VALUE;

        for (PetriSim sim : this.getListObj()) {
            if (min > sim.getTimeLocal()) {
                min = sim.getTimeLocal();
            }
        }
        return min;
    }
    
    public double getNextEventTime() {   // added by Inna 18.04.16
        double min = listObj.get(0).getTimeMin();  //пошук найближчої події

        for (PetriSim e : listObj) {
            if (e.getTimeMin() < min) {
                min = e.getTimeMin();
            }
        }
        return min;
    }

    public void modelInput() {
        sortObjParallel(listObj).parallelStream().forEach(PetriSim::input);
    }

    private PetriSim chooseObj(ArrayList<PetriSim> arrayK) {
        int num;
        int max;

        if (arrayK.size() > 1) { //вибір обєкта, що запускається
            max = arrayK.size();
            sortObjParallel(arrayK);

            for (int i = 1; i < arrayK.size(); i++) {
                if (arrayK.get(i).getPriority() < arrayK.get(i - 1).getPriority()) {
                    max = i - 1;
                    break;
                }
            }
            if (max == 0) {
                num = 0;
            } else {
                num = new Random().nextInt(max);
            }
        } else {
            num = 0;
        }
        return arrayK.get(num);
    }

    public void parallelGo(double timeModeling) { //added by Inna 18.04.16

        timeState.setModelingTime(timeModeling);

        t = 0;
        double min;

        if (isProtokolPrint == true) {
            System.out.println(" Початкове маркірування об'єктів: ");
            this.printMark();
        }

        ArrayList<PetriSim> conflictObj = new ArrayList<>();  //список конфліктних обєктів
        while (t < timeModeling) {
            conflictObj.clear();
            modelInput(); //можливо змінились умови для інших обєктів
            if (isProtokolPrint == true) {
                System.out.println("Вхід маркерів в переходи:");
                this.printMark();//ДРУК поточного маркірування
            }

            min = getNextEventTime(); //пошук найближчої події
            if (isStatistica == true) {
                for (PetriSim e : listObj) {
                    e.doStatistics((min - t) / min); //статистика за час "дельта т", для спільних позицій потрібно статистику збирати тільки один раз!!!
                }
            }

            t = min; // просування часу

            timeState.setCurrentTime(t); // просування часу //3.12.2015

            if (isProtokolPrint == true) {
                System.out.println(" Просування часу time = " + t + "\n");
            }
            if (t <= timeModeling) {

                for (PetriSim e : listObj) {
                    if (t == e.getTimeMin()) {
                        conflictObj.add(e);
                    }
                }

                if (isProtokolPrint == true) {
                    System.out.println(" Список конфліктних обєктів  " + "\n");
                    for (int ii = 0; ii < conflictObj.size(); ii++) {
                        System.out.println(" K [ " + ii + "  ] = " + conflictObj.get(ii).getName() + "\n");
                    }
                }

                PetriSim chosen = chooseObj(conflictObj);
                if (isProtokolPrint == true) {
                    System.out.println(" Обраний об'єкт  " + chosen.getName() + "\n" + " NextEvent " + "\n"
                            + " \n time =   " + t + "   Подія '" + chosen.getEventMin().getName() + "'\n"
                            + "                       відбувається для об'єкту   " + chosen.getName() + "\n");
                }

                chosen.doT();
                chosen.stepEvent(); // виконання події

                if (isProtokolPrint == true) {
                    System.out.println("Вихід маркерів з переходів:");
                    this.printMark();//ДРУК поточного маркірування
                }

            }
        }
    }

    /**
     * Simulating from zero time until the time equal time modeling
     *
     * @param timeModeling time modeling
     */
    public void go(double timeModeling) {

        timeState.setModelingTime(timeModeling);

        t = 0;
        double min;

        sortObjParallel(listObj).forEach(PetriSim::input);//виправлено 4.4.2016 Богдан
        if (isProtokolPrint == true) {
            listObj.forEach(PetriSim::printMark);
        }

        ArrayList<PetriSim> K = new ArrayList<>();
        Random r = new Random();

        while (t < timeModeling) {
            K.clear();

            min = getNextEventTime(); //пошук найближчої події
            if (isStatistica == true) {
                for (PetriSim e : listObj) {
                    e.doStatistics((min - t) / min); //статистика за час "дельта т", для спільних позицій потрібно статистику збирати тільки один раз!!!
                }
            }

            t = min; // просування часу

            timeState.setCurrentTime(t); // просування часу //3.12.2015

            if (isProtokolPrint == true) {
                System.out.println(" Просування часу time = " + t + "\n");
            }
            if (t <= timeModeling) {

                for (PetriSim e : listObj) {
                    if (t == e.getTimeMin()) { // розв'язання конфлікту об'єктів рівноймовірнісним способом
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

                    sortObjParallel(K); //параллельне сортування. Богдан 4.04.2016.
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

                for (PetriSim e : listObj) {
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
                    listObj.forEach(PetriSim::printMark); //ДРУК поточного маркірування
                }

                //можливо змінились умови для інших обєктів
                //вхід маркерів в переходи Петрі-об'єкта
                sortObj(listObj).forEach(PetriSim::input);

                if (isProtokolPrint == true) {
                    System.out.println("Вхід маркерів в переходи:");

                    //ДРУК поточного маркірування
                    listObj.forEach(PetriSim::printMark);
                }
            }
        }
    }

    public void go(double timeModeling, JTextArea area) { //виведення протоколу подій та результатів моделювання у об"єкт класу JTextArea
        area.setText(" Events protokol ");
        timeState.setModelingTime(timeModeling);
        t = 0;
        double min;
        sortObj(listObj).forEach(PetriSim::input); //виправлено 9.11.2015
        

        ArrayList<PetriSim> K = new ArrayList<>();
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
                        e.doStatistics((min - t) / min); //статистика за час "дельта т", для спільних позицій потрібно статистику збирати тільки один раз!!!
                    }
                }
            }

            t = min; // просування часу

            timeState.setCurrentTime(t); // просування часу //3.12.2015

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
                    
                    //ДРУК поточного маркірування
                    listObj.forEach((e) -> e.printMark(area)); 
                }
                
                //можливо змінились умови для інших обєктів
                //вхід маркерів в переходи Петрі-об'єкта
                sortObj(listObj).forEach(PetriSim::input);
                
                if (isProtokolPrint == true) {
                    area.append("Вхід маркерів в переходи:");
                    listObj.forEach((e) -> e.printMark(area)); //ДРУК поточного маркірування
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

    public static ArrayList<PetriSim> sortObj(ArrayList<PetriSim> TT) {

        TT.sort((o1, o2) -> {
            if (o1.getPriority() < o2.getPriority()) {
                return 1; //сортування у спадаючому порядку!!!
            } else if (o1.getPriority() > o2.getPriority()) {
                return -1;
            } else {
                return 0;
            }
        });
        //    System.out.println("SORT"+TT.get(0));
        return TT;
    }

    private static ArrayList<PetriSim> sortObjParallel(ArrayList<PetriSim> TT) {

        //PetriSim[] temp = (PetriSim[])TT.toArray();  
        PetriSim[] temp = new PetriSim[TT.size()];
        for (int i = 0; i < TT.size(); i++) {
            temp[i] = TT.get(i);
        }

        if (TT.size() > 1) {
            Arrays.parallelSort(temp, (o1, o2) -> { // виправлена помилка = сортування має бути у спадаючому порядку
                if (o1.getPriority() < o2.getPriority()) {
                    return 1; // '>' was changed by Inna on '<'
                } else if (o1.getPriority() > o2.getPriority()) {
                    return -1; // '<' was changed by Inna on '>'
                } else {
                    return 0;
                }
            });
        }
        TT.clear();
        TT.addAll(Arrays.asList(temp));
        return TT;
    }

    // Output
    
    public void printState() {
        listObj.forEach(PetriSim::printState);
    }
    
    public void printMark() { //ДРУК поточного маркірування
        listObj.forEach(PetriSim::printMark);
    }

    public void printBuffer() { //ДРУК поточного маркірування
        listObj.forEach(PetriSim::printBuffer);
    }
}
