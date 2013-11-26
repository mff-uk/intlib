package cz.cuni.mff.xrg.odcs.loader.file;

import cz.cuni.mff.xrg.odcs.commons.data.DataUnitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.cuni.mff.xrg.odcs.commons.dpu.DPUContext;
import cz.cuni.mff.xrg.odcs.commons.dpu.DPUException;
import cz.cuni.mff.xrg.odcs.commons.dpu.annotation.AsLoader;
import cz.cuni.mff.xrg.odcs.commons.dpu.annotation.InputDataUnit;
import cz.cuni.mff.xrg.odcs.commons.message.MessageType;
import cz.cuni.mff.xrg.odcs.commons.module.dpu.ConfigurableBase;
import cz.cuni.mff.xrg.odcs.commons.web.*;
import cz.cuni.mff.xrg.odcs.rdf.enums.RDFFormatType;
import cz.cuni.mff.xrg.odcs.rdf.exceptions.CannotOverwriteFileException;
import cz.cuni.mff.xrg.odcs.rdf.exceptions.RDFException;
import cz.cuni.mff.xrg.odcs.rdf.interfaces.DataValidator;
import cz.cuni.mff.xrg.odcs.rdf.interfaces.RDFDataUnit;
import cz.cuni.mff.xrg.odcs.rdf.validators.RepositoryDataValidator;

/**
 * @author Jiri Tomes
 * @author Petyr
 */
@AsLoader
public class FileLoader extends ConfigurableBase<FileLoaderConfig>
		implements ConfigDialogProvider<FileLoaderConfig> {

	private final Logger logger = LoggerFactory.getLogger(FileLoader.class);

	@InputDataUnit
	public RDFDataUnit rdfDataUnit;

	public FileLoader() {
		super(FileLoaderConfig.class);
	}

	@Override
	public void execute(DPUContext context) throws DPUException, DataUnitException {

		final String filePath = config.FilePath;
		final RDFFormatType formatType = config.RDFFileFormat;
		final boolean isNameUnique = config.DiffName;
		final boolean canFileOverwritte = true;
		final boolean validateDataBefore = config.validDataBefore;

		if (validateDataBefore) {
			DataValidator dataValidator = new RepositoryDataValidator(
					rdfDataUnit);

			if (!dataValidator.areDataValid()) {
				final String message = "RDF Data to load are not valid - LOADING to File FAIL";
				logger.error(dataValidator.getErrorMessage());

				context.sendMessage(MessageType.WARNING, message, dataValidator
						.getErrorMessage());

				throw new RDFException(message);
			} else {
				context.sendMessage(MessageType.INFO,
						"RDF Data for loading to file are valid");
				context.sendMessage(MessageType.INFO,
						"Loading data to file STARTS JUST NOW");
			}
		}


		final long triplesCount = rdfDataUnit.getTripleCount();
		logger.info("Loading {} triples", triplesCount);

		try {
			rdfDataUnit.loadToFile(filePath, formatType,
					canFileOverwritte, isNameUnique);
		} catch (RDFException | CannotOverwriteFileException ex) {
			context.sendMessage(MessageType.ERROR, ex.getMessage());
			throw new DPUException(ex);
		}
	}

	@Override
	public AbstractConfigDialog<FileLoaderConfig> getConfigurationDialog() {
		return new FileLoaderDialog();
	}
}
