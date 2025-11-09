package controller.division;


import java.time.temporal.ChronoUnit;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {
    
    // Lấy danh sách các ngày giữa hai mốc
    public static List<Date> getDatesBetween(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        long numOfDays = ChronoUnit.DAYS.between(start, end) + 1;
        
        for (int i = 0; i < numOfDays; i++) {
            LocalDate date = start.plusDays(i);
            dates.add(Date.valueOf(date));
        }
        return dates;
    }
    
    // Format ngày sang dd/MM
    public static String formatDate_dd_MM(Date date) {
        return date.toLocalDate().getDayOfMonth() + "/" + date.toLocalDate().getMonthValue();
    }
}