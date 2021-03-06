package org.proteored.pacom.analysis.gui.tasks;

import java.util.List;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.IllegalMiapeArgumentException;
import org.proteored.miapeapi.exceptions.MiapeDataInconsistencyException;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.experiment.model.ExperimentList;
import org.proteored.miapeapi.experiment.model.datamanager.DataManager;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.pacom.analysis.conf.ExperimentListAdapter;
import org.proteored.pacom.analysis.conf.jaxb.CPExperimentList;
import org.springframework.beans.factory.BeanDefinitionStoreException;

public class DataLoaderTask extends SwingWorker<ExperimentList, Void> {
	private static final Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private final CPExperimentList cpExpList;
	private final Integer minPeptideLength;
	private final List<Filter> filters;
	private final boolean groupingAtExperimentListLevel;
	private final boolean processInParallel;
	private final boolean annotateProteinsInUniprot;
	public static final String DATA_LOADED_DONE = "data loaded done";
	public static final String DATA_LOADED_START = "data loaded start";
	public static final String DATA_LOADED_ERROR = "data loaded error";
	private final boolean doNotGroupNonConclusiveProteins;
	private final boolean separateNonConclusiveProteins;

	public DataLoaderTask(CPExperimentList cpExpList, Integer minPeptideLength, boolean groupingAtExperimentListLevel,
			List<Filter> filters, boolean processInParallel, boolean annotateProteinsInUniprot,
			boolean doNotGroupNonConclusiveProteins, boolean separateNonConclusiveProteins) {
		this.cpExpList = cpExpList;
		this.minPeptideLength = minPeptideLength;
		this.filters = filters;
		this.groupingAtExperimentListLevel = groupingAtExperimentListLevel;
		this.processInParallel = processInParallel;
		this.annotateProteinsInUniprot = annotateProteinsInUniprot;
		this.doNotGroupNonConclusiveProteins = doNotGroupNonConclusiveProteins;
		this.separateNonConclusiveProteins = separateNonConclusiveProteins;
	}

	// public DataLoaderTask(CPExperimentList cpExpList, boolean
	// processInParallel) {
	// this(cpExpList, null, false, null, processInParallel);
	// }

	@Override
	protected ExperimentList doInBackground() throws Exception {

		try {
			firePropertyChange(DATA_LOADED_START, null, null);

			log.info("Clearing DataManager static information");
			DataManager.clearStaticInfo();

			log.info("Loading data");

			ExperimentList expList = new ExperimentListAdapter(cpExpList, minPeptideLength,
					groupingAtExperimentListLevel, filters, processInParallel, annotateProteinsInUniprot,
					doNotGroupNonConclusiveProteins, separateNonConclusiveProteins).adapt();
			firePropertyChange(DATA_LOADED_DONE, null, expList);

			return expList;
		} catch (MiapeDataInconsistencyException e) {
			e.printStackTrace();
			firePropertyChange(DATA_LOADED_ERROR, null, e.getMessage());
			return null;
		} catch (WrongXMLFormatException e) {
			e.printStackTrace();
			firePropertyChange(DATA_LOADED_ERROR, null, e.getMessage());
			return null;
		} catch (IllegalMiapeArgumentException e) {
			e.printStackTrace();
			firePropertyChange(DATA_LOADED_ERROR, null, e.getMessage());
			return null;
		} catch (BeanDefinitionStoreException e) {
			e.printStackTrace();
			firePropertyChange(DATA_LOADED_ERROR, null, "Error trying to connect to the Internet.<br>"
					+ "PACom uses the internet connection to retrieve some resources, collect some protein annotations and check for new updates.<br>"

					+ "Please check your internet connection or institution firewall and run the software again.");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			firePropertyChange(DATA_LOADED_ERROR, null, e.getMessage());
			return null;
		}
	}

	@Override
	protected void done() {
		if (isCancelled()) {
			log.info("Data loading cancelled");
		}
	}
}
