package wbs.domain.date;

import java.time.LocalDate;

/**
 * 祝日.
 * @param value
 */
public record Holiday(LocalDate value) {
    public Holiday(String value) {
        this(LocalDate.parse(value));
    }
}
