package org.dice_research.opal.linguistic_ambiguities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

	public static void main(String[] args) throws ResourceException, ConfigurationException, IOException {
		Main main = new Main();

		Synonyms synonyms = main.getSysnonyms();

		String mcloudTexts = main.getMcloudTexts();
		String[] mcloudWords = mcloudTexts.split("[^\\p{L}]+");

		SortedSet<String> wordsWithSynonyms = new TreeSet<>();
		Set<String> synonymKeysLowercase = synonyms.keysLowerCase();
		for (int i = 0; i < mcloudWords.length; i++) {
			if (synonymKeysLowercase.contains(mcloudWords[i].toLowerCase())) {
				wordsWithSynonyms.add(mcloudWords[i]);
			}
		}

		// Example run:
		// Number of keys (words): 6668
		// Number of values (synonyms): 21634
		// mCLOUD words: 71897
		// Words with synonyms: 584
		System.out.println("Synonyms cache file: " + synonymsFile.getAbsolutePath());
		System.out.println("Number of keys   (words):    " + synonyms.getNumberOfKeys());
		System.out.println("Number of values (synonyms): " + synonyms.getNumberOfValues());
		System.out.println();
		System.out.println("mCLOUD cache file: " + mcloudFile.getAbsolutePath());
		System.out.println("mCLOUD words: " + mcloudWords.length);
		System.out.println();
		System.out.println("Words with synonyms: " + wordsWithSynonyms.size());
		System.out.println();

		// TODO
		for (String wordWithSynonyms : wordsWithSynonyms) {
			System.out.println(wordWithSynonyms);
		}
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