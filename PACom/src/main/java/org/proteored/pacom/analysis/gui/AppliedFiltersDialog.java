/*
 * AppliedFiltersDialog.java
 * Created on __DATE__, __TIME__
 */

package org.proteored.pacom.analysis.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.ui.RefineryUtilities;
import org.proteored.miapeapi.experiment.model.filters.Filter;
import org.proteored.miapeapi.experiment.model.sort.SorterUtil;
import org.proteored.pacom.gui.ImageManager;

/**
 * 
 * @author __USER__
 */
public class AppliedFiltersDialog extends javax.swing.JDialog {

	/** Creates new form AppliedFiltersDialog */
	public AppliedFiltersDialog(java.awt.Frame parent, boolean modal, List<Filter> list) {
		super(parent, modal);

		initComponents();
		this.setTitle("Applied filters");
		SorterUtil.sortFilters(list);
		showFiltersInformation(list);
		RefineryUtilities.centerFrameOnScreen(this);
		// set icon image
		this.setIconImage(ImageManager.getImageIcon(ImageManager.PACOM_LOGO).getImage());
		this.pack();
	}

	private void showFiltersInformation(List<Filter> list) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(6, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 0;
		List<Filter> differentFilters = getDifferentFilters(list);
		String plural;
		String plural2 = "";
		if (differentFilters.size() > 1) {
			plural = "s were";
			plural2 = "in this order";
		} else {
			plural = " was";
		}
		JLabel l1 = new JLabel(
				"<html><b>" + differentFilters.size() + " filter" + plural + " applied " + plural2 + "</b></html>");
		panel.add(l1, c);
		c.gridy++;
		panel.add(new JLabel(""), c);
		for (Filter filter : differentFilters) {
			c.gridy++;
			String string = "- " + filter.toString();

			JLabel l = new JLabel(string);
			panel.add(l, c);
		}
		this.jPanel1.setLayout(new BorderLayout());
		this.jPanel1.add(panel, BorderLayout.NORTH);

	}

	private List<Filter> getDifferentFilters(List<Filter> list) {
		List<Filter> differentFilters = new ArrayList<Filter>();
		for (Filter filter : list) {
			boolean equal = false;
			for (Filter filter2 : differentFilters) {
				if (filter2.equals(filter))
					equal = true;
			}
			if (!equal)
				differentFilters.add(filter);
		}
		return differentFilters;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel1 = new javax.swing.JPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 331, Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 238, Short.MAX_VALUE));

		jScrollPane1.setViewportView(jPanel1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 357, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addContainerGap().addComponent(jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
										.addContainerGap())));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 266, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addContainerGap().addComponent(jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
										.addContainerGap())));

		pack();
	}// </editor-fold>
		// GEN-END:initComponents

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	// End of variables declaration//GEN-END:variables

}