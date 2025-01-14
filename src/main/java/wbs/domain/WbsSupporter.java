package wbs.domain;

import wbs.domain.config.Config;
import wbs.domain.date.CalendarDate;
import wbs.domain.firstreview.FirstReviewEndDate;
import wbs.domain.firstreview.FirstReviewManHour;
import wbs.domain.firstreview.FirstReviewStartDate;
import wbs.domain.member.AverageOfAvailabilityInOrg;
import wbs.domain.member.Member;
import wbs.domain.member.MemberName;
import wbs.domain.member.Members;
import wbs.domain.secondreview.SecondReviewEndDate;
import wbs.domain.secondreview.SecondReviewManHour;
import wbs.domain.secondreview.SecondReviewStartDate;
import wbs.domain.task.TaskEndDate;
import wbs.domain.task.TaskManHour;
import wbs.domain.task.TaskStartDate;
import wbs.domain.task.TotalManHour;

public class WbsSupporter {
    private final Config config;
    private final EndDateCalculator endDate = new EndDateCalculator();

    public WbsSupporter(Config config) {
        this.config = config;
    }

    public Result execute(
            TotalManHour totalManHour,
            MemberName taskMemberName,
            TaskStartDate taskStartDate,
            SecondReviewManHour secondReviewManHour,
            MemberName secondReviewMemberName) {
        // 作成終了日
        FirstReviewManHour firstReviewManHour = totalManHour.multiply(config.getFirstReviewRate());
        TaskManHour taskManHour = totalManHour.minus(firstReviewManHour);

        Members members = config.getMembers();
        Member taskMember = members.get(taskMemberName);

        CalendarDate startDate = new CalendarDate(taskStartDate.value(), config.getHolidays(), taskMember.getVacations());

        CalendarDate taskEndDate = endDate.calculate(
            taskManHour,
            config.getLeadTimeRate(),
            startDate,
            taskMember.getAvailabilityRate()
        );

        // 1次レビュー開始日
        CalendarDate firstReviewStartDate = taskEndDate.nextAvailabilityDate(members.getOrganizationVacations(taskMemberName));

        // 1次レビュー終了日
        AverageOfAvailabilityInOrg averageOfAvailabilityInOrg = members.averageOfAvailabilityInOrg(taskMemberName);
        CalendarDate firstReviewEndDate = endDate.calculate(
            firstReviewManHour,
            config.getLeadTimeRate(),
            firstReviewStartDate,
            averageOfAvailabilityInOrg
        );

        // 2次レビュー開始日
        Member secondReviewMember = members.get(secondReviewMemberName);
        CalendarDate secondReviewStartDate = firstReviewEndDate.nextAvailabilityDate(secondReviewMember.getVacations());

        // 2次レビュー終了日
        CalendarDate secondReviewEndDate = endDate.calculate(
            secondReviewManHour,
            config.getLeadTimeRate(),
            secondReviewStartDate,
            secondReviewMember.getAvailabilityRate()
        );

        return new Result(
            new TaskEndDate(taskEndDate.value()),
            new FirstReviewStartDate(firstReviewStartDate.value()),
            new FirstReviewEndDate(firstReviewEndDate.value()),
            new SecondReviewStartDate(secondReviewStartDate.value()),
            new SecondReviewEndDate(secondReviewEndDate.value())
        );
    }

}
