/*
 * MyJPanelList.java Created on __DATE__, __TIME__
 */

package org.proteored.pacom.analysis.gui;

import javax.swing.JTextField;

/**
 *
 * @author __USER__
 */
public class MyJPanelList extends javax.swing.JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/** Creates new form MyJPanelList */
	public MyJPanelList() {
		initComponents();
		jListPeptides.setFont(new JTextField().getFont());
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
		jListPeptides = new javax.swing.JList<String>();

		jListPeptides.setModel(new javax.swing.AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public int getSize() {
				return strings.length;
			}

			@Override
			public String getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane1.setViewportView(jListPeptides);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE));
	}// </editor-fold>
		// GEN-END:initComponents

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	public javax.swing.JList<String> jListPeptides;
	private javax.swing.JScrollPane jScrollPane1;
	// End of variables declaration//GEN-END:variables

}