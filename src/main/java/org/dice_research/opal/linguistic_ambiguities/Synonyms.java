package org.dice_research.opal.linguistic_ambiguities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

/**
 * Container for synonyms. Key objects typically represent words. Values are
 * synonyms of the respective keys.
 *
 * @author Adrian Wilke
 */
public class Synonyms {

	protected SortedMap<String, SortedSet<String>> map;

	public Synonyms() {
		map = new TreeMap<>();
	}

	public void add(String key, String value) {
		if (!map.containsKey(key)) {
			map.put(key, new TreeSet<>());
		}
		map.get(key).add(value);
	}

	public SortedSet<String> get(String key) {
		return map.get(key);
	}

	public Set<String> keys() {
		return map.keySet();
	}

	public Set<String> keysLowerCase() {
		SortedSet<String> set = new TreeSet<>();
		for (String key : map.keySet()) {
			set.add(key.toLowerCase());
		}
		return set;
	}

	public int getNumberOfKeys() {
		return map.size();
	}

	public int getNumberOfValues() {
		int counter = 0;
		for (SortedSet<String> values : map.values()) {
			counter += values.size();
		}
		return counter;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, SortedSet<String>> entry : map.entrySet()) {
			stringBuilder.append(entry.getKey());
			stringBuilder.append(" ");
			stringBuilder.append(entry.getValue());
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

	public void export(File file) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		for (Entry<String, SortedSet<String>> entry : map.entrySet()) {
			for (String value : entry.getValue()) {
				stringBuilder.append(entry.getKey());
				stringBuilder.append(System.lineSeparator());
				stringBuilder.append(value);
				stringBuilder.append(System.lineSeparator());
			}
		}
		FileUtils.write(file, stringBuilder.toString(), StandardCharsets.UTF_8);
	}

	public static Synonyms importFile(File file) throws IOException {
		Synonyms synonyms = new Synonyms();
		String key = null;
		for (String line : FileUtils.readLines(file, StandardCharsets.UTF_8)) {
			if (key == null) {
				key = line;
			} else {
				synonyms.add(key, line);
				key = null;
			}
		}
		return synonyms;
	}
}