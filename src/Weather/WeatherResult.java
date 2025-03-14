package Weather;

import java.util.List;

/*
 * 此类用于储存和访问天气信息，包括天气位置信息、每日天气信息列表和数据的更新。
 * This class is used to store and access weather information, including weather location information, daily weather information list and data updates.
 */
public class WeatherResult {
    private WeatherLocation location;
    private List<WeatherDaily> daily;
    private String lastUpdate;

    /*
     * 封装信息。
     * getter and setter.
     */
    public WeatherLocation getLocation() {
        return location;
    }

    public void setLocation(WeatherLocation location) {
        this.location = location;
    }

    public List<WeatherDaily> getDaily() {
        return daily;
    }

    public void setDaily(List<WeatherDaily> daily) {
        this.daily = daily;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
/*
 * End of WeatherResult Class.
 * Written and checked by Fan Xinkang.
 */