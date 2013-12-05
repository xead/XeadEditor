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
import java.awt.event.ActionEvent;
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
	private JLabel jLabelFunctionKeys = new JLabel();
	private JPanel jPanelStatement = new JPanel();
	private JPanel jPanelStatementHeader = new JPanel();
	private JLabel jLabelStatementHeader = new JLabel();
	private JLabel jLabelStatementCursorPos = new JLabel();
	private JPanel jPanelStatementFontSizeAndCursorPos = new JPanel();
	private JPanel jPanelStatementFontSize = new JPanel();
	private JButton jButtonStatementFontSizeS = new JButton();
	private JButton jButtonStatementFontSizeM = new JButton();
	private JButton jButtonStatementFontSizeL = new JButton();
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
			frame_.scanStringInTextArea(jTextAreaStatement, jTextFieldScanText.getText(), jCheckBoxScanText.isSelected());
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
	private String originalText = "";
	private String returnText = "";
	private UndoManager undoManager = new UndoManager();
	
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
		jPanelStatementHeader.setPreferredSize(new Dimension(200, 20));
		jLabelStatementHeader.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelStatementHeader.setText(" " + res.getString("Script"));
		jLabelStatementHeader.setPreferredSize(new Dimension(150, 20));

		jPanelStatementFontSizeAndCursorPos.setLayout(new BorderLayout());
		jPanelStatementFontSizeAndCursorPos.setPreferredSize(new Dimension(200, 20));
		jPanelStatementFontSizeAndCursorPos.add(jLabelStatementCursorPos, BorderLayout.EAST);
		jPanelStatementFontSizeAndCursorPos.add(jPanelStatementFontSize, BorderLayout.CENTER);
		jPanelStatementFontSize.setLayout(new GridLayout(1, 3));
		jPanelStatementFontSize.add(jButtonStatementFontSizeS);
		jPanelStatementFontSize.add(jButtonStatementFontSizeM);
		jPanelStatementFontSize.add(jButtonStatementFontSizeL);
		jButtonStatementFontSizeS.setFont(new java.awt.Font("Dialog", 0, 11));
		jButtonStatementFontSizeS.setText("F");
		jButtonStatementFontSizeS.addActionListener(new DialogEditScript_jButtonStatementFontSize_actionAdapter(this));
		jButtonStatementFontSizeM.setFont(new java.awt.Font("Dialog", 0, 13));
		jButtonStatementFontSizeM.setText("F");
		jButtonStatementFontSizeM.setEnabled(false);
		jButtonStatementFontSizeM.addActionListener(new DialogEditScript_jButtonStatementFontSize_actionAdapter(this));
		jButtonStatementFontSizeL.setFont(new java.awt.Font("Dialog", 0, 15));
		jButtonStatementFontSizeL.setText("F");
		jButtonStatementFontSizeL.addActionListener(new DialogEditScript_jButtonStatementFontSize_actionAdapter(this));
		jLabelStatementCursorPos.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelStatementCursorPos.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelStatementCursorPos.setPreferredSize(new Dimension(70, 20));
		jLabelStatementCursorPos.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		jPanelStatementHeader.add(jLabelStatementHeader, BorderLayout.WEST);
		jPanelStatementHeader.add(jPanelStatementFontSizeAndCursorPos, BorderLayout.EAST);

		jPanelScan.setPreferredSize(new Dimension(10, 100));
		jPanelScan.setLayout(null);
		jLabelScanText.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelScanText.setText(res.getString("ScanStringInScript"));
		jLabelScanText.setBounds(new Rectangle(11, 10, 200, 15));
		jTextFieldScanText.setFont(new java.awt.Font("SansSerif", 0, 12));
		jTextFieldScanText.setBounds(new Rectangle(11, 28, 400, 22));
		jCheckBoxScanText.setFont(new java.awt.Font("SansSerif", 0, 12));
		jCheckBoxScanText.setBounds(new Rectangle(11, 50, 200, 22));
		jCheckBoxScanText.setText(res.getString("CaseSensitive"));
		jLabelFunctionKeys.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelFunctionKeys.setText(res.getString("ScriptEditorFunctionKeys"));
		jLabelFunctionKeys.setBounds(new Rectangle(11, 78, 400, 15));
		jPanelScan.add(jLabelScanText);
		jPanelScan.add(jTextFieldScanText);
		jPanelScan.add(jCheckBoxScanText);
		jPanelScan.add(jLabelFunctionKeys);

		jPanelInformation.setLayout(new BorderLayout());
		jPanelInformation.add(jScrollPaneFieldInformation, BorderLayout.CENTER);
		jPanelInformation.add(jPanelScan, BorderLayout.SOUTH);

		jScrollPaneFieldInformation.setBorder(BorderFactory.createEtchedBorder());
		jTextAreaStatement.setFont(new java.awt.Font("Monospaced", 0, 14));
		jTextAreaStatement.setEditable(true);
		jTextAreaStatement.setOpaque(true);
		jTextAreaStatement.setTabSize(4);
		jTextAreaStatement.addCaretListener(new DialogEditScript_jTextAreaStatement_caretAdapter(this));
		jTextAreaStatement.getDocument().addUndoableEditListener(undoManager);
		ActionMap am = jTextAreaStatement.getActionMap();
		am.put(DefaultEditorKit.pasteAction, pasteAction);
		jScrollPaneStatement.getViewport().setView(jTextAreaStatement);

		jTextAreaFieldInformation.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaFieldInformation.setEditable(false);
		jTextAreaFieldInformation.setOpaque(false);
		jScrollPaneFieldInformation.getViewport().add(jTextAreaFieldInformation);

		jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		jSplitPane.setDividerLocation(250);
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

		this.setResizable(true);
        Rectangle screenRect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int screenWidth = (int)screenRect.getWidth();
		int screenHeight = (int)screenRect.getHeight();
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.getContentPane().add(jSplitPane,  BorderLayout.CENTER);
	}

	public String request(String subtitle, String text, String fieldInfo, int caretPos) {
		this.setTitle(res.getString("EditTableScriptTitle") + " - " +  subtitle);

		jTextAreaFieldInformation.setText(fieldInfo);
		jTextAreaFieldInformation.setCaretPosition(0);

		originalText = text;
		returnText = text;

		jTextAreaStatement.setText(text);
		jTextAreaStatement.setCaretPosition(caretPos);
		jTextAreaStatement.requestFocus();
		undoManager.discardAllEdits();

		super.setVisible(true);

		return returnText;
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			int rtn = 0;
			String scriptError = frame_.checkSyntaxError(jTextAreaStatement.getText(), false);
			//
			if (jTextAreaStatement.getText().equals(originalText)) {
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
					returnText = jTextAreaStatement.getText();
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
	
	public int getCaretPosition() {
		return jTextAreaStatement.getCaretPosition();
	}

	void jTextAreaStatement_caretUpdate(CaretEvent e) {
	    Point pos = frame_.getCaretPositionInText(jTextAreaStatement);
	    jLabelStatementCursorPos.setText(pos.x + " : " + pos.y);
	}

	void jButtonjButtonStatementFontSize_actionPerformed(ActionEvent e) {
		jButtonStatementFontSizeS.setEnabled(true);
		jButtonStatementFontSizeM.setEnabled(true);
		jButtonStatementFontSizeL.setEnabled(true);

		if (e.getSource() == jButtonStatementFontSizeS) {
			jTextAreaStatement.setFont(new java.awt.Font("Monospaced", 0, 12));
			jButtonStatementFontSizeS.setEnabled(false);
		}
		if (e.getSource() == jButtonStatementFontSizeM) {
			jTextAreaStatement.setFont(new java.awt.Font("Monospaced", 0, 14));
			jButtonStatementFontSizeM.setEnabled(false);
		}
		if (e.getSource() == jButtonStatementFontSizeL) {
			jTextAreaStatement.setFont(new java.awt.Font("Monospaced", 0, 16));
			jButtonStatementFontSizeL.setEnabled(false);
		}

		jTextAreaStatement.requestFocus();
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
		}
	}
	
	public void redo() {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}
}

class DialogEditScript_jButtonStatementFontSize_actionAdapter implements java.awt.event.ActionListener {
	DialogEditScript adaptee;
	DialogEditScript_jButtonStatementFontSize_actionAdapter(DialogEditScript adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonjButtonStatementFontSize_actionPerformed(e);
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