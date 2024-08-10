package hu.david.giczi.mvmxpert.tonmhh.service;

import hu.david.giczi.mvmxpert.tonmhh.model.ParcelData;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileProcess {

    public static List<ParcelData> PARCEL_DATA_LIST;
    public static String FOLDER_PATH;
    public static String FILE_NAME;


    public void openInputFile() {
        JFileChooser jfc = new JFileChooser(){

            private static final long serialVersionUID = 1L;

            @Override
            protected JDialog createDialog( Component parent ) throws HeadlessException {
                JDialog dialog = super.createDialog( parent );
                dialog.setLocationRelativeTo(null);
                dialog.setIconImage(
                        new ImageIcon(Objects.requireNonNull(
                                this.getClass().getResource("/logo/MVM.jpg"))).getImage() );
                return dialog;
            }
        };
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx");
            }

            @Override
            public String getDescription() {
                return "*.xlsx";
            }
        });
        jfc.setCurrentDirectory(FOLDER_PATH == null ?
                FileSystemView.getFileSystemView().getHomeDirectory() : new File(FOLDER_PATH));
        jfc.setDialogTitle("Földrészlet adatok fájl megnyitása");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            FILE_NAME = selectedFile.getName();
            FOLDER_PATH = selectedFile.getParent();
            try {
                getXLSXFileData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            FOLDER_PATH = null;
            FILE_NAME = null;
        }
    }


    private void getXLSXFileData() throws IOException {
        FileInputStream fis = new FileInputStream(FOLDER_PATH + "/" + FILE_NAME);
        String password = JOptionPane.showInputDialog(null, "Jelszó megadása:",
                    "A fájl jelszóval védett?", JOptionPane.WARNING_MESSAGE);
        if( password == null ){
            return;
        }
        XSSFWorkbook workbook;
         try{
             workbook = (XSSFWorkbook) WorkbookFactory.create(fis, "4558_2024");
         }catch (EncryptedDocumentException e){
             JOptionPane.showMessageDialog(null, "A fájl nem nyitható meg.",
                     "Jelszó megadása szükséges", JOptionPane.INFORMATION_MESSAGE);
             return;
         }
        XSSFSheet sheet = workbook.getSheetAt(0);
        parseParcelData(sheet);
    }

    private void parseParcelData(XSSFSheet sheet){
        PARCEL_DATA_LIST = new ArrayList<>();
        ParcelData parcelData = new ParcelData();
        String parcelId;
        for (Row row : sheet) {
            if( row.getRowNum() == 0 ){
                continue;
            }

            parcelData.setLocation(row.getCell(1).getStringCellValue());
            parcelId = parcelData.createParcelId(row.getCell(2), row.getCell(3),
                    row.getCell(4), row.getCell(5));

            if( parcelId != null && parcelId.equals(parcelData.createParcelId(
                    row.getCell(2), row.getCell(3),
                    row.getCell(4), row.getCell(5))) &&
                    !PARCEL_DATA_LIST.contains(parcelData)) {
                parcelData.setParcelId(parcelId);
                parcelData.setTown(row.getCell(0).getStringCellValue());
                parcelData.setUtilization(row.getCell(7).getStringCellValue());
                parcelData.addMinister(row.getCell(10).getStringCellValue());
                PARCEL_DATA_LIST.add(parcelData);
            }

            if( !parcelData.getParcelId().equals(parcelData.createParcelId(
                    row.getCell(2), row.getCell(3),
                    row.getCell(4), row.getCell(5)))) {
                parcelData = new ParcelData();
                parcelData.setParcelId(parcelId);
                parcelData.setTown(row.getCell(0).getStringCellValue());
                parcelData.setLocation(row.getCell(1).getStringCellValue());
                parcelData.setUtilization(row.getCell(7).getStringCellValue());
                parcelData.addMinister(row.getCell(10).getStringCellValue());
                PARCEL_DATA_LIST.add(parcelData);
            }

            if( parcelData.getParcelId().equals(parcelData.createParcelId(
                    row.getCell(2), row.getCell(3),
                    row.getCell(4), row.getCell(5))) &&
                    PARCEL_DATA_LIST.contains(parcelData)) {
                parcelData.addMinister(row.getCell(10).getStringCellValue());
            }
        }
    }

}
