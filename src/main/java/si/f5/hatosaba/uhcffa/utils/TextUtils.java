package si.f5.hatosaba.uhcffa.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

public class TextUtils {

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return dtf.format(LocalDateTime.now());
    }

    public static String formatSeconds(int seconds) {
        int minutes = seconds / 60;
        seconds %= 60;
        String sMinutes = minutes + "";
        String sSeconds = seconds + "";
        if (minutes < 10)
            sMinutes = "0" + minutes;
        if (seconds < 10)
            sSeconds = "0" + seconds;
        return sMinutes + ":" + sSeconds;
    }

    public static double formatDouble(double number, int precision) {
        int tmp = (int)number * (int)Math.pow(10.0D, precision);
        return tmp / Math.pow(10.0D, precision);
    }

    public static String enumToString(String enumName) {
        StringJoiner sb = new StringJoiner(" ");
        for (String s : enumName.split("_"))
            sb.add(s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase());
        return sb.toString();
    }
}

