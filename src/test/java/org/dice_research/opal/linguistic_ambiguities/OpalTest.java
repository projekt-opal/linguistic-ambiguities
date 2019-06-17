package org.dice_research.opal.linguistic_ambiguities;

import static org.junit.Assert.assertFalse;

import org.dice_research.opal.linguistic_ambiguities.exceptions.ConfigurationException;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ResourceException;
import org.junit.jupiter.api.Test;

/**
 * Tests DBnary SPARQL processing.
 *
 * @author Adrian Wilke
 */
class OpalTest {

	public static final int LIMIT = 20;

	@Test
	void testMcloud() throws ResourceException, ConfigurationException {
		String texts = new Opal().setEndpoint(Opal.OPAL_ENDPOINT).getMcloudTexts();

		// Not empty
		assertFalse(texts.isEmpty());

		System.out.println(texts);
	}

	@Test
	void testGovdata() throws ResourceException, ConfigurationException {
		String texts = new Opal().setEndpoint(Opal.OPAL_ENDPOINT).getGovdataTexts();

		// Not empty
		assertFalse(texts.isEmpty());

		System.out.println(texts);
	}

}