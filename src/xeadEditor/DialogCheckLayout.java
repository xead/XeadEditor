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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.w3c.dom.NodeList;

import xeadEditor.Editor.MainTreeNode;
import xeadEditor.Editor.SortableDomElementListModel;
import xeadEditor.Editor.TableModelReadOnlyList;

public class DialogCheckLayout extends JDialog {
	private static final long serialVersionUID = 1L;
	public static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	public static final int FIELD_UNIT_HEIGHT = 25;
	public static final int FIELD_HORIZONTAL_MARGIN = 1;
	public static final int FIELD_VERTICAL_MARGIN = 5;
	public static final int DEFAULT_LABEL_WIDTH = 150;
	public static final int ROW_UNIT_HEIGHT = 25;
	public static final int SEQUENCE_WIDTH = 45;
	public static final int FONT_SIZE = 18;
	private static ImageIcon ICON_CHECK_0A = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck0A.PNG")));
	private static ImageIcon ICON_CHECK_1A = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck1A.PNG")));
	private static ImageIcon ICON_CHECK_0D = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck0D.PNG")));
	private static ImageIcon ICON_CHECK_1D = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck1D.PNG")));
	private static ImageIcon ICON_CHECK_0R = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck0R.PNG")));
	private static ImageIcon ICON_CHECK_1R = new ImageIcon(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("iCheck1R.PNG")));

	private JPanel jPanelMain = new JPanel();
	private JTable jTableMain = new JTable();
	private JScrollPane jScrollPane = new JScrollPane();
	private Editor editor;
	private org.w3c.dom.Element functionElement;
	private StringTokenizer workTokenizer;
	private SortableDomElementListModel sortableList;
	private DialogCheckLayoutPrimaryTable primaryTable;
	private DialogCheckLayoutDetailTable detailTable;
	private ArrayList<DialogCheckLayoutReferTable> referTableList1 = new ArrayList<DialogCheckLayoutReferTable>(); 
	private ArrayList<DialogCheckLayoutReferTable> referTableList2 = new ArrayList<DialogCheckLayoutReferTable>(); 
	private Rectangle screenRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	private String panelType_ = "";
	private int extForXF110_ = 0;
	private ArrayList<DialogCheckLayoutColumn> columnList = null;
	private ArrayList<DialogCheckLayoutFilter> filterList = null;
	public String driverFontName_;
	
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
		jTableMain.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTableMain.setRowHeight(ROW_UNIT_HEIGHT);
		this.setResizable(false);
		this.setTitle(res.getString("CheckLayoutTitle"));
	 	this.setIconImage(Toolkit.getDefaultToolkit().createImage(xeadEditor.Editor.class.getResource("title.png")));
		this.setPreferredSize(new Dimension(233, 167));
		this.getContentPane().add(jScrollPane, BorderLayout.CENTER);
	}

	public void request(MainTreeNode node, String panelType, int tabIndex, int extForXF110, String driverFontName) {
		driverFontName_ = driverFontName;

		jTableMain.setFont(new java.awt.Font(driverFontName_, 0, FONT_SIZE));
		jTableMain.getTableHeader().setFont(new java.awt.Font(driverFontName_, 0, FONT_SIZE));
		jTableMain.getTableHeader().setResizingAllowed(false);
		jTableMain.getTableHeader().setReorderingAllowed(false);
		DefaultTableCellRenderer rendererTableHeader = (DefaultTableCellRenderer)jTableMain.getTableHeader().getDefaultRenderer();
		rendererTableHeader.setHorizontalAlignment(SwingConstants.LEFT);

		functionElement = node.getElement();
		panelType_ = panelType;
		extForXF110_ = extForXF110;

		referTableList1.clear();
		referTableList2.clear();
		detailTable = null;

		if (panelType_.equals("Function100ColumnList")) {
			showLayoutOfTableColumns(functionElement.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function100FilterList")) {
			showLayoutOfTableFilters(functionElement.getElementsByTagName("Filter"));
		}
		if (panelType_.equals("Function110ColumnList")) {
			showLayoutOfTableColumns(functionElement.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function110FilterList")) {
			showLayoutOfTableFilters(functionElement.getElementsByTagName("Filter"));
		}
		if (panelType_.equals("Function110BatchFieldList")) {
			showLayoutOfPanelFields(functionElement.getElementsByTagName("BatchField"), true, true);
		}
		if (panelType_.equals("Function200FieldList")) {
			showLayoutOfPanelFields(functionElement.getElementsByTagName("Field"), true, false);
		}
		if (panelType_.equals("Function200TabFieldList")) {
			showLayoutOfPanelFields(editor.getFunction200TabFieldList(), true, false);
		}
		if (panelType_.equals("Function300HeaderFieldList")) {
			showLayoutOfPanelFields(functionElement.getElementsByTagName("Field"), false, false);
		}
		if (panelType_.equals("Function300DetailFieldList") && tabIndex >= 0) {
			NodeList detailTableList = functionElement.getElementsByTagName("Detail");
			sortableList = editor.getSortedListModel(detailTableList, "Order");
			org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(tabIndex);
			detailTable = new DialogCheckLayoutDetailTable(element.getAttribute("Table"), element.getAttribute("KeyFields"), this);
			showLayoutOfTableColumns(element.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function300DetailFilterList") && tabIndex >= 0) {
			NodeList detailTableList = functionElement.getElementsByTagName("Detail");
			sortableList = editor.getSortedListModel(detailTableList, "Order");
			org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(tabIndex);
			detailTable = new DialogCheckLayoutDetailTable(element.getAttribute("Table"), element.getAttribute("KeyFields"), this);
			showLayoutOfTableFilters(element.getElementsByTagName("Filter"));
		}
		if (panelType_.equals("Function310HeaderFieldList")) {
			showLayoutOfPanelFields(functionElement.getElementsByTagName("Field"), true, false);
		}
		if (panelType_.equals("Function310DetailFieldList")) {
			detailTable = new DialogCheckLayoutDetailTable(functionElement.getAttribute("DetailTable"), functionElement.getAttribute("DetailKeyFields"), this);
			showLayoutOfTableColumns(functionElement.getElementsByTagName("Column"));
		}
		if (panelType_.equals("Function310AddRowListColumnList")) {
			showLayoutOfTableColumns(functionElement.getElementsByTagName("AddRowListColumn"));
		}
	}

	private void showLayoutOfTableColumns(NodeList functionColumnList) {
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
		org.w3c.dom.Element element;
		primaryTable = new DialogCheckLayoutPrimaryTable(functionElement, this, false);
		NodeList referNodeList = primaryTable.getTableElement().getElementsByTagName("Refer");
		sortableList = editor.getSortedListModel(referNodeList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			element = (org.w3c.dom.Element)sortableList.getElementAt(i);
			referTableList1.add(new DialogCheckLayoutReferTable(element));
		}
		if (panelType_.equals("Function300DetailFieldList")
				|| panelType_.equals("Function310DetailFieldList")) {
			referNodeList = detailTable.getTableElement().getElementsByTagName("Refer");
			sortableList = editor.getSortedListModel(referNodeList, "Order");
			for (int i = 0; i < sortableList.getSize(); i++) {
				element = (org.w3c.dom.Element)sortableList.getElementAt(i);
				referTableList2.add(new DialogCheckLayoutReferTable(element));
			}
		}

		////////////////////////////
		// Setup columns on table //
		////////////////////////////
		ArrayList<String> optionList;
		DialogCheckLayoutColumn field;
		int posX, posY;
		columnList = new ArrayList<DialogCheckLayoutColumn>();
		sortableList = editor.getSortedListModel(functionColumnList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			optionList = editor.getOptionList(((org.w3c.dom.Element)sortableList.getElementAt(i)).getAttribute("FieldOptions"));
			if (!optionList.contains("HIDDEN")) {
				field = new DialogCheckLayoutColumn((org.w3c.dom.Element)sortableList.getElementAt(i), this);
				columnList.add(field);
			}
		}
		TableModelReadOnlyList tableModel = editor.new TableModelReadOnlyList();
		jTableMain.setModel(tableModel);
		jScrollPane.getViewport().add(jTableMain, null);
		tableModel.addColumn("");
		TableColumn column = jTableMain.getColumnModel().getColumn(0);
		HeadersRenderer headersRenderer;
		if (extForXF110_ == 1) {
			headersRenderer = new HeadersRenderer(true);
		} else {
			headersRenderer = new HeadersRenderer(false);
		}
		CellsRenderer cellsRenderer = new CellsRenderer(headersRenderer);
		column.setHeaderRenderer(headersRenderer);
		column.setCellRenderer(cellsRenderer);
		column.setPreferredWidth(headersRenderer.getWidth());
		int biggestWidth = headersRenderer.getWidth();
		for (int r = 0; r < 4; r++) {
			Object[] cell = new Object[1];
			cell[0] = Integer.toString(r+1);
			tableModel.addRow(cell);
		}
		jTableMain.setRowHeight(headersRenderer.getHeight());
		int workHeight = headersRenderer.getHeight() * 6 + 30;
		jTableMain.setRowSelectionInterval(0, 0);

		///////////////////////////////////////////////////////////////
		// Adjust panel size and position according to fields layout //
		///////////////////////////////////////////////////////////////
		int workWidth = biggestWidth + 50;
		if (functionElement.getAttribute("Size").equals("")) {
			workWidth = screenRect.width;
			posX = screenRect.x;
		} else {
			if (functionElement.getAttribute("Size").equals("AUTO")) {
				if (workWidth > screenRect.width) {
					workWidth = screenRect.width;
					posX = screenRect.x;
				} else {
					posX = ((screenRect.width - workWidth) / 2) + screenRect.x;
				}
				if (workHeight > screenRect.height) {
					workHeight = screenRect.height;
				}
			} else {
				workWidth = this.getPreferredSize().width;
				posX = this.getLocation().x;
			}
		}
		posY = ((screenRect.height - workHeight) / 2) + screenRect.y;
		this.setPreferredSize(new Dimension(workWidth, workHeight));
		this.setLocation(posX, posY);

		///////////////////////////////////////
		// Arrange components and show panel //
		///////////////////////////////////////
		this.pack();
		super.setVisible(true);
	}

	private void showLayoutOfTableFilters(NodeList functionFilterList) {
		//////////////////////////////////////////////
		// Setup the primary table and refer tables //
		//////////////////////////////////////////////
		primaryTable = new DialogCheckLayoutPrimaryTable(functionElement, this, false);
		NodeList referNodeList = primaryTable.getTableElement().getElementsByTagName("Refer");
		sortableList = editor.getSortedListModel(referNodeList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(i);
			referTableList1.add(new DialogCheckLayoutReferTable(element));
		}

		///////////////////////////
		// Setup Filter fields ////
		///////////////////////////
		DialogCheckLayoutFilter filter;
		int maxWidth = 0;
		int maxHeight = 0;
		int posX = 0;
		int posY = 8;
		int wrkInt = 0;
		Dimension dimOfPriviousField = new Dimension(0,0);
		Dimension dim;
		filterList = new ArrayList<DialogCheckLayoutFilter>();
		jPanelMain.removeAll();
		jScrollPane.getViewport().add(jPanelMain, null);
		sortableList = editor.getSortedListModel(functionFilterList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			filter = new DialogCheckLayoutFilter((org.w3c.dom.Element)sortableList.getElementAt(i), this);
			filterList.add(filter);
			if (!filter.isHidden()) {
				if (wrkInt > 0) {
					if (filter.isVerticalPosition()) {
						posX = 0;
						posY = posY + dimOfPriviousField.height + filter.getVerticalMargin();
					} else {
						posX = posX + dimOfPriviousField.width;
					}
				}

				dim = filter.getPreferredSize();
				dimOfPriviousField = new Dimension(dim.width, dim.height);
				filter.setBounds(posX, posY, dim.width, dim.height);
				jPanelMain.add(filter);

				if (posX + dim.width > maxWidth) {
					maxWidth = posX + dim.width;
				}
				if (posY + dim.height > maxHeight) {
					maxHeight = posY + dim.height;
				}

				wrkInt++;
			}
		}
		jPanelMain.setPreferredSize(new Dimension(maxWidth, maxHeight));

		///////////////////////////////////////////////////////////////
		// Adjust panel size and position according to fields layout //
		///////////////////////////////////////////////////////////////
		int workWidth = maxWidth + 50;
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
		int workHeight = maxHeight + 60;
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

	private void showLayoutOfPanelFields(NodeList functionFieldList, boolean isOnEditablePanel, boolean isForBatchTable) {
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
		primaryTable = new DialogCheckLayoutPrimaryTable(functionElement, this, isForBatchTable);
		NodeList referNodeList = primaryTable.getTableElement().getElementsByTagName("Refer");
		sortableList = editor.getSortedListModel(referNodeList, "Order");
		for (int i = 0; i < sortableList.getSize(); i++) {
			org.w3c.dom.Element element = (org.w3c.dom.Element)sortableList.getElementAt(i);
			referTableList1.add(new DialogCheckLayoutReferTable(element));
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
		return referTableList1;
	}

	public ArrayList<DialogCheckLayoutFilter> getFilterList() {
		return filterList;
	}

	public String getTableIDOfTableAlias(String tableAlias) {
		String tableID = tableAlias;
		DialogCheckLayoutReferTable referTable;
		for (int j = 0; j < referTableList1.size(); j++) {
			referTable = referTableList1.get(j);
			if (referTable.getTableAlias().equals(tableAlias)) {
				tableID = referTable.getTableID();
				break;
			}
		}
		for (int j = 0; j < referTableList2.size(); j++) {
			referTable = referTableList2.get(j);
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
	
	public int getFieldVerticalMargin() {
		return FIELD_VERTICAL_MARGIN;
	}
	
	static int adjustFontSizeToGetPreferredWidthOfLabel(JLabel jLabel, int initialWidth) {
		int width = initialWidth;
		int initialFontSize = jLabel.getFont().getSize();
		FontMetrics metrics = jLabel.getFontMetrics(jLabel.getFont());
		if (metrics.stringWidth(jLabel.getText()) > width) {
			for (int i = initialFontSize; i > 10; i--) {
				jLabel.setFont(new java.awt.Font(jLabel.getFont().getFontName(), 0, jLabel.getFont().getSize()-1));
				metrics = jLabel.getFontMetrics(jLabel.getFont());
				if (metrics.stringWidth(jLabel.getText()) <= width) {
					break;
				}
			}
		}
		return metrics.stringWidth(jLabel.getText());
	}
	
	public String getStringData(String type, int dataSize, int decimal, ArrayList<String> dataTypeOptionList) {
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
				if (dataTypeOptionList != null
						&& !dataTypeOptionList.contains("NO_EDIT")
						&& !dataTypeOptionList.contains("ZERO_SUPPRESS")) {
					if (i % 3 == 0 && i < intSize) {
						bf.append(",");
					}
				}
			}
			if (dataTypeOptionList != null
					&& dataTypeOptionList.contains("ACCEPT_MINUS")) {
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
		//
		//en00 06/17/10
		//en01 Thur,06/17/01
		//en10 Jun17,2010
		//en11 Thur,Jun17,2001
		//
		//jp00 10/06/17
		//jp01 10/06/17(木)
		//jp10 2010/06/17
		//jp11 2010/06/17(木)
		//jp20 2010年6月17日
		//jp21 2010年6月17日(木)
		//jp30 H22/06/17
		//jp31 H22/06/17(水)
		//jp40 H22年06月17日
		//jp41 H22年06月17日(水)
		//jp50 平成22年06月17日
		//jp51 平成22年06月17日(水)
		//
		java.util.Date date = new java.util.Date();
		//
		Calendar cal = Calendar.getInstance();
		if (date != null) { 
			cal.setTime(date);
		}
		//
		StringBuffer buf = new StringBuffer();
		SimpleDateFormat formatter;
		//
		if (dateFormat.equals("")) {
			formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "US", "US"));
			buf.append(formatter.format(cal.getTime()));
		}
		//
		if (dateFormat.equals("en00") || dateFormat.equals("en01")) {
			if (dateFormat.equals("en01")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
			formatter = new SimpleDateFormat("MM/dd/yy", new Locale("en", "US", "US"));
			buf.append(formatter.format(cal.getTime()));
		}
		//
		if (dateFormat.equals("en10") || dateFormat.equals("en11")) {
			if (dateFormat.equals("en11")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
			formatter = new SimpleDateFormat("MMMdd,yyyy", new Locale("en", "US", "US"));
			buf.append(formatter.format(cal.getTime()));
		}
		//
		if (dateFormat.equals("jp00") || dateFormat.equals("jp01")) {
			formatter = new SimpleDateFormat("yy/MM/dd");
			buf.append(formatter.format(cal.getTime()));
			if (dateFormat.equals("jp01")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
		}
		//
		if (dateFormat.equals("jp10") || dateFormat.equals("jp11")) {
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			buf.append(formatter.format(cal.getTime()));
			if (dateFormat.equals("jp11")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
		}
		//
		if (dateFormat.equals("jp20") || dateFormat.equals("jp21")) {
			formatter = new SimpleDateFormat("yyyy年MM月dd日");
			buf.append(formatter.format(cal.getTime()));
			if (dateFormat.equals("jp21")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
		}
		//
		if (dateFormat.equals("jp30") || dateFormat.equals("jp31")) {
			formatter = new SimpleDateFormat("Gyy/MM/dd", new Locale("ja", "JP", "JP"));
			buf.append(formatter.format(cal.getTime()));
			if (dateFormat.equals("jp31")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
		}
		//
		if (dateFormat.equals("jp40") || dateFormat.equals("jp41")) {
			formatter = new SimpleDateFormat("Gyy年MM月dd日", new Locale("ja", "JP", "JP"));
			buf.append(formatter.format(cal.getTime()));
			if (dateFormat.equals("jp41")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
		}
		//
		if (dateFormat.equals("jp50") || dateFormat.equals("jp51")) {
			formatter = new SimpleDateFormat("GGGGyy年MM月dd日", new Locale("ja", "JP", "JP"));
			buf.append(formatter.format(cal.getTime()));
			if (dateFormat.equals("jp51")) {
				buf.append(getDayOfWeek(cal, dateFormat));
			}
		}
		//
		return buf.toString();
	}

	static String getDayOfWeek(Calendar cal, String dateFormat) {
		String result = "";
		String language = dateFormat.substring(0, 2);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		//
		if (dayOfWeek == 1) {
			if (language.equals("jp")) {
				result = "(日)";
			}
			if (language.equals("en")) {
				result = "Sun,";
			}
		}
		if (dayOfWeek == 2) {
			if (language.equals("jp")) {
				result = "(月)";
			}
			if (language.equals("en")) {
				result = "Mon,";
			}
		}
		if (dayOfWeek == 3) {
			if (language.equals("jp")) {
				result = "(火)";
			}
			if (language.equals("en")) {
				result = "Tue,";
			}
		}
		if (dayOfWeek == 4) {
			if (language.equals("jp")) {
				result = "(水)";
			}
			if (language.equals("en")) {
				result = "Wed,";
			}
		}
		if (dayOfWeek == 5) {
			if (language.equals("jp")) {
				result = "(木)";
			}
			if (language.equals("en")) {
				result = "Thur,";
			}
		}
		if (dayOfWeek == 6) {
			if (language.equals("jp")) {
				result = "(金)";
			}
			if (language.equals("en")) {
				result = "Fri,";
			}
		}
		if (dayOfWeek == 7) {
			if (language.equals("jp")) {
				result = "(土)";
			}
			if (language.equals("en")) {
				result = "Sat,";
			}
		}
		//
		return result;
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

	class HeadersRenderer extends JPanel implements TableCellRenderer {   
		private static final long serialVersionUID = 1L;
		private JPanel westPanel = new JPanel();
		private JLabel numberLabel = new JLabel("No.");
		private JPanel checkBoxPanel = new JPanel();
		private JCheckBox checkBox = new JCheckBox();
		private JPanel centerPanel = new JPanel();
		private ArrayList<JLabel> headerList = new ArrayList<JLabel>();
		private int totalWidthOfCenterPanel = 0;
		private int totalHeight = 0;
		private boolean isWithCheckBox_;
		
		public HeadersRenderer(boolean isWithCheckBox) {
			isWithCheckBox_ = isWithCheckBox;
			//
			setupColumnBoundsAndHeaderHeight();
			//
			this.setLayout(new BorderLayout());
			int workWidth;
			if (isWithCheckBox_) {
				GridLayout layout = new GridLayout();
				layout.setColumns(2);
				layout.setRows(1);
				westPanel.setLayout(layout);
				westPanel.setPreferredSize(new Dimension(SEQUENCE_WIDTH*2, 10));
				numberLabel.setFont(new java.awt.Font(driverFontName_, 0, FONT_SIZE));
				numberLabel.setBorder(new HeaderBorder());
				numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
				checkBox.setHorizontalAlignment(SwingConstants.CENTER);
				checkBoxPanel.setBorder(new HeaderBorder());
				checkBoxPanel.setLayout(new BorderLayout());
				checkBoxPanel.add(checkBox, BorderLayout.CENTER);
				westPanel.add(numberLabel);
				westPanel.add(checkBoxPanel);
				workWidth = SEQUENCE_WIDTH*2;
				westPanel.setPreferredSize(new Dimension(workWidth, totalHeight));
				this.add(westPanel, BorderLayout.WEST);
			} else {
				centerPanel.setLayout(null);
				numberLabel.setFont(new java.awt.Font(driverFontName_, 0, FONT_SIZE));
				numberLabel.setBorder(new HeaderBorder());
				numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
				workWidth = SEQUENCE_WIDTH;
				numberLabel.setPreferredSize(new Dimension(workWidth, totalHeight));
				this.add(numberLabel, BorderLayout.WEST);
			}
			//
			centerPanel.setLayout(null);
			this.setPreferredSize(new Dimension(totalWidthOfCenterPanel + workWidth, totalHeight));
			this.add(centerPanel, BorderLayout.CENTER);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
			return this;
		}
		
		public int getWidth() {
			return this.getPreferredSize().width;
		}
		
		public int getSequenceWidth() {
			return SEQUENCE_WIDTH;
		}
		
		public String getSequenceLabel() {
			return numberLabel.getText();
		}
		
		public boolean isWithCheckBox() {
			return isWithCheckBox_;
		}
		
		public int getHeight() {
			return this.getPreferredSize().height;
		}
		
		public ArrayList<JLabel> getColumnHeaderList() {
			return headerList;
		}
		
		public void setupColumnBoundsAndHeaderHeight() {
			int fromX = 0;
			int fromY = 0;
			int width, height, wrkInt1, wrkInt2;
			JLabel header;
			//
			totalWidthOfCenterPanel = 0;
			centerPanel.removeAll();
			headerList.clear();
			for (int i = 0; i < columnList.size(); i++) {
					header = new JLabel();
					header.setFont(new java.awt.Font(driverFontName_, 0, FONT_SIZE));
					if (columnList.get(i).getValueType().equals("IMAGE")
							|| columnList.get(i).getValueType().equals("FLAG")) {
						header.setHorizontalAlignment(SwingConstants.CENTER);
						header.setText(columnList.get(i).getCaption());
					} else {
						if (columnList.get(i).getBasicType().equals("INTEGER")
								|| columnList.get(i).getBasicType().equals("FLOAT")) {
							header.setHorizontalAlignment(SwingConstants.RIGHT);
							header.setText(columnList.get(i).getCaption() + " ");
						} else {
							header.setHorizontalAlignment(SwingConstants.LEFT);
							header.setText(columnList.get(i).getCaption());
						}
					}
					header.setOpaque(true);
					//
					width = columnList.get(i).getWidth();
					height = ROW_UNIT_HEIGHT * columnList.get(i).getRows();
					if (i > 0) {
						fromX = headerList.get(i-1).getBounds().x + headerList.get(i-1).getBounds().width;
						fromY = headerList.get(i-1).getBounds().y + headerList.get(i-1).getBounds().height;
						for (int j = i-1; j >= 0; j--) {
							if (columnList.get(i).getLayout().equals("VERTICAL")) {
								wrkInt1 = headerList.get(j).getBounds().y + headerList.get(j).getBounds().height;
								if (wrkInt1 <= fromY) {
									fromX = headerList.get(j).getBounds().x;
								} else {
									break;
								}
							} else {
								wrkInt1 = headerList.get(j).getBounds().x + headerList.get(j).getBounds().width;
								if (wrkInt1 <= fromX) {
									fromY = headerList.get(j).getBounds().y;
								} else {
									break;
								}
							}
						}
						for (int j = i-1; j >= 0; j--) {
							wrkInt1 = headerList.get(j).getBounds().x + headerList.get(j).getBounds().width;
							wrkInt2 = fromX + width;
							if (wrkInt2 < wrkInt1 && wrkInt2+2 > wrkInt1) {
								width = wrkInt1 - fromX;
							}
						}
					}
					//
					header.setBounds(new Rectangle(fromX, fromY, width, height));
					header.setBorder(new HeaderBorder());
					headerList.add(header);
					centerPanel.add(header);
					//
					if (fromX + width > totalWidthOfCenterPanel) {
						totalWidthOfCenterPanel = fromX + width;
					}
					if (fromY + height > totalHeight) {
						totalHeight = fromY + height;
					}
			}
		}
	}  

	class HeaderBorder implements Border {
		public Insets getBorderInsets(Component c){
			return new Insets(2, 2, 2, 2);
		}
		public boolean isBorderOpaque(){
			return false;
		}
		public void paintBorder (Component c, Graphics g, int x, int y, int width, int height){
			g.setColor(Color.white);
			g.drawLine(x, y, x+width, y);
			g.drawLine(x, y, x, y+height);
			g.setColor(Color.gray);
			g.drawLine(x, y+height-1, x+width, y+height-1);
			g.drawLine(x+width-1, y+height, x+width-1, y);
		}
	}

	class CellBorder implements Border {
		public Insets getBorderInsets(Component c){
			return new Insets(0, 0, 0, 2);
		}
		public boolean isBorderOpaque(){
			return false;
		}
		public void paintBorder (Component c, Graphics g, int x, int y, int width, int height){
			g.setColor(Color.gray);
			g.drawLine(x+width-1, y+height+1, x+width-1, y);
		}
	}

	public class CellsRenderer extends JPanel implements TableCellRenderer {
		private static final long serialVersionUID = 1L;
		private JPanel westPanel = new JPanel();
		private JLabel numberCell = new JLabel("");
		private JPanel checkBoxPanel = new JPanel();
		private JCheckBox checkBox = new JCheckBox();
		private JPanel centerPanel = new JPanel();
		private ArrayList<JLabel> cellList = new ArrayList<JLabel>();
		private HeadersRenderer headersRenderer_;

		public CellsRenderer(HeadersRenderer headersRenderer) {
			headersRenderer_ = headersRenderer;
			//
			numberCell.setFont(new java.awt.Font(driverFontName_, 0, FONT_SIZE));
			numberCell.setBorder(new CellBorder());
			numberCell.setHorizontalAlignment(SwingConstants.CENTER);
			numberCell.setOpaque(true);
			//
			this.setLayout(new BorderLayout());
			if (headersRenderer_.isWithCheckBox_) {
				GridLayout layout = new GridLayout();
				layout.setColumns(2);
				layout.setRows(1);
				westPanel.setLayout(layout);
				checkBox.setHorizontalAlignment(SwingConstants.CENTER);
				checkBoxPanel.setBorder(new CellBorder());
				checkBoxPanel.setLayout(new BorderLayout());
				checkBoxPanel.add(checkBox, BorderLayout.CENTER);
				westPanel.add(numberCell);
				westPanel.add(checkBoxPanel);
			}
			//
			centerPanel.setLayout(null);
			centerPanel.setOpaque(false);
			JLabel cell;
			Rectangle rec;
			cellList.clear();
			centerPanel.removeAll();
			for (int i = 0; i < headersRenderer_.getColumnHeaderList().size(); i++) {
				cell = new JLabel();
				cell.setFont(new java.awt.Font(driverFontName_, 0, FONT_SIZE));
				cell.setHorizontalAlignment(headersRenderer_.getColumnHeaderList().get(i).getHorizontalAlignment());
				rec = headersRenderer_.getColumnHeaderList().get(i).getBounds();
				cell.setBounds(rec.x, rec.y, rec.width, rec.height);
				cell.setBorder(new HeaderBorder());
				cellList.add(cell);
				centerPanel.add(cell);
			}
			//
			int widthOfCenterPanel;
			if (headersRenderer_.isWithCheckBox()) {
				widthOfCenterPanel = headersRenderer_.getWidth() - headersRenderer_.getSequenceWidth()*2;
				centerPanel.setPreferredSize(new Dimension(widthOfCenterPanel, headersRenderer_.getHeight()));
				westPanel.setPreferredSize(new Dimension(headersRenderer_.getSequenceWidth()*2, headersRenderer_.getHeight()));
				this.add(westPanel, BorderLayout.WEST);
			} else {
				widthOfCenterPanel = headersRenderer_.getWidth() - headersRenderer_.getSequenceWidth();
				centerPanel.setPreferredSize(new Dimension(widthOfCenterPanel, headersRenderer_.getHeight()));
				numberCell.setPreferredSize(new Dimension(headersRenderer_.getSequenceWidth(), headersRenderer_.getHeight()));
				this.add(numberCell, BorderLayout.WEST);
			}
			this.add(centerPanel, BorderLayout.CENTER);
			this.setPreferredSize(new Dimension(headersRenderer_.getWidth(), headersRenderer_.getHeight()));
		}   

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setBackground(table.getSelectionBackground());
				numberCell.setBackground(table.getSelectionBackground());
				checkBox.setBackground(table.getSelectionBackground());
				numberCell.setForeground(table.getSelectionForeground());
			} else {
				if (row%2==0) {
					setBackground(table.getBackground());
					numberCell.setBackground(table.getBackground());
					checkBox.setBackground(table.getBackground());
				} else {
					Color bg = new Color(240, 240, 255);
					setBackground(bg);
					numberCell.setBackground(bg);
					checkBox.setBackground(bg);
				}
				numberCell.setForeground(table.getForeground());
			}
			setFocusable(false);
			String rowNumber = (String)value;
			numberCell.setText(rowNumber);
			for (int i = 0; i < cellList.size(); i++) {
				cellList.get(i).setText(columnList.get(i).getValue().toString());
				if (isSelected) {
					cellList.get(i).setForeground(table.getSelectionForeground());
				} else {
					cellList.get(i).setForeground(table.getForeground());
				}
			}
			return this;
		}
	} 
}

class DialogCheckLayoutColumn extends Object {
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
	private int fieldRows = 1;
	private String dataTypeOptions = "";
	private ArrayList<String> dataTypeOptionList;
	private String fieldOptions = "";
	private ArrayList<String> fieldOptionList;
	private DialogCheckLayout dialog_;
	private Object value = "";
	private String valueType = "STRING";
	private String fieldLayout = "";

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
		FontMetrics metrics = jLabel.getFontMetrics(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		String wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "CAPTION");
		if (!wrkStr.equals("")) {
			fieldCaption = wrkStr;
		}
		int captionWidth = metrics.stringWidth(fieldCaption) + 18;
		//
		if (fieldOptionList.contains("HORIZONTAL")) {
			fieldLayout = "HORIZONTAL";
		} else {
			if (fieldOptionList.contains("VERTICAL")) {
				fieldLayout = "VERTICAL";
			}
		}
		//
		String dateFormat = dialog.getEditor().getDateFormat();
		String language = dateFormat.substring(0, 2);
		String basicType = this.getBasicType();
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "KUBUN");
		if (!wrkStr.equals("")) {
			Connection connection = null;
			try {
				String wrk = "";
				StringBuffer buf1 = new StringBuffer();
				buf1.append("select * from ");
				buf1.append(dialog_.getEditor().getSystemUserVariantsTableID());
				buf1.append(" where IDUSERKUBUN = '");
				buf1.append(wrkStr);
				buf1.append("'");
				connection = dialog_.getEditor().getDatabaseConnection("");
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
			} catch (SQLException e) {
				try {
					connection.close();
				} catch (SQLException e1) {
				} finally {
					JOptionPane.showMessageDialog(null, DialogCheckLayout.res.getString("DBConnectMessage9"));
				}
			} catch(Exception e) {
			}
		} else {
			if ((dataTypeOptionList.contains("KANJI") || dataTypeOptionList.contains("ZIPADRS"))
					&& !dataType.equals("VARCHAR") && !dataType.equals("LONG VARCHAR")) {
				fieldWidth = dataSize * DialogCheckLayout.FONT_SIZE + 5;
				value = dialog_.getStringData("KANJI", dataSize, 0, dataTypeOptionList);
			} else {
				if (dataTypeOptionList.contains("FYEAR")) {
					fieldWidth = 100;
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
						fieldWidth = 120;
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
							fieldWidth = 80;
							if (language.equals("jp")) {
								value = "１月度";
							} else {
								value = "Jan";
							}
						} else {
							if (basicType.equals("INTEGER") || basicType.equals("FLOAT")) {
								String stringValue = dialog_.getStringData("NUMBER", dataSize, decimalSize, dataTypeOptionList);
								fieldWidth = stringValue.length() * (DialogCheckLayout.FONT_SIZE/2 + 2) + 15;
								value = stringValue;
							} else {
								if (basicType.equals("DATE")) {
									String stringValue = dialog.getDateValue(dialog.getEditor().getDateFormat());
									fieldWidth = metrics.stringWidth(stringValue) + 10;
									value = stringValue;
								} else {
									if (dataTypeOptionList.contains("IMAGE")) {
										valueType = "IMAGE";
										fieldWidth = 60;
										fieldRows = 2;
										value = "image";
									} else {
										wrkStr = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "BOOLEAN");
										if (!wrkStr.equals("")) {
											valueType = "FLAG";
											fieldWidth = 25;
											value = "(v)";
										} else {
											if (dataType.equals("VARCHAR") || dataType.equals("LONG VARCHAR")) {
												fieldWidth = 400;
												if (dataTypeOptionList.contains("KANJI")) {
													value = dialog_.getStringData("KANJI", dataSize, 0, dataTypeOptionList);
												} else {
													value = dialog_.getStringData("STRING", dataSize, 0, dataTypeOptionList);
												}
											} else {
												fieldWidth = dataSize * (DialogCheckLayout.FONT_SIZE/2 + 2) + 15;
												value = dialog_.getStringData("STRING", dataSize, 0, dataTypeOptionList);
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
		//
		if (fieldWidth > 400) {
			fieldWidth = 400;
		}
		if (captionWidth > fieldWidth) {
			fieldWidth = captionWidth;
		}
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "WIDTH");
		if (!wrkStr.equals("")) {
			fieldWidth = Integer.parseInt(wrkStr);
		}
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "ROWS");
		if (!wrkStr.equals("")) {
			fieldRows = Integer.parseInt(wrkStr);
		}
	}
	
	public String getValueType() {
		return valueType;
	}
	
	public int getRows() {
		return fieldRows;
	}
	
	public String getLayout() {
		return fieldLayout;
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
}

class DialogCheckLayoutFilter extends JPanel {
	private static final long serialVersionUID = 1L;
	private org.w3c.dom.Element fieldElement_ = null;
	private DialogCheckLayout dialog_ = null;
	private String dataType = "";
	private String dataTypeOptions = "";
	private ArrayList<String> dataTypeOptionList;
	private String tableID = "";
	private String tableAlias = "";
	private String fieldID = "";
	private String fieldName = "";
	private String fieldCaption = "";
	private String fieldOptions = "";
	private ArrayList<String> fieldOptionList;
	private int dataSize = 5;
	private int decimalSize = 0;
	private JPanel jPanelField = new JPanel();
	private JLabel jLabelField = new JLabel();
	private JComponent component = null;
	private boolean isVertical = false;
	private int verticalMargin = 5;
	private int horizontalMargin = 30;
	private boolean isEditable_ = true;
	private boolean isHidden = false;

	public DialogCheckLayoutFilter(org.w3c.dom.Element fieldElement, DialogCheckLayout dialog) {
		super();
		String wrkStr;
		fieldElement_ = fieldElement;
		dialog_ = dialog;

		fieldOptions = fieldElement_.getAttribute("FieldOptions");
		fieldOptionList = dialog_.getEditor().getOptionList(fieldOptions);
		StringTokenizer workTokenizer1 = new StringTokenizer(fieldElement_.getAttribute("DataSource"), "." );
		tableAlias = workTokenizer1.nextToken();
		tableID = dialog_.getTableIDOfTableAlias(tableAlias);
		fieldID =workTokenizer1.nextToken();

		org.w3c.dom.Element workElement = dialog_.getEditor().getSpecificFieldElement(tableID, fieldID);
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

		if (fieldOptionList.contains("VERTICAL")) {
			isVertical = true;
		}
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "VERTICAL");
		if (!wrkStr.equals("")) {
			isVertical = true;
			verticalMargin = Integer.parseInt(wrkStr);
		}
		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "HORIZONTAL");
		if (!wrkStr.equals("")) {
			horizontalMargin = Integer.parseInt(wrkStr) + 5;
		}

		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "CAPTION");
		if (!wrkStr.equals("")) {
			fieldCaption = wrkStr;
		}
		jLabelField = new JLabel(fieldCaption);
		jLabelField.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelField.setFont(new java.awt.Font(dialog_.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		int width = DialogCheckLayout.adjustFontSizeToGetPreferredWidthOfLabel(jLabelField, DialogCheckLayout.DEFAULT_LABEL_WIDTH);
		if (isVertical || dialog_.getFilterList().size() == 0) {
			jLabelField.setPreferredSize(new Dimension(DialogCheckLayout.DEFAULT_LABEL_WIDTH, DialogCheckLayout.FIELD_UNIT_HEIGHT));
		} else {
			jLabelField.setPreferredSize(new Dimension(width + horizontalMargin, DialogCheckLayout.FIELD_UNIT_HEIGHT));
		}
		isEditable_ = !fieldOptionList.contains("NON_EDITABLE");

		jPanelField.setLayout(null);
		this.setLayout(new BorderLayout());
		this.add(jLabelField, BorderLayout.WEST);
		this.add(jPanelField, BorderLayout.CENTER);

		////////////////////////////////////////////////////////////////////////////////
		// Steps to check BOOLEAN should be here because the field can be specified   //
		// as PROMPT_LIST1/2. This happens because BOOLEAN is placed recently         //
		////////////////////////////////////////////////////////////////////////////////
		if (!dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "BOOLEAN").equals("")) {
			component = new DialogCheckLayoutCheckBox(isEditable_, dialog_);
		} else {
			////////////////////////////////////////////////////////////////////////////////
			// PROMPT_LIST1 is the list with blank row, PROMPT_LIST2 is without blank row //
			////////////////////////////////////////////////////////////////////////////////
			if (fieldOptionList.contains("PROMPT_LIST1") || fieldOptionList.contains("PROMPT_LIST2")) {
				DialogCheckLayoutReferTable referTable = null;
				ArrayList<DialogCheckLayoutReferTable> referTableList = dialog_.getReferTableList();
				for (int i = 0; i < referTableList.size(); i++) {
					if (referTableList.get(i).getTableID().equals(tableID)) {
						if (referTableList.get(i).getTableAlias().equals("") || referTableList.get(i).getTableAlias().equals(tableAlias)) {
							referTable = referTableList.get(i);
							break;
						}
					}
				}
				component = new DialogCheckLayoutComboBox(fieldElement, dataTypeOptions, referTable, dialog_);

			} else {
				wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "PROMPT_CALL");
				if (!wrkStr.equals("")) {
					component = new DialogCheckLayoutPromptCallField(fieldElement, isEditable_, dialog_);
					if (component.getPreferredSize().width < 70) {
						component.setPreferredSize(new Dimension(70, component.getPreferredSize().height));
					}
				} else {
					if (dataType.equals("DATE")) {
						component = new DialogCheckLayoutDateField(isEditable_, dialog_);
					} else {
						if (dataTypeOptionList.contains("YMONTH")) {
							component = new DialogCheckLayoutYMonthBox(isEditable_, dialog_);
						} else {
							if (dataTypeOptionList.contains("MSEQ")) {
								component = new DialogCheckLayoutMSeqBox(isEditable_, dialog_);
							} else {
								if (dataTypeOptionList.contains("FYEAR")) {
									component = new DialogCheckLayoutFYearBox(isEditable_, dialog_);
								} else {
									component = new DialogCheckLayoutTextField(this.getBasicType(), dataSize, decimalSize, dataTypeOptions, fieldOptions, isEditable_, dialog_);
								}
							}
						}
					}
				}
			}
		}

		if (fieldOptionList.contains("HIDDEN")) {
			isHidden = true;
		}

		if (decimalSize > 0) {
			wrkStr = "<html>" + fieldName + " " + tableAlias + "." + fieldID + " (" + dataSize + "," + decimalSize + ")<br>";
		} else {
			wrkStr = "<html>" + fieldName + " " + tableAlias + "." + fieldID + " (" + dataSize + ")<br>";
		}
		this.setToolTipText(wrkStr);
		component.setToolTipText(wrkStr);
		component.setBounds(new Rectangle(5, 0, component.getPreferredSize().width, component.getPreferredSize().height));
		jPanelField.add(component);

		wrkStr = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions, "WIDTH");
		if (wrkStr.equals("")) {
			if (component.getBounds().width > 250) {
				component.setBounds(new Rectangle(component.getBounds().x, component.getBounds().y, 250, component.getBounds().height));
			}
		} else {
			component.setBounds(new Rectangle(component.getBounds().x, component.getBounds().y, Integer.parseInt(wrkStr), 24));
		}
		this.setPreferredSize(new Dimension(jLabelField.getPreferredSize().width + component.getBounds().width + 5, component.getBounds().height));
	}
	
	public boolean isHidden() {
		return isHidden;
	}

	public boolean isVerticalPosition(){
		return isVertical;
	}

	public int getVerticalMargin(){
		return verticalMargin;
	}

	public String getBasicType(){
		return dialog_.getEditor().getBasicTypeOf(dataType);
	}

	public String getDataType(){
		return dataType;
	}
	
	public String getFieldID(){
		return fieldID;
	}
	
	public String getTableAlias(){
		return tableAlias;
	}
	
	public String getTableID(){
		return tableID;
	}
	
	public String getDataSourceName(){
		return tableAlias + "." + fieldID;
	}
	
	public boolean isEditable() {
		return isEditable_;
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
			} else {
				isEditable = false;
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
		jLabelField.setFont(new java.awt.Font(dialog_.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		if (fieldOptionList.contains("CAPTION_LENGTH_VARIABLE")) {
			FontMetrics metrics = jLabelField.getFontMetrics(jLabelField.getFont());
			jLabelField.setPreferredSize(new Dimension(metrics.stringWidth(fieldCaption), DialogCheckLayout.FIELD_UNIT_HEIGHT));
		} else {
			jLabelField.setPreferredSize(new Dimension(DialogCheckLayout.DEFAULT_LABEL_WIDTH, DialogCheckLayout.FIELD_UNIT_HEIGHT));
			DialogCheckLayout.adjustFontSizeToGetPreferredWidthOfLabel(jLabelField, DialogCheckLayout.DEFAULT_LABEL_WIDTH);
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
				if (isEditable && (wrkStr.equals("XF200") || wrkStr.equals("XF310"))
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
								component = new DialogCheckLayoutUrlField(dataSize, fieldOptions, isEditable, dialog_);
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
			jLabelFieldComment.setFont(new java.awt.Font(dialog_.driverFontName_, 0, DialogCheckLayout.FONT_SIZE-2));
			jLabelFieldComment.setVerticalAlignment(SwingConstants.TOP);
			FontMetrics metrics = jLabelFieldComment.getFontMetrics(jLabelFieldComment.getFont());
			this.setPreferredSize(new Dimension(this.getPreferredSize().width + metrics.stringWidth(wrkStr) + 6, this.getPreferredSize().height));
		}
		//
		if (dataTypeOptionList.contains("ZIPADRS") && isOnEditablePanel) {
			jButtonToRefferZipNo = new JButton();
			jButtonToRefferZipNo.setText("<");
			jButtonToRefferZipNo.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
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
		jTextField.setFont(new java.awt.Font(dialog_.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		FontMetrics metrics = jTextField.getFontMetrics(jTextField.getFont());
		this.setFont(new java.awt.Font(dialog_.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
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
						fieldWidth = dataSize * DialogCheckLayout.FONT_SIZE + 20;
						this.addItem(dialog_.getStringData("KANJI", dataSize, 0, workDataTypeOptionList));
					} else {
						fieldWidth = dataSize * (DialogCheckLayout.FONT_SIZE/2 +2) + 30;
						this.addItem(dialog_.getStringData("STRING", dataSize, 0, workDataTypeOptionList));
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
		this.setFont(new java.awt.Font(dialog_.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		this.setFocusable(false);
		//
		String wrkStr1, wrkStr2, value = "";
		int fieldHeight, fieldWidth = 0;
		if (dataTypeOptionList.contains("KANJI") || dataTypeOptionList.contains("ZIPADRS")) {
			value = dialog_.getStringData("KANJI", digits, 0, dataTypeOptionList);
			fieldWidth = digits_ * DialogCheckLayout.FONT_SIZE + 10;
		} else {
			wrkStr1 = dialog_.getEditor().getOptionValueWithKeyword(dataTypeOptions, "KUBUN");
			wrkStr2 = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions_, "PROMPT_CALL");
			if (!wrkStr1.equals("") && wrkStr2.equals("")) {
				try {
					FontMetrics metrics = this.getFontMetrics(this.getFont());
					StringBuffer buf1 = new StringBuffer();
					buf1.append("select * from ");
					buf1.append(dialog_.getEditor().getSystemUserVariantsTableID());
					buf1.append(" where IDUSERKUBUN = '");
					buf1.append(wrkStr1);
					buf1.append("' order by SQLIST");
					Connection connection = dialog_.getEditor().getDatabaseConnection("");
					if (connection != null && !connection.isClosed()) {
						Statement statement = connection.createStatement();
						ResultSet result = statement.executeQuery(buf1.toString());
						while (result.next()) {
							wrkStr1 = result.getString("TXUSERKUBUN").trim();
							if (metrics.stringWidth(wrkStr1) > fieldWidth) {
								fieldWidth = metrics.stringWidth(wrkStr1);
							}
							if (value.equals("")) {
								value = wrkStr1;
							}
						}
						fieldWidth = fieldWidth + 10;
					}
				} catch(Exception e) {
				}
			} else {
				if (basicType_.equals("INTEGER") || basicType_.equals("FLOAT")) {
					value = dialog_.getStringData("NUMBER", digits, decimal_, dataTypeOptionList);
					fieldWidth = value.length() * (DialogCheckLayout.FONT_SIZE/2 + 2) + 15;
				} else {
					if (basicType_.equals("DATETIME")) {
						value = "9999/99/99 HH:MM:SS.SSS";
						fieldWidth = 24 * (DialogCheckLayout.FONT_SIZE/2 + 2);
					} else {
						value = dialog_.getStringData("STRING", digits, 0, dataTypeOptionList);
						fieldWidth = digits_ * (DialogCheckLayout.FONT_SIZE/2 + 2) + 10;
					}
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
		wrkStr1 = dialog_.getEditor().getOptionValueWithKeyword(fieldOptions_, "WIDTH");
		if (!wrkStr1.equals("")) {
			fieldWidth = Integer.parseInt(wrkStr1);
		}
		this.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	}
}

class DialogCheckLayoutUrlField extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField jTextField = new JTextField();
	private JLabel jLabel = new JLabel();
	public DialogCheckLayoutUrlField(int digits, String fieldOptions, boolean isEditable, DialogCheckLayout dialog){
		super();
		//
		jTextField.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jTextField.setText(dialog.getStringData("STRING", digits, 0, null));
		jTextField.setFocusable(false);
		//
		jLabel.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jLabel.setForeground(Color.blue);
		jLabel.setHorizontalAlignment(SwingConstants.LEFT);
		jLabel.setBorder(jTextField.getBorder());
		jLabel.setText("<html><u>" + dialog.getStringData("STRING", digits, 0, null));
		//
		this.setLayout(new BorderLayout());
		if (isEditable) {
			this.add(jTextField, BorderLayout.CENTER);
		} else {
			this.add(jLabel, BorderLayout.CENTER);
		}
		int fieldWidth = 50;
		String wrkStr = dialog.getEditor().getOptionValueWithKeyword(fieldOptions, "WIDTH");
		if (!wrkStr.equals("")) {
			fieldWidth = Integer.parseInt(wrkStr);
		} else {
			fieldWidth = digits * (DialogCheckLayout.FONT_SIZE/2 + 2) + 15;
		}
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
		jTextField.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jTextField.setFocusable(false);
		jTextField.setText(dialog.getStringData("STRING", size, 0, null));
		//
		jButton.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jButton.setPreferredSize(new Dimension(100, dialog.getFieldUnitHeight()));
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
		jTextField.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jTextField.setFocusable(false);
		FontMetrics metrics = jTextField.getFontMetrics(jTextField.getFont());
		String value = dialog.getDateValue(dialog.getEditor().getDateFormat());
		int width = metrics.stringWidth(value) + 10;
		jTextField.setText(value);
		this.setPreferredSize(new Dimension(width + 26, dialog.getFieldUnitHeight()));
		this.setLayout(new BorderLayout());
		this.add(jTextField, BorderLayout.CENTER);
		if (isEditable) {
			jTextField.setEditable(true);
			ImageIcon imageIcon = new ImageIcon(xeadEditor.Editor.class.getResource("prompt.png"));
		 	jButton.setIcon(imageIcon);
			jButton.setPreferredSize(new Dimension(26, dialog.getFieldUnitHeight()));
			this.add(jButton, BorderLayout.EAST);
		} else {
			jTextField.setEditable(false);
			jPanelDummy.setPreferredSize(new Dimension(26, dialog.getFieldUnitHeight()));
			this.add(jPanelDummy, BorderLayout.EAST);
			this.setBorder(jTextField.getBorder());
			jTextField.setBorder(null);
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
		jTextArea.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
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
		int fieldHeight = rows_ * dialog.getFieldUnitHeight() + (rows_ - 1) * dialog.getFieldVerticalMargin();
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
		jTextField.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		jTextField.setBounds(new Rectangle(0, 0, 110, dialog.getFieldUnitHeight()));
		//
		jComboBoxYear.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jComboBoxYear.setBounds(new Rectangle(0, 0, 110, dialog.getFieldUnitHeight()));
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
		jTextField.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		jComboBoxMSeq.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE-2));
		if (language.equals("en")) {
			jComboBoxMSeq.setBounds(new Rectangle(0, 0, 60, dialog.getFieldUnitHeight()));
			jTextField.setBounds(new Rectangle(0, 0, 60, dialog.getFieldUnitHeight()));
			this.setSize(new Dimension(50, dialog.getFieldUnitHeight()));
			jComboBoxMSeq.addItem("Jan");
		}
		if (language.equals("jp")) {
			jComboBoxMSeq.setBounds(new Rectangle(0, 0, 80, dialog.getFieldUnitHeight()));
			jTextField.setBounds(new Rectangle(0, 0, 80, dialog.getFieldUnitHeight()));
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
		jTextField.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jTextField.setEditable(false);
		jTextField.setFocusable(false);

		jComboBoxYear.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
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
		jComboBoxMonth.setFont(new java.awt.Font(dialog.driverFontName_, 0, DialogCheckLayout.FONT_SIZE));
		jComboBoxMonth.setFocusable(false);
		if (language.equals("en")) {
			jComboBoxMonth.setBounds(new Rectangle(0, 0, 70, dialog.getFieldUnitHeight()));
			jComboBoxYear.setBounds(new Rectangle(71, 0, 80, dialog.getFieldUnitHeight()));
			jTextField.setBounds(new Rectangle(0, 0, 151, dialog.getFieldUnitHeight()));
			this.setSize(new Dimension(151, dialog.getFieldUnitHeight()));
			jComboBoxMonth.addItem("Jan");
		}
		if (language.equals("jp")) {
			jComboBoxYear.setBounds(new Rectangle(0, 0, 80, dialog.getFieldUnitHeight()));
			jComboBoxMonth.setBounds(new Rectangle(81, 0, 60, dialog.getFieldUnitHeight()));
			jTextField.setBounds(new Rectangle(0, 0, 141, dialog.getFieldUnitHeight()));
			this.setSize(new Dimension(141, dialog.getFieldUnitHeight()));
			jComboBoxMonth.addItem("01");
		}

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
	private org.w3c.dom.Element tableElement = null;
	private org.w3c.dom.Element functionElement_ = null;
	private String tableID = "";
	private ArrayList<String> keyFieldList = new ArrayList<String>();
	private DialogCheckLayout dialog_;
	private StringTokenizer workTokenizer;
	public DialogCheckLayoutPrimaryTable(org.w3c.dom.Element functionElement, DialogCheckLayout dialog, boolean isForBatchTable){
		super();
		functionElement_ = functionElement;
		dialog_ = dialog;
		if (isForBatchTable) {
			tableID = functionElement_.getAttribute("BatchTable"); //XF110 BatchTable//
		} else {
			if (functionElement_.getAttribute("Type").equals("XF100")
					|| functionElement_.getAttribute("Type").equals("XF110")
					|| functionElement_.getAttribute("Type").equals("XF200")) {
				tableID = functionElement_.getAttribute("PrimaryTable");
			}
			if (functionElement_.getAttribute("Type").equals("XF300")
					|| functionElement_.getAttribute("Type").equals("XF310")) {
				tableID = functionElement_.getAttribute("HeaderTable");
			}
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
	private org.w3c.dom.Element tableElement = null;
	private String tableID_ = "";
	private ArrayList<String> keyFieldList = new ArrayList<String>();
	private DialogCheckLayout dialog_;
	private StringTokenizer workTokenizer;
	public DialogCheckLayoutDetailTable(String tableID, String keyFields, DialogCheckLayout dialog){
		super();
		dialog_ = dialog;
		tableID_ = tableID;
		tableElement = dialog_.getEditor().getSpecificXETreeNode("Table", tableID_).getElement();
		String wrkStr1;
		org.w3c.dom.Element workElement;
		if (keyFields.equals("")) {
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
			workTokenizer = new StringTokenizer(keyFields, ";" );
			while (workTokenizer.hasMoreTokens()) {
				keyFieldList.add(workTokenizer.nextToken());
			}
		}
	}
	public String getName() {
		return tableElement.getAttribute("Name");
	}
	public String getTableID(){
		return tableID_;
	}
	public org.w3c.dom.Element getTableElement(){
		return tableElement;
	}
	public ArrayList<String> getKeyFieldList(){
		return keyFieldList;
	}
}

class DialogCheckLayoutReferTable extends Object {
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
