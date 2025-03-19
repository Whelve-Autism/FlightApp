package UserInterface;

import Flight.FlightManager;
import Passenger.PassengerManager;
import Weather.WeatherSearch;
import Travel.Travel;
import Mail.Mail;
import java.util.Scanner;

/*
 * 此类用于显示主菜单，根据选项来调用相应的功能。
 * This class is used to display the main menu and call the corresponding functions according to the options.
 */
public class Menu {
    private final PassengerManager passengerManager;
    private final FlightManager flightManager;
    private final Scanner scanner;
    private final WeatherSearch weatherSearch;
    private final Travel travel;

    /*
     * 构造函数，传入 PassengerManager 和 FlightManager 实例。
     * Constructor, passing PassengerManager and FlightManager instances.
     */
    public Menu(PassengerManager passengerManager, FlightManager flightManager) {
        this.flightManager = flightManager;
        this.passengerManager = passengerManager;

        /*
         * 初始化。
         * Initialization.
         */
        this.scanner = new Scanner(System.in);
        this.weatherSearch = new WeatherSearch(scanner);
        this.travel = new Travel();
    }

    /*
     * 显示主菜单。
     * UserInterface.Display the main menu.
     */
    public int displayMenu() {
        Display.printlnRandomColor("""
                * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                *                    FlightApp Menu                       *
                * 1) Create a flight                                      *
                * 2) Modify a flight                                      *
                * 3) Delete a flight                                      *
                * 4) Add a passenger and select a flight                  *
                * 5) Change the flight                                    *
                * 6) Cancel the ticket purchase                           *
                * 7) Check the weather information                        *
                * 8) Recommended tourist attractions                      *
                * 9) Send flight and passenger information to the mailbox *
                * 0) Exit                                                 *
                * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            ==>>""");
        int option = scanner.nextInt();

        /*
         * 虚拟读取，以清除扫描仪类中的缓冲区-错误。
         * Dummy read to clear the buffer-bug in Scanner class.
         */
        scanner.nextLine();
        return option;
    }

    /*
     * 根据输入的选项跳转到相应的方法。
     * Jump to the corresponding method according to the input option.
     */
    public void handleUserChoice() {

        /*
         * 显示开始界面。
         * UserInterface.Display the start interface.
         */
        Display.beginDisplay();
        int option = displayMenu();
        while (option != 0) {

            /*
             * try-catch 环绕包装。
             * Try-catch wrap-around packaging.
             */
            try {
                switch (option) {
                    case 1:
                        flightManager.inputFlightInfo();
                        break;
                    case 2:
                        flightManager.updateFlightInfo();
                        break;
                    case 3:
                        flightManager.deleteFlight();
                        break;
                    case 4:
                        passengerManager.addPassengerAndSelectFlight();
                        break;
                    case 5:
                        passengerManager.rebookPassenger();
                        break;
                    case 6:
                        passengerManager.deletePassenger();
                        break;
                    case 7:
                        weatherSearch.fetchWeatherForCities();
                        break;
                    case 8:
                        travel.travelAdvice();
                        break;
                    case 9:
                        Display.printlnRandomColor("The email is being sent. Please wait for about 3 seconds.");
                        Mail.sendEmail(flightManager.getFlights(), passengerManager.getPassengers());
                        Display.printlnRandomColor("\nFlight and passenger information have been sent to the mailbox.");
                        break;
                    default:
                        Display.printlnRandomColor("Please enter the numbers in 0-9.");
                }
            } catch (Exception e) {
                Display.printlnRandomColor("Error: " + e.getMessage());
            }
            Display.printlnRandomColor("\nPress enter key to continue... ");

            /*
             * 虚拟读取，以清除扫描仪类中的缓冲区-错误。
             * Dummy read to clear the buffer-bug in Scanner class.
             */
            scanner.nextLine();

            /*
             * 重新显示菜单并获取选择。
             * Re-display menu and get choice.
             */
            option = displayMenu();
        }

        /* 选择选项0后，显示结束界面。
         * UserInterface.Display the end interface after selecting option 0.
         */
        Display.endDisplay();
        Display.printlnRandomColor("The program has been exited... Bye... ");
    }
}
/*
 * End of UserInterface.Menu Class.
 * Checked by Fan Xinkang.
 */