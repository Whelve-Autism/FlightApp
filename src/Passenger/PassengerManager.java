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
        System.out.println("Please enter the passenger's name: ");
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
            System.out.println("Please enter the passenger's gender (Male / Female): ");
            String gender = scanner.nextLine();
            if (gender.equals("Male") || gender.equals("Female")) {
                return gender;
            } else {
                System.out.println("The gender format is incorrect, please re-enter: ");
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
                System.out.println("Please enter the passenger's baggage weight (kg): ");
                int weightOfLuggage = scanner.nextInt();

                /*
                 * 虚拟读取，以清除扫描仪类中的缓冲区-错误。
                 * Dummy read to clear the buffer-bug in Scanner class.
                 */
                scanner.nextLine();
                if (weightOfLuggage >= 0 && weightOfLuggage <= 100) {
                    return weightOfLuggage;
                } else {
                    System.out.println("The weight of the luggage is out of range, please re-enter: ");
                }
            } catch (Exception e) {
                System.out.println("The input is invalid. Please enter a valid number: ");

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
            System.out.println("Please enter the passenger's contact information (11-digit phone number): ");
            String telephoneNumber = scanner.nextLine();
            if (telephoneNumber.matches("\\d{11}")) {
                return telephoneNumber;
            } else {
                System.out.println("The format of the mobile phone number is incorrect. Please re-enter: ");
            }
        }
    }

    /*
     * 添加乘客并选择航班。
     * Add passenger and select flight.
     */
    public void addPassengerAndSelectFlight() {
        System.out.println("Note: Once there are passengers on the flight, the flight cannot be modified.");

        /*
         * 选择出发地和目的地。
         * Select departure and destination.
         */
        String departure = selectOption(FlightInformation.getAllowedAirports(), "Please select the departure: ");
        String destination = selectOption(FlightInformation.getAllowedAirports(), "Please select the destination: ");

        /*
         * 确保出发地和目的地不能相同。
         * Ensure that the departure and destination are not the same.
         */
        while (departure.equals(destination)) {
            System.out.println("The place of departure and destination cannot be the same. Please select again: ");
            departure = selectOption(FlightInformation.getAllowedAirports(), "Please select the departure: ");
            destination = selectOption(FlightInformation.getAllowedAirports(), "Please select the destination: ");
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
            System.out.println("No eligible flights have been found. See you next time. ");
        } else {
            FlightInformation selectedFlight = flightManager.selectFlight(matchingFlights);
            if (selectedFlight != null) {
                System.out.println("You have chosen the flight: " + selectedFlight.getFlightNumber());
                if (selectedFlight.getAvailableSeats() == 0) {
                    System.out.println("There are no seats available for this flight, so tickets cannot be purchased.");
                } else {
                    System.out.print("Do you want to buy a ticket for this flight? (Yes / No): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("Yes")) {

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
                            System.out.println("If your luggage exceeds 20kg, you need to check in your luggage. The charge is 100 yuan, and 15 yuan will be added for each excess of 1kg.");
                            additionalLuggageFee = 100.0 + (luggageWeight - 20) * 15.0;
                        }
                        double totalCost = basePrice + additionalLuggageFee;

                        /*
                         * 输出购票成功的消息，并显示乘客的信息和航班号。
                         * Output booking success message and show passenger information and flight number.
                         */
                        System.out.println("You have purchased the flight.：" + selectedFlight.getFlightNumber());
                        System.out.println("The passenger information is as follows: ");
                        System.out.println("Name: " + passenger.getName());
                        System.out.println("Gender: " + passenger.getGender());
                        System.out.println("Weight of luggage: " + luggageWeight + "kg");
                        System.out.println("Telephone number: " + passenger.getTelephoneNumber());
                        System.out.println("Flight number: " + passenger.getFlightNumber());
                        System.out.println("Flight price: " + basePrice + "yuan");
                        if (additionalLuggageFee > 0) {
                            System.out.println("Extra baggage charges: " + additionalLuggageFee + "yuan");
                        }
                        System.out.println("Total cost: " + totalCost + "yuan");
                    } else {
                        System.out.println("The ticket purchase has been canceled.");
                    }
                }
            } else {
                System.out.println("No flight was selected.");
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

                /*
                 * 获取用户输入，并转换为整数。
                 * Get user input and convert to integer.
                 */
                choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < options.length) {
                    return options[choice];
                } else {
                    System.out.println("The selection is invalid. Please re-enter: ");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid option number: ");
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
            System.out.println((i + 1) + ". Passengers: " + passenger.getName());
            System.out.println("   Gender: " + passenger.getGender());
            System.out.println("   Weight of luggage: " + passenger.getWeightOfLuggage() + "kg");
            System.out.println("   Telephone number: " + passenger.getTelephoneNumber());
            System.out.println("   Flight number: " + passenger.getFlightNumber());
            System.out.println();
        }
    }

    /*
     * 改签乘客航班
     * Rebook passenger flight.
     */
    public void rebookPassenger() {
        if (passengers.isEmpty()) {
            System.out.println("There is no passenger information, so the visa cannot be changed.");
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
        System.out.print("Please enter the passenger number to be changed (enter numbers) or press Enter to skip: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Passengers have not been selected, and the visa change operation has been canceled.");
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
                System.out.println("The selection is invalid. Please re-enter: ");
                return;
            }
        } catch (Exception e) {
            System.out.println("The input is invalid. Please enter a valid option number: ");
            return;
        }

        /*
         * 获取选择的乘客。
         * Get the selected passenger.
         */
        PassengerInformation passenger = passengers.get(passengerIndex);
        System.out.println("You have chosen the passenger: " + passenger.getName() + ".");

        /*
         * 获取当前航班。
         * Get the current flight.
         */
        FlightInformation currentFlight = flightManager.findFlightByNumber(passenger.getFlightNumber());
        if (currentFlight == null) {
            System.out.println("The current flight information does not exist, and the visa change operation has been canceled.");
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
        System.out.print("Please enter the new flight number (enter numbers) or press Enter to skip: ");
        input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("No new flight was selected, and the visa change operation has been canceled.");
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
                System.out.println("The selection is invalid. Please re-enter: ");
                return;
            }
        } catch (Exception e) {
            System.out.println("The input is invalid. Please enter a valid option number: ");
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
            System.out.println("The new flight is the same as the current flight, and the visa change operation has been canceled.");
            return;
        }
        if (newFlight.getAvailableSeats() == 0) {
            System.out.println("There are no seats available for this flight and cannot be changed.");
            return;
        }

        /*
         * 更新当前航班的可用座位数。
         * Update the available seats of the current flight.
         */
        currentFlight.addAvailableSeats();
        if (!flightManager.hasPassenger(currentFlight.getFlightNumber())) {

            /*
             * 如果当前航班没有乘客，则更新 flightHasPassenger。
             * If the current flight has no passengers, update flightHasPassenger.
             */
            flightManager.setFlightHasPassenger(currentFlight.getFlightNumber(), false);
        }

        /*
         * 更新新航班的可用座位数。
         * Update the available seats of the new flight.
         */
        newFlight.reduceAvailableSeats();

        /*
         * 如果当前航班有乘客，则更新 flightHasPassenger。
         * If the current flight has passengers, update flightHasPassenger.
         */
        flightManager.setFlightHasPassenger(newFlight.getFlightNumber(), true);

        /*
         * 更新乘客的航班号。
         * Update the flight number of the passenger.
         */
        passenger.setFlightNumber(newFlight.getFlightNumber());
        System.out.println("The visa change is successful, and the new flight number is: " + newFlight.getFlightNumber() + ".");
    }

    /*
     * 删除乘客的购票信息。
     * Delete passenger ticket information.
     */
    public void deletePassenger() {
        if (passengers.isEmpty()) {
            System.out.println("There is no passenger information and cannot be deleted.");
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
        System.out.print("Please enter the passenger number you want to delete (enter the number) or press Enter to skip: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Passengers were not selected, and the deletion operation was canceled.");
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
                System.out.println("The selection is invalid. Please re-enter: ");
                return;
            }
        } catch (Exception e) {
            System.out.println("The input is invalid. Please enter a valid option number: ");
            return;
        }

        /*
         * 获取选择的乘客。
         * Get the selected passenger.
         */
        PassengerInformation passenger = passengers.get(passengerIndex);
        System.out.println("You have chosen the passenger: " + passenger.getName() + ".");

        /*
         * 获取当前航班。
         * Get the current flight.
         */
        FlightInformation currentFlight = flightManager.findFlightByNumber(passenger.getFlightNumber());
        if (currentFlight == null) {
            System.out.println("The current flight information does not exist, and the deletion operation has been canceled.");
            return;
        }

        /*
         * 确认删除。
         * Confirm deletion.
         */
        System.out.print("Do you want to confirm the deletion of passenger " + passenger.getName() + "? (Yes / No): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("Yes")) {
            passengers.remove(passenger);

            /*
             * 增加当前航班的可用座位数。
             * Increase the available seats of the current flight.
             */
            currentFlight.addAvailableSeats();
            if (!flightManager.hasPassenger(currentFlight.getFlightNumber())) {

                /*
                 * 设置没有乘客购票。
                 * Set flightHasPassenger to false.
                 */
                flightManager.setFlightHasPassenger(currentFlight.getFlightNumber(), false);
            }
            System.out.println("Passenger " + passenger.getName() + "'s ticket purchase information has been deleted.");
        } else {
            System.out.println("The deletion operation has been canceled.");
        }
    }
}
/*
 * End of PassengerManager Class.
 * Checked by Fan Xinkang.
 */