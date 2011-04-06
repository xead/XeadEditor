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

import xeadEditor.Editor.MainTreeNode;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogCreateTable extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JButton jButtonCreate = new JButton();
	private JButton jButtonClose = new JButton();
	private JPanel jPanelMain = new JPanel();
	private JPanel jPanelTop = new JPanel();
	private JPanel jPanelStatement = new JPanel();
	private JPanel jPanelMessage = new JPanel();
	private JLabel jLabelSubsystem = new JLabel();
	private JComboBox jComboBoxSubsystem = new JComboBox();
	private JLabel jLabelTableName = new JLabel();
	private Editor_KanjiTextField jTextFieldTableName = new Editor_KanjiTextField();
	private JLabel jLabelStatement = new JLabel();
	private JScrollPane jScrollPaneStatement = new JScrollPane();
	private JTextArea jTextAreaStatement = new JTextArea();
	private JLabel jLabelMessage = new JLabel();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private JSplitPane jSplitPane = new JSplitPane();
	private Editor frame_;
	private JPanel jPanelButtons = new JPanel();
	private boolean executed;
	private ArrayList<MainTreeNode> subsystemNodeList;
	private ArrayList<String> messageList = new ArrayList<String>();
	private ArrayList<String> fieldIDList = new ArrayList<String>();
	private ArrayList<String> validTypeList = new ArrayList<String>();
	private String primaryKeyFieldID = "";
	
	public DialogCreateTable(Editor frame) {
		super(frame, "", true);
		frame_ = frame;
		try {
			init();
			pack();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void init() throws Exception {
		//
		jScrollPaneMessage.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(new BorderLayout());
		//
		jLabelSubsystem.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelSubsystem.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSubsystem.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSubsystem.setText(res.getString("CreateInSubsystem"));
		jLabelSubsystem.setBounds(new Rectangle(11, 12, 116, 15));
		jComboBoxSubsystem.setFont(new java.awt.Font("SansSerif", 0, 12));
		jComboBoxSubsystem.setBounds(new Rectangle(135, 9, 200, 22));
		jComboBoxSubsystem.addActionListener(new DialogCreateTable_jComboBoxSubsystem_actionAdapter(this));
		jLabelTableName.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelTableName.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelTableName.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelTableName.setText(res.getString("TableName"));
		jLabelTableName.setBounds(new Rectangle(350, 12, 116, 15));
		jTextFieldTableName.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTextFieldTableName.setBounds(new Rectangle(473, 9, 200, 22));
		jPanelTop.setLayout(null);
		jPanelTop.setBorder(BorderFactory.createEtchedBorder());
		jPanelTop.setPreferredSize(new Dimension(100, 40));
		jPanelTop.add(jLabelSubsystem, null);
		jPanelTop.add(jComboBoxSubsystem, null);
		jPanelTop.add(jLabelTableName, null);
		jPanelTop.add(jTextFieldTableName, null);
		jLabelStatement.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelStatement.setText(" " + res.getString("CreateTableStatement"));
		jLabelStatement.setPreferredSize(new Dimension(100, 17));
		jTextAreaStatement.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaStatement.setEditable(true);
		jTextAreaStatement.setOpaque(true);
		jTextAreaStatement.setLineWrap(true);
		jScrollPaneStatement.getViewport().add(jTextAreaStatement);
		jPanelMain.setLayout(new BorderLayout());
		jPanelMain.add(jPanelTop, BorderLayout.NORTH);
		jPanelMain.add(jPanelStatement, BorderLayout.CENTER);
		jPanelStatement.setLayout(new BorderLayout());
		jPanelStatement.add(jLabelStatement, BorderLayout.NORTH);
		jPanelStatement.add(jScrollPaneStatement, BorderLayout.CENTER);
		//
		jLabelMessage.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelMessage.setText(" " + res.getString("Message"));
		jLabelMessage.setPreferredSize(new Dimension(100, 17));
		jTextAreaMessage.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setLineWrap(true);
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jPanelMessage.setLayout(new BorderLayout());
		jPanelMessage.add(jLabelMessage, BorderLayout.NORTH);
		jPanelMessage.add(jScrollPaneMessage, BorderLayout.CENTER);
		//
		jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane.setDividerLocation(350);
		jSplitPane.add(jPanelMain, JSplitPane.TOP);
		jSplitPane.add(jPanelMessage, JSplitPane.BOTTOM);
		//
		jButtonClose.setText(res.getString("Close"));
		jButtonClose.setBounds(new Rectangle(30, 8, 80, 25));
		jButtonClose.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonClose.addActionListener(new DialogCreateTable_jButtonClose_actionAdapter(this));
		jButtonCreate.setText(res.getString("CreateDefinition"));
		jButtonCreate.setBounds(new Rectangle(600, 8, 110, 25));
		jButtonCreate.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCreate.addActionListener(new DialogCreateTable_jButtonCreate_actionAdapter(this));
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(400, 41));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonClose, null);
		jPanelButtons.add(jButtonCreate, null);
		//
		validTypeList.add("CHAR");
		validTypeList.add("VARCHAR");
		validTypeList.add("INTEGER");
		validTypeList.add("SMALLINT");
		validTypeList.add("BIGINT");
		validTypeList.add("DOUBLE");
		validTypeList.add("DECIMAL");
		validTypeList.add("NUMERIC");
		validTypeList.add("REAL");
		validTypeList.add("DATE");
		validTypeList.add("TIME");
		validTypeList.add("TIMESTAMP");
		//
		this.setTitle(res.getString("CreateTableTitle"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(750, 550));
		this.getContentPane().add(jSplitPane,  BorderLayout.CENTER);
	}
	//
	public boolean request() {
		//
		executed = false;
		//
		org.w3c.dom.Element element;
		MainTreeNode node;
		MainTreeNode subsystemList = frame_.getSubsystemListNode();
		jComboBoxSubsystem.removeAllItems();
		jComboBoxSubsystem.addItem(res.getString("SelectFromList"));
		subsystemNodeList = new ArrayList<MainTreeNode>();
		subsystemNodeList.add(null);
		for (int i = 0; i < subsystemList.getChildCount(); i++) {
			node = (MainTreeNode)subsystemList.getChildAt(i);
			element = (org.w3c.dom.Element)node.getElement();
			jComboBoxSubsystem.addItem(element.getAttribute("ID") + " " + element.getAttribute("Name"));
			subsystemNodeList.add(node);
		}
		jTextFieldTableName.setText("*ID");
		jTextAreaStatement.setText("");
		jButtonCreate.setEnabled(false);
		//
		jPanelButtons.getRootPane().setDefaultButton(jButtonClose);
		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = frame_.getSize();
		Point loc = frame_.getLocation();
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		jTextAreaMessage.setText(res.getString("CreateTableComment"));
		super.setVisible(true);
		//
		return executed;
	}
	//
	void jComboBoxSubsystem_actionPerformed(ActionEvent e) {
		if (jComboBoxSubsystem.getSelectedIndex() == 0) {
			jButtonCreate.setEnabled(false);
		} else {
			jButtonCreate.setEnabled(true);
		}
	}
	//
	void jButtonCreate_actionPerformed(ActionEvent e) {
		String wrkStr;
		int bracketOpen = 0;
		int bracketClose = 0;
		int scanStartFrom = 0;
		int posOfCREATE_TABLE = 0;
		int posOfFirstBracket = 0;
		int posWork = 0;
		String tableID = "";
		String tableName = jTextFieldTableName.getText();
		String tableAttr = "";
		org.w3c.dom.Element tableElement;
		//
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			//
			messageList.clear();
			//
			String statements = " " + jTextAreaStatement.getText().toUpperCase();
			if (statements.length() > 1) {
				if (statements.contains("CREATE TABLE ")) {
					while (scanStartFrom < statements.length()) {
						posOfCREATE_TABLE = statements.indexOf("CREATE TABLE ", scanStartFrom);
						if (posOfCREATE_TABLE >= 0) {
							posOfFirstBracket = statements.indexOf("(", posOfCREATE_TABLE);
							if (posOfFirstBracket > 0) {
								posWork = posOfFirstBracket;
								bracketOpen = 0;
								bracketClose = 0;
								while (posWork < statements.length()) {
									wrkStr = statements.substring(posWork,posWork+1);
									if (wrkStr.equals("(")) {
										bracketOpen++;
									}
									if (wrkStr.equals(")")) {
										bracketClose++;
									}
									if (bracketOpen == bracketClose && bracketOpen > 0) {
										tableID = getSubstringInOrder(statements.substring(posOfCREATE_TABLE+13, posOfFirstBracket), 0).toUpperCase();
										if (tableName.equals("*ID") || tableName.equals("")) {
											tableName = tableID;
										}
										tableAttr = statements.substring(posOfFirstBracket+1, posWork);
										posOfCREATE_TABLE = posWork;
										break;
									}
									posWork++;
								}
							}
							scanStartFrom = posOfCREATE_TABLE+1;
						} else {
							scanStartFrom = statements.length();
						}
					}
					//
					if (tableID.contains("_")) {
						messageList.add(res.getString("ErrorMessage105"));
					} else {
						MainTreeNode subsystemNode = subsystemNodeList.get(jComboBoxSubsystem.getSelectedIndex());
						tableElement = createTableDefinition(subsystemNode, tableID, tableName, tableAttr);
						if (tableElement == null) {
							messageList.add(res.getString("ErrorMessage97"));
						} else {
							if (frame_.getSpecificXETreeNode("Table", tableElement.getAttribute("ID")) == null) {
								frame_.getSystemNode().getElement().appendChild(tableElement);
								MainTreeNode childNode = frame_.new MainTreeNode("Table", tableElement, frame_);
								if (childNode != null) {
									((MainTreeNode)subsystemNode.getChildAt(0)).add(childNode);
									frame_.getUndoManager().addLogOfAdd(childNode);
									((MainTreeNode)subsystemNode.getChildAt(0)).sortChildNodes();
								}
								messageList.add(res.getString("ErrorMessage98") + tableElement.getAttribute("ID") + res.getString("ErrorMessage99"));
								executed = true;
							} else {
								messageList.add(res.getString("ErrorMessage100") + tableElement.getAttribute("ID") + res.getString("ErrorMessage101"));
							}
						}
					}
				}
			}
			//
		} catch (Exception ex1) {
			ex1.printStackTrace();
			messageList.add("Creating definition of table failed.\n" + ex1.getMessage());
		} finally {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < messageList.size(); i++) {
				buf.append(messageList.get(i) + "\n");
			}
			jTextAreaMessage.setText(buf.toString());
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	//
	void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	//
	org.w3c.dom.Element createTableDefinition(MainTreeNode subsystemNode, String tableID, String tableName, String tableAttributes) {
		ArrayList<String> attrList = new ArrayList<String>();
		org.w3c.dom.Element fieldElement;
		String wrkStr1 = "";

		///////////////////////////
		//Create Table Definition//
		///////////////////////////
		org.w3c.dom.Element tableElement = frame_.getDomDocument().createElement("Table");
		tableElement.setAttribute("ID", tableID);
		tableElement.setAttribute("Name", tableName);
		tableElement.setAttribute("SubsystemID", subsystemNode.getElement().getAttribute("ID"));
		tableElement.setAttribute("ActiveWhere", "");
		tableElement.setAttribute("RangeKey", "");
		tableElement.setAttribute("Remarks", "");

		///////////////////////////////////
		//Substring attributes into array//
		///////////////////////////////////
		int bracketOpen = 0;
		int lastPosOfBracketClose = 0;
		int bracketClose = 0;
		tableAttributes = tableAttributes.replace("\n", "");
		int posStartFrom = 0;
		for (int pos = 0; pos < tableAttributes.length(); pos++) {
			if (tableAttributes.substring(pos, pos+1).equals(",")) {
				if (bracketOpen == bracketClose) {
					attrList.add(tableAttributes.substring(posStartFrom, pos));
					posStartFrom = pos + 1;
					bracketOpen = 0;
					bracketClose = 0;
				}
			}
			if (tableAttributes.substring(pos, pos+1).equals("(")) {
				bracketOpen++;
			}
			if (tableAttributes.substring(pos, pos+1).equals(")")) {
				bracketClose++;
				if (bracketOpen == bracketClose) {
					lastPosOfBracketClose = pos;
				}
			}
		}
		if (lastPosOfBracketClose > posStartFrom) {
			attrList.add(tableAttributes.substring(posStartFrom, lastPosOfBracketClose + 1));
		}

		/////////////////////
		//Create TableField//
		/////////////////////
		primaryKeyFieldID = "";
		fieldIDList.clear();
		for (int i = 0; i < attrList.size(); i++) {
			if (!attrList.get(i).contains("CONSTRAINT ")) {
				fieldElement = createFieldDefinition(i, attrList.get(i));
				if (fieldElement != null) {
					fieldIDList.add(fieldElement.getAttribute("ID"));
					tableElement.appendChild(fieldElement);
				}
			}
		}

		////////////////////////////
		//Set Fields of PrimaryKey//
		////////////////////////////
		if (primaryKeyFieldID.equals("")) {
			for (int i = 0; i < attrList.size(); i++) {
				if (attrList.get(i).contains("PRIMARY KEY")) {
					primaryKeyFieldID = getKeyFieldID(attrList.get(i));
					break;
				}
			}
		}
		if (!primaryKeyFieldID.equals("")) {
			org.w3c.dom.Element primaryKeyElement = frame_.getDomDocument().createElement("Key");
			primaryKeyElement.setAttribute("Type", "PK");
			primaryKeyElement.setAttribute("Fields", primaryKeyFieldID);
			tableElement.appendChild(primaryKeyElement);
		}

		//////////////
		//Create SK //
		//////////////
		for (int i = 0; i < attrList.size(); i++) {
			if (attrList.get(i).contains(" UNIQUE") && !attrList.get(i).contains(" PRIMARY KEY")) {
				wrkStr1 = getKeyFieldID(attrList.get(i));
				if (!wrkStr1.equals("")) {
					org.w3c.dom.Element secondaryKeyElement = frame_.getDomDocument().createElement("Key");
					secondaryKeyElement.setAttribute("Type", "SK");
					secondaryKeyElement.setAttribute("Fields", wrkStr1);
					tableElement.appendChild(secondaryKeyElement);
				}
			}
		}

		if (fieldIDList.size() > 0) {
			return tableElement;
		} else {
			return null;
		}
	}
	//
	org.w3c.dom.Element createFieldDefinition(int sortKey, String fieldAttrString) {
		String fieldID = "";
		String nullable = "T";
		String comment = "";
		int posStart = 0;
		int posEnd = 0;
		//
		////////////////
		//Get Field ID//
		////////////////
		fieldID = getSubstringInOrder(fieldAttrString, 0).toUpperCase();
		if (fieldID.equals("")) {
			return null;
		}
		////////////////
		//Get NOT NULL//
		////////////////
		if (fieldAttrString.contains("NOT NULL")) {
			nullable = "F";
		}
		////////////////
		//Get COMMENT //
		////////////////
		comment = "";
		int i = fieldAttrString.indexOf(" COMMENT ", 0);
		if (i > 0) {
			for (int j=i+9; j < fieldAttrString.length(); j++) {
				if (posStart == 0 && !fieldAttrString.substring(j,j+1).equals("'")) {
					posStart = j;
				}
				posEnd = j;
				if (posStart > 0 && fieldAttrString.substring(j,j+1).equals("'")) {
					break;
				}
			}
			if (posEnd > posStart) {
				comment = fieldAttrString.substring(posStart, posEnd);
			}
		}
		////////////////////////
		//Set field attributes//
		////////////////////////
		org.w3c.dom.Element fieldElement = frame_.getDomDocument().createElement("Field");
		fieldElement.setAttribute("ID", fieldID);
		if (comment.equals("")) {
			fieldElement.setAttribute("Name", fieldID);
		} else {
			fieldElement.setAttribute("Name", comment);
		}
		fieldElement.setAttribute("Order", frame_.getFormatted4ByteString(sortKey * 10));
		String wrkStr = getSubstringInOrder(fieldAttrString, 1);
		i = wrkStr.indexOf("(", 0);
		if (i > 0) {
			String wrkType = wrkStr.substring(0, i);
			fieldElement.setAttribute("Type", wrkType);
			if (wrkType.equals("DOUBLE")
					 || wrkType.equals("DECIMAL")
					 || wrkType.equals("NUMERIC")
					 || wrkType.equals("REAL")) {
				StringTokenizer tokenizer = new StringTokenizer(wrkStr.substring(i+1, wrkStr.length()-1), ",");
				fieldElement.setAttribute("Size", tokenizer.nextToken());
				if (tokenizer.hasMoreTokens()) {
					fieldElement.setAttribute("Decimal", tokenizer.nextToken());
				} else {
					fieldElement.setAttribute("Decimal", "0");
				}
			} else {
				fieldElement.setAttribute("Size", wrkStr.substring(i+1, wrkStr.length()-1));
				fieldElement.setAttribute("Decimal", "");
			}
		} else {
			fieldElement.setAttribute("Type", wrkStr);
			if (wrkStr.equals("CHAR")) {
				fieldElement.setAttribute("Size", "5");
				fieldElement.setAttribute("Decimal", "");
			}
			if (wrkStr.equals("VARCHAR")) {
				fieldElement.setAttribute("Size", "50");
				fieldElement.setAttribute("Decimal", "");
			}
			if (wrkStr.equals("INTEGER")) {
				fieldElement.setAttribute("Size", "9");
				fieldElement.setAttribute("Decimal", "0");
			}
			if (wrkStr.equals("SMALLINT")) {
				fieldElement.setAttribute("Size", "4");
				fieldElement.setAttribute("Decimal", "0");
			}
			if (wrkStr.equals("BIGINT")) {
				fieldElement.setAttribute("Size", "11");
				fieldElement.setAttribute("Decimal", "0");
			}
			if (wrkStr.equals("DOUBLE") || wrkStr.equals("DECIMAL") || wrkStr.equals("NUMERIC")	|| wrkStr.equals("REAL")) {
				fieldElement.setAttribute("Size", "9");
				fieldElement.setAttribute("Decimal", "1");
			}
			if (wrkStr.equals("DATE")) {
				fieldElement.setAttribute("Size", "10");
				fieldElement.setAttribute("Decimal", "");
			}
			if (wrkStr.equals("TIME")) {
				fieldElement.setAttribute("Size", "8");
				fieldElement.setAttribute("Decimal", "");
			}
			if (wrkStr.equals("TIMESTAMP")) {
				fieldElement.setAttribute("Size", "26");
				fieldElement.setAttribute("Decimal", "");
			}
		}
		fieldElement.setAttribute("Nullable", nullable);
		fieldElement.setAttribute("TypeOptions", "");
		//
		if (validTypeList.contains(fieldElement.getAttribute("Type"))) {
			if (fieldAttrString.contains(" PRIMARY KEY")) {
				primaryKeyFieldID = fieldID;
			}
			return fieldElement;
		} else {
			messageList.add(res.getString("ErrorMessage102") + fieldElement.getAttribute("ID") + res.getString("ErrorMessage103") + fieldElement.getAttribute("Type") + res.getString("ErrorMessage104"));
			return null;
		}
	}
	//
	String getSubstringInOrder(String value, int order) {
		int namePosFrom = -1;
		int namePosThru = -1;
		String wrkStr;
		String substring = "";
		int counter = -1;
		//
		for (int i = 0; i < value.length(); i++) {
			wrkStr = value.substring(i, i+1);
			if (!wrkStr.equals(" ") && namePosFrom == -1) {
				namePosFrom = i;
			}
			if (wrkStr.equals(" ") && namePosFrom != -1 && namePosThru == -1) {
				counter++;
				namePosThru = i;
				if (counter == order) {
					break;
				} else {
					namePosFrom = -1;
					namePosThru = -1;
				}
			}
		}
		if (namePosThru == -1) {
			namePosThru = value.length();
		}
		//
		if (namePosFrom < namePosThru) {
			substring = value.substring(namePosFrom, namePosThru);
		}
		//
		return substring;
	}
	//
	String getKeyFieldID(String value) {
		int pos1, pos2;
		String wrkStr1, wrkStr2;
		StringBuffer buf = new StringBuffer();
		//
		int count = 0;
		pos1 = value.indexOf("(", 0);
		if (pos1 > 0) {
			pos2 = value.indexOf(")", pos1);
			wrkStr1 = value.substring(pos1+1, pos2);
			StringTokenizer workTokenizer = new StringTokenizer(wrkStr1, ",");
			while (workTokenizer.hasMoreTokens()) {
				wrkStr2 = workTokenizer.nextToken().trim().toUpperCase();
				if (fieldIDList.contains(wrkStr2)) {
					if (count > 0) {
						buf.append(";");
					}
					buf.append(wrkStr2);
					count++;
				}
			}
		}
		//
		return buf.toString();
	}
}

class DialogCreateTable_jComboBoxSubsystem_actionAdapter implements java.awt.event.ActionListener {
	DialogCreateTable adaptee;
	DialogCreateTable_jComboBoxSubsystem_actionAdapter(DialogCreateTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxSubsystem_actionPerformed(e);
	}
}

class DialogCreateTable_jButtonCreate_actionAdapter implements java.awt.event.ActionListener {
	DialogCreateTable adaptee;
	DialogCreateTable_jButtonCreate_actionAdapter(DialogCreateTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCreate_actionPerformed(e);
	}
}

class DialogCreateTable_jButtonClose_actionAdapter implements java.awt.event.ActionListener {
	DialogCreateTable adaptee;
	DialogCreateTable_jButtonClose_actionAdapter(DialogCreateTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonClose_actionPerformed(e);
	}
}
