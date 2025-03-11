package Passenger;

public class PassengerInformation {
    private String name;
    private String gender;
    private int weightOfLuggage;
    private String telephoneNumber;

    // getter和setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getWeightOfLuggage() {
        return weightOfLuggage;
    }

    public void setWeightOfLuggage(int weightOfLuggage) {
        this.weightOfLuggage = weightOfLuggage;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public PassengerInformation(String name, String gender, int weightOfLuggage, String telephoneNumber) {
        this.name = name;
        this.gender = gender;
        this.weightOfLuggage = weightOfLuggage;
        this.telephoneNumber = telephoneNumber;
    }
}
