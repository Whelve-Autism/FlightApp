package Flight;

import java.util.*;

public class FlightInformation {
    private String flightNumber;
    private String departure;
    private String destination;
    private String departureTime;
    private String aircraftType;
    private int availableSeats;
    private int initialAvailableSeats;

    // getter和setter方法
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        if (isValidAirport(departure)) {
            this.departure = departure;
        } else {
            throw new IllegalArgumentException("Invalid departure airport: " + departure);
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        if (isValidAirport(destination)) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Invalid destination airport: " + destination);
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

    public void setAvailableSeats(int availableSeats) {
        if (availableSeats < 0) {
            throw new IllegalArgumentException("可用座位数不能为负数");
        }
        this.availableSeats = availableSeats;
    }

    public int getInitialAvailableSeats() {
        return initialAvailableSeats;
    }

    public void setInitialAvailableSeats(int initialAvailableSeats) {
        this.initialAvailableSeats = initialAvailableSeats;
    }

    // 定义允许的机场集合
    private static final Set<String> allowedAirports = new HashSet<>();
    private static final String[] allowedAirportsArray = {
            "北京首都国际机场",
            "上海浦东国际机场",
            "广州白云国际机场",
            "南京禄口国际机场",
            "深圳宝安国际机场",
            "成都天府国际机场",
            "武汉天河国际机场",
            "苏南硕放国际机场"
    };

    /**
     * 获取允许的机场列表
     *
     * @return 允许的机场数组
     */
    public static String[] getAllowedAirports() {
        return allowedAirportsArray;
    }

    /**
     * 验证机场是否在允许的机场列表中
     *
     * @param airport 机场名称
     * @return 如果机场在允许的列表中返回true，否则返回false
     */
    private static boolean isValidAirport(String airport) {
        return allowedAirports.contains(airport);
    }

    // 定义机场之间的飞行时间（单位：小时）
    private static final Map<String, Map<String, Double>> flightTime = new HashMap<>();

    static {
        // 初始化允许的机场集合
        for (String airport : allowedAirportsArray) {
            allowedAirports.add(airport);
        }

        // 初始化飞行时间
        initializeFlightTimes();
    }

    private static void initializeFlightTimes() {
        // 使用二维数组存储飞行时间数据
        double[][] flightTimeData = {
                {0.0, 2.0, 2.5, 1.5, 3.0, 2.0, 2.0, 2.0}, // 北京首都国际机场
                {2.0, 0.0, 2.0, 1.0, 2.5, 3.0, 1.5, 0.5}, // 上海浦东国际机场
                {2.5, 2.0, 0.0, 2.0, 1.5, 2.0, 2.0, 1.5}, // 广州白云国际机场
                {1.5, 1.0, 2.0, 0.0, 2.0, 1.5, 2.0, 1.5}, // 南京禄口国际机场
                {3.0, 2.5, 1.5, 2.0, 0.0, 1.5, 2.0, 2.0}, // 深圳宝安国际机场
                {2.0, 3.0, 2.0, 1.5, 1.5, 0.0, 1.5, 2.0}, // 成都天府国际机场
                {2.0, 1.5, 2.0, 2.0, 2.0, 1.5, 0.0, 1.5}, // 武汉天河国际机场
                {2.0, 0.5, 1.5, 1.5, 2.0, 2.0, 1.5, 0.0}  // 苏南硕放国际机场
        };

        for (int i = 0; i < allowedAirportsArray.length; i++) {
            String fromAirport = allowedAirportsArray[i];
            Map<String, Double> toAirports = new HashMap<>();
            for (int j = 0; j < allowedAirportsArray.length; j++) {
                if (i != j) {
                    String toAirport = allowedAirportsArray[j];
                    toAirports.put(toAirport, flightTimeData[i][j]);
                }
            }
            flightTime.put(fromAirport, toAirports);
        }
    }

    /**
     * 获取两个机场之间的飞行时间
     *
     * @param departure 出发地机场
     * @param destination 目的地机场
     * @return 飞行时间（小时）
     */
    double getFlightTime(String departure, String destination) {
        if (flightTime.containsKey(departure) && flightTime.get(departure).containsKey(destination)) {
            return flightTime.get(departure).get(destination);
        } else {
            throw new IllegalArgumentException("No flight time data available between " + departure + " and " + destination);
        }
    }

    /**
     * 计算两个机场之间的价格
     *
     * @param departure 出发地机场
     * @param destination 目的地机场
     * @return 价格（元）
     */
    public double calculatePrice(String departure, String destination) {
        double flightTime = getFlightTime(departure, destination);
        final double pricePerHour = 500.0; // 每小时价格
        double basePrice = flightTime * pricePerHour;

        switch (aircraftType) {
            case "波音737":
                basePrice *= 0.8;
                break;
            case "波音747":
                basePrice *= 1.2;
                break;
            case "波音777":
                // 保持原价
                break;
            case "波音787":
                basePrice *= 1.1;
                break;
            case "空客320":
                basePrice *= 0.8;
                break;
            case "空客380":
                basePrice *= 1.3;
                break;
            default:
                throw new IllegalArgumentException("Invalid aircraft type: " + aircraftType);
        }

        return basePrice;
    }

    /**
     * 选择飞机型号
     *
     * @param scanner 扫描器
     * @return 选择的飞机型号
     */
    public static String selectAircraftType(Scanner scanner) {
        String[] validTypes = {"波音737", "波音747", "波音777", "波音787", "空客320", "空客380"};
        for (int i = 0; i < validTypes.length; i++) {
            System.out.println((i + 1) + ". " + validTypes[i]);
        }
        System.out.print("请选择飞机型号：");
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < validTypes.length) {
                    return validTypes[choice];
                } else {
                    System.out.println("选择无效，请重新输入。");
                }
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的选项编号。");
            }
        }
    }

    /**
     * 减少可用座位数
     */
    public void reduceAvailableSeats() {
        if (availableSeats > 0) {
            availableSeats--;
        } else {
            throw new IllegalStateException("没有可用座位");
        }
    }

    /**
     * 增加可用座位数
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

        // 计算并输出航班情况
        double flightTime = getFlightTime(departure, destination);
        System.out.println("以下是您添加航班的具体明细：");
        System.out.println("航班号: " + flightNumber);
        System.out.println("出发地: " + departure);
        System.out.println("目的地: " + destination);
        System.out.println("出发时间: " + departureTime);
        System.out.println("机型: " + aircraftType);
        System.out.println("可选座位数: " + availableSeats);
        System.out.println("飞行时间: " + flightTime + "小时");
        System.out.println("飞行价格: " + calculatePrice(departure, destination));
    }

    // 新增方法：获取允许的飞机型号列表
    public static String[] getAllowedAircraftTypes() {
        return new String[]{"波音737", "波音747", "波音777", "波音787", "空客320", "空客380"};
    }

    @Override
    public String toString() {
        return "航班号: " + flightNumber + ", 出发地: " + departure + ", 目的地: " + destination + ", 出发时间: " + departureTime + ", 机型: " + aircraftType + ", 可用座位数: " + availableSeats;
    }
}