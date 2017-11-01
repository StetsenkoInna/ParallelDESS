package PetriObj;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.util.*;
import javax.swing.JTextArea;

/**
 * This class is Petri simulator. <br>
 * The object of this class recreates dynamics of functioning according to Petri
 * net, given in his data field. Such object is named Petri-object.
 *
 * @author Стеценко Інна
 */
public class PetriSim implements Serializable {
   
    private static double timeCurr=0;
    private static double timeMod = Double.MAX_VALUE - 1;
    
    
    private String name;
    private int numObj; //поточний номер створюваного об"єкта   //додано 6 серпня
    private static int next = 1; //лічильник створених об"єктів  //додано 6 серпня
    private int priority;
    private double timeMin;
 
    private int numP;
    private int numT;
    private int numIn;
    private int numOut;
    private PetriP[] ListP = new PetriP[numP];
    private PetriT[] ListT = new PetriT[numT];
    private ArcIn[] ListIn = new ArcIn[numIn];
    private ArcOut[] ListOut = new ArcOut[numOut];
    private PetriT eventMin;
    private PetriNet net;
    private ArrayList<PetriP> ListPositionsForStatistica = new ArrayList<PetriP>();
    //..... з таким списком статистика спільних позицій працює правильно...

    /**
     * Constructs the Petri simulator with given Petri net and time modeling
     *
     * @param pNet Petri net that describes the dynamics of object
     */
    public PetriSim(PetriNet pNet) {
        net = pNet;
        name = net.getName();
        numObj = next; 
        next++;        
        timeMin = Double.MAX_VALUE;
      
        ListP = net.getListP();
        ListT = net.getListT();
        ListIn = net.getTieIn();
        ListOut = net.getTieOut();
        numP = ListP.length;
        numT = ListT.length;
        numIn = ListIn.length;
        numOut = ListOut.length;
        eventMin = this.getEventMin();
        priority = 0;
        ListPositionsForStatistica.addAll(Arrays.asList(ListP));

    }

    /**
     *
     * @return PetriNet
     */
    public PetriNet getNet() {
        return net;
    }

    /**
     *
     * @return name of Petri-object
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return list of places for statistics which use for statistics
     */
    public ArrayList<PetriP> getListPositionsForStatistica() {
        return ListPositionsForStatistica;
    }

    /**
     * Get priority of Petri-object
     *
     * @return value of priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     *
     * @return the number of object
     */
    public int getNumObj() {
        return numObj;
    }

    /**
     * Set priority of Petri-object
     *
     * @param a value of priority
     */
    public void setPriority(int a) {
        priority = a;
    }

    /**
     * Set current time of Petri-object in value, given a parameter a
     *
     * @param a a value of current time
     */
  

    /**
     * This method uses for describing other actions associated with transition
     * markers output.<br>
     * Such as the output markers into the other Petri-object.<br>
     * The method is overridden in subclass.
     */
    public void doT() {

    }

    /**
     * Determines the next event and its moment.
     */
    public void eventMin() {
        PetriT event = null; //пошук часу найближчої події
        // якщо усі переходи порожні, то це означає зупинку імітації, 
        // отже за null значенням eventMin можна відслідковувати зупинку імітації
        double min = Double.MAX_VALUE;
        for (PetriT transition : ListT) {
            if (transition.getMinTime() < min) {
                event = transition;
                min = transition.getMinTime();
            }
        }
        timeMin = min;
        eventMin = event;
    }

    /**
     *
     * @return moment of next event
     */
    public double getTimeMin() {
        return timeMin;
    }

    /**
     * Finds the set of transitions for which the firing condition is true and
     * sorts it on priority value
     *
     * @return the sorted list of transitions with the true firing condition
     */
    public ArrayList<PetriT> findActiveT() {
        ArrayList<PetriT> aT = new ArrayList<PetriT>();

        for (PetriT transition : ListT) {
            if ((transition.condition(ListP) == true) && (transition.getProbability() != 0)) {
                aT.add(transition);

            }
        }

        if (aT.size() > 1) {
            aT = this.sortT(aT); // сортування за пріоритетом переходів
        }
        return aT;
    }

    /**
     * Do one event
     */
    public void step() { //один крок ,використовується для одного об'єкту мережа Петрі

        System.out.println("Next Step  " + "time=" + getTimeCurr());

        this.printMark();//друкувати поточне маркування
        ArrayList<PetriT> activeT = this.findActiveT();     //формування списку активних переходів

        if ((activeT.isEmpty() && isBufferEmpty() == true) || getTimeCurr() >= getTimeMod()) { //зупинка імітації за умови, що
                                                                                            //немає переходів, які запускаються,
         //   stop = true;                              // і немає маркерів у переходах, або вичерпаний час моделювання
            System.out.println("STOP in Net  " + this.getName());
            timeMin = getTimeMod();
            for (PetriP p : ListP) {
                p.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : ListT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу
        } else {
            while (activeT.size() > 0) { //вхід маркерів в переходи доки можливо

                this.doConflikt(activeT).actIn(ListP, getTimeCurr()); //розв'язання конфліктів
                activeT = this.findActiveT(); //оновлення списку активних переходів

            }

            this.eventMin();//знайти найближчу подію та ії час
            
            for (PetriP position : ListP) {
                position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : ListT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу

            if (getTimeCurr() <= getTimeMod()) {

                //Вихід маркерів
                eventMin.actOut(ListP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу

                if (eventMin.getBuffer() > 0) {
                    boolean u = true;
                    while (u == true) {
                        eventMin.minEvent();
                        if (eventMin.getMinTime() == getTimeCurr()) {

                            eventMin.actOut(ListP);
                            // this.printMark();//друкувати поточне маркування
                        } else {
                            u = false;
                        }
                    }
                    //   віддали з буфера переходу eventMin.getName()
                    // this.printMark();//друкувати поточне маркування
                }
                //Додано 6.08.2011!!!
                for (PetriT transition : ListT) {//ВАЖЛИВО!!Вихід з усіх переходів, що час виходу маркерів == поточний момент час.

                    if (transition.getBuffer() > 0 && transition.getMinTime() == getTimeCurr()) {
                        transition.actOut(ListP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу

                        // this.printMark();//друкувати поточне маркування
                        if (transition.getBuffer() > 0) {
                            boolean u = true;
                            while (u == true) {
                                transition.minEvent();
                                if (transition.getMinTime() == getTimeCurr()) {
                                    transition.actOut(ListP);
                                    // this.printMark();//друкувати поточне маркування
                                } else {
                                    u = false;
                                }
                            }

                            //   this.printMark();//друкувати поточне маркування
                        }
                    }
                }
            }

        }
    }

    public void step(JTextArea area) //один крок ,використовується для одного об'єкту мережа Петрі
    {
        area.append("\n Наступна подія, поточний час = " + getTimeCurr());

        this.printMark();//друкувати поточне маркування
        ArrayList<PetriT> activeT =  this.findActiveT();     //формування списку активних переходів
        for (PetriT T : activeT) {
            area.append("\nСписок переходів з виконаною умовою запуску " + T.getName());
        }
        if ((activeT.isEmpty() && isBufferEmpty() == true) || getTimeCurr() >= getTimeMod()) { //зупинка імітації за умови, що
            //не має переходів, які запускаються,
          //  stop = true;                              // і не має фішок в переходах або вичерпаний час моделювання
            area.append("\n STOP, немає активних переходів і не виконана умова запуску для жодного з переходів " + this.getName());
            timeMin = getTimeMod();
            for (PetriP position : ListP) {
                position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : ListT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу
        } else {

            while (activeT.size() > 0) {      //вхід маркерів в переходи доки можливо

                area.append("\n Вибір переходу для запуску " + this.doConflikt(activeT).getName());
                this.doConflikt(activeT).actIn(ListP, getTimeCurr()); //розв'язання конфліктів
                activeT = this.findActiveT(); //оновлення списку активних переходів
            }
            area.append("\n Вхід маркерів в переходи:");
            this.printMark(area);//друкувати поточне маркування

            this.eventMin();//знайти найближчу подію та ії час
            for (PetriP position : ListP) {
                position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : ListT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу

            if (getTimeCurr() <= getTimeMod()) {

                area.append("\n поточний час =" + getTimeCurr() + "   " + eventMin.getName());
                //Вихід маркерів
                eventMin.actOut(ListP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                area.append("\n Вихід маркерів з переходу " + eventMin.getName());
                this.printMark(area);//друкувати поточне маркування

                if (eventMin.getBuffer() > 0) {

                    boolean u = true;
                    while (u == true) {
                        eventMin.minEvent();
                        if (eventMin.getMinTime() == getTimeCurr()) {
                            // System.out.println("MinTime="+TEvent.getMinTime());
                            eventMin.actOut(ListP);
                            // this.printMark();//друкувати поточне маркування
                        } else {
                            u = false;
                        }
                    }
                    area.append("Вихід маркерів з буфера переходу " + eventMin.getName());
                    this.printMark(area);//друкувати поточне маркування
                }
                //Додано 6.08.2011!!!
                for (PetriT transition : ListT) { //ВАЖЛИВО!!Вихід з усіх переходів, що час виходу маркерів == поточний момент час.
                    if (transition.getBuffer() > 0 && transition.getMinTime() == getTimeCurr()) {
                        transition.actOut(ListP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                        area.append("\n Вихід маркерів з переходу " + transition.getName());
                        this.printMark(area);//друкувати поточне маркування
                        if (transition.getBuffer() > 0) {
                            boolean u = true;
                            while (u == true) {
                                transition.minEvent();
                                if (transition.getMinTime() == getTimeCurr()) {
                                    // System.out.println("MinTime="+TEvent.getMinTime());
                                    transition.actOut(ListP);
                                    // this.printMark();//друкувати поточне маркування
                                } else {
                                    u = false;
                                }
                            }
                            area.append("\n Вихід маркерів з буфера переходу " + transition.getName());
                            this.printMark(area);//друкувати поточне маркування
                        }
                    }
                }
            }
        }
        /* area.append("\n                 СТАТИСТИКА");
         area.append("\n Cередні значення кількості маркерів у позиціях:");
         for (PetriP P: this.ListP)
         area.append("\n "+P.getName()+"  "+Double.toString(P.getMean()));
         area.append("\n Cередні значення кількості активних каналів переходів:");
         for(PetriT T: this.ListT)
         area.append("\n "+T.getName()+"  "+Double.toString(T.getMean()));*/
    }

    /**
     * It does the first transitions input markers
     */
    public void start() {//вхід маркерів в переходи Петрі-об'єкта

        ArrayList<PetriT> activeT = this.findActiveT();     //формування списку активних переходів

        if (activeT.isEmpty() && isBufferEmpty() == true) { //зупинка імітації за умови, що
            //не має переходів, які запускаються,
           // stop = true;                              // і не має фішок в переходах

            timeMin = Double.MAX_VALUE;
      //3.12.15      timeCurr =timeMin;         //просування часу
        } else {
          //  stop = false;
            while (activeT.size() > 0) { //запуск переходів доки можливо
                 this.doConflikt(activeT).actIn(ListP, getTimeCurr()); //розв'язання конфліктів
                activeT = this.findActiveT(); //оновлення списку активних переходів
            }

            this.eventMin();//знайти найближчу подію та ії час
        }
    }

    /**
     * It does the transitions input and output markers in the current moment
     */
    public void stepEvent() {  //один крок,вихід та вхід маркерів в переходи Петрі-об"єкта, використовується для множини Петрі-об'єктів
        if(isStop()){
            timeMin = getTimeMod() + 1;
           // timeCurr = timeMin;        //просування часу
            return; //зупинка імітації
        }
        if (getTimeCurr() <= getTimeMod()) {

            eventMin.actOut(ListP);//здійснення події

            // this.printMark();//друкувати поточне маркування
            if (eventMin.getBuffer() > 0) {
                boolean u = true;
                while (u == true) {
                    eventMin.minEvent();
                    if (eventMin.getMinTime() == getTimeCurr()) {
                        eventMin.actOut(ListP);
                    } else {
                        u = false;
                    }
                }

            }
            //Додано 6.08.2011!!!
            for (PetriT transition : ListT) { //ВАЖЛИВО!!Вихід з усіх переходів, що час виходу маркерів == поточний момент час.
            
                if (transition.getBuffer() > 0 && transition.getMinTime() == getTimeCurr()) {
                    transition.actOut(ListP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                    
                   
                    
                    if (transition.getBuffer() > 0) {
                        boolean u = true;
                        while (u == true) {
                            transition.minEvent();
                            if (transition.getMinTime() == getTimeCurr()) {
                                transition.actOut(ListP);
                                // this.printMark();//друкувати поточне маркування
                            } else {
                                u = false;
                            }
                        }
                    }
                }
            }

        }

        ArrayList<PetriT> activeT = this.findActiveT();     //формування списку активних переходів

        if (activeT.isEmpty() && isBufferEmpty() == true) { //зупинка імітації за умови, що
                                                    //не має переходів, які запускаються,
           // stop = true;                              // і не має маркерів у переходах

            timeMin = getTimeMod() + 1;
            eventMin = null;
          //  timeCurr = timeMin;        //просуванням часу маэ управляти модель 
        } else {
          //  stop = false;
            while (activeT.size() > 0) {//запуск переходів доки можливо
            
                this.doConflikt(activeT).actIn(ListP, getTimeCurr()); //розв'язання конфліктів
                activeT = this.findActiveT(); //оновлення списку активних переходів
            }

            this.eventMin();//знайти найближчу подію та ії час

        }
    }

    /**
     * Calculates mean value of quantity of markers in places and quantity of
     * active channels of transitions
     */
    public void doStatistica() {
        for (PetriP position : ListP) {
            position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
        }
        for (PetriT transition : ListT) {
            transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
        }

    }

    /**
     *
     * @param dt - the time interval
     */
    public void doStatistica(double dt) {
        if (dt > 0) {
            for (PetriP position : ListPositionsForStatistica) {
                position.changeMean(dt);
            }
        }
        if (dt > 0) {
            for (PetriT transition : ListT) {
                transition.changeMean(dt);
            }
        }
    }

    /**
     * This method use for simulating Petri-object
     */
    public void go() {
        setTimeCurr(0);

        while (getTimeCurr() <= getTimeMod() && isStop() == false) {
            PetriSim.this.step();
            if (isStop() == true) {
                System.out.println("STOP in net  " + this.getName());
            }
            this.printMark();//друкувати поточне маркування
        }
    }

    /**
     * This method use for simulating Petri-object until current time less then
     * the momemt in parametr time
     *
     * @param time - the simulation time
     */
    public void go(double time) {

        while (getTimeCurr() < time && isStop() == false) {
            step();
            if (isStop() == true) {
                System.out.println("STOP in net  " + this.getName());
            }
            // this.printMark();//друкувати поточне маркування
        }
    }

    public void go(double time, JTextArea area) {

        while (getTimeCurr() < time && isStop() == false) {
            step(area);
            if (isStop() == true) {
                area.append("STOP in net  " + this.getName());
            }
            // this.printMark();//друкувати поточне маркування
        }
    }

    /**
     * Determines is all of transitions has empty buffer
     *
     * @return true if buffer is empty for all transitions of Petri net
     */
    public boolean isBufferEmpty() {
        boolean c = true;
        for (PetriT e : ListT) {
            if (e.getBuffer() > 0) {
                c = false;
                break;
            }
        }
        return c;
    }

    /**
     * Do printing the current marking of Petri net
     */
    public void printMark() {
        System.out.print("Mark in Net  " + this.getName() + "   ");
        for (PetriP position : ListP) {
            System.out.print(position.getMark() + "  ");
        }
        System.out.println();
    }
    public void printBuffer(){
    System.out.print("Buffer in Net  " + this.getName() + "   ");
        for (PetriT transition : ListT) {
            System.out.print(transition.getBuffer() + "  ");
        }
        System.out.println();
    }
    public void printMark(JTextArea area) {
        area.append("\n Mark in Net  " + this.getName() + "   \n");
        for (PetriP position : ListP) {
            area.append(position.getMark() + "  ");
        }
        area.append("\n");
    }

    /**
     *
     * @return time modeling
     */
  /*  public static double getTimeMod() {
        return timeMod;
    }
*/
    /**
     * @param aTimeMod the timeMod to set
     */
/*    public static void setTimeMod(double aTimeMod) {
        timeMod = aTimeMod;
    }
*/
    /**
     *
     * @return current time
     */
 /*   public double getTimeCurr() {
        return timeCurr;
    }
*/
   
    /**
     *
     * @return the nearest event
     */
    public final PetriT getEventMin() {
        this.eventMin();
        return eventMin;
    }

    /**
     * This method solves conflict between transitions given in parametr TT
     *
     * @param TT the list of transitions
     * @return the transition - winner of conflict
     */
    public PetriT doConflikt(ArrayList<PetriT> TT) {//
        PetriT aT = TT.get(0);
        if (TT.size() > 1) {
            aT = TT.get(0);
            int i = 0;
            while (i < TT.size() && TT.get(i).getPriority() == aT.getPriority()) {
                i++;
            }
            if (i == 1)
             ; else {
                double r = Math.random();
                int j = 0;
                double sum = 0;
                double prob;
                while (j < TT.size() && TT.get(j).getPriority() == aT.getPriority()) {

                    if (TT.get(j).getProbability() == 1.0) {
                        prob = 1.0 / i;
                    } else {
                        prob = TT.get(j).getProbability();
                    }

                    sum = sum + prob;
                    if (r < sum) {
                        aT = TT.get(j);
                        break;
                    } //вибір переходу за значенням ймовірності
                    else {
                        j++;
                    }
                }
            }
        }

        return aT;

    }

    /**
     * This method sorts the list of transitions by its priorities
     *
     * @param TT the list of transitions for sorting
     * @return the sorted list of transitions
     */
    public ArrayList<PetriT> sortT(ArrayList<PetriT> TT) {//сортування списку активних переходів за спаданням пріоритету
        PetriT aT;
        if (TT.size() > 1) {

            for (int i = 0; i < (TT.size() - 1); i++) {
                int max = i;
                int maxPriority = TT.get(i).getPriority(); //Додано 10.11.11 ВАЖЛИВО!!!
                for (int j = i + 1; j < TT.size(); j++) {
                    if (TT.get(j).getPriority() > maxPriority) {
                        max = j;
                        maxPriority = TT.get(j).getPriority();
                    } //Додано 10.11.11 ВАЖЛИВО!!!
                }
                if (max > i) {
                    aT = TT.get(max);
                    TT.set(max, TT.get(i));
                    TT.set(i, aT);
                }
            }
        }
        return TT;
    }

    /**
     * @return the stop
     */
    public boolean isStop() {
        this.eventMin();
        return (eventMin==null);
    }
    
      /**
     * @return the timeCurr
     */
    public static double getTimeCurr() {
        return timeCurr;
    }

    /**
     * @param aTimeCurr the timeCurr to set
     */
    public static void setTimeCurr(double aTimeCurr) {
        timeCurr = aTimeCurr;
    }

    /**
     * @return the timeMod
     */
    public static double getTimeMod() {
        return timeMod;
    }

    /**
     * @param aTimeMod the timeMod to set
     */
    public static void setTimeMod(double aTimeMod) {
        timeMod = aTimeMod;
    }

   

}
