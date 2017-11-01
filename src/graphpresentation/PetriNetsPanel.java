/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphpresentation;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriP;
import PetriObj.PetriT;
import PetriObj.ArcIn;
import PetriObj.ArcOut;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import graphnet.GraphPetriNet;
import graphnet.GraphPetriPlace;
import graphnet.GraphPetriTransition;
import graphnet.GraphArcIn;
import graphnet.GraphArcOut;
import java.awt.geom.AffineTransform;

/**
 * Creates new form PetriNetsPanel
 *
 * @author Ольга
 */
public class PetriNetsPanel extends javax.swing.JPanel {

    /**
     * Creates new form PetriNetsPanel
     */
    private static int id; // нумерація графічних елементів
    private GraphPetriNet graphNet;  //added 4.12.2012
    private List<GraphPetriNet> graphNetList = new ArrayList();  // для відображення кількох мереж  09.01.13
    private boolean isSettingTie;
    private GraphElement current;
    private GraphElement choosen;
    private GraphArc currentTie;
    private GraphArc choosenTie;
    private int savedId;
    public SetArc setTieFrame = new SetArc(this);
    public SetPosition setPositionFrame = new SetPosition(this);
    public SetTransition setTransitionFrame = new SetTransition(this);
   // private Point currentPlacementPoint; // поточна точка на панелі, вибрана користувачем  09.01.13
    private JTextField nameTextField;
  //  private final String COPY_NAME = "_copy";
    private final String DEFAULT_NAME = "Untitled";
  //  private AffineTransform at = new AffineTransform();

    public PetriNetsPanel(JTextField textField) {

        initComponents();
        this.setBackground(Color.WHITE);
        
        
        nameTextField = textField;
        this.setNullPanel(); // починаємо заново створювати усі списки графічних елементів  //додано 3.12.2012
        setFocusable(true);
        
    

        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
       

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               /* System.out.println("keyPressed:  e "+e.getKeyCode());
                if(choosen!=null)System.out.println("keyPressed: choosen "+choosen.getName());
                    else  System.out.println("keyPressed:  choosen null");
                if(choosenTie!=null)System.out.println("keyPressed: choosenTie"+choosenTie.getQuantity());
                    else  System.out.println("keyPressed:  choosenTie null");*/
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (choosenTie != null) {
                        removeTie(choosenTie);
                        choosenTie = null;
                        currentTie = null;
                    }
                    if (choosen != null) {
                        try {
                            remove(choosen);
                            choosen = null;
                            current = null;
                        } catch (ExceptionInvalidNetStructure ex) {
                            Logger.getLogger(PetriNetsPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

    }

    private void removeTie(GraphArc s) {
        if (s == null) {
            return;
        }
        if (s == currentTie) {
            currentTie = null;
        }

        if (s.getClass().equals(GraphArcOut.class)) {
            graphNet.getGraphTieOutList().remove((GraphArcOut) s); //added by Inna 4.12.2012

        } else {
            graphNet.getGraphTieInList().remove((GraphArcIn) s); //added by Inna 4.12.2012
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        this.requestFocusInWindow(); //added 1.06.2013
        //додано 3.12.2012
        if (graphNet == null) {
            graphNet = new GraphPetriNet();
        }
        //тобто на початку роботи встановлюється графічна мережа з порожніми списками графічних елементів та порожньою мережею Петрі!!!
/*        if (currentPlacementPoint != null) {
            paintCurrentPlacementPoint(g2);
        }*/
        graphNet.paintGraphPetriNet(g2, g);
        // промальовуємо всі мережі
        for (GraphPetriNet pnet : graphNetList) {
            if (pnet != graphNet) {
                pnet.paintGraphPetriNet(g2, g);
            }
        }
        if (currentTie != null) {
            g2.setColor(Color.BLUE);
            g.setColor(Color.BLUE);
            currentTie.drawGraphElement(g2);
        }
        if (choosenTie != null) {
            g2.setColor(Color.BLUE);
            g.setColor(Color.BLUE);
            choosenTie.drawGraphElement(g2);
        }
        if (current != null) {
            g2.setColor(Color.BLUE);
            current.drawGraphElement(g2);
        }
        if (choosen != null) {
            g2.setColor(Color.BLUE);
            choosen.drawGraphElement(g2);
        }
        
        
        //12.01.16
       
     //   this.transform(g2);
        
    }

    public GraphElement find(Point2D p) {
        for (GraphPetriPlace pp : graphNet.getGraphPetriPlaceList()) {
            if (pp.isGraphElement(p)) {
                return pp;
            }
        }
        for (GraphPetriTransition pt : graphNet.getGraphPetriTransitionList()) {
            if (pt.isGraphElement(p)) {
                return pt;
            }
        }
        // 11.01.13
        // якщо є декілька мереж, то ведеться пошук по всім мережам і встановлюється Поточна мережа та, в якій буде знайдено елемент
        for (GraphPetriNet pnet : graphNetList) {
            for (GraphPetriPlace pp : pnet.getGraphPetriPlaceList()) {
                if (pp.isGraphElement(p)) {
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                      /*  if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return pp;

                }
            }
            for (GraphPetriTransition pt : pnet.getGraphPetriTransitionList()) {
                if (pt.isGraphElement(p)) {
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                      /*  if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return pt;
                }
            }
        }
        return null;
    }

    public GraphArc findTie(Point2D p) {
        for (GraphArcOut to : graphNet.getGraphTieOutList()) {
            if (to.isEnoughDistance(p)) {
                return to;
            }
        }
        for (GraphArcIn ti : graphNet.getGraphTieInList()) {
            if (ti.isEnoughDistance(p)) {
                return ti;
            }
        }
        for (GraphPetriNet pnet : graphNetList) {
            for (GraphArcOut to : pnet.getGraphTieOutList()) {
                if (to.isEnoughDistance(p)) {
                   // System.out.println("Current element is from  net = " + pnet.getPetriNet().getName());
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                      /*  if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return to;
                }
            }
            for (GraphArcIn ti : pnet.getGraphTieInList()) {
                if (ti.isEnoughDistance(p)) {
                   // System.out.println("Current element is from  net = " + pnet.getPetriNet().getName());
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                    /*    if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return ti;
                }
            }
        }
        return null;
    }

    public void remove(GraphElement s) throws ExceptionInvalidNetStructure {
        if (s == null) {
            return;
        }
        if (s == current) {
            current = null;

        }
       /* if(current!=null)System.out.println("remove : "+current.getName()+"  "+s.getName());
        else System.out.println("remove : current null");*/
        graphNet.delGraphElement(s); //added by Inna 4.12.2012

        repaint();
    }

    public class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (current != null) {
                current = null;
                repaint();
            } else {
                current = find(e.getPoint());
                if (current != null) {
                    current.setNewCoordinates(e.getPoint());
                    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    choosen = current;

                    String currentType = current.getType(); // added by Katya 23.10.2016
                    int currentId = current.getId();
                    Point2D currentCenter = current.getGraphElementCenter();
                    
                    for (GraphArcOut to : graphNet.getGraphTieOutList()) {
                        if (((GraphPetriTransition)to.getBeginElement()).getId() == currentId && to.getBeginElement().getType().equals(currentType)) {
                            to.movingBeginElement(currentCenter);
                            to.changeBorder();
                        }
                        if (((GraphPetriPlace)to.getEndElement()).getId() == currentId && to.getEndElement().getType().equals(currentType)) {
                            to.movingEndElement(currentCenter);
                            to.changeBorder();
                        }
                    }
                    for (GraphArcIn ti : graphNet.getGraphTieInList()) {
                        if (((GraphPetriPlace)ti.getBeginElement()).getId() == currentId && ti.getBeginElement().getType().equals(currentType)) {
                            ti.movingBeginElement(currentCenter);
                            ti.changeBorder();
                        }
                        if (((GraphPetriTransition)ti.getEndElement()).getId() == currentId && ti.getEndElement().getType().equals(currentType)) {
                            ti.movingEndElement(currentCenter);
                            ti.changeBorder();
                        }
                    }
                    choosenTie = null;
                }
               // currentPlacementPoint = e.getPoint();
            }

            if (isSettingTie == true) {
                current = find(e.getPoint());
                if (current != null) {
                    if (current.getClass().equals(GraphPetriPlace.class)) {
                        currentTie = new GraphArcIn();
                        graphNet.getGraphTieInList().add((GraphArcIn) currentTie); //3.12.2012
                        currentTie.settingNewTie(current); //set begin element, point and setting LINe(0,0)
                    } else if (current.getClass().equals(GraphPetriTransition.class)) { //26.01.2013
                        currentTie = new GraphArcOut();
                        graphNet.getGraphTieOutList().add((GraphArcOut) currentTie); //3.12.2012
                        currentTie.settingNewTie(current);
                    }
                } else {    //26.01.2013
                    isSettingTie = false;
                }
                // System.out.println("after added tie we have such graph net:");
                // graphNet.print();
            }
            isSettingTie = false;//26.01.2013
            choosenTie = null;

            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (current != null) {
                current.setNewCoordinates(e.getPoint());
            } else {
                current = find(e.getPoint());
                
                if (current != null && e.getClickCount() >= 2) { //change 2->1??
                    String currentType = current.getType(); // added by Katya 23.10.2016
                    int currentId = current.getId();
                    Point2D currentCenter = current.getGraphElementCenter();
                    
                    choosen = current;
                    for (GraphArcIn ti : graphNet.getGraphTieInList()) {
                        if (((GraphPetriPlace)ti.getBeginElement()).getId() == currentId && ti.getBeginElement().getType().equals(currentType)) {
                            ti.movingBeginElement(currentCenter);
                            ti.changeBorder();
                            break;
                        }
                        if (((GraphPetriTransition)ti.getEndElement()).getId() == currentId && ti.getEndElement().getType().equals(currentType)) {
                            ti.movingEndElement(currentCenter);
                            ti.changeBorder();
                            break;
                        }
                    }
                    for (GraphArcOut to : graphNet.getGraphTieOutList()) {
                        if (((GraphPetriTransition)to.getBeginElement()).getId() == currentId && to.getBeginElement().getType().equals(currentType)) {
                            to.movingBeginElement(currentCenter);
                            to.changeBorder();
                            break;
                        }
                        if (((GraphPetriPlace)to.getEndElement()).getId() == currentId && to.getEndElement().getType().equals(currentType)) {
                            to.movingEndElement(currentCenter);
                            to.changeBorder();
                            break;
                        }
                    }

                    if (choosen.getClass().equals(GraphPetriPlace.class)) {
                        setPositionFrame.setVisible(true);
                        setPositionFrame.setInfo(choosen);

                    } else {
                        setTransitionFrame.setVisible(true);
                        setTransitionFrame.setInfo(choosen);
                    }
                }

                currentTie = findTie(e.getPoint());
                if (currentTie != null && e.getClickCount() >= 2) {
                    choosenTie = currentTie;
                    setTieFrame.setVisible(true);
                    setTieFrame.setInfo(choosenTie);
                }
                if (currentTie != null) {
                    choosenTie = currentTie;
                    choosen = null;
                    currentTie = null;
                }
            }
            current = null;

            setCursor(Cursor.getDefaultCursor());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            current = null;
            setCursor(Cursor.getDefaultCursor());
            if (currentTie != null) {
                current = find(e.getPoint());
                if (current != null) {
                    if (currentTie.finishSettingNewTie(current)) {
                        currentTie.setPetriElements();
                        currentTie.changeBorder();
                        currentTie.updateCoordinates();
                        isSettingTie = false;
                        
                        int currBeginId, currEndId;
                        if (currentTie.getClass().equals(GraphArcIn.class)) {
                            currBeginId = ((GraphPetriPlace)currentTie.getBeginElement()).getId();
                            currEndId = ((GraphPetriTransition)currentTie.getEndElement()).getId();
                        } else {
                            currBeginId = ((GraphPetriTransition)currentTie.getBeginElement()).getId();
                            currEndId = ((GraphPetriPlace)currentTie.getEndElement()).getId();
                        }

                        for (GraphArcIn ti : graphNet.getGraphTieInList()) {
                            if (((GraphPetriPlace)ti.getBeginElement()).getId() == currEndId && ((GraphPetriTransition)ti.getEndElement()).getId() == currBeginId) {
                                currentTie.twoTies(ti);
                                currentTie.updateCoordinates();
                            }
                        }
                        for (GraphArcOut to : graphNet.getGraphTieOutList()) {
                            if (((GraphPetriTransition)to.getBeginElement()).getId() == currEndId && ((GraphPetriPlace)to.getEndElement()).getId() == currBeginId) {
                                currentTie.twoTies(to);
                                currentTie.updateCoordinates();
                            }
                        }
                        currentTie = null;
                    } else {                        //1.02.2013 цей фрагмент дозволяє відслідковувати намагання 
                        removeCurrentTie();// з"єднати позицію з позицією чи перехід з переходом
                        //та знищувати неправильно намальовану дугу
                    }

                    current = null;
                } else {
                    removeCurrentTie();//1.02.2013;
                }
            }
            currentTie = null;
            repaint();
        }
    }

    private void removeCurrentTie() { //1.02.2013 цей метод дозволяє знищувати намальовану дугу
        if (currentTie.getClass().equals(GraphArcIn.class)) // 
        {
            graphNet.getGraphTieInList().remove(currentTie);
        } else if (currentTie.getClass().equals(GraphArcOut.class)) {
            graphNet.getGraphTieOutList().remove(currentTie);
        } else ;
        currentTie = null;
        repaint();
    }

    private class MouseMotionHandler implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (current != null && currentTie == null) {  //пересування позиції чи переходу
                // currentPlacementPoint = null;
                current.setNewCoordinates(e.getPoint());
                
                String currentType = current.getType(); // added by Katya 23.10.2016
                int currentId = current.getId();
                Point ePoint = e.getPoint();
                
                for (GraphArcIn ti : graphNet.getGraphTieInList()) {
                    if (((GraphPetriPlace)ti.getBeginElement()).getId() == currentId && ti.getBeginElement().getType().equals(currentType)) {
                        ti.movingBeginElement(ePoint);
                        ti.changeBorder();
                    }
                    if (((GraphPetriTransition)ti.getEndElement()).getId() == currentId && ti.getEndElement().getType().equals(currentType)) {
                        ti.movingEndElement(ePoint);
                        ti.changeBorder();
                    }
                }
                for (GraphArcOut to : graphNet.getGraphTieOutList()) {
                    if (((GraphPetriTransition)to.getBeginElement()).getId() == currentId && to.getBeginElement().getType().equals(currentType)) {
                        to.movingBeginElement(ePoint);
                        to.changeBorder();
                    }
                    if (((GraphPetriPlace)to.getEndElement()).getId() == currentId && to.getEndElement().getType().equals(currentType)) {
                        to.movingEndElement(ePoint);
                        to.changeBorder();
                    }
                }

                repaint();
            }
            // коли малюємо дугу
            if (currentTie != null && current != null) {
                // System.out.println("Setting new coordinates for tie");
                currentTie.setNewCoordinates(e.getPoint());
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (current != null && currentTie == null) {
               // currentPlacementPoint = null;
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                current.setNewCoordinates(e.getPoint());
                repaint();
            }
        }
    }

    public GraphElement getCurrent() {
        return current;
    }

    public void setCurrent(GraphElement e) {
        current = e;
    }

    public GraphElement getChoosen() {
        return choosen;
    }

    public void setCurrentGraphTie(GraphArc t) {
        currentTie = t;
    }

    public GraphArc getCurrentGraphTie() {
        return currentTie;
    }

    public GraphArc getChoosenTie() {
        return choosenTie;
    }

    public int getSavedId() {
        return savedId;
    }

    public void saveId() {
        this.savedId = id;
    }

    public static String getPetriTName() {
        return "T" + id;
    }

    public static String getPetriPName() {
        return "P" + id;
    }

    public void setIsSettingTie(boolean b) { //26.01.2013
        isSettingTie = b;
    }

    public final void setNullPanel() {
        current = null;
        currentTie = null;
        choosen = null;
        choosenTie = null;
        id = 0;
        PetriP.initNext(); //ось тут і обнуляється, а я шукаю...
        PetriT.initNext(); //навіть коли читаємо з файлу...
        ArcIn.initNext(); //додано Інна 20.11.2012
        ArcOut.initNext(); //додано Інна 20.11.2012
        GraphPetriPlace.setNullSimpleName();
        GraphPetriTransition.setNullSimpleName();
        graphNetList = new ArrayList(); // 15.01.13
        graphNet = new GraphPetriNet();
        repaint();
    }

    public void addGraphNet(GraphPetriNet net) {  //Тепер написаний цей метод... //можливо достатньо скористатись setNet???

        // graphNet = net; //4.12.2012  createCopy() НЕ працює...
        //02.02.2012

        graphNetList.add(graphNet);
        graphNetList.add(net);
        graphNet = net;

        int maxIdPetriNet = 0; //
        for (GraphPetriPlace pp : graphNet.getGraphPetriPlaceList()) {  //відшукуємо найбільший id для позицій
            if (maxIdPetriNet < pp.getId()) {
                maxIdPetriNet = pp.getId();
            }
        }
        for (GraphPetriTransition pt : graphNet.getGraphPetriTransitionList()) { //відшукуємо найбільший id для переходів і позицій 
            if (maxIdPetriNet < pt.getId()) {
                maxIdPetriNet = pt.getId();
            }
        }
        if (maxIdPetriNet > id) // встановлюємо новий id - найбільший
        {
            id = maxIdPetriNet;
        }
        id++;
        // graphNetList.add(graphNet); //11.01.13
        repaint();
    }

    public void deletePetriNet() {
        graphNet = null;
        repaint();
    }

    public GraphPetriNet getGraphNet() {
        return graphNet;
    }

    public void setGraphNet(GraphPetriNet net) { //коректно працює тільки якщо потім не змінювати граф
        //рекомендується використовувати addGraphNet
        graphNet = net;
        repaint();
    }

    public List<GraphPetriNet> getGraphNetList() {  //11.01.13
        return graphNetList;
    }

    public GraphPetriNet getLastGraphNetList() {  //11.01.13
        return graphNetList.get(graphNetList.size() - 1);
    }

    public static int getIdPosition() {  //назва методу не за стандартом Чоме немає id для зв"язків?
        return id++;
    }

    public static int getIdTransition() { //назва методу не за стандартом 
        return id++;
    }

 /*   public Point getCurrentPlacementPoint() { //09.01.13
        return currentPlacementPoint;
    }*/

    //11.01.13
  /*  private void paintCurrentPlacementPoint(Graphics2D g2) {
        Double x1 = currentPlacementPoint.getX();
        Double y1 = currentPlacementPoint.getY() - 5;
        Double y2 = y1 + 10;
        g2.drawLine(x1.intValue(), y1.intValue(), x1.intValue(), y2.intValue());
        x1 = x1 - 5;
        Double x2 = x1 + 10;
        y1 = y1 + 5;
        g2.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y1.intValue());
    }*/
    
    //12.01.16
  /*  public void transform(Graphics2D g2){
      //  AffineTransform at = g2.getTransform();
        AffineTransform toCenterAt = new AffineTransform();
        toCenterAt.concatenate(at);
     
        toCenterAt.scale(0.1, 0.1);
        g2.transform(toCenterAt);
        g2.setTransform(at);
        
        System.out.println("TRANSFORM PANEL");
        
        
    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(229, 229, 229));
        setPreferredSize(new java.awt.Dimension(20000, 20000));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
