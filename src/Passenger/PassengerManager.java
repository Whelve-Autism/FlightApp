package Passenger;

import Flight.FlightManager;
import Flight.FlightInformation;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * 此类用于管理乘客信息。
 * This class is used to manage passenger information.
 */
public class PassengerManager {
    private List<PassengerInformation> passengers;
    private Scanner scanner;
    private FlightManager flightManager;

    /*
     * 构造函数，初始化乘客信息和Scanner对象。
     * Constructor initializes the passenger information and Scanner object.
     */
    public PassengerManager(FlightManager flightManager) {
        this.passengers = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.flightManager = flightManager;
    }

    /*
     * 录入乘客信息。
     * Enter passenger information.
     */
    public PassengerInformation inputPassengerInfo() {
        System.out.println("请输入乘客姓名:");
        String name = scanner.nextLine();
        String gender = getValidGender(scanner);
        int weightOfLuggage = getValidWeight(scanner);
        String telephoneNumber = getValidPhoneNumber(scanner);
        PassengerInformation passenger = new PassengerInformation(name, gender, weightOfLuggage, telephoneNumber);
        return passenger;
    }

    /*
     * 选择乘客性别。
     * Select passenger gender.
     */
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

    /*
     * 选择乘客行李重量。
     * Select passenger luggage weight.
     */
    private int getValidWeight(Scanner scanner) {
        while (true) {
            try {
                System.out.println("请输入乘客行李重量（kg）:");
                int weightOfLuggage = scanner.nextInt();

                /*
                 * 虚拟读取，以清除扫描仪类中的缓冲区-错误。
                 * Dummy read to clear the buffer-bug in Scanner class.
                 */
                scanner.nextLine();
                if (weightOfLuggage >= 0 && weightOfLuggage <= 100) {
                    return weightOfLuggage;
                } else {
                    System.out.println("行李重量超出范围，请重新输入：");
                }
            } catch (Exception e) {
                System.out.println("输入无效，请输入一个有效的数字：");

                /*
                 * 虚拟读取，以清除扫描仪类中的缓冲区-错误。
                 * Dummy read to clear the buffer-bug in Scanner class.
                 */
                scanner.nextLine();
            }
        }
    }

    /*
     * 选择乘客联系方式。
     * Select passenger contact information.
     */
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

    /*
     * 添加乘客并选择航班。
     * Add passenger and select flight.
     */
    public void addPassengerAndSelectFlight() {
        System.out.println("注意：一旦航班上有乘客，航班则不能修改。");

        /*
         * 选择出发地和目的地。
         * Select departure and destination.
         */
        String departure = selectOption(FlightInformation.getAllowedAirports(), "请选择出发地：");
        String destination = selectOption(FlightInformation.getAllowedAirports(), "请选择目的地：");

        /*
         * 确保出发地和目的地不能相同。
         * Ensure that the departure and destination are not the same.
         */
        while (departure.equals(destination)) {
            System.out.println("起飞地和目的地不能相同，请重新选择。");
            departure = selectOption(FlightInformation.getAllowedAirports(), "请选择出发地：");
            destination = selectOption(FlightInformation.getAllowedAirports(), "请选择目的地：");
        }

        /*
         * 查询符合条件的航班。
         * Search for flights that meet the criteria.
         */
        List<FlightInformation> matchingFlights = flightManager.searchFlights(departure, destination);
        flightManager.printFlights(matchingFlights);

        /*
         * 如果没有符合条件的航班，则提示并结束程序。
         * If there are no matching flights, prompt and end the program.
         */
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

                        /*
                         * 录入乘客信息。
                         * Enter passenger information.
                         */
                        PassengerInformation passenger = inputPassengerInfo();

                        /*
                         * 只有在购票成功后才添加乘客信息。
                         * Only add passenger information after successful booking.
                         */
                        passengers.add(passenger);

                        /*
                         * 设置航班有乘客购票。
                         * Set flight has passenger.
                         */
                        flightManager.setFlightHasPassenger(selectedFlight.getFlightNumber(), true);

                        /*
                         * 减少可用座位数。
                         * Reduce available seats.
                         */
                        selectedFlight.reduceAvailableSeats();

                        /*
                         * 设置乘客的航班号。
                         * Set passenger's flight number.
                         */
                        passenger.setFlightNumber(selectedFlight.getFlightNumber());

                        /*
                         * 计算票价，并根据行李重量计算附加费用。
                         * Calculate ticket price and calculate additional fee based on luggage weight.
                         */
                        double basePrice = selectedFlight.calculatePrice(departure, destination);
                        int luggageWeight = passenger.getWeightOfLuggage();
                        double additionalLuggageFee = 0.0;
                        if (luggageWeight > 20) {
                            System.out.println("您的行李超过20kg，需要托运行李，收费100元，每超1kg加15元。");
                            additionalLuggageFee = 100.0 + (luggageWeight - 20) * 15.0;
                        }
                        double totalCost = basePrice + additionalLuggageFee;

                        /*
                         * 输出购票成功的消息，并显示乘客的信息和航班号。
                         * Output booking success message and show passenger information and flight number.
                         */
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
                    } else {
                        System.out.println("购票已取消。");
                    }
                }
            } else {
                System.out.println("没有选择任何航班。");
            }
        }
    }

    /*
     * 选择选项并且返回。
     * Select option and return.
     */
    private String selectOption(String[] options, String prompt) {
        for (int i = 0; i < options.length; i++) {

            /*
             * 数组下标从0开始，所以需要加1。
             * The array index starts from 0, so we need to add 1.
             */
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print(prompt);
        int choice;

        /*
         * 循环直到输入有效选项。
         * Loop until input a valid option.
         */
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

    /*
     * 获取所有乘客信息。
     * Get all passenger information.
     */
    public List<PassengerInformation> getPassengers() {
        return passengers;
    }

    /*
     * 打印所有乘客信息。
     * Print all passenger information.
     */
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

    /*
     * 改签乘客航班
     * Rebook passenger flight.
     */
    public void rebookPassenger() {
        if (passengers.isEmpty()) {
            System.out.println("没有乘客信息，无法改签。");
            return;
        }

        /*
         * 打印所有乘客供选择。
         * Print all passengers for selection.
         */
        printPassengers();

        /*
         * 选择要改签的乘客。
         * Select passenger to rebook.
         */
        System.out.print("请输入要改签的乘客编号（输入数字），按回车跳过: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("未选择乘客，改签操作已取消。");
            return;
        }

        /*
         * 获取选择的乘客编号，并检查是否有效。
         * Get the selected passenger number and check if it is valid.
         */
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

        /*
         * 获取选择的乘客。
         * Get the selected passenger.
         */
        PassengerInformation passenger = passengers.get(passengerIndex);
        System.out.println("您选择了乘客: " + passenger.getName());

        /*
         * 获取当前航班。
         * Get the current flight.
         */
        FlightInformation currentFlight = flightManager.findFlightByNumber(passenger.getFlightNumber());
        if (currentFlight == null) {
            System.out.println("当前航班信息不存在，改签操作已取消。");
            return;
        }

        /*
         * 查询所有航班供用户选择。
         * Search for all flights for selection.
         */
        List<FlightInformation> allFlights = flightManager.getFlights();
        flightManager.printFlights(allFlights);

        /*
         * 选择新的航班。
         * Select new flight.
         */
        System.out.print("请输入新的航班编号（输入数字），按回车跳过: ");
        input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("未选择新航班，改签操作已取消。");
            return;
        }

        /*
         * 获取选择的航班编号，并检查是否有效。
         * Get the selected flight number and check if it is valid.
         */
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

        /*
         * 获取选择的航班。
         * Get the selected flight.
         */
        FlightInformation newFlight = allFlights.get(flightIndex);

        /*
         * 检查新航班是否与当前航班相同。
         * Check if the new flight is the same as the current flight.
         */
        if (newFlight.getFlightNumber().equals(currentFlight.getFlightNumber())) {
            System.out.println("新航班与当前航班相同，改签操作已取消。");
            return;
        }
        if (newFlight.getAvailableSeats() == 0) {
            System.out.println("该航班没有可用座位，无法改签。");
            return;
        }

        /*
         * 更新当前航班的可用座位数。
         * Update the available seats of the current flight.
         */
        currentFlight.addAvailableSeats();
        if (!flightManager.hasPassenger(currentFlight.getFlightNumber())) {
            flightManager.setFlightHasPassenger(currentFlight.getFlightNumber(), false); // 当前航班没有乘客购票
        }

        /*
         * 更新新航班的可用座位数。
         * Update the available seats of the new flight.
         */
        newFlight.reduceAvailableSeats();
        flightManager.setFlightHasPassenger(newFlight.getFlightNumber(), true); // 新航班有乘客购票

        /*
         * 更新乘客的航班号。
         * Update the flight number of the passenger.
         */
        passenger.setFlightNumber(newFlight.getFlightNumber());

        System.out.println("改签成功，新的航班号为：" + newFlight.getFlightNumber());
    }

    /*
     * 删除乘客的购票信息。
     * Delete passenger ticket information.
     */
    public void deletePassenger() {
        if (passengers.isEmpty()) {
            System.out.println("没有乘客信息，无法删除。");
            return;
        }

        /*
         * 打印所有乘客供用户选择。
         * Print all passengers for selection.
         */
        printPassengers();

        /*
         * 选择要删除的乘客。
         * Select passenger to delete.
         */
        System.out.print("请输入要删除的乘客编号（输入数字），按回车跳过: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("未选择乘客，删除操作已取消。");
            return;
        }

        /*
         * 获取选择的乘客编号，并检查是否有效。
         * Get the selected passenger number and check if it is valid.
         */
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

        /*
         * 获取选择的乘客。
         * Get the selected passenger.
         */
        PassengerInformation passenger = passengers.get(passengerIndex);
        System.out.println("您选择了乘客: " + passenger.getName());

        /*
         * 获取当前航班。
         * Get the current flight.
         */
        FlightInformation currentFlight = flightManager.findFlightByNumber(passenger.getFlightNumber());
        if (currentFlight == null) {
            System.out.println("当前航班信息不存在，删除操作已取消。");
            return;
        }

        /*
         * 确认删除。
         * Confirm deletion.
         */
        System.out.print("是否确认删除乘客 " + passenger.getName() + "? (是/否): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("是")) {
            passengers.remove(passenger);

            /*
             * 增加当前航班的可用座位数。
             * Increase the available seats of the current flight.
             */
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
/*
 * End of PassengerManager Class.
 * Checked by Fan Xinkang.
 */