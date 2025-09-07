package service;

import java.time.Duration;
import java.time.LocalDateTime;

import model.Schedule;

public class ScheduleService {
	
    // 残り時間（分）を計算
    public static Duration timeUntil(Schedule schedule) {
        return Duration.between(LocalDateTime.now(), schedule.getDateTime());
    }

}
