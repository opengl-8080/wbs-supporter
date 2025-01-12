package wbs.domain.date;

import wbs.domain.member.Vacation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * 日付.
 * @param value
 * @param holidays
 * @param vacations
 */
public record CalendarDate(
    LocalDate value,
    List<Holiday> holidays,
    List<Vacation> vacations
) {
    public CalendarDate(String value,
                        List<Holiday> holidays,
                        List<Vacation> vacations) {
        this(LocalDate.parse(value), holidays, vacations);
    }

    /**
     * 次の稼働日を取得する.
     * @return 翌稼働日
     */
    public CalendarDate nextAvailabilityDate() {
        LocalDate result = value.plusDays(1);
        while (isSaturday(result) || isSunday(result) || isHoliday(result) || isVacation(result)) {
            result = result.plusDays(1);
        }

        return new CalendarDate(result, holidays, vacations);
    }

    /**
     * 休暇一覧を指定されたものに差し替えたうえで次の稼働日を取得する.
     * @param vacations 休暇一覧
     * @return 翌稼働日
     */
    public CalendarDate nextAvailabilityDate(List<Vacation> vacations) {
        CalendarDate date = new CalendarDate(value, holidays, vacations);
        return date.nextAvailabilityDate();
    }

    /**
     * 前日を取得する.
     * @return 前日
     */
    public CalendarDate previousDate() {
        return new CalendarDate(value.plusDays(-1), holidays, vacations);
    }

    private boolean isSaturday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }

    private boolean isSunday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private boolean isHoliday(LocalDate date) {
        return holidays.stream()
                .anyMatch(holiday -> holiday.value().isEqual(date));
    }

    private boolean isVacation(LocalDate date) {
        return vacations.stream()
                .anyMatch(vacation -> vacation.value().isEqual(date));
    }
}
