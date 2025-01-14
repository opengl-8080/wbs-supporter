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
         * リードタイム割合: 1.2
         * 実期間: 1.5 * 1.2 = 1.8
         *
         * 1/10(金) 0.4 (0.4) ※開始日
         * 1/11(土) -
         * 1/12(日) -
         * 1/13(月) - ※祝日
         * 1/14(火) 0.4 (0.8)
         * 1/15(水) - ※休暇
         * 1/16(木) 0.4 (1.2)
         * 1/17(金) 0.4 (1.6)
         * 1/18(土) -
         * 1/19(日) -
         * 1/20(月) 0.4 (2.0) ※終了日
         */

        ManHour manHour = () -> new BigDecimal("1.5");
        LeadTimeRate leadTimeRate = new LeadTimeRate("1.20");
        CalendarDate startDate = new CalendarDate(
                LocalDate.parse("2025-01-10"),
                List.of(new Holiday("2025-01-13")),
                List.of(new Vacation("2025-01-15")));
        AvailabilityRate availabilityRate = () -> new BigDecimal("0.40");

        CalendarDate endDate = sut.calculate(
            manHour,
            leadTimeRate,
            startDate,
            availabilityRate
        );

        assertThat(endDate.value()).isEqualTo("2025-01-20");
    }

    @Test
    @DisplayName("残工数が0丁度になるケース")
    void test2() {
        /*
         * 実稼働率: 0.6
         * 必要工数: 1.0
         * リードタイム割合: 1.2
         * 実期間: 1.0 * 1.2 = 1.2
         *
         * 1/14(火) 0.6 (0.6) ※開始日
         * 1/15(水) 0.6 (1.2) ※終了日
         */

        ManHour manHour = () -> new BigDecimal("1.0");
        LeadTimeRate leadTimeRate = new LeadTimeRate("1.2");
        CalendarDate startDate = new CalendarDate(
                LocalDate.parse("2025-01-14"),
                List.of(),
                List.of());
        AvailabilityRate availabilityRate = () -> new BigDecimal("0.60");

        CalendarDate endDate = sut.calculate(
            manHour,
            leadTimeRate,
            startDate,
            availabilityRate
        );

        assertThat(endDate.value()).isEqualTo("2025-01-15");
    }
}