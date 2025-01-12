package wbs.domain.task;

import wbs.domain.firstreview.FirstReviewRate;
import wbs.domain.firstreview.FirstReviewManHour;

import java.math.BigDecimal;

/**
 * 全体工数
 */
public record TotalManHour(BigDecimal value) {

    public TotalManHour(String value) {
        this(new BigDecimal(value));
    }

    /**
     * 全体工数に1次レビュー工数割合を掛けて1次レビューの工数を算出する.
     * @param firstReviewRate 1次レビュー工数割合
     * @return 1次レビュー工数
     */
    public FirstReviewManHour multiply(FirstReviewRate firstReviewRate) {
        return new FirstReviewManHour(value.multiply(firstReviewRate.value()));
    }

    /**
     * 全体工数から1次レビュー工数を減算して作成工数を算出する.
     * @param firstReviewManHour 1次レビュー工数
     * @return 作成工数
     */
    public TaskManHour minus(FirstReviewManHour firstReviewManHour) {
        return new TaskManHour(value.add(firstReviewManHour.value().multiply(new BigDecimal("-1"))));
    }
}
