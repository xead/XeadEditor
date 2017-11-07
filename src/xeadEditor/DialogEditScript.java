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
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

public class DialogEditScript extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel jPanelInformation = new JPanel();
	private JPanel jPanelScan = new JPanel();
	private JLabel jLabelScanText = new JLabel();
	private JTextField jTextFieldScanText = new JTextField();
	private JCheckBox jCheckBoxScanText = new JCheckBox();
	private JLabel jLabelReplaceText = new JLabel();
	private JTextField jTextFieldReplaceText = new JTextField();
	private JCheckBox jCheckBoxReplaceAll = new JCheckBox();
	private JLabel jLabelFunctionKeys1 = new JLabel();
	private JLabel jLabelFunctionKeys2 = new JLabel();
	private JLabel jLabelFunctionKeys3 = new JLabel();
	private JPanel jPanelStatement = new JPanel();
	private JPanel jPanelStatementHeader = new JPanel();
	private JLabel jLabelStatementHeader = new JLabel();
	private JLabel jLabelStatementCursorPos = new JLabel();
	private JScrollPane jScrollPaneStatement = new JScrollPane();
	private JTextArea jTextAreaStatement = new JTextArea();
	private JScrollPane jScrollPaneFieldInformation = new JScrollPane();
	private JTextArea jTextAreaFieldInformation = new JTextArea();
	private JSplitPane jSplitPane = new JSplitPane();
	private Editor frame_;
	private Action helpAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			frame_.browseHelp();
		}
	};
	private Action scanAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			frame_.scanStringInTextArea(jTextAreaStatement, jTextFieldScanText.getText(), jCheckBoxScanText.isSelected(), jTextFieldReplaceText.getText(), jCheckBoxReplaceAll.isSelected());
		}
	};
	private Action checkAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			frame_.checkSyntaxError(jTextAreaStatement.getText(), true);
		}
	};
	private Action indentAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			indentRows();
		}
	};
	private Action commentAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			commentRows();
		}
	};
	private Action pasteAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			frame_.pasteTextToIndent(jScrollPaneStatement);
		}
	};
	private Action undoAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			undo();
		}
	};
	private Action redoAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			redo();
		}
	};
	private Action modeAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			changeMode();
		}
	};
	private Action listMethodsAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			dialogAssistList.listMethods(jScrollPaneStatement);
		}
	};
	private String originalText = "";
	private String idModeText = "";
	private String returnText = "";
	private UndoManager undoManager = new UndoManager();
	private DialogAssistList dialogAssistList;
	
	public DialogEditScript(Editor frame) {
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
		this.getContentPane().setLayout(new BorderLayout());

		jPanelStatement.setLayout(new BorderLayout());
		jPanelStatement.add(jPanelStatementHeader, BorderLayout.NORTH);
		jPanelStatement.add(jScrollPaneStatement, BorderLayout.CENTER);

		jPanelStatementHeader.setLayout(new BorderLayout());
		jPanelStatementHeader.setPreferredSize(new Dimension(200, 25));
		jLabelStatementHeader.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelStatementHeader.setText(" " + res.getString("Script"));
		jLabelStatementHeader.setPreferredSize(new Dimension(150, 25));

		jLabelStatementCursorPos.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelStatementCursorPos.setForeground(Color.darkGray);
		jLabelStatementCursorPos.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelStatementCursorPos.setPreferredSize(new Dimension(80, 25));
		jLabelStatementCursorPos.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		jPanelStatementHeader.add(jLabelStatementHeader, BorderLayout.WEST);
		jPanelStatementHeader.add(jLabelStatementCursorPos, BorderLayout.EAST);

		jPanelScan.setPreferredSize(new Dimension(10, 181));
		jPanelScan.setLayout(null);
		jLabelScanText.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelScanText.setText(res.getString("ScanStringInScript"));
		jLabelScanText.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelScanText.setBounds(new Rectangle(5, 10, 90, 20));
		jTextFieldScanText.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldScanText.setBounds(new Rectangle(100, 7, 340, 25));
		jTextFieldScanText.addFocusListener(new FocusAdapter() {
			@Override public void focusGained(FocusEvent e) {
				((JTextField)e.getComponent()).selectAll();
			}
		});
		jLabelReplaceText.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelReplaceText.setText(res.getString("ReplaceStringInScript"));
		jLabelReplaceText.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelReplaceText.setBounds(new Rectangle(5, 39, 90, 20));
		jTextFieldReplaceText.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldReplaceText.setBounds(new Rectangle(100, 36, 340, 25));
		jTextFieldReplaceText.addFocusListener(new FocusAdapter() {
			@Override public void focusGained(FocusEvent e) {
				((JTextField)e.getComponent()).selectAll();
			}
		});
		jCheckBoxScanText.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxScanText.setBounds(new Rectangle(5, 68, 230, 22));
		jCheckBoxScanText.setText(res.getString("CaseSensitive"));
		jCheckBoxReplaceAll.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jCheckBoxReplaceAll.setBounds(new Rectangle(240, 68, 230, 22));
		jCheckBoxReplaceAll.setText(res.getString("ReplaceAll"));
		jLabelFunctionKeys1.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelFunctionKeys1.setText(res.getString("ScriptEditorFunctionKeys1"));
		jLabelFunctionKeys1.setBounds(new Rectangle(11, 99, 450, 20));
		jLabelFunctionKeys2.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelFunctionKeys2.setText(res.getString("ScriptEditorFunctionKeys2"));
		jLabelFunctionKeys2.setBounds(new Rectangle(11, 125, 450, 20));
		jLabelFunctionKeys3.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelFunctionKeys3.setText(res.getString("ScriptEditorFunctionKeys3"));
		jLabelFunctionKeys3.setBounds(new Rectangle(11, 151, 450, 20));
		jPanelScan.add(jLabelScanText);
		jPanelScan.add(jTextFieldScanText);
		jPanelScan.add(jCheckBoxScanText);
		jPanelScan.add(jLabelReplaceText);
		jPanelScan.add(jTextFieldReplaceText);
		jPanelScan.add(jCheckBoxReplaceAll);
		jPanelScan.add(jLabelFunctionKeys1);
		jPanelScan.add(jLabelFunctionKeys2);
		jPanelScan.add(jLabelFunctionKeys3);

		jPanelInformation.setLayout(new BorderLayout());
		jPanelInformation.add(jScrollPaneFieldInformation, BorderLayout.CENTER);
		jPanelInformation.add(jPanelScan, BorderLayout.SOUTH);

		jScrollPaneFieldInformation.setBorder(BorderFactory.createEtchedBorder());
		jTextAreaStatement.setBackground(frame_.colorScriptBackground);
		jTextAreaStatement.setForeground(frame_.colorScriptForeground);
		jTextAreaStatement.setCaretColor(frame_.colorScriptCaret);
		jTextAreaStatement.setEditable(true);
		jTextAreaStatement.setOpaque(true);
		jTextAreaStatement.setTabSize(4);
		jTextAreaStatement.addCaretListener(new DialogEditScript_jTextAreaStatement_caretAdapter(this));
		jTextAreaStatement.addKeyListener(new DialogEditScript_jTextAreaStatement_keyAdapter(this));
		jTextAreaStatement.getDocument().addUndoableEditListener(undoManager);
		ActionMap am = jTextAreaStatement.getActionMap();
		am.put(DefaultEditorKit.pasteAction, pasteAction);
		jScrollPaneStatement.getViewport().setView(jTextAreaStatement);

		jTextAreaFieldInformation.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaFieldInformation.setEditable(false);
		jTextAreaFieldInformation.setOpaque(false);
		jScrollPaneFieldInformation.getViewport().add(jTextAreaFieldInformation);

		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		jSplitPane.setDividerLocation(450);
		jSplitPane.add(jPanelStatement, JSplitPane.RIGHT);
		jSplitPane.add(jPanelInformation, JSplitPane.LEFT);
		InputMap inputMap = jSplitPane.getInputMap(JSplitPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.clear();
		ActionMap actionMap = jSplitPane.getActionMap();
		actionMap.clear();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "HELP");
		actionMap.put("HELP", helpAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "CHECK");
		actionMap.put("CHECK", checkAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "SCAN");
		actionMap.put("SCAN", scanAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK), "INDENT");
		actionMap.put("INDENT", indentAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeyEvent.CTRL_DOWN_MASK), "COMMENT");
		actionMap.put("COMMENT", commentAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "UNDO");
		actionMap.put("UNDO", undoAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "REDO");
		actionMap.put("REDO", redoAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "MODE");
		actionMap.put("MODE", modeAction);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK), "LIST");
		actionMap.put("LIST", listMethodsAction);
		dialogAssistList = new DialogAssistList(frame_, this);
		
		this.setResizable(true);
        Rectangle screenRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int screenWidth = (int)screenRect.getWidth();
		int screenHeight = (int)screenRect.getHeight();
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.getContentPane().add(jSplitPane,  BorderLayout.CENTER);
	}

	public String request(String subtitle, String idText, String fieldInfo, int caretPos, String nameText) {
		this.setTitle(res.getString("EditTableScriptTitle") + " - " +  subtitle);

		jTextAreaFieldInformation.setText(fieldInfo);
		jTextAreaFieldInformation.setCaretPosition(0);

		originalText = idText;
		returnText = idText;
		idModeText = idText;

		if (nameText.equals("")) {
			jTextAreaStatement.setText(idText);
			jTextAreaStatement.setEditable(true);
		} else {
			jTextAreaStatement.setText(nameText);
			jTextAreaStatement.setEditable(false);
		}
	
		jTextAreaStatement.getCaret().setVisible(true);
		jTextAreaStatement.setCaretPosition(caretPos);
		jTextAreaStatement.setFont(new java.awt.Font(frame_.scriptFontName, 0, frame_.scriptFontSize));
		jTextAreaStatement.requestFocus();
		undoManager.discardAllEdits();

		super.setVisible(true);

		return returnText;
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			int rtn = 0;
			String edittedText = "";
			if (jTextAreaStatement.isEditable()) {
				edittedText = jTextAreaStatement.getText();
			} else {
				edittedText = idModeText;
			}
			String scriptError = frame_.checkSyntaxError(edittedText, false);

			if (edittedText.equals(originalText)) {
				if (scriptError.equals("")) {
					super.setVisible(false);
				} else {
					Object[] bts = {res.getString("Close"), res.getString("BackToEdit")};
					rtn = JOptionPane.showOptionDialog(this, res.getString("ScriptError") + "\n" + scriptError,
							res.getString("EditTableScriptTitle"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[0]);
					if (rtn == 0) {
						super.setVisible(false);
					}
				}
			} else {
				Object[] bts = {res.getString("CloseSaving"), res.getString("CloseNotSaving"), res.getString("BackToEdit")};
				if (scriptError.equals("")) {
					rtn = JOptionPane.showOptionDialog(this, res.getString("ScriptChangesMessage"),
							res.getString("EditTableScriptTitle"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[0]);
				} else {
					rtn = JOptionPane.showOptionDialog(this, res.getString("ScriptError") + "\n" + scriptError + "\n\n" + res.getString("ScriptChangesMessage"),
							res.getString("EditTableScriptTitle"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, bts, bts[0]);
				}
				if (rtn == 0) {
					returnText = edittedText;
					super.setVisible(false);
				}
				if (rtn == 1) {
					returnText = originalText;
					super.setVisible(false);
				}
			}
		} else {
			super.processWindowEvent(e);
		}
	}
	
	public boolean isEditable() {
		return jTextAreaStatement.isEditable();
	}
	
	public int getCaretPosition() {
		return jTextAreaStatement.getCaretPosition();
	}

	void jTextAreaStatement_caretUpdate(CaretEvent e) {
		dialogAssistList.setVisible(false);
		if (jTextAreaStatement.isEditable()) {
			Point pos = frame_.getCaretPositionInText(jTextAreaStatement);
			jLabelStatementCursorPos.setText(pos.x + " : " + pos.y);
		} else {
			jLabelStatementCursorPos.setText("NAME");
		}
	}
	
	public void indentRows() {
		frame_.indentRows(jScrollPaneStatement);
	}
	
	public void commentRows() {
		frame_.commentRows(jScrollPaneStatement);
	}
	
	public void undo() {
		if (undoManager.canUndo()) {
			undoManager.undo();
			if (jTextAreaStatement.getText().equals("") && undoManager.canUndo()) {
				undoManager.undo();
			}
		}
	}
	
	public void redo() {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}
	
	public void changeMode() {
		////////////////////////////
		// Restore caret position //
		////////////////////////////
		int caretPos = jTextAreaStatement.getCaretPosition();
		String text = jTextAreaStatement.getText().substring(0, caretPos);
		int pos = 0;
		int rowsOfCursor = 0;
		while (pos > -1) {
			pos = text.indexOf("\n", pos);
			if (pos > -1) {
				pos++;
				rowsOfCursor++;
			}
		}

		///////////////////////////////////////////////
		// Change mode between id-mode and name-mode //
		///////////////////////////////////////////////
		if (jTextAreaStatement.isEditable()) {
			jTextAreaStatement.setEditable(false);
			jLabelStatementCursorPos.setText("NAME");
			idModeText = jTextAreaStatement.getText();
			String nameModeText = idModeText;
			for (int i = 0; i < frame_.dataSourceIDList.size(); i++) {
				nameModeText = nameModeText.replaceAll(
						frame_.dataSourceAliasList.get(i)+"_"+frame_.dataSourceIDList.get(i)+".value",
						frame_.dataSourceAliasNameList.get(i)+"_"+frame_.dataSourceNameList.get(i)+".value"); 
				nameModeText = nameModeText.replaceAll(
						frame_.dataSourceAliasList.get(i)+"_"+frame_.dataSourceIDList.get(i)+".oldValue",
						frame_.dataSourceAliasNameList.get(i)+"_"+frame_.dataSourceNameList.get(i)+".oldValue"); 
				nameModeText = nameModeText.replaceAll(
						frame_.dataSourceAliasList.get(i)+"_"+frame_.dataSourceIDList.get(i)+".valueChanged",
						frame_.dataSourceAliasNameList.get(i)+"_"+frame_.dataSourceNameList.get(i)+".valueChanged"); 
				nameModeText = nameModeText.replaceAll(
						frame_.dataSourceAliasList.get(i)+"_"+frame_.dataSourceIDList.get(i)+".color",
						frame_.dataSourceAliasNameList.get(i)+"_"+frame_.dataSourceNameList.get(i)+".color"); 
				nameModeText = nameModeText.replaceAll(
						frame_.dataSourceAliasList.get(i)+"_"+frame_.dataSourceIDList.get(i)+".editable",
						frame_.dataSourceAliasNameList.get(i)+"_"+frame_.dataSourceNameList.get(i)+".editable"); 
				nameModeText = nameModeText.replaceAll(
						frame_.dataSourceAliasList.get(i)+"_"+frame_.dataSourceIDList.get(i)+".enabled",
						frame_.dataSourceAliasNameList.get(i)+"_"+frame_.dataSourceNameList.get(i)+".enabled"); 
				nameModeText = nameModeText.replaceAll(
						frame_.dataSourceAliasList.get(i)+"_"+frame_.dataSourceIDList.get(i)+".error",
						frame_.dataSourceAliasNameList.get(i)+"_"+frame_.dataSourceNameList.get(i)+".error"); 
			}
			nameModeText = frame_.translateTableOperationInScript(nameModeText);
			jTextAreaStatement.setText(nameModeText);
		} else {
			jTextAreaStatement.setEditable(true);
			jTextAreaStatement.setText(idModeText);
		}

		///////////////////////////
		// Adjust caret position //
		///////////////////////////
		text = jTextAreaStatement.getText();
		pos = 0;
		int wrkInt = 0;
		while (wrkInt < rowsOfCursor) {
			pos = text.indexOf("\n", pos);
			pos++;
			wrkInt++;
		}
		jTextAreaStatement.setCaretPosition(pos);
		jTextAreaStatement.getCaret().setVisible(true);
	}
	
	void jTextAreaStatement_keyTyped(KeyEvent e) {
		if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0){
			if (!jTextAreaStatement.isEditable()) {
				int position = jTextAreaStatement.getCaretPosition();
				JOptionPane.showMessageDialog(null, res.getString("ErrorMessage128"));
				jTextAreaStatement.setCaretPosition(position);
				jTextAreaStatement.getCaret().setVisible(true);
			}
		}
	}
}

class DialogEditScript_jTextAreaStatement_caretAdapter implements javax.swing.event.CaretListener {
	DialogEditScript adaptee;
	DialogEditScript_jTextAreaStatement_caretAdapter(DialogEditScript adaptee) {
		this.adaptee = adaptee;
	}
	public void caretUpdate(CaretEvent e) {
		adaptee.jTextAreaStatement_caretUpdate(e);
	}
}

class DialogEditScript_jTextAreaStatement_keyAdapter extends java.awt.event.KeyAdapter {
	DialogEditScript adaptee;
	DialogEditScript_jTextAreaStatement_keyAdapter(DialogEditScript adaptee) {
		this.adaptee = adaptee;
	}
	public void keyTyped(KeyEvent e) {
		adaptee.jTextAreaStatement_keyTyped(e);
	}
}