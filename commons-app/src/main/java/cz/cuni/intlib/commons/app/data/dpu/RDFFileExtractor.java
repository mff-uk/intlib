package cz.cuni.intlib.commons.app.data.dpu;

import java.util.jar.JarFile;

/**
 * Component for extracting data from RDF file.
 *
 * @author Jiri Tomes;
 */
public class RDFFileExtractor implements DPU {

    private String name;
    private String description;
    private JarFile jarFile;
    private TemplateConfiguration templateConfiguration;

    public RDFFileExtractor(String name, String description, JarFile jarFile) {
        this.name = name;
        this.description = description;
        this.jarFile = jarFile;
        this.templateConfiguration = new TemplateConfiguration();
    }

    public String getDescription() {
        return description;
    }

    public JarFile getJarFile() {
        return jarFile;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return Type.EXTRACTOR;
    }

    public SubType getSubtype() {
        return SubType.RDF_EXTRACTOR;
    }

    public TemplateConfiguration getTemplateConfiguration() {
        return templateConfiguration;
    }
}
