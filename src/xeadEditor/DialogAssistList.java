package xeadEditor;

/*
 * Copyright (c) 2016 WATANABE kozo <qyf05466@nifty.com>,
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

import xeadEditor.Editor.SortableDomElementListModel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogAssistList extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JScrollPane scrollPaneToBeAssisted;
	private JTextArea textAreaToBeAssisted;
	private JList jListAssistList;
	private ArrayList<String> instanceAssistListArray;
	private ArrayList<String> sessionAssistListArray;
	private JScrollPane jScrollPaneAssistList = null;
	private Point scrollPos;
	private int caretPos;
	private int stringPosInTextFrom;
	private String assistMode = "";
	private String idSelected = "";
	private Editor frame_;

	public DialogAssistList(Editor frame) {
		super();
		frame_ = frame;
		try {
			jbInit();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogAssistList(Editor frame, JDialog dialog) {
		super(dialog, false);
		frame_ = frame;
		try {
			jbInit();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public DialogAssistList(Editor frame, JDialog dialog, boolean isMordal) {
		super(dialog, isMordal);
		frame_ = frame;
		try {
			jbInit();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		jScrollPaneAssistList = new JScrollPane();
		jListAssistList = new JList();
		jListAssistList.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE+2));
		DefaultListModel model = new DefaultListModel();
		jListAssistList.setModel(model);
		jListAssistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jListAssistList.addKeyListener(new DialogAssistList_keyAdapter(this));
		jScrollPaneAssistList.getViewport().add(jListAssistList, null);
		jScrollPaneAssistList.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().add(jScrollPaneAssistList, null);
		this.setPreferredSize(new Dimension(400, 300));
		this.setUndecorated(true);
		instanceAssistListArray = new ArrayList<String>();
		instanceAssistListArray.add("cancelWithMessage(message)");
		instanceAssistListArray.add("callFunction(functionID)");
		instanceAssistListArray.add("commit()");
		instanceAssistListArray.add("rollback()");
		instanceAssistListArray.add("getFunctionID()");
		instanceAssistListArray.add("getParmMap()");
		instanceAssistListArray.add("getReturnMap()");
		instanceAssistListArray.add("setProcessLog(text)");
		instanceAssistListArray.add("createTableOperator(oparation, tableID)");
		instanceAssistListArray.add("createTableOperator(sql)");
		instanceAssistListArray.add("createTableEvaluator(tableID)");
		instanceAssistListArray.add("getFieldObjectByID(tableID, fieldID)");
		instanceAssistListArray.add("getUserValueOf(dataSourceName)");
		instanceAssistListArray.add("setUserValueOf(dataSourceName, value)");
		instanceAssistListArray.add("getVariant(variantID)");
		instanceAssistListArray.add("setVariant(variantID, value)");
		instanceAssistListArray.add("setStatusMessage(message)");
		instanceAssistListArray.add("executeScript(text)");
		Collections.sort(instanceAssistListArray);
		sessionAssistListArray = new ArrayList<String>();
		sessionAssistListArray.add("getDatabaseName()");
		sessionAssistListArray.add("getDatabaseUser()");
		sessionAssistListArray.add("getDatabasePassword()");
		sessionAssistListArray.add("getFileFolder()");
		sessionAssistListArray.add("getFileName()");
		sessionAssistListArray.add("getImageFileFolder()");
		sessionAssistListArray.add("getOutputFolder()");
		sessionAssistListArray.add("getAdminEmail()");
		sessionAssistListArray.add("getSessionID()");
		sessionAssistListArray.add("getUserEmployeeNo()");
		sessionAssistListArray.add("getUserEmailAddress()");
		sessionAssistListArray.add("getUserID()");
		sessionAssistListArray.add("getUserName()");
		sessionAssistListArray.add("getAttribute(attributeID)");
		sessionAssistListArray.add("setAttribute(attributeID, value)");
		sessionAssistListArray.add("getFunctionName(functionID)");
		sessionAssistListArray.add("getSubDBListSize()");
		sessionAssistListArray.add("getSubDBName(index)");
		sessionAssistListArray.add("getSubDBUser(index)");
		sessionAssistListArray.add("getSubDBPassword(index)");
		sessionAssistListArray.add("getSystemProperty(propertyID)");
		sessionAssistListArray.add("compressTable(tableID)");
		sessionAssistListArray.add("createTableOperator(sql)");
		sessionAssistListArray.add("createTableOperator(operation, tableID)");
		sessionAssistListArray.add("commit()");
		sessionAssistListArray.add("rollback()");
		sessionAssistListArray.add("decodeBase64StringToByteArray(value)");
		sessionAssistListArray.add("encodeByteArrayToBase64String(byteArray)");
		sessionAssistListArray.add("getTableName(tableID)");
		sessionAssistListArray.add("getFieldName(tableID, fieldID)");
		sessionAssistListArray.add("getFieldType(tableID, fieldID)");
		sessionAssistListArray.add("getFieldSize(tableID, fieldID)");
		sessionAssistListArray.add("getFieldDecimal(tableID, fieldID)");
		sessionAssistListArray.add("copyFile(originalName, newName)");
		sessionAssistListArray.add("existsFile(fileName)");
		sessionAssistListArray.add("deleteFile(fileName)");
		sessionAssistListArray.add("renameFile(currentName, newName)");
		sessionAssistListArray.add("createExcelFileOperator(fileName)");
		sessionAssistListArray.add("createTextFileOperator(operation, fileName, separator, charset)");
		sessionAssistListArray.add("getAnnualExchangeRate(currency, fYear, type)");
		sessionAssistListArray.add("getMonthlyExchangeRate(currency, fYear, mSeq, type)");
		sessionAssistListArray.add("getMonthlyExchangeRate(currency, date, type)");
		sessionAssistListArray.add("getNextNumber(numberID)");
		sessionAssistListArray.add("setNextNumber(numberID, value)");
		sessionAssistListArray.add("getSystemVariantFloat(variantID)");
		sessionAssistListArray.add("getSystemVariantInteger(variantID)");
		sessionAssistListArray.add("getSystemVariantString(variantID)");
		sessionAssistListArray.add("setSystemVariant(variantID, value)");
		sessionAssistListArray.add("getTaxAmount(date, amount)");
		sessionAssistListArray.add("getTaxAmount(date, amount, kbKazei)");
		sessionAssistListArray.add("getUserVariantDescription(variantID, value)");
		sessionAssistListArray.add("getDaysBetweenDates(dateFrom, dateThru, countType, kbCalendar)");
		sessionAssistListArray.add("getMinutesBetweenTimes(timeFrom, timeThru)");
		sessionAssistListArray.add("getOffsetDate(date, days, countType, kbCalendar)");
		sessionAssistListArray.add("getOffsetDateTime(date, time, minutes countType, kbCalendar)");
		sessionAssistListArray.add("formatTime(time)");
		sessionAssistListArray.add("getOffsetYearMonth(yearMonth, months)");
		sessionAssistListArray.add("getTimeStamp()");
		sessionAssistListArray.add("getThisMonth()");
		sessionAssistListArray.add("getToday()");
		sessionAssistListArray.add("isOffDate(date, kbCalendar)");
		sessionAssistListArray.add("isValidDate(date)");
		sessionAssistListArray.add("isValidDateFormat(date, separator)");
		sessionAssistListArray.add("isValidTime(time, format)");
		sessionAssistListArray.add("getFYearOfDate(date)");
		sessionAssistListArray.add("getMSeqOfDate(date)");
		sessionAssistListArray.add("getErrorOfAccountDate(date)");
		sessionAssistListArray.add("getYearMonthOfFYearMSeq(fYearMSeq)");
		sessionAssistListArray.add("executeProgram(programName)");
		sessionAssistListArray.add("browseFile(fileName)");
		sessionAssistListArray.add("editFile(fileName)");
		sessionAssistListArray.add("sendMail(addressFrom, addressTo, addressCc, subject, message, fileName, attachedName, charset)");
		sessionAssistListArray.add("startProgress(text, max)");
		sessionAssistListArray.add("incrementProgress()");
		sessionAssistListArray.add("endProgress()");
		sessionAssistListArray.add("getOptionDialog()");
		sessionAssistListArray.add("getInputDialog()");
		sessionAssistListArray.add("getCheckListDialog()"); 
		sessionAssistListArray.add("getDigestedValue(value, algorithm, expand, salt)");
		sessionAssistListArray.add("getRandomString(length, characters)");
		sessionAssistListArray.add("requestWebService(uri, encoding)");
		sessionAssistListArray.add("createWebServiceRequest(uri, encoding)");
		sessionAssistListArray.add("parseStringToGetXmlDocument(data, encoding)");
		sessionAssistListArray.add("parseXmlDocumentToGetString(document)");
		sessionAssistListArray.add("createXmlDocument(name)");
		sessionAssistListArray.add("createXmlNode(document, name)");
		sessionAssistListArray.add("getXmlNode(node, name)");
		sessionAssistListArray.add("getXmlNodeList(node, name)");
		sessionAssistListArray.add("getXmlNodeContent(node, name)");
		sessionAssistListArray.add("createJsonObject(text)");
		sessionAssistListArray.add("createJsonArray(text)");
		sessionAssistListArray.add("getJsonObject(object, key)");
		sessionAssistListArray.add("getJsonObject(array, index)");
		sessionAssistListArray.add("showDialogToChooseFile(fileExtention, directory, defaultFile)");
		sessionAssistListArray.add("copyTableRecords(fromFile, toFile, processType, count, isToCommitEachTime)");
		Collections.sort(sessionAssistListArray);
	}

	public String listIDs(String text, ArrayList<String> idList, ArrayList<String> nameList, JTextField nameField) {
		String wrkStr;
		assistMode = "IDS";
		idSelected = "";
		DefaultListModel model = (DefaultListModel)jListAssistList.getModel();
		model.removeAllElements();
		model.addElement("(" + res.getString("Close") + ")");
		for (int i = 0; i < idList.size(); i++) {
			wrkStr = idList.get(i).toUpperCase();
			if (wrkStr.startsWith(text.toUpperCase())) {
				model.addElement(idList.get(i) + " - " + nameList.get(i));
			}
		}
		if (jListAssistList.getModel().getSize() == 0) {
			JOptionPane.showMessageDialog(null, res.getString("AssistListMessage"));
		} else {
			if (jListAssistList.getModel().getSize() <= 2) {
				idSelected = jListAssistList.getModel().getElementAt(1).toString();
			} else {
				jListAssistList.setSelectedIndex(0);
				jScrollPaneAssistList.getViewport().setViewPosition(new Point(0,0));
				this.setLocation(this.getParent().getLocation().x + nameField.getBounds().x, this.getParent().getLocation().y + nameField.getBounds().y);
				this.setVisible(true);
			}
		}
		return idSelected;
	}
	

	public void listMethods(JScrollPane scrollPane) {
		assistMode = "METHODS";
		scrollPaneToBeAssisted = scrollPane;
		textAreaToBeAssisted = (JTextArea)scrollPaneToBeAssisted.getViewport().getView();

		scrollPos = scrollPaneToBeAssisted.getViewport().getViewPosition();
		caretPos = textAreaToBeAssisted.getCaretPosition();
		Point caretPoint = textAreaToBeAssisted.getCaret().getMagicCaretPosition();
		Point areaPoint = textAreaToBeAssisted.getLocationOnScreen();

		int pos = caretPos - 1;
		String wrkStr;
		String stringToBeAssisted = textAreaToBeAssisted.getText().substring(0, caretPos);
		if (!stringToBeAssisted.equals("session.") && !stringToBeAssisted.equals("instance.")) {
			stringToBeAssisted = "";
			while (pos >= 0) {
				wrkStr = textAreaToBeAssisted.getText().substring(pos, pos+1);
				if (wrkStr.equals("\n") || wrkStr.equals("\t") || wrkStr.equals(",") || wrkStr.equals(" ")
						|| wrkStr.equals(";") || wrkStr.equals("(") || wrkStr.equals("{") || wrkStr.equals("}")) {
					stringPosInTextFrom = pos + 1;
					if (stringPosInTextFrom < caretPos) {
						stringToBeAssisted = textAreaToBeAssisted.getText().substring(stringPosInTextFrom, caretPos);
					}
					break;
				}
				pos--;
			}
		}
		setupAssistList(stringToBeAssisted);
		if (jListAssistList.getModel().getSize() == 0) {
			JOptionPane.showMessageDialog(null, res.getString("AssistListMessage"));
		} else {
			if (jListAssistList.getModel().getSize() == 1) {
				updateTextWithAssistCode(jListAssistList.getModel().getElementAt(0).toString() + ";");
			} else {
				jListAssistList.setSelectedIndex(0);
				jScrollPaneAssistList.getViewport().setViewPosition(new Point(0,0));
				int posX = areaPoint.x + caretPoint.x;
				int rightPosX = posX + this.getBounds().width;
				if (rightPosX > frame_.screenWidth) {
					posX = posX - (rightPosX - frame_.screenWidth);
				}
				int posY = areaPoint.y + caretPoint.y;
				int bottomPosY = posY + this.getBounds().height;
				if (bottomPosY > (frame_.screenHeight - 30)) {
					posY = posY - (bottomPosY - frame_.screenHeight + 30);
				}
				this.setLocation(posX, posY);
				this.setVisible(true);
			}
		}
	}

	public void setupAssistList(String string) {
		String wrkStr;
		DefaultListModel model = (DefaultListModel)jListAssistList.getModel();
		model.removeAllElements();

		if (string.equals("")) {
			model.addElement("Table definition");
		}

		ArrayList<String> functionList = frame_.getScriptFunctionList();
		for (int i = 0; i < functionList.size(); i++) {
			wrkStr = functionList.get(i).toUpperCase();
			if (functionList.get(i).toUpperCase().startsWith(string.toUpperCase())) {
				model.addElement(functionList.get(i));
			}
		}

		if (string.equals("") || string.startsWith("instance.")) {
			for (int i = 0; i < instanceAssistListArray.size(); i++) {
				wrkStr = "INSTANCE."+instanceAssistListArray.get(i).toUpperCase();
				if (wrkStr.startsWith(string.toUpperCase())) {
					if (string.equals("")) {
						model.addElement("instance." + instanceAssistListArray.get(i));
					} else {
						model.addElement(instanceAssistListArray.get(i));
					}
				}
			}
		}

		if (string.equals("") || string.startsWith("session.")) {
			for (int i = 0; i < sessionAssistListArray.size(); i++) {
				wrkStr = "SESSION."+sessionAssistListArray.get(i).toUpperCase();
				if (wrkStr.startsWith(string.toUpperCase())) {
					if (string.equals("")) {
						model.addElement("session." + sessionAssistListArray.get(i));
					} else {
						model.addElement(sessionAssistListArray.get(i));
					}
				}
			}
		}
	}

	void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (assistMode.equals("METHODS")) {
				if (jListAssistList.getSelectedIndex() > -1) {
					if (jListAssistList.getSelectedValue().toString().equals("Table definition")) {
						String tableID = JOptionPane.showInputDialog(null, res.getString("SpecifyTableID"));
						if (tableID != null && !tableID.equals("")) {
							updateTextWithAssistCode(getTableDefinition(tableID));
						}
					} else {
						updateTextWithAssistCode(jListAssistList.getSelectedValue().toString() + ";");
					}
				}
			}
			if (assistMode.equals("IDS")) {
				if (jListAssistList.getSelectedIndex() > -1) {
					idSelected = jListAssistList.getSelectedValue().toString();
				}
			}
			this.setVisible(false);
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.setVisible(false);
		}
	}

	String getTableDefinition(String tableID) {
		org.w3c.dom.Element tableElement, element;
		String databaseName, wrkStr, moduleKeyFields;
		ArrayList<String> optionList = new ArrayList<String>();
		ArrayList<String> pkFieldList = new ArrayList<String>();
		StringTokenizer workTokenizer;
		StringBuffer moduleBuf = new StringBuffer();

		tableID = tableID.toUpperCase();
		tableElement = frame_.getSpecificTableElement(tableID);
		if (tableElement == null) {
				JOptionPane.showMessageDialog(null, "ID is invalid.");
		} else {
			databaseName = frame_.getDatabaseName(tableElement.getAttribute("DB"));
			moduleBuf.append("// " + tableID + " " + tableElement.getAttribute("Name") + "\n");

			NodeList keyList = tableElement.getElementsByTagName("Key");
			for (int i = 0; i < keyList.getLength(); i++) {
				element = (org.w3c.dom.Element)keyList.item(i);
				if (element.getAttribute("Type").equals("PK")) {
					moduleKeyFields = element.getAttribute("Fields");
					workTokenizer = new StringTokenizer(moduleKeyFields, ";");
					while (workTokenizer.hasMoreTokens()) {
						wrkStr = workTokenizer.nextToken();
						pkFieldList.add(wrkStr);
					}
				}
			}

			NodeList fieldList = tableElement.getElementsByTagName("Field");
			SortableDomElementListModel sortingList = frame_.getSortedListModel(fieldList, "Order");
			for (int i = 0; i < sortingList.getSize(); i++) {
				element = (org.w3c.dom.Element)sortingList.getElementAt(i);
				optionList = frame_.getOptionList(element.getAttribute("TypeOptions"));
				if (!optionList.contains("VIRTUAL")) {

					if (pkFieldList.contains(element.getAttribute("ID"))) {
						moduleBuf.append("// * ");
					} else {
						moduleBuf.append("// ");
					}
					moduleBuf.append(element.getAttribute("ID") + " " + element.getAttribute("Name") + " ");
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
						if (frame_.getBasicTypeOf(element.getAttribute("Type")).equals("INTEGER")) {
							moduleBuf.append(" Default 0");
						} else {
							if (frame_.getBasicTypeOf(element.getAttribute("Type")).equals("FLOAT")) {
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
					moduleBuf.append("\n");
				}
			}
		}

		return moduleBuf.toString();
	}

	void updateTextWithAssistCode(String codeToBeInserted) {
		String text = textAreaToBeAssisted.getText();
		int slicedThru = caretPos;

		String stringToBeReplaced = text.substring(stringPosInTextFrom, caretPos);
		if (stringToBeReplaced.startsWith("instance.") || stringToBeReplaced.startsWith("session.")) {
			String strWrk = stringToBeReplaced.replace("instance.","").replace("session.","");
			if (codeToBeInserted.startsWith(strWrk)) {
				slicedThru = text.indexOf(".", stringPosInTextFrom) + 1;
			}
		} else {
			if (codeToBeInserted.startsWith(stringToBeReplaced)) {
				slicedThru = stringPosInTextFrom;
			}
		}

		StringBuffer buf = new StringBuffer();
		buf.append(text.substring(0, slicedThru));
		buf.append(codeToBeInserted);
		buf.append(text.substring(caretPos, text.length()));
		textAreaToBeAssisted.setText(buf.toString());

		textAreaToBeAssisted.updateUI();
		scrollPaneToBeAssisted.getViewport().setViewPosition(scrollPos);
		textAreaToBeAssisted.setCaretPosition(slicedThru + codeToBeInserted.length());

		// without this step edit tool will disappear //
		frame_.jTextAreaFunction000Script.add(frame_.jPanelFunction000ScriptEditTool);
	}
}

class DialogAssistList_keyAdapter extends java.awt.event.KeyAdapter {
	DialogAssistList adaptee;
	DialogAssistList_keyAdapter(DialogAssistList adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.keyReleased(e);
	}
}