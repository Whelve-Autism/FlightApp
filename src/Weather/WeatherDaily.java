package Weather;

/*
 * 此类用于储存和访问天气信息，包括日期、白天天气情况及状态代码、夜晚天气情况及状态代码、最高温度、最低温度、降雨量、降水量、风向、风向角度、风速、风力和空气湿度。
 * This class is used to store and access weather information, including date, daytime weather conditions and status codes, night weather conditions and status codes, maximum temperature, minimum temperature, rainfall, precipitation, wind direction, wind direction angle, wind speed, wind force and air humidity.
 */
public class WeatherDaily {
    private String date;
    private String textDay;
    private String codeDay;
    private String textNight;
    private String codeNight;
    private String high;
    private String low;
    private String rainfall;
    private String precipitation;
    private String windDirection;
    private String windDirectionDegree;
    private String windSpeed;
    private String windScale;
    private String humidity;

    /*
     * 封装信息。
     * getter and setter.
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTextDay() {
        return textDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay;
    }

    public String getCodeDay() {
        return codeDay;
    }

    public void setCodeDay(String codeDay) {
        this.codeDay = codeDay;
    }

    public String getTextNight() {
        return textNight;
    }

    public void setTextNight(String textNight) {
        this.textNight = textNight;
    }

    public String getCodeNight() {
        return codeNight;
    }

    public void setCodeNight(String codeNight) {
        this.codeNight = codeNight;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getRainfall() {
        return rainfall;
    }

    public void setRainfall(String rainfall) {
        this.rainfall = rainfall;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindDirectionDegree() {
        return windDirectionDegree;
    }

    public void setWindDirectionDegree(String windDirectionDegree) {
        this.windDirectionDegree = windDirectionDegree;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
/*
 * End of WeatherDaily Class.
 * Written and checked by Fan Xinkang.
 */