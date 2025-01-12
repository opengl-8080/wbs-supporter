package wbs.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ConfigBean {
    public BigDecimal firstReviewRate;
    public List<LocalDate> holidays;
    public Map<String, MemberBean> members;
}
