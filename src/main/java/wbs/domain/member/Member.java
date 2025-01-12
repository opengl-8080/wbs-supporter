package wbs.domain.member;

import java.util.ArrayList;
import java.util.List;

/**
 * 担当者.
 */
public class Member {
    private final MemberName name;
    private final Organization organization;
    private final BasicAvailabilityRate basicAvailabilityRate;
    private final List<Vacation> vacations;

    public Member(MemberName name, Organization organization, BasicAvailabilityRate basicAvailabilityRate, List<Vacation> vacations) {
        this.name = name;
        this.organization = organization;
        this.basicAvailabilityRate = basicAvailabilityRate;
        this.vacations = vacations;
    }

    public boolean isSameName(MemberName memberName) {
        return this.name.equals(memberName);
    }

    public boolean isSameOrganization(Member member) {
        return this.organization.equals(member.organization);
    }

    public BasicAvailabilityRate getAvailabilityRate() {
        return basicAvailabilityRate;
    }

    public List<Vacation> getVacations() {
        return new ArrayList<>(vacations);
    }

    public MemberName getName() {
        return name;
    }

    public Organization getOrganization() {
        return organization;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name=" + name +
                ", organization=" + organization +
                ", basicAvailabilityRate=" + basicAvailabilityRate +
                ", vacations=" + vacations +
                '}';
    }
}
