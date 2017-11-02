package PetriObj;

import java.util.ArrayList;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This class contains methods for creating Petri net in accordance with choosen
 * object and with given parameters.
 *
 * @author Inna V. Stetsenko
 */
public class NetLibrary {

    /**
     * Creates Petri net that describes the dynamics of system of the mass
     * service (with unlimited queue)
     *
     * @param timeModeling time modeling
     * @param numDevices quantity of devices
     * @param timeServ mean value of service time of unit
     * @param destribution service time destribution
     * @throws ExceptionInvalidNetStructure if one of net's transitions has no input position.
     * @return Petri net dynamics of which corresponds to system of mass service with given parameters
     */
    public static PetriNet CreateNetSMO(double timeModeling, int numDevices, double timeServ, String destribution) throws ExceptionInvalidNetStructure {

        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();

        d_P.add(new PetriP("кількість вимог в очікуванні", 0));
        d_P.add(new PetriP("кількість вільних пристроїв", numDevices));
        d_P.add(new PetriP("кількість обслугованих", 0));

        d_T.add(new PetriT("обслуговування", timeServ, timeModeling));
        d_T.get(0).setDistribution(destribution, d_T.get(0).getTimeServ());

        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));

        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));

        PetriNet d_Net = new PetriNet("СМО з необмеженою чергою ", d_P, d_T, d_In, d_Out);

        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
   

    /**
     * Creates Petri net that describes the dynamics of arrivals of demands for
     * service
     *
     * @param timeModeling time modeling
     * @param timeGen mean value of interval between arrivals
     * @param destribution generating time destribution
     * @return Petri net dynamics of which corresponds to generator
     * @throws PetriObj.ExceptionInvalidNetStructure if Petri net has invalid structure
     */
    public static PetriNet CreateNetGenerator(double timeModeling, double timeGen, String destribution) throws ExceptionInvalidNetStructure {

        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();

        d_P.add(new PetriP("P0", 1));
        d_P.add(new PetriP("P1", 0));

        d_T.add(new PetriT("надходження", timeGen, timeModeling));
        d_T.get(0).setDistribution(destribution, d_T.get(0).getTimeServ());

        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));

        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));

        PetriNet d_Net = new PetriNet("Генератор надходження вимог на обслуговування ", d_P, d_T, d_In, d_Out);

        PetriP.initNext();   //ВАЖЛИВО!!! занулення лічильників об"єктів
        PetriT.initNext();   // нумерація переходів та позиція ВІДНОСНА в межах об"єкта
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    /**
     * Creates Petri net that describes the route choice with given
     * probabilities
     *
     * @param timeModeling time modeling
     * @param numOfWay quantity of possibilities in choice ("ways")
     * @param probabilities set of values probabilities for each "way"
     * @return Petri net dynamics of which corresponds to fork of routs
     * @throws PetriObj.ExceptionInvalidNetStructure if Petri net has invalid structure
     */
    public static PetriNet CreateNetFork(double timeModeling, int numOfWay, double[] probabilities) throws ExceptionInvalidNetStructure {

        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();

        d_P.add(new PetriP("P0", 0));
        for (int j = 0; j < numOfWay; j++) {
            d_P.add(new PetriP("P" + (j + 1), 0));
        }

        for (int j = 0; j < numOfWay; j++) {
            d_T.add(new PetriT("вибір маршруту " + (j + 1), 0, timeModeling));
        }
        for (int j = 0; j < numOfWay; j++) {
            d_T.get(j).setProbability(probabilities[j]);
        }

        for (int j = 0; j < numOfWay; j++) {
            d_In.add(new ArcIn(d_P.get(0), d_T.get(j), 1));
        }

        for (int j = 0; j < numOfWay; j++) {
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(j + 1), 1));
        }

        PetriNet d_Net = new PetriNet("Розгалуження маршруту ", d_P, d_T, d_In, d_Out);

        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

}
