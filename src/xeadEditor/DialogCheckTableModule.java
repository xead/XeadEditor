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
	private JButton jButtonPut = new JButton();
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
	private ArrayList<String> fieldListToBePut = new ArrayList<String>();
	private ArrayList<String> fieldTypeListToBePut = new ArrayList<String>();
	private ArrayList<Integer> fieldSizeListToBePut = new ArrayList<Integer>();
	private ArrayList<Integer> fieldDecimalListToBePut = new ArrayList<Integer>();
	private ArrayList<String> fieldNullableListToBePut = new ArrayList<String>();
	private ArrayList<ArrayList<Object>> keyValueList = new ArrayList<ArrayList<Object>>();
	private ArrayList<ArrayList<Object>> fieldValueList = new ArrayList<ArrayList<Object>>();
	private ArrayList<String> fieldListToBeNullable = new ArrayList<String>();
	private ArrayList<String> fieldListToBeNullableDataType = new ArrayList<String>();
	private ArrayList<String> fieldListToBeNotNull = new ArrayList<String>();
	private ArrayList<String> fieldListToBeNotNullDataType = new ArrayList<String>();
	private ArrayList<String> addingSKList = new ArrayList<String>();
	private ArrayList<String> addingXKList = new ArrayList<String>();
	private ArrayList<String> indexListToBeDropped = new ArrayList<String>();
	private boolean isDifferentPK;
	private boolean isWithoutModule;
	private boolean isWithoutPK;
	private org.w3c.dom.Element tableElement;
	private Connection connection_;
	private String updateCounterID;
	private String databaseName;
	
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
		jScrollPaneMessage.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(new BorderLayout());

		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setTabSize(4);
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);

		jButtonClose.setText(res.getString("Close"));
		jButtonClose.setBounds(new Rectangle(20, 8, 100, 27));
		jButtonClose.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonClose.addActionListener(new DialogCheckTableModule_jButtonClose_actionAdapter(this));
		jButtonCreate.setText(res.getString("ModuleCreate"));
		jButtonCreate.setBounds(new Rectangle(140, 8, 150, 27));
		jButtonCreate.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCreate.addActionListener(new DialogCheckTableModule_jButtonCreate_actionAdapter(this));
		jButtonAlter.setText(res.getString("ModuleModify"));
		jButtonAlter.setBounds(new Rectangle(310, 8, 150, 27));
		jButtonAlter.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonAlter.addActionListener(new DialogCheckTableModule_jButtonAlter_actionAdapter(this));
		jButtonPut.setText(res.getString("ModulePut"));
		jButtonPut.setBounds(new Rectangle(480, 8, 150, 27));
		jButtonPut.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonPut.addActionListener(new DialogCheckTableModule_jButtonPut_actionAdapter(this));
		jButtonDelete.setText(res.getString("ModuleDelete"));
		jButtonDelete.setBounds(new Rectangle(650, 8, 150, 27));
		jButtonDelete.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonDelete.addActionListener(new DialogCheckTableModule_jButtonDelete_actionAdapter(this));
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(100, 43));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonClose, null);
		jPanelButtons.add(jButtonCreate, null);
		jPanelButtons.add(jButtonAlter, null);
		jPanelButtons.add(jButtonPut, null);
		jPanelButtons.add(jButtonDelete, null);

		this.setTitle(res.getString("ModuleCheck"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(830, 600));
		this.getContentPane().add(jScrollPaneMessage,  BorderLayout.CENTER);
	}

	public String request(MainTreeNode tableNode, boolean isShowDialog) {
		if (frame_.getSystemNode().getElement().getAttribute("AutoConnectToEdit").equals("T")) {
			errorStatus = tableNode.getErrorStatus();
			tableElement = tableNode.getElement();
			databaseName = frame_.getDatabaseName(tableElement.getAttribute("DB"));
			connection_ = frame_.getDatabaseConnection(tableElement.getAttribute("DB"));
			if (connection_ != null) {
				if (tableNode.getType().equals("Table")) {
					checkTableModule("");
					if (isShowDialog) {
						jPanelButtons.getRootPane().setDefaultButton(jButtonClose);
						Dimension dlgSize = this.getPreferredSize();
						Dimension frmSize = frame_.getSize();
						Point loc = frame_.getLocation();
						this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
						super.setVisible(true);
					}
				}
			}
		}
		return errorStatus;
	}

	void checkTableModule(String requestType) {
		org.w3c.dom.Element element, keyElement;
		String wrkStr, tableID, moduleID, fieldID;
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
		ArrayList<String> optionList = new ArrayList<String>();
		int workIndex, count1, count2, wrkInt;

		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));

			keyFieldList.clear();
			keyFieldTypeList.clear();
			fieldListToBeAdded.clear();
			fieldListToBeDropped.clear();
			fieldListToBeConverted.clear();
			fieldTypeListToBeConverted.clear();
			fieldSizeListToBeConvertedOld.clear();
			fieldSizeListToBeConvertedNew.clear();
			fieldListToBePut.clear();
			fieldTypeListToBePut.clear();
			fieldSizeListToBePut.clear();
			fieldDecimalListToBePut.clear();
			fieldNullableListToBePut.clear();
			fieldDecimalListToBeConvertedOld.clear();
			fieldDecimalListToBeConvertedNew.clear();
			fieldListToBeNullable.clear();
			fieldListToBeNullableDataType.clear();
			fieldListToBeNotNull.clear();
			fieldListToBeNotNullDataType.clear();
			addingSKList.clear();
			addingXKList.clear();
			indexListToBeDropped.clear();
			isDifferentPK = false;
			isWithoutModule = false;
			isWithoutPK = true;

			updateCounterID = tableElement.getAttribute("UpdateCounter");
			if (updateCounterID.equals("")) {
				updateCounterID = DEFAULT_UPDATE_COUNTER;
			}

			tableID = tableElement.getAttribute("ID");
			moduleID = tableElement.getAttribute("ModuleID");
			if (moduleID.equals("")) {
				moduleID = tableID;
			}
			if (databaseName.contains("jdbc:postgresql")) {
				tableID = frame_.getCaseShiftValue(tableID, "Lower");
				moduleID = frame_.getCaseShiftValue(moduleID, "Lower");
			}
			ResultSet rs1 = connection_.getMetaData().getColumns(null, null, moduleID, null);
			if (rs1.next()) {

				moduleBuf.append("Create table " + frame_.getCaseShiftValue(moduleID, "Upper") + " (\n");

				///////////////////////////////////////////
				// Field check from definition to module //
				///////////////////////////////////////////
				NodeList fieldList = tableElement.getElementsByTagName("Field");
				SortableDomElementListModel sortingList = frame_.getSortedListModel(fieldList, "Order");
			    for (int i = 0; i < sortingList.getSize(); i++) {
			        element = (org.w3c.dom.Element)sortingList.getElementAt(i);
			        optionList = frame_.getOptionList(element.getAttribute("TypeOptions"));
					if (!optionList.contains("VIRTUAL")) {

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

						// DDL FieldID and Data Type//
						moduleBuf.append("\t" + element.getAttribute("ID") + " ");
						if ((databaseName.contains("jdbc:sqlserver") || databaseName.contains("jdbc:oracle"))
								&& element.getAttribute("Type").equals("CHAR")
								&& optionList.contains("KANJI")) {
							moduleBuf.append("NCHAR");
							moduleBuf.append("(");
							moduleBuf.append(element.getAttribute("Size"));
							moduleBuf.append(")");
						} else {
							if (databaseName.contains("jdbc:sqlserver")
									&& element.getAttribute("Type").equals("DOUBLE")) {
								moduleBuf.append("FLOAT");
							} else {
								moduleBuf.append(element.getAttribute("Type"));

							}
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
									if (element.getAttribute("Type").equals("CHAR")) {
										moduleBuf.append("(");
										moduleBuf.append(element.getAttribute("Size"));
										moduleBuf.append(")");
									}
									if (element.getAttribute("Type").contains("VARCHAR")
											&& !element.getAttribute("Size").equals("0")) {
										moduleBuf.append("(");
										moduleBuf.append(element.getAttribute("Size"));
										moduleBuf.append(")");
									}
								}
							}
						}

						// DDL Not Null and Comment //
						if (!element.getAttribute("Nullable").equals("T")) {
							moduleBuf.append(" Not null");
						}
						moduleBuf.append(" Comment '" + element.getAttribute("Name") + "',\n");

						fieldID = element.getAttribute("ID");
						if (databaseName.contains("jdbc:postgresql")) {
							fieldID = frame_.getCaseShiftValue(fieldID, "Lower");
						}
						ResultSet rs2 = connection_.getMetaData().getColumns(null, null, moduleID, fieldID);
						if (rs2.next()) {

							if (rs2.getString("COLUMN_SIZE") == null || rs2.getString("COLUMN_SIZE").equals("")) {
								sizeOfModuleField = 1;
							} else {
								sizeOfModuleField = Integer.parseInt(rs2.getString("COLUMN_SIZE"));
							}
							if (rs2.getString("DECIMAL_DIGITS") == null) {
								decimalOfModuleField = 0;
							} else {
								decimalOfModuleField = Integer.parseInt(rs2.getString("DECIMAL_DIGITS"));
							}

							if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
								typeDescriptionsOfModuleField = rs2.getString("TYPE_NAME") + "(" + sizeOfModuleField + "," + decimalOfModuleField + ")";
							} else {
								if (element.getAttribute("Type").equals("CHAR") || element.getAttribute("Type").equals("VARCHAR")) {
									typeDescriptionsOfModuleField = rs2.getString("TYPE_NAME") + "(" + sizeOfModuleField + ")";
								} else {
									typeDescriptionsOfModuleField = rs2.getString("TYPE_NAME");
								}
							}

							if (rs2.getString("IS_NULLABLE").equals("YES")) {
								isNullableOfModuleField = true;

								////////////////////////////////////////////////////////////////////////////
								// AutoNumber key field of ACCESS is set to be Null-able. Note that value //
								// of rs2.getString("IS_INCREMENT") is always "NO" for unknown reason.    //
								////////////////////////////////////////////////////////////////////////////
								if (databaseName.contains("jdbc:ucanaccess") && element.getAttribute("Type").equals("INTEGER")) {
									NodeList keyList = tableElement.getElementsByTagName("Key");
									keyElement = (org.w3c.dom.Element)keyList.item(0);
									if (keyElement != null) {
										workTokenizer = new StringTokenizer(keyElement.getAttribute("Fields"), ";");
										if (workTokenizer.countTokens() == 1 && workTokenizer.nextToken().equals(element.getAttribute("ID"))) {
											isNullableOfModuleField = false;
										}
									}
								}
							} else {
								isNullableOfModuleField = false;
							}

							if (isEquivalentDataType(element.getAttribute("Type"), sizeOfDefinitionField, rs2.getString("TYPE_NAME"), databaseName, optionList)) {
								if (element.getAttribute("Type").equals("CHAR")
										|| element.getAttribute("Type").contains("VARCHAR")
										|| element.getAttribute("Type").equals("DECIMAL")
										|| (element.getAttribute("Type").equals("NUMERIC") && !rs2.getString("TYPE_NAME").equals("NUMBER"))) {
									if (element.getAttribute("Type").contains("VARCHAR")
											&& (sizeOfDefinitionField == 0 || sizeOfDefinitionField <= sizeOfModuleField)) {
									} else {
										if (!rs2.getString("TYPE_NAME").equals("money")
												&& !rs2.getString("TYPE_NAME").equals("YESNO")
												&& !rs2.getString("TYPE_NAME").equals("BYTE")
												&& !rs2.getString("TYPE_NAME").equals("BOOLEAN")) {
											if (sizeOfDefinitionField != sizeOfModuleField
													|| decimalOfDefinitionField != decimalOfModuleField) {
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
									}
								}
							} else {
								countOfErrors++;
								fieldListToBeDropped.add(element.getAttribute("ID"));
								fieldListToBeAdded.add(element.getAttribute("ID"));
								if (element.getAttribute("Type").equals("CHAR") && optionList.contains("KANJI")) {
									buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage1") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage48") + typeDescriptionsOfDefinitionField + res.getString("ModuleCheckMessage3") + typeDescriptionsOfModuleField + res.getString("ModuleCheckMessage4"));
								} else {
									buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage1") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage2") + typeDescriptionsOfDefinitionField + res.getString("ModuleCheckMessage3") + typeDescriptionsOfModuleField + res.getString("ModuleCheckMessage4"));
								}
							}
							if (!isNullableOfModuleField && isNullableOfDefinitionField) {
								countOfErrors++;
								fieldListToBeNullable.add(element.getAttribute("ID"));
								if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
									fieldListToBeNullableDataType.add(rs2.getString("TYPE_NAME") + "(" + sizeOfModuleField + "," + decimalOfModuleField + ")");
								} else {
									if (!element.getAttribute("Size").equals("0")
											&& (element.getAttribute("Type").equals("CHAR") || element.getAttribute("Type").equals("VARCHAR"))) {
										fieldListToBeNullableDataType.add(rs2.getString("TYPE_NAME") + "(" + sizeOfModuleField + ")");
									} else {
										fieldListToBeNullableDataType.add(rs2.getString("TYPE_NAME"));
									}
								}
								buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage5") + element.getAttribute("ID") + "(" + element.getAttribute("Name") +")" + res.getString("ModuleCheckMessage6"));
							}
							if (isNullableOfModuleField && !isNullableOfDefinitionField) {
								countOfErrors++;
								fieldListToBeNotNull.add(element.getAttribute("ID"));
								if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
									fieldListToBeNotNullDataType.add(rs2.getString("TYPE_NAME") + "(" + sizeOfModuleField + "," + decimalOfModuleField + ")");
								} else {
									if (!element.getAttribute("Size").equals("0")
											&& (element.getAttribute("Type").equals("CHAR") || element.getAttribute("Type").equals("VARCHAR"))) {
										fieldListToBeNotNullDataType.add(rs2.getString("TYPE_NAME") + "(" + sizeOfModuleField + ")");
									} else {
										fieldListToBeNotNullDataType.add(rs2.getString("TYPE_NAME"));
									}
								}
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

				//////////////////////////
			    // Check update counter //
				//////////////////////////
			    if (!updateCounterID.toUpperCase().equals("*NONE")) {
			    	fieldID = updateCounterID;
			    	if (databaseName.contains("jdbc:postgresql")) {
						fieldID = frame_.getCaseShiftValue(fieldID, "Lower");
			    	}
			    	ResultSet rs2 = connection_.getMetaData().getColumns(null, null, moduleID, fieldID);
			    	if (rs2.next()) {
			    		if (!isEquivalentDataType("INTEGER", 9, rs2.getString("TYPE_NAME"), databaseName, null)) {
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
			    }

				///////////////////////////////////////////
				// Field check from module to definition //
				///////////////////////////////////////////
				ResultSet rs3 = connection_.getMetaData().getColumns(null, null, moduleID, null);
				while (rs3.next()) {

					exist = false;
					if (updateCounterID.equals(frame_.getCaseShiftValue(rs3.getString("COLUMN_NAME"), "Upper"))) {
						exist = true;
					} else {
						for (int i = 0; i < fieldList.getLength(); i++) {
							element = (org.w3c.dom.Element)fieldList.item(i);
					        optionList = frame_.getOptionList(element.getAttribute("TypeOptions"));
							if (element.getAttribute("ID").equals(frame_.getCaseShiftValue(rs3.getString("COLUMN_NAME"), "Upper"))
									&& !optionList.contains("VIRTUAL")) {
								exist = true;
								break;
							}
						}
					}
					if (!exist) {
						if (rs3.getString("COLUMN_SIZE") == null || rs3.getString("COLUMN_SIZE").equals("")) {
							sizeOfModuleField = 1;
						} else {
							if (rs3.getString("TYPE_NAME").equals("BOOLEAN")) {
								sizeOfModuleField = 1;
							} else {
								sizeOfModuleField = Integer.parseInt(rs3.getString("COLUMN_SIZE"));
							}
						}
						if (rs3.getString("DECIMAL_DIGITS") == null) {
							decimalOfModuleField = 0;
						} else {
							decimalOfModuleField = Integer.parseInt(rs3.getString("DECIMAL_DIGITS"));
						}

						if (rs3.getString("TYPE_NAME").equals("DECIMAL") || rs3.getString("TYPE_NAME").equals("NUMERIC")) {
							typeDescriptionsOfModuleField = rs3.getString("TYPE_NAME") + "(" + sizeOfModuleField + "," + decimalOfModuleField + ")";
						} else {
							if (rs3.getString("TYPE_NAME").equals("CHAR")) {
								typeDescriptionsOfModuleField = rs3.getString("TYPE_NAME") + "(" + sizeOfModuleField + ")";
							} else {
								typeDescriptionsOfModuleField = rs3.getString("TYPE_NAME");
							}
						}

						countOfErrors++;
						fieldListToBeDropped.add(frame_.getCaseShiftValue(rs3.getString("COLUMN_NAME"), "Upper"));
						buf.append("(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage16") + frame_.getCaseShiftValue(rs3.getString("COLUMN_NAME"), "Upper") + " [" + typeDescriptionsOfModuleField + "]" + res.getString("ModuleCheckMessage17"));

						fieldListToBePut.add(frame_.getCaseShiftValue(rs3.getString("COLUMN_NAME"), "Upper"));
						fieldTypeListToBePut.add(getDataTypeForEditor(rs3.getString("TYPE_NAME"), sizeOfModuleField, databaseName));
						fieldSizeListToBePut.add(sizeOfModuleField);
						fieldDecimalListToBePut.add(decimalOfModuleField);
						fieldNullableListToBePut.add(rs3.getString("IS_NULLABLE"));
					}
				}
				rs3.close();

				////////////////////////////////////////////////////////
				// Collect unique key and index information of module //
				////////////////////////////////////////////////////////
				indexNameList.clear();
				indexFieldsList.clear();
				indexAscDescList.clear();
				indexNotUniqueList.clear();
				ResultSet rs4 = connection_.getMetaData().getIndexInfo(null, null, moduleID, false, true);
				while (rs4.next()) {

					workIndex = indexNameList.indexOf(rs4.getString("INDEX_NAME"));
					if (workIndex == -1) {
						indexNameList.add(rs4.getString("INDEX_NAME"));
						workIndex = indexNameList.size() - 1;
						indexFieldsList.add("");
						indexAscDescList.add("");
						if (rs4.getString("NON_UNIQUE") == null) {
							indexNotUniqueList.add("false");
						} else {
							if (rs4.getString("NON_UNIQUE").equals("0")
									|| rs4.getString("NON_UNIQUE").equals("f")
									|| rs4.getString("NON_UNIQUE").equals("FALSE")
									|| rs4.getString("NON_UNIQUE").equals("false")) {
								indexNotUniqueList.add("false");
							}
							if (rs4.getString("NON_UNIQUE").equals("1")
									|| rs4.getString("NON_UNIQUE").equals("t")
									|| rs4.getString("NON_UNIQUE").equals("TRUE")
									|| rs4.getString("NON_UNIQUE").equals("true")) {
								indexNotUniqueList.add("true");
							}
						}
					}
					if (indexFieldsList.get(workIndex).equals("")) {
						indexFieldsList.set(workIndex, frame_.getCaseShiftValue(rs4.getString("COLUMN_NAME"), "Upper"));
					} else {
						indexFieldsList.set(workIndex, indexFieldsList.get(workIndex) + ";" + frame_.getCaseShiftValue(rs4.getString("COLUMN_NAME"), "Upper"));
					}
					if (indexAscDescList.get(workIndex).equals("")) {
						if (rs4.getString("ASC_OR_DESC") != null && rs4.getString("ASC_OR_DESC").equals("D")) {
							indexAscDescList.set(workIndex, "D");
						} else {
							indexAscDescList.set(workIndex, "A");
						}
					} else {
						if (rs4.getString("ASC_OR_DESC") != null && rs4.getString("ASC_OR_DESC").equals("D")) {
							indexAscDescList.set(workIndex, indexAscDescList.get(workIndex) + ";D");
						} else {
							indexAscDescList.set(workIndex, indexAscDescList.get(workIndex) + ";A");
						}
					}
				}
				rs4.close();

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

							count1 = 0;
							count2 = 0;

							fieldList2.clear();
							workTokenizer = new StringTokenizer(indexFieldsList.get(i), ";");
							while (workTokenizer.hasMoreTokens()) {
								fieldList2.add(workTokenizer.nextToken());
							}

							ascDescList2.clear();
							workTokenizer = new StringTokenizer(indexAscDescList.get(i), ";");
							while (workTokenizer.hasMoreTokens()) {
								ascDescList2.add(workTokenizer.nextToken());
							}

							for (int k = 0; k < fieldList2.size(); k++) {
								count1++;
								wrkInt = fieldList1.indexOf(fieldList2.get(k).replaceAll("\"", ""));
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
					///////////////////////////////////////////////////////////////////////////////////////////////
					// Those "function indexes" which contains "(" in its expression should be skipped to check. //
					///////////////////////////////////////////////////////////////////////////////////////////////
					if (!exist && indexNotUniqueList.get(i).equals("true") && !indexFieldsList.get(i).contains("(")) {
						countOfErrors++;
						indexListToBeDropped.add(indexNameList.get(i));

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

				/////////////////////////////////////////
				// Key check from definition to module //
				/////////////////////////////////////////
				String moduleKeyFields;
				int countOfKey = 0;
				int countOfSK = 0;
				boolean isWithoutPKDefined = true;
				for (int i = 0; i < keyList.getLength(); i++) {
					element = (org.w3c.dom.Element)keyList.item(i);
					if (element.getAttribute("Type").equals("PK")) {

						countOfKey++;
						moduleBuf.append("Constraint " + tableElement.getAttribute("ID") + "_PK" + " Primary key (");
						moduleBuf.append(element.getAttribute("Fields").replace(";", ", ") + ")");

						isWithoutPKDefined = false;
						moduleKeyFields = element.getAttribute("Fields");
						workTokenizer = new StringTokenizer(moduleKeyFields, ";");
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

						count1 = 0;
						count2 = 0;
						wrkStr = "";

						ResultSet rs5 = connection_.getMetaData().getPrimaryKeys(null, null, moduleID);
						while (rs5.next()) {
							count1++;
							if (keyFieldList.contains(frame_.getCaseShiftValue(rs5.getString("COLUMN_NAME"), "Upper"))) {
								count2++;
							}
							if (wrkStr.equals("")) {
								wrkStr = wrkStr + frame_.getCaseShiftValue(rs5.getString("COLUMN_NAME"), "Upper");
							} else {
								wrkStr = wrkStr + ";" + frame_.getCaseShiftValue(rs5.getString("COLUMN_NAME"), "Upper");
							}
						}
						rs5.close();

						if (count1 != count2) {
							keyFieldList.clear();
							countOfErrors++;
							isDifferentPK = true;
							buf.append("(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage22") + moduleKeyFields + res.getString("ModuleCheckMessage23") + wrkStr + res.getString("ModuleCheckMessage24"));
						}
					}

					if (element.getAttribute("Type").equals("SK") || element.getAttribute("Type").equals("XK")) {

						if (element.getAttribute("Type").equals("SK")) {
							if (countOfKey > 0) {
								moduleBuf.append(",\n");
							}
							countOfKey++;
							countOfSK++;
							moduleBuf.append("Constraint " + tableElement.getAttribute("ID") + "_SK" + countOfSK + " Unique (");
							moduleBuf.append(element.getAttribute("Fields").replace(";", ", ") + ")");
						}

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

								count1 = 0;
								count2 = 0;

								fieldList2.clear();
								workTokenizer = new StringTokenizer(indexFieldsList.get(j), ";");
								while (workTokenizer.hasMoreTokens()) {
									fieldList2.add(workTokenizer.nextToken().replaceAll("\"", "")); // Literals are with field ID if ID is KANJI // 
								}

								ascDescList2.clear();
								workTokenizer = new StringTokenizer(indexAscDescList.get(j), ";");
								while (workTokenizer.hasMoreTokens()) {
									ascDescList2.add(workTokenizer.nextToken());
								}

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

				///////////////////////////////////////////////
				// Collect foreign key constraints of module //
				///////////////////////////////////////////////
				foreignKeyNameList.clear();
				foreignKeyTableList.clear();
				foreignKeyFieldList.clear();
				ResultSet rs7 = connection_.getMetaData().getImportedKeys(null, null, moduleID);
				while (rs7.next()) {
					workIndex = foreignKeyNameList.indexOf(rs7.getString("FK_NAME"));
					if (workIndex == -1) {
						workIndex = 0;
						foreignKeyNameList.add(rs7.getString("FK_NAME"));
						foreignKeyTableList.add(frame_.getCaseShiftValue(rs7.getString("FKTABLE_NAME"), "Upper"));
						foreignKeyFieldList.add(frame_.getCaseShiftValue(rs7.getString("FKCOLUMN_NAME"), "Upper"));
						nativeFieldList.add(frame_.getCaseShiftValue(rs7.getString("PKCOLUMN_NAME"), "Upper"));
					} else {
						foreignKeyFieldList.set(workIndex, foreignKeyFieldList.get(workIndex) + "," + frame_.getCaseShiftValue(rs7.getString("FKCOLUMN_NAME"), "Upper"));
						nativeFieldList.set(workIndex, nativeFieldList.get(workIndex) + "," + frame_.getCaseShiftValue(rs7.getString("PKCOLUMN_NAME"), "Upper"));
					}
				}
				rs7.close();
				for (int i = 0; i < foreignKeyNameList.size(); i++) {
					moduleBuf.append(",\nConstraint " + foreignKeyNameList.get(i) + " Foreign key (" + foreignKeyFieldList.get(i) + ") References " + foreignKeyTableList.get(i) + " (" + nativeFieldList.get(i) + ")");
				}
				moduleBuf.append("\n)");

				////////////////////////////////////////
				// Check PK from module to definition //
				////////////////////////////////////////
				if (isWithoutPKDefined) {
					wrkStr = "";
					ResultSet rs6 = connection_.getMetaData().getPrimaryKeys(null, null, moduleID);
					while (rs6.next()) {
						if (wrkStr.equals("")) {
							wrkStr = wrkStr + frame_.getCaseShiftValue(rs6.getString("COLUMN_NAME"), "Upper");
						} else {
							wrkStr = wrkStr + ";" + frame_.getCaseShiftValue(rs6.getString("COLUMN_NAME"), "Upper");
						}
					}
					rs6.close();
					if (!wrkStr.equals("")) {
						countOfErrors++;
						//isDifferentPK = true;
						buf.append("(" + countOfErrors + ") " + res.getString("ModuleCheckMessage29") + wrkStr + res.getString("ModuleCheckMessage30"));
					}
				}

				if (countOfErrors > 0) {
					errorStatus = "ER2";
				} else {
					errorStatus = "";
				}

			} else {

				isWithoutModule = true;
				countOfErrors++;
				buf.append("(" + countOfErrors + ") "+ res.getString("ModuleCheckMessage31"));

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

				errorStatus = "ER1";
			}
			rs1.close();

			jButtonAlter.setEnabled(false);
			jButtonCreate.setEnabled(false);
			jButtonDelete.setEnabled(false);
			jButtonPut.setEnabled(false);

			if (countOfErrors > 0) {
				buf.append("\n");
				if (isWithoutModule) {
					if (!isWithoutPK) {
						jButtonCreate.setEnabled(true);
						buf.append(res.getString("ModuleCheckMessage32"));
					}
				} else {
					jButtonDelete.setEnabled(true);
					if (isDifferentPK) {
						buf.append(res.getString("ModuleCheckMessage33"));
					} else {
						if (fieldListToBeDropped.size() > 0 || fieldListToBeNullable.size() > 0) {
							buf.append(res.getString("ModuleCheckMessage34")); 
						}
						jButtonAlter.setEnabled(true);
					}
				}
				if (fieldListToBePut.size() > 0) {
					jButtonPut.setEnabled(true);
				}
				jTextAreaMessage.setText(buf.toString());
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

		} catch (SQLException e) {
			jTextAreaMessage.setText(e.getMessage());
			jButtonAlter.setEnabled(false);
			jButtonCreate.setEnabled(false);
			jButtonDelete.setEnabled(false);
			jButtonPut.setEnabled(false);
			try {
				connection_.close();
			} catch (SQLException e1) {
			} finally {
				JOptionPane.showMessageDialog(null, res.getString("DBConnectMessage9"));
			}
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	boolean isEquivalentDataType(String dataTypeDefiition, int size, String dataTypeModule, String dbDriverName, ArrayList<String> optionList) {
		boolean isEquivalent = false;

		if (dataTypeDefiition.equals(dataTypeModule.toUpperCase())) {
			if ((dbDriverName.contains("jdbc:oracle") || dbDriverName.contains("jdbc:sqlserver"))
					&& dataTypeDefiition.equals("CHAR") && optionList.contains("KANJI")) {
			} else {
				isEquivalent = true;
			}
		} else {

			if (dataTypeDefiition.equals("SMALLINT")) {
				if (dataTypeModule.equals("int2")
						|| dataTypeModule.equals("tinyint")
						|| dataTypeModule.equals("NUMBER")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("INTEGER")) {
				if (dataTypeModule.equals("INT")
						|| dataTypeModule.equals("INT SIGNED")
						|| dataTypeModule.equals("INT UNSIGNED")
						|| dataTypeModule.equals("int identity")
						|| dataTypeModule.equals("money")
						|| dataTypeModule.equals("NUMBER")
						|| dataTypeModule.equals("SERIAL")) {
					isEquivalent = true;
				}
				if (dataTypeModule.equals("int")
						|| dataTypeModule.equals("int4")
						|| dataTypeModule.equals("serial")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("BIGINT")) {
				if (dbDriverName.contains("jdbc:ucanaccess")) {
					if (dataTypeModule.equals("LONG")
							|| dataTypeModule.equals("COUNTER")) {
						isEquivalent = true;
					}
				} else {
					if (dataTypeModule.equals("int8")
							|| dataTypeModule.equals("bigserial")
							|| dataTypeModule.equals("BIGSERIAL")
							|| dataTypeModule.equals("money")
							|| dataTypeModule.equals("NUMBER")) {
						isEquivalent = true;
					}
				}
			}

			if (dataTypeDefiition.equals("REAL")) {
				if (dataTypeModule.equals("float4")
						|| dataTypeModule.equals("float")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("DOUBLE")) {
				if (dataTypeModule.equals("float")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("DOUBLE PRECISION")) {
				if (dataTypeModule.equals("float8")
						|| dataTypeModule.equals("FLOAT")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("NUMERIC")) {
				if (dataTypeModule.equals("DECIMAL")
						|| dataTypeModule.equals("CURRENCY")
						|| dataTypeModule.equals("money")
						|| dataTypeModule.equals("NUMBER")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("DECIMAL")) {
				if (dataTypeModule.equals("numeric")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("CHAR")) {
				if (dbDriverName.contains("jdbc:ucanaccess")) {
					if (size <= 100 && dataTypeModule.equals("VARCHAR")) {
						isEquivalent = true;
					}
					if (size == 1 && dataTypeModule.equals("YESNO")) {
						isEquivalent = true;
					}
					if (size == 1 && dataTypeModule.equals("BYTE")) {
						isEquivalent = true;
					}
					if (size == 1 && dataTypeModule.equals("BOOLEAN")) {
						isEquivalent = true;
					}
				} else {
					if (optionList.contains("KANJI")) {
						if (dataTypeModule.equals("NCHAR")
								|| dataTypeModule.equals("nchar")) {
							isEquivalent = true;
						}
					}
					if (dataTypeModule.equals("bpchar")
							|| dataTypeModule.equals("CHARACTER")
							|| dataTypeModule.equals("uniqueidentifier")
							|| dataTypeModule.equals("uuid")
							|| dataTypeModule.equals("BIT")
							|| dataTypeModule.equals("bool")) {
						isEquivalent = true;
					}
					if (size == 19 && dataTypeModule.equals("DATETIME")) {
						isEquivalent = true;
					}
				}
			}

			if (dataTypeDefiition.equals("LONG VARCHAR")) {
				if (dbDriverName.contains("jdbc:oracle")) {
					if (dataTypeModule.equals("LONG")
							|| dataTypeModule.equals("NVARCHAR2")) {
						isEquivalent = true;
					}
				} else {
					if (dataTypeModule.equals("MEDIUMTEXT")
							|| dataTypeModule.equals("LONGTEXT")
							|| dataTypeModule.equals("image")
							|| dataTypeModule.equals("CLOB")
							|| dataTypeModule.equals("json")
							|| dataTypeModule.equals("jsonb")
							|| dataTypeModule.equals("hstore")
							|| dataTypeModule.equals("xml")) {
						isEquivalent = true;
					}
				}
				if (dataTypeModule.equals("text") || dataTypeModule.equals("TEXT")) {
					isEquivalent = true;
				}
				if (dbDriverName.contains("jdbc:h2") && dataTypeModule.equals("VARCHAR")) {
					isEquivalent = true;
				}
				if (dbDriverName.contains("jdbc:ucanaccess") && dataTypeModule.equals("VARCHAR")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("VARCHAR")) {
				if (dataTypeModule.equals("text")
						|| dataTypeModule.equals("nvarchar")
						|| dataTypeModule.equals("json")
						|| dataTypeModule.equals("jsonb")
						|| dataTypeModule.equals("hstore")
						|| dataTypeModule.equals("xml")
						|| dataTypeModule.equals("VARCHAR2")
						|| dataTypeModule.equals("NVARCHAR2")
						|| dataTypeModule.equals("character varying")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("BYTEA")) {
				if (dataTypeModule.equals("bytea")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("CLOB")) {
				if (dataTypeModule.endsWith("TEXT")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("BLOB")) {
				if (dataTypeModule.endsWith("OLE")
						|| dataTypeModule.endsWith("LONGBLOB")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.startsWith("TIMESTAMP")) {
				if (dataTypeModule.startsWith("TIMESTAMP")
						|| dataTypeModule.equals("DATETIME")
						|| dataTypeModule.equals("datetime")) {
					isEquivalent = true;
				}
			}

			if (dataTypeDefiition.equals("DATETIME")) {
				if (dataTypeModule.startsWith("TIMESTAMP")
						|| dataTypeModule.equals("timestamp")) {
					isEquivalent = true;
				}
			}

			if (dbDriverName.contains("jdbc:ucanaccess") && dataTypeModule.equals("TIMESTAMP")) {
				if (dataTypeDefiition.equals("DATE")
						|| dataTypeDefiition.equals("DATETIME")) {
					isEquivalent = true;
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
		ArrayList<String> optionList = new ArrayList<String>();
		boolean firstField = true;

		Object[] bts = {res.getString("Cancel"), res.getString("Execute")};
		int rtn = JOptionPane.showOptionDialog(this, res.getString("ModuleCheckMessage36"), res.getString("ModuleModify") + " " + tableElement.getAttribute("ID") + " " + tableElement.getAttribute("Name"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[1]);
		if (rtn == 1) {
			try {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));

				Statement statement = connection_.createStatement();

				//////////////////////////////////
				// Alter column as NULL-allowed //
				//////////////////////////////////
				for (int i = 0; i < fieldListToBeNullable.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
					if (databaseName.contains("jdbc:mysql")) {
						buf.append(" MODIFY COLUMN ");
					} else {
						buf.append(" ALTER COLUMN ");
					}
					buf.append(fieldListToBeNullable.get(i));
					if (databaseName.contains("jdbc:mysql") || databaseName.contains("jdbc:sqlserver")) {
						buf.append(" ");
						buf.append(fieldListToBeNullableDataType.get(i));
					}
					if (databaseName.contains("jdbc:postgresql")) {
						buf.append(" DROP NOT NULL");
					} else {
						if (databaseName.contains("jdbc:h2")) {
							buf.append(" SET NULL");
						} else {
							buf.append(" NULL");
						}
					}
					statement.executeUpdate(buf.toString());
				}

				//////////////////////////////
				// Alter column as NOT NULL //
				//////////////////////////////
				for (int i = 0; i < fieldListToBeNotNull.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
					if (databaseName.contains("jdbc:mysql")) {
						buf.append(" MODIFY COLUMN ");
					} else {
						buf.append(" ALTER COLUMN ");
					}
					buf.append(fieldListToBeNotNull.get(i));
					if (databaseName.contains("jdbc:mysql") || databaseName.contains("jdbc:sqlserver")) {
						buf.append(" ");
						buf.append(fieldListToBeNotNullDataType.get(i));
					}
					if (databaseName.contains("jdbc:postgresql") || databaseName.contains("jdbc:h2")) {
						buf.append(" SET NOT NULL");
					} else {
						buf.append(" NOT NULL");
					}
					statement.executeUpdate(buf.toString());
				}

				////////////////
				// Drop index //
				////////////////
				for (int i = 0; i < indexListToBeDropped.size(); i++) {
					buf = new StringBuffer();
					buf.append("DROP INDEX ");
					buf.append(indexListToBeDropped.get(i));
					statement.executeUpdate(buf.toString());
				}

				///////////////////////////////////////////////////////
				// Save values of fields which size is being changed //
				///////////////////////////////////////////////////////
				if (fieldListToBeConverted.size() > 0 && keyFieldList.size() > 0) {
					Object value;
					String query;
					if (tableElement.getAttribute("ModuleID").equals("")) {
						query = "SELECT * FROM " + tableElement.getAttribute("ID");
					} else {
						query = "SELECT * FROM " + tableElement.getAttribute("ModuleID");
					}
					ResultSet rs1 = statement.executeQuery(query);
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

				/////////////////
				// Drop column //
				/////////////////
				for (int i = 0; i < fieldListToBeDropped.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
					buf.append(" DROP COLUMN ");
					buf.append(fieldListToBeDropped.get(i));
					statement.executeUpdate(buf.toString());
				}

				////////////////
				// Add column //
				////////////////
				NodeList fieldList = tableElement.getElementsByTagName("Field");
				for (int i = 0; i < fieldListToBeAdded.size(); i++) {
					buf = new StringBuffer();
					buf.append("ALTER TABLE ");
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
					if (databaseName.contains("jdbc:oracle")) {
						buf.append(" ADD (");
					}
					if (databaseName.contains("jdbc:sqlserver")) {
						buf.append(" ADD ");
					}
					if (updateCounterID.equals(fieldListToBeAdded.get(i))) {
						if (!databaseName.contains("jdbc:oracle") && !databaseName.contains("jdbc:sqlserver")) {
							buf.append(" ADD COLUMN ");
						}
						buf.append(updateCounterID);
						buf.append(" INTEGER DEFAULT 0");
						if (databaseName.contains("jdbc:oracle")) {
							buf.append(")");
						}
						statement.executeUpdate(buf.toString());
					} else {
						for (int j = 0; j < fieldList.getLength(); j++) {
							element = (org.w3c.dom.Element)fieldList.item(j);
					        optionList = frame_.getOptionList(element.getAttribute("TypeOptions"));
							if (element.getAttribute("ID").equals(fieldListToBeAdded.get(i))) {
								if (!databaseName.contains("jdbc:oracle") && !databaseName.contains("jdbc:sqlserver")) {
									buf.append(" ADD COLUMN ");
								}
								buf.append(element.getAttribute("ID"));
								buf.append(" ");
								buf.append(getDataTypeForDBMS(element.getAttribute("Type"), databaseName, optionList));
								if (getBasicTypeOf(element.getAttribute("Type")).equals("STRING")) {
									if (element.getAttribute("Type").equals("CHAR") || element.getAttribute("Type").equals("VARCHAR")) {
										if (!element.getAttribute("Size").equals("0")) {
											buf.append("(");
											buf.append(element.getAttribute("Size"));
											buf.append(")");
										}
									}
								} else {
									if (getBasicTypeOf(element.getAttribute("Type")).equals("INTEGER")) {
										buf.append(" Default 0");
									} else {
										if (getBasicTypeOf(element.getAttribute("Type")).equals("FLOAT")) {
											if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
												buf.append("(");
												buf.append(element.getAttribute("Size"));
												buf.append(",");
												buf.append(element.getAttribute("Decimal"));
												buf.append(")");
											}
											buf.append(" Default 0.0");
										}
									}
								}
								if (element.getAttribute("Nullable").contains("F")
										&& !element.getAttribute("Type").equals("DATE")
										&& !fieldListToBeConverted.contains(element.getAttribute("ID"))) {
									buf.append(" NOT NULL");
								}
								if (databaseName.contains("jdbc:oracle")) {
									buf.append(")");
								}
								statement.executeUpdate(buf.toString());
								break;
							}
						}
					}
				}

				////////////////////////////////////////////////////
				// Restore values of fields which size is changed //
				////////////////////////////////////////////////////
				if (fieldListToBeConverted.size() > 0 && keyFieldList.size() > 0) {
					for (int i = 0; i < keyValueList.size(); i++) {
						buf = new StringBuffer();
						buf.append("update ");
						if (tableElement.getAttribute("ModuleID").equals("")) {
							buf.append(tableElement.getAttribute("ID"));
						} else {
							buf.append(tableElement.getAttribute("ModuleID"));
						}
						buf.append(" set ");

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

						buf.append(" where ") ;

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

						statement.executeUpdate(buf.toString());
					}
					keyValueList.clear();
					fieldValueList.clear();
				}

				///////////////////////
				// Add Secondary Key //
				///////////////////////
				for (int i = 0; i < addingSKList.size(); i++) {
					buf = new StringBuffer();
					buf.append("CREATE UNIQUE INDEX UniqueIndexOf");
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
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
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
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

				///////////////
				// Add Index //
				///////////////
				for (int i = 0; i < addingXKList.size(); i++) {
					buf = new StringBuffer();
					buf.append("CREATE INDEX Index");
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
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
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append(tableElement.getAttribute("ID"));
					} else {
						buf.append(tableElement.getAttribute("ModuleID"));
					}
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

				///////////////////////////////////////////
				// Check module and refresh error status //
				///////////////////////////////////////////
				checkTableModule("ALTER");

			} catch (SQLException ex1) {
				JOptionPane.showMessageDialog(this, res.getString("ModuleCheckMessage37") + buf .toString() + "\n" + ex1.getMessage());
			} catch (Exception ex1) {
				JOptionPane.showMessageDialog(this, ex1.getMessage());
			} finally {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	public Object getTableOperationValue(Object value, String type, int sizeOld, int sizeNew, int decimalOld, int decimalNew){
		Object returnValue = "";
		String wrkStr1, wrkStr2;
		String basicType = frame_.getBasicTypeOf(type);

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
			if (value == null) {
				returnValue = "''";
			} else {
				wrkStr1 = value.toString().trim();
				returnValue = "'" + wrkStr1 + "'";
				if (sizeNew < sizeOld) {
					if (wrkStr1.length() > sizeNew) {
						returnValue = "'" + wrkStr1.substring(0, sizeNew) + "'";
					}
				}
			}
		}
		if (basicType.equals("DATE")) {
			if (value == null) {
				returnValue = "NULL";
			} else {
				String strDate = (String)value;
				if (strDate == null || strDate.equals("")) {
					returnValue = "NULL";
				} else {
					returnValue = "'" + strDate + "'";
				}
			}
		}
		if (basicType.equals("DATETIME")) {
			if (value == null) {
				returnValue = "NULL";
			} else {
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
		}

		return returnValue;
	}
	
	void jButtonCreate_actionPerformed(ActionEvent e) {
		int countOfPhysicalFields = 0;
		org.w3c.dom.Element element;
		String sqlText = "";

		NodeList fieldList = tableElement.getElementsByTagName("Field");
		for (int i = 0; i < fieldList.getLength(); i++) {
			element = (org.w3c.dom.Element)fieldList.item(i);
			if (!frame_.getOptionList(element.getAttribute("TypeOptions")).contains("VIRTUAL")) {
				countOfPhysicalFields++;
			}
		}

		if (countOfPhysicalFields == 0) {
			JOptionPane.showMessageDialog(this, res.getString("ModuleCheckMessage38"));
		} else {
			Object[] bts = {res.getString("Cancel"), res.getString("Execute")};
			int rtn = JOptionPane.showOptionDialog(this, res.getString("ModuleCheckMessage39"), res.getString("ModuleCreate") + " " + tableElement.getAttribute("ID") + " " + tableElement.getAttribute("Name"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[1]);
			if (rtn == 1) {
				try {
					setCursor(new Cursor(Cursor.WAIT_CURSOR));

					Statement statement = connection_.createStatement();
					sqlText = getSqlToCreateTable();
					statement.executeUpdate(sqlText);
					
					String dateValueSeparator = "'";
					if (databaseName.contains("jdbc:ucanaccess")) {
						dateValueSeparator = "#";
					}
					ArrayList<String> sqlList = frame_.getSqlToInsertInitialRecord(tableElement.getAttribute("ID"), dateValueSeparator);
					if (sqlList.size() > 0) {
						for (int i = 0; i < sqlList.size(); i++) {
							statement.executeUpdate(sqlList.get(i));
						}
					}

					checkTableModule("CREATE");

				} catch (SQLException ex1) {
					jTextAreaMessage.setText(res.getString("ModuleCheckMessage40") + sqlText + "\n" + ex1.getMessage());
				} catch (Exception ex1) {
					jTextAreaMessage.setText(ex1.getMessage());
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
			if (databaseName.contains("jdbc:postgresql")) {
				tableID = frame_.getCaseShiftValue(tableID, "Lower");
			}
			deleteTable(connection_, tableID);
		}
	}

	public void deleteTable(Connection connection, String tableID) {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Statement statement = connection.createStatement();
			statement.executeUpdate("DROP TABLE " + tableID);
			checkTableModule("DROP");
		} catch (SQLException ex1) {
			JOptionPane.showMessageDialog(this, res.getString("ModuleCheckMessage42"));
		} catch (Exception ex1) {
			JOptionPane.showMessageDialog(this, ex1.getMessage());
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	void jButtonPut_actionPerformed(ActionEvent e) {
		Object[] bts = {res.getString("Cancel"), res.getString("Execute")};
		int rtn = JOptionPane.showOptionDialog(this, res.getString("ModuleCheckMessage47"), res.getString("ModuleDelete") + " " + tableElement.getAttribute("ID") + " " + tableElement.getAttribute("Name"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[0]);
		if (rtn == 1) {

			/////////////////////////////////////////////////////
			// Put field definitions into the table definition //
			/////////////////////////////////////////////////////
			for (int i = 0; i < fieldListToBePut.size(); i++) {
				org.w3c.dom.Element newElement = frame_.createNewElementAccordingToType("TableFieldList");
				if (newElement != null) {
					newElement.setAttribute("ID", fieldListToBePut.get(i));
					newElement.setAttribute("Name", fieldListToBePut.get(i));
					newElement.setAttribute("Order", Editor.getFormatted4ByteString(i * 10));
					if (!fieldTypeListToBePut.get(i).equals("")) {
						newElement.setAttribute("Type", fieldTypeListToBePut.get(i));
					}
					newElement.setAttribute("Size", fieldSizeListToBePut.get(i).toString());
					newElement.setAttribute("Decimal", fieldDecimalListToBePut.get(i).toString());
					if (fieldNullableListToBePut.get(i).toUpperCase().equals("YES")) {
						newElement.setAttribute("Nullable", "T");
					} else {
						newElement.setAttribute("Nullable", "F");
					}
					frame_.currentMainTreeNode.getElement().appendChild(newElement);
				}
			}
			frame_.currentMainTreeNode.updateFields();
		}
	}

	void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	
	String getDataTypeForEditor(String dataTypeOfModule, int lengthModule, String dbDriverName) {
		String dataTypeDefinition = "";
		String dataTypeModule = dataTypeOfModule.toUpperCase();

		if (dataTypeModule.equals("DOUBLE")) {
			dataTypeDefinition = "DOUBLE";
		}
		if (dataTypeModule.equals("DATE")) {
			dataTypeDefinition = "DATE";
		}
		if (dataTypeModule.equals("TIME")) {
			dataTypeDefinition = "TIME";
		}
		if (dataTypeModule.equals("DATETIME")) {
			dataTypeDefinition = "DATETIME";
		}
		if (dataTypeModule.equals("TIMETZ")) {
			dataTypeDefinition = "TIMETZ";
		}
		if (dataTypeModule.equals("TIMESTAMP")) {
			dataTypeDefinition = "TIMESTAMP";
		}
		if (dataTypeModule.equals("TIMESTAMPTZ")) {
			dataTypeDefinition = "TIMESTAMPTZ";
		}
		if (dataTypeModule.equals("BINARY")
				|| dataTypeModule.equals("RAW")) {
			dataTypeDefinition = "BINARY";
		}
		if (dataTypeModule.equals("VARBINARY")) {
			dataTypeDefinition = "VARBINARY";
		}
		if (dataTypeModule.equals("CLOB")) {
			dataTypeDefinition = "CLOB";
		}
		if (dataTypeModule.equals("BLOB")
			|| dataTypeModule.equals("LONGBLOB")
			|| dataTypeModule.equals("OLE")) {
			dataTypeDefinition = "BLOB";
		}
		if (dataTypeModule.equals("BYTEA")) {
			dataTypeDefinition = "BYTEA";
		}
		if (dataTypeModule.equals("SMALLINT")
				|| dataTypeModule.equals("TINY INT")
				|| dataTypeModule.equals("INT2")) {
			dataTypeDefinition = "SMALLINT";
		}
		if (dataTypeModule.equals("INTEGER")
				|| dataTypeModule.equals("INT")
				|| dataTypeModule.equals("INT SIGNED")
				|| dataTypeModule.equals("INT UNSIGNED")
				|| dataTypeModule.equals("SERIAL")
				|| dataTypeModule.equals("INT4")) {
			dataTypeDefinition = "INTEGER";
		}
		if (dataTypeModule.equals("BIGINT")
				|| dataTypeModule.equals("LONG")
				|| dataTypeModule.equals("COUNTER")
				|| dataTypeModule.equals("INT8")
				|| dataTypeModule.equals("BIGSERIAL")
				|| dataTypeModule.equals("NUMBER")) {
			dataTypeDefinition = "BIGINT";
		}
		if (dataTypeModule.equals("REAL")
				|| dataTypeModule.equals("CURRENCY")
				|| dataTypeModule.equals("FLOAT4")) {
			dataTypeDefinition = "REAL";
		}
		if (dataTypeModule.equals("DOUBLE PRECISION")
				|| dataTypeModule.equals("FLOAT")
				|| dataTypeModule.equals("FLOAT8")) {
			dataTypeDefinition = "DOUBLE PRECISION";
		}
		if (dataTypeModule.equals("NUMERIC")
				|| dataTypeModule.equals("MONEY")
				|| dataTypeModule.equals("CURRENCY")
				|| dataTypeModule.equals("NUMBER")) {
			dataTypeDefinition = "NUMERIC";
		}
		if (dataTypeModule.equals("DECIMAL")
				|| dataTypeModule.equals("NUMERIC")) {
			dataTypeDefinition = "DECIMAL";
		}
		if (dataTypeModule.equals("CHAR")
				|| dataTypeModule.equals("CHARACTER")
				|| dataTypeModule.equals("NCHAR")
				|| dataTypeModule.equals("BPCHAR")
				|| dataTypeModule.equals("BOOL")
				|| dataTypeModule.equals("BIT")
				|| dataTypeModule.equals("UNIQUEIDENTIFIER")
				|| dataTypeModule.equals("UUID")) {
			dataTypeDefinition = "CHAR";
		}
		if (dataTypeModule.equals("LONG VARCHAR")
				|| dataTypeModule.equals("MEDIUMTEXT")
				|| dataTypeModule.equals("LONGTEXT")
				|| dataTypeModule.equals("CLOB")
				|| dataTypeModule.equals("LONG")
				|| dataTypeModule.equals("NVARCHAR2")
				|| dataTypeModule.equals("IMAGE")
				|| dataTypeModule.equals("TEXT")) {
			dataTypeDefinition = "LONG VARCHAR";
		}
		if (dbDriverName.contains("jdbc:ucanaccess") && dataTypeModule.equals("VARCHAR")) {
			if (lengthModule <= 100) {
				dataTypeDefinition = "CHAR";
			} else {
				dataTypeDefinition = "VARCHAR";
			}
		} else {
			if (dataTypeModule.equals("VARCHAR")
					|| dataTypeModule.equals("VARCHAR2")
					|| dataTypeModule.equals("NVARCHAR2")
					|| dataTypeModule.equals("CHARACTER VARYING")) {
				dataTypeDefinition = "VARCHAR";
			}
		}
		return dataTypeDefinition;
	}

	public String checkTableModule(MainTreeNode tableNode) {
		try {
			tableElement = tableNode.getElement();
			connection_ = frame_.getDatabaseConnection(tableElement.getAttribute("DB"));
			databaseName = frame_.getDatabaseName(tableElement.getAttribute("DB"));
			updateCounterID = tableElement.getAttribute("UpdateCounter");
			if (updateCounterID.equals("")) {
				updateCounterID = DEFAULT_UPDATE_COUNTER;
			}
			Statement statement = connection_.createStatement();
			String sqlText = getSqlToCreateTable();
			statement.executeUpdate(sqlText);
			
			String dateValueSeparator = "'";
			if (databaseName.contains("jdbc:ucanaccess")) {
				dateValueSeparator = "#";
			}
			ArrayList<String> sqlList = frame_.getSqlToInsertInitialRecord(tableElement.getAttribute("ID"), dateValueSeparator);
			if (sqlList.size() > 0) {
				for (int i = 0; i < sqlList.size(); i++) {
					statement.executeUpdate(sqlList.get(i));
				}
			}
		} catch (SQLException e) {
		}
		return this.request(tableNode, false);
	}
	
	String getSqlToCreateTable() {
		StringBuffer buf = new StringBuffer();
		org.w3c.dom.Element element;
		StringTokenizer workTokenizer;
		String wrkStr;
		ArrayList<String> optionList = new ArrayList<String>();
		boolean firstField = true;

		buf.append("Create table ");
		if (tableElement.getAttribute("ModuleID").equals("")) {
			buf.append(tableElement.getAttribute("ID"));
		} else {
			buf.append(tableElement.getAttribute("ModuleID"));
		}
		buf.append(" (\n");

		NodeList fieldList = tableElement.getElementsByTagName("Field");
		SortableDomElementListModel sortingList = frame_.getSortedListModel(fieldList, "Order");
	    for (int i = 0; i < sortingList.getSize(); i++) {
	        element = (org.w3c.dom.Element)sortingList.getElementAt(i);
	        optionList = frame_.getOptionList(element.getAttribute("TypeOptions"));
			if (!optionList.contains("VIRTUAL")) {
				if (firstField) {
					firstField = false;
				} else {
					buf.append(",\n");
				}
				buf.append("     ");
				buf.append(element.getAttribute("ID"));
				buf.append(" ");
				buf.append(getDataTypeForDBMS(element.getAttribute("Type"), databaseName, optionList));
				if (getBasicTypeOf(element.getAttribute("Type")).equals("STRING")) {
					if (element.getAttribute("Type").equals("CHAR") || element.getAttribute("Type").equals("VARCHAR")) {
						if (!element.getAttribute("Size").equals("0")) {
							buf.append("(");
							buf.append(element.getAttribute("Size"));
							buf.append(")");
						}
					}
				} else {
					if (getBasicTypeOf(element.getAttribute("Type")).equals("INTEGER")) {
						buf.append(" Default 0");
					} else {
						if (getBasicTypeOf(element.getAttribute("Type")).equals("FLOAT")) {
							if (element.getAttribute("Type").equals("DECIMAL") || element.getAttribute("Type").equals("NUMERIC")) {
								buf.append("(");
								buf.append(element.getAttribute("Size"));
								buf.append(",");
								buf.append(element.getAttribute("Decimal"));
								buf.append(")");
							}
							buf.append(" Default 0.0");
						}
					}
				}
				if (!element.getAttribute("Nullable").equals("T")) {
					buf.append(" Not null");
				}
			}
		}
    	buf.append(",\n");
	    if (!updateCounterID.toUpperCase().equals("*NONE")) {
	    	buf.append(updateCounterID);
			if (databaseName.contains("jdbc:ucanaccess")) {
		    	buf.append(" LONG Default 0,\n");
			} else {
				buf.append(" INTEGER Default 0,\n");
			}
	    }

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
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append("Constraint " + tableElement.getAttribute("ID") + "_PK Primary key (");
					} else {
						buf.append("Constraint " + tableElement.getAttribute("ModuleID") + "_PK Primary key (");
					}
				}
				if (element.getAttribute("Type").equals("SK")) {
					countOfSK++;
					if (tableElement.getAttribute("ModuleID").equals("")) {
						buf.append("Constraint " + tableElement.getAttribute("ID") + "_SK" + countOfSK + " Unique (");
					} else {
						buf.append("Constraint " + tableElement.getAttribute("ModuleID") + "_SK" + countOfSK + " Unique (");
					}
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

		if (databaseName.contains("jdbc:mysql")) {
			String engines[] = {"MyISAM", "InnoDB"};
		    int index = JOptionPane.showOptionDialog(this,
		    	"Choose table engine type.", "MySQL ENGINE Option", 
		    	JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
		    	null, engines, engines[1]);
		    if (index != JOptionPane.CLOSED_OPTION){
			    buf.append(" ENGINE=");
			    buf.append(engines[index]);
		    }
		}

		return buf.toString();
	}
	
	String getDataTypeForDBMS(String dataType, String dbDriverName, ArrayList<String> optionList) {
		String alternative = dataType;

		if (dbDriverName.contains("jdbc:postgresql")) {
			if (dataType.equals("TIMETZ")) {
				alternative = "time with time zone";
			}
			if (dataType.equals("TIMESTAMPTZ")) {
				alternative = "timestamp with time zone";
			}
			if (dataType.equals("LONG VARCHAR")) {
				alternative = "text";
			}
			if (dataType.equals("BYTEA")) {
				alternative = "bytea";
			}
			if (dataType.equals("DATETIME")) {
				alternative = "timestamp";
			}
			alternative = alternative.toLowerCase();
		}

		if (dbDriverName.contains("jdbc:oracle")) {
			if (dataType.equals("CHAR") && optionList.contains("KANJI")) {
				alternative = "NCHAR";
			}
			if (dataType.equals("VARCHAR") && optionList.contains("KANJI")) {
				alternative = "NVARCHAR2";
			}
			if (dataType.equals("LONG VARCHAR")) {
				alternative = "LONG";
			}
			if (dataType.equals("DATETIME")) {
				alternative = "TIMESTAMP";
			}
			if (dataType.equals("BINARY")) {
				alternative = "RAW";
			}
		}

		if (dbDriverName.contains("jdbc:mysql")) {
			if (dataType.equals("INTEGER")) {
				alternative = "INT";
			}
			if (dataType.equals("DOUBLE PRECISION")) {
				alternative = "FLOAT";
			}
			if (dataType.equals("LONG VARCHAR")) {
				alternative = "LONGTEXT";
			}
		}

		if (dbDriverName.contains("jdbc:derby")) {
			if (dataType.equals("DATETIME")) {
				alternative = "TIMESTAMP";
			}
		}

		if (dbDriverName.contains("jdbc:h2")) {
			if (dataType.equals("LONG VARCHAR")) {
				alternative = "VARCHAR";
			}
		}

		if (dbDriverName.contains("jdbc:ucanaccess")) {
			if (dataType.equals("CHAR")) {
				alternative = "TEXT";
			}
			if (dataType.equals("VARCHAR") || dataType.equals("LONG VARCHAR")) {
				alternative = "MEMO";
			}
			if (dataType.equals("INTEGER")
					|| dataType.equals("BIGINT")) {
				alternative = "LONG";
			}
			if (dataType.equals("REAL")) {
				alternative = "CURRENCY";
			}
			if (dataType.equals("BLOB")) {
				alternative = "OLE";
			}
		}

		if (dbDriverName.contains("jdbc:sqlserver")) {
			if (dataType.equals("INTEGER")) {
				alternative = "int";
			}
			if (dataType.equals("LONG VARCHAR")) {
				alternative = "text";
			}
			if (dataType.equals("DOUBLE")) {
				alternative = "float";
			}
			if (dataType.equals("CHAR") && optionList.contains("KANJI")) {
				alternative = "nchar";
			}
			alternative = alternative.toLowerCase();
		}

		return alternative;
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
		if (dataType.equals("BYTEA")) {
			basicType = "BYTEA";
		}
		if (dataType.equals("DATE")) {
			basicType = "DATE";
		}
		if (dataType.startsWith("TIME")) {
			basicType = "TIME";
		}
		if (dataType.startsWith("TIMESTAMP")) {
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

class DialogCheckTableModule_jButtonPut_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckTableModule adaptee;
	DialogCheckTableModule_jButtonPut_actionAdapter(DialogCheckTableModule adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonPut_actionPerformed(e);
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
