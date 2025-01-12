package wbs.domain.member;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Members {
    private final List<Member> members;

    public Members(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }

    /**
     * 指定された名前の担当者を取得する.
     * @param name 担当者名
     * @return 担当者
     */
    public Member get(MemberName name) {
        return members.stream()
                .filter(member -> member.isSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("指定された名前の担当者は存在しません > " + name.value()));
    }

    /**
     * 指定された名前の担当者が所属する組織の平均稼働率を計算する.
     * @param name 担当者名
     * @return 担当者が所属する組織の平均稼働率
     */
    public AverageOfAvailabilityInOrg averageOfAvailabilityInOrg(MemberName name) {
        final Member target = get(name);
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (Member member : members) {
            if (member.isSameOrganization(target)) {
                total = total.add(member.getAvailabilityRate().value());
                count++;
            }
        }

        return new AverageOfAvailabilityInOrg(total.divide(new BigDecimal(count), RoundingMode.HALF_UP));
    }

    /**
     * 指定した名前の担当者が所属する組織のメンバーが全員休暇を取る日を一覧で取得する.
     * @param name 担当者名
     * @return 全員が休暇する日
     */
    public List<Vacation> getOrganizationVacations(MemberName name) {
        Member target = get(name);
        Map<Vacation, Integer> map = new HashMap<>();

        int count = 0;
        for (Member member : members) {
            if (member.isSameOrganization(target)) {
                count++;
                for (Vacation vacation : member.getVacations()) {
                    Integer sum = map.getOrDefault(vacation, 0);
                    map.put(vacation, sum+1);
                }
            }
        }

        List<Vacation> vacations = new ArrayList<>();

        for (Map.Entry<Vacation, Integer> entry : map.entrySet()) {
            if (entry.getValue() == count) {
                vacations.add(entry.getKey());
            }
        }

        return vacations;
    }
}
