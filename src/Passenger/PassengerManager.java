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
        System.out.println("注意：一旦航班上有乘客，航班则不能修改。");

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

                        // 设置乘客的航班号
                        passenger.setFlightNumber(selectedFlight.getFlightNumber());

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
                        System.out.println("航班号: " + passenger.getFlightNumber());
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
        for (int i = 0; i < passengers.size(); i++) {
            PassengerInformation passenger = passengers.get(i);
            System.out.println((i + 1) + ". 乘客: " + passenger.getName());
            System.out.println("   性别: " + passenger.getGender());
            System.out.println("   行李重量: " + passenger.getWeightOfLuggage() + "kg");
            System.out.println("   联系方式: " + passenger.getTelephoneNumber());
            System.out.println("   航班号: " + passenger.getFlightNumber());
            System.out.println();
        }
    }

    // 改签乘客航班
    public void rebookPassenger() {
        if (passengers.isEmpty()) {
            System.out.println("没有乘客信息，无法改签。");
            return;
        }

        // 打印所有乘客供用户选择
        printPassengers();

        // 选择要改签的乘客
        System.out.print("请输入要改签的乘客编号（输入数字），按回车跳过: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("未选择乘客，改签操作已取消。");
            return;
        }

        int passengerIndex;
        try {
            passengerIndex = Integer.parseInt(input) - 1;
            if (passengerIndex < 0 || passengerIndex >= passengers.size()) {
                System.out.println("选择无效，请重新输入。");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("输入无效，请输入有效的选项编号。");
            return;
        }

        PassengerInformation passenger = passengers.get(passengerIndex);
        System.out.println("您选择了乘客: " + passenger.getName());

        // 获取当前航班
        FlightInformation currentFlight = flightManager.findFlightByNumber(passenger.getFlightNumber());
        if (currentFlight == null) {
            System.out.println("当前航班信息不存在，改签操作已取消。");
            return;
        }

        // 查询所有航班供用户选择
        List<FlightInformation> allFlights = flightManager.getFlights();
        flightManager.printFlights(allFlights);

        // 选择新的航班
        System.out.print("请输入新的航班编号（输入数字），按回车跳过: ");
        input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("未选择新航班，改签操作已取消。");
            return;
        }

        int flightIndex;
        try {
            flightIndex = Integer.parseInt(input) - 1;
            if (flightIndex < 0 || flightIndex >= allFlights.size()) {
                System.out.println("选择无效，请重新输入。");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("输入无效，请输入有效的选项编号。");
            return;
        }

        FlightInformation newFlight = allFlights.get(flightIndex);

        // 检查新航班是否与当前航班相同
        if (newFlight.getFlightNumber().equals(currentFlight.getFlightNumber())) {
            System.out.println("新航班与当前航班相同，改签操作已取消。");
            return;
        }

        if (newFlight.getAvailableSeats() == 0) {
            System.out.println("该航班没有可用座位，无法改签。");
            return;
        }

        // 更新当前航班的可用座位数
        currentFlight.addAvailableSeats();
        if (!flightManager.hasPassenger(currentFlight.getFlightNumber())) {
            flightManager.setFlightHasPassenger(currentFlight.getFlightNumber(), false); // 当前航班没有乘客购票
        }

        // 更新新航班的可用座位数
        newFlight.reduceAvailableSeats();
        flightManager.setFlightHasPassenger(newFlight.getFlightNumber(), true); // 新航班有乘客购票

        // 更新乘客的航班号
        passenger.setFlightNumber(newFlight.getFlightNumber());

        System.out.println("改签成功，新的航班号为：" + newFlight.getFlightNumber());
    }

    // 删除乘客的购票信息
    public void deletePassenger() {
        if (passengers.isEmpty()) {
            System.out.println("没有乘客信息，无法删除。");
            return;
        }

        // 打印所有乘客供用户选择
        printPassengers();

        // 选择要删除的乘客
        System.out.print("请输入要删除的乘客编号（输入数字），按回车跳过: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("未选择乘客，删除操作已取消。");
            return;
        }

        int passengerIndex;
        try {
            passengerIndex = Integer.parseInt(input) - 1;
            if (passengerIndex < 0 || passengerIndex >= passengers.size()) {
                System.out.println("选择无效，请重新输入。");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("输入无效，请输入有效的选项编号。");
            return;
        }

        PassengerInformation passenger = passengers.get(passengerIndex);
        System.out.println("您选择了乘客: " + passenger.getName());

        // 获取当前航班
        FlightInformation currentFlight = flightManager.findFlightByNumber(passenger.getFlightNumber());
        if (currentFlight == null) {
            System.out.println("当前航班信息不存在，删除操作已取消。");
            return;
        }

        // 确认删除
        System.out.print("是否确认删除乘客 " + passenger.getName() + "? (是/否): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("是")) {
            passengers.remove(passenger);

            // 增加当前航班的可用座位数
            currentFlight.addAvailableSeats();
            if (!flightManager.hasPassenger(currentFlight.getFlightNumber())) {
                flightManager.setFlightHasPassenger(currentFlight.getFlightNumber(), false); // 当前航班没有乘客购票
            }

            System.out.println("乘客 " + passenger.getName() + " 的购票信息已删除。");
        } else {
            System.out.println("删除已取消。");
        }
    }
}