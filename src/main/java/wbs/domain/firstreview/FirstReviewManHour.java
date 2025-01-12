package wbs.domain.firstreview;

import wbs.domain.ManHour;

import java.math.BigDecimal;

/**
 * 1次レビュー工数.
 * @param value
 */
public record FirstReviewManHour(BigDecimal value) implements ManHour {

}
