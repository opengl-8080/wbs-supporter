package wbs.domain;

import wbs.domain.firstreview.FirstReviewEndDate;
import wbs.domain.firstreview.FirstReviewStartDate;
import wbs.domain.secondreview.SecondReviewEndDate;
import wbs.domain.secondreview.SecondReviewStartDate;
import wbs.domain.task.TaskEndDate;

/**
 * 処理結果.
 */
public record Result(
    TaskEndDate taskEndDate,
    FirstReviewStartDate firstReviewStartDate,
    FirstReviewEndDate firstReviewEndDate,
    SecondReviewStartDate secondReviewStartDate,
    SecondReviewEndDate secondReviewEndDate
) {
}
