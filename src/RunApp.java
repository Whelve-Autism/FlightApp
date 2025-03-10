import Flight.FlightManager;
import Passenger.PassengerManager;

public class RunApp {
    public static void main(String[] args) {
        FlightManager flightManager = new FlightManager();
        PassengerManager passengerManager = new PassengerManager(flightManager); // 传递相同的 FlightManager 实例
        Menu menu = new Menu(passengerManager, flightManager);
        menu.handleUserChoice();
    }
}
