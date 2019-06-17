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
	void test() throws ResourceException, ConfigurationException {
		String mcloudTexts = new Opal().setEndpoint(Opal.OPAL_ENDPOINT).getMcloudTexts();

		// Not empty
		assertFalse(mcloudTexts.isEmpty());

		System.out.println(mcloudTexts);
	}

}