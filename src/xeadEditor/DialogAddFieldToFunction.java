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

//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import javax.swing.table.TableColumn;
import org.w3c.dom.NodeList;

import xeadEditor.Editor.SortableDomElementListModel;

//import xeadEditor.Editor.MainTreeNode;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
//import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogAddFieldToFunction extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel jPanelMain = new JPanel();
	private JButton jButtonOK = new JButton();
	private JButton jButtonSelectAll = new JButton();
	private JButton jButtonCancel = new JButton();
	private Editor frame_;
	private DefaultListModel<JCheckBox> listModelDataSource = new DefaultListModel<JCheckBox>();
	private JList<JCheckBox> jListDataSource = new JList<JCheckBox>(listModelDataSource);
	private JScrollPane jScrollPaneDataSourceList = new JScrollPane();
	private JPanel jPanelButtons = new JPanel();
	private org.w3c.dom.Element objectElement_ = null;
	private String tableType_ = "";
	private SortableDomElementListModel sortingList;
	private ArrayList<String> tableIDList = new ArrayList<String>();
	private ArrayList<String> tableAliasList = new ArrayList<String>();
	private ArrayList<String> fieldIDList = new ArrayList<String>();
	private ArrayList<String> fieldNameList = new ArrayList<String>();
	private ArrayList<Boolean> isNumericList = new ArrayList<Boolean>();
	private ArrayList<Boolean> isKubunList = new ArrayList<Boolean>();
	private ArrayList<String> functionDataSourceList = new ArrayList<String>();
	private int order = 0;
	private int result = 0;

	public DialogAddFieldToFunction(Editor frame) {
		super(frame, "", true);
		frame_ = frame;
		try {
			jbInit();
			pack();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(new BorderLayout());
		jPanelMain.setLayout(new BorderLayout());
		jListDataSource.setBorder(null);
		Editor_CheckBoxListRenderer renderer = new Editor_CheckBoxListRenderer(frame_);
		jListDataSource.setCellRenderer(renderer);
		jListDataSource.setBackground(jPanelMain.getBackground());
		jListDataSource.addMouseListener(new DialogAddFieldToFunction_jListDataSource_mouseAdapter(this));
		jScrollPaneDataSourceList.setBorder(BorderFactory.createEtchedBorder());
		jScrollPaneDataSourceList.getViewport().add(jListDataSource, null);
		jPanelMain.add(jScrollPaneDataSourceList, BorderLayout.CENTER);

		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(350, 45));
		jButtonCancel.setBounds(new Rectangle(18, 10, 100, 27));
		jButtonCancel.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogAddFieldToFunction_jButtonCancel_actionAdapter(this));
		jButtonSelectAll.setBounds(new Rectangle(128, 10, 100, 27));
		jButtonSelectAll.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonSelectAll.setText(res.getString("SelectAll"));
		jButtonSelectAll.addActionListener(new DialogAddFieldToFunction_jButtonSelectAll_actionAdapter(this));
		jButtonOK.setBounds(new Rectangle(238, 10, 100, 27));
		jButtonOK.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonOK.setText(res.getString("Add"));
		jButtonOK.addActionListener(new DialogAddFieldToFunction_jButtonOK_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonCancel);
		jPanelButtons.add(jButtonSelectAll);
		jPanelButtons.add(jButtonOK);

		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(360, 450));
		this.getContentPane().add(jPanelMain,  BorderLayout.CENTER);
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		this.pack();
	}

	public int request(org.w3c.dom.Element objectElement, String tableType) {
		NodeList nodeList;
		NodeList functionFieldNodeList = null;
		NodeList functionFieldNodeList2 = null;
		org.w3c.dom.Element wrkElement, fieldElement;
		String tableID = "";
		String wrkStr, dataType;
		org.w3c.dom.Element tableElement = null;
		StringTokenizer workTokenizer;
	
		objectElement_ = objectElement;
		tableType_ = tableType;
		result = 0;
	
		this.setTitle(res.getString("AddFields"));

		if (tableType_.equals("Function010FieldList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function100ColumnList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function100FilterList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function110ColumnList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function110FilterList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function110BatchFieldList")) {
			tableID = objectElement.getAttribute("BatchTable");
		}
		if (tableType_.equals("Function200FieldList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function200TabFieldList")) {
			wrkElement = (org.w3c.dom.Element)objectElement.getParentNode();
			tableID = wrkElement.getAttribute("PrimaryTable");
			functionFieldNodeList2 = ((org.w3c.dom.Element)objectElement.getParentNode()).getElementsByTagName("Field");
		}
		if (tableType_.equals("Function210FieldList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function290PhraseList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function300HeaderFieldList")) {
			tableID = objectElement.getAttribute("HeaderTable");
		}
		if (tableType_.equals("Function300DetailFieldList")) {
			tableID = objectElement.getAttribute("Table");
		}
		if (tableType_.equals("Function300DetailFilterList")) {
			tableID = objectElement.getAttribute("Table");
		}
		if (tableType_.equals("Function310HeaderFieldList")) {
			tableID = objectElement.getAttribute("HeaderTable");
		}
		if (tableType_.equals("Function310DetailFieldList")) {
			tableID = objectElement.getAttribute("DetailTable");
		}
//		if (tableType_.equals("Function310AddRowListColumnList")) {
//			tableID = objectElement.getAttribute("AddRowListTable");
//		}
		if (tableType_.equals("Function290PhraseList")) {
			tableID = objectElement.getAttribute("PrimaryTable");
		}
		if (tableType_.equals("Function390HeaderPhraseList")) {
			tableID = objectElement.getAttribute("HeaderTable");
		}
		if (tableType_.equals("Function390DetailFieldList")) {
			tableID = objectElement.getAttribute("Table");
		}

		if (tableType_.equals("Function200TabFieldList")) {
			functionFieldNodeList = ((org.w3c.dom.Element)objectElement.getParentNode()).getElementsByTagName(frame_.getFieldTagNameAccordingComponentType(tableType_));
		} else {
			functionFieldNodeList = objectElement.getElementsByTagName(frame_.getFieldTagNameAccordingComponentType(tableType_));
		}

		tableIDList.clear();
		tableAliasList.clear();
		fieldIDList.clear();
		fieldNameList.clear();
		isNumericList.clear();
		isKubunList.clear();
		tableElement = frame_.getSpecificXETreeNode("Table", tableID).getElement();
		nodeList = tableElement.getElementsByTagName("Field");
		sortingList = frame_.getSortedListModel(nodeList, "Order");
		for (int i = 0; i < sortingList.getSize(); i++) {
			wrkElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
			tableIDList.add(tableID);
			tableAliasList.add(tableID);
			fieldIDList.add(wrkElement.getAttribute("ID"));
			fieldNameList.add(wrkElement.getAttribute("Name"));
			dataType = frame_.getBasicTypeOf(wrkElement.getAttribute("Type"));
			if (dataType.equals("INTEGER") || dataType.equals("FLOAT")) {
				isNumericList.add(true);
			} else {
				isNumericList.add(false);
			}
			if (wrkElement.getAttribute("TypeOptions").contains("KUBUN(")) {
				isKubunList.add(true);
			} else {
				isKubunList.add(false);
			}
		}
		nodeList = tableElement.getElementsByTagName("Refer");
		sortingList = frame_.getSortedListModel(nodeList, "Order");
		for (int i = 0; i < sortingList.getSize(); i++) {
			wrkElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
			workTokenizer = new StringTokenizer(wrkElement.getAttribute("Fields"), ";" );
			while (workTokenizer.hasMoreTokens()) {
				wrkStr = workTokenizer.nextToken();
				tableIDList.add(wrkElement.getAttribute("ToTable"));
				if (wrkElement.getAttribute("TableAlias").equals("")) {
					tableAliasList.add(wrkElement.getAttribute("ToTable"));
				} else {
					tableAliasList.add(wrkElement.getAttribute("TableAlias"));
				}
				fieldIDList.add(wrkStr);
				fieldNameList.add(frame_.getFieldNames(wrkElement.getAttribute("ToTable"), wrkStr, "", false));

				fieldElement = frame_.getSpecificFieldElement(wrkElement.getAttribute("ToTable"), wrkStr);
				dataType = frame_.getBasicTypeOf(fieldElement.getAttribute("Type"));
				if (dataType.equals("INTEGER") || dataType.equals("FLOAT")) {
					isNumericList.add(true);
				} else {
					isNumericList.add(false);
				}
				if (fieldElement.getAttribute("TypeOptions").contains("KUBUN(")) {
					isKubunList.add(true);
				} else {
					isKubunList.add(false);
				}
			}
		}

		functionDataSourceList.clear();
		if (tableType_.equals("Function010FieldList")) {
			sortingList = frame_.getSortedListModel(functionFieldNodeList, "DataSource");
			for (int i = 0; i < sortingList.getSize(); i++) {
				wrkElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
				functionDataSourceList.add(wrkElement.getAttribute("DataSource"));
			}
		} else {
			if (tableType_.equals("Function200TabFieldList")) {
				sortingList = frame_.getSortedListModel(functionFieldNodeList, "DataSource");
				for (int i = 0; i < sortingList.getSize(); i++) {
					wrkElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					functionDataSourceList.add(wrkElement.getAttribute("DataSource"));
				}
				sortingList = frame_.getSortedListModel(functionFieldNodeList2, "DataSource");
				for (int i = 0; i < sortingList.getSize(); i++) {
					wrkElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					functionDataSourceList.add(wrkElement.getAttribute("DataSource"));
				}
			} else {
				if (!tableType_.equals("Function100FilterList")
						&& !tableType_.equals("Function110FilterList")
						&& !tableType_.equals("Function300DetailFilterList")) {
					sortingList = frame_.getSortedListModel(functionFieldNodeList, "Order");
					for (int i = 0; i < sortingList.getSize(); i++) {
						wrkElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
						functionDataSourceList.add(wrkElement.getAttribute("DataSource"));
					}
				}
			}
		}

		listModelDataSource.clear();
		for (int i = 0; i < fieldIDList.size(); i++) {
			if (!functionDataSourceList.contains(tableAliasList.get(i) + "." + fieldIDList.get(i))
					|| !tableAliasList.get(i).equals(tableID)) {
				DialogAddFieldToFunction_CheckBox checkBox = new DialogAddFieldToFunction_CheckBox();
				checkBox.setFont(new java.awt.Font("SansSerif", 0, 12));
				checkBox.setText(tableAliasList.get(i) + "." + fieldNameList.get(i));
				checkBox.setDataSourceName(tableAliasList.get(i) + "." + fieldIDList.get(i));
				checkBox.setNumeric(isNumericList.get(i));
				checkBox.setKubun(isKubunList.get(i));
				listModelDataSource.addElement(checkBox);
			}
		}

		if (listModelDataSource.getSize() > 0) {
			jButtonOK.setEnabled(false);
			Dimension dlgSize = this.getPreferredSize();
			Dimension frmSize = frame_.getSize();
			Point loc = frame_.getLocation();
			this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
			super.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, res.getString("NoFieldsLeftToBeAdded"));
		}

		return result;
	}

	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element wrkElement;
		org.w3c.dom.Element newElement = null;
		order = frame_.getOrderOfCurrentSelectedRow() + 1;
		boolean anySelected = false;

		for (int i = 0; i < listModelDataSource.getSize(); i++) {
			DialogAddFieldToFunction_CheckBox checkBox = (DialogAddFieldToFunction_CheckBox)listModelDataSource.getElementAt(i);
			if (checkBox.isSelected()){
				anySelected = true;
				newElement = frame_.getDomDocument().createElement(frame_.getFieldTagNameAccordingComponentType(tableType_));

				if (newElement != null) {
					result = 1;
					newElement.setAttribute("Order", Editor.getFormatted4ByteString(order));
					order++;

					if (tableType_.equals("Function290PhraseList") || tableType_.equals("Function390HeaderPhraseList")) {
						newElement.setAttribute("Block", "PARAGRAPH");
						newElement.setAttribute("Alignment", "LEFT");
						newElement.setAttribute("AlignmentMargin", "0");
						newElement.setAttribute("Value", "&DataSource(" + checkBox.getDataSourceName() + ")");
						newElement.setAttribute("FontID", frame_.getDefaultPrintFontID());
						newElement.setAttribute("FontSize", "12");
						newElement.setAttribute("FontStyle", "");
					} else {
						newElement.setAttribute("DataSource", checkBox.getDataSourceName());
						newElement.setAttribute("FieldOptions", "");

						if (tableType_.equals("Function100FilterList")
								|| tableType_.equals("Function110FilterList")
								|| tableType_.equals("Function300DetailFilterList")) {
							if (checkBox.isNumeric()) {
								newElement.setAttribute("FieldOptions", "IGNORE_IF_ZERO");	
							} else {
								if (checkBox.isKubun()) {
									newElement.setAttribute("FieldOptions", "PROMPT_LIST1");	
								}
							}
						}
						if (tableType_.equals("Function390DetailFieldList")) {
							newElement.setAttribute("Width", "10");
							if (checkBox.isNumeric()) {
								newElement.setAttribute("Alignment", "RIGHT");
							} else {
								newElement.setAttribute("Alignment", "LEFT");
							}
						}
						if (tableType_.equals("Function110ColumnList")) {
							wrkElement = frame_.currentMainTreeNode.getElement();
							if (!wrkElement.getAttribute("BatchTable").equals("")) {
								if (hasDuplicatedReferField(wrkElement.getAttribute("BatchTable"), checkBox.getDataSourceName())) {
									JOptionPane.showMessageDialog(this, res.getString("WarningMessage1")+"\n"+checkBox.getDataSourceName());
								}
							}
						}
						if (tableType_.equals("Function110BatchFieldList")) {
							wrkElement = frame_.currentMainTreeNode.getElement();
							if (hasDuplicatedReferField(wrkElement.getAttribute("PrimaryTable"), checkBox.getDataSourceName())) {
								JOptionPane.showMessageDialog(this, res.getString("WarningMessage2")+"\n"+checkBox.getDataSourceName());
							}
						}
						if (tableType_.equals("Function300HeaderFieldList")) {
							NodeList detailList = frame_.currentMainTreeNode.getElement().getElementsByTagName("Detail");
							for (int j = 0; j < detailList.getLength(); j++) {
								wrkElement = (org.w3c.dom.Element)detailList.item(j);
								if (hasDuplicatedReferField(wrkElement.getAttribute("Table"), checkBox.getDataSourceName())) {
									JOptionPane.showMessageDialog(this, res.getString("WarningMessage3")+"\n"+checkBox.getDataSourceName());
								}
							}
						}
						if (tableType_.equals("Function300DetailFieldList")) {
							wrkElement = frame_.currentMainTreeNode.getElement();
							if (hasDuplicatedReferField(wrkElement.getAttribute("HeaderTable"), checkBox.getDataSourceName())) {
								JOptionPane.showMessageDialog(this, res.getString("WarningMessage4")+"\n"+checkBox.getDataSourceName());
							}
						}
						if (tableType_.equals("Function310HeaderFieldList")) {
							wrkElement = frame_.currentMainTreeNode.getElement();
							if (hasDuplicatedReferField(wrkElement.getAttribute("DetailTable"), checkBox.getDataSourceName())) {
								JOptionPane.showMessageDialog(this, res.getString("WarningMessage4")+"\n"+checkBox.getDataSourceName());
							}
						}
						if (tableType_.equals("Function310DetailFieldList")) {
							wrkElement = frame_.currentMainTreeNode.getElement();
							if (hasDuplicatedReferField(wrkElement.getAttribute("HeaderTable"), checkBox.getDataSourceName())) {
								JOptionPane.showMessageDialog(this, res.getString("WarningMessage4")+"\n"+checkBox.getDataSourceName());
							}
						}
					}
					objectElement_.appendChild(newElement);
					frame_.informationOnThisPageChanged = true;
				}
			}
		}

		if (anySelected) {
			frame_.updateOrderOfFieldRows();
		}

		this.setVisible(false);
	}

	int checkEmulatingDataSourceName(String name, NodeList emulatableFieldList) {
		int reply = 0;
		org.w3c.dom.Element wrkElement;
		if (emulatableFieldList != null) {
			for (int j = 0; j < emulatableFieldList.getLength(); j++) {
				wrkElement = (org.w3c.dom.Element)emulatableFieldList.item(j);
				if (wrkElement.getAttribute("DataSource").equals(name)) {
					reply = 1;
					break;
				}
			}
		}
		return reply;
	}

	boolean hasDuplicatedReferField(String tableID, String dataSourceName) {
		boolean isDuplicated = false;
		org.w3c.dom.Element wrkElement;
		NodeList nodeList;
		StringTokenizer wrkTokenizer;
		String wrkStr;

		wrkElement = frame_.getSpecificXETreeNode("Table", tableID).getElement();
		nodeList = wrkElement.getElementsByTagName("Refer");
		for (int k = 0; k < nodeList.getLength(); k++) {
			wrkElement = (org.w3c.dom.Element)nodeList.item(k);
			wrkTokenizer = new StringTokenizer(wrkElement.getAttribute("Fields"), ";" );
			while (wrkTokenizer.hasMoreTokens()) {
				wrkStr = wrkTokenizer.nextToken();
				if (wrkElement.getAttribute("TableAlias").equals("")) {
					wrkStr = wrkElement.getAttribute("ToTable") + "." + wrkStr;
				} else {
					wrkStr = wrkElement.getAttribute("TableAlias") + "." + wrkStr;
				}
				if (wrkStr.equals(dataSourceName)) {
					isDuplicated = true;
					break;
				}
			}
			if (isDuplicated) {
				break;
			}
		}
		return isDuplicated;
	}

	void jButtonSelectAll_actionPerformed(ActionEvent e) {
		for (int i = 0; i < listModelDataSource.getSize(); i++) {
			DialogAddFieldToFunction_CheckBox checkBox = (DialogAddFieldToFunction_CheckBox)listModelDataSource.getElementAt(i);
			checkBox.setSelected(true);
		}
		jListDataSource.updateUI();
		if (listModelDataSource.getSize() > 0) {
			jButtonOK.setEnabled(true);
		}
	}

	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void jListDataSource_mouseClicked(MouseEvent e) {
	    Point p = e.getPoint();
	    int index = jListDataSource.locationToIndex(p);

	    if (index > -1) {
	    	JCheckBox checkBox = (JCheckBox)listModelDataSource.getElementAt(index);
    		if (checkBox.isSelected()){
    			checkBox.setSelected(false);
    		} else {
    			checkBox.setSelected(true);
    		}

	    	jListDataSource.repaint();
	    }

		jButtonOK.setEnabled(false);
		for (int i = 0; i < listModelDataSource.getSize(); i++) {
			JCheckBox checkBox = (JCheckBox)listModelDataSource.getElementAt(i);
			if (checkBox.isSelected()){
				jButtonOK.setEnabled(true);
				break;
			}
		}
	}
}

class DialogAddFieldToFunction_CheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;
	private String dataSourceName_ = "";
	private boolean isNumeric_ = false;
	private boolean isKubun_ = false;
	DialogAddFieldToFunction_CheckBox() {
		super();
	}
	public void setDataSourceName(String name) {
		dataSourceName_ = name;
	}
	public void setNumeric(boolean isNumeric) {
		isNumeric_ = isNumeric;
	}
	public void setKubun(boolean isKubun) {
		isKubun_ = isKubun;
	}
	public String getDataSourceName() {
		return dataSourceName_;
	}
	public boolean isNumeric() {
		return isNumeric_;
	}
	public boolean isKubun() {
		return isKubun_;
	}
}

class DialogAddFieldToFunction_jListDataSource_mouseAdapter extends java.awt.event.MouseAdapter {
	DialogAddFieldToFunction adaptee;
	DialogAddFieldToFunction_jListDataSource_mouseAdapter(DialogAddFieldToFunction adaptee) {
		this.adaptee = adaptee;
	}
	public void mouseClicked(MouseEvent e) {
		adaptee.jListDataSource_mouseClicked(e);
	}
}

class DialogAddFieldToFunction_jButtonOK_actionAdapter implements java.awt.event.ActionListener {
	DialogAddFieldToFunction adaptee;
	DialogAddFieldToFunction_jButtonOK_actionAdapter(DialogAddFieldToFunction adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonOK_actionPerformed(e);
	}
}

class DialogAddFieldToFunction_jButtonSelectAll_actionAdapter implements java.awt.event.ActionListener {
	DialogAddFieldToFunction adaptee;
	DialogAddFieldToFunction_jButtonSelectAll_actionAdapter(DialogAddFieldToFunction adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonSelectAll_actionPerformed(e);
	}
}

class DialogAddFieldToFunction_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogAddFieldToFunction adaptee;
	DialogAddFieldToFunction_jButtonCancel_actionAdapter(DialogAddFieldToFunction adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}
