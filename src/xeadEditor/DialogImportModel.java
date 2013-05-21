package xeadEditor;

/*
 * Copyright (c) 2011 WATANABE kozo <qyf05466@nifty.com>,
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
	private JLabel jLabelSystemNameFrom = new JLabel();
	private JTextField jTextFieldSystemNameFrom = new JTextField();
	private JLabel jLabelSystemVersionFrom = new JLabel();
	private JTextField jTextFieldSystemVersionFrom = new JTextField();
	private ArrayList<org.w3c.dom.Element> subsystemNodeListFrom = null;
	private NodeList dataTypeNodeListFrom = null;
	private TableColumn column0, column1, column2, column3;
	private DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentRight = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	private JButton jButtonCloseDialog = new JButton();
	private JButton jButtonImport = new JButton();
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
		panelMain.setPreferredSize(new Dimension(700, 620));
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(jPanelNorth, BorderLayout.NORTH);
		panelMain.add(jPanelCenter, BorderLayout.CENTER);
		panelMain.add(jPanelSouth, BorderLayout.SOUTH);
		///////////////////////////////////
		// jPanelNorth and objects on it //
		///////////////////////////////////
		jPanelNorth.setBorder(BorderFactory.createEtchedBorder());
		jPanelNorth.setPreferredSize(new Dimension(800, 41));
		jLabelSystemNameFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelSystemNameFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSystemNameFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSystemNameFrom.setText(res.getString("SystemNameImporting"));
		jLabelSystemNameFrom.setBounds(new Rectangle(6, 12, 150, 15));
		jTextFieldSystemNameFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTextFieldSystemNameFrom.setBounds(new Rectangle(163, 9, 300, 21));
		jTextFieldSystemNameFrom.setEditable(false);
		jLabelSystemVersionFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelSystemVersionFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSystemVersionFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSystemVersionFrom.setText(res.getString("SystemVersion"));
		jLabelSystemVersionFrom.setBounds(new Rectangle(468, 12, 96, 15));
		jTextFieldSystemVersionFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTextFieldSystemVersionFrom.setBounds(new Rectangle(571, 9, 105, 22));
		jTextFieldSystemVersionFrom.setEditable(false);
		jPanelNorth.setLayout(null);
		jPanelNorth.add(jLabelSystemNameFrom);
		jPanelNorth.add(jTextFieldSystemNameFrom);
		jPanelNorth.add(jLabelSystemVersionFrom);
		jPanelNorth.add(jTextFieldSystemVersionFrom);
		//////////////////////////////////////
		//jPanelCenterTop and objects on it //
		//////////////////////////////////////
		jLabelSubsystemFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelSubsystemFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSubsystemFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSubsystemFrom.setText(res.getString("SubsystemImportingFrom"));
		jLabelSubsystemFrom.setBounds(new Rectangle(6, 12, 150, 15));
		jComboBoxSubsystemFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jComboBoxSubsystemFrom.setBounds(new Rectangle(163, 9, 300, 22));
		jComboBoxSubsystemFrom.addActionListener(new DialogImportModel_jComboBoxSubsystemFrom_actionAdapter(this));
		jCheckBoxOverrideID.setFont(new java.awt.Font("SansSerif", 0, 12));
		jCheckBoxOverrideID.setBounds(new Rectangle(500, 9, 150, 22));
		jCheckBoxOverrideID.setText(res.getString("OverrideID"));
		jPanelCenterTop.setPreferredSize(new Dimension(450, 40));
		jPanelCenterTop.setBorder(BorderFactory.createEtchedBorder());
		jPanelCenterTop.setLayout(null);
		jPanelCenterTop.add(jLabelSubsystemFrom);
		jPanelCenterTop.add(jComboBoxSubsystemFrom);
		jPanelCenterTop.add(jCheckBoxOverrideID);
		//
		jScrollPaneElementListFrom.setPreferredSize(new Dimension(450, 200));
		jScrollPaneElementListFrom.getViewport().add(jTableElementListFrom, null);
		jTextAreaMessage.setFont(new java.awt.Font("SansSerif", 0, 14));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setWrapStyleWord(true);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setBackground(SystemColor.control);
		jScrollPaneMessage.setPreferredSize(new Dimension(800, 70));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage, null);
		jPanelCenter.setLayout(new BorderLayout());
		jPanelCenter.add(jPanelCenterTop, BorderLayout.NORTH);
		jPanelCenter.add(jScrollPaneElementListFrom, BorderLayout.CENTER);
		jPanelCenter.add(jScrollPaneMessage, BorderLayout.SOUTH);
		//
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentRight.setHorizontalAlignment(4); //RIGHT//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		//
		jTableElementListFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTableElementListFrom.setBackground(SystemColor.control);
		jTableElementListFrom.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableElementListFrom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableElementListFrom.getSelectionModel().addListSelectionListener(new DialogImportModel_jTableElementListFrom_listSelectionAdapter(this));
		jTableElementListFrom.setRowSelectionAllowed(true);
		tableModelElementListFrom.addColumn("NO.");
		tableModelElementListFrom.addColumn("ID");
		tableModelElementListFrom.addColumn(res.getString("ElementName"));
		tableModelElementListFrom.addColumn(res.getString("ElementType"));
		column0 = jTableElementListFrom.getColumnModel().getColumn(0);
		column1 = jTableElementListFrom.getColumnModel().getColumn(1);
		column2 = jTableElementListFrom.getColumnModel().getColumn(2);
		column3 = jTableElementListFrom.getColumnModel().getColumn(3);
		column0.setPreferredWidth(34);
		column1.setPreferredWidth(100);
		column2.setPreferredWidth(330);
		column3.setPreferredWidth(200);
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		jTableElementListFrom.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, 12));
		///////////////////////////////////
		// jPanelSouth and objects on it //
		///////////////////////////////////
		jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
		jPanelSouth.setPreferredSize(new Dimension(800, 40));
		jButtonCloseDialog.setBounds(new Rectangle(20, 7, 70, 25));
		jButtonCloseDialog.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogImportModel_jButtonCloseDialog_actionAdapter(this));
		jButtonImport.setBounds(new Rectangle(400, 7, 120, 25));
		jButtonImport.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonImport.setText(res.getString("Import"));
		jButtonImport.addActionListener(new DialogImportModel_jButtonImport_actionAdapter(this));
		jButtonImport.setEnabled(false);
		jButtonImport.setFocusCycleRoot(true);
		jPanelSouth.setLayout(null);
		jPanelSouth.add(jButtonCloseDialog);
		jPanelSouth.add(jButtonImport);
		///////////////////////
		// DialogImportModel //
		///////////////////////
		this.setResizable(false);
		this.getContentPane().add(panelMain);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public boolean request(String fileName) {
		try {
			this.setTitle(res.getString("ImportDialogModelTitle1") + fileName + res.getString("ImportDialogModelTitle2"));
			isListBeingRebuild = false;
			newlyAddedTableIDList.clear();
			newlyAddedFunctionIDList.clear();
			jTextAreaMessage.setText(res.getString("ImportModelMessage0"));
			jButtonImport.setEnabled(false);
			subsystemElementFrom = null;
			//
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
			//
			jComboBoxSubsystemFrom.removeAllItems();
			jComboBoxSubsystemFrom.addItem(res.getString("SelectFromList"));
			dataTypeNodeListFrom = domDocumentImportingFrom.getElementsByTagName("DataType");
			subsystemNodeListFrom = new ArrayList<org.w3c.dom.Element>();
			subsystemNodeListFrom.add(null);
			//
			org.w3c.dom.Element element;
			NodeList nodelist = domDocumentImportingFrom.getElementsByTagName("Subsystem");
			SortableDomElementListModel sortingList = frame_.getSortedListModel(nodelist, "SortKey");
			for (int i = 0; i < sortingList.getSize(); i++) {
				element = (org.w3c.dom.Element)sortingList.get(i);
				jComboBoxSubsystemFrom.addItem(element.getAttribute("SortKey") + " " + element.getAttribute("Name"));
				subsystemNodeListFrom.add(element);
			}
			//
			super.setVisible(true);
			//
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, res.getString("FailedToParse") + "\n\n" + e.getMessage());
			e.printStackTrace();
		}
		//
		return (newlyAddedTableIDList.size() > 0 || newlyAddedFunctionIDList.size() > 0);
	}

	public void jComboBoxSubsystemFrom_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element element, functionTypeElement;
		isListBeingRebuild = true;
		subsystemElementFrom = null;
		String subsystemID = "";
		//
		if (subsystemNodeListFrom != null && jComboBoxSubsystemFrom.getSelectedIndex() >= 0) {
			try {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				//
				if (tableModelElementListFrom.getRowCount() > 0) {
					int rowCount = tableModelElementListFrom.getRowCount();
					for (int i = 0; i < rowCount; i++) {tableModelElementListFrom.removeRow(0);}
				}
				//
				subsystemElementFrom = subsystemNodeListFrom.get(jComboBoxSubsystemFrom.getSelectedIndex());
				if (subsystemElementFrom != null) {
					subsystemID = subsystemElementFrom.getAttribute("ID");
					//
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
							if (!isExistingTable(element.getAttribute("SortKey"))) {
								Cell[1] = "<html><b>" + Cell[1];
								Cell[2] = "<html><b>" + Cell[2];
								Cell[3] = "<html><b>" + Cell[3];
							}
							tableModelElementListFrom.addRow(Cell);
						}
					}
					//
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
							if (!isExistingFunction(element.getAttribute("SortKey"))) {
								Cell[1] = "<html><b>" + Cell[1];
								Cell[2] = "<html><b>" + Cell[2];
								Cell[3] = "<html><b>" + Cell[3];
							}
							tableModelElementListFrom.addRow(Cell);
						}
					}
					//
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
			org.w3c.dom.Element element = tableRowNumber.getElement();
			String message = "";
			//
			if (tableRowNumber.isTable()) {
				message = getErrorWithTable(element);
				if (message.equals("")) {
					jTextAreaMessage.setText(res.getString("ImportModelMessage3"));
					jButtonImport.setEnabled(true);
				} else {
					message = "Table " + element.getAttribute("SortKey") + " " + message;
					jTextAreaMessage.setText(message);
				}
			}
			//
			if (tableRowNumber.isFunction()) {
				message = getErrorWithFunction(element);
				if (message.equals("")) {
					jTextAreaMessage.setText(res.getString("ImportModelMessage3"));
					jButtonImport.setEnabled(true);
				} else {
					message = "Function " + element.getAttribute("SortKey") + " " + message;
					jTextAreaMessage.setText(message);
				}
			} 
		}
	}
	
	private String getErrorWithTable(org.w3c.dom.Element element) {
		String error = "";
		org.w3c.dom.Element workElement;
		String dataType, alias;
		//
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			HashMap<String, String> attrMap;
			NodeList nodeList = element.getElementsByTagName("TableField");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				alias = workElement.getAttribute("Alias");
				if (alias.equals("")) {
					error = res.getString("ImportModelMessage14") + " FieldName=" + workElement.getAttribute("Name");
				} else {
					attrMap = convertDataTypeIDToAttrMap(workElement.getAttribute("DataTypeID"));
					dataType = attrMap.get("Type");
					if (!dataType.equals("CHAR")
							&& !dataType.equals("VARCHAR")
							&& !dataType.equals("INTEGER")
							&& !dataType.equals("SMALLINT")
							&& !dataType.equals("BIGINT")
							&& !dataType.equals("DOUBLE")
							&& !dataType.equals("DECIMAL")
							&& !dataType.equals("NUMERIC")
							&& !dataType.equals("INT")
							&& !dataType.equals("REAL")
							&& !dataType.equals("DATE")
							&& !dataType.equals("TIME")
							&& !dataType.equals("TIMESTAMP")) {
						error = res.getString("ImportModelMessage13")
							+ " Field=" + workElement.getAttribute("Name") + "("  + alias + ")"
							+ " DataType=" + dataType;
					}
				}
				if (!error.equals("")) {
					break;
				}
			}
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		//
		return error;
	}
	
	private String getErrorWithFunction(org.w3c.dom.Element element) {
		String error = "";
		String primaryTableExternalID, headerTableExternalID;
		org.w3c.dom.Element workElement;
		NodeList nodeList;
		//
		//
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			String functionType = "";
			for (int i = 0; i < functionTypeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)functionTypeList.item(i);
				if (workElement.getAttribute("ID").equals(element.getAttribute("FunctionTypeID"))) {
					functionType = workElement.getAttribute("SortKey").trim();
					break;
				}
			}
			if (!functionType.equals("XF000") && !functionType.equals("XF100")
					&& !functionType.equals("XF110") && !functionType.equals("XF200")
					&& !functionType.equals("XF290") && !functionType.equals("XF300")
					&& !functionType.equals("XF310") && !functionType.equals("XF390")) {
				return res.getString("ImportModelMessage12") + "(" + functionType + ")";
			}
			//
			if (functionType.equals("XF100") || functionType.equals("XF110")
					|| functionType.equals("XF200") || functionType.equals("XF290")) {
				primaryTableExternalID = "";
				nodeList = element.getElementsByTagName("IOTable");
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
					error = res.getString("ImportModelMessage6");
				} else {
					if (!isExistingTable(primaryTableExternalID)) {
						error = res.getString("ImportModelMessage7") + "(" + primaryTableExternalID + ")";
					}
				}
			}
			//
			if (functionType.equals("XF300") || functionType.equals("XF310") || functionType.equals("XF390")) {
				headerTableExternalID = "";
				ArrayList<String> detailTableExternalIDList = new ArrayList<String>();
				nodeList = element.getElementsByTagName("IOTable");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("HEADER")
							|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("HeaderTable").toUpperCase())) {
						headerTableExternalID = convertTableInternalIDToExternalID(workElement.getAttribute("TableID"));
						break;
					}
				}
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("DETAIL")
							|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("DetailTable").toUpperCase())) {
						detailTableExternalIDList.add(convertTableInternalIDToExternalID(workElement.getAttribute("TableID")));
					}
				}
				if (headerTableExternalID.equals("")) {
					error = res.getString("ImportModelMessage8");
				} else {
					if (!isExistingTable(headerTableExternalID)) {
						error = res.getString("ImportModelMessage9") + "(" + headerTableExternalID + ")";
					}
				}
				if (error.equals("")) {
					if (detailTableExternalIDList.size() == 0) {
						error = res.getString("ImportModelMessage10");
					} else {
						int numberOfValidDetailTables = 0;
						for (int i = 0; i < detailTableExternalIDList.size(); i++) {
							if (isExistingTable(detailTableExternalIDList.get(i))
									&& isValidWithKeys(headerTableExternalID, detailTableExternalIDList.get(i))) {
								numberOfValidDetailTables++;
							}
						}
						if (numberOfValidDetailTables == 0) {
							error = res.getString("ImportModelMessage10");
						}
					}
				}
			}
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		//
		return error;
	}
	
//	private boolean isValidWithKeys(String headerTableID, String detailTableID) {
//		boolean isValid = true;
//		org.w3c.dom.Element element;
//		String fieldID;
//		StringTokenizer tokenizer1, tokenizer2;
//		ArrayList<String> headerPKFieldTypeList = new ArrayList<String>();
//		ArrayList<String> detailPKFieldTypeList = new ArrayList<String>();
//		//
//		org.w3c.dom.Element headerTableElement = getCurrentTableElementWithID(headerTableID);
//		NodeList headerTableKeyNodeList = headerTableElement.getElementsByTagName("Key");
//		NodeList headerTableFieldNodeList = headerTableElement.getElementsByTagName("Field");
//		org.w3c.dom.Element headerTableKeyElement = null;
//		for (int i = 0; i < headerTableKeyNodeList.getLength(); i++) {
//			element = (org.w3c.dom.Element)headerTableKeyNodeList.item(i);
//			if (element.getAttribute("Type").equals("PK")) {
//				headerTableKeyElement = element;
//				break;
//			}
//		}
//		//
//		org.w3c.dom.Element detailTableElement = getCurrentTableElementWithID(detailTableID);
//		NodeList detailTableKeyNodeList = detailTableElement.getElementsByTagName("Key");
//		NodeList detailTableFieldNodeList = detailTableElement.getElementsByTagName("Field");
//		org.w3c.dom.Element detailTableKeyElement = null;
//		for (int i = 0; i < detailTableKeyNodeList.getLength(); i++) {
//			element = (org.w3c.dom.Element)detailTableKeyNodeList.item(i);
//			if (element.getAttribute("Type").equals("PK")) {
//				detailTableKeyElement = element;
//				break;
//			}
//		}
//		//
//		if (headerTableKeyElement == null || detailTableKeyElement == null) {
//			isValid = false;
//		} else {
//			tokenizer1 = new StringTokenizer(headerTableKeyElement.getAttribute("Fields"), ";");
//			tokenizer2 = new StringTokenizer(detailTableKeyElement.getAttribute("Fields"), ";");
//			if (tokenizer1.countTokens() >= tokenizer2.countTokens()) {
//				isValid = false;
//			} else {
//				while (tokenizer1.hasMoreTokens()) {
//					fieldID = tokenizer1.nextToken();
//					for (int i = 0; i < headerTableFieldNodeList.getLength(); i++) {
//						element = (org.w3c.dom.Element)headerTableFieldNodeList.item(i);
//						if (element.getAttribute("ID").equals(fieldID)) {
//							headerPKFieldTypeList.add(element.getAttribute("Type"));
//						}
//					}	
//				}
//				while (tokenizer2.hasMoreTokens()) {
//					fieldID = tokenizer2.nextToken();
//					for (int i = 0; i < detailTableFieldNodeList.getLength(); i++) {
//						element = (org.w3c.dom.Element)detailTableFieldNodeList.item(i);
//						if (element.getAttribute("ID").equals(fieldID)) {
//							detailPKFieldTypeList.add(element.getAttribute("Type"));
//						}
//					}	
//				}
//				for (int i = 0; i < headerPKFieldTypeList.size(); i++) {
//					if (!headerPKFieldTypeList.get(i).equals(detailPKFieldTypeList.get(i))) {
//						isValid = false;
//						break;
//					}
//				}
//				if (!isValid && !getReferKeysOfDetailTableToHeaderTable().equals("")) {
//					isValid = true;
//				}
//			}
//		}
//		//
//		return isValid;
//	}
	private boolean isValidWithKeys(String headerTableID, String detailTableID) {
		if (getDetailKeys(headerTableID, detailTableID)[0].equals("*ERROR")) {
			return false;
		} else {
			return true;
		}
	}

	private String[] getDetailKeys(String headerTableID, String detailTableID) {
		String[] keyFields = {"", ""};
		org.w3c.dom.Element element;
		String fieldID;
		StringTokenizer tokenizer1, tokenizer2;
		ArrayList<String> headerPKFieldTypeList = new ArrayList<String>();
		ArrayList<String> detailPKFieldTypeList = new ArrayList<String>();
		//
		org.w3c.dom.Element headerTableElement = getCurrentTableElementWithID(headerTableID);
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
		//
		org.w3c.dom.Element detailTableElement = getCurrentTableElementWithID(detailTableID);
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
		//
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
					if (!headerPKFieldTypeList.get(i).equals(detailPKFieldTypeList.get(i))) {
						isErrorWithKey = true;
						break;
					}
				}
			} else {
				isErrorWithKey = true;
			}
			//
			if (isErrorWithKey) {
				keyFields = getKeysAccordingToRelationship(headerTableElement, detailTableElement);
			}
		}
		//
		return keyFields;
	}

	private String[] getKeysAccordingToRelationship(org.w3c.dom.Element headerTableElement, org.w3c.dom.Element detailTableElement) {
		String[] keys = {"", ""};
		org.w3c.dom.Element relationshipElement;
		String internalHeaderTableID = getInternalTableID(headerTableElement.getAttribute("ID"));
		String internalDetailTableID = getInternalTableID(detailTableElement.getAttribute("ID"));
		//
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
		//
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
		//
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
							element2 = (org.w3c.dom.Element)sortingList.get(k);
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
							element2 = (org.w3c.dom.Element)sortingList.get(k);
							for (int m = 0; m < fieldList.getLength(); m++) {
								element3 = (org.w3c.dom.Element)fieldList.item(m);
								if (element2.getAttribute("FieldID").equals(element3.getAttribute("ID"))) {
									primaryKeyFieldIDList.add(element3.getAttribute("Alias"));
								}
							}
						}
					}
				}
				//
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
								bf.append(";");
								bf.append(primaryKeyFieldIDList.get(j));
							}
						}
					}
					keyFields = bf.toString();
				}
				//
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
		org.w3c.dom.Element currentElement;
		//
		if (jTableElementListFrom.getSelectedRow() != -1) {
			TableRowNumber tableRowNumber = (TableRowNumber)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 0);
			org.w3c.dom.Element element = tableRowNumber.getElement();
			String id = element.getAttribute("SortKey").toUpperCase();
			//
			if (jCheckBoxOverrideID.isSelected()) {
				id = JOptionPane.showInputDialog(null, res.getString("ImportModelMessage4"), id);
			}
			//
			if (id != null) {
				if (id.equals("")) {
					message = res.getString("ImportModelMessage5");
				} else {
					try {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						//
						id = id.toUpperCase();
						//
						createSubsystemIfNotExisting();
						//
						if (tableRowNumber.isTable()) {
							NodeList currentElementList = frame_.getDomDocument().getElementsByTagName("Table");
							for (int i = 0; i < currentElementList.getLength(); i++) {
								currentElement = (org.w3c.dom.Element)currentElementList.item(i);
								if (currentElement.getAttribute("ID").equals(id)) {
									message = "Table " + id + " " + res.getString("ImportModelMessage2");
									break;
								}
							}
							if (message.equals("")) {
								message = importTable(element, id);
								if (message.equals("")) {
									newlyAddedTableIDList.add(id);
									message = "Table " + id + " " + res.getString("ImportModelMessage15");
								} else {
									message = "Table " + id + " " + message;
								}
							}
						}
						//
						if (tableRowNumber.isFunction()) {
							NodeList currentElementList = frame_.getDomDocument().getElementsByTagName("Function");
							for (int i = 0; i < currentElementList.getLength(); i++) {
								currentElement = (org.w3c.dom.Element)currentElementList.item(i);
								if (currentElement.getAttribute("ID").equals(id)) {
									message = "Function " + id + " " + res.getString("ImportModelMessage2");
									break;
								}
							}
							if (message.equals("")) {
								message = importFunction(element, id);
								if (message.equals("")) {
									newlyAddedFunctionIDList.add(id);
									message = "Function " + id + " " + res.getString("ImportModelMessage15");
								} else {
									message = "Function " + id + " " + message;
								}
							}
						}
						//
						String cellText = (String)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 1);
						tableModelElementListFrom.setValueAt(cellText.replaceFirst("<html><b>", ""), jTableElementListFrom.getSelectedRow(), 1);
						cellText = (String)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 2);
						tableModelElementListFrom.setValueAt(cellText.replaceFirst("<html><b>", ""), jTableElementListFrom.getSelectedRow(), 2);
						cellText = (String)tableModelElementListFrom.getValueAt(jTableElementListFrom.getSelectedRow(), 3);
						tableModelElementListFrom.setValueAt(cellText.replaceFirst("<html><b>", ""), jTableElementListFrom.getSelectedRow(), 3);
					} finally {
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
				jTextAreaMessage.setText(message);
			}
		}
	}

	private void createSubsystemIfNotExisting() {
		boolean isExistingSubsystem = false;
		org.w3c.dom.Element workElement, subsystemElement;
		//
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
	
	private String importTable(org.w3c.dom.Element modelElement, String tableID) {
		String error = "";
		SortableDomElementListModel fieldSortingList, sortingList;
		org.w3c.dom.Element workElement1, workElement2, workElement3, childElement;
		//
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("System");
		org.w3c.dom.Element systemElementImportInto = (org.w3c.dom.Element)nodeList.item(0);
		org.w3c.dom.Element newElementToBeAdded = frame_.getDomDocument().createElement("Table");
		systemElementImportInto.appendChild(newElementToBeAdded);
		//
		newElementToBeAdded.setAttribute("ID", tableID);
		newElementToBeAdded.setAttribute("Name", modelElement.getAttribute("Name"));
		newElementToBeAdded.setAttribute("Remarks", modelElement.getAttribute("Descriptions"));
		newElementToBeAdded.setAttribute("SubsystemID", subsystemElementFrom.getAttribute("SortKey"));
		//
		HashMap<String, String> attrMap;
		nodeList = modelElement.getElementsByTagName("TableField");
		fieldSortingList = frame_.getSortedListModel(nodeList, "SortKey");
		for (int i = 0; i < fieldSortingList.getSize(); i++) {
			workElement1 = (org.w3c.dom.Element)fieldSortingList.elementAt(i);
			if (workElement1.getAttribute("AttributeType").equals("NATIVE")
					|| workElement1.getAttribute("AttributeType").equals("DERIVABLE")) {
				childElement = frame_.getDomDocument().createElement("Field");
				childElement.setAttribute("ID", workElement1.getAttribute("Alias"));
				childElement.setAttribute("Name", workElement1.getAttribute("Name"));
				childElement.setAttribute("Remarks", workElement1.getAttribute("Descriptions"));
				childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
				//
				attrMap = convertDataTypeIDToAttrMap(workElement1.getAttribute("DataTypeID"));
				childElement.setAttribute("Type", attrMap.get("Type"));
				childElement.setAttribute("Size", attrMap.get("Size"));
				childElement.setAttribute("Decimal", attrMap.get("Decimal"));
				//
				if (workElement1.getAttribute("NotNull").equals("true")) {
					childElement.setAttribute("Nullable", "F");
				} else {
					childElement.setAttribute("Nullable", "T");
				}
				//
				if (workElement1.getAttribute("AttributeType").equals("NATIVE")) {
					childElement.setAttribute("TypeOptions", "");
				} else {
					childElement.setAttribute("TypeOptions", "VIRTUAL");
				}
				//
				newElementToBeAdded.appendChild(childElement);
			}
		}
		//
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
					workElement2 = (org.w3c.dom.Element)sortingList.elementAt(j);
					for (int k = 0; k < fieldSortingList.getSize(); k++) {
						workElement3 = (org.w3c.dom.Element)fieldSortingList.elementAt(k);
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
		//
		nodeList = modelElement.getElementsByTagName("TableKey");
		for (int i = 0; i < nodeList.getLength(); i++) {
			workElement1 = (org.w3c.dom.Element)nodeList.item(i);
			if (workElement1.getAttribute("Type").equals("SK")) {
				childElement = frame_.getDomDocument().createElement("Key");
				childElement.setAttribute("Type", "SK");
				bf = new StringBuffer();
				NodeList nodeList2 = workElement1.getElementsByTagName("TableKeyField");
				sortingList = frame_.getSortedListModel(nodeList2, "SortKey");
				for (int j = 0; j < sortingList.getSize(); j++) {
					workElement2 = (org.w3c.dom.Element)sortingList.elementAt(j);
					for (int k = 0; k < fieldSortingList.getSize(); k++) {
						workElement3 = (org.w3c.dom.Element)fieldSortingList.elementAt(k);
						if (workElement3.getAttribute("ID").equals(workElement2.getAttribute("FieldID"))) {
							if (j > 0) {
								bf.append(";");
							}
							bf.append(workElement3.getAttribute("Alias"));
						}
					}
				}
				childElement.setAttribute("Fields", bf.toString());
				newElementToBeAdded.appendChild(childElement);
			}
		}
		//
		return error;
	}
	
	private String importFunction(org.w3c.dom.Element modelElement, String functionID) {
		String error = "";
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
		//
		String functionType = "";
		for (int i = 0; i < functionTypeList.getLength(); i++) {
			workElement = (org.w3c.dom.Element)functionTypeList.item(i);
			if (workElement.getAttribute("ID").equals(modelElement.getAttribute("FunctionTypeID"))) {
				functionType = workElement.getAttribute("SortKey").trim();
				break;
			}
		}
		//
		nodeList = frame_.getDomDocument().getElementsByTagName("System");
		org.w3c.dom.Element systemElementImportInto = (org.w3c.dom.Element)nodeList.item(0);
		org.w3c.dom.Element newElementToBeAdded = frame_.getDomDocument().createElement("Function");
		systemElementImportInto.appendChild(newElementToBeAdded);
		newElementToBeAdded.setAttribute("ID", functionID);
		newElementToBeAdded.setAttribute("Name", modelElement.getAttribute("Name"));
		newElementToBeAdded.setAttribute("SubsystemID", subsystemElementFrom.getAttribute("SortKey"));
		newElementToBeAdded.setAttribute("Type", functionType);
		//
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
		//
		if (functionType.equals("XF300")
				|| functionType.equals("XF310")
				|| functionType.equals("XF390")) {
			nodeList = modelElement.getElementsByTagName("IOTable");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("HEADER")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("HeaderTable").toUpperCase())) {
					headerTableID = convertTableInternalIDToExternalID(workElement.getAttribute("TableID"));
					headerTableElement = getCurrentTableElementWithID(headerTableID);
					break;
				}
			}
			String tableID;
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("NameExtension").toUpperCase().startsWith("DETAIL")
						|| workElement.getAttribute("Descriptions").toUpperCase().startsWith(res.getString("DetailTable").toUpperCase())) {
					tableID = convertTableInternalIDToExternalID(workElement.getAttribute("TableID"));
					if (isExistingTable(tableID) && isValidWithKeys(headerTableID, tableID)) {
						detailTableIDList.add(tableID);
						String[] keys = getDetailKeys(headerTableID, tableID);
						headerTableKeysList.add(keys[0]);
						detailTableKeysList.add(keys[1]);
						detailTableElementList.add(getCurrentTableElementWithID(tableID));
					}
				}
			}
		}
		//
		if (functionType.equals("XF000")) {
			newElementToBeAdded.setAttribute("Script", "");
		}
		//
		if (functionType.equals("XF100")) {
			newElementToBeAdded.setAttribute("PrimaryTable", primaryTableID);
			newElementToBeAdded.setAttribute("KeyFields", "");
			newElementToBeAdded.setAttribute("OrderBy", "");
			newElementToBeAdded.setAttribute("Size", "AUTO");
			newElementToBeAdded.setAttribute("InitialMsg", "");
			newElementToBeAdded.setAttribute("InitialListing", "T");
			//
			nodeList = modelElement.getElementsByTagName("FunctionUsedByThis");
			if (nodeList.getLength() == 1) {
				workElement = (org.w3c.dom.Element)nodeList.item(0);
				newElementToBeAdded.setAttribute("DetailFunction", convertFunctionInternalIDToExternalID(workElement.getAttribute("FunctionID")));
			} else {
				newElementToBeAdded.setAttribute("DetailFunction", "XXXXXX");
			}
			//
			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Column");
					childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
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
					childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}
			//
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
		//
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
			//
			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Column");
					childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
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
					childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}
			//
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
		//
		if (functionType.equals("XF200")) {
			newElementToBeAdded.setAttribute("PrimaryTable", primaryTableID);
			newElementToBeAdded.setAttribute("KeyFields", "");
			newElementToBeAdded.setAttribute("Size", "AUTO");
			newElementToBeAdded.setAttribute("InitialMsg", "");
			//
			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
				childElement = frame_.getDomDocument().createElement("Field");
				childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
				childElement.setAttribute("DataSource", primaryTableID + "." + workElement.getAttribute("ID")); 
				childElement.setAttribute("FieldOptions", "");
				newElementToBeAdded.appendChild(childElement);
			}
			//
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
		//
		if (functionType.equals("XF290")) {
			newElementToBeAdded.setAttribute("PrimaryTable", primaryTableID);
			newElementToBeAdded.setAttribute("KeyFields", "");
			newElementToBeAdded.setAttribute("PageSize", "A4");
			newElementToBeAdded.setAttribute("Direction", "");
			newElementToBeAdded.setAttribute("Margins", "50;50;50;50");
			newElementToBeAdded.setAttribute("WithPageNumber", "F");
			//
			//Get Default Font ID//
			org.w3c.dom.Element fontElement;
			String defaultFontID = "";
			NodeList fontList = frame_.getDomDocument().getElementsByTagName("PrintFont");
			sortingList = frame_.getSortedListModel(fontList, "FontName");
			fontElement = (org.w3c.dom.Element)sortingList.getElementAt(0);
			defaultFontID = fontElement.getAttribute("ID");
			//
			childElement = frame_.getDomDocument().createElement("Phrase");
			childElement.setAttribute("Order", "0000");
			childElement.setAttribute("Block", "HEADER");
			childElement.setAttribute("Value", "&Text(" + modelElement.getAttribute("Name") + ")");
			childElement.setAttribute("Alignment", "CENTER");
			childElement.setAttribute("FontID", defaultFontID);
			childElement.setAttribute("FontSize", "15");
			childElement.setAttribute("FontStyle", "BOLD");
			newElementToBeAdded.appendChild(childElement);
			//
			nodeList = primaryTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Phrase");
					childElement.setAttribute("Order", frame_.getFormatted4ByteString((i+1) * 10));
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
		//
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
			//
			newElementToBeAdded.setAttribute("HeaderTable", headerTableID);
			newElementToBeAdded.setAttribute("HeaderKeyFields", "");
			newElementToBeAdded.setAttribute("HeaderFunction", "");
			newElementToBeAdded.setAttribute("Size", "AUTO");
			//
			nodeList = headerTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 7) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Field");
					childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", headerTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}
			//
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
				//
				childElement = frame_.getDomDocument().createElement("Detail");
				childElement.setAttribute("Order", frame_.getFormatted4ByteString((p+1)*10));
				childElement.setAttribute("Table", detailTableIDList.get(p));
				childElement.setAttribute("HeaderKeyFields", headerTableKeysList.get(p));
				childElement.setAttribute("KeyFields", detailKeyFields);
				childElement.setAttribute("DetailFunction", "NONE");
				childElement.setAttribute("Caption", detailTableElementList.get(p).getAttribute("Name"));
				childElement.setAttribute("InitialMsg", "");
				childElement.setAttribute("InitialListing", "T");
				newElementToBeAdded.appendChild(childElement);
				//
				int columnCount = 0;
				nodeList = detailTableElementList.get(p).getElementsByTagName("Field");
				sortingList = frame_.getSortedListModel(nodeList, "Order");
				for (int i = 0; i < sortingList.getSize(); i++) {
					if (columnCount < 5) {
						workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
						if (!headerPKFieldIDList.contains(workElement.getAttribute("ID"))) {
							grandChildElement = frame_.getDomDocument().createElement("Column");
							grandChildElement.setAttribute("Order", frame_.getFormatted4ByteString(columnCount * 10));
							grandChildElement.setAttribute("DataSource", detailTableIDList.get(p) + "." + workElement.getAttribute("ID")); 
							grandChildElement.setAttribute("FieldOptions", "");
							childElement.appendChild(grandChildElement);
							columnCount++;
						}
					} else {
						break;
					}
				}
				//
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
		//
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
			//
			newElementToBeAdded.setAttribute("HeaderTable", headerTableID);
			newElementToBeAdded.setAttribute("HeaderKeyFields", headerKeyFields);
			newElementToBeAdded.setAttribute("DetailTable", detailTableIDList.get(0));
			newElementToBeAdded.setAttribute("DetailKeyFields", detailKeyFields);
			newElementToBeAdded.setAttribute("Size", "AUTO");
			//
			nodeList = headerTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 7) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("Field");
					childElement.setAttribute("Order", frame_.getFormatted4ByteString(i * 10));
					childElement.setAttribute("DataSource", headerTableID + "." + workElement.getAttribute("ID")); 
					childElement.setAttribute("FieldOptions", "");
					newElementToBeAdded.appendChild(childElement);
				} else {
					break;
				}
			}
			//
			int columnCount = 0;
			nodeList = detailTableElementList.get(0).getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (columnCount < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (!headerPKFieldIDList.contains(workElement.getAttribute("ID"))) {
						childElement = frame_.getDomDocument().createElement("Column");
						childElement.setAttribute("Order", frame_.getFormatted4ByteString(columnCount * 10));
						childElement.setAttribute("DataSource", detailTableIDList.get(0) + "." + workElement.getAttribute("ID")); 
						childElement.setAttribute("FieldOptions", "");
						newElementToBeAdded.appendChild(childElement);
						columnCount++;
					}
				} else {
					break;
				}
			}
			//
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
		//
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
			//
			//Get Default Font ID//
			org.w3c.dom.Element fontElement;
			String defaultFontID = "";
			NodeList fontList = frame_.getDomDocument().getElementsByTagName("PrintFont");
			sortingList = frame_.getSortedListModel(fontList, "FontName");
			fontElement = (org.w3c.dom.Element)sortingList.getElementAt(0);
			defaultFontID = fontElement.getAttribute("ID");
			//
			newElementToBeAdded.setAttribute("HeaderTable", headerTableID);
			newElementToBeAdded.setAttribute("HeaderKeyFields", headerKeyFields);
			newElementToBeAdded.setAttribute("DetailTable", detailTableIDList.get(0));
			newElementToBeAdded.setAttribute("DetailKeyFields", detailKeyFields);
			newElementToBeAdded.setAttribute("PageSize", "A4");
			newElementToBeAdded.setAttribute("Direction", "");
			newElementToBeAdded.setAttribute("Margins", "50;50;50;50");
			newElementToBeAdded.setAttribute("WithPageNumber", "T");
			newElementToBeAdded.setAttribute("TableFontID", defaultFontID);
			newElementToBeAdded.setAttribute("TableFontSize", "9");
			newElementToBeAdded.setAttribute("TableRowNoWidth", "5");
			//
			childElement = frame_.getDomDocument().createElement("HeaderPhrase");
			childElement.setAttribute("Order", "0000");
			childElement.setAttribute("Block", "HEADER");
			childElement.setAttribute("Value", "&Text(" + modelElement.getAttribute("Name") + ")");
			childElement.setAttribute("Alignment", "CENTER");
			childElement.setAttribute("FontID", defaultFontID);
			childElement.setAttribute("FontSize", "15");
			childElement.setAttribute("FontStyle", "BOLD");
			newElementToBeAdded.appendChild(childElement);
			//
			nodeList = headerTableElement.getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (i < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					childElement = frame_.getDomDocument().createElement("HeaderPhrase");
					childElement.setAttribute("Order", frame_.getFormatted4ByteString((i+1) * 10));
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
			//
			int columnCount = 0;
			nodeList = detailTableElementList.get(0).getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				if (columnCount < 5) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (!headerPKFieldIDList.contains(workElement.getAttribute("ID"))) {
						childElement = frame_.getDomDocument().createElement("Column");
						childElement.setAttribute("Order", frame_.getFormatted4ByteString(columnCount * 10));
						childElement.setAttribute("DataSource", detailTableIDList.get(0) + "." + workElement.getAttribute("ID")); 
						childElement.setAttribute("FieldOptions", "");
						childElement.setAttribute("Width", "19");
						childElement.setAttribute("Alignment", "LEFT");
						newElementToBeAdded.appendChild(childElement);
						columnCount++;
					}
				} else {
					break;
				}
			}
		}
		//
		return error;
	}
	
	private org.w3c.dom.Element getCurrentTableElementWithID(String id) {
		org.w3c.dom.Element element = null;
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("Table");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(id)) {
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
		private DialogImportModel dialog_;
		public TableRowNumber(int num, org.w3c.dom.Element elm, String type, DialogImportModel dialog) {
			number_ = num;
			element_ = elm;
			type_ = type;
			dialog_ = dialog;
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