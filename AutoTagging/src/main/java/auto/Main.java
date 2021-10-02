package auto;

import auto.files.Tokenizer;
import auto.matchers.ParseMatchers;
import auto.tagging.Driver;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Invalid usage: auto <ruleset-path> <directory>");
			System.out.println("               auto --tokenize <fileName>");
			System.exit(2);
		}

		if ("--tokenize".equals(args[0])) {
			try {
				Tokenizer.TokenizeFile(args[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;
		}

		JSONObject root = null;

		try {
			root = new JSONObject(Files.readString(Path.of(args[0])));
		} catch (IOException e) {
			System.out.println("Invalid ruleset file");
			System.exit(3);
		}

		var matcherSet = ParseMatchers.parseMatchers(root);

		try {
			Driver.TagDirectory(args[1], "out", matcherSet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
