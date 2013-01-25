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
		jLabelID.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelID.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelID.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelID.setBounds(new Rectangle(11, 12, 89, 15));
		jTextFieldID.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldID.setBounds(new Rectangle(105, 9, 70, 21));
		jTextFieldID.addKeyListener(new DialogAddDetailTable_jTextFieldID_keyAdapter(this));
		jTextFieldName.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextFieldName.setBounds(new Rectangle(180, 9, 305, 22));
		jTextFieldName.setEditable(false);
		jTextFieldName.setFocusable(false);
		jLabelHdrKeyFields.setText(res.getString("HDRTableKeys"));
		jLabelHdrKeyFields.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelHdrKeyFields.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelHdrKeyFields.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelHdrKeyFields.setBounds(new Rectangle(11, 40, 89, 15));
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
		jButtonOK.addActionListener(new DialogAddDetailTable_jButtonOK_actionAdapter(this));
		jButtonCancel.setBounds(new Rectangle(44, 10, 73, 25));
		jButtonCancel.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogAddDetailTable_jButtonCancel_actionAdapter(this));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonOK);
		jPanelButtons.add(jButtonCancel);
		//
		this.setTitle(res.getString("AddTabDialogTitle"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(760, 171));
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
		jTextFieldName.setText("");
		jTextFieldDtlKeyFields.setText("");
		jTextFieldDtlKeyFields.setEditable(false);
		jTextFieldDtlKeyFields.setFocusable(false);
		jTextAreaMessage.setText(res.getString("AddTabDialogComment"));
		jButtonOK.setEnabled(false);
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

	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element elementHdr, elementDtl, childElement, workElement;
		StringTokenizer workTokenizerHdr, workTokenizerDtl;
		String dataSourceHdr, dataSourceDtl;
		String errorMessage = "";
		//
		NodeList detailList = objectFunctionElement_.getElementsByTagName("Detail");
		sortingList = frame_.getSortedListModel(detailList, "Order");
	    for (int i = 0; i < sortingList.getSize(); i++) {
			workElement = (org.w3c.dom.Element)sortingList.elementAt(i);
			lastOrder = Integer.parseInt(workElement.getAttribute("Order"));
		}
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
		if (errorMessage.equals("")) {
			workTokenizerDtl = new StringTokenizer(jTextFieldDtlKeyFields.getText(), ";");
			if (workTokenizerDtl.countTokens() == 0) {
				errorMessage = res.getString("ErrorMessage73");
			} else {
				if (workTokenizerHdr.countTokens() >= workTokenizerDtl.countTokens()) {
					errorMessage = res.getString("ErrorMessage74");
				} else {
					while (workTokenizerDtl.hasMoreTokens()) {
						dataSourceDtl = workTokenizerDtl.nextToken();
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
									errorMessage = res.getString("ErrorMessage77");
									break;
								}
							}
						}
					}
				}
			}
		}
		//
		if (errorMessage.equals("")) {
			newElement = frame_.getDomDocument().createElement("Detail");
			lastOrder = lastOrder + 10;
			newElement.setAttribute("Order", frame_.getFormatted4ByteString(lastOrder));
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
			newElement.setAttribute("DetailFunction", "XXXXX");
			newElement.setAttribute("Caption", detailTableNode.getElement().getAttribute("Name"));
			newElement.setAttribute("InitialMsg", "");
			//
			NodeList nodeList = detailTableNode.getElement().getElementsByTagName("Field");
			sortingList = frame_.getSortedListModel(nodeList, "Order");
		    for (int i = 0; i < sortingList.getSize(); i++) {
		    	if (i < 4) {
		    		workElement = (org.w3c.dom.Element)sortingList.getElementAt(i);
		    		childElement = frame_.getDomDocument().createElement("Column");
		    		childElement.setAttribute("Order", "00" + Integer.toString(i + 1) + "0");
		    		childElement.setAttribute("DataSource", detailTableNode.getElement().getAttribute("ID") + "." + workElement.getAttribute("ID")); 
	    			childElement.setAttribute("FieldOptions", "");
		    		newElement.appendChild(childElement);
		    	} else {
		    		break;
		    	}
			}
			//
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "0");
			childElement.setAttribute("Number", "3");
			childElement.setAttribute("Caption", res.getString("Close"));
			childElement.setAttribute("Action", "EXIT");
			newElement.appendChild(childElement);
			childElement = frame_.getDomDocument().createElement("Button");
			childElement.setAttribute("Position", "3");
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
			//
			this.setVisible(false);
			//
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
