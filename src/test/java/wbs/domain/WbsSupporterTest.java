package wbs.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wbs.domain.config.Config;
import wbs.domain.date.Holiday;
import wbs.domain.member.BasicAvailabilityRate;
import wbs.domain.firstreview.FirstReviewRate;
import wbs.domain.member.Member;
import wbs.domain.member.MemberName;
import wbs.domain.member.Members;
import wbs.domain.member.Organization;
import wbs.domain.member.Vacation;
import wbs.domain.task.TaskStartDate;
import wbs.domain.secondreview.SecondReviewManHour;
import wbs.domain.task.TotalManHour;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WbsSupporterTest {
    Config config = new Config();

    @Test
    @DisplayName("基本形")
    void test1() {
        /*
         * Taroの稼働率: 0.4
         * 1次レビュー割合: 0.2
         * リードタイム割合: 1.2
         * 全体工数: 2.0
         * 
         * 1次レビュー日数: 2.0 * 0.2 * 1.2 = 0.48
         * 作成日数 2.0 * 0.8 * 1.2 = 1.92
         * 
         * 組織Aの平均稼働率: (0.4 + 0.9) / 2 = 0.65
         * 
         * 2次レビュアー稼働率: 0.8
         * 2次レビュー工数: 1.5
         * 2次レビューリードタイム込み日数: 1.5 * 1.2 = 1.8
         *
         * 1/10(金) 0.4(0.4) ※作成開始日
         * 1/11(土) -
         * 1/12(日) -
         * 1/13(月) - ※祝日
         * 1/14(火) - ※Taro休暇日
         * 1/15(水) 0.4(0.8)
         * 1/16(木) 0.4(1.2)
         * 1/17(金) 0.4(1.6)
         * 1/18(土) -
         * 1/19(日) -
         * 1/20(月) 0.4(2.0) ※作成終了日
         * 1/21(火) 0.65(0.65) ※1次レビュー開始日, 1次レビュー終了日
         * 1/22(水) 0.8(0.8) ※2次レビュー開始日
         * 1/23(木) 0.8(1.6)
         * 1/24(金) 0.8(2.4) ※2次レビュー終了日
         */
        config.setHolidays(List.of(new Holiday("2025-01-13")));
        config.setFirstReviewRate(new FirstReviewRate("0.2"));
        config.setLeadTimeRate(new LeadTimeRate("1.2"));

        Member taro = new Member(
            new MemberName("Taro"),
            new Organization("A"),
            new BasicAvailabilityRate("0.40"),
            List.of(new Vacation("2025-01-14"))
        );
        Member jiro = new Member(
            new MemberName("Jiro"),
            new Organization("A"),
            new BasicAvailabilityRate("0.90"),
            List.of()
        );
        Member mike = new Member(
            new MemberName("Mike"),
            new Organization("B"),
            new BasicAvailabilityRate("0.80"),
            List.of()
        );
        Members members = new Members(List.of(taro, jiro, mike));
        config.setMembers(members);
        WbsSupporter sut = new WbsSupporter(config);

        TotalManHour totalManHour = new TotalManHour("2.0");
        MemberName taskMember = new MemberName("Taro");
        TaskStartDate taskStartDate = new TaskStartDate("2025-01-10");
        SecondReviewManHour secondReviewManHour = new SecondReviewManHour("1.5");
        MemberName secondReviewMember = new MemberName("Mike");

        Result result = sut.execute(totalManHour, taskMember, taskStartDate, secondReviewManHour, secondReviewMember);

        assertThat(result.taskEndDate().value()).isEqualTo("2025-01-20");
        assertThat(result.firstReviewStartDate().value()).isEqualTo("2025-01-21");
        assertThat(result.firstReviewEndDate().value()).isEqualTo("2025-01-21");
        assertThat(result.secondReviewStartDate().value()).isEqualTo("2025-01-22");
        assertThat(result.secondReviewEndDate().value()).isEqualTo("2025-01-24");
    }

    @Test
    @DisplayName("1次レビューの休暇考慮")
    void test2() {
        /*
         * Taroの稼働率: 0.9
         * 1次レビュー割合: 0.2
         * リードタイム割合: 1.2
         * 全体工数: 4.5
         * 
         * 1次レビュー工数: 4.5 * 0.2 * 1.2 = 1.08
         * 作成工数: 4.5 * 0.8 * 1.2 = 4.32
         * 
         * 組織Aの平均稼働率: (0.9 + 0.3) / 2 = 0.6
         *
         * 2次レビュアー稼働率: 1.0
         * 2次レビュー工数: 0.8
         * 2次レビューリードタイム込み日数: 0.8 * 1.2 = 0.98
         *
         * 1/10(金) 0.9(0.9) ※作成開始日
         * 1/11(土) -
         * 1/12(日) -
         * 1/13(月) - ※祝日
         * 1/14(火) 0.9(1.8)
         * 1/15(水) 0.9(2.7)
         * 1/16(木) 0.9(3.6)
         * 1/17(金) 0.9(4.5) ※作成完了日
         * 1/18(土) -
         * 1/19(日) -
         * 1/20(月) 0.6(0.6),Taro休暇 ※1次レビュー開始日
         * 1/21(火) - ※Jiro,Taro休暇
         * 1/22(水) 0.6(1.2) ※1次レビュー終了日
         * 1/23(木) 1.0(1.0) ※2次レビュー開始日, 2次レビュー終了日
         */
        config.setHolidays(List.of(new Holiday("2025-01-13")));
        config.setFirstReviewRate(new FirstReviewRate("0.2"));
        config.setLeadTimeRate(new LeadTimeRate("1.2"));

        Member taro = new Member(
                new MemberName("Taro"),
                new Organization("A"),
                new BasicAvailabilityRate("0.90"),
                List.of(new Vacation("2025-01-20"), new Vacation("2025-01-21"))
        );
        Member jiro = new Member(
                new MemberName("Jiro"),
                new Organization("A"),
                new BasicAvailabilityRate("0.30"),
                List.of(new Vacation("2025-01-21"))
        );
        Member mike = new Member(
                new MemberName("Mike"),
                new Organization("B"),
                new BasicAvailabilityRate("1.00"),
                List.of()
        );
        Members members = new Members(List.of(taro, jiro, mike));
        config.setMembers(members);
        WbsSupporter sut = new WbsSupporter(config);

        TotalManHour totalManHour = new TotalManHour("4.50");
        MemberName taskMember = new MemberName("Taro");
        TaskStartDate taskStartDate = new TaskStartDate("2025-01-10");
        SecondReviewManHour secondReviewManHour = new SecondReviewManHour("0.80");
        MemberName secondReviewMember = new MemberName("Mike");

        Result result = sut.execute(totalManHour, taskMember, taskStartDate, secondReviewManHour, secondReviewMember);

        assertThat(result.taskEndDate().value()).isEqualTo("2025-01-17");
        assertThat(result.firstReviewStartDate().value()).isEqualTo("2025-01-20");
        assertThat(result.firstReviewEndDate().value()).isEqualTo("2025-01-22");
        assertThat(result.secondReviewStartDate().value()).isEqualTo("2025-01-23");
        assertThat(result.secondReviewEndDate().value()).isEqualTo("2025-01-23");
    }

    @Test
    @DisplayName("2次レビューの休暇考慮")
    void test3() {
        /*
         * Taroの稼働率: 1.0
         * 1次レビュー割合: 0.2
         * リードタイム割合: 1.2
         * 全体工数: 0.8
         * 
         * 1次レビュー工数: 0.8 * 0.2 * 1.2 = 0.19
         * 作成工数: 0.8 * 0.8 * 1.2 = 0.77
         * 
         * 組織Aの平均稼働率: (1.0 + 0.5) / 2 = 0.75
         *
         * 2次レビュアー稼働率: 0.5
         * 2次レビュー工数: 0.8
         * 2次レビューリードタイム込み日数: 0.8 * 1.2 = 0.98
         *
         * 1/10(金) 1.0(1.0) ※作成完了日
         * 1/11(土) -
         * 1/12(日) -
         * 1/13(月) - ※祝日
         * 1/14(火) 0.75(0.75) ※1次レビュー開始日, 1次レビュー終了日
         * 1/15(水) 0.5(0.5) ※2次レビュー開始日
         * 1/16(木) - ※Mike休暇日
         * 1/17(金) 0.5(1.0) ※2次レビュー終了日
         */
        config.setHolidays(List.of(new Holiday("2025-01-13")));
        config.setFirstReviewRate(new FirstReviewRate("0.2"));
        config.setLeadTimeRate(new LeadTimeRate("1.2"));

        Member taro = new Member(
                new MemberName("Taro"),
                new Organization("A"),
                new BasicAvailabilityRate("1.00"),
                List.of()
        );
        Member jiro = new Member(
                new MemberName("Jiro"),
                new Organization("A"),
                new BasicAvailabilityRate("0.50"),
                List.of()
        );
        Member mike = new Member(
                new MemberName("Mike"),
                new Organization("B"),
                new BasicAvailabilityRate("0.50"),
                List.of(new Vacation("2025-01-16"))
        );
        Members members = new Members(List.of(taro, jiro, mike));
        config.setMembers(members);
        WbsSupporter sut = new WbsSupporter(config);

        TotalManHour totalManHour = new TotalManHour("0.80");
        MemberName taskMember = new MemberName("Taro");
        TaskStartDate taskStartDate = new TaskStartDate("2025-01-10");
        SecondReviewManHour secondReviewManHour = new SecondReviewManHour("0.80");
        MemberName secondReviewMember = new MemberName("Mike");

        Result result = sut.execute(totalManHour, taskMember, taskStartDate, secondReviewManHour, secondReviewMember);

        assertThat(result.taskEndDate().value()).isEqualTo("2025-01-10");
        assertThat(result.firstReviewStartDate().value()).isEqualTo("2025-01-14");
        assertThat(result.firstReviewEndDate().value()).isEqualTo("2025-01-14");
        assertThat(result.secondReviewStartDate().value()).isEqualTo("2025-01-15");
        assertThat(result.secondReviewEndDate().value()).isEqualTo("2025-01-17");
    }
}