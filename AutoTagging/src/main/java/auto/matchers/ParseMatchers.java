package auto.matchers;

import auto.models.matchers.Matcher;
import auto.models.matchers.MatcherSet;
import auto.matchers.rules.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseMatchers {
	public static MatcherSet parseMatchers(JSONObject root) {
		List<Matcher> matchers = new ArrayList<>();

		for (Object matcher : root.getJSONArray("matchers")) {
			if (!(matcher instanceof JSONObject)) {
				throw new IllegalStateException("Expected JSON object in matchers array");
			}

			matchers.add(parseMatcher((JSONObject) matcher));
		}

		return new MatcherSet(matchers);
	}

	public static Matcher parseMatcher(JSONObject matcher) {
		var tag = matcher.getString("tag");
		var rules = parseRules(matcher.getJSONArray("rules"));

		return new Matcher(rules, tag);
	}

	public static List<Rule> parseRules(JSONArray rulesArray) {
		List<Rule> rules = new ArrayList<>();

		for (Object rule : rulesArray) {
			rules.add(parseRule((JSONObject) rule));
		}

		return rules;
	}

	public static Rule parseRule(JSONObject rule) {
		String type = rule.getString("type");

		if ("keyword".equals(type)) {
			String keyword = rule.getString("keyword");

			return new KeywordRule(keyword);
		} else if ("keywordset".equals(type)) {
			List<String> keywords = new ArrayList<>();

			for (Object keywordObject : rule.getJSONArray("keywords")) {
				keywords.add(keywordObject.toString());
			}

			return new KeywordSetRule(keywords);
		} else if ("offset".equals(type)) {
			int offset = rule.getInt("offset");
			String tag = rule.getString("tag");

			return new OffsetRule(offset, tag);
		} else if ("and".equals(type)) {
			return new AndRule(parseRules(rule.getJSONArray("rules")));
		} else if ("or".equals(type)) {
			return new OrRule(parseRules(rule.getJSONArray("rules")));
		} else if ("matchingtag".equals(type)) {
			String tag = rule.getString("tag");

			return new MatchingTagRule(tag);
		} else if ("regex".equals(type)) {
			return new RegexRule(rule.getString("regex"));
		}

		throw new IllegalStateException("Invalid rule type");
	}
}
