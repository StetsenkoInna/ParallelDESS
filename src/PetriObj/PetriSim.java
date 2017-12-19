package PetriObj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class is Petri simulator. <br>
 * The object of this class recreates dynamics of functioning according to Petri
 * net, given in his data field. Such object is named Petri-object.
 *
 * @author Inna V. Stetsenko
 */
public class PetriSim implements Serializable, Runnable {

    // TODO: move testing logic out of the domain logic
    private final static String OUT_DIR = "./statistics";

    static {
        new File(OUT_DIR).mkdirs();
    }

    private ModelingTimeState timeState;
    private double timeLocal;
    
    private PetriNet net;
    private String name;
    private int numObj; //поточний номер створюваного об"єкта   //додано 6 серпня
    private static int next = 1; //лічильник створених об"єктів  //додано 6 серпня
    private int priority;
    private double timeMin;

    private PetriP[] listP;
    private PetriT[] listT;
    private ArcIn[] listIn;
    private ArcOut[] listOut;

    private PetriT eventMin;
    private ArrayList<PetriP> ListPositionsForStatistica = new ArrayList<>();
    //..... з таким списком статистика спільних позицій працює правильно...

    private PetriSim previousObj;
    private PetriSim nextObj;
    
    private final Lock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition(); //empty buff of external events
    private final Condition condLimitEvents = lock.newCondition(); //

    private List<Double> timeExternalInput = Collections.synchronizedList(new ArrayList<>());

    private ArrayList<PetriT> outT = new ArrayList<>(); // переходи, які спричиняють зміну стану спільних позицій, або позицій інших об'єктів
    private ArrayList<PetriT> inT = new ArrayList<>(); // переходи, які сприймають зміну стану спільних позицій, або позицій інших об'єктів
    private static int limitArrayExtInputs = 10;

    private int counter = 0; // count +1 if reinstant and -1 if rollback

    private RandomAccessFile f;

    /**
     * Constructs the Petri simulator with given Petri net and time modeling
     *
     * @param net Petri net that describes the dynamics of object
     */
    public PetriSim(PetriNet net) {
        this(net, new ModelingTimeState());
    }
    
    public PetriSim(PetriNet net, ModelingTimeState timeState) {
        this.net = net;
        this.timeState = timeState;
        
        name = net.getName();
        numObj = next;
        next++;
        timeMin = Double.MAX_VALUE;
        timeLocal = timeState.getCurrentTime();

        listP = net.getListP();
        listT = net.getListT();
        listIn = net.getArcIn();
        listOut = net.getArcOut();

        eventMin = getEventMin();
        priority = 0;
        ListPositionsForStatistica.addAll(Arrays.asList(listP));
        try {
            f = new RandomAccessFile(new File(OUT_DIR + "/simTestData" + numObj + ".txt"), "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PetriSim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void setTimeState(ModelingTimeState timeState) {
        this.timeState = timeState;
    }
    
    /**
     * @return the timeCurr
     */
    public double getTimeCurr() {
        return timeState.getCurrentTime();
    }

    /**
     * Sets modeling current time
     * 
     * @param currentTime time to set
     */
    @Deprecated
    public void setTimeCurr(double currentTime) {
        timeState.setCurrentTime(currentTime);
    }

    /**
     * @return the timeMod
     */
    public double getTimeMod() {
        return timeState.getModelingTime();
    }

    /**
     * @param modelingTime the modeling time to set
     */
    @Deprecated
    public void setTimeMod(double modelingTime) {
        timeState.setModelingTime(modelingTime);
    }
    
    /**
     * @return the timeLocal
     */
    public double getTimeLocal() {
        return timeLocal;
    }

    /**
     * @param tLocal the timeLocal to set
     */
    public void setTimeLocal(double tLocal) {
        this.timeLocal = tLocal;
    }
    
    
    /**
     * @return PetriNet
     */
    public PetriNet getNet() {
        return net;
    }

    /**
     * @return name of Petri-object
     */
    public String getName() {
        return name;
    }

    
    /**
     * @return the listP
     */
    public PetriP[] getListP() {
        return listP;
    }

    /**
     * @return the listT
     */
    public PetriT[] getListT() {
        return listT;
    }

    /**
     * @return the listIn
     */
    public ArcIn[] getListIn() {
        return listIn;
    }

    /**
     * @return the listOut
     */
    public ArcOut[] getListOut() {
        return listOut;
    }

    
    /**
     * @return the outT
     */
    public ArrayList<PetriT> getOutT() {
        return outT;
    }

    /**
     * @param outT the outT to set
     */
    public void setOutT(ArrayList<PetriT> outT) {
        this.outT = outT;
    }

    public void addOutT(PetriT transition) {
        getOutT().add(transition);
    }
    
    
    /**
     * @return the inT
     */
    public ArrayList<PetriT> getInT() {
        return inT;
    }

    /**
     * @param inT the inT to set
     */
    public void setInT(ArrayList<PetriT> inT) {
        this.inT = inT;
    }

    public void addInT(PetriT transition) {
        getInT().add(transition);
    }
    
    
    /**
     * @return list of places for statistics which use for statistics
     */
    public ArrayList<PetriP> getListPositionsForStatistica() {
        return ListPositionsForStatistica;
    }

    /**
     * @param ListPositionsForStatistica the ListPositionsForStatistica to set
     */
    public void setListPositionsForStatistica(ArrayList<PetriP> ListPositionsForStatistica) {
        this.ListPositionsForStatistica = ListPositionsForStatistica;
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
     * Set priority of Petri-object
     *
     * @param a value of priority
     */
    public void setPriority(int a) {
        priority = a;
    }
    
    /**
     * @return moment of next event
     */
    public double getTimeMin() {
        return timeMin;
    }
    
    /**
     * @param timeMin the timeMin to set
     */
    public void setTimeMin(double timeMin) {
        this.timeMin = timeMin;
    }

    /**
     * @return the number of object
     */
    public int getNumObj() {
        return numObj;
    }

    
    /**
     * @return the previousObj
     */
    public PetriSim getPreviousObj() {
        return previousObj;
    }

    /**
     * @param previousObj the previousObj to set
     */
    public void setPreviousObj(PetriSim previousObj) {
        this.previousObj = previousObj;
    }

    /**
     * @return the nextObj
     */
    public PetriSim getNextObj() {
        return nextObj;
    }

    /**
     * @param nextObj the nextObj to set
     */
    public void setNextObj(PetriSim nextObj) {
        this.nextObj = nextObj;
    }

    
    /**
     * @return the timeExternalInput
     */
    public synchronized List<Double> getTimeExternalInput() {
        return timeExternalInput;
    }

    public synchronized double getFirstTimeExternalInput() {
        return getTimeExternalInput().get(0);
    }

    public synchronized double getLastTimeExternalInput() { // значно покращило роботу алгоритму!
        return getTimeExternalInput().get(getTimeExternalInput().size() - 1);
    }
    
    /**
     * @param timeExternalInput the timeExternalInput to set
     */
    public synchronized void setTimeExternalInput(ArrayList<Double> timeExternalInput) {
        this.timeExternalInput = timeExternalInput;
    }

    public void addTimeExternalInput(double t) {
        getLock().lock();
        try {
            getTimeExternalInput().add(t);
            getCond().signal();
        } finally {
            getLock().unlock();
        }
    }

    public void removeTimeExternalInput() {
        getTimeExternalInput().remove(0); //видалення зі списку запланованих подій

        if (getTimeExternalInput().size() <= getLimitArrayExtInputs()) { // надаємо можливість об єкту напрацювати події
            previousObj.getLock().lock();
            try {
                previousObj.getCondLimitEvents().signal();
            } finally {
                previousObj.getLock().unlock();
            }
        }
    }
    
    
    /**
     * @return the lock
     */
    public Lock getLock() {
        return lock;
    }

    /**
     * @return the cond
     */
    public Condition getCond() {
        return cond;
    }

    /**
     * @return the condLimitEvents
     */
    public Condition getCondLimitEvents() {
        return condLimitEvents;
    }
    
    /**
     * @return the limitArrayExtInputs
     */
    public static int getLimitArrayExtInputs() {
        return limitArrayExtInputs;
    }

    /**
     * @param aLimitArrayExtInputs the limitArrayExtInputs to set
     */
    public static void setLimitArrayExtInputs(int aLimitArrayExtInputs) {
        limitArrayExtInputs = aLimitArrayExtInputs;
    }
    
    
    /**
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }
    
    public void increaseCounter() {
        counter++;
    }

    public void decreaseCounter() {
        counter--;
    }
    
    
    /**
     * @return the nearest event
     */
    public final PetriT getEventMin() {
        eventMin();
        return eventMin;
    }
    
    /**
     * Determines the next event and its moment.
     */
    public void eventMin() {
        PetriT event = null; //пошук часу найближчої події
        // якщо усі переходи порожні, то це означає зупинку імітації, 
        // отже за null значенням eventMin можна відслідковувати зупинку імітації
        double min = Double.MAX_VALUE;
        for (PetriT transition : getListT()) {
            if (transition.getMinTime() < min) {
                min = transition.getMinTime();
                event = transition;
            }
        }

        setTimeMin(min);
        eventMin = event;
        //  System.out.println(name+ "in eventMin() we have timeMin =  "+timeMin );
    }
    
    
    /**
     * @return the stop
     */
    public boolean isStop() {
        // rewroten for parallel by Inna 20.05.2016
        for (PetriT transition : listT) {
            if (transition.condition(listP) || transition.getBuffer() > 0) {
                return false;
            }
        }
        if (previousObj != null && !timeExternalInput.isEmpty()) {
            return false;
        }
        if (nextObj != null) {
            if (nextObj.getTimeExternalInput().size() >= getLimitArrayExtInputs()) { //дати можливість іншим об єктам відпрацювати накопичені події
                return false;
            }
        }
        return true;
    }
    
    /**
     * @return the stop
     */
    public boolean isStopSerial() {
        this.eventMin();
        return (eventMin == null);
    }
    
    /**
     * Determines is all of transitions has empty buffer
     *
     * @return true if buffer is empty for all transitions of Petri net
     */
    public boolean isBufferEmpty() {
        boolean c = true;
        for (PetriT e : getListT()) {
            if (e.getBuffer() > 0) {
                c = false;
                break;
            }
        }
        return c;
    }
    
    private double getLimitTime(){
        if (previousObj != null) {
            this.getLock().lock();
            try {
                while (getTimeExternalInput().isEmpty()) {
                    this.getCond().await();
                }
                //встановлення інтервалу імітації до моменту події входу маркерів в об'єкт ззовні
                return Math.min(getFirstTimeExternalInput(), getTimeMod());
            } catch (InterruptedException ex) {
                Logger.getLogger(PetriSim.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.getLock().unlock();
            }
        }
        return getTimeMod();
    }
    
    @Override
    public void run() { //step Event in Parallel, by Inna 19.05.16
        while (getTimeLocal() < getTimeMod()) {
            double limitTime = getTimeMod(); //для об єкта без зовнішніх подій

            if (previousObj != null) {
                this.getLock().lock();
                try {
                    while (getTimeExternalInput().isEmpty()) {
                        this.getCond().await();
                    }

                    limitTime = getFirstTimeExternalInput(); //встановлення інтервалу імітації до моменту події входу маркерів в об'єкт ззовні
                    if (limitTime > getTimeMod()) {
                        limitTime = getTimeMod(); //6.07.2016
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PetriSim.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    this.getLock().unlock();
                }
            } else { // previousObj == null
                limitTime = getTimeMod();
            }

            if (getTimeLocal() < limitTime) {    //<=????
                //   System.out.println(this.getName() + " will go until " + limitTime + ", have local time " + getTimeLocal());
                goUntil(limitTime);
                //   System.out.println(this.getName()+" counter = "+this.getCounter());

                //  System.out.println(beginWait.get(beginWait.size()-1)
                //                  +"  "+endWait.get(endWait.size()-1));
            } else { // повідомлення про неправильне визначення limitTime
                // System.out.println(this.getName() + " will not go until " + limitTime + ", have local time " + getTimeLocal() + ">= time modeling!");
                return;
            }
        }
        // System.out.println(this.getName() + " have finished simulation:");
        //  this.printState();
    }
    
    public void goUntil(double limit) {
        while (getTimeLocal() < limit) {  //просування часу в межах відведеного інтервалу
            while (isStop()) { // перевірка передумов запуску input
                lock.lock();
                try {
                    // System.out.println(this.getName() + " is waiting for input....  ");
                    this.getCond().await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PetriSim.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    lock.unlock();
                }
            }

            input(); // змінився timeMin
            //   System.out.println(this.getName() + " did input, new value of timeMin =  " + getTimeMin() + " limitTime = " + limit);
            if (getTimeMin() < limit) {
                moveTimeLocal(getTimeMin());
                output(); //
                //    System.out.println(this.getName() + " was done output, new value of timeLocal =  " + getTimeLocal());
            } else if (limit >= getTimeMod()) { // усі внутрішні події вичерпані, залишалась зовнішня - на кінці безпечного інтервалу
                moveTimeLocal(getTimeMod());
                if (nextObj != null) {
                    nextObj.addTimeExternalInput(Double.MAX_VALUE); // тобто не очікуємо подій ззовні
                }
            } else if (previousObj != null) {
                if (getTimeExternalInput().isEmpty() || getLastTimeExternalInput() < Double.MAX_VALUE) {
                    while (getTimeExternalInput().isEmpty()) {
                        this.getLock().lock();
                        try {
                            //      System.out.println(this.getName() + " is waiting for timeExternalInput ....  " );
                            this.getCond().await();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PetriSim.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            this.getLock().unlock();
                        }
                    }
                }
                if (getFirstTimeExternalInput() > getTimeMod()) {
                    moveTimeLocal(getTimeMod());

                    if (nextObj != null) {
                        nextObj.addTimeExternalInput(Double.MAX_VALUE); // тобто не очікуємо подій ззовні
                    }
                } else {
                    moveTimeLocal(limit);
                    //*****************
                    reinstateActOut(previousObj.getOutT().get(0), previousObj.getListP()[previousObj.getListP().length - 1]); //вихід у спільну позицію, припущення: вона задана останньою у списку позицій !!!
                    //*****************
                    removeTimeExternalInput(); //видалення зі списку запланованих подій
                }
            } else {
                moveTimeLocal(limit);
            }
        }
    }
    
    /**
     * It does the transitions input markers
     *
     */
    public void input() { //вхід маркерів в переходи Петрі-об'єкта added by Inna 15.05.2016
        //вхід в уcі переходи, які не використовують спільні позиції

        ArrayList<PetriT> activeT = this.findActiveT(); //формування списку активних переходів
        if (activeT.isEmpty() && isBufferEmpty()) {     //зупинка імітації за умови, що
            setTimeMin(Double.MAX_VALUE);
        } else {
            while (activeT.size() > 0) { // запуск переходів доки можливо та доки відсутній невчасний вхід в перехід
                // невчасні визанчаються у методі findActiveT і не заносяться до списку активних
                PetriT transition = this.doConflict(activeT);

                transition.actIn(getListP(), getTimeLocal()); //розв'язання конфліктів, здійснення входу в перехід
                activeT = this.findActiveT(); //оновлення списку активних переходів
            }
            this.eventMin();//знайти найближчу подію та ії час
        }
    }

    /**
     * It does the transitions output markers
     *
     */
    public void output() { //added by Inna 15.05.2016, rewroten 17.05.2016-2.06.2016
        if (nextObj != null) {
            listP[listP.length - 1].setExternal(true);
        }

        for (PetriT transition : getListT()) { //Вихід з усіх переходів, що час виходу маркерів == поточний момент час.
            while (transition.getMinTime() == getTimeLocal() && transition.getBuffer() > 0) {
                transition.actOut(getListP()); // Вихід маркерів з переходу, що відповідає найближчому моменту часу
                // але тільки у позиції, які не є зовнішніми

                if (nextObj != null && getOutT().contains(transition)) {
                    nextObj.addTimeExternalInput((Double) getTimeLocal());

                    getLock().lock();
                    try {
                        while (nextObj.getTimeExternalInput().size() >= getLimitArrayExtInputs()) { //очікувати доки спрацюють інші об єкти
                            getCondLimitEvents().await();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PetriSim.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        getLock().unlock();
                    }
                }
            }
        }
    }
    
    public void moveTimeLocal(double t) {
        doStatistics((t - getTimeLocal()) / t);
        setTimeLocal(t);
    }
    
    
    /**
     * Finds the set of transitions for which the firing condition is true and
     * sorts it on priority value
     *
     * @return the sorted list of transitions with the true firing condition
     */
    public ArrayList<PetriT> findActiveT() {
        ArrayList<PetriT> aT = new ArrayList<>();   
        for (PetriT transition : getListT()) {
            if (transition.condition(getListP()) && (transition.getProbability() != 0)) {
                aT.add(transition);
            }
        }
        if (aT.size() > 1) {
            aT = sortT(aT); // сортування за пріоритетом переходів
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
        // rewrote by Inna 18.04.16
        TT.sort((o1, o2) -> {
            if (o1.getPriority() < o2.getPriority()) {
                return 1; //сортування у спадаючому порядку!!!
            } else if (o1.getPriority() > o2.getPriority()) {
                return -1;
            } else {
                return 0;
            }
        });
        return TT;
    }
    
    
    /**
     * This method solves conflict between transitions given in parametr TT
     *
     * @param TT the list of transitions
     * @return the transition - winner of conflict
     */
    public PetriT doConflict(ArrayList<PetriT> TT) {//
        PetriT aT = TT.get(0);
        if (TT.size() > 1) {
            aT = TT.get(0);
            int i = 0;
            while (i < TT.size() && TT.get(i).getPriority() == aT.getPriority()) {
                i++;
            }
            if (i > 1) {
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
    
    public void reinstateActOut(PetriT transition, PetriP position) {
        for (ArcOut arc : previousObj.getListOut()) {
            if (arc.getNumT() == transition.getNumber() && arc.getNumP() == position.getNumber()) {
                //  System.out.println("reinstate " + ", transition " + transition.getName() + ", position " + position.getName()+", "+this.getName());
                position.increaseMark(arc.getQuantity());
                //System.out.println("reinstate to position "+ position.getName()+", now mark = "+position.getMark()+", tLocal "+this.getTimeLocal()+", tExternal  "+timeExternalInput.get(0));
                increaseCounter();
                break;
            } else {
                // System.out.println(arc.getNumT()+" == "+ transition.getNumber() +" &&  "+ arc.getNumP() +" == "+ position.getNumber());
            }
        }
    }
    
    
    /**
     * Calculates mean value of quantity of markers in places and quantity of
     * active channels of transitions
     */
    public void doStatistica() {
        for (PetriP position : getListP()) {
            position.changeMean((getTimeMin() - getTimeCurr()) / getTimeMod());
        }
        for (PetriT transition : getListT()) {
            transition.changeMean((getTimeMin() - getTimeCurr()) / getTimeMod());
        }
    }

    /**
     * Calculates mean value of quantity of markers in places and quantity of
     * active channels of transitions
     *
     * @param dt - the time interval
     */
    public void doStatistics(double dt) {
        if (dt > 0) {
            for (PetriP position : getListPositionsForStatistica()) {
                // ListPositionsForStatistica.stream().forEach((PetriP position) -> {
                position.changeMean(dt);

                //  });
            }
            // writeStatistics();
            for (PetriT transition : getListT()) {
                transition.changeMean(dt);
            }
        }
    }


    public void step(JTextArea area) { //один крок ,використовується для одного об'єкту мережа Петрі
        area.append("\n Наступна подія, поточний час = " + getTimeCurr());

        this.printMark();//друкувати поточне маркування
        ArrayList<PetriT> activeT = this.findActiveT();     //формування списку активних переходів
        for (PetriT T : activeT) {
            area.append("\nСписок переходів з виконаною умовою запуску " + T.getName());
        }
        if ((activeT.isEmpty() && isBufferEmpty() == true) || getTimeCurr() >= getTimeMod()) { //зупинка імітації за умови, що
            //не має переходів, які запускаються,
            //  stop = true;                              // і не має фішок в переходах або вичерпаний час моделювання
            area.append("\n STOP, немає активних переходів і не виконана умова запуску для жодного з переходів " + this.getName());
            setTimeMin(getTimeMod());
            for (PetriP position : getListP()) {
                position.changeMean((getTimeMin() - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : getListT()) {
                transition.changeMean((getTimeMin() - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(getTimeMin());         //просування часу
        } else {

            while (activeT.size() > 0) {      //вхід маркерів в переходи доки можливо

                area.append("\n Вибір переходу для запуску " + this.doConflict(activeT).getName());
                this.doConflict(activeT).actIn(getListP(), getTimeCurr()); //розв'язання конфліктів
                activeT = this.findActiveT(); //оновлення списку активних переходів
            }
            area.append("\n Вхід маркерів в переходи:");
            this.printMark(area);//друкувати поточне маркування

            this.eventMin();//знайти найближчу подію та ії час
            for (PetriP position : getListP()) {
                position.changeMean((getTimeMin() - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : getListT()) {
                transition.changeMean((getTimeMin() - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(getTimeMin());         //просування часу

            if (getTimeCurr() <= getTimeMod()) {

                area.append("\n поточний час =" + getTimeCurr() + "   " + eventMin.getName());
                //Вихід маркерів
                eventMin.actOut(getListP());//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                area.append("\n Вихід маркерів з переходу " + eventMin.getName());
                this.printMark(area);//друкувати поточне маркування

                if (eventMin.getBuffer() > 0) {

                    boolean u = true;
                    while (u == true) {
                        eventMin.minEvent();
                        if (eventMin.getMinTime() == getTimeCurr()) {
                            // System.out.println("MinTime="+TEvent.getMinTime());
                            eventMin.actOut(getListP());
                            // this.printMark();//друкувати поточне маркування
                        } else {
                            u = false;
                        }
                    }
                    area.append("Вихід маркерів з буфера переходу " + eventMin.getName());
                    this.printMark(area);//друкувати поточне маркування
                }
                //Додано 6.08.2011!!!
                for (PetriT transition : getListT()) { //ВАЖЛИВО!!Вихід з усіх переходів, що час виходу маркерів == поточний момент час.
                    if (transition.getBuffer() > 0 && transition.getMinTime() == getTimeCurr()) {
                        transition.actOut(getListP());//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                        area.append("\n Вихід маркерів з переходу " + transition.getName());
                        this.printMark(area);//друкувати поточне маркування
                        if (transition.getBuffer() > 0) {
                            boolean u = true;
                            while (u == true) {
                                transition.minEvent();
                                if (transition.getMinTime() == getTimeCurr()) {
                                    // System.out.println("MinTime="+TEvent.getMinTime());
                                    transition.actOut(getListP());
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
     * It does the transitions input and output markers in the current moment
     */
    public void stepEvent() {  //один крок,вихід та вхід маркерів в переходи Петрі-об"єкта, використовується для множини Петрі-об'єктів
        if (isStop()) {
            setTimeMin(Double.MAX_VALUE);
        } else {
            output();
            input();
        }
    }
    

    public void printState() {
        /*  if(previousObj!=null){
            System.out.print(", size of buffer  "+timeExternalInput.size());
            if(!timeExternalInput.isEmpty())
                System.out.println(", first external event "+timeExternalInput.get(0));
        }*/
        this.printMark();
        this.printBuffer();
    }

    public void printStateExternalEvent() {
        if (previousObj != null) {
            System.out.print(this.getName() + " size " + timeExternalInput.size());
            if (!timeExternalInput.isEmpty()) {
                System.out.println(", first external event " + timeExternalInput.get(0) + ", tlocal " + timeLocal + ", tMin " + timeMin);
            }
        }
    }
    
    /**
     * Do printing the current marking of Petri net
     */
    public void printMark() {
        System.out.print("Mark in Net " + this.getName() + "   ");
        for (PetriP position : getListP()) {
            System.out.print(position.getMark() + "  ");
        }
        System.out.println();
    }

    public void printBuffer() {
        System.out.print("Buffer in Net  " + this.getName() + "   ");
        for (PetriT transition : getListT()) {
            System.out.print(transition.getBuffer() + "  ");
        }
        System.out.println();
    }

    public void printMark(JTextArea area) {
        area.append("\n Mark in Net " + this.getName() + "   \n");
        for (PetriP position : getListP()) {
            area.append(position.getMark() + "  ");
        }
        area.append("\n");
    }
    
    public synchronized void writeStatistics() {
        try {
            f.writeBytes(getListP()[0].getMark() + "\t"
                    + this.getTimeLocal() + "\t"
                    + getListP()[0].getMean() + "\n");
        } catch (IOException ex) {
            Logger.getLogger(PetriSim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * This method uses for describing other actions associated with transition
     * markers output.<br>
     * Such as the output markers into the other Petri-object.<br>
     * The method is overridden in subclass.
     */
    public void doT() {

    }
}
