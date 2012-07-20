package xeadEditor;

/*
 * Copyright (c) 2012 WATANABE kozo <qyf05466@nifty.com>,
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.w3c.dom.NodeList;
import xeadEditor.Editor.MainTreeNode;
import xeadEditor.Editor.SortableDomElementListModel;

public class DialogCheckLayout extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private static final int FIELD_UNIT_HEIGHT = 24;
	private static final int FIELD_HORIZONTAL_MARGIN = 1;
	private static final int FIELD_VERTICAL_MARGIN = 5;
	private static final int FONT_SIZE = 14;
	private static final int ROW_HEIGHT = 18;
	private static ImageIcon ICON_CHECK_0A = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck0A.PNG")));
	private static ImageIcon ICON_CHECK_1A = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck1A.PNG")));
	private static ImageIcon ICON_CHECK_0D = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck0D.PNG")));
	private static ImageIcon ICON_CHECK_1D = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck1D.PNG")));
	private static ImageIcon ICON_CHECK_0R = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck0R.PNG")));
	private static ImageIcon ICON_CHECK_1R = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck1R.PNG")));
	//
	private JPanel jPanelMain = new JPanel();
	private JTable jTableMain = new JTable();
	private JScrollPane jScrollPane = new JScrollPane();
	private Editor editor;
	private org.w3c.dom.Element functionElement;
	private StringTokenizer workTokenizer;
	private SortableDomElementListModel sortableList;
	private DialogCheckLayoutPrimaryTable primaryTable;
	private DialogCheckLayoutDetailTable detailTable;
	private ArrayList<DialogCheckLayoutReferTable> referTableList = new ArrayList<DialogCheckLayoutReferTable>(); 
	private ArrayList<DialogCheckLayoutReferTable> referTableList2 = new ArrayList<DialogCheckLayoutReferTable>(); 
	private Rectangle screenRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	private String panelType_ = "";
	private int extForXF110_ = 0;
	
	public DialogCheckLayout(Editor parent) {
		super(parent);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			editor = parent;
			jbInit(parent);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit(Editor parent) throws Exception  {
		jPanelMain.setLayout(null);
		jTableMain.setFont(new java.awt.Font("SansSerif", 0, FONT_SIZE));
		jTableMain.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableMain.setRowHeight(ROW_HEIGHT);
		jTableMain.getTableHeader().setFont(new java.awt.Font("SansSerif", 0, FONT_SIZE));
		DefaultTableCellRenderer rendererTableHeader = (DefaultTableCellRenderer)jTableMain.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(SwingConstants.LEFT);
		//
		this.setResizable(false);
		this.setTitle(res.getString("CheckLayoutTitle"));
	 	this.setIconImage(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("title.png")));
		this.setPreferredSize(new Dimension(233, 167));
		this.getContentPane().add(jScrollPane, BorderLayout.CENTER);
	}

	public void request(MainTreeNode node, String panelType, int tabIndex, int extForXF110) {
		functionElement = node.getElement();
		panelType_ = panelType;
		extForXF110_ = extForXF110;
		//
		referTableList.clear();
		referTableList2.clear();
		detailTable = null;
		//
		if (panelType_.equals("Function100ColumnList")) {
			showLayoutOfTable(functionElement.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function110ColumnList")) {
			showLayoutOfTable(functionElement.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function110BatchFieldList")) {
			showLayoutOfPanel(functionElement.getElementsByTagName("BatchField"), true);
		}
		if (panelType_.equals("Function200FieldList")) {
			showLayoutOfPanel(functionElement.getElementsByTagName("Field"), true);
		}
		if (panelType_.equals("Function200TabFieldList")) {
			showLayoutOfPanel(editor.getFunction200TabFieldList(), true);
		}
		if (panelType_.equals("Function300HeaderFieldList")) {
			showLayoutOfPanel(functionElement.getElementsByTagName("Field"), false);
		}
		if (panelType_.equals("Function300DetailFieldList") && tabIndex >= 0) {
			NodeList detailTableList = functionElement.getElementsByTagName("Detail");
			sortableList = editor.getSortedListModel(detailTableList, "Order");
			org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(tabIndex);
			detailTable = new DialogCheckLayoutDetailTable(element, this);
			showLayoutOfTable(element.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function310HeaderFieldList")) {
			showLayoutOfPanel(functionElement.getElementsByTagName("Field"), true);
		}
		if (panelType_.equals("Function310DetailFieldList")) {
			showLayoutOfTable(functionElement.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function310AddRowListColumnList")) {
			showLayoutOfTable(functionElement.getElementsByTagName("AddRowListColumn"));
		}
	}

	private void showLayoutOfTable(NodeList functionColumnList) {
		///////////////////////////////////////////////////////////
		// Set panel size and position according specified sizes //
		///////////////////////////////////////////////////////////
		if (!functionElement.getAttribute("Size").equals("")
				&& !functionElement.getAttribute("Size").equals("AUTO")) {
			workTokenizer = new StringTokenizer(functionElement.getAttribute("Size"), ";" );
			int width = Integer.parseInt(workTokenizer.nextToken());
			int height = Integer.parseInt(workTokenizer.nextToken());
			this.setPreferredSize(new Dimension(width, height));
			int posX = ((screenRect.width - width) / 2) + screenRect.x;
			int posY = ((screenRect.height - height) / 2) + screenRect.y;
			this.setLocation(posX, posY);
			this.pack();
		}

		//////////////////////////////////////////////
		// Setup the primary table and refer tables //
		//////////////////////////////////////////////
		primaryTable = new DialogCheckLayoutPrimaryTable(functionElement, this);
		NodeList referNodeList = primaryTable.getTableElement().getElementsByTagName("Refer");
		sortableList = editor.getSortedListModel(referNodeList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(i);
			referTableList.add(new DialogCheckLayoutReferTable(element));
		}
		if (panelType_.equals("Function300DetailFieldList")) {
			referNodeList = detailTable.getTableElement().getElementsByTagName("Refer");
			sortableList = editor.getSortedListModel(referNodeList, "Order");
			for (int i = 0; i < sortableList.getSize(); i++) {
				org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(i);
				referTableList2.add(new DialogCheckLayoutReferTable(element));
			}
		}

		////////////////////////////
		// Setup columns on table //
		////////////////////////////
		TableColumn column;
		DialogCheckLayoutColumn field;
		int biggestWidth = 0;
		int workHeight = 100;
		int posX, posY;
		DefaultTableModel tableModel = new DefaultTableModel();
		jTableMain.setModel(tableModel);
		jScrollPane.getViewport().add(jTableMain, null);
		tableModel.addColumn("NO.");
		ArrayList<DialogCheckLayoutColumn> fieldList = new ArrayList<DialogCheckLayoutColumn>();
		sortableList = editor.getSortedListModel(functionColumnList, "Order");
		if (extForXF110_ == 0) {
			for (int i = 0; i < sortableList.getSize(); i++) {
				field = new DialogCheckLayoutColumn((org.w3c.dom.Element)sortableList.getElementAt(i), this);
				tableModel.addColumn(field.getCaption());
				fieldList.add(field);
			}
			for (int i = 0; i < jTableMain.getColumnModel().getColumnCount(); i++) {
				column = jTableMain.getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(38);
					column.setCellRenderer(new DialogCheckLayoutRowNumberRenderer());
					biggestWidth = 38;
				} else {
					column.setPreferredWidth(fieldList.get(i-1).getWidth());
					column.setHeaderRenderer(fieldList.get(i-1).getHeaderRenderer());
					column.setCellRenderer(fieldList.get(i-1).getCellRenderer());
					biggestWidth = biggestWidth + fieldList.get(i-1).getWidth();
				}
			}
			for (int r = 0; r < 4; r++) {
				Object[] Cell = new Object[jTableMain.getColumnModel().getColumnCount()];
				for (int i = 0; i < jTableMain.getColumnModel().getColumnCount(); i++) {
					if (i == 0) {
						Cell[i] = r+1;
					} else {
						Cell[i] = fieldList.get(i-1).getValue();
					}
				}
				tableModel.addRow(Cell);
			}
		}
		if (extForXF110_ == 1) {
			tableModel.addColumn("");
			for (int i = 0; i < sortableList.getSize(); i++) {
				field = new DialogCheckLayoutColumn((org.w3c.dom.Element)sortableList.getElementAt(i), this);
				if (field.isVisibleOnPanel(extForXF110_)) {
					tableModel.addColumn(field.getCaption());
					fieldList.add(field);
				}
			}
			for (int i = 0; i < jTableMain.getColumnModel().getColumnCount(); i++) {
				column = jTableMain.getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(38);
					column.setCellRenderer(new DialogCheckLayoutRowNumberRenderer());
					biggestWidth = 38;
				} else {
					if (i == 1) {
						column.setPreferredWidth(22);
						column.setCellRenderer(new DialogCheckLayoutCheckBoxRenderer());
						column.setHeaderRenderer(new DialogCheckLayoutCheckBoxHeaderRenderer());
						biggestWidth = biggestWidth + 22;
					} else {
						column.setPreferredWidth(fieldList.get(i-2).getWidth());
						column.setHeaderRenderer(fieldList.get(i-2).getHeaderRenderer());
						column.setCellRenderer(fieldList.get(i-2).getCellRenderer());
						biggestWidth = biggestWidth + fieldList.get(i-2).getWidth();
					}
				}
			}
			for (int r = 0; r < 4; r++) {
				Object[] Cell = new Object[jTableMain.getColumnModel().getColumnCount()];
				for (int i = 0; i < jTableMain.getColumnModel().getColumnCount(); i++) {
					if (i == 0) {
						Cell[i] = r+1;
					} else {
						if (i == 1) {
							Cell[i] = "";
						} else {
							Cell[i] = fieldList.get(i-2).getValue();
						}
					}
				}
				tableModel.addRow(Cell);
			}
		}
		if (extForXF110_ == 2) {
			for (int i = 0; i < sortableList.getSize(); i++) {
				field = new DialogCheckLayoutColumn((org.w3c.dom.Element)sortableList.getElementAt(i), this);
				if (field.isVisibleOnPanel(extForXF110_)) {
					tableModel.addColumn(field.getCaption());
					fieldList.add(field);
				}
			}
			for (int i = 0; i < jTableMain.getColumnModel().getColumnCount(); i++) {
				column = jTableMain.getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(38);
					column.setCellRenderer(new DialogCheckLayoutRowNumberRenderer());
					biggestWidth = 38;
				} else {
					column.setPreferredWidth(fieldList.get(i-1).getWidth());
					column.setHeaderRenderer(fieldList.get(i-1).getHeaderRenderer());
					column.setCellRenderer(fieldList.get(i-1).getCellRenderer());
					biggestWidth = biggestWidth + fieldList.get(i-1).getWidth();
				}
			}
			for (int r = 0; r < 4; r++) {
				Object[] Cell = new Object[jTableMain.getColumnModel().getColumnCount()];
				for (int i = 0; i < jTableMain.getColumnModel().getColumnCount(); i++) {
					if (i == 0) {
						Cell[i] = r+1;
					} else {
						Cell[i] = fieldList.get(i-1).getValue();
					}
				}
				tableModel.addRow(Cell);
			}
		}
		jTableMain.setPreferredSize(new Dimension(biggestWidth, workHeight));

		///////////////////////////////////////////////////////////////
		// Adjust panel size and position according to fields layout //
		///////////////////////////////////////////////////////////////
		int workWidth = biggestWidth + 50;
		if (functionElement.getAttribute("Size").equals("")) {
			workWidth = screenRect.width;
			posX = screenRect.x;
		} else {
			if (functionElement.getAttribute("Size").equals("AUTO")) {
				if (workWidth < 800) {
					workWidth = 800;
				}
				if (workWidth > screenRect.width) {
					workWidth = screenRect.width;
					posX = screenRect.x;
				} else {
					posX = ((screenRect.width - workWidth) / 2) + screenRect.x;
				}
			} else {
				workWidth = this.getPreferredSize().width;
				posX = this.getLocation().x;
			}
		}
		posY = ((screenRect.height - workHeight) / 2) + screenRect.y;
		this.setPreferredSize(new Dimension(workWidth, workHeight+100));
		this.setLocation(posX, posY);

		///////////////////////////////////////
		// Arrange components and show panel //
		///////////////////////////////////////
		this.pack();
		super.setVisible(true);
	}

	private void showLayoutOfPanel(NodeList functionFieldList, boolean isOnEditablePanel) {
		///////////////////////////////////////////////////////////
		// Set panel size and position according specified sizes //
		///////////////////////////////////////////////////////////
		if (!functionElement.getAttribute("Size").equals("")
				&& !functionElement.getAttribute("Size").equals("AUTO")) {
			workTokenizer = new StringTokenizer(functionElement.getAttribute("Size"), ";" );
			int width = Integer.parseInt(workTokenizer.nextToken());
			int height = Integer.parseInt(workTokenizer.nextToken());
			this.setPreferredSize(new Dimension(width, height));
			int posX = ((screenRect.width - width) / 2) + screenRect.x;
			int posY = ((screenRect.height - height) / 2) + screenRect.y;
			this.setLocation(posX, posY);
			this.pack();
		}

		//////////////////////////////////////////////
		// Setup the primary table and refer tables //
		//////////////////////////////////////////////
		primaryTable = new DialogCheckLayoutPrimaryTable(functionElement, this);
		NodeList referNodeList = primaryTable.getTableElement().getElementsByTagName("Refer");
		sortableList = editor.getSortedListModel(referNodeList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(i);
			referTableList.add(new DialogCheckLayoutReferTable(element));
		}

		///////////////////////////
		// Setup fields on panel //
		///////////////////////////
		jPanelMain.removeAll();
		jScrollPane.getViewport().add(jPanelMain, null);
		Dimension dimOfPriviousField = new Dimension(0,0);
		Dimension dim = new Dimension(0,0);
		int posX = 0;
		int posY = 0;
		int biggestWidth = 300;
		int biggestHeight = 50;
		boolean firstVisibleField = true;
		DialogCheckLayoutField field;
		sortableList = editor.getSortedListModel(functionFieldList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			field = new DialogCheckLayoutField((org.w3c.dom.Element)sortableList.getElementAt(i), isOnEditablePanel, this);
			if (firstVisibleField) {
				posX = 0;
				posY = FIELD_VERTICAL_MARGIN + 3;
				firstVisibleField = false;
			} else {
				if (field.isHorizontal()) {
					posX = posX + dimOfPriviousField.width + field.getPositionMargin() + FIELD_HORIZONTAL_MARGIN;
				} else {
					posX = 0;
					posY = posY + dimOfPriviousField.height + field.getPositionMargin() + FIELD_VERTICAL_MARGIN;
				}
			}
			dim = field.getPreferredSize();
			field.setBounds(posX, posY, dim.width, dim.height);
			if (posX + dim.width > biggestWidth) {
				biggestWidth = posX + dim.width;
			}
			if (posY + dim.height > biggestHeight) {
				biggestHeight = posY + dim.height;
			}
			if (field.isHorizontal()) {
				dimOfPriviousField = new Dimension(dim.width, FIELD_UNIT_HEIGHT);
			} else {
				dimOfPriviousField = new Dimension(dim.width, dim.height);
			}
			jPanelMain.add(field);
		}
		jPanelMain.setPreferredSize(new Dimension(biggestWidth, biggestHeight));

		///////////////////////////////////////////////////////////////
		// Adjust panel size and position according to fields layout //
		///////////////////////////////////////////////////////////////
		int workWidth = biggestWidth + 50;
		if (functionElement.getAttribute("Size").equals("")) {
			workWidth = screenRect.width;
			posX = screenRect.x;
		} else {
			if (functionElement.getAttribute("Size").equals("AUTO")) {
				if (workWidth < 800) {
					workWidth = 800;
				}
				if (workWidth > screenRect.width) {
					workWidth = screenRect.width;
					posX = screenRect.x;
				} else {
					posX = ((screenRect.width - workWidth) / 2) + screenRect.x;
				}
			} else {
				workWidth = this.getPreferredSize().width;
				posX = this.getLocation().x;
			}
		}
		int workHeight = biggestHeight + 60;
		if (workHeight > screenRect.height) {
			workHeight = screenRect.height;
			posY = screenRect.y;
		} else {
			posY = ((screenRect.height - workHeight) / 2) + screenRect.y;
		}
		this.setPreferredSize(new Dimension(workWidth, workHeight));
		this.setLocation(posX, posY);

		///////////////////////////////////////
		// Arrange components and show panel //
		///////////////////////////////////////
		this.pack();
		super.setVisible(true);
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			cancel();
		}
		super.processWindowEvent(e);
	}

	private void cancel() {
		dispose();
	}
	
	public org.w3c.dom.Element getFunctionElement() {
		return functionElement;
	}

	public Editor getEditor() {
		return editor;
	}

	public DialogCheckLayoutPrimaryTable getPrimaryTable() {
		return primaryTable;
	}

	public ArrayList<DialogCheckLayoutReferTable> getReferTableList() {
		return referTableList;
	}

	public String getTableIDOfTableAlias(String tableAlias) {
		String tableID = tableAlias;
		DialogCheckLayoutReferTable referTable;
		for (int j = 0; j < referTableList.size(); j++) {
			referTable = referTableList.get(j);
			referTable.getTableAlias();
			if (referTable.getTableAlias().equals(tableAlias)) {
				tableID = referTable.getTableID();
				break;
			}
		}
		for (int j = 0; j < referTableList2.size(); j++) {
			referTable = referTableList2.get(j);
			referTable.getTableAlias();
			if (referTable.getTableAlias().equals(tableAlias)) {
				tableID = referTable.getTableID();
				break;
			}
		}
		return tableID;
	}
	
	public int getFieldUnitHeight() {
		return FIELD_UNIT_HEIGHT;
	}
	
	public String getStringData(String type, int dataSize, int decimal, boolean isAcceptMinus) {
		String value = "";
		StringBuffer bf = new StringBuffer();
		if (type.equals("KANJI")) {
			for (int i = 0; i < dataSize; i++) {
				bf.append("Ｘ");
			}
			value = bf.toString();
		}
		if (type.equals("STRING")) {
			for (int i = 0; i < dataSize; i++) {
				bf.append("X");
			}
			value = bf.toString();
		}
		if (type.equals("NUMBER")) {
			if (decimal > 0) {
				for (int i = 0; i < decimal; i++) {
					bf.append("9");
				}
				bf.append(".");
			}
			int intSize = dataSize - decimal;
			for (int i = 1; i <= intSize; i++) {
				bf.append("9");
				if (i % 3 == 0 && i < intSize) {
					bf.append(",");
				}
			}
			if (isAcceptMinus) {
				bf.append("-");
			}
			value = bf.reverse().toString();
		}
		return value;
	}

	public ImageIcon getCheckBoxIcon(String type) {
		ImageIcon image = null;
		if (type.equals("0A")) {
			image = ICON_CHECK_0A; 
		}
		if (type.equals("1A")) {
			image = ICON_CHECK_1A; 
		}
		if (type.equals("0D")) {
			image = ICON_CHECK_0D; 
		}
		if (type.equals("1D")) {
			image = ICON_CHECK_1D; 
		}
		if (type.equals("0R")) {
			image = ICON_CHECK_0R; 
		}
		if (type.equals("1R")) {
			image = ICON_CHECK_1R; 
		}
		return image;
	}
	
	public String getDateValue(String dateFormat) {
		String value = "";
		if (dateFormat.equals("en00")) {
			value = "06/17/10";
		}
		if (dateFormat.equals("en01")) {
			value = "Thur,06/17/01";
		}
		if (dateFormat.equals("en10")) {
			value = "Jun17,2010";
		}
		if (dateFormat.equals("en11")) {
			value = "Thur,Jun17,2001";
		}
		if (dateFormat.equals("jp00")) {
			value = "10/06/17";
		}
		if (dateFormat.equals("jp01")) {
			value = "10/06/17(木)";
		}
		if (dateFormat.equals("jp10")) {
			value = "2010/06/17";
		}
		if (dateFormat.equals("jp11")) {
			value = "2010/06/17(木)";
		}
		if (dateFormat.equals("jp20")) {
			value = "2010年6月17日";
		}
		if (dateFormat.equals("jp21")) {
			value = "2010年6月17日(木)";
		}
		if (dateFormat.equals("jp30")) {
			value = "H22/06/17";
		}
		if (dateFormat.equals("jp31")) {
			value = "H22/06/17(木)";
		}
		if (dateFormat.equals("jp40")) {
			value = "H22年06月17日";
		}
		if (dateFormat.equals("jp41")) {
			value = "H22年06月17日(木)";
		}
		if (dateFormat.equals("jp50")) {
			value = "平成22年06月17日";
		}
		if (dateFormat.equals("jp51")) {
			value = "平成22年06月17日(木)";
		}
		return value;
	}
	
	public String getUserExpressionOfYearMonth(String yearMonth, String dateFormat) {
		String result = "";
		//
		if (yearMonth.length() == 6) {
			int year = Integer.parseInt(yearMonth.substring(0, 4));
			int month = Integer.parseInt(yearMonth.substring(4, 6)) - 1;
			SimpleDateFormat formatter;
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, 1);
			//
			if (dateFormat.equals("en00")
					|| dateFormat.equals("en01")
					|| dateFormat.equals("en10")
					|| dateFormat.equals("en11")) {
				formatter = new SimpleDateFormat("MMM,yyyy", new Locale("en", "US", "US"));
				result = formatter.format(cal.getTime());
			}
			//
			if (dateFormat.equals("jp00")
					|| dateFormat.equals("jp01")
					|| dateFormat.equals("jp10")
					|| dateFormat.equals("jp11")
					|| dateFormat.equals("jp20")
					|| dateFormat.equals("jp21")) {
				formatter = new SimpleDateFormat("yyyy年MM月", new Locale("ja", "US", "US"));
				result = formatter.format(cal.getTime());
			}
			//
			if (dateFormat.equals("jp30")
					|| dateFormat.equals("jp31")
					|| dateFormat.equals("jp40")
					|| dateFormat.equals("jp41")
					|| dateFormat.equals("jp50")
					|| dateFormat.equals("jp51")) {
				formatter = new SimpleDateFormat("Gyy年MM月", new Locale("ja", "JP", "JP"));
				result = formatter.format(cal.getTime());
			}
		}
		//
		if (yearMonth.length() == 4) {
			int year = Integer.parseInt(yearMonth.substring(0, 4));
			int month = 0;
			SimpleDateFormat formatter;
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, 1);
			//
			if (dateFormat.equals("en00")
					|| dateFormat.equals("en01")
					|| dateFormat.equals("en10")
					|| dateFormat.equals("en11")) {
				formatter = new SimpleDateFormat("fiscal yyyy", new Locale("en", "US", "US"));
				result = formatter.format(cal.getTime());
			}
			//
			if (dateFormat.equals("jp00")
					|| dateFormat.equals("jp01")
					|| dateFormat.equals("jp10")
					|| dateFormat.equals("jp11")
					|| dateFormat.equals("jp20")
					|| dateFormat.equals("jp21")) {
				formatter = new SimpleDateFormat("yyyy年度", new Locale("ja", "US", "US"));
				result = formatter.format(cal.getTime());
			}
			//
			if (dateFormat.equals("jp30")
					|| dateFormat.equals("jp31")
					|| dateFormat.equals("jp40")
					|| dateFormat.equals("jp41")
					|| dateFormat.equals("jp50")
					|| dateFormat.equals("jp51")) {
				formatter = new SimpleDateFormat("Gyy年度", new Locale("ja", "JP", "JP"));
				result = formatter.format(cal.getTime());
			}
		}
		//
		return result;
	}
}

class DialogCheckLayoutColumn extends Object {
	private static final long serialVersionUID = 1L;
	org.w3c.dom.Element functionFieldElement_ = null;
	org.w3c.dom.Element tableElement = null;
	private String fieldName = "";
	private String dataSourceName = "";
	private String tableID_ = "";
	private String tableAlias_ = "";
	private String fieldID_ = "";
	private String fieldCaption = "";
	private String dataType = "";
	private int dataSize = 5;
	private int decimalSize = 0;
	private int fieldWidth = 50;
	private String dataTypeOptions = "";
	private ArrayList<String> dataTypeOptionList;
	private String fieldOptions = "";
	private ArrayList<String> fieldOptionList;
	private DialogCheckLayout dialog_;
	private Object value = "";

	public DialogCheckLayoutColumn(org.w3c.dom.Element functionFieldElement, DialogCheckLayout dialog){
		super();
		//
		dialog_ = dialog;
		functionFieldElement_ = functionFieldElement;
		fieldOptions = functionFieldElement_.getAttribute("FieldOptions");
		fieldOptionList = dialog_.getEditor().getOptionList(fieldOptions);
		//
		dataSourceName = functionFieldElement_.getAttribute("DataSource");
		StringTokenizer workTokenizer = new StringTokenizer(dataSourceName, "." );
		tableAlias_ = workTokenizer.nextToken();
		tableID_ = dialog_.getTableIDOfTableAlias(tableAlias_);
		fieldID_ =workTokenizer.nextToken();
		//
		org.w3c.dom.Element workElement = dialog_.getEditor().getSpecificFieldElement(tableID_, fieldID_);
		fieldName = workElement.getAttribute("Name");
		dataType = workElement.getAttribute("Type");
		dataTypeOptions = workElement.getAttribute("TypeOptions");
		dataTypeOptionList = dialog_.getEditor().getOptionList(dataTypeOptions);
		if (workElement.getAttribute("Name").equals("")) {
			fieldCaption = workElement.getAttribute("ID");
		} else {
			fieldCaption = fieldName;
		}
		dataSize = Integer.parseInt(workElement.getAttribute("Size"));
		if (dataSize > 50) {
			dataSize = 50;
		}
		if (!workElement.getAttribute("Decimal").equals("")) {
			decimalSize = Integer.parseInt(workElement.getAttribute("Decimal"));
		}
		//
		JLabel jLabel = new JLabel();
		FontMetrics metrics = jLabel.getFontMetrics(new java.awt.Font("Dialog", 0, 14));
		String wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "CAPTION");
		if (!wrkStr.equals("")) {
			fieldCaption = wrkStr;
		}
		int captionWidth = metrics.stringWidth(getLongestSegment(fieldCaption)) + 18;
		//
		String dateFormat = dialog.getEditor().getDateFormat();
		String language = dateFormat.substring(0, 2);
		String basicType = this.getBasicType();
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "KUBUN");
		if (!wrkStr.equals("")) {
			try {
				String wrk = "";
				StringBuffer buf1 = new StringBuffer();
				buf1.append("select * from ");
				buf1.append(dialog_.getEditor().getSystemUserVariantsTableID());
				buf1.append(" where IDUSERKUBUN = '");
				buf1.append(wrkStr);
				buf1.append("'");
				Connection connection = dialog_.getEditor().getDatabaseConnection("");
				if (connection != null && !connection.isClosed()) {
					Statement statement = connection.createStatement();
					ResultSet result = statement.executeQuery(buf1.toString());
					while (result.next()) {
						wrk = result.getString("TXUSERKUBUN").trim();
						if (value.equals("")) {
							value = wrk;
						}
						if (metrics.stringWidth(wrk)+10 > fieldWidth) {
							fieldWidth = metrics.stringWidth(wrk)+10;
							value = wrk;
						}
					}
				}
			} catch(Exception e) {
			}
		} else {
			if (dataTypeOptionList.contains("KANJI") || dataTypeOptionList.contains("ZIPADRS")) {
				fieldWidth = dataSize * 14 + 5;
				value = dialog_.getStringData("KANJI", dataSize, 0, false);
			} else {
				if (dataTypeOptionList.contains("FYEAR")) {
					fieldWidth = 85;
					if (language.equals("en")
							|| dateFormat.equals("jp00")
							|| dateFormat.equals("jp01")
							|| dateFormat.equals("jp10")
							|| dateFormat.equals("jp11")
							|| dateFormat.equals("jp20")
							|| dateFormat.equals("jp21")) {
						value = dialog.getUserExpressionOfYearMonth("9999", dateFormat);
					} else {
						value = "H99";
					}
				} else {
					if (dataTypeOptionList.contains("YMONTH")) {
						fieldWidth = 85;
						if (language.equals("en")
								|| dateFormat.equals("jp00")
								|| dateFormat.equals("jp01")
								|| dateFormat.equals("jp10")
								|| dateFormat.equals("jp11")
								|| dateFormat.equals("jp20")
								|| dateFormat.equals("jp21")) {
							value = "9999";
						} else {
							value = "H99";
						}
					} else {
						if (dataTypeOptionList.contains("MSEQ")) {
							fieldWidth = 50;
							if (language.equals("jp")) {
								value = "１月度";
							} else {
								value = "Jan";
							}
						} else {
							if (basicType.equals("INTEGER") || basicType.equals("FLOAT")) {
								String stringValue = dialog_.getStringData("NUMBER", dataSize, decimalSize, dataTypeOptionList.contains("ACCEPT_MINUS"));
								fieldWidth = stringValue.length() * 7 + 21;
								value = stringValue;
							} else {
								if (basicType.equals("DATE")) {
									String stringValue = dialog.getDateValue(dialog.getEditor().getDateFormat());
									fieldWidth = metrics.stringWidth(stringValue) + 10;
									value = stringValue;
								} else {
									if (dataTypeOptionList.contains("IMAGE")) {
										fieldWidth = 60;
										value = "";
									} else {
										fieldWidth = dataSize * 7 + 15;
										value = dialog_.getStringData("STRING", dataSize, 0, false);
									}
								}
							}
						}
					}
				}
			}
		}
		//
		if (fieldWidth > 320) {
			fieldWidth = 320;
		}
		//
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "WIDTH");
		if (!wrkStr.equals("")) {
			fieldWidth = Integer.parseInt(wrkStr);
		}
		//
		if (captionWidth > fieldWidth) {
			fieldWidth = captionWidth;
		}
	}

	public DialogCheckLayoutHorizontalAlignmentHeaderRenderer getHeaderRenderer(){
		DialogCheckLayoutHorizontalAlignmentHeaderRenderer renderer = null;
		if (this.getBasicType().equals("INTEGER") || this.getBasicType().equals("FLOAT")) {
			if (dataTypeOptionList.contains("MSEQ") || dataTypeOptionList.contains("FYEAR")) {
				renderer = new DialogCheckLayoutHorizontalAlignmentHeaderRenderer(SwingConstants.LEFT);
			} else {
				renderer = new DialogCheckLayoutHorizontalAlignmentHeaderRenderer(SwingConstants.RIGHT);
			}
		} else {
			renderer = new DialogCheckLayoutHorizontalAlignmentHeaderRenderer(SwingConstants.LEFT);
		}
		return renderer;
	}

	public TableCellRenderer getCellRenderer(){
		TableCellRenderer renderer = null;
		//
		String wrkStr = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "BOOLEAN");
		if (wrkStr.equals("")) {
			renderer = new DialogCheckLayoutTableCellRenderer();
			if (this.getBasicType().equals("INTEGER") || this.getBasicType().equals("FLOAT")) {
				if (dataTypeOptionList.contains("MSEQ") || dataTypeOptionList.contains("FYEAR")) {
					((DialogCheckLayoutTableCellRenderer)renderer).setHorizontalAlignment(SwingConstants.LEFT);
				} else {
					((DialogCheckLayoutTableCellRenderer)renderer).setHorizontalAlignment(SwingConstants.RIGHT);
				}
			} else {
				((DialogCheckLayoutTableCellRenderer)renderer).setHorizontalAlignment(SwingConstants.LEFT);
			}
		} else {
			renderer = new DialogCheckLayoutTableCellRendererWithCheckBox(dataTypeOptions);
			((DialogCheckLayoutTableCellRendererWithCheckBox)renderer).setHorizontalAlignment(SwingConstants.CENTER);
		}
		//
		return renderer;
	}
	
	public String getCaption() {
		return fieldCaption;
	}
	
	public int getWidth() {
		return fieldWidth;
	}

	public String getBasicType(){
		return dialog_.getEditor().getBasicTypeOf(dataType);
	}

	public Object getValue() {
		return value;
	}

	public boolean isVisibleOnPanel(int ext){
		boolean isVisibleOnPanel = false;
		if (fieldOptionList.contains("LIST1") && ext == 1) {
			isVisibleOnPanel = true;
		}
		if (fieldOptionList.contains("LIST2") && ext == 2) {
			isVisibleOnPanel = true;
		}
		if (!fieldOptionList.contains("LIST1") && !fieldOptionList.contains("LIST2")) {
			isVisibleOnPanel = true;
		}
		return isVisibleOnPanel;
	}
	
	private String getLongestSegment(String caption) {
		String value = "";
		ArrayList<String> stringList = new ArrayList<String>();
		String wrkStr = caption.toUpperCase();
		wrkStr = wrkStr.replace("<HTML>", "");
		wrkStr = wrkStr.replace("</HTML>", "");
		StringTokenizer workTokenizer = new StringTokenizer(wrkStr, "<BR>");
		while (workTokenizer.hasMoreTokens()) {
			stringList.add(workTokenizer.nextToken());
		}
		for (int i = 0; i < stringList.size(); i++) {
			if (stringList.get(i).length() > value.length()) {
				value = stringList.get(i);
			}
		}
		return value;
	}
}

class DialogCheckLayoutField extends JPanel {
	private static final long serialVersionUID = 1L;
	org.w3c.dom.Element functionFieldElement_ = null;
	org.w3c.dom.Element tableElement = null;
	private String fieldName = "";
	private String fieldRemarks = "";
	private String dataSourceName = "";
	private String tableID_ = "";
	private String tableAlias_ = "";
	private String fieldID_ = "";
	private String fieldCaption = "";
	private String dataType = "";
	private int dataSize = 5;
	private int decimalSize = 0;
	private String dataTypeOptions = "";
	private ArrayList<String> dataTypeOptionList;
	private String fieldOptions = "";
	private ArrayList<String> fieldOptionList;
	private int fieldRows = 1;
	private JPanel jPanelField = new JPanel();
	private JLabel jLabelField = new JLabel();
	private JPanel jPanelFieldComment = null;
	private JLabel jLabelFieldComment = null;
	private JButton jButtonToRefferZipNo = null;
	private boolean isEditable = true;
	private boolean isHorizontal = false;
	private boolean isAutoDetailRowNumber = false;
	private int positionMargin = 0;
	private JComponent component = new JTextField("TEST");
	private DialogCheckLayout dialog_;
	
	public DialogCheckLayoutField(org.w3c.dom.Element functionFieldElement,	boolean isOnEditablePanel, DialogCheckLayout dialog){
		super();
		//
		dialog_ = dialog;
		functionFieldElement_ = functionFieldElement;
		fieldOptions = functionFieldElement_.getAttribute("FieldOptions");
		fieldOptionList = dialog_.getEditor().getOptionList(fieldOptions);
		//
		dataSourceName = functionFieldElement_.getAttribute("DataSource");
		StringTokenizer workTokenizer = new StringTokenizer(dataSourceName, "." );
		tableAlias_ = workTokenizer.nextToken();
		tableID_ = dialog_.getTableIDOfTableAlias(tableAlias_);
		fieldID_ =workTokenizer.nextToken();
		String wrkStr;
		//
		if (isOnEditablePanel) {
			if (tableID_.equals(dialog_.getPrimaryTable().getTableID()) && tableAlias_.equals(tableID_)) {
				isEditable = true;
				for (int i = 0; i < dialog_.getPrimaryTable().getKeyFieldList().size(); i++) {
					if (dialog_.getPrimaryTable().getKeyFieldList().get(i).equals(fieldID_)) {
						isEditable = false;
						break;
					}
				}
			}
		} else {
			isEditable = false;
		}
		//
		if (fieldOptionList.contains("HORIZONTAL")) {
			isHorizontal = true;
		}
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "HORIZONTAL");
		if (!wrkStr.equals("")) {
			isHorizontal = true;
			positionMargin = Integer.parseInt(wrkStr);
		}
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "VERTICAL");
		if (!wrkStr.equals("")) {
			positionMargin = Integer.parseInt(wrkStr);
		}
		//
		org.w3c.dom.Element workElement = dialog_.getEditor().getSpecificFieldElement(tableID_, fieldID_);
		fieldName = workElement.getAttribute("Name");
		fieldRemarks = dialog_.getEditor().substringLinesWithTokenOfEOL(workElement.getAttribute("Remarks"), "<br>");
		dataType = workElement.getAttribute("Type");
		dataTypeOptions = workElement.getAttribute("TypeOptions");
		dataTypeOptionList = dialog_.getEditor().getOptionList(dataTypeOptions);
		if (workElement.getAttribute("Name").equals("")) {
			fieldCaption = workElement.getAttribute("ID");
		} else {
			fieldCaption = fieldName;
		}
		dataSize = Integer.parseInt(workElement.getAttribute("Size"));
		if (!workElement.getAttribute("Decimal").equals("")) {
			decimalSize = Integer.parseInt(workElement.getAttribute("Decimal"));
		}
		//
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "CAPTION");
		if (!wrkStr.equals("")) {
			fieldCaption = wrkStr;
		}
		jLabelField.setText(fieldCaption);
		jLabelField.setFocusable(false);
		jLabelField.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelField.setVerticalAlignment(SwingConstants.TOP);
		jLabelField.setFont(new java.awt.Font("Dialog", 0, 14));
		FontMetrics metrics = jLabelField.getFontMetrics(new java.awt.Font("Dialog", 0, 14));
		if (fieldOptionList.contains("CAPTION_LENGTH_VARIABLE")) {
			jLabelField.setPreferredSize(new Dimension(metrics.stringWidth(fieldCaption), dialog_.getFieldUnitHeight()));
		} else {
			jLabelField.setPreferredSize(new Dimension(120, dialog_.getFieldUnitHeight()));
			if (metrics.stringWidth(fieldCaption) > 120) {
				jLabelField.setFont(new java.awt.Font("Dialog", 0, 12));
				metrics = jLabelField.getFontMetrics(new java.awt.Font("Dialog", 0, 12));
				if (metrics.stringWidth(fieldCaption) > 120) {
					jLabelField.setFont(new java.awt.Font("Dialog", 0, 10));
				}
			}
		}
		//
		component.setPreferredSize(new Dimension(120, dialog_.getFieldUnitHeight()));
		//
		if (fieldOptionList.contains("PROMPT_LIST")) {
			DialogCheckLayoutReferTable referTable = null;
			ArrayList<DialogCheckLayoutReferTable> referTableList = dialog_.getReferTableList();
			for (int i = 0; i < referTableList.size(); i++) {
				if (referTableList.get(i).getTableID().equals(tableID_)) {
					if (referTableList.get(i).getTableAlias().equals("") || referTableList.get(i).getTableAlias().equals(tableAlias_)) {
						referTable = referTableList.get(i);
						break;
					}
				}
			}
			component = new DialogCheckLayoutComboBox(functionFieldElement_, dataTypeOptions, referTable, dialog_);
		} else {
			wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "PROMPT_CALL");
			if (!wrkStr.equals("")) {
				component = new DialogCheckLayoutPromptCallField(functionFieldElement, isEditable, dialog_);
			} else {
				wrkStr = dialog.getFunctionElement().getAttribute("Type"); 
				if ((wrkStr.equals("XF200") || wrkStr.equals("XF310"))
						&& 	(!dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "KUBUN").equals("")
							|| !dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "VALUES").equals(""))) {
					component = new DialogCheckLayoutComboBox(functionFieldElement_, dataTypeOptions, null, dialog_);
				} else {
					if (!dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "BOOLEAN").equals("")) {
						component = new DialogCheckLayoutCheckBox(isEditable, dialog_);
					} else {
						if (dataType.equals("VARCHAR") || dataType.equals("LONG VARCHAR")) {
							DialogCheckLayoutTextArea textarea = new DialogCheckLayoutTextArea(dataSize, dataTypeOptions, fieldOptions, isEditable, dialog_);
							fieldRows = textarea.getRows();
							component = textarea;
						} else {
							if (dataTypeOptionList.contains("URL")) {
								component = new DialogCheckLayoutUrlField(dataSize, isEditable, dialog_);
							} else {
								if (dataTypeOptionList.contains("IMAGE")) {
									DialogCheckLayoutImageField imagefield = new DialogCheckLayoutImageField(fieldOptions, dataSize, isEditable, dialog_);
									fieldRows = imagefield.getRows();
									component = imagefield;
								} else {
									if (dataType.equals("DATE")) {
										component = new DialogCheckLayoutDateField(isEditable, dialog_);
									} else {
										if (dataTypeOptionList.contains("YMONTH")) {
											component = new DialogCheckLayoutYMonthBox(isEditable, dialog_);
										} else {
											if (dataTypeOptionList.contains("MSEQ")) {
												component = new DialogCheckLayoutMSeqBox(isEditable, dialog_);
											} else {
												if (dataTypeOptionList.contains("FYEAR")) {
													component = new DialogCheckLayoutFYearBox(isEditable, dialog_);
												} else {
													if (isAutoDetailRowNumber) {
														if (dataSize < 5) { 
															component = new DialogCheckLayoutTextField("STRING", 5, 0, "", "", isEditable, dialog_);
														} else {
															component = new DialogCheckLayoutTextField("STRING", dataSize, 0, "", "", isEditable, dialog_);
														}
													} else {
														component = new DialogCheckLayoutTextField(this.getBasicType(), dataSize, decimalSize, dataTypeOptions, fieldOptions, isEditable, dialog_);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		component.setBounds(new Rectangle(5, 0, component.getPreferredSize().width, component.getPreferredSize().height));
		//
		jPanelField.setLayout(null);
		jPanelField.setPreferredSize(new Dimension(component.getPreferredSize().width + 5, component.getPreferredSize().height));
		jPanelField.add((JComponent)component);
		//
		this.setFocusable(false);
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(jPanelField, BorderLayout.CENTER);
		if (fieldOptionList.contains("NO_CAPTION")) {
			this.setPreferredSize(new Dimension(component.getWidth() + 10, component.getPreferredSize().height));
		} else {
			this.setPreferredSize(new Dimension(component.getWidth() + jLabelField.getPreferredSize().width + 10, component.getPreferredSize().height));
			this.add(jLabelField, BorderLayout.WEST);
		}
		//
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "COMMENT");
		if (!wrkStr.equals("")) {
			jLabelFieldComment = new JLabel();
			jLabelFieldComment.setText(" " + wrkStr);
			jLabelFieldComment.setForeground(Color.blue);
			jLabelFieldComment.setFont(new java.awt.Font("Dialog", 0, 12));
			jLabelFieldComment.setVerticalAlignment(SwingConstants.TOP);
			metrics = jLabelFieldComment.getFontMetrics(new java.awt.Font("Dialog", 0, 12));
			this.setPreferredSize(new Dimension(this.getPreferredSize().width + metrics.stringWidth(wrkStr) + 6, this.getPreferredSize().height));
		}
		//
		if (dataTypeOptionList.contains("ZIPADRS") && isOnEditablePanel) {
			jButtonToRefferZipNo = new JButton();
			jButtonToRefferZipNo.setText("<");
			jButtonToRefferZipNo.setFont(new java.awt.Font("SansSerif", 0, 9));
			jButtonToRefferZipNo.setPreferredSize(new Dimension(37, this.getPreferredSize().height));
			this.setPreferredSize(new Dimension(this.getPreferredSize().width + 34, this.getPreferredSize().height));
		}
		//
		if (jButtonToRefferZipNo != null || jLabelFieldComment != null) {
			jPanelFieldComment = new JPanel();
			jPanelFieldComment.setLayout(new BorderLayout());
			int width = 2;
			if (jButtonToRefferZipNo != null) {
				width = width + jButtonToRefferZipNo.getPreferredSize().width;
			}
			if (jLabelFieldComment == null) {
				this.add(jButtonToRefferZipNo, BorderLayout.EAST);
			} else {
				width = width + jLabelFieldComment.getPreferredSize().width + 3;
				jPanelFieldComment.add(jLabelFieldComment, BorderLayout.CENTER);
				if (jButtonToRefferZipNo != null) {
					jPanelFieldComment.add(jButtonToRefferZipNo, BorderLayout.WEST);
				}
				jPanelFieldComment.setPreferredSize(new Dimension(width, component.getHeight()));
				this.add(jPanelFieldComment, BorderLayout.EAST);
			}
		}
		//
		if (decimalSize > 0) {
			wrkStr = "<html>" + fieldName + " " + dataSourceName + " (" + dataSize + "," + decimalSize + ")<br>" + fieldRemarks;
		} else {
			wrkStr = "<html>" + fieldName + " " + dataSourceName + " (" + dataSize + ")<br>" + fieldRemarks;
		}
		this.setToolTipText(wrkStr);
		component.setToolTipText(wrkStr);
	}

	public boolean isHorizontal(){
		return isHorizontal;
	}

	public int getPositionMargin(){
		return positionMargin;
	}

	public int getRows(){
		return fieldRows;
	}

	public String getDataSourceName(){
		return dataSourceName;
	}

	public String getCaption(){
		return fieldCaption;
	}

	public String getBasicType(){
		return dialog_.getEditor().getBasicTypeOf(dataType);
	}
	
	public ArrayList<String> getTypeOptionList() {
		return dataTypeOptionList;
	}
	
	public ArrayList<String> getFieldOptionList() {
		return fieldOptionList;
	}
}

class DialogCheckLayoutComboBox extends JComboBox {
	private static final long serialVersionUID = 1L;
	private String dataTypeOptions_ = "";
	private String fieldOptions_ = "";
	private String tableID = "";
	private String tableAlias = "";
	private String fieldID = "";
	private JTextField jTextField = new JTextField();
	private DialogCheckLayoutReferTable referTable_ = null;
	private DialogCheckLayout dialog_;
	
	public DialogCheckLayoutComboBox(org.w3c.dom.Element functionFieldElement, String dataTypeOptions, DialogCheckLayoutReferTable referTable, DialogCheckLayout dialog){
		super();
		//
		StringTokenizer workTokenizer;
		org.w3c.dom.Element workElement;
		int fieldWidth = 0;
		String wrk = "";
		String strWrk;
		//
		fieldOptions_ = functionFieldElement.getAttribute("FieldOptions");
		//
		dataTypeOptions_ = dataTypeOptions;
		workTokenizer = new StringTokenizer(functionFieldElement.getAttribute("DataSource"), "." );
		tableAlias = workTokenizer.nextToken();
		tableID = tableAlias;
		referTable_ = referTable;
		if (referTable_ != null && referTable_.getTableAlias().equals(tableAlias)) {
			tableID = referTable_.getTableID();
		}
		fieldID =workTokenizer.nextToken();
		dialog_ = dialog;
		//
		jTextField.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		FontMetrics metrics = jTextField.getFontMetrics(new java.awt.Font("Dialog", 0, 14));
		this.setFont(new java.awt.Font("Dialog", 0, 14));
		this.setFocusable(false);
		//
		strWrk = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions_, "VALUES");
		if (!strWrk.equals("")) {
			workTokenizer = new StringTokenizer(strWrk, ";" );
			while (workTokenizer.hasMoreTokens()) {
				wrk = workTokenizer.nextToken();
				this.addItem(wrk);
				if (metrics.stringWidth(wrk) > fieldWidth) {
					fieldWidth = metrics.stringWidth(wrk);
				}
			}
			fieldWidth = fieldWidth + 30;
		} else {
			strWrk = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions_, "KUBUN");
			if (!strWrk.equals("")) {
				try {
					StringBuffer buf1 = new StringBuffer();
					buf1.append("select * from ");
					buf1.append(dialog_.getEditor().getSystemUserVariantsTableID());
					buf1.append(" where IDUSERKUBUN = '");
					buf1.append(strWrk);
					buf1.append("' order by SQLIST");
					Connection connection = dialog_.getEditor().getDatabaseConnection("");
					if (connection != null && !connection.isClosed()) {
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(buf1.toString());
						while (result.next()) {
							wrk = result.getString("TXUSERKUBUN").trim();
							this.addItem(wrk);
							if (metrics.stringWidth(wrk) > fieldWidth) {
								fieldWidth = metrics.stringWidth(wrk);
							}
						}
					}
					fieldWidth = fieldWidth + 30;
				} catch(Exception e) {
				}
			} else {
				if (referTable_ != null) {
					workElement = dialog_.getEditor().getSpecificFieldElement(tableID, fieldID);
					ArrayList<String> workDataTypeOptionList = dialog_.getEditor().getOptionList(workElement.getAttribute("TypeOptions"));
					int dataSize = Integer.parseInt(workElement.getAttribute("Size"));
					if (workDataTypeOptionList.contains("KANJI") || workDataTypeOptionList.contains("ZIPADRS")) {
						fieldWidth = dataSize * 14 + 20;
						this.addItem(dialog_.getStringData("KANJI", dataSize, 0, false));
					} else {
						fieldWidth = dataSize * 7 + 30;
						this.addItem(dialog_.getStringData("STRING", dataSize, 0, false));
					}
					if (fieldWidth > 800) {
						fieldWidth = 800;
					}
				}
			}
		}
		strWrk = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions_, "WIDTH");
		if (strWrk.equals("")) {
			this.setPreferredSize(new Dimension(fieldWidth, dialog_.getFieldUnitHeight()));
		} else {
			this.setPreferredSize(new Dimension(Integer.parseInt(strWrk), dialog_.getFieldUnitHeight()));
		}
	}
}

class DialogCheckLayoutTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	private String basicType_ = "";
	private int digits_ = 5;
	private int decimal_ = 0;
	private ArrayList<String> dataTypeOptionList;
	private String fieldOptions_;
	private DialogCheckLayout dialog_;
	//
	public DialogCheckLayoutTextField(String basicType, int digits, int decimal, String dataTypeOptions, String fieldOptions, boolean isEditable, DialogCheckLayout dialog) {
		super();
		//
		dialog_ = dialog;
		basicType_ = basicType;
		digits_ = digits;
		decimal_ = decimal;
		dataTypeOptionList = dialog_.getEditor().getOptionList(dataTypeOptions);
		fieldOptions_ = fieldOptions;
		//
		if (basicType_.equals("INTEGER") || basicType_.equals("FLOAT")) {
			this.setHorizontalAlignment(SwingConstants.RIGHT);
		} else {
			this.setHorizontalAlignment(SwingConstants.LEFT);
		}
		this.setFont(new java.awt.Font("Monospaced", 0, 14));
		this.setFocusable(false);
		//
		String wrkStr, value = "";
		int fieldHeight, fieldWidth = 0;
		if (dataTypeOptionList.contains("KANJI") || dataTypeOptionList.contains("ZIPADRS")) {
			value = dialog_.getStringData("KANJI", digits, 0, false);
			fieldWidth = digits_ * 14 + 10;
		} else {
			wrkStr = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "KUBUN");
			if (!wrkStr.equals("")) {
				try {
					FontMetrics metrics = this.getFontMetrics(new java.awt.Font("Dialog", 0, 14));
					StringBuffer buf1 = new StringBuffer();
					buf1.append("select * from ");
					buf1.append(dialog_.getEditor().getSystemUserVariantsTableID());
					buf1.append(" where IDUSERKUBUN = '");
					buf1.append(wrkStr);
					buf1.append("' order by SQLIST");
					Connection connection = dialog_.getEditor().getDatabaseConnection("");
					if (connection != null && !connection.isClosed()) {
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(buf1.toString());
						while (result.next()) {
							wrkStr = result.getString("TXUSERKUBUN").trim();
							if (metrics.stringWidth(wrkStr) > fieldWidth) {
								fieldWidth = metrics.stringWidth(wrkStr);
							}
							if (value.equals("")) {
								value = wrkStr;
							}
						}
					}
					fieldWidth = fieldWidth + 10;
				} catch(Exception e) {
				}
			} else {
				if (basicType_.equals("INTEGER") || basicType_.equals("FLOAT")) {
					value = dialog_.getStringData("NUMBER", digits, decimal_, dataTypeOptionList.contains("ACCEPT_MINUS"));
					//fieldWidth = value.length() * 7 + 21;
					fieldWidth = value.length() * 7 + 21;
				} else {
					value = dialog_.getStringData("STRING", digits, 0, false);
					fieldWidth = digits_ * 7 + 10;
				}
			}
		}
		this.setText(value);
		super.setEditable(isEditable);
		//
		if (fieldWidth > 800) {
			fieldWidth = 800;
		}
		fieldHeight = dialog_.getFieldUnitHeight();
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions_, "WIDTH");
		if (!wrkStr.equals("")) {
			fieldWidth = Integer.parseInt(wrkStr);
		}
		this.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	}
}

class DialogCheckLayoutUrlField extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField jTextField = new JTextField();
	private JLabel jLabel = new JLabel();
	public DialogCheckLayoutUrlField(int digits, boolean isEditable, DialogCheckLayout dialog){
		super();
		jTextField.setFont(new java.awt.Font("Monospaced", 0, 14));
		jTextField.setText(dialog.getStringData("STRING", digits, 0, false));
		jTextField.setFocusable(false);
		jLabel.setFont(new java.awt.Font("Monospaced", 0, 14));
		jLabel.setForeground(Color.blue);
		jLabel.setHorizontalAlignment(SwingConstants.LEFT);
		jLabel.setBorder(jTextField.getBorder());
		jLabel.setText("<html><ul>" + dialog.getStringData("STRING", digits, 0, false));
		this.setLayout(new BorderLayout());
		this.add(jTextField, BorderLayout.CENTER);
		int fieldWidth;
		fieldWidth = digits * 7 + 10;
		if (fieldWidth > 800) {
			fieldWidth = 800;
		}
		this.setPreferredSize(new Dimension(fieldWidth, dialog.getFieldUnitHeight()));
	}
}

class DialogCheckLayoutPromptCallField extends JPanel {
	private static final long serialVersionUID = 1L;
	private String tableID = "";
	private String tableAlias = "";
	private String fieldID = "";
	private DialogCheckLayoutTextField textField;
	private JButton jButton = new JButton();
    private DialogCheckLayout dialog_;
    private org.w3c.dom.Element fieldElement_;
    private ArrayList<DialogCheckLayoutReferTable> referTableList_;

    public DialogCheckLayoutPromptCallField(org.w3c.dom.Element fieldElement, boolean isEditable, DialogCheckLayout dialog){
		super();
		//
		fieldElement_ = fieldElement;
		dialog_ = dialog;
		//
		String fieldOptions = fieldElement_.getAttribute("FieldOptions");
		StringTokenizer workTokenizer = new StringTokenizer(fieldElement_.getAttribute("DataSource"), "." );
		tableAlias = workTokenizer.nextToken();
		fieldID =workTokenizer.nextToken();
		//
		tableID = tableAlias;
		referTableList_ = dialog_.getReferTableList();
		for (int i = 0; i < referTableList_.size(); i++) {
			if (referTableList_.get(i).getTableAlias().equals(tableAlias)) {
				tableID = referTableList_.get(i).getTableID();
				break;
			}
		}
		//
		org.w3c.dom.Element workElement = dialog_.getEditor().getSpecificFieldElement(tableID, fieldID);
		String dataType = workElement.getAttribute("Type");
		String dataTypeOptions = workElement.getAttribute("TypeOptions");
		int dataSize = Integer.parseInt(workElement.getAttribute("Size"));
		if (dataSize > 50) {
			dataSize = 50;
		}
		int decimalSize = 0;
		if (!workElement.getAttribute("Decimal").equals("")) {
			decimalSize = Integer.parseInt(workElement.getAttribute("Decimal"));
		}
		//
		textField = new DialogCheckLayoutTextField(dialog_.getEditor().getBasicTypeOf(dataType), dataSize, decimalSize, dataTypeOptions, fieldOptions, isEditable, dialog_);
		textField.setLocation(5, 0);
		//
		ImageIcon imageIcon = new ImageIcon(xeadEditor.Editor.class.getResource("prompt.png"));
	 	jButton.setIcon(imageIcon);
		jButton.setPreferredSize(new Dimension(26, dialog_.getFieldUnitHeight()));
		jButton.setFocusable(false);
		//
		this.setSize(new Dimension(textField.getWidth() + 27, dialog_.getFieldUnitHeight()));
		this.setLayout(new BorderLayout());
		this.add(textField, BorderLayout.CENTER);
		this.add(jButton, BorderLayout.EAST);
	}
}

class DialogCheckLayoutImageField extends JPanel {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private int rows_;
	private JTextField jTextField = new JTextField();
	private JScrollPane jScrollPane = new JScrollPane();
	private JPanel jPanelBottom = new JPanel();
	private JButton jButton = new JButton();
	private String fieldOptions_ = "";
    private Color normalModeColor = null;
    private static int DEFAULT_ROWS = 11;
    private static int DEFAULT_WIDTH = 400;
	private final int FIELD_VERTICAL_MARGIN = 5;

	public DialogCheckLayoutImageField(String fieldOptions, int size, boolean isEditable, DialogCheckLayout dialog){
		super();
		//
		fieldOptions_ = fieldOptions;
		//
		jTextField.setEditable(false);
		Border workBorder = jTextField.getBorder();
		normalModeColor = jTextField.getBackground();
		jTextField.setBorder(BorderFactory.createLineBorder(normalModeColor));
		jTextField.setBackground(Color.white);
		jTextField.setEditable(true);
		jTextField.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextField.setFocusable(false);
		jTextField.setText(dialog.getStringData("STRING", size, 0, false));
		//
		jButton.setFont(new java.awt.Font("Dialog", 0, 14));
		jButton.setPreferredSize(new Dimension(80, dialog.getFieldUnitHeight()));
		jButton.setText(res.getString("Refresh"));
		jButton.setFocusable(false);
		jPanelBottom.setPreferredSize(new Dimension(200, dialog.getFieldUnitHeight()));
	    jScrollPane.setBorder(null);
	    jPanelBottom.setLayout(new BorderLayout());
	    jPanelBottom.add(jTextField, BorderLayout.CENTER);
	    jPanelBottom.add(jButton, BorderLayout.EAST);
		//
		this.setFocusable(false);
	    this.setBorder(workBorder);
		this.setLayout(new BorderLayout());
		if (isEditable) {
			this.add(jPanelBottom, BorderLayout.SOUTH);
		}
		//
		rows_ = DEFAULT_ROWS;
		String wrkStr = dialog.getEditor().getOptionValueWithKeyword(fieldOptions_, "ROWS");
		if (!wrkStr.equals("")) {
			rows_ = Integer.parseInt(wrkStr);
		}
		int fieldHeight = rows_ * dialog.getFieldUnitHeight() - FIELD_VERTICAL_MARGIN - 3;
		int fieldWidth = DEFAULT_WIDTH;
		wrkStr = dialog.getEditor().getOptionValueWithKeyword(fieldOptions_, "WIDTH");
		if (!wrkStr.equals("")) {
			fieldWidth = Integer.parseInt(wrkStr);
		}
		//
		this.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	}
	
	public int getRows() {
		return rows_;
	}
}

class DialogCheckLayoutCheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;
	public DialogCheckLayoutCheckBox(boolean isEditable, DialogCheckLayout dialog){
		super();
	    this.setDisabledSelectedIcon(dialog.getCheckBoxIcon("1D"));
	    this.setDisabledIcon(dialog.getCheckBoxIcon("0D"));
	    this.setSelectedIcon(dialog.getCheckBoxIcon("1A"));
	    this.setIcon(dialog.getCheckBoxIcon("0A"));
	    this.setRolloverSelectedIcon(dialog.getCheckBoxIcon("1R"));
	    this.setRolloverIcon(dialog.getCheckBoxIcon("0R"));
		this.setText("");
		this.setSelected(true);
		this.setEnabled(isEditable);
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(20, dialog.getFieldUnitHeight()));
	}
}

class DialogCheckLayoutDateField extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField jTextField = new JTextField();
	private JButton jButton = new JButton();
	private JPanel jPanelDummy = new JPanel();
	public DialogCheckLayoutDateField(boolean isEditable, DialogCheckLayout dialog){
		super();
		jTextField.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextField.setEditable(false);
		FontMetrics metrics = jTextField.getFontMetrics(new java.awt.Font("Dialog", 0, 14));
		String value = dialog.getDateValue(dialog.getEditor().getDateFormat());
		int width = metrics.stringWidth(value) + 10;
		jTextField.setText(value);
		this.setPreferredSize(new Dimension(width + 26, dialog.getFieldUnitHeight()));
		this.setLayout(new BorderLayout());
		this.add(jTextField, BorderLayout.CENTER);
		if (isEditable) {
			ImageIcon imageIcon = new ImageIcon(xeadEditor.Editor.class.getResource("prompt.png"));
		 	jButton.setIcon(imageIcon);
			jButton.setPreferredSize(new Dimension(26, dialog.getFieldUnitHeight()));
			this.add(jButton, BorderLayout.EAST);
		} else {
			jPanelDummy.setPreferredSize(new Dimension(26, dialog.getFieldUnitHeight()));
			this.add(jPanelDummy, BorderLayout.EAST);
		}
	}
}

class DialogCheckLayoutTextArea extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private String fieldOptions_ = "";
	private JTextArea jTextArea = new JTextArea();
	private int rows_ = 2;
    private static Color ACTIVE_COLOR = SystemColor.white;
    private static Color INACTIVE_COLOR = SystemColor.control;
	public DialogCheckLayoutTextArea(int digits, String dataTypeOptions, String fieldOptions, boolean isEditable, DialogCheckLayout dialog){
		super();
		String wrkStr;
		fieldOptions_ = fieldOptions;
		jTextArea.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextArea.setLineWrap(true);
		jTextArea.setWrapStyleWord(true);
		jTextArea.setFocusable(false);
		jTextArea.setEditable(false);
		if (isEditable) {
			jTextArea.setBackground(ACTIVE_COLOR);
		} else {
			jTextArea.setBackground(INACTIVE_COLOR);
		}
		this.getViewport().add(jTextArea, null);
		//
		wrkStr = dialog.getEditor().getOptionValueWithKeyword(fieldOptions_, "ROWS");
		if (!wrkStr.equals("")) {
			rows_ = Integer.parseInt(wrkStr);
		}
		//
		int fieldWidth = 800;
		wrkStr = dialog.getEditor().getOptionValueWithKeyword(fieldOptions_, "WIDTH");
		if (!wrkStr.equals("")) {
			fieldWidth = Integer.parseInt(wrkStr);
		}
		//
		int fieldHeight = rows_ * dialog.getFieldUnitHeight();
		this.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	}
	public int getRows() {
		return rows_;
	}
}

class DialogCheckLayoutFYearBox extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField jTextField = new JTextField();
	private JComboBox jComboBoxYear = new JComboBox();
    private String dateFormat = "";
    private String language = "";
	
	public DialogCheckLayoutFYearBox(boolean isEditable, DialogCheckLayout dialog){
		super();
		dateFormat = dialog.getEditor().getDateFormat();
		language = dateFormat.substring(0, 2);
		//
		jTextField.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		jTextField.setBounds(new Rectangle(0, 0, 80, dialog.getFieldUnitHeight()));
		//
		jComboBoxYear.setFont(new java.awt.Font("Dialog", 0, 12));
		jComboBoxYear.setBounds(new Rectangle(0, 0, 80, dialog.getFieldUnitHeight()));
		if (language.equals("en")
				|| dateFormat.equals("jp00")
				|| dateFormat.equals("jp01")
				|| dateFormat.equals("jp10")
				|| dateFormat.equals("jp11")
				|| dateFormat.equals("jp20")
				|| dateFormat.equals("jp21")) {
			String wrkStr = dialog.getUserExpressionOfYearMonth("9999", dateFormat);
			jComboBoxYear.addItem(wrkStr);
			jTextField.setText(wrkStr);
		} else {
			jComboBoxYear.addItem("H99");
			jTextField.setText("H99");
		}
		//
		this.setLayout(null);
		this.setFocusable(false);
		if (isEditable) {
			this.add(jComboBoxYear);
		} else {
			this.add(jTextField);
		}
		this.setPreferredSize(new Dimension(80, dialog.getFieldUnitHeight()));
	}
}

class DialogCheckLayoutMSeqBox extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField jTextField = new JTextField();
	private JComboBox jComboBoxMSeq = new JComboBox();
    private String language = "";
	
	public DialogCheckLayoutMSeqBox(boolean isEditable, DialogCheckLayout dialog){
		super();
		language = dialog.getEditor().getDateFormat().substring(0, 2);
		jTextField.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		jComboBoxMSeq.setFont(new java.awt.Font("Dialog", 0, 12));
		if (language.equals("en")) {
			jComboBoxMSeq.setBounds(new Rectangle(0, 0, 50, dialog.getFieldUnitHeight()));
			jTextField.setBounds(new Rectangle(0, 0, 50, dialog.getFieldUnitHeight()));
			this.setSize(new Dimension(50, dialog.getFieldUnitHeight()));
			jComboBoxMSeq.addItem("Jan");
		}
		if (language.equals("jp")) {
			jComboBoxMSeq.setBounds(new Rectangle(0, 0, 62, dialog.getFieldUnitHeight()));
			jTextField.setBounds(new Rectangle(0, 0, 62, dialog.getFieldUnitHeight()));
			this.setSize(new Dimension(62, dialog.getFieldUnitHeight()));
			jComboBoxMSeq.addItem("１月度");
		}
		this.setLayout(null);
		this.setFocusable(false);
		if (isEditable) {
			this.add(jComboBoxMSeq);
		} else {
			this.add(jTextField);
		}
	}
}

class DialogCheckLayoutYMonthBox extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField jTextField = new JTextField();
	private JComboBox jComboBoxYear = new JComboBox();
	private JComboBox jComboBoxMonth = new JComboBox();
    private String dateFormat = "";
    private String language = "";
	
	public DialogCheckLayoutYMonthBox(boolean isEditable, DialogCheckLayout dialog){
		super();
		dateFormat = dialog.getEditor().getDateFormat();
		language = dateFormat.substring(0, 2);
		jTextField.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextField.setBounds(new Rectangle(0, 0, 85, dialog.getFieldUnitHeight()));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		//
		jComboBoxYear.setFont(new java.awt.Font("Dialog", 0, 14));
		jComboBoxYear.setFocusable(false);
		if (language.equals("en")
				|| dateFormat.equals("jp00")
				|| dateFormat.equals("jp01")
				|| dateFormat.equals("jp10")
				|| dateFormat.equals("jp11")
				|| dateFormat.equals("jp20")
				|| dateFormat.equals("jp21")) {
			jComboBoxYear.addItem("9999");
		} else {
			jComboBoxYear.addItem("H99");
		}
		jComboBoxMonth.setFont(new java.awt.Font("Dialog", 0, 14));
		jComboBoxMonth.setFocusable(false);
		if (language.equals("en")) {
			jComboBoxMonth.setBounds(new Rectangle(0, 0, 55, dialog.getFieldUnitHeight()));
			jComboBoxYear.setBounds(new Rectangle(56, 0, 60, dialog.getFieldUnitHeight()));
			this.setSize(new Dimension(116, dialog.getFieldUnitHeight()));
			jComboBoxMonth.addItem("Jan");
		}
		if (language.equals("jp")) {
			jComboBoxYear.setBounds(new Rectangle(0, 0, 60, dialog.getFieldUnitHeight()));
			jComboBoxMonth.setBounds(new Rectangle(61, 0, 45, dialog.getFieldUnitHeight()));
			this.setSize(new Dimension(106, dialog.getFieldUnitHeight()));
			jComboBoxMonth.addItem("01");
		}
		//
		this.setLayout(null);
		this.setFocusable(false);
		if (isEditable) {
			this.add(jComboBoxYear);
			this.add(jComboBoxMonth);
		} else {
			this.add(jTextField);
		}
	}
}

class DialogCheckLayoutPrimaryTable extends Object {
	private static final long serialVersionUID = 1L;
	private org.w3c.dom.Element tableElement = null;
	private org.w3c.dom.Element functionElement_ = null;
	private String tableID = "";
	private ArrayList<String> keyFieldList = new ArrayList<String>();
	private DialogCheckLayout dialog_;
	private StringTokenizer workTokenizer;
	public DialogCheckLayoutPrimaryTable(org.w3c.dom.Element functionElement, DialogCheckLayout dialog){
		super();
		functionElement_ = functionElement;
		dialog_ = dialog;
		if (functionElement_.getAttribute("Type").equals("XF100")
				|| functionElement_.getAttribute("Type").equals("XF110")
				|| functionElement_.getAttribute("Type").equals("XF200")) {
			tableID = functionElement_.getAttribute("PrimaryTable");
		}
		if (functionElement_.getAttribute("Type").equals("XF300")
				|| functionElement_.getAttribute("Type").equals("XF310")) {
			tableID = functionElement_.getAttribute("HeaderTable");
		}
		tableElement = dialog_.getEditor().getSpecificXETreeNode("Table", tableID).getElement();
		String wrkStr1;
		org.w3c.dom.Element workElement;
		if (functionElement_.getAttribute("KeyFields").equals("")) {
			NodeList nodeList = tableElement.getElementsByTagName("Key");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("Type").equals("PK")) {
					workTokenizer = new StringTokenizer(workElement.getAttribute("Fields"), ";" );
					while (workTokenizer.hasMoreTokens()) {
						wrkStr1 = workTokenizer.nextToken();
						keyFieldList.add(wrkStr1);
					}
				}
			}
		} else {
			workTokenizer = new StringTokenizer(functionElement_.getAttribute("KeyFields"), ";" );
			while (workTokenizer.hasMoreTokens()) {
				keyFieldList.add(workTokenizer.nextToken());
			}
		}
	}
	public String getName() {
		return tableElement.getAttribute("Name");
	}
	public String getTableID(){
		return tableID;
	}
	public org.w3c.dom.Element getTableElement(){
		return tableElement;
	}
	public ArrayList<String> getKeyFieldList(){
		return keyFieldList;
	}
}

class DialogCheckLayoutDetailTable extends Object {
	private static final long serialVersionUID = 1L;
	private org.w3c.dom.Element tableElement = null;
	private org.w3c.dom.Element detailElement_ = null;
	private String tableID = "";
	private ArrayList<String> keyFieldList = new ArrayList<String>();
	private DialogCheckLayout dialog_;
	private StringTokenizer workTokenizer;
	public DialogCheckLayoutDetailTable(org.w3c.dom.Element detailElement, DialogCheckLayout dialog){
		super();
		detailElement_ = detailElement;
		dialog_ = dialog;
		tableID = detailElement_.getAttribute("Table");
		tableElement = dialog_.getEditor().getSpecificXETreeNode("Table", tableID).getElement();
		String wrkStr1;
		org.w3c.dom.Element workElement;
		if (detailElement_.getAttribute("KeyFields").equals("")) {
			NodeList nodeList = tableElement.getElementsByTagName("Key");
			for (int i = 0; i < nodeList.getLength(); i++) {
				workElement = (org.w3c.dom.Element)nodeList.item(i);
				if (workElement.getAttribute("Type").equals("PK")) {
					workTokenizer = new StringTokenizer(workElement.getAttribute("Fields"), ";" );
					while (workTokenizer.hasMoreTokens()) {
						wrkStr1 = workTokenizer.nextToken();
						keyFieldList.add(wrkStr1);
					}
				}
			}
		} else {
			workTokenizer = new StringTokenizer(detailElement_.getAttribute("KeyFields"), ";" );
			while (workTokenizer.hasMoreTokens()) {
				keyFieldList.add(workTokenizer.nextToken());
			}
		}
	}
	public String getName() {
		return tableElement.getAttribute("Name");
	}
	public String getTableID(){
		return tableID;
	}
	public org.w3c.dom.Element getTableElement(){
		return tableElement;
	}
	public ArrayList<String> getKeyFieldList(){
		return keyFieldList;
	}
}

class DialogCheckLayoutReferTable extends Object {
	private static final long serialVersionUID = 1L;
	private org.w3c.dom.Element referElement_ = null;
	private String tableID = "";
	private String tableAlias = "";
	public DialogCheckLayoutReferTable(org.w3c.dom.Element referElement){
		super();
		referElement_ = referElement;
		tableID = referElement_.getAttribute("ToTable");
		tableAlias = referElement_.getAttribute("TableAlias");
		if (tableAlias.equals("")) {
			tableAlias = tableID;
		}
	}
	public String getTableID(){
		return tableID;
	}
	public String getTableAlias(){
		return tableAlias;
	}
}


class DialogCheckLayoutHorizontalAlignmentHeaderRenderer implements TableCellRenderer{
	private int horizontalAlignment = SwingConstants.LEFT;
	public DialogCheckLayoutHorizontalAlignmentHeaderRenderer(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
		JLabel l = (JLabel)r.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
		l.setHorizontalAlignment(horizontalAlignment);
		return l;
	}
}

class DialogCheckLayoutRowNumberRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		int number = (Integer)value;
		//
		setText(Integer.toString(number));
		setFont(new java.awt.Font("Dialog", 0, 14));
		setHorizontalAlignment(SwingConstants.RIGHT);
		//
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			if (row%2==0) {
				setBackground(table.getBackground());
			} else {
				setBackground(new Color(240, 240, 255));
			}
			setForeground(table.getForeground());
		}
		//
		validate();
		return this;
	}
}

class DialogCheckLayoutTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			setText((String)value);
			setFont(new java.awt.Font("Dialog", 0, 14));
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			if (row%2==0) {
				setBackground(table.getBackground());
			} else {
				setBackground(new Color(240, 240, 255));
			}
			setForeground(table.getForeground());
		}
		validate();
		return this;
	}
}

class DialogCheckLayoutTableCellRendererWithCheckBox extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	public DialogCheckLayoutTableCellRendererWithCheckBox(String dataTypeOptions) {
		super(dataTypeOptions);
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		this.setOpaque(true);
		if (isSelected) {
			this.setBackground(table.getSelectionBackground());
		} else {
			if (row%2==0) {
				this.setBackground(table.getBackground());
			} else {
				this.setBackground(new Color(240, 240, 255));
			}
		}
		validate();
		return this;
	}
}

class DialogCheckLayoutCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	public DialogCheckLayoutCheckBoxRenderer() {
		super();
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int	row, int column) {
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			if (row%2==0) {
				setBackground(table.getBackground());
			} else {
				setBackground(new Color(240, 240, 255));
			}
			setForeground(table.getForeground());
		}
		return this;
	} 
} 

class DialogCheckLayoutCheckBoxHeaderRenderer extends JCheckBox implements TableCellRenderer {   
	private static final long serialVersionUID = 1L;
	protected DialogCheckLayoutCheckBoxHeaderRenderer rendererComponent;   
	protected int column;   
	protected boolean mousePressed = false;   
	public DialogCheckLayoutCheckBoxHeaderRenderer() {   
		rendererComponent = this;   
	}   
	public Component getTableCellRendererComponent(	JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
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
}  

