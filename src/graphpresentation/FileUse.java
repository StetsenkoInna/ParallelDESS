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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import graphnet.GraphPetriNet;

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
            panel.addPetriNet(net); //можливо переназвати його setGraphNet(net)???
            pnetName = net.getPetriNet().getName();
            ois.close();
            // 11.01.13 якщо задана поточна точка на панелі, то центр мережі буде в цій точці
            if (panel.getCurrentPlacementPoint() != null) {
                panel.getLastGraphNetList().changeLocation(panel.getCurrentPlacementPoint());
            }
            panel.repaint();

        } catch (FileNotFoundException e) {
            System.out.println("Such file was not found");
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

    public void saveNetAsMethod(GraphPetriNet pnet, JTextArea area) throws ExceptionInvalidNetStructure {

        PetriNet net;
        if (pnet.getPetriNet() == null) {
            pnet.createPetriNet("Untitled");
        }
        net = pnet.getPetriNet();
        area.setText("\n");
        area.append(
                "public static PetriNet CreateNet" + net.getName() + "() throws ExceptionInvalidNetStructure {\n"
                + "\t" + "ArrayList<PetriP> d_P = new ArrayList<PetriP>();\n"
                + "\t" + "ArrayList<PetriT> d_T = new ArrayList<PetriT>();\n"
                + "\t" + "ArrayList<TieIn> d_In = new ArrayList<TieIn>();\n"
                + "\t" + "ArrayList<TieOut> d_Out = new ArrayList<TieOut>();\n");
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
        for (ArcIn In : net.getArcIn()) {
            area.append(
                    "\t" + "d_In.add(new TieIn(" + "d_P.get(" + In.getNumP() + ")," + "d_T.get(" + In.getNumT() + ")," + In.getQuantity() + "));\n");

            if (In.getIsInf() == true) {
                area.append(
                        "\t" + "d_In.get(" + j + ").setInf(true);\n");
            }
            j++;
        }

        for (ArcOut Out : net.getArcOut()) {
            area.append(
                    "\t" + "d_Out.add(new TieOut(" + "d_T.get(" + Out.getNumT() + ")," + "d_P.get(" + Out.getNumP() + ")," + Out.getQuantity() + "));\n");
        }

        area.append(
                "\t" + "PetriNet d_Net = new PetriNet(\"" + net.getName() + "\",d_P,d_T,d_In,d_Out);\n");

        area.append(
                "\t" + "PetriP.initNext();\n"
                + "\t" + "PetriT.initNext();\n"
                + "\t" + "TieIn.initNext();\n"
                + "\t" + "TieOut.initNext();\n"
                + "\n\t" + "return d_Net;\n");

        area.append("}");

    }

    public void saveMethodInNetLibrary(PetriNetsPanel panel, PetriNetsFrame frame, JTextArea area) {  //added by Inna 20.05.2013
        try {

            String directory = new File(".").getCanonicalPath(); //читаємо поточну директорію

           
           // RandomAccessFile f = new RandomAccessFile(directory + "/src/" + directory.substring(directory.lastIndexOf("\\") + 1) + "/NetLibrary.java", "rw");
             RandomAccessFile f = new RandomAccessFile(directory + "/src/libnet" + "/NetLibrary.java", "rw");
                                //відкриваємо файл NetLibrary для читання та записування даних
            // файл відкриваємо у директорії, де знаходиться основний проект користувача
            // файл NetLibrary розміщується у пакеті з таким самим іменем, що проект
            System.out.println(directory + "\n" + directory + "\\src\\" + directory.substring(directory.lastIndexOf("\\") + 1) + "\\NetLibrary.java");
            //якщо файлу немає, виникне помилка.....    
            long n = f.length();  //длина файла 
            if (n == 0) {
                f.writeBytes("package " + directory.substring(directory.lastIndexOf("\\") + 1).toLowerCase() + ";\n"
                        + "import PetriObj.PetriNet;\n"
                        + "import PetriObj.PetriP;\n"
                        + "import PetriObj.PetriT;\n"
                        + "import PetriObj.TieIn;\n"
                        + "import PetriObj.TieOut;\n"
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
