package wbs.domain;

import java.math.BigDecimal;

/**
 * リードタイム割合.
 */
public record LeadTimeRate(BigDecimal value) {

    public LeadTimeRate(String value) {
        this(new BigDecimal(value));
    }
}
