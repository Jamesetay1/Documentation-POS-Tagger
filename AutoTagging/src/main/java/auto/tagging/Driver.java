package auto.tagging;

import auto.files.FileScanner;
import auto.models.TokenInput;
import auto.models.matchers.MatcherSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Driver {
	public static void TagDirectory(String directory, String outputDirectory, MatcherSet matcherSet) throws IOException {
		File outputFile = new File(outputDirectory);
		if (!outputFile.exists()) {
			outputFile.delete();
			outputFile.mkdir();
		}

		FileScanner scanner = new FileScanner(directory);

		List<TokenInput> tokens;
		while ((tokens = scanner.nextTokens()) != null) {
			var taggedTokens = Tagger.tag(matcherSet, tokens);

			JSONArray taggedTokensJson = new JSONArray(taggedTokens);

			JSONObject outputJson = new JSONObject();
			outputJson.put("type", "autotagged");
			outputJson.put("tokens", taggedTokensJson);

			Path outputPath = Path.of(outputDirectory, scanner.curPath().getFileName().toString());
			Files.writeString(outputPath, outputJson.toString(2), StandardOpenOption.CREATE);
		}
	}
}
