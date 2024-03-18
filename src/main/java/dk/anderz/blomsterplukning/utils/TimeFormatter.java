package dk.anderz.blomsterplukning.utils;

public class TimeFormatter {
    public static String formatTime(Integer seconds){
        int days = (int) Math.floor(seconds/86400);
        int hours = (int) Math.floor((seconds - (days * 86400)) / 3600);
        int minutes = (int) Math.floor((seconds - ((days * 86400) + (hours*3600))) / 60);
        int sekunder = (int) Math.floor(seconds - ((days * 86400) + (hours * 3600) + (minutes * 60)));
        if (days <= 0){
            return hours + "t, " + minutes + "m & " + sekunder + "s";
        }
        if (hours <= 0){
            return minutes + "m & " + sekunder + "s";
        }
        if (minutes <= 0){
            return sekunder + "s";
        }
        return days + "d, " + hours + "t, " + minutes + "m & " + sekunder + "s";
    }

    public static String secToMin(Integer seconds){
        int min = (int) Math.floor(seconds/60);
        int sec = (int) Math.floor(seconds - min*60);
        return min + "m, " + sec + "s";
    }

    public static Double formatMilli(Double milli){
        if (milli < 0){
            return Double.valueOf((milli-milli-milli)/1000);
        } else {
            return Double.valueOf(milli/1000);
        }
    }
}
