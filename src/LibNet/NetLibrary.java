package LibNet;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import PetriObj.ArcIn;
import PetriObj.ArcOut;
import PetriObj.ExceptionInvalidTimeDelay;
import java.util.ArrayList;
public class NetLibrary {
    
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
    
/**
     * Creates Petri net that describes the dynamics of system of the mass
     * service (with unlimited queue)
     *
     * @param numChannel the quantity of devices
     * @param timeMean the mean value of service time of unit
     * @param name the individual name of SMO
     * @throws ExceptionInvalidTimeDelay if one of net's transitions has no input position.
     * @return Petri net dynamics of which corresponds to system of mass service with given parameters
     */
public static PetriNet CreateNetSMOwithoutQueue(int numChannel, double timeMean, String name) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",numChannel));
	d_P.add(new PetriP("P3",0));
	d_T.add(new PetriT("T1",timeMean,Double.MAX_VALUE));
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

 /**
     * Creates Petri net that describes the dynamics of arrivals of demands for
     * service
     *
     * @param timeMean mean value of interval between arrivals
     * @return Petri net dynamics of which corresponds to generator
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid structure
     */
public static PetriNet CreateNetGenerator(double timeMean) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",1));
	d_P.add(new PetriP("P2",0));
	d_T.add(new PetriT("T1", timeMean,Double.MAX_VALUE));
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
 /**
     * Creates Petri net that describes the route choice with given
     * probabilities
     *
     * @param p1 the probability of choosing the first route
     * @param p2 the probability of choosing the second route
     * @param p3 the probability of choosing the third route
     * @return Petri net dynamics of which corresponds to fork of routs
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid structure
     */
public static PetriNet CreateNetFork(double p1, double p2, double p3) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure{
	ArrayList<PetriP> d_P = new ArrayList<PetriP>();
	ArrayList<PetriT> d_T = new ArrayList<PetriT>();
	ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
	ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("P3",0));
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_T.add(new PetriT("T1",0.0,Double.MAX_VALUE));
	d_T.get(0).setProbability(p1);
	d_T.add(new PetriT("T2",0.0,Double.MAX_VALUE));
	d_T.get(1).setProbability(p2);
	d_T.add(new PetriT("T3",0.0,Double.MAX_VALUE));
	d_T.get(2).setProbability(p3);
	d_T.add(new PetriT("T4",0.0,Double.MAX_VALUE));
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
   /**
     * Creates Petri net that describes the route choice with given
     * probabilities
     *
     * @param numOfWay quantity of possibilities in choice ("ways")
     * @param probabilities set of values probabilities for each "way"
     * @return Petri net dynamics of which corresponds to fork of routs
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid structure
     */
    public static PetriNet CreateNetFork(int numOfWay, double[] probabilities) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {

        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();

        d_P.add(new PetriP("P0", 0));
        for (int j = 0; j < numOfWay; j++) {
            d_P.add(new PetriP("P" + (j + 1), 0));
        }

        for (int j = 0; j < numOfWay; j++) {
            d_T.add(new PetriT("вибір маршруту " + (j + 1), 0));
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

        PetriNet d_Net = new PetriNet("Fork ", d_P, d_T, d_In, d_Out);

        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }    

public static PetriNet CreateNetMalware() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("NewPacket",0));
	d_P.add(new PetriP("Firewall",0));//p1
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("WebServer",0));//p3
	d_P.add(new PetriP("P4",0));
	d_P.add(new PetriP("P5",0));
	d_P.add(new PetriP("FileServer",0));//p6
	d_P.add(new PetriP("P7",0));
	d_P.add(new PetriP("OS",0));//p8
	d_P.add(new PetriP("P9",0));
	d_P.add(new PetriP("VulnOfWall",1));
	d_P.add(new PetriP("VulnOfWebServ",1));
	d_P.add(new PetriP("VulnOfFileServ",1));
	d_P.add(new PetriP("VulnOfOS",1));
	d_P.add(new PetriP("VulnOfAuthorization",1));
	d_P.add(new PetriP("P15",0));
	d_P.add(new PetriP("P16",0));
	d_P.add(new PetriP("Success",0));  //p17
	d_P.add(new PetriP("Alarm",0));
	d_T.add(new PetriT("NetworkAccessControl",1.0));
	d_T.add(new PetriT("Authorization",0.0));
	d_T.get(1).setPriority(1);
	d_T.get(1).setProbability(0.5);
	d_T.add(new PetriT("RunWS",1.0));
	d_T.get(2).setPriority(1);
	d_T.get(2).setProbability(0.5);
	d_T.add(new PetriT("RunFS",1.0));
	d_T.get(3).setPriority(1);
	d_T.get(3).setProbability(0.5);
	d_T.add(new PetriT("RunOS",1.0));
	d_T.get(4).setPriority(1);
	d_T.get(4).setProbability(0.5);
	d_T.add(new PetriT("T1",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_T.add(new PetriT("T3",0.0));
	d_T.add(new PetriT("T4",0.0));
	d_T.get(8).setPriority(1);
	d_T.get(8).setProbability(0.5);
	d_T.add(new PetriT("T5",0.0));
	d_T.get(9).setPriority(1);
	d_T.get(9).setProbability(0.5);
	d_T.add(new PetriT("T6",0.0));
	d_T.get(10).setPriority(1);
	d_T.get(10).setProbability(0.5);
	d_T.add(new PetriT("T7",0.0));
	d_T.get(11).setPriority(1);
	d_T.get(11).setProbability(0.5);
	d_In.add(new ArcIn(d_P.get(0),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(6),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(8),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(10),d_T.get(0),1));
	d_In.get(10).setInf(true);
	d_In.add(new ArcIn(d_P.get(12),d_T.get(3),1));
	d_In.get(11).setInf(true);
	d_In.add(new ArcIn(d_P.get(14),d_T.get(1),1));
	d_In.get(12).setInf(true);
	d_In.add(new ArcIn(d_P.get(11),d_T.get(2),1));
	d_In.get(13).setInf(true);
	d_In.add(new ArcIn(d_P.get(13),d_T.get(4),1));
	d_In.get(14).setInf(true);
	d_In.add(new ArcIn(d_P.get(8),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(9),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(15),d_T.get(6),1));
	d_In.add(new ArcIn(d_P.get(16),d_T.get(7),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(8),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(11),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(10),1));
	d_In.add(new ArcIn(d_P.get(4),d_T.get(9),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(5),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(6),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(8),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(9),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(15),1));
	d_Out.add(new ArcOut(d_T.get(6),d_P.get(16),1));
	d_Out.add(new ArcOut(d_T.get(7),d_P.get(17),1));
	d_Out.add(new ArcOut(d_T.get(8),d_P.get(18),1));
	d_Out.add(new ArcOut(d_T.get(11),d_P.get(18),1));
	d_Out.add(new ArcOut(d_T.get(10),d_P.get(18),1));
	d_Out.add(new ArcOut(d_T.get(9),d_P.get(18),1));
	PetriNet d_Net = new PetriNet("malware",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}
public static PetriNet CreateNetAdmin() throws ExceptionInvalidNetStructure {
	ArrayList<PetriP> d_P = new ArrayList<>();
	ArrayList<PetriT> d_T = new ArrayList<>();
	ArrayList<ArcIn> d_In = new ArrayList<>();
	ArrayList<ArcOut> d_Out = new ArrayList<>();
	d_P.add(new PetriP("User",1));
	d_P.add(new PetriP("P1",0));
	d_P.add(new PetriP("P2",0));
	d_P.add(new PetriP("Damaged",0));
	d_P.add(new PetriP("Harmless",0));
	d_P.add(new PetriP("Executed",0));
	d_P.add(new PetriP("NewPacket",0));
	d_P.add(new PetriP("Admin",1));
	d_P.add(new PetriP("FileServer",0));
	d_P.add(new PetriP("Alarm",0));
	d_P.add(new PetriP("P10",0));
	d_T.add(new PetriT("DoTest",1.0));
	d_T.add(new PetriT("ControlTime",10.0));
	d_T.add(new PetriT("T3",0.0));
	d_T.add(new PetriT("T4",0.0));
	d_T.add(new PetriT("RepairResources",0.0));
	d_T.add(new PetriT("T2",0.0));
	d_T.add(new PetriT("T3",0.0));
	d_In.add(new ArcIn(d_P.get(1),d_T.get(1),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(2),1));
	d_In.add(new ArcIn(d_P.get(2),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(5),d_T.get(3),1));
	d_In.add(new ArcIn(d_P.get(7),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(3),d_T.get(4),1));
	d_In.add(new ArcIn(d_P.get(9),d_T.get(5),1));
	d_In.add(new ArcIn(d_P.get(10),d_T.get(0),1));
	d_In.add(new ArcIn(d_P.get(0),d_T.get(6),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(1),1));
	d_Out.add(new ArcOut(d_T.get(1),d_P.get(2),1));
	d_Out.add(new ArcOut(d_T.get(2),d_P.get(3),1));
	d_Out.add(new ArcOut(d_T.get(3),d_P.get(4),1));
	d_Out.add(new ArcOut(d_T.get(0),d_P.get(6),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(7),1));
	d_Out.add(new ArcOut(d_T.get(4),d_P.get(8),1));
	d_Out.add(new ArcOut(d_T.get(6),d_P.get(10),1));
	d_Out.add(new ArcOut(d_T.get(6),d_P.get(0),1));
	d_Out.add(new ArcOut(d_T.get(5),d_P.get(10),1));
	PetriNet d_Net = new PetriNet("admin",d_P,d_T,d_In,d_Out);
	PetriP.initNext();
	PetriT.initNext();
	ArcIn.initNext();
	ArcOut.initNext();

	return d_Net;
}

}