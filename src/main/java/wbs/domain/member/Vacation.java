package wbs.domain.member;

import java.time.LocalDate;

/**
 * 休暇.
 */
public record Vacation(LocalDate value) {

    public Vacation(String value) {
        this(LocalDate.parse(value));
    }
}
