package wbs.domain.task;

import java.time.LocalDate;

/**
 * 作成終了日.
 */
public record TaskEndDate(LocalDate value) {
    public TaskEndDate(String value) {
        this(LocalDate.parse(value));
    }
}
