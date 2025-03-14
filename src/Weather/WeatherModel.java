package Weather;

import java.util.List;

/*
 * 此类用于封装与天气结果相关的数据模型。
 * This class is used to encapsulate data models related to weather results.
 */
public class WeatherModel {

    /*
     * 存储天气结果列表。
     * Store a list of weather results.
     */
    private List<WeatherResult> results;

    /*
     * 封装信息。
     * getter and setter.
     */
    public List<WeatherResult> getResults() {
        return results;
    }

    public void setResults(List<WeatherResult> results) {
        this.results = results;
    }
}
/*
 * End of WeatherModel Class.
 * Written and checked by Fan Xinkang.
 */