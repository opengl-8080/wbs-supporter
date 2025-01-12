package wbs.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wbs.domain.date.CalendarDate;
import wbs.domain.date.Holiday;
import wbs.domain.member.Vacation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EndDateCalculatorTest {

    EndDateCalculator sut = new EndDateCalculator();

    @Test
    @DisplayName("残工数が0未満になるケース")
    void test() {
        /*
         * 実稼働率: 0.4
         * 必要工数: 1.5
         *
         * 1/10(金) 0.4 (0.4) ※開始日
         * 1/11(土) -
         * 1/12(日) -
         * 1/13(月) - ※祝日
         * 1/14(火) 0.4 (0.8)
         * 1/15(水) - ※休暇
         * 1/16(木) 0.4 (1.2)
         * 1/17(金) 0.4 (1.6) ※終了日
         */

        ManHour manHour = () -> new BigDecimal("1.5");
        CalendarDate startDate = new CalendarDate(
                LocalDate.parse("2025-01-10"),
                List.of(new Holiday("2025-01-13")),
                List.of(new Vacation("2025-01-15")));
        AvailabilityRate availabilityRate = () -> new BigDecimal("0.40");

        CalendarDate endDate = sut.calculate(
            manHour,
            startDate,
            availabilityRate
        );

        assertThat(endDate.value()).isEqualTo("2025-01-17");
    }

    @Test
    @DisplayName("残工数が0丁度になるケース")
    void test2() {
        /*
         * 実稼働率: 0.5
         * 必要工数: 1.0
         *
         * 1/14(火) 0.5 (0.5) ※終了日
         * 1/15(水) 0.5 (1.0) ※終了日
         */

        ManHour manHour = () -> new BigDecimal("1.0");
        CalendarDate startDate = new CalendarDate(
                LocalDate.parse("2025-01-14"),
                List.of(),
                List.of());
        AvailabilityRate availabilityRate = () -> new BigDecimal("0.50");

        CalendarDate endDate = sut.calculate(
            manHour,
            startDate,
            availabilityRate
        );

        assertThat(endDate.value()).isEqualTo("2025-01-15");
    }
}