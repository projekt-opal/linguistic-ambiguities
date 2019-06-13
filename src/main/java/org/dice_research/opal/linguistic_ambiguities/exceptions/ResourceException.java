package org.dice_research.opal.linguistic_ambiguities.exceptions;

/**
 * Thrown on exeptions accesing a resource.
 *
 * @author Adrian Wilke
 */
public class ResourceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ResourceException() {
		super();
	}

	public ResourceException(String message) {
		super(message);
	}

	public ResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceException(Throwable cause) {
		super(cause);
	}

}