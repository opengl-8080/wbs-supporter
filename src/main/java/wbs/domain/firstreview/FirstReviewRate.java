package wbs.domain.firstreview;

import java.math.BigDecimal;

/**
 * 1次レビューの工数割合.
 * @param value
 */
public record FirstReviewRate(BigDecimal value) {
    public FirstReviewRate(String value) {
        this(new BigDecimal(value));
    }
}
