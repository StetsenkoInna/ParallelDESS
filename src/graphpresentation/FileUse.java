/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphpresentation;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import PetriObj.ArcIn;
import PetriObj.ArcOut;
import java.awt.FileDialog;
import PetriObj.PetriMainElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import graphnet.GraphArcIn;
import graphnet.GraphArcOut;
import graphnet.GraphPetriPlace;
import graphnet.GraphPetriTransition;
import graphnet.GraphPetriNet;
import java.awt.geom.Point2D;

/**
 *
 * @author Olya &  Inna
 */
public class FileUse {

    // private final String COPY_NAME = "_copy"; // 15.01.13 
    private final String PATTERN = ".pns";

    public String openFile(PetriNetsPanel panel, JFrame frame) throws ExceptionInvalidNetStructure {
        String pnetName = "";
        FileDialog fdlg;
        fdlg = new FileDialog(frame, "Відкрити файл ",
                FileDialog.LOAD);
        fdlg.setVisible(true);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            // System.out.println("Opening file '" + fdlg.getDirectory() + fdlg.getFile() + "'");
            fis = new FileInputStream(fdlg.getDirectory() + fdlg.getFile());
            ois = new ObjectInputStream(fis);
            GraphPetriNet net = ((GraphPetriNet) ois.readObject()).clone();  //створюємо копію, щоб відновити стан усіх класів з усіма номерами...
            panel.addGraphNet(net); //можливо переназвати його setGraphNet(net)???
            pnetName = net.getPetriNet().getName();
            ois.close();
            // 11.01.13 якщо задана поточна точка на панелі, то центр мережі буде в цій точці
          /*  if (panel.getCurrentPlacementPoint() != null) {
                panel.getLastGraphNetList().changeLocation(panel.getCurrentPlacementPoint());
            }*/
            panel.repaint();

        } catch (FileNotFoundException e) {
            System.out.println("Such file was not found");
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FileUse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException e) {
                return null;
            }
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException e) {
                return null;
            }

        }
        //  return pnetName.substring(0, pnetName.length() - COPY_NAME.length());
        return pnetName.substring(0, pnetName.length());
    }

    public GraphPetriNet openFile(JFrame frame) throws ExceptionInvalidNetStructure {
        GraphPetriNet net = null;
        FileDialog fdlg;
        fdlg = new FileDialog(frame, "Відкрити файл ",
                FileDialog.LOAD);
        fdlg.setVisible(true);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            //  System.out.println("Opening file '" + fdlg.getDirectory() + fdlg.getFile() + "'");
            fis = new FileInputStream(fdlg.getDirectory() + fdlg.getFile());
            ois = new ObjectInputStream(fis);
            net = ((GraphPetriNet) ois.readObject()).clone();  //створюємо копію, щоб відновити стан усіх класів з усіма номерами...             
            ois.close();
        } catch (FileNotFoundException e) {
            // System.out.println("Such file was not found");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FileUse.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return net;
    }

    public void newWorksheet(PetriNetsPanel panel) {
        panel.setNullPanel();
    }

    public void saveGraphNetAs(PetriNetsPanel panel, JFrame frame) throws ExceptionInvalidNetStructure {
        FileDialog fdlg;
        fdlg = new FileDialog(frame,
                "Зберегти графічну мережу Петрі як ...",
                FileDialog.SAVE);
        fdlg.setVisible(true);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fdlg.setFilenameFilter(null);
            //   System.out.println("Saving GraphNet as '" + fdlg.getDirectory() + fdlg.getFile() + "'");
            fos = new FileOutputStream(fdlg.getDirectory() + fdlg.getFile() + PATTERN);
            oos = new ObjectOutputStream(fos);
            panel.getGraphNet().createPetriNet(fdlg.getFile());
            oos.writeObject(panel.getGraphNet());
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void saveGraphNetAs(GraphPetriNet net, JFrame frame) throws ExceptionInvalidNetStructure {
        FileDialog fdlg;
        fdlg = new FileDialog(frame,
                "Зберегти Графічну мережу Петрі як ...",
                FileDialog.SAVE);
        fdlg.setVisible(true);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fdlg.setFilenameFilter(null);
            //System.out.println("Saving GraphNet as '" + fdlg.getDirectory() + fdlg.getFile() + "'");
            net.createPetriNet(fdlg.getFile()); // мережа має ім"я як ім"я файлу   
            fos = new FileOutputStream(fdlg.getDirectory() + fdlg.getFile() + PATTERN);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(net);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void savePetriNetAs(PetriNetsPanel panel, JFrame frame) throws ExceptionInvalidNetStructure {
        FileDialog fdlg;
        fdlg = new FileDialog(frame,
                "Зберегти мережу Петрі як ...",
                FileDialog.SAVE);
        fdlg.setVisible(true);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fdlg.setFilenameFilter(null);
            // System.out.println("Saving PetriNet as '" + fdlg.getDirectory() + fdlg.getFile() + "'");
            // перед зберіганням формується мережа Петрі об"єкту graphNet
            panel.getGraphNet().createPetriNet(fdlg.getFile()); // мережа має ім"я як ім"я файлу
            fos = new FileOutputStream(fdlg.getDirectory() + fdlg.getFile() + PATTERN);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(panel.getGraphNet().getPetriNet());
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public boolean saveGraphNet(GraphPetriNet pnet, String name) throws ExceptionInvalidNetStructure {  // saving graph in the same folder as project
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        if (name.equalsIgnoreCase("")) {
            name = "Untitled";
        }

        try {
            String pnetName = name;
            //System.out.println("Saving GraphPetriNet with name '" + pnetName + "'");
          /*  if (pnetName.contains(COPY_NAME)) {
             pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
             //System.out.println("Changing name to '" + pnetName + "'");
             }*/
            pnet.createPetriNet(pnetName); // мережа має ім"я як ім"я файлу      
            File file = new File(pnetName + ".pns");
            // System.out.println("Saving path = " + file.getAbsolutePath());
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(pnet);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }
    
    public GraphPetriNet generateGraphNetBySimpleNet(PetriNet net, Point paneCenter) { // added by Katya 16.10.2016
        ArrayList<GraphPetriPlace> grPlaces = new ArrayList<>();
        ArrayList<GraphPetriTransition> grTransitions = new ArrayList<>();
        ArrayList<GraphArcIn> grTieIns = new ArrayList<>();
        ArrayList<GraphArcOut> grTieOuts = new ArrayList<>();
        
        ArrayList<PetriP> availPetriPlaces = new ArrayList<>(Arrays.asList(net.getListP())); // modified by Katya 20.11.2016 (including the "while" and 1st "for" loop)
        ArrayList<PetriT> availPetriTrans = new ArrayList<>(Arrays.asList(net.getListT()));
        
        ArrayList<VerticalSet> sets = new ArrayList<>();
        
        // first transition
        PetriT firstTran = availPetriTrans.remove(0);
        VerticalSet firstSet = new VerticalSet(false);
        firstSet.AddElement(firstTran);
        sets.add(firstSet);
        
        while (!availPetriPlaces.isEmpty() || !availPetriTrans.isEmpty()) {
            // step
            VerticalSet lastSet = null;
            int lastSetIndex = 0;
            for (VerticalSet set : sets) {
                if (set.GetReadyStatus() == false) {
                    lastSet = set;
                    lastSetIndex = sets.indexOf(lastSet);
                    break;
                }
            }
            if (lastSet == null) {
                break;
            }
            if (lastSet.IsForPlaces()) {
                // new transitions
                ArrayList<PetriT> inTrans = new ArrayList<>();
                for (ArcOut outArc : net.getTieOut()) {
                    for (PetriMainElement placeElem : lastSet.GetElements()) {
                        PetriP place = (PetriP) placeElem;
                        if (place.getNumber() == outArc.getNumP()) {
                            for (PetriT tran : availPetriTrans) {
                                if (tran.getNumber() == outArc.getNumT()) {
                                    inTrans.add(tran);
                                }
                            }
                        }
                    }
                }
                ArrayList<PetriT> outTrans = new ArrayList<>();
                for (ArcIn inArc : net.getTieIn()) {
                    for (PetriMainElement placeElem : lastSet.GetElements()) {
                        PetriP place = (PetriP) placeElem;
                        if (place.getNumber() == inArc.getNumP()) {
                            for (PetriT tran : availPetriTrans) {
                                if (!inTrans.contains(tran) && tran.getNumber() == inArc.getNumT()) {
                                    outTrans.add(tran);
                                }
                            }
                        }
                    }
                }
                
                if (!inTrans.isEmpty()) {
                    if (lastSetIndex == 0) {
                        sets.add(0, new VerticalSet(!lastSet.IsForPlaces()));
                        lastSetIndex = 1;
                    }
                }
                if (!outTrans.isEmpty()) {
                    if (sets.size() == (lastSetIndex + 1)) {
                        sets.add(new VerticalSet(!lastSet.IsForPlaces()));
                    }
                }
                
                for (PetriT tran : inTrans) {
                    sets.get(lastSetIndex - 1).AddElement(tran);
                    sets.get(lastSetIndex - 1).SetAsNotReady();
                    availPetriTrans.remove(tran);
                }
                for (PetriT tran : outTrans) {
                    sets.get(lastSetIndex + 1).AddElement(tran);
                    sets.get(lastSetIndex + 1).SetAsNotReady();
                    availPetriTrans.remove(tran);
                }
            } else {
                // new places
                ArrayList<PetriP> inPlaces = new ArrayList<>();
                for (ArcIn inArc : net.getTieIn()) {
                    for (PetriMainElement tranElem : lastSet.GetElements()) {
                        PetriT tran = (PetriT) tranElem;
                        if (tran.getNumber() == inArc.getNumT()) {
                            for (PetriP place : availPetriPlaces) {
                                if (place.getNumber() == inArc.getNumP()) {
                                    inPlaces.add(place);
                                }
                            }
                        }
                    }
                }
                ArrayList<PetriP> outPlaces = new ArrayList<>();
                for (ArcOut outArc : net.getTieOut()) {
                    for (PetriMainElement tranElem : lastSet.GetElements()) {
                        PetriT tran = (PetriT) tranElem;
                        if (tran.getNumber() == outArc.getNumT()) {
                            for (PetriP place : availPetriPlaces) {
                                if (!inPlaces.contains(place) && place.getNumber() == outArc.getNumP()) {
                                    outPlaces.add(place);
                                }
                            }
                        }
                    }
                }
                
                if (!inPlaces.isEmpty()) {
                    if (lastSetIndex == 0) {
                        sets.add(0, new VerticalSet(!lastSet.IsForPlaces()));
                        lastSetIndex = 1;
                    }
                }
                if (!outPlaces.isEmpty()) {
                    if (sets.size() == (lastSetIndex + 1)) {
                        sets.add(new VerticalSet(!lastSet.IsForPlaces()));
                    }
                }
                
                for (PetriP place : inPlaces) {
                    sets.get(lastSetIndex - 1).AddElement(place);
                    sets.get(lastSetIndex - 1).SetAsNotReady();
                    availPetriPlaces.remove(place);
                }
                for (PetriP place : outPlaces) {
                    sets.get(lastSetIndex + 1).AddElement(place);
                    sets.get(lastSetIndex + 1).SetAsNotReady();
                    availPetriPlaces.remove(place);
                }
            }
            
            lastSet.SetAsReady();
        }
        
        double x = 0, y = 0;
        
        for (VerticalSet set : sets) {
            ArrayList<PetriMainElement> elements = set.GetElements();
            int size = elements.size();
            x += 80;
            y = ((size % 2) == 0) ? (- (size / 2 * 80) - 40) : (- (size / 2 * 80) - 80);
            for (PetriMainElement elem : elements) {
                y += 80;
                if (set.IsForPlaces()) {
                    PetriP place = (PetriP)elem;
                    GraphPetriPlace grPlace = new GraphPetriPlace(place);
                    grPlace.setNewCoordinates(new Point2D.Double(x, y));
                    grPlaces.add(grPlace);
                } else {
                    PetriT tran = (PetriT)elem;
                    GraphPetriTransition grTran = new GraphPetriTransition(tran);
                    grTran.setNewCoordinates(new Point2D.Double(x, y));
                    grTransitions.add(grTran);
                }
            }
        }
        
        for (ArcIn inArc : net.getTieIn()) {
            GraphArcIn grInArc = new GraphArcIn(inArc);
            GraphPetriTransition endTransition = null;
            for (GraphPetriTransition grTran : grTransitions) {
                if (grTran.getNumber() == inArc.getNumT()) {
                    endTransition = grTran;
                }
            }
            GraphPetriPlace beginPlace = null;
            for (GraphPetriPlace grPlace : grPlaces) {
                if (grPlace.getNumber() == inArc.getNumP()) {
                    beginPlace = grPlace;
                }
            }
            grInArc.settingNewTie(beginPlace);
            grInArc.finishSettingNewTie(endTransition);
            grTieIns.add(grInArc);
        }
        
        for (ArcOut outArc : net.getTieOut()) {
            GraphArcOut grOutArc = new GraphArcOut(outArc);
            GraphPetriTransition beginTransition = null;
            for (GraphPetriTransition grTran : grTransitions) {
                if (grTran.getNumber() == outArc.getNumT()) {
                    beginTransition = grTran;
                }
            }
            GraphPetriPlace endPlace = null;
            for (GraphPetriPlace grPlace : grPlaces) {
                if (grPlace.getNumber() == outArc.getNumP()) {
                    endPlace = grPlace;
                }
            }
            grOutArc.settingNewTie(beginTransition);
            grOutArc.finishSettingNewTie(endPlace);
            grTieOuts.add(grOutArc);
        }
        
        GraphPetriNet graphNet =  new GraphPetriNet(net, grPlaces, grTransitions, grTieIns, grTieOuts);
        graphNet.changeLocation(paneCenter);
        return graphNet;
    }
    
    public PetriNet convertMethodToPetriNet(String methodText) throws ExceptionInvalidNetStructure { // added by Katya 16.10.2016
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        
        String invalidMethodTextMessage = "Method text is invalid.";
        
        Pattern pattern = Pattern.compile(Pattern.quote("d_P.add(new PetriP(\"") + "(.*?)" + Pattern.quote("\",") + "(.*?)" + Pattern.quote("));"));
        Matcher matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            String pName = match1;
            int mark = Integer.parseInt(match2);
            d_P.add(new PetriP(pName, mark));
        }
        
        pattern = Pattern.compile(Pattern.quote("d_T.add(new PetriT(\"") + "(.*?)" + Pattern.quote("\",") + "(.*?)" + Pattern.quote("));"));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            String tName = match1;
            double param = Double.parseDouble(match2);
            d_T.add(new PetriT(tName, param));
        }
        
        pattern = Pattern.compile(Pattern.quote("d_T.get(") + "(.*?)" + Pattern.quote(").setDistribution(\"") + "(.*?)" + Pattern.quote("\", d_T.get("));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            int j = Integer.parseInt(match1);
            String distribution = match2;
            d_T.get(j).setDistribution(distribution, d_T.get(j).getTimeServ());
        }
        
        pattern = Pattern.compile(Pattern.quote("d_T.get(") + "(.*?)" + Pattern.quote(").setParamDeviation(") + "(.*?)" + Pattern.quote(");"));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            int j = Integer.parseInt(match1);
            double paramDeviation = Double.parseDouble(match2);
            d_T.get(j).setParamDeviation(paramDeviation);
        }
        
        pattern = Pattern.compile(Pattern.quote("d_T.get(") + "(.*?)" + Pattern.quote(").setPriority(") + "(.*?)" + Pattern.quote(");"));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            int j = Integer.parseInt(match1);
            int priority = Integer.parseInt(match2);
            d_T.get(j).setPriority(priority);
        }
        
        pattern = Pattern.compile(Pattern.quote("d_T.get(") + "(.*?)" + Pattern.quote(").setProbability(") + "(.*?)" + Pattern.quote(");"));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            int j = Integer.parseInt(match1);
            double probability = Double.parseDouble(match2);
            d_T.get(j).setProbability(probability);
        }
        
        pattern = Pattern.compile(Pattern.quote("d_In.add(new ArcIn(d_P.get(") + "(.*?)" + Pattern.quote("),d_T.get(")
                + "(.*?)" + Pattern.quote("),") + "(.*?)" + Pattern.quote("));"));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            String match3 = matcher.group(3);
            int numP = Integer.parseInt(match1);
            int numT = Integer.parseInt(match2);
            int quantity = Integer.parseInt(match3);
            d_In.add(new ArcIn(d_P.get(numP), d_T.get(numT), quantity));
        }
        
        pattern = Pattern.compile(Pattern.quote("d_In.get(") + "(.*?)" + Pattern.quote(").setInf(true);"));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match = matcher.group(1);
            int j = Integer.parseInt(match);
            d_In.get(j).setInf(true);
        }
        
        pattern = Pattern.compile(Pattern.quote("d_Out.add(new ArcOut(d_T.get(") + "(.*?)" + Pattern.quote("),d_P.get(")
                + "(.*?)" + Pattern.quote("),") + "(.*?)" + Pattern.quote("));"));
        matcher = pattern.matcher(methodText);
        while (matcher.find()) {
            String match1 = matcher.group(1);
            String match2 = matcher.group(2);
            String match3 = matcher.group(3);
            int numT = Integer.parseInt(match1);
            int numP = Integer.parseInt(match2);
            int quantity = Integer.parseInt(match3);
            d_Out.add(new ArcOut(d_T.get(numT), d_P.get(numP), quantity));
        }
        
        String netName = "";
        pattern = Pattern.compile(Pattern.quote("PetriNet d_Net = new PetriNet(\"") + "(.*?)" + Pattern.quote("\","));
        matcher = pattern.matcher(methodText);
        if (matcher.find()) {
            netName = matcher.group(1);
        } else {
            throw new ExceptionInvalidNetStructure(invalidMethodTextMessage);
        }
        
        PetriNet net = new PetriNet(netName, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();
        
        return net;
    }
    
    public String openMethod(PetriNetsPanel panel, String methodFullName, JFrame frame) throws ExceptionInvalidNetStructure { // added by Katya 16.10.2016
        String methodName = methodFullName.substring(0, methodFullName.indexOf("(")); // modified by Katya 22.11.2016 (till the "try" block)
        String paramsString = methodFullName.substring(methodFullName.indexOf("(") + 1);
        paramsString = paramsString.substring(0, paramsString.length() - 1);
        String initialParamsString = paramsString;
        int paramsNumber = (paramsString.length() - paramsString.replace(" arg", "").length()) / 4;
        paramsString = paramsString.replaceAll("arg\\d+", "\\\\\\E(.*?)\\\\\\Q");
        paramsString = "\\Q" + paramsString;
        paramsString = paramsString.substring(0, paramsString.length() - 2);
        String pnetName = "";
        FileInputStream fis = null;
        try {
            String libraryText = "";
            fis = new FileInputStream(new File(".").getCanonicalPath() + "\\src\\libnet\\NetLibrary.java"); // modified by Katya 23.10.2016
            int content;
            while ((content = fis.read()) != -1) {
		libraryText += (char) content;
            }
            String methodBeginning = "public static PetriNet " + methodName + "("; // modified by Katya 20.11.2016
            String methodEnding = "return d_Net;";
            String methodText = "";
            Pattern pattern = Pattern.compile(Pattern.quote(methodBeginning) + paramsString + Pattern.quote(")") + "([[^}]^\\r]*)" + Pattern.quote(methodEnding)); // modified by Katya 22.11.2016
            Matcher matcher = pattern.matcher(libraryText);
            if (matcher.find()) {
                methodText = methodBeginning + initialParamsString + ")" + matcher.group(paramsNumber + 1) + methodEnding + "}"; // modified by Katya 22.11.2016
            } else {
                throw new FileNotFoundException();
            }
            PetriNetsFrame petriNetsFrame = (PetriNetsFrame)frame;
            JScrollPane pane = petriNetsFrame.GetPetriNetPanelScrollPane();
            Point paneCenter = new Point(pane.getLocation().x+pane.getBounds().width/2, pane.getLocation().y+pane.getBounds().height/2);
            GraphPetriNet net = generateGraphNetBySimpleNet(convertMethodToPetriNet(methodText), paneCenter);
            panel.addGraphNet(net);
            pnetName = net.getPetriNet().getName();
            panel.repaint();
        } catch (FileNotFoundException e) {
            System.out.println("Method not found");
        } catch (IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pnetName.substring(0, pnetName.length());
    }

    public void saveNetAsMethod(GraphPetriNet pnet, JTextArea area) throws ExceptionInvalidNetStructure {

        PetriNet net;
        if (pnet.getPetriNet() == null) {
            pnet.createPetriNet("Untitled");
        }
        net = pnet.getPetriNet();
        area.setText("\n");
        area.append(
                "public static PetriNet CreateNet" + net.getName() + "() throws ExceptionInvalidNetStructure {\n"
                + "\t" + "ArrayList<PetriP> d_P = new ArrayList<>();\n"
                + "\t" + "ArrayList<PetriT> d_T = new ArrayList<>();\n"
                + "\t" + "ArrayList<ArcIn> d_In = new ArrayList<>();\n"
                + "\t" + "ArrayList<ArcOut> d_Out = new ArrayList<>();\n");
        for (PetriP P : net.getListP()) {
            area.append(
                    "\t" + "d_P.add(new PetriP(" + "\"" + P.getName() + "\"," + P.getMark() + "));\n");
        }

        int j = 0;
        for (PetriT T : net.getListT()) {
            area.append("\t" + "d_T.add(new PetriT(" + "\"" + T.getName() + "\"," + T.getParametr() + "));\n");

            if (T.getDistribution() != null) {
                area.append("\t" + "d_T.get(" + j + ").setDistribution(\"" + T.getDistribution() + "\", d_T.get(" + j + ").getTimeServ());\n");
                area.append("\t" + "d_T.get(" + j + ").setParamDeviation(" + T.getParamDeviation() + ");\n");

            }
            if (T.getPriority() != 0) {
                area.append("\t" + "d_T.get(" + j + ").setPriority(" + T.getPriority() + ");\n");
            }
            if (T.getProbability() != 1.0) {
                area.append("\t" + "d_T.get(" + j + ").setProbability(" + T.getProbability() + ");\n");
            }
            j++;
        }
        j = 0;
        for (ArcIn In : net.getTieIn()) {
            area.append(
                    "\t" + "d_In.add(new ArcIn(" + "d_P.get(" + In.getNumP() + ")," + "d_T.get(" + In.getNumT() + ")," + In.getQuantity() + "));\n");

            if (In.getIsInf() == true) {
                area.append(
                        "\t" + "d_In.get(" + j + ").setInf(true);\n");
            }
            j++;
        }

        for (ArcOut Out : net.getTieOut()) {
            area.append(
                    "\t" + "d_Out.add(new ArcOut(" + "d_T.get(" + Out.getNumT() + ")," + "d_P.get(" + Out.getNumP() + ")," + Out.getQuantity() + "));\n");
        }

        area.append(
                "\t" + "PetriNet d_Net = new PetriNet(\"" + net.getName() + "\",d_P,d_T,d_In,d_Out);\n");

        area.append(
                "\t" + "PetriP.initNext();\n"
                + "\t" + "PetriT.initNext();\n"
                + "\t" + "ArcIn.initNext();\n"
                + "\t" + "ArcOut.initNext();\n"
                + "\n\t" + "return d_Net;\n");

        area.append("}");

    }

    public void saveMethodInNetLibrary(PetriNetsPanel panel, PetriNetsFrame frame, JTextArea area) {  //added by Inna 20.05.2013
        try {

            String directory = new File(".").getCanonicalPath(); //читаємо поточну директорію
            
            RandomAccessFile f = new RandomAccessFile(directory + "\\src\\libnet\\NetLibrary.java", "rw");
            //  RandomAccessFile f = new RandomAccessFile(directory + "/src" + "/NetLibrary.java", "rw");
                                //відкриваємо файл NetLibrary для читання та записування даних
            // файл відкриваємо у директорії, де знаходиться основний проект користувача
            // файл NetLibrary розміщується у пакеті з таким самим іменем, що проект
            System.out.println(directory + "\n" + directory + "\\src" +  "\\NetLibrary.java");
            //якщо файлу немає, виникне помилка.....    
            long n = f.length();  //длина файла 
            if (n == 0) {
                f.writeBytes("package " + directory.substring(directory.lastIndexOf("\\") + 1).toLowerCase() + ";\n"
                        +"import PetriObj.ExceptionInvalidNetStructure;\n"
                        + "import PetriObj.PetriNet;\n"
                        + "import PetriObj.PetriP;\n"
                        + "import PetriObj.PetriT;\n"
                        + "import PetriObj.ArcIn;\n"
                        + "import PetriObj.ArcOut;\n"
                        + "import java.util.ArrayList;\n"
                        + "public class NetLibrary {\n\n"
                        + "}");
           //JOptionPane.showMessageDialog(area, "Method can't be saved. \n Class NetLibrary must be in main directory of your project.");
                //return;
                n = f.length();
            }

            n -= 1;
            f.seek(n);  //встановлюємо вказівник файлу на елемент з номером n           

            String c = f.readLine();
            while (c != null && !c.contains("}") && n > 0) {  //читаем, пока не обнаружим строку, содержащую скобочку
                //   System.out.println("n= "+n+ ",   line= "+c);
                n -= 1;  //передвинуть на один символ
                f.seek(n);
                c = f.readLine();
            }
      // System.out.println("n= "+n+ ",   line= "+c+" pointer= "+f.getFilePointer() );

            if (n > 0) {
                f.seek(n - 1);
                String s = area.getText() + "\n" + c;  //добавляем отброшенную скобочку
                f.write(s.getBytes());

                JOptionPane.showMessageDialog(area, "Method was successfully added. See in class NetLibrary.");
            } else {
                JOptionPane.showMessageDialog(area, "symbol '}' doesn't find in file NetLibrary.java");
            }
            f.close();
        } catch (IOException ex) {
            Logger.getLogger(PetriNetsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
