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

import javax.swing.*;
import javax.swing.table.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.*;

import xeadEditor.Editor.SortableDomElementListModel;

import java.io.*;
import java.net.URI;

public class DialogScanField extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private Editor frame_;
	private BorderLayout borderLayoutMain = new BorderLayout();
	private JPanel panelMain = new JPanel();
	private JPanel jPanelNorth = new JPanel();
	private JPanel jPanelSouth = new JPanel();
	private JLabel jLabelFieldID = new JLabel();
	private JTextField jTextFieldFieldID = new JTextField();
	private JLabel jLabelFieldName = new JLabel();
	private JTextField jTextFieldFieldName = new JTextField();
	private JButton jButtonScan = new JButton();
	private JScrollPane jScrollPaneScanResult = new JScrollPane();
	private TableModelReadOnlyList tableModelScanResult = new TableModelReadOnlyList();
	private JTable jTableScanResult = new JTable(tableModelScanResult);
	private TableColumn column0, column1, column2, column3, column4, column5, column6, column7, column8;
	private DefaultTableCellRenderer rendererTableHeader = null;
	private DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private JPanel jPanelButtons = new JPanel();
	private JButton jButtonCloseDialog = new JButton();
	private JButton jButtonGenerateListData = new JButton();
	private JProgressBar jProgressBar = new JProgressBar();
	private ArrayList<String> fieldIDList = new ArrayList<String>();
	private ArrayList<String> fieldNameList = new ArrayList<String>();

	public DialogScanField(Editor frame, String title, boolean modal) {
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

	public DialogScanField(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		panelMain.setBorder(null);
		panelMain.setLayout(borderLayoutMain);
		panelMain.add(jPanelNorth, BorderLayout.NORTH);
		panelMain.add(jPanelSouth, BorderLayout.SOUTH);
		panelMain.add(jScrollPaneScanResult, BorderLayout.CENTER);
		//
		//jPanelNorth and objects on it
		jPanelNorth.setBorder(null);
		jPanelNorth.setPreferredSize(new Dimension(100, 45));
		jLabelFieldID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelFieldID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelFieldID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelFieldID.setText(res.getString("FieldID"));
		jLabelFieldID.setBounds(new Rectangle(5, 9, 120, 28));
		jTextFieldFieldID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldFieldID.setBounds(new Rectangle(130, 9, 300, 28));
		jLabelFieldName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelFieldName.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelFieldName.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelFieldName.setText(res.getString("FieldName"));
		jLabelFieldName.setBounds(new Rectangle(440, 9, 120, 28));
		jTextFieldFieldName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldFieldName.setBounds(new Rectangle(565, 9, 300, 28));
		jButtonScan.setBounds(new Rectangle(880, 9, 120, 28));
		jButtonScan.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonScan.setText(res.getString("ScanStart"));
		jButtonScan.addActionListener(new DialogScanField_jButtonScan_actionAdapter(this));
		jProgressBar.setBounds(new Rectangle(880, 9, 120, 28));
		jProgressBar.setVisible(false);
		jPanelNorth.setLayout(null);
		jPanelNorth.add(jLabelFieldID);
		jPanelNorth.add(jTextFieldFieldID);
		jPanelNorth.add(jLabelFieldName);
		jPanelNorth.add(jTextFieldFieldName);
		jPanelNorth.add(jButtonScan);
		jPanelNorth.add(jProgressBar);
		//
		jTableScanResult.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTableScanResult.setBackground(SystemColor.control);
		jTableScanResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableScanResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableScanResult.setRowSelectionAllowed(true);
		jTableScanResult.setRowHeight(Editor.TABLE_ROW_HEIGHT);
		tableModelScanResult.addColumn("No.");
		tableModelScanResult.addColumn(res.getString("SubsystemName"));
		tableModelScanResult.addColumn(res.getString("TableID"));
		tableModelScanResult.addColumn(res.getString("TableName"));
		tableModelScanResult.addColumn(res.getString("FieldID"));
		tableModelScanResult.addColumn(res.getString("FieldName"));
		tableModelScanResult.addColumn(res.getString("FieldType"));
		tableModelScanResult.addColumn(res.getString("FieldExtType"));
		tableModelScanResult.addColumn(res.getString("Remarks"));
		column0 = jTableScanResult.getColumnModel().getColumn(0);
		column1 = jTableScanResult.getColumnModel().getColumn(1);
		column2 = jTableScanResult.getColumnModel().getColumn(2);
		column3 = jTableScanResult.getColumnModel().getColumn(3);
		column4 = jTableScanResult.getColumnModel().getColumn(4);
		column5 = jTableScanResult.getColumnModel().getColumn(5);
		column6 = jTableScanResult.getColumnModel().getColumn(6);
		column7 = jTableScanResult.getColumnModel().getColumn(7);
		column8 = jTableScanResult.getColumnModel().getColumn(8);
		column0.setPreferredWidth(40);
		column1.setPreferredWidth(200);
		column2.setPreferredWidth(100);
		column3.setPreferredWidth(200);
		column4.setPreferredWidth(150);
		column5.setPreferredWidth(200);
		column6.setPreferredWidth(150);
		column7.setPreferredWidth(200);
		column8.setPreferredWidth(200);
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		column4.setCellRenderer(rendererAlignmentLeft);
		column5.setCellRenderer(rendererAlignmentLeft);
		column6.setCellRenderer(rendererAlignmentLeft);
		column7.setCellRenderer(rendererAlignmentLeft);
		column8.setCellRenderer(rendererAlignmentLeft);
		jTableScanResult.getTableHeader().setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		rendererTableHeader = (DefaultTableCellRenderer)jTableScanResult.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		jScrollPaneScanResult.getViewport().add(jTableScanResult, null);
		//
		//jPanelSouth and objects on it
		jPanelSouth.setBorder(null);
		jPanelSouth.setLayout(new BorderLayout());
		jPanelSouth.add(jScrollPaneMessage, BorderLayout.CENTER);
		jPanelSouth.add(jPanelButtons, BorderLayout.SOUTH);
		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setWrapStyleWord(true);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setBackground(SystemColor.control);
		jScrollPaneMessage.getViewport().add(jTextAreaMessage, null);
		jPanelSouth.setPreferredSize(new Dimension(100, 95));
		jPanelButtons.setBorder(null);
		jPanelButtons.setPreferredSize(new Dimension(100, 43));
		jButtonCloseDialog.setBounds(new Rectangle(40, 8, 100, 27));
		jButtonCloseDialog.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogScanField_jButtonCloseDialog_actionAdapter(this));
		jButtonGenerateListData.setBounds(new Rectangle(880, 8, 100, 27));
		jButtonGenerateListData.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonGenerateListData.setText(res.getString("GenerateList"));
		jButtonGenerateListData.addActionListener(new DialogScanField_jButtonGenerateListData_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonGenerateListData);
		jPanelButtons.add(jButtonCloseDialog);
		//
		this.setResizable(false);
		this.setTitle(res.getString("ScanField"));
		this.getContentPane().add(panelMain);
		this.getRootPane().setDefaultButton(jButtonScan);
		this.setPreferredSize(new Dimension(1024, 768));
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public void request() {
		if (tableModelScanResult.getRowCount() > 0) {
			int rowCount = tableModelScanResult.getRowCount();
			for (int i = 0; i < rowCount; i++) {tableModelScanResult.removeRow(0);}
		}
		jTextAreaMessage.setText(res.getString("ScanFieldHintMessage"));
		super.setVisible(true);
	}

	void jButtonScan_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element subsystemElement, tableElement, fieldElement;
		NodeList subsystemList, tableList, fieldList;
		SortableDomElementListModel sortedTableList;
		SortableDomElementListModel sortedFieldList;
		StringTokenizer tokenizer;
		boolean isToBeSelected = false;
		int rowNumber = 0;
		String subsystemID = "";
		String subsystemName = "";

		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));

			////////////////////////////////
			// Clear previous scan result //
			////////////////////////////////
			if (tableModelScanResult.getRowCount() > 0) {
				int rowCount = tableModelScanResult.getRowCount();
				for (int i = 0; i < rowCount; i++) {tableModelScanResult.removeRow(0);}
			}
			
			////////////////////
			// Setup criteria //
			////////////////////
			fieldIDList.clear();
			jTextFieldFieldID.setText(jTextFieldFieldID.getText().toUpperCase());
			String wrkStr = jTextFieldFieldID.getText().replaceAll(" ","");
			if (wrkStr.contains(";")) {
				tokenizer = new StringTokenizer(wrkStr, ";" );
				while (tokenizer.hasMoreTokens()) {
					fieldIDList.add(tokenizer.nextToken());
				}
			} else {
				fieldIDList.add(wrkStr);
			}
			fieldNameList.clear();
			wrkStr = jTextFieldFieldName.getText().replaceAll(" ","");
			if (wrkStr.contains(";")) {
				tokenizer = new StringTokenizer(wrkStr, ";" );
				while (tokenizer.hasMoreTokens()) {
					fieldNameList.add(tokenizer.nextToken());
				}
			} else {
				fieldNameList.add(wrkStr);
			}

			//////////////////////////////////////////////////////
			// Setup table list to count elements to be scanned //
			//////////////////////////////////////////////////////
			tableList = frame_.getDomDocument().getElementsByTagName("Table");
			sortedTableList = frame_.getSortedListModel(tableList, "SubsystemID");
			jProgressBar.setValue(0);
			jProgressBar.setMaximum(sortedTableList.getSize());
			jProgressBar.setVisible(true);
			jButtonScan.setVisible(false);

			///////////////////////////////
			// Scan Table/Field elements //
			///////////////////////////////
			subsystemList = frame_.getDomDocument().getElementsByTagName("Subsystem");
			for (int i = 0; i < sortedTableList.getSize(); i++) {

				jProgressBar.setValue(jProgressBar.getValue() + 1);
				jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
				
				tableElement = (org.w3c.dom.Element)sortedTableList.getElementAt(i);
				if (!subsystemID.equals(tableElement.getAttribute("SubsystemID"))) {
					subsystemID = tableElement.getAttribute("SubsystemID");
					for (int j = 0; j < subsystemList.getLength(); j++) {
						subsystemElement = (org.w3c.dom.Element)subsystemList.item(j);
						if (subsystemElement.getAttribute("ID").equals(subsystemID)) {
							subsystemName = subsystemElement.getAttribute("Name");
						}
					}
				}
				fieldList = tableElement.getElementsByTagName("Field");
				sortedFieldList = frame_.getSortedListModel(fieldList, "Order");
				for (int j = 0; j < sortedFieldList.getSize(); j++) {
					fieldElement = (org.w3c.dom.Element)sortedFieldList.getElementAt(j);

					//////////////////////////
					// Check to be selected //
					//////////////////////////
					isToBeSelected = false;
					if (jTextFieldFieldID.getText().equals("") && jTextFieldFieldName.getText().equals("")) {
						isToBeSelected = true;
					} else {
						for (int k = 0; k < fieldIDList.size(); k++) {
							if (fieldIDList.get(k).startsWith("*")) {
								if (fieldIDList.get(k).endsWith("*")) {
									if (fieldElement.getAttribute("ID").contains(fieldIDList.get(k).replaceAll("\\*", ""))) {
										isToBeSelected = true;
									}
								} else {
									if (fieldElement.getAttribute("ID").endsWith(fieldIDList.get(k).replaceAll("\\*", ""))) {
										isToBeSelected = true;
									}
								}
							} else {
								if (fieldIDList.get(k).endsWith("*")) {
									if (fieldElement.getAttribute("ID").startsWith(fieldIDList.get(k).replaceAll("\\*", ""))) {
										isToBeSelected = true;
									}
								} else {
									if (fieldElement.getAttribute("ID").equals(fieldIDList.get(k))) {
										isToBeSelected = true;
									}
								}
							}
						}
						if (!isToBeSelected) {
							for (int k = 0; k < fieldNameList.size(); k++) {
								if (fieldNameList.get(k).startsWith("*")) {
									if (fieldNameList.get(k).endsWith("*")) {
										if (fieldElement.getAttribute("Name").contains(fieldNameList.get(k).replaceAll("\\*", ""))) {
											isToBeSelected = true;
										}
									} else {
										if (fieldElement.getAttribute("Name").endsWith(fieldNameList.get(k).replaceAll("\\*", ""))) {
											isToBeSelected = true;
										}
									}
								} else {
									if (fieldNameList.get(k).endsWith("*")) {
										if (fieldElement.getAttribute("Name").startsWith(fieldNameList.get(k).replaceAll("\\*", ""))) {
											isToBeSelected = true;
										}
									} else {
										if (fieldElement.getAttribute("Name").equals(fieldNameList.get(k))) {
											isToBeSelected = true;
										}
									}
								}
							}
						}
					}

					//////////////////////////////////////
					// Add a row if it's to be selected //
					//////////////////////////////////////
					if (isToBeSelected) {
						Object[] Cell = new Object[9];
						rowNumber++;
						Cell[0] = rowNumber;
						Cell[1] = tableElement.getAttribute("SubsystemID") + " " + subsystemName;
						Cell[2] = tableElement.getAttribute("ID");
						Cell[3] = tableElement.getAttribute("Name");
						if (frame_.getOptionList(fieldElement.getAttribute("TypeOptions")).contains("VIRTUAL")) {
							Cell[4] = "(" + fieldElement.getAttribute("ID") + ")";
							Cell[5] = fieldElement.getAttribute("Name");
						} else {
							Cell[4] = fieldElement.getAttribute("ID");
							Cell[5] = fieldElement.getAttribute("Name");
						}
						Cell[6] = frame_.getDescriptionsOfTypeAndSize(fieldElement.getAttribute("Type"), fieldElement.getAttribute("Size"), fieldElement.getAttribute("Decimal"));
						Cell[7] = frame_.getDescriptionsOfTypeOptions(fieldElement, false, null);
						Cell[8] = frame_.getFirstParagraph(fieldElement.getAttribute("Remarks"));
						tableModelScanResult.addRow(Cell);

					}
				}
			}
		} finally {
			jProgressBar.setVisible(false);
			jButtonScan.setVisible(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		if (rowNumber == 0) {
			jTextAreaMessage.setText(res.getString("ScanFieldNotFound"));
		} else {
			jTextAreaMessage.setText(rowNumber + res.getString("ScanFieldFound"));
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
		File xlsFile = null;
		String xlsFileName = "";
		FileOutputStream fileOutputStream = null;
		//
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet workSheet = workBook.createSheet(this.getTitle());
		workSheet.setDefaultRowHeight( (short) 300);
		HSSFFooter workSheetFooter = workSheet.getFooter();
		workSheetFooter.setRight(this.getTitle() + "  Page " + HSSFFooter.page() + " / " + HSSFFooter.numPages() );
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
			xlsFile = File.createTempFile("XeadEditor_CheckFunctionsCalled_", ".xls", outputFolder);
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
				for (int j = 1; j < 9; j++) {
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

	class TableModelReadOnlyList extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {return false;}
	}
}

class DialogScanField_jButtonScan_actionAdapter implements java.awt.event.ActionListener {
	DialogScanField adaptee;

	DialogScanField_jButtonScan_actionAdapter(DialogScanField adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonScan_actionPerformed(e);
	}
}

class DialogScanField_jButtonCloseDialog_actionAdapter implements java.awt.event.ActionListener {
	DialogScanField adaptee;

	DialogScanField_jButtonCloseDialog_actionAdapter(DialogScanField adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCloseDialog_actionPerformed(e);
	}
}

class DialogScanField_jButtonGenerateListData_actionAdapter implements java.awt.event.ActionListener {
	DialogScanField adaptee;

	DialogScanField_jButtonGenerateListData_actionAdapter(DialogScanField adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonGenerateListData_actionPerformed(e);
	}
}

