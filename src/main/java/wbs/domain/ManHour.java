package wbs.domain;

import java.math.BigDecimal;

/**
 * 工数(人日).
 */
public interface ManHour {
    BigDecimal value();

    /**
     * リードタイムを考慮した日数を計算する.
     * @param leadTimeRate リードタイム割合
     * @return リードタイムを考慮した日数
     */
    default BigDecimal leadTime(LeadTimeRate leadTimeRate) {
        return value().multiply(leadTimeRate.value());
    }
}
