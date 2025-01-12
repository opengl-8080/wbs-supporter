package wbs.domain;

import wbs.domain.date.CalendarDate;

import java.math.BigDecimal;

/**
 * 終了日の計算を行うクラス.
 */
public class EndDateCalculator {

    public CalendarDate calculate(
            ManHour manHour,
            CalendarDate startDate,
            AvailabilityRate availabilityRate) {

        // 残工数
        BigDecimal restManHour = manHour.value();
        CalendarDate currentDate = startDate.previousDate();
        while (BigDecimal.ZERO.compareTo(restManHour) < 0) { // 残工数が 0 を切るまで繰り返す
            currentDate = currentDate.nextAvailabilityDate();
            restManHour = restManHour.add(minus(availabilityRate.value()));
        }

        return currentDate;
    }

    private BigDecimal minus(BigDecimal value) {
        return value.multiply(new BigDecimal("-1"));
    }
}
