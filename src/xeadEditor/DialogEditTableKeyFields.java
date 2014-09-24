package xeadEditor;

/*
 * Copyright (c) 2014 WATANABE kozo <qyf05466@nifty.com>,
 * All rights reserved.
 *
 * This file is part of XEAD Editor.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the XEAD Project nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.w3c.dom.*;
import xeadEditor.Editor.SortableDomElementListModel;

public class DialogEditTableKeyFields extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private Editor frame_;
	private BorderLayout borderLayoutMain = new BorderLayout();
	private JPanel panelMain = new JPanel();
	private JScrollPane jScrollPaneFieldList = new JScrollPane();
	private TableModelReadOnlyList tableModelFieldList = new TableModelReadOnlyList();
	private JTable jTableFieldList = new JTable(tableModelFieldList);
	private JScrollPane jScrollPaneKeyFieldList = new JScrollPane();
	private TableModelReadOnlyList tableModelKeyFieldList = new TableModelReadOnlyList();
	private JTable jTableKeyFieldList = new JTable(tableModelKeyFieldList);
	private TableColumn column0, column1, column2;
	private DefaultTableCellRenderer rendererTableHeader = null;
	private DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	private JPanel jPanelButtons = new JPanel();
	private JButton jButtonCloseDialog = new JButton();
	private JButton jButtonAddField = new JButton();
	private JButton jButtonRemoveField = new JButton();
	private JButton jButtonReturn = new JButton();
	private ArrayList<String> currentKeyFieldIDList = new ArrayList<String>();
	private String edittedKeyFields = "";
	private SortableDomElementListModel fieldSortingList;
	private org.w3c.dom.Element tableElement_;
	private boolean isTableBeingSetup = false;

	public DialogEditTableKeyFields(Editor frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			frame_ = frame;
			jbInit();
			pack();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogEditTableKeyFields(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		panelMain.setBorder(null);
		panelMain.setLayout(borderLayoutMain);
		panelMain.add(jScrollPaneFieldList, BorderLayout.NORTH);
		panelMain.add(jPanelButtons, BorderLayout.SOUTH);
		panelMain.add(jScrollPaneKeyFieldList, BorderLayout.CENTER);

		jTableFieldList.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTableFieldList.setBackground(SystemColor.control);
		jTableFieldList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableFieldList.getSelectionModel().addListSelectionListener(new DialogEditTableKeyFields_jTableFieldList_listSelectionAdapter(this));
		jTableFieldList.addKeyListener(new DialogEditTableKeyFields_jTableFieldList_keyAdapter(this));
		jTableFieldList.setRowSelectionAllowed(true);
		jTableFieldList.setRowHeight(Editor.TABLE_ROW_HEIGHT);
		tableModelFieldList.addColumn("NO.");
		tableModelFieldList.addColumn(res.getString("FieldID"));
		tableModelFieldList.addColumn(res.getString("FieldName"));
		column0 = jTableFieldList.getColumnModel().getColumn(0);
		column1 = jTableFieldList.getColumnModel().getColumn(1);
		column2 = jTableFieldList.getColumnModel().getColumn(2);
		column0.setPreferredWidth(40);
		column1.setPreferredWidth(360);
		column2.setPreferredWidth(360);
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		jTableFieldList.getTableHeader().setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		rendererTableHeader = (DefaultTableCellRenderer)jTableFieldList.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		jScrollPaneFieldList.getViewport().add(jTableFieldList, null);
		jScrollPaneFieldList.setPreferredSize(new Dimension(100, 300));

		jTableKeyFieldList.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTableKeyFieldList.setBackground(SystemColor.control);
		jTableKeyFieldList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableKeyFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableKeyFieldList.getSelectionModel().addListSelectionListener(new DialogEditTableKeyFields_jTableKeyFieldList_listSelectionAdapter(this));
		jTableKeyFieldList.addKeyListener(new DialogEditTableKeyFields_jTableKeyFieldList_keyAdapter(this));
		jTableKeyFieldList.setRowSelectionAllowed(true);
		jTableKeyFieldList.setRowHeight(Editor.TABLE_ROW_HEIGHT);
		tableModelKeyFieldList.addColumn("NO.");
		tableModelKeyFieldList.addColumn(res.getString("KeyFieldID"));
		tableModelKeyFieldList.addColumn(res.getString("KeyFieldName"));
		column0 = jTableKeyFieldList.getColumnModel().getColumn(0);
		column1 = jTableKeyFieldList.getColumnModel().getColumn(1);
		column2 = jTableKeyFieldList.getColumnModel().getColumn(2);
		column0.setPreferredWidth(40);
		column1.setPreferredWidth(360);
		column2.setPreferredWidth(360);
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		jTableKeyFieldList.getTableHeader().setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		rendererTableHeader = (DefaultTableCellRenderer)jTableKeyFieldList.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		jScrollPaneKeyFieldList.getViewport().add(jTableKeyFieldList, null);

		jPanelButtons.setBorder(null);
		jPanelButtons.setPreferredSize(new Dimension(100, 43));
		jButtonCloseDialog.setBounds(new Rectangle(40, 8, 100, 27));
		jButtonCloseDialog.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogEditTableKeyFields_jButtonCloseDialog_actionAdapter(this));
		jButtonAddField.setBounds(new Rectangle(200, 8, 180, 27));
		jButtonAddField.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonAddField.setText(res.getString("EditKeyFieldsToPutIntoKey"));
		jButtonAddField.addActionListener(new DialogEditTableKeyFields_jButtonAddField_actionAdapter(this));
		jButtonRemoveField.setBounds(new Rectangle(420, 8, 180, 27));
		jButtonRemoveField.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonRemoveField.setText(res.getString("EditKeyFieldsToRemoveFromKey"));
		jButtonRemoveField.addActionListener(new DialogEditTableKeyFields_jButtonRemoveField_actionAdapter(this));
		jButtonReturn.setBounds(new Rectangle(660, 8, 100, 27));
		jButtonReturn.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonReturn.setText("OK");
		jButtonReturn.addActionListener(new DialogEditTableKeyFields_jButtonReturn_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonCloseDialog);
		jPanelButtons.add(jButtonAddField);
		jPanelButtons.add(jButtonRemoveField);
		jPanelButtons.add(jButtonReturn);

		this.setResizable(false);
		this.setTitle(res.getString("AddKeyMessage"));
		this.getContentPane().add(panelMain);
		this.setPreferredSize(new Dimension(800, 600));
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public String request(org.w3c.dom.Element tableElement, String keyFields) {
		
		/////////////////////////////////////////////////
		// Setup current key field list and field list //
		/////////////////////////////////////////////////
		tableElement_ = tableElement;
		edittedKeyFields = keyFields;
		currentKeyFieldIDList.clear();
		StringTokenizer workTokenizer = new StringTokenizer(keyFields, ";" );
		while (workTokenizer.hasMoreTokens()) {
			currentKeyFieldIDList.add(workTokenizer.nextToken());
		}
		NodeList nodeList = tableElement_.getElementsByTagName("Field");
		fieldSortingList = frame_.getSortedListModel(nodeList, "Order");

		///////////////////////////////////////////
		// Setup field table and key-field table //
		///////////////////////////////////////////
		setupTables();

		//////////////////////////////////////
		// Setup buttons and list selection //
		//////////////////////////////////////
		jButtonAddField.setEnabled(false);
    	jButtonRemoveField.setEnabled(false);
	    if (tableModelFieldList.getRowCount() > 0
	    		&& tableModelKeyFieldList.getRowCount() == 0) {
	    	jTableFieldList.setRowSelectionInterval(0, 0);
	    	jTableFieldList.requestFocus();
	    }
	    
		super.setVisible(true);

		return edittedKeyFields;
	}
	
	void setupTables() {
		org.w3c.dom.Element element;
		isTableBeingSetup = true;
		int rowNumber;
		
		///////////////////////////
		// Setup Table of Fields //
		///////////////////////////
		if (tableModelFieldList.getRowCount() > 0) {
			int rowCount = tableModelFieldList.getRowCount();
			for (int i = 0; i < rowCount; i++) {tableModelFieldList.removeRow(0);}
		}
		rowNumber = 1;
	    for (int i = 0; i < fieldSortingList.getSize(); i++) {
	        element = (org.w3c.dom.Element)fieldSortingList.getElementAt(i);
			if (!frame_.getOptionList(element.getAttribute("TypeOptions")).contains("VIRTUAL")
					&& !currentKeyFieldIDList.contains(element.getAttribute("ID"))) {
				Object[] Cell = new Object[3];
				Cell[0] = rowNumber++;
				Cell[1] = element.getAttribute("ID");
				Cell[2] = element.getAttribute("Name");
				tableModelFieldList.addRow(Cell);
			}
		}

	    ///////////////////////////////
		// Setup Table of Key-Fields //
		///////////////////////////////
	    if (tableModelKeyFieldList.getRowCount() > 0) {
			int rowCount = tableModelKeyFieldList.getRowCount();
			for (int i = 0; i < rowCount; i++) {tableModelKeyFieldList.removeRow(0);}
		}
		rowNumber = 1;
	    for (int i = 0; i < currentKeyFieldIDList.size(); i++) {
		    for (int j = 0; j < fieldSortingList.getSize(); j++) {
		        element = (org.w3c.dom.Element)fieldSortingList.getElementAt(j);
				if (element.getAttribute("ID").equals(currentKeyFieldIDList.get(i))) {
					Object[] Cell = new Object[3];
					Cell[0] = rowNumber++;
					Cell[1] = element.getAttribute("ID");
					Cell[2] = element.getAttribute("Name");
					tableModelKeyFieldList.addRow(Cell);
				}
			}
	    }
	    
		///////////////////////////////
		// Set return button enabled //
		///////////////////////////////
	    if (tableModelKeyFieldList.getRowCount() > 0) {
	    	jButtonReturn.setEnabled(true);
	    } else {
	    	jButtonReturn.setEnabled(false);
	    }
	    isTableBeingSetup = false;
	}
	
	void jTableFieldList_valueChanged() {
		if (jTableFieldList.getSelectedRowCount() > 0 && !isTableBeingSetup) {
			jButtonAddField.setEnabled(true);
			this.getRootPane().setDefaultButton(jButtonAddField);

			jTableKeyFieldList.clearSelection();
			jButtonRemoveField.setEnabled(false);
		}
	}
	
	void jTableKeyFieldList_valueChanged() {
		if (jTableKeyFieldList.getSelectedRowCount() > 0 && !isTableBeingSetup) {
			jButtonRemoveField.setEnabled(true);
			this.getRootPane().setDefaultButton(jButtonRemoveField);
			jTableFieldList.clearSelection();
			jButtonAddField.setEnabled(false);
		}
	}

	void jButtonCloseDialog_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void jButtonAddField_actionPerformed(ActionEvent e) {
		int wrkInt = jTableFieldList.getSelectedRow();
		String fieldID = tableModelFieldList.getValueAt(wrkInt, 1).toString();
		currentKeyFieldIDList.add(fieldID);

		setupTables();

		if (tableModelFieldList.getRowCount() > wrkInt) {
	    	jTableFieldList.setRowSelectionInterval(wrkInt, wrkInt);
	    	jTableFieldList.requestFocus();
	    } else {
	    	if (tableModelFieldList.getRowCount() > 0) {
	    		jTableFieldList.setRowSelectionInterval(tableModelFieldList.getRowCount()-1, tableModelFieldList.getRowCount()-1);
	    		jTableFieldList.requestFocus();
	    	} else {
	    		jButtonAddField.setEnabled(false);
	    	}
	    }
	}

	void jButtonRemoveField_actionPerformed(ActionEvent e) {
		int wrkInt = jTableKeyFieldList.getSelectedRow();
		String fieldID = tableModelKeyFieldList.getValueAt(wrkInt, 1).toString();
		currentKeyFieldIDList.remove(fieldID);

		setupTables();

		if (tableModelKeyFieldList.getRowCount() > wrkInt) {
	    	jTableKeyFieldList.setRowSelectionInterval(wrkInt, wrkInt);
	    	jTableKeyFieldList.requestFocus();
	    } else {
			if (tableModelKeyFieldList.getRowCount() > 0) {
		    	jTableKeyFieldList.setRowSelectionInterval(tableModelKeyFieldList.getRowCount()-1, tableModelKeyFieldList.getRowCount()-1);
		    	jTableKeyFieldList.requestFocus();
			} else {
				if (tableModelFieldList.getRowCount() > 0) {
			    	jTableFieldList.setRowSelectionInterval(0, 0);
			    	jTableFieldList.requestFocus();
			    }
		    	jTableKeyFieldList.clearSelection();
		    	jButtonRemoveField.setEnabled(false);
			}
	    }
	}

	void jButtonReturn_actionPerformed(ActionEvent e) {
		StringBuffer bf = new StringBuffer();
	    for (int i = 0; i < currentKeyFieldIDList.size(); i++) {
	    	if (bf.length() > 0) {
	    		bf.append(";");
	    	}
    		bf.append(currentKeyFieldIDList.get(i));
	    }
	    edittedKeyFields = bf.toString();

	    this.setVisible(false);
	}
	
	void jTableFieldList_keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && tableModelFieldList.getRowCount() > 0) {
			jButtonAddField_actionPerformed(null);
		}
	}
	
	void jTableKeyFieldList_keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && tableModelKeyFieldList.getRowCount() > 0) {
			jButtonRemoveField_actionPerformed(null);
		}
	}

	class TableModelReadOnlyList extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {return false;}
	}
}

class DialogEditTableKeyFields_jTableFieldList_listSelectionAdapter implements ListSelectionListener {
	DialogEditTableKeyFields adaptee;
	DialogEditTableKeyFields_jTableFieldList_listSelectionAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void valueChanged(ListSelectionEvent e) {
		adaptee.jTableFieldList_valueChanged();
	}
}

class DialogEditTableKeyFields_jTableKeyFieldList_listSelectionAdapter implements ListSelectionListener {
	DialogEditTableKeyFields adaptee;
	DialogEditTableKeyFields_jTableKeyFieldList_listSelectionAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void valueChanged(ListSelectionEvent e) {
		adaptee.jTableKeyFieldList_valueChanged();
	}
}

class DialogEditTableKeyFields_jButtonReturn_actionAdapter implements java.awt.event.ActionListener {
	DialogEditTableKeyFields adaptee;

	DialogEditTableKeyFields_jButtonReturn_actionAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonReturn_actionPerformed(e);
	}
}

class DialogEditTableKeyFields_jButtonCloseDialog_actionAdapter implements java.awt.event.ActionListener {
	DialogEditTableKeyFields adaptee;

	DialogEditTableKeyFields_jButtonCloseDialog_actionAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCloseDialog_actionPerformed(e);
	}
}

class DialogEditTableKeyFields_jButtonAddField_actionAdapter implements java.awt.event.ActionListener {
	DialogEditTableKeyFields adaptee;

	DialogEditTableKeyFields_jButtonAddField_actionAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonAddField_actionPerformed(e);
	}
}

class DialogEditTableKeyFields_jButtonRemoveField_actionAdapter implements java.awt.event.ActionListener {
	DialogEditTableKeyFields adaptee;

	DialogEditTableKeyFields_jButtonRemoveField_actionAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonRemoveField_actionPerformed(e);
	}
}

class DialogEditTableKeyFields_jTableFieldList_keyAdapter extends java.awt.event.KeyAdapter {
	DialogEditTableKeyFields adaptee;
	DialogEditTableKeyFields_jTableFieldList_keyAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void keyPressed(KeyEvent e) {
		adaptee.jTableFieldList_keyPressed(e);
	}
}

class DialogEditTableKeyFields_jTableKeyFieldList_keyAdapter extends java.awt.event.KeyAdapter {
	DialogEditTableKeyFields adaptee;
	DialogEditTableKeyFields_jTableKeyFieldList_keyAdapter(DialogEditTableKeyFields adaptee) {
		this.adaptee = adaptee;
	}
	public void keyPressed(KeyEvent e) {
		adaptee.jTableKeyFieldList_keyPressed(e);
	}
}