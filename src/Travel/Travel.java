package Travel;

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
        scenicSpots.put("北京", "故宫博物馆, 八达岭长城, 天安门广场, 颐和园");
        scenicSpots.put("上海", "外滩, 上海迪士尼乐园，上海野生动物园, 东方明珠");
        scenicSpots.put("广州", "沙面大街, 白云山, 白鹅潭, 珠江夜游");
        scenicSpots.put("南京", "中山陵, 秦淮河, 明孝陵, 夫子庙");
        scenicSpots.put("深圳", "莲花山公园, 深圳湾公园, 深圳欢乐谷, 深圳世界之窗");
        scenicSpots.put("成都", "都江堰, 青城山, 锦里古街, 春熙路");
        scenicSpots.put("武汉", "黄鹤楼, 东湖绿道, 湖北省博物馆, 楚河汉街");
        scenicSpots.put("无锡", "太湖, 蠡园, 灵山大佛, 三国城");
    }

    /*
      显示城市列表。
      Show city list.
     */
    public void displayCities() {
        System.out.println("请选择一个城市:");
        int index = 1;
        for (String city : scenicSpots.keySet()) {
            System.out.println(index + ". " + city);
            index++;
        }
    }

    /*
      显示指定城市的推荐景点。
      Display recommended scenic spots for a specified city.
     */
    public void displayScenicSpots(String city) {
        String scenicSpots = this.scenicSpots.get(city);
        if (scenicSpots != null) {
            System.out.println(city + "的推荐景区: " + scenicSpots);
        } else {
            System.out.println("未找到该城市的推荐景区信息。");
        }
    }

    /*
      获取选择的城市和景点信息，并显示推荐景点。
      Get selected city and scenic spot information, and display recommended scenic spots.
     */
    public void travelAdvice() {
        Scanner scanner = new Scanner(System.in);
        displayCities();
        System.out.print("输入城市序号: ");
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
            System.out.println("无效的序号，请输入有效的城市序号。");
        }
    }
}
/*
  End of Menu Class.
  Checked by Fan Xinkang.
 */