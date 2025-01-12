package wbs.domain.member;

import wbs.domain.AvailabilityRate;

import java.math.BigDecimal;

/**
 * 基本稼働率.
 * @param value
 */
public record BasicAvailabilityRate(BigDecimal value) implements AvailabilityRate {
    public BasicAvailabilityRate(String value) {
        this(new BigDecimal(value));
    }
}
