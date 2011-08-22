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
import java.util.ResourceBundle;
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

public class DialogCheckFunctionsCalled extends JDialog {
	private static final long serialVersionUID = 1L;
	static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	Editor frame_;
	BorderLayout borderLayoutMain = new BorderLayout();
	JPanel panelMain = new JPanel();
	JPanel jPanelSouth = new JPanel();
	JScrollPane jScrollPaneCheckResult = new JScrollPane();
	TableModelReadOnlyList tableModelCheckResult = new TableModelReadOnlyList();
	JTable jTableCheckResult = new JTable(tableModelCheckResult);
	TableColumn column0, column1, column2, column3;
	DefaultTableCellRenderer rendererTableHeader = null;
	DefaultTableCellRenderer rendererAlignmentCenter = new DefaultTableCellRenderer();
	DefaultTableCellRenderer rendererAlignmentRight = new DefaultTableCellRenderer();
	DefaultTableCellRenderer rendererAlignmentLeft = new DefaultTableCellRenderer();
	JButton jButtonCloseDialog = new JButton();
	JButton jButtonGenerateListData = new JButton();
	SortableDomElementListModel functionNodeList1, functionNodeList2, menuNodeList, tableNodeList;

	public DialogCheckFunctionsCalled(Editor frame, String title, boolean modal) {
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

	public DialogCheckFunctionsCalled(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		//
		panelMain.setLayout(borderLayoutMain);
		panelMain.setPreferredSize(new Dimension(880, 300));
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(jPanelSouth, BorderLayout.SOUTH);
		panelMain.add(jScrollPaneCheckResult, BorderLayout.CENTER);
		//
		jTableCheckResult.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTableCheckResult.setBackground(SystemColor.control);
		jTableCheckResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableCheckResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTableCheckResult.setRowSelectionAllowed(true);
		tableModelCheckResult.addColumn("NO.");
		tableModelCheckResult.addColumn(res.getString("ElementType"));
		tableModelCheckResult.addColumn(res.getString("ElementName"));
		tableModelCheckResult.addColumn(res.getString("Status"));
		column0 = jTableCheckResult.getColumnModel().getColumn(0);
		column1 = jTableCheckResult.getColumnModel().getColumn(1);
		column2 = jTableCheckResult.getColumnModel().getColumn(2);
		column3 = jTableCheckResult.getColumnModel().getColumn(3);
		column0.setPreferredWidth(34);
		column1.setPreferredWidth(95);
		column2.setPreferredWidth(200);
		column3.setPreferredWidth(525);
		rendererAlignmentCenter.setHorizontalAlignment(0); //CENTER//
		rendererAlignmentRight.setHorizontalAlignment(4); //RIGHT//
		rendererAlignmentLeft.setHorizontalAlignment(2); //LEFT//
		column0.setCellRenderer(rendererAlignmentCenter);
		column1.setCellRenderer(rendererAlignmentLeft);
		column2.setCellRenderer(rendererAlignmentLeft);
		column3.setCellRenderer(rendererAlignmentLeft);
		jTableCheckResult.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, 12));
		rendererTableHeader = (DefaultTableCellRenderer)jTableCheckResult.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(2); //LEFT//
		jScrollPaneCheckResult.getViewport().add(jTableCheckResult, null);
		//
		//jPanelSouth and objects on it
		jPanelSouth.setBorder(BorderFactory.createEtchedBorder());
		jPanelSouth.setPreferredSize(new Dimension(800, 40));
		jButtonCloseDialog.setBounds(new Rectangle(20, 7, 70, 25));
		jButtonCloseDialog.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCloseDialog.setText(res.getString("Close"));
		jButtonCloseDialog.addActionListener(new DialogCheckFunctionsCalled_jButtonCloseDialog_actionAdapter(this));
		jButtonGenerateListData.setBounds(new Rectangle(760, 7, 90, 25));
		jButtonGenerateListData.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonGenerateListData.setText(res.getString("GenerateList"));
		jButtonGenerateListData.addActionListener(new DialogCheckFunctionsCalled_jButtonGenerateListData_actionAdapter(this));
		jPanelSouth.setLayout(null);
		jPanelSouth.add(jButtonGenerateListData);
		jPanelSouth.add(jButtonCloseDialog);
		//
		this.setResizable(false);
		this.setTitle(res.getString("CheckFunctionsCalled"));
		this.getContentPane().add(panelMain);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public void request() {
		super.setVisible(true);
	}

	public int numberOfInvalidCalls() {
		org.w3c.dom.Element element1, element2, element3;
		NodeList nodeList1, nodeList2;
		int numberOfInvalidCalls = 0;
		boolean isNotCalledByAnyElement;
		int pos1, pos2;
		MainTreeNode node;
		String wrkStr;
		//
		if (tableModelCheckResult.getRowCount() > 0) {
			int rowCount = tableModelCheckResult.getRowCount();
			for (int i = 0; i < rowCount; i++) {tableModelCheckResult.removeRow(0);}
		}
		//
		nodeList1 = frame_.getDomDocument().getElementsByTagName("Function");
		functionNodeList1 = frame_.getSortedListModel(nodeList1, "ID");
		functionNodeList2 = frame_.getSortedListModel(nodeList1, "ID");
		nodeList1 = frame_.getDomDocument().getElementsByTagName("Menu");
		menuNodeList = frame_.getSortedListModel(nodeList1, "ID");
		nodeList1 = frame_.getDomDocument().getElementsByTagName("Table");
		tableNodeList = frame_.getSortedListModel(nodeList1, "ID");
		//
		for (int i = 0; i < tableNodeList.getSize(); i++) {
			element1 = (org.w3c.dom.Element)tableNodeList.getElementAt(i);
			nodeList1 = element1.getElementsByTagName("Script");
			for (int j = 0; j < nodeList1.getLength(); j++) {
				element2 = (org.w3c.dom.Element)nodeList1.item(j);
				wrkStr = frame_.substringLinesWithTokenOfEOL(element2.getAttribute("Text"), "\n");
				wrkStr = frame_.removeCommentsFromScriptText(wrkStr).replace(" ", "");
				pos1 = -1;
				while (pos1 < wrkStr.length()) {
					pos1 = wrkStr.indexOf("instance.callFunction('", pos1 + 1);
					if (pos1 == -1) {
						break;
					} else {
						pos2 = wrkStr.indexOf("')", pos1 + 1);
						if (pos2 == -1) {
							break;
						} else {
							node = frame_.getSpecificXETreeNode("Function", wrkStr.substring(pos1+23, pos2).trim());
							if (node == null) {
								numberOfInvalidCalls++;
								Object[] Cell = new Object[4];
								Cell[0] = numberOfInvalidCalls;
								Cell[1] = res.getString("TableDefinition");
								Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
								Cell[3] = res.getString("CheckFunctionsCalledMessage7") + element2.getAttribute("Name") + res.getString("CheckFunctionsCalledMessage8") + wrkStr.substring(pos1+23, pos2) + res.getString("CheckFunctionsCalledMessage9");
								tableModelCheckResult.addRow(Cell);
							}
						}
					}
				}
				pos1 = -1;
				while (pos1 < wrkStr.length()) {
					pos1 = wrkStr.indexOf("instance.functionID=='", pos1 + 1);
					if (pos1 == -1) {
						break;
					} else {
						pos2 = wrkStr.indexOf("'", pos1 + 22);
						if (pos2 == -1) {
							break;
						} else {
							node = frame_.getSpecificXETreeNode("Function", wrkStr.substring(pos1+22, pos2));
							if (node == null) {
								numberOfInvalidCalls++;
								Object[] Cell = new Object[4];
								Cell[0] = numberOfInvalidCalls;
								Cell[1] = res.getString("TableDefinition");
								Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
								Cell[3] = res.getString("CheckFunctionsCalledMessage7") + element2.getAttribute("Name") + res.getString("CheckFunctionsCalledMessage8") + wrkStr.substring(pos1+22, pos2) + res.getString("CheckFunctionsCalledMessage9");
								tableModelCheckResult.addRow(Cell);
							}
						}
					}
				}
			}
		}
		//
		for (int i = 0; i < functionNodeList1.getSize(); i++) {
			element1 = (org.w3c.dom.Element)functionNodeList1.getElementAt(i);
			//
			isNotCalledByAnyElement = true;
			for (int j = 0; j < menuNodeList.getSize(); j++) {
				element2 = (org.w3c.dom.Element)menuNodeList.getElementAt(j);
				nodeList1 = element2.getElementsByTagName("Option");
				for (int k = 0; k < nodeList1.getLength(); k++) {
					element3 = (org.w3c.dom.Element)nodeList1.item(k);
					if (element3.getAttribute("FunctionID").equals(element1.getAttribute("ID"))) {
						isNotCalledByAnyElement = false;
						break;
					}
				}
				if (!isNotCalledByAnyElement) {
					break;
				}
			}
			if (isNotCalledByAnyElement) {
				for (int j = 0; j < functionNodeList2.getSize(); j++) {
					element2 = (org.w3c.dom.Element)functionNodeList2.getElementAt(j);
					if (!frame_.getFunctionUsageInFunction(element2, element1.getAttribute("ID")).equals("")) {
						isNotCalledByAnyElement = false;
						break;
					}
				}
			}
			if (isNotCalledByAnyElement) {
				for (int j = 0; j < tableNodeList.getSize(); j++) {
					element2 = (org.w3c.dom.Element)tableNodeList.getElementAt(j);
					if (!frame_.getFunctionUsageInTableScript(element2, element1.getAttribute("ID")).equals("")) {
						isNotCalledByAnyElement = false;
						break;
					}
				}
			}
			if (isNotCalledByAnyElement) {
				numberOfInvalidCalls++;
				Object[] Cell = new Object[4];
				Cell[0] = numberOfInvalidCalls;
				Cell[1] = res.getString("FunctionDefinition");
				Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
				Cell[3] = res.getString("CheckFunctionsCalledMessage2");
				tableModelCheckResult.addRow(Cell);
			}
			//
			if (element1.getAttribute("Type").equals("XF000")) {
				wrkStr = frame_.substringLinesWithTokenOfEOL(element1.getAttribute("Script"), "\n");
				wrkStr = frame_.removeCommentsFromScriptText(wrkStr).replace(" ", "");
				pos1 = -1;
				while (pos1 < wrkStr.length()) {
					pos1 = wrkStr.indexOf("instance.callFunction('", pos1 + 1);
					if (pos1 == -1) {
						break;
					} else {
						pos2 = wrkStr.indexOf("')", pos1 + 1);
						if (pos2 == -1) {
							break;
						} else {
							node = frame_.getSpecificXETreeNode("Function", wrkStr.substring(pos1+23, pos2).trim());
							if (node == null) {
								numberOfInvalidCalls++;
								Object[] Cell = new Object[4];
								Cell[0] = numberOfInvalidCalls;
								Cell[1] = res.getString("FunctionDefinition");
								Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
								Cell[3] = res.getString("CheckFunctionsCalledMessage3") + wrkStr.substring(pos1+23, pos2).trim() + res.getString("CheckFunctionsCalledMessage4");
								tableModelCheckResult.addRow(Cell);
							}
						}
					}
				}
			}
			//
			if (element1.getAttribute("Type").equals("XF100") || element1.getAttribute("Type").equals("XF110")) {
				if (!element1.getAttribute("DetailFunction").equals("")) {
					node = frame_.getSpecificXETreeNode("Function", element1.getAttribute("DetailFunction"));
					if (node == null) {
						numberOfInvalidCalls++;
						Object[] Cell = new Object[4];
						Cell[0] = numberOfInvalidCalls;
						Cell[1] = res.getString("FunctionDefinition");
						Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
						Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element1.getAttribute("DetailFunction") + res.getString("CheckFunctionsCalledMessage6");
						tableModelCheckResult.addRow(Cell);
					}
				}
				nodeList1 = element1.getElementsByTagName("Button");
				for (int j = 0; j < nodeList1.getLength(); j++) {
					element2 = (org.w3c.dom.Element)nodeList1.item(j);
					pos1 = element2.getAttribute("Action").indexOf("CALL(");
					if (pos1 >= 0) {
						pos2 = element2.getAttribute("Action").indexOf(")");
						node = frame_.getSpecificXETreeNode("Function", element2.getAttribute("Action").substring(pos1+5, pos2));
						if (node == null) {
							numberOfInvalidCalls++;
							Object[] Cell = new Object[4];
							Cell[0] = numberOfInvalidCalls;
							Cell[1] = res.getString("FunctionDefinition");
							Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
							Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element2.getAttribute("Action").substring(pos1+5, pos2) + res.getString("CheckFunctionsCalledMessage6");
							tableModelCheckResult.addRow(Cell);
						}
					}
					if (element2.getAttribute("Action").equals("ADD") && element1.getAttribute("DetailFunction").equals("")) {
						numberOfInvalidCalls++;
						Object[] Cell = new Object[4];
						Cell[0] = numberOfInvalidCalls;
						Cell[1] = res.getString("FunctionDefinition");
						Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
						Cell[3] = res.getString("CheckFunctionsCalledMessage10");
						tableModelCheckResult.addRow(Cell);
					}
				}
			}
			//
			if (element1.getAttribute("Type").equals("XF200")) { 
				nodeList1 = element1.getElementsByTagName("Button");
				for (int j = 0; j < nodeList1.getLength(); j++) {
					element2 = (org.w3c.dom.Element)nodeList1.item(j);
					pos1 = element2.getAttribute("Action").indexOf("CALL(");
					if (pos1 >= 0) {
						pos2 = element2.getAttribute("Action").indexOf(")");
						node = frame_.getSpecificXETreeNode("Function", element2.getAttribute("Action").substring(pos1+5, pos2));
						if (node == null) {
							numberOfInvalidCalls++;
							Object[] Cell = new Object[4];
							Cell[0] = numberOfInvalidCalls;
							Cell[1] = res.getString("FunctionDefinition");
							Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
							Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element2.getAttribute("Action").substring(pos1+5, pos2) + res.getString("CheckFunctionsCalledMessage6");
							tableModelCheckResult.addRow(Cell);
						}
					}
				}
				if (!element1.getAttribute("FunctionAfterInsert").equals("")) {
					node = frame_.getSpecificXETreeNode("Function", element1.getAttribute("FunctionAfterInsert"));
					if (node == null) {
						numberOfInvalidCalls++;
						Object[] Cell = new Object[4];
						Cell[0] = numberOfInvalidCalls;
						Cell[1] = res.getString("FunctionDefinition");
						Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
						Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element1.getAttribute("FunctionAfterInsert") + res.getString("CheckFunctionsCalledMessage6");
						tableModelCheckResult.addRow(Cell);
					}
				}
			}
			//
			if (element1.getAttribute("Type").equals("XF300")) { 
				if (!element1.getAttribute("HeaderFunction").equals("")) {
					node = frame_.getSpecificXETreeNode("Function", element1.getAttribute("HeaderFunction"));
					if (node == null) {
						numberOfInvalidCalls++;
						Object[] Cell = new Object[4];
						Cell[0] = numberOfInvalidCalls;
						Cell[1] = res.getString("FunctionDefinition");
						Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
						Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element1.getAttribute("HeaderFunction") + res.getString("CheckFunctionsCalledMessage6");
						tableModelCheckResult.addRow(Cell);
					}
				}
				nodeList1 = element1.getElementsByTagName("Detail");
				for (int j = 0; j < nodeList1.getLength(); j++) {
					element2 = (org.w3c.dom.Element)nodeList1.item(j);
					if (!element2.getAttribute("DetailFunction").equals("") && !element2.getAttribute("DetailFunction").equals("NONE")) {
						node = frame_.getSpecificXETreeNode("Function", element2.getAttribute("DetailFunction"));
						if (node == null) {
							numberOfInvalidCalls++;
							Object[] Cell = new Object[4];
							Cell[0] = numberOfInvalidCalls;
							Cell[1] = res.getString("FunctionDefinition");
							Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
							Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element2.getAttribute("DetailFunction") + res.getString("CheckFunctionsCalledMessage6");
							tableModelCheckResult.addRow(Cell);
						}
					}
					nodeList2 = element2.getElementsByTagName("Button");
					for (int k = 0; k < nodeList2.getLength(); k++) {
						element3 = (org.w3c.dom.Element)nodeList2.item(k);
						pos1 = element3.getAttribute("Action").indexOf("CALL(");
						if (pos1 >= 0) {
							pos2 = element3.getAttribute("Action").indexOf(")");
							node = frame_.getSpecificXETreeNode("Function", element3.getAttribute("Action").substring(pos1+5, pos2));
							if (node == null) {
								numberOfInvalidCalls++;
								Object[] Cell = new Object[4];
								Cell[0] = numberOfInvalidCalls;
								Cell[1] = res.getString("FunctionDefinition");
								Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
								Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element3.getAttribute("Action").substring(pos1+5, pos2) + res.getString("CheckFunctionsCalledMessage6");
								tableModelCheckResult.addRow(Cell);
							}
						}
						if (element3.getAttribute("Action").equals("ADD") && element2.getAttribute("DetailFunction").equals("")) {
							numberOfInvalidCalls++;
							Object[] Cell = new Object[4];
							Cell[0] = numberOfInvalidCalls;
							Cell[1] = res.getString("FunctionDefinition");
							Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
							Cell[3] = res.getString("CheckFunctionsCalledMessage10");
							tableModelCheckResult.addRow(Cell);
						}
						if (element3.getAttribute("Action").equals("HEADER") && element1.getAttribute("HeaderFunction").equals("")) {
							numberOfInvalidCalls++;
							Object[] Cell = new Object[4];
							Cell[0] = numberOfInvalidCalls;
							Cell[1] = res.getString("FunctionDefinition");
							Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
							Cell[3] = res.getString("CheckFunctionsCalledMessage11");
							tableModelCheckResult.addRow(Cell);
						}
					}
				}
			}
			//
			if (element1.getAttribute("Type").equals("XF310")) { 
				nodeList1 = element1.getElementsByTagName("AddRowListButton");
				for (int j = 0; j < nodeList1.getLength(); j++) {
					element2 = (org.w3c.dom.Element)nodeList1.item(j);
					pos1 = element2.getAttribute("Action").indexOf("CALL(");
					if (pos1 >= 0) {
						pos2 = element2.getAttribute("Action").indexOf(")");
						node = frame_.getSpecificXETreeNode("Function", element2.getAttribute("Action").substring(pos1+5, pos2));
						if (node == null) {
							numberOfInvalidCalls++;
							Object[] Cell = new Object[4];
							Cell[0] = numberOfInvalidCalls;
							Cell[1] = res.getString("FunctionDefinition");
							Cell[2] = element1.getAttribute("ID") + " " + element1.getAttribute("Name");
							Cell[3] = res.getString("CheckFunctionsCalledMessage5") + element2.getAttribute("Action").substring(pos1+5, pos2) + res.getString("CheckFunctionsCalledMessage6");
							tableModelCheckResult.addRow(Cell);
						}
					}
				}
			}
		}
		//
		return numberOfInvalidCalls;
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
			xlsFile = File.createTempFile("XeadEditor_CheckFunctionsCalled_", ".xls", outputFolder);
			if (outputFolder == null) {
				xlsFile.deleteOnExit();
			}
			xlsFileName = xlsFile.getPath();
			fileOutputStream = new FileOutputStream(xlsFileName);
			//
			currentRowNumber++;
			HSSFRow rowCaption = workSheet.createRow(currentRowNumber);
			for (int i = 0; i < tableModelCheckResult.getColumnCount(); i++) {
				HSSFCell cell = rowCaption.createCell(i);
				cell.setCellStyle(styleHeaderNumber);
				cell.setCellValue(new HSSFRichTextString(tableModelCheckResult.getColumnName(i)));
				Rectangle rect = jTableCheckResult.getCellRect(0, i, true);
				workSheet.setColumnWidth(i, rect.width * 40);
			}
			//
			for (int i = 0; i < tableModelCheckResult.getRowCount(); i++) {
				currentRowNumber++;
				HSSFRow rowData = workSheet.createRow(currentRowNumber);
				//
				HSSFCell cell0 = rowData.createCell(0); //Column of Sequence Number
				cell0.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell0.setCellStyle(styleDataInteger);
				cell0.setCellValue(i + 1);
				//
				for (int j = 1; j < 4; j++) {
					HSSFCell cell = rowData.createCell(j);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(styleDataString);
					cell.setCellValue(new HSSFRichTextString(tableModelCheckResult.getValueAt(i, j).toString()));
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

class DialogCheckFunctionsCalled_jButtonCloseDialog_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckFunctionsCalled adaptee;

	DialogCheckFunctionsCalled_jButtonCloseDialog_actionAdapter(DialogCheckFunctionsCalled adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCloseDialog_actionPerformed(e);
	}
}

class DialogCheckFunctionsCalled_jButtonGenerateListData_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckFunctionsCalled adaptee;

	DialogCheckFunctionsCalled_jButtonGenerateListData_actionAdapter(DialogCheckFunctionsCalled adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonGenerateListData_actionPerformed(e);
	}
}

