package wbs.domain.task;

import wbs.domain.ManHour;

import java.math.BigDecimal;

/**
 * 作成工数.
 * @param value
 */
public record TaskManHour(BigDecimal value) implements ManHour {
    public TaskManHour(String value) {
        this(new BigDecimal(value));
    }
}
