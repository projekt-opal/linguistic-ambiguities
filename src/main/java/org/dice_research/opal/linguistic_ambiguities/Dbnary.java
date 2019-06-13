package org.dice_research.opal.linguistic_ambiguities;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ConfigurationException;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ResourceException;

/**
 * DBnary access to get synonyms of German nouns.
 *
 * Typical use: Call {@link #setEndpoint(String)} and {@link #getSynonyms()}.
 *
 * @see DBnary at http://kaiko.getalp.org/
 *
 * @author Adrian Wilke
 */
public class Dbnary {

	public static final String DBNARY_ENDPOINT = "http://kaiko.getalp.org/sparql";
	public static final String RESOURCE_SPARQL_SYNONYMS = "sparql-german-synonyms.txt";

	/**
	 * see http://vos.openlinksw.com/owiki/wiki/VOS/VirtConfigScale ResultSetMaxRows
	 */
	public static final int LIMIT = 10000;

	public static void main(String[] args) throws ResourceException, ConfigurationException {
		Synonyms synonyms = new Dbnary().setEndpoint(DBNARY_ENDPOINT).getSynonyms();
		System.out.println(synonyms);
	}

	protected String endpoint;
	protected RDFConnection rdfConnection;
	protected int max;

	/**
	 * Creates or just returns the connection to a (DBnary) SPARQL endpoint.
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

	public String getEndpoint() {
		return endpoint;
	}

	public Synonyms getSynonyms() throws ResourceException, ConfigurationException {
		Synonyms synonyms = new Synonyms();
		String sparqlSelect = Resources.getResourceAsString(RESOURCE_SPARQL_SYNONYMS);
		if (max > 0) {
			addSynonyms(synonyms, sparqlSelect, max, 0);
		} else {
			int offset = 0;
			while (addSynonyms(synonyms, sparqlSelect, LIMIT, offset)) {
				offset += LIMIT;
			}
		}
		return synonyms;
	}

	/**
	 * Adds synonyms by executing a SPARQL request.
	 * 
	 * @return false, if no results returned.
	 * @throws ConfigurationException
	 */
	protected boolean addSynonyms(Synonyms synonyms, String defaultQuery, int limit, int offset)
			throws ConfigurationException {
		String sparqlSelect = defaultQuery + " LIMIT " + limit + " OFFSET " + offset;
		RDFConnection rdfConnection = getConnection();
		QueryExecution queryExecution = rdfConnection.query(sparqlSelect);
		ResultSet resultSet = queryExecution.execSelect();
		if (resultSet.hasNext() == false) {
			return false;
		}
		int resultCounter = 0;
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			synonyms.add(querySolution.get("germannoun").asLiteral().getLexicalForm(),
					querySolution.get("synonym").asLiteral().getLexicalForm());
			resultCounter++;
		}
		if (resultCounter < limit) {
			return false;
		}
		return true;
	}

	public Dbnary setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	/**
	 * Sets a maximum to the DBnary SPARQL select query. Typically used in test
	 * cases.
	 */
	public Dbnary setMaximum(int max) {
		this.max = max;
		return this;
	}
}