package Flight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FlightManager {
    private List<FlightInformation> flights = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private Map<String, Boolean> flightHasPassenger = new HashMap<>(); // 记录航班是否有乘客购票

    // 录入航班信息并检查合法性
    public FlightInformation inputFlightInfo() {
        System.out.println("请输入航班号（6位数字）：");
        String flightNumber;
        do {
            flightNumber = scanner.nextLine();
            if (!flightNumber.matches("\\d{6}")) {
                System.out.println("航班号必须是6位数字，请重新输入：");
            } else if (isFlightNumberExists(flightNumber)) {
                System.out.println("航班号 " + flightNumber + " 已经存在，请重新输入：");
            }
        } while (!flightNumber.matches("\\d{6}") || isFlightNumberExists(flightNumber));

        // 使用公共方法选择出发地
        String departure = selectOption(FlightInformation.getAllowedAirports(), "请选择出发地：");

        // 使用公共方法选择目的地
        String destination;
        do {
            destination = selectOption(FlightInformation.getAllowedAirports(), "请选择目的地：");
            if (departure.equals(destination)) {
                System.out.println("起飞地和目的地不能相同，请重新选择。");
            }
        } while (departure.equals(destination));

        // 获取起飞时间
        String departureTime;
        while (true) {
            try {
                System.out.println("请输入起飞时间（yyyy-MM-dd HH:mm）：");
                departureTime = scanner.nextLine();
                validateDateTimeInput(departureTime);
                break;
            } catch (ParseException e) {
                System.out.println("时间格式不正确，请重新输入。");
            }
        }

        // 显示飞机型号选择菜单
        String aircraftType = FlightInformation.selectAircraftType(scanner);

        int availableSeats;
        while (true) {
            try {
                System.out.println("请输入可用座位数：");
                availableSeats = Integer.parseInt(scanner.nextLine());
                if (availableSeats <= 0) {
                    throw new IllegalArgumentException("座位数必须为正整数");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的座位数。");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " 请重新输入。");
            }
        }

        FlightInformation flight = new FlightInformation(flightNumber, departure, destination, departureTime, aircraftType, availableSeats);
        addFlight(flight);
        flightHasPassenger.put(flightNumber, false); // 初始化航班没有乘客购票
        System.out.println("航班添加成功。");
        return flight;
    }

    private void addFlight(FlightInformation flight) {
        flights.add(flight);
    }

    // 查询符合条件的航班
    public List<FlightInformation> searchFlights(String departure, String destination) {
        List<FlightInformation> matchingFlights = new ArrayList<>();
        for (FlightInformation flight : flights) {
            if (flight.getDeparture().equals(departure) && flight.getDestination().equals(destination)) {
                matchingFlights.add(flight);
            }
        }
        return matchingFlights;
    }

    // 打印航班列表
    public void printFlights(List<FlightInformation> flights) {
        if (flights.isEmpty()) {
            System.out.println("没有找到符合条件的航班。");
        } else {
            System.out.println("找到以下符合条件的航班：");
            for (int i = 0; i < flights.size(); i++) {
                FlightInformation flight = flights.get(i);
                System.out.println((i + 1) + ". 航班号: " + flight.getFlightNumber());
                System.out.println("   出发地: " + flight.getDeparture());
                System.out.println("   目的地: " + flight.getDestination());
                System.out.println("   出发时间: " + flight.getDepartureTime());
                System.out.println("   机型: " + flight.getAircraftType());
                System.out.println("   可选座位数: " + flight.getAvailableSeats());
                System.out.println("   飞行时间: " + flight.getFlightTime(flight.getDeparture(), flight.getDestination()) + "小时");
                System.out.println("   飞行价格: " + flight.calculatePrice(flight.getDeparture(), flight.getDestination()));
                System.out.println();
            }
        }
    }

    // 选择航班
    public FlightInformation selectFlight(List<FlightInformation> flights) {
        if (flights.isEmpty()) {
            return null;
        }

        while (true) {
            try {
                System.out.print("请选择航班编号（输入数字）：");
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < flights.size()) {
                    return flights.get(choice);
                } else {
                    System.out.println("选择无效，请重新输入。");
                }
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的选项编号。");
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

    private void validateDateTimeInput(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setLenient(false);
        sdf.parse(dateTime);
    }

    // 修改航班信息
    public void updateFlightInfo() {
        if (flights.isEmpty()) {
            System.out.println("没有航班记录可以修改。");
            return;
        }

        // 找出所有没有乘客购票的航班
        List<FlightInformation> modifiableFlights = new ArrayList<>();
        for (FlightInformation flight : flights) {
            if (!flightHasPassenger.getOrDefault(flight.getFlightNumber(), false)) {
                modifiableFlights.add(flight);
            }
        }

        if (modifiableFlights.isEmpty()) {
            System.out.println("所有航班都有乘客购票，无法修改。");
            return;
        }

        // 打印所有可修改的航班供用户选择
        System.out.println("找到以下可以修改的航班：");
        for (int i = 0; i < modifiableFlights.size(); i++) {
            FlightInformation flight = modifiableFlights.get(i);
            System.out.println((i + 1) + ". 航班号: " + flight.getFlightNumber());
            System.out.println("   出发地: " + flight.getDeparture());
            System.out.println("   目的地: " + flight.getDestination());
            System.out.println("   出发时间: " + flight.getDepartureTime());
            System.out.println("   机型: " + flight.getAircraftType());
            System.out.println("   可选座位数: " + flight.getAvailableSeats());
            System.out.println("   飞行时间: " + flight.getFlightTime(flight.getDeparture(), flight.getDestination()) + "小时");
            System.out.println("   飞行价格: " + flight.calculatePrice(flight.getDeparture(), flight.getDestination()));
            System.out.println();
        }

        // 选择要修改的航班
        FlightInformation flightToUpdate = selectFlight(modifiableFlights);
        if (flightToUpdate == null) {
            System.out.println("选择无效，无法修改航班。");
            return;
        }

        // 显示当前航班信息
        System.out.println("当前航班信息如下：");
        System.out.println("航班号: " + flightToUpdate.getFlightNumber());
        System.out.println("出发地: " + flightToUpdate.getDeparture());
        System.out.println("目的地: " + flightToUpdate.getDestination());
        System.out.println("起飞时间: " + flightToUpdate.getDepartureTime());
        System.out.println("机型: " + flightToUpdate.getAircraftType());
        System.out.println("可用座位数: " + flightToUpdate.getAvailableSeats());

        // 录入新的航班信息
        System.out.println("请输入新的航班信息（如果不想修改某项，请直接按回车键保留原信息）：");

        // 修改航班号
        String newFlightNumber = flightToUpdate.getFlightNumber();
        System.out.println("请输入新的航班号（6位数字），当前航班号为 " + newFlightNumber + "：");
        String inputFlightNumber = scanner.nextLine().trim();
        if (!inputFlightNumber.isEmpty()) {
            if (inputFlightNumber.matches("\\d{6}")) {
                if (isFlightNumberExists(inputFlightNumber) && !inputFlightNumber.equals(flightToUpdate.getFlightNumber())) {
                    System.out.println("航班号 " + inputFlightNumber + " 已经存在，修改未生效。");
                    return;
                }
                newFlightNumber = inputFlightNumber;
            } else {
                System.out.println("航班号必须是6位数字，修改未生效。");
                return;
            }
        }

        // 修改出发地
        String newDeparture = selectOptionWithDefault(FlightInformation.getAllowedAirports(), "请选择新的出发地，当前出发地为 " + flightToUpdate.getDeparture() + "：", flightToUpdate.getDeparture());

        // 修改目的地
        String newDestination;
        do {
            newDestination = selectOptionWithDefault(FlightInformation.getAllowedAirports(), "请选择新的目的地，当前目的地为 " + flightToUpdate.getDestination() + "：", flightToUpdate.getDestination());
            if (newDeparture.equals(newDestination)) {
                System.out.println("起飞地和目的地不能相同，请重新选择。");
            }
        } while (newDeparture.equals(newDestination));

        // 修改起飞时间
        String newDepartureTime = flightToUpdate.getDepartureTime();
        if (!modifyDateTime(newDepartureTime)) {
            return;
        }

        // 修改飞机型号
        String newAircraftType = flightToUpdate.getAircraftType();
        System.out.println("请选择新的飞机型号，当前机型为 " + newAircraftType + "：");
        String inputAircraftType = selectOptionWithDefault(FlightInformation.getAllowedAircraftTypes(), "请选择新的飞机型号，当前机型为 " + newAircraftType + "：", newAircraftType);
        if (inputAircraftType.equals(newAircraftType)) {
            System.out.println("飞机型号未修改。");
        } else {
            newAircraftType = inputAircraftType;
        }

        // 修改可用座位数
        int newAvailableSeats = flightToUpdate.getAvailableSeats();
        if (!modifyAvailableSeats(newAvailableSeats)) {
            return;
        }

        // 检查是否有任何修改
        if (newFlightNumber.equals(flightToUpdate.getFlightNumber()) &&
                newDeparture.equals(flightToUpdate.getDeparture()) &&
                newDestination.equals(flightToUpdate.getDestination()) &&
                newDepartureTime.equals(flightToUpdate.getDepartureTime()) &&
                newAircraftType.equals(flightToUpdate.getAircraftType()) &&
                newAvailableSeats == flightToUpdate.getAvailableSeats()) {
            System.out.println("没有进行任何修改。");
            return;
        }

        // 更新航班信息
        flightToUpdate.setFlightNumber(newFlightNumber);
        flightToUpdate.setDeparture(newDeparture);
        flightToUpdate.setDestination(newDestination);
        flightToUpdate.setDepartureTime(newDepartureTime);
        flightToUpdate.setAircraftType(newAircraftType);
        flightToUpdate.setAvailableSeats(newAvailableSeats);

        // 更新 flightHasPassenger 映射
        if (!newFlightNumber.equals(flightToUpdate.getFlightNumber())) {
            flightHasPassenger.remove(flightToUpdate.getFlightNumber());
            flightHasPassenger.put(newFlightNumber, false);
        }

        System.out.println("航班信息修改成功。");
        printFlights(List.of(flightToUpdate));
    }

    // 辅助方法：选择选项并允许保留默认值
    private String selectOptionWithDefault(String[] options, String prompt, String defaultValue) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print(prompt);
        int choice;
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                choice = Integer.parseInt(input) - 1;
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

    // 辅助方法：修改起飞时间
    private boolean modifyDateTime(String currentDateTime) {
        while (true) {
            try {
                System.out.println("请输入新的起飞时间（yyyy-MM-dd HH:mm），当前起飞时间为 " + currentDateTime + "：");
                String newDateTime = scanner.nextLine().trim();
                if (newDateTime.isEmpty()) {
                    return true;
                }
                validateDateTimeInput(newDateTime);
                return true;
            } catch (ParseException e) {
                System.out.println("时间格式不正确，请重新输入。");
            }
        }
    }

    // 辅助方法：修改可用座位数
    private boolean modifyAvailableSeats(int currentSeats) {
        while (true) {
            try {
                System.out.println("请输入新的可用座位数，当前座位数为 " + currentSeats + "：");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return true;
                }
                int newSeats = Integer.parseInt(input);
                if (newSeats <= 0) {
                    throw new IllegalArgumentException("座位数必须为正整数");
                }
                return true;
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的座位数。");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " 请重新输入。");
            }
        }
    }

    // 设置航班是否有乘客购票
    public void setFlightHasPassenger(String flightNumber, boolean hasPassenger) {
        flightHasPassenger.put(flightNumber, hasPassenger);
    }

    // 删除指定的航班
    public void deleteFlight() {
        if (flights.isEmpty()) {
            System.out.println("没有航班记录可以删除。");
            return;
        }

        // 找出所有没有乘客购票的航班
        List<FlightInformation> deletableFlights = new ArrayList<>();
        for (FlightInformation flight : flights) {
            if (!flightHasPassenger.getOrDefault(flight.getFlightNumber(), false)) {
                deletableFlights.add(flight);
            }
        }

        if (deletableFlights.isEmpty()) {
            System.out.println("所有航班都有乘客购票，无法删除。");
            return;
        }

        // 打印所有可删除的航班供用户选择
        System.out.println("找到以下可以删除的航班：");
        for (int i = 0; i < deletableFlights.size(); i++) {
            FlightInformation flight = deletableFlights.get(i);
            System.out.println((i + 1) + ". 航班号: " + flight.getFlightNumber());
            System.out.println("   出发地: " + flight.getDeparture());
            System.out.println("   目的地: " + flight.getDestination());
            System.out.println("   出发时间: " + flight.getDepartureTime());
            System.out.println("   机型: " + flight.getAircraftType());
            System.out.println("   可选座位数: " + flight.getAvailableSeats());
            System.out.println("   飞行时间: " + flight.getFlightTime(flight.getDeparture(), flight.getDestination()) + "小时");
            System.out.println("   飞行价格: " + flight.calculatePrice(flight.getDeparture(), flight.getDestination()));
            System.out.println();
        }

        // 选择要删除的航班
        FlightInformation flightToDelete = selectFlight(deletableFlights);
        if (flightToDelete == null) {
            System.out.println("选择无效，无法删除航班。");
            return;
        }

        // 确认删除
        System.out.print("是否确认删除航班 " + flightToDelete.getFlightNumber() + "? (是/否): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("是")) {
            flights.remove(flightToDelete);
            flightHasPassenger.remove(flightToDelete.getFlightNumber());
            System.out.println("航班 " + flightToDelete.getFlightNumber() + " 已删除。");
        } else {
            System.out.println("删除已取消。");
        }
    }

    // 检查航班号是否已经存在
    private boolean isFlightNumberExists(String flightNumber) {
        for (FlightInformation flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return true;
            }
        }
        return false;
    }
}