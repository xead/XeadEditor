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

		panelMain.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(borderLayout1);
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(400, 43));
		jPanelButtons.setLayout(null);

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

		jPanelButtons.add(jButtonOK, null);
		jPanelButtons.add(jButtonCancel, null);

		jButtonCancel.setBounds(new Rectangle(44, 8, 100, 27));
		jButtonCancel.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCancel.setText(res.getString("Cancel"));
		jButtonCancel.addActionListener(new DialogAddList_jButtonCancel_actionAdapter(this));
		jButtonOK.setBounds(new Rectangle(410, 8, 100, 27));
		jButtonOK.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonOK.setText("OK");
		jButtonOK.addActionListener(new DialogAddList_jButtonOK_actionAdapter(this));

		jLabelHeadID.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelHeadID.setBounds(new Rectangle(43, 5, 200, 22));
		jLabelHeadName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelHeadName.setBounds(new Rectangle(249, 5, 300, 22));

		jLabelNo1.setText("1");
		jLabelNo1.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo1.setBounds(new Rectangle(12, 27, 25, 27));
		jLabelNo1.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID1.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldID1.setBounds(new Rectangle(41, 27, 200, 25));
		jTextFieldName1.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName1.setBounds(new Rectangle(247, 27, 300, 25));

		jLabelNo2.setText("2");
		jLabelNo2.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo2.setBounds(new Rectangle(12, 53, 25, 25));
		jLabelNo2.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID2.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldID2.setBounds(new Rectangle(41, 53, 200, 25));
		jTextFieldName2.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName2.setBounds(new Rectangle(247, 53, 300, 25));

		jLabelNo3.setText("3");
		jLabelNo3.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo3.setBounds(new Rectangle(12, 79, 25, 25));
		jLabelNo3.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID3.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldID3.setBounds(new Rectangle(41, 79, 200, 25));
		jTextFieldName3.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName3.setBounds(new Rectangle(247, 79, 300, 25));

		jLabelNo4.setText("4");
		jLabelNo4.setBounds(new Rectangle(12, 105, 25, 25));
		jLabelNo4.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo4.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID4.setBounds(new Rectangle(41, 105, 200, 25));
		jTextFieldID4.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName4.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName4.setBounds(new Rectangle(247, 105, 300, 25));

		jLabelNo5.setText("5");
		jLabelNo5.setBounds(new Rectangle(12, 131, 25, 25));
		jLabelNo5.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo5.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID5.setBounds(new Rectangle(41, 131, 200, 25));
		jTextFieldID5.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName5.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName5.setBounds(new Rectangle(247, 131, 300, 25));

		jLabelNo6.setText("6");
		jLabelNo6.setBounds(new Rectangle(12, 157, 25, 25));
		jLabelNo6.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo6.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID6.setBounds(new Rectangle(41, 157, 200, 25));
		jTextFieldID6.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName6.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName6.setBounds(new Rectangle(247, 157, 300, 25));

		jLabelNo7.setText("7");
		jLabelNo7.setBounds(new Rectangle(12, 183, 25, 25));
		jLabelNo7.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo7.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID7.setBounds(new Rectangle(41, 183, 200, 25));
		jTextFieldID7.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName7.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName7.setBounds(new Rectangle(247, 183, 300, 25));

		jLabelNo8.setText("8");
		jLabelNo8.setBounds(new Rectangle(12, 209, 25, 25));
		jLabelNo8.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo8.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID8.setBounds(new Rectangle(41, 209, 200, 25));
		jTextFieldID8.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName8.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName8.setBounds(new Rectangle(247, 209, 300, 25));

		jLabelNo9.setText("9");
		jLabelNo9.setBounds(new Rectangle(12, 235, 25, 25));
		jLabelNo9.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo9.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID9.setBounds(new Rectangle(41, 235, 200, 25));
		jTextFieldID9.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName9.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName9.setBounds(new Rectangle(247, 235, 300, 25));

		jLabelNo10.setText("10");
		jLabelNo10.setBounds(new Rectangle(12, 261, 25, 25));
		jLabelNo10.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelNo10.setHorizontalAlignment(SwingConstants.RIGHT);
		jTextFieldID10.setBounds(new Rectangle(41, 261, 200, 25));
		jTextFieldID10.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName10.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldName10.setBounds(new Rectangle(247, 261, 300, 25));

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
			labelList.get(i).setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
			idList.get(i).setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
			nameList.get(i).setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		}

		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setSize(new Dimension(400, 347));
		panelMain.setPreferredSize(new Dimension(560, 302));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
	}

	public int request(String parentType) {
		reply = 0;
		parentType_ = parentType;

		if (parentType_.equals("MenuList")) {
			this.setTitle(res.getString("AddNewMenus"));
			jLabelHeadID.setText(res.getString("IDWithLength1")+"2"+res.getString("IDWithLength2"));
			jLabelHeadName.setText(res.getString("MenuName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(2);
				nameList.get(i).setText("");
			}
		}
		if (parentType_.equals("SubsystemList")) {
			this.setTitle(res.getString("AddNewSubsystems"));
			jLabelHeadID.setText(res.getString("IDWithLength1")+"10"+res.getString("IDWithLength2"));
			jLabelHeadName.setText(res.getString("SubsystemName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(10);
				nameList.get(i).setText("");
			}
		}
		if (parentType_.equals("TableList")) {
			this.setTitle(res.getString("AddNewTables"));
			jLabelHeadID.setText(res.getString("IDWithLength1")+"40"+res.getString("IDWithLength2"));
			jLabelHeadName.setText(res.getString("TableName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(40);
				nameList.get(i).setText("");
			}
		}
		if (parentType_.equals("TableFieldList")) {
			this.setTitle(res.getString("AddNewFields"));
			jLabelHeadID.setText(res.getString("IDWithLength1")+"40"+res.getString("IDWithLength2"));
			jLabelHeadName.setText(res.getString("FieldName"));
			for (int i = 0; i < 10; i++) {
				idList.get(i).setText("");
				idList.get(i).setMaxLength(40);
				nameList.get(i).setText("");
			}
		}

		jTextFieldID1.requestFocus();
		jPanelButtons.getRootPane().setDefaultButton(jButtonOK);
		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = frame_.getSize();
		Point loc = frame_.getLocation();
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		this.pack();
		super.setVisible(true);

		return reply;
	}

	void jButtonOK_actionPerformed(ActionEvent e) {
		org.w3c.dom.Element element;
		reply = 1;
		boolean hasNoError = true;
		boolean duplicated;
		NodeList nodeList;

		for (int i = 0; i < 10; i++) {
			if (idList.get(i).getText().contains(" ") || idList.get(i).getText().contains("@")) {
				if (hasNoError) {
					JOptionPane.showMessageDialog(null, res.getString("ErrorMessage131"));
					hasNoError = false;
				}
			} else {
				if (!idList.get(i).getText().equals("") && !nameList.get(i).getText().equals("")) {
					idList.get(i).setText(frame_.getCaseShiftValue(idList.get(i).getText(), "Upper"));
					if (parentType_.equals("TableFieldList")) {
						duplicated = false;
						nodeList = frame_.currentMainTreeNode.getElement().getElementsByTagName("Field");
						for (int j = 0; j < nodeList.getLength(); j++) {
							element = (org.w3c.dom.Element)nodeList.item(j);
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
		}
		this.setVisible(false);
	}

	void jButtonImport_actionPerformed(ActionEvent e) {
		reply = 2;
		this.setVisible(false);
	}

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
