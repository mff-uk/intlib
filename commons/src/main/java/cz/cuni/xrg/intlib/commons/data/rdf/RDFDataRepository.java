package cz.cuni.xrg.intlib.commons.data.rdf;

import cz.cuni.xrg.intlib.commons.data.DataUnit;
import cz.cuni.xrg.intlib.commons.extractor.ExtractException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.rio.RDFFormat;

/**
 * Enable work with RDF data repository.
 *
 * @author Jiri Tomes
 * @author Petyr
 *
 */
public interface RDFDataRepository extends DataUnit {

    public void addTripleToRepository(String namespace, String subjectName, String predicateName,
            String objectName);

    public void extractRDFfromXMLFileToRepository(String path, String suffix, String baseURI,
            boolean useSuffix) throws ExtractException;

    public void loadRDFfromRepositoryToXMLFile(String directoryPath, String fileName,
            org.openrdf.rio.RDFFormat format) throws CannotOverwriteFileException;

    public void loadRDFfromRepositoryToXMLFile(String directoryPath, String fileName, org.openrdf.rio.RDFFormat format,
            boolean canFileOverWrite, boolean isNameUnique) throws CannotOverwriteFileException;

    public void loadtoSPARQLEndpoint(URL endpointURL, String defaultGraphURI, WriteGraphType graphType);

    public void loadtoSPARQLEndpoint(URL endpointURL, String defaultGraphURI, String name,
            String password, WriteGraphType graphType);

    public void loadtoSPARQLEndpoint(URL endpointURL, List<String> endpointGraphsURI, String userName,
            String password, WriteGraphType graphType);

    public void extractfromSPARQLEndpoint(URL endpointURL, String defaultGraphUri, String query) throws ExtractException;

    public void extractfromSPARQLEndpoint(URL endpointURL, String defaultGraphUri, String query,
            String hostName, String password, RDFFormat format) throws ExtractException;

    public void extractfromSPARQLEndpoint(URL endpointURL, List<String> endpointGraphsURI,
            String query, String hostName, String password) throws ExtractException;

    public void transformUsingSPARQL(String updateQuery);

    public long getTripleCountInRepository();

    public boolean isTripleInRepository(String namespace, String subjectName, String predicateName,
            String objectName);

    public void cleanAllRepositoryData();

    public void copyAllDataToTargetRepository(RDFDataRepository targetRepo);

    public void mergeRepositoryData(RDFDataRepository second);

    public Repository getDataRepository();

    public List<Statement> getRepositoryStatements();
    
    public Map<String, List<String>> makeQueryOverRepository(String query); 

}
