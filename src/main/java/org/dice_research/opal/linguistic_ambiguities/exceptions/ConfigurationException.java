package org.dice_research.opal.linguistic_ambiguities.exceptions;

/**
 * Thrown on exeptions accesing a resource.
 *
 * @author Adrian Wilke
 */
public class ConfigurationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}