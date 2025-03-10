package Passenger;

import Flight.FlightManager;
import Flight.FlightInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PassengerManager {
    private List<PassengerInformation> passengers;
    private Scanner scanner;
    private FlightManager flightManager;

    public PassengerManager(FlightManager flightManager) {
        this.passengers = new ArrayList<>();
        this.scanner = new Scanner(System.in); // 确保 scanner 正确初始化
        this.flightManager = flightManager;
    }

    // 录入乘客信息
    public PassengerInformation inputPassengerInfo() {
        System.out.println("请输入乘客姓名:");
        String name = scanner.nextLine();

        String gender = getValidGender(scanner);

        int weightOfLuggage = getValidWeight(scanner);

        String telephoneNumber = getValidPhoneNumber(scanner);

        PassengerInformation passenger = new PassengerInformation(name, gender, weightOfLuggage, telephoneNumber);
        return passenger;
    }

    private String getValidGender(Scanner scanner) {
        while (true) {
            System.out.println("请输入乘客性别（男/女）:");
            String gender = scanner.nextLine();
            if (gender.equals("男") || gender.equals("女")) {
                return gender;
            } else {
                System.out.println("性别格式不正确，请重新输入：");
            }
        }
    }

    private int getValidWeight(Scanner scanner) {
        while (true) {
            try {
                System.out.println("请输入乘客行李重量（kg）:");
                int weightOfLuggage = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (weightOfLuggage >= 0 && weightOfLuggage <= 100) {
                    return weightOfLuggage;
                } else {
                    System.out.println("行李重量超出范围，请重新输入：");
                }
            } catch (Exception e) {
                System.out.println("输入无效，请输入一个有效的数字：");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private String getValidPhoneNumber(Scanner scanner) {
        while (true) {
            System.out.println("请输入乘客联系方式（11位电话号码）:");
            String telephoneNumber = scanner.nextLine();
            if (telephoneNumber.matches("\\d{11}")) {
                return telephoneNumber;
            } else {
                System.out.println("手机号码格式不正确，请重新输入：");
            }
        }
    }

    // 添加乘客并选择航班
    public void addPassengerAndSelectFlight() {
        System.out.println("注意：一旦录入乘客，航班则不能修改。");

        // 选择出发地和目的地
        String departure = selectOption(FlightInformation.getAllowedAirports(), "请选择出发地：");
        String destination = selectOption(FlightInformation.getAllowedAirports(), "请选择目的地：");

        // 确保出发地和目的地不能相同
        while (departure.equals(destination)) {
            System.out.println("起飞地和目的地不能相同，请重新选择。");
            departure = selectOption(FlightInformation.getAllowedAirports(), "请选择出发地：");
            destination = selectOption(FlightInformation.getAllowedAirports(), "请选择目的地：");
        }

        // 查询符合条件的航班
        List<FlightInformation> matchingFlights = flightManager.searchFlights(departure, destination);
        flightManager.printFlights(matchingFlights);

        if (matchingFlights.isEmpty()) {
            System.out.println("没有找到符合条件的航班，请下次再见。");
        } else {
            FlightInformation selectedFlight = flightManager.selectFlight(matchingFlights);
            if (selectedFlight != null) {
                System.out.println("您选择了航班：" + selectedFlight.getFlightNumber());
                if (selectedFlight.getAvailableSeats() == 0) {
                    System.out.println("该航班没有可用座位，无法购票。");
                } else {
                    System.out.print("是否要购买该航班的票？(是/否): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("是")) {
                        // 录入乘客信息
                        PassengerInformation passenger = inputPassengerInfo();
                        passengers.add(passenger); // 只有在购票成功后才添加乘客信息
                        flightManager.setFlightHasPassenger(selectedFlight.getFlightNumber(), true); // 设置航班有乘客购票

                        // 减少可用座位数
                        selectedFlight.reduceAvailableSeats();

                        // 计算票价
                        double basePrice = selectedFlight.calculatePrice(departure, destination);
                        int luggageWeight = passenger.getWeightOfLuggage();
                        double additionalLuggageFee = 0.0;

                        if (luggageWeight > 20) {
                            System.out.println("您的行李超过20kg，需要托运行李，收费100元，每超1kg加15元。");
                            additionalLuggageFee = 100.0 + (luggageWeight - 20) * 15.0;
                        }

                        double totalCost = basePrice + additionalLuggageFee;

                        // 输出购票成功的消息，并显示乘客的信息和航班号
                        System.out.println("您已购买航班：" + selectedFlight.getFlightNumber());
                        System.out.println("乘客信息如下：");
                        System.out.println("姓名: " + passenger.getName());
                        System.out.println("性别: " + passenger.getGender());
                        System.out.println("行李重量: " + luggageWeight + "kg");
                        System.out.println("联系方式: " + passenger.getTelephoneNumber());
                        System.out.println("航班号: " + selectedFlight.getFlightNumber());
                        System.out.println("票价: " + basePrice + "元");
                        if (additionalLuggageFee > 0) {
                            System.out.println("额外行李费用: " + additionalLuggageFee + "元");
                        }
                        System.out.println("总费用: " + totalCost + "元");

                        // 这里可以继续处理购票后的逻辑，例如保存购票信息等
                    } else {
                        System.out.println("购票已取消。");
                    }
                }
            } else {
                System.out.println("没有选择任何航班。");
            }
        }
    }

    private String selectOption(String[] options, String prompt) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print(prompt);
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < options.length) {
                    return options[choice];
                } else {
                    System.out.println("选择无效，请重新输入。");
                }
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的选项编号。");
            }
        }
    }

    // 获取所有乘客信息
    public List<PassengerInformation> getPassengers() {
        return passengers;
    }

    // 打印所有乘客信息
    public void printPassengers() {
        for (PassengerInformation passenger : passengers) {
            System.out.println(passenger);
        }
    }
}