package files;

import models.TaggedToken;
import org.apache.xpath.operations.String;
import org.json.JSONException;
import org.json.JSONObject;

import javax.json.JsonObject;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {
	private List<Path> jsonFiles;
	private int i;

	public FileScanner(String directory) throws IOException {
		jsonFiles = scanForJSONFiles(directory);
		i = 0;
	}

	private JSONObject nextFile() throws IOException {
		if (i >= jsonFiles.size()) {
			return null;
		}

		String text = Files.readString(jsonFiles.get(i));
		i++;

		JSONObject obj;
		try {
			obj = new JSONObject(text);
		} catch (JSONException exc) {
			return nextFile();
		}

		return obj;
	}

	public boolean isValidTrainingFile(JsonObject object) {

	}

	public List<TaggedToken> nextTokens() throws IOException {
		var nextFile = nextFile();
		if (nextFile == null) {
			return null;
		}

		var tokensJson = nextFile.optJSONArray("tokens");
		if (tokensJson == null) {
			return nextTokens();
		}

		List<TaggedToken> tokens = new ArrayList<>();
		for (Object obj : tokensJson) {
			if (obj instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) obj;

				tokens.add(new TaggedToken(jsonObj.optString("tag", null), jsonObj.optString("token", null)));
			}
		}

		if (!tokens.stream().allMatch(t -> t.tag != null)) {
			return nextTokens();
		}

		return tokens;
	}

	public static List<Path> scanForJSONFiles(String directory) throws IOException {
		List<Path> files = new ArrayList<>();

		Path start = Path.of(directory);
		Files.walkFileTree(start, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				if (file.toString().endsWith(".json")) {
					files.add(file);
				}

				return FileVisitResult.CONTINUE;
			}
		});

		return files;
	}
}
