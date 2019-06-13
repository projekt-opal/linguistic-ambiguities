package org.dice_research.opal.linguistic_ambiguities;

import java.io.File;
import java.io.IOException;

import org.dice_research.opal.linguistic_ambiguities.exceptions.ConfigurationException;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ResourceException;

/**
 * Main entry point.
 *
 * @author Adrian Wilke
 */
public class Main {

	public static void main(String[] args) throws ResourceException, ConfigurationException, IOException {

		// Get synonyms of german nouns
		// Example run:
		// Number of keys (words): 6683
		// Number of values (synonyms): 21663
		Synonyms synonyms = new Dbnary().setEndpoint(Dbnary.DBNARY_ENDPOINT).getSynonyms();
		System.out.println("Number of keys   (words):    " + synonyms.getNumberOfKeys());
		System.out.println("Number of values (synonyms): " + synonyms.getNumberOfValues());

		// Save synonyms to file (to be used as a cache)
		File file = new File("synonyms-german.txt");
		synonyms.export(file);
		System.out.println("Exported file to: " + file.getAbsolutePath());
	}
}
