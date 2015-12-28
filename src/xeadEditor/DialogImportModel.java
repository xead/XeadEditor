package xeadEditor;

/*
 * Copyright (c) 2015 WATANABE kozo <qyf05466@nifty.com>,
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

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import xeadEditor.Editor.SortableDomElementListModel;

public class DialogImportModel extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private Editor frame_;
	private JPanel panelMain = new JPanel();
	private JPanel jPanelNorth = new JPanel();
	private JPanel jPanelCenter = new JPanel();
	private JPanel jPanelCenterTop = new JPanel();
	private JPanel jPanelSouth = new JPanel();
	private JScrollPane jScrollPaneElementListFrom = new JScrollPane();
	private TableModelReadOnlyList tableModelElementListFrom = new TableModelReadOnlyList();
	private JTable jTableElementListFrom = new JTable(tableModelElementListFrom);
	private JLabel jLabelSubsystemFrom = new JLabel();
	private JComboBox jComboBoxSubsystemFrom = new JComboBox();
	private JCheckBox jCheckBoxOverrideID = new JCheckBox();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private JLabel jLabelFileNameFrom = new JLabel();
	private JTextField jTextFieldFileNameFrom = new JTextField();
	private JLabel jLabelSystemNameFrom = new JLabel();
	private JTextField jTextFieldSystemNameFrom = new JTextField();
	private JLabel jLabelSystemVersionFrom = new JLabel();
	private JTextField jTextFieldSystemVersionFrom = new JTextField();
	private ArrayList<org.w3c.dom.Element> subsystemNodeListFrom = null;
	private NodeList dataTypeNodeListFrom = null;
	private TableColumn column0, column1, column2, column3, column4;
	private DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentRight = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	private JButton jButtonCloseDialog = new JButton();
	private JButton jButtonImport = new JButton();
	private JButton jButtonRefresh = new JButton();
	private Document domDocumentImportingFrom = null;
	private NodeList functionTypeList = null;
	private NodeList currentFunctionList = null;
	private NodeList currentTableList = null;
	private NodeList tableListInModel = null;
	private NodeList functionListInModel = null;
	private ArrayList<String> newlyAddedTableIDList = new ArrayList<String>();
	private ArrayList<String> newlyAddedFunctionIDList = new ArrayList<String>();
	private boolean isListBeingRebuild = false;
	private org.w3c.dom.Element subsystemElementFrom = null;

	public DialogImportModel(Editor frame, String title, boolean modal) {
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

	public DialogImportModel(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		///////////////
		// panelMain //
		///////////////
		panelMain.setLayout(new BorderLayout());
		panelMain.setPreferredSize(new Dimension(850, 750));
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(jPanelNorth, BorderLayout.NORTH);
		panelMain.add(jPanelCenter, BorderLayout.CENTER);
		panelMain.add(jPanelSouth, BorderLayout.SOUTH);

		///////////////////////////////////
		// jPanelNorth and objects on it //
		///////////////////////////////////
		jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
		jPanelNorth.setPreferredSize(new Dimension(800, 74));
		jLabelFileNameFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelFileNameFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelFileNameFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelFileNameFrom.setText(res.getString("FileNameImporting"));
		jLabelFileNameFrom.setBounds(new Rectangle(5, 12, 180, 20));
		jTextFieldFileNameFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldFileNameFrom.setBounds(new Rectangle(190, 9, 625, 25));
		jTextFieldFileNameFrom.setEditable(false);
		jLabelSystemNameFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelSystemNameFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSystemNameFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSystemNameFrom.setText(res.getString("SystemNameImporting"));
		jLabelSystemNameFrom.setBounds(new Rectangle(5, 43, 180, 20));
		jTextFieldSystemNameFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldSystemNameFrom.setBounds(new Rectangle(190, 40, 380, 25));
		jTextFieldSystemNameFrom.setEditable(false);
		jLabelSystemVersionFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelSystemVersionFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSystemVersionFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSystemVersionFrom.setText(res.getString("SystemVersion"));
		jLabelSystemVersionFrom.setBounds(new Rectangle(580, 43, 130, 20));
		jTextFieldSystemVersionFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldSystemVersionFrom.setBounds(new Rectangle(715, 40, 100, 25));
		jTextFieldSystemVersionFrom.setEditable(false);

		jPanelNorth.setLayout(null);
		jPanelNorth.add(jLabelFileNameFrom);
		jPanelNorth.add(jTextFieldFileNameFrom);
		jPanelNorth.add(jLabelSystemNameFrom);
		jPanelNorth.add(jTextFieldSystemNameFrom);
		jPanelNorth.add(jLabelSystemVersionFrom);
		jPanelNorth.add(jTextFieldSystemVersionFrom);

		//////////////////////////////////////
		//jPanelCenterTop and objects on it //
		//////////////////////////////////////
		jLabelSubsystemFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelSubsystemFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSubsystemFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSubsystemFrom.setText(res.getString("SubsystemImportingFrom"));
		jLabelSubsystemFrom.setBounds(new Rectangle(5, 12, 180, 20));
		jComboBoxSubsystemFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jComboBoxSubsystemFrom.setBounds(new Rectangle(190, 9, 330, 25));
		jComboBoxSubsystemFrom.addActionListener(new DialogImportModel_jComboBoxSubsystemFrom_actionAdapter(this));
		jCheckBoxOverrideID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxOverrideID.setBounds(new Rectangle(540, 9, 200, 25));
		jCheckBoxOverrideID.setText(res.getString("OverrideID"));
		jPanelCenterTop.setPreferredSize(new Dimension(100, 43));
		jPanelCenterTop.setBorder(BorderFactory.createEtchedBorder());
		jPanelCenterTop.setLayout(null);
		jPanelCenterTop.add(jLabelSubsystemFrom);
		jPanelCenterTop.add(jComboBoxSubsystemFrom);
		jPanelCenterTop.add(jCheckBoxOverrideID);

		jScrollPaneElementListFrom.getViewport().add(jTableElementListFrom, null);
		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setWrapStyleWord(true);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setBackground(SystemColor.control);
		jScrollPaneMessage.setPreferredSize(new Dimension(800, 170));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage, null);
		jPanelCenter.setLayout(new BorderLayout());
		jPanelCenter.add(jPanelCenterTop, BorderLayout.NORTH);
		jPanelCenter.add(jScrollPaneElementListFrom, BorderLayout.CENTER);
		jPanelCenter.add(jScrollPaneMessage, BorderLayout.SOUTH);

		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentRight.setHorizontalAlignment(4); //RIGHT//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//

		jTableElementListFrom.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTableElementListFrom.setBackground(SystemColor.control);
		jTableElementListFrom.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableElementListFrom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableElementListFrom.getSelectionModel().addListSelectionListener(new DialogImportModel_jTableElementListFrom_listSelectionAdapter(this));
		jTableElementListFrom.setRowSelectionAllowed(true);
		jTableElementListFrom.setRowHeight(Editor.TABLE_ROW_HEIGHT);
		tableModelElementListFrom.addColumn("NO.");
		tableModelElementListFrom.addColumn("ID");
		tableModelElementListFrom.addColumn(res.getString("ElementName"));
		tableModelElementListFrom.addColumn(res.getString("ElementType"));
		tableModelElementListFrom.addColumn(res.getString("ElementStatus"));
		column0 = jTableElementListFrom.getColumnModel().getColumn(0);
		column1 = jTableElementListFrom.getColumnModel().getColumn(1);
		column2 = jTableElementListFrom.getColumnModel().getColumn(2);
		column3 = jTableElementListFrom.getColumnModel().getColumn(3);
		column4 = jTableElementListFrom.getColumnModel().getColumn(4);
		column0.setPreferredWidth(40);
		column1.setPreferredWidth(120);
		column2.setPreferredWidth(310);
		column3.setPreferredWidth(245);
		column4.setPreferredWidth(95);
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		column4.setCellRenderer(rendererAlignmentLeft);
		jTableElementListFrom.getTableHeader().setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));

		///////////////////////////////////
		// jPanelSouth and objects on it //
		///////////////////////////////////
		jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
		jPanelSouth.setPreferredSize(new Dimension(800, 43));
		jButtonCloseDialog.setBounds(new Rectangle(20, 8, 100, 27));
		jButtonCloseDialog.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogImportModel_jButtonCloseDialog_actionAdapter(this));
		jButtonImport.setBounds(new Rectangle(300, 8, 200, 27));
		jButtonImport.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonImport.setText(res.getString("Import"));
		jButtonImport.addActionListener(new DialogImportModel_jButtonImport_actionAdapter(this));
		jButtonImport.setEnabled(false);
		jButtonImport.setFocusCycleRoot(true);
		jButtonRefresh.setBounds(new Rectangle(680, 8, 150, 27));
		jButtonRefresh.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonRefresh.setText(res.getString("Refresh"));
		jButtonRefresh.addActionListener(new DialogImportModel_jButtonRefresh_actionAdapter(this));
		jButtonRefresh.setEnabled(false);
		jPanelSouth.setLayout(null);
		jPanelSouth.add(jButtonCloseDialog);
		jPanelSouth.add(jButtonImport);
		jPanelSouth.add(jButtonRefresh);

		///////////////////////
		// DialogImportModel //
		///////////////////////
		this.setTitle(res.getString("ImportDialogModelTitle"));
		this.setResizable(false);
		this.getContentPane().add(panelMain);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public boolean request(String fileName) {
		try {
			isListBeingRebuild = false;
			newlyAddedTableIDList.clear();
			newlyAddedFunctionIDList.clear();
			jTextAreaMessage.setText(res.getString("ImportModelMessage0"));
			jButtonImport.setEnabled(false);
			subsystemElementFrom = null;

			jTextFieldFileNameFrom.setText(fileName);
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new FileInputStream(fileName)));
			domDocumentImportingFrom = parser.getDocument();
			NodeList nodeList = domDocumentImportingFrom.getElementsByTagName("System");
			org.w3c.dom.Element systemElementImportFrom = (org.w3c.dom.Element)nodeList.item(0);
			jTextFieldSystemNameFrom.setText(systemElementImportFrom.getAttribute("Name"));
			jTextFieldSystemVersionFrom.setText(systemElementImportFrom.getAttribute("Version"));
			functionTypeList = domDocumentImportingFrom.getElementsByTagName("FunctionType");
			currentTableList = frame_.getDomDocument().getElementsByTagName("Table");
			currentFunctionList = frame_.getDomDocument().getElementsByTagName("Function");
			tableListInModel = domDocumentImportingFrom.getElementsByTagName("Table");
			functionListInModel = domDocumentImportingFrom.getElementsByTagName("Function");

			jComboBoxSubsystemFrom.removeAllItems();
			jComboBoxSubsystemFrom.addItem(res.getString("SelectFromList"));
			dataTypeNodeListFrom = domDocumentImportingFrom.getElementsByTagName("DataType");
			subsystemNodeListFrom = new ArrayList<org.w3c.dom.Element>();
			subsystemNodeListFrom.add(null);

			org.w3c.dom.Element element;
			NodeList nodelist = domDocumentImportingFrom.getElementsByTagName("Subsystem");
			SortableDomElementListModel sortingList = frame_.getSortedListModel(nodelist, "SortKey");
			for (int i = 0; i < sortingList.getSize(); i++) {
				element = (org.w3c.dom.Element)sortingList.getElementAt(i);
				jComboBoxSubsystemFrom.addItem(element.getAttribute("SortKey") + " " + element.getAttribute("Name"));
				subsystemNodeListFrom.add(element);
			}

			super.setVisible(true);

		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, res.getString("FailedToParse") + "\n\n" + e.getMessage());
			e.printStackTrace();
		}

		return (newlyAddedTableIDList.size() > 0 || newlyAddedFunctionIDList.size() > 0);
	}

	public void jComboBoxSubsystemFrom_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element element, functionTypeElement;
		isListBeingRebuild = true;
		subsystemElementFrom = null;
		String wrkStr, subsystemID = "";
		jButtonRefresh.setEnabled(false);

		if (subsystemNodeListFrom != null && jComboBoxSubsystemFrom.getSelectedIndex() >= 0) {
			try {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));

				if (tableModelElementListFrom.getRowCount() > 0) {
					int rowCount = tableModelElementListFrom.getRowCount();
					for (int i = 0; i < rowCount; i++) {tableModelElementListFrom.removeRow(0);}
				}

				subsystemElementFrom = subsystemNodeListFrom.get(jComboBoxSubsystemFrom.getSelectedIndex());
				if (subsystemElementFrom != null) {
					subsystemID = subsystemElementFrom.getAttribute("ID");
					jButtonRefresh.setEnabled(true);

					int rowNumber = 1;
					NodeList nodelist = domDocumentImportingFrom.getElementsByTagName("Table");
					SortableDomElementListModel sortingList = frame_.getSortedListModel(nodelist, "SortKey");
					for (int i = 0; i < sortingList.getSize(); i++) {
						element = (org.w3c.dom.Element)sortingList.getElementAt(i);
						if (element.getAttribute("SubsystemID").equals(subsystemID)) {
							Object[] Cell = new Object[5];
							Cell[0] = new TableRowNumber(rowNumber++, element, "Table", this);
							Cell[1] = element.getAttribute("SortKey");
							Cell[2] = element.getAttribute("Name");
							Cell[3] = "TABLE";
							wrkStr = getStatusOfTable(element);
							if (wrkStr.startsWith("ALTERED:")) {
								Cell[4] = "ALTERED";
							} else {
								if (wrkStr.startsWith("ERROR:")) {
									Cell[4] = "ERROR";
								} else {
									Cell[4] = wrkStr;
								}
							}
							((TableRowNumber)Cell[0]).setMessage(wrkStr);
							if (Cell[4].equals("NEW") || Cell[4].equals("ALTERED")) {
								Cell[1] = "<html><b>" + Cell[1];
								Cell[2] = "<html><b>" + Cell[2];
								Cell[3] = "<html><b>" + Cell[3];
								Cell[4] = "<html><b>" + Cell[4];
							}
							tableModelElementListFrom.addRow(Cell);
						}
					}

					nodelist = domDocumentImportingFrom.getElementsByTagName("Function");
					sortingList = frame_.getSortedListModel(nodelist, "SortKey");
					for (int i = 0; i < sortingList.getSize(); i++) {
						element = (org.w3c.dom.Element)sortingList.getElementAt(i);
						if (element.getAttribute("SubsystemID").equals(subsystemID)) {
							Object[] Cell = new Object[5];
							Cell[0] = new TableRowNumber(rowNumber++, element, "Function", this);
							Cell[1] = element.getAttribute("SortKey");
							Cell[2] = element.getAttribute("Name");
							Cell[3] = "???";
							for (int j = 0; j < functionTypeList.getLength(); j++) {
								functionTypeElement = (org.w3c.dom.Element)functionTypeList.item(j);
								if (functionTypeElement.getAttribute("ID").equals(element.getAttribute("FunctionTypeID"))) {
									Cell[3] = functionTypeElement.getAttribute("SortKey") + " " + functionTypeElement.getAttribute("Name");
								}
							}
							wrkStr = getStatusOfFunction(element);
							if (wrkStr.startsWith("ALTERED:")) {
								Cell[4] = "ALTERED";
							} else {
								if (wrkStr.startsWith("ERROR:")) {
									Cell[4] = "ERROR";
								} else {
									Cell[4] = wrkStr;
								}
							}
							((TableRowNumber)Cell[0]).setMessage(wrkStr);
							if (Cell[4].equals("NEW") || Cell[4].equals("ALTERED")) {
								Cell[1] = "<html><b>" + Cell[1];
								Cell[2] = "<html><b>" + Cell[2];
								Cell[3] = "<html><b>" + Cell[3];
								Cell[4] = "<html><b>" + Cell[4];
							}
							tableModelElementListFrom.addRow(Cell);
						}
					}

					jButtonImport.setEnabled(false);
				}
			} finally {
				jTextAreaMessage.setText(res.getString("ImportModelMessage1"));
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		isListBeingRebuild = false;
	}
	
	public void jTableElementListFrom_valueChanged(ListSelectionEvent e) {
		jButtonImport.setEnabled(false);
		if (jTableElementListFrom.getSelectedRow() != -1 && !isListBeingRebuild) {
			jTableElementListFrom.paintImmediately(jTableElementListFrom.getBounds());
			TableRowNumber tableRowNumber = (TableRowNumber)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 0);
			String message = tableRowNumber.getMessage();
			if (message.startsWith("ERROR:")) {
				jButtonImport.setEnabled(false);
				jTextAreaMessage.setText(res.getString("ImportModelMessage20") + message.replace("ERROR:", "\n" + res.getString("ImportModelMessage29") + "\n"));
			}
			if (message.startsWith("ALTERED:")) {
				jButtonImport.setEnabled(true);
				jTextAreaMessage.setText(res.getString("ImportModelMessage18") + message.replace("ALTERED:", "\n" + res.getString("ImportModelMessage30") + "\n"));
			}
			if (message.equals("NEW")) {
				jButtonImport.setEnabled(true);
				jTextAreaMessage.setText(res.getString("ImportModelMessage3"));
			}
			if (message.equals("IDENTICAL")) {
				jTextAreaMessage.setText(res.getString("ImportModelMessage19"));
			}
			jTextAreaMessage.setCaretPosition(0);
		}
	}
	
	private String getStatusOfTable(org.w3c.dom.Element elementFrom) {
		String status = "";
		org.w3c.dom.Element workElement, fieldElementFrom, fieldElementInto;
		org.w3c.dom.Element keyElementFrom, keyElementInto;
		org.w3c.dom.Element elementInto = null;
		String dataType, alias, keyFieldID, keyType;
		ArrayList<String> aliasList = new ArrayList<String>();
		boolean isValidType, fieldIsAdded, fieldIsDeleted, keyIsAdded, keyIsDeleted;
		StringBuffer error = new StringBuffer();
		SortableDomElementListModel fieldListFrom, fieldListInto = null;
		NodeList nodeList, keyListFrom, keyListInto = null;
		StringBuffer altered = new StringBuffer();
		HashMap<String, String> attrMap;
		int countOfErrors = 0; int countOfAltered = 0;

		////////////////////////////////
		// Checking Table Definitions //
		////////////////////////////////
		for (int i = 0; i < currentTableList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)currentTableList.item(i);
			if (workElement.getAttribute("ID").equals(elementFrom.getAttribute("SortKey"))) {
				elementInto = workElement;
				break;
			}
		}
		if (elementInto != null) {
			if (!elementInto.getAttribute("Name").equals(elementFrom.getAttribute("Name"))) {
				countOfAltered++;
				altered.append(countOfAltered + "." + res.getString("ImportModelMessage21")
						+ elementInto.getAttribute("Name") + res.getString("ImportModelMessage22") + "\n");
			}
			if (!elementInto.getAttribute("Remarks").equals(elementFrom.getAttribute("Descriptions"))) {
				countOfAltered++;
				altered.append(countOfAltered + "." + res.getString("Remarks") + "\n");
			}
			if (!elementInto.getAttribute("SubsystemID").equals(subsystemElementFrom.getAttribute("SortKey"))) {
				countOfAltered++;
				altered.append(countOfAltered + "." + res.getString("ImportModelMessage23")
						+ elementInto.getAttribute("SubsystemID") + res.getString("ImportModelMessage22") + "\n");
			}

			nodeList = elementInto.getElementsByTagName("Field");
			fieldListInto = frame_.getSortedListModel(nodeList, "Order");
			keyListInto = elementInto.getElementsByTagName("Key");
		}

		////////////////////////////////
		// Checking Field Definitions //
		////////////////////////////////
		nodeList = elementFrom.getElementsByTagName("TableField");
		fieldListFrom = frame_.getSortedListModel(nodeList, "SortKey");
		for (int i = 0; i < fieldListFrom.size(); i++) {
			fieldElementFrom = (org.w3c.dom.Element)fieldListFrom.getElementAt(i);
			if (fieldElementFrom.getAttribute("Alias").equals("")) {
				alias = fieldElementFrom.getAttribute("Name");
			} else {
				alias = fieldElementFrom.getAttribute("Alias");
			}
			if (alias.equals("")) {
				countOfErrors++;
				error.append(countOfErrors + "." + res.getString("ImportModelMessage14")
							+ " FieldName=" + fieldElementFrom.getAttribute("Name") + "\n");
			} else {
				if (aliasList.contains(alias)) {
					countOfErrors++;
					error.append(countOfErrors + "." + res.getString("ImportModelMessage17")
								+ " FieldName=" + fieldElementFrom.getAttribute("Name") + "\n");
				} else {
					attrMap = convertDataTypeIDToAttrMap(fieldElementFrom.getAttribute("DataTypeID"));
					dataType = attrMap.get("Type");
					isValidType = false;
					for (int j = 0; j < frame_.jComboBoxTableFieldType.getItemCount(); j++) {
						if (frame_.jComboBoxTableFieldType.getItemAt(j).equals(dataType)) {
							isValidType = true;
							break;
						}
					}
					if (isValidType) {
						if (fieldListInto != null) {
							fieldIsAdded = true;
							for (int j = 0; j < fieldListInto.getSize(); j++) {
								fieldElementInto = (org.w3c.dom.Element)fieldListInto.getElementAt(j);
								if (fieldElementInto.getAttribute("ID").equals(alias)) {
									fieldIsAdded = false;
									if (!fieldElementInto.getAttribute("Name").equals(fieldElementFrom.getAttribute("Name"))) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ res.getString("Name") + ":"
														+ fieldElementFrom.getAttribute("Name") + "\n");
									}
									if (!fieldElementInto.getAttribute("Remarks").equals(fieldElementFrom.getAttribute("Descriptions"))) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ res.getString("Remarks") + "\n");
									}
									if (fieldElementInto.getAttribute("Nullable").equals("T") && fieldElementFrom.getAttribute("NotNull").equals("true")
											|| fieldElementInto.getAttribute("Nullable").equals("F") && fieldElementFrom.getAttribute("NotNull").equals("false")) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ "NotNull" + ":"
														+ fieldElementFrom.getAttribute("NotNull") + "\n");
									}
									if (!attrMap.get("Type").equals(fieldElementInto.getAttribute("Type"))) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ res.getString("DataType") + ":"
														+ attrMap.get("Type") + "\n");
									}
									if (i != j) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ res.getString("ListOrder") + "\n");
									}
									if (!attrMap.get("Type").equals("DATE") && !attrMap.get("Type").equals("DATETIME")) {
										if (!attrMap.get("Size").equals(fieldElementInto.getAttribute("Size"))) {
											countOfAltered++;
											altered.append(countOfAltered + "." + "Field "
															+ alias + res.getString("ImportModelMessage24")
															+ res.getString("FieldSize") + ":"
															+ attrMap.get("Size") + "\n");
										}
										if (!attrMap.get("Decimal").equals(fieldElementInto.getAttribute("Decimal"))) {
											countOfAltered++;
											altered.append(countOfAltered + "." + "Field "
															+ alias + res.getString("ImportModelMessage24")
															+ res.getString("Decimal") + ":"
															+ attrMap.get("Decimal") + "\n");
										}
									}
									if (fieldElementInto.getAttribute("NoUpdate").equals("T") && fieldElementFrom.getAttribute("NoUpdate").equals("false")
											|| fieldElementInto.getAttribute("NoUpdate").equals("F") && fieldElementFrom.getAttribute("NoUpdate").equals("true")) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ res.getString("NoUpdate") + ":"
														+ fieldElementFrom.getAttribute("NoUpdate") + "\n");
									}
									if (fieldElementInto.getAttribute("TypeOptions").contains("VIRTUAL")
											&& !fieldElementFrom.getAttribute("AttributeType").equals("DERIVABLE")) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ res.getString("AttributeType") + ":"
														+ fieldElementFrom.getAttribute("AttributeType") + "\n");
									}
									if (!fieldElementInto.getAttribute("TypeOptions").contains("VIRTUAL")
											&& fieldElementFrom.getAttribute("AttributeType").equals("DERIVABLE")) {
										countOfAltered++;
										altered.append(countOfAltered + "." + "Field "
														+ alias + res.getString("ImportModelMessage24")
														+ res.getString("AttributeType") + ":"
														+ fieldElementFrom.getAttribute("AttributeType") + "\n");
									}
									break;
								}
							}
							if (fieldIsAdded) {
								countOfAltered++;
								altered.append(countOfAltered + "." + "Field "
												+ alias + res.getString("ImportModelMessage25") + "\n");
							}
						}
						
					} else {
						countOfErrors++;
						error.append(countOfErrors + "." + res.getString("ImportModelMessage13")
								+ " Field=" + fieldElementFrom.getAttribute("Name") + "("  + alias + ")"
								+ " DataType=" + dataType + "\n");
					}
				}
			}
			aliasList.add(alias);
		}
		if (fieldListInto != null) {
			for (int j = 0; j < fieldListInto.size(); j++) {
				fieldIsDeleted = true;
				fieldElementInto = (org.w3c.dom.Element)fieldListInto.getElementAt(j);
				for (int i = 0; i < fieldListFrom.size(); i++) {
					fieldElementFrom = (org.w3c.dom.Element)fieldListFrom.getElementAt(i);
					alias = fieldElementFrom.getAttribute("Alias");
					if (alias.equals(fieldElementInto.getAttribute("ID"))) {
						fieldIsDeleted = false;
						break;
					}
				}
				if (fieldIsDeleted) {
					countOfAltered++;
					altered.append(countOfAltered + "." + "Field "
									+ fieldElementInto.getAttribute("ID")
									+ res.getString("ImportModelMessage26") + "\n");
				}
			}
		}

		//////////////////////////////
		// Checking Key Definitions //
		//////////////////////////////
		if (keyListInto != null) {
			keyListFrom = elementFrom.getElementsByTagName("TableKey");
			for (int i = 0; i < keyListFrom.getLength(); i++) {

				keyElementFrom = (org.w3c.dom.Element)keyListFrom.item(i);
				keyType = keyElementFrom.getAttribute("Type");
				if (!keyType.equals("FK")) {

					keyFieldID = getFieldIDListOfTableKeyFrom(elementFrom, keyElementFrom);
					keyIsAdded = true;
					for (int j = 0; j < keyListInto.getLength(); j++) {

						keyElementInto = (org.w3c.dom.Element)keyListInto.item(j);
						if (keyType.equals("PK") && keyElementInto.getAttribute("Type").equals(keyType)) {
							keyIsAdded = false;
							if (!keyElementInto.getAttribute("Fields").equals(keyFieldID)) {
								countOfAltered++;
								altered.append(countOfAltered + "." + keyType
												+ " With Fields[" + keyFieldID
												+ "]" + res.getString("ImportModelMessage27") + "\n");
							}
							break;
						}
						if (!keyType.equals("PK") && keyElementInto.getAttribute("Type").equals(keyType)
								&& keyElementInto.getAttribute("Fields").equals(keyFieldID)) {
							keyIsAdded = false;
							break;
						}
					}
					if (!keyType.equals("PK") && keyIsAdded) {
						countOfAltered++;
						altered.append(countOfAltered + "." + keyType
										+ " With Fields[" + keyFieldID
										+ "]" + res.getString("ImportModelMessage27") + "\n");
					}
				}
			}
			for (int j = 0; j < keyListInto.getLength(); j++) {
				keyElementInto = (org.w3c.dom.Element)keyListInto.item(j);
				if (!keyElementInto.getAttribute("Type").equals("PK") && !keyElementInto.getAttribute("Type").equals("FK")) {
					keyIsDeleted = true;
					for (int i = 0; i < keyListFrom.getLength(); i++) {
						keyElementFrom = (org.w3c.dom.Element)keyListFrom.item(i);
						keyType = keyElementFrom.getAttribute("Type");
						keyFieldID = getFieldIDListOfTableKeyFrom(elementFrom, keyElementFrom);
						if (keyElementInto.getAttribute("Type").equals(keyType)
								&& keyElementInto.getAttribute("Fields").equals(keyFieldID)) {
							keyIsDeleted = false;
							break;
						}
					}
					if (keyIsDeleted) {
						countOfAltered++;
						altered.append(countOfAltered + "." + keyElementInto.getAttribute("Type")
										+ " With Fields[" + keyElementInto.getAttribute("Fields")
										+ "]" + res.getString("ImportModelMessage26") + "\n");
					}
				}
			}
		}

		/////////////////////////////
		// Processing Status Value //
		/////////////////////////////
		if (elementInto == null) {
			String wrk = error.toString();
			if (wrk.equals("")) {
				status = "NEW";
			} else {
				status = "ERROR:" + wrk;
			}
		} else {
			String wrk = error.toString();
			if (wrk.equals("")) {
				wrk = altered.toString();
				if (wrk.equals("")) {
					status = "IDENTICAL";
				} else {
					status = "ALTERED:" + wrk; 
				}
			} else {
				status = "ERROR:" + wrk;
			}
		}

		return status;
	}

	private String getFieldIDListOfTableKeyFrom(org.w3c.dom.Element tableElementFrom, org.w3c.dom.Element keyElementFrom) {
		String fieldID = "";
		org.w3c.dom.Element workElement1, workElement2;
		String externalID, internalID;
		ArrayList<String> fieldInternalIDList = new ArrayList<String>();
		ArrayList<String> fieldExternalIDList = new ArrayList<String>();
		NodeList fieldListFrom = tableElementFrom.getElementsByTagName("TableField");

		NodeList nodeList = keyElementFrom.getElementsByTagName("TableKeyField");
		SortableDomElementListModel sortingList = frame_.getSortedListModel(nodeList, "SortKey");
		for (int i = 0; i < sortingList.getSize(); i++) {
			workElement1 = (org.w3c.dom.Element)sortingList.getElementAt(i);
			internalID = workElement1.getAttribute("FieldID");
			externalID = "";
			for (int j = 0; j < fieldListFrom.getLength(); j++) {
				workElement2 = (org.w3c.dom.Element)fieldListFrom.item(j);
				if (workElement2.getAttribute("ID").equals(internalID)) {
					if (workElement2.getAttribute("Alias").equals("")) {
						externalID = workElement2.getAttribute("Name");
					} else {
						externalID = workElement2.getAttribute("Alias");
					}
					if (keyElementFrom.getAttribute("Type").equals("XK")) {
						if (workElement1.getAttribute("AscDesc").equals("D")) {
							externalID = externalID + "(D)";
						} else {
							externalID = externalID + "(A)";
						}
					}
					break;
				}
			}
			fieldInternalIDList.add(internalID);
			fieldExternalIDList.add(externalID);
		}

		for (int i = 0; i < fieldExternalIDList.size(); i++) {
			if (!fieldID.equals("")) {
				fieldID = fieldID + ";";
			}
			fieldID = fieldID + fieldExternalIDList.get(i);
		}
		return fieldID;
	}
	
	private String getStatusOfFunction(org.w3c.dom.Element elementFrom) {
		String status = "";
		org.w3c.dom.Element workElement;
		org.w3c.dom.Element elementInto = null;
		StringBuffer error = new StringBuffer();
		StringBuffer altered = new StringBuffer();
		int countOfErrors = 0;
		int countOfAltered = 0;
		String primaryTableExternalID, headerTableExternalID, headerTableInternalID;
		NodeList nodeList;

		///////////////////////////////////
		// Checking Function Definitions //
		///////////////////////////////////
		for (int i = 0; i < currentFunctionList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)currentFunctionList.item(i);
			if (workElement.getAttribute("ID").equals(elementFrom.getAttribute("SortKey"))) {
				elementInto = workElement;
				break;
			}
		}
		if (elementInto != null) {
			if (!elementInto.getAttribute("Name").equals(elementFrom.getAttribute("Name"))) {
				countOfAltered++;
				altered.append(countOfAltered + "." + res.getString("ImportModelMessage21")
						+ elementInto.getAttribute("Name") + res.getString("ImportModelMessage22") + "\n");
			}
			if (!elementInto.getAttribute("SubsystemID").equals(subsystemElementFrom.getAttribute("SortKey"))) {
				countOfAltered++;
				altered.append(countOfAltered + "." + res.getString("ImportModelMessage23")
						+ elementInto.getAttribute("SubsystemID") + res.getString("ImportModelMessage22") + "\n");
			}
		}

		////////////////////////////
		// Checking Function Type //
		////////////////////////////
		String functionType = "";
		for (int i = 0; i < functionTypeList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)functionTypeList.item(i);
			if (workElement.getAttribute("ID").equals(elementFrom.getAttribute("FunctionTypeID"))) {
				functionType = workElement.getAttribute("SortKey").trim();
				break;
			}
		}
		if (!functionType.equals("XF000") && !functionType.equals("XF100")
				&& !functionType.equals("XF110") && !functionType.equals("XF200")
				&& !functionType.equals("XF290") && !functionType.equals("XF300")
				&& !functionType.equals("XF310") && !functionType.equals("XF390")) {
			countOfErrors++;
			error.append(countOfErrors + "." + res.getString("ImportModelMessage12") + "(" + functionType + ")\n");
		} else {
			if (elementInto != null && !elementInto.getAttribute("Type").equals(functionType)) {
				countOfErrors++;
				error.append(countOfErrors + "." + res.getString("ImportModelMessage28")
							+ elementInto.getAttribute("Type") + res.getString("ImportModelMessage22") + "\n");
			}
		}

		//////////////////////////////////////////////////////
		// Checking IO-Table For XF100, XF110, XF200, XF290 //
		//////////////////////////////////////////////////////
		if (functionType.equals("XF100") || functionType.equals("XF110")
				|| functionType.equals("XF200") || functionType.equals("XF290")) {
			primaryTableExternalID = "";
			nodeList = elementFrom.getElementsByTagName("IOTable");
			if (nodeList.getLength() == 1) {
				workElement = (org.w3c.dom.Element)nodeList.item(0);
				primaryTableExternalID = convertTableInternalIDToExternalID(workElement.getAttribute("TableID"));
			} else {
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("PRIMARY")
							|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("PrimaryTable").toUpperCase())) {
						primaryTableExternalID = convertTableInternalIDToExternalID(workElement.getAttribute("TableID"));
						break;
					}
				}
			}
			if (primaryTableExternalID.equals("")) {
				countOfErrors++;
				error.append(countOfErrors + "." + res.getString("ImportModelMessage6") + "\n");
			} else {
				if (isExistingTable(primaryTableExternalID)) {
					if (elementInto != null && elementInto.getAttribute("Type").equals(functionType)) {
						if (!elementInto.getAttribute("PrimaryTable").equals(primaryTableExternalID)) {
							countOfAltered++;
							altered.append(countOfAltered + "." + res.getString("PrimaryTable") + ":"
											+ primaryTableExternalID + "\n");
						}
					}
				} else {
					countOfErrors++;
					error.append(countOfErrors + "." + res.getString("ImportModelMessage7")
									+ "(" + primaryTableExternalID + ")\n");
				}
			}
		}

		///////////////////////////////////////////////
		// Checking IO-Table For XF300, XF310, XF390 //
		///////////////////////////////////////////////
		if (functionType.equals("XF300") || functionType.equals("XF310") || functionType.equals("XF390")) {
			headerTableExternalID = "";
			headerTableInternalID = "";
			ArrayList<String> detailTableExternalIDList = new ArrayList<String>();
			ArrayList<String> detailTableInternalIDList = new ArrayList<String>();
			nodeList = elementFrom.getElementsByTagName("IOTable");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("HEADER")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("HeaderTable").toUpperCase())) {
					headerTableInternalID = workElement.getAttribute("TableID");
					headerTableExternalID = convertTableInternalIDToExternalID(headerTableInternalID);
					break;
				}
			}
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("DETAIL")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("DetailTable").toUpperCase())) {
					detailTableInternalIDList.add(workElement.getAttribute("TableID"));
					detailTableExternalIDList.add(convertTableInternalIDToExternalID(workElement.getAttribute("TableID")));
				}
			}
			if (headerTableExternalID.equals("")) {
				countOfErrors++;
				error.append(countOfErrors + "." + res.getString("ImportModelMessage8") + "\n");
			} else {
				if (isExistingTable(headerTableExternalID)) {
					if (elementInto != null && elementInto.getAttribute("Type").equals(functionType)) {
						if (!elementInto.getAttribute("HeaderTable").equals(headerTableExternalID)) {
							countOfAltered++;
							altered.append(countOfAltered + "." + res.getString("HDRTable") + ":"
											+ headerTableExternalID + "\n");
						}
					}
				} else {
					countOfErrors++;
					error.append(countOfErrors + "." + res.getString("ImportModelMessage9")
									+ "(" + headerTableExternalID + ")\n");
				}
			}
			if (detailTableExternalIDList.size() == 0) {
				countOfErrors++;
				error.append(countOfErrors + "." + res.getString("ImportModelMessage10") + "\n");
			} else {
				int numberOfValidDetailTables = 0;
				NodeList detailTabList;
				boolean isFound;
				for (int i = 0; i < detailTableExternalIDList.size(); i++) {
					if (isExistingTable(detailTableExternalIDList.get(i))
							&& isValidWithKeys(headerTableInternalID, detailTableInternalIDList.get(i))) {
						numberOfValidDetailTables++;
						if (functionType.equals("XF310")) {
							if (!elementInto.getAttribute("DetailTable").equals(detailTableExternalIDList.get(i))) {
								countOfAltered++;
								altered.append(countOfAltered + "." + res.getString("DTLTable") + ":"
										+ detailTableExternalIDList.get(i) + "\n");
							}
						}
						if (functionType.equals("XF300") || functionType.equals("XF390")) {
							detailTabList = elementInto.getElementsByTagName("Detail");
							isFound = false;
							for (int j = 0; j < detailTabList.getLength(); j++) {
								workElement = (org.w3c.dom.Element)detailTabList.item(j);
								if (workElement.getAttribute("Table").equals(detailTableExternalIDList.get(i))) {
									isFound = true;
									break;
								}
							}
							if (!isFound) {
								countOfAltered++;
								altered.append(countOfAltered + "." + res.getString("DTLTable") + ":"
										+ detailTableExternalIDList.get(i) + "\n");
							}
						}
					}
				}
				if (numberOfValidDetailTables == 0) {
					countOfErrors++;
					error.append(countOfErrors + "." + res.getString("ImportModelMessage10") + "\n");
				}
			}
		}

		/////////////////////////////
		// Processing Status Value //
		/////////////////////////////
		if (elementInto == null) {
			String wrk = error.toString();
			if (wrk.equals("")) {
				status = "NEW";
			} else {
				status = "ERROR:" + wrk;
			}
		} else {
			String wrk = error.toString();
			if (wrk.equals("")) {
				wrk = altered.toString();
				if (wrk.equals("")) {
					status = "IDENTICAL";
				} else {
					status = "ALTERED:" + wrk; 
				}
			} else {
				status = "ERROR:" + wrk;
			}
		}

		return status;
	}

	private boolean isValidWithKeys(String headerTableID, String detailTableID) {
		if (getDetailKeys(headerTableID, detailTableID)[0].equals("*ERROR")) {
			return false;
		} else {
			return true;
		}
	}

	private String[] getDetailKeys(String headerTableInternalID, String detailTableInternalID) {
		String[] keyFields = {"", ""};
		org.w3c.dom.Element element;
		String fieldID;
		int index = 0;
		String keyFieldsOfHeaderTable = "";
		String keyFieldsOfDetailTable = "";
		StringTokenizer tokenizer1, tokenizer2;
		ArrayList<String> headerPKFieldTypeList = new ArrayList<String>();
		ArrayList<String> detailPKFieldTypeList = new ArrayList<String>();

		String tableExternalID = convertTableInternalIDToExternalID(headerTableInternalID);
		org.w3c.dom.Element headerTableElement = getCurrentTableElementWithID(tableExternalID);
		NodeList headerTableKeyNodeList = headerTableElement.getElementsByTagName("Key");
		NodeList headerTableFieldNodeList = headerTableElement.getElementsByTagName("Field");
		org.w3c.dom.Element headerTableKeyElement = null;
		for (int i = 0; i < headerTableKeyNodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)headerTableKeyNodeList.item(i);
			if (element.getAttribute("Type").equals("PK")) {
				headerTableKeyElement = element;
				break;
			}
		}

		tableExternalID = convertTableInternalIDToExternalID(detailTableInternalID);
		org.w3c.dom.Element detailTableElement = getCurrentTableElementWithID(tableExternalID);
		NodeList detailTableKeyNodeList = detailTableElement.getElementsByTagName("Key");
		NodeList detailTableFieldNodeList = detailTableElement.getElementsByTagName("Field");
		org.w3c.dom.Element detailTableKeyElement = null;
		for (int i = 0; i < detailTableKeyNodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)detailTableKeyNodeList.item(i);
			if (element.getAttribute("Type").equals("PK")) {
				detailTableKeyElement = element;
				break;
			}
		}

		if (headerTableKeyElement == null || detailTableKeyElement == null) {
			keyFields[0] = "*ERROR";
		} else {
			boolean isErrorWithKey = false;
			tokenizer1 = new StringTokenizer(headerTableKeyElement.getAttribute("Fields"), ";");
			tokenizer2 = new StringTokenizer(detailTableKeyElement.getAttribute("Fields"), ";");
			if (tokenizer1.countTokens() < tokenizer2.countTokens()) {
				while (tokenizer1.hasMoreTokens()) {
					fieldID = tokenizer1.nextToken();
					for (int i = 0; i < headerTableFieldNodeList.getLength(); i++) {
						element = (org.w3c.dom.Element)headerTableFieldNodeList.item(i);
						if (element.getAttribute("ID").equals(fieldID)) {
							headerPKFieldTypeList.add(element.getAttribute("Type"));
						}
					}	
				}
				while (tokenizer2.hasMoreTokens()) {
					fieldID = tokenizer2.nextToken();
					for (int i = 0; i < detailTableFieldNodeList.getLength(); i++) {
						element = (org.w3c.dom.Element)detailTableFieldNodeList.item(i);
						if (element.getAttribute("ID").equals(fieldID)) {
							detailPKFieldTypeList.add(element.getAttribute("Type"));
						}
					}	
				}
				for (int i = 0; i < headerPKFieldTypeList.size(); i++) {
					if (i == 0) {
						keyFieldsOfHeaderTable = headerPKFieldTypeList.get(i);
					} else {
						keyFieldsOfHeaderTable = keyFieldsOfHeaderTable + ";" + headerPKFieldTypeList.get(i);
					}
					index = detailPKFieldTypeList.indexOf(headerPKFieldTypeList.get(i));
					if (index == -1) {
						isErrorWithKey = true;
						break;
					} else {
						if (i == 0) {
							keyFieldsOfDetailTable = detailPKFieldTypeList.get(index);
						} else {
							keyFieldsOfDetailTable = keyFieldsOfDetailTable + ";" + detailPKFieldTypeList.get(index);
						}
					}
				}
				if (!isErrorWithKey) {
					for (int i = 0; i < detailPKFieldTypeList.size(); i++) {
						if (!headerPKFieldTypeList.contains(detailPKFieldTypeList.get(i))) {
							keyFieldsOfDetailTable = keyFieldsOfDetailTable + ";" + detailPKFieldTypeList.get(i);
						}
					}
					keyFields[0] = getFieldsOfKey(headerTableInternalID, keyFieldsOfHeaderTable, false);
					keyFields[1] = getFieldsOfKey(detailTableInternalID, keyFieldsOfDetailTable, true);
				}	
			} else {
				isErrorWithKey = true;
			}

			if (isErrorWithKey) {
				keyFields = getKeysAccordingToRelationship(headerTableElement, detailTableElement);
			}
		}

		return keyFields;
	}

	private String[] getKeysAccordingToRelationship(org.w3c.dom.Element headerTableElement, org.w3c.dom.Element detailTableElement) {
		String[] keys = {"", ""};
		org.w3c.dom.Element relationshipElement;
		String internalHeaderTableID = getInternalTableID(headerTableElement.getAttribute("ID"));
		String internalDetailTableID = getInternalTableID(detailTableElement.getAttribute("ID"));

		NodeList relationshipList = domDocumentImportingFrom.getElementsByTagName("Relationship");
		for (int i = 0; i < relationshipList.getLength(); i++) {
			relationshipElement = (org.w3c.dom.Element)relationshipList.item(i);
			if (relationshipElement.getAttribute("Table1ID").equals(internalHeaderTableID)
					&& relationshipElement.getAttribute("Table2ID").equals(internalDetailTableID)
					&& relationshipElement.getAttribute("Type").equals("REFFER")) {
				keys[0] = getFieldsOfKey(internalHeaderTableID, relationshipElement.getAttribute("TableKey1ID"), false);
				keys[1] = getFieldsOfKey(internalDetailTableID, relationshipElement.getAttribute("TableKey2ID"), true);
				break;
			}
		}
		if (keys[0].equals("")) {
			keys[0] = "*ERROR";
		}

		return keys;
	}
	
	private String getInternalTableID(String externalID) {
		String internalID = "";
		org.w3c.dom.Element element;
		for (int i = 0; i < tableListInModel.getLength(); i++) {
			element = (org.w3c.dom.Element)tableListInModel.item(i);
			if (element.getAttribute("SortKey").equals(externalID)) {
				internalID = element.getAttribute("ID");
				break;
			}
		}
		return internalID;
	}

	private String getFieldsOfKey(String internalTableID, String internalKeyID, boolean isDetailKeys) {
		String keyFields = "";
		org.w3c.dom.Element element, element1, element2, element3;
		ArrayList<String> keyFieldIDList = new ArrayList<String>(); 
		ArrayList<String> primaryKeyFieldIDList = new ArrayList<String>(); 

		for (int i = 0; i < tableListInModel.getLength(); i++) {
			element = (org.w3c.dom.Element)tableListInModel.item(i);
			if (element.getAttribute("ID").equals(internalTableID)) {
				NodeList fieldList = element.getElementsByTagName("TableField");
				NodeList keyList = element.getElementsByTagName("TableKey");
				for (int j = 0; j < keyList.getLength(); j++) {
					element1 = (org.w3c.dom.Element)keyList.item(j);
					if (element1.getAttribute("ID").equals(internalKeyID)) {
						NodeList keyFieldList = element1.getElementsByTagName("TableKeyField");
						SortableDomElementListModel sortingList = frame_.getSortedListModel(keyFieldList, "SortKey");
						for (int k = 0; k < sortingList.getSize(); k++) {
							element2 = (org.w3c.dom.Element)sortingList.getElementAt(k);
							for (int m = 0; m < fieldList.getLength(); m++) {
								element3 = (org.w3c.dom.Element)fieldList.item(m);
								if (element2.getAttribute("FieldID").equals(element3.getAttribute("ID"))) {
									keyFieldIDList.add(element3.getAttribute("Alias"));
								}
							}
						}
					}
					if (isDetailKeys && element1.getAttribute("Type").equals("PK")) {
						NodeList keyFieldList = element1.getElementsByTagName("TableKeyField");
						SortableDomElementListModel sortingList = frame_.getSortedListModel(keyFieldList, "SortKey");
						for (int k = 0; k < sortingList.getSize(); k++) {
							element2 = (org.w3c.dom.Element)sortingList.getElementAt(k);
							for (int m = 0; m < fieldList.getLength(); m++) {
								element3 = (org.w3c.dom.Element)fieldList.item(m);
								if (element2.getAttribute("FieldID").equals(element3.getAttribute("ID"))) {
									primaryKeyFieldIDList.add(element3.getAttribute("Alias"));
								}
							}
						}
					}
				}

				StringBuffer bf = new StringBuffer();
				if (keyFieldIDList.contains("")) {
					keyFields = "";
				} else {
					for (int j = 0; j < keyFieldIDList.size(); j++) {
						if (j > 0) {
							bf.append(";");
						}
						bf.append(keyFieldIDList.get(j));
					}
					if (isDetailKeys) {
						for (int j = 0; j < primaryKeyFieldIDList.size(); j++) {
							if (!keyFieldIDList.contains(primaryKeyFieldIDList.get(j))) {
								if (!bf.toString().equals("")) {
									bf.append(";");
								}
								bf.append(primaryKeyFieldIDList.get(j));
							}
						}
					}
					keyFields = bf.toString();
				}

				break;
			}
		}

		return keyFields;
	}
	
	private HashMap<String, String> convertDataTypeIDToAttrMap(String dataTypeID) {
		String wrkStr;
		String dataType = "";
		String dataSize = "0";
		String dataDecimal = "";
		HashMap<String, String> attrMap = new HashMap<String, String>();
		int pos1, pos2, pos3;
		org.w3c.dom.Element element;
		for (int i = 0; i < dataTypeNodeListFrom.getLength(); i++) {
			element = (org.w3c.dom.Element)dataTypeNodeListFrom.item(i);
			if (element.getAttribute("ID").equals(dataTypeID)) {
				wrkStr = element.getAttribute("SQLExpression").toUpperCase();
				dataSize = element.getAttribute("Length");
				pos1 = wrkStr.indexOf("(", 0);
				if (pos1 > 0) {
					dataType = wrkStr.substring(0, pos1);
					pos2 = wrkStr.indexOf(".", pos1);
					if (pos2 > 0) {
						dataSize = wrkStr.substring(pos1+1, pos2);
						pos3 = wrkStr.indexOf(")", pos2);
						if (pos3 > 0) {
							dataDecimal = wrkStr.substring(pos2+1, pos3);
						}
					} else {
						pos2 = wrkStr.indexOf(")", pos1);
						if (pos2 > 0) {
							dataSize = wrkStr.substring(pos1+1, pos2);
						}
					}
				} else {
					dataType = wrkStr;
				}
				break;
			}
		}
		attrMap.put("Type", dataType);
		attrMap.put("Size", dataSize);
		attrMap.put("Decimal", dataDecimal);
		return attrMap;
	}
	
	private String convertTableInternalIDToExternalID(String internalID) {
		String externalID = "";
		org.w3c.dom.Element element;
		for (int i = 0; i < tableListInModel.getLength(); i++) {
			element = (org.w3c.dom.Element)tableListInModel.item(i);
			if (element.getAttribute("ID").equals(internalID)) {
				externalID = element.getAttribute("SortKey");
				break;
			}
		}
		return externalID;
	}
	
	private String convertFunctionInternalIDToExternalID(String internalID) {
		String externalID = "";
		org.w3c.dom.Element element;
		for (int i = 0; i < functionListInModel.getLength(); i++) {
			element = (org.w3c.dom.Element)functionListInModel.item(i);
			if (element.getAttribute("ID").equals(internalID)) {
				externalID = element.getAttribute("SortKey");
				break;
			}
		}
		return externalID;
	}

	void jButtonImport_actionPerformed(ActionEvent e) {
		String message = "";
		org.w3c.dom.Element currentElement = null;
		boolean isNotExisting;

		if (jTableElementListFrom.getSelectedRow() != -1) {
			TableRowNumber tableRowNumber = (TableRowNumber)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 0);
			org.w3c.dom.Element element = tableRowNumber.getElement();
			String id = element.getAttribute("SortKey").toUpperCase();

			if (jCheckBoxOverrideID.isSelected()) {
				id = JOptionPane.showInputDialog(null, res.getString("ImportModelMessage4"), id);
			}

			if (id != null) {
				if (id.equals("")) {
					message = res.getString("ImportModelMessage5");
				} else {
					try {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						id = id.toUpperCase();
						createSubsystemIfNotExisting();

						if (tableRowNumber.isTable()) {
							isNotExisting = true;
							NodeList currentElementList = frame_.getDomDocument().getElementsByTagName("Table");
							for (int i = 0; i < currentElementList.getLength(); i++) {
								currentElement = (org.w3c.dom.Element)currentElementList.item(i);
								if (currentElement.getAttribute("ID").equals(id)) {
									isNotExisting= false;
									break;
								}
							}
							if (isNotExisting) {
								importToCreateTable(element, id);
							} else {
								importToUpdateTable(element, currentElement);
							}
							if (!newlyAddedTableIDList.contains(id)) {
								newlyAddedTableIDList.add(id);
							}
							message = "Table " + id + " " + res.getString("ImportModelMessage15");
						}

						if (tableRowNumber.isFunction()) {
							isNotExisting = true;
							NodeList currentElementList = frame_.getDomDocument().getElementsByTagName("Function");
							for (int i = 0; i < currentElementList.getLength(); i++) {
								currentElement = (org.w3c.dom.Element)currentElementList.item(i);
								if (currentElement.getAttribute("ID").equals(id)) {
									isNotExisting= false;
									break;
								}
							}
							if (isNotExisting) {
								importToCreateFunction(element, id);
							} else {
								importToUpdateFunction(element, currentElement);
							}
							if (!newlyAddedFunctionIDList.contains(id)) {
								newlyAddedFunctionIDList.add(id);
							}
							message = "Function " + id + " " + res.getString("ImportModelMessage15");
						}

						String cellText = (String)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 1);
						tableModelElementListFrom.setValueAt(cellText.replaceFirst("<html><b>", ""), jTableElementListFrom.getSelectedRow(), 1);
						cellText = (String)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 2);
						tableModelElementListFrom.setValueAt(cellText.replaceFirst("<html><b>", ""), jTableElementListFrom.getSelectedRow(), 2);
						cellText = (String)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 3);
						tableModelElementListFrom.setValueAt(cellText.replaceFirst("<html><b>", ""), jTableElementListFrom.getSelectedRow(), 3);
						cellText = (String)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 4);
						tableModelElementListFrom.setValueAt(cellText.replaceFirst("<html><b>", ""), jTableElementListFrom.getSelectedRow(), 4);

					} finally {
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
				jTextAreaMessage.setText(message);
			}
		}
	}

	void jButtonRefresh_actionPerformed(ActionEvent e) {
		jComboBoxSubsystemFrom_actionPerformed(null);
	}

	private void createSubsystemIfNotExisting() {
		boolean isExistingSubsystem = false;
		org.w3c.dom.Element workElement, subsystemElement;

		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("Subsystem");
		for (int i = 0; i < nodeList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)nodeList.item(i);
			if (workElement.getAttribute("ID").equals(subsystemElementFrom.getAttribute("SortKey"))) { 
				isExistingSubsystem = true;
				break;
			}
		}
		if (!isExistingSubsystem) {
			subsystemElement = frame_.getDomDocument().createElement("Subsystem");
			subsystemElement.setAttribute("ID", subsystemElementFrom.getAttribute("SortKey"));
			subsystemElement.setAttribute("Name", subsystemElementFrom.getAttribute("Name"));
			subsystemElement.setAttribute("Remarks", subsystemElementFrom.getAttribute("Descriptions"));
			org.w3c.dom.Element systemElementImportInto = (org.w3c.dom.Element)nodeList.item(0);
			systemElementImportInto.appendChild(subsystemElement);
		}
	}
	
	private void importToCreateTable(org.w3c.dom.Element modelElement, String tableID) {
		SortableDomElementListModel fieldSortingList, sortingList;
		org.w3c.dom.Element workElement1, workElement2, workElement3, childElement;

		/////////////////////////////
		// Create Table Definition //
		/////////////////////////////
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("System");
		org.w3c.dom.Element systemElementImportInto = (org.w3c.dom.Element)nodeList.item(0);
		org.w3c.dom.Element newElementToBeAdded = frame_.getDomDocument().createElement("Table");
		systemElementImportInto.appendChild(newElementToBeAdded);
		newElementToBeAdded.setAttribute("ID", tableID);
		newElementToBeAdded.setAttribute("Name", modelElement.getAttribute("Name"));
		newElementToBeAdded.setAttribute("Remarks", modelElement.getAttribute("Descriptions"));
		newElementToBeAdded.setAttribute("SubsystemID", subsystemElementFrom.getAttribute("SortKey"));

		/////////////////////////////
		// Create Field Definition //
		/////////////////////////////
		HashMap<String, String> attrMap;
		nodeList = modelElement.getElementsByTagName("TableField");
		fieldSortingList = frame_.getSortedListModel(nodeList, "SortKey");
		for (int i = 0; i < fieldSortingList.getSize(); i++) {
			workElement1 = (org.w3c.dom.Element)fieldSortingList.getElementAt(i);

			childElement = frame_.getDomDocument().createElement("Field");

			if (workElement1.getAttribute("Alias").equals("")) {
				childElement.setAttribute("ID", workElement1.getAttribute("Name"));
			} else {
				childElement.setAttribute("ID", workElement1.getAttribute("Alias"));
			}
			childElement.setAttribute("Name", workElement1.getAttribute("Name"));
			childElement.setAttribute("Remarks", workElement1.getAttribute("Descriptions"));
			childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));

			attrMap = convertDataTypeIDToAttrMap(workElement1.getAttribute("DataTypeID"));
			childElement.setAttribute("Type", attrMap.get("Type"));
			childElement.setAttribute("Size", attrMap.get("Size"));
			childElement.setAttribute("Decimal", attrMap.get("Decimal"));

			if (workElement1.getAttribute("NotNull").equals("true")) {
				childElement.setAttribute("Nullable", "F");
			} else {
				childElement.setAttribute("Nullable", "T");
			}

			if (workElement1.getAttribute("NoUpdate").equals("true")) {
				childElement.setAttribute("NoUpdate", "T");
			} else {
				childElement.setAttribute("NoUpdate", "F");
			}

			if (workElement1.getAttribute("AttributeType").equals("DERIVABLE")) {
				childElement.setAttribute("TypeOptions", "VIRTUAL");
			} else {
				childElement.setAttribute("TypeOptions", "");
			}

			newElementToBeAdded.appendChild(childElement);
		}

		//////////////////////////
		// Create PK Definition //
		//////////////////////////
		childElement = frame_.getDomDocument().createElement("Key");
		childElement.setAttribute("Type", "PK");
		StringBuffer bf = new StringBuffer();
		nodeList = modelElement.getElementsByTagName("TableKey");
		for (int i = 0; i < nodeList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)nodeList.item(i);
			if (workElement1.getAttribute("Type").equals("PK")) {
				NodeList nodeList2 = workElement1.getElementsByTagName("TableKeyField");
				sortingList = frame_.getSortedListModel(nodeList2, "SortKey");
				for (int j = 0; j < sortingList.getSize(); j++) {
					workElement2 = (org.w3c.dom.Element)sortingList.getElementAt(j);
					for (int k = 0; k < fieldSortingList.getSize(); k++) {
						workElement3 = (org.w3c.dom.Element)fieldSortingList.getElementAt(k);
						if (workElement3.getAttribute("ID").equals(workElement2.getAttribute("FieldID"))) {
							if (j > 0) {
								bf.append(";");
							}
							bf.append(workElement3.getAttribute("Alias"));
						}
					}
				}
				break;
			}
		}
		childElement.setAttribute("Fields", bf.toString());
		newElementToBeAdded.appendChild(childElement);

		/////////////////////////////
		// Create SK/XK Definition //
		/////////////////////////////
		nodeList = modelElement.getElementsByTagName("TableKey");
		for (int i = 0; i < nodeList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)nodeList.item(i);
			if (workElement1.getAttribute("Type").equals("SK") || workElement1.getAttribute("Type").equals("XK")) {
				childElement = frame_.getDomDocument().createElement("Key");
				childElement.setAttribute("Type", workElement1.getAttribute("Type"));
				childElement.setAttribute("Fields", getFieldIDListOfTableKeyFrom(modelElement, workElement1));
				newElementToBeAdded.appendChild(childElement);
			}
		}
	}
	
	private void importToUpdateTable(org.w3c.dom.Element modelTableElement, org.w3c.dom.Element currentTableElement) {
		SortableDomElementListModel fieldSortingList;
		org.w3c.dom.Element workElement1, workElement2, childElement;
		NodeList nodeList;
		boolean isToCreateField;

		/////////////////////////////
		// Update Table Definition //
		/////////////////////////////
		currentTableElement.setAttribute("Name", modelTableElement.getAttribute("Name"));
		currentTableElement.setAttribute("Remarks", modelTableElement.getAttribute("Descriptions"));
		currentTableElement.setAttribute("SubsystemID", subsystemElementFrom.getAttribute("SortKey"));

		////////////////////////////////////
		// Update/Create Field Definition //
		////////////////////////////////////
		NodeList currentFieldList = currentTableElement.getElementsByTagName("Field");
		NodeList currentKeyList = currentTableElement.getElementsByTagName("Key");
		HashMap<String, String> attrMap;
		nodeList = modelTableElement.getElementsByTagName("TableField");
		fieldSortingList = frame_.getSortedListModel(nodeList, "SortKey");
		for (int i = 0; i < fieldSortingList.getSize(); i++) {
			workElement1 = (org.w3c.dom.Element)fieldSortingList.getElementAt(i);

			isToCreateField = false;
			childElement = null;
			for (int j = 0; j < currentFieldList.getLength(); j++) {
				workElement2 = (org.w3c.dom.Element)currentFieldList.item(j);
				if (workElement1.getAttribute("Alias").equals("")) {
					if (workElement2.getAttribute("ID").equals(workElement1.getAttribute("Name"))) {
						childElement = workElement2;
						break;
					}
				} else {
					if (workElement2.getAttribute("ID").equals(workElement1.getAttribute("Alias"))) {
						childElement = workElement2;
						break;
					}
				}
			}
			if (childElement == null) {
				isToCreateField = true;
				childElement = frame_.getDomDocument().createElement("Field");
				currentTableElement.appendChild(childElement);
			}

			childElement.setAttribute("Name", workElement1.getAttribute("Name"));
			childElement.setAttribute("Remarks", workElement1.getAttribute("Descriptions"));
			childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
			attrMap = convertDataTypeIDToAttrMap(workElement1.getAttribute("DataTypeID"));
			childElement.setAttribute("Type", attrMap.get("Type"));
			childElement.setAttribute("Size", attrMap.get("Size"));
			childElement.setAttribute("Decimal", attrMap.get("Decimal"));
			if (workElement1.getAttribute("NotNull").equals("true")) {
				childElement.setAttribute("Nullable", "F");
			} else {
				childElement.setAttribute("Nullable", "T");
			}
			if (workElement1.getAttribute("NoUpdate").equals("true")) {
				childElement.setAttribute("NoUpdate", "T");
			} else {
				childElement.setAttribute("NoUpdate", "F");
			}

			if (isToCreateField) {
				if (workElement1.getAttribute("Alias").equals("")) {
					childElement.setAttribute("ID", workElement1.getAttribute("Name"));
				} else {
					childElement.setAttribute("ID", workElement1.getAttribute("Alias"));
				}
				if (workElement1.getAttribute("AttributeType").equals("DERIVABLE")) {
					childElement.setAttribute("TypeOptions", "VIRTUAL");
				} else {
					childElement.setAttribute("TypeOptions", "");
				}
			}
		}

		/////////////////////////////
		// Create SK/XK Definition //
		/////////////////////////////
		nodeList = modelTableElement.getElementsByTagName("TableKey");
		for (int i = 0; i < nodeList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)nodeList.item(i);
			if (workElement1.getAttribute("Type").equals("SK") || workElement1.getAttribute("Type").equals("XK")) {
				childElement = null;
				for (int j = 0; j < currentKeyList.getLength(); j++) {
					workElement2 = (org.w3c.dom.Element)currentKeyList.item(j);
					if (isIdenticalKeyDefinition(modelTableElement, workElement1, workElement2)) {
						childElement = workElement2;
						break;
					}
				}
				if (childElement == null) {
					childElement = frame_.getDomDocument().createElement("Key");
					childElement.setAttribute("Type", workElement1.getAttribute("Type"));
					childElement.setAttribute("Fields", getFieldIDListOfTableKeyFrom(modelTableElement, workElement1));
					currentTableElement.appendChild(childElement);
				}
			}
		}
	}

	private boolean isIdenticalKeyDefinition(org.w3c.dom.Element modelTableElement, org.w3c.dom.Element modelKeyElement, org.w3c.dom.Element currentKeyElement) {
		boolean isIdentical = false;
		String keyFieldIDFrom = getFieldIDListOfTableKeyFrom(modelTableElement, modelKeyElement);
		if (modelKeyElement.getAttribute("Type").equals("SK")) {
			if (currentKeyElement.getAttribute("Fields").equals(keyFieldIDFrom)) {
				isIdentical = true;
			}
		}
		if (modelKeyElement.getAttribute("Type").equals("XK")) {
			String wrkStr, keyFieldIDCurrent = "";
			StringTokenizer tokenizer = new StringTokenizer(currentKeyElement.getAttribute("Fields"), ";");
			while (tokenizer.hasMoreTokens()) {
				wrkStr = tokenizer.nextToken();
				if (!wrkStr.endsWith("(A)") && !wrkStr.endsWith("(D)")) {
					wrkStr = wrkStr + "(A)";
				}
				if (!keyFieldIDCurrent.equals("")) {
					keyFieldIDCurrent = keyFieldIDCurrent + ";";
				}
				keyFieldIDCurrent = keyFieldIDCurrent + wrkStr;
			}
			if (keyFieldIDCurrent.equals(keyFieldIDFrom)) {
				isIdentical = true;
			}
		}
		return isIdentical;
	}

	private void importToCreateFunction(org.w3c.dom.Element modelElement, String functionID) {
		String primaryTableID = "";
		String headerTableID = "";
		ArrayList<String> detailTableIDList = new ArrayList<String>();
		ArrayList<String> detailTableKeysList = new ArrayList<String>();
		ArrayList<String> headerTableKeysList = new ArrayList<String>();
		String detailKeyFields = "";
		org.w3c.dom.Element workElement, childElement, grandChildElement;
		org.w3c.dom.Element primaryTableElement = null;
		org.w3c.dom.Element headerTableElement = null;
		ArrayList<org.w3c.dom.Element> detailTableElementList = new ArrayList<org.w3c.dom.Element>();
		SortableDomElementListModel sortingList;
		NodeList nodeList;
		String dataType;

		String functionType = "";
		for (int i = 0; i < functionTypeList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)functionTypeList.item(i);
			if (workElement.getAttribute("ID").equals(modelElement.getAttribute("FunctionTypeID"))) {
				functionType = workElement.getAttribute("SortKey").trim();
				break;
			}
		}

		nodeList = frame_.getDomDocument().getElementsByTagName("System");
		org.w3c.dom.Element systemElementImportInto = (org.w3c.dom.Element)nodeList.item(0);
		org.w3c.dom.Element newElementToBeAdded = frame_.getDomDocument().createElement("Function");
		systemElementImportInto.appendChild(newElementToBeAdded);
		newElementToBeAdded.setAttribute("ID", functionID);
		newElementToBeAdded.setAttribute("Name", modelElement.getAttribute("Name"));
		newElementToBeAdded.setAttribute("SubsystemID", subsystemElementFrom.getAttribute("SortKey"));
		newElementToBeAdded.setAttribute("Type", functionType);

		if (functionType.equals("XF100")
				|| functionType.equals("XF110")
				|| functionType.equals("XF200")
				|| functionType.equals("XF290")) {
			nodeList = modelElement.getElementsByTagName("IOTable");
			if (nodeList.getLength() == 1) {
				workElement = (org.w3c.dom.Element)nodeList.item(0);
				primaryTableID = convertTableInternalIDToExternalID(workElement.getAttribute("TableID"));
			} else {
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("PRIMARY")
							|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("PrimaryTable").toUpperCase())) {
						primaryTableID = convertTableInternalIDToExternalID(workElement.getAttribute("TableID"));
						break;
					}
				}
			}
			primaryTableElement = getCurrentTableElementWithID(primaryTableID);
		}

		if (functionType.equals("XF300")
				|| functionType.equals("XF310")
				|| functionType.equals("XF390")) {
			String headerTableInternalID = "";
			String detailTableInternalID = "";
			nodeList = modelElement.getElementsByTagName("IOTable");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("HEADER")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("HeaderTable").toUpperCase())) {
					headerTableInternalID = workElement.getAttribute("TableID");
					headerTableID = convertTableInternalIDToExternalID(headerTableInternalID);
					headerTableElement = getCurrentTableElementWithID(headerTableID);
					break;
				}
			}
			String tableID;
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("DETAIL")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("DetailTable").toUpperCase())) {
					detailTableInternalID = workElement.getAttribute("TableID");
					tableID = convertTableInternalIDToExternalID(detailTableInternalID);
					if (isExistingTable(tableID) && isValidWithKeys(headerTableInternalID, detailTableInternalID)) {
						detailTableIDList.add(tableID);
						String[] keys = getDetailKeys(headerTableInternalID, detailTableInternalID);
						headerTableKeysList.add(keys[0]);
						detailTableKeysList.add(keys[1]);
						detailTableElementList.add(getCurrentTableElementWithID(tableID));
					}
				}
			}
		}

		if (functionType.equals("XF000")) {
			newElementToBeAdded.setAttribute("Script", "");
		}

		if (functionType.equals("XF100")) {
			newElementToBeAdded.setAttribute("PrimaryTable", primaryTableID);
			newElementToBeAdded.setAttribute("KeyFields", "");
			newElementToBeAdded.setAttribute("OrderBy", "");
			newElementToBeAdded.setAttribute("Size", "AUTO");
			newElementToBeAdded.setAttribute("InitialMsg", "");
			newElementToBeAdded.setAttribute("InitialListing", "T");

			nodeList = modelElement.getElementsByTagName("FunctionUsedByThis");
			if (nodeList.getLength() == 1) {
				workElement = (org.w3c.dom.Element)nodeList.item(0);
				newElementToBeAdded.setAttribute("DetailFunction", convertFunctionInternalIDToExternalID(workElement.getAttribute("FunctionID")));
			} else {
				newElementToBeAdded.setAttribute("DetailFunction", "NONE");
			}

			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Column");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 3) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Filter");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
					dataType = workElement.getAttribute("Type");
					if (frame_.getBasicTypeOf(dataType).equals("INTEGER")
							|| frame_.getBasicTypeOf(dataType).equals("FLOAT")) {
						childElement.setAttribute("FieldOptions", "IGNORE_IF_ZERO");	
					} else {
						childElement.setAttribute("FieldOptions", "");
					}
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}

			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "0");
			childElement.setAttribute("Number", "3");
			childElement.setAttribute("Caption", res.getString("Close"));
			childElement.setAttribute("Action", "EXIT");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "3");
			childElement.setAttribute("Number", "6");
			childElement.setAttribute("Caption", res.getString("Add"));
			childElement.setAttribute("Action", "ADD");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "6");
			childElement.setAttribute("Number", "12");
			childElement.setAttribute("Caption", res.getString("Output"));
			childElement.setAttribute("Action", "OUTPUT");
			newElementToBeAdded.appendChild(childElement);
		}

		if (functionType.equals("XF110")) {
			newElementToBeAdded.setAttribute("PrimaryTable", primaryTableID);
			newElementToBeAdded.setAttribute("KeyFields", "");
			newElementToBeAdded.setAttribute("OrderBy", "");
			newElementToBeAdded.setAttribute("BatchTable", "");
			newElementToBeAdded.setAttribute("BatchKeyFields", "");
			newElementToBeAdded.setAttribute("BatchWithKeyFields", "");
			newElementToBeAdded.setAttribute("Size", "AUTO");
			newElementToBeAdded.setAttribute("InitialMsg", "");
			newElementToBeAdded.setAttribute("InitialListing", "T");

			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Column");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 3) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Filter");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
					dataType = workElement.getAttribute("Type");
					if (frame_.getBasicTypeOf(dataType).equals("INTEGER")
							|| frame_.getBasicTypeOf(dataType).equals("FLOAT")) {
						childElement.setAttribute("FieldOptions", "IGNORE_IF_ZERO");	
					} else {
						childElement.setAttribute("FieldOptions", "");
					}
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}

			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "0");
			childElement.setAttribute("Number", "3");
			childElement.setAttribute("Caption", res.getString("Close"));
			childElement.setAttribute("Action", "EXIT");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "2");
			childElement.setAttribute("Number", "4");
			childElement.setAttribute("Caption", res.getString("Previous"));
			childElement.setAttribute("Action", "PREV");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "4");
			childElement.setAttribute("Number", "5");
			childElement.setAttribute("Caption", res.getString("Next"));
			childElement.setAttribute("Action", "NEXT");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "4");
			childElement.setAttribute("Number", "5");
			childElement.setAttribute("Caption", res.getString("Update"));
			childElement.setAttribute("Action", "UPDATE");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "6");
			childElement.setAttribute("Number", "12");
			childElement.setAttribute("Caption", res.getString("Output"));
			childElement.setAttribute("Action", "OUTPUT");
			newElementToBeAdded.appendChild(childElement);
		}

		if (functionType.equals("XF200")) {
			newElementToBeAdded.setAttribute("PrimaryTable", primaryTableID);
			newElementToBeAdded.setAttribute("KeyFields", "");
			newElementToBeAdded.setAttribute("Size", "AUTO");
			newElementToBeAdded.setAttribute("InitialMsg", "");

			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
				childElement = frame_.getDomDocument().createElement("Field");
				childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
				childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
				childElement.setAttribute("FieldOptions", "");
				newElementToBeAdded.appendChild(childElement);
			}

			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "0");
			childElement.setAttribute("Number", "3");
			childElement.setAttribute("Caption", res.getString("Close"));
			childElement.setAttribute("Action", "EXIT");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "2");
			childElement.setAttribute("Number", "5");
			childElement.setAttribute("Caption", res.getString("Edit"));
			childElement.setAttribute("Action", "EDIT");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "3");
			childElement.setAttribute("Number", "7");
			childElement.setAttribute("Caption", res.getString("Copy"));
			childElement.setAttribute("Action", "COPY");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "4");
			childElement.setAttribute("Number", "9");
			childElement.setAttribute("Caption", res.getString("Delete"));
			childElement.setAttribute("Action", "DELETE");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "6");
			childElement.setAttribute("Number", "12");
			childElement.setAttribute("Caption", res.getString("Output"));
			childElement.setAttribute("Action", "OUTPUT");
			newElementToBeAdded.appendChild(childElement);
		}

		if (functionType.equals("XF290")) {
			newElementToBeAdded.setAttribute("PrimaryTable", primaryTableID);
			newElementToBeAdded.setAttribute("KeyFields", "");
			newElementToBeAdded.setAttribute("PageSize", "A4");
			newElementToBeAdded.setAttribute("Direction", "NORMAL");
			newElementToBeAdded.setAttribute("Margins", "50;50;50;50");
			newElementToBeAdded.setAttribute("WithPageNumber", "F");

			///////////////////////
			//Get Default Font ID//
			///////////////////////
			org.w3c.dom.Element fontElement;
			String defaultFontID = "";
			NodeList fontList = frame_.getDomDocument().getElementsByTagName("PrintFont");
			sortingList = frame_.getSortedListModel(fontList, "FontName");
			fontElement = (org.w3c.dom.Element)sortingList.getElementAt(0);
			defaultFontID = fontElement.getAttribute("ID");

			childElement = frame_.getDomDocument().createElement("Phrase");
			childElement.setAttribute("Order", "0000");
			childElement.setAttribute("Block", "HEADER");
			childElement.setAttribute("Value", "&Text(" + modelElement.getAttribute("Name").replace("‚Ìˆóü", "").replace("‚Ì”­s", "") + ")");
			childElement.setAttribute("Alignment", "CENTER");
			childElement.setAttribute("FontID", defaultFontID);
			childElement.setAttribute("FontSize", "15");
			childElement.setAttribute("FontStyle", "BOLD");
			newElementToBeAdded.appendChild(childElement);

			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Phrase");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString((i+1) * 10));
					childElement.setAttribute("Block", "PARAGRAPH");
					childElement.setAttribute("Value", "&DataSource(" + primaryTableID + "." + workElement.getAttribute("ID") + ")"); 
					childElement.setAttribute("Alignment", "LEFT");
					childElement.setAttribute("AlignmentMargin", "0");
					childElement.setAttribute("FontID", defaultFontID);
					childElement.setAttribute("FontSize", "12");
					childElement.setAttribute("FontStyle", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}
		}

		if (functionType.equals("XF300")) {
			String headerKeyFields = "";
			ArrayList<String> headerPKFieldIDList = new ArrayList<String>();
			nodeList = headerTableElement.getElementsByTagName("Key");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("Type").equals("PK")) {
					headerKeyFields = workElement.getAttribute("Fields");
					StringTokenizer tokenizer = new StringTokenizer(headerKeyFields, ";");
					while (tokenizer.hasMoreTokens()) {
						headerPKFieldIDList.add(tokenizer.nextToken());
					}
					break;
				}
			}

			newElementToBeAdded.setAttribute("HeaderTable", headerTableID);
			newElementToBeAdded.setAttribute("HeaderKeyFields", "");
			newElementToBeAdded.setAttribute("HeaderFunction", "");
			newElementToBeAdded.setAttribute("Size", "AUTO");

			nodeList = headerTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 7) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Field");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", headerTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}

			for (int p = 0; p < detailTableElementList.size(); p++) {
				if (detailTableKeysList.get(p).equals("")) {
					nodeList = detailTableElementList.get(p).getElementsByTagName("Key");
					for (int i = 0; i < nodeList.getLength(); i++) {
						workElement = (org.w3c.dom.Element)nodeList.item(i);
						if (workElement.getAttribute("Type").equals("PK")) {
							detailKeyFields = workElement.getAttribute("Fields");
							break;
						}
					}
				} else {
					detailKeyFields = detailTableKeysList.get(p);
				}

				childElement = frame_.getDomDocument().createElement("Detail");
				childElement.setAttribute("Order", Editor.getFormatted4ByteString((p+1)*10));
				childElement.setAttribute("Table", detailTableIDList.get(p));
				childElement.setAttribute("HeaderKeyFields", headerTableKeysList.get(p));
				childElement.setAttribute("KeyFields", detailKeyFields);
				childElement.setAttribute("DetailFunction", "NONE");
				childElement.setAttribute("Caption", detailTableElementList.get(p).getAttribute("Name"));
				childElement.setAttribute("InitialMsg", "");
				childElement.setAttribute("InitialListing", "T");
				newElementToBeAdded.appendChild(childElement);

				int columnCount = 0;
				nodeList = detailTableElementList.get(p).getElementsByTagName("Field");
				sortingList = frame_.getSortedListModel(nodeList, "Order");
				for (int i = 0; i < sortingList.getSize(); i++) {
					if (columnCount < 5) {
						workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
						if (!headerPKFieldIDList.contains(workElement.getAttribute("ID"))) {
							grandChildElement = frame_.getDomDocument().createElement("Column");
							grandChildElement.setAttribute("Order", Editor.getFormatted4ByteString(columnCount * 10));
							grandChildElement.setAttribute("DataSource", detailTableIDList.get(p) + "." + workElement.getAttribute("ID")); 
							grandChildElement.setAttribute("FieldOptions", "");
							childElement.appendChild(grandChildElement);
							columnCount++;
						}
					} else {
						break;
					}
				}

				grandChildElement = frame_.getDomDocument().createElement("Button");
				grandChildElement.setAttribute("Position", "0");
				grandChildElement.setAttribute("Number", "3");
				grandChildElement.setAttribute("Caption", res.getString("Close"));
				grandChildElement.setAttribute("Action", "EXIT");
				childElement.appendChild(grandChildElement);
				grandChildElement = frame_.getDomDocument().createElement("Button");
				grandChildElement.setAttribute("Position", "2");
				grandChildElement.setAttribute("Number", "6");
				grandChildElement.setAttribute("Caption", res.getString("Add"));
				grandChildElement.setAttribute("Action", "ADD");
				childElement.appendChild(grandChildElement);
				grandChildElement = frame_.getDomDocument().createElement("Button");
				grandChildElement.setAttribute("Position", "4");
				grandChildElement.setAttribute("Number", "8");
				grandChildElement.setAttribute("Caption", res.getString("HDRData"));
				grandChildElement.setAttribute("Action", "HEADER");
				childElement.appendChild(grandChildElement);
				grandChildElement = frame_.getDomDocument().createElement("Button");
				grandChildElement.setAttribute("Position", "6");
				grandChildElement.setAttribute("Number", "12");
				grandChildElement.setAttribute("Caption", res.getString("Output"));
				grandChildElement.setAttribute("Action", "OUTPUT");
				childElement.appendChild(grandChildElement);
			}
		}

		if (functionType.equals("XF310")) {
			String headerKeyFields = "";
			ArrayList<String> headerPKFieldIDList = new ArrayList<String>();
			nodeList = headerTableElement.getElementsByTagName("Key");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("Type").equals("PK")) {
					headerKeyFields = workElement.getAttribute("Fields");
					StringTokenizer tokenizer = new StringTokenizer(headerKeyFields, ";");
					while (tokenizer.hasMoreTokens()) {
						headerPKFieldIDList.add(tokenizer.nextToken());
					}
					break;
				}
			}
			nodeList = detailTableElementList.get(0).getElementsByTagName("Key");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("Type").equals("PK")) {
					detailKeyFields = workElement.getAttribute("Fields");
					break;
				}
			}

			newElementToBeAdded.setAttribute("HeaderTable", headerTableID);
			newElementToBeAdded.setAttribute("HeaderKeyFields", headerKeyFields);
			newElementToBeAdded.setAttribute("DetailTable", detailTableIDList.get(0));
			newElementToBeAdded.setAttribute("DetailKeyFields", detailKeyFields);
			newElementToBeAdded.setAttribute("Size", "AUTO");

			nodeList = headerTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 7) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Field");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", headerTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}

			int columnCount = 0;
			nodeList = detailTableElementList.get(0).getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (columnCount < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (!headerPKFieldIDList.contains(workElement.getAttribute("ID"))) {
						childElement = frame_.getDomDocument().createElement("Column");
						childElement.setAttribute("Order", Editor.getFormatted4ByteString(columnCount * 10));
						childElement.setAttribute("DataSource", detailTableIDList.get(0) + "." + workElement.getAttribute("ID")); 
						childElement.setAttribute("FieldOptions", "");
						newElementToBeAdded.appendChild(childElement);
						columnCount++;
					}
				} else {
					break;
				}
			}

			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "0");
			childElement.setAttribute("Number", "3");
			childElement.setAttribute("Caption", res.getString("Close"));
			childElement.setAttribute("Action", "EXIT");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "2");
			childElement.setAttribute("Number", "5");
			childElement.setAttribute("Caption", res.getString("Update"));
			childElement.setAttribute("Action", "UPDATE");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "4");
			childElement.setAttribute("Number", "6");
			childElement.setAttribute("Caption", res.getString("AddRow"));
			childElement.setAttribute("Action", "ADD_ROW");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "5");
			childElement.setAttribute("Number", "9");
			childElement.setAttribute("Caption", res.getString("RemoveRow"));
			childElement.setAttribute("Action", "REMOVE_ROW");
			newElementToBeAdded.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "6");
			childElement.setAttribute("Number", "12");
			childElement.setAttribute("Caption", res.getString("Output"));
			childElement.setAttribute("Action", "OUTPUT");
			newElementToBeAdded.appendChild(childElement);
		}

		if (functionType.equals("XF390")) {
			String headerKeyFields = "";
			ArrayList<String> headerPKFieldIDList = new ArrayList<String>();
			nodeList = headerTableElement.getElementsByTagName("Key");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("Type").equals("PK")) {
					headerKeyFields = workElement.getAttribute("Fields");
					StringTokenizer tokenizer = new StringTokenizer(headerKeyFields, ";");
					while (tokenizer.hasMoreTokens()) {
						headerPKFieldIDList.add(tokenizer.nextToken());
					}
					break;
				}
			}
			nodeList = detailTableElementList.get(0).getElementsByTagName("Key");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("Type").equals("PK")) {
					detailKeyFields = workElement.getAttribute("Fields");
					break;
				}
			}

			///////////////////////
			//Get Default Font ID//
			///////////////////////
			org.w3c.dom.Element fontElement;
			String defaultFontID = "";
			NodeList fontList = frame_.getDomDocument().getElementsByTagName("PrintFont");
			sortingList = frame_.getSortedListModel(fontList, "FontName");
			fontElement = (org.w3c.dom.Element)sortingList.getElementAt(0);
			defaultFontID = fontElement.getAttribute("ID");

			newElementToBeAdded.setAttribute("HeaderTable", headerTableID);
			newElementToBeAdded.setAttribute("HeaderKeyFields", headerKeyFields);
			newElementToBeAdded.setAttribute("PageSize", "A4");
			newElementToBeAdded.setAttribute("Direction", "LANDSCAPE");
			newElementToBeAdded.setAttribute("Margins", "50;50;50;50");
			newElementToBeAdded.setAttribute("WithPageNumber", "T");

			childElement = frame_.getDomDocument().createElement("HeaderPhrase");
			childElement.setAttribute("Order", "0000");
			childElement.setAttribute("Block", "HEADER");
			childElement.setAttribute("Value", "&Text(" + modelElement.getAttribute("Name").replace("‚Ìˆóü", "").replace("‚Ì”­s", "") + ")");
			childElement.setAttribute("Alignment", "CENTER");
			childElement.setAttribute("FontID", defaultFontID);
			childElement.setAttribute("FontSize", "15");
			childElement.setAttribute("FontStyle", "BOLD");
			newElementToBeAdded.appendChild(childElement);

			nodeList = headerTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("HeaderPhrase");
					childElement.setAttribute("Order", Editor.getFormatted4ByteString((i+1) * 10));
					childElement.setAttribute("Block", "PARAGRAPH");
					childElement.setAttribute("Value", "&DataSource(" + headerTableID + "." + workElement.getAttribute("ID") + ")"); 
					childElement.setAttribute("Alignment", "LEFT");
					childElement.setAttribute("AlignmentMargin", "0");
					childElement.setAttribute("FontID", defaultFontID);
					childElement.setAttribute("FontSize", "12");
					childElement.setAttribute("FontStyle", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}

			for (int p = 0; p < detailTableElementList.size(); p++) {
				if (detailTableKeysList.get(p).equals("")) {
					nodeList = detailTableElementList.get(p).getElementsByTagName("Key");
					for (int i = 0; i < nodeList.getLength(); i++) {
						workElement = (org.w3c.dom.Element)nodeList.item(i);
						if (workElement.getAttribute("Type").equals("PK")) {
							detailKeyFields = workElement.getAttribute("Fields");
							break;
						}
					}
				} else {
					detailKeyFields = detailTableKeysList.get(p);
				}

				childElement = frame_.getDomDocument().createElement("Detail");
				childElement.setAttribute("Order", Editor.getFormatted4ByteString((p+1)*10));
				childElement.setAttribute("Table", detailTableIDList.get(p));
				childElement.setAttribute("HeaderKeyFields", headerTableKeysList.get(p));
				childElement.setAttribute("KeyFields", detailKeyFields);
				if (detailTableElementList.size() > 1) {
					childElement.setAttribute("Caption", detailTableElementList.get(p).getAttribute("Name"));
				} else {
					childElement.setAttribute("Caption", "");
				}
				childElement.setAttribute("CaptionFontSize", "12");
				childElement.setAttribute("CaptionFontStyle", "");
				childElement.setAttribute("FontID", defaultFontID);
				childElement.setAttribute("FontSize", "10");
				childElement.setAttribute("RowNoWidth", "5");
				newElementToBeAdded.appendChild(childElement);

				int columnCount = 0;
				nodeList = detailTableElementList.get(p).getElementsByTagName("Field");
				sortingList = frame_.getSortedListModel(nodeList, "Order");
				for (int i = 0; i < sortingList.getSize(); i++) {
					if (columnCount < 5) {
						workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
						if (!headerPKFieldIDList.contains(workElement.getAttribute("ID"))) {
							grandChildElement = frame_.getDomDocument().createElement("Column");
							grandChildElement.setAttribute("Order", Editor.getFormatted4ByteString(columnCount * 10));
							grandChildElement.setAttribute("DataSource", detailTableIDList.get(p) + "." + workElement.getAttribute("ID")); 
							grandChildElement.setAttribute("FieldOptions", "");
							grandChildElement.setAttribute("Width", "19");
							grandChildElement.setAttribute("Alignment", "LEFT");
							childElement.appendChild(grandChildElement);
							columnCount++;
						}
					} else {
						break;
					}
				}
			}
		}
	}

	private void importToUpdateFunction(org.w3c.dom.Element modelFunctionElement, org.w3c.dom.Element currentFunctionElement) {
		org.w3c.dom.Element workElement, workElement2;

		////////////////////////////////
		// Update Function Definition //
		////////////////////////////////
		currentFunctionElement.setAttribute("Name", modelFunctionElement.getAttribute("Name"));
		currentFunctionElement.setAttribute("SubsystemID", subsystemElementFrom.getAttribute("SortKey"));

		///////////////////////////
		// Getting Function Type //
		///////////////////////////
		String functionType = "";
		for (int i = 0; i < functionTypeList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)functionTypeList.item(i);
			if (workElement.getAttribute("ID").equals(modelFunctionElement.getAttribute("FunctionTypeID"))) {
				functionType = workElement.getAttribute("SortKey").trim();
				break;
			}
		}

		///////////////////////////////////////
		// Adding Detail Tab of XF300, XF390 //
		///////////////////////////////////////
		if (functionType.equals("XF300") || functionType.equals("XF390")) {
			String headerTableID = "";
			org.w3c.dom.Element headerTableElement = null;
			org.w3c.dom.Element childElement = null;
			org.w3c.dom.Element grandChildElement = null;
			String headerTableInternalID = "";
			String detailTableInternalID = "";
			String detailTableExternalID = "";
			String headerKeyFields = "";
			String detailKeyFields = "";
			boolean isFound;
			SortableDomElementListModel sortingList;
			NodeList detailTabList, nodeList, nodeList2;
			ArrayList<org.w3c.dom.Element> detailTableElementList = new ArrayList<org.w3c.dom.Element>();

			///////////////////////////////////////////////
			// Extracting New and Valid Detail Table IOs //
			///////////////////////////////////////////////
			nodeList = modelFunctionElement.getElementsByTagName("IOTable");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("HEADER")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("HeaderTable").toUpperCase())) {
					headerTableInternalID = workElement.getAttribute("TableID");
					headerTableID = convertTableInternalIDToExternalID(headerTableInternalID);
					headerTableElement = getCurrentTableElementWithID(headerTableID);

					nodeList2 = headerTableElement.getElementsByTagName("Key");
					for (int j = 0; j < nodeList2.getLength(); j++) {
						workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
						if (workElement2.getAttribute("Type").equals("PK")) {
							headerKeyFields = workElement2.getAttribute("Fields");
							break;
						}
					}
					break;
				}
			}
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("DETAIL")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("DetailTable").toUpperCase())) {
					detailTableInternalID = workElement.getAttribute("TableID");
					detailTableExternalID = convertTableInternalIDToExternalID(detailTableInternalID);
					if (isExistingTable(detailTableExternalID) && isValidWithKeys(headerTableInternalID, detailTableInternalID)) {
						detailTabList = currentFunctionElement.getElementsByTagName("Detail");
						isFound = false;
						for (int j = 0; j < detailTabList.getLength(); j++) {
							workElement = (org.w3c.dom.Element)detailTabList.item(j);
							if (workElement.getAttribute("Table").equals(detailTableExternalID)) {
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							detailTableElementList.add(getCurrentTableElementWithID(detailTableExternalID));
						}
					}
				}
			}

			///////////////////////
			//Get Default Font ID//
			///////////////////////
			String defaultFontID = "";
			if (functionType.equals("XF390")) {
				NodeList fontList = frame_.getDomDocument().getElementsByTagName("PrintFont");
				sortingList = frame_.getSortedListModel(fontList, "FontName");
				workElement = (org.w3c.dom.Element)sortingList.getElementAt(0);
				defaultFontID = workElement.getAttribute("ID");
			}

			////////////////////////////////////////////////////////////
			// Creating A Detail Tab Element with Columns and Buttons //
			////////////////////////////////////////////////////////////
			for (int p = 0; p < detailTableElementList.size(); p++) {
				nodeList = detailTableElementList.get(p).getElementsByTagName("Key");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (workElement.getAttribute("Type").equals("PK")) {
						detailKeyFields = workElement.getAttribute("Fields");
						break;
					}
				}
				childElement = frame_.getDomDocument().createElement("Detail");
				childElement.setAttribute("Order", Editor.getFormatted4ByteString((p+1)*10));
				childElement.setAttribute("Table", detailTableElementList.get(p).getAttribute("ID"));
				childElement.setAttribute("HeaderKeyFields", headerKeyFields);
				childElement.setAttribute("KeyFields", detailKeyFields);
				childElement.setAttribute("Caption", detailTableElementList.get(p).getAttribute("Name"));
				if (functionType.equals("XF390")) {
					childElement.setAttribute("CaptionFontSize", "12");
					childElement.setAttribute("CaptionFontStyle", "");
					childElement.setAttribute("FontID", defaultFontID);
					childElement.setAttribute("FontSize", "10");
					childElement.setAttribute("RowNoWidth", "5");
				}
				currentFunctionElement.appendChild(childElement);

				//////////////////////////////
				// Creating Column Elements //
				//////////////////////////////
				int columnCount = 0;
				nodeList = detailTableElementList.get(p).getElementsByTagName("Field");
				sortingList = frame_.getSortedListModel(nodeList, "Order");
				for (int i = 0; i < sortingList.getSize(); i++) {
					if (columnCount < 5) {
						workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
						if (!headerKeyFields.contains(workElement.getAttribute("ID"))) {
							grandChildElement = frame_.getDomDocument().createElement("Column");
							grandChildElement.setAttribute("Order", Editor.getFormatted4ByteString(columnCount * 10));
							grandChildElement.setAttribute("DataSource", detailTableElementList.get(p).getAttribute("ID") + "." + workElement.getAttribute("ID")); 
							grandChildElement.setAttribute("FieldOptions", "");
							if (functionType.equals("XF390")) {
								grandChildElement.setAttribute("Width", "19");
								grandChildElement.setAttribute("Alignment", "LEFT");
							}
							childElement.appendChild(grandChildElement);
							columnCount++;
						}
					} else {
						break;
					}
				}

				//////////////////////////////
				// Creating Button Elements //
				//////////////////////////////
				if (functionType.equals("XF300")) {
					grandChildElement = frame_.getDomDocument().createElement("Button");
					grandChildElement.setAttribute("Position", "0");
					grandChildElement.setAttribute("Number", "3");
					grandChildElement.setAttribute("Caption", res.getString("Close"));
					grandChildElement.setAttribute("Action", "EXIT");
					childElement.appendChild(grandChildElement);
					grandChildElement = frame_.getDomDocument().createElement("Button");
					grandChildElement.setAttribute("Position", "2");
					grandChildElement.setAttribute("Number", "6");
					grandChildElement.setAttribute("Caption", res.getString("Add"));
					grandChildElement.setAttribute("Action", "ADD");
					childElement.appendChild(grandChildElement);
					grandChildElement = frame_.getDomDocument().createElement("Button");
					grandChildElement.setAttribute("Position", "4");
					grandChildElement.setAttribute("Number", "8");
					grandChildElement.setAttribute("Caption", res.getString("HDRData"));
					grandChildElement.setAttribute("Action", "HEADER");
					childElement.appendChild(grandChildElement);
					grandChildElement = frame_.getDomDocument().createElement("Button");
					grandChildElement.setAttribute("Position", "6");
					grandChildElement.setAttribute("Number", "12");
					grandChildElement.setAttribute("Caption", res.getString("Output"));
					grandChildElement.setAttribute("Action", "OUTPUT");
					childElement.appendChild(grandChildElement);
				}
			}
		}
	}	

	private org.w3c.dom.Element getCurrentTableElementWithID(String externalTableID) {
		org.w3c.dom.Element element = null;
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("Table");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(externalTableID)) {
				return element;
			}
		}
		return null;
	}

	public void jButtonCloseDialog_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	
	private boolean isExistingTable(String tableID) {
		boolean isExisting = false;
		org.w3c.dom.Element tableElement;
		for (int i = 0; i < currentTableList.getLength(); i++) {
			tableElement = (org.w3c.dom.Element)currentTableList.item(i);
			if (tableElement.getAttribute("ID").equals(tableID)) {
				isExisting = true;
				break;
			}
		}
		if (newlyAddedTableIDList.contains(tableID)) {
			isExisting = true;
		}
		return isExisting;
	}
	
	private boolean isExistingFunction(String functionID) {
		boolean isExisting = false;
		org.w3c.dom.Element functionElement;
		for (int i = 0; i < currentFunctionList.getLength(); i++) {
			functionElement = (org.w3c.dom.Element)currentFunctionList.item(i);
			if (functionElement.getAttribute("ID").equals(functionID)) {
				isExisting = true;
				break;
			}
		}
		if (newlyAddedFunctionIDList.contains(functionID)) {
			isExisting = true;
		}
		return isExisting;
	}

	class TableModelReadOnlyList extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {return false;}
	}

	class TableRowNumber extends Object {
		private org.w3c.dom.Element element_;
		private int number_;
		private String type_;
		private String message_;
		private DialogImportModel dialog_;
		public TableRowNumber(int num, org.w3c.dom.Element elm, String type, DialogImportModel dialog) {
			number_ = num;
			element_ = elm;
			type_ = type;
			dialog_ = dialog;
			message_ = "";
		}
		public String toString() {
			String value = Integer.toString(number_);
			if (isTable() && !dialog_.isExistingTable(element_.getAttribute("SortKey"))) {
				return "<html><b>" + value;
			}
			if (isFunction() && !dialog_.isExistingFunction(element_.getAttribute("SortKey"))) {
				return "<html><b>" + value;
			}
			return value;
		}
		public org.w3c.dom.Element getElement() {
			return element_;
		}
		public boolean isTable() {
			return type_.equals("Table");
		}
		public boolean isFunction() {
			return type_.equals("Function");
		}
		public void setMessage(String message) {
			message_ = message;
		}
		public String getMessage() {
			return message_;
		}
	}
}

class DialogImportModel_jButtonCloseDialog_actionAdapter implements java.awt.event.ActionListener {
	DialogImportModel adaptee;

	DialogImportModel_jButtonCloseDialog_actionAdapter(DialogImportModel adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCloseDialog_actionPerformed(e);
	}
}

class DialogImportModel_jButtonImport_actionAdapter implements java.awt.event.ActionListener {
	DialogImportModel adaptee;

	DialogImportModel_jButtonImport_actionAdapter(DialogImportModel adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonImport_actionPerformed(e);
	}
}

class DialogImportModel_jButtonRefresh_actionAdapter implements java.awt.event.ActionListener {
	DialogImportModel adaptee;

	DialogImportModel_jButtonRefresh_actionAdapter(DialogImportModel adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonRefresh_actionPerformed(e);
	}
}

class DialogImportModel_jComboBoxSubsystemFrom_actionAdapter implements java.awt.event.ActionListener {
	DialogImportModel adaptee;
	DialogImportModel_jComboBoxSubsystemFrom_actionAdapter(DialogImportModel adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxSubsystemFrom_actionPerformed(e);
	}
}

class DialogImportModel_jTableElementListFrom_listSelectionAdapter implements ListSelectionListener {
	DialogImportModel adaptee;
	DialogImportModel_jTableElementListFrom_listSelectionAdapter(DialogImportModel adaptee) {
		this.adaptee = adaptee;
	}
	public void valueChanged(ListSelectionEvent e) {
		adaptee.jTableElementListFrom_valueChanged(e);
	}
}