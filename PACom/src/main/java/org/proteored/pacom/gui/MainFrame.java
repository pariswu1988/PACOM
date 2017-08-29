/*
 * MainFrame.java Created on __DATE__, __TIME__
 */

package org.proteored.pacom.gui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.proteored.pacom.analysis.gui.Miape2ExperimentListDialog;
import org.proteored.pacom.gui.tasks.CheckUpdateTask;
import org.proteored.pacom.gui.tasks.OntologyLoaderTask;
import org.proteored.pacom.gui.tasks.OntologyLoaderWaiter;
import org.proteored.pacom.utils.AppVersion;
import org.proteored.pacom.utils.PropertiesReader;

/**
 *
 * @author __USER__
 */
public class MainFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4998871134548349223L;
	// by default
	public static String miapetool_access_script = "http://www.proteored.org/acceso.asp?pmArea=8";

	private static final String URL_MIAPE_EXTRACTOR_TUTORIAL = "https://github.com/smdb21/PACOM/wiki";
	private static final String URL_MIAPE_EXTRACTOR_BATCH_TUTORIAL = "https://github.com/smdb21/PACOM/wiki/How-to-import-datasets#importing-multiple-datasets-with-batch-import";

	// by default:
	private static AppVersion version;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("log4j.logger.org.proteored");
	private OntologyLoaderTask ontologyLoader;
	private CheckUpdateTask updateTask;
	private Miape2ExperimentListDialog miape2experimentListDialog;
	private MiapeExtractionBatchFrame miapeExtractionBatchFrame;
	private final static String dataInspectionTooltip = "<html><br><ul><li>Inspect your data creating your own inspection projects.</li><li>Compare complex experiments in an intuitive way.</li><li>Get a lot of charts representing qualitative data from your experiments.</li><li>Filter data applying several filters (FDR, score thresholds, etc...)</li><li>Export your data into PRIDE XML format</li></ul><br></html>";
	private final static String dataImportToolTip = "<html><br>Extract and import datasets from input data files such as:<br><ul><li>mzIdentML</li><li>mzML</li><li>PRIDE XML</li><li>X!Tandem output XML</li> <li>DTASelect output</li><li>Separated values tables</li></ul><br></html>";
	private final static String batchDataImportToolTip = "<html><br>Batch data import from datasets files such as: <ul> <li>mzIdentML</li> <li>mzML</li> <li>PRIDE XML</li> <li>X!Tandem output XML</li> <li>DTASelect output</li> <li>Separated values tables</li> </ul>Using a batch data import file you can import multiple datasets.<br><br></html>";

	public static final boolean parallelProcessingOnExtraction = true;

	private static final String APP_PROPERTIES = "app.properties";

	private final boolean checkUpdates = true;

	public static File currentFolder = new File(".");

	/**
	 * Creates new form MainFrame
	 *
	 * @throws Exception
	 */
	public MainFrame() {
		/*
		 * MiapeMSDocument miapeMS; miapeMS =
		 * createDefault_4800MALDITOFTOF_MiapeMS(); System.out.println(
		 * "INFO despues del readMiapeMSMetadata: " +
		 * miapeMS.toXml().toString());
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}

		// set icon image
		setIconImage(ImageManager.getImageIcon(ImageManager.PACOM_LOGO).getImage());

		try {
			// do background tasks for resources loading
			// this.ontologyLoader = new OntologyLoaderTask();
			// this.ontologyLoader.addPropertyChangeListener(this);
			// this.ontologyLoader.execute();

			// Launch update checker
			if (checkUpdates) {
				// this has to be modified
				// TODO
				updateTask = new CheckUpdateTask();
				updateTask.execute();
			}
			initComponents();

			try {
				version = getVersion();
				if (version != null) {
					String suffix = " (v" + version.toString() + ")";
					if (!getTitle().endsWith(suffix))
						setTitle(getTitle() + suffix);
				}
			} catch (Exception e1) {
			}

			// load ontologies
			loadOntologies();

		} catch (Exception e) {
			String message = "";
			if (e.getMessage().startsWith("XML reader error") || e.getMessage().contains("Failed to access")) {
				message = "Webservice is unreachable.";
			} else {
				message = e.getMessage();
			}
			writeErrorMessage(message);
		}

	}

	private void loadOntologies() {
		// OntologyLoaderTask.getCvManager();
		new OntologyLoaderWaiter().execute();
	}

	private void writeErrorMessage(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>Error initializating the tool: <b>" + message
				+ "</b><br/>Try to restart and if the problem persist, contact to 'miape_support@proteored.org'</html>");
		// this.jLabelInit.setText(sb.toString());

		// cancel tasks
		if (ontologyLoader != null)
			ontologyLoader.cancel(true);
		if (updateTask != null)
			updateTask.cancel(true);

		// show error message
		JOptionPane.showMessageDialog(this, sb.toString(), "Error loading", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void dispose() {
		if (ontologyLoader != null) {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				boolean canceled = ontologyLoader.cancel(true);
				if (canceled || ontologyLoader.getState() != StateValue.STARTED)
					break;
			}
		}
		if (updateTask != null)
			updateTask.cancel(true);

		super.dispose();
	}

	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItemExit = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		jMenuItemStandard2MIAPE = new javax.swing.JMenuItem();
		jMenuItemBatchMiapeExtraction = new javax.swing.JMenuItem();
		jMenu3 = new javax.swing.JMenu();
		jMenuItemStartProjectComparison = new javax.swing.JMenuItem();
		jMenuHelp = new javax.swing.JMenu();
		jMenuItemMIAPEExtractionTutorial = new javax.swing.JMenuItem();
		jMenuItemMIAPEExtractionBatchTutorial = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("PACOM - Proteomics Assay COMparator");
		setResizable(false);

		jMenu1.setText("Exit");

		jMenuItemExit.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
		jMenuItemExit.setText("Exit");
		jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exit(evt);
			}
		});
		jMenu1.add(jMenuItemExit);

		jMenuBar1.add(jMenu1);

		jMenu2.setText("Import data");
		jMenu2.setToolTipText(dataImportToolTip);

		jMenuItemStandard2MIAPE.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
		jMenuItemStandard2MIAPE.setText("Go to Import data");
		jMenuItemStandard2MIAPE.setToolTipText(dataImportToolTip);
		jMenuItemStandard2MIAPE.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemStandard2MIAPEActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItemStandard2MIAPE);

		jMenuItemBatchMiapeExtraction.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_MASK));
		jMenuItemBatchMiapeExtraction.setText("Go to Batch Import data");
		jMenuItemBatchMiapeExtraction.setToolTipText(batchDataImportToolTip);
		jMenuItemBatchMiapeExtraction.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemBatchMiapeExtractionActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItemBatchMiapeExtraction);

		jMenuBar1.add(jMenu2);

		jMenu3.setText("Data inspection");

		jMenuItemStartProjectComparison.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
		jMenuItemStartProjectComparison.setText("Go to Data Inspection");
		jMenuItemStartProjectComparison.setToolTipText(dataInspectionTooltip);
		jMenuItemStartProjectComparison.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemStartProjectComparisonActionPerformed(evt);
			}
		});
		jMenu3.add(jMenuItemStartProjectComparison);

		jMenuBar1.add(jMenu3);

		jMenuHelp.setText("Help");

		jMenuItemMIAPEExtractionTutorial.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
		jMenuItemMIAPEExtractionTutorial.setText("PACOM Tutorial");
		jMenuItemMIAPEExtractionTutorial.setToolTipText("Download the PACOM Tutorial (PDF)");
		jMenuItemMIAPEExtractionTutorial.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemMIAPEExtractionTutorialActionPerformed(evt);
			}
		});
		jMenuHelp.add(jMenuItemMIAPEExtractionTutorial);

		jMenuItemMIAPEExtractionBatchTutorial.setAccelerator(
				javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
		jMenuItemMIAPEExtractionBatchTutorial.setText("Batch Import data Tutorial");
		jMenuItemMIAPEExtractionBatchTutorial.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemMIAPEExtractionBatchTutorialActionPerformed(evt);
			}
		});
		jMenuHelp.add(jMenuItemMIAPEExtractionBatchTutorial);

		jMenuBar1.add(jMenuHelp);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1.setLayout(new GridBagLayout());
		JLabel jLabelInit = new javax.swing.JLabel("Import data");
		jLabelInit.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelInit.setFont(new Font("Dialog", Font.PLAIN, 18));
		jLabelInit.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		jPanel1.add(jLabelInit, c);
		JLabel jLabelInit2 = new javax.swing.JLabel("Batch import data");
		jLabelInit2.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelInit2.setFont(new Font("Dialog", Font.PLAIN, 18));
		jLabelInit2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		c.fill = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		jPanel1.add(jLabelInit2, c);
		JLabel jLabelInit3 = new javax.swing.JLabel("Data Inspection");
		jLabelInit3.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelInit3.setFont(new Font("Dialog", Font.PLAIN, 18));
		jLabelInit3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		c.fill = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		jPanel1.add(jLabelInit3, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		JButton loadButton = new JButton();
		loadButton.setIcon(ImageManager.getImageIcon(ImageManager.LOAD_LOGO_128));
		loadButton.setPressedIcon(ImageManager.getImageIcon(ImageManager.LOAD_LOGO_128_CLICKED));
		loadButton.setRolloverIcon(ImageManager.getImageIcon(ImageManager.LOAD_LOGO_128_HOVER));

		loadButton.setFocusable(false);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startDataImport();

			}
		});
		loadButton.setToolTipText(dataImportToolTip);
		loadButton.setBorder(BorderFactory.createEmptyBorder());
		loadButton.setContentAreaFilled(false);
		loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jPanel1.add(loadButton, c);
		//
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		JButton batchLoadButton = new JButton();
		batchLoadButton.setIcon(ImageManager.getImageIcon(ImageManager.BATCH_LOAD_LOGO_128));
		batchLoadButton.setPressedIcon(ImageManager.getImageIcon(ImageManager.BATCH_LOAD_LOGO_128_CLICKED));
		batchLoadButton.setRolloverIcon(ImageManager.getImageIcon(ImageManager.BATCH_LOAD_LOGO_128_HOVER));

		batchLoadButton.setFocusable(false);
		batchLoadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showBatchMiapeExtractionFrame();

			}
		});
		batchLoadButton.setToolTipText(batchDataImportToolTip);
		batchLoadButton.setBorder(BorderFactory.createEmptyBorder());
		batchLoadButton.setContentAreaFilled(false);
		batchLoadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jPanel1.add(batchLoadButton, c);
		//
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		JButton inspectButton = new JButton();
		inspectButton.setIcon(ImageManager.getImageIcon(ImageManager.PACOM_LOGO_128));
		inspectButton.setPressedIcon(ImageManager.getImageIcon(ImageManager.PACOM_LOGO_128_CLICKED));
		inspectButton.setRolloverIcon(ImageManager.getImageIcon(ImageManager.PACOM_LOGO_128_HOVER));
		inspectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		inspectButton.setFocusable(false);
		inspectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startProjectComparison();
			}
		});
		inspectButton.setToolTipText(dataInspectionTooltip);
		inspectButton.setBorder(BorderFactory.createEmptyBorder());
		inspectButton.setContentAreaFilled(false);
		jPanel1.add(inspectButton, c);
		getContentPane().setLayout(layout);

		pack();
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension dialogSize = getSize();
		setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
	}// </editor-fold>

	private void startDataImport() {
		setVisible(false);
		MiapeExtractionFrame standard2MIAPEDialog = MiapeExtractionFrame.getInstance(this, true);
		standard2MIAPEDialog.setVisible(true);

	}

	// GEN-END:initComponents

	private void jMenuItemMIAPEExtractionBatchTutorialActionPerformed(java.awt.event.ActionEvent evt) {
		showMiapeExtractionBatchTutorial();
	}

	private void showMiapeExtractionBatchTutorial() {
		final int showConfirmDialog = JOptionPane.showConfirmDialog(this,
				"Do you want to open a browser window to go to the PACOM Wiki file?", "Go to WIKI",
				JOptionPane.YES_NO_OPTION);
		if (showConfirmDialog == JOptionPane.YES_OPTION) {
			try {
				openBrowser(URL_MIAPE_EXTRACTOR_BATCH_TUTORIAL);
			} catch (IOException e) {

			} catch (URISyntaxException e) {

			}
		}

	}

	private void jMenuItemBatchMiapeExtractionActionPerformed(java.awt.event.ActionEvent evt) {
		showBatchMiapeExtractionFrame();
	}

	private void showBatchMiapeExtractionFrame() {
		setVisible(false);
		miapeExtractionBatchFrame = MiapeExtractionBatchFrame.getInstace(this);
		miapeExtractionBatchFrame.setVisible(true);

	}

	private void jMenuItemMIAPEExtractionTutorialActionPerformed(java.awt.event.ActionEvent evt) {
		showMIAPEExtractionTutorial();
	}

	private void showMIAPEExtractionTutorial() {
		final int showConfirmDialog = JOptionPane.showConfirmDialog(this,
				"Do you want to open a browser window to go to the PACOM Wiki page?", "Go to WIKI",
				JOptionPane.YES_NO_OPTION);
		if (showConfirmDialog == JOptionPane.YES_OPTION) {
			try {
				openBrowser(URL_MIAPE_EXTRACTOR_TUTORIAL);
			} catch (IOException e) {

			} catch (URISyntaxException e) {

			}
		}
	}

	private void jMenuItemStartProjectComparisonActionPerformed(java.awt.event.ActionEvent evt) {
		startProjectComparison();
	}

	private void startProjectComparison() {
		setVisible(false);
		miape2experimentListDialog = Miape2ExperimentListDialog.getInstance(this);
		if (miape2experimentListDialog.isCorrectlyInitialized())
			miape2experimentListDialog.setVisible(true);

	}

	private void exit(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void jMenuItemStandard2MIAPEActionPerformed(java.awt.event.ActionEvent evt) {
		startDataImport();

	}

	public void openBrowser(String url) throws IOException, URISyntaxException {
		java.awt.Desktop browser = java.awt.Desktop.getDesktop();
		java.net.URI location = new java.net.URI(url);
		browser.browse(location);
	}

	// public static void autoScroll(JScrollPane jScrollPane,
	// final JTextArea jTextArea) {
	// jScrollPane.getVerticalScrollBar().addAdjustmentListener(
	// new AdjustmentListener() {
	// @Override
	// public void adjustmentValueChanged(AdjustmentEvent e) {
	// jTextArea.select(jTextArea.getHeight() + 1000, 0);
	// }
	// });
	// }

	public static String getHash(String password, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();

		return digest.digest(password.getBytes()).toString();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new MainFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenu jMenuHelp;
	private javax.swing.JMenuItem jMenuItemBatchMiapeExtraction;
	private javax.swing.JMenuItem jMenuItemExit;
	private javax.swing.JMenuItem jMenuItemMIAPEExtractionBatchTutorial;
	private javax.swing.JMenuItem jMenuItemMIAPEExtractionTutorial;
	private javax.swing.JMenuItem jMenuItemStandard2MIAPE;
	private javax.swing.JMenuItem jMenuItemStartProjectComparison;
	private javax.swing.JPanel jPanel1;

	// End of variables declaration//GEN-END:variables

	public static AppVersion getVersion() {
		if (version == null) {
			try {
				String tmp = PropertiesReader.getProperties(APP_PROPERTIES).getProperty("assembly.dir");
				if (tmp.contains("v")) {
					version = new AppVersion(tmp.split("v")[1]);
				} else {
					version = new AppVersion(tmp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return version;

	}

}