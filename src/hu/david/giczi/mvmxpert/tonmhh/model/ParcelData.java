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
    private final List<Address> administerAdressList;


    public ParcelData() {
        this.administerList = new ArrayList<>();
        this.administerAdressList = new ArrayList<>();
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

    public List<Address> getAdministerAdressList() {
        return administerAdressList;
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

    public String getAdministersAsString(){
        StringBuilder sb = new StringBuilder();
        for (String administer : administerList) {
            sb.append(administer).append("\r\n");
        }
        return administerList.isEmpty() ? null :  sb.substring(0, sb.length() - 2);
    }

    public String getAdministerZipCodeAsString(){
        StringBuilder sb = new StringBuilder();
        for (Address address : administerAdressList) {
            if(address.getCompleteAddress() != null ){
                sb.append(address.getCompleteAddress()).append("\r\n");
                continue;
            }
            if( address.getZipCode() != null ){
                sb.append(address.getZipCode()).append("\r\n");
            }
            else {
                sb.append("-").append("\r\n");
            }
        }
        return sb.toString().isEmpty() ? null :  sb.substring(0, sb.length() - 2);
    }

    public String getAdministerTownAsString(){
        StringBuilder sb = new StringBuilder();
        for (Address address : administerAdressList) {
            if(address.getCompleteAddress() != null ){
                sb.append(address.getCompleteAddress()).append("\r\n");
                continue;
            }
            if( address.getTown() != null ){
                sb.append(address.getTown()).append("\r\n");
            }
            else {
                sb.append("-").append("\r\n");
            }
        }
        return sb.toString().isEmpty() ? null :  sb.substring(0, sb.length() - 2);
    }

    public String getAdministerLocationNameAsString(){
        StringBuilder sb = new StringBuilder();
        for (Address address : administerAdressList) {
            if(address.getCompleteAddress() != null ){
                sb.append(address.getCompleteAddress()).append("\r\n");
                continue;
            }
            if( address.getLocationName() != null ){
                sb.append(address.getLocationName()).append("\r\n");
            }
            else {
                sb.append("-").append("\r\n");
            }
        }
        return sb.toString().isEmpty() ? null :  sb.substring(0, sb.length() - 2);
    }

    public String getAdministerLocationTypeAsString(){
        StringBuilder sb = new StringBuilder();
        for (Address address : administerAdressList) {
            if(address.getCompleteAddress() != null ){
                sb.append(address.getCompleteAddress()).append("\r\n");
                continue;
            }
            if( address.getLocationType() != null ){
                sb.append(address.getLocationType()).append("\r\n");
            }
            else {
                sb.append("-").append("\r\n");
            }
        }
        return sb.toString().isEmpty() ? null :  sb.substring(0, sb.length() - 2);
    }

    public String getAdministerLocationNumberAsString(){
        StringBuilder sb = new StringBuilder();
        for (Address address : administerAdressList) {
            if(address.getCompleteAddress() != null ){
                sb.append(address.getCompleteAddress()).append("\r\n");
                continue;
            }
            if( address.getLocationNumber() != null ){
                sb.append(address.getLocationNumber()).append("\r\n");
            }
            else {
                sb.append("-").append("\r\n");
            }
        }
        return sb.toString().isEmpty() ? null :  sb.substring(0, sb.length() - 2);
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
    public void addMinister(String administer, String address) {
        if ( isUpperCaseAdminister(administer) ) {
            administerList.add(administer.trim());
            addMinisterAddress(address);
        }
    }

    private void addMinisterAddress(String address){
        Address ownerAddress = new Address();
        String[] addressData = address.split("\\s+");
        if( addressData.length < 4 || addressData.length > 6){
            ownerAddress.setCompleteAddress(address);
            administerAdressList.add(ownerAddress);
            return;
        }
        ownerAddress.setZipCode(addressData[0]);
        ownerAddress.setTown(addressData[1]);
       switch (addressData.length){
           case 4:
            ownerAddress.setLocationName(addressData[2]);
            break;
           case 5:
            ownerAddress.setLocationName(addressData[2]);
            ownerAddress.setLocationType(addressData[3]);
            ownerAddress.setLocationNumber(addressData[4]);
            break;
           case 6:
            ownerAddress.setLocationName(addressData[2] + " " + addressData[3]);
            ownerAddress.setLocationType(addressData[4]);
            ownerAddress.setLocationNumber(addressData[5]);
        }

        administerAdressList.add(ownerAddress);

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




