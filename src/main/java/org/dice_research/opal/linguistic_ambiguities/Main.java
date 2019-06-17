package org.dice_research.opal.linguistic_ambiguities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ConfigurationException;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ResourceException;

/**
 * Main entry point.
 *
 * @author Adrian Wilke
 */
public class Main {

	static File synonymsFile = new File("synonyms-german.txt");
	static File mcloudFile = new File("mcloud.txt");
	static File elasticSearchSynonymFile = new File("synonym.txt");

	/**
	 * Gets synonyms and fulltext-words. Creates ElasticSearch synonym file.
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/guide/current/using-synonyms.html
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/2.4/analysis-synonym-tokenfilter.html
	 */
	public static void main(String[] args) throws ResourceException, ConfigurationException, IOException {
		Main main = new Main();

		// Prepare synonyms
		// Maintain (lowercase) key indexes in list
		Synonyms synonyms = main.getSysnonyms();
		List<String> synonymKeys = new ArrayList<>(synonyms.keys());
		List<String> synonymKeysLowercase = new ArrayList<>(synonymKeys.size());
		for (String key : synonymKeys) {
			synonymKeysLowercase.add(key.toLowerCase());
		}

		// Prepare fulltext-words
		String mcloudTexts = main.getMcloudTexts();
		String[] mcloudWords = mcloudTexts.split("[^\\p{L}]+");

		// Match synonyms and fulltext-words
		// Maintain indexes
		Set<Integer> indexes = new HashSet<Integer>();
		for (int i = 0; i < mcloudWords.length; i++) {
			if (synonymKeysLowercase.contains(mcloudWords[i].toLowerCase())) {
				indexes.add(synonymKeysLowercase.indexOf(mcloudWords[i].toLowerCase()));
			}
		}

		// Create ElasticSearch synonyms
		StringBuilder stringBuilder = new StringBuilder();
		for (Integer index : indexes) {
			boolean first = true;
			for (String synonym : synonyms.get(synonymKeys.get(index))) {
				if (first) {
					first = false;
				} else {
					stringBuilder.append(", ");
				}
				stringBuilder.append(synonym.toLowerCase());
			}
			stringBuilder.append(" => ");
			stringBuilder.append(synonymKeys.get(index).toLowerCase());
			stringBuilder.append(System.lineSeparator());
		}
		FileUtils.write(elasticSearchSynonymFile, stringBuilder.toString(), StandardCharsets.UTF_8);

		// Example run:
		// Number of keys (words): 6668
		// Number of values (synonyms): 21634
		// mCLOUD words: 71897
		// Words with synonyms: 518
		System.out.println("Synonyms cache file: " + synonymsFile.getAbsolutePath());
		System.out.println("Number of keys   (words):    " + synonyms.getNumberOfKeys());
		System.out.println("Number of values (synonyms): " + synonyms.getNumberOfValues());
		System.out.println();
		System.out.println("mCLOUD cache file: " + mcloudFile.getAbsolutePath());
		System.out.println("mCLOUD words: " + mcloudWords.length);
		System.out.println();
		System.out.println("ElasticSearch synonym file: " + elasticSearchSynonymFile.getAbsolutePath());
		System.out.println("Words with synonyms: " + indexes.size());
	}

	String getMcloudTexts() throws IOException, ResourceException, ConfigurationException {
		if (mcloudFile.exists()) {
			return FileUtils.readFileToString(mcloudFile, StandardCharsets.UTF_8);
		} else {
			Opal opal = new Opal();
			opal.setEndpoint(Opal.OPAL_ENDPOINT);
			String mcloudTexts = opal.getMcloudTexts();

			FileUtils.write(mcloudFile, mcloudTexts, StandardCharsets.UTF_8);

			return mcloudTexts;
		}
	}

	Synonyms getSysnonyms() throws IOException, ResourceException, ConfigurationException {

		if (synonymsFile.exists()) {

			// Use cache file
			return Synonyms.importFile(synonymsFile);

		} else {

			// Get synonyms of german nouns
			Dbnary dbnary = new Dbnary();
			dbnary.setEndpoint(Dbnary.DBNARY_ENDPOINT);
			Synonyms synonyms = dbnary.getSynonyms();

			// Save synonyms to file (to be used as a cache)
			synonyms.export(synonymsFile);

			return synonyms;
		}
	}
}