/*
 * ExporterDialog.java Created on __DATE__, __TIME__
 */

package org.proteored.pacom.analysis.exporters.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.experiment.model.ExtendedIdentifiedPeptide;
import org.proteored.miapeapi.experiment.model.IdentificationSet;
import org.proteored.miapeapi.experiment.model.PeptideOccurrence;
import org.proteored.miapeapi.experiment.model.ProteinGroup;
import org.proteored.miapeapi.experiment.model.ProteinGroupOccurrence;
import org.proteored.miapeapi.experiment.model.filters.Filters;
import org.proteored.miapeapi.experiment.model.grouping.ProteinEvidence;
import org.proteored.miapeapi.experiment.model.sort.ProteinComparatorKey;
import org.proteored.miapeapi.experiment.model.sort.ProteinGroupComparisonType;
import org.proteored.pacom.analysis.exporters.ExporterManager;
import org.proteored.pacom.analysis.exporters.tasks.TSVExporter;
import org.proteored.pacom.analysis.gui.ChartManagerFrame;
import org.proteored.pacom.analysis.gui.TsvFileFilter;
import org.proteored.pacom.gui.ImageManager;
import org.proteored.pacom.gui.MainFrame;

/**
 *
 * @author __USER__
 */
public class ExporterDialog extends javax.swing.JDialog implements PropertyChangeListener, ExporterManager {
	private final Set<IdentificationSet> idSets = new HashSet<IdentificationSet>();
	private TSVExporter exporter;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");
	private boolean previousCollapsePeptides;
	private boolean previousCollapseProteins;
	private boolean previousIncludeDecoy;
	private boolean previousIncludeExperimentReplicate;
	private boolean previousIncludeGene;
	private boolean previousSearchForProteinSequence;
	private boolean previousExcludeNonConclusive;
	private final boolean isFDRApplied;
	private Filters filter;
	private Boolean distinguisModifiedPeptides;
	private final ChartManagerFrame chartManagerFrame;

	public ExporterDialog(ChartManagerFrame parent, Collection<IdentificationSet> idSets) {
		super(parent, true);
		this.chartManagerFrame = parent;
		this.distinguisModifiedPeptides = parent.distinguishModifiedPeptides();
		this.idSets.addAll(idSets);

		initComponents();
		if (!this.idSets.isEmpty() && parent.getFiltersDialog() != null
				&& parent.getFiltersDialog().isFDRFilterDefined()) {
			jCheckBoxIncludeDecoy.setEnabled(true);
			isFDRApplied = true;
		} else {
			isFDRApplied = false;
		}
		if (parent.getCurrentChartType().equals(ChartManagerFrame.PROTEIN_OVERLAPING)) {
			// disable export peptides
			this.jCheckBoxCollapsePeptides.setEnabled(false);
			this.jRadioButtonExportPeptides.setEnabled(false);
			this.jRadioButtonExportProteins.setSelected(true);
			this.jCheckBoxCollapseProteins.setEnabled(true);
		}
		if (parent.getCurrentChartType().equals(ChartManagerFrame.PEPTIDE_OVERLAPING)) {
			// disable export proteins
			this.jCheckBoxCollapseProteins.setEnabled(false);
			this.jRadioButtonExportProteins.setEnabled(false);
			this.jRadioButtonExportPeptides.setSelected(true);
			this.jCheckBoxCollapsePeptides.setEnabled(true);

		}
		// Just enable if the protein sequences have not been retrieved before
		jCheckBoxSearchForProteinSequence.setEnabled(!parent.isProteinSequencesRetrieved());

		// if (parent != null && parent.isChr16ChartShowed())
		// this.jCheckBoxIncludeGeneInfo.setEnabled(true);
		updateSizesLabels();
		// set icon image
		setIconImage(ImageManager.getImageIcon(ImageManager.PROTEORED_MIAPE_API).getImage());

		jButtonCancel.setIcon(ImageManager.getImageIcon(ImageManager.STOP));
		jButtonExport.setIcon(ImageManager.getImageIcon(ImageManager.EXCEL_TABLE));
		jCheckBoxIncludeNonConclusiveProteins.setSelected(chartManagerFrame.countNonConclusiveProteins());
		pack();

	}

	public void setFilter(Filters filter) {
		this.filter = filter;
		// disable jcheckbox include non conclusive proteins
		// because it is only usable when not filters are involved, meaning that
		// the exporter is general, not from overlapping graph
		this.jCheckBoxIncludeNonConclusiveProteins.setEnabled(false);
		// update numbers
		updateSizesLabels();
	}

	private String getNumString(Integer numPeptides, Integer numProteins) {
		String ret = "";
		if (numProteins != null)
			ret = "Exporting " + numProteins + " proteins";
		if (numPeptides != null)
			if ("".equals(ret))
				ret = "Exporting " + numPeptides + " peptides";
			else
				ret = ret + " and " + numPeptides + " peptides";
		return ret;
	}

	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		jFileChooser1 = new JFileChooser(MainFrame.currentFolder);
		jLabelNumPeptides = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jTextFieldFilePath = new javax.swing.JTextField();
		jButtonSelect = new javax.swing.JButton();
		jPanelOptions = new javax.swing.JPanel();
		jCheckBoxIncludeDecoy = new javax.swing.JCheckBox();
		jCheckBoxIncludeExperimentReplicateOrigin = new javax.swing.JCheckBox();
		jCheckBoxSearchForProteinSequence = new javax.swing.JCheckBox();
		jCheckBoxIncludeGeneInfo = new javax.swing.JCheckBox();
		jCheckBoxCollapsePeptides = new javax.swing.JCheckBox();
		jCheckBoxCollapseProteins = new javax.swing.JCheckBox();
		jRadioButtonExportPeptides = new javax.swing.JRadioButton();
		jRadioButtonExportProteins = new javax.swing.JRadioButton();
		jCheckBoxIncludeNonConclusiveProteins = new javax.swing.JCheckBox();
		jCheckBoxIncludeNonConclusiveProteins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSizesLabels();
			}
		});
		jPanel2 = new javax.swing.JPanel();
		jButtonExport = new javax.swing.JButton();
		jProgressBar1 = new javax.swing.JProgressBar();
		jButtonCancel = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Export Identification Data");
		setResizable(false);

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(),
				"Output file"));

		jButtonSelect.setText("Select");
		jButtonSelect.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonSelectActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel1Layout.createSequentialGroup().addContainerGap()
								.addComponent(jTextFieldFilePath, javax.swing.GroupLayout.DEFAULT_SIZE, 352,
										Short.MAX_VALUE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jButtonSelect).addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jButtonSelect).addComponent(jTextFieldFilePath,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanelOptions.setBorder(javax.swing.BorderFactory
				.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Options"));

		jCheckBoxIncludeDecoy.setText("include DECOY hits");
		jCheckBoxIncludeDecoy.setToolTipText(
				"<html>If this options is activated, decoy peptides/proteins <br>will also be included in the exported file.<br>\nThis options only is available if a FDR filter is applied.</html>");
		jCheckBoxIncludeDecoy.setEnabled(false);
		jCheckBoxIncludeDecoy.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBoxIncludeDecoyActionPerformed(evt);
			}
		});

		jCheckBoxIncludeExperimentReplicateOrigin.setText("include project tree level names");
		jCheckBoxIncludeExperimentReplicateOrigin.setToolTipText(
				"<html>\nAdd columns indicating at which node of the comparison project tree, each identification item belongs\n</html>");

		jCheckBoxSearchForProteinSequence.setText("calculate protein coverages");
		jCheckBoxSearchForProteinSequence.setToolTipText(
				"<html>\nThis options will retrieve protein sequences from the Internet in order to\n<br>\ncalculate the protein coverage. Depending on the number of proteins you\n<br>have, it can take several minutes.\n</html>");

		jCheckBoxIncludeGeneInfo.setText("include gene information");
		jCheckBoxIncludeGeneInfo.setToolTipText(
				"<html>\nThis option will add some columns containing information<br>\nabout the genes associated with each protein.\n</html>");

		jCheckBoxCollapsePeptides.setSelected(true);
		jCheckBoxCollapsePeptides.setText("export just the best peptides");
		jCheckBoxCollapsePeptides.setToolTipText(
				"<html>\nIf this option is activated, if the same peptide has been detected\n<br>\nmore than once, it will appear only once in the exported table and\n<br>\nthe score will be the best score of all occurrences.</html>");
		jCheckBoxCollapsePeptides.setEnabled(false);
		jCheckBoxCollapsePeptides.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBoxCollapsePeptidesActionPerformed(evt);
			}
		});

		jCheckBoxCollapseProteins.setSelected(true);
		jCheckBoxCollapseProteins.setText("export just the best proteins");
		jCheckBoxCollapseProteins.setToolTipText(
				"<html>\nIf this option is activated, if the same protein has been detected\n<br>\nmore than once, it will appear only once in the exported table and\n<br>\nthe score will be the best score of all occurrences.</html>");
		jCheckBoxCollapseProteins.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jCheckBoxCollapseProteinsActionPerformed(evt);
			}
		});

		buttonGroup1.add(jRadioButtonExportPeptides);
		jRadioButtonExportPeptides.setText("export peptides");
		jRadioButtonExportPeptides.setToolTipText(
				"<html>Peptide information will be together with its assigned protein\n<br>\naccession, but no more protein information will be exported.</html>");
		jRadioButtonExportPeptides.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonExportPeptidesActionPerformed(evt);
			}
		});

		buttonGroup1.add(jRadioButtonExportProteins);
		jRadioButtonExportProteins.setSelected(true);
		jRadioButtonExportProteins.setText("export proteins");
		jRadioButtonExportProteins.setToolTipText("Only protein information will be exported.");
		jRadioButtonExportProteins.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonExportProteinsActionPerformed(evt);
			}
		});

		jCheckBoxIncludeNonConclusiveProteins.setText("include NONCONCLUSIVE proteins");
		jCheckBoxIncludeNonConclusiveProteins.setToolTipText(
				"<html>\nSelect this checkbox in order to exclude the Non Conclusive proteins<br>\nin the exported data.<br>\nNon conclusive protein is a protein that shares all its matched peptides<br> with either conclusive or indistinguishable proteins.\n</html>");

		javax.swing.GroupLayout jPanelOptionsLayout = new javax.swing.GroupLayout(jPanelOptions);
		jPanelOptions.setLayout(jPanelOptionsLayout);
		jPanelOptionsLayout
				.setHorizontalGroup(
						jPanelOptionsLayout
								.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										jPanelOptionsLayout.createSequentialGroup().addContainerGap()
												.addGroup(
														jPanelOptionsLayout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jCheckBoxIncludeDecoy)
																.addComponent(jCheckBoxIncludeExperimentReplicateOrigin)
																.addComponent(jCheckBoxIncludeGeneInfo)
																.addComponent(jCheckBoxIncludeNonConclusiveProteins)
																.addComponent(jCheckBoxSearchForProteinSequence))
												.addGap(21, 21,
														21)
						.addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jRadioButtonExportPeptides).addComponent(jRadioButtonExportProteins)
								.addComponent(jCheckBoxCollapseProteins, javax.swing.GroupLayout.PREFERRED_SIZE, 198,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jCheckBoxCollapsePeptides)).addContainerGap()));
		jPanelOptionsLayout.setVerticalGroup(jPanelOptionsLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanelOptionsLayout.createSequentialGroup()
						.addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jCheckBoxIncludeDecoy).addComponent(jRadioButtonExportProteins))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jCheckBoxIncludeExperimentReplicateOrigin)
								.addComponent(jRadioButtonExportPeptides))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jCheckBoxIncludeGeneInfo).addComponent(jCheckBoxCollapseProteins))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jCheckBoxIncludeNonConclusiveProteins)
								.addComponent(jCheckBoxCollapsePeptides))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jCheckBoxSearchForProteinSequence).addGap(14, 14, 14)));

		jButtonExport.setIcon(new javax.swing.ImageIcon(
				"C:\\Users\\Salva\\workspace\\miape-extractor\\src\\main\\resources\\excel_table.png")); // NOI18N
		jButtonExport.setText("Export");
		jButtonExport.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonExportActionPerformed(evt);
			}
		});

		jProgressBar1.setStringPainted(true);

		jButtonCancel.setIcon(new javax.swing.ImageIcon(
				"C:\\Users\\Salva\\workspace\\miape-extractor\\src\\main\\resources\\stop.png")); // NOI18N
		jButtonCancel.setText("Cancel");
		jButtonCancel.setEnabled(false);
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCancelActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel2Layout.createSequentialGroup().addComponent(jButtonExport)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228,
												Short.MAX_VALUE)
										.addComponent(jButtonCancel))
						.addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
						.addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
						.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jButtonExport)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jButtonCancel)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
						.addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabelNumPeptides, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
								.addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jPanelOptions, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabelNumPeptides, javax.swing.GroupLayout.PREFERRED_SIZE, 16,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanelOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 163,
								javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 498) / 2, (screenSize.height - 419) / 2, 498, 419);
	}// </editor-fold>
		// GEN-END:initComponents

	private void jRadioButtonExportProteinsActionPerformed(java.awt.event.ActionEvent evt) {
		if (jRadioButtonExportProteins.isSelected()) {
			jCheckBoxCollapsePeptides.setEnabled(false);
			jCheckBoxCollapseProteins.setEnabled(true);
			updateSizesLabels();
		}
	}

	private void jRadioButtonExportPeptidesActionPerformed(java.awt.event.ActionEvent evt) {
		if (jRadioButtonExportPeptides.isSelected()) {
			jCheckBoxCollapsePeptides.setEnabled(true);
			jCheckBoxCollapseProteins.setEnabled(false);
			updateSizesLabels();
		}
	}

	private void jCheckBoxIncludeDecoyActionPerformed(java.awt.event.ActionEvent evt) {
		updateSizesLabels();
	}

	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
		exporter.cancel(true);

	}

	private void jCheckBoxCollapseProteinsActionPerformed(java.awt.event.ActionEvent evt) {
		updateSizesLabels();
	}

	private void updateSizesLabels() {
		if (idSets.isEmpty())
			return;
		Integer sizeProteins = null;
		Set<ProteinComparatorKey> keys = new HashSet<ProteinComparatorKey>();

		if (jRadioButtonExportProteins.isSelected()) {
			if (jCheckBoxCollapseProteins.isSelected()) {
				for (IdentificationSet idSet : idSets) {
					HashMap<String, ProteinGroupOccurrence> proteinGroupOccurrenceList = idSet
							.getProteinGroupOccurrenceList();
					for (String proteinGroupKey : proteinGroupOccurrenceList.keySet()) {
						ProteinGroupOccurrence pg = proteinGroupOccurrenceList.get(proteinGroupKey);
						if (!isNonConclusiveProteinsIncluded() && pg.getEvidence() == ProteinEvidence.NONCONCLUSIVE) {
							continue;
						}
						if (!jCheckBoxIncludeDecoy.isSelected()) {
							if (pg.isDecoy()) {
								continue;
							}
						}
						ProteinComparatorKey pck = pg.getKey(this.chartManagerFrame.getComparisonType());

						if (filter != null) {
							if (filter.canCheck(pck) && filter.isValid(pck)) {
								keys.add(pck);
							}
						} else {
							keys.add(pck);
						}
					}
				}
				// sizeProteins =
				// idSet.getNumDifferentProteinGroups(isNonConclusiveProteinsIncluded());
				sizeProteins = keys.size();
			} else {
				Set<ProteinComparatorKey> accs = new HashSet<ProteinComparatorKey>();

				for (IdentificationSet idSet : idSets) {
					// export proteins and no collapse
					List<ProteinGroup> identifiedProteinGroups = idSet.getIdentifiedProteinGroups();
					for (ProteinGroup proteinGroup : identifiedProteinGroups) {

						if (!isNonConclusiveProteinsIncluded()
								&& proteinGroup.getEvidence() == ProteinEvidence.NONCONCLUSIVE) {
							continue;
						}
						if (!jCheckBoxIncludeDecoy.isSelected()) {
							if (proteinGroup.isDecoy()) {
								continue;
							}
						}
						ProteinComparatorKey pck = new ProteinComparatorKey(proteinGroup.getAccessions(),
								chartManagerFrame.getComparisonType());

						if (filter != null) {
							if (filter.canCheck(pck) && filter.isValid(pck)) {
								accs.add(pck);
							}
						} else {
							accs.add(pck);
						}
					}
				}
				// sizeProteins =
				// idSet.getTotalNumProteinGroups(isNonConclusiveProteinsIncluded());
				sizeProteins = accs.size();
			}
		}
		// removed because is already taken into account in the loop
		// if (sizeProteins != null && !jCheckBoxIncludeDecoy.isSelected()) {
		// if (jCheckBoxCollapseProteins.isSelected()) {
		// sizeProteins = sizeProteins -
		// idSet.getNumDifferentProteinGroupsDecoys();
		// } else {
		// sizeProteins = sizeProteins - idSet.getNumProteinGroupDecoys();
		// }
		// }
		Set<String> peptideKeys = new HashSet<String>();
		Integer sizePeptides = null;
		if (jRadioButtonExportPeptides.isSelected()) {
			if (jCheckBoxCollapsePeptides.isSelected()) {
				for (IdentificationSet idSet : idSets) {
					HashMap<String, PeptideOccurrence> peptideOccurrenceList = idSet
							.getPeptideOccurrenceList(isDistinguishModifiedPeptides());
					for (String peptideKey : peptideOccurrenceList.keySet()) {
						ExtendedIdentifiedPeptide peptide = peptideOccurrenceList.get(peptideKey).getBestPeptide();
						// if (!isNonConclusiveProteinsIncluded()
						// && peptide.getRelation() ==
						// PeptideRelation.NONDISCRIMINATING) {
						// continue;
						// }
						if (!jCheckBoxIncludeDecoy.isSelected()) {
							if (peptide.isDecoy()) {
								continue;
							}
						}
						String sequence = peptide.getKey(isDistinguishModifiedPeptides());
						if (filter != null) {

							// export peptides from the peptides that pass the
							// filter:

							if (filter.canCheck(sequence) && filter.isValid(sequence)) {
								peptideKeys.add(sequence);
							}
						} else {
							peptideKeys.add(sequence);
						}
					}
				}
				sizePeptides = peptideKeys.size();
				// sizePeptides = idSet.getNumDifferentPeptides(true);
			} else {
				int num = 0;

				for (IdentificationSet idSet : idSets) {
					List<ExtendedIdentifiedPeptide> identifiedPeptides = idSet.getIdentifiedPeptides();
					for (ExtendedIdentifiedPeptide peptide : identifiedPeptides) {
						if (!jCheckBoxIncludeDecoy.isSelected()) {
							if (peptide.isDecoy()) {
								continue;
							}
						}
						// if (!isNonConclusiveProteinsIncluded()
						// && peptide.getRelation() ==
						// PeptideRelation.NONDISCRIMINATING) {
						// continue;
						// }
						if (filter != null) {
							String sequence = peptide.getKey(isDistinguishModifiedPeptides());
							if (filter.canCheck(sequence) && filter.isValid(sequence)) {
								num++;
							} else {
								// log.info(sequence + " discarded");
							}
						} else {
							num++;
						}
					}
					log.info(num + "/" + identifiedPeptides.size());
				}

				// sizePeptides = idSet.getTotalNumPeptides();
				sizePeptides = num;
			}
		}
		// removed because is already taken into account in the loop
		// if (sizePeptides != null && !jCheckBoxIncludeDecoy.isSelected()) {
		// if (jCheckBoxCollapseProteins.isSelected())
		// sizePeptides = sizePeptides -
		// idSet.getNumDifferentPeptideDecoys(true);
		// else
		// sizePeptides = sizePeptides - idSet.getNumPeptideDecoys();
		// }
		jLabelNumPeptides.setText(getNumString(sizePeptides, sizeProteins));

	}

	private void jCheckBoxCollapsePeptidesActionPerformed(java.awt.event.ActionEvent evt) {
		updateSizesLabels();
	}

	private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {
		log.info("Export button is pressed");
		final File file = getFile();
		setControlStatusToDisabled();
		jButtonCancel.setEnabled(true);
		jProgressBar1.setIndeterminate(true);
		exporter = new TSVExporter(this, idSets, file, this.filter);
		exporter.setDistinguisModificatedPeptides(isDistinguishModifiedPeptides());
		exporter.addPropertyChangeListener(this);
		exporter.execute();

	}

	@Override
	public boolean isReplicateAndExperimentOriginIncluded() {
		return jCheckBoxIncludeExperimentReplicateOrigin.isSelected();
	}

	@Override
	public boolean isDecoyHitsIncluded() {
		return jCheckBoxIncludeDecoy.isSelected();
	}

	@Override
	public boolean showPeptides() {
		return jRadioButtonExportPeptides.isSelected();

	}

	@Override
	public boolean isGeneInfoIncluded() {

		return jCheckBoxIncludeGeneInfo.isSelected();

	}

	@Override
	public boolean retrieveProteinSequences() {
		return jCheckBoxSearchForProteinSequence.isSelected();
	}

	private void jButtonSelectActionPerformed(java.awt.event.ActionEvent evt) {
		jTextFieldFilePath.setText(selectFile());
	}

	public String selectFile() {
		String filename = "";
		jFileChooser1 = new javax.swing.JFileChooser(MainFrame.currentFolder);
		jFileChooser1.setDialogTitle("Select an existing file or type the name for a new one");
		jFileChooser1.setFileFilter(new TsvFileFilter());
		jFileChooser1.showOpenDialog(this);
		File selectedFile = jFileChooser1.getSelectedFile();
		if (selectedFile != null) {
			MainFrame.currentFolder = selectedFile.getParentFile();
			filename = selectedFile.toString();
			String extension = FilenameUtils.getExtension(filename);
			if (extension == null || "".equals(extension)) {
				filename = filename + ".tsv";
			}
			log.info("Selected File: " + filename);
		} else
			filename = "null";
		return filename;
	}

	private File getFile() {
		final String path = jTextFieldFilePath.getText();
		validatePath(path);
		return new File(path);
	}

	private void validatePath(String path) {
		if ("".equals(path)) {
			JOptionPane.showMessageDialog(this, "Select the output path", "Error", JOptionPane.WARNING_MESSAGE);
		}

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ExporterDialog dialog = new ExporterDialog(null, null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JButton jButtonCancel;
	private javax.swing.JButton jButtonExport;
	private javax.swing.JButton jButtonSelect;
	private javax.swing.JCheckBox jCheckBoxCollapsePeptides;
	private javax.swing.JCheckBox jCheckBoxCollapseProteins;
	private javax.swing.JCheckBox jCheckBoxIncludeDecoy;
	private javax.swing.JCheckBox jCheckBoxIncludeExperimentReplicateOrigin;
	private javax.swing.JCheckBox jCheckBoxIncludeGeneInfo;
	private javax.swing.JCheckBox jCheckBoxIncludeNonConclusiveProteins;
	private javax.swing.JCheckBox jCheckBoxSearchForProteinSequence;
	private javax.swing.JFileChooser jFileChooser1;
	private javax.swing.JLabel jLabelNumPeptides;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	public javax.swing.JPanel jPanelOptions;
	private javax.swing.JProgressBar jProgressBar1;
	private javax.swing.JRadioButton jRadioButtonExportPeptides;
	private javax.swing.JRadioButton jRadioButtonExportProteins;
	private javax.swing.JTextField jTextFieldFilePath;

	// End of variables declaration//GEN-END:variables

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (TSVExporter.DATA_EXPORTING_DONE.equals(evt.getPropertyName())) {
			final File file = (File) evt.getNewValue();
			JOptionPane.showMessageDialog(this, "Data exported succesfully at " + file.getAbsolutePath(),
					"Data exported", JOptionPane.INFORMATION_MESSAGE);
			setControlStatusToPrevious();
			jButtonCancel.setEnabled(false);
			jButtonExport.setEnabled(true);
			jProgressBar1.setValue(0);
			jProgressBar1.setString("");

		} else if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			jProgressBar1.setValue(progress);
			jProgressBar1.setString("Exporting..." + progress + "%");
			jButtonExport.setEnabled(true);

		} else if (TSVExporter.DATA_EXPORTING_ERROR.equals(evt.getPropertyName())) {
			JOptionPane.showMessageDialog(this, "Error exporting data: " + evt.getNewValue(), "Error exporting data",
					JOptionPane.INFORMATION_MESSAGE);
			setControlStatusToPrevious();
			jButtonCancel.setEnabled(false);
			jButtonExport.setEnabled(true);

		} else if (TSVExporter.DATA_EXPORTING_CANCELED.equals(evt.getPropertyName())) {
			jProgressBar1.setValue(0);
			setControlStatusToPrevious();
			jButtonCancel.setEnabled(false);
			jButtonExport.setEnabled(true);
		} else if (TSVExporter.DATA_EXPORTING_SORTING.equals(evt.getPropertyName())) {
			int size = (Integer) evt.getNewValue();
			jProgressBar1.setIndeterminate(true);
			jProgressBar1.setString("Sorting " + size + " items by score...");
			jProgressBar1.setStringPainted(true);
		} else if (TSVExporter.DATA_EXPORTING_SORTING_DONE.equals(evt.getPropertyName())) {
			jProgressBar1.setIndeterminate(false);
		}
	}

	private void setControlStatusToPrevious() {
		jCheckBoxCollapsePeptides.setEnabled(previousCollapsePeptides);
		jCheckBoxCollapseProteins.setEnabled(previousCollapseProteins);
		jCheckBoxIncludeDecoy.setEnabled(previousIncludeDecoy);
		jCheckBoxIncludeExperimentReplicateOrigin.setEnabled(previousIncludeExperimentReplicate);
		jCheckBoxIncludeGeneInfo.setEnabled(previousIncludeGene);
		jCheckBoxSearchForProteinSequence.setEnabled(previousSearchForProteinSequence);
		jButtonExport.setEnabled(true);
		jRadioButtonExportPeptides.setEnabled(true);
		jRadioButtonExportProteins.setEnabled(true);
		jCheckBoxIncludeNonConclusiveProteins.setEnabled(previousExcludeNonConclusive);

	}

	private void setControlStatusToDisabled() {
		previousCollapsePeptides = jCheckBoxCollapsePeptides.isEnabled();
		previousCollapseProteins = jCheckBoxCollapseProteins.isEnabled();
		previousIncludeDecoy = jCheckBoxIncludeDecoy.isEnabled();
		previousIncludeExperimentReplicate = jCheckBoxIncludeExperimentReplicateOrigin.isEnabled();
		previousIncludeGene = jCheckBoxIncludeGeneInfo.isEnabled();
		previousSearchForProteinSequence = jCheckBoxSearchForProteinSequence.isEnabled();
		previousExcludeNonConclusive = jCheckBoxIncludeNonConclusiveProteins.isEnabled();

		jCheckBoxCollapsePeptides.setEnabled(false);
		jCheckBoxCollapseProteins.setEnabled(false);
		jCheckBoxIncludeDecoy.setEnabled(false);
		jCheckBoxIncludeExperimentReplicateOrigin.setEnabled(false);
		jCheckBoxIncludeGeneInfo.setEnabled(false);
		jCheckBoxSearchForProteinSequence.setEnabled(false);
		jRadioButtonExportProteins.setEnabled(false);
		jRadioButtonExportPeptides.setEnabled(false);
		jCheckBoxIncludeNonConclusiveProteins.setEnabled(false);
		jButtonExport.setEnabled(false);

	}

	@Override
	public boolean showBestPeptides() {
		return jCheckBoxCollapsePeptides.isSelected();
	}

	@Override
	public boolean showBestProteins() {
		return jCheckBoxCollapseProteins.isSelected();
	}

	@Override
	public boolean isNonConclusiveProteinsIncluded() {
		return jCheckBoxIncludeNonConclusiveProteins.isSelected();
	}

	@Override
	public boolean isFDRApplied() {
		return isFDRApplied;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.proteored.pacom.analysis.exporters.ExporterManager#
	 * isDistinguishModifiedPeptides()
	 */
	@Override
	public boolean isDistinguishModifiedPeptides() {
		if (this.distinguisModifiedPeptides != null) {
			return this.distinguisModifiedPeptides;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.proteored.pacom.analysis.exporters.ExporterManager#getComparisonType(
	 * )
	 */
	@Override
	public ProteinGroupComparisonType getComparisonType() {
		return this.chartManagerFrame.getComparisonType();
	}

}