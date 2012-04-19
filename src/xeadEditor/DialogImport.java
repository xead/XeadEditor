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
import javax.swing.table.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import xeadEditor.Editor.MainTreeNode;
import xeadEditor.Editor.SortableDomElementListModel;

public class DialogImport extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private Editor frame_;
	private JPanel panelMain = new JPanel();
	private JPanel jPanelNorth = new JPanel();
	private JPanel jPanelCenter = new JPanel();
	private JPanel jPanelCenterTop = new JPanel();
	private JPanel jPanelCenterTableList = new JPanel();
	private JPanel jPanelCenterFunctionList = new JPanel();
	private JPanel jPanelSouth = new JPanel();
	private JSplitPane jSplitPane = new JSplitPane();
	private JScrollPane jScrollPaneTableListFrom = new JScrollPane();
	private JScrollPane jScrollPaneFunctionListFrom = new JScrollPane();
	private JScrollPane jScrollPaneTableListInto = new JScrollPane();
	private JScrollPane jScrollPaneFunctionListInto = new JScrollPane();
	private TableModelTableListFrom tableModelTableListFrom = new TableModelTableListFrom();
	private JTable jTableTableListFrom = new JTable(tableModelTableListFrom);
	private TableModelFunctionListFrom tableModelFunctionListFrom = new TableModelFunctionListFrom();
	private JTable jTableFunctionListFrom = new JTable(tableModelFunctionListFrom);
	private TableModelReadOnlyList tableModelTableListInto = new TableModelReadOnlyList();
	private JTable jTableTableListInto = new JTable(tableModelTableListInto);
	private TableModelReadOnlyList tableModelFunctionListInto = new TableModelReadOnlyList();
	private JTable jTableFunctionListInto = new JTable(tableModelFunctionListInto);
	private JPanel jPanelImportFromTop = new JPanel();
	private JPanel jPanelImportIntoTop = new JPanel();
	private JLabel jLabelSubsystemFrom = new JLabel();
	private JComboBox jComboBoxSubsystemFrom = new JComboBox();
	private JLabel jLabelSubsystemInto = new JLabel();
	private JComboBox jComboBoxSubsystemInto = new JComboBox();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private JLabel jLabelSystemNameFrom = new JLabel();
	private JTextField jTextFieldSystemNameFrom = new JTextField();
	private JLabel jLabelSystemVersionFrom = new JLabel();
	private JTextField jTextFieldSystemVersionFrom = new JTextField();
	private ArrayList<org.w3c.dom.Element> subsystemNodeListFrom = null;
	private ArrayList<org.w3c.dom.Element> subsystemNodeListInto = null;
	private TableColumn column0, column1, column2, column3, column4;
	private DefaultTableCellRenderer rendererTableHeader = null;
	private CheckBoxHeaderRenderer checkBoxHeaderRendererTableListFrom = null;
	private CheckBoxHeaderRenderer checkBoxHeaderRendererFunctionListFrom = null;
	private DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentRight = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	private JButton jButtonCloseDialog = new JButton();
	private JButton jButtonImport = new JButton();
	private Document domDocumentImportingFrom = null;
	private Calendar calendar;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-HH:mm:ss");
	private boolean anyElementsImported = false;

	public DialogImport(Editor frame, String title, boolean modal) {
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

	public DialogImport(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		//
		//panelMain
		panelMain.setLayout(new BorderLayout());
		panelMain.setPreferredSize(new Dimension(800, 620));
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(jPanelNorth, BorderLayout.NORTH);
		panelMain.add(jPanelCenter, BorderLayout.CENTER);
		panelMain.add(jPanelSouth, BorderLayout.SOUTH);
		//
		//jPanelNorth and objects on it
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
		//
		//jPanelCenterTop and objects on it
		jLabelSubsystemFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelSubsystemFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSubsystemFrom.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSubsystemFrom.setText(res.getString("SubsystemImportingFrom"));
		jLabelSubsystemFrom.setBounds(new Rectangle(6, 12, 150, 15));
		jComboBoxSubsystemFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jComboBoxSubsystemFrom.setBounds(new Rectangle(163, 9, 210, 22));
		jComboBoxSubsystemFrom.addActionListener(new DialogImport_jComboBoxSubsystemFrom_actionAdapter(this));
		jLabelSubsystemInto.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelSubsystemInto.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSubsystemInto.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSubsystemInto.setText(res.getString("SubsystemImportingInto"));
		jLabelSubsystemInto.setBounds(new Rectangle(11, 12, 96, 15));
		jComboBoxSubsystemInto.setFont(new java.awt.Font("SansSerif", 0, 12));
		jComboBoxSubsystemInto.setBounds(new Rectangle(115, 9, 210, 22));
		jComboBoxSubsystemInto.addActionListener(new DialogImport_jComboBoxSubsystemInto_actionAdapter(this));
		jPanelImportFromTop.setPreferredSize(new Dimension(450, 40));
		jPanelImportFromTop.setBorder(BorderFactory.createEtchedBorder());
		jPanelImportFromTop.setLayout(null);
		jPanelImportFromTop.add(jLabelSubsystemFrom);
		jPanelImportFromTop.add(jComboBoxSubsystemFrom);
		jPanelImportIntoTop.setBorder(BorderFactory.createEtchedBorder());
		jPanelImportIntoTop.setLayout(null);
		jPanelImportIntoTop.add(jLabelSubsystemInto);
		jPanelImportIntoTop.add(jComboBoxSubsystemInto);
		jPanelCenterTop.setPreferredSize(new Dimension(800, 42));
		jPanelCenterTop.setLayout(new BorderLayout());
		jPanelCenterTop.add(jPanelImportFromTop, BorderLayout.WEST);
		jPanelCenterTop.add(jPanelImportIntoTop, BorderLayout.CENTER);
		//
		//jSplitPane and objects on it
		jSplitPane.setBorder(null);
		jSplitPane.setBounds(new Rectangle(11, 244, 380, 58));
		jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane.setDividerLocation(120);
		jSplitPane.setPreferredSize(new Dimension(750, 400));
		jSplitPane.add(jPanelCenterTableList, JSplitPane.TOP);
		jSplitPane.add(jPanelCenterFunctionList, JSplitPane.BOTTOM);
		jScrollPaneTableListFrom.setPreferredSize(new Dimension(450, 200));
		jScrollPaneTableListFrom.getViewport().add(jTableTableListFrom, null);
		jScrollPaneTableListInto.getViewport().add(jTableTableListInto, null);
		jPanelCenterTableList.setLayout(new BorderLayout());
		jPanelCenterTableList.add(jScrollPaneTableListFrom, BorderLayout.WEST);
		jPanelCenterTableList.add(jScrollPaneTableListInto, BorderLayout.CENTER);
		jScrollPaneFunctionListFrom.setPreferredSize(new Dimension(450, 200));
		jScrollPaneFunctionListFrom.getViewport().add(jTableFunctionListFrom, null);
		jScrollPaneFunctionListInto.getViewport().add(jTableFunctionListInto, null);
		jPanelCenterFunctionList.setLayout(new BorderLayout());
		jPanelCenterFunctionList.add(jScrollPaneFunctionListFrom, BorderLayout.WEST);
		jPanelCenterFunctionList.add(jScrollPaneFunctionListInto, BorderLayout.CENTER);
		jTextAreaMessage.setFont(new java.awt.Font("SansSerif", 0, 14));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setWrapStyleWord(true);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setBackground(SystemColor.control);
		jScrollPaneMessage.setPreferredSize(new Dimension(800, 140));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage, null);
		jPanelCenter.setLayout(new BorderLayout());
		jPanelCenter.add(jPanelCenterTop, BorderLayout.NORTH);
		jPanelCenter.add(jSplitPane, BorderLayout.CENTER);
		jPanelCenter.add(jScrollPaneMessage, BorderLayout.SOUTH);
		//
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentRight.setHorizontalAlignment(4); //RIGHT//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		//
		jTableTableListFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTableTableListFrom.setBackground(SystemColor.control);
		jTableTableListFrom.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableTableListFrom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableTableListFrom.setRowSelectionAllowed(true);
		tableModelTableListFrom.addColumn("NO.");
		tableModelTableListFrom.addColumn("");
		tableModelTableListFrom.addColumn(res.getString("TableID"));
		tableModelTableListFrom.addColumn(res.getString("TableName"));
		tableModelTableListFrom.addColumn(res.getString("ProcessType"));
		column0 = jTableTableListFrom.getColumnModel().getColumn(0);
		column1 = jTableTableListFrom.getColumnModel().getColumn(1);
		column2 = jTableTableListFrom.getColumnModel().getColumn(2);
		column3 = jTableTableListFrom.getColumnModel().getColumn(3);
		column4 = jTableTableListFrom.getColumnModel().getColumn(4);
		column0.setPreferredWidth(34);
		column1.setPreferredWidth(22);
		column2.setPreferredWidth(80);
		column3.setPreferredWidth(200);
		column4.setPreferredWidth(80);
		column0.setCellRenderer(rendererAlignmentCenter);
		CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
		column1.setCellRenderer(checkBoxRenderer);
		DefaultCellEditor editorWithCheckBox = new DefaultCellEditor(new JCheckBox());
		column1.setCellEditor(editorWithCheckBox);
		checkBoxHeaderRendererTableListFrom = new CheckBoxHeaderRenderer(new CheckBoxHeaderListener(jTableTableListFrom));
		column1.setHeaderRenderer(checkBoxHeaderRendererTableListFrom);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		column4.setCellRenderer(rendererAlignmentCenter);
		jTableTableListFrom.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, 12));
		rendererTableHeader = (DefaultTableCellRenderer)jTableTableListFrom.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		//
		jTableFunctionListFrom.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTableFunctionListFrom.setBackground(SystemColor.control);
		jTableFunctionListFrom.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableFunctionListFrom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableFunctionListFrom.setRowSelectionAllowed(true);
		tableModelFunctionListFrom.addColumn("NO.");
		tableModelFunctionListFrom.addColumn("");
		tableModelFunctionListFrom.addColumn(res.getString("FunctionID"));
		tableModelFunctionListFrom.addColumn(res.getString("FunctionName"));
		tableModelFunctionListFrom.addColumn(res.getString("ProcessType"));
		column0 = jTableFunctionListFrom.getColumnModel().getColumn(0);
		column1 = jTableFunctionListFrom.getColumnModel().getColumn(1);
		column2 = jTableFunctionListFrom.getColumnModel().getColumn(2);
		column3 = jTableFunctionListFrom.getColumnModel().getColumn(3);
		column4 = jTableFunctionListFrom.getColumnModel().getColumn(4);
		column0.setPreferredWidth(34);
		column1.setPreferredWidth(22);
		column2.setPreferredWidth(80);
		column3.setPreferredWidth(200);
		column4.setPreferredWidth(80);
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(checkBoxRenderer);
		column1.setCellEditor(editorWithCheckBox);
		checkBoxHeaderRendererFunctionListFrom = new CheckBoxHeaderRenderer(new CheckBoxHeaderListener(jTableFunctionListFrom));
		column1.setHeaderRenderer(checkBoxHeaderRendererFunctionListFrom);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		column4.setCellRenderer(rendererAlignmentCenter);
		jTableFunctionListFrom.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, 12));
		rendererTableHeader = (DefaultTableCellRenderer)jTableFunctionListFrom.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		//
		jTableTableListInto.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTableTableListInto.setBackground(SystemColor.control);
		jTableTableListInto.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableTableListInto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableTableListInto.setRowSelectionAllowed(true);
		tableModelTableListInto.addColumn("NO.");
		tableModelTableListInto.addColumn(res.getString("TableID"));
		tableModelTableListInto.addColumn(res.getString("TableName"));
		column0 = jTableTableListInto.getColumnModel().getColumn(0);
		column1 = jTableTableListInto.getColumnModel().getColumn(1);
		column2 = jTableTableListInto.getColumnModel().getColumn(2);
		column0.setPreferredWidth(34);
		column1.setPreferredWidth(80);
		column2.setPreferredWidth(200);
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		jTableTableListInto.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, 12));
		rendererTableHeader = (DefaultTableCellRenderer)jTableTableListInto.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		//
		jTableFunctionListInto.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTableFunctionListInto.setBackground(SystemColor.control);
		jTableFunctionListInto.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableFunctionListInto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableFunctionListInto.setRowSelectionAllowed(true);
		tableModelFunctionListInto.addColumn("NO.");
		tableModelFunctionListInto.addColumn(res.getString("FunctionID"));
		tableModelFunctionListInto.addColumn(res.getString("FunctionName"));
		column0 = jTableFunctionListInto.getColumnModel().getColumn(0);
		column1 = jTableFunctionListInto.getColumnModel().getColumn(1);
		column2 = jTableFunctionListInto.getColumnModel().getColumn(2);
		column0.setPreferredWidth(34);
		column1.setPreferredWidth(80);
		column2.setPreferredWidth(200);
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		jTableFunctionListInto.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, 12));
		rendererTableHeader = (DefaultTableCellRenderer)jTableFunctionListInto.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		//
		//jPanelSouth and objects on it
		jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
		jPanelSouth.setPreferredSize(new Dimension(800, 40));
		jButtonCloseDialog.setBounds(new Rectangle(20, 7, 70, 25));
		jButtonCloseDialog.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogImport_jButtonCloseDialog_actionAdapter(this));
		jButtonImport.setBounds(new Rectangle(400, 7, 120, 25));
		jButtonImport.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonImport.setText(res.getString("Import"));
		jButtonImport.addActionListener(new DialogImport_jButtonImport_actionAdapter(this));
		jButtonImport.setEnabled(false);
		jButtonImport.setFocusCycleRoot(true);
		jPanelSouth.setLayout(null);
		jPanelSouth.add(jButtonCloseDialog);
		jPanelSouth.add(jButtonImport);
		//
		//DialogImport
		this.setResizable(false);
		this.getContentPane().add(panelMain);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public boolean request(String fileName) {
		org.w3c.dom.Element element;
		MainTreeNode node;
		//
		anyElementsImported = false;
		this.setTitle(res.getString("ImportDialogTitle1") + fileName + res.getString("ImportDialogTitle2"));
		//
		try {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new FileInputStream(fileName)));
			domDocumentImportingFrom = parser.getDocument();
			NodeList nodeList = domDocumentImportingFrom.getElementsByTagName("System");
			org.w3c.dom.Element systemElementImportFrom = (org.w3c.dom.Element)nodeList.item(0);
			jTextFieldSystemNameFrom.setText(systemElementImportFrom.getAttribute("Name"));
			jTextFieldSystemVersionFrom.setText(systemElementImportFrom.getAttribute("Version"));
			//
			jComboBoxSubsystemFrom.removeAllItems();
			jComboBoxSubsystemFrom.addItem(res.getString("SelectFromList"));
			subsystemNodeListFrom = new ArrayList<org.w3c.dom.Element>();
			subsystemNodeListFrom.add(null);
			//
			NodeList nodelist = domDocumentImportingFrom.getElementsByTagName("Subsystem");
			SortableDomElementListModel sortingList = frame_.getSortedListModel(nodelist, "ID");
			for (int i = 0; i < sortingList.getSize(); i++) {
				element = (org.w3c.dom.Element)sortingList.get(i);
				jComboBoxSubsystemFrom.addItem(element.getAttribute("ID") + " " + element.getAttribute("Name"));
				subsystemNodeListFrom.add(element);
			}
			//
			jComboBoxSubsystemInto.removeAllItems();
			jComboBoxSubsystemInto.addItem(res.getString("SelectFromList"));
			subsystemNodeListInto = new ArrayList<org.w3c.dom.Element>();
			subsystemNodeListInto.add(null);
			//
			MainTreeNode subsystemListInto = frame_.getSubsystemListNode();
			for (int i = 0; i < subsystemListInto.getChildCount(); i++) {
				node = (MainTreeNode)subsystemListInto.getChildAt(i);
				jComboBoxSubsystemInto.addItem(node.getElement().getAttribute("ID") + " " + node.getElement().getAttribute("Name"));
				subsystemNodeListInto.add(node.getElement());
			}
			//
			checkBoxHeaderRendererTableListFrom.setSelected(false);
			if (tableModelTableListFrom.getRowCount() > 0) {
				int rowCount = tableModelTableListFrom.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelTableListFrom.removeRow(0);}
			}
			if (tableModelTableListInto.getRowCount() > 0) {
				int rowCount = tableModelTableListInto.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelTableListInto.removeRow(0);}
			}
			checkBoxHeaderRendererFunctionListFrom.setSelected(false);
			if (tableModelFunctionListFrom.getRowCount() > 0) {
				int rowCount = tableModelFunctionListFrom.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelFunctionListFrom.removeRow(0);}
			}
			if (tableModelFunctionListInto.getRowCount() > 0) {
				int rowCount = tableModelFunctionListInto.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelFunctionListInto.removeRow(0);}
			}
			//
			if (jTextAreaMessage.getText().equals("")) {
				jTextAreaMessage.setText(res.getString("ImportMessage1") + "\n");
			} else {
				jTextAreaMessage.setText(jTextAreaMessage.getText() + "\n");
			}
			jButtonImport.setEnabled(false);
			//
			super.setVisible(true);
			//
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, res.getString("FailedToParse") + "\n\n" + e.getMessage());
			e.printStackTrace();
		}
		//
		return anyElementsImported;
	}

	void jButtonImport_actionPerformed(ActionEvent e) {
		TableRowNumber tableRowNumber;
		String message;
		//
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			for (int i = 0; i < tableModelTableListFrom.getRowCount(); i++) {
				if (tableModelTableListFrom.getValueAt(i, 1).equals(Boolean.TRUE)) {
					tableRowNumber = (TableRowNumber)tableModelTableListFrom.getValueAt(i, 0);
					message = importTableElement(tableRowNumber.getElement());
					jTextAreaMessage.setText(getNewMessage(message, ""));
					jScrollPaneMessage.validate();
					jScrollPaneMessage.paintImmediately(0,0,jScrollPaneMessage.getWidth(),jScrollPaneMessage.getHeight());
				}
			}
			//
			for (int i = 0; i < tableModelFunctionListFrom.getRowCount(); i++) {
				if (tableModelFunctionListFrom.getValueAt(i, 1).equals(Boolean.TRUE)) {
					tableRowNumber = (TableRowNumber)tableModelFunctionListFrom.getValueAt(i, 0);
					message = importFunctionElement(tableRowNumber.getElement());
					jTextAreaMessage.setText(getNewMessage(message, ""));
					jScrollPaneMessage.validate();
					jScrollPaneMessage.paintImmediately(0,0,jScrollPaneMessage.getWidth(),jScrollPaneMessage.getHeight());
				}
			}
			//
			jComboBoxSubsystemFrom_actionPerformed(null);
			jComboBoxSubsystemInto_actionPerformed(null);
			//
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private String importTableElement(org.w3c.dom.Element importingElement) {
		String error, message = "";
		String tableID = importingElement.getAttribute("ID");
		org.w3c.dom.Element currentElement;
		//
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("System");
		org.w3c.dom.Element systemElementImportInto = (org.w3c.dom.Element)nodeList.item(0);
		org.w3c.dom.Element subsystemElementImportInto = subsystemNodeListInto.get(jComboBoxSubsystemInto.getSelectedIndex());
		String subsystemIDImportInto = subsystemElementImportInto.getAttribute("ID");
		//
		org.w3c.dom.Element newElement = (org.w3c.dom.Element)frame_.getDomDocument().importNode(importingElement, true);
		newElement.setAttribute("SubsystemID", subsystemIDImportInto);
		//
		NodeList currentElementList = frame_.getDomDocument().getElementsByTagName("Table");
		for (int i = 0; i < currentElementList.getLength(); i++) {
			currentElement = (org.w3c.dom.Element)currentElementList.item(i);
			if (currentElement.getAttribute("ID").equals(tableID)) {
				if (currentElement.getAttribute("SubsystemID").equals(subsystemIDImportInto)) {
					///////////////////////////////
					// Steps to MERGE definition //
					///////////////////////////////
					error = getErrorWithTable(newElement, currentElement);
					if (error.equals("")) {
						mergeTableElement(newElement, currentElement);
						message = "Table " + tableID + " " + res.getString("ImportMessage2");
						anyElementsImported = true;
					} else {
						message = "Table " + tableID + " " + res.getString("ImportMessage5") + error;
					}
				} else {
					///////////////////////////////////////
					// UNMATCH definition to be rejected //
					///////////////////////////////////////
					message = "Table " + tableID + " " + res.getString("ImportMessage6");
				}
				break;
			}
		}
		if (message.equals("")) {
			/////////////////////////////
			// Steps to ADD definition //
			/////////////////////////////
			error = getErrorWithTable(newElement, null);
			if (error.equals("")) {
				systemElementImportInto.appendChild(newElement);
				message = "Table " + tableID + " " + res.getString("ImportMessage4");
				anyElementsImported = true;
			} else {
				message = "Table " + tableID + " " + res.getString("ImportMessage5") + error;
			}
		}
		//
		return message;
	}
	
	private String getErrorWithTable(org.w3c.dom.Element newElement, org.w3c.dom.Element currentElement) {
		String error = "";
		boolean isTheSamePK = false;
		org.w3c.dom.Element workElement, workElement2;
		NodeList nodeList, nodeList2;
		//
		if (!isValidDatabaseID(newElement.getAttribute("DB"))) {
			error = res.getString("ImportMessage33");
		} else {
			if (currentElement == null) {
				isTheSamePK = true;
			} else {
				nodeList = newElement.getElementsByTagName("Key");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (workElement.getAttribute("Type").equals("PK")) {
						nodeList2 = currentElement.getElementsByTagName("Key");
						for (int j = 0; j < nodeList2.getLength(); j++) {
							workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
							if (workElement2.getAttribute("Type").equals("PK")) {
								if (workElement.getAttribute("Fields").equals(workElement2.getAttribute("Fields"))) {
									isTheSamePK = true;
									break;
								}
							}
						}
						break;
					}
				}
			}
			//
			if (isTheSamePK) {
				nodeList = newElement.getElementsByTagName("Refer");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (error.equals("") && !isValidTableID(workElement.getAttribute("ToTable"))) {
						error = res.getString("ImportMessage7");
						break;
					}
					if (error.equals("") && !workElement.getAttribute("ToKeyFields").equals("")) {
						if (!isValidFields(workElement.getAttribute("ToTable"), workElement.getAttribute("ToKeyFields"))) {
							error = res.getString("ImportMessage8");
							break;
						}
					}
					if (error.equals("") && !workElement.getAttribute("Fields").equals("")) {
						if (!isValidFields(workElement.getAttribute("ToTable"), workElement.getAttribute("Fields"))) {
							error = res.getString("ImportMessage9");
							break;
						}
					}
					if (!error.equals("")) {
						break;
					}
				}
			} else {
				error = res.getString("ImportMessage10");
			}
		}
		//
		return error;
	}
	
	private void mergeTableElement(org.w3c.dom.Element newElement, org.w3c.dom.Element currentElement) {
		org.w3c.dom.Element workElement, workElement2, element;
		NodeList nodeList, nodeList2;
		boolean isElementToBeAdded = true;
		//
		currentElement.setAttribute("Name", newElement.getAttribute("Name"));
		currentElement.setAttribute("Remarks", newElement.getAttribute("Remarks"));
		currentElement.setAttribute("RangeKey", newElement.getAttribute("RangeKey"));
		currentElement.setAttribute("UpdateCounter", newElement.getAttribute("UpdateCounter"));
		currentElement.setAttribute("DeleteOperation", newElement.getAttribute("DeleteOperation"));
		currentElement.setAttribute("ActiveWhere", newElement.getAttribute("ActiveWhere"));
		currentElement.setAttribute("DetailRowNumberAuto", newElement.getAttribute("DetailRowNumberAuto"));
		//
    	nodeList = newElement.getElementsByTagName("Field");
    	for (int i = 0; i < nodeList.getLength(); i++) {
    		workElement = (org.w3c.dom.Element)nodeList.item(i);
    		nodeList2 = currentElement.getElementsByTagName("Field");
    		isElementToBeAdded = true;
    		for (int j = 0; j < nodeList2.getLength(); j++) {
    			workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
    			if (workElement2.getAttribute("ID").equals(workElement.getAttribute("ID"))) {
    				isElementToBeAdded = false;
    				workElement2.setAttribute("Name", workElement.getAttribute("Name"));
    				workElement2.setAttribute("Order", workElement.getAttribute("Order"));
    				workElement2.setAttribute("Remarks", workElement.getAttribute("Remarks"));
    				workElement2.setAttribute("Type", workElement.getAttribute("Type"));
    				workElement2.setAttribute("Size", workElement.getAttribute("Size"));
    				workElement2.setAttribute("Decimal", workElement.getAttribute("Decimal"));
    				workElement2.setAttribute("Nullable", workElement.getAttribute("Nullable"));
    				workElement2.setAttribute("TypeOptions", workElement.getAttribute("TypeOptions"));
    				break;
    			}
    		}
    		if (isElementToBeAdded) {
    			element = (org.w3c.dom.Element)frame_.getDomDocument().importNode(workElement, false);
    			currentElement.appendChild(element);
    		}
    	}
		//
    	nodeList = newElement.getElementsByTagName("Key");
    	for (int i = 0; i < nodeList.getLength(); i++) {
    		workElement = (org.w3c.dom.Element)nodeList.item(i);
    		if (!workElement.getAttribute("Type").equals("PK")) {
    	    	nodeList2 = currentElement.getElementsByTagName("Key");
    	    	isElementToBeAdded = true;
    	    	for (int j = 0; j < nodeList2.getLength(); j++) {
    	    		workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
    	    		if (workElement2.getAttribute("Type").equals(workElement.getAttribute("Type"))
    	    				&& workElement2.getAttribute("Fields").equals(workElement.getAttribute("Fields"))) {
    	    			isElementToBeAdded = false;
       	    			break;
    	    		}
    	    	}
    	    	if (isElementToBeAdded) {
    	    		element = (org.w3c.dom.Element)frame_.getDomDocument().importNode(workElement, false);
    	    		currentElement.appendChild(element);
    	    	}
    		}
    	}
		//
    	nodeList = newElement.getElementsByTagName("Refer");
    	for (int i = 0; i < nodeList.getLength(); i++) {
    		workElement = (org.w3c.dom.Element)nodeList.item(i);
    		nodeList2 = currentElement.getElementsByTagName("Refer");
    		isElementToBeAdded = true;
    		for (int j = 0; j < nodeList2.getLength(); j++) {
    			workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
	    		if (workElement2.getAttribute("ToTable").equals(workElement.getAttribute("ToTable"))
	    				&& workElement2.getAttribute("TableAlias").equals(workElement.getAttribute("TableAlias"))) {
    				isElementToBeAdded = false;
    				workElement2.setAttribute("Order", workElement.getAttribute("Order"));
    				workElement2.setAttribute("ToKeyFields", workElement.getAttribute("ToKeyFields"));
    				workElement2.setAttribute("WithKeyFields", workElement.getAttribute("WithKeyFields"));
    				workElement2.setAttribute("Fields", workElement.getAttribute("Fields"));
    				break;
    			}
    		}
    		if (isElementToBeAdded) {
    			element = (org.w3c.dom.Element)frame_.getDomDocument().importNode(workElement, false);
    			currentElement.appendChild(element);
    		}
    	}
		//
    	nodeList = newElement.getElementsByTagName("Script");
    	for (int i = 0; i < nodeList.getLength(); i++) {
    		workElement = (org.w3c.dom.Element)nodeList.item(i);
    		nodeList2 = currentElement.getElementsByTagName("Script");
    		isElementToBeAdded = true;
    		for (int j = 0; j < nodeList2.getLength(); j++) {
    			workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
	    		if (workElement2.getAttribute("Name").equals(workElement.getAttribute("Name"))) {
    				isElementToBeAdded = false;
    				workElement2.setAttribute("Order", workElement.getAttribute("Order"));
    				workElement2.setAttribute("EventP", workElement.getAttribute("EventP"));
    				workElement2.setAttribute("EventR", workElement.getAttribute("EventR"));
    				workElement2.setAttribute("Text", workElement.getAttribute("Text"));
    				break;
    			}
    		}
    		if (isElementToBeAdded) {
    			element = (org.w3c.dom.Element)frame_.getDomDocument().importNode(workElement, false);
    			currentElement.appendChild(element);
    		}
    	}
	}
	
	private String importFunctionElement(org.w3c.dom.Element importingElement) {
		String message = "";
		String error;
		String functionID = importingElement.getAttribute("ID");
		org.w3c.dom.Element currentElement;
		//
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("System");
		org.w3c.dom.Element systemElementImportInto = (org.w3c.dom.Element)nodeList.item(0);
		org.w3c.dom.Element subsystemElementImportInto = subsystemNodeListInto.get(jComboBoxSubsystemInto.getSelectedIndex());
		String subsystemIDImportInto = subsystemElementImportInto.getAttribute("ID");
		//
		org.w3c.dom.Element newElement = (org.w3c.dom.Element)frame_.getDomDocument().importNode(importingElement, true);
		newElement.setAttribute("SubsystemID", subsystemIDImportInto);
		//
		NodeList currentElementList = frame_.getDomDocument().getElementsByTagName("Function");
		for (int i = 0; i < currentElementList.getLength(); i++) {
			currentElement = (org.w3c.dom.Element)currentElementList.item(i);
			if (currentElement.getAttribute("ID").equals(functionID)) {
				if (currentElement.getAttribute("SubsystemID").equals(subsystemIDImportInto)) {
					/////////////////////////////////
					// Steps to REPLACE definition //
					/////////////////////////////////
					error = getErrorWithFunction(newElement);
					if (error.equals("")) {
						systemElementImportInto.replaceChild(newElement, currentElement);
						message = "Function " + functionID + " " + res.getString("ImportMessage3");
						anyElementsImported = true;
					} else {
						message = "Function " + functionID + " " + res.getString("ImportMessage5") + error;
					}
				} else {
					///////////////////////////////////////
					// UNMATCH definition to be rejected //
					///////////////////////////////////////
					message = "Function " + functionID + " " + res.getString("ImportMessage6");
				}
				break;
			}
		}
		if (message.equals("")) {
			/////////////////////////////
			// Steps to ADD definition //
			/////////////////////////////
			error = getErrorWithFunction(newElement);
			if (error.equals("")) {
				systemElementImportInto.appendChild(newElement);
				message = "Function " + functionID + " " + res.getString("ImportMessage4");
				anyElementsImported = true;
			} else {
				message = "Function " + functionID + " " + res.getString("ImportMessage5") + error;
			}
		}
		//
		return message;
	}
	
	private String getErrorWithFunction(org.w3c.dom.Element element) {
		String error = "";
		boolean isInvalidType = true;
		StringTokenizer workTokenizer;
		String wrkStr;
		org.w3c.dom.Element workElement, workElement2;
		NodeList nodeList, nodeList2;
		//
		String functionType = element.getAttribute("Type");
		//
		if (functionType.equals("XF000")) {
			isInvalidType = false;
		}
		//
		if (functionType.equals("XF100") || functionType.equals("XF110")) {
			isInvalidType = false;
			if (!isValidTableID(element.getAttribute("PrimaryTable"))) {
				error = res.getString("ImportMessage11");
			}
			if (error.equals("") && !element.getAttribute("KeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("PrimaryTable"), element.getAttribute("KeyFields"))) {
					error = res.getString("ImportMessage12");
				}
			}
			if (error.equals("") && !element.getAttribute("OrderBy").equals("")) {
				workTokenizer = new StringTokenizer(element.getAttribute("OrderBy"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					if (!isValidDataSourceName(element.getAttribute("PrimaryTable"), wrkStr)) {
						error = res.getString("ImportMessage13");
						break;
					}
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Column");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("PrimaryTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage14");
						break;
					}
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Filter");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("PrimaryTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage15");
						break;
					}
				}
			}
		}
		//
		if (functionType.equals("XF110")) {
			isInvalidType = false;
			if (error.equals("") && !element.getAttribute("BatchTable").equals("")) {
				if (!isValidTableID(element.getAttribute("BatchTable"))) {
					error = res.getString("ImportMessage16");
				}
				if (error.equals("") && !element.getAttribute("BatchKeyFields").equals("")) {
					if (!isValidFields(element.getAttribute("BatchTable"), element.getAttribute("BatchKeyFields"))) {
						error = res.getString("ImportMessage17");
					}
				}
				if (error.equals("") && !element.getAttribute("BatchWithKeyFields").equals("")) {
					workTokenizer = new StringTokenizer(element.getAttribute("BatchWithKeyFields"), ";");
					while (workTokenizer.hasMoreTokens()) {
						wrkStr = workTokenizer.nextToken();
						if (!isValidDataSourceName(element.getAttribute("PrimaryTable"), wrkStr)) {
							error = res.getString("ImportMessage17");
							break;
						}
					}
				}
			}
		}
		//
		if (functionType.equals("XF200")) {
			isInvalidType = false;
			if (!isValidTableID(element.getAttribute("PrimaryTable"))) {
				error = res.getString("ImportMessage11");
			}
			if (error.equals("") && !element.getAttribute("KeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("PrimaryTable"), element.getAttribute("KeyFields"))) {
					error = res.getString("ImportMessage12");
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Field");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("PrimaryTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage18");
						break;
					}
				}
			}
		    if (error.equals("")) {
		    	nodeList = element.getElementsByTagName("Tab");
		    	for (int i = 0; i < nodeList.getLength(); i++) {
		    		workElement = (org.w3c.dom.Element)nodeList.item(i);
		    		nodeList2 = workElement.getElementsByTagName("TabField");
		    		for (int j = 0; j < nodeList2.getLength(); j++) {
		    			workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
		    			if (!isValidDataSourceName(element.getAttribute("PrimaryTable"), workElement2.getAttribute("DataSource"))) {
		    				error = res.getString("ImportMessage24");
		    				break;
		    			}
		    		}
		    		if (!error.equals("")) {
		    			break;
		    		}
		    	}
		    }
		}
		//
		if (functionType.equals("XF290")) {
			isInvalidType = false;
			if (!isValidTableID(element.getAttribute("PrimaryTable"))) {
				error = res.getString("ImportMessage11");
			}
			if (error.equals("") && !element.getAttribute("KeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("PrimaryTable"), element.getAttribute("KeyFields"))) {
					error = res.getString("ImportMessage12");
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Phrase");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidFontID(workElement.getAttribute("FontID"))) {
						error = res.getString("ImportMessage31");
						break;
					}
				}
			}
		}
		//
		if (functionType.equals("XF300")) {
			isInvalidType = false;
			if (!isValidTableID(element.getAttribute("HeaderTable"))) {
				error = res.getString("ImportMessage19");
			}
			if (error.equals("") && !element.getAttribute("HeaderKeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("HeaderTable"), element.getAttribute("HeaderKeyFields"))) {
					error = res.getString("ImportMessage20");
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Field");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("HeaderTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage21");
						break;
					}
				}
			}
		    if (error.equals("")) {
		    	nodeList = element.getElementsByTagName("Detail");
		    	for (int i = 0; i < nodeList.getLength(); i++) {
		    		workElement = (org.w3c.dom.Element)nodeList.item(i);
		    		if (!isValidTableID(workElement.getAttribute("Table"))) {
						error = res.getString("ImportMessage22");
		    		}
		    		if (error.equals("") && !workElement.getAttribute("KeyFields").equals("")) {
		    			if (!isValidFields(workElement.getAttribute("Table"), workElement.getAttribute("KeyFields"))) {
							error = res.getString("ImportMessage23");
		    			}
		    		}
		    		if (error.equals("") && !workElement.getAttribute("HeaderKeyFields").equals("")) {
		    			if (!isValidFields(element.getAttribute("HeaderTable"), workElement.getAttribute("HeaderKeyFields"))) {
							error = res.getString("ImportMessage23");
		    			}
		    		}
		    		if (error.equals("") && !workElement.getAttribute("OrderBy").equals("")) {
		    			workTokenizer = new StringTokenizer(workElement.getAttribute("OrderBy"), ";");
		    			while (workTokenizer.hasMoreTokens()) {
		    				wrkStr = workTokenizer.nextToken();
		    				if (!isValidDataSourceName(workElement.getAttribute("Table"), wrkStr)) {
								error = res.getString("ImportMessage13");
		    					break;
		    				}
		    			}
		    		}
		    		if (error.equals("")) {
		    			nodeList2 = workElement.getElementsByTagName("Column");
		    			for (int j = 0; j < nodeList2.getLength(); j++) {
		    				workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
		    				if (!isValidDataSourceName(workElement.getAttribute("Table"), workElement2.getAttribute("DataSource"))) {
								error = res.getString("ImportMessage24");
		    					break;
		    				}
		    			}
		    		}
		    		if (error.equals("")) {
		    			nodeList2 = workElement.getElementsByTagName("Filter");
		    			for (int j = 0; j < nodeList2.getLength(); j++) {
		    				workElement2 = (org.w3c.dom.Element)nodeList2.item(j);
		    				if (!isValidDataSourceName(workElement.getAttribute("Table"), workElement2.getAttribute("DataSource"))) {
								error = res.getString("ImportMessage15");
		    					break;
		    				}
		    			}
		    		}
		    		if (!error.equals("")) {
		    			break;
		    		}
		    	}
		    }
		    if (error.equals("") && !element.getAttribute("StructureTable").equals("")) {
			    if (!isValidTableID(element.getAttribute("StructureTable"))) {
			    	error = res.getString("ImportMessage34");
			    }
				if (error.equals("") && !element.getAttribute("StructureTableKeys").equals("")) {
					if (!isValidFields(element.getAttribute("StructureTable"), element.getAttribute("StructureTableKeys"))) {
						error = res.getString("ImportMessage34");
					}
				}
				if (error.equals("") && !element.getAttribute("StructureChildKeys").equals("")) {
					if (!isValidFields(element.getAttribute("StructureTable"), element.getAttribute("StructureChildKeys"))) {
						error = res.getString("ImportMessage34");
					}
				}
				if (error.equals("") && !element.getAttribute("StructureNodeText").equals("")) {
					if (!isValidFields(element.getAttribute("StructureTable"), element.getAttribute("StructureNodeText"))) {
						error = res.getString("ImportMessage35");
					}
				}
				if (error.equals("") && !element.getAttribute("StructureNodeIconsField").equals("")) {
					if (!isValidFields(element.getAttribute("StructureTable"), element.getAttribute("StructureNodeIconsField"))) {
						error = res.getString("ImportMessage36");
					}
				}
		    }
		}
		//
		if (functionType.equals("XF310")) {
			isInvalidType = false;
			if (!isValidTableID(element.getAttribute("HeaderTable"))) {
				error = res.getString("ImportMessage19");
			}
			if (error.equals("") && !element.getAttribute("HeaderKeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("HeaderTable"), element.getAttribute("HeaderKeyFields"))) {
					error = res.getString("ImportMessage20");
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Field");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("HeaderTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage21");
						break;
					}
				}
			}
			if (error.equals("") && !isValidTableID(element.getAttribute("DetailTable"))) {
				error = res.getString("ImportMessage22");
			}
			if (error.equals("") && !element.getAttribute("DetailKeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("DetailTable"), element.getAttribute("DetailKeyFields"))) {
					error = res.getString("ImportMessage23");
				}
			}
			if (error.equals("") && !element.getAttribute("DetailOrderBy").equals("")) {
				workTokenizer = new StringTokenizer(element.getAttribute("DetailOrderBy"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					if (!isValidDataSourceName(element.getAttribute("DetailTable"), wrkStr)) {
						error = res.getString("ImportMessage13");
						break;
					}
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Column");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("DetailTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage24");
						break;
					}
				}
			}
			if (error.equals("") && !isValidTableID(element.getAttribute("AddRowListTable"))) {
				error = res.getString("ImportMessage25");
			}
			if (error.equals("") && !element.getAttribute("AddRowListWithFields").equals("")) {
				if (!isValidFields(element.getAttribute("AddRowListTable"), element.getAttribute("AddRowListWithFields"))) {
					error = res.getString("ImportMessage26");
				}
			}
			if (error.equals("") && !element.getAttribute("AddRowListWithHeaderFields").equals("")) {
				workTokenizer = new StringTokenizer(element.getAttribute("AddRowListWithHeaderFields"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					if (!isValidDataSourceName(element.getAttribute("HeaderTable"), wrkStr)) {
						error = res.getString("ImportMessage26");
						break;
					}
				}
			}
			if (error.equals("") && !element.getAttribute("AddRowListOrderBy").equals("")) {
				workTokenizer = new StringTokenizer(element.getAttribute("AddRowListOrderBy"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					if (!isValidDataSourceName(element.getAttribute("AddRowListTable"), wrkStr)) {
						error = res.getString("ImportMessage27");
						break;
					}
				}
			}
			if (error.equals("") && !element.getAttribute("AddRowListReturnDataSources").equals("")) {
				workTokenizer = new StringTokenizer(element.getAttribute("AddRowListReturnDataSources"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					if (!isValidDataSourceName(element.getAttribute("AddRowListTable"), wrkStr)) {
						error = res.getString("ImportMessage28");
						break;
					}
				}
			}
			if (error.equals("") && !element.getAttribute("AddRowListReturnToDetailDataSources").equals("")) {
				workTokenizer = new StringTokenizer(element.getAttribute("AddRowListReturnToDetailDataSources"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					if (!isValidDataSourceName(element.getAttribute("DetailTable"), wrkStr)) {
						error = res.getString("ImportMessage28");
						break;
					}
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("AddRowListColumn");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("AddRowListTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage29");
						break;
					}
				}
			}
		}
		//
		if (functionType.equals("XF390")) {
			isInvalidType = false;
			if (!isValidTableID(element.getAttribute("HeaderTable"))) {
				error = res.getString("ImportMessage19");
			}
			if (error.equals("") && !element.getAttribute("HeaderKeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("HeaderTable"), element.getAttribute("HeaderKeyFields"))) {
					error = res.getString("ImportMessage20");
				}
			}
			if (error.equals("") && !isValidTableID(element.getAttribute("DetailTable"))) {
				error = res.getString("ImportMessage22");
			}
			if (error.equals("") && !element.getAttribute("DetailKeyFields").equals("")) {
				if (!isValidFields(element.getAttribute("DetailTable"), element.getAttribute("DetailKeyFields"))) {
					error = res.getString("ImportMessage23");
				}
			}
			if (error.equals("") && !element.getAttribute("DetailOrderBy").equals("")) {
				workTokenizer = new StringTokenizer(element.getAttribute("DetailOrderBy"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					if (!isValidDataSourceName(element.getAttribute("DetailTable"), wrkStr)) {
						error = res.getString("ImportMessage13");
						break;
					}
				}
			}
			if (error.equals("")) {
				if (!isValidFontID(element.getAttribute("TableFontID"))) {
					error = res.getString("ImportMessage32");
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("HeaderPhrase");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidFontID(workElement.getAttribute("FontID"))) {
						error = res.getString("ImportMessage31");
						break;
					}
				}
			}
			if (error.equals("")) {
				nodeList = element.getElementsByTagName("Column");
				for (int i = 0; i < nodeList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)nodeList.item(i);
					if (!isValidDataSourceName(element.getAttribute("DetailTable"), workElement.getAttribute("DataSource"))) {
						error = res.getString("ImportMessage24");
						break;
					}
				}
			}
		}
		//
		if (isInvalidType) {
			error = res.getString("ImportMessage30");
		}
		//
		return error;
	}
	
	private boolean isValidFontID(String id) {
		boolean isValid = false;
		org.w3c.dom.Element element;
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("PrintFont");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(id)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	private boolean isValidDatabaseID(String id) {
		boolean isValid = false;
		org.w3c.dom.Element element;
		if (id.equals("")) {
			isValid = true;
		} else {
			NodeList nodeList = frame_.getDomDocument().getElementsByTagName("SubDB");
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (org.w3c.dom.Element)nodeList.item(i);
				if (element.getAttribute("ID").equals(id)) {
					isValid = true;
					break;
				}
			}
		}
		return isValid;
	}
	
	private boolean isValidTableID(String id) {
		boolean isValid = false;
		org.w3c.dom.Element element;
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("Table");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(id)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	private boolean isValidFields(String tableID, String fields) {
		boolean isValid = false;
		org.w3c.dom.Element element, workElement;
		StringTokenizer workTokenizer;
		NodeList workList;
		String wrkStr;
		int countOfFields = 0;
		int countOfMatch = 0;
		//
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("Table");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(tableID)) {
				workList = element.getElementsByTagName("Field");
				workTokenizer = new StringTokenizer(fields, ";");
				countOfFields = workTokenizer.countTokens();
				while (workTokenizer.hasMoreTokens()) {
					wrkStr = workTokenizer.nextToken();
					for (int j = 0; j < workList.getLength(); j++) {
						workElement = (org.w3c.dom.Element)workList.item(j);
						if (workElement.getAttribute("ID").equals(wrkStr)) {
							countOfMatch++;
							break;
						}
					}
				}
				if (countOfFields == countOfMatch) {
					isValid = true;
				}
				break;
			}
		}
		//
		return isValid;
	}
	
	private boolean isValidDataSourceName(String ownerTableID, String dataSourceName) {
		boolean isValid = false;
		org.w3c.dom.Element element, workElement;
		NodeList workList;
		//
		StringTokenizer workTokenizer = new StringTokenizer(dataSourceName, ".");
		String alias = workTokenizer.nextToken();
		String fieldID = workTokenizer.nextToken();
		//
		NodeList nodeList = frame_.getDomDocument().getElementsByTagName("Table");
		for (int i = 0; i < nodeList.getLength(); i++) {
			element = (org.w3c.dom.Element)nodeList.item(i);
			if (element.getAttribute("ID").equals(ownerTableID)) {
				if (alias.equals(ownerTableID)) {
					workList = element.getElementsByTagName("Field");
					for (int j = 0; j < workList.getLength(); j++) {
						workElement = (org.w3c.dom.Element)workList.item(j);
						if (workElement.getAttribute("ID").equals(fieldID)) {
							isValid = true;
							break;
						}
					}
					if (!isValid) {
						break;
					}
				} else {
					workList = element.getElementsByTagName("Refer");
					for (int j = 0; j < workList.getLength(); j++) {
						workElement = (org.w3c.dom.Element)workList.item(j);
						if (workElement.getAttribute("TableAlias").equals(alias)) {
							workTokenizer = new StringTokenizer(workElement.getAttribute("Fields"), ";");
							while (workTokenizer.hasMoreTokens()) {
								if (workTokenizer.nextToken().equals(fieldID)) {
									isValid = true;
									break;
								}
							}
							if (!isValid) {
								break;
							}
						} else {
							if (workElement.getAttribute("ToTable").equals(alias)) {
								workTokenizer = new StringTokenizer(workElement.getAttribute("Fields"), ";");
								while (workTokenizer.hasMoreTokens()) {
									if (workTokenizer.nextToken().equals(fieldID)) {
										isValid = true;
										break;
									}
								}
								if (!isValid) {
									break;
								}
							}
						}
					}
					if (!isValid) {
						break;
					}
				}
				break;
			}
		}
		return isValid;
	}
	
	private String getNewMessage(String newMessage1, String newMessage2) {
		StringBuffer bf = new StringBuffer();
		//
		if (!jTextAreaMessage.getText().equals("")) {
			bf.append(jTextAreaMessage.getText());
			bf.append("\n");
		}
		//
		bf.append("> ");
		bf.append(newMessage1);
		if (!newMessage2.equals("")) {
			bf.append(" ");
			bf.append(newMessage2);
		}
		//
		calendar = Calendar.getInstance();
		bf.append("(");
		bf.append(formatter.format(calendar.getTime()));
		bf.append(")");
		//
		return bf.toString();
	}
	
	boolean isReadyToStartImport() {
		boolean isReady = false;
		if (jComboBoxSubsystemInto.getSelectedIndex() > 0) {
			for (int i = 0; i < tableModelTableListFrom.getRowCount(); i++) {
				if (tableModelTableListFrom.getValueAt(i, 1).equals(Boolean.TRUE)) {
					isReady = true;
					break;
				}
			}
			if (!isReady) {
				for (int i = 0; i < tableModelFunctionListFrom.getRowCount(); i++) {
					if (tableModelFunctionListFrom.getValueAt(i, 1).equals(Boolean.TRUE)) {
						isReady = true;
						break;
					}
				}
			}
		}
		return isReady;
	}

	void jButtonCloseDialog_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void jComboBoxSubsystemFrom_actionPerformed(ActionEvent e) {
		String subsystemID;
		org.w3c.dom.Element subsystemElement, element;
		int rowNumber;
		//
		if (subsystemNodeListFrom != null && jComboBoxSubsystemFrom.getSelectedIndex() >= 0) {
			checkBoxHeaderRendererTableListFrom.setSelected(false);
			checkBoxHeaderRendererFunctionListFrom.setSelected(false);
			//
			subsystemElement = subsystemNodeListFrom.get(jComboBoxSubsystemFrom.getSelectedIndex());
			//
			if (tableModelTableListFrom.getRowCount() > 0) {
				int rowCount = tableModelTableListFrom.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelTableListFrom.removeRow(0);}
			}
			if (tableModelFunctionListFrom.getRowCount() > 0) {
				int rowCount = tableModelFunctionListFrom.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelFunctionListFrom.removeRow(0);}
			}
			//
			if (subsystemElement != null) {
				subsystemID = subsystemElement.getAttribute("ID");
				//
				rowNumber = 1;
				NodeList nodelist = domDocumentImportingFrom.getElementsByTagName("Table");
				SortableDomElementListModel sortingList = frame_.getSortedListModel(nodelist, "ID");
				for (int i = 0; i < sortingList.getSize(); i++) {
					element = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (element.getAttribute("SubsystemID").equals(subsystemID)) {
						Object[] Cell = new Object[5];
						Cell[0] = new TableRowNumber(rowNumber++, element);
						Cell[1] = Boolean.FALSE;
						Cell[2] = element.getAttribute("ID");
						Cell[3] = element.getAttribute("Name");
						if (jComboBoxSubsystemInto.getSelectedIndex() == 0) {
							Cell[4] = "?";
						} else {
							Cell[4] = getProcessType("Table", element.getAttribute("ID"));
						}
						tableModelTableListFrom.addRow(Cell);
					}
				}
				//
				rowNumber = 1;
				nodelist = domDocumentImportingFrom.getElementsByTagName("Function");
				sortingList = frame_.getSortedListModel(nodelist, "ID");
				for (int i = 0; i < sortingList.getSize(); i++) {
					element = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (element.getAttribute("SubsystemID").equals(subsystemID)) {
						Object[] Cell = new Object[5];
						Cell[0] = new TableRowNumber(rowNumber++, element);
						Cell[1] = Boolean.FALSE;
						Cell[2] = element.getAttribute("ID");
						Cell[3] = element.getAttribute("Name");
						if (jComboBoxSubsystemInto.getSelectedIndex() == 0) {
							Cell[4] = "?";
						} else {
							Cell[4] = getProcessType("Function", element.getAttribute("ID"));
						}
						tableModelFunctionListFrom.addRow(Cell);
					}
				}
				//
				if (jComboBoxSubsystemInto.getSelectedIndex() == 0 && subsystemNodeListInto.size() > 1) {
					for (int i = 1; i < subsystemNodeListInto.size(); i++) {
						element = (org.w3c.dom.Element)subsystemNodeListInto.get(i);
						if (element.getAttribute("ID").equals(subsystemID)) {
							jComboBoxSubsystemInto.setSelectedIndex(i);
						}
					}
				}
				//
				jButtonImport.setEnabled(false);
			}
		}
	}
	
	String getProcessType(String tagName, String id) {
		String processType = "";
		String subsystemID;
		org.w3c.dom.Element subsystemElement, element;
		NodeList nodeList;
		//
		subsystemElement = subsystemNodeListInto.get(jComboBoxSubsystemInto.getSelectedIndex());
		if (subsystemElement != null) {
			subsystemID = subsystemElement.getAttribute("ID");
			processType = "ADD";
			//
			nodeList = frame_.getDomDocument().getElementsByTagName(tagName);
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (org.w3c.dom.Element)nodeList.item(i);
				if (element.getAttribute("ID").equals(id)) {
					if (element.getAttribute("SubsystemID").equals(subsystemID)) {
						if (tagName.equals("Table")) {
							processType = "MERGE";
						} else {
							processType = "REPLACE";
						}
					} else {
						processType = "UNMATCH";
					}
					break;
				}
			}
		}
		//
		return processType;
	}

	void jComboBoxSubsystemInto_actionPerformed(ActionEvent e) {
		String subsystemID;
		org.w3c.dom.Element subsystemElement, element;
		TableRowNumber tableRowNumber;
		int rowNumber;
		//
		if (subsystemNodeListInto != null && jComboBoxSubsystemInto.getSelectedIndex() >= 0) {
			subsystemElement = subsystemNodeListInto.get(jComboBoxSubsystemInto.getSelectedIndex());
			//
			if (tableModelTableListInto.getRowCount() > 0) {
				int rowCount = tableModelTableListInto.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelTableListInto.removeRow(0);}
			}
			if (tableModelFunctionListInto.getRowCount() > 0) {
				int rowCount = tableModelFunctionListInto.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelFunctionListInto.removeRow(0);}
			}
			//
			if (subsystemElement != null) {
				subsystemID = subsystemElement.getAttribute("ID");
				//
				rowNumber = 1;
				NodeList nodelist = frame_.getDomDocument().getElementsByTagName("Table");
				SortableDomElementListModel sortingList = frame_.getSortedListModel(nodelist, "ID");
				for (int i = 0; i < sortingList.getSize(); i++) {
					element = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (element.getAttribute("SubsystemID").equals(subsystemID)) {
						Object[] Cell = new Object[3];
						Cell[0] = rowNumber++;
						Cell[1] = element.getAttribute("ID");
						Cell[2] = element.getAttribute("Name");
						tableModelTableListInto.addRow(Cell);
					}
				}
				//
				rowNumber = 1;
				nodelist = frame_.getDomDocument().getElementsByTagName("Function");
				sortingList = frame_.getSortedListModel(nodelist, "ID");
				for (int i = 0; i < sortingList.getSize(); i++) {
					element = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (element.getAttribute("SubsystemID").equals(subsystemID)) {
						Object[] Cell = new Object[3];
						Cell[0] = rowNumber++;
						Cell[1] = element.getAttribute("ID");
						Cell[2] = element.getAttribute("Name");
						tableModelFunctionListInto.addRow(Cell);
					}
				}
				//
				for (int i = 0; i < tableModelTableListFrom.getRowCount(); i++) {
					tableRowNumber = (TableRowNumber)tableModelTableListFrom.getValueAt(i, 0);
					tableModelTableListFrom.setValueAt(getProcessType("Table", tableRowNumber.getElement().getAttribute("ID")), i, 4);
				}
				for (int i = 0; i < tableModelFunctionListFrom.getRowCount(); i++) {
					tableRowNumber = (TableRowNumber)tableModelFunctionListFrom.getValueAt(i, 0);
					tableModelFunctionListFrom.setValueAt(getProcessType("Function", tableRowNumber.getElement().getAttribute("ID")), i, 4);
				}
				//
				jButtonImport.setEnabled(isReadyToStartImport());
			}
		}
	}

	class CheckBoxHeaderListener implements ItemListener {
		private CheckBoxHeaderRenderer rendererComponent_ = null;
		private JTable table_ = null;
		CheckBoxHeaderListener(JTable table) {
			table_ = table;
		}
		public void setRenderer(CheckBoxHeaderRenderer rendererComponent) {
			rendererComponent_ = rendererComponent;
		}
		public void itemStateChanged(ItemEvent e) {   
			if (rendererComponent_ != null) {
				if (table_.equals(jTableTableListFrom)) {
					if (rendererComponent_.isSelected()) {
						for (int i = 0; i < tableModelTableListFrom.getRowCount(); i++) {
							tableModelTableListFrom.setValueAt(Boolean.TRUE, i, 1);
						}
					} else {
						for (int i = 0; i < tableModelTableListFrom.getRowCount(); i++) {
							tableModelTableListFrom.setValueAt(Boolean.FALSE, i, 1);
						}
					}
				}
				if (table_.equals(jTableFunctionListFrom)) {
					if (rendererComponent_.isSelected()) {
						for (int i = 0; i < tableModelFunctionListFrom.getRowCount(); i++) {
							tableModelFunctionListFrom.setValueAt(Boolean.TRUE, i, 1);
						}
					} else {
						for (int i = 0; i < tableModelFunctionListFrom.getRowCount(); i++) {
							tableModelFunctionListFrom.setValueAt(Boolean.FALSE, i, 1);
						}
					}
				}
				//
				jButtonImport.setEnabled(isReadyToStartImport());
			}
		}   
	}   

	class CheckBoxHeaderRenderer extends JCheckBox implements TableCellRenderer, MouseListener {   
		private static final long serialVersionUID = 1L;
		protected CheckBoxHeaderRenderer rendererComponent;   
		protected int column;   
		protected boolean mousePressed = false;   
		public CheckBoxHeaderRenderer(CheckBoxHeaderListener itemListener) {   
			rendererComponent = this;   
			rendererComponent.addItemListener(itemListener);  
			itemListener.setRenderer(this);
		}   
		public Component getTableCellRendererComponent(	JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
			if (table != null) {   
				JTableHeader header = table.getTableHeader();   
				if (header != null) {   
					header.addMouseListener(rendererComponent);   
				}   
			}   
			setColumn(column);   
			rendererComponent.setText("");   
			rendererComponent.setBackground(new Color(219,219,219));   
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));   
			return rendererComponent;   
		}   
		protected void setColumn(int column) {   
			this.column = column;   
		}   
		public int getColumn() {   
			return column;   
		}   
		protected void handleClickEvent(MouseEvent e) {   
			if (mousePressed) {   
				mousePressed=false;   
				JTableHeader header = (JTableHeader)(e.getSource());   
				JTable tableView = header.getTable();   
				TableColumnModel columnModel = tableView.getColumnModel();   
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());   
				int column = tableView.convertColumnIndexToModel(viewColumn);   
				if (viewColumn == this.column && e.getClickCount() == 1 && column != -1) {   
					doClick();   
				}   
			}   
		}   
		public void mouseClicked(MouseEvent e) {   
			handleClickEvent(e);   
			((JTableHeader)e.getSource()).repaint();   
		}   
		public void mousePressed(MouseEvent e) {   
			mousePressed = true;   
		}   
		public void mouseReleased(MouseEvent e) {   
		}   
		public void mouseEntered(MouseEvent e) {   
		}   
		public void mouseExited(MouseEvent e) {   
		}   
	}  

	class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
		private static final long serialVersionUID = 1L;
		CheckBoxRenderer() {
			setHorizontalAlignment(JLabel.CENTER);
		}
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelected((value != null && ((Boolean)value).booleanValue()));
			//
			jButtonImport.setEnabled(isReadyToStartImport());
			//
			return this;
		}
	}

	class TableModelReadOnlyList extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {return false;}
	}

	class TableModelTableListFrom extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable( int row, int col) {
			if (col != 1) {return false;} else {return true;}
		}
	}

	class TableModelFunctionListFrom extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable( int row, int col) {
			if (col != 1) {return false;} else {return true;}
		}
	}

	class TableRowNumber extends Object {
		private org.w3c.dom.Element element_;
		private int number_;
		public TableRowNumber(int num, org.w3c.dom.Element elm) {
			number_ = num;
			element_ = elm;
		}
		public String toString() {
			return Integer.toString(number_);
		}
		public org.w3c.dom.Element getElement() {
			return element_;
		}
	}
}

class DialogImport_jButtonCloseDialog_actionAdapter implements java.awt.event.ActionListener {
	DialogImport adaptee;

	DialogImport_jButtonCloseDialog_actionAdapter(DialogImport adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCloseDialog_actionPerformed(e);
	}
}

class DialogImport_jButtonImport_actionAdapter implements java.awt.event.ActionListener {
	DialogImport adaptee;

	DialogImport_jButtonImport_actionAdapter(DialogImport adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonImport_actionPerformed(e);
	}
}

class DialogImport_jComboBoxSubsystemFrom_actionAdapter implements java.awt.event.ActionListener {
	DialogImport adaptee;
	DialogImport_jComboBoxSubsystemFrom_actionAdapter(DialogImport adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxSubsystemFrom_actionPerformed(e);
	}
}

class DialogImport_jComboBoxSubsystemInto_actionAdapter implements java.awt.event.ActionListener {
	DialogImport adaptee;
	DialogImport_jComboBoxSubsystemInto_actionAdapter(DialogImport adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxSubsystemInto_actionPerformed(e);
	}
}


