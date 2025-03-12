package Passenger;

public class PassengerInformation {
    private String name;
    private String gender;
    private int weightOfLuggage;
    private String telephoneNumber;
    private String flightNumber;
    private String departure;
    private String destination;
    private String departureTime;

    public PassengerInformation(String name, String gender, int weightOfLuggage, String telephoneNumber) {
        this.name = name;
        this.gender = gender;
        this.weightOfLuggage = weightOfLuggage;
        this.telephoneNumber = telephoneNumber;
        this.flightNumber = null; // 初始化为空，表示尚未购票
        this.departure = null;
        this.destination = null;
        this.departureTime = null;
    }

    // Getter 和 Setter 方法

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

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "PassengerInformation{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", weightOfLuggage=" + weightOfLuggage +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime='" + departureTime + '\'' +
                '}';
    }
}