package org.dice_research.opal.linguistic_ambiguities;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdfconnection.RDFConnection;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ConfigurationException;
import org.dice_research.opal.linguistic_ambiguities.exceptions.ResourceException;

/**
 * Gets OPAL graph data.
 * 
 * @author Adrian Wilke
 */
public class Opal extends Endpoint {

	public static final String OPAL_ENDPOINT = "http://opalpro.cs.upb.de:3030/opal/sparql";
	public static final String RESOURCE_SPARQL_MCLOUD = "sparql-mcloud.txt";

	/**
	 * Gets mCLOUD texts in OPAL graph
	 * 
	 * @throws ResourceException
	 * @throws ConfigurationException
	 */
	public String getMcloudTexts() throws ResourceException, ConfigurationException {
		String sparqlSelect = Resources.getResourceAsString(RESOURCE_SPARQL_MCLOUD);
		RDFConnection rdfConnection = getConnection();
		QueryExecution queryExecution = rdfConnection.query(sparqlSelect);
		ResultSet resultSet = queryExecution.execSelect();
		StringBuilder stringBuilder = new StringBuilder();
		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();
			stringBuilder.append(querySolution.get("title").asLiteral().getLexicalForm());
			stringBuilder.append(System.lineSeparator());
			stringBuilder.append(querySolution.get("description").asLiteral().getLexicalForm());
			stringBuilder.append(System.lineSeparator());
		}
		return stringBuilder.toString();
	}

}