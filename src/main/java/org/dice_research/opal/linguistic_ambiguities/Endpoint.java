package org.dice_research.opal.linguistic_ambiguities;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ConfigurationException;

public class Endpoint {

	protected String endpoint;
	protected RDFConnection rdfConnection;

	/**
	 * Creates or just returns the connection to a SPARQL endpoint.
	 * 
	 * @throws ConfigurationException if no endpoint is set.
	 */
	public RDFConnection getConnection() throws ConfigurationException {
		if (endpoint == null) {
			throw new ConfigurationException("No SPARQL endpoint set.");
		}
		if (rdfConnection == null) {
			RDFConnectionRemoteBuilder rdfConnectionRemoteBuilder = RDFConnectionRemote.create()
					.destination(getEndpoint());
			rdfConnection = rdfConnectionRemoteBuilder.build();
		}
		return rdfConnection;
	}

	public Endpoint setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public String getEndpoint() {
		return endpoint;
	}
}
