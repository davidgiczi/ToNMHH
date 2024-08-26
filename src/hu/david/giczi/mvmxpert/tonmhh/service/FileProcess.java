package hu.david.giczi.mvmxpert.tonmhh.service;

import hu.david.giczi.mvmxpert.tonmhh.model.Address;
import hu.david.giczi.mvmxpert.tonmhh.model.ParcelData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileProcess {

    public static List<ParcelData> PARCEL_DATA_LIST;
    public static String FOLDER_PATH;
    public static String FILE_NAME;
    private static final String INVALID_CHARACTERS = "[\\\\/:*?\"<>|]";


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
                JOptionPane.showMessageDialog(null,
                        "Hibás fájl.",
                        "A kiválasztott fájl nem nyitható meg",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        else {
            FOLDER_PATH = null;
            FILE_NAME = null;
        }
    }


    private void getXLSXFileData() throws IOException {
    int option =  JOptionPane.showOptionDialog(null, "A fájl jelszóval védett?",
                "Fájl megnyitása", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Igen", "Nem"}, "Igen");
        if( option == 0 ){
            String password = JOptionPane.showInputDialog(null, "Jelszó megadása:",
                    "A fájl megnyitása", JOptionPane.QUESTION_MESSAGE);

            if( password != null ){

                try {
                    openWorkbookByPassword(password);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
    }
}
       else {
        openWorkbook();
       }
    }

    private void openWorkbookByPassword(String password)
            throws IOException, GeneralSecurityException {
        try {
            FileInputStream fis = new FileInputStream(FOLDER_PATH + "/" + FILE_NAME);
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            EncryptionInfo info = new EncryptionInfo(fs);
            Decryptor decryptor = Decryptor.getInstance(info);
            if (decryptor.verifyPassword(password)) {
                XSSFWorkbook workbook = new XSSFWorkbook(decryptor.getDataStream(fs));
                XSSFSheet sheet = workbook.getSheetAt(0);
                parseParcelData(sheet);
                fis.close();
                workbook.close();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Nem megfelelő jelszó.",
                        "A kiválasztott fájl nem nyitható meg", JOptionPane.WARNING_MESSAGE);
            }
        }catch (OfficeXmlFileException e){
            JOptionPane.showMessageDialog(null,
                    "A fájl jelszó nélkül nyitható meg.",
                    "A kiválasztott fájl nem nyitható meg", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openWorkbook() throws IOException {
        FileInputStream fis =  new FileInputStream(FOLDER_PATH + "/" + FILE_NAME);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            parseParcelData(sheet);
            fis.close();
            workbook.close();
        } catch (OLE2NotOfficeXmlFileException e){
            JOptionPane.showMessageDialog(null,
                    "A fájl jelszóval védett.",
                    "A kiválasztott fájl nem nyitható meg", JOptionPane.WARNING_MESSAGE);
        }
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
                parcelData.addMinister(row.getCell(10).getStringCellValue(),
                        row.getCell(11).getStringCellValue());
                PARCEL_DATA_LIST.add(parcelData);
            }

            if( parcelData.getParcelId() != null && !parcelData.getParcelId()
                    .equals(parcelData.createParcelId(
                    row.getCell(2), row.getCell(3),
                    row.getCell(4), row.getCell(5)))) {
                parcelData = new ParcelData();
                parcelData.setParcelId(parcelId);
                parcelData.setTown(row.getCell(0).getStringCellValue());
                parcelData.setLocation(row.getCell(1).getStringCellValue());
                parcelData.setUtilization(row.getCell(7).getStringCellValue());
                parcelData.addMinister(row.getCell(10).getStringCellValue(),
                        row.getCell(11).getStringCellValue());
                PARCEL_DATA_LIST.add(parcelData);
            }

            if( parcelData.getParcelId() != null &&
                    parcelData.getParcelId().equals(parcelData.createParcelId(
                    row.getCell(2), row.getCell(3),
                    row.getCell(4), row.getCell(5))) &&
                    PARCEL_DATA_LIST.contains(parcelData)) {
                parcelData.addMinister(row.getCell(10).getStringCellValue(),
                        row.getCell(11).getStringCellValue());
            }
        }
        JOptionPane.showMessageDialog(null,
                "Beolvasott földrészletek száma: " + PARCEL_DATA_LIST.size() + " db.",
                "Metadat riport",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void chooseParcelDataFolder(String fileName) {
        fileName = convertFileName(fileName);
        if( fileName == null ){
            FOLDER_PATH = null;
            return;
        }
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
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setCurrentDirectory(FOLDER_PATH == null ?
                FileSystemView.getFileSystemView().getHomeDirectory() : new File(FOLDER_PATH));
        jfc.setDialogTitle("Földrészlet adatok fájl mentése, mentési mappa választása");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            FOLDER_PATH = selectedFile.getAbsolutePath();
            try {
                saveParcelData(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                FOLDER_PATH = null;
            }
        }
        else{
            FOLDER_PATH = null;
        }
    }

    private String convertFileName(String fileName){
        fileName = fileName.trim().replaceAll(INVALID_CHARACTERS, "_");
        if( 1 > fileName.length() ){
            return null;
        }
        else if( fileName.lastIndexOf(".") != -1 ){
             fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    private void saveParcelData(String fileName) throws IOException{
        InputStream is = FileProcess.class.getClassLoader()
                .getResourceAsStream("nmhh_template/NMHH.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(Objects.requireNonNull(is));
        File outputFile = new File(FOLDER_PATH + "/" + fileName + ".xlsx");
        if( outputFile.exists() ){
            if ( JOptionPane.showConfirmDialog(null, "Létező fájl:\n" +
                            outputFile.getAbsolutePath() , "Felülírja?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
               saveData(workbook, outputFile);
            } else {
                return;
            }
        }
        saveData(workbook, outputFile);
        JOptionPane.showMessageDialog(null,
                "Feldolgozott földrészlet adatok mentve:\n" + outputFile.getAbsolutePath(),
                "Fájl mentve",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveData(XSSFWorkbook workbook, File outputFile) throws IOException{
        FileOutputStream out = new FileOutputStream(outputFile);
        XSSFSheet sheet1 =  workbook.getSheetAt(0);
        int rowIndex = 1;
        for (ParcelData parcelData : FileProcess.PARCEL_DATA_LIST) {
            XSSFRow row = sheet1.createRow(rowIndex);
            row.createCell(0).setCellValue(parcelData.getTown());
            row.createCell(1).setCellValue(parcelData.getLocation());
            row.createCell(2).setCellValue(parcelData.getParcelId());
            rowIndex++;
        }
        rowIndex = 1;
        XSSFSheet sheet2 = workbook.getSheetAt(1);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        for (ParcelData parcelData : FileProcess.PARCEL_DATA_LIST) {
             XSSFRow row = sheet2.createRow(rowIndex);
             row.createCell(0).setCellValue(parcelData.getTown());
             row.createCell(1).setCellValue(parcelData.getParcelId());
             row.createCell(3).setCellValue(parcelData.getUtilization());
             XSSFCell cell = row.createCell(7);
             cell.setCellStyle(cellStyle);
             cell.setCellValue(parcelData.getAdministersAsString());
             if( parcelData.getAdministerZipCodeAsString() != null ){
                 XSSFCell zipCode = row.createCell(9);
                 zipCode.setCellStyle(cellStyle);
                 zipCode.setCellValue(parcelData.getAdministerZipCodeAsString());
             }
            if( parcelData.getAdministerTownAsString() != null ){
               XSSFCell town = row.createCell(10);
               town.setCellStyle(cellStyle);
               town.setCellValue(parcelData.getAdministerTownAsString());
           }
           if( parcelData.getAdministerLocationNameAsString() != null ){
                XSSFCell locationName = row.createCell(11);
                locationName.setCellStyle(cellStyle);
                locationName.setCellValue(parcelData.getAdministerLocationNameAsString());
            }
           if( parcelData.getAdministerLocationTypeAsString() != null ){
               XSSFCell locationType = row.createCell(12);
               locationType.setCellStyle(cellStyle);
               locationType.setCellValue(parcelData.getAdministerLocationTypeAsString());
           }
           if( parcelData.getAdministerLocationNumberAsString() != null ){
               XSSFCell locationNumber = row.createCell(13);
               locationNumber.setCellStyle(cellStyle);
               locationNumber.setCellValue(parcelData.getAdministerLocationNumberAsString());
           }
             rowIndex++;
        }
        workbook.write(out);
        out.close();
    }
}
