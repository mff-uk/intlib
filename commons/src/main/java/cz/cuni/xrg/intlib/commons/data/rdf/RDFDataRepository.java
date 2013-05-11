package cz.cuni.xrg.intlib.commons.data.rdf;

import cz.cuni.xrg.intlib.commons.data.DataUnit;
import java.net.URL;
import java.util.List;
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

    public void addTripleToRepository(String namespace, String subjectName, String predicateName, String objectName);

    public void extractRDFfromXMLFileToRepository(String path, String suffix, String baseURI, boolean useSuffix);

    public void loadRDFfromRepositoryToXMLFile(String directoryPath, String fileName, org.openrdf.rio.RDFFormat format) throws CannotOverwriteFileException;

    public void loadRDFfromRepositoryToXMLFile(String directoryPath, String fileName, org.openrdf.rio.RDFFormat format,
            boolean canFileOverWrite, boolean isNameUnique) throws CannotOverwriteFileException;

    public void loadtoSPARQLEndpoint(URL endpointURL, String defaultGraphURI);

    public void loadtoSPARQLEndpoint(URL endpointURL, String defaultGraphURI, String name, String password);

    public void loadtoSPARQLEndpoint(URL endpointURL, List<String> defaultGraphsURI, String userName, String password);

    public void extractfromSPARQLEndpoint(URL endpointURL, String defaultGraphUri, String query);

    public void extractfromSPARQLEndpoint(URL endpointURL, String defaultGraphUri, String query, String hostName, String password, RDFFormat format);

    public void extractfromSPARQLEndpoint(URL endpointURL, List<String> defaultGraphsUri, String query, String hostName, String password);

    public void transformUsingSPARQL(String updateQuery);

    public long getTripleCountInRepository();

    public boolean isTripleInRepository(String namespace, String subjectName, String predicateName, String objectName);

    public void cleanAllRepositoryData();

    public void copyAllDataToTargetRepository(Repository targetRepository);
}
