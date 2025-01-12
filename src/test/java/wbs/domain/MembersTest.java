package wbs.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wbs.domain.member.BasicAvailabilityRate;
import wbs.domain.member.AverageOfAvailabilityInOrg;
import wbs.domain.member.Member;
import wbs.domain.member.MemberName;
import wbs.domain.member.Members;
import wbs.domain.member.Organization;
import wbs.domain.member.Vacation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MembersTest {

    @Test
    @DisplayName("指定された名前の担当者を取得できる")
    void test1() {
        final Member foo = new Member(
                new MemberName("Foo"),
                new Organization("A"),
                new BasicAvailabilityRate("1.0"),
                List.of()
        );

        final Member bar = new Member(
                new MemberName("Bar"),
                new Organization("A"),
                new BasicAvailabilityRate("1.0"),
                List.of()
        );

        Members sut = new Members(List.of(foo, bar));

        Member result = sut.get(new MemberName("Bar"));

        assertThat(result).isSameAs(bar);
    }

    @Test
    @DisplayName("指定された名前の担当者が所属する組織の平均稼働率を取得する")
    void test2() {
        final Member foo = new Member(
            new MemberName("Foo"),
            new Organization("A"),
            new BasicAvailabilityRate("1.00"),
            List.of()
        );

        final Member bar = new Member(
            new MemberName("Bar"),
            new Organization("A"),
            new BasicAvailabilityRate("0.50"),
            List.of()
        );

        final Member fizz = new Member(
            new MemberName("Fizz"),
            new Organization("B"),
            new BasicAvailabilityRate("0.50"),
            List.of()
        );

        Members sut = new Members(List.of(foo, bar, fizz));

        AverageOfAvailabilityInOrg result = sut.averageOfAvailabilityInOrg(new MemberName("Bar"));

        assertThat(result.value()).isEqualTo("0.75");
    }

    @Test
    @DisplayName("指定した担当者が所属する組織が全員休む休暇リストを取得できる")
    void test3() {
        final Member foo = new Member(
            new MemberName("Foo"),
            new Organization("A"),
            new BasicAvailabilityRate("1.00"),
            List.of(
                new Vacation("2025-01-15"),
                new Vacation("2025-01-16"),
                new Vacation("2025-01-18")
            )
        );

        final Member bar = new Member(
            new MemberName("Bar"),
            new Organization("A"),
            new BasicAvailabilityRate("0.50"),
            List.of(
                new Vacation("2025-01-16"),
                new Vacation("2025-01-17"),
                new Vacation("2025-01-18")
            )
        );

        final Member fizz = new Member(
            new MemberName("Fizz"),
            new Organization("B"),
            new BasicAvailabilityRate("0.50"),
            List.of()
        );

        Members sut = new Members(List.of(foo, bar, fizz));

        List<Vacation> vacations = sut.getOrganizationVacations(new MemberName("Foo"));

        assertThat(vacations).containsExactlyInAnyOrder(
            new Vacation("2025-01-16"),
            new Vacation("2025-01-18")
        );
    }
}