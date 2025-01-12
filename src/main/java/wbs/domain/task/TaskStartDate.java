package wbs.domain.task;

import java.time.LocalDate;

/**
 * 作成開始日.
 * @param value
 */
public record TaskStartDate(LocalDate value) {
    public TaskStartDate(String value) {
        this(LocalDate.parse(value));
    }
}
