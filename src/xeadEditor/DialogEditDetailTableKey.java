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

public class DialogEditDetailTableKey extends JDialog {
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
	private String headerPK = "";
	private String detailPK = "";
	private boolean isValidated = false;
	
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
		//
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(new BorderLayout());
		//
		jLabelID.setText(res.getString("DTLTableID"));
		jLabelID.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelID.setBounds(new Rectangle(11, 12, 89, 15));
		jTextFieldID.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldID.setBounds(new Rectangle(105, 9, 70, 21));
		jTextFieldID.setEditable(false);
		jTextFieldID.setFocusable(false);
		jTextFieldName.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldName.setBounds(new Rectangle(180, 9, 305, 22));
		jTextFieldName.setEditable(false);
		jTextFieldName.setFocusable(false);
		jLabelHdrKeyFields.setText(res.getString("HDRTableKeys"));
		jLabelHdrKeyFields.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelHdrKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelHdrKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelHdrKeyFields.setBounds(new Rectangle(11, 40, 89, 15));
		//jTextFieldHdrKeyFields.setEditable(false);
		//jTextFieldHdrKeyFields.setFocusable(false);
		jTextFieldHdrKeyFields.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldHdrKeyFields.setBounds(new Rectangle(105, 37, 380, 21));
		jLabelDtlKeyFields.setText(res.getString("DTLTableKeys"));
		jLabelDtlKeyFields.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelDtlKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelDtlKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelDtlKeyFields.setBounds(new Rectangle(11, 68, 89, 15));
		jTextFieldDtlKeyFields.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldDtlKeyFields.setBounds(new Rectangle(105, 65, 380, 21));
		jScrollPaneMessage.setBounds(new Rectangle(493, 9, 250, 77));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jTextAreaMessage.setFont(new java.awt.Font("Dialog", 0, 12));
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
		jPanelButtons.setPreferredSize(new Dimension(350, 43));
		jButtonOK.setBounds(new Rectangle(620, 10, 73, 25));
		jButtonOK.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new DialogEditDetailTableKey_jButtonOK_actionAdapter(this));
		jButtonCancel.setBounds(new Rectangle(44, 10, 73, 25));
		jButtonCancel.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogEditDetailTableKey_jButtonCancel_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonOK);
		jPanelButtons.add(jButtonCancel);
		//
		this.setTitle(res.getString("EditDetailTableKeyTitle"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(760, 171));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		this.pack();
	}
	//
	public boolean request(org.w3c.dom.Element objectFunctionElement, String detailTableID, String headerKeys, String detailKeys) {
		org.w3c.dom.Element workElement;
		//
		isValidated = false;
		headerTableNode = frame_.getSpecificXETreeNode("Table", objectFunctionElement.getAttribute("HeaderTable"));
		detailTableNode = frame_.getSpecificXETreeNode("Table", detailTableID);
		if (headerTableNode != null && detailTableNode != null) {
			//
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
			//
			jTextFieldID.setText(detailTableID);
			jTextFieldID.requestFocus();
			jTextFieldName.setText("");
			jTextFieldName.setText(detailTableNode.getElement().getAttribute("Name"));
			if (headerKeys.equals("")) {
				jTextFieldHdrKeyFields.setText(headerPK);
			} else {
				jTextFieldHdrKeyFields.setText(headerKeys);
			}
			if (detailKeys.equals("")) {
				jTextFieldDtlKeyFields.setText(detailPK);
			} else {
				jTextFieldDtlKeyFields.setText(detailKeys);
			}
			jTextAreaMessage.setText(res.getString("EditDetailTableKeyComment"));
			//
			Dimension dlgSize = this.getPreferredSize();
			Dimension frmSize = frame_.getSize();
			Point loc = frame_.getLocation();
			this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
			jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
			//
			super.setVisible(true);
		}
		//
		return isValidated;
	}
	//
	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element elementHdr, elementDtl;
		StringTokenizer workTokenizerHdr, workTokenizerDtl;
		String dataSourceHdr, dataSourceDtl;
		String errorMessage = "";
		//
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
		//
		workTokenizerDtl = new StringTokenizer(jTextFieldDtlKeyFields.getText(), ";");
		if (workTokenizerDtl.countTokens() == 0) {
			errorMessage = res.getString("ErrorMessage73");
		} else {
			if (workTokenizerHdr.countTokens() >= workTokenizerDtl.countTokens()) {
				errorMessage = res.getString("ErrorMessage74");
			} else {
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
							errorMessage = res.getString("ErrorMessage77");
							break;
						}
					}
				}
			}
		}
		//
		if (errorMessage.equals("")) {
			isValidated = true;
			this.setVisible(false);
		} else {
			isValidated = false;
			JOptionPane.showMessageDialog(this, errorMessage);
		}
	}
	//
	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	//
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
	//
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
	//
	public String getDetailTableName() {
		return jTextFieldName.getText();
	}
	//
	public String getDetailTablePK() {
		return detailPK;
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
