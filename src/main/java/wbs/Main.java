package wbs;

import wbs.config.ConfigLoader;
import wbs.domain.Result;
import wbs.domain.WbsSupporter;
import wbs.domain.config.Config;
import wbs.domain.member.MemberName;
import wbs.domain.secondreview.SecondReviewManHour;
import wbs.domain.task.TaskStartDate;
import wbs.domain.task.TotalManHour;

import java.io.Console;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new RuntimeException("設定ファイルを引数で指定してください");
        }
        final ConfigLoader configLoader = new ConfigLoader();
        final Config config;
        try (final FileInputStream in = new FileInputStream(args[0])) {
            config = configLoader.load(in);
        }

        final WbsSupporter wbsSupporter = new WbsSupporter(config);

        final Console console = System.console();
        while (true) {
            try {
                final String line = console.readLine("> ");
                if (line.isEmpty() || line.isBlank()) {
                    // から入力の場合はスキップ
                    continue;
                } else if ("exit".equals(line)) {
                    break;
                }
                
                final String[] tokens = line.split(",");

                final TotalManHour totalManHour = new TotalManHour(new BigDecimal(tokens[0]));
                final MemberName taskMemberName = new MemberName(tokens[1]);
                final TaskStartDate startDate = new TaskStartDate(LocalDate.parse(tokens[2]));
                final SecondReviewManHour secondReviewManHour = new SecondReviewManHour(new BigDecimal(tokens[3]));
                final MemberName secondReviewerName = new MemberName(tokens[4]);

                final Result result = wbsSupporter.execute(
                    totalManHour,
                    taskMemberName,
                    startDate,
                    secondReviewManHour,
                    secondReviewerName
                );

                System.out.printf("%s,%s,%s,%s,%s%n",
                    result.taskEndDate().value(),
                    result.firstReviewStartDate().value(),
                    result.firstReviewEndDate().value(),
                    result.secondReviewStartDate().value(),
                    result.secondReviewEndDate().value()
                );
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
