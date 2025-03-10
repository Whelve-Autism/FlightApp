import Flight.FlightManager;
import Passenger.PassengerManager;

import java.util.Scanner;

// 菜单类
public class Menu {
    private final PassengerManager passengerManager;
    private final FlightManager flightManager;
    private final Scanner scanner;

    public Menu(PassengerManager passengerManager, FlightManager flightManager) {
        this.flightManager = new FlightManager();
        this.passengerManager = new PassengerManager(this.flightManager); // 传递相同的 FlightManager 实例
        this.scanner = new Scanner(System.in);
    }

    // 显示主菜单
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
                * 7)                                                      *
                * 8)                                                      *
                * 9) Send flight and passenger information to the mailbox *
                * 0) Exit                                                 *
                * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            ==>>""");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline after reading int
        return option;
    }

    // 处理用户输入
    public void handleUserChoice() {
        int option = displayMenu();

        while (option != 0) {
            try {
                switch (option) {
                    case 1:
                        flightManager.inputFlightInfo();
                        break;
                    case 2:
                        flightManager.updateFlightInfo();
                        break;
                    case 3:
                        // 删除航班记录
                        break;
                    case 4:
                        passengerManager.addPassengerAndSelectFlight();
                        break;
                    case 5:
                        // 乘客改签
                        break;
                    case 6:
                        // 乘客退票
                        break;
                    case 7:
                        // 其他功能
                        break;
                    case 8:
                        // 其他功能
                        break;
                    case 9:
                        // 发送信息到邮箱
                        break;
                    default:
                        System.out.println("请输入0-9中的数字。");
                }
            } catch (Exception e) {
                System.out.println("发生错误: " + e.getMessage());
            }

            System.out.println("\nPress enter key to continue... ");
            scanner.nextLine(); // Consume newline

            option = displayMenu(); // 重新显示菜单并获取用户选择
        }
    }
}