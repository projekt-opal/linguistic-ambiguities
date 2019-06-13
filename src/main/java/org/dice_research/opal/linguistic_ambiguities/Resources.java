package org.dice_research.opal.linguistic_ambiguities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dice_research.opal.linguistic_ambiguities.exceptions.ResourceException;

/**
 * Handling Java resources.
 *
 * @author Adrian Wilke
 */
public class Resources {

	/**
	 * Gets the resource for the given name as String.
	 * 
	 * @throws ResourceException if no resource is found for the given name.
	 */
	public static String getResourceAsString(String resourceName) throws ResourceException {
		// see https://www.baeldung.com/reading-file-in-java
		InputStream inputStream = Resources.class.getResourceAsStream(resourceName);
		if (inputStream == null) {
			throw new ResourceException("Could not find resource: " + resourceName);
		}
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			throw new ResourceException(e);
		}
		return resultStringBuilder.toString();
	}
}