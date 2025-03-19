import Flight.FlightManager;
import Passenger.PassengerManager;
import UserInterface.Menu;

/*
 * 此类用于启动应用程序，创建 PassengerManager, FlightManager 和 UserInterface.Menu 实例。
 * This class is used to launch applications and create PassengerManager, FlightManager and UserInterface.Menu instances.
 */
public class RunApp {
    public static void main(String[] args) {
        FlightManager flightManager = new FlightManager();
        PassengerManager passengerManager = new PassengerManager(flightManager);
        Menu menu = new Menu(passengerManager, flightManager);
        menu.handleUserChoice();
    }
}
/*
 * End of RunApp Class.
 * Written and checked by Fan Xinkang.
 */