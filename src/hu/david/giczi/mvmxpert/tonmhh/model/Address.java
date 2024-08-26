package hu.david.giczi.mvmxpert.tonmhh.model;

public class Address {

    private String town;
    private String zipCode;
    private String locationName;
    private String locationType;
    private String locationNumber;
    private String completeAddress;
    private final static String[] LOCATION_TYPES = {"út", "utca", "tér", "körút", "tanya"};

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode.trim();
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName.trim();
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType.trim();
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(String locationNumber) {
        this.locationNumber = locationNumber.trim();
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress.trim();
    }

    @Override
    public String toString() {
        return "Address{" +
                "town='" + town + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", locationName='" + locationName + '\'' +
                ", locationType='" + locationType + '\'' +
                ", locationNumber='" + locationNumber + '\'' +
                '}';
    }
}
