package Passenger;

/*
 * 此类用于储存和访问乘客信息，包括姓名、性别、行李重量、联系电话、航班号、出发地、目的地和出发时间。
 * This class is used to store and access passenger information, including name, gender, luggage weight, telephone number, flight number, departure, destination, and departure time.
 */
public class PassengerInformation {
    private String name;
    private String gender;
    private int weightOfLuggage;
    private String telephoneNumber;
    private String flightNumber;
    private String departure;
    private String destination;
    private String departureTime;

    /*
     * 封装信息。
     * getter and setter.
     */
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

    /*
     * 初始化乘客信息。
     * Initialize passenger information.
     */
    public PassengerInformation(String name, String gender, int weightOfLuggage, String telephoneNumber) {
        this.name = name;
        this.gender = gender;
        this.weightOfLuggage = weightOfLuggage;
        this.telephoneNumber = telephoneNumber;

        /*
         * 初始化，航班号、出发地、目的地和出发时间都为空。
         * Initialize, flight number, departure, destination, and departure time are all empty.
         */
        this.flightNumber = null;
        this.departure = null;
        this.destination = null;
        this.departureTime = null;
    }

    /*
     * 重写toString方法，用于打印乘客信息。
     * Override the toString method to print passenger information.
     */
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
/*
 * End of PassengerInformation Class.
 * Checked by Fan Xinkang.
 */