package org.dice_research.opal.linguistic_ambiguities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.IOException;

import org.dice_research.opal.linguistic_ambiguities.exceptions.ConfigurationException;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ResourceException;
import org.junit.jupiter.api.Test;

/**
 * Tests DBnary SPARQL processing.
 *
 * @author Adrian Wilke
 */
class DbnaryTest {

	public static final int LIMIT = 20;

	@Test
	void test() throws ResourceException, ConfigurationException {
		Synonyms synonyms = new Dbnary().setEndpoint(Dbnary.DBNARY_ENDPOINT).setMaximum(LIMIT).getSynonyms();

		// Not empty / there should be at least one key
		assertNotEquals(0, synonyms.getNumberOfKeys());

		// The number of values/synonyms should be equal to the number of results, which
		// is set by LIMIT
		int values = 0;
		for (String key : synonyms.keys()) {
			values += synonyms.get(key).size();
		}
		assertEquals(LIMIT, values);

		System.out.println(synonyms);
	}

	@Test
	public void testExportImport() throws ResourceException, ConfigurationException, IOException {
		Synonyms synonyms = new Dbnary().setEndpoint(Dbnary.DBNARY_ENDPOINT).setMaximum(LIMIT).getSynonyms();
		File testfile = File.createTempFile(this.getClass().getSimpleName(), ".test.tmp");
		synonyms.export(testfile);
		Synonyms imported = Synonyms.importFile(testfile);

		// Not empty / there should be at least one key
		assertNotEquals(0, synonyms.getNumberOfKeys());

		// Same number of keys
		assertEquals(synonyms.getNumberOfKeys(), imported.getNumberOfKeys());

		// Same keys
		assertEquals(synonyms.keys(), imported.keys());

		// Same number of values
		assertEquals(synonyms.getNumberOfValues(), imported.getNumberOfValues());
	}

}