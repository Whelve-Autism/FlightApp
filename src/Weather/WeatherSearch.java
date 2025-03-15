package Weather;

import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 * 此类用于查询天气信息。
 * This class is used to search for weather information.
 */
public class WeatherSearch {
    private OkHttpClient okHttpClient;
    private Scanner scanner;

    /*
     * 构造函数，初始化 okHttpClient 实例。
     * Constructor, initialize the okHttpClient instance.
     */
    public WeatherSearch(Scanner scanner) {

        /*
         * 构建 okHttpClient 实例。
         * Build an okHttpClient instance.
         */
        okHttpClient = new OkHttpClient();
        this.scanner = scanner;
    }

    /*
     * 根据输入的 url，读取页面内容并返回。
     * Read the page content and return it according to the input url.
     */
    public String getPageContentSync(String url) {

        /*
         * 参数判断，未输入参数则直接返回。
         * Parameter judgment. If no parameter is entered, return directly.
         */
        if (url == null || url.isEmpty()) {
            return null;
        }

        /*
         * 定义一个 request。
         * Define a request.
         */
        Request request = new Request.Builder().url(url).build();

        /*
         * 使用 client 去请求。
         * Use client to request.
         */
        try (Response response = okHttpClient.newCall(request).execute()) {

            /*
             * 获得返回结果。
             * Get the return result.
             */
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            System.out.println("Request " + url + " error.");
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 根据选择的城市，获取天气信息。
     * Get weather information based on the selected city.
     */
    public void fetchWeatherForCities() {
        List<String> cities = Arrays.asList("Beijing", "Shanghai", "Guangzhou", "Nanjing", "Shenzhen", "Chengdu", "Wuhan", "Wuxi");
        System.out.println("Please select a city to check the weather information: ");
        for (int i = 0; i < cities.size(); i++) {

            /*
             * 数组下标从0开始，所以需要加1。
             * The array index starts from 0, so we need to add 1.
             */
            System.out.println((i + 1) + ". " + cities.get(i));
        }
        int choice = scanner.nextInt();

        /*
         * 虚拟读取，以清除扫描仪类中的缓冲区-错误。
         * Dummy read to clear the buffer-bug in Scanner class.
         */
        scanner.nextLine();

        /*
         * 判断选择的城市是否在列表中，如果存在则获取天气信息，否则提示无效的选择。
         * Check if the selected city is in the list, if it exists, get the weather information, otherwise prompt for an invalid selection.
         */
        if (choice >= 1 && choice <= cities.size()) {
            String city = cities.get(choice - 1);

            /*
             * API密钥。
             * API key.
             */
            String apiKey = "SCYrvkytJze9qyzOh";
            String baseUrl = "https://api.seniverse.com/v3/weather/daily.json?key=%s&location=%s";
            String url = String.format(baseUrl, apiKey, city);
            String content = getPageContentSync(url);

            /*
             * 解析 JSON 数据并输出。
             * Parse JSON data and output.
             */
            if (content != null) {
                WeatherModel weatherModel = JSON.parseObject(content, WeatherModel.class);
                if (weatherModel != null && weatherModel.getResults() != null && !weatherModel.getResults().isEmpty()) {
                    WeatherResult result = weatherModel.getResults().get(0);
                    WeatherLocation location = result.getLocation();
                    List<WeatherDaily> dailyList = result.getDaily();

                    /*
                     * 输出天气信息。
                     * Output weather information.
                     */
                    System.out.println("City: " + location.getName());
                    for (WeatherDaily daily : dailyList) {
                        System.out.println("Date: " + daily.getDate());
                        System.out.println("Daytime weather: " + daily.getTextDay());
                        System.out.println("Night weather: " + daily.getTextNight());
                        System.out.println("The highest temperature: " + daily.getHigh() + "°C");
                        System.out.println("The lowest temperature: " + daily.getLow() + "°C");
                        System.out.println("Wind direction: " + daily.getWindDirection());
                        System.out.println("Wind speed: " + daily.getWindSpeed() + " km/h");
                        System.out.println("Moisture content: " + daily.getHumidity() + "%");
                        System.out.println();
                    }
                } else {
                    System.out.println("No weather information for " + city + " was found.");
                }
            } else {
                System.out.println("Unable to get the weather information of Nanjing " + city + ".");
            }
        } else {
            System.out.println("Invalid selection. Please select again: ");
        }
    }
}
/*
 * End of WeatherSearch Class.
 * Written and checked by Fan Xinkang.
 */