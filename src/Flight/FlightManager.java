package Flight;

import UserInterface.Display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
 * 此类用于管理航班信息。
 * This class is used to manage flight information.
 */
public class FlightManager {
    private List<FlightInformation> flights = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private Map<String, Boolean> flightHasPassenger = new HashMap<>();

    /*
     * 录入航班信息。
     * Input flight information.
     */
    public FlightInformation inputFlightInfo() {
        Display.printlnRandomColor("Please enter the flight number (6 digits): ");
        String flightNumber;

        /*
         * 循环判断航班号, 如果航班号格式不正确或已经存在，则需要重新输入。
         * Loop to judge the flight number. If the format of the flight number is incorrect or already exists, it needs to be re-entered.
         */
        do {
            flightNumber = scanner.nextLine();
            if (!flightNumber.matches("\\d{6}")) {
                Display.printlnRandomColor("The flight number must be 6 digits. Please re-enter: ");
            } else if (isFlightNumberExists(flightNumber)) {
                Display.printlnRandomColor("Flight number " + flightNumber + " already exists. Please re-enter: ");
            }
        } while (!flightNumber.matches("\\d{6}") || isFlightNumberExists(flightNumber));

        /*
         * 选择出发地。
         * Select the departure.
         */
        String departure = selectOption(FlightInformation.getAllowedAirports(), "Please select the place of departure: ");

        /*
         * 选择目的地。
         * Select the destination.
         */
        String destination;

        /*
         * 确保起飞地和目的地不能相同。
         * Ensure that the departure and destination are not the same.
         */
        do {
            destination = selectOption(FlightInformation.getAllowedAirports(), "Please select the place of destination: ");
            if (departure.equals(destination)) {
                Display.printlnRandomColor("The place of departure and destination cannot be the same. Please select again: ");
            }
        } while (departure.equals(destination));

        /*
         * 获取起飞时间。
         * Get the departure time.
         */
        String departureTime;

        /*
         * while (true) 是一个无限循环，直到用户输入一个有效的起飞时间。
         * while (true) is an infinite loop that continues until the user enters a valid departure time.
         */
        while (true) {
            try {
                Display.printlnRandomColor("Please enter the take-off time (yyyy-MM-dd HH:mm):");
                departureTime = scanner.nextLine();
                validateDateTimeInput(departureTime);
                break;
            } catch (Exception e) {
                Display.printlnRandomColor("The time format is incorrect. Please re-enter: ");
            }
        }

        /*
         * 选择飞机型号。
         * Select the aircraft type.
         */
        String aircraftType = FlightInformation.selectAircraftType(scanner);

        /*
         * 定义初始座位数。
         * Define the number of initial seats.
         */
        int availableSeats;
        while (true) {
            try {
                Display.printlnRandomColor("Please enter the number of available seats: ");
                availableSeats = Integer.parseInt(scanner.nextLine());
                if (availableSeats <= 0) {
                    throw new IllegalArgumentException("The number of seats must be a positive integer.");
                }
                break;

            /*
             * 处理不同输入错误，抛出不同的异常。
             * Handle different input errors and throw different exceptions.
             */
            } catch (NumberFormatException e) {
                Display.printlnRandomColor("Please enter a valid number of seats.");
            } catch (IllegalArgumentException e) {
                Display.printlnRandomColor(e.getMessage() + " . Please re-enter: ");
            }
        }

        /*
         * 创建航班对象并添加到列表中。
         * Create a flight object and add it to the list.
         */
        FlightInformation flight = new FlightInformation(flightNumber, departure, destination, departureTime, aircraftType, availableSeats);
        addFlight(flight);

        /*
         * 初始化航班，确认航班没有乘客购票。
         * Initialize the flight and confirm that the flight has no passengers booked.
         */
        flightHasPassenger.put(flightNumber, false);
        Display.printlnRandomColor("Flight addition is successful.");
        return flight;
    }

    /*
     * 添加航班。
     * Add flight.
     */
    private void addFlight(FlightInformation flight) {
        flights.add(flight);
    }

    /*
     * 查询符合条件的航班。
     * Search for the flights that meet the criteria.
     */
    public List<FlightInformation> searchFlights(String departure, String destination) {
        List<FlightInformation> matchingFlights = new ArrayList<>();
        for (FlightInformation flight : flights) {
            if (flight.getDeparture().equals(departure) && flight.getDestination().equals(destination)) {
                matchingFlights.add(flight);
            }
        }
        return matchingFlights;
    }

    /*
     * 打印航班列表。
     * Print flight list.
     */
    public void printFlights(List<FlightInformation> flights) {
        if (flights.isEmpty()) {
            Display.printlnRandomColor("No eligible flights were found.");
        } else {
            Display.printlnRandomColor("Find the following eligible flights: ");
            for (int i = 0; i < flights.size(); i++) {
                FlightInformation flight = flights.get(i);

                /*
                 * 数组下标从0开始，所以需要加1。
                 * The array index starts from 0, so we need to add 1.
                 */
                Display.printlnRandomColor((i + 1) + ". Flight number: " + flight.getFlightNumber());
                Display.printlnRandomColor("   Departure: " + flight.getDeparture());
                Display.printlnRandomColor("   Destination: " + flight.getDestination());
                Display.printlnRandomColor("   Departure time: " + flight.getDepartureTime());
                Display.printlnRandomColor("   Aircraft type: " + flight.getAircraftType());
                Display.printlnRandomColor("   Available seats: " + flight.getAvailableSeats());
                Display.printlnRandomColor("   Flight time: " + flight.getFlightTime(flight.getDeparture(), flight.getDestination()) + "hours");
                Display.printlnRandomColor("   Flight price: " + flight.calculatePrice(flight.getDeparture(), flight.getDestination()));
                System.out.println();
            }
        }
    }

    /*
     * 选择航班。
     * Select flight.
     */
    public FlightInformation selectFlight(List<FlightInformation> flights) {
        if (flights.isEmpty()) {
            return null;
        }
        while (true) {
            try {
                Display.printlnRandomColor("Please select the flight number (enter the number): ");
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < flights.size()) {
                    return flights.get(choice);
                } else {
                    Display.printlnRandomColor("The selection is invalid, please re-enter: ");
                }
            } catch (Exception e) {
                Display.printlnRandomColor("Please enter a valid option number: ");
            }
        }
    }

    /*
     * 选择选项。
     * Select option.
     */
    private String selectOption(String[] options, String prompt) {
        return selectOptionWithDefault(options, prompt, null);
    }

    /*
     * throw 表示抛出异常，如果输入的日期时间格式不正确，则抛出ParseException异常。
     * If the input date time format is incorrect, throw a ParseException exception.
     */
    private void validateDateTimeInput(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setLenient(false);
        sdf.parse(dateTime);
    }

    /*
     * 修改航班信息。
     * Update flight information.
     */
    public void updateFlightInfo() {
        if (flights.isEmpty()) {
            Display.printlnRandomColor("There is no flight record to modify.");
            return;
        }

        /*
         * 找出所有初始座位数未更改的航班。
         * Find all flights whose initial seat count has not been modified.
         */
        List<FlightInformation> modifiableFlights = new ArrayList<>();
        for (FlightInformation flight : flights) {
            if (flight.getAvailableSeats() == flight.getInitialAvailableSeats()) {
                modifiableFlights.add(flight);
            }
        }
        if (modifiableFlights.isEmpty()) {
            Display.printlnRandomColor("The number of available seats on all flights has been changed and cannot be modified.");
            return;
        }

        /*
         * 打印所有可修改的航班供选择。
         * Print all modifiable flights for selection.
         */
        Display.printlnRandomColor("Find the following flights that can be modified: ");
        for (int i = 0; i < modifiableFlights.size(); i++) {
            FlightInformation flight = modifiableFlights.get(i);

            /*
             * 数组下标从0开始，所以需要加1。
             * The array index starts from 0, so we need to add 1.
             */
            Display.printlnRandomColor((i + 1) + ". Flight number: " + flight.getFlightNumber());
            Display.printlnRandomColor("   Departure: " + flight.getDeparture());
            Display.printlnRandomColor("   Destination: " + flight.getDestination());
            Display.printlnRandomColor("   Departure time: " + flight.getDepartureTime());
            Display.printlnRandomColor("   Aircraft type: " + flight.getAircraftType());
            Display.printlnRandomColor("   Available seats: " + flight.getAvailableSeats());
            Display.printlnRandomColor("   Flight time: " + flight.getFlightTime(flight.getDeparture(), flight.getDestination()) + "hours");
            Display.printlnRandomColor("   Flight price: " + flight.calculatePrice(flight.getDeparture(), flight.getDestination()));
            System.out.println();
        }

        /*
         * 选择要修改的航班。
         * Select the flight to modify.
         */
        FlightInformation flightToUpdate = selectFlight(modifiableFlights);
        if (flightToUpdate == null) {
            Display.printlnRandomColor("The selection is invalid, and the flight cannot be modified.");
            return;
        }

        /*
         * 打印当前航班信息。
         * Print current flight information.
         */
        Display.printlnRandomColor("The current flight information is as follows: ");
        Display.printlnRandomColor("Flight number: " + flightToUpdate.getFlightNumber());
        Display.printlnRandomColor("Departure: " + flightToUpdate.getDeparture());
        Display.printlnRandomColor("Destination: " + flightToUpdate.getDestination());
        Display.printlnRandomColor("Departure time: " + flightToUpdate.getDepartureTime());
        Display.printlnRandomColor("Aircraft type: " + flightToUpdate.getAircraftType());
        Display.printlnRandomColor("Available seats: " + flightToUpdate.getAvailableSeats());

        /*
         * 录入新的航班信息。
         * Enter new flight information.
         */
        Display.printlnRandomColor("Please enter the new flight information (if you don't want to modify a certain item, please press the Enter key directly to keep the original information): ");

        /*
         * 修改航班号。
         * Modify flight number.
         */
        String newFlightNumber = flightToUpdate.getFlightNumber();
        Display.printlnRandomColor("Please enter the new flight number (6 digits). The current flight number is " + newFlightNumber + ": ");

        /*
         * 如果输入了航班号，则检查是否为6位数字，并且判断是否已经存在。
         * If the input flight number is not empty, check if it is 6 digits and if it exists.
         */
        String inputFlightNumber = scanner.nextLine().trim();
        if (!inputFlightNumber.isEmpty()) {

            /*
             * 使用正则表达式，判断是否为6位数字。
             * Check if the input flight number is 6 digits and if it exists.
             */
            if (inputFlightNumber.matches("\\d{6}")) {
                if (isFlightNumberExists(inputFlightNumber) && !inputFlightNumber.equals(flightToUpdate.getFlightNumber())) {
                    Display.printlnRandomColor("The flight number " + inputFlightNumber + " already exists. Please re-input: ");
                    return;
                }
                newFlightNumber = inputFlightNumber;
            } else {
                Display.printlnRandomColor("The flight number must be 6 digits, and the modification has not taken effect.");
                return;
            }
        }

        /*
         * 修改出发地。
         * Modify departure.
         */
        String newDeparture = selectOptionWithDefault(FlightInformation.getAllowedAirports(), "Please select a new place of departure. The current place of departure is " + flightToUpdate.getDeparture() + ": ", flightToUpdate.getDeparture());

        /*
         * 修改目的地。
         * Modify destination.
         */
        String newDestination;
        do {
            newDestination = selectOptionWithDefault(FlightInformation.getAllowedAirports(), "Please select a new destination. The current destination is " + flightToUpdate.getDestination() + ": ", flightToUpdate.getDestination());
            if (newDeparture.equals(newDestination)) {
                Display.printlnRandomColor("The place of departure and destination cannot be the same. Please select again: ");
            }
        } while (newDeparture.equals(newDestination));

        /*
         * 修改起飞时间。
         * Modify departure time.
         */
        String newDepartureTime = flightToUpdate.getDepartureTime();
        if (!modifyDateTime(newDepartureTime)) {
            return;
        }

        /*
         * 修改飞机型号。
         * Modify aircraft type.
         */
        String newAircraftType = flightToUpdate.getAircraftType();
        Display.printlnRandomColor("Please select a new aircraft model. The current model is " + newAircraftType + ": ");
        String inputAircraftType = selectOptionWithDefault(FlightInformation.getAllowedAircraftTypes(), "Please select a new aircraft model. The current model is " + newAircraftType + ": ", newAircraftType);
        if (inputAircraftType.equals(newAircraftType)) {
            Display.printlnRandomColor("The aircraft model has not been modified. ");
        } else {
            newAircraftType = inputAircraftType;
        }

        /*
         * 修改可用座位数。
         * Modify available seats.
         */
        int newAvailableSeats = flightToUpdate.getAvailableSeats();
        if (!modifyAvailableSeats(newAvailableSeats)) {
            return;
        }

        /*
         * 检查是否有任何修改。
         * Check if there are any modifications.
         */
        if (newFlightNumber.equals(flightToUpdate.getFlightNumber()) &&
                newDeparture.equals(flightToUpdate.getDeparture()) &&
                newDestination.equals(flightToUpdate.getDestination()) &&
                newDepartureTime.equals(flightToUpdate.getDepartureTime()) &&
                newAircraftType.equals(flightToUpdate.getAircraftType()) &&
                newAvailableSeats == flightToUpdate.getAvailableSeats()) {
            Display.printlnRandomColor("No modification has been made. ");
            return;
        }

        /*
         * 更新航班信息。
         * Update flight information.
         */
        flightToUpdate.setFlightNumber(newFlightNumber);
        flightToUpdate.setDeparture(newDeparture);
        flightToUpdate.setDestination(newDestination);
        flightToUpdate.setDepartureTime(newDepartureTime);
        flightToUpdate.setAircraftType(newAircraftType);
        flightToUpdate.setAvailableSeats(newAvailableSeats);

        /*
         * 如果航班号改变，则更新 flightHasPassenger，并将其设置为 false。
         * If the flight number changes, update flightHasPassenger and set it to false.
         */
        if (!newFlightNumber.equals(flightToUpdate.getFlightNumber())) {
            flightHasPassenger.remove(flightToUpdate.getFlightNumber());
            flightHasPassenger.put(newFlightNumber, false);
        }
        Display.printlnRandomColor("The flight information has been modified successfully.");
        printFlights(List.of(flightToUpdate));
    }

    /*
     * 辅助方法1：选择选项，直到输入有效。
     * Helper method1: select option until input is valid.
     */
    private String selectOptionWithDefault(String[] options, String prompt, String defaultValue) {
        for (int i = 0; i < options.length; i++) {

            /*
             * 数组下标从0开始，所以需要加1。
             * The array index starts from 0, so we need to add 1.
             */
            Display.printlnRandomColor((i + 1) + ". " + options[i]);
        }
        Display.printRandomColor(prompt);
        int choice;
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty() && defaultValue != null) {
                return defaultValue;
            }
            try {
                choice = Integer.parseInt(input) - 1;
                if (choice >= 0 && choice < options.length) {
                    return options[choice];
                } else {
                    Display.printlnRandomColor("The selection is invalid. Please re-enter: ");
                }
            } catch (Exception e) {
                Display.printlnRandomColor("Please enter a valid option number: ");
            }
        }
    }

    /*
     * 辅助方法2：修改起飞时间，直到输入有效。
     * Helper method2: modify departure time until input is valid.
     */
    private boolean modifyDateTime(String currentDateTime) {
        while (true) {
            try {
                Display.printlnRandomColor("Please enter the new take-off time (yyyy-MM-dd HH:mm), the current take-off time is " + currentDateTime + ": ");
                String newDateTime = scanner.nextLine().trim();
                if (newDateTime.isEmpty()) {
                    return true;
                }
                validateDateTimeInput(newDateTime);
                return true;
            } catch (Exception e) {
                Display.printlnRandomColor("The time format is incorrect. Please re-enter: ");
            }
        }
    }

    /*
     * 辅助方法3：修改可用座位数，直到输入有效。
     * Helper method3: modify available seats until input is valid.
     */
    private boolean modifyAvailableSeats(int currentSeats) {
        while (true) {
            try {
                Display.printlnRandomColor("Please enter the new number of available seats. The current number of seats is " + currentSeats + ":");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return true;
                }
                int newSeats = Integer.parseInt(input);
                if (newSeats <= 0) {
                    throw new IllegalArgumentException("The number of seats must be a positive integer.");
                }
                return true;

            /*
             * 处理不同输入错误，抛出不同的异常。
             * Handle different input errors and throw different exceptions.
             */
            } catch (NumberFormatException e) {
                Display.printlnRandomColor("Please enter a valid number of seats: ");
            } catch (IllegalArgumentException e) {
                Display.printlnRandomColor(e.getMessage() + "Please re-enter: ");
            }
        }
    }

    /*
     * 确定航班是否有乘客。
     * Determine whether the flight has passengers.
     */
    public void setFlightHasPassenger(String flightNumber, boolean hasPassenger) {
        flightHasPassenger.put(flightNumber, hasPassenger);
    }

    /*
     * 删除指定的航班。
     * Delete the specified flight.
     */
    public void deleteFlight() {
        if (flights.isEmpty()) {
            Display.printlnRandomColor("There is no flight record that can be deleted.");
            return;
        }

        /*
         * 找出所有可用座位数未更改的航班。
         * Find all flights whose available seats have not been changed.
         */
        List<FlightInformation> deletableFlights = new ArrayList<>();
        for (FlightInformation flight : flights) {
            if (flight.getAvailableSeats() == flight.getInitialAvailableSeats()) {
                deletableFlights.add(flight);
            }
        }
        if (deletableFlights.isEmpty()) {
            Display.printlnRandomColor("The number of available seats on all flights has been changed and cannot be deleted.");
            return;
        }

        /*
         * 打印所有可删除的航班供选择。
         * Print all deletable flights for selection.
         */
        Display.printlnRandomColor("Find the following flights that can be deleted: ");
        for (int i = 0; i < deletableFlights.size(); i++) {
            FlightInformation flight = deletableFlights.get(i);

            /*
             * 数组下标从0开始，所以需要加1。
             * The array index starts from 0, so we need to add 1.
             */
            Display.printlnRandomColor((i + 1) + ". Flight number: " + flight.getFlightNumber());
            Display.printlnRandomColor("   Departure: " + flight.getDeparture());
            Display.printlnRandomColor("   Destination: " + flight.getDestination());
            Display.printlnRandomColor("   Departure time: " + flight.getDepartureTime());
            Display.printlnRandomColor("   Aircraft type: " + flight.getAircraftType());
            Display.printlnRandomColor("   Available seats: " + flight.getAvailableSeats());
            Display.printlnRandomColor("   Flight time: " + flight.getFlightTime(flight.getDeparture(), flight.getDestination()) + "hours");
            Display.printlnRandomColor("   Flight price: " + flight.calculatePrice(flight.getDeparture(), flight.getDestination()));
            System.out.println();
        }

        /*
         * 选择要删除的航班。
         * Select the flight to delete.
         */
        FlightInformation flightToDelete = selectFlight(deletableFlights);
        if (flightToDelete == null) {
            Display.printlnRandomColor("The selection is invalid, and the flight cannot be deleted.");
            return;
        }

        /*
         * 确认删除。
         * Confirm deletion.
         */
        Display.printlnRandomColor("Whether to confirm the deletion of the flight " + flightToDelete.getFlightNumber() + "? (Yes / No): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("Yes")) {
            flights.remove(flightToDelete);
            flightHasPassenger.remove(flightToDelete.getFlightNumber());
            Display.printlnRandomColor("Flight " + flightToDelete.getFlightNumber() + " has been deleted.");
        } else {
            Display.printlnRandomColor("The deletion has been canceled.");
        }
    }

    /*
     * 检查航班号是否已经存在。
     * Check if the flight number already exists.
     */
    private boolean isFlightNumberExists(String flightNumber) {
        for (FlightInformation flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return true;
            }
        }
        return false;
    }

    /*
     * 获取所有航班。
     * Get all flights.
     */
    public List<FlightInformation> getFlights() {
        return flights;
    }

    /*
     * 根据航班号查找航班。
     * Find flight by flight number.
     */
    public FlightInformation findFlightByNumber(String flightNumber) {
        for (FlightInformation flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    /*
     * 判断航班上是否有乘客。
     * Determine whether the flight has passengers.
     */
    public boolean hasPassenger(String flightNumber) {
        return flightHasPassenger.getOrDefault(flightNumber, false);
    }
}
/*
 * End of FlightManager Class.
 * Checked by Fan Xinkang.
 */