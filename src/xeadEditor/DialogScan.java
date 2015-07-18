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
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.*;

import xeadEditor.Editor.MainTreeNode;
import xeadEditor.Editor.SortableDomElementListModel;

import java.io.*;
import java.net.URI;

public class DialogScan extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private Editor frame_;
	private BorderLayout borderLayoutMain = new BorderLayout();
	private JPanel panelMain = new JPanel();
	private JPanel jPanelNorth = new JPanel();
	private JPanel jPanelSouth = new JPanel();
	private JSplitPane jSplitPane = new JSplitPane();
	private JScrollPane jScrollPaneScanResult = new JScrollPane();
	private JScrollPane jScrollPaneScanDetail = new JScrollPane();
	private JLabel jLabel1 = new JLabel();
	private JTextField jTextFieldScan = new JTextField();
	private JLabel jLabel2 = new JLabel();
	private JTextField jTextFieldReplace = new JTextField();
	private JCheckBox jCheckBoxCaseSensitive = new JCheckBox();
	private JLabel jLabel3 = new JLabel();
	private JCheckBox jCheckBoxSystem = new JCheckBox();
	private JCheckBox jCheckBoxMenu = new JCheckBox();
	private JCheckBox jCheckBoxSubsystem = new JCheckBox();
	private JCheckBox jCheckBoxTable = new JCheckBox();
	private JCheckBox jCheckBoxFunction = new JCheckBox();
	private JComboBox jComboBoxSubsystems = new JComboBox();
	private JCheckBox jCheckBoxAll = new JCheckBox();
	private ArrayList<MainTreeNode> subsystemNodeList;
	private TableModelScanResult tableModelScanResult = new TableModelScanResult();
	private JTable jTableScanResult = new JTable(tableModelScanResult);
	private TableColumn column0, column1, column2, column3, column4, column5, column6;
	private DefaultTableCellRenderer rendererTableHeader = null;
	private CheckBoxHeaderRenderer checkBoxHeaderRenderer = null;
	private DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentRight = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	private JTextPane jTextPaneScanDetail = new JTextPane();
	private JButton jButtonStartScan = new JButton();
	private JProgressBar jProgressBar = new JProgressBar();
	private JButton jButtonCloseDialog = new JButton();
	private JButton jButtonReplaceAllSelected = new JButton();
	private JButton jButtonGenerateListData = new JButton();
	private boolean updated = false;
	private int countOfRows = 0;
	private int countOfStrings = 0;
	private int countOfRowsReplaced = 0;
	private int countOfStringsReplaced = 0;
	private String stringToBeScanned = "";
	private String stringToBeReplaced = "";
	private SimpleAttributeSet attrs = new SimpleAttributeSet();
	private StyledDocument doc = null;
	private Style style = null;

	public DialogScan(Editor frame, String title, boolean modal) {
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

	public DialogScan(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		//
		//panelMain
		panelMain.setLayout(borderLayoutMain);
		panelMain.setPreferredSize(new Dimension(1020, 700));
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(jPanelNorth, BorderLayout.NORTH);
		panelMain.add(jPanelSouth, BorderLayout.SOUTH);
		panelMain.add(jSplitPane, BorderLayout.CENTER);
		//
		//jPanelNorth and objects on it
		jPanelNorth.setBorder(null);
		jPanelNorth.setLayout(null);
		jPanelNorth.setPreferredSize(new Dimension(1000, 108));
		jLabel1.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setText(res.getString("ScanString"));
		jLabel1.setBounds(new Rectangle(5, 13, 130, 20));
		jTextFieldScan.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldScan.setText("");
		jTextFieldScan.setBounds(new Rectangle(140, 10, 240, 25));
		jTextFieldScan.addKeyListener(new DialogScan_jTextFieldScan_keyAdapter(this));
		jLabel2.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel2.setText(res.getString("ScanStringReplacedTo"));
		jLabel2.setBounds(new Rectangle(390, 13, 130, 20));
		jTextFieldReplace.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldReplace.setText("");
		jTextFieldReplace.setBounds(new Rectangle(525, 10, 240, 25));
		jTextFieldReplace.addKeyListener(new DialogScan_jTextFieldReplace_keyAdapter(this));
		jCheckBoxCaseSensitive.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxCaseSensitive.setText(res.getString("CaseSensitive"));
		jCheckBoxCaseSensitive.setBounds(new Rectangle(770, 10, 220, 25));
		jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel3.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabel3.setText(res.getString("ScanRange"));
		jLabel3.setBounds(new Rectangle(5, 44, 130, 20));
		jCheckBoxSystem.setBounds(new Rectangle(140, 41, 150, 25));
		jCheckBoxSystem.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxSystem.setText(res.getString("SystemDescriptions"));
		jCheckBoxSystem.setSelected(true);
		jCheckBoxMenu.setBounds(new Rectangle(290, 41, 150, 25));
		jCheckBoxMenu.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxMenu.setText(res.getString("MenuDefinitions"));
		jCheckBoxMenu.setSelected(true);
		jCheckBoxSubsystem.setBounds(new Rectangle(140, 72, 150, 25));
		jCheckBoxSubsystem.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxSubsystem.setText(res.getString("SubsystemDescriptions"));
		jCheckBoxSubsystem.addChangeListener(new DialogScan_jCheckBoxSubsystem_changeAdapter(this));
		jCheckBoxSubsystem.setSelected(true);
		jCheckBoxTable.setBounds(new Rectangle(290, 72, 150, 25));
		jCheckBoxTable.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxTable.setText(res.getString("TableDefinitions"));
		jCheckBoxTable.addChangeListener(new DialogScan_jCheckBoxTable_changeAdapter(this));
		jCheckBoxTable.setSelected(true);
		jCheckBoxFunction.setBounds(new Rectangle(440, 72, 150, 25));
		jCheckBoxFunction.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxFunction.setText(res.getString("FunctionDefinitions"));
		jCheckBoxFunction.addChangeListener(new DialogScan_jCheckBoxFunction_changeAdapter(this));
		jCheckBoxFunction.setSelected(true);
		jComboBoxSubsystems.setBounds(new Rectangle(590, 72, 240, 25));
		jComboBoxSubsystems.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxAll.setBounds(new Rectangle(850, 36, 140, 25));
		jCheckBoxAll.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxAll.setText(res.getString("CheckAllItems"));
		jCheckBoxAll.setSelected(true);
		jCheckBoxAll.addActionListener(new DialogScan_jCheckBoxAll_actionAdapter(this));
		jButtonStartScan.setBounds(new Rectangle(850, 63, 130, 36));
		jButtonStartScan.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonStartScan.setText(res.getString("ScanStart"));
		jButtonStartScan.addActionListener(new DialogScan_jButtonStartScan_actionAdapter(this));
		jButtonStartScan.setEnabled(false);
		jButtonStartScan.setFocusCycleRoot(true);
		jProgressBar.setBounds(new Rectangle(850, 43, 130, 56));
		jProgressBar.setVisible(false);
		//
		jPanelNorth.add(jTextFieldScan, null);
		jPanelNorth.add(jCheckBoxTable, null);
		jPanelNorth.add(jTextFieldReplace, null);
		jPanelNorth.add(jLabel2, null);
		jPanelNorth.add(jCheckBoxCaseSensitive, null);
		jPanelNorth.add(jCheckBoxSubsystem, null);
		jPanelNorth.add(jCheckBoxFunction, null);
		jPanelNorth.add(jCheckBoxTable, null);
		jPanelNorth.add(jLabel1, null);
		jPanelNorth.add(jCheckBoxSystem, null);
		jPanelNorth.add(jCheckBoxMenu, null);
		jPanelNorth.add(jLabel3, null);
		jPanelNorth.add(jComboBoxSubsystems, null);
		jPanelNorth.add(jCheckBoxAll, null);
		jPanelNorth.add(jButtonStartScan, null);
		jPanelNorth.add(jProgressBar, null);
		//
		//jSplitPane and objects on it
		jSplitPane.setBorder(null);
		jSplitPane.setBounds(new Rectangle(11, 244, 380, 58));
		jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane.setDividerLocation(300);
		jSplitPane.setPreferredSize(new Dimension(750, 300));
		jScrollPaneScanDetail.setBounds(new Rectangle(132, 144, 282, 51));
		jSplitPane.add(jScrollPaneScanResult, JSplitPane.TOP);
		jSplitPane.add(jScrollPaneScanDetail, JSplitPane.BOTTOM);
		jTableScanResult.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTableScanResult.setBackground(SystemColor.control);
		jTableScanResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableScanResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableScanResult.setRowSelectionAllowed(true);
		jTableScanResult.getSelectionModel().addListSelectionListener(new DialogScan_jTableScanResult_listSelectionAdapter(this));
		jTableScanResult.setRowHeight(Editor.TABLE_ROW_HEIGHT);
		jTableScanResult.addMouseListener(new DialogScan_jTableScanResult_mouseAdapter(this));
		jTableScanResult.getTableHeader().setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		tableModelScanResult.addColumn("NO.");
		tableModelScanResult.addColumn("");
		tableModelScanResult.addColumn(res.getString("ElementType"));
		tableModelScanResult.addColumn(res.getString("ElementName"));
		tableModelScanResult.addColumn(res.getString("AttributeName"));
		tableModelScanResult.addColumn(res.getString("AttributeValue"));
		tableModelScanResult.addColumn(res.getString("HitCount"));
		column0 = jTableScanResult.getColumnModel().getColumn(0);
		column1 = jTableScanResult.getColumnModel().getColumn(1);
		column2 = jTableScanResult.getColumnModel().getColumn(2);
		column3 = jTableScanResult.getColumnModel().getColumn(3);
		column4 = jTableScanResult.getColumnModel().getColumn(4);
		column5 = jTableScanResult.getColumnModel().getColumn(5);
		column6 = jTableScanResult.getColumnModel().getColumn(6);
		column0.setPreferredWidth(40);
		column1.setPreferredWidth(30);
		column2.setPreferredWidth(190);
		column3.setPreferredWidth(400);
		column4.setPreferredWidth(125);
		column5.setPreferredWidth(145);
		column6.setPreferredWidth(35);
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentRight.setHorizontalAlignment(4); //RIGHT//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		column0.setCellRenderer(rendererAlignmentCenter);
		CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
		column1.setCellRenderer(checkBoxRenderer);
		DefaultCellEditor editorWithCheckBox = new DefaultCellEditor(new JCheckBox());
		column1.setCellEditor(editorWithCheckBox);
		checkBoxHeaderRenderer = new CheckBoxHeaderRenderer(new CheckBoxHeaderListener());
		column1.setHeaderRenderer(checkBoxHeaderRenderer);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		column4.setCellRenderer(rendererAlignmentLeft);
		column5.setCellRenderer(rendererAlignmentLeft);
		column6.setCellRenderer(rendererAlignmentRight);
		rendererTableHeader = (DefaultTableCellRenderer)jTableScanResult.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		jScrollPaneScanResult.getViewport().add(jTableScanResult, null);
		jTextPaneScanDetail.setFont(new java.awt.Font(frame_.scriptFontName, 0, Editor.MAIN_FONT_SIZE + 2));
		jTextPaneScanDetail.setEditable(false);
		jTextPaneScanDetail.setBackground(SystemColor.control);
		jScrollPaneScanDetail.getViewport().add(jTextPaneScanDetail, null);
		doc = jTextPaneScanDetail.getStyledDocument();
		style = doc.addStyle("style1",null);
		StyleConstants.setBackground(style, Color.cyan);
		//
		//jPanelSouth and objects on it
		jPanelSouth.setBorder(null);
		jPanelSouth.setLayout(null);
		jPanelSouth.setPreferredSize(new Dimension(800, 43));
		jButtonCloseDialog.setBounds(new Rectangle(30, 8, 120, 27));
		jButtonCloseDialog.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogScan_jButtonCloseDialog_actionAdapter(this));
		jButtonReplaceAllSelected.setBounds(new Rectangle(380, 8, 230, 27));
		jButtonReplaceAllSelected.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonReplaceAllSelected.setText(res.getString("ReplaceAllSelected"));
		jButtonReplaceAllSelected.addActionListener(new DialogScan_jButtonReplaceRowsSelected_actionAdapter(this));
		jButtonGenerateListData.setBounds(new Rectangle(840, 8, 120, 27));
		jButtonGenerateListData.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonGenerateListData.setText(res.getString("GenerateList"));
		jButtonGenerateListData.setEnabled(false);
		jButtonGenerateListData.addActionListener(new DialogScan_jButtonGenerateListData_actionAdapter(this));
		jPanelSouth.add(jButtonCloseDialog, null);
		jPanelSouth.add(jButtonReplaceAllSelected, null);
		jPanelSouth.add(jButtonGenerateListData, null);
		//
		//DialogScan
		this.setTitle(res.getString("ScanDialogTitle"));
		this.getContentPane().add(panelMain);
		this.setPreferredSize(new Dimension(1024, 768));
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public boolean request() {
		updated = false;
		//
		org.w3c.dom.Element element;
		MainTreeNode node;
		MainTreeNode subsystemList = frame_.getSubsystemListNode();
		jComboBoxSubsystems.removeAllItems();
		jComboBoxSubsystems.addItem(res.getString("AllSubsystems"));
		subsystemNodeList = new ArrayList<MainTreeNode>();
		subsystemNodeList.add(null);
		for (int i = 0; i < subsystemList.getChildCount(); i++) {
			node = (MainTreeNode)subsystemList.getChildAt(i);
			element = (org.w3c.dom.Element)node.getElement();
			jComboBoxSubsystems.addItem(element.getAttribute("ID") + " " + element.getAttribute("Name"));
			subsystemNodeList.add(node);
		}
		//
		if (tableModelScanResult.getRowCount() > 0) {
			int rowCount = tableModelScanResult.getRowCount();
			for (int i = 0; i < rowCount; i++) {tableModelScanResult.removeRow(0);}
		}
		jTextPaneScanDetail.setText(res.getString("ScanDialogComment1"));
		checkBoxHeaderRenderer.setSelected(false);
		//
		jButtonReplaceAllSelected.setEnabled(false);
		jButtonGenerateListData.setEnabled(false);
		jTextFieldScan.requestFocusInWindow();
		//
		super.setVisible(true);
		//
		return updated;
	}

	void jCheckBoxSubsystem_stateChanged(ChangeEvent e) {
		if (jCheckBoxSubsystem.isSelected() || jCheckBoxTable.isSelected() || jCheckBoxFunction.isSelected()) {
			jComboBoxSubsystems.setEnabled(true);
		} else {
			jComboBoxSubsystems.setEnabled(false);
		}
	}

	void jButtonStartScan_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element workElement, workElement1, workElement2;
		String subsystemID = "";
		NodeList systemList = null;
		NodeList workList = null;
		NodeList workList1 = null;
		NodeList workList2 = null;
		SortableDomElementListModel sortedMenuList = null;
		SortableDomElementListModel sortedSubsystemList = null;
		SortableDomElementListModel sortedTableList = null;
		SortableDomElementListModel sortedFunctionList = null;
		//
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			if (jCheckBoxCaseSensitive.isSelected()) {
				stringToBeScanned = jTextFieldScan.getText();
			} else {
				stringToBeScanned = jTextFieldScan.getText().toUpperCase();
			}
			//
			//Clear previous scan result//
			countOfRows = 0;
			countOfStrings = 0;
			jTextPaneScanDetail.setText("");
			if (tableModelScanResult.getRowCount() > 0) {
				int rowCount = tableModelScanResult.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelScanResult.removeRow(0);}
			}
			jButtonReplaceAllSelected.setEnabled(false);
			checkBoxHeaderRenderer.setSelected(false);
			jTableScanResult.getTableHeader().repaint();   
			//
			//Get ID of Subsystem if it is specified to be scanned;
			if (jComboBoxSubsystems.getSelectedIndex() != 0) {
				MainTreeNode subsystemNode = subsystemNodeList.get(jComboBoxSubsystems.getSelectedIndex());
				workElement = subsystemNode.getElement();
				subsystemID = workElement.getAttribute("ID");
			}
			//
			//Count number of elements to be scanned//
			int countOfElementsToBeScanned = 0;
			if (jCheckBoxSystem.isSelected()) {
				systemList = frame_.getDomDocument().getElementsByTagName("System");
				countOfElementsToBeScanned += systemList.getLength();
			}
			if (jCheckBoxMenu.isSelected()) {
				workList = frame_.getDomDocument().getElementsByTagName("Menu");
				sortedMenuList = frame_.getSortedListModel(workList, "ID");
				countOfElementsToBeScanned += sortedMenuList.getSize();
			}
			if (jCheckBoxSubsystem.isSelected()) {
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				sortedSubsystemList = frame_.getSortedListModel(workList, "ID");
				if (subsystemID.equals("")) {
					countOfElementsToBeScanned += sortedSubsystemList.getSize();
				} else {
					countOfElementsToBeScanned++;
				}
			}
			if (jCheckBoxTable.isSelected()) {
				workList = frame_.getDomDocument().getElementsByTagName("Table");
				sortedTableList = frame_.getSortedListModel(workList, "SubsystemID");
				if (subsystemID.equals("")) {
					countOfElementsToBeScanned += workList.getLength();
				} else {
					for (int i = 0; i < sortedTableList.getSize(); i++) {
						workElement = (org.w3c.dom.Element)sortedTableList.getElementAt(i);
						if (subsystemID.equals(workElement.getAttribute("SubsystemID"))) {
							countOfElementsToBeScanned++;
						}
					}
				}
			}
			if (jCheckBoxFunction.isSelected()) {
				workList = frame_.getDomDocument().getElementsByTagName("Function");
				sortedFunctionList = frame_.getSortedListModel(workList, "SubsystemID");
				if (subsystemID.equals("")) {
					countOfElementsToBeScanned += workList.getLength();
				} else {
					for (int i = 0; i < sortedFunctionList.getSize(); i++) {
						workElement = (org.w3c.dom.Element)sortedFunctionList.getElementAt(i);
						if (subsystemID.equals(workElement.getAttribute("SubsystemID"))) {
							countOfElementsToBeScanned++;
						}
					}
				}
			}
			//
			jProgressBar.setValue(0);
			jProgressBar.setMaximum(countOfElementsToBeScanned);
			jProgressBar.setVisible(true);
			jButtonStartScan.setVisible(false);
			//
			//Scan System element//
			if (jCheckBoxSystem.isSelected()) {
				jProgressBar.setValue(jProgressBar.getValue() + 1);
				jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
				//
				workElement = (org.w3c.dom.Element)systemList.item(0);
				scanAttribute(workElement, "System", res.getString("System"), "Name", res.getString("Name"));
				scanAttribute(workElement, "System", res.getString("System"), "Remarks", res.getString("Remarks"));
				scanAttribute(workElement, "System", res.getString("System"), "LoginScript", res.getString("LoginScript"));
				scanAttribute(workElement, "System", res.getString("System"), "ScriptFunctions", res.getString("ScriptFunctions"));
			}
			//
			//Scan Menu elements//
			if (jCheckBoxMenu.isSelected()) {
				for (int i = 0; i < sortedMenuList.getSize(); i++) {
					workElement = (org.w3c.dom.Element)sortedMenuList.getElementAt(i);
					jProgressBar.setValue(jProgressBar.getValue() + 1);
					jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
					//
					scanAttribute(workElement, "Menu", res.getString("Menu"), "Name", res.getString("Name"));
					scanAttribute(workElement, "Menu", res.getString("Menu"), "CrossCheckersToBeLoaded", res.getString("MenuCrossCheckers"));
					scanAttribute(workElement, "Menu", res.getString("Menu"), "HelpURL", res.getString("MenuHelpURL"));
					//
					workList1 = workElement.getElementsByTagName("Option");
					for (int j = 0; j < workList1.getLength(); j++) {
						workElement1 = (org.w3c.dom.Element)workList1.item(j);
						scanAttribute(workElement1, "MenuOption", res.getString("Menu/Option"), "OptionName", res.getString("OptionName"));
					}
				}
			}
			//
			//Scan Subsystem elements//
			if (jCheckBoxSubsystem.isSelected()) {
				for (int i = 0; i < sortedSubsystemList.getSize(); i++) {
					workElement = (org.w3c.dom.Element)sortedSubsystemList.getElementAt(i);
					if (subsystemID.equals("") || subsystemID.equals(workElement.getAttribute("ID"))) {
						jProgressBar.setValue(jProgressBar.getValue() + 1);
						jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
						//
						scanAttribute(workElement, "Subsystem", res.getString("Subsystem"), "Name", res.getString("Name"));
						scanAttribute(workElement, "Subsystem", res.getString("Subsystem"), "Descriptions", res.getString("Remarks"));
						//
						if (!subsystemID.equals("") && subsystemID.equals(workElement.getAttribute("ID"))) {
							break;
						}
					}
				}
			}
			//
			//Scan Table elements//
			if (jCheckBoxTable.isSelected()) {
				for (int i = 0; i < sortedTableList.getSize(); i++) {
					workElement = (org.w3c.dom.Element)sortedTableList.getElementAt(i);
					if (subsystemID.equals("") || subsystemID.equals(workElement.getAttribute("SubsystemID"))) {
						jProgressBar.setValue(jProgressBar.getValue() + 1);
						jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
						//
						scanAttribute(workElement, "Table", res.getString("TableDefinition"), "Name", res.getString("Name"));
						scanAttribute(workElement, "Table", res.getString("TableDefinition"), "UpdateCounter", res.getString("UpdateCounter"));
						scanAttribute(workElement, "Table", res.getString("TableDefinition"), "ActiveWhere", res.getString("ActiveWhere"));
						scanAttribute(workElement, "Table", res.getString("TableDefinition"), "Remarks", res.getString("Remarks"));
						//
						workList1 = workElement.getElementsByTagName("Field");
						for (int j = 0; j < workList1.getLength(); j++) {
							workElement1 = (org.w3c.dom.Element)workList1.item(j);
							//scanAttribute(workElement1, "TableField", res.getString("Table/Field"), "ID", "ID");
							scanAttribute(workElement1, "TableField", res.getString("Table/Field"), "Name", res.getString("Name"));
							scanAttribute(workElement1, "TableField", res.getString("Table/Field"), "TypeOptions", res.getString("TypeOptions"));
							scanAttribute(workElement1, "TableField", res.getString("Table/Field"), "Remarks", res.getString("Remarks"));
						}
						//
						workList1 = workElement.getElementsByTagName("Script");
						for (int j = 0; j < workList1.getLength(); j++) {
							workElement1 = (org.w3c.dom.Element)workList1.item(j);
							scanAttribute(workElement1, "TableScript", res.getString("Table/Script"), "Name", res.getString("Name"));
							scanAttribute(workElement1, "TableScript", res.getString("Table/Script"), "Text", res.getString("Text"));
						}
					}
				}
			}
			//
			//Scan Function elements//
			if (jCheckBoxFunction.isSelected()) {
				for (int i = 0; i < sortedFunctionList.getSize(); i++) {
					workElement = (org.w3c.dom.Element)sortedFunctionList.getElementAt(i);
					if (subsystemID.equals("") || subsystemID.equals(workElement.getAttribute("SubsystemID"))) {
						jProgressBar.setValue(jProgressBar.getValue() + 1);
						jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
						//
						scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "Name", res.getString("Name"));
						//
						if (workElement.getAttribute("Type").equals("XF000")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "Script", res.getString("Script"));
						}
						//
						if (workElement.getAttribute("Type").equals("XF010")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "Script", res.getString("Script"));
						}
						//
						if (workElement.getAttribute("Type").equals("XF100")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "FixedWhere", res.getString("FixedWhere"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "InitialMsg", res.getString("InitialMessage"));
							//
							workList1 = workElement.getElementsByTagName("Column");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionColumn", res.getString("Function/Field"), "FieldOptions_CAPTION", res.getString("Caption"));
							}
							//
							workList1 = workElement.getElementsByTagName("Filter");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionFilter", res.getString("Function/Filter"), "FieldOptions_CAPTION", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionFilter", res.getString("Function/Filter"), "FieldOptions_VALUE", res.getString("InitialValue"));
							}
							//
							workList1 = workElement.getElementsByTagName("Button");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionButton", res.getString("Function/Button"), "Caption", res.getString("Caption"));
							}
						}
						//
						if (workElement.getAttribute("Type").equals("XF110")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "FixedWhere", res.getString("FixedWhere"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "InitialMsg", res.getString("InitialMessage"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "BatchRecordFunctionMsg", res.getString("BatchRecordCallMsg"));
							//
							workList1 = workElement.getElementsByTagName("Column");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionColumn", res.getString("Function/Field"), "FieldOptions_CAPTION", res.getString("Caption"));
							}
							//
							workList1 = workElement.getElementsByTagName("Filter");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionFilter", res.getString("Function/Filter"), "FieldOptions_CAPTION", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionFilter", res.getString("Function/Filter"), "FieldOptions_VALUE", res.getString("InitialValue"));
							}
							//
							workList1 = workElement.getElementsByTagName("BatchField");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionBatchField", res.getString("Function/BatchField"), "FieldOptions_CAPTION", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionBatchField", res.getString("Function/BatchField"), "FieldOptions_COMMENT", res.getString("Comment"));
							}
							//
							workList1 = workElement.getElementsByTagName("Button");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionButton", res.getString("Function/Button"), "Caption", res.getString("Caption"));
							}
						}
						//
						if (workElement.getAttribute("Type").equals("XF200")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "FixedWhere", res.getString("FixedWhere"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "InitialMsg", res.getString("InitialMessage"));
							//
							workList1 = workElement.getElementsByTagName("Field");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionField", res.getString("Function/Field"), "FieldOptions_CAPTION", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionField", res.getString("Function/Field"), "FieldOptions_COMMENT", res.getString("Comment"));
							}
							//
							workList1 = workElement.getElementsByTagName("Tab");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "Function200Tab", res.getString("Function/FieldTab"), "Caption", res.getString("Caption"));
								//
								workList2 = workElement1.getElementsByTagName("TabField");
								for (int k = 0; k < workList2.getLength(); k++) {
									workElement2 = (org.w3c.dom.Element)workList2.item(k);
									scanAttribute(workElement2, "Function200TabField", res.getString("Function/Field"), "FieldOptions_CAPTION", res.getString("Caption"));
									scanAttribute(workElement2, "Function200TabField", res.getString("Function/Field"), "FieldOptions_COMMENT", res.getString("Comment"));
								}
							}
							//
							workList1 = workElement.getElementsByTagName("Button");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionButton", res.getString("Function/Button"), "Caption", res.getString("Caption"));
							}
						}
						//
						if (workElement.getAttribute("Type").equals("XF210")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "FixedWhere", res.getString("FixedWhere"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "InitialMsg", res.getString("InitialMessage"));
							//
							workList1 = workElement.getElementsByTagName("Field");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionField", res.getString("Function/Field"), "FieldOptions_CAPTION", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionField", res.getString("Function/Field"), "FieldOptions_COMMENT", res.getString("Comment"));
							}
							//
							workList1 = workElement.getElementsByTagName("Button");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionButton", res.getString("Function/Button"), "Caption", res.getString("Caption"));
							}
						}
						//
						if (workElement.getAttribute("Type").equals("XF290")) {
							workList1 = workElement.getElementsByTagName("Phrase");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionPhrase", res.getString("Function/Phrase"), "Value", res.getString("PhraseFormula"));
							}
						}
						//
						if (workElement.getAttribute("Type").equals("XF300")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "FixedWhere", res.getString("FixedWhere"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "InitialMsg", res.getString("InitialMessage"));
							//
							workList1 = workElement.getElementsByTagName("Field");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionHeaderField", res.getString("Function/HDRField"), "FieldOptions_CAPTION", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionHeaderField", res.getString("Function/HDRField"), "FieldOptions_COMMENT", res.getString("Comment"));
							}
							//
							workList1 = workElement.getElementsByTagName("Detail");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionDetail", res.getString("Function/DTLTab"), "Caption", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionDetail", res.getString("Function/DTLTab"), "FixedWhere", res.getString("FixedWhere"));
								scanAttribute(workElement1, "FunctionDetail", res.getString("Function/DTLTab"), "InitialMsg", res.getString("InitialMessage"));
								//
								workList2 = workElement1.getElementsByTagName("Column");
								for (int k = 0; k < workList2.getLength(); k++) {
									workElement2 = (org.w3c.dom.Element)workList2.item(k);
									scanAttribute(workElement2, "Function300DetailField", res.getString("Function/DTLField"), "FieldOptions_CAPTION", res.getString("Caption"));
								}
								//
								workList2 = workElement1.getElementsByTagName("Filter");
								for (int k = 0; k < workList2.getLength(); k++) {
									workElement2 = (org.w3c.dom.Element)workList2.item(k);
									scanAttribute(workElement2, "Function300DetailFilter", res.getString("Function/Filter"), "FieldOptions_CAPTION", res.getString("Caption"));
									scanAttribute(workElement2, "Function300DetailFilter", res.getString("Function/Filter"), "FieldOptions_VALUE", res.getString("InitialValue"));
								}
								//
								workList2 = workElement1.getElementsByTagName("Button");
								for (int k = 0; k < workList2.getLength(); k++) {
									workElement2 = (org.w3c.dom.Element)workList2.item(k);
									scanAttribute(workElement2, "Function300DetailButton", res.getString("Function/DTLButton"), "Caption", res.getString("Caption"));
								}
							}
						}
						//
						if (workElement.getAttribute("Type").equals("XF310")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "HeaderFixedWhere", res.getString("FixedWhereHDR"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "DetailFixedWhere", res.getString("FixedWhereDTL"));
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "InitialMsg", res.getString("InitialMessage"));
							//
							workList1 = workElement.getElementsByTagName("Field");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionHeaderField", res.getString("Function/HDRField"), "FieldOptions_CAPTION", res.getString("Caption"));
								scanAttribute(workElement1, "FunctionHeaderField", res.getString("Function/HDRField"), "FieldOptions_COMMENT", res.getString("Comment"));
							}
							//
							workList1 = workElement.getElementsByTagName("Column");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "Function310DetailField", res.getString("Function/DTLField"), "FieldOptions_CAPTION", res.getString("Caption"));
							}
							//
							workList1 = workElement.getElementsByTagName("AddRowListColumn");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionAddRowListColumn", res.getString("Function/AddRowListField"), "FieldOptions_CAPTION", res.getString("Caption"));
							}
							//
							workList1 = workElement.getElementsByTagName("AddRowListButton");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionAddRowListButton", res.getString("Function/AddRowListButton"), "Caption", res.getString("Caption"));
							}
							//
							workList1 = workElement.getElementsByTagName("Button");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionButton", res.getString("Function/Button"), "Caption", res.getString("Caption"));
							}
						}
						//
						if (workElement.getAttribute("Type").equals("XF390")) {
							scanAttribute(workElement, "Function", res.getString("FunctionDefinition"), "DetailFixedWhere", res.getString("FixedWhereDTL"));
							//
							workList1 = workElement.getElementsByTagName("HeaderPhrase");
							for (int j = 0; j < workList1.getLength(); j++) {
								workElement1 = (org.w3c.dom.Element)workList1.item(j);
								scanAttribute(workElement1, "FunctionPhrase", res.getString("Function/HDRPhrase"), "Value", res.getString("PhraseFormula"));
							}
							//
							workList1 = workElement.getElementsByTagName("Detail");
							if (workList1.getLength() == 0) {
								workList2 = workElement.getElementsByTagName("Column");
								for (int j = 0; j < workList2.getLength(); j++) {
									workElement1 = (org.w3c.dom.Element)workList2.item(j);
									scanAttribute(workElement1, "Function390DetailField", res.getString("Function/DTLField"), "FieldOptions_CAPTION", res.getString("Caption"));
								}
							} else {
								for (int j = 0; j < workList1.getLength(); j++) {
									workElement1 = (org.w3c.dom.Element)workList1.item(j);
									scanAttribute(workElement1, "FunctionDetail", res.getString("Function/DTLTab"), "Caption", res.getString("Caption"));
									scanAttribute(workElement1, "FunctionDetail", res.getString("Function/DTLTab"), "FixedWhere", res.getString("FixedWhere"));
									//
									workList2 = workElement1.getElementsByTagName("Column");
									for (int k = 0; k < workList2.getLength(); k++) {
										workElement2 = (org.w3c.dom.Element)workList2.item(k);
										scanAttribute(workElement2, "Function390DetailField", res.getString("Function/DTLField"), "FieldOptions_CAPTION", res.getString("Caption"));
									}
								}
							}
						}
					}
				}
			}
			//
			if (countOfRows > 0) {
				jTextPaneScanDetail.setText(countOfRows + res.getString("ScanDialogComment2") + countOfStrings + res.getString("ScanDialogComment3"));
				jButtonGenerateListData.setEnabled(true);
			} else {
				jTextPaneScanDetail.setText(res.getString("ScanDialogComment4"));
				jButtonGenerateListData.setEnabled(false);
			}
		} finally {
			jProgressBar.setVisible(false);
			jButtonStartScan.setVisible(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			jTextFieldScan.requestFocusInWindow();
		}
	}

	void scanAttribute(org.w3c.dom.Element element, String elementType, String elementTypeName, String attributeType, String attributeTypeName) {
		String targetString = "";
		String keyword = "";
		//
		if (attributeType.equals("TypeOptions")) {
			attributeTypeName = res.getString("FieldKubunID");
			keyword = "KUBUN";
			targetString = frame_.getOptionValueWithKeyword(element.getAttribute(attributeType), keyword);
			if (targetString.equals("")) {
				attributeTypeName = res.getString("FieldValueList");
				keyword = "VALUES";
				targetString = frame_.getOptionValueWithKeyword(element.getAttribute(attributeType), keyword);
				if (targetString.equals("")) {
					attributeTypeName = res.getString("AutoNumberID");
					keyword = "AUTO_NUMBER";
					targetString = frame_.getOptionValueWithKeyword(element.getAttribute(attributeType), keyword);
					if (targetString.equals("")) {
						return;
					}
				}
			}
		} else {
			if (attributeType.equals("FieldOptions_CAPTION")) {
				keyword = "CAPTION";
				targetString = frame_.getOptionValueWithKeyword(element.getAttribute("FieldOptions"), keyword);
			} else {
				if (attributeType.equals("FieldOptions_COMMENT")) {
					keyword = "COMMENT";
					targetString = frame_.getOptionValueWithKeyword(element.getAttribute("FieldOptions"), keyword);
				} else {
					if (attributeType.equals("FieldOptions_VALUE")) {
						keyword = "VALUE";
						targetString = frame_.getOptionValueWithKeyword(element.getAttribute("FieldOptions"), keyword);
					} else {
						if (attributeType.equals("Descriptions") || attributeType.equals("Remarks") || attributeType.equals("Text") || attributeType.equals("Script")) {
							targetString = frame_.substringLinesWithTokenOfEOL(element.getAttribute(attributeType), "\n");
						} else {
							targetString = element.getAttribute(attributeType);
						}
					}
				}
			}
		}
		if (!jCheckBoxCaseSensitive.isSelected()) {
			targetString = targetString.toUpperCase();
		}
		//
		int scanningPosFrom = 0;
		int countOfScanned = 0;
		int workInt = 0;
		if (attributeType.equals("TypeOptions")
				|| attributeType.equals("FieldOptions_CAPTION")
				|| attributeType.equals("FieldOptions_COMMENT")
				|| attributeType.equals("FieldOptions_VALUE")) {
			do {
				workInt = scanningPosFrom;
				scanningPosFrom = targetString.indexOf(stringToBeScanned, scanningPosFrom);
				if (scanningPosFrom != -1) {
					workInt = targetString.indexOf(stringToBeScanned + "(", workInt);
					if (workInt == -1) {
						countOfScanned++;
						scanningPosFrom++;
					}
				}
			} while(scanningPosFrom != -1);
		} else {
			do {
				scanningPosFrom = targetString.indexOf(stringToBeScanned, scanningPosFrom);
				if (scanningPosFrom != -1) {
					scanningPosFrom++;
					countOfScanned++;
				}
			} while(scanningPosFrom != -1);
		}
		//
		if (countOfScanned > 0) {
			Object[] Cell = new Object[7];
			countOfRows++;
			countOfStrings = countOfStrings + countOfScanned;
			Cell[0] = new TableRowNumber(countOfRows, element, attributeType, keyword);
			Cell[1] = Boolean.FALSE;
			Cell[2] = elementTypeName;
			Cell[3] = getItemName(element, elementType);
			Cell[4] = attributeTypeName;
			if (attributeType.equals("TypeOptions")) {
				Cell[5] = frame_.getOptionValueWithKeyword(element.getAttribute(attributeType), keyword);
			} else {
				if (attributeType.equals("FieldOptions_CAPTION")
						|| attributeType.equals("FieldOptions_COMMENT")
						|| attributeType.equals("FieldOptions_VALUE")) {
					Cell[5] = frame_.getOptionValueWithKeyword(element.getAttribute("FieldOptions"), keyword);
				} else {
					if (attributeType.equals("Descriptions")
							|| attributeType.equals("Remarks")
							|| attributeType.equals("Text")
							|| attributeType.equals("Script")) {
						Cell[5] = frame_.getFirstSentence(element.getAttribute(attributeType));
					} else {
						Cell[5] = element.getAttribute(attributeType);
					}
				}
			}
			Cell[6] = Integer.toString(countOfScanned);
			tableModelScanResult.addRow(Cell);
		}
	}

	String getItemName(org.w3c.dom.Element element, String elementType) {
		NodeList workList = null;
		String wrkStr = "";
		org.w3c.dom.Element workElement1, workElement2, workElement3;
		String itemName =  elementType + "???";
		//
		if (elementType.equals("System")) {
			itemName =  element.getAttribute("Name");
		}
		//
		if (elementType.equals("Menu") || elementType.equals("Subsystem")) {
			itemName =  element.getAttribute("ID") + " " + element.getAttribute("Name");
		}
	    //
		if (elementType.equals("MenuOption")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			itemName = workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name");
		}
		//
		if (elementType.equals("Table")) {
			workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int m = 0; m < workList.getLength(); m++) {
				workElement1 = (org.w3c.dom.Element)workList.item(m);
				if (element.getAttribute("SubsystemID").equals(workElement1.getAttribute("ID"))) {
					itemName = workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
						+ element.getAttribute("ID") + " " + element.getAttribute("Name");
					break;
				}
			}
		}
	    //
	    if (elementType.equals("TableField")) {
	      workElement1 = (org.w3c.dom.Element)element.getParentNode();
	      workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
	      for (int m = 0; m < workList.getLength(); m++) {
	        workElement2 = (org.w3c.dom.Element)workList.item(m);
	        if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
	          itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
	          	+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
	          	+ element.getAttribute("ID") + " " + element.getAttribute("Name");
	          break;
	        }
	      }
	    }
	    //
	    if (elementType.equals("TableScript")) {
	      workElement1 = (org.w3c.dom.Element)element.getParentNode();
	      workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
	      for (int m = 0; m < workList.getLength(); m++) {
	        workElement2 = (org.w3c.dom.Element)workList.item(m);
	        if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
	          itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
	          	+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
	          	+ element.getAttribute("Name");
	          break;
	        }
	      }
	    }
		//
		if (elementType.equals("Function")) {
			workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int m = 0; m < workList.getLength(); m++) {
				workElement1 = (org.w3c.dom.Element)workList.item(m);
				if (element.getAttribute("SubsystemID").equals(workElement1.getAttribute("ID"))) {
					itemName = workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
						+ element.getAttribute("ID") + " " + element.getAttribute("Name");
					break;
				}
			}
		}
		//
		if (elementType.equals("FunctionField") || elementType.equals("FunctionFilter") || elementType.equals("FunctionColumn")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", workElement1.getAttribute("PrimaryTable"));
			if (tableNode != null) {
				NodeList referList = tableNode.getElement().getElementsByTagName("Refer");
				wrkStr = element.getAttribute("DataSource");
				int pos1 = wrkStr.indexOf(".");
				String tableAlias = wrkStr.substring(0, pos1);
				String fieldID = wrkStr.substring(pos1+1, wrkStr.length());
				org.w3c.dom.Element tableElement = tableNode.getElement();
				String tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
				String tableName = tableID + " " + tableElement.getAttribute("Name");
				org.w3c.dom.Element fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
				String fieldName = fieldID + " " + fieldElement.getAttribute("Name");
				//
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				for (int m = 0; m < workList.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)workList.item(m);
					if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
						itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
						+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
						+ tableName + " + " + fieldName;
						break;
					}
				}
			}
		}
		//
		if (elementType.equals("FunctionBatchField")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", workElement1.getAttribute("BatchTable"));
			if (tableNode != null) {
				NodeList referList = tableNode.getElement().getElementsByTagName("Refer");
				wrkStr = element.getAttribute("DataSource");
				int pos1 = wrkStr.indexOf(".");
				String tableAlias = wrkStr.substring(0, pos1);
				String fieldID = wrkStr.substring(pos1+1, wrkStr.length());
				org.w3c.dom.Element tableElement = tableNode.getElement();
				String tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
				String tableName = tableID + " " + tableElement.getAttribute("Name");
				org.w3c.dom.Element fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
				String fieldName = fieldID + " " + fieldElement.getAttribute("Name");
				//
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				for (int m = 0; m < workList.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)workList.item(m);
					if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
						itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
						+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
						+ tableName + " + " + fieldName;
						break;
					}
				}
			}
		}
		//
		if (elementType.equals("Function200Tab")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int m = 0; m < workList.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)workList.item(m);
				if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
					itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
					+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
					+ element.getAttribute("Caption");
					break;
				}
			}
		}
		//
		if (elementType.equals("Function200TabField")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			workElement2 = (org.w3c.dom.Element)workElement1.getParentNode();
			//
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", workElement2.getAttribute("PrimaryTable"));
			if (tableNode != null) {
				NodeList referList = tableNode.getElement().getElementsByTagName("Refer");
				wrkStr = element.getAttribute("DataSource");
				int pos1 = wrkStr.indexOf(".");
				String tableAlias = wrkStr.substring(0, pos1);
				String fieldID = wrkStr.substring(pos1+1, wrkStr.length());
				org.w3c.dom.Element tableElement = tableNode.getElement();
				String tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
				String tableName = tableID + " " + tableElement.getAttribute("Name");
				org.w3c.dom.Element fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
				String fieldName = fieldID + " " + fieldElement.getAttribute("Name");
				//
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				for (int m = 0; m < workList.getLength(); m++) {
					workElement3 = (org.w3c.dom.Element)workList.item(m);
					if (workElement2.getAttribute("SubsystemID").equals(workElement3.getAttribute("ID"))) {
						itemName = workElement3.getAttribute("ID") + " " + workElement3.getAttribute("Name") + " + "
						+ workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
						+ workElement1.getAttribute("Caption") + " + "
						+ tableName + " + " + fieldName;
						break;
					}
				}
			}
		}
		//
		if (elementType.equals("FunctionHeaderField")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", workElement1.getAttribute("HeaderTable"));
			if (tableNode != null) {
				NodeList referList = tableNode.getElement().getElementsByTagName("Refer");
				wrkStr = element.getAttribute("DataSource");
				int pos1 = wrkStr.indexOf(".");
				String tableAlias = wrkStr.substring(0, pos1);
				String fieldID = wrkStr.substring(pos1+1, wrkStr.length());
				org.w3c.dom.Element tableElement = tableNode.getElement();
				String tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
				String tableName = tableID + " " + tableElement.getAttribute("Name");
				org.w3c.dom.Element fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
				String fieldName = fieldID + " " + fieldElement.getAttribute("Name");
				//
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				for (int m = 0; m < workList.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)workList.item(m);
					if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
						itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
						+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
						+ tableName + " + " + fieldName;
						break;
					}
				}
			}
		}
		//
		if (elementType.equals("FunctionPhrase")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int m = 0; m < workList.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)workList.item(m);
				if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
					itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
					+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name");
					break;
				}
			}
		}
		//
		if (elementType.equals("FunctionDetail")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int m = 0; m < workList.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)workList.item(m);
				if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
					itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
					+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
					+ element.getAttribute("Caption");
					break;
				}
			}
		}
		//
		if (elementType.equals("Function300DetailField") || elementType.equals("Function300DetailFilter")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			workElement2 = (org.w3c.dom.Element)workElement1.getParentNode();
			//
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", workElement1.getAttribute("Table"));
			if (tableNode != null) {
				NodeList referList = tableNode.getElement().getElementsByTagName("Refer");
				wrkStr = element.getAttribute("DataSource");
				int pos1 = wrkStr.indexOf(".");
				String tableAlias = wrkStr.substring(0, pos1);
				String fieldID = wrkStr.substring(pos1+1, wrkStr.length());
				org.w3c.dom.Element tableElement = tableNode.getElement();
				String tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
				String tableName = tableID + " " + tableElement.getAttribute("Name");
				org.w3c.dom.Element fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
				String fieldName = fieldID + " " + fieldElement.getAttribute("Name");
				//
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				for (int m = 0; m < workList.getLength(); m++) {
					workElement3 = (org.w3c.dom.Element)workList.item(m);
					if (workElement2.getAttribute("SubsystemID").equals(workElement3.getAttribute("ID"))) {
						itemName = workElement3.getAttribute("ID") + " " + workElement3.getAttribute("Name") + " + "
						+ workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
						+ workElement1.getAttribute("Caption") + " + "
						+ tableName + " + " + fieldName;
						break;
					}
				}
			}
		}
		//
		if (elementType.equals("Function300DetailButton")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			workElement2 = (org.w3c.dom.Element)workElement1.getParentNode();
			//
			workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int m = 0; m < workList.getLength(); m++) {
				workElement3 = (org.w3c.dom.Element)workList.item(m);
				if (workElement2.getAttribute("SubsystemID").equals(workElement3.getAttribute("ID"))) {
					itemName = workElement3.getAttribute("ID") + " " + workElement3.getAttribute("Name") + " + "
					+ workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
					+ workElement1.getAttribute("Caption");
					break;
				}
			}
		}
		//
		if (elementType.equals("Function310DetailField") || elementType.equals("Function390DetailField")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", workElement1.getAttribute("DetailTable"));
			if (tableNode != null) {
				NodeList referList = tableNode.getElement().getElementsByTagName("Refer");
				wrkStr = element.getAttribute("DataSource");
				int pos1 = wrkStr.indexOf(".");
				String tableAlias = wrkStr.substring(0, pos1);
				String fieldID = wrkStr.substring(pos1+1, wrkStr.length());
				org.w3c.dom.Element tableElement = tableNode.getElement();
				String tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
				String tableName = tableID + " " + tableElement.getAttribute("Name");
				org.w3c.dom.Element fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
				String fieldName = fieldID + " " + fieldElement.getAttribute("Name");
				//
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				for (int m = 0; m < workList.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)workList.item(m);
					if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
						itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
						+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
						+ tableName + " + " + fieldName;
						break;
					}
				}
			}
		}
		//
		if (elementType.equals("FunctionAddRowListColumn")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			//
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", workElement1.getAttribute("AddRowListTable"));
			if (tableNode != null) {
				NodeList referList = tableNode.getElement().getElementsByTagName("Refer");
				wrkStr = element.getAttribute("DataSource");
				int pos1 = wrkStr.indexOf(".");
				String tableAlias = wrkStr.substring(0, pos1);
				String fieldID = wrkStr.substring(pos1+1, wrkStr.length());
				org.w3c.dom.Element tableElement = tableNode.getElement();
				String tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
				String tableName = tableID + " " + tableElement.getAttribute("Name");
				org.w3c.dom.Element fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
				String fieldName = fieldID + " " + fieldElement.getAttribute("Name");
				//
				workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
				for (int m = 0; m < workList.getLength(); m++) {
					workElement2 = (org.w3c.dom.Element)workList.item(m);
					if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
						itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
						+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name") + " + "
						+ tableName + " + " + fieldName;
						break;
					}
				}
			}
		}
		//
		if (elementType.equals("FunctionButton") || elementType.equals("FunctionAddRowListButton")) {
			workElement1 = (org.w3c.dom.Element)element.getParentNode();
			workList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int m = 0; m < workList.getLength(); m++) {
				workElement2 = (org.w3c.dom.Element)workList.item(m);
				if (workElement1.getAttribute("SubsystemID").equals(workElement2.getAttribute("ID"))) {
					itemName = workElement2.getAttribute("ID") + " " + workElement2.getAttribute("Name") + " + "
					+ workElement1.getAttribute("ID") + " " + workElement1.getAttribute("Name");
					break;
				}
			}
		}
		//
		return itemName;
	}

	void jTableScanResult_valueChanged(ListSelectionEvent e) {
		if (jTableScanResult.getRowCount() > 0) {
			if (jTableScanResult.getSelectedRow() > -1) {
				setSelectedRowValueToTextPane();
			} else {
				jTextPaneScanDetail.setText("");
			}
		}
	}

	void jButtonReplaceRowsSelected_actionPerformed(ActionEvent e) {
		int countOfHits = 0;
		String attributeType = "";
		String targetString = "";
		String keyword = "";
		//
		countOfRowsReplaced = 0;
		countOfStringsReplaced = 0;
		stringToBeReplaced = jTextFieldReplace.getText();
		//
		for (int i = 0; i < tableModelScanResult.getRowCount(); i++) {
			if (((Boolean)tableModelScanResult.getValueAt(i, 1)).booleanValue()) {
				//
				countOfHits = Integer.parseInt((String)tableModelScanResult.getValueAt(i, 6));
				if (countOfHits > 0) {
					//
					TableRowNumber tableRowNumber = (TableRowNumber)tableModelScanResult.getValueAt(i, 0);
					//
					org.w3c.dom.Element element = tableRowNumber.getElement();
					attributeType = tableRowNumber.getAttributeType();
					keyword = tableRowNumber.getKeyword();
					//
					if (attributeType.equals("TypeOptions")) {
						targetString = frame_.getOptionValueWithKeyword(element.getAttribute(attributeType), keyword);
					} else {
						if (attributeType.equals("FieldOptions_CAPTION")
								|| attributeType.equals("FieldOptions_COMMENT")
								|| attributeType.equals("FieldOptions_VALUE")) {
							targetString = frame_.getOptionValueWithKeyword(element.getAttribute("FieldOptions"), keyword);
						} else {
							if (attributeType.equals("Descriptions")
									|| attributeType.equals("Remarks")
									|| attributeType.equals("Text")
									|| attributeType.equals("Script")) {
								targetString = frame_.substringLinesWithTokenOfEOL(element.getAttribute(attributeType), "\n");
							} else {
								targetString = element.getAttribute(attributeType);
							}
						}
					}
					if (attributeType.equals("TypeOptions")) {
						targetString = replaceValueWithKeyword(element.getAttribute(attributeType), keyword, targetString);
					} else {
						if (attributeType.equals("FieldOptions_CAPTION")
								|| attributeType.equals("FieldOptions_COMMENT")
								|| attributeType.equals("FieldOptions_VALUE")) {
							targetString = replaceValueWithKeyword(element.getAttribute("FieldOptions"), keyword, targetString);
						} else {
							targetString = replaceStringValue(targetString);
							if (attributeType.equals("Descriptions")
									|| attributeType.equals("Remarks")
									|| attributeType.equals("Text")
									|| attributeType.equals("Script")) {
								targetString = frame_.concatLinesWithTokenOfEOL(targetString);
							}
						}
					}
					//
					countOfRowsReplaced++;
					countOfStringsReplaced = countOfStringsReplaced + countOfHits;
					element.setAttribute(attributeType, targetString);
					//
					if (attributeType.equals("TypeOptions")) {
						tableModelScanResult.setValueAt(frame_.getOptionValueWithKeyword(element.getAttribute(attributeType), keyword), i, 5);
					} else {
						if (attributeType.equals("FieldOptions_CAPTION")
								|| attributeType.equals("FieldOptions_COMMENT")
								|| attributeType.equals("FieldOptions_VALUE")) {
							tableModelScanResult.setValueAt(frame_.getOptionValueWithKeyword(element.getAttribute("FieldOptions"), keyword), i, 5);
						} else {
							if (attributeType.equals("Descriptions")
									|| attributeType.equals("Remarks")
									|| attributeType.equals("Text")
									|| attributeType.equals("Script")) {
								tableModelScanResult.setValueAt(frame_.getFirstSentence(element.getAttribute(attributeType)), i, 5);
							} else {
								tableModelScanResult.setValueAt(element.getAttribute(attributeType), i, 5);
							}
						}
					}
				}
			}
		}
		if (countOfRowsReplaced > 0) {
			updated = true;
			setSelectedRowValueToTextPane();
		}
		JOptionPane.showMessageDialog(this, countOfRowsReplaced + res.getString("ScanDialogComment5") + countOfStringsReplaced + res.getString("ScanDialogComment6"));
	}

	String replaceValueWithKeyword(String originalString, String keyword, String value) {
		StringBuffer buf = new StringBuffer();
		int pos1 = originalString.indexOf(keyword + "(");
		if (pos1 > -1) {
			int pos2 = originalString.indexOf(")", pos1);
			if (pos2 > pos1) {
				String str1 = originalString.substring(0, pos1 + keyword.length() + 1);
				String str2 = originalString.substring(pos2, originalString.length());
				buf.append(str1);
				buf.append(value);
				buf.append(str2);
			}
		}
		return buf.toString();
	}

	String replaceStringValue(String originalString) {
		StringBuffer buf = new StringBuffer();
		//
		String caseProcessedOriginalString = originalString;
		if (!jCheckBoxCaseSensitive.isSelected()) {
			caseProcessedOriginalString = originalString.toUpperCase();
		}
		String caseProcessedStringToBeScanned = stringToBeScanned;
		if (!jCheckBoxCaseSensitive.isSelected()) {
			caseProcessedStringToBeScanned = stringToBeScanned.toUpperCase();
		}
		//
		int pos1 = 0;
		int pos2 = 0;
		while (pos2 > -1) {
			pos2 = caseProcessedOriginalString.indexOf(caseProcessedStringToBeScanned, pos1);
			if (pos2 >= pos1) {
				buf.append(originalString.substring(pos1, pos2));
				buf.append(stringToBeReplaced);
				pos1 = pos2 + caseProcessedStringToBeScanned.length();
			} else {
				buf.append(originalString.substring(pos1, originalString.length()));
			}
		}
		//
		return buf.toString();
	}
	
	void setSelectedRowValueToTextPane() {
		if (jTableScanResult.getSelectedRow() > -1) {
			TableRowNumber tableRowNumber = (TableRowNumber)tableModelScanResult.getValueAt(jTableScanResult.getSelectedRow(), 0);
			org.w3c.dom.Element element = tableRowNumber.getElement();
			//
			String attrType = tableRowNumber.getAttributeType();
			String keyword = tableRowNumber.getKeyword();
			//
			String imageText = "";
			if (attrType.equals("TypeOptions")) {
				imageText = frame_.getOptionValueWithKeyword(element.getAttribute(attrType), keyword);
			} else {
				if (attrType.equals("FieldOptions_CAPTION")
						|| attrType.equals("FieldOptions_COMMENT")
						|| attrType.equals("FieldOptions_VALUE")) {
					imageText = frame_.getOptionValueWithKeyword(element.getAttribute("FieldOptions"), keyword);
				} else {
					if (attrType.equals("Descriptions")
							|| attrType.equals("Remarks")
							|| attrType.equals("Text")
							|| attrType.equals("Script")
							|| attrType.equals("LoginScript")
							|| attrType.equals("ScriptFunctions")) {
						imageText = frame_.substringLinesWithTokenOfEOL(element.getAttribute(attrType), "\n");
					} else {
						imageText = element.getAttribute(attrType);
					}
				}
			}
			//
			String workString = imageText;
			if (!jCheckBoxCaseSensitive.isSelected()) {
				workString = imageText.toUpperCase();
			}
			jTextPaneScanDetail.removeStyle("style1");
			jTextPaneScanDetail.setText(imageText);
			StyledDocument styledDocument = jTextPaneScanDetail.getStyledDocument();
			Style style1 = styledDocument.addStyle("style1", null);
			StyleConstants.setForeground(style1, Color.BLACK);
			styledDocument.setCharacterAttributes(0, 9999, style1, false);
			int scanningPosFrom = 0;
			int workInt = 0;
			if (attrType.equals("TypeOptions")
					|| attrType.equals("FieldOptions_CAPTION")
					|| attrType.equals("FieldOptions_COMMENT")
					|| attrType.equals("FieldOptions_VALUE")) {
				do {
					workInt = workString.indexOf(stringToBeScanned, scanningPosFrom);
					if (workInt == -1) {
						scanningPosFrom = -1;
					} else {
						if (imageText.indexOf(stringToBeScanned + "(", workInt) == -1) {
							doc.setCharacterAttributes(workInt, stringToBeScanned.length(), style, false);
							scanningPosFrom = workInt + stringToBeScanned.length();
						}
					}
				} while(scanningPosFrom != -1);
			} else {
				do {
					workInt = workString.indexOf(stringToBeScanned, scanningPosFrom);
					if (workInt == -1) {
						scanningPosFrom = -1;
					} else {
						doc.setCharacterAttributes(workInt, stringToBeScanned.length(), style, false);
						scanningPosFrom = workInt + stringToBeScanned.length();
					}
				} while(scanningPosFrom != -1);
			}
			doc.setParagraphAttributes(0, jTextPaneScanDetail.getDocument().getLength(), attrs, false); 
			jTextPaneScanDetail.setCaretPosition(0);
		}
	}

	void jButtonGenerateListData_actionPerformed(ActionEvent e) {
		try {
			frame_.getDesktop().browse(getExcellBookURI());
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "IOException : "+ e1.getMessage());
			e1.printStackTrace(frame_.getExceptionStream());
		}
	}

	URI getExcellBookURI() {
		Boolean selectFlag;
		File xlsFile = null;
		String xlsFileName = "";
		FileOutputStream fileOutputStream = null;
		//
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet workSheet = workBook.createSheet("Sheet1");
		workSheet.setDefaultRowHeight( (short) 300);
		HSSFFooter workSheetFooter = workSheet.getFooter();
		workSheetFooter.setRight(jTextFieldScan.getText() + "  Page " + HSSFFooter.page() + " / " + HSSFFooter.numPages() );
		//
//		HSSFFont fontHeader = workBook.createFont();
//		fontHeader = workBook.createFont();
//		fontHeader.setFontName(res.getString("XLSFontHDR"));
//		fontHeader.setFontHeightInPoints((short)11);
		//
//		HSSFFont fontDetail = workBook.createFont();
//		fontDetail.setFontName(res.getString("XLSFontDTL"));
//		fontDetail.setFontHeightInPoints((short)11);
		//
		HSSFCellStyle styleHeader = workBook.createCellStyle();
		styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//
		HSSFCellStyle styleHeaderNumber = workBook.createCellStyle();
		styleHeaderNumber.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//
		HSSFCellStyle styleDataInteger = workBook.createCellStyle();
		styleDataInteger.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleDataInteger.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		styleDataInteger.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		//
		HSSFCellStyle styleDataString = workBook.createCellStyle();
		styleDataString.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleDataString.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		styleDataString.setWrapText(true);
		styleDataString.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		//
		int currentRowNumber = -1;
		//
		try {
			File outputFolder = null;
			String wrkStr = frame_.getSystemNode().getElement().getAttribute("OutputFolder"); 
			if (wrkStr.equals("")) {
				outputFolder = null;
			} else {
				if (wrkStr.contains("<CURRENT>")) {
					wrkStr = wrkStr.replace("<CURRENT>", frame_.getCurrentFileFolder());
				}
				outputFolder = new File(wrkStr);
				if (!outputFolder.exists()) {
					outputFolder = null;
				}
			}
			//
			xlsFile = File.createTempFile("XeadEditor_ScanResult_", ".xls", outputFolder);
			if (outputFolder == null) {
				xlsFile.deleteOnExit();
			}
			xlsFileName = xlsFile.getPath();
			fileOutputStream = new FileOutputStream(xlsFileName);
			//
			currentRowNumber++;
			HSSFRow rowCaption = workSheet.createRow(currentRowNumber);
			for (int i = 0; i < tableModelScanResult.getColumnCount(); i++) {
				HSSFCell cell = rowCaption.createCell(i);
				cell.setCellStyle(styleHeaderNumber);
				if (i == 1) {
					cell.setCellValue(new HSSFRichTextString(res.getString("Sel")));
				} else {
					cell.setCellValue(new HSSFRichTextString(tableModelScanResult.getColumnName(i)));
				}
				Rectangle rect = jTableScanResult.getCellRect(0, i, true);
				workSheet.setColumnWidth(i, rect.width * 40);
			}
			//
			for (int i = 0; i < tableModelScanResult.getRowCount(); i++) {
				currentRowNumber++;
				HSSFRow rowData = workSheet.createRow(currentRowNumber);
				//
				HSSFCell cell0 = rowData.createCell(0); //Column of Sequence Number
				cell0.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell0.setCellStyle(styleDataInteger);
				cell0.setCellValue(i + 1);
				//
				for (int j = 1; j < 7; j++) {
					HSSFCell cell = rowData.createCell(j);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(styleDataString);
					if (j == 1) {
						selectFlag = (Boolean)tableModelScanResult.getValueAt(i,j);
						if (selectFlag.booleanValue()) {
							cell.setCellValue(new HSSFRichTextString("v"));
						} else {
							cell.setCellValue(new HSSFRichTextString(""));
						}
					} else {
						if (j == 6) {
							cell.setCellValue(Double.parseDouble(tableModelScanResult.getValueAt(i, j).toString()));
						} else {
							cell.setCellValue(new HSSFRichTextString(tableModelScanResult.getValueAt(i, j).toString()));
						}
					}
				}
			}
			//
			workBook.write(fileOutputStream);
			fileOutputStream.close();
			//
		} catch (Exception ex1) {
			JOptionPane.showMessageDialog(null, "Exception : "+ ex1.getMessage());
			ex1.printStackTrace(frame_.getExceptionStream());
			try {
				fileOutputStream.close();
			} catch (Exception ex2) {
				ex2.printStackTrace(frame_.getExceptionStream());
			}
		}
		//
		return xlsFile.toURI();
		
	}

	void jButtonCloseDialog_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void jTextFieldScan_keyReleased(KeyEvent e) {
		if (jTextFieldScan.getText().equals("")) {
			jButtonStartScan.setEnabled(false);
		} else {
			jButtonStartScan.setEnabled(true);
			panelMain.getRootPane().setDefaultButton(jButtonStartScan);
			jTextFieldReplace.setText(jTextFieldScan.getText());
		}
	}

	void jTextFieldReplace_keyReleased(KeyEvent e) {
		jButtonReplaceAllSelected.setEnabled(false);
		if (!stringToBeScanned.equals(jTextFieldReplace.getText().toUpperCase()) && !jCheckBoxCaseSensitive.isSelected()
				|| !stringToBeScanned.equals(jTextFieldReplace.getText()) && jCheckBoxCaseSensitive.isSelected()) {
			for (int i = 0; i < tableModelScanResult.getRowCount(); i++) {
				if (((Boolean)tableModelScanResult.getValueAt(i, 1)).booleanValue()) {
					jButtonReplaceAllSelected.setEnabled(true);
					break;
				}
			}
		}
	}

	void jTableScanResult_mouseClicked(MouseEvent e) {
		jButtonReplaceAllSelected.setEnabled(false);
		if (!stringToBeScanned.equals(jTextFieldReplace.getText().toUpperCase()) && !jCheckBoxCaseSensitive.isSelected()
				|| !stringToBeScanned.equals(jTextFieldReplace.getText()) && jCheckBoxCaseSensitive.isSelected()) {
			for (int i = 0; i < tableModelScanResult.getRowCount(); i++) {
				if (((Boolean)tableModelScanResult.getValueAt(i, 1)).booleanValue()) {
					jButtonReplaceAllSelected.setEnabled(true);
					break;
				}
			}
		}
	}

	class CheckBoxHeaderListener implements ItemListener {
		private CheckBoxHeaderRenderer rendererComponent_ = null;   
		public void setRenderer(CheckBoxHeaderRenderer rendererComponent) {
			rendererComponent_ = rendererComponent;
		}
		public void itemStateChanged(ItemEvent e) {   
			if (rendererComponent_ != null) {
				if (rendererComponent_.isSelected()) {
					for (int i = 0; i < tableModelScanResult.getRowCount(); i++) {
						tableModelScanResult.setValueAt(Boolean.TRUE, i, 1);
					}
					if (!stringToBeScanned.equals(jTextFieldReplace.getText().toUpperCase()) && !jCheckBoxCaseSensitive.isSelected()
							|| !stringToBeScanned.equals(jTextFieldReplace.getText()) && jCheckBoxCaseSensitive.isSelected()) {
						jButtonReplaceAllSelected.setEnabled(true);
					}
				} else {
					for (int i = 0; i < tableModelScanResult.getRowCount(); i++) {
						tableModelScanResult.setValueAt(Boolean.FALSE, i, 1);
					}
					jButtonReplaceAllSelected.setEnabled(false);
				}
			}
		}   
	}   

	void jCheckBoxAll_actionPerformed(ActionEvent e) {
		jCheckBoxSystem.setSelected((jCheckBoxAll.isSelected()));
		jCheckBoxMenu.setSelected((jCheckBoxAll.isSelected()));
		jCheckBoxSubsystem.setSelected((jCheckBoxAll.isSelected()));
		jCheckBoxTable.setSelected((jCheckBoxAll.isSelected()));
		jCheckBoxFunction.setSelected((jCheckBoxAll.isSelected()));
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
			return this;
		}
	}

	class TableModelScanResult extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable( int row, int col) {
			if (col != 1) {return false;} else {return true;}
		}
	}

	class TableRowNumber extends Object {
		private org.w3c.dom.Element element_;
		private int number_;
		private String attributeType_;
		private String keyword_;
		public TableRowNumber(int num, org.w3c.dom.Element elm, String attr, String kwd) {
			number_ = num;
			element_ = elm;
			attributeType_ = attr;
			keyword_ = kwd;
		}
		public String toString() {
			return Integer.toString(number_);
		}
		public org.w3c.dom.Element getElement() {
			return element_;
		}
		public String getAttributeType() {
			return attributeType_;
		}
		public String getKeyword() {
			return keyword_;
		}
	}
}

class DialogScan_jButtonStartScan_actionAdapter implements java.awt.event.ActionListener {
	DialogScan adaptee;

	DialogScan_jButtonStartScan_actionAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonStartScan_actionPerformed(e);
	}
}

class DialogScan_jButtonCloseDialog_actionAdapter implements java.awt.event.ActionListener {
	DialogScan adaptee;

	DialogScan_jButtonCloseDialog_actionAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCloseDialog_actionPerformed(e);
	}
}

class DialogScan_jButtonReplaceRowsSelected_actionAdapter implements java.awt.event.ActionListener {
	DialogScan adaptee;

	DialogScan_jButtonReplaceRowsSelected_actionAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonReplaceRowsSelected_actionPerformed(e);
	}
}

class DialogScan_jButtonGenerateListData_actionAdapter implements java.awt.event.ActionListener {
	DialogScan adaptee;

	DialogScan_jButtonGenerateListData_actionAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonGenerateListData_actionPerformed(e);
	}
}

class DialogScan_jCheckBoxAll_actionAdapter implements java.awt.event.ActionListener {
	DialogScan adaptee;

	DialogScan_jCheckBoxAll_actionAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jCheckBoxAll_actionPerformed(e);
	}
}

class DialogScan_jTextFieldScan_keyAdapter extends java.awt.event.KeyAdapter {
	DialogScan adaptee;

	DialogScan_jTextFieldScan_keyAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.jTextFieldScan_keyReleased(e);
	}
}

class DialogScan_jTextFieldReplace_keyAdapter extends java.awt.event.KeyAdapter {
	DialogScan adaptee;

	DialogScan_jTextFieldReplace_keyAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.jTextFieldReplace_keyReleased(e);
	}
}

class DialogScan_jCheckBoxSubsystem_changeAdapter  implements ChangeListener {
	DialogScan adaptee;
	DialogScan_jCheckBoxSubsystem_changeAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void stateChanged(ChangeEvent e) {
		adaptee.jCheckBoxSubsystem_stateChanged(e);
	}
}

class DialogScan_jCheckBoxTable_changeAdapter  implements ChangeListener {
	DialogScan adaptee;
	DialogScan_jCheckBoxTable_changeAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void stateChanged(ChangeEvent e) {
		adaptee.jCheckBoxSubsystem_stateChanged(e);
	}
}

class DialogScan_jCheckBoxFunction_changeAdapter  implements ChangeListener {
	DialogScan adaptee;
	DialogScan_jCheckBoxFunction_changeAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void stateChanged(ChangeEvent e) {
		adaptee.jCheckBoxSubsystem_stateChanged(e);
	}
}

class DialogScan_jTableScanResult_listSelectionAdapter  implements ListSelectionListener {
	DialogScan adaptee;
	DialogScan_jTableScanResult_listSelectionAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void valueChanged(ListSelectionEvent e) {
		adaptee.jTableScanResult_valueChanged(e);
	}
}

class DialogScan_jTableScanResult_mouseAdapter extends java.awt.event.MouseAdapter {
	DialogScan adaptee;
	DialogScan_jTableScanResult_mouseAdapter(DialogScan adaptee) {
		this.adaptee = adaptee;
	}
	public void mouseClicked(MouseEvent e) {
		adaptee.jTableScanResult_mouseClicked(e);
	}
}

