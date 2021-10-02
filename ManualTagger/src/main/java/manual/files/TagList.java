package manual.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TagList {
	private static final String TAG_LIST_NAME = "tags.json";

	private List<String> tags;

	public TagList() {
		this.tags = new ArrayList<>();
	}

	public TagList(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getTags() {
		return tags;
	}

	public boolean addTag(String tag) {
		if (tags.contains(tag)) {
			return false;
		}

		tags.add(tag);
		return true;
	}

	public String[] startsWith(String search) {
		return tags.stream().filter(tag -> tag.startsWith(search)).collect(Collectors.toList()).toArray(new String[] {});
	}

	public void save() throws IOException {
		StringBuilder tagsString = new StringBuilder();

		for (int i = 0; i < tags.size(); i++) {
			tagsString.append(tags.get(i));

			if (i + 1 < tags.size()) {
				tagsString.append('\n');
			}
		}

		Files.writeString(Path.of(TAG_LIST_NAME), tagsString.toString(), StandardOpenOption.CREATE);
	}

	public static TagList loadTagList() throws IOException {
		File tagList = new File(TAG_LIST_NAME);

		if (tagList.exists()) {
			return new TagList(Files.readAllLines(tagList.toPath()));
		}

		return new TagList();
	}
}
