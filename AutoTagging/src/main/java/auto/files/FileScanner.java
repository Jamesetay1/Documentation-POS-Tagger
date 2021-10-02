package auto.files;

import auto.models.TaggedToken;
import auto.models.TokenInput;
import edu.stanford.nlp.simple.Token;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;
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

	public Path curPath() {
		return jsonFiles.get(i - 1);
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

		if ("tokens".equals(obj.getString("type"))) {
			return obj;
		} else {
			return nextFile();
		}
	}

	public List<TokenInput> nextTokens() throws IOException {
		var nextFile = nextFile();
		if (nextFile == null) {
			return null;
		}

		var tokensJson = nextFile.optJSONArray("tokens");
		if (tokensJson == null) {
			return nextTokens();
		}

		List<TokenInput> tokens = new ArrayList<>();
		for (Object obj : tokensJson) {
			if (obj instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) obj;

				tokens.add(new TokenInput(jsonObj.optString("token", null), jsonObj.optBoolean("code", false)));
			}
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
