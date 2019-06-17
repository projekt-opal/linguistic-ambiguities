package org.dice_research.opal.linguistic_ambiguities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

		// Example run:
		// Number of keys (words): 6683
		// Number of values (synonyms): 21663
		System.out.println("Cache file: " + synonymsFile.getAbsolutePath());
		System.out.println("Number of keys   (words):    " + synonyms.getNumberOfKeys());
		System.out.println("Number of values (synonyms): " + synonyms.getNumberOfValues());

		String mcloudTexts = main.getMcloudTexts();
		System.out.println(mcloudTexts);
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