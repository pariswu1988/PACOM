package org.proteored.pacom.analysis.exporters.tasks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.filters.Filters;
import org.proteored.miapeapi.experiment.model.sort.ProteinComparatorKey;
import org.proteored.miapeapi.experiment.model.sort.ProteinGroupComparisonType;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.pacom.analysis.exporters.Exporter;
import org.proteored.pacom.analysis.exporters.ExporterManager;
import org.proteored.pacom.analysis.exporters.util.ExportedColumns;
import org.proteored.pacom.analysis.exporters.util.ExporterUtil;

public class TSVExporter extends SwingWorker<Void, String> implements Exporter<File> {
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	private Set<IdentificationSet> idSets = new HashSet<IdentificationSet>();
	private final char separator;
	private final File file;
	private final boolean includeReplicateAndExperimentOrigin;
	private final boolean includeDecoyHits;
	private final boolean showPeptides;
	private final boolean retrieveProteinSequences;
	private final boolean includeGeneInfo;
	private final boolean showBestPeptides;
	private final boolean showBestProteins;
	private final boolean excludeNonConclusiveProteins;
	private String error = null;

	private final boolean isFDRApplied;

	private final Filters filter;

	private boolean distinguisModificatedPeptides;

	private ProteinGroupComparisonType comparisonType;

	public TSVExporter(ExporterManager expManager, Collection<IdentificationSet> idSets, File file, Filters filter) {

		this.file = file;
		this.separator = TAB;
		this.idSets.addAll(idSets);
		this.filter = filter;
		this.includeReplicateAndExperimentOrigin = expManager.isReplicateAndExperimentOriginIncluded();
		this.comparisonType = expManager.getComparisonType();
		this.includeDecoyHits = expManager.isDecoyHitsIncluded();
		this.showPeptides = expManager.showPeptides();
		this.includeGeneInfo = expManager.isGeneInfoIncluded();
		this.showBestPeptides = expManager.showBestPeptides();
		this.showBestProteins = expManager.showBestProteins();
		this.retrieveProteinSequences = expManager.retrieveProteinSequences();
		this.excludeNonConclusiveProteins = !expManager.isNonConclusiveProteinsIncluded();
		this.isFDRApplied = expManager.isFDRApplied();
	}

	private void saveAs(String path) throws IOException {

		BufferedOutputStream out = null;
		out = null;
		// create file
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));

			List<String> columnsStringList = ExportedColumns.getColumnsString(this.includeReplicateAndExperimentOrigin,
					this.showPeptides, this.includeGeneInfo, this.isFDRApplied, idSets);
			ExporterUtil exporterUtil = ExporterUtil.getInstance(idSets, includeReplicateAndExperimentOrigin,
					showPeptides, includeGeneInfo, retrieveProteinSequences, excludeNonConclusiveProteins);
			String columnsString = exporterUtil.getStringFromList(columnsStringList, separator) + NEWLINE;

			log.info(columnsString);
			out.write(columnsString.getBytes());

			// final HashMap<String,
			// IdentificationOccurrence<ExtendedIdentifiedProtein>>
			// proteinOccurrenceList = this.idSet
			// .getProteinOccurrenceList();
			for (IdentificationSet idSet : idSets) {
				int progress = 0;
				// if (progress == 0)
				// return;
				if (this.showPeptides) {
					if (this.showBestPeptides) {
						final HashMap<String, PeptideOccurrence> peptideOccurrenceHashMap = idSet
								.getPeptideOccurrenceList(true);
						// sort if there is a FDR Filter activated that tells us
						// which is the score sort

						ArrayList<PeptideOccurrence> peptideOccurrenceList = new ArrayList<PeptideOccurrence>();
						for (PeptideOccurrence peptideOccurrence : peptideOccurrenceHashMap.values()) {
							if (!includeDecoyHits && peptideOccurrence.isDecoy()) {
								continue;
							}
							if (filter != null) {
								String sequence = peptideOccurrence.getFirstOccurrence()
										.getKey(distinguisModificatedPeptides);

								if (filter.canCheck(sequence) && !filter.isValid(sequence)) {
									continue;
								}

							}
							peptideOccurrenceList.add(peptideOccurrence);
						}
						firePropertyChange(DATA_EXPORTING_SORTING, null, peptideOccurrenceList.size());

						SorterUtil.sortPeptideOcurrencesByBestPeptideScore(peptideOccurrenceList);
						firePropertyChange(DATA_EXPORTING_SORTING_DONE, null, null);
						Iterator<PeptideOccurrence> iterator = peptideOccurrenceList.iterator();

						int total = peptideOccurrenceList.size();
						int i = 1;
						while (iterator.hasNext()) {
							PeptideOccurrence peptideOccurrence = iterator.next();

							Thread.sleep(1);

							final List<String> peptideColumns = exporterUtil.getPeptideInfoList(peptideOccurrence,
									columnsStringList, i++, idSet);
							String lineString = exporterUtil.getStringFromList(peptideColumns, separator) + NEWLINE;
							// log.info(lineString);
							out.write(lineString.getBytes());

							progress++;

							final int percentage = progress * 100 / total;
							log.info(percentage + " %");
							setProgress(percentage);
						}
					} else {
						final List<ExtendedIdentifiedPeptide> identifiedPeptides = idSet.getIdentifiedPeptides();
						// sort if there is a FDR Filter activated that tells us
						// which is the score sort
						List<ExtendedIdentifiedPeptide> peptidelistToExport = new ArrayList<ExtendedIdentifiedPeptide>();

						for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
							Thread.sleep(1);
							if (!includeDecoyHits && peptide.isDecoy()) {
								continue;
							}
							String peptideKey = peptide.getKey(distinguisModificatedPeptides);
							if (filter != null && filter.canCheck(peptideKey)) {
								if (!filter.isValid(peptideKey)) {
									continue;
								}

							}
							peptidelistToExport.add(peptide);
						}
						firePropertyChange(DATA_EXPORTING_SORTING, null, peptidelistToExport.size());
						SorterUtil.sortPeptidesByBestPeptideScore(peptidelistToExport, true);
						firePropertyChange(DATA_EXPORTING_SORTING_DONE, null, null);

						int total = peptidelistToExport.size();
						int i = 1;
						for (ExtendedIdentifiedPeptide peptide : peptidelistToExport) {

							Thread.sleep(1);

							PeptideOccurrence peptideOccurrence = new PeptideOccurrence(
									peptide.getModificationString());
							peptideOccurrence.addOccurrence(peptide);
							final List<String> lineStringList = exporterUtil.getPeptideInfoList(peptideOccurrence,
									columnsStringList, i++, idSet);
							String lineString = exporterUtil.getStringFromList(lineStringList, separator) + NEWLINE;
							// log.info(lineString);
							out.write(lineString.getBytes());

							progress++;
							final int percentage = progress * 100 / total;
							log.info(percentage + " %");
							setProgress(percentage);
						}
					}
				} else {
					// JUST PROTEINS
					if (this.showBestProteins) {
						List<ProteinGroupOccurrence> proteinGroupOccurrenceList = new ArrayList<ProteinGroupOccurrence>();

						final Collection<ProteinGroupOccurrence> proteinOccurrenceSet = idSet
								.getProteinGroupOccurrenceList().values();
						for (ProteinGroupOccurrence proteinGroupOccurrence : proteinOccurrenceSet) {
							if (excludeNonConclusiveProteins
									&& ExporterUtil.isNonConclusiveProtein(proteinGroupOccurrence)) {
								continue;
							}
							if (!includeDecoyHits && proteinGroupOccurrence.isDecoy()) {
								continue;
							}
							ProteinComparatorKey key = proteinGroupOccurrence.getKey(this.comparisonType);
							if (filter != null && filter.canCheck(key)) {
								if (!filter.isValid(key)) {
									continue;
								}
							}
							proteinGroupOccurrenceList.add(proteinGroupOccurrence);
						}

						// sort if there is a FDR Filter activated that tells us
						// which is the score sort

						firePropertyChange(DATA_EXPORTING_SORTING, null, proteinGroupOccurrenceList.size());
						try {
							SorterUtil.sortProteinGroupOcurrencesByBestPeptideScore(proteinGroupOccurrenceList);
						} catch (Exception e) {

						}
						firePropertyChange(DATA_EXPORTING_SORTING_DONE, null, null);
						Iterator<ProteinGroupOccurrence> iterator = proteinGroupOccurrenceList.iterator();

						int total = proteinGroupOccurrenceList.size();
						int i = 1;
						while (iterator.hasNext()) {
							ProteinGroupOccurrence proteinGroupOccurrence = iterator.next();
							Thread.sleep(1);

							final List<String> proteinStringList = exporterUtil
									.getProteinInfoList(proteinGroupOccurrence, columnsStringList, i++, idSet);
							String proteinString = exporterUtil.getStringFromList(proteinStringList, separator)
									+ NEWLINE;
							// System.out.println(peptideString);
							out.write(proteinString.getBytes());

							progress++;
							final int percentage = progress * 100 / total;
							log.info(percentage + " %");
							setProgress(percentage);
						}
					} else {
						final List<ProteinGroup> proteinGroups = idSet.getIdentifiedProteinGroups();
						// sort if there is a FDR Filter activated that tells us
						// which is the score sort
						List<ProteinGroup> proteinGroupsToExport = new ArrayList<ProteinGroup>();
						for (ProteinGroup proteinGroup : proteinGroups) {
							if (!includeDecoyHits && proteinGroup.isDecoy()) {
								continue;
							}
							if (excludeNonConclusiveProteins && ExporterUtil.isNonConclusiveProtein(proteinGroup)) {
								continue;
							}
							ProteinGroupOccurrence proteinOccurrence = new ProteinGroupOccurrence();
							proteinOccurrence.addOccurrence(proteinGroup);
							ProteinComparatorKey key = proteinOccurrence.getKey(this.comparisonType);
							if (filter != null) {
								if (filter.canCheck(key) && !filter.isValid(key)) {
									continue;
								}
							}
							proteinGroupsToExport.add(proteinGroup);
						}
						firePropertyChange(DATA_EXPORTING_SORTING, null, proteinGroupsToExport.size());

						SorterUtil.sortProteinGroupsByBestPeptideScore(proteinGroupsToExport);
						firePropertyChange(DATA_EXPORTING_SORTING_DONE, null, null);

						int total = proteinGroupsToExport.size();
						int i = 1;
						for (ProteinGroup proteinGroup : proteinGroupsToExport) {

							Thread.sleep(1);

							ProteinGroupOccurrence proteinOccurrence = new ProteinGroupOccurrence();
							proteinOccurrence.addOccurrence(proteinGroup);

							final List<String> proteinStringList = exporterUtil.getProteinInfoList(proteinOccurrence,
									columnsStringList, i++, idSet);
							String proteinString = exporterUtil.getStringFromList(proteinStringList, separator)
									+ NEWLINE;
							// System.out.println(peptideString);
							out.write(proteinString.getBytes());

							progress++;
							final int percentage = progress * 100 / total;
							log.info(percentage + " %");
							setProgress(percentage);
						}
					}
				}
			}
		} catch (

		Exception e)

		{
			if (!(e instanceof InterruptedException)) {
				e.printStackTrace();
				error = e.getMessage();
			}
		} finally

		{

			// Close the BufferedOutputStream
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			if (error != null)
				this.cancel(true);
		}

	}

	public void save() throws IOException {
		saveAs(file.getAbsolutePath());
	}

	public char getSeparator() {
		return separator;
	}

	@Override
	protected Void doInBackground() {
		export();
		return null;
	}

	@Override
	protected void done() {

		if (!this.isCancelled())
			firePropertyChange(DATA_EXPORTING_DONE, null, this.file);
		else {
			if (error != null) {
				firePropertyChange(DATA_EXPORTING_ERROR, null, error);
			} else {
				firePropertyChange(DATA_EXPORTING_CANCELED, null, null);
			}
		}
	}

	@Override
	public File export() {
		try {
			save();
		} catch (IOException e) {
			firePropertyChange(DATA_EXPORTING_ERROR, null, e.getMessage());
			e.printStackTrace();
			this.cancel(true);
		}
		return null;
	}

	public void setDistinguisModificatedPeptides(boolean distinguisModificatedPeptides) {
		this.distinguisModificatedPeptides = distinguisModificatedPeptides;
	}

}
