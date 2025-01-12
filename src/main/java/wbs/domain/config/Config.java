package wbs.domain.config;

import wbs.domain.date.Holiday;
import wbs.domain.firstreview.FirstReviewRate;
import wbs.domain.member.Members;

import java.util.List;

public class Config {

    private List<Holiday> holidays;
    private FirstReviewRate firstReviewRate;
    private Members members;

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public FirstReviewRate getFirstReviewRate() {
        return firstReviewRate;
    }

    public void setFirstReviewRate(FirstReviewRate firstReviewRate) {
        this.firstReviewRate = firstReviewRate;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }
}
