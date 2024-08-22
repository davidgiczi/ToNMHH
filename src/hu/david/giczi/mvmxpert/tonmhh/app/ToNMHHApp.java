package hu.david.giczi.mvmxpert.tonmhh.app;

import hu.david.giczi.mvmxpert.tonmhh.service.FileProcess;
import javax.swing.*;


public class ToNMHHApp {

    public static void main(String[] args) {

        FileProcess fp = new FileProcess();
        fp.openInputFile();
        if( FileProcess.PARCEL_DATA_LIST == null || FileProcess.PARCEL_DATA_LIST.isEmpty() ){
            return;
        }
        String fileName = JOptionPane.showInputDialog(null, "Menteni kívánt fájl neve:",
                "Fájl nevének megadása", JOptionPane.QUESTION_MESSAGE);
        if( fileName == null || fileName.isEmpty() ){
            JOptionPane.showMessageDialog(null,
                    "Menteni kívánt fájl nevének megadása szükséges.",
                    "Földrészlet adatok nem menthetők",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        fp.chooseParcelDataFolder(fileName);
        if( FileProcess.FOLDER_PATH == null ){
            JOptionPane.showMessageDialog(null,
                    "Földrészlet adatok nem menthetők.",
                    "Hibás mentés",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
