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
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class DialogSQL extends JDialog {
	private static final long serialVersionUID = 1L;
	static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JButton jButtonCommit = new JButton();
	private JButton jButtonClose = new JButton();
	private JPanel jPanelStatement = new JPanel();
	private JPanel jPanelStatementTop = new JPanel();
	private JPanel jPanelMessage = new JPanel();
	private JLabel jLabelConnection = new JLabel();
	private JComboBox jComboBoxConnection = new JComboBox();
	private JScrollPane jScrollPaneStatement = new JScrollPane();
	private JTextArea jTextAreaStatement = new JTextArea();
	private JLabel jLabelMessage = new JLabel();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private JSplitPane jSplitPane = new JSplitPane();
	private Editor frame_;
	private JPanel jPanelButtons = new JPanel();
	private boolean sqlExecuted;
	private Calendar calendar;
	private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
	private Action commitAction = new AbstractAction(){
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			jButtonCommit_actionPerformed(null);
		}
	};
	
	public DialogSQL(Editor frame) {
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
		jScrollPaneMessage.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().setLayout(new BorderLayout());
		InputMap inputMap  = jScrollPaneStatement.getInputMap(JScrollPane.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.clear();
		ActionMap actionMap = jScrollPaneStatement.getActionMap();
		actionMap.clear();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), "RUN");
		actionMap.put("RUN", commitAction);
		//
		jLabelConnection.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelConnection.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelConnection.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelConnection.setText(res.getString("Connection"));
		jLabelConnection.setPreferredSize(new Dimension(100, 17));
		jLabelConnection.setBounds(new Rectangle(11, 12, 96, 15));
		jComboBoxConnection.setFont(new java.awt.Font("SansSerif", 0, 12));
		jComboBoxConnection.setBounds(new Rectangle(115, 9, 730, 22));
		jPanelStatementTop.setLayout(null);
		jPanelStatementTop.setPreferredSize(new Dimension(10, 40));
		jPanelStatementTop.add(jLabelConnection);
		jPanelStatementTop.add(jComboBoxConnection);
		jPanelStatement.setLayout(new BorderLayout());
		jPanelStatement.add(jPanelStatementTop, BorderLayout.NORTH);
		jPanelStatement.add(jScrollPaneStatement, BorderLayout.CENTER);
		//
		jTextAreaStatement.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaStatement.setEditable(true);
		jTextAreaStatement.setOpaque(true);
		jTextAreaStatement.setLineWrap(true);
		jScrollPaneStatement.getViewport().add(jTextAreaStatement);
		//
		jLabelMessage.setFont(new java.awt.Font("SansSerif", 0, 12));
		jLabelMessage.setText(" " + res.getString("Message"));
		jLabelMessage.setPreferredSize(new Dimension(100, 17));
		jTextAreaMessage.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setText(res.getString("SqlConsoleComment"));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jPanelMessage.setLayout(new BorderLayout());
		jPanelMessage.add(jLabelMessage, BorderLayout.NORTH);
		jPanelMessage.add(jScrollPaneMessage, BorderLayout.CENTER);
		//
		jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane.setDividerLocation(150);
		jSplitPane.add(jPanelStatement, JSplitPane.TOP);
		jSplitPane.add(jPanelMessage, JSplitPane.BOTTOM);
		//
		jButtonClose.setText(res.getString("Close"));
		jButtonClose.setBounds(new Rectangle(30, 8, 80, 25));
		jButtonClose.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonClose.addActionListener(new DialogSQL_jButtonClose_actionAdapter(this));
		jButtonCommit.setText(res.getString("SqlCommitF9"));
		jButtonCommit.setBounds(new Rectangle(750, 8, 100, 25));
		jButtonCommit.setFont(new java.awt.Font("Dialog", 0, 12));
		jButtonCommit.addActionListener(new DialogSQL_jButtonCommit_actionAdapter(this));
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(400, 41));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonClose, null);
		jPanelButtons.add(jButtonCommit, null);
		//
		this.setTitle(res.getString("SqlConsole"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(900, 600));
		this.getContentPane().add(jSplitPane,  BorderLayout.CENTER);
	}

	public boolean request(Connection connection) {
		sqlExecuted = false;
		jComboBoxConnection.removeAllItems();
		for (int i = 0; i < frame_.getDatabaseNameList().size(); i++) {
			jComboBoxConnection.addItem(frame_.getDatabaseNameList().get(i));
		}
		jTextAreaStatement.requestFocus();
		jPanelButtons.getRootPane().setDefaultButton(jButtonClose);
		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = frame_.getSize();
		Point loc = frame_.getLocation();
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		super.setVisible(true);
		return sqlExecuted;
	}

	void jButtonCommit_actionPerformed(ActionEvent e) {
		ResultSetMetaData resultSetMetaData;
		ResultSet resultSet;
		StringBuffer bf = new StringBuffer();
		String sql = "";
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Connection connection = frame_.getDatabaseConnList().get(jComboBoxConnection.getSelectedIndex());
			Statement statement = connection.createStatement();
			sql = jTextAreaStatement.getText().trim().replace("\n", " ");
			if (sql.length() > 1) {
				if (sql.toUpperCase().startsWith("SELECT ")) {
					int seq = 0;
					resultSet = statement.executeQuery(sql);
					resultSetMetaData = resultSet.getMetaData();
					bf.append(jTextAreaMessage.getText());
					bf.append("\n> ");
					bf.append(sql);
					bf.append("\n");
					if (seq == 0) {
						bf.append("No.");
						for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
							bf.append(",");
							bf.append(resultSetMetaData.getColumnName(i));
						}
						bf.append("\n");
					}
					while (resultSet.next()) {
						seq++;
						bf.append(seq);
						for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
							bf.append(",");
							if (resultSet.getObject(resultSetMetaData.getColumnName(i)) == null) {
								bf.append("<null>");
							} else {
								bf.append(resultSet.getObject(resultSetMetaData.getColumnName(i)).toString().trim());
							}
						}
						bf.append("\n");
					}
					bf.append("(");
					calendar = Calendar.getInstance();
					bf.append(formatter.format(calendar.getTime()));
					bf.append(")\n");
					resultSet.close();
					jTextAreaMessage.setText(bf.toString());
				} else {
					statement.executeUpdate(sql);
					bf.append(jTextAreaMessage.getText());
					bf.append("\n> ");
					bf.append(sql);
					bf.append("\n");
					bf.append(res.getString("SqlConsoleMessage1"));
					calendar = Calendar.getInstance();
					bf.append(formatter.format(calendar.getTime()));
					bf.append(")\n");
					jTextAreaMessage.setText(bf.toString());
				}
				sqlExecuted = true;
			}
		} catch (SQLException ex1) {
			bf.append(jTextAreaMessage.getText());
			bf.append("\n> ");
			bf.append(sql);
			bf.append("\n");
			bf.append(res.getString("SqlConsoleMessage2"));
			bf.append(ex1.getMessage());
			bf.append("\n(");
			calendar = Calendar.getInstance();
			bf.append(formatter.format(calendar.getTime()));
			bf.append(")\n");
			jTextAreaMessage.setText(bf.toString());
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	void jButtonClose_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}

class DialogSQL_jButtonCommit_actionAdapter implements java.awt.event.ActionListener {
	DialogSQL adaptee;
	DialogSQL_jButtonCommit_actionAdapter(DialogSQL adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCommit_actionPerformed(e);
	}
}

class DialogSQL_jButtonClose_actionAdapter implements java.awt.event.ActionListener {
	DialogSQL adaptee;
	DialogSQL_jButtonClose_actionAdapter(DialogSQL adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonClose_actionPerformed(e);
	}
}
