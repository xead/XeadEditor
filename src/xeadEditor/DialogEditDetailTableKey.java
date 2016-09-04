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
import xeadEditor.Editor.MainTreeNode;
import xeadEditor.Editor.SortableDomElementListModel;
import java.awt.event.*;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogEditDetailTableKey extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel panelMain = new JPanel();
	private JButton jButtonOK = new JButton();
	private JButton jButtonCancel = new JButton();
	private Editor frame_;
	private org.w3c.dom.Element functionElement_;
	private int tabIndex_;
	private JLabel jLabelID = new JLabel();
	private JLabel jLabelHdrKeyFields = new JLabel();
	private JLabel jLabelDtlKeyFields = new JLabel();
	private JTextField jTextFieldID = new JTextField();
	private JTextField jTextFieldName = new JTextField();
	private JTextField jTextFieldHdrKeyFields = new JTextField();
	private JTextField jTextFieldDtlKeyFields = new JTextField();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private JPanel jPanelButtons = new JPanel();
	private MainTreeNode headerTableNode = null;
	private MainTreeNode detailTableNode = null;
	private String originalDetailTableID = "";
	private String headerPK = "";
	private String detailPK = "";
	private boolean isValidated = false;
	private SortableDomElementListModel sortingList;
	
	public DialogEditDetailTableKey(Editor frame) {
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

		jLabelID.setText(res.getString("DTLTableID"));
		jLabelID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelID.setBounds(new Rectangle(5, 12, 130, 20));
		jTextFieldID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldID.setBounds(new Rectangle(140, 9, 120, 25));
		jTextFieldID.addKeyListener(new DialogEditDetailTableKey_jTextFieldID_keyAdapter(this));
		jTextFieldName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName.setBounds(new Rectangle(265, 9, 450, 25));
		jTextFieldName.setEditable(false);
		jTextFieldName.setFocusable(false);
		
		jLabelHdrKeyFields.setText(res.getString("HDRTableKeys"));
		jLabelHdrKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelHdrKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelHdrKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelHdrKeyFields.setBounds(new Rectangle(5, 43, 130, 20));
		jTextFieldHdrKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldHdrKeyFields.setBounds(new Rectangle(140, 40, 575, 25));

		jLabelDtlKeyFields.setText(res.getString("DTLTableKeys"));
		jLabelDtlKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelDtlKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelDtlKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelDtlKeyFields.setBounds(new Rectangle(5, 74, 130, 20));
		jTextFieldDtlKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldDtlKeyFields.setBounds(new Rectangle(140, 71, 575, 25));

		jScrollPaneMessage.setBounds(new Rectangle(10, 102, 705, 60));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setFocusable(false);

		panelMain.setBorder(null);
		panelMain.setLayout(null);
		panelMain.add(jLabelID);
		panelMain.add(jTextFieldID);
		panelMain.add(jTextFieldName);
		panelMain.add(jLabelHdrKeyFields);
		panelMain.add(jTextFieldHdrKeyFields);
		panelMain.add(jLabelDtlKeyFields);
		panelMain.add(jTextFieldDtlKeyFields);
		panelMain.add(jScrollPaneMessage);

		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(350, 43));
		jButtonCancel.setBounds(new Rectangle(40, 8, 100, 27));
		jButtonCancel.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogEditDetailTableKey_jButtonCancel_actionAdapter(this));
		jButtonOK.setBounds(new Rectangle(585, 8, 100, 27));
		jButtonOK.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new DialogEditDetailTableKey_jButtonOK_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonOK);
		jPanelButtons.add(jButtonCancel);

		this.setTitle(res.getString("EditDetailTableKeyTitle"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(730, 252));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		this.pack();
	}

	public boolean request(org.w3c.dom.Element functionElement, int tabIndex, String detailTableID, String headerKeys, String detailKeys) {
		org.w3c.dom.Element workElement;

		isValidated = false;
		functionElement_ = functionElement;
		tabIndex_ = tabIndex;
		originalDetailTableID = detailTableID;
		headerTableNode = frame_.getSpecificXETreeNode("Table", functionElement_.getAttribute("HeaderTable"));
		detailTableNode = frame_.getSpecificXETreeNode("Table", detailTableID);
		if (headerTableNode != null && detailTableNode != null) {

	    	NodeList keyList = headerTableNode.getElement().getElementsByTagName("Key");
	    	for (int i = 0; i < keyList.getLength(); i++) {
	    		workElement = (org.w3c.dom.Element)keyList.item(i);
	    		if (workElement.getAttribute("Type").equals("PK")) {
	    			headerPK = workElement.getAttribute("Fields");
	    			break;
	    		}
	    	}
	    	keyList = detailTableNode.getElement().getElementsByTagName("Key");
	    	for (int i = 0; i < keyList.getLength(); i++) {
	    		workElement = (org.w3c.dom.Element)keyList.item(i);
	    		if (workElement.getAttribute("Type").equals("PK")) {
	    			detailPK = workElement.getAttribute("Fields");
	    			break;
	    		}
	    	}

			jTextFieldID.setText(detailTableID);
			jTextFieldID.requestFocus();
			jTextFieldName.setText("");
			jTextFieldName.setText(detailTableNode.getElement().getAttribute("Name"));
			if (headerKeys.equals("")) {
				jTextFieldHdrKeyFields.setText(headerPK);
			} else {
				jTextFieldHdrKeyFields.setText(headerKeys);
			}
			if (functionElement_.getAttribute("Type").equals("XF300") || functionElement_.getAttribute("Type").equals("XF390")) {
				jTextFieldHdrKeyFields.setEditable(true);
			} else {
				jTextFieldHdrKeyFields.setEditable(false);
			}
			if (detailKeys.equals("")) {
				jTextFieldDtlKeyFields.setText(detailPK);
			} else {
				jTextFieldDtlKeyFields.setText(detailKeys);
			}
			jTextAreaMessage.setText(res.getString("EditDetailTableKeyComment"));

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
		org.w3c.dom.Element elementHdr, elementDtl;
		StringTokenizer workTokenizerHdr, workTokenizerDtl;
		String dataSourceHdr, dataSourceDtl;
		String errorMessage = "";

		if (!jTextFieldID.getText().equals(originalDetailTableID)) {
			detailTableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText());
			errorMessage = validateNewTableID();
		}

		if (errorMessage.equals("")) {
			workTokenizerHdr = new StringTokenizer(jTextFieldHdrKeyFields.getText(), ";");
			if (workTokenizerHdr.countTokens() == 0) {
				errorMessage = res.getString("ErrorMessage106");
			} else {
				while (workTokenizerHdr.hasMoreTokens()) {
					dataSourceHdr = workTokenizerHdr.nextToken();
					elementHdr = frame_.getSpecificFieldElement(headerTableNode.getElement().getAttribute("ID"), dataSourceHdr);
					if (elementHdr == null ) {
						errorMessage = res.getString("ErrorMessage75") + dataSourceHdr + res.getString("ErrorMessage76");
						break;
					}
				}
			}

			workTokenizerHdr = new StringTokenizer(jTextFieldHdrKeyFields.getText(), ";");
			workTokenizerDtl = new StringTokenizer(jTextFieldDtlKeyFields.getText(), ";");
			if (workTokenizerDtl.countTokens() == 0) {
				errorMessage = res.getString("ErrorMessage73");
			} else {
				if (workTokenizerHdr.countTokens() >= workTokenizerDtl.countTokens()) {
					errorMessage = res.getString("ErrorMessage74");
				} else {
					int count = 0;
					while (workTokenizerDtl.hasMoreTokens()) {
						count++;
						dataSourceDtl = workTokenizerDtl.nextToken();
						elementDtl = frame_.getSpecificFieldElement(detailTableNode.getElement().getAttribute("ID"), dataSourceDtl);
						if (elementDtl == null ) {
							errorMessage = res.getString("ErrorMessage75") + dataSourceDtl + res.getString("ErrorMessage76");
							break;
						} else {
							if (count <= workTokenizerHdr.countTokens()) {
								dataSourceHdr = workTokenizerHdr.nextToken();
								elementHdr = frame_.getSpecificFieldElement(headerTableNode.getElement().getAttribute("ID"), dataSourceHdr);
								if (!elementHdr.getAttribute("Type").equals(elementDtl.getAttribute("Type")) || !elementHdr.getAttribute("Size").equals(elementDtl.getAttribute("Size")) || !elementHdr.getAttribute("Decimal").equals(elementDtl.getAttribute("Decimal"))) {
									errorMessage = res.getString("ErrorMessage77");
									break;
								}
							}
						}
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
		String dataSource;
		org.w3c.dom.Element element1, element2;
		NodeList detailList, detailFieldList, detailFilterList;
		StringTokenizer tokenizer;

		StringBuffer buf = new StringBuffer();
		buf.append(res.getString("ErrorMessage119"));

		NodeList headerReferList = headerTableNode.getElement().getElementsByTagName("Refer");
		NodeList detailReferList = detailTableNode.getElement().getElementsByTagName("Refer");
		
		if (functionElement_.getAttribute("Type").equals("XF300")) {
			detailList = functionElement_.getElementsByTagName("Detail");
			sortingList = frame_.getSortedListModel(detailList, "Order");
		    for (int i = 0; i < sortingList.getSize(); i++) {
		    	if (i == tabIndex_) {
			        element1 = (org.w3c.dom.Element)sortingList.getElementAt(i);
					dataSource = element1.getAttribute("OrderBy");
					tokenizer = new StringTokenizer(dataSource, ";");
					while (tokenizer.hasMoreTokens()) {
						if (isInvalidDataSourceName(tokenizer.nextToken(), buf, detailReferList, headerReferList, res.getString("OrderByFields"))) {
							countOfFieldErrors++;
						}
					}
			        detailFieldList = element1.getElementsByTagName("Column");
					for (int j = 0; j < detailFieldList.getLength(); j++) {
						element2 = (org.w3c.dom.Element)detailFieldList.item(j);
						if (isInvalidFieldElement(element2, buf, detailReferList, headerReferList, res.getString("Columns"))) {
							countOfFieldErrors++;
						}
					}
					detailFilterList = element1.getElementsByTagName("Filter");
					for (int j = 0; j < detailFilterList.getLength(); j++) {
						element2 = (org.w3c.dom.Element)detailFilterList.item(j);
						if (isInvalidFieldElement(element2, buf, detailReferList, headerReferList, res.getString("Filters"))) {
							countOfFieldErrors++;
						}
					}
		    	}
		    }
		}
	
		if (functionElement_.getAttribute("Type").equals("XF310")) {
			dataSource = functionElement_.getAttribute("DetailOrderBy");
			tokenizer = new StringTokenizer(dataSource, ";");
			while (tokenizer.hasMoreTokens()) {
				if (isInvalidDataSourceName(tokenizer.nextToken(), buf, detailReferList, headerReferList, res.getString("OrderByFields"))) {
					countOfFieldErrors++;
				}
			}
			detailFieldList = functionElement_.getElementsByTagName("Column");
			for (int i = 0; i < detailFieldList.getLength(); i++) {
				element2 = (org.w3c.dom.Element)detailFieldList.item(i);
				if (isInvalidFieldElement(element2, buf, detailReferList, headerReferList, res.getString("Columns"))) {
					countOfFieldErrors++;
				}
			}
//			dataSource = functionElement_.getAttribute("AddRowListReturnToDetailDataSources");
//			tokenizer = new StringTokenizer(dataSource, ";");
//			while (tokenizer.hasMoreTokens()) {
//				if (isInvalidDataSourceName(tokenizer.nextToken(), buf, detailReferList, headerReferList, res.getString("AddRowListReturnToField"))) {
//					countOfFieldErrors++;
//				}
//			}
		}
		
		if (functionElement_.getAttribute("Type").equals("XF390")) {
			dataSource = functionElement_.getAttribute("DetailOrderBy");
			tokenizer = new StringTokenizer(dataSource, ";");
			while (tokenizer.hasMoreTokens()) {
				if (isInvalidDataSourceName(tokenizer.nextToken(), buf, detailReferList, headerReferList, res.getString("OrderByFields"))) {
					countOfFieldErrors++;
				}
			}
			detailFieldList = functionElement_.getElementsByTagName("Column");
			for (int i = 0; i < detailFieldList.getLength(); i++) {
				element2 = (org.w3c.dom.Element)detailFieldList.item(i);
				if (isInvalidFieldElement(element2, buf, detailReferList, headerReferList, res.getString("Columns"))) {
					countOfFieldErrors++;
				}
			}
		}

		if (countOfFieldErrors > 0) {
			buf.append(res.getString("ErrorMessage120"));
			message = buf.toString();
		}
		
		return message;
	}
	
	boolean isInvalidFieldElement(org.w3c.dom.Element functionFieldElement, StringBuffer buf, NodeList detailReferList, NodeList headerReferList, String remarks) {
		String wrkStr, tableID, tableAlias, fieldID;
		int wrkInt;
		StringTokenizer tokenizer;
		org.w3c.dom.Element fieldElement, element;
		boolean isInvalid = false;
		
		wrkStr = functionFieldElement.getAttribute("DataSource");
		wrkInt = wrkStr.indexOf(".");
		tableAlias = wrkStr.substring(0, wrkInt);
		if (tableAlias.equals(originalDetailTableID)) {
			tableAlias = jTextFieldID.getText();
			tableID = jTextFieldID.getText();
		} else {
			tableID = frame_.getTableIDOfTableAlias(tableAlias, detailReferList, headerReferList);
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
				for (int i = 0; i < headerReferList.getLength(); i++) {
					element = (org.w3c.dom.Element)headerReferList.item(i);
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
				for (int i = 0; i < detailReferList.getLength(); i++) {
					element = (org.w3c.dom.Element)detailReferList.item(i);
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
	
	boolean isInvalidDataSourceName(String dataSource, StringBuffer buf, NodeList detailReferList, NodeList headerReferList, String remarks) {
		String tableID, tableAlias, fieldID;
		int wrkInt;
		StringTokenizer tokenizer;
		org.w3c.dom.Element fieldElement, element;
		boolean isInvalid = false;
		
		wrkInt = dataSource.indexOf(".");
		tableAlias = dataSource.substring(0, wrkInt);
		if (tableAlias.equals(originalDetailTableID)) {
			tableAlias = jTextFieldID.getText();
			tableID = jTextFieldID.getText();
		} else {
			tableID = frame_.getTableIDOfTableAlias(tableAlias, detailReferList, headerReferList);
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
				for (int i = 0; i < headerReferList.getLength(); i++) {
					element = (org.w3c.dom.Element)headerReferList.item(i);
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
				for (int i = 0; i < detailReferList.getLength(); i++) {
					element = (org.w3c.dom.Element)detailReferList.item(i);
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
	
	public String getDetailTableIDToBeChanged() {
		String id = "";
		if (!jTextFieldID.getText().equals(originalDetailTableID)
				&& !jTextFieldID.getText().equals("")) {
			id = jTextFieldID.getText();
		}
		return id;
	}

	public String getValidatedHeaderKeys() {
		String keys = "";
		if (isValidated) {
			if (jTextFieldHdrKeyFields.getText().equals("")) {
				keys = headerPK;
			} else {
				keys = jTextFieldHdrKeyFields.getText();
			}
		} 
		return keys;
	}

	public String getValidatedDetailKeys() {
		String keys = "";
		if (isValidated) {
			if (jTextFieldDtlKeyFields.getText().equals("")) {
				keys = detailPK;
			} else {
				keys = jTextFieldDtlKeyFields.getText();
			}
		} 
		return keys;
	}

	public String getDetailTableName() {
		return jTextFieldName.getText();
	}

	public String getDetailTablePK() {
		return detailPK;
	}

	void jTextFieldID_keyReleased(KeyEvent e) {
		jButtonOK.setEnabled(false);
		detailTableNode = null;
		jTextFieldName.setText("");
		if (!jTextFieldID.getText().equals("")) {
			jTextFieldName.setText("N/A");
			detailTableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText());
			if (detailTableNode == null) {
				detailTableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText().toUpperCase());
				if (detailTableNode != null) {
					jButtonOK.setEnabled(true);
					jTextFieldName.setText(detailTableNode.getElement().getAttribute("Name"));
					jTextFieldID.setText(jTextFieldID.getText().toUpperCase());
					this.getRootPane().setDefaultButton(jButtonOK);
				}
			} else {
				jButtonOK.setEnabled(true);
				jTextFieldName.setText(detailTableNode.getElement().getAttribute("Name"));
				this.getRootPane().setDefaultButton(jButtonOK);
			}
		}
	}
}

class DialogEditDetailTableKey_jTextFieldID_keyAdapter extends java.awt.event.KeyAdapter {
	DialogEditDetailTableKey adaptee;
	DialogEditDetailTableKey_jTextFieldID_keyAdapter(DialogEditDetailTableKey adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.jTextFieldID_keyReleased(e);
	}
}

class DialogEditDetailTableKey_jButtonOK_actionAdapter implements java.awt.event.ActionListener {
	DialogEditDetailTableKey adaptee;
	DialogEditDetailTableKey_jButtonOK_actionAdapter(DialogEditDetailTableKey adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonOK_actionPerformed(e);
	}
}

class DialogEditDetailTableKey_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogEditDetailTableKey adaptee;
	DialogEditDetailTableKey_jButtonCancel_actionAdapter(DialogEditDetailTableKey adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}
