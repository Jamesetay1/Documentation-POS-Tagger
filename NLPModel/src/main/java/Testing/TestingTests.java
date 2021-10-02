package Testing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class TestingTests {

    public static void main(String[] args) throws IOException {
        String correctDir = "D:\\__SENIOR_DESIGN__\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\GradingData";
        String taggedDir = "D:\\__SENIOR_DESIGN__\\sd-may21-35-softwaredocumentationpos\\TrainingDir\\TaggingOut";
        TestResults results = JSONCompare.doTest(correctDir, taggedDir);

        String out = results.toString() + "\nMiss Summary\n";
        for(TestResults.Miss miss : results.GetMisses()){
            out += "{" + miss.token + "} missed " + miss.numMisses + " times.\n";
            out += "    " + miss.missTypes.toString() + "\n";
            out += "    " + miss.actualVsTagged.toString() + "\n";
        }
        System.out.println(out);
    }
}
