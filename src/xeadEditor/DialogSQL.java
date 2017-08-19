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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class DialogSQL extends JDialog {
	private static final long serialVersionUID = 1L;
	static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JButton jButtonCommit = new JButton();
	private JButton jButtonListTables = new JButton();
	private JButton jButtonClose = new JButton();
	private JPanel jPanelStatement = new JPanel();
	private JPanel jPanelStatementTop = new JPanel();
	private JPanel jPanelMessage = new JPanel();
	private JLabel jLabelConnection = new JLabel();
	private JComboBox jComboBoxConnection = new JComboBox();
	private ArrayList<String> dbIDList = new ArrayList<String>();
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
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "RUN");
		actionMap.put("RUN", commitAction);
		//
		jLabelConnection.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelConnection.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelConnection.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelConnection.setText(res.getString("AvailableConnection"));
		jLabelConnection.setBounds(new Rectangle(5, 12, 190, 20));
		jComboBoxConnection.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jComboBoxConnection.setBounds(new Rectangle(200, 9, 790, 25));
		jPanelStatementTop.setLayout(null);
		jPanelStatementTop.setPreferredSize(new Dimension(10, 43));
		jPanelStatementTop.add(jLabelConnection);
		jPanelStatementTop.add(jComboBoxConnection);
		jPanelStatement.setLayout(new BorderLayout());
		jPanelStatement.add(jPanelStatementTop, BorderLayout.NORTH);
		jPanelStatement.add(jScrollPaneStatement, BorderLayout.CENTER);
		//
		jTextAreaStatement.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaStatement.setEditable(true);
		jTextAreaStatement.setOpaque(true);
		jTextAreaStatement.setLineWrap(true);
		jTextAreaStatement.setWrapStyleWord(true);
		jScrollPaneStatement.getViewport().add(jTextAreaStatement);
		//
		jLabelMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelMessage.setText(" " + res.getString("Message"));
		jLabelMessage.setPreferredSize(new Dimension(100, 23));
		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setTabSize(16);
		jTextAreaMessage.setText(res.getString("SqlConsoleComment"));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jPanelMessage.setLayout(new BorderLayout());
		jPanelMessage.add(jLabelMessage, BorderLayout.NORTH);
		jPanelMessage.add(jScrollPaneMessage, BorderLayout.CENTER);
		//
		jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane.setDividerLocation(200);
		jSplitPane.add(jPanelStatement, JSplitPane.TOP);
		jSplitPane.add(jPanelMessage, JSplitPane.BOTTOM);
		//
		jButtonClose.setText(res.getString("Close"));
		jButtonClose.setBounds(new Rectangle(30, 8, 100, 27));
		jButtonClose.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonClose.addActionListener(new DialogSQL_jButtonClose_actionAdapter(this));
		jButtonCommit.setText(res.getString("SqlCommitF5"));
		jButtonCommit.setBounds(new Rectangle(400, 8, 150, 27));
		jButtonCommit.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCommit.addActionListener(new DialogSQL_jButtonCommit_actionAdapter(this));
		jButtonListTables.setText(res.getString("SqlListModules"));
		jButtonListTables.setBounds(new Rectangle(820, 8, 150, 27));
		jButtonListTables.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonListTables.addActionListener(new DialogSQL_jButtonListTables_actionAdapter(this));
		jPanelButtons.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtons.setPreferredSize(new Dimension(100, 43));
		jPanelButtons.setLayout(null);
		jPanelButtons.add(jButtonClose, null);
		jPanelButtons.add(jButtonCommit, null);
		jPanelButtons.add(jButtonListTables, null);
		//
		this.setTitle(res.getString("SqlConsole"));
		this.getContentPane().add(jPanelButtons,  BorderLayout.SOUTH);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(1010, 700));
		this.getContentPane().add(jSplitPane,  BorderLayout.CENTER);
	}

	public boolean request(Connection connection) {
		sqlExecuted = false;
		int selectedIndex = jComboBoxConnection.getSelectedIndex();
		jComboBoxConnection.removeAllItems();
		for (int i = 0; i < frame_.getDatabaseNameList().size(); i++) {
			try {
				if (frame_.getDatabaseConnList().get(i) != null && !frame_.getDatabaseConnList().get(i).isClosed()) {
					jComboBoxConnection.addItem(frame_.getDatabaseNameList().get(i));
				}
			} catch (SQLException e) {}
		}
		if (selectedIndex > -1 && selectedIndex < jComboBoxConnection.getItemCount()) {
			jComboBoxConnection.setSelectedIndex(selectedIndex);
		}
		dbIDList = frame_.getDatabaseIDList();
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

	void jButtonListTables_actionPerformed(ActionEvent e) {
		ResultSet resultSet1, resultSet2;
		StringBuffer bf = new StringBuffer();
		String tableName, moduleID, wrkStr;
		org.w3c.dom.Element tableElement;
		NodeList tableList = frame_.getDomDocument().getElementsByTagName("Table");
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			bf.append(jTextAreaMessage.getText());
			bf.append("\n<Table Module>\t<X-TEA Defined Name>\t<Rows>\n");
			Connection connection = frame_.getDatabaseConnList().get(jComboBoxConnection.getSelectedIndex());
			Statement statement = connection.createStatement();
			DatabaseMetaData metaData = connection.getMetaData();
//		    resultSet1 = metaData.getTables(null, null, "%", null);
		    resultSet1 = metaData.getTables(null, null, "%", new String[] {"TABLE"}); 
		    while (resultSet1.next()) {
				try {
					if (jComboBoxConnection.getSelectedItem().toString().contains("postgres")) {
						bf.append(resultSet1.getString("TABLE_NAME"));
						bf.append("\t");
						tableName = "N/A";
						for (int i = 0; i < tableList.getLength(); i++) {
							tableElement = (org.w3c.dom.Element)tableList.item(i);
							if (tableElement.getAttribute("DB").equals(dbIDList.get(jComboBoxConnection.getSelectedIndex()))) {
								if (tableElement.getAttribute("ModuleID").equals("")) {
									moduleID = tableElement.getAttribute("ID");
								} else {
									moduleID = tableElement.getAttribute("ModuleID");
								}
								wrkStr = resultSet1.getString("TABLE_NAME").toUpperCase();
								if (moduleID.equals(wrkStr)) {
									tableName = tableElement.getAttribute("Name");
									break;
								}
							}
						}
						bf.append(tableName);
						bf.append("\t");
						bf.append("N/A");
						bf.append("\n");
					} else {
						resultSet2= statement.executeQuery("SELECT COUNT(*) AS COUNT FROM " + resultSet1.getString(3));
						if (resultSet2.next()) {
							bf.append(resultSet1.getString(3));
							bf.append("\t");
							wrkStr = resultSet1.getString(3).toUpperCase();
							tableName = "N/A";
							for (int i = 0; i < tableList.getLength(); i++) {
								tableElement = (org.w3c.dom.Element)tableList.item(i);
								if (tableElement.getAttribute("DB").equals(dbIDList.get(jComboBoxConnection.getSelectedIndex()))) {
									if (tableElement.getAttribute("ModuleID").equals("")) {
										moduleID = tableElement.getAttribute("ID");
									} else {
										moduleID = tableElement.getAttribute("ModuleID");
									}
									if (moduleID.equals(wrkStr)) {
										tableName = tableElement.getAttribute("Name");
										break;
									}
								}
							}
							bf.append(tableName);
							bf.append("\t");
							bf.append(resultSet2.getInt("COUNT"));
							bf.append("\n");
						}
					}
				} catch (Exception e1) {
				}
			}
			bf.append("(");
			calendar = Calendar.getInstance();
			bf.append(formatter.format(calendar.getTime()));
			bf.append(")\n");
			jTextAreaMessage.setText(bf.toString());
		} catch (SQLException ex1) {
			bf.append(jTextAreaMessage.getText());
			bf.append("\n> Listing tables failed.\n");
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

class DialogSQL_jButtonListTables_actionAdapter implements java.awt.event.ActionListener {
	DialogSQL adaptee;
	DialogSQL_jButtonListTables_actionAdapter(DialogSQL adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonListTables_actionPerformed(e);
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
