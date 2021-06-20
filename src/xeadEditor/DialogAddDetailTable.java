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

import xeadEditor.Editor.SortableDomElementListModel;
import xeadEditor.Editor.MainTreeNode;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogAddDetailTable extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel panelMain = new JPanel();
	private JButton jButtonOK = new JButton();
	private JButton jButtonCancel = new JButton();
	private Editor frame_;
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
	private org.w3c.dom.Element objectFunctionElement_ = null;
	private org.w3c.dom.Element newElement = null;
	private int lastOrder = 0;
	private SortableDomElementListModel sortingList;
	private String headerPK = "";
	private String detailPK = "";
	private ArrayList<String> tableIDList = new ArrayList<String>();
	private ArrayList<String> tableNameList = new ArrayList<String>();
	private DialogAssistList dialogAssistList;
	private Action actionListTables = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			listTables();
		}
	};
	
	public DialogAddDetailTable(Editor frame) {
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
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(new BorderLayout());
		//
		jLabelID.setText(res.getString("DTLTableID"));
		jLabelID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelID.setBounds(new Rectangle(5, 12, 130, 24));
		jTextFieldID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldID.setBounds(new Rectangle(140, 9, 120, 29));
		jTextFieldID.addKeyListener(new DialogAddDetailTable_jTextFieldID_keyAdapter(this));
		InputMap inputMap1 = jTextFieldID.getInputMap(JTextField.WHEN_FOCUSED);
		inputMap1.clear();
		ActionMap actionMap1 = jTextFieldID.getActionMap();
		actionMap1.clear();
		inputMap1.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK), "LIST");
		actionMap1.put("LIST", actionListTables);
		jTextFieldName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName.setBounds(new Rectangle(265, 9, 350, 29));
		jTextFieldName.setEditable(false);
		jTextFieldName.setFocusable(false);
		jLabelHdrKeyFields.setText(res.getString("HDRTableKeys"));
		jLabelHdrKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelHdrKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelHdrKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelHdrKeyFields.setBounds(new Rectangle(5, 47, 130, 24));
		jTextFieldHdrKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldHdrKeyFields.setBounds(new Rectangle(140, 44, 475, 29));
		jLabelDtlKeyFields.setText(res.getString("DTLTableKeys"));
		jLabelDtlKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelDtlKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelDtlKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelDtlKeyFields.setBounds(new Rectangle(5, 80, 130, 24));
		jTextFieldDtlKeyFields.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldDtlKeyFields.setBounds(new Rectangle(140, 77, 475, 29));
		jScrollPaneMessage.setBounds(new Rectangle(620, 9, 360, 96));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setFocusable(false);
		panelMain.setLayout(null);
		panelMain.add(jLabelID);
		panelMain.add(jTextFieldID);
		panelMain.add(jTextFieldName);
		panelMain.add(jLabelHdrKeyFields);
		panelMain.add(jTextFieldHdrKeyFields);
		panelMain.add(jLabelDtlKeyFields);
		panelMain.add(jTextFieldDtlKeyFields);
		panelMain.add(jScrollPaneMessage);
		//
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(100, 47));
		jButtonOK.setBounds(new Rectangle(850, 8, 100, 29));
		jButtonOK.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new DialogAddDetailTable_jButtonOK_actionAdapter(this));
		jButtonCancel.setBounds(new Rectangle(50, 8, 100, 29));
		jButtonCancel.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogAddDetailTable_jButtonCancel_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonOK);
		jPanelButtons.add(jButtonCancel);
		//
		dialogAssistList = new DialogAssistList(frame_, this, true);
		//
		this.setTitle(res.getString("AddTabDialogTitle"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(995, 202));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		this.pack();
	}

	public org.w3c.dom.Element request(org.w3c.dom.Element objectFunctionElement) {
		org.w3c.dom.Element workElement;
		//
		newElement = null;
		objectFunctionElement_ = objectFunctionElement;
		//
		jTextFieldID.setText("");
		jTextFieldID.setEditable(true);
		jTextFieldID.setFocusable(true);
		jTextFieldID.requestFocus();
		jTextFieldName.setText("(Ctrl+Space to list)");
		jTextFieldDtlKeyFields.setText("");
		jTextFieldDtlKeyFields.setEditable(false);
		jTextFieldDtlKeyFields.setFocusable(false);
		jTextAreaMessage.setText(res.getString("AddTabDialogComment"));
		jButtonOK.setEnabled(false);
		//
		org.w3c.dom.Element element;
		tableIDList.clear();
		tableNameList.clear();
		NodeList xmlnodelist1 = frame_.getDomDocument().getElementsByTagName("Table");
		sortingList = frame_.getSortedListModel(xmlnodelist1, "ID");
		for (int i = 0; i < sortingList.getSize(); i++) {
	    	element = (org.w3c.dom.Element)sortingList.getElementAt(i);
	    	tableIDList.add(element.getAttribute("ID"));
	    	tableNameList.add(element.getAttribute("Name"));
		}
	    //
    	headerTableNode = frame_.getSpecificXETreeNode("Table", objectFunctionElement_.getAttribute("HeaderTable"));
    	NodeList keyList = headerTableNode.getElement().getElementsByTagName("Key");
    	for (int i = 0; i < keyList.getLength(); i++) {
    		workElement = (org.w3c.dom.Element)keyList.item(i);
    		if (workElement.getAttribute("Type").equals("PK")) {
    			headerPK = workElement.getAttribute("Fields");
    			break;
    		}
    	}
	    if (objectFunctionElement_.getAttribute("HeaderKeyFields").equals("")) {
			jTextFieldHdrKeyFields.setText(headerPK);
	    } else {
			jTextFieldHdrKeyFields.setText(objectFunctionElement_.getAttribute("HeaderKeyFields"));
	    }
		//
		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = frame_.getSize();
		Point loc = frame_.getLocation();
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		super.setVisible(true);
		//
		return newElement;
	}

	void listTables() {
		String selectedValue = dialogAssistList.listIDs(jTextFieldID.getText(), tableIDList, tableNameList, jTextFieldName);
		if (selectedValue.contains(" - ")) {
			StringTokenizer tokenizer = new StringTokenizer(selectedValue, " - ");
			jTextFieldID.setText(tokenizer.nextToken());
			jTextFieldID_keyReleased(null);					
		}
	}

	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element elementHdr, elementDtl, childElement, workElement;
		StringTokenizer workTokenizerHdr, workTokenizerDtl;
		String dataSourceHdr, dataSourceDtl;
		String errorMessage = "";

		NodeList detailList = objectFunctionElement_.getElementsByTagName("Detail");
		sortingList = frame_.getSortedListModel(detailList, "Order");
	    for (int i = 0; i < sortingList.getSize(); i++) {
			workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
			lastOrder = Integer.parseInt(workElement.getAttribute("Order"));
		}

		workTokenizerHdr = new StringTokenizer(jTextFieldHdrKeyFields.getText(), ";");
		int countOfHeaderKeyFields = workTokenizerHdr.countTokens();
		if (countOfHeaderKeyFields == 0) {
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

		if (errorMessage.equals("")) {
			if (objectFunctionElement_.getAttribute("Type").equals("XF300") && sortingList.getSize() >= 20) {
				errorMessage = res.getString("ErrorMessage130");
			}
		}

		if (errorMessage.equals("")) {
			workTokenizerDtl = new StringTokenizer(jTextFieldDtlKeyFields.getText(), ";");
			if (workTokenizerDtl.countTokens() == 0) {
				errorMessage = res.getString("ErrorMessage73");
			} else {
				if (countOfHeaderKeyFields >= workTokenizerDtl.countTokens()) {
					errorMessage = res.getString("ErrorMessage74");
				} else {
					ArrayList<String> fieldIDList = new ArrayList<String>();
					while (workTokenizerDtl.hasMoreTokens()) {
						dataSourceDtl = workTokenizerDtl.nextToken();
						if (fieldIDList.contains(dataSourceDtl)) {
							errorMessage = res.getString("ErrorMessage77");
						} else {
							fieldIDList.add(dataSourceDtl);
						}
						elementDtl = frame_.getSpecificFieldElement(detailTableNode.getElement().getAttribute("ID"), dataSourceDtl);
						if (elementDtl == null ) {
							errorMessage = res.getString("ErrorMessage75") + dataSourceDtl + res.getString("ErrorMessage76");
							break;
						}
					}
					if (errorMessage.equals("")) {
						workTokenizerHdr = new StringTokenizer(jTextFieldHdrKeyFields.getText(), ";");
						workTokenizerDtl = new StringTokenizer(jTextFieldDtlKeyFields.getText(), ";");
						while (workTokenizerHdr.hasMoreTokens()) {
							dataSourceHdr = workTokenizerHdr.nextToken();
							dataSourceDtl = workTokenizerDtl.nextToken();
							elementHdr = frame_.getSpecificFieldElement(headerTableNode.getElement().getAttribute("ID"), dataSourceHdr);
							elementDtl = frame_.getSpecificFieldElement(detailTableNode.getElement().getAttribute("ID"), dataSourceDtl);
							if (elementDtl == null ) {
								errorMessage = res.getString("ErrorMessage75") + dataSourceDtl + res.getString("ErrorMessage76");
								break;
							} else {
								if (!elementHdr.getAttribute("Type").equals(elementDtl.getAttribute("Type")) || !elementHdr.getAttribute("Size").equals(elementDtl.getAttribute("Size")) || !elementHdr.getAttribute("Decimal").equals(elementDtl.getAttribute("Decimal"))) {
									errorMessage = res.getString("ErrorMessage77") + "\n"
											+ elementHdr.getAttribute("ID") + "(" + elementHdr.getAttribute("Type") + elementHdr.getAttribute("Size") + ")"
											+ ":" + elementDtl.getAttribute("ID") + "(" + elementDtl.getAttribute("Type") + elementDtl.getAttribute("Size") + ")";
									break;
								}
							}
						}
					}
				}
			}
		}

		if (errorMessage.equals("")) {
			newElement = frame_.getDomDocument().createElement("Detail");
			lastOrder = lastOrder + 10;
			newElement.setAttribute("Order", Editor.getFormatted4ByteString(lastOrder));
			newElement.setAttribute("Table", detailTableNode.getElement().getAttribute("ID"));
			if (jTextFieldHdrKeyFields.getText().equals(headerPK)) {
				newElement.setAttribute("HeaderKeyFields", "");
			} else {
				newElement.setAttribute("HeaderKeyFields", jTextFieldHdrKeyFields.getText());
			}
			if (jTextFieldDtlKeyFields.getText().equals(detailPK)) {
				newElement.setAttribute("KeyFields", "");
			} else {
				newElement.setAttribute("KeyFields", jTextFieldDtlKeyFields.getText());
			}
			newElement.setAttribute("Caption", detailTableNode.getElement().getAttribute("Name"));

			boolean isNotHeaderKey;
			org.w3c.dom.Element workElement1, workElement2;
			String headerTableKeys = "";
			if (!jTextFieldHdrKeyFields.getText().equals(headerPK)) {
				headerTableKeys = jTextFieldHdrKeyFields.getText();
			}
			String detailTableKeys = "";
			if (!jTextFieldDtlKeyFields.getText().equals(detailPK)) {
				detailTableKeys = jTextFieldDtlKeyFields.getText();
			}
			String workHeaderTableKeys = headerTableKeys;
			if (workHeaderTableKeys.equals("")) {
				workElement1 = frame_.getSpecificPKElement(objectFunctionElement_.getAttribute("HeaderTable"));
				workHeaderTableKeys = workElement1.getAttribute("Fields");
			}
			String workDetailTableKeys = detailTableKeys;
			if (workDetailTableKeys.equals("")) {
				workElement2 = frame_.getSpecificPKElement(detailTableNode.getElement().getAttribute("ID"));
				workDetailTableKeys = workElement2.getAttribute("Fields");
			}
			ArrayList<String> headerKeyList = new ArrayList<String>();
			StringTokenizer workTokenizer = new StringTokenizer(workHeaderTableKeys, ";" );
			while (workTokenizer.hasMoreTokens()) {
				headerKeyList.add(workTokenizer.nextToken());
			}
			ArrayList<String> detailKeyList = new ArrayList<String>();
			workTokenizer = new StringTokenizer(workDetailTableKeys, ";" );
			while (workTokenizer.hasMoreTokens()) {
				detailKeyList.add(workTokenizer.nextToken());
			}

			if (objectFunctionElement_.getAttribute("Type").equals("XF300")) {

				newElement.setAttribute("DetailFunction", "NONE");
				newElement.setAttribute("InitialListing", "T");
				newElement.setAttribute("InitialMsg", "");

				int columnCount = 0;
				NodeList nodeList = detailTableNode.getElement().getElementsByTagName("Field");
				sortingList = frame_.getSortedListModel(nodeList, "Order");
				for (int i = 0; i < sortingList.getSize(); i++) {
					if (columnCount < 5) {
						workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
						isNotHeaderKey = true;
						for (int j = 0; j < headerKeyList.size(); j++) {
							if (workElement.getAttribute("ID").equals(detailKeyList.get(j))) {
								isNotHeaderKey = false;
								break;
							}
						}
						if (isNotHeaderKey) {
							childElement = frame_.getDomDocument().createElement("Column");
							childElement.setAttribute("Order", "00" + Integer.toString(i + 1) + "0");
							childElement.setAttribute("DataSource", detailTableNode.getElement().getAttribute("ID") + "." + workElement.getAttribute("ID")); 
							childElement.setAttribute("FieldOptions", "");
							newElement.appendChild(childElement);
							columnCount++;
						}
					} else {
						break;
					}
				}

				childElement = frame_.getDomDocument().createElement("Button");
				childElement.setAttribute("Position", "0");
				childElement.setAttribute("Number", "3");
				childElement.setAttribute("Caption", res.getString("Close"));
				childElement.setAttribute("Action", "EXIT");
				newElement.appendChild(childElement);
				childElement = frame_.getDomDocument().createElement("Button");
				childElement.setAttribute("Position", "2");
				childElement.setAttribute("Number", "6");
				childElement.setAttribute("Caption", res.getString("Add"));
				childElement.setAttribute("Action", "ADD");
				newElement.appendChild(childElement);
				childElement = frame_.getDomDocument().createElement("Button");
				childElement.setAttribute("Position", "4");
				childElement.setAttribute("Number", "8");
				childElement.setAttribute("Caption", res.getString("HDRData"));
				childElement.setAttribute("Action", "HEADER");
				newElement.appendChild(childElement);
				childElement = frame_.getDomDocument().createElement("Button");
				childElement.setAttribute("Position", "6");
				childElement.setAttribute("Number", "12");
				childElement.setAttribute("Caption", res.getString("Output"));
				childElement.setAttribute("Action", "OUTPUT");
				newElement.appendChild(childElement);
				frame_.informationOnThisPageChanged = true;
			}
			
			if (objectFunctionElement_.getAttribute("Type").equals("XF390")) {

				//Get Default Font ID//
				org.w3c.dom.Element fontElement;
				String defaultFontID = "";
				NodeList fontList = frame_.getDomDocument().getElementsByTagName("PrintFont");
				sortingList = frame_.getSortedListModel(fontList, "FontName");
				fontElement = (org.w3c.dom.Element)sortingList.getElementAt(0);
				defaultFontID = fontElement.getAttribute("ID");

				newElement.setAttribute("CaptionFontSize", "12");
				newElement.setAttribute("CaptionFontStyle", "");
				newElement.setAttribute("FontID", defaultFontID);
				newElement.setAttribute("FontSize", "10");
				newElement.setAttribute("RowNoWidth", "5");

				int columnCount = 0;
				NodeList nodeList = detailTableNode.getElement().getElementsByTagName("Field");
				sortingList = frame_.getSortedListModel(nodeList, "Order");
				for (int i = 0; i < sortingList.getSize(); i++) {
					if (columnCount < 5) {
						workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
						isNotHeaderKey = true;
						for (int j = 0; j < headerKeyList.size(); j++) {
							if (workElement.getAttribute("ID").equals(detailKeyList.get(j))) {
								isNotHeaderKey = false;
								break;
							}
						}
						if (isNotHeaderKey) {
							childElement = frame_.getDomDocument().createElement("Column");
							childElement.setAttribute("Order", Editor.getFormatted4ByteString(columnCount * 10));
							childElement.setAttribute("DataSource", detailTableNode.getElement().getAttribute("ID") + "." + workElement.getAttribute("ID")); 
							childElement.setAttribute("FieldOptions", "");
							childElement.setAttribute("Width", "19");
							childElement.setAttribute("Alignment", "LEFT");
							newElement.appendChild(childElement);
							columnCount++;
						}
					} else {
						break;
					}
				}
				frame_.informationOnThisPageChanged = true;
			}

			this.setVisible(false);

		} else {
			JOptionPane.showMessageDialog(this, errorMessage);
		}
	}

	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	void jTextFieldID_keyReleased(KeyEvent e) {
		detailTableNode = null;
		org.w3c.dom.Element workElement;
		jButtonOK.setEnabled(false);
		jTextFieldName.setText("");
	    jTextFieldDtlKeyFields.setEditable(false);
	    jTextFieldDtlKeyFields.setFocusable(false);
	    //
		if (!jTextFieldID.getText().equals("")) {
			jTextFieldName.setText("N/A");
			detailTableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText());
			if (detailTableNode == null) {
				detailTableNode = frame_.getSpecificXETreeNode("Table", jTextFieldID.getText().toUpperCase());
				if (detailTableNode != null) {
					jTextFieldID.setText(jTextFieldID.getText().toUpperCase());
				}
			}
			if (detailTableNode != null) {
				jButtonOK.setEnabled(true);
				jTextFieldName.setText(detailTableNode.getElement().getAttribute("Name"));
			    jTextFieldDtlKeyFields.setEditable(true);
			    jTextFieldDtlKeyFields.setFocusable(true);
		    	NodeList keyList = detailTableNode.getElement().getElementsByTagName("Key");
		    	for (int i = 0; i < keyList.getLength(); i++) {
		    		workElement = (org.w3c.dom.Element)keyList.item(i);
		    		if (workElement.getAttribute("Type").equals("PK")) {
		    			detailPK = workElement.getAttribute("Fields");
		    			jTextFieldDtlKeyFields.setText(detailPK);
		    			break;
		    		}
		    	}
			    jTextFieldDtlKeyFields.requestFocus();
			    jTextAreaMessage.setText(res.getString("AddTabDialogKeyComment"));
			}
		}
	}
}

class DialogAddDetailTable_jTextFieldID_keyAdapter extends java.awt.event.KeyAdapter {
	DialogAddDetailTable adaptee;
	DialogAddDetailTable_jTextFieldID_keyAdapter(DialogAddDetailTable adaptee) {
		this.adaptee = adaptee;
	}
	public void keyReleased(KeyEvent e) {
		adaptee.jTextFieldID_keyReleased(e);
	}
}

class DialogAddDetailTable_jButtonOK_actionAdapter implements java.awt.event.ActionListener {
	DialogAddDetailTable adaptee;
	DialogAddDetailTable_jButtonOK_actionAdapter(DialogAddDetailTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonOK_actionPerformed(e);
	}
}

class DialogAddDetailTable_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogAddDetailTable adaptee;
	DialogAddDetailTable_jButtonCancel_actionAdapter(DialogAddDetailTable adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}
