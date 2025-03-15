package Flight;

import java.util.*;

/*
 * 此类用于储存和访问航班信息，包括航班号、出发机场、到达机场、出发时刻、飞机型号、剩余座位数、初始座位数。
 * This class is used to store and access flight information, including flight number, departure airport, destination airport, departure time, aircraft type, remaining seats, and initial seats.
 */
public class FlightInformation {
    private String flightNumber;
    private String departure;
    private String destination;
    private String departureTime;
    private String aircraftType;
    private int availableSeats;
    private int initialAvailableSeats;

    /*
     * 封装信息。
     * getter and setter.
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    /*
     * 设置出发机场，并验证其是否在允许的机场列表中。
     * Set the departure airport and validate whether it is in the allowed airport list.
     */
    public void setDeparture(String departure) {
        if (isValidAirport(departure)) {
            this.departure = departure;
        } else {
            throw new IllegalArgumentException("Invalid departure airport: " + departure + ".");
        }
    }

    public String getDestination() {
        return destination;
    }

    /*
     * 设置到达机场，并验证其是否在允许的机场列表中。
     * Set the destination airport and validate whether it is in the allowed airport list.
     */
    public void setDestination(String destination) {
        if (isValidAirport(destination)) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Invalid destination airport: " + destination + ".");
        }
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    /*
     * 设置剩余座位数，并验证其是否为非负数。
     * Set the remaining seats and validate whether it is non-negative.
     */
    public void setAvailableSeats(int availableSeats) {
        if (availableSeats < 0) {
            throw new IllegalArgumentException("The initial number of seats cannot be negative.");
        }
        this.availableSeats = availableSeats;
    }

    public int getInitialAvailableSeats() {
        return initialAvailableSeats;
    }

    public void setInitialAvailableSeats(int initialAvailableSeats) {
        this.initialAvailableSeats = initialAvailableSeats;
    }

    /*
     * 定义指定的机场集合。
     * Define the designated airport array.
     */
    private static final Set<String> allowedAirports = new HashSet<>();
    private static final String[] allowedAirportsArray = {
            "Beijing Capital International Airport",
            "Shanghai Pudong International Airport",
            "Guangzhou Baiyun International Airport",
            "Nanjing Lukou International Airport",
            "Shenzhen Bao'an International Airport",
            "Chengdu Tianfu International airport",
            "Wuhan Tianhe International Airport",
            "Sunan Shuofang International Airport"
    };

    /*
     * 获取允许的机场列表，并且返回允许的机场数组。
     * Get the list of allowed airports and return the allowed airport array.
     */
    public static String[] getAllowedAirports() {
        return allowedAirportsArray;
    }

    /*
     * 验证机场是否在允许的机场列表中。
     * Check if the airport is in the allowed airport list.
     */
    private static boolean isValidAirport(String airport) {
        return allowedAirports.contains(airport);
    }

    /*
     * 定义指定机场之间的飞行时间。
     * Define the flight time between designated airports.
     */
    private static final Map<String, Map<String, Double>> flightTime = new HashMap<>();

    /*
     * 初始化指定的机场集合。
     * Initialize the allowed airport list.
     */
    static {
        for (String airport : allowedAirportsArray) {
            allowedAirports.add(airport);
        }

        /*
         * 初始化飞行时间。
         * Initialize the flight time.
         */
        initializeFlightTimes();
    }

    private static void initializeFlightTimes() {

        /*
         * 使用二维数组存储飞行时间数据。
         * The flight time data is stored in a two-dimensional array.
         */
        double[][] flightTimeData = {
                {0.0, 2.0, 2.5, 1.5, 3.0, 2.0, 2.0, 2.0}, // 北京首都国际机场 (Beijing Capital International Airport)
                {2.0, 0.0, 2.0, 1.0, 2.5, 3.0, 1.5, 0.5}, // 上海浦东国际机场 (Shanghai Pudong International Airport)
                {2.5, 2.0, 0.0, 2.0, 1.5, 2.0, 2.0, 1.5}, // 广州白云国际机场 (Guangzhou Baiyun International Airport)
                {1.5, 1.0, 2.0, 0.0, 2.0, 1.5, 2.0, 1.5}, // 南京禄口国际机场 (Nanjing Lukou International Airport)
                {3.0, 2.5, 1.5, 2.0, 0.0, 1.5, 2.0, 2.0}, // 深圳宝安国际机场 (Shenzhen Bao'an International Airport)
                {2.0, 3.0, 2.0, 1.5, 1.5, 0.0, 1.5, 2.0}, // 成都天府国际机场 (Chengdu Tianfu International airport)
                {2.0, 1.5, 2.0, 2.0, 2.0, 1.5, 0.0, 1.5}, // 武汉天河国际机场 (Wuhan Tianhe International Airport)
                {2.0, 0.5, 1.5, 1.5, 2.0, 2.0, 1.5, 0.0}  // 苏南硕放国际机场 (Sunan Shuofang International Airport)
        };

        /*
         * 将数据填充到flightTime中。
         * Fill the data into flightTime.
         */
        for (int i = 0; i < allowedAirportsArray.length; i++) {
            String fromAirport = allowedAirportsArray[i];
            Map<String, Double> toAirports = new HashMap<>();
            for (int j = 0; j < allowedAirportsArray.length; j++) {

                /* 出发机场和到达机场不能相同。
                 * Departure airport and destination airport cannot be the same.
                 */
                if (i != j) {
                    String toAirport = allowedAirportsArray[j];
                    toAirports.put(toAirport, flightTimeData[i][j]);
                }
            }
            flightTime.put(fromAirport, toAirports);
        }
    }

    /*
     * 获取两个机场之间的飞行时间。
     * Get the flight time between two airports.
     */
    double getFlightTime(String departure, String destination) {
        if (flightTime.containsKey(departure) && flightTime.get(departure).containsKey(destination)) {
            return flightTime.get(departure).get(destination);
        } else {
            throw new IllegalArgumentException("No flight time data available between " + departure + " and " + destination + ".");
        }
    }

    /*
     * 计算两个机场之间的飞行价格。
     * Calculate the flight price between two airports.
     */
    public double calculatePrice(String departure, String destination) {
        double flightTime = getFlightTime(departure, destination);
        final double pricePerHour = 500.0;
        double basePrice = flightTime * pricePerHour;

        /*
         * 根据飞机型号计算价格。
         * Calculate the price based on the aircraft type.
         */
        switch (aircraftType) {
            case "Boeing 737":
                basePrice *= 0.7;
                break;
            case "Boeing 747":
                basePrice *= 1.3;
                break;
            case "Boeing 777":
                basePrice *= 1.1;
                break;
            case "Boeing 787":
                basePrice *= 1.2;
                break;
            case "Airbus 320":
                basePrice *= 0.8;
                break;
            case "Airbus 380":
                basePrice *= 1.4;
                break;
            default:
                throw new IllegalArgumentException("Invalid aircraft type: " + aircraftType + ".");
        }
        return basePrice;
    }

    /*
     * 选择飞机型号。
     * Select aircraft type.
     */
    public static String selectAircraftType(Scanner scanner) {
        String[] validTypes = {"Boeing 737", "Boeing 747", "Boeing 777", "Boeing 787", "Airbus 320", "Airbus 380"};
        for (int i = 0; i < validTypes.length; i++) {

            /*
             * 数组下标从0开始，所以需要加1。
             * The array index starts from 0, so we need to add 1.
             */
            System.out.println((i + 1) + ". " + validTypes[i]);
        }
        System.out.print("Please select the aircraft model: ");
        int choice;
        while (true) {
            try {

                /*
                 * 数组下标从0开始，所以需要减1。
                 * The array index starts from 0, so we need to subtract 1.
                 */
                choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < validTypes.length) {
                    return validTypes[choice];
                } else {
                    System.out.println("The selection is invalid, please re-enter.");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid option number.");
            }
        }
    }

    /*
     * 减少可用座位数。
     * Reduce the available seats.
     */
    public void reduceAvailableSeats() {
        if (availableSeats > 0) {
            availableSeats--;
        } else {
            throw new IllegalStateException("There are no seats available.");
        }
    }

    /*
     * 增加可用座位数。
     * Add the available seats.
     */
    public void addAvailableSeats() {
        availableSeats++;
    }

    public FlightInformation(String flightNumber, String departure, String destination, String departureTime, String aircraftType, int availableSeats) {
        this.flightNumber = flightNumber;
        setDeparture(departure);
        setDestination(destination);
        this.departureTime = departureTime;
        this.aircraftType = aircraftType;
        setAvailableSeats(availableSeats);
        this.initialAvailableSeats = availableSeats;

        /*
         * 计算并输出航班信息。
         * Calculate and output the flight information.
         */
        double flightTime = getFlightTime(departure, destination);
        System.out.println("The following are the specific details of the flights you added:");
        System.out.println("Flight number: " + flightNumber);
        System.out.println("Departure: " + departure);
        System.out.println("Destination: " + destination);
        System.out.println("Departure time: " + departureTime);
        System.out.println("Aircraft type: " + aircraftType);
        System.out.println("AvailableSeats: " + availableSeats);
        System.out.println("Flight time: " + flightTime + "hours");
        System.out.println("Flight price: " + calculatePrice(departure, destination));
    }

    /*
     * 获取允许的飞机型号列表。
     * Get the list of allowed aircraft types.
     */
    public static String[] getAllowedAircraftTypes() {
        return new String[]{"Boeing 737", "Boeing 747", "Boeing 777", "Boeing 787", "Airbus 320", "Airbus 380"};
    }

    /*
     * 重写toString方法，返回航班信息。
     * Override the toString method to return the flight information.
     */
    @Override
    public String toString() {
        return "Flight number: " + flightNumber + ", Departure: " + departure + ", Destination: " + destination + ", Departure time: " + departureTime + ", Aircraft type: " + aircraftType + ", AvailableSeats: " + availableSeats;
    }
}
/*
 * End of FlightInformation Class.
 * Checked by Fan Xinkang.
 */