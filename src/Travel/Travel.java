package Travel;

import UserInterface.Display;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
  此类用于获取选择的城市和景点信息，并显示推荐景点。
  This class is used to get the selected city and scenic spot information, and display recommended scenic spots.
 */
public class Travel {
    private Map<String, String> scenicSpots;

    /*
      初始化城市和景点信息。
      Initialize city and scenic spot information.
     */
    public Travel() {
        scenicSpots = new HashMap<>();
        scenicSpots.put("Beijing", "Forbidden City Museum, Badaling Great Wall, Tiananmen Square, Summer Palace");
        scenicSpots.put("Shanghai", "The Bund, Shanghai Disneyland, Shanghai Wildlife Park, Oriental Pearl Tower");
        scenicSpots.put("Guangzhou", "Shamian Street, Baiyun Mountain, White Goose Pool, Pearl River Night Cruise");
        scenicSpots.put("Nanjing", "Dr. Sun Yat-sen's Mausoleum, Qinhuai River, Ming Xiaoling Mausoleum, Confucius Temple");
        scenicSpots.put("Shenzhen", "Lianhua Mountain Park, Shenzhen Bay Park, Happy Valley, Window of the World");
        scenicSpots.put("Chengdu", "Dujiangyan Irrigation System, Mount Qingcheng, Jinli Ancient Street, Chunxi Road");
        scenicSpots.put("Wuhan", "Yellow Crane Tower, East Lake Greenway, Hubei Provincial Museum, Chu River Han Street");
        scenicSpots.put("Wuxi", "Taihu Lake, Li Garden, Lingshan Grand Buddha, Three Kingdoms City");
    }

    /*
      显示城市列表。
      Show city list.
     */
    public void displayCities() {
        Display.printlnRandomColor("Please select a city: ");
        int index = 1;
        for (String city : scenicSpots.keySet()) {
            Display.printlnRandomColor(index + ". " + city);
            index++;
        }
    }

    /*
      显示指定城市的推荐景点。
      UserInterface.Display recommended scenic spots for a specified city.
     */
    public void displayScenicSpots(String city) {
        String scenicSpots = this.scenicSpots.get(city);
        if (scenicSpots != null) {
            Display.printlnRandomColor(city + "'s recommended scenic spots: " + scenicSpots);
        } else {
            Display.printlnRandomColor("No recommended scenic spot information of the city was found.");
        }
    }

    /*
      获取选择的城市和景点信息，并显示推荐景点。
      Get selected city and scenic spot information, and display recommended scenic spots.
     */
    public void travelAdvice() {
        Scanner scanner = new Scanner(System.in);
        displayCities();
        Display.printRandomColor("Enter the city serial number: ");
        int selectedIndex = scanner.nextInt();

        /*
          虚拟读取，以清除扫描仪类中的缓冲区-错误。
          Dummy read to clear the buffer-bug in Scanner class.
         */
        scanner.nextLine();

        /*
          检查用户输入的序号是否有效。
          Check if the user input index is valid.
         */
        if (selectedIndex > 0 && selectedIndex <= scenicSpots.size()) {
            String selectedCity = (String) scenicSpots.keySet().toArray()[selectedIndex - 1];
            displayScenicSpots(selectedCity);
        } else {
            Display.printlnRandomColor("Invalid serial number. Please enter a valid city serial number.");
        }
    }
}
/*
  End of Travel.Travel Class.
  Checked by Fan Xinkang.
 */