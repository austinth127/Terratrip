package road.trip.util;

import org.springframework.boot.SpringApplication;
import road.trip.RoadTripApplication;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class UtilityFunctions {

    /**
     * Used to determine whether the time frame of a category overlaps with the time frame of a trip
     * @param categoryStartMonth
     * @param categoryEndMonth
     * @param tripStartDate
     * @param tripEndDate
     * @return
     */
    public static boolean fallsWithinTimeframe(Month categoryStartMonth, Month categoryEndMonth, LocalDate tripStartDate, LocalDate tripEndDate){
        Integer tripStart = tripStartDate.getMonthValue(), tripEnd = tripEndDate.getMonthValue();
        Integer categoryStart = categoryStartMonth.getValue(), categoryEnd = categoryEndMonth.getValue();
        Set<Integer> tripTimeFrame = new HashSet<>(), categoryTimeFrame = new HashSet<>();

        tripTimeFrame.add(tripStart);
        while(tripStart != tripEnd){
            tripStart = tripStart % 12 + 1;
            tripTimeFrame.add(tripStart);
        }
        categoryTimeFrame.add(categoryStart);
        while(categoryStart != categoryEnd){
            categoryStart = categoryStart % 12 + 1;
            categoryTimeFrame.add(categoryStart);
        }
        tripTimeFrame.retainAll(categoryTimeFrame);
        return !tripTimeFrame.isEmpty();
    }

    public static void main(String[] args) {
        Month categoryStart = Month.JUNE, categoryEnd = Month.JUNE;
        LocalDate tripStart = LocalDate.of(2020, 7, 7), tripEnd = LocalDate.of(2020, 7, 8);

        boolean intersects = fallsWithinTimeframe(categoryStart, categoryEnd, tripStart, tripEnd);
    }
}
