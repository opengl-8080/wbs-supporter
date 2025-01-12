package wbs.config;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import wbs.domain.config.Config;
import wbs.domain.date.Holiday;
import wbs.domain.firstreview.FirstReviewRate;
import wbs.domain.member.BasicAvailabilityRate;
import wbs.domain.member.Member;
import wbs.domain.member.MemberName;
import wbs.domain.member.Members;
import wbs.domain.member.Organization;
import wbs.domain.member.Vacation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigLoader {

    public Config load(InputStream in) {
        try (final Jsonb jsonb = JsonbBuilder.create()) {
            final ConfigBean configBean = jsonb.fromJson(in, ConfigBean.class);

            final Config config = new Config();

            config.setFirstReviewRate(new FirstReviewRate(configBean.firstReviewRate));
            config.setHolidays(configBean.holidays.stream().map(Holiday::new).toList());

            List<Member> members = new ArrayList<>();
            for (Map.Entry<String, MemberBean> entry : configBean.members.entrySet()) {
                final MemberBean memberBean = entry.getValue();
                final Member member = new Member(
                    new MemberName(entry.getKey()),
                    new Organization(memberBean.organization),
                    new BasicAvailabilityRate(memberBean.availabilityRate),
                    memberBean.vacations.stream().map(Vacation::new).toList()
                );
                members.add(member);
            }
            config.setMembers(new Members(members));

            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
