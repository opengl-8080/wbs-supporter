package wbs.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wbs.domain.date.CalendarDate;
import wbs.domain.date.Holiday;
import wbs.domain.member.Vacation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarDateTest {

    @Test
    @DisplayName("土日を飛ばして翌営業日を取得できる")
    void test1() {
        CalendarDate sut = new CalendarDate("2025-01-10", List.of(), List.of());

        CalendarDate result = sut.nextAvailabilityDate();

        assertThat(result.value()).isEqualTo("2025-01-13");
    }

    @Test
    @DisplayName("祝日を飛ばして翌営業日を取得できる")
    void test2() {
        CalendarDate sut = new CalendarDate("2025-01-10", List.of(
                new Holiday("2025-01-13")
        ), List.of());

        CalendarDate result = sut.nextAvailabilityDate();

        assertThat(result.value()).isEqualTo("2025-01-14");
    }

    @Test
    @DisplayName("休暇を飛ばして翌営業日を取得できる")
    void test3() {
        CalendarDate sut = new CalendarDate("2025-01-10", List.of(
                new Holiday("2025-01-13")
        ), List.of(
                new Vacation("2025-01-14")
        ));

        CalendarDate result = sut.nextAvailabilityDate();

        assertThat(result.value()).isEqualTo("2025-01-15");
    }

    @Test
    @DisplayName("単純な前日を取得できる")
    void test4() {
        CalendarDate sut = new CalendarDate("2025-01-06", List.of(), List.of());

        CalendarDate result = sut.previousDate();

        assertThat(result.value()).isEqualTo("2025-01-05");
    }
}