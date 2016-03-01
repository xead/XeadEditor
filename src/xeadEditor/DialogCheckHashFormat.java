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
import javax.swing.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class DialogCheckHashFormat extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel panelMain = new JPanel();
	private Editor frame_;
	private JLabel jLabelHashFormat = new JLabel();
	private JLabel jLabelValue = new JLabel();
	private JLabel jLabelSalt = new JLabel();
	private JTextField jTextFieldHashFormat = new JTextField();
	private JTextField jTextFieldValue = new JTextField();
	private JTextField jTextFieldSalt = new JTextField();
	private JButton jButtonHash = new JButton();
	private JScrollPane jScrollPaneMessage = new JScrollPane();
	private JTextArea jTextAreaMessage = new JTextArea();
	private String digestAlgorithm = "MD5";
	private int countOfExpand = 1;
	private boolean isValueSalted = false;
	private DigestAdapter digestAdapter = null;
	
	public DialogCheckHashFormat(Editor frame) {
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

		jLabelHashFormat.setText(res.getString("HashFormat"));
		jLabelHashFormat.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelHashFormat.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelHashFormat.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelHashFormat.setBounds(new Rectangle(5, 12, 130, 20));
		jTextFieldHashFormat.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldHashFormat.setBounds(new Rectangle(140, 9, 300, 25));
		jTextFieldHashFormat.setEditable(false);
		
		jLabelValue.setText(res.getString("HashFormatValue"));
		jLabelValue.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelValue.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelValue.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelValue.setBounds(new Rectangle(5, 43, 130, 20));
		jTextFieldValue.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldValue.setBounds(new Rectangle(140, 40, 300, 25));

		jLabelSalt.setText(res.getString("HashFormatSalt"));
		jLabelSalt.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabelSalt.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabelSalt.setHorizontalTextPosition(SwingConstants.LEADING);
		jLabelSalt.setBounds(new Rectangle(5, 74, 130, 20));
		jTextFieldSalt.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldSalt.setBounds(new Rectangle(140, 71, 300, 25));
		jButtonHash.setBounds(new Rectangle(440, 70, 100, 27));
		jButtonHash.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonHash.setText("Hash");
		jButtonHash.addActionListener(new DialogCheckHashFormat_jButtonHash_actionAdapter(this));

		jScrollPaneMessage.setBounds(new Rectangle(10, 102, 530, 70));
		jScrollPaneMessage.getViewport().add(jTextAreaMessage);
		jTextAreaMessage.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextAreaMessage.setLineWrap(true);
		jTextAreaMessage.setWrapStyleWord(true);
		jTextAreaMessage.setOpaque(false);
		jTextAreaMessage.setEditable(false);

		panelMain.setBorder(null);
		panelMain.setLayout(null);
		panelMain.add(jLabelHashFormat);
		panelMain.add(jTextFieldHashFormat);
		panelMain.add(jLabelValue);
		panelMain.add(jTextFieldValue);
		panelMain.add(jLabelSalt);
		panelMain.add(jTextFieldSalt);
		panelMain.add(jButtonHash);
		panelMain.add(jScrollPaneMessage);

		this.setTitle(res.getString("HashFormatTitle"));
		this.setResizable(false);
		this.setPreferredSize(new Dimension(555, 220));
		this.getContentPane().add(panelMain,  BorderLayout.CENTER);
		this.pack();
	}

	public void request(String hashFormat) {
		digestAlgorithm = "MD5";
		countOfExpand = 1;
		isValueSalted = false;
		digestAdapter = null;
		jTextFieldValue.setEditable(true);
		jTextFieldValue.setEnabled(true);
		jTextFieldSalt.setEditable(true);
		jTextFieldSalt.setEnabled(true);
		jButtonHash.setEnabled(true);
		jTextAreaMessage.setText(res.getString("HashFormatMessage1"));

		try {
			if (hashFormat.equals("") || hashFormat.toUpperCase().equals("*DEFAULT")) {
				jTextFieldHashFormat.setText("*Default(MD5;1;false)");
			} else {
				StringTokenizer workTokenizer = new StringTokenizer(hashFormat, ";" );
				digestAlgorithm = workTokenizer.nextToken();
				if (workTokenizer.hasMoreTokens()) {
					countOfExpand = Integer.parseInt(workTokenizer.nextToken());
				}
				if (workTokenizer.hasMoreTokens()) {
					isValueSalted = Boolean.parseBoolean(workTokenizer.nextToken());
				}
				jTextFieldHashFormat.setText(digestAlgorithm + ";" + countOfExpand + ";" + isValueSalted);
			}
			digestAdapter = new DigestAdapter(digestAlgorithm);
		} catch (Exception e) {
			jTextFieldHashFormat.setText(hashFormat);
			jTextFieldValue.setEditable(false);
			jTextFieldValue.setEnabled(false);
			jTextFieldSalt.setEditable(false);
			jTextFieldSalt.setEnabled(false);
			jButtonHash.setEnabled(false);
			jTextAreaMessage.setText(res.getString("HashFormatMessage2"));
		}
		if (!isValueSalted) {
			jTextFieldSalt.setText("");
			jTextFieldSalt.setEditable(false);
			jTextFieldSalt.setEnabled(false);
		}

		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = frame_.getSize();
		Point loc = frame_.getLocation();
		this.getRootPane().setDefaultButton(jButtonHash);
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		this.setVisible(true);
	}

	void jButtonHash_actionPerformed(ActionEvent e) {
		if (digestAdapter != null) {
			String digestedValue = "";
			int count = countOfExpand - 1;
			digestedValue = digestAdapter.digest(jTextFieldValue.getText() + jTextFieldSalt.getText());
			for (int i=0;i<count;i++) {
				digestedValue = digestAdapter.digest(digestedValue + jTextFieldSalt.getText());
			}
			jTextAreaMessage.setText(digestedValue);
		}
	}
}

class DigestAdapter {
	private MessageDigest digest_;

	public DigestAdapter(String algorithm) throws NoSuchAlgorithmException {
		digest_ = MessageDigest.getInstance(algorithm);
	}

	public synchronized String digest(String str) {
		return toHexString(digestArray(str));
	}

	public synchronized byte[] digestArray(String str) {
		byte[] hash = digest_.digest(str.getBytes());
		digest_.reset();
		return hash;
	}

	private String toHexString(byte[] arr) {
		StringBuffer buff = new StringBuffer(arr.length * 2);
		for (int i = 0; i < arr.length; i++) {
			String b = Integer.toHexString(arr[i] & 0xff);
			if (b.length() == 1) {
				buff.append("0");
			}
			buff.append(b);
		}
		return buff.toString();
	}
}

class DialogCheckHashFormat_jButtonHash_actionAdapter implements java.awt.event.ActionListener {
	DialogCheckHashFormat adaptee;
	DialogCheckHashFormat_jButtonHash_actionAdapter(DialogCheckHashFormat adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonHash_actionPerformed(e);
	}
}
