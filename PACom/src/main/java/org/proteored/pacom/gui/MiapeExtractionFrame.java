/*
 * Standard2MIAPEDialog.java Created on __DATE__, __TIME__
 */

package org.proteored.pacom.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker.StateValue;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.factories.MiapeDocumentFactory;
import org.proteored.miapeapi.factories.ms.MiapeMSDocumentFactory;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.xml.ms.MIAPEMSXmlFile;
import org.proteored.miapeapi.xml.ms.MiapeMSDocumentImpl;
import org.proteored.miapeapi.xml.ms.MiapeMSXmlFactory;
import org.proteored.miapeapi.xml.ms.merge.MiapeMSMerger;
import org.proteored.pacom.analysis.gui.tasks.MiapeRetrieverManager;
import org.proteored.pacom.analysis.util.FileManager;
import org.proteored.pacom.gui.miapemsforms.MetadataLoader;
import org.proteored.pacom.gui.tasks.LoadProjectsTask;
import org.proteored.pacom.gui.tasks.MIAPEMSChecker;
import org.proteored.pacom.gui.tasks.MiapeExtractionTask;
import org.proteored.pacom.gui.tasks.OntologyLoaderTask;
import org.proteored.pacom.gui.tasks.OntologyLoaderWaiter;
import org.proteored.pacom.utils.HttpUtilities;
import org.proteored.pacom.utils.MiapeExtractionParametersUtil;
import org.proteored.pacom.utils.MiapeExtractionResult;
import org.proteored.pacom.utils.MiapeExtractionRunParameters;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 *
 * @author __USER__
 */
public class MiapeExtractionFrame extends javax.swing.JFrame
		implements PropertyChangeListener, MiapeExtractionRunParameters {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");
	private static MiapeExtractionFrame instance;
	private static final String MZIDENTML_FILE_LABEL = "mzIdentML file:";
	private static final String MZML_FILE_LABEL = "mzML file:";
	private static final String MGF_FILE_LABEL = "mgf file:";
	private static final String NOT_APPLICABLE = "not applicable";
	private static final String PRIDE_FILE_LABEL = "PRIDE xml file:";
	private static final String XTANDEM_FILE_LABEL = "X!Tandem xml file:";
	private static final String DTASELECT_FILE_LABEL = "DTASelect file:";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public void dispose() {
		if (miapeExtractionTask != null && miapeExtractionTask.getState() == StateValue.STARTED) {
			boolean canceled = miapeExtractionTask.cancel(true);
			while (!canceled) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				canceled = miapeExtractionTask.cancel(true);
			}
		}
		if (miapeMSChecker != null)
			miapeMSChecker.cancel(true);
		if (mainFrame != null) {
			mainFrame.setEnabled(true);
			mainFrame.setVisible(true);
		}
		super.dispose();
	}

	private static final int ALLMODE = -1;
	private static final int PRIDEXMLMODE = 0;
	// private static final int MGFMZIDENTMLMODE = 1;
	private static final int MZIDENTMLMODE = 2;
	private static final int MZMLMODE = 3;
	private static final int MGFMODE = 4;
	private static final int DTASELECTMODE = 5;
	private static final int XTANDEMMODE = 6;
	// for mzML conversion
	public boolean isFastParsing = false;
	public boolean isShallowParsing = false;
	private MIAPEMSChecker miapeMSChecker;
	private boolean extractionStarted = false;
	private int currentUserID;
	private boolean listenToItemEvents;

	public static MiapeExtractionFrame getInstance(MainFrame mainFrame2, boolean b) {
		if (instance == null) {
			instance = new MiapeExtractionFrame(mainFrame2, b);
		}
		instance.mainFrame = mainFrame2;
		instance.initializeFrame();
		instance.changeRadioStatus();

		return instance;
	}

	/** Creates new form Standard2MIAPEDialog */
	private MiapeExtractionFrame(MainFrame parent, boolean modal) {

		// super(parent, modal);
		initComponents();
		try {
			UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (UnsupportedLookAndFeelException ex) {
		}

		if (parent != null) {
			ftpPath = MainFrame.ftpPath;
			mainFrame = parent;
			mainFrame.setEnabled(false);
			mainFrame.setVisible(false);
			currentUserID = mainFrame.userID;
			// autoscroll in the status field
			// this.mainFrame.autoScroll(jScrollPane1, jTextAreaStatus);
		} else {

		}
		changeRadioStatus();
		// Load projects in background
		loadProjects(false, storeMIAPEsInDB());

		FileManager.deleteMetadataFile(MIAPEMSChecker.CURRENT_MZML);
		FileManager.deleteMetadataFile(MIAPEMSChecker.CURRENT_PRIDEXML);

		// set icon image
		setIconImage(ImageManager.getImageIcon(ImageManager.PROTEORED_MIAPE_API).getImage());
		jButtonSubmit.setIcon(ImageManager.getImageIcon(ImageManager.ADD));
		jButtonSubmit.setPressedIcon(ImageManager.getImageIcon(ImageManager.ADD_CLICKED));
		jButtonClearStatus.setIcon(ImageManager.getImageIcon(ImageManager.CLEAR));
		jButtonClearStatus.setPressedIcon(ImageManager.getImageIcon(ImageManager.CLEAR_CLICKED));
		jButtonEditMetadata.setIcon(ImageManager.getImageIcon(ImageManager.FINISH));
		jButtonEditMetadata.setPressedIcon(ImageManager.getImageIcon(ImageManager.FINISH_CLICKED));

		// wait for the ontology loading. When done, it will notify to this
		// class and metadata combo will be able to be filled
		appendStatus("Loading ontologies...");
		OntologyLoaderWaiter waiter = new OntologyLoaderWaiter();
		waiter.addPropertyChangeListener(this);
		waiter.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		// check if the mainFrame user id has change. In that case, remove the
		// loaded projects
		if (currentUserID != mainFrame.userID) {
			currentUserID = mainFrame.userID;
			loadProjects(true, storeMIAPEsInDB());
		}
		if (mainFrame != null) {
			mainFrame.setVisible(!b);
		}
		super.setVisible(b);
	}

	/**
	 * Loads projects from repository in background
	 */
	private void loadProjects(boolean forceChange, boolean remoteStorage) {
		if (loadedProjects == null || loadedProjects.isEmpty() || forceChange) {
			if ((MainFrame.userName != null && MainFrame.password != null && remoteStorage) || !remoteStorage) {
				if (loadedProjects != null)
					loadedProjects.clear();
				LoadProjectsTask loadProjectsThread = new LoadProjectsTask(this, !remoteStorage, currentUserID,
						MainFrame.userName, MainFrame.password);
				loadProjectsThread.addPropertyChangeListener(this);
				loadProjectsThread.execute();
			}
		}

	}

	public void initMetadataCombo(String selectedConfigurationName, ControlVocabularyManager cvManager) {
		if (!isMzIdentMLSelected() && !isXTandemSelected() && !(isPRIDESelected() && !isMIAPEMSChecked())) {
			jButtonEditMetadata.setEnabled(true);
			jComboBoxMetadata.setEnabled(true);
			jLabelMiapeMSMetadata.setEnabled(true);
		}

		final List<String> metadataList = FileManager.getMetadataList(cvManager);
		// sort by name
		Collections.sort(metadataList);
		if (metadataList != null) {
			metadataList.add(0, "");
			jComboBoxMetadata.setModel(new DefaultComboBoxModel(metadataList.toArray()));
			if (selectedConfigurationName != null)
				jComboBoxMetadata.setSelectedItem(selectedConfigurationName);
			else
				jLabelMiapeMSMetadata.setText("");
		}
		if (!metadataList.isEmpty() && !"".equals(metadataList.get(0))) {
			appendStatus("Metadata templates loaded.");
		} else {
			appendStatus("No metadata templates found in the file system.");
		}
	}

	private void initializeFrame() {
		jTextFieldInputFile.setText("");
		jTextFieldInputFile2.setText("");
		jComboBoxMetadata.setSelectedIndex(0);
		jTextAreaStatus.setText("");
		jTextFieldProjectName.setText("");
		jProgressBar.setIndeterminate(false);
		this.setCursor(null); // turn off the wait cursor
		jButtonSubmit.setEnabled(true);
		if (miapeExtractionTask != null) {
			boolean canceled = miapeExtractionTask.cancel(true);
			log.info("Task canceled=" + canceled);
			miapeExtractionTask = null;
		}
		extractionStarted = false;

	}

	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		buttonGroupInputFileFormat = new javax.swing.ButtonGroup();
		jFileChooser = new JFileChooser(MainFrame.currentFolder);
		buttonGroupProcessingType = new javax.swing.ButtonGroup();
		buttonGroupStoreOrNotStore = new javax.swing.ButtonGroup();
		jPanel1 = new javax.swing.JPanel();
		jTextFieldInputFile = new javax.swing.JTextField();
		jTextFieldInputFile.setEnabled(false);
		jButtonInputFile = new javax.swing.JButton();
		jButtonInputFile.setEnabled(false);
		jPanel4 = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		jCheckBoxMS = new javax.swing.JCheckBox();
		jCheckBoxMS.setEnabled(false);
		jCheckBoxMSI = new javax.swing.JCheckBox();
		jCheckBoxMSI.setEnabled(false);
		jCheckBoxMSI.setSelected(true);
		jPanel8 = new javax.swing.JPanel();
		jCheckBoxLocalProcessinInParallel = new javax.swing.JCheckBox();
		inputFileLabel1 = new javax.swing.JLabel();
		inputFileLabel1.setEnabled(false);
		inputFileLabel2 = new javax.swing.JLabel();
		jTextFieldInputFile2 = new javax.swing.JTextField();
		jButtonInputFile2 = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jPanel5 = new javax.swing.JPanel();
		jComboBoxMetadata = new javax.swing.JComboBox();
		jComboBoxMetadata.setEnabled(false);
		jLabelMiapeMSMetadata = new javax.swing.JLabel();
		jButtonEditMetadata = new javax.swing.JButton();
		jButtonEditMetadata.setEnabled(false);
		jPanel2 = new javax.swing.JPanel();
		jTextFieldProjectName = new javax.swing.JTextField();
		jButtonProject = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextAreaStatus = new javax.swing.JTextArea();
		jProgressBar = new javax.swing.JProgressBar();
		jButtonClearStatus = new javax.swing.JButton();
		jButtonSubmit = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Import data");
		setResizable(false);

		jPanel1.setBorder(
				new TitledBorder(null, "Select input file(s)", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		jButtonInputFile.setText("Select file");
		jButtonInputFile.setToolTipText("Select an input file to extract the information");
		jButtonInputFile.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonInputFileActionPerformed(evt);
			}
		});

		jPanel4.setBorder(null);

		JPanel panel = new JPanel();
		panel.setToolTipText(
				"<html>Input type + MS data file:<br>\r\nUse one of these options if you want later to export a PRIDE XML file containing the spectra. \r\n</html>");
		panel.setBorder(new TitledBorder(null, "Input type + MS data file", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Input type", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 149, Short.MAX_VALUE)
								.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup()
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(60, Short.MAX_VALUE)));
		jRadioButtonMzIdentML = new javax.swing.JRadioButton();
		jRadioButtonMzIdentML.setSelected(true);

		buttonGroupInputFileFormat.add(jRadioButtonMzIdentML);
		jRadioButtonMzIdentML.setText("mzIdentML");
		jRadioButtonMzIdentML.setToolTipText(
				"<html>Extract MIAPE MSI information from a mzIdentML file.<br>\nIf a metadata template is used, a MIAPE MS will be also created.</html>");
		jRadioButtonPRIDE = new javax.swing.JRadioButton();

		buttonGroupInputFileFormat.add(jRadioButtonPRIDE);
		jRadioButtonPRIDE.setText("PRIDE XML");
		jRadioButtonPRIDE.setToolTipText(
				"<html>Extract MIAPE MS and/or MSI information from a PRIDE XML file.<br>\nIf a metadata template is used, its information will be added to the resulting MIAPE MS document.</html>");
		jRadioButtonPRIDE.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonPRIDEActionPerformed(evt);
			}
		});
		jRadioButtonXTandem = new javax.swing.JRadioButton();

		buttonGroupInputFileFormat.add(jRadioButtonXTandem);
		jRadioButtonXTandem.setText("XTandem XML");
		jRadioButtonXTandem.setToolTipText(
				"<html>Extract MIAPE MSI information from a XTandem XML result file.<br>\nIf a metadata template is used, a MIAPE MS will be also created.</html>");
		jRadioButtonXTandem.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonXTandemActionPerformed(evt);
			}
		});
		jRadioButtonDTASelect = new javax.swing.JRadioButton();

		buttonGroupInputFileFormat.add(jRadioButtonDTASelect);
		jRadioButtonDTASelect.setText("DTASelect");
		jRadioButtonDTASelect.setToolTipText(
				"<html>Extract MIAPE MSI information from a DTASelect result file.<br>\nIf a metadata template is used, a MIAPE MS will be also created.</html>");
		jRadioButtonDTASelect.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonDTASelectActionPerformed(evt);
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(jRadioButtonMzIdentML)
								.addComponent(jRadioButtonPRIDE).addComponent(jRadioButtonDTASelect)
								.addComponent(jRadioButtonXTandem))
						.addContainerGap(75, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(jRadioButtonMzIdentML)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(jRadioButtonPRIDE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(jRadioButtonDTASelect)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(jRadioButtonXTandem)
						.addContainerGap()));
		panel_1.setLayout(gl_panel_1);
		jRadioButtonMzIdentML.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonMzIdentMLActionPerformed(evt);
			}
		});
		jRadioButtonMzIdentMLMGF = new javax.swing.JRadioButton();

		buttonGroupInputFileFormat.add(jRadioButtonMzIdentMLMGF);
		jRadioButtonMzIdentMLMGF.setText("mzIdentML + mgf");
		jRadioButtonMzIdentMLMGF.setToolTipText(
				"<html>Imports dataset from a mzIdentML file and keeps the PSMs linked to the spectra using a MGF file.<br>\r\nA PRIDE XML file could be created just in case of using a mgf file that has been used directly in the search.<br>\r\nA metadata template will be mandatory in order to complete the Mass Spectrometry metadata information.</html>");
		jRadioButtonMzIdentMLMGF.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonMzIdentMLMGFActionPerformed(evt);
			}
		});
		jRadioButtonMzMLMzIdentML = new javax.swing.JRadioButton();

		buttonGroupInputFileFormat.add(jRadioButtonMzMLMzIdentML);
		jRadioButtonMzMLMzIdentML.setText("mzIdentML + mzML");
		jRadioButtonMzMLMzIdentML.setToolTipText(
				"<html>Imports dataset from a mzIdentML file and keeps the PSMs linked to the spectra using a mzML file.<br>\r\nA PRIDE XML file could be created just in case of using a mzML file that has been used directly in the search.<br>\r\nThe use of a metadata template will be optional in this case.</html>");
		jRadioButtonXTandemMGF = new javax.swing.JRadioButton();

		buttonGroupInputFileFormat.add(jRadioButtonXTandemMGF);
		jRadioButtonXTandemMGF.setText("XTandem XML + mgf");
		jRadioButtonXTandemMGF.setToolTipText(
				"<html>Imports dataset from a XTandem XML output file and keeps the PSMs linked to the spectra using a mgf file.<br>\r\nA PRIDE XML file could be created just in case of using a mgf file that has been used directly in the search.<br>\r\nA metadata template will be mandatory in order to complete the Mass Spectrometry metadata information.</html>");
		jRadioButtonXTandemMGF.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonXTandemMGFActionPerformed(evt);
			}
		});
		jRadioButtonDTASelectMGF = new javax.swing.JRadioButton();

		buttonGroupInputFileFormat.add(jRadioButtonDTASelectMGF);
		jRadioButtonDTASelectMGF.setText("DTASelect-filter + mgf");
		jRadioButtonDTASelectMGF.setToolTipText(
				"<html>Imports dataset from a DTASelect-filter.txt file and keeps the PSMs linked to the spectra using a mgf file.<br>\r\nA PRIDE XML file could be created just in case of using a mgf file that has been used directly in the search.<br>\r\nA metadata template will be mandatory in order to complete the Mass Spectrometry metadata information.</html>");
		jRadioButtonDTASelectMGF.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonDTASelectMGFActionPerformed(evt);
			}
		});

		JLabel lblforPrideExport = new JLabel("(for PRIDE export)");
		lblforPrideExport.setToolTipText(
				"<html>Input type + MS data file:<br>\r\nUse one of these options if you want later to export a PRIDE XML file containing the spectra. \r\n</html>");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(jRadioButtonXTandemMGF, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false).addComponent(lblforPrideExport)
								.addComponent(jRadioButtonDTASelectMGF, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jRadioButtonMzIdentMLMGF, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jRadioButtonMzMLMzIdentML, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addComponent(lblforPrideExport)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(jRadioButtonMzIdentMLMGF)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(jRadioButtonMzMLMzIdentML)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(jRadioButtonXTandemMGF)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(jRadioButtonDTASelectMGF)
						.addContainerGap(9, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		jRadioButtonMzMLMzIdentML.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jRadioButtonMzMLMzIdentMLActionPerformed(evt);
			}
		});
		jPanel4.setLayout(jPanel4Layout);

		jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Output data type(s)"));
		jPanel6.setToolTipText("<html>Types of data that is going to be extracted and imported/html>");
		jCheckBoxMS.setText("Mass Spectrometry data");
		jCheckBoxMS.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				jCheckBoxMSItemStateChanged(evt);
			}
		});

		jCheckBoxMSI.setText("Protein/Peptide identification data");

		javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(jPanel6Layout);
		jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jCheckBoxMS).addComponent(jCheckBoxMSI));
		jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel6Layout.createSequentialGroup().addComponent(jCheckBoxMS)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBoxMSI)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel8.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Processing type",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		jPanel8.setToolTipText("Processing type");

		jCheckBoxLocalProcessinInParallel.setSelected(true);
		jCheckBoxLocalProcessinInParallel.setText("multi-core processing");

		javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
		jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, jPanel8Layout.createSequentialGroup().addContainerGap()
						.addComponent(jCheckBoxLocalProcessinInParallel).addContainerGap(82, Short.MAX_VALUE)));
		jPanel8Layout
				.setVerticalGroup(jPanel8Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel8Layout.createSequentialGroup().addContainerGap()
								.addComponent(jCheckBoxLocalProcessinInParallel)
								.addContainerGap(173, Short.MAX_VALUE)));
		jPanel8.setLayout(jPanel8Layout);

		inputFileLabel1.setText("not applicable:");

		inputFileLabel2.setText("mzIdentML file:");

		jButtonInputFile2.setText("Select file");
		jButtonInputFile2.setToolTipText("Select a standard xml file to extract the input information");
		jButtonInputFile2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonInputFile2ActionPerformed(evt);
			}
		});

		jScrollPane2.setBorder(null);
		jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		jPanel5.setBorder(new TitledBorder(null, "Mass Spectrometry metadata (for PRIDE export)", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		jPanel5.setToolTipText(
				"<html>In case of using input files with MIAPE Mass Spectrometry information,<br>you can predefine some required<br> metadata to complement data from mzML or PRIDE XML.</html>");

		jComboBoxMetadata
				.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Loading metadata templates..." }));
		jComboBoxMetadata.addItemListener(new java.awt.event.ItemListener() {
			@Override
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				jComboBoxMetadataItemStateChanged(evt);
			}
		});

		jLabelMiapeMSMetadata.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jLabelMiapeMSMetadata.setAutoscrolls(true);

		jButtonEditMetadata.setIcon(new javax.swing.ImageIcon(
				"C:\\Users\\Salva\\workspace\\miape-extractor\\src\\main\\resources\\finish.png")); // NOI18N
		jButtonEditMetadata.setText("Edit");
		jButtonEditMetadata.setToolTipText("Inspect Mass Spectrometry metadata");
		jButtonEditMetadata.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonEditMetadataActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel5Layout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(jLabelMiapeMSMetadata, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jComboBoxMetadata, Alignment.LEADING, 0, 263, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(jButtonEditMetadata)
				.addContainerGap(340, Short.MAX_VALUE)));
		jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel5Layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jComboBoxMetadata, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jButtonEditMetadata))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jLabelMiapeMSMetadata, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel5.setLayout(jPanel5Layout);

		jScrollPane2.setViewportView(jPanel5);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel1Layout
				.createSequentialGroup().addContainerGap()
				.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(jPanel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE))
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
										.addComponent(inputFileLabel2).addComponent(inputFileLabel1))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(jTextFieldInputFile2).addComponent(jTextFieldInputFile,
												GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE))
								.addGap(18)
								.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
										.addComponent(jButtonInputFile2).addComponent(jButtonInputFile))))
				.addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(inputFileLabel1)
								.addComponent(jTextFieldInputFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jButtonInputFile))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(inputFileLabel2)
						.addComponent(jTextFieldInputFile2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(jButtonInputFile2))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
						.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 308, Short.MAX_VALUE)
						.addGroup(jPanel1Layout.createSequentialGroup()
								.addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(jPanel8,
										GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addContainerGap()));
		jPanel1.setLayout(jPanel1Layout);

		jPanel2.setBorder(new TitledBorder(null, "Select (or create) a project name", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		jPanel2.setToolTipText(
				"<html>Write directly a new project name to create a new<br>\n project in which the data will be stored.</html>");

		jTextFieldProjectName.setToolTipText(
				"<html>Write directly a new project name to create a new<br>\n project in which the data will be stored.</html>");

		jButtonProject.setText("Select project");
		jButtonProject
				.setToolTipText("<html>Select one of your projects<br>or write a new name to create a new one</html>");
		jButtonProject.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonProjectActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel2Layout.createSequentialGroup().addContainerGap()
								.addComponent(jTextFieldProjectName, javax.swing.GroupLayout.DEFAULT_SIZE, 667,
										Short.MAX_VALUE)
								.addGap(18, 18, 18).addComponent(jButtonProject).addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jButtonProject).addComponent(jTextFieldProjectName,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));

		jScrollPane1.setAutoscrolls(true);

		jTextAreaStatus.setColumns(20);
		jTextAreaStatus.setEditable(false);
		jTextAreaStatus.setFont(new java.awt.Font("Dialog", 0, 10));
		jTextAreaStatus.setRows(5);
		jTextAreaStatus.setToolTipText("Task status");
		jScrollPane1.setViewportView(jTextAreaStatus);

		jButtonClearStatus.setText("Clear status");
		jButtonClearStatus.setToolTipText("Clear the status panel");
		jButtonClearStatus.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonClearStatusActionPerformed(evt);
			}
		});

		jButtonSubmit.setText("Import data");
		jButtonSubmit.setToolTipText("Start with the extraction of input data");
		jButtonSubmit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonSubmitActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout
				.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
								.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 788,
												Short.MAX_VALUE)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
								.addGroup(
										jPanel3Layout.createSequentialGroup().addComponent(jButtonClearStatus)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														578, Short.MAX_VALUE)
												.addComponent(jButtonSubmit)))
								.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jButtonClearStatus).addComponent(jButtonSubmit,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup().addContainerGap()
										.addGroup(
												layout.createParallelGroup(Alignment.LEADING)
														.addGroup(layout.createSequentialGroup()
																.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 874,
																		Short.MAX_VALUE)
																.addContainerGap())
										.addGroup(
												layout.createSequentialGroup()
														.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 874,
																Short.MAX_VALUE)
														.addContainerGap()).addComponent(jPanel1, Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE).addContainerGap()));
		getContentPane().setLayout(layout);

		pack();
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension dialogSize = getSize();
		setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
	}// </editor-fold>

	private void jCheckBoxMSItemStateChanged(java.awt.event.ItemEvent evt) {
		if (!jCheckBoxMS.isSelected()) {
			jComboBoxMetadata.setSelectedIndex(0);
		}
		jComboBoxMetadata.setEnabled(jCheckBoxMS.isSelected());
		jButtonEditMetadata.setEnabled(jCheckBoxMS.isSelected());
	}

	private void jButtonEditMetadataActionPerformed(java.awt.event.ActionEvent evt) {
		if (miapeMSChecker == null || !miapeMSChecker.getState().equals(StateValue.STARTED)) {
			if (miapeMSChecker != null)
				miapeMSChecker.cancel(true);

			boolean extractFromStandardFile;
			if ("".equals(jComboBoxMetadata.getSelectedItem()))
				extractFromStandardFile = true;
			else
				extractFromStandardFile = false;
			miapeMSChecker = new MIAPEMSChecker(this, extractFromStandardFile);
			miapeMSChecker.addPropertyChangeListener(this);
			miapeMSChecker.execute();
			appendStatus("Opening metadata editor...");

		} else
			appendStatus("MIAPE MS metadata is currently being checked. Try again later");
	}

	private void jComboBoxMetadataItemStateChanged(java.awt.event.ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			String metadataFileName = (String) jComboBoxMetadata.getSelectedItem();
			MetadataLoader metadataLoader = new MetadataLoader(metadataFileName);
			metadataLoader.addPropertyChangeListener(this);
			metadataLoader.execute();

			if (metadataFileName != null && !"".equals(metadataFileName)) {
				jCheckBoxMS.setSelected(true);
			} else {
				jCheckBoxMS.setSelected(false);
			}
		}
	}

	// private void
	// jButtonAddAdditionalDataActionPerformed(java.awt.event.ActionEvent evt) {
	// // select the additional information
	// // open select instrument window
	// if (this.jRadioButtonMzIdentMLMGF.isSelected())
	// this.selectInstrument();
	// }

	private void changeRadioStatus() {
		if (mainFrame != null) {
			listenToItemEvents = false;
			if (MainFrame.userName != null && !"".equals(MainFrame.userName) && MainFrame.password != null
					&& !"".equals(MainFrame.password)) {

			} else {

				// enable multicore processing option
				jCheckBoxLocalProcessinInParallel.setEnabled(true);

			}
			listenToItemEvents = true;
		}

	}

	// private void showLocalProcessingWarn() {
	// if (!MainFrame.localWorkflow) {
	// String localProcessingMessage = "<html>If you select local processing,
	// you have two options:<br>"
	// + "<ul><li><b>Store in repository</b>: all the MIAPE information will be
	// extracted locally from the input files<br>"
	// + "and the extracted information will be sent to the ProteoRed MIAPE
	// repository.</li>"
	// + "<li><b>Fully local worflow</b>: all the MIAPE information will be
	// extracted locally from the input files<br>"
	// + "and the extracted information will stored in local files.<br>"
	// + "No interaction with ProteoRed MIAPE repository will be
	// performed.</li></ul></html>";
	// String title = "Local processing vs Remote processing";
	//
	// JOptionPane.showMessageDialog(this, localProcessingMessage, title,
	// JOptionPane.INFORMATION_MESSAGE);
	// }
	// }

	private void jRadioButtonMzMLMzIdentMLActionPerformed(java.awt.event.ActionEvent evt) {
		// disable and select MIAPE MS
		jCheckBoxMS.setEnabled(false);
		jCheckBoxMS.setSelected(true);
		// disable and select MIAPE MSI

		jCheckBoxMSI.setSelected(true);

		jComboBoxMetadata.setEnabled(true);
		jButtonEditMetadata.setEnabled(true);

		enablePrimaryInputTextFile(MZML_FILE_LABEL);
		enableSecondaryInputTextFile(MZIDENTML_FILE_LABEL);

		// set mzIdentML file to the secondary input file label
		String mzMLPlusmzIdentMLMessage = "<html>With this option, you will be able to create:<br><ul><li>a MIAPE MS document from the mzML file, and</li> <li>a MIAPE MSI document from the mzIdentML file</li></ul>"
				+ "If the mzIdentML file comes from a MASCOT search, later you will be able to<br>create a complete PRIDE XML file from both MIAPE MS and MSI documents<br>(option 'export MIAPE to PRIDE XML')</html>";
		// show mzML + mzIdentML warnning
		JOptionPane.showMessageDialog(this, mzMLPlusmzIdentMLMessage, "mzML + mzIdentML",
				JOptionPane.INFORMATION_MESSAGE);
		// show local processing warning
		// if (jRadioButtonLocalProcessing.isSelected())
		// showLocalProcessingWarn();
	}

	private void jButtonInputFile2ActionPerformed(java.awt.event.ActionEvent evt) {

		int mode = ALLMODE;
		if (jRadioButtonMzIdentML.isSelected() || jRadioButtonMzIdentMLMGF.isSelected()
				|| jRadioButtonMzMLMzIdentML.isSelected()) {
			mode = MZIDENTMLMODE;
		} else if (jRadioButtonXTandem.isSelected() || jRadioButtonXTandemMGF.isSelected()) {
			mode = XTANDEMMODE;
		} else if (jRadioButtonDTASelect.isSelected() || jRadioButtonDTASelectMGF.isSelected()) {
			mode = DTASELECTMODE;
		}
		// select the file
		String selectedFile = selectFile(mode);
		if (selectedFile.compareTo("null") == 0)
			log.info("ERROR: I/O");
		else {
			jTextFieldInputFile2.setText(selectedFile);
		}

	}

	private void jRadioButtonXTandemActionPerformed(java.awt.event.ActionEvent evt) {
		jComboBoxMetadata.setEnabled(false);
		jButtonEditMetadata.setEnabled(false);

		// the same as for mzIdentML
		jRadioButtonMzIdentMLActionPerformed(evt);

		disablePrimaryInputTextFile();
		enableSecondaryInputTextFile(XTANDEM_FILE_LABEL);

		// reset combo box, deleting current mzml if exists
		FileManager.deleteMetadataFile(MIAPEMSChecker.CURRENT_MZML);

	}

	private void jRadioButtonDTASelectActionPerformed(java.awt.event.ActionEvent evt) {
		jComboBoxMetadata.setEnabled(false);
		jButtonEditMetadata.setEnabled(false);
		// disable and not select MIAPE MS
		jCheckBoxMS.setEnabled(false);
		jCheckBoxMS.setSelected(false);
		// disable and select MIAPE MSI

		jCheckBoxMSI.setSelected(true);
		// the same as for mzIdentML
		jRadioButtonMzIdentMLActionPerformed(evt);

		disablePrimaryInputTextFile();
		enableSecondaryInputTextFile(DTASELECT_FILE_LABEL);

		// reset combo box, deleting current mzml if exists
		FileManager.deleteMetadataFile(MIAPEMSChecker.CURRENT_MZML);

	}

	private void jButtonClearStatusActionPerformed(java.awt.event.ActionEvent evt) {
		jTextAreaStatus.setText("");
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	private void jRadioButtonMzIdentMLActionPerformed(java.awt.event.ActionEvent evt) {
		jComboBoxMetadata.setEnabled(false);
		jButtonEditMetadata.setEnabled(false);

		// disable and not select MIAPE MS
		jCheckBoxMS.setEnabled(false);
		jCheckBoxMS.setSelected(false);
		// disable and select MIAPE MSI

		jCheckBoxMSI.setSelected(true);

		// disable secondary input file
		disablePrimaryInputTextFile();
		enableSecondaryInputTextFile(MZIDENTML_FILE_LABEL);

		// this.jLabelMiapeMSMetadata.setText("");
		// reset combo box, deleting current mzml if exists
		FileManager.deleteMetadataFile(MIAPEMSChecker.CURRENT_MZML);

	}

	private void jRadioButtonPRIDEActionPerformed(java.awt.event.ActionEvent evt) {
		jComboBoxMetadata.setEnabled(false);
		jButtonEditMetadata.setEnabled(false);
		// disable additional data labels
		// enable and check MIAPE MS checkbox
		jCheckBoxMS.setEnabled(true);
		// enable and check MIAPE MSI checkbox
		jCheckBoxMSI.setSelected(true);

		jComboBoxMetadata.setEnabled(jCheckBoxMS.isSelected());
		jButtonEditMetadata.setEnabled(jCheckBoxMS.isSelected());

		enablePrimaryInputTextFile(PRIDE_FILE_LABEL);
		disableSecondaryInputTextFile();

		// show check boxes tooltip
		showCheckBoxesTooltip();

		// reset combo box, deleting current mzml if exists
		FileManager.deleteMetadataFile(MIAPEMSChecker.CURRENT_MZML);

		String PRIDEMessage = "<html>With this option, you can also optionally import the Mass Spectrometry data and metadata<br>"
				+ "from the PRIDE XML file in order to keep it linked with the PSMs and be able to create<br>"
				+ "a new PRIDE XML potentially aggregating more datasets in a single file.</html>";
		// show mzML + mzIdentML warning
		JOptionPane.showMessageDialog(this, PRIDEMessage, "PRIDE", JOptionPane.INFORMATION_MESSAGE);
	}

	private void jRadioButtonMzIdentMLMGFActionPerformed(java.awt.event.ActionEvent evt) {
		// disable and select MIAPE MS
		jCheckBoxMS.setEnabled(false);
		jCheckBoxMS.setSelected(true);
		// disable and select MIAPE MSI

		jCheckBoxMSI.setSelected(true);

		jComboBoxMetadata.setEnabled(true);
		jButtonEditMetadata.setEnabled(true);

		enablePrimaryInputTextFile(MGF_FILE_LABEL);
		enableSecondaryInputTextFile(MZIDENTML_FILE_LABEL);

		String mgfPlusmzIdentMLMessage = "<html>With this option, you will be able to create <ul><li>a MIAPE MS document from the mgf file. "
				+ "(Some minimal information about the spectrometer<br>will be asked to you before to start the process).</li>"
				+ "<li>a MIAPE MSI document from the mzIdentML file.</li></ul>"
				+ "If the mzIdentML file comes from a MASCOT search that comes from the mgf file, "
				+ "later you will be able to create<br>a complete PRIDE XML file from both MIAPE MS and MSI documents (option MIAPE to Standard)</html>";
		// show mzML + mzIdentML warning
		JOptionPane.showMessageDialog(this, mgfPlusmzIdentMLMessage, "mgf + mzIdentML",
				JOptionPane.INFORMATION_MESSAGE);

		// show local processing warning
		// if (jRadioButtonLocalProcessing.isSelected())
		// showLocalProcessingWarn();

	}

	private void jRadioButtonXTandemMGFActionPerformed(java.awt.event.ActionEvent evt) {
		// disable and select MIAPE MS
		jCheckBoxMS.setEnabled(false);
		jCheckBoxMS.setSelected(true);
		// disable and select MIAPE MSI

		jCheckBoxMSI.setSelected(true);

		jComboBoxMetadata.setEnabled(true);
		jButtonEditMetadata.setEnabled(true);

		enablePrimaryInputTextFile(MGF_FILE_LABEL);
		enableSecondaryInputTextFile(XTANDEM_FILE_LABEL);

		String mgfPlusXTandemMessage = "<html>With this option, you will be able to create <ul><li>a MIAPE MS document from the mgf file. "
				+ "(Some minimal information about the spectrometer<br>will be asked to you before to start the process).</li>"
				+ "<li>a MIAPE MSI document from the XTandem XML file.</li></ul>"
				+ "If the XTandem XML file comes from a search using the mgf file, "
				+ "later you will be able to create<br>a complete PRIDE XML file from both MIAPE MS and MSI documents (option MIAPE to Standard)</html>";
		// show mzML + mzIdentML warning
		JOptionPane.showMessageDialog(this, mgfPlusXTandemMessage, "mgf + XTandem XML",
				JOptionPane.INFORMATION_MESSAGE);

		// show local processing warning
		// if (jRadioButtonLocalProcessing.isSelected())
		// showLocalProcessingWarn();

	}

	private void jRadioButtonDTASelectMGFActionPerformed(java.awt.event.ActionEvent evt) {
		// disable and select MIAPE MS
		jCheckBoxMS.setEnabled(false);
		jCheckBoxMS.setSelected(true);
		// disable and select MIAPE MSI

		jCheckBoxMSI.setSelected(true);

		jComboBoxMetadata.setEnabled(true);
		jButtonEditMetadata.setEnabled(true);

		enablePrimaryInputTextFile(MGF_FILE_LABEL);
		enableSecondaryInputTextFile(DTASELECT_FILE_LABEL);

		String mgfPlusDTASelectMessage = "<html>With this option, you will be able to create <ul><li>a MIAPE MS document from the mgf file. "
				+ "(Some minimal information about the spectrometer<br>will be asked to you before to start the process).</li>"
				+ "<li>a MIAPE MSI document from the DTASelect file.</li></ul>"
				+ "If the DTASelect file comes from a search using the mgf file, "
				+ "later you will be able to create<br>a complete PRIDE XML file from both MIAPE MS and MSI documents (option MIAPE to Standard)</html>";
		// show mzML + mzIdentML warning
		JOptionPane.showMessageDialog(this, mgfPlusDTASelectMessage, "mgf + DTASelect",
				JOptionPane.INFORMATION_MESSAGE);

		// show local processing warning
		// if (jRadioButtonLocalProcessing.isSelected())
		// showLocalProcessingWarn();

	}

	private void showCheckBoxesTooltip() {
		ToolTipManager.sharedInstance().mouseMoved(new MouseEvent(jPanel6, 0, 0, 0, 5, 45, 0, false));
	}

	private void enableSecondaryInputTextFile(String textSecondaryLabel) {
		jTextFieldInputFile2.setEnabled(true);
		inputFileLabel2.setText(textSecondaryLabel);
		inputFileLabel2.setEnabled(true);
		jButtonInputFile2.setEnabled(true);
	}

	private void enablePrimaryInputTextFile(String textPrimaryLabel) {
		jTextFieldInputFile.setEnabled(true);
		inputFileLabel1.setText(textPrimaryLabel);
		inputFileLabel1.setEnabled(true);
		jButtonInputFile.setEnabled(true);
	}

	private void disableSecondaryInputTextFile() {
		jTextFieldInputFile2.setEnabled(false);
		inputFileLabel2.setEnabled(false);
		jButtonInputFile2.setEnabled(false);
		inputFileLabel2.setText(NOT_APPLICABLE);
	}

	private void disablePrimaryInputTextFile() {
		jTextFieldInputFile.setEnabled(false);
		inputFileLabel1.setEnabled(false);
		jButtonInputFile.setEnabled(false);
		inputFileLabel1.setText(NOT_APPLICABLE);
	}

	// public void selectInstrument() {
	// InstrumentSummary[] data;
	// data = readInstrumentsData();
	// additionalDataForm = new TFrmInputTable(this, true,
	// TFrmInputTable.INTRUMENT_MODE, data);
	// additionalDataForm.setVisible(true);
	// }

	private String selectFile(int _mode) {
		String filename = "";
		jFileChooser = new JFileChooser(MainFrame.currentFolder);
		switch (_mode) {
		case ALLMODE:
			jFileChooser.setDialogTitle("Select a file");
			break;
		case PRIDEXMLMODE:
			jFileChooser.setDialogTitle("Select a PRIDE XML file");

			jFileChooser.setFileFilter(new TFileExtension("PRIDE XML files", new String[] { "XML", "xml" }));

			break;
		// case MGFMZIDENTMLMODE:
		// this.jFileChooser.setDialogTitle("Select a mgf file");
		// this.jFileChooser.setFileFilter(new TFileExtension(
		// "Mascot Generic files", new String[] { "MGF", "mgf" }));
		// break;
		case MGFMODE:
			jFileChooser.setDialogTitle("Select a mgf file");
			jFileChooser.setFileFilter(new TFileExtension("Mascot Generic files", new String[] { "MGF", "mgf" }));
			break;
		case MZIDENTMLMODE:
			jFileChooser.setDialogTitle("Select a mzIdentML file");
			jFileChooser
					.setFileFilter(new TFileExtension("mzIdentML files", new String[] { "mzid", "xml", "mzidentml" }));
			break;
		case MZMLMODE:
			jFileChooser.setDialogTitle("Select a mzML file");
			jFileChooser.setFileFilter(new TFileExtension("mzML files", new String[] { "mzml", "xml" }));
			break;
		case DTASELECTMODE:
			jFileChooser.setDialogTitle("Select a DTASelect file");
			jFileChooser.setFileFilter(new TFileExtension("DTASelect-filter files", new String[] { "txt" }));
			break;
		case XTANDEMMODE:
			jFileChooser.setDialogTitle("Select a XTandem output");
			jFileChooser.setFileFilter(new TFileExtension("XTandem output files", new String[] { "xml" }));
			break;
		}
		// fileChooser.setDialogTitle("Select a PRIDE file");
		// fileChooser.setFileFilter(new TFileExtension("XML files", new
		// String[] { "XML", "xml" }));
		// fileChooser.setCurrentDirectory(openfileDirectrory);
		jFileChooser.showOpenDialog(this);
		File selectedFile = jFileChooser.getSelectedFile();
		if (selectedFile != null) {
			filename = selectedFile.toString();
			if (selectedFile.isDirectory()) {
				MainFrame.currentFolder = selectedFile;
			} else {
				MainFrame.currentFolder = selectedFile.getParentFile();
			}
			log.info("Selected File: " + filename);
		} else
			filename = "null";
		return (filename);
	}

	private void jButtonSubmitActionPerformed(java.awt.event.ActionEvent evt) {
		if (evt.getSource() instanceof JButton) {
			if (!((JButton) evt.getSource()).isEnabled())
				return;
		}
		// clear status
		jTextAreaStatus.setText("");

		// in case of MIAPE MS generation conversion, check if MIAPE MS is
		// complete or not and then, show MIAPE MS Metadata forms
		if (jCheckBoxMS.isSelected()) {
			String selectedItem = (String) jComboBoxMetadata.getSelectedItem();
			if ("".equals(selectedItem)) {
				// do not let continue if mgf + mzIdentml or mgf + XTandem
				// options are selected. Other options, show a warning:
				if (isMzIdentMLPlusMGFSelected() || isXTandemPlusMGFSelected() || isDTASelectPlusMGFSelected()) {
					final int option = JOptionPane.showConfirmDialog(this,
							"<html>MGF input file doesn't contain any metadata. <br>"
									+ "You must select one preconfigured metadata information in the dropdown list or introduce the information yourself.<br><br>"
									+ "Do you want to go to complete metadata, click on YES.</html>",
							"No additional metadata has been selected", JOptionPane.YES_NO_CANCEL_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						// open MIAPEMSForms
						miapeMSChecker = new MIAPEMSChecker(this, true);
						miapeMSChecker.addPropertyChangeListener(this);
						miapeMSChecker.execute();
					} else {
						return;
					}
				} else {
					final int option = JOptionPane.showConfirmDialog(this,
							"<html>Metadata requested by MIAPE MS guidelines is not usually present in the<br>"
									+ "input files (spectrometer details, data processing, etc...).<br><br>"
									+ "You can select one preconfigured metadata information in the dropdown list.<br><br>"
									+ "Do you want to continue without complete metadata? (YES)<br>"
									+ "If you want to go to complete metadata, click on NO.</html>",
							"No additional metadata has been selected", JOptionPane.YES_NO_CANCEL_OPTION);
					if (option == JOptionPane.NO_OPTION) {
						// open MIAPEMSForms
						miapeMSChecker = new MIAPEMSChecker(this, true);
						miapeMSChecker.addPropertyChangeListener(this);
						miapeMSChecker.execute();
					} else if (option == JOptionPane.CANCEL_OPTION) {
						return;
					} else {
						startExtraction();
						return;
					}
				}
			} else {
				String metadataString = MetadataLoader.getMetadataString(selectedItem);
				final int option = JOptionPane
						.showConfirmDialog(this,
								"<html>You have selected the following metadata to add to the MIAPE MS document.<br>Are you sure you want to continue?:<br><br>"
										+ metadataString + "</html>",
								"Metadata confirmation", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					startExtraction();
					return;
				}
			}

		} else {
			startExtraction();
			return;
		}
		log.info("No miape extraction process started");
	}

	private synchronized void startExtraction() {

		// in case of mzML conversion, show the parsing mode dialog:
		if (jRadioButtonMzMLMzIdentML.isSelected()) {
			ParsingModeDialog parsingModeDialog = new ParsingModeDialog(this, true);
			parsingModeDialog.setVisible(true);
			if (!isFastParsing && !isShallowParsing)
				return;
		}
		if (miapeExtractionTask != null && miapeExtractionTask.getState().equals(StateValue.STARTED)) {

			appendStatus("A request has been already sent to the server. Please wait...");
			return;
		}

		if (!extractionStarted) {
			extractionStarted = true;
			miapeExtractionTask = new MiapeExtractionTask(this, MainFrame.getMiapeExtractorWebservice(),
					MainFrame.getMiapeAPIWebservice(), MainFrame.userName, MainFrame.password,
					isLocalProcessingInParallel());
			miapeExtractionTask.addPropertyChangeListener(this);
			miapeExtractionTask.execute();
		} else {
			log.info("There is already a task for miape extraction");
		}

	}

	private boolean isLocalProcessingInParallel() {
		return jCheckBoxLocalProcessinInParallel.isSelected();
	}

	private void jButtonProjectActionPerformed(java.awt.event.ActionEvent evt) {
		appendStatus("Opening project table");
		loadProjects(true, false);
		Map<Integer, String> miapeProjects = getLoadedProjects();
		// this.appendStatus(miapeProjects.size() + " projects retrieved\n");
		if (miapeProjects != null && !miapeProjects.isEmpty()) {
			additionalDataForm = new TFrmInputTable(this, true, TFrmInputTable.PROJECT_MODE, miapeProjects);
			additionalDataForm.setVisible(true);
		} else {
			appendStatus("There is no projects to show");
		}
	}

	private void jButtonInputFileActionPerformed(java.awt.event.ActionEvent evt) {

		int mode = ALLMODE;
		if (jRadioButtonMzIdentML.isSelected()) {
			mode = MZIDENTMLMODE;
		} else if (jRadioButtonMzIdentMLMGF.isSelected() || jRadioButtonDTASelectMGF.isSelected()
				|| jRadioButtonXTandemMGF.isSelected()) {
			mode = MGFMODE;
		} else if (jRadioButtonPRIDE.isSelected()) {
			mode = PRIDEXMLMODE;
		} else if (jRadioButtonMzMLMzIdentML.isSelected()) {
			mode = MZMLMODE;
		}
		// select the file
		String selectedFile = selectFile(mode);
		if ("".equals(selectedFile) || selectedFile.compareTo("null") == 0) {
			log.info("ERROR: MGF I/O");
			return;
		}
		jTextFieldInputFile.setText(selectedFile);

		if (mode == MZMLMODE || (mode == PRIDEXMLMODE && isMIAPEMSChecked())) {
			// Starting metadata extraction
			jLabelMiapeMSMetadata.setText("");
			MIAPEMSChecker checker = new MIAPEMSChecker(this, true);
			checker.addPropertyChangeListener(this);
			checker.setSave(true);
			checker.execute();

		}
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.ButtonGroup buttonGroupInputFileFormat;
	private javax.swing.ButtonGroup buttonGroupProcessingType;
	private javax.swing.ButtonGroup buttonGroupStoreOrNotStore;
	private javax.swing.JLabel inputFileLabel1;
	private javax.swing.JLabel inputFileLabel2;
	private javax.swing.JButton jButtonClearStatus;
	private javax.swing.JButton jButtonEditMetadata;
	public javax.swing.JButton jButtonInputFile;
	public javax.swing.JButton jButtonInputFile2;
	public javax.swing.JButton jButtonProject;
	public javax.swing.JButton jButtonSubmit;
	private javax.swing.JCheckBox jCheckBoxLocalProcessinInParallel;
	public javax.swing.JCheckBox jCheckBoxMS;
	public javax.swing.JCheckBox jCheckBoxMSI;
	private javax.swing.JComboBox jComboBoxMetadata;
	private javax.swing.JFileChooser jFileChooser;
	private javax.swing.JLabel jLabelMiapeMSMetadata;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JProgressBar jProgressBar;
	private javax.swing.JRadioButton jRadioButtonMzIdentML;
	private javax.swing.JRadioButton jRadioButtonMzIdentMLMGF;
	private javax.swing.JRadioButton jRadioButtonXTandemMGF;
	private javax.swing.JRadioButton jRadioButtonDTASelectMGF;
	private javax.swing.JRadioButton jRadioButtonMzMLMzIdentML;
	private javax.swing.JRadioButton jRadioButtonPRIDE;
	private javax.swing.JRadioButton jRadioButtonXTandem;
	private javax.swing.JRadioButton jRadioButtonDTASelect;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea jTextAreaStatus;
	private javax.swing.JTextField jTextFieldInputFile;
	private javax.swing.JTextField jTextFieldInputFile2;
	private javax.swing.JTextField jTextFieldProjectName;
	// End of variables declaration//GEN-END:variables

	private TFrmInputTable additionalDataForm;

	private MiapeExtractionTask miapeExtractionTask;
	private MainFrame mainFrame = null;
	public String ftpPath = null;
	public String id_msi;
	public String id_ms;
	private boolean isLoadingProjects; // indicate if the thread
										// LoadProjectsThread is already loading
										// or not
	private final Map<Integer, String> loadedProjects = new HashMap<Integer, String>();

	// public int selectedInstrumentNumber;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			jProgressBar.setValue(progress);
		} else if (MiapeExtractionTask.NOTIFICATION.equals(evt.getPropertyName())) {
			String notificacion = evt.getNewValue().toString();
			appendStatus(notificacion);
		} else if (MiapeExtractionTask.MIAPE_CREATION_ERROR.equals(evt.getPropertyName())) {
			if (evt.getNewValue() != null) {
				MiapeExtractionResult errorMessage = (MiapeExtractionResult) evt.getNewValue();
				appendStatus(errorMessage.getErrorMessage());
			}
			jProgressBar.setIndeterminate(false);
			jButtonSubmit.setEnabled(true);
			this.setCursor(null); // turn off the wait cursor
			extractionStarted = false;
			appendStatus("Process finished.");
		} else if (MIAPEMSChecker.MIAPE_MS_CHECKING_IN_PROGRESS.equals(evt.getPropertyName())) {
			appendStatus("Extracting MIAPE MS metadata from file...");
			jProgressBar.setIndeterminate(true);
		} else if (MIAPEMSChecker.MIAPE_MS_CHECKING_ERROR.equals(evt.getPropertyName())) {
			String error = evt.getNewValue().toString();
			appendStatus(error);
			jProgressBar.setIndeterminate(false);
		} else if (MIAPEMSChecker.MIAPE_MS_CHECKING_DONE.equals(evt.getPropertyName())) {
			appendStatus(
					"MIAPE MS metadata edition completed. Click again on \"Create MIAPE(s)\" to extract MIAPE information.");
			jProgressBar.setIndeterminate(false);
		} else if (MetadataLoader.METADATA_READED.equals(evt.getPropertyName())) {
			String string = (String) evt.getNewValue();
			jLabelMiapeMSMetadata.setText(string);
		} else if (MiapeExtractionTask.MIAPE_MSI_CREATED_DONE.equals(evt.getPropertyName())) {
			extractionStarted = false;
			String miapeIDString = (String) evt.getNewValue();
			log.info("Miape MSI created done finished: " + miapeIDString);
			if (miapeIDString != null && !miapeExtractionTask.isLocalMIAPEExtraction()) {
				log.info("Starting retrieving of the MIAPE MSI created some moments ago");
				int miapeID = Integer.valueOf(miapeIDString);
				MiapeRetrieverManager.getInstance(MainFrame.userName, MainFrame.password)
						.addRetrievingWithPriority(miapeID, "MSI", null);
			}
			FileManager.deleteMetadataFile(MIAPEMSChecker.CURRENT_MZML);
			initMetadataCombo(null, getControlVocabularyManager());
			// load new projects
			loadProjects(false, storeMIAPEsInDB());
		} else if (MiapeExtractionTask.MIAPE_MS_CREATED_DONE.equals(evt.getPropertyName())) {
			extractionStarted = false;
			String miapeIDString = (String) evt.getNewValue();
			log.info("Miape MS created done finished: " + miapeIDString);
			if (miapeIDString != null) {
				log.info("Starting retrieving of the MIAPE MS created some moments ago");
				int miapeID = Integer.valueOf(miapeIDString);
				MiapeRetrieverManager.getInstance(MainFrame.userName, MainFrame.password)
						.addRetrievingWithPriority(miapeID, "MS", null);
			}
			// load new projects
			loadProjects(false, storeMIAPEsInDB());
		} else if (OntologyLoaderWaiter.ONTOLOGY_LOADED.equals(evt.getPropertyName())) {
			ControlVocabularyManager cvManager = (ControlVocabularyManager) evt.getNewValue();
			appendStatus("Ontologies loaded.");
			initMetadataCombo(null, cvManager);

		} else if (MIAPEMSChecker.MIAPE_MS_METADATA_EXTRACTION_DONE.equals(evt.getPropertyName())) {
			appendStatus("Metadata loaded.");
			if (isMzMLPlusMzIdentMLSelected() || isMzMLSelected())
				initMetadataCombo(MIAPEMSChecker.CURRENT_MZML, getControlVocabularyManager());
			else if (isPRIDESelected() && isMIAPEMSChecked())
				initMetadataCombo(MIAPEMSChecker.CURRENT_PRIDEXML, getControlVocabularyManager());
			jProgressBar.setIndeterminate(false);
		} else if (MiapeExtractionTask.MIAPE_CREATION_TOTAL_DONE.equals(evt.getPropertyName())) {
			jProgressBar.setIndeterminate(false);
			MiapeExtractionResult extractionResult = (MiapeExtractionResult) evt.getNewValue();
			showOpenBrowserDialog(extractionResult.getDirectLinkToMIAPEMS(), extractionResult.getDirectLinkToMIAPEMSI(),
					extractionResult.getDirectLinkText());

			jProgressBar.setIndeterminate(false);
			jButtonSubmit.setEnabled(true);
			this.setCursor(null); // turn off the wait cursor
			extractionStarted = false;
			appendStatus("Process finished.");

		} else if (MiapeExtractionTask.MIAPE_CREATION_STARTS.equals(evt.getPropertyName())) {
			jButtonSubmit.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			jProgressBar.setIndeterminate(true);
			appendStatus("Starting process...");

		}
	}

	@Override
	public MiapeMSDocument getMiapeMSMetadata() {

		final String miapeFileName = (String) jComboBoxMetadata.getSelectedItem();
		if ("".equals(miapeFileName))
			return null;
		final File metadataFile = FileManager.getMetadataFile(miapeFileName);
		if (metadataFile == null)
			return null;
		MIAPEMSXmlFile xmlFile = new MIAPEMSXmlFile(metadataFile);

		try {
			MiapeMSDocument metadataMiapeMS = MiapeMSXmlFactory.getFactory().toDocument(xmlFile,
					getControlVocabularyManager(), null, null, null);
			MiapeExtractionParametersUtil.setNameToMetadataMiapeMS((MiapeMSDocumentImpl) metadataMiapeMS, this);

			String miapeMSName = metadataMiapeMS.getName();
			// merge with a MIAPE with just a project

			MiapeDate today = new MiapeDate(new Date());
			MiapeMSDocument miapeMSJustWithProject = (MiapeMSDocument) MiapeMSDocumentFactory
					.createMiapeMSDocumentBuilder(
							MiapeDocumentFactory.createProjectBuilder(jTextFieldProjectName.getText())
									.date(new MiapeDate(new Date())).build(),
							miapeMSName, null)
					.date(today).modificationDate(new Date()).build();
			MiapeMSDocument ret = MiapeMSMerger.getInstance(getControlVocabularyManager()).merge(metadataMiapeMS,
					miapeMSJustWithProject);

			return ret;
		} catch (MiapeDatabaseException e) {
			e.printStackTrace();
		} catch (MiapeSecurityException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected ControlVocabularyManager getControlVocabularyManager() {
		return OntologyLoaderTask.getCvManager();
	}

	// public void fillInstrumentUserSelection(String userSelection, int i) {
	// this.jLabelInstrument.setText(userSelection);
	// this.selectedInstrumentNumber = i + 1;
	// }

	public void fillProjectUserSelection(String userSelection) {
		jTextFieldProjectName.setText(userSelection);
	}

	@Override
	public String getProjectName() {
		return jTextFieldProjectName.getText();
	}

	public void setProjectName(String name) {
		jTextFieldProjectName.setText(name);
	}

	public String getPrimaryInputFileName() {
		return jTextFieldInputFile.getText();
	}

	public String getSecondaryInputFileName() {
		return jTextFieldInputFile2.getText();
	}

	@Override
	public String getMzMLFileName() {
		if (isMzMLSelected() || isMzMLPlusMzIdentMLSelected())
			return jTextFieldInputFile.getText();
		return null;
	}

	@Override
	public String getMzIdentMLFileName() {
		if (isMzIdentMLSelected() || isMzIdentMLPlusMGFSelected() || isMzMLPlusMzIdentMLSelected())
			return jTextFieldInputFile2.getText();
		return null;
	}

	@Override
	public String getPRIDEXMLFileName() {
		if (isPRIDESelected())
			return jTextFieldInputFile.getText();
		return null;
	}

	@Override
	public String getMgfFileName() {
		if (isMzIdentMLPlusMGFSelected() || isMGFSelected() || isXTandemPlusMGFSelected())
			return jTextFieldInputFile.getText();
		return null;
	}

	@Override
	public String getXTandemFileName() {
		if (isXTandemSelected())
			return jTextFieldInputFile2.getText();
		return null;
	}

	// public String getInstrument() {
	// return this.jLabelInstrument.getText();
	// }

	@Override
	public boolean isMzMLSelected() {
		return jRadioButtonMzMLMzIdentML.isSelected();
	}

	@Override
	public boolean isMzIdentMLSelected() {
		return jRadioButtonMzIdentML.isSelected();
	}

	@Override
	public boolean isXTandemSelected() {
		return jRadioButtonXTandem.isSelected() || jRadioButtonXTandemMGF.isSelected();
	}

	@Override
	public boolean isMzIdentMLPlusMGFSelected() {
		return jRadioButtonMzIdentMLMGF.isSelected();
	}

	@Override
	public boolean isXTandemPlusMGFSelected() {
		return jRadioButtonXTandemMGF.isSelected();
	}

	@Override
	public boolean isPRIDESelected() {
		return jRadioButtonPRIDE.isSelected();
	}

	@Override
	public boolean isMzMLPlusMzIdentMLSelected() {
		return jRadioButtonMzMLMzIdentML.isSelected();
	}

	public String getStatus() {
		return jTextAreaStatus.getText();
	}

	private void appendStatus(String text) {
		ZonedDateTime zonedDateTime = ZonedDateTime.now();

		String dateText = zonedDateTime.format(formatter);
		jTextAreaStatus.append(dateText + ": " + text + "\n");
		jTextAreaStatus.setCaretPosition(jTextAreaStatus.getText().length() - 1);
	}

	@Override
	public boolean isMIAPEMSChecked() {
		return jCheckBoxMS.isSelected();
	}

	@Override
	public boolean isMIAPEMSIChecked() {
		return jCheckBoxMSI.isSelected();
	}

	@Override
	public boolean isLocalProcessing() {
		return true;
	}

	public void setLoadedProjects(Map<Integer, String> projects) {

		loadedProjects.clear();
		loadedProjects.putAll(projects);
	}

	private Map<Integer, String> getLoadedProjects() {
		while (isLoadingProjects()) {
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return loadedProjects;
	}

	public synchronized boolean isLoadingProjects() {
		return isLoadingProjects;
	}

	public synchronized void setLoadingProjects(boolean b) {
		isLoadingProjects = b;
	}

	public static void main(String[] args) {
		MiapeExtractionFrame instance = new MiapeExtractionFrame(null, false);
		instance.setVisible(true);

	}

	/**
	 * Show a dialog with the option of opening a browser with the direct link
	 * to the MIAPE documents
	 *
	 * @param msURL
	 * @param msiURL
	 * @param directLinks
	 */
	private void showOpenBrowserDialog(URL msURL, URL msiURL, String directLinks) {
		String plural = "";
		if (msURL != null && msiURL != null)
			plural = "(s)";

		Object[] dialog_options = { "Yes, open browser", "No, close this dialog" };
		int selected_option = JOptionPane.showOptionDialog(this,
				directLinks + "\n" + "\nClick on yes to open a browser to go directly to the document" + plural + "\n",
				"MIAPE document" + plural + " created", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, dialog_options, dialog_options[1]);
		if (selected_option == 0) { // Yes
			if (msURL != null)
				HttpUtilities.openURL(msURL.toString());
			if (msiURL != null)
				HttpUtilities.openURL(msiURL.toString());
		}
	}

	@Override
	public boolean isFastParsing() {
		return isFastParsing;
	}

	@Override
	public String getDescription() {
		return MiapeExtractionParametersUtil.getDescription(this);
	}

	@Override
	public List<File> getInputFiles() {
		List<File> ret = new ArrayList<File>();
		if (jTextFieldInputFile.isEnabled()) {
			File file = new File(jTextFieldInputFile.getText());
			if (file.exists())
				ret.add(file);
		}
		if (jTextFieldInputFile2.isEnabled()) {
			File file = new File(jTextFieldInputFile2.getText());
			if (file.exists())
				ret.add(file);
		}

		return ret;
	}

	@Override
	public Integer getAssociatedMiapeMS() {
		return null;
	}

	@Override
	public Integer getAssociatedMiapeMSGeneratorJob() {
		return null;
	}

	@Override
	public String getTemplateName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMGFSelected() {
		return jRadioButtonDTASelectMGF.isSelected() || jRadioButtonMzIdentMLMGF.isSelected()
				|| jRadioButtonXTandemMGF.isSelected();
	}

	@Override
	public boolean storeMIAPEsInDB() {
		return false;
	}

	@Override
	public String getDtaSelectFileName() {
		if (isDTASelectSelected())
			return jTextFieldInputFile2.getText();
		return null;
	}

	@Override
	public boolean isDTASelectSelected() {
		return jRadioButtonDTASelect.isSelected();
	}

	@Override
	public boolean isDTASelectPlusMGFSelected() {
		return jRadioButtonDTASelectMGF.isSelected();
	}
}