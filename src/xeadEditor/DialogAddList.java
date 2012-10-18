package xeadEditor;

/*
 * Copyright (c) 2012 WATANABE kozo <qyf05466@nifty.com>,
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
import xeadEditor.Editor.TableRowNumber;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DialogAddList extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel panelMain = new JPanel();
	private JButton jButtonOK = new JButton();
	private JButton jButtonCancel = new JButton();
	private int reply;
	private Editor frame_;
	private String parentType_ = "";
	private JLabel jLabelHeadID = new JLabel();
	private JLabel jLabelHeadName = new JLabel();
	private Editor_KanjiTextField jTextFieldName1 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID1 = new LimitSizeTextField();
	private JLabel jLabelNo1 = new JLabel();
	private Editor_KanjiTextField jTextFieldName2 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID2 = new LimitSizeTextField();
	private JLabel jLabelNo2 = new JLabel();
	private Editor_KanjiTextField jTextFieldName3 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID3 = new LimitSizeTextField();
	private JLabel jLabelNo3 = new JLabel();
	private Editor_KanjiTextField jTextFieldName4 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID4 = new LimitSizeTextField();
	private JLabel jLabelNo4 = new JLabel();
	private Editor_KanjiTextField jTextFieldName5 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID5 = new LimitSizeTextField();
	private JLabel jLabelNo5 = new JLabel();
	private Editor_KanjiTextField jTextFieldName6 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID6 = new LimitSizeTextField();
	private JLabel jLabelNo6 = new JLabel();
	private Editor_KanjiTextField jTextFieldName7 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID7 = new LimitSizeTextField();
	private JLabel jLabelNo7 = new JLabel();
	private Editor_KanjiTextField jTextFieldName8 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID8 = new LimitSizeTextField();
	private JLabel jLabelNo8 = new JLabel();
	private Editor_KanjiTextField jTextFieldName9 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID9 = new LimitSizeTextField();
	private JLabel jLabelNo9 = new JLabel();
	private Editor_KanjiTextField jTextFieldName10 = new Editor_KanjiTextField();
	private LimitSizeTextField jTextFieldID10 = new LimitSizeTextField();
	private JLabel jLabelNo10 = new JLabel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel jPanelButtons = new JPanel();
	private ArrayList<JLabel> labelList = new ArrayList<JLabel>();
	private ArrayList<LimitSizeTextField> idList = new ArrayList<LimitSizeTextField>();
	private ArrayList<Editor_KanjiTextField> nameList = new ArrayList<Editor_KanjiTextField>();

	public DialogAddList(Editor frame) {
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
		this.getContentPane().setLayout(borderLayout1);
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(400, 43));
		jPanelButtons.setLayout(null);
		//
		panelMain.setLayout(null);
		panelMain.add(jLabelHeadID);
		panelMain.add(jLabelHeadName);
		panelMain.add(jTextFieldID1);
		panelMain.add(jTextFieldID2);
		panelMain.add(jTextFieldID3);
		panelMain.add(jTextFieldID4);
		panelMain.add(jTextFieldID5);
		panelMain.add(jTextFieldID6);
		panelMain.add(jTextFieldID7);
		panelMain.add(jTextFieldID8);
		panelMain.add(jTextFieldID9);
		panelMain.add(jTextFieldID10);
		panelMain.add(jTextFieldName1);
		panelMain.add(jTextFieldName2);
		panelMain.add(jTextFieldName3);
		panelMain.add(jTextFieldName4);
		panelMain.add(jTextFieldName5);
		panelMain.add(jTextFieldName6);
		panelMain.add(jTextFieldName7);
		panelMain.add(jTextFieldName8);
		panelMain.add(jTextFieldName9);
		panelMain.add(jTextFieldName10);
		panelMain.add(jLabelNo1);
		panelMain.add(jLabelNo2);
		panelMain.add(jLabelNo3);
		panelMain.add(jLabelNo4);
		panelMain.add(jLabelNo5);
		panelMain.add(jLabelNo6);
		panelMain.add(jLabelNo7);
		panelMain.add(jLabelNo8);
		panelMain.add(jLabelNo9);
		panelMain.add(jLabelNo10);
		//
		jPanelButtons.add(jButtonOK, null);
		jPanelButtons.add(jButtonCancel, null);
		//
		panelMain.setPreferredSize(new Dimension(400, 310));
		jButtonOK.setBounds(new Rectangle(284, 10, 73, 25));
		jButtonOK.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new DialogAddList_jButtonOK_actionAdapter(this));
		jButtonCancel.setBounds(new Rectangle(44, 10, 73, 25));
		jButtonCancel.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogAddList_jButtonCancel_actionAdapter(this));
		//
		jLabelHeadID.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelHeadID.setBounds(new Rectangle(46, 11, 70, 15));
		jLabelHeadName.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelHeadName.setBounds(new Rectangle(142, 10, 150, 15));
		//
		jLabelNo1.setText("1");
		jLabelNo1.setBounds(new Rectangle(12, 29, 19, 15));
		jTextFieldID1.setBounds(new Rectangle(41, 28, 86, 22));
		jTextFieldName1.setBounds(new Rectangle(136, 28, 249, 22));
		jTextFieldName2.setBounds(new Rectangle(136, 55, 249, 22));
		jTextFieldID2.setBounds(new Rectangle(41, 55, 86, 22));
		jLabelNo2.setText("2");
		jLabelNo2.setBounds(new Rectangle(12, 56, 19, 15));
		jTextFieldName3.setBounds(new Rectangle(136, 82, 249, 22));
		jTextFieldID3.setBounds(new Rectangle(41, 82, 86, 22));
		jLabelNo3.setBounds(new Rectangle(12, 83, 19, 15));
		jLabelNo3.setText("3");
		jTextFieldName4.setBounds(new Rectangle(136, 109, 249, 22));
		jTextFieldID4.setBounds(new Rectangle(41, 109, 86, 22));
		jLabelNo4.setBounds(new Rectangle(12, 110, 19, 15));
		jLabelNo4.setText("4");
		jTextFieldName5.setBounds(new Rectangle(136, 136, 249, 22));
		jTextFieldID5.setBounds(new Rectangle(41, 136, 86, 22));
		jLabelNo5.setBounds(new Rectangle(12, 137, 19, 15));
		jLabelNo5.setText("5");
		jTextFieldName6.setBounds(new Rectangle(136, 164, 249, 22));
		jTextFieldID6.setBounds(new Rectangle(41, 164, 86, 22));
		jLabelNo6.setBounds(new Rectangle(12, 165, 19, 15));
		jLabelNo6.setText("6");
		jTextFieldName7.setBounds(new Rectangle(136, 191, 249, 22));
		jTextFieldID7.setBounds(new Rectangle(41, 191, 86, 22));
		jLabelNo7.setBounds(new Rectangle(12, 192, 19, 15));
		jLabelNo7.setText("7");
		jTextFieldName8.setBounds(new Rectangle(136, 218, 249, 22));
		jTextFieldID8.setBounds(new Rectangle(41, 218, 86, 22));
		jLabelNo8.setBounds(new Rectangle(12, 219, 19, 15));
		jLabelNo8.setText("8");
		jTextFieldName9.setBounds(new Rectangle(136, 245, 249, 22));
		jTextFieldID9.setBounds(new Rectangle(41, 245, 86, 22));
		jLabelNo9.setBounds(new Rectangle(12, 246, 19, 15));
		jLabelNo9.setText("9");
		jTextFieldName10.setBounds(new Rectangle(136, 272, 249, 22));
		jTextFieldID10.setBounds(new Rectangle(41, 272, 86, 22));
		jLabelNo10.setBounds(new Rectangle(12, 273, 19, 15));
		jLabelNo10.setText("10");
		//
		labelList.add(jLabelNo1);
		labelList.add(jLabelNo2);
		labelList.add(jLabelNo3);
		labelList.add(jLabelNo4);
		labelList.add(jLabelNo5);
		labelList.add(jLabelNo6);
		labelList.add(jLabelNo7);
		labelList.add(jLabelNo8);
		labelList.add(jLabelNo9);
		labelList.add(jLabelNo10);
		idList.add(jTextFieldID1);
		idList.add(jTextFieldID2);
		idList.add(jTextFieldID3);
		idList.add(jTextFieldID4);
		idList.add(jTextFieldID5);
		idList.add(jTextFieldID6);
		idList.add(jTextFieldID7);
		idList.add(jTextFieldID8);
		idList.add(jTextFieldID9);
		idList.add(jTextFieldID10);
		nameList.add(jTextFieldName1);
		nameList.add(jTextFieldName2);
		nameList.add(jTextFieldName3);
		nameList.add(jTextFieldName4);
		nameList.add(jTextFieldName5);
		nameList.add(jTextFieldName6);
		nameList.add(jTextFieldName7);
		nameList.add(jTextFieldName8);
		nameList.add(jTextFieldName9);
		nameList.add(jTextFieldName10);
		for (int i = 0; i < 10; i++) {
			labelList.get(i).setHorizontalAlignment(SwingConstants.RIGHT);
			labelList.get(i).setFont(new java.awt.Font("Dialog", 0, 12));
			idList.get(i).setFont(new java.awt.Font("Dialog", 0, 12));
			nameList.get(i).setFont(new java.awt.Font("Dialog", 0, 12));
		}
		//
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setSize(new Dimension(400, 347));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
	}
	//
	public int request(String parentType) {
		reply = 0;
		parentType_ = parentType;
		//
		//for (int i = 0; i < 10; i++) {
		//	idList.get(i).setText("");
		//	nameList.get(i).setText("");
		//}
		//
		if (parentType_.equals("MenuList")) {
			this.setTitle(res.getString("AddNewMenus"));
			jLabelHeadID.setText("ID(Max10)");
			jLabelHeadName.setText(res.getString("MenuName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(10);
				nameList.get(i).setText("");
			}
		}
		if (parentType_.equals("SubsystemList")) {
			this.setTitle(res.getString("AddNewSubsystems"));
			jLabelHeadID.setText("ID(Max10)");
			jLabelHeadName.setText(res.getString("SubsystemName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(10);
				nameList.get(i).setText("");
			}
		}
		if (parentType_.equals("TableList")) {
			this.setTitle(res.getString("AddNewTables"));
			jLabelHeadID.setText("ID(Max20)");
			jLabelHeadName.setText(res.getString("TableName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(20);
				nameList.get(i).setText("");
			}
		}
		if (parentType_.equals("TableFieldList")) {
			this.setTitle(res.getString("AddNewFields"));
			jLabelHeadID.setText("ID(Max40)");
			jLabelHeadName.setText(res.getString("FieldName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(40);
				nameList.get(i).setText("");
			}
		}
		//
		jTextFieldID1.requestFocus();
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = frame_.getSize();
		Point loc = frame_.getLocation();
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		this.pack();
		super.setVisible(true);
		//
		return reply;
	}
	//
	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element element;
		TableRowNumber tableRowNumber;
		reply = 1;
		boolean noError = true;
		boolean duplicated;
		//
		//if (parentType_.equals("TableList")) {
		//	for (int i = 0; i < 10; i++) {
		//		if (idList.get(i).getText().contains("_")) {
		//			noError = false;
		//			JOptionPane.showMessageDialog(this, res.getString("ErrorMessage105"));
		//			break;
		//		}
		//	}
		//}
		//
		if (noError) {
			for (int i = 0; i < 10; i++) {
				if (!idList.get(i).getText().equals("") && !nameList.get(i).getText().equals("")) {
					idList.get(i).setText(idList.get(i).getText().toUpperCase());
					if (parentType_.equals("TableFieldList")) {
						duplicated = false;
						for (int j = 0; j < frame_.tableModelTableFieldList.getRowCount(); j++) {
							tableRowNumber = (TableRowNumber)frame_.tableModelTableFieldList.getValueAt(j, 0);
							element = tableRowNumber.getElement();
							if (element.getAttribute("ID").equals(idList.get(i).getText())) {
								duplicated = true;
								break;
							}
						}
						if (duplicated) {
							JOptionPane.showMessageDialog(this, res.getString("ErrorMessage16") + idList.get(i).getText() + res.getString("ErrorMessage17"));
						} else {
							org.w3c.dom.Element newElement = frame_.createNewElementAccordingToType(parentType_);
							if (newElement != null) {
								newElement.setAttribute("ID", idList.get(i).getText());
								newElement.setAttribute("Name", nameList.get(i).getText());
								frame_.currentMainTreeNode.getElement().appendChild(newElement);
								frame_.currentMainTreeNode.updateFields();
							}
						}
					} else {
						frame_.currentMainTreeNode.addChildNode(null, idList.get(i).getText(), nameList.get(i).getText());
					}
				}
			}
			//
			this.setVisible(false);
		}
	}
	//
	void jButtonImport_actionPerformed(ActionEvent e) {
		reply = 2;
		this.setVisible(false);
	}
	//
	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}

class DialogAddList_jButtonOK_actionAdapter implements java.awt.event.ActionListener {
	DialogAddList adaptee;
	DialogAddList_jButtonOK_actionAdapter(DialogAddList adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonOK_actionPerformed(e);
	}
}

class DialogAddList_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogAddList adaptee;
	DialogAddList_jButtonCancel_actionAdapter(DialogAddList adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}
