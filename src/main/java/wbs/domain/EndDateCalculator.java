package wbs.domain;

import wbs.domain.date.CalendarDate;

import java.math.BigDecimal;

/**
 * 終了日の計算を行うクラス.
 */
public class EndDateCalculator {

    public CalendarDate calculate(
            ManHour manHour,
            LeadTimeRate leadTimeRate,
            CalendarDate startDate,
            AvailabilityRate availabilityRate) {

        // 残日数
        BigDecimal restDays = manHour.leadTime(leadTimeRate);
        CalendarDate currentDate = startDate.previousDate();
        while (BigDecimal.ZERO.compareTo(restDays) < 0) { // 残日数が 0 を切るまで繰り返す
            currentDate = currentDate.nextAvailabilityDate();
            restDays = restDays.add(minus(availabilityRate.value()));
        }

        return currentDate;
    }

    private BigDecimal minus(BigDecimal value) {
        return value.multiply(new BigDecimal("-1"));
    }
}
