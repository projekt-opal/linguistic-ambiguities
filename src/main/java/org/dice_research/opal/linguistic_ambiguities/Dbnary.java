package org.dice_research.opal.linguistic_ambiguities;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
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
public class Dbnary extends Endpoint {

	public static final String DBNARY_ENDPOINT = "http://kaiko.getalp.org/sparql";
	public static final String RESOURCE_SPARQL_SYNONYMS = "sparql-german-synonyms.txt";

	/**
	 * see http://vos.openlinksw.com/owiki/wiki/VOS/VirtConfigScale ResultSetMaxRows
	 */
	public static final int LIMIT = 10000;

	protected int max;

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

	/**
	 * Sets a maximum to the DBnary SPARQL select query. Typically used in test
	 * cases.
	 */
	public Dbnary setMaximum(int max) {
		this.max = max;
		return this;
	}
}