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
     * Display the main menu.
     */
    public int displayMenu() {
        System.out.println("""
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
                        System.out.print("请稍后");

                        /*
                         * 预计等待9秒。
                         * Estimated waiting for 9 seconds.
                         */
                        for (int i = 0; i < 30; i++) {

                            /*
                             * 每等待0.3秒输出一个点
                             * Output a dot every 0.3 seconds.
                             */
                            Thread.sleep(300);
                            System.out.print(".");
                        }
                        Mail.sendEmail(flightManager.getFlights(), passengerManager.getPassengers());
                        System.out.println("\n航班和乘客信息已发送到邮箱。");
                        break;
                    default:
                        System.out.println("请输入0-9中的数字。");
                }
            } catch (Exception e) {
                System.out.println("发生错误: " + e.getMessage());
            }
            System.out.println("\nPress enter key to continue... ");

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
    }
}
/*
 * End of Menu Class.
 * Checked by Fan Xinkang.
 */