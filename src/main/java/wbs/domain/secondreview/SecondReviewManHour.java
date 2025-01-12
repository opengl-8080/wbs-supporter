package wbs.domain.secondreview;

import wbs.domain.ManHour;

import java.math.BigDecimal;

/**
 * 2次レビュー工数.
 * @param value
 */
public record SecondReviewManHour(BigDecimal value) implements ManHour {
    public SecondReviewManHour(String value) {
        this(new BigDecimal(value));
    }
}
