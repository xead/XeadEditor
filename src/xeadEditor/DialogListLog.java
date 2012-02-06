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
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.w3c.dom.*;

import xeadEditor.Editor.MainTreeNode;
import xeadEditor.Editor.SortableDomElementListModel;

import java.io.*;
import java.net.URI;

public class DialogListLog extends JDialog {
	private static final long serialVersionUID = 1L;
	static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	Editor frame_;
	BorderLayout borderLayoutMain = new BorderLayout();
	JPanel panelMain = new JPanel();
	JPanel jPanelNorth = new JPanel();
	JPanel jPanelSouth = new JPanel();
	JPanel jPanelNorth1 = new JPanel();
	JScrollPane jScrollPaneNorth2 = new JScrollPane();
	JPanel jPanelKeyFields = new JPanel();
	JPanel jPanelNorth3 = new JPanel();
	JSplitPane jSplitPane = new JSplitPane();
	JScrollPane jScrollPaneScanResult = new JScrollPane();
	JScrollPane jScrollPaneScanDetail = new JScrollPane();
	JLabel jLabelTableID = new JLabel();
	JTextField jTextFieldTableID = new JTextField();
	JLabel jLabelTableName = new JLabel();
	JTextField jTextFieldTableName = new JTextField();
	JLabel jLabelListingOperation = new JLabel();
	JCheckBox jCheckBoxSelect = new JCheckBox();
	JCheckBox jCheckBoxInsert = new JCheckBox();
	JCheckBox jCheckBoxUpdate = new JCheckBox();
	JCheckBox jCheckBoxDelete = new JCheckBox();
	TableModelReadOnlyList tableModelScanResult = new TableModelReadOnlyList();
	JTable jTableScanResult = new JTable(tableModelScanResult);
	TableColumn column0, column1, column2, column3, column4, column5, column6;
	DefaultTableCellRenderer rendererTableHeader = null;
	DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	DefaultTableCellRenderer rendererAlignmentRight = new DefaultTableCellRenderer();
	DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	JTextArea jTextAreaScanDetail = new JTextArea();
	JButton jButtonStartScan = new JButton();
	JProgressBar jProgressBar = new JProgressBar();
	JButton jButtonCloseDialog = new JButton();
	JButton jButtonGenerateListData = new JButton();
	int countOfRows = 0;
	int countOfStrings = 0;
	int countOfRowsReplaced = 0;
	int countOfStringsReplaced = 0;
	JOptionPane jOptionPane = new JOptionPane();
	String stringToBeScanned = "";
	String stringToBeReplaced = "";
	ArrayList<Editor_EditableTableField> keyFieldList = new ArrayList<Editor_EditableTableField>();

	public DialogListLog(Editor frame, String title, boolean modal) {
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

	public DialogListLog(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		//
		panelMain.setLayout(borderLayoutMain);
		panelMain.setPreferredSize(new Dimension(920, 570));
		panelMain.setBorder(null);
		panelMain.add(jPanelNorth, BorderLayout.NORTH);
		panelMain.add(jPanelSouth, BorderLayout.SOUTH);
		panelMain.add(jSplitPane, BorderLayout.CENTER);
		//
		jPanelNorth.setBorder(null);
		jPanelNorth.setPreferredSize(new Dimension(800, 129));
		jPanelNorth.setLayout(new BorderLayout());
		jPanelNorth1.setPreferredSize(new Dimension(800, 38));
		jPanelNorth1.setLayout(null);
		jLabelTableID.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelTableID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelTableID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelTableID.setText(res.getString("TableID"));
		jLabelTableID.setBounds(new Rectangle(11, 11, 96, 15));
		jTextFieldTableID.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTextFieldTableID.setBounds(new Rectangle(115, 8, 80, 22));
		jTextFieldTableID.setEditable(false);
		jLabelTableName.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelTableName.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelTableName.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelTableName.setText(res.getString("TableName"));
		jLabelTableName.setBounds(new Rectangle(211, 11, 96, 15));
		jTextFieldTableName.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTextFieldTableName.setBounds(new Rectangle(315, 8, 250, 22));
		jTextFieldTableName.setEditable(false);
		jButtonStartScan.setBounds(new Rectangle(650, 5, 150, 28));
		jButtonStartScan.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonStartScan.setText(res.getString("RetrieveLog"));
		jButtonStartScan.addActionListener(new DialogListLog_jButtonStartScan_actionAdapter(this));
		jButtonStartScan.setFocusCycleRoot(true);
		jProgressBar.setBounds(new Rectangle(650, 5, 150, 28));
		jProgressBar.setVisible(false);
		jScrollPaneNorth2.setPreferredSize(new Dimension(800, 38));
		jScrollPaneNorth2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPaneNorth2.getViewport().add(jPanelKeyFields, null);
		jPanelNorth3.setPreferredSize(new Dimension(800, 38));
		jPanelNorth3.setLayout(null);
		jPanelKeyFields.setLayout(null);
		jLabelListingOperation.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelListingOperation.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelListingOperation.setText(res.getString("ListingOperations"));
		jLabelListingOperation.setBounds(new Rectangle(8, 11, 90, 15));
		jCheckBoxSelect.setBounds(new Rectangle(110, 8, 125, 22));
		jCheckBoxSelect.setFont(new java.awt.Font("Dialog", 0, 12));
		jCheckBoxSelect.setText("SELECT");
		jCheckBoxSelect.setSelected(true);
		jCheckBoxInsert.setBounds(new Rectangle(235, 8, 125, 22));
		jCheckBoxInsert.setFont(new java.awt.Font("Dialog", 0, 12));
		jCheckBoxInsert.setText("INSERT");
		jCheckBoxInsert.setSelected(true);
		jCheckBoxUpdate.setBounds(new Rectangle(360, 8, 125, 22));
		jCheckBoxUpdate.setFont(new java.awt.Font("Dialog", 0, 12));
		jCheckBoxUpdate.setText("UPDATE");
		jCheckBoxUpdate.setSelected(true);
		jCheckBoxDelete.setBounds(new Rectangle(485, 8, 125, 22));
		jCheckBoxDelete.setFont(new java.awt.Font("Dialog", 0, 12));
		jCheckBoxDelete.setText("DELETE");
		jCheckBoxDelete.setSelected(true);
		//
		jPanelNorth.add(jPanelNorth1, BorderLayout.NORTH);
		jPanelNorth.add(jScrollPaneNorth2, BorderLayout.CENTER);
		jPanelNorth.add(jPanelNorth3, BorderLayout.SOUTH);
		jPanelNorth1.add(jLabelTableID);
		jPanelNorth1.add(jTextFieldTableID);
		jPanelNorth1.add(jLabelTableName);
		jPanelNorth1.add(jTextFieldTableName);
		jPanelNorth1.add(jButtonStartScan);
		jPanelNorth1.add(jProgressBar);
		jPanelNorth3.add(jLabelListingOperation);
		jPanelNorth3.add(jCheckBoxSelect);
		jPanelNorth3.add(jCheckBoxInsert);
		jPanelNorth3.add(jCheckBoxUpdate);
		jPanelNorth3.add(jCheckBoxDelete);
		//
		jSplitPane.setBorder(null);
		jSplitPane.setBounds(new Rectangle(11, 244, 380, 58));
		jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane.setDividerLocation(300);
		jSplitPane.setPreferredSize(new Dimension(750, 300));
		jScrollPaneScanDetail.setBounds(new Rectangle(132, 144, 282, 51));
		jSplitPane.add(jScrollPaneScanResult, JSplitPane.TOP);
		jSplitPane.add(jScrollPaneScanDetail, JSplitPane.BOTTOM);
		jTableScanResult.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTableScanResult.setBackground(SystemColor.control);
		jTableScanResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableScanResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableScanResult.setRowSelectionAllowed(true);
		jTableScanResult.getSelectionModel().addListSelectionListener(new DialogListLog_jTableScanResult_listSelectionAdapter(this));
		tableModelScanResult.addColumn("NO.");
		tableModelScanResult.addColumn(res.getString("SessionNo"));
		tableModelScanResult.addColumn(res.getString("UserID"));
		tableModelScanResult.addColumn(res.getString("LoginTime"));
		tableModelScanResult.addColumn(res.getString("FunctionID"));
		tableModelScanResult.addColumn(res.getString("Result"));
		tableModelScanResult.addColumn(res.getString("TableOperation"));
		column0 = jTableScanResult.getColumnModel().getColumn(0);
		column1 = jTableScanResult.getColumnModel().getColumn(1);
		column2 = jTableScanResult.getColumnModel().getColumn(2);
		column3 = jTableScanResult.getColumnModel().getColumn(3);
		column4 = jTableScanResult.getColumnModel().getColumn(4);
		column5 = jTableScanResult.getColumnModel().getColumn(5);
		column6 = jTableScanResult.getColumnModel().getColumn(6);
		column0.setPreferredWidth(34);
		column1.setPreferredWidth(90);
		column2.setPreferredWidth(70);
		column3.setPreferredWidth(150);
		column4.setPreferredWidth(70);
		column5.setPreferredWidth(50);
		column6.setPreferredWidth(420);
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentRight.setHorizontalAlignment(4); //RIGHT//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		column4.setCellRenderer(rendererAlignmentLeft);
		column5.setCellRenderer(rendererAlignmentLeft);
		column6.setCellRenderer(rendererAlignmentLeft);
		jTableScanResult.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, 12));
		rendererTableHeader = (DefaultTableCellRenderer)jTableScanResult.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		jScrollPaneScanResult.getViewport().add(jTableScanResult, null);
		jTextAreaScanDetail.setFont(new java.awt.Font("SansSerif", 0, 14));
		jTextAreaScanDetail.setLineWrap(true);
		jTextAreaScanDetail.setEditable(false);
		jTextAreaScanDetail.setEditable(false);
		jTextAreaScanDetail.setBackground(SystemColor.control);
		jScrollPaneScanDetail.getViewport().add(jTextAreaScanDetail, null);
		//
		jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
		jPanelSouth.setPreferredSize(new Dimension(800, 40));
		jButtonCloseDialog.setBounds(new Rectangle(20, 7, 70, 25));
		jButtonCloseDialog.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogListLog_jButtonCloseDialog_actionAdapter(this));
		jButtonGenerateListData.setBounds(new Rectangle(760, 7, 90, 25));
		jButtonGenerateListData.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonGenerateListData.setText(res.getString("GenerateList"));
		jButtonGenerateListData.setEnabled(false);
		jButtonGenerateListData.addActionListener(new DialogListLog_jButtonGenerateListData_actionAdapter(this));
		jPanelSouth.setLayout(null);
		jPanelSouth.add(jButtonGenerateListData);
		jPanelSouth.add(jButtonCloseDialog);
		//
		this.setResizable(false);
		this.setTitle(res.getString("ListLogTitle"));
		this.getContentPane().add(panelMain);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public void request(org.w3c.dom.Element tableElement, HashMap<String, Object> columnMap, boolean isCaptionWithName) {
		org.w3c.dom.Element element1, element2;
		NodeList nodeList1, nodeList2;
		String fieldID;
		int digits, columnWidth, posX;
		Editor_EditableTableField editableField;
		Dimension dim;
		JLabel label = new JLabel();
		FontMetrics metrics = label.getFontMetrics(new java.awt.Font("SansSerif", 0, 12));
		StringTokenizer workTokenizer;
		//
		jTextFieldTableID.setText(tableElement.getAttribute("ID"));
		jTextFieldTableName.setText(tableElement.getAttribute("Name"));
		//
		posX = 8;
		jPanelKeyFields.removeAll();
		keyFieldList.clear();
		nodeList1 = tableElement.getElementsByTagName("Key");
		nodeList2 = tableElement.getElementsByTagName("Field");
		//
		SortableDomElementListModel sortingList = frame_.getSortedListModel(nodeList1, "Type");
	    for (int i = 0; i < sortingList.getSize(); i++) {
	    	//
	        element1 = (org.w3c.dom.Element)sortingList.elementAt(i);
	        if (element1.getAttribute("Type").equals("PK") || element1.getAttribute("Type").equals("SK")) {
	        	//
				workTokenizer = new StringTokenizer(element1.getAttribute("Fields"), ";");
				while (workTokenizer.hasMoreTokens()) {
					//
					fieldID = workTokenizer.nextToken();
					//
				    for (int j = 0; j < nodeList2.getLength(); j++) {
				    	//
				        element2 = (org.w3c.dom.Element)nodeList2.item(j);
			        	if (element2.getAttribute("ID").equals(fieldID)) {
			        		//
			        		if (element2.getAttribute("Size").equals("")) {
			        			digits = 5;
			        		} else {
			        			digits = Integer.parseInt(element2.getAttribute("Size"));
			        		}
			        		if (frame_.getOptionList(element2.getAttribute("TypeOptions")).contains("KANJI")) {
			        			columnWidth = digits * 12 + 10;
			        		} else {
			        			columnWidth = digits * 6 + 10;
			        		}
			        		if (columnWidth > 200) {
			        			columnWidth = 200;
			        		}
			        		if (columnWidth < metrics.stringWidth(element2.getAttribute("Name")) + 15) {
			        			columnWidth = metrics.stringWidth(element2.getAttribute("Name")) + 15;
			        		}
			        		if (columnWidth < metrics.stringWidth(element2.getAttribute("ID")) + 15) {
			        			columnWidth = metrics.stringWidth(element2.getAttribute("ID")) + 15;
			        		}
			        		//
			        		editableField = new Editor_EditableTableField(element2, frame_);
			        		keyFieldList.add(editableField);
			        		//
			        		dim = editableField.getPreferredSize();
			        		editableField.setBounds(posX, 8, dim.width, dim.height);
			        		editableField.setEnabled(true);
			        		editableField.setEditable(true);
			        		if (columnMap != null) {
			        			editableField.setValue(columnMap.get(editableField.getFieldID()));
			        		}
			        		if (!isCaptionWithName) {
			        			editableField.setCaption(fieldID);
			        		}
			    	        if (element1.getAttribute("Type").equals("PK")) {
			    	        	editableField.setKey(true);
			    	        }
			        		posX = posX + dim.width + 3;
			        		jPanelKeyFields.add(editableField, null);
			        	}
				    }
				}
	        }
		}
		//
		jButtonGenerateListData.setEnabled(false);
		if (tableModelScanResult.getRowCount() > 0) {
			int rowCount = tableModelScanResult.getRowCount();
			for (int i = 0; i < rowCount; i++) {tableModelScanResult.removeRow(0);}
		}
		//
		jTextAreaScanDetail.setText(res.getString("ListLogComment"));
		//
		jButtonGenerateListData.setEnabled(false);
		jButtonStartScan.requestFocusInWindow();
		//
		super.setVisible(true);
	}

	void jButtonStartScan_actionPerformed(ActionEvent e) {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			jButtonGenerateListData.setEnabled(false);
			if (tableModelScanResult.getRowCount() > 0) {
				int rowCount = tableModelScanResult.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelScanResult.removeRow(0);}
			}
			//
			int count = 0;
			String logString;
			StringTokenizer workTokenizer;
			ResultSet resultHeader, resultDetail;
			String tableID;
			MainTreeNode tableNode;
			Connection connection;
			//
			tableID = frame_.getSystemNode().getElement().getAttribute("SessionTable");
			tableNode = frame_.getSpecificXETreeNode("Table", tableID);
			connection = frame_.getDatabaseConnList().get(frame_.getDatabaseIDList().indexOf(tableNode.getElement().getAttribute("DB")));
			Statement statementHeader = connection.createStatement();
			//
			tableID = frame_.getSystemNode().getElement().getAttribute("SessionDetailTable");
			tableNode = frame_.getSpecificXETreeNode("Table", tableID);
			connection = frame_.getDatabaseConnList().get(frame_.getDatabaseIDList().indexOf(tableNode.getElement().getAttribute("DB")));
			Statement statementDetail = connection.createStatement();
			//
			StringBuffer buf = new StringBuffer();
			buf.append("select * from ");
			buf.append(frame_.getSystemNode().getElement().getAttribute("SessionTable"));
			buf.append(" order by NRSESSION ");
			resultHeader = statementHeader.executeQuery(buf.toString());
			//
			jProgressBar.setValue(0);
			jProgressBar.setMaximum(resultHeader.getRow());
			jProgressBar.setVisible(true);
			jButtonStartScan.setVisible(false);
			//
			while (resultHeader.next()) {
				//
				jProgressBar.setValue(jProgressBar.getValue() + 1);
				jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
				//
				buf = new StringBuffer();
				buf.append("select * from ");
				buf.append(frame_.getSystemNode().getElement().getAttribute("SessionDetailTable"));
				buf.append(" where NRSESSION = '");
				buf.append(resultHeader.getString("NRSESSION"));
				buf.append("'");
				resultDetail = statementDetail.executeQuery(buf.toString());
				while (resultDetail.next()) {
					//
					if (resultDetail.getString("TXERRORLOG") != null) {
						workTokenizer = new StringTokenizer(resultDetail.getString("TXERRORLOG"), "\n");
						while (workTokenizer.hasMoreTokens()) {
							//
							logString = workTokenizer.nextToken();
							if (isTargetLog(logString)) {
								count++;
								Object[] Cell = new Object[7];
								Cell[0] = count;
								Cell[1] = resultHeader.getString("NRSESSION");
								Cell[2] = resultHeader.getString("IDUSER");
								Cell[3] = resultHeader.getString("DTLOGIN");
								Cell[4] = resultDetail.getString("IDPROGRAM");
								Cell[5] = resultDetail.getString("KBPROGRAMSTATUS");
								Cell[6] = logString.substring(2);
								tableModelScanResult.addRow(Cell);
							}
						}
					}
				}
				resultDetail.close();
			}
			//
			resultHeader.close();
			//
			if (count > 0) {
				jTextAreaScanDetail.setText(count + res.getString("ListLogMessage1"));
				jButtonGenerateListData.setEnabled(true);
			} else {
				jTextAreaScanDetail.setText(res.getString("ListLogMessage2"));
			}
			//
		} catch (SQLException ex) {
			jTextAreaScanDetail.setText("SQLException :"+ ex.getMessage());
		} finally {
			jProgressBar.setVisible(false);
			jButtonStartScan.setVisible(true);
			jButtonStartScan.requestFocusInWindow();
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	boolean isTargetLog(String logString) {
		boolean isTargetLog = false;
		//
		if (jCheckBoxSelect.isSelected()
				&& logString.contains("> select ")
				&& logString.contains(" from " + jTextFieldTableID.getText() + " ")) {
			isTargetLog = isValidatedWithKey(getKeyValueMapInLog(logString ,"select"));
		}
		if (jCheckBoxInsert.isSelected()
				&& logString.contains("> insert into " + jTextFieldTableID.getText() + " ")) {
			isTargetLog = isValidatedWithKey(getKeyValueMapInLog(logString ,"insert"));
		}
		if (jCheckBoxUpdate.isSelected()
				&& logString.contains("> update " + jTextFieldTableID.getText() + " ")) {
			isTargetLog = isValidatedWithKey(getKeyValueMapInLog(logString ,"update"));
		}
		if (jCheckBoxDelete.isSelected()
				&& logString.contains("> delete from " + jTextFieldTableID.getText() + " ")) {
			isTargetLog = isValidatedWithKey(getKeyValueMapInLog(logString ,"delete"));
		}
		//
		return isTargetLog;
	}
	
	HashMap<String, Object> getKeyValueMapInLog(String logString, String operation) {
		HashMap<String, Object> keyValueMap = new HashMap<String, Object>();
		int pos1, pos2, pos3;
		String text1, text2, wrkStr1, wrkStr2;
		StringTokenizer workTokenizer1, workTokenizer2;
		//
		if (operation.equals("select") || operation.equals("update") || operation.equals("delete")) {
			pos1 = logString.indexOf(" where ");
			if (pos1 > -1) {
				pos2 = logString.indexOf(" order by ");
				if (pos2 > pos1) {
					text1 = logString.substring(pos1 + 7, pos2);
				} else {
					pos2 = logString.indexOf("(", pos1);
					if (pos2 > -1) {
						text1 = logString.substring(pos1 + 7, pos2);
					} else {
						text1 = logString.substring(pos1 + 7);
					}
				}
//				workTokenizer1 = new StringTokenizer(text1, " and ");
//				JOptionPane.showMessageDialog(null, text1 + " : " + workTokenizer1.countTokens());
//				while (workTokenizer1.hasMoreTokens()) {
//					wrkStr = workTokenizer1.nextToken();
//					workTokenizer2 = new StringTokenizer(wrkStr, "=");
//					if (workTokenizer2.countTokens() == 2) {
//						wrkStr1 = workTokenizer2.nextToken().trim();
//						wrkStr2 = workTokenizer2.nextToken().trim().replace("\"", "");
//						keyValueMap.put(wrkStr1, wrkStr2);
//					}
//				}
				String[] keyAry = text1.split(" and ");
			    for (int i=0; i < keyAry.length; i++) {
					String[] valueAry = keyAry[i].split("=");
					if (valueAry.length == 2) {
						keyValueMap.put(valueAry[0].trim(), valueAry[1].replace("\"", "").trim());
					}
			    }
			}
		}
		//
		if (operation.equals("insert")) {
			pos1 = logString.indexOf("(");
			if (pos1 > -1) {
				pos2 = logString.indexOf(") values(", pos1 + 1);
				if (pos2 > -1) {
					pos3 = logString.indexOf(")", pos2 + 9);
					if (pos3 > -1) {
						text1 = logString.substring(pos1 + 1, pos2);
						text2 = logString.substring(pos2 + 9, pos3);
						workTokenizer1 = new StringTokenizer(text1, ",");
						workTokenizer2 = new StringTokenizer(text2, ",");
						if (workTokenizer1.countTokens() == workTokenizer2.countTokens()) {
							while (workTokenizer1.hasMoreTokens()) {
								wrkStr1 = workTokenizer1.nextToken().trim();
								wrkStr2 = workTokenizer2.nextToken().trim().replace("\"", "");
								keyValueMap.put(wrkStr1, wrkStr2);
							}
						}
					}
				}
			}
		}
		//
		return keyValueMap;
	}
	
	boolean isValidatedWithKey(HashMap<String, Object> keyValueMapInLog) {
		Object keyValueInLog;
		int countOfFieldIDContained = 0;
		int countOfFieldValueMatched = 0;
		//
		for (int i = 0; i < keyFieldList.size(); i++) {
			keyValueInLog = keyValueMapInLog.get(keyFieldList.get(i).getFieldID());
			if (keyValueInLog != null) {
				countOfFieldIDContained++;
				//
				if (keyFieldList.get(i).isNull()) {
					countOfFieldValueMatched++;
				} else {
					if (keyValueInLog.equals(keyFieldList.get(i).getInternalValue())) {
						countOfFieldValueMatched++;
					}
				}
			}
		}
		//
		if (countOfFieldIDContained > 0 && countOfFieldIDContained == countOfFieldValueMatched) {
			return true;
		} else {
			return false;
		}
	}

	void jTableScanResult_valueChanged(ListSelectionEvent e) {
		if (jTableScanResult.getRowCount() > 0) {
			if (jTableScanResult.getSelectedRow() > -1) {
				setSelectedRowValueToTextPane();
			} else {
				jTextAreaScanDetail.setText("");
			}
		}
	}
	
	void setSelectedRowValueToTextPane() {
		if (jTableScanResult.getSelectedRow() > -1) {
			jTextAreaScanDetail.setText((String)tableModelScanResult.getValueAt(jTableScanResult.getSelectedRow(), 6));
			jTextAreaScanDetail.setCaretPosition(0);
		}
	}

	void jButtonGenerateListData_actionPerformed(ActionEvent e) {
		try {
			frame_.getDesktop().browse(getExcellBookURI());
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "IOException :");
			e1.printStackTrace(frame_.getExceptionStream());
		}
	}
	
	URI getExcellBookURI() {
		File xlsFile = null;
		String xlsFileName = "";
		FileOutputStream fileOutputStream = null;
		//
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet workSheet = workBook.createSheet(jTextFieldTableID.getText());
		workSheet.setDefaultRowHeight( (short) 300);
		HSSFFooter workSheetFooter = workSheet.getFooter();
		workSheetFooter.setRight(jTextFieldTableID.getText() + "  Page " + HSSFFooter.page() + " / " + HSSFFooter.numPages() );
		//
		HSSFFont fontHeader = workBook.createFont();
		fontHeader = workBook.createFont();
		fontHeader.setFontName(res.getString("XLSFontHDR"));
		fontHeader.setFontHeightInPoints((short)11);
		//
		HSSFFont fontDetail = workBook.createFont();
		fontDetail.setFontName(res.getString("XLSFontDTL"));
		fontDetail.setFontHeightInPoints((short)11);
		//
		HSSFCellStyle styleHeader = workBook.createCellStyle();
		styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHeader.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleHeader.setFont(fontHeader);
		//
		HSSFCellStyle styleHeaderNumber = workBook.createCellStyle();
		styleHeaderNumber.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleHeaderNumber.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleHeaderNumber.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleHeaderNumber.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleHeaderNumber.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleHeaderNumber.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHeaderNumber.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleHeaderNumber.setFont(fontHeader);
		//
		HSSFCellStyle styleDataInteger = workBook.createCellStyle();
		styleDataInteger.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleDataInteger.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleDataInteger.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleDataInteger.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleDataInteger.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleDataInteger.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		styleDataInteger.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		//
		HSSFCellStyle styleDataString = workBook.createCellStyle();
		styleDataString.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleDataString.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleDataString.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleDataString.setBorderTop(HSSFCellStyle.BORDER_THIN);
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
			xlsFile = File.createTempFile("XeadEditor_" + jTextFieldTableID.getText() + "_AuditLog_", ".xls", outputFolder);
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
				cell.setCellValue(new HSSFRichTextString(tableModelScanResult.getColumnName(i)));
				Rectangle rect = jTableScanResult.getCellRect(0, i, true);
				if (i == 6) {
					workSheet.setColumnWidth(i, rect.width * 80);
				} else {
					workSheet.setColumnWidth(i, rect.width * 40);
				}
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
					cell.setCellValue(new HSSFRichTextString(tableModelScanResult.getValueAt(i, j).toString()));
				}
			}
			//
			workBook.write(fileOutputStream);
			fileOutputStream.close();
			//
		} catch (Exception ex1) {
			JOptionPane.showMessageDialog(null, "Exception :");
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

	class TableModelReadOnlyList extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {return false;}
	}
}

class DialogListLog_jButtonStartScan_actionAdapter implements java.awt.event.ActionListener {
	DialogListLog adaptee;

	DialogListLog_jButtonStartScan_actionAdapter(DialogListLog adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonStartScan_actionPerformed(e);
	}
}

class DialogListLog_jButtonCloseDialog_actionAdapter implements java.awt.event.ActionListener {
	DialogListLog adaptee;

	DialogListLog_jButtonCloseDialog_actionAdapter(DialogListLog adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCloseDialog_actionPerformed(e);
	}
}

class DialogListLog_jButtonGenerateListData_actionAdapter implements java.awt.event.ActionListener {
	DialogListLog adaptee;

	DialogListLog_jButtonGenerateListData_actionAdapter(DialogListLog adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonGenerateListData_actionPerformed(e);
	}
}

class DialogListLog_jTableScanResult_listSelectionAdapter  implements ListSelectionListener {
	DialogListLog adaptee;
	DialogListLog_jTableScanResult_listSelectionAdapter(DialogListLog adaptee) {
		this.adaptee = adaptee;
	}
	public void valueChanged(ListSelectionEvent e) {
		adaptee.jTableScanResult_valueChanged(e);
	}
}