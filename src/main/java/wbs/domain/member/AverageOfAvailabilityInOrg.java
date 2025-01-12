package wbs.domain.member;

import wbs.domain.AvailabilityRate;

import java.math.BigDecimal;

/**
 * 組織の平均稼働率.
 * @param value
 */
public record AverageOfAvailabilityInOrg(BigDecimal value) implements AvailabilityRate {
}
