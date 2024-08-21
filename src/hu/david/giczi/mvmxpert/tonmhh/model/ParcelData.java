package hu.david.giczi.mvmxpert.tonmhh.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParcelData {

    private String town;
    private String location;
    private String parcelId;

    private String utilization;
    private final static String[] PARCEL_TYPE = {"belterület", "külterület", "zártkert"};

    private final List<String> administerList;


    public ParcelData() {
        this.administerList = new ArrayList<>();
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location.trim();
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public String createParcelId(Cell cell1, Cell cell2, Cell cell3, Cell cell4) {
       String parcelId = null;
       if( PARCEL_TYPE[1].equals(location) ){

           if( cell1 != null && cell2 != null && cell3 != null && cell4 != null &&
                   cell1.getCellType() == CellType.NUMERIC && cell2.getCellType() == CellType.NUMERIC &&
                   cell3.getCellType() == CellType.NUMERIC && cell4.getCellType() == CellType.NUMERIC ) {
               parcelId = "0" + (int) cell1.getNumericCellValue() + "/" + (int) cell2.getNumericCellValue() +
                       "/" + (int) cell3.getNumericCellValue() + "/" + (int) cell4.getNumericCellValue();
           }
           else if( cell1 != null && cell2 != null && cell3 != null &&
                   cell1.getCellType() == CellType.NUMERIC && cell2.getCellType() == CellType.NUMERIC &&
                   cell3.getCellType() == CellType.NUMERIC ) {
               parcelId = "0" + (int) cell1.getNumericCellValue() + "/" + (int) cell2.getNumericCellValue() +
                       "/" + (int) cell3.getNumericCellValue();
           }
           else if( cell1 != null && cell2 != null &&
                   cell1.getCellType() == CellType.NUMERIC && cell2.getCellType() == CellType.NUMERIC ){
               parcelId = "0" + (int) cell1.getNumericCellValue() + "/" + (int) cell2.getNumericCellValue();
           }
           else if( cell1 != null  && cell1.getCellType() == CellType.NUMERIC ) {
               parcelId = "0" + (int) cell1.getNumericCellValue();
           }

       }
       else if( PARCEL_TYPE[0].equals(location) ||
               PARCEL_TYPE[2].equals(location) ) {

           if( cell1 != null && cell2 != null && cell3 != null && cell4 != null &&
                   cell1.getCellType() == CellType.NUMERIC && cell2.getCellType() == CellType.NUMERIC &&
                   cell3.getCellType() == CellType.NUMERIC && cell4.getCellType() == CellType.NUMERIC ) {
               parcelId = (int) cell1.getNumericCellValue() + "/" + (int) cell2.getNumericCellValue() +
                       "/" + (int) cell3.getNumericCellValue() + "/" + (int) cell4.getNumericCellValue();
           }
           else if( cell1 != null && cell2 != null && cell3 != null &&
                   cell1.getCellType() == CellType.NUMERIC && cell2.getCellType() == CellType.NUMERIC &&
                   cell3.getCellType() == CellType.NUMERIC ) {
               parcelId = (int) cell1.getNumericCellValue() + "/" + (int) cell2.getNumericCellValue() +
                       "/" + (int) cell3.getNumericCellValue();
           }
           else if( cell1 != null && cell2 != null && cell1.getCellType() == CellType.NUMERIC &&
                   cell2.getCellType() == CellType.NUMERIC ) {
               parcelId = (int) cell1.getNumericCellValue() + "/" + (int) cell2.getNumericCellValue();
           }
           else if( cell1 != null && cell1.getCellType() == CellType.NUMERIC ) {
               parcelId = String.valueOf((int) cell1.getNumericCellValue());
           }
       }
       return parcelId;
    }

    public String getUtilization() {
        return utilization;
    }

    public void setUtilization(String utilization) {
        this.utilization = utilization.trim();
    }

    public List<String> getAdministerList() {
        return administerList;
    }

    public String getAdministersAsString(){
        StringBuilder sb = new StringBuilder();
        for (String administer : administerList) {
            sb.append(administer).append("\n");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public boolean isUpperCaseAdminister(String administer) {
        String[] inputData = administer.trim().split("\\s+");
        int upperCase = 0;
        int sumLength = 0;
        for (String data : inputData) {
            sumLength += data.length() - 1;
            for (int i = 0; i < data.length() - 1; i++) {
                if (Character.isUpperCase(data.charAt(i))) {
                    upperCase++;
                }
            }
        }
       return upperCase == sumLength;
    }
    public void addMinister(String administer) {
        if (isUpperCaseAdminister(administer)) {
            administerList.add(administer.trim());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParcelData that = (ParcelData) o;
        return Objects.equals(parcelId, that.parcelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parcelId);
    }

    @Override
    public String toString() {
        return "ParcelData{" +
                "town='" + town + '\'' +
                ", location='" + location + '\'' +
                ", parcelId='" + parcelId + '\'' +
                ", utilization='" + utilization + '\'' +
                ", administerList=" + administerList +
                '}';
    }
}




