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
     
    
    
        d_P.add(new PetriP("кількість вимог в очікуванні",0));
        d_P.add(new PetriP("кількість вільних пристроїв",numDevices));
        d_P.add(new PetriP("кількість обслугованих",0));
  
     
        d_T.add(new PetriT("обслуговування",timeServ));
        d_T.get(0).setDistribution(destribution, d_T.get(0).getTimeServ());
       
        d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
        d_In.add(new ArcIn(d_P.get(1),d_T.get(0),1));
        
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
   
        PetriNet d_Net = new PetriNet("СМО з необмеженою чергою ",d_P,d_T,d_In,d_Out);
       
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
      
         d_T.add(new PetriT("надходження",timeGen));
         d_T.get(0).setDistribution(destribution, d_T.get(0).getTimeServ());
           
        d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
        
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(0),1));
        d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
    
        PetriNet d_Net = new PetriNet("Генератор надходження вимог на обслуговування ",d_P,d_T,d_In,d_Out);
        
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
	d_P.add(new PetriP("Є зелене світло в 3 та 4",1));
	d_P.add(new PetriP("Є зелене світло в 1 та 2",0));
	d_T.add(new PetriT("Жовте світло",timeYellowR));
	d_T.add(new PetriT("Зелене світло в 3 та 4",timeGreen));
	d_T.add(new PetriT("Жовте світло",timeYellowG));
	d_T.add(new PetriT("Зелене світло в 1 та 2",timeRed));
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

public static PetriNet CreateNetAvto(double timeParam,int numS) throws ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("",1));
	d_P.add(new PetriP("Черга авто",0));
	d_P.add(new PetriP("Кількість авто, що переїхали",0));
	d_P.add(new PetriP("Є дозвіл",0));
	d_P.add(new PetriP("Кількість смуг руху",numS));
	d_T.add(new PetriT("Надходження",timeParam));
	d_T.get(0).setDistribution("unif", d_T.get(0).getTimeServ());
	d_T.get(0).setParamDeviation(2.0);
	d_T.add(new PetriT("Переїзд перехрестя",2.0));
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
public static PetriNet CreateNetSMOwithoutQueue(int numChannel, double timeMean, String name) throws ExceptionInvalidNetStructure{
	
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
	PetriNet d_Net = new PetriNet("SMOwithoutQueue"+name,d_P,d_T,d_In,d_Out);
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
	
        ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
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

 public static PetriNet CreateNetSMOgroup(int numInGroup,int numChannel, double timeMean) throws ExceptionInvalidNetStructure{
        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
        d_P.add(new PetriP("P0", 0));
        for (int j = 0; j < numInGroup; j++) {
            d_P.add(new PetriP("P" + (2 * j + 1), numChannel));
            d_P.add(new PetriP("P" + (2 * j + 2), 0));
            d_T.add(new PetriT("T"+(j), timeMean));
            d_T.get(j).setDistribution("exp", d_T.get(j).getTimeServ());
            d_T.get(j).setParamDeviation(0.0);
            d_In.add(new ArcIn(d_P.get(2 * j), d_T.get(j), 1));
            d_In.add(new ArcIn(d_P.get(2 * j + 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(2 * j + 1), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(2 * j + 2), 1));
        }
        PetriNet d_Net = new PetriNet("SMOwithoutQueue", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
public static PetriNet CreateNetSMOgroup(int numInGroup,int numChannel, double timeMean, String name) throws ExceptionInvalidNetStructure{
        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
        d_P.add(new PetriP("P0", 0));
        for (int j = 0; j < numInGroup; j++) {
            d_P.add(new PetriP("P" + (2 * j + 1), numChannel));
            d_P.add(new PetriP("P" + (2 * j + 2), 0));
            d_T.add(new PetriT("T"+(j), timeMean));
            d_T.get(j).setDistribution("exp", d_T.get(j).getTimeServ());
            d_T.get(j).setParamDeviation(0.0);
            d_In.add(new ArcIn(d_P.get(2 * j), d_T.get(j), 1));
            d_In.add(new ArcIn(d_P.get(2 * j + 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(2 * j + 1), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(2 * j + 2), 1));
        }
        PetriNet d_Net = new PetriNet(name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

}
