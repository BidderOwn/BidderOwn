package site.bidderown.server.base.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static String getRemainingTime(LocalDateTime current, LocalDateTime endTime){
        Duration duration = Duration.between( current, endTime);
        // 60 1분
        // 60 * 60 1시간  3600
        // 60 * 60 * 24 하루 86400
        long totalSeconds = duration.toSeconds();
        long day = totalSeconds / 86400;
        totalSeconds = totalSeconds % 86400;
        long hour = totalSeconds / 3600;
        totalSeconds = totalSeconds % 3600;
        long minute = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if(duration.toMinutes() < 1)
            return seconds + "초";
        else if (duration.toMinutes() < 60)
            return minute + "분 " + seconds + "초";
        else if (duration.toMinutes() < 1440)
            return hour + "시간 " + minute + "분 ";
        else
            return day + "일 " + hour + "시간 " + minute + "분 ";

    }
    public static String getRegistrationTime(LocalDateTime createdTime, LocalDateTime current){
        Duration duration = Duration.between(createdTime , current);
        if(duration.toMinutes() < 1)
        {
            return "방금 전";
        } else if (duration.toMinutes() < 60) {
            return  duration.toMinutes() + "분 전";
        } else if (duration.toMinutes() < 1440) {
            return duration.toHours() + "시간 전";
        }
        else
            return duration.toDays() + "일 전";
    }

    public static LocalDateTime setExpireAt(Integer period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plusNDay = now.plus(period, ChronoUnit.DAYS);
        return LocalDateTime.of(
                plusNDay.getYear(),
                plusNDay.getMonth(),
                plusNDay.getDayOfMonth(),
                plusNDay.getHour(),
                plusNDay.getMinute(),
                plusNDay.getSecond());
    }

    public static LocalDateTime getCurrentOClockPlus(int hour) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(hour).truncatedTo(ChronoUnit.HOURS);
    }

    public static LocalDateTime getCurrentOClock() {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0);
    }
}
