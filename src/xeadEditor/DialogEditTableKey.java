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
import xeadEditor.Editor.MainTreeNode;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogEditTableKey extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel panelMain = new JPanel();
	private JButton jButtonOK = new JButton();
	private JButton jButtonCancel = new JButton();
	private Editor frame_;
	private org.w3c.dom.Element functionElement_;
	private JLabel jLabelID = new JLabel();
	private JLabel jLabelKeyFields = new JLabel();
	private JTextField jTextFieldID = new JTextField();
	private JTextField jTextFieldName = new JTextField();
	private JTextField jTextFieldKeyFields = new JTextField();
	private JPanel jPanelButtons = new JPanel();
	private MainTreeNode tableNode = null;
	private String originalTableID = "";
	private String primaryKey = "";
	private boolean isValidated = false;
	
	public DialogEditTableKey(Editor frame) {
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
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(new BorderLayout());

		jLabelID.setText(res.getString("TableID"));
		jLabelID.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelID.setBounds(new Rectangle(11, 12, 89, 15));
		jTextFieldID.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldID.setBounds(new Rectangle(105, 9, 70, 21));
		jTextFieldID.addKeyListener(new DialogEditTableKey_jTextFieldID_keyAdapter(this));
		jTextFieldName.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldName.setBounds(new Rectangle(180, 9, 305, 22));
		jTextFieldName.setEditable(false);
		jTextFieldName.setFocusable(false);
		jLabelKeyFields.setText(res.getString("TableKey"));
		jLabelKeyFields.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelKeyFields.setBounds(new Rectangle(11, 40, 89, 15));
		jTextFieldKeyFields.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldKeyFields.setBounds(new Rectangle(105, 37, 380, 21));
		panelMain.setLayout(null);
		panelMain.add(jLabelID);
		panelMain.add(jTextFieldID);
		panelMain.add(jTextFieldName);
		panelMain.add(jLabelKeyFields);
		panelMain.add(jTextFieldKeyFields);

		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(350, 43));
		jButtonOK.setBounds(new Rectangle(380, 10, 73, 25));
		jButtonOK.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new DialogEditTableKey_jButtonOK_actionAdapter(this));
		jButtonCancel.setBounds(new Rectangle(44, 10, 73, 25));
		jButtonCancel.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogEditTableKey_jButtonCancel_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonOK);
		jPanelButtons.add(jButtonCancel);

		this.setTitle(res.getString("EditTableKeyTitle"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(505, 143));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		this.pack();
	}

	public boolean request(org.w3c.dom.Element functionElement, String tableID, String tableKeys) {
		org.w3c.dom.Element workElement;

		isValidated = false;
		functionElement_ = functionElement;
		originalTableID = tableID;
		tableNode = frame_.getSpecificXETreeNode("Table", tableID);
		if (tableNode != null) {

	    	NodeList keyList = tableNode.getElement().getElementsByTagName("Key");
	    	for (int i = 0; i < keyList.getLength(); i++) {
	    		workElement = (org.w3c.dom.Element)keyList.item(i);
	    		if (workElement.getAttribute("Type").equals("PK")) {
	    			primaryKey = workElement.getAttribute("Fields");
	    			break;
	    		}
	    	}

			jTextFieldID.setText(tableID);
			jTextFieldID.requestFocus();
			jTextFieldName.setText("");
			jTextFieldName.setText(tableNode.getElement().getAttribute("Name"));
			if (tableKeys.equals("")) {
				jTextFieldKeyFields.setText(primaryKey);
			} else {
				jTextFieldKeyFields.setText(tableKeys);
			}

			Dimension dlgSize = this.getPreferredSize();
			Dimension frmSize = frame_.getSize();
			Point loc = frame_.getLocation();
			this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
			jPanelButtons.getRootPane().setDefaultButton(jButtonOK);

			super.setVisible(true);
		}

		return isValidated;
	}

	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element element;
		StringTokenizer workTokenizer;
		String dataSource;
		String errorMessage = "";
		if (!jTextFieldID.getText().equals(originalTableID)) {
			tableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText());
			errorMessage = validateNewTableID();
		}

		if (errorMessage.equals("")) {
			workTokenizer = new StringTokenizer(jTextFieldKeyFields.getText(), ";");
			if (workTokenizer.countTokens() == 0) {
				errorMessage = res.getString("ErrorMessage73");
			} else {
				while (workTokenizer.hasMoreTokens()) {
					dataSource = workTokenizer.nextToken();
					element = frame_.getSpecificFieldElement(tableNode.getElement().getAttribute("ID"), dataSource);
					if (element == null ) {
						errorMessage = res.getString("ErrorMessage75") + dataSource + res.getString("ErrorMessage76");
						break;
					}
				}
			}
		}

		if (errorMessage.equals("")) {
			isValidated = true;
			this.setVisible(false);
		} else {
			isValidated = false;
			JOptionPane.showMessageDialog(this, errorMessage);
		}
	}
	
	String validateNewTableID() {
		String message = "";
		int countOfFieldErrors = 0;
		org.w3c.dom.Element element;
		StringTokenizer tokenizer;
		StringBuffer buf = new StringBuffer();
		buf.append(res.getString("ErrorMessage119"));

		NodeList referList = tableNode.getElement().getElementsByTagName("Refer");

		if (functionElement_.getAttribute("Type").equals("XF100")
				|| functionElement_.getAttribute("Type").equals("XF110")) {
			String dataSource = functionElement_.getAttribute("OrderBy");
			if (dataSource.contains("(D)")) {
				dataSource = dataSource.replaceAll("(D)", "");
			}
			if (dataSource.contains("(A)")) {
				dataSource = dataSource.replaceAll("(A)", "");
			}
			tokenizer = new StringTokenizer(dataSource, ";");
			while (tokenizer.hasMoreTokens()) {
				if (isInvalidDataSourceName(tokenizer.nextToken(), buf, referList, res.getString("OrderByFields"))) {
					countOfFieldErrors++;
				}
			}
			NodeList fieldList = functionElement_.getElementsByTagName("Column");
			for (int i = 0; i < fieldList.getLength(); i++) {
				element = (org.w3c.dom.Element)fieldList.item(i);
				if (isInvalidFieldElement(element, buf, referList, res.getString("Columns"))) {
					countOfFieldErrors++;
				}
			}
			NodeList filterList = functionElement_.getElementsByTagName("Filter");
			for (int i = 0; i < filterList.getLength(); i++) {
				element = (org.w3c.dom.Element)filterList.item(i);
				if (isInvalidFieldElement(element, buf, referList, res.getString("Filters"))) {
					countOfFieldErrors++;
				}
			}
			if (functionElement_.getAttribute("Type").equals("XF110")) {
				String batchWithKeyDataSource = functionElement_.getAttribute("BatchWithKeyFields");
				tokenizer = new StringTokenizer(batchWithKeyDataSource, ";");
				while (tokenizer.hasMoreTokens()) {
					if (isInvalidDataSourceName(tokenizer.nextToken(), buf, referList, res.getString("BatchWithKeyField"))) {
						countOfFieldErrors++;
					}
				}
			}
		}

		if (functionElement_.getAttribute("Type").equals("XF200")
				|| functionElement_.getAttribute("Type").equals("XF300")
				|| functionElement_.getAttribute("Type").equals("XF310")) {
			NodeList fieldList = functionElement_.getElementsByTagName("Field");
			for (int i = 0; i < fieldList.getLength(); i++) {
				element = (org.w3c.dom.Element)fieldList.item(i);
				if (isInvalidFieldElement(element, buf, referList, res.getString("Fields"))) {
					countOfFieldErrors++;
				}
			}
		}

		if (countOfFieldErrors > 0) {
			buf.append(res.getString("ErrorMessage120"));
			message = buf.toString();
		} else {
			if (functionElement_.getAttribute("Type").equals("XF300")) {
				if (!functionElement_.getAttribute("StructureTable").equals("")) {
					message = res.getString("ErrorMessage123");
				}
			}
		}
		
		return message;
	}
	
	boolean isInvalidFieldElement(org.w3c.dom.Element functionFieldElement, StringBuffer buf, NodeList referList, String remarks) {
		String wrkStr, tableID, tableAlias, fieldID;
		int wrkInt;
		StringTokenizer tokenizer;
		org.w3c.dom.Element fieldElement, element;
		boolean isInvalid = false;
		
		wrkStr = functionFieldElement.getAttribute("DataSource");
		wrkInt = wrkStr.indexOf(".");
		tableAlias = wrkStr.substring(0, wrkInt);
		if (tableAlias.equals(originalTableID)) {
			tableAlias = jTextFieldID.getText();
			tableID = jTextFieldID.getText();
		} else {
			tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
		}
		fieldID = wrkStr.substring(wrkInt+1, wrkStr.length());

		fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
		if (fieldElement == null) {
			buf.append(tableAlias);
			buf.append(".");
			buf.append(fieldID);
			buf.append(" - ");
			buf.append(remarks);
			buf.append("\n");
			isInvalid = true;
		} else {
			if (!tableID.equals(jTextFieldID.getText())) {
				boolean isContainedInReferList = false;
				for (int i = 0; i < referList.getLength(); i++) {
					element = (org.w3c.dom.Element)referList.item(i);
					if (element.getAttribute("TableAlias").equals(tableAlias)
						|| (element.getAttribute("TableAlias").equals("") && element.getAttribute("ToTable").equals(tableAlias))) {
						tokenizer = new StringTokenizer(element.getAttribute("Fields"), ";");
						while (tokenizer.hasMoreTokens()) {
							if (tokenizer.nextToken().equals(fieldID)) {
								isContainedInReferList = true;
							}
						}
					}
				}
				if (!isContainedInReferList) {
					buf.append(tableAlias);
					buf.append(".");
					buf.append(fieldID);
					buf.append(" - ");
					buf.append(remarks);
					buf.append("\n");
					isInvalid = true;
				}
			}
		}

		return isInvalid;
	}
	
	boolean isInvalidDataSourceName(String dataSource, StringBuffer buf, NodeList referList, String remarks) {
		String tableID, tableAlias, fieldID;
		int wrkInt;
		StringTokenizer tokenizer;
		org.w3c.dom.Element fieldElement, element;
		boolean isInvalid = false;
		
		wrkInt = dataSource.indexOf(".");
		tableAlias = dataSource.substring(0, wrkInt);
		if (tableAlias.equals(originalTableID)) {
			tableAlias = jTextFieldID.getText();
			tableID = jTextFieldID.getText();
		} else {
			tableID = frame_.getTableIDOfTableAlias(tableAlias, referList, null);
		}
		fieldID = dataSource.substring(wrkInt+1, dataSource.length());
		fieldID = fieldID.replace("(D)", "");
		fieldID = fieldID.replace("(A)", "");

		fieldElement = frame_.getSpecificFieldElement(tableID, fieldID);
		if (fieldElement == null) {
			buf.append(tableAlias);
			buf.append(".");
			buf.append(fieldID);
			buf.append(" - ");
			buf.append(remarks);
			buf.append("\n");
			isInvalid = true;
		} else {
			if (!tableID.equals(jTextFieldID.getText())) {
				boolean isContainedInReferList = false;
				for (int i = 0; i < referList.getLength(); i++) {
					element = (org.w3c.dom.Element)referList.item(i);
					if (element.getAttribute("TableAlias").equals(tableAlias)
						|| (element.getAttribute("TableAlias").equals("") && element.getAttribute("ToTable").equals(tableAlias))) {
						tokenizer = new StringTokenizer(element.getAttribute("Fields"), ";");
						while (tokenizer.hasMoreTokens()) {
							if (tokenizer.nextToken().equals(fieldID)) {
								isContainedInReferList = true;
							}
						}

					}
				}
				if (!isContainedInReferList) {
					buf.append(tableAlias);
					buf.append(".");
					buf.append(fieldID);
					buf.append(" - ");
					buf.append(remarks);
					buf.append("\n");
					isInvalid = true;
				}
			}
		}

		return isInvalid;
	}

	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	
	public String getTableIDToBeChanged() {
		String id = "";
		if (!jTextFieldID.getText().equals(originalTableID)
				&& !jTextFieldID.getText().equals("")) {
			id = jTextFieldID.getText();
		}
		return id;
	}

	public String getValidatedKeys() {
		String keys = "";
		if (isValidated) {
			if (jTextFieldKeyFields.getText().equals("")) {
				keys = primaryKey;
			} else {
				keys = jTextFieldKeyFields.getText();
			}
		} 
		return keys;
	}

	public String getTableName() {
		return jTextFieldName.getText();
	}

	public String getTablePK() {
		return primaryKey;
	}

	void jTextFieldID_keyReleased(KeyEvent e) {
		jButtonOK.setEnabled(false);
		tableNode = null;
		jTextFieldName.setText("");
		if (!jTextFieldID.getText().equals("")) {
			jTextFieldName.setText("N/A");
			tableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText());
			if (tableNode == null) {
				tableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText().toUpperCase());
				if (tableNode != null) {
					jButtonOK.setEnabled(true);
					jTextFieldName.setText(tableNode.getElement().getAttribute("Name"));
					jTextFieldID.setText(jTextFieldID.getText().toUpperCase());
					this.getRootPane().setDefaultButton(jButtonOK);
				}
			} else {
				jButtonOK.setEnabled(true);
				jTextFieldName.setText(tableNode.getElement().getAttribute("Name"));
				this.getRootPane().setDefaultButton(jButtonOK);
			}
		}
	}
}

class DialogEditTableKey_jTextFieldID_keyAdapter extends java.awt.event.KeyAdapter {
	DialogEditTableKey adaptee;
	DialogEditTableKey_jTextFieldID_keyAdapter(DialogEditTableKey adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.jTextFieldID_keyReleased(e);
	}
}

class DialogEditTableKey_jButtonOK_actionAdapter implements java.awt.event.ActionListener {
	DialogEditTableKey adaptee;
	DialogEditTableKey_jButtonOK_actionAdapter(DialogEditTableKey adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonOK_actionPerformed(e);
	}
}

class DialogEditTableKey_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogEditTableKey adaptee;
	DialogEditTableKey_jButtonCancel_actionAdapter(DialogEditTableKey adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}
