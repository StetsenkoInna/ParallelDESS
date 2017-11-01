/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphpresentation;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import graphpresentation.PetriNetsFrame;
import java.util.ArrayList;
import libnet.NetLibrary;

/**
 *
 * @author Інна
 */
public class SimTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ExceptionInvalidNetStructure {

    // цей фрагмент призначений для відкриття вікна графічної розробки мережі Петрі
    // та збереженні її у вигляді метода
        
        PetriNetsFrame f = new PetriNetsFrame();
      //  f.setVisible(true);
    // цей фрагмент призначений для запуску імітації моделі,
    // складеної з конструктивних елементів
    // результати моделювання представляються на консолі    
        
        double timeModeling = 100.0;
        PetriObjModel model = createModelTest(timeModeling);
        model.go(timeModeling);

    }

    // наступний фрагмент коду демонструє створення моделі з Петрі-об"єктів
    // на жаль, це ще не автоматизовано
 /*   private static PetriObjModel createModel(double timeMod) {
        ArrayList<PetriSim> list = new ArrayList<>();
        // розробка конструктивних елементів моделі
        PetriSim firstElement = new PetriSim(NetLibrary.CreateNetSimple(10), timeMod);
        PetriSim secondElement = new PetriSim(NetLibrary.CreateNetSimple(50), timeMod);
        // зв"язування елементів спільною позицією
        firstElement.getNet().getListP()[1] = secondElement.getNet().getListP()[1];

        // формування списку елементів
        list.add(firstElement);
        list.add(secondElement);

        // створення моделі       
        PetriObjModel model = new PetriObjModel(list);
        return model;

    }*/
 private static PetriObjModel createModelTest(double timeMod) throws ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        // розробка конструктивних елементів моделі
                // формування списку елементів
        list.add(new PetriSim(NetLibrary.CreateNetGenerator(2.0,null)));
        list.add(new PetriSim(NetLibrary.CreateNetGenerator(3.0,null)));

        // створення моделі       
        PetriObjModel model = new PetriObjModel(list);
        return model;

    }

}
