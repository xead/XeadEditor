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

import xeadEditor.Editor.SortableDomElementListModel;
import xeadEditor.Editor.MainTreeNode;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogAddReferTable extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel panelMain = new JPanel();
	private JButton jButtonNext = new JButton();
	private JButton jButtonOK = new JButton();
	private JButton jButtonCancel = new JButton();
	private Editor frame_;
	private JLabel jLabelID = new JLabel();
	private JLabel jLabelAlias = new JLabel();
	private JLabel jLabelToKeyFields = new JLabel();
	private JLabel jLabelWithKeyFields = new JLabel();
	private JTextField jTextFieldID = new JTextField();
	private JTextField jTextFieldAlias = new JTextField();
	private JTextField jTextFieldName = new JTextField();
	private JComboBox jComboBoxToKeyFieldsList = new JComboBox();
	private ArrayList<String> fieldsList = new ArrayList<String>();
	private ArrayList<String> fieldsDefaultValuesList = new ArrayList<String>();
	private JTextField jTextFieldWithKeyFields = new JTextField();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private JPanel jPanelButtons = new JPanel();
	private MainTreeNode referTableNode = null;
	private org.w3c.dom.Element objectTableElement_ = null;
	private org.w3c.dom.Element newElement = null;
	private int lastOrder = 0;
	private ArrayList<String> tableIDList = new ArrayList<String>();
	private ArrayList<String> fieldIDList = new ArrayList<String>();
	private ArrayList<String> dataSourceList = new ArrayList<String>();
	private ArrayList<String> candidateTableIDList = new ArrayList<String>();
	private ArrayList<String> candidateTableNameList = new ArrayList<String>();
	private DialogAssistList dialogAssistList;
	private SortableDomElementListModel sortingList;
	private Action actionListTables = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			listTables(jTextFieldID, jTextFieldName);
		}
	};

	public DialogAddReferTable(Editor frame) {
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
		//
		jLabelID.setText(res.getString("TableID"));
		jLabelID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelID.setBounds(new Rectangle(5, 12, 130, 20));
		jTextFieldID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldID.setBounds(new Rectangle(140, 9, 120, 25));
		jTextFieldID.addKeyListener(new DialogAddReferTable_jTextFieldID_keyAdapter(this));
		InputMap inputMap1 = jTextFieldID.getInputMap(JTextField.WHEN_FOCUSED);
		inputMap1.clear();
		ActionMap actionMap1 = jTextFieldID.getActionMap();
		actionMap1.clear();
		inputMap1.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK), "LIST");
		actionMap1.put("LIST", actionListTables);
		jTextFieldName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName.setBounds(new Rectangle(265, 9, 300, 25));
		jTextFieldName.setEditable(false);
		jTextFieldName.setFocusable(false);
		jLabelAlias.setText(res.getString("Alias"));
		jLabelAlias.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelAlias.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelAlias.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelAlias.setBounds(new Rectangle(575, 12, 130, 20));
		jTextFieldAlias.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldAlias.setBounds(new Rectangle(710, 9, 120, 25));
		jTextFieldAlias.addKeyListener(new DialogAddReferTable_jTextFieldAlias_keyAdapter(this));
		jTextFieldAlias.addFocusListener(new FocusAdapter() {
			@Override public void focusGained(FocusEvent e) {
				((JTextField)e.getComponent()).selectAll();
			}
		});
		//
		jLabelToKeyFields.setText(res.getString("JoinToKeys"));
		jLabelToKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelToKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelToKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelToKeyFields.setBounds(new Rectangle(5, 43, 130, 20));
		jComboBoxToKeyFieldsList.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jComboBoxToKeyFieldsList.setBounds(new Rectangle(140, 40, 690, 25));
		jComboBoxToKeyFieldsList.addActionListener(new DialogAddReferTable_jComboBoxToKeyFieldsList_actionAdapter(this));
		//
		jLabelWithKeyFields.setText(res.getString("JoinWithKeys"));
		jLabelWithKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelWithKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelWithKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelWithKeyFields.setBounds(new Rectangle(5, 74, 130, 20));
		jTextFieldWithKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldWithKeyFields.setBounds(new Rectangle(140, 71, 690, 25));
		//
		jScrollPaneMessage.setBounds(new Rectangle(10, 102, 820, 70));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setFocusable(false);
		//
		panelMain.setLayout(null);
		panelMain.setBorder(null);
		panelMain.add(jLabelID);
		panelMain.add(jTextFieldID);
		panelMain.add(jTextFieldName);
		panelMain.add(jLabelAlias);
		panelMain.add(jTextFieldAlias);
		panelMain.add(jLabelToKeyFields);
		panelMain.add(jComboBoxToKeyFieldsList);
		panelMain.add(jLabelWithKeyFields);
		panelMain.add(jTextFieldWithKeyFields);
		panelMain.add(jScrollPaneMessage);
		//
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(350, 43));
		jButtonNext.setBounds(new Rectangle(660, 8, 100, 27));
		jButtonNext.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonNext.setText(res.getString("Next"));
		jButtonNext.addActionListener(new DialogAddReferTable_jButtonNext_actionAdapter(this));
		jButtonOK.setBounds(new Rectangle(675, 8, 100, 27));
		jButtonOK.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new DialogAddReferTable_jButtonOK_actionAdapter(this));
		jButtonCancel.setBounds(new Rectangle(50, 8, 100, 27));
		jButtonCancel.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogAddReferTable_jButtonCancel_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonOK);
		jPanelButtons.add(jButtonNext);
		jPanelButtons.add(jButtonCancel);
		//
		dialogAssistList = new DialogAssistList(frame_, this, true);
		//
		this.setTitle(res.getString("AddJoinTable"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(845, 262));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		this.pack();
	}

	public org.w3c.dom.Element request(org.w3c.dom.Element objectTableElement) {
		//
		newElement = null;
		objectTableElement_ = objectTableElement;
		//
		jTextFieldID.setText("");
		jTextFieldID.setEditable(true);
		jTextFieldID.setFocusable(true);
		jTextFieldID.requestFocus();
		jTextFieldName.setText("(Ctrl+Space to list)");
		jTextFieldAlias.setText("*TableID");
		jTextFieldAlias.setEditable(true);
		jTextFieldAlias.setFocusable(true);
		jComboBoxToKeyFieldsList.removeAllItems();
		jComboBoxToKeyFieldsList.setEnabled(false);
		jComboBoxToKeyFieldsList.setFocusable(false);
		jTextFieldWithKeyFields.setText("");
		jTextFieldWithKeyFields.setEditable(false);
		jTextFieldWithKeyFields.setFocusable(false);
		jButtonNext.setEnabled(false);
		jButtonNext.setVisible(true);
		jButtonOK.setVisible(false);
		jTextAreaMessage.setText(res.getString("AddJoinTableMessage1"));
		//
		org.w3c.dom.Element element;
		candidateTableIDList.clear();
		candidateTableNameList.clear();
		NodeList xmlnodelist1 = frame_.getDomDocument().getElementsByTagName("Table");
		sortingList = frame_.getSortedListModel(xmlnodelist1, "ID");
		for (int i = 0; i < sortingList.getSize(); i++) {
	    	element = (org.w3c.dom.Element)sortingList.getElementAt(i);
	    	candidateTableIDList.add(element.getAttribute("ID"));
	    	candidateTableNameList.add(element.getAttribute("Name"));
		}
		//
		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = frame_.getSize();
		Point loc = frame_.getLocation();
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		jPanelButtons.getRootPane().setDefaultButton(jButtonNext);
		super.setVisible(true);
		//
		return newElement;
	}

	void listTables(JTextField idField, JTextField nameField) {
		String selectedValue = dialogAssistList.listIDs(idField.getText(), candidateTableIDList, candidateTableNameList, nameField);
		if (selectedValue.contains(" - ")) {
			StringTokenizer tokenizer = new StringTokenizer(selectedValue, " - ");
			idField.setText(tokenizer.nextToken());
			if (idField == jTextFieldID) {
				jTextFieldID_keyReleased(null);					
			}
		}
	}

	void jButtonNext_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element workElement;
		StringTokenizer workTokenizer;
		String tableAlias = "";
		String wrkStr;
		boolean duplicated = false;
		//
		if (jTextFieldName.getText().equals("") || jTextFieldName.getText().equals("N/A")) {
			JOptionPane.showMessageDialog(this, res.getString("ErrorMessage90"));
			jTextFieldID.requestFocus();
		} else {
			if (jTextFieldAlias.getText().equals("")) {
				jTextFieldAlias.setText("*TableID");
			}
			String firstChar = jTextFieldAlias.getText().substring(0, 1);
			if (jTextFieldAlias.getText().equals("") || firstChar.equals("*")) {
				jTextFieldAlias.setText("*TableID");
				tableAlias = "";
			} else {
				tableAlias = jTextFieldAlias.getText();
			}
			//
			String aliasError = getAliasError(tableAlias);
			if (!aliasError.equals("")) {
				JOptionPane.showMessageDialog(this, aliasError);
				jTextFieldAlias.requestFocus();
			} else {
				tableIDList.clear();
				fieldIDList.clear();
				dataSourceList.clear();
				//
				NodeList fieldList = objectTableElement_.getElementsByTagName("Field");
				for (int i = 0; i < fieldList.getLength(); i++) {
					workElement = (org.w3c.dom.Element)fieldList.item(i);
					tableIDList.add(objectTableElement_.getAttribute("ID"));
					fieldIDList.add(workElement.getAttribute("ID"));
					dataSourceList.add(objectTableElement_.getAttribute("ID") + "." + workElement.getAttribute("ID"));
				}
				//
				NodeList referList = objectTableElement_.getElementsByTagName("Refer");
				SortableDomElementListModel sortingList = frame_.getSortedListModel(referList, "Order");
				for (int i = 0; i < sortingList.getSize(); i++) {
					workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
					if (workElement.getAttribute("ToTable").equals(jTextFieldID.getText())) {
						if (workElement.getAttribute("TableAlias").equals(tableAlias)) {
							duplicated = true;
							break;
						}
					}
					lastOrder = Integer.parseInt(workElement.getAttribute("Order"));
					workTokenizer = new StringTokenizer(workElement.getAttribute("Fields"), ";");
					while (workTokenizer.hasMoreTokens()) {
						wrkStr = workTokenizer.nextToken();
						fieldIDList.add(wrkStr);
						tableIDList.add(workElement.getAttribute("ToTable"));
						if (workElement.getAttribute("TableAlias").equals("")) {
							dataSourceList.add(workElement.getAttribute("ToTable") + "." + wrkStr);
						} else {
							dataSourceList.add(workElement.getAttribute("TableAlias") + "." + wrkStr);
						}
					}
				}
				//
				if (duplicated) {
					JOptionPane.showMessageDialog(this, res.getString("ErrorMessage91"));
					if (jTextFieldAlias.getText().equals("*TableAlias")) {
						jTextFieldAlias.setText("");
					}
					jTextFieldAlias.requestFocus();
				} else {
					jTextFieldID.setEditable(false);
					jTextFieldID.setFocusable(false);
					jTextFieldAlias.setEditable(false);
					jTextFieldAlias.setFocusable(false);
					jComboBoxToKeyFieldsList.setEnabled(true);
					jComboBoxToKeyFieldsList.setFocusable(true);
					jComboBoxToKeyFieldsList.requestFocus();
					jTextFieldWithKeyFields.setEditable(true);
					jTextFieldWithKeyFields.setFocusable(true);
					jTextAreaMessage.setText(res.getString("AddJoinTableMessage2")+ jTextFieldID.getText() + res.getString("AddJoinTableMessage3") + objectTableElement_.getAttribute("ID") + res.getString("AddJoinTableMessage4"));
					jButtonNext.setVisible(false);
					jButtonOK.setVisible(true);
					jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
					fieldsList.clear();
					fieldsDefaultValuesList.clear();
					//
					String fieldIDOfObjectTable;
					NodeList keyList = referTableNode.getElement().getElementsByTagName("Key");
					for (int i = 0; i < keyList.getLength(); i++) {
						workElement = (org.w3c.dom.Element)keyList.item(i);
						if (workElement.getAttribute("Type").equals("PK") || workElement.getAttribute("Type").equals("SK")) {
							//
							fieldsList.add(workElement.getAttribute("Fields"));
							//
							StringBuffer buf = new StringBuffer();
							int count = 0;
							workTokenizer = new StringTokenizer(workElement.getAttribute("Fields"), ";");
							while (workTokenizer.hasMoreTokens()) {
								if (count > 0) {
									buf.append(";");
								}
								fieldIDOfObjectTable = getFieldIDWithTheSameDataTypeInTheTargetTable(referTableNode.getElement(), workTokenizer.nextToken(), objectTableElement_);
								buf.append(objectTableElement_.getAttribute("ID") + "." + fieldIDOfObjectTable);
								count++;
							}
							fieldsDefaultValuesList.add(buf.toString());
							//
							if (workElement.getAttribute("Type").equals("PK")) {
								jComboBoxToKeyFieldsList.addItem(res.getString("PKey") + "(" + workElement.getAttribute("Fields") + ")");
								jTextFieldWithKeyFields.requestFocus();
								jTextFieldWithKeyFields.setText(buf.toString());
							}
							//
							if (workElement.getAttribute("Type").equals("SK")) {
								jComboBoxToKeyFieldsList.addItem(res.getString("SKey") + "(" + workElement.getAttribute("Fields") + ")");
							}
						}
					}
				}
			}
		}
	}

	String getFieldIDWithTheSameDataTypeInTheTargetTable(org.w3c.dom.Element tableElement, String fieldID, org.w3c.dom.Element targetTableElement) {
		String fieldIDFound = "";
		org.w3c.dom.Element element, fieldElement = null;
		//
		NodeList fieldList = tableElement.getElementsByTagName("Field");
		for (int i = 0; i < fieldList.getLength(); i++) {
			element = (org.w3c.dom.Element)fieldList.item(i);
			if (element.getAttribute("ID").equals(fieldID)) {
				fieldElement = element;
				break;
			}
		}
		//
		if (fieldElement != null) {
			fieldList = targetTableElement.getElementsByTagName("Field");
			for (int i = 0; i < fieldList.getLength(); i++) {
				element = (org.w3c.dom.Element)fieldList.item(i);
				if (element.getAttribute("ID").equals(fieldID)) {
					fieldIDFound = fieldID;
					break;
				}
			}
			if (fieldIDFound.equals("")) {
				for (int i = 0; i < fieldList.getLength(); i++) {
					element = (org.w3c.dom.Element)fieldList.item(i);
					if (element.getAttribute("Type").equals(fieldElement.getAttribute("Type"))
							&& element.getAttribute("Size").equals(fieldElement.getAttribute("Size"))
							&& element.getAttribute("Decimal").equals(fieldElement.getAttribute("Decimal"))) {
						fieldIDFound = element.getAttribute("ID");
						break;
					}
				}
			}
		}
		//
		return fieldIDFound;
	}
	
	String getAliasError(String alias) {
		String message = "";
		if (!alias.equals(jTextFieldID.getText()) && !alias.equals("")) {
			MainTreeNode tableNode = frame_.getSpecificXETreeNode("Table", alias);
			if (tableNode != null) {
				message = res.getString("ErrorMessage118");
			}
		}
		if (message.equals("")) {
			if (objectTableElement_.getAttribute("ID").equals(jTextFieldID.getText()) && alias.equals("")) {
				message = res.getString("ErrorMessage132");
			}
		}
		return message;
	}
	
	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element elementTo, elementWith;
		StringTokenizer workTokenizerTo, workTokenizerWith;
		String fieldIDTo, dataSourceWith;
		int index;
		String errorMessage = "";
		int decimalTo, decimalWith;
		//
		workTokenizerTo = new StringTokenizer(fieldsList.get(jComboBoxToKeyFieldsList.getSelectedIndex()), ";");
		workTokenizerWith = new StringTokenizer(jTextFieldWithKeyFields.getText(), ";");
		if (workTokenizerTo.countTokens() == 0 || workTokenizerWith.countTokens() == 0) {
			errorMessage = res.getString("ErrorMessage92");
		} else {
			if (workTokenizerTo.countTokens() != workTokenizerWith.countTokens()) {
				errorMessage = res.getString("ErrorMessage93");
			} else {
				while (workTokenizerTo.hasMoreTokens()) {
					fieldIDTo = workTokenizerTo.nextToken();
					dataSourceWith = workTokenizerWith.nextToken();
					elementTo = frame_.getSpecificFieldElement(referTableNode.getElement().getAttribute("ID"), fieldIDTo);
					if (elementTo == null) {
						errorMessage = res.getString("ErrorMessage3") + fieldIDTo + res.getString("ErrorMessage4");
						break;
					} else {
						index = dataSourceList.indexOf(dataSourceWith);
						if (index == -1 ) {
							errorMessage = res.getString("ErrorMessage75") + dataSourceWith + res.getString("ErrorMessage76");
							break;
						} else {
							elementWith = frame_.getSpecificFieldElement(tableIDList.get(index), fieldIDList.get(index));
							decimalTo = 0;
							decimalWith = 0;
							if (!elementTo.getAttribute("Decimal").equals("")) {
								decimalTo = Integer.parseInt(elementTo.getAttribute("Decimal"));
							}
							if (!elementWith.getAttribute("Decimal").equals("")) {
								decimalWith = Integer.parseInt(elementWith.getAttribute("Decimal"));
							}
							if (!elementTo.getAttribute("Type").equals(elementWith.getAttribute("Type")) || !elementTo.getAttribute("Size").equals(elementWith.getAttribute("Size")) || decimalTo != decimalWith) {
								errorMessage = res.getString("ErrorMessage94");
								break;
							}
						}
					}
				}
			}
		}
		if (errorMessage.equals("")) {
			newElement = frame_.getDomDocument().createElement("Refer");
			lastOrder = lastOrder + 10;
			newElement.setAttribute("Order", Editor.getFormatted4ByteString(lastOrder));
			newElement.setAttribute("ToTable", referTableNode.getElement().getAttribute("ID"));
			if (jTextFieldAlias.getText().equals("*TableID")) {
				newElement.setAttribute("TableAlias", "");
			} else {
				newElement.setAttribute("TableAlias", jTextFieldAlias.getText());
			}
			newElement.setAttribute("ToKeyFields", fieldsList.get(jComboBoxToKeyFieldsList.getSelectedIndex()));
			newElement.setAttribute("WithKeyFields", jTextFieldWithKeyFields.getText());
			newElement.setAttribute("Fields", "");
			frame_.informationOnThisPageChanged = true;
			this.setVisible(false);
		} else {
			JOptionPane.showMessageDialog(this, errorMessage);
		}
	}

	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void jComboBoxToKeyFieldsList_actionPerformed(ActionEvent e) {
		if (jComboBoxToKeyFieldsList.getSelectedIndex() > -1) {
			jTextFieldWithKeyFields.setText(fieldsDefaultValuesList.get(jComboBoxToKeyFieldsList.getSelectedIndex()));
		}
	}

	void jTextFieldID_keyReleased(KeyEvent e) {
		referTableNode = null;
		jButtonNext.setEnabled(false);
		jTextFieldName.setText("");
		if (!jTextFieldID.getText().equals("")) {
			jTextFieldName.setText("N/A");
			referTableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText());
			if (referTableNode == null) {
				referTableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText().toUpperCase());
				if (referTableNode != null) {
					jButtonNext.setEnabled(true);
					jTextFieldName.setText(referTableNode.getElement().getAttribute("Name"));
					jTextFieldID.setText(jTextFieldID.getText().toUpperCase());
				}
			} else {
				jButtonNext.setEnabled(true);
				jTextFieldName.setText(referTableNode.getElement().getAttribute("Name"));
			}
		}
	}

	void jTextFieldAlias_keyReleased(KeyEvent e) {
		if (!jTextFieldAlias.getText().equals("")) {
			String wrkStr = jTextFieldAlias.getText().toUpperCase();
			String firstChar = wrkStr.substring(0, 1);
			String lastChar = wrkStr.substring(wrkStr.length()-1, wrkStr.length());
			if (!firstChar.equals("*")) {
				if (wrkStr.length() == 1) {
					if (!lastChar.matches("[a-zA-Z]+")) {
						JOptionPane.showMessageDialog(null, res.getString("ErrorMessage95"));
						jTextFieldAlias.setText(wrkStr.replace(lastChar, ""));
					} else {
						jTextFieldAlias.setText(wrkStr);
					}
				} else {
					if (!lastChar.matches("[0-9a-zA-Z]+")) {
						JOptionPane.showMessageDialog(null, res.getString("ErrorMessage96"));
						jTextFieldAlias.setText(wrkStr.replace(lastChar, ""));
					} else {
						jTextFieldAlias.setText(wrkStr);
					}
				}
			}
		}
	}
}

class DialogAddReferTable_jTextFieldID_keyAdapter extends java.awt.event.KeyAdapter {
	DialogAddReferTable adaptee;
	DialogAddReferTable_jTextFieldID_keyAdapter(DialogAddReferTable adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.jTextFieldID_keyReleased(e);
	}
}

class DialogAddReferTable_jTextFieldAlias_keyAdapter extends java.awt.event.KeyAdapter {
	DialogAddReferTable adaptee;
	DialogAddReferTable_jTextFieldAlias_keyAdapter(DialogAddReferTable adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.jTextFieldAlias_keyReleased(e);
	}
}

class DialogAddReferTable_jButtonNext_actionAdapter implements java.awt.event.ActionListener {
	DialogAddReferTable adaptee;
	DialogAddReferTable_jButtonNext_actionAdapter(DialogAddReferTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonNext_actionPerformed(e);
	}
}

class DialogAddReferTable_jButtonOK_actionAdapter implements java.awt.event.ActionListener {
	DialogAddReferTable adaptee;
	DialogAddReferTable_jButtonOK_actionAdapter(DialogAddReferTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonOK_actionPerformed(e);
	}
}

class DialogAddReferTable_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogAddReferTable adaptee;
	DialogAddReferTable_jButtonCancel_actionAdapter(DialogAddReferTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}

class DialogAddReferTable_jComboBoxToKeyFieldsList_actionAdapter implements java.awt.event.ActionListener {
	DialogAddReferTable adaptee;
	DialogAddReferTable_jComboBoxToKeyFieldsList_actionAdapter(DialogAddReferTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxToKeyFieldsList_actionPerformed(e);
	}
}

