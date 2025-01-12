package wbs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wbs.config.ConfigLoader;
import wbs.domain.config.Config;
import wbs.domain.date.Holiday;
import wbs.domain.firstreview.FirstReviewRate;
import wbs.domain.member.BasicAvailabilityRate;
import wbs.domain.member.Member;
import wbs.domain.member.MemberName;
import wbs.domain.member.Members;
import wbs.domain.member.Organization;
import wbs.domain.member.Vacation;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigLoaderTest {

    final ConfigLoader sut = new ConfigLoader();

    @Test
    @DisplayName("JSONをパースして設定を読み込める")
    void test1() throws Exception {
        String json = """
            {
              "holidays": [
                "2025-01-13", "2025-02-11", "2025-02-24"
              ],
              "firstReviewRate": 0.20,
              "members": {
                "Taro": {
                  "organization": "A",
                  "vacations": [
                    "2025-01-24", "2025-01-27"
                  ],
                  "availabilityRate": 0.50
                },
                "Jiro": {
                  "organization": "A",
                  "vacations": [],
                  "availabilityRate": 1.00
                },
                "Mike": {
                  "organization": "B",
                  "vacations": [
                    "2025-02-03"
                  ],
                  "availabilityRate": 0.80
                }
              }
            }
        """;

        try (final ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            final Config result = sut.load(in);

            assertThat(result.getHolidays()).containsExactly(
                new Holiday("2025-01-13"),
                new Holiday("2025-02-11"),
                new Holiday("2025-02-24")
            );

            assertThat(result.getFirstReviewRate()).isEqualTo(new FirstReviewRate("0.20"));

            assertThat(result.getMembers()).usingRecursiveComparison()
                    .ignoringCollectionOrder()
                    .isEqualTo(new Members(List.of(
                        new Member(
                            new MemberName("Taro"),
                            new Organization("A"),
                            new BasicAvailabilityRate("0.50"),
                            List.of(
                                new Vacation("2025-01-24"),
                                new Vacation("2025-01-27")
                            )
                        ),
                        new Member(
                            new MemberName("Jiro"),
                            new Organization("A"),
                            new BasicAvailabilityRate("1.00"),
                            List.of()
                        ),
                        new Member(
                            new MemberName("Mike"),
                            new Organization("B"),
                            new BasicAvailabilityRate("0.80"),
                            List.of(
                                new Vacation("2025-02-03")
                            )
                        )
                    )));
        }
    }
}