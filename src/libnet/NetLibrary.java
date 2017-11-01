package libnet;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import PetriObj.ArcIn;
import PetriObj.ArcOut;
import java.util.ArrayList;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 * This class contains methods for creating Petri net in accordance with choosen object and with given parameters.
 * @author Стеценко Інна
 */
public class NetLibrary {


    /**
     * Creates Petri net that describes the dynamics of system of the mass service (with unlimited queue)
     * @param timeModeling time modeling 
     * @param numDevices quantity of devices
     * @param timeServ mean value of service time of unit
     * @param destribution service time destribution 
     * @return
     */
    public static PetriNet CreateNetSMO(double timeModeling, int numDevices, double timeServ, String destribution) throws ExceptionInvalidNetStructure
{

      ArrayList<PetriP> d_P = new ArrayList<PetriP>();
      ArrayList<PetriT> d_T = new ArrayList<PetriT>();
      ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
      ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
     
    
    
        d_P.add(new PetriP("Number of requests pending",0));
        d_P.add(new PetriP("Number of free engines",numDevices));
        d_P.add(new PetriP("Number of processed",0));
  
     
        d_T.add(new PetriT("Processing",timeServ));
        d_T.get(0).setDistribution(destribution, d_T.get(0).getTimeServ());
       
        d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
        d_In.add(new ArcIn(d_P.get(1),d_T.get(0),1));
        
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
   
        PetriNet d_Net = new PetriNet("SMO with an unlimited queue",d_P,d_T,d_In,d_Out);
       
       PetriP.initNext();
       PetriT.initNext();
       ArcIn.initNext();
       ArcOut.initNext();
       
      return d_Net;
}

    /**
     * Creates Petri net that describes the dynamics of arrivals of demands for service
     * @param timeGen mean value of interval between arrivals
     * @param destribution generating time destribution 
     * @return
     */
    public static PetriNet CreateNetGenerator(double timeGen, String destribution) throws ExceptionInvalidNetStructure
{

      ArrayList<PetriP> d_P = new ArrayList<PetriP>();
      ArrayList<PetriT> d_T = new ArrayList<PetriT>();
      ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
      ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
     
        d_P.add(new PetriP("P0",1));
        d_P.add(new PetriP("P1",0));
      
         d_T.add(new PetriT("Income",timeGen));
         d_T.get(0).setDistribution(destribution, d_T.get(0).getTimeServ());
           
        d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
        
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(0),1));
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
    
        PetriNet d_Net = new PetriNet("Request generator",d_P,d_T,d_In,d_Out);
        
       PetriP.initNext();   //ВАЖЛИВО!!! занулення лічильників об"єктів
       PetriT.initNext();   // нумерація переходів та позиція ВІДНОСНА в межах об"єкта
       ArcIn.initNext();
       ArcOut.initNext();
  
	return d_Net;



}
public static PetriNet CreateNetas() throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1",2.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	PetriNet d_Net = new PetriNet("as",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}

public static PetriNet CreateNetgenerator() throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",1));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1",10.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	PetriNet d_Net = new PetriNet("generator",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}


public static PetriNet CreateNetchoice() throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",1000));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	PetriNet d_Net = new PetriNet("choice",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}

public static PetriNet CreateNetchoices() throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",1000));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P1",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_T.add(new PetriT("T1",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	PetriNet d_Net = new PetriNet("choices",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetChoices2() throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",5000));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P1",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_T.add(new PetriT("T1",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	PetriNet d_Net = new PetriNet("Choices2",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}

public static PetriNet CreateNetLights(double timeGreen, double timeYellowG, double timeRed, double timeYellowR) throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",1));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("There are green lights in 3 and 4",1));
	d_P.add(new PetriP("There are green lights in 1 and 2",0));
	d_T.add(new PetriT("Yellow lights",timeYellowR));
	d_T.add(new PetriT("Green lights in 3 and 4",timeGreen));
	d_T.add(new PetriT("Yellow lights",timeYellowG));
	d_T.add(new PetriT("Green lights in 1 and 2",timeRed));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("Lights",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();
	
        return d_Net;
}

public static PetriNet CreateNetAvto(double timeParam, int numS) throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("",1));
	d_P.add(new PetriP("Car queue",0));
	d_P.add(new PetriP("Cars which have passed",0));
	d_P.add(new PetriP("Can move",0));
	d_P.add(new PetriP("Number of road lines",numS));
	d_T.add(new PetriT("Income",timeParam));
	d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
	d_T.get(0).setParamDeviation(2.0);
	d_T.add(new PetriT("Moving over the crossroads",2.0));
	d_T.get(1).setDistribution("unif", d_T.get(1).getTimeServ());
	d_T.get(1).setParamDeviation(0.2);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(1),1));
	d_In.get(2).setInf(true);
	d_In.add(new ArcIn(d_P.get(4),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("Avto",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetSMOwithoutQueue(int numChannel, double timeMean) throws ExceptionInvalidNetStructure{
	
        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",numChannel));
	d_P.add(new PetriP("P3",0));
	d_T.add(new PetriT("T1",timeMean));
	d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
	d_T.get(0).setParamDeviation(0.0);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	PetriNet d_Net = new PetriNet("SMOwithoutQueue",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetGenerator(double timeMean) throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",1));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1", timeMean));
	d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
	d_T.get(0).setParamDeviation(0.0);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(0),1));
	PetriNet d_Net = new PetriNet("Generator",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetFork(double p1, double p2, double p3) throws ExceptionInvalidNetStructure{
	
        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.get(0).setProbability(p1);
	d_T.add(new PetriT("T2",0.0));
	d_T.get(1).setProbability(p2);
	d_T.add(new PetriT("T3",0.0));
	d_T.get(2).setProbability(p3);
	d_T.add(new PetriT("T4",0.0));
	d_T.get(3).setProbability(1-p1-p2-p3);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(3),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(4),1));
	PetriNet d_Net = new PetriNet("Fork",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetSample() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	PetriNet d_Net = new PetriNet("Sample",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetThreePlaces() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_T.add(new PetriT("T1",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	PetriNet d_Net = new PetriNet("ThreePlaces",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetLong() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",100));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P4",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_T.add(new PetriT("T3",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	PetriNet d_Net = new PetriNet("Long",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetTreeStruct() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("P6",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_T.add(new PetriT("T3",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(5),1));
	PetriNet d_Net = new PetriNet("TreeStruct",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetArrow() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	PetriNet d_Net = new PetriNet("Arrow",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}

public static PetriNet CreateNetRound() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(0),1));
	PetriNet d_Net = new PetriNet("Untitled",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetUntitled() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",3));
	d_T.add(new PetriT("T1",0.0));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(0),1));
	PetriNet d_Net = new PetriNet("Untitled",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetUntitled(double time) throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1",time));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	PetriNet d_Net = new PetriNet("Untitled",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetUntitled() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	PetriNet d_Net = new PetriNet("Untitled",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetRRRRR() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1",0.0));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	PetriNet d_Net = new PetriNet("RRRRR",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
}