package Weather;

/*
 * 此类用于储存和访问天气信息，包括唯一标识、名称、国家、相关路径信息、时区和时区偏移量。
 * This class is used to store and access weather information, including unique identification, name, country, relevant path information, time zone and time zone offset.
 */
public class WeatherLocation {
    private String id;
    private String name;
    private String country;
    private String path;
    private String timezone;
    private String timezoneOffset;

    /*
     * 封装信息。
     * getter and setter.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(String timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }
}
/*
 * End of Weather.WeatherLocation Class.
 * Written and checked by Fan Xinkang.
 */