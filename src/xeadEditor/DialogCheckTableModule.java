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
import org.w3c.dom.NodeList;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import xeadEditor.Editor.SortableDomElementListModel;
import xeadEditor.Editor.MainTreeNode;

public class DialogCheckTableModule extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private static String DEFAULT_UPDATE_COUNTER = "UPDCOUNTER";
	private JButton jButtonCreate = new JButton();
	private JButton jButtonAlter = new JButton();
	private JButton jButtonDelete = new JButton();
	private JButton jButtonClose = new JButton();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private Editor frame_;
	private JPanel jPanelButtons = new JPanel();
	private String errorStatus = "";
	private ArrayList<String> keyFieldList = new ArrayList<String>();
	private ArrayList<String> keyFieldTypeList = new ArrayList<String>();
	private ArrayList<String> fieldListToBeAdded = new ArrayList<String>();
	private ArrayList<String> fieldListToBeDropped = new ArrayList<String>();
	private ArrayList<String> fieldListToBeConverted = new ArrayList<String>();
	private ArrayList<String> fieldTypeListToBeConverted = new ArrayList<String>();
	private ArrayList<Integer> fieldSizeListToBeConvertedOld = new ArrayList<Integer>();
	private ArrayList<Integer> fieldSizeListToBeConvertedNew = new ArrayList<Integer>();
	private ArrayList<Integer> fieldDecimalListToBeConvertedOld = new ArrayList<Integer>();
	private ArrayList<Integer> fieldDecimalListToBeConvertedNew = new ArrayList<Integer>();
	private ArrayList<ArrayList<Object>> keyValueList = new ArrayList<ArrayList<Object>>();
	private ArrayList<ArrayList<Object>> fieldValueList = new ArrayList<ArrayList<Object>>();
	private ArrayList<String> fieldListToBeNullable = new ArrayList<String>();
	private ArrayList<String> fieldListToBeNotNull = new ArrayList<String>();
	private ArrayList<String> addingSKList = new ArrayList<String>();
	private ArrayList<String> addingXKList = new ArrayList<String>();
	private ArrayList<String> indexListToBeDropped = new ArrayList<String>();
	private boolean isDifferentPK;
	private boolean isWithoutModule;
	private boolean isWithoutPK;
	private org.w3c.dom.Element tableElement;
	private Connection connection_;
	private String updateCounterID;
	
	public DialogCheckTableModule(Editor frame) {
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
		//
		jScrollPaneMessage.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(new BorderLayout());
		//
		jTextAreaMessage.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setTabSize(4);
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		//
		jButtonClose.setText(res.getString("Close"));
		jButtonClose.setBounds(new Rectangle(30, 8, 80, 25));
		jButtonClose.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonClose.addActionListener(new DialogCheckTableModule_jButtonClose_actionAdapter(this));
		jButtonAlter.setText(res.getString("ModuleModify"));
		jButtonAlter.setBounds(new Rectangle(140, 8, 130, 25));
		jButtonAlter.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonAlter.addActionListener(new DialogCheckTableModule_jButtonAlter_actionAdapter(this));
		jButtonCreate.setText(res.getString("ModuleCreate"));
		jButtonCreate.setBounds(new Rectangle(300, 8, 130, 25));
		jButtonCreate.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCreate.addActionListener(new DialogCheckTableModule_jButtonCreate_actionAdapter(this));
		jButtonDelete.setText(res.getString("ModuleDelete"));
		jButtonDelete.setBounds(new Rectangle(460, 8, 130, 25));
		jButtonDelete.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonDelete.addActionListener(new DialogCheckTableModule_jButtonDelete_actionAdapter(this));
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(400, 41));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonClose, null);
		jPanelButtons.add(jButtonAlter, null);
		jPanelButtons.add(jButtonCreate, null);
		jPanelButtons.add(jButtonDelete, null);
		//
		this.setTitle(res.getString("ModuleCheck"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(620, 300));
		this.getContentPane().add(jScrollPaneMessage,  BorderLayout.CENTER);
	}

	public String request(Connection connection, MainTreeNode tableNode, boolean isShowDialog) {
		//
		errorStatus = tableNode.getErrorStatus();
		//
		if (connection != null && tableNode.getType().equals("Table")) {
			//
			connection_ = connection;
			tableElement = tableNode.getElement();
			//
		    updateCounterID = tableElement.getAttribute("UpdateCounter");
		    if (updateCounterID.equals("")) {
		    	updateCounterID = DEFAULT_UPDATE_COUNTER;
		    }
			//
			checkTableModule("");
			//
			if (isShowDialog) {
				jPanelButtons.getRootPane().setDefaultButton(jButtonClose);
				Dimension dlgSize = this.getPreferredSize();
				Dimension frmSize = frame_.getSize();
				Point loc = frame_.getLocation();
				this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
				super.setVisible(true);
			}
		}
		//
		return errorStatus;
	}

	void checkTableModule(String requestType) {
		org.w3c.dom.Element element;
		String wrkStr, tableID, fieldID;
		StringBuffer buf = new StringBuffer();
		StringBuffer moduleBuf = new StringBuffer();
		int sizeOfModuleField, sizeOfDefinitionField;
		int decimalOfModuleField, decimalOfDefinitionField;
		String typeDescriptionsOfModuleField, typeDescriptionsOfDefinitionField;
		boolean isNullableOfModuleField, isNullableOfDefinitionField;
		int countOfErrors = 0;
		boolean exist;
		StringTokenizer workTokenizer, workTokenizer2;
		ArrayList<String> fieldList1 = new ArrayList<String>();
		ArrayList<String> ascDescList1 = new ArrayList<String>();
		ArrayList<String> fieldList2 = new ArrayList<String>();
		ArrayList<String> ascDescList2 = new ArrayList<String>();
		ArrayList<String> indexNameList = new ArrayList<String>();
		ArrayList<String> indexFieldsList = new ArrayList<String>();
		ArrayList<String> indexAscDescList = new ArrayList<String>();
		ArrayList<String> indexNotUniqueList = new ArrayList<String>();
		ArrayList<String> foreignKeyNameList = new ArrayList<String>();
		ArrayList<String> foreignKeyTableList = new ArrayList<String>();
		ArrayList<String> foreignKeyFieldList = new ArrayList<String>();
		ArrayList<String> nativeFieldList = new ArrayList<String>();
		int workIndex, count1, count2, wrkInt;
		//
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			keyFieldList.clear();
			keyFieldTypeList.clear();
			fieldListToBeAdded.clear();
			fieldListToBeDropped.clear();
			fieldListToBeConverted.clear();
			fieldTypeListToBeConverted.clear();
			fieldSizeListToBeConvertedOld.clear();
			fieldSizeListToBeConvertedNew.clear();
			fieldDecimalListToBeConvertedOld.clear();
			fieldDecimalListToBeConvertedNew.clear();
			fieldListToBeNullable.clear();
			fieldListToBeNotNull.clear();
			addingSKList.clear();
			addingXKList.clear();
			indexListToBeDropped.clear();
			isDifferentPK = false;
			isWithoutModule = false;
			isWithoutPK = true;
			//
			tableID = tableElement.getAttribute("ID");
			if (frame_.getDatabaseName().contains("jdbc:postgresql")) {
				tableID = tableID.toLowerCase();
			}
			ResultSet rs1 = connection_.getMetaData().getColumns(null, null, tableID, null);
			if (rs1.next()) {
				//
				moduleBuf.append("Create table " + tableElement.getAttribute("ID") + " (\n");
				//
				///////////////////////////////////////////
				// Field check from definition to module //
				///////////////////////////////////////////
				NodeList fieldList = tableElement.getElementsByTagName("Field");
				SortableDomElementListModel sortingList = frame_.getSortedListModel(fieldList, "Order");
			    for (int i = 0; i < sortingList.getSize(); i++) {
			        element = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (!frame_.getOptionList(element.getAttribute("TypeOptions")).contains("VIRTUAL")) {
						//
						sizeOfDefinitionField = Integer.parseInt(element.getAttribute("Size"));
						if (element.getAttribute("Decimal").equals("")) {
							decimalOfDefinitionField = 0;
						} else {
							decimalOfDefinitionField = Integer.parseInt(element.getAttribute("Decimal"));
						}
						if (frame_.isWithDecimal(element.getAttribute("Type"))) {
							typeDescriptionsOfDefinitionField = element.getAttribute("Type") + "(" + element.getAttribute("Size") + "," + decimalOfDefinitionField + ")";
						} else {
							typeDescriptionsOfDefinitionField = element.getAttribute("Type") + "(" + element.getAttribute("Size") + ")";
						}
						if (element.getAttribute("Nullable").equals("T")) {
							isNullableOfDefinitionField = true;
						} else {
							isNullableOfDefinitionField = false;
						}
						//
						moduleBuf.append("\t" + element.getAttribute("ID") + " ");
						moduleBuf.append(element.getAttribute("Type"));
						if (getBasicTypeOf(element.getAttribute("Type")).equals("INTEGER")) {
							moduleBuf.append(" Default 0");
						} else {
							if (getBasicTypeOf(element.getAttribute("Type")).equals("FLOAT")) {
								if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
									moduleBuf.append("(");
									moduleBuf.append(element.getAttribute("Size"));
									moduleBuf.append(",");
									moduleBuf.append(element.getAttribute("Decimal"));
									moduleBuf.append(")");
								}
								moduleBuf.append(" Default 0.0");
							} else {
								if (element.getAttribute("Type").equals("CHAR") || element.getAttribute("Type").equals("VARCHAR")) {
									moduleBuf.append("(");
									moduleBuf.append(element.getAttribute("Size"));
									moduleBuf.append(")");
								}
							}
						}
						if (element.getAttribute("Nullable").equals("T")) {
							moduleBuf.append(" null");
						} else {
							moduleBuf.append("");
						}
						moduleBuf.append(" Comment '" + element.getAttribute("Name") + "',\n");
						//
						fieldID = element.getAttribute("ID");
						if (frame_.getDatabaseName().contains("jdbc:postgresql")) {
							fieldID = fieldID.toLowerCase();
						}
						ResultSet rs2 = connection_.getMetaData().getColumns(null, null, tableID, fieldID);
						if (rs2.next()) {
							//
							sizeOfModuleField = Integer.parseInt(rs2.getString("COLUMN_SIZE"));
							if (rs2.getString("DECIMAL_DIGITS") == null) {
								decimalOfModuleField = 0;
							} else {
								decimalOfModuleField = Integer.parseInt(rs2.getString("DECIMAL_DIGITS"));
							}
							//
							if (element.getAttribute("Type").equals("DECIMAL")
									|| element.getAttribute("Type").equals("NUMERIC")) {
								typeDescriptionsOfModuleField = rs2.getString("TYPE_NAME") + "(" + rs2.getString("COLUMN_SIZE") + "," + decimalOfModuleField + ")";
							} else {
								if (element.getAttribute("Type").equals("CHAR")) {
									typeDescriptionsOfModuleField = rs2.getString("TYPE_NAME") + "(" + rs2.getString("COLUMN_SIZE") + ")";
								} else {
									typeDescriptionsOfModuleField = rs2.getString("TYPE_NAME");
								}
							}
							//
							if (rs2.getString("IS_NULLABLE").equals("YES")) {
								isNullableOfModuleField = true;
							} else {
								isNullableOfModuleField = false;
							}
							//
							if (isEquivalentDataType(element.getAttribute("Type"), rs2.getString("TYPE_NAME"))) {
								if (element.getAttribute("Type").equals("CHAR")
										|| element.getAttribute("Type").equals("DECIMAL")
										|| element.getAttribute("Type").equals("NUMERIC")) {
									if (sizeOfDefinitionField != sizeOfModuleField || decimalOfDefinitionField != decimalOfModuleField) {
										countOfErrors++;
										fieldListToBeDropped.add(element.getAttribute("ID"));
										fieldListToBeAdded.add(element.getAttribute("ID"));
										fieldListToBeConverted.add(element.getAttribute("ID"));
										fieldTypeListToBeConverted.add(element.getAttribute("Type"));
										fieldSizeListToBeConvertedOld.add(sizeOfModuleField);
										fieldSizeListToBeConvertedNew.add(sizeOfDefinitionField);
										fieldDecimalListToBeConvertedOld.add(decimalOfModuleField);
										fieldDecimalListToBeConvertedNew.add(decimalOfDefinitionField);
										buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage1") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage2") + typeDescriptionsOfDefinitionField + res.getString("ModuleCheckMessage3") + typeDescriptionsOfModuleField + res.getString("ModuleCheckMessage4"));
									}
								}
								if (element.getAttribute("Type").equals("VARCHAR")) {
									if (sizeOfDefinitionField != sizeOfModuleField) {
										countOfErrors++;
										fieldListToBeDropped.add(element.getAttribute("ID"));
										fieldListToBeAdded.add(element.getAttribute("ID"));
										fieldListToBeConverted.add(element.getAttribute("ID"));
										fieldTypeListToBeConverted.add(element.getAttribute("Type"));
										fieldSizeListToBeConvertedOld.add(sizeOfModuleField);
										fieldSizeListToBeConvertedNew.add(sizeOfDefinitionField);
										fieldDecimalListToBeConvertedOld.add(0);
										fieldDecimalListToBeConvertedNew.add(0);
										buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage1") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage2") + typeDescriptionsOfDefinitionField + res.getString("ModuleCheckMessage3") + typeDescriptionsOfModuleField + "(" + sizeOfModuleField + ")" + res.getString("ModuleCheckMessage4"));
									}
								}
							} else {
								countOfErrors++;
								fieldListToBeDropped.add(element.getAttribute("ID"));
								fieldListToBeAdded.add(element.getAttribute("ID"));
								buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage1") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage2") + typeDescriptionsOfDefinitionField + res.getString("ModuleCheckMessage3") + typeDescriptionsOfModuleField + res.getString("ModuleCheckMessage4"));
							}
							if (!isNullableOfModuleField && isNullableOfDefinitionField) {
								countOfErrors++;
								fieldListToBeNullable.add(element.getAttribute("ID"));
								buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage5") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage6"));
							}
							if (isNullableOfModuleField && !isNullableOfDefinitionField) {
								countOfErrors++;
								fieldListToBeNotNull.add(element.getAttribute("ID"));
								buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage7") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage8"));
							}
						} else {
							countOfErrors++;
							fieldListToBeAdded.add(element.getAttribute("ID"));
							buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage9") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage10"));
						}
						rs2.close();
					}
				}
			    //
				//////////////////////////
			    // Check update counter //
				//////////////////////////
				fieldID = updateCounterID;
				if (frame_.getDatabaseName().contains("jdbc:postgresql")) {
					fieldID = fieldID.toLowerCase();
				}
			    ResultSet rs2 = connection_.getMetaData().getColumns(null, null, tableID, fieldID);
				if (rs2.next()) {
					//if (!rs2.getString("TYPE_NAME").equals("INTEGER") && !rs2.getString("TYPE_NAME").equals("INT")) {
					if (!isEquivalentDataType("INTEGER", rs2.getString("TYPE_NAME"))) {
						countOfErrors++;
						fieldListToBeDropped.add(updateCounterID);
						fieldListToBeAdded.add(updateCounterID);
						buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage11") + updateCounterID + res.getString("ModuleCheckMessage12") + rs2.getString("TYPE_NAME") + res.getString("ModuleCheckMessage13"));
					}
				} else {
					countOfErrors++;
					fieldListToBeAdded.add(updateCounterID);
					buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage14") + updateCounterID + res.getString("ModuleCheckMessage15"));
				}
				rs2.close();
				//
				int columnCounter = 0;
				//
				///////////////////////////////////////////
				// Field check from module to definition //
				///////////////////////////////////////////
				ResultSet rs3 = connection_.getMetaData().getColumns(null, null, tableID, null);
				while (rs3.next()) {
					//
					columnCounter++;
					//
					exist = false;
					if (updateCounterID.equals(rs3.getString("COLUMN_NAME").toUpperCase())) {
						exist = true;
					} else {
						for (int i = 0; i < fieldList.getLength(); i++) {
							element = (org.w3c.dom.Element)fieldList.item(i);
							if (element.getAttribute("ID").equals(rs3.getString("COLUMN_NAME").toUpperCase())
									&& !frame_.getOptionList(element.getAttribute("TypeOptions")).contains("VIRTUAL")) {
								exist = true;
								break;
							}
						}
					}
					if (!exist) {
						sizeOfModuleField = Integer.parseInt(rs3.getString("COLUMN_SIZE"));
						if (rs3.getString("DECIMAL_DIGITS") == null) {
							decimalOfModuleField = 0;
						} else {
							decimalOfModuleField = Integer.parseInt(rs3.getString("DECIMAL_DIGITS"));
						}
						//
						if (rs3.getString("TYPE_NAME").equals("DECIMAL") || rs3.getString("TYPE_NAME").equals("NUMERIC")) {
							typeDescriptionsOfModuleField = rs3.getString("TYPE_NAME") + "(" + rs3.getString("COLUMN_SIZE") + "," + decimalOfModuleField + ")";
						} else {
							if (rs3.getString("TYPE_NAME").equals("CHAR")) {
								typeDescriptionsOfModuleField = rs3.getString("TYPE_NAME") + "(" + rs3.getString("COLUMN_SIZE") + ")";
							} else {
								typeDescriptionsOfModuleField = rs3.getString("TYPE_NAME");
							}
						}
						//
						countOfErrors++;
						fieldListToBeDropped.add(rs3.getString("COLUMN_NAME").toUpperCase());
						buf.append("(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage16") + rs3.getString("COLUMN_NAME").toUpperCase() + " [" + typeDescriptionsOfModuleField + "]" + res.getString("ModuleCheckMessage17"));
					}
				}
				rs3.close();
				//
				////////////////////////////////////////////////////////
				// Collect unique key and index information of module //
				////////////////////////////////////////////////////////
				indexNameList.clear();
				indexFieldsList.clear();
				indexAscDescList.clear();
				indexNotUniqueList.clear();
				ResultSet rs4 = connection_.getMetaData().getIndexInfo(null, null, tableID, false, true);
				while (rs4.next()) {
					//
					workIndex = indexNameList.indexOf(rs4.getString("INDEX_NAME"));
					if (workIndex == -1) {
						indexNameList.add(rs4.getString("INDEX_NAME"));
						workIndex = indexNameList.size() - 1;
						indexFieldsList.add("");
						indexAscDescList.add("");
						if (rs4.getString("NON_UNIQUE").equals("0")
								|| rs4.getString("NON_UNIQUE").equals("f")
								|| rs4.getString("NON_UNIQUE").equals("false")) {
							indexNotUniqueList.add("false");
						}
						if (rs4.getString("NON_UNIQUE").equals("1")
								|| rs4.getString("NON_UNIQUE").equals("t")
								|| rs4.getString("NON_UNIQUE").equals("true")) {
							indexNotUniqueList.add("true");
						}
					}
					if (indexFieldsList.get(workIndex).equals("")) {
						indexFieldsList.set(workIndex, rs4.getString("COLUMN_NAME").toUpperCase());
					} else {
						indexFieldsList.set(workIndex, indexFieldsList.get(workIndex) + ";" + rs4.getString("COLUMN_NAME").toUpperCase());
					}
					if (indexAscDescList.get(workIndex).equals("")) {
						if (rs4.getString("ASC_OR_DESC").equals("D")) {
							indexAscDescList.set(workIndex, "D");
						} else {
							indexAscDescList.set(workIndex, "A");
						}
					} else {
						if (rs4.getString("ASC_OR_DESC").equals("D")) {
							indexAscDescList.set(workIndex, indexAscDescList.get(workIndex) + ";D");
						} else {
							indexAscDescList.set(workIndex, indexAscDescList.get(workIndex) + ";A");
						}
					}
				}
				rs4.close();
				//
				/////////////////////////////////////////
				// Key check from module to definition //
				/////////////////////////////////////////
				NodeList keyList = tableElement.getElementsByTagName("Key");
				for (int i = 0; i < indexNameList.size(); i++) {
					exist = false;
					for (int j = 0; j < keyList.getLength(); j++) {
						fieldList1.clear();
						ascDescList1.clear();
						element = (org.w3c.dom.Element)keyList.item(j);
						workTokenizer = new StringTokenizer(element.getAttribute("Fields"), ";");
						while (workTokenizer.hasMoreTokens()) {
							wrkStr = workTokenizer.nextToken();
							wrkInt = wrkStr.indexOf("(D)");
							if (wrkInt == -1) {
								fieldList1.add(wrkStr);
								ascDescList1.add("A");
							} else {
								fieldList1.add(wrkStr.replace("(D)", ""));
								ascDescList1.add("D");
							}
						}
						if ((indexNotUniqueList.get(i).equals("false") && (element.getAttribute("Type").equals("SK") || element.getAttribute("Type").equals("PK"))) ||
								(indexNotUniqueList.get(i).equals("true") && element.getAttribute("Type").equals("XK"))) {
							//
							count1 = 0;
							count2 = 0;
							//
							fieldList2.clear();
							workTokenizer = new StringTokenizer(indexFieldsList.get(i), ";");
							while (workTokenizer.hasMoreTokens()) {
								fieldList2.add(workTokenizer.nextToken());
							}
							//
							ascDescList2.clear();
							workTokenizer = new StringTokenizer(indexAscDescList.get(i), ";");
							while (workTokenizer.hasMoreTokens()) {
								ascDescList2.add(workTokenizer.nextToken());
							}
							//
							for (int k = 0; k < fieldList2.size(); k++) {
								count1++;
								wrkInt = fieldList1.indexOf(fieldList2.get(k));
								if (wrkInt != -1 && ascDescList1.get(wrkInt).equals(ascDescList2.get(k))) {
									count2++;
								}
							}
							if (count1 == count2) {
								exist = true;
								break;
							}
						}
					}
					if (!exist && indexNotUniqueList.get(i).equals("false")) {
						countOfErrors++;
						indexListToBeDropped.add(indexNameList.get(i));
						buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage18") + indexFieldsList.get(i) + res.getString("ModuleCheckMessage19"));
					}
					if (!exist && indexNotUniqueList.get(i).equals("true")) {
						countOfErrors++;
						indexListToBeDropped.add(indexNameList.get(i));
						//
						count1 = 0;
						StringBuffer wrkBuf = new StringBuffer();
						workTokenizer = new StringTokenizer(indexFieldsList.get(i), ";");
						workTokenizer2 = new StringTokenizer(indexAscDescList.get(i), ";");
						while (workTokenizer.hasMoreTokens()) {
							if (count1 > 0) {
								wrkBuf.append(";");
							}
							wrkBuf.append(workTokenizer.nextToken());
							wrkBuf.append("(");
							wrkBuf.append(workTokenizer2.nextToken());
							wrkBuf.append(")");
							count1++;
						}
						wrkStr = wrkBuf.toString().replace("(A)", "");
						buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage20") + wrkStr + res.getString("ModuleCheckMessage21"));
					}
				}
				//
				/////////////////////////////////////////
				// Key check from definition to module //
				/////////////////////////////////////////
				int countOfKey = 0;
				int countOfSK = 0;
				boolean isWithoutPKDefined = true;
				for (int i = 0; i < keyList.getLength(); i++) {
					element = (org.w3c.dom.Element)keyList.item(i);
					if (element.getAttribute("Type").equals("PK")) {
						//
						countOfKey++;
						moduleBuf.append("Constraint " + tableElement.getAttribute("ID") + "_PK" + " Primary key (");
						moduleBuf.append(element.getAttribute("Fields").replace(";", ", ") + ")");
						//
						isWithoutPKDefined = false;
						workTokenizer = new StringTokenizer(element.getAttribute("Fields"), ";");
						while (workTokenizer.hasMoreTokens()) {
							keyFieldList.add(workTokenizer.nextToken());
						}
						for (int j = 0; j < keyFieldList.size(); j++) {
							for (int k = 0; k < fieldList.getLength(); k++) {
								element = (org.w3c.dom.Element)fieldList.item(k);
								if (element.getAttribute("ID").equals(keyFieldList.get(j))) {
									keyFieldTypeList.add(element.getAttribute("Type"));
									break;
								}
							}
							if (fieldListToBeDropped.contains(keyFieldList.get(j))) {
								countOfErrors++;
								isDifferentPK = true;
								buf.append("(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage46"));
							}
						}
						//
						count1 = 0;
						count2 = 0;
						wrkStr = "";
						//
						ResultSet rs5 = connection_.getMetaData().getPrimaryKeys(null, null, tableID);
						while (rs5.next()) {
							count1++;
							if (keyFieldList.contains(rs5.getString("COLUMN_NAME").toUpperCase())) {
								count2++;
							}
							if (wrkStr.equals("")) {
								wrkStr = wrkStr + rs5.getString("COLUMN_NAME").toUpperCase();
							} else {
								wrkStr = wrkStr + ";" + rs5.getString("COLUMN_NAME").toUpperCase();
							}
						}
						rs5.close();
						//
						if (count1 != count2) {
							keyFieldList.clear();
							countOfErrors++;
							isDifferentPK = true;
							buf.append("(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage22") + element.getAttribute("Fields") + res.getString("ModuleCheckMessage23") + wrkStr + res.getString("ModuleCheckMessage24"));
						}
					}
					//
					if (element.getAttribute("Type").equals("SK") || element.getAttribute("Type").equals("XK")) {
						//
						if (element.getAttribute("Type").equals("SK")) {
							if (countOfKey > 0) {
								moduleBuf.append(",\n");
							}
							countOfKey++;
							countOfSK++;
							moduleBuf.append("Constraint " + tableElement.getAttribute("ID") + "_SK" + countOfSK + " Unique key (");
							moduleBuf.append(element.getAttribute("Fields").replace(";", ", ") + ")");
						}
						//
						fieldList1.clear();
						ascDescList1.clear();
						exist = false;
						workTokenizer = new StringTokenizer(element.getAttribute("Fields"), ";");
						while (workTokenizer.hasMoreTokens()) {
							wrkStr = workTokenizer.nextToken();
							wrkInt = wrkStr.indexOf("(D)");
							if (wrkInt == -1) {
								fieldList1.add(wrkStr);
								ascDescList1.add("A");
							} else {
								fieldList1.add(wrkStr.replace("(D)", ""));
								ascDescList1.add("D");
							}
						}
						for (int j = 0; j < indexNameList.size(); j++) {
							if ((indexNotUniqueList.get(j).equals("false") && element.getAttribute("Type").equals("SK")) ||
								(indexNotUniqueList.get(j).equals("true") && element.getAttribute("Type").equals("XK"))) {
								//
								count1 = 0;
								count2 = 0;
								//
								fieldList2.clear();
								workTokenizer = new StringTokenizer(indexFieldsList.get(j), ";");
								while (workTokenizer.hasMoreTokens()) {
									fieldList2.add(workTokenizer.nextToken());
								}
								//
								ascDescList2.clear();
								workTokenizer = new StringTokenizer(indexAscDescList.get(j), ";");
								while (workTokenizer.hasMoreTokens()) {
									ascDescList2.add(workTokenizer.nextToken());
								}
								//
								for (int k = 0; k < fieldList2.size(); k++) {
									count1++;
									wrkInt = fieldList1.indexOf(fieldList2.get(k));
									if (wrkInt != -1 && ascDescList1.get(wrkInt).equals(ascDescList2.get(k))) {
										count2++;
									}
								}
								if (count1 == count2) {
									exist = true;
									break;
								}
							}
						}
						if (!exist && element.getAttribute("Type").equals("SK")) {
							countOfErrors++;
							addingSKList.add(element.getAttribute("Fields"));
							buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage25") + element.getAttribute("Fields") + res.getString("ModuleCheckMessage26"));
						}
						if (!exist && element.getAttribute("Type").equals("XK")) {
							countOfErrors++;
							addingXKList.add(element.getAttribute("Fields"));
							buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage27") + element.getAttribute("Fields") + res.getString("ModuleCheckMessage28"));
						}
					}
				}
				//
				///////////////////////////////////////////////
				// Collect foreign key constraints of module //
				///////////////////////////////////////////////
				foreignKeyNameList.clear();
				foreignKeyTableList.clear();
				foreignKeyFieldList.clear();
				ResultSet rs7 = connection_.getMetaData().getImportedKeys(null, null, tableID);
				while (rs7.next()) {
					//
					workIndex = foreignKeyNameList.indexOf(rs7.getString("FK_NAME"));
					if (workIndex == -1) {
						workIndex = 0;
						foreignKeyNameList.add(rs7.getString("FK_NAME"));
						foreignKeyTableList.add(rs7.getString("FKTABLE_NAME").toUpperCase());
						foreignKeyFieldList.add(rs7.getString("FKCOLUMN_NAME").toUpperCase());
						nativeFieldList.add(rs7.getString("PKCOLUMN_NAME").toUpperCase());
					} else {
						foreignKeyFieldList.set(workIndex, foreignKeyFieldList.get(workIndex) + "," + rs7.getString("FKCOLUMN_NAME").toUpperCase());
						nativeFieldList.set(workIndex, nativeFieldList.get(workIndex) + "," + rs7.getString("PKCOLUMN_NAME").toUpperCase());
					}
				}
				rs7.close();
				for (int i = 0; i < foreignKeyNameList.size(); i++) {
					moduleBuf.append(",\nConstraint " + foreignKeyNameList.get(i) + " Foreign key (" + foreignKeyFieldList.get(i) + ") References " + foreignKeyTableList.get(i) + " (" + nativeFieldList.get(i) + ")");
				}
				moduleBuf.append("\n)");
				//
				////////////////////////////////////////
				// Check PK from module to definition //
				////////////////////////////////////////
				if (isWithoutPKDefined) {
					//
					wrkStr = "";
					ResultSet rs6 = connection_.getMetaData().getPrimaryKeys(null, null, tableID);
					while (rs6.next()) {
						if (wrkStr.equals("")) {
							wrkStr = wrkStr + rs6.getString("COLUMN_NAME").toUpperCase();
						} else {
							wrkStr = wrkStr + ";" + rs6.getString("COLUMN_NAME").toUpperCase();
						}
					}
					rs6.close();
					//
					if (!wrkStr.equals("")) {
						countOfErrors++;
						isDifferentPK = true;
						buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage29") + wrkStr + res.getString("ModuleCheckMessage30"));
					}
				}
				//
				if (countOfErrors > 0) {
					errorStatus = "ER2";
				} else {
					errorStatus = "";
				}
				//
			} else {
				//
				isWithoutModule = true;
				//
				countOfErrors++;
				buf.append("(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage31"));
				//
				NodeList keyList = tableElement.getElementsByTagName("Key");
				for (int i = 0; i < keyList.getLength(); i++) {
					element = (org.w3c.dom.Element)keyList.item(i);
					if (element.getAttribute("Type").equals("PK") && !element.getAttribute("Fields").equals("")) {
						isWithoutPK = false;
						break;
					}
				}
				if (isWithoutPK) {
					countOfErrors++;
					buf.append("\n(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage38"));
				}
				//
				errorStatus = "ER1";
			}
			rs1.close();
			//
			jButtonAlter.setEnabled(false);
			jButtonCreate.setEnabled(false);
			jButtonDelete.setEnabled(false);
			//
			if (countOfErrors > 0) {
				buf.append("\n");
				//
				if (isWithoutModule) {
					//
					if (!isWithoutPK) {
						jButtonCreate.setEnabled(true);
						buf.append(res.getString("ModuleCheckMessage32"));
					}
					//
				} else {
					//
					jButtonDelete.setEnabled(true);
					//
					if (isDifferentPK) {
						buf.append(res.getString("ModuleCheckMessage33"));
					} else {
						if (fieldListToBeDropped.size() > 0 || fieldListToBeNullable.size() > 0) {
							buf.append(res.getString("ModuleCheckMessage34")); 
						}
						jButtonAlter.setEnabled(true);
					}
				}
				jTextAreaMessage.setText(buf.toString());
				//
			} else {
				jButtonDelete.setEnabled(true);
				String sql = moduleBuf.toString();
				if (requestType.equals("CREATE")) {
					jTextAreaMessage.setText(res.getString("ModuleCheckMessage43") + "\n\n< Data Descriptions >\n" + sql);
				} else {
					if (requestType.equals("ALTER")) {
						jTextAreaMessage.setText(res.getString("ModuleCheckMessage44") + "\n\n< Data Descriptions >\n" + sql);
					} else {
						if (foreignKeyNameList.size() > 0) {
							jTextAreaMessage.setText(res.getString("ModuleCheckMessage45") + "\n\n< Data Descriptions >\n" + sql);
						} else {
							jTextAreaMessage.setText(res.getString("ModuleCheckMessage35") + "\n\n< Data Descriptions >\n" + sql);
						}
					}
				}
				jTextAreaMessage.setCaretPosition(0);
			}
			//
		} catch (SQLException e) {
			jTextAreaMessage.setText(e.getMessage());
			e.printStackTrace();
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	boolean isEquivalentDataType(String dataTypeDefiition, String dataTypeModule) {
		boolean isEquivalent = false;
		if (dataTypeDefiition.equals(dataTypeModule)) {
			isEquivalent = true;
		} else {
			if (dataTypeDefiition.equals(dataTypeModule.toUpperCase())) {
				isEquivalent = true;
			} else {
				if (dataTypeDefiition.equals("SMALLINT")) {
					if (dataTypeModule.equals("int2")) {
						isEquivalent = true;
					}
				}
				if (dataTypeDefiition.equals("INTEGER")) {
					if (dataTypeModule.equals("INT")) {
						isEquivalent = true;
					}
					if (dataTypeModule.equals("int")
							|| dataTypeModule.equals("int4")) {
						isEquivalent = true;
					}
				}
				if (dataTypeDefiition.equals("BIGINT")) {
					if (dataTypeModule.equals("int8")) {
						isEquivalent = true;
					}
				}
				if (dataTypeDefiition.equals("NUMERIC")) {
					if (dataTypeModule.equals("DECIMAL")) {
						isEquivalent = true;
					}
				}
				if (dataTypeDefiition.equals("DECIMAL")) {
					if (dataTypeModule.equals("numeric")) {
						isEquivalent = true;
					}
				}
				if (dataTypeDefiition.equals("CHAR")) {
					if (dataTypeModule.equals("bpchar")) {
						isEquivalent = true;
					}
				}
				if (dataTypeDefiition.equals("LONG VARCHAR")) {
					if (dataTypeModule.equals("MEDIUMTEXT")) {
						isEquivalent = true;
					}
					if (dataTypeModule.equals("text")) {
						isEquivalent = true;
					}
				}
			}
		}
		return isEquivalent;
	}

	void jButtonAlter_actionPerformed(ActionEvent e) {
		StringBuffer buf = null;
		org.w3c.dom.Element element;
		int wrkCount;
		StringTokenizer workTokenizer;
		String wrkStr;
		boolean firstField = true;
		//
		Object[] bts = {res.getString("Cancel"), res.getString("Execute")};
		int rtn = JOptionPane.showOptionDialog(this, res.getString("ModuleCheckMessage36"), res.getString("ModuleModify") + " " + tableElement.getAttribute("ID") + " " + tableElement.getAttribute("Name"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[1]);
		if (rtn == 1) {
			try {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				//
				Statement statement = connection_.createStatement();
				//
				//////////////////////////////////
				// Alter column as NULL-allowed //
				//////////////////////////////////
				for (int i = 0; i < fieldListToBeNullable.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					buf.append(tableElement.getAttribute("ID"));
					buf.append(" ALTER COLUMN ");
					buf.append(fieldListToBeNullable.get(i));
					buf.append(" NULL");
					statement.executeUpdate(buf.toString());
				}
				//
				//////////////////////////////
				// Alter column as NOT NULL //
				//////////////////////////////
				for (int i = 0; i < fieldListToBeNotNull.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					buf.append(tableElement.getAttribute("ID"));
					buf.append(" ALTER COLUMN ");
					buf.append(fieldListToBeNotNull.get(i));
					buf.append(" NOT NULL");
					statement.executeUpdate(buf.toString());
				}
				//
				////////////////
				// Drop index //
				////////////////
				for (int i = 0; i < indexListToBeDropped.size(); i++) {
					buf = new StringBuffer();
					buf.append("DROP INDEX ");
					buf.append(indexListToBeDropped.get(i));
					statement.executeUpdate(buf.toString());
				}
				//
				///////////////////////////////////////////////////////
				// Save values of fields which size is being changed //
				///////////////////////////////////////////////////////
				if (fieldListToBeConverted.size() > 0 && keyFieldList.size() > 0) {
					Object value;
					ResultSet rs1 = statement.executeQuery("SELECT * FROM " + tableElement.getAttribute("ID"));
					while (rs1.next()) {
						ArrayList<Object> keyValues = new ArrayList<Object>();
						for (int i = 0; i < keyFieldList.size(); i++) {
							value = rs1.getObject(keyFieldList.get(i));
							keyValues.add(value);
						}
						keyValueList.add(keyValues);
						ArrayList<Object> fieldValues = new ArrayList<Object>();
						for (int i = 0; i < fieldListToBeConverted.size(); i++) {
							value = rs1.getObject(fieldListToBeConverted.get(i));
							fieldValues.add(value);
						}
						fieldValueList.add(fieldValues);
					}
				}
				//
				/////////////////
				// Drop column //
				/////////////////
				for (int i = 0; i < fieldListToBeDropped.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					buf.append(tableElement.getAttribute("ID"));
					buf.append(" DROP COLUMN ");
					buf.append(fieldListToBeDropped.get(i));
					statement.executeUpdate(buf.toString());
				}
				//
				////////////////
				// Add column //
				////////////////
				NodeList fieldList = tableElement.getElementsByTagName("Field");
				for (int i = 0; i < fieldListToBeAdded.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					buf.append(tableElement.getAttribute("ID"));
					if (updateCounterID.equals(fieldListToBeAdded.get(i))) {
						buf.append(" ADD COLUMN ");
						buf.append(updateCounterID);
						buf.append(" INTEGER DEFAULT 0");
						statement.executeUpdate(buf.toString());
					} else {
						for (int j = 0; j < fieldList.getLength(); j++) {
							element = (org.w3c.dom.Element)fieldList.item(j);
							if (element.getAttribute("ID").equals(fieldListToBeAdded.get(i))) {
								buf.append(" ADD COLUMN ");
								buf.append(element.getAttribute("ID"));
								buf.append(" ");
								buf.append(element.getAttribute("Type"));
								if (getBasicTypeOf(element.getAttribute("Type")).equals("STRING")) {
									buf.append("(");
									buf.append(element.getAttribute("Size"));
									buf.append(")");
									buf.append(" DEFAULT ''");
								} else {
									if (getBasicTypeOf(element.getAttribute("Type")).equals("INTEGER")) {
										buf.append(" DEFAULT 0");
									} else {
										if (getBasicTypeOf(element.getAttribute("Type")).equals("FLOAT")) {
											if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
												buf.append("(");
												buf.append(element.getAttribute("Size"));
												buf.append(",");
												buf.append(element.getAttribute("Decimal"));
												buf.append(")");
											}
											buf.append(" DEFAULT 0.0");
										}
									}
								}
								if (element.getAttribute("Nullable").contains("F") && !element.getAttribute("Type").equals("DATE")) {
									buf.append(" NOT NULL");
								}
								statement.executeUpdate(buf.toString());
								break;
							}
						}
					}
				}
				//
				////////////////////////////////////////////////////
				// Restore values of fields which size is changed //
				////////////////////////////////////////////////////
				if (fieldListToBeConverted.size() > 0 && keyFieldList.size() > 0) {
					for (int i = 0; i < keyValueList.size(); i++) {
						buf = new StringBuffer();
						buf.append("update ");
						buf.append(tableElement.getAttribute("ID"));
						buf.append(" set ");
						//
						firstField = true;
						for (int j = 0; j < fieldListToBeConverted.size(); j++) {
							if (!firstField) {
								buf.append(", ");
							}
							buf.append(fieldListToBeConverted.get(j)) ;
							buf.append("=");
							buf.append(getTableOperationValue(fieldValueList.get(i).get(j), fieldTypeListToBeConverted.get(j), fieldSizeListToBeConvertedOld.get(j), fieldSizeListToBeConvertedNew.get(j), fieldDecimalListToBeConvertedOld.get(j), fieldDecimalListToBeConvertedNew.get(j)));
							firstField = false;
						}
						//
						buf.append(" where ") ;
						//
						firstField = true;
						for (int j = 0; j < keyFieldList.size(); j++) {
							if (!firstField) {
								buf.append(" and ") ;
							}
							buf.append(keyFieldList.get(j)) ;
							buf.append("=") ;
							buf.append(getTableOperationValue(keyValueList.get(i).get(j), keyFieldTypeList.get(j), 0, 0, 0, 0));
							firstField = false;
						}
						//
						int recordCount = statement.executeUpdate(buf.toString());
						if (recordCount == 1) {
							connection_.commit();
						} else {
							try {
								connection_.rollback();
							} catch (SQLException e6) {
								e6.printStackTrace();
							}
						}
					}
					keyValueList.clear();
					fieldValueList.clear();
				}
				//
				///////////////////////
				// Add Secondary Key //
				///////////////////////
				for (int i = 0; i < addingSKList.size(); i++) {
					buf = new StringBuffer();
					buf.append("CREATE UNIQUE INDEX UniqueIndexOf");
					buf.append(tableElement.getAttribute("ID"));
					buf.append("With");
					wrkCount = -1;
					workTokenizer = new StringTokenizer(addingSKList.get(i), ";");
					while (workTokenizer.hasMoreTokens()) {
						wrkCount++;
						if (wrkCount > 0) {
							buf.append("_");
						}
						buf.append(workTokenizer.nextToken());
					}
					buf.append(" ON ");
					buf.append(tableElement.getAttribute("ID"));
					buf.append("(");
					wrkCount = -1;
					workTokenizer = new StringTokenizer(addingSKList.get(i), ";");
					while (workTokenizer.hasMoreTokens()) {
						wrkCount++;
						if (wrkCount > 0) {
							buf.append(", ");
						}
						buf.append(workTokenizer.nextToken());
					}
					buf.append(")");
					statement.executeUpdate(buf.toString());
				}
				//
				///////////////
				// Add Index //
				///////////////
				for (int i = 0; i < addingXKList.size(); i++) {
					buf = new StringBuffer();
					buf.append("CREATE INDEX Index");
					buf.append(tableElement.getAttribute("ID"));
					buf.append("With");
					wrkCount = -1;
					workTokenizer = new StringTokenizer(addingXKList.get(i), ";");
					while (workTokenizer.hasMoreTokens()) {
						wrkCount++;
						if (wrkCount > 0) {
							buf.append("_");
						}
						wrkStr = workTokenizer.nextToken();
						if (wrkStr.contains("(D)")) {
							wrkStr = wrkStr.replace("(D)", "_desc");
							buf.append(wrkStr);
						} else {
							buf.append(wrkStr);
						}
					}
					buf.append(" ON ");
					buf.append(tableElement.getAttribute("ID"));
					buf.append("(");
					wrkCount = -1;
					workTokenizer = new StringTokenizer(addingXKList.get(i), ";");
					while (workTokenizer.hasMoreTokens()) {
						wrkCount++;
						if (wrkCount > 0) {
							buf.append(", ");
						}
						wrkStr = workTokenizer.nextToken();
						if (wrkStr.contains("(D)")) {
							wrkStr = wrkStr.replace("(D)", " DESC");
						}
						buf.append(wrkStr);
					}
					buf.append(")");
					statement.executeUpdate(buf.toString());
				}
				//
				//////////////////////////////////
				// Commit all changes to module //
				//////////////////////////////////
				connection_.commit();
				//
				///////////////////////////////////////////
				// Check module and refresh error status //
				///////////////////////////////////////////
				checkTableModule("ALTER");
				//
			} catch (SQLException ex1) {
				JOptionPane.showMessageDialog(this, res.getString("ModuleCheckMessage37") + buf .toString() + "\n" + ex1.getMessage());
				try {
					connection_.rollback();
				} catch (SQLException ex2) {
					ex2.printStackTrace();
				}
			} finally {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	public Object getTableOperationValue(Object value, String type, int sizeOld, int sizeNew, int decimalOld, int decimalNew){
		Object returnValue = "";
		String wrkStr1, wrkStr2;
		String basicType = frame_.getBasicTypeOf(type);
		//
		if (basicType.equals("INTEGER")) {
			returnValue = Long.parseLong(value.toString());
		}
		if (basicType.equals("FLOAT")) {
			wrkStr1 = value.toString();
			returnValue = Double.parseDouble(wrkStr1);
			if (sizeNew < sizeOld || decimalNew < decimalOld) {
				wrkStr2 = "0";
				StringTokenizer workTokenizer = new StringTokenizer(wrkStr1, ".");
				if (workTokenizer.countTokens() >= 1) {
					wrkStr1 = workTokenizer.nextToken();
					if (wrkStr1.length() > (sizeNew-decimalNew)) {
						wrkStr1 = wrkStr1.substring(wrkStr1.length()-(sizeNew-decimalNew), wrkStr1.length());
					}
				} else {
					wrkStr1 = "0";
				}
				if (decimalNew > 0) {
					if (workTokenizer.countTokens() == 1) {
						wrkStr2 = workTokenizer.nextToken();
						if (wrkStr2.length() > decimalNew) {
							wrkStr2 = wrkStr2.substring(0, decimalNew);
						}
					}
					returnValue = Double.parseDouble(wrkStr1 + "." + wrkStr2);
				} else {
					returnValue = Double.parseDouble(wrkStr1);
				}
			}
		}
		if (basicType.equals("STRING")) {
			wrkStr1 = value.toString().trim();
			returnValue = "'" + wrkStr1 + "'";
			if (sizeNew < sizeOld) {
				if (wrkStr1.length() > sizeNew) {
					returnValue = "'" + wrkStr1.substring(0, sizeNew) + "'";
				}
			}
		}
		if (basicType.equals("DATE")) {
			String strDate = (String)value;
			if (strDate == null || strDate.equals("")) {
				returnValue = "NULL";
			} else {
				returnValue = "'" + strDate + "'";
			}
		}
		if (basicType.equals("DATETIME")) {
			String timeDate = (String)value;
			if (timeDate == null || timeDate.equals("")) {
				returnValue = "NULL";
			} else {
				if (timeDate.equals("CURRENT_TIMESTAMP")) {
					returnValue = timeDate;
				} else {
					timeDate = timeDate.replace("/", "-");
					returnValue = "'" + timeDate + "'";
				}
			}
		}
		return returnValue;
	}
	
	void jButtonCreate_actionPerformed(ActionEvent e) {
		int countOfPhysicalFields = 0;
		org.w3c.dom.Element element;
		String sqlText = "";
		//
		NodeList fieldList = tableElement.getElementsByTagName("Field");
		for (int i = 0; i < fieldList.getLength(); i++) {
			element = (org.w3c.dom.Element)fieldList.item(i);
			if (!element.getAttribute("TypeOptions").contains("VIRTUAL")) {
				countOfPhysicalFields++;
			}
		}
		//
		if (countOfPhysicalFields == 0) {
			JOptionPane.showMessageDialog(this, res.getString("ModuleCheckMessage38"));
		} else {
			Object[] bts = {res.getString("Cancel"), res.getString("Execute")};
			int rtn = JOptionPane.showOptionDialog(this, res.getString("ModuleCheckMessage39"), res.getString("ModuleCreate") + " " + tableElement.getAttribute("ID") + " " + tableElement.getAttribute("Name"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[1]);
			if (rtn == 1) {
				try {
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
					//
					Statement statement = connection_.createStatement();
					sqlText = getSqlToCreateTable();
					statement.executeUpdate(sqlText);
					connection_.commit();
					//
					checkTableModule("CREATE");
					//
				} catch (SQLException ex1) {
					ex1.printStackTrace();
					JOptionPane.showMessageDialog(this, res.getString("ModuleCheckMessage40") + sqlText + "\n" + ex1.getMessage());
					try {
						connection_.rollback();
					} catch (SQLException ex2) {
						ex2.printStackTrace();
					}
				} finally {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
	}

	void jButtonDelete_actionPerformed(ActionEvent e) {
		Object[] bts = {res.getString("Cancel"), res.getString("Execute")};
		int rtn = JOptionPane.showOptionDialog(this, res.getString("ModuleCheckMessage41"), res.getString("ModuleDelete") + " " + tableElement.getAttribute("ID") + " " + tableElement.getAttribute("Name"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[0]);
		if (rtn == 1) {
			String tableID = tableElement.getAttribute("ID");
			if (frame_.getDatabaseName().contains("jdbc:postgresql")) {
				tableID = tableID.toLowerCase();
			}
			deleteTable(connection_, tableID);
		}
	}

	public void deleteTable(Connection connection, String tableID) {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			Statement statement = connection.createStatement();
			statement.executeUpdate("DROP TABLE " + tableID);
			connection.commit();
			//
			checkTableModule("DROP");
			//
		} catch (SQLException ex1) {
			ex1.printStackTrace();
			JOptionPane.showMessageDialog(this, res.getString("ModuleCheckMessage42"));
			try {
				connection.rollback();
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	String getSqlToCreateTable() {
		StringBuffer buf = new StringBuffer();
		org.w3c.dom.Element element;
		StringTokenizer workTokenizer;
		String wrkStr;
		boolean firstField = true;
		//
		buf.append("CREATE TABLE ");
		buf.append(tableElement.getAttribute("ID"));
		buf.append(" (\n");
		//
		NodeList fieldList = tableElement.getElementsByTagName("Field");
		SortableDomElementListModel sortingList = frame_.getSortedListModel(fieldList, "Order");
	    for (int i = 0; i < sortingList.getSize(); i++) {
	        element = (org.w3c.dom.Element)sortingList.getElementAt(i);
			if (!element.getAttribute("TypeOptions").contains("VIRTUAL")) {
				if (firstField) {
					firstField = false;
				} else {
					buf.append(",\n");
				}
				buf.append("     ");
				buf.append(element.getAttribute("ID"));
				buf.append(" ");
				if (frame_.getDatabaseName().contains("jdbc:postgresql") && element.getAttribute("Type").equals("LONG VARCHAR")) {
					buf.append("text");
				} else {
					buf.append(element.getAttribute("Type"));
				}
				if (getBasicTypeOf(element.getAttribute("Type")).equals("STRING")) {
					if (element.getAttribute("Type").equals("CHAR") || element.getAttribute("Type").equals("VARCHAR")) {
						buf.append("(");
						buf.append(element.getAttribute("Size"));
						buf.append(")");
					}
				} else {
					if (getBasicTypeOf(element.getAttribute("Type")).equals("INTEGER")) {
						buf.append(" DEFAULT 0");
					} else {
						if (getBasicTypeOf(element.getAttribute("Type")).equals("FLOAT")) {
							if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
								buf.append("(");
								buf.append(element.getAttribute("Size"));
								buf.append(",");
								buf.append(element.getAttribute("Decimal"));
								buf.append(")");
							}
							buf.append(" DEFAULT 0.0");
						}
					}
				}
				if (element.getAttribute("Nullable").equals("T")) {
					buf.append(" NULL");
				} else {
					buf.append(" NOT NULL");
				}
			}
		}
		buf.append(",\n");
		buf.append(updateCounterID);
		buf.append(" INTEGER DEFAULT 0,\n");
		//
		int wrkCount1 = -1;
		int wrkCount2 = -1;
		int countOfSK = 0;
		NodeList keyList = tableElement.getElementsByTagName("Key");
		sortingList = frame_.getSortedListModel(keyList, "Type");
	    for (int i = 0; i < sortingList.getSize(); i++) {
	        element = (org.w3c.dom.Element)sortingList.getElementAt(i);
			if (element.getAttribute("Type").equals("PK") || element.getAttribute("Type").equals("SK")) {
				wrkCount1++;
				if (wrkCount1 > 0) {
					buf.append("),\n");
				}
				if (element.getAttribute("Type").equals("PK")) {
					buf.append("CONSTRAINT " + tableElement.getAttribute("ID") + "_PK PRIMARY KEY (");
				}
				if (element.getAttribute("Type").equals("SK")) {
					countOfSK++;
					buf.append("CONSTRAINT " + tableElement.getAttribute("ID") + "_SK" + countOfSK + " UNIQUE (");
				}
				wrkCount2 = -1;
				workTokenizer = new StringTokenizer(element.getAttribute("Fields"), ";");
				while (workTokenizer.hasMoreTokens()) {
					wrkCount2++;
					if (wrkCount2 > 0) {
						buf.append(", ");
					}
					wrkStr = workTokenizer.nextToken();
					buf.append(wrkStr);
				}
			}
		}
		buf.append(")\n)\n");
		//
		return buf.toString();
	}
	
	static String getBasicTypeOf(String dataType){
		String basicType = "";
		if (dataType.equals("INTEGER")
				|| dataType.equals("SMALLINT")
				|| dataType.equals("BIGINT")
					) {
			basicType = "INTEGER";
		}
		if (dataType.equals("DOUBLE")
				|| dataType.equals("DECIMAL")
				|| dataType.equals("DOUBLE PRECISION")
				|| dataType.equals("NUMERIC")
				|| dataType.equals("REAL")
					) {
			basicType = "FLOAT";
		}
		if (dataType.equals("CHAR")
				|| dataType.equals("VARCHAR")
				|| dataType.equals("LONG VARCHAR")
					) {
			basicType = "STRING";
		}
		if (dataType.equals("BINARY")
				|| dataType.equals("VARBINARY")
					) {
			basicType = "BINARY";
		}
		if (dataType.equals("CLOB")) {
			basicType = "CLOB";
		}
		if (dataType.equals("BLOB")) {
			basicType = "BLOB";
		}
		if (dataType.equals("DATE")) {
			basicType = "DATE";
		}
		if (dataType.equals("TIME")) {
			basicType = "TIME";
		}
		if (dataType.equals("TIMESTAMP")) {
			basicType = "DATETIME";
		}
		return basicType;
	}
}

class DialogCheckTableModule_jButtonAlter_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckTableModule adaptee;
	DialogCheckTableModule_jButtonAlter_actionAdapter(DialogCheckTableModule adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonAlter_actionPerformed(e);
	}
}

class DialogCheckTableModule_jButtonCreate_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckTableModule adaptee;
	DialogCheckTableModule_jButtonCreate_actionAdapter(DialogCheckTableModule adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCreate_actionPerformed(e);
	}
}

class DialogCheckTableModule_jButtonDelete_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckTableModule adaptee;
	DialogCheckTableModule_jButtonDelete_actionAdapter(DialogCheckTableModule adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonDelete_actionPerformed(e);
	}
}

class DialogCheckTableModule_jButtonClose_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckTableModule adaptee;
	DialogCheckTableModule_jButtonClose_actionAdapter(DialogCheckTableModule adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonClose_actionPerformed(e);
	}
}
