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

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.apache.xerces.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class DialogToListChangesOfFiles extends JDialog {
	private static final long serialVersionUID = 1L;
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	private JPanel panelMain = new JPanel();
	private JScrollPane jScrollPane = new JScrollPane();
	private JProgressBar jProgressBar = new JProgressBar();
	private JButton jButtonStart = new JButton();
	private JButton jButtonCancel = new JButton();
	private JTextArea jTextArea = new JTextArea();
	private JLabel jLabel1 = new JLabel();
	private JTextField jTextFieldImportFileName = new JTextField();
	private JLabel jLabel2 = new JLabel();
	private JTextField jTextFieldImportSystemName = new JTextField();
	private Editor frame_;
	private org.w3c.dom.Document domDocumentOld;
	private org.w3c.dom.Element systemElementOld, systemElementNew;
	private int countOfChanges = 0;
	private StringBuffer buffer;

	public DialogToListChangesOfFiles(Editor frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			frame_ = frame;
			jbInit();
			pack();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public DialogToListChangesOfFiles(Editor frame) {
		this(frame, "", true);
	}

	private void jbInit() throws Exception {
		this.setResizable(false);
		this.setTitle(res.getString("FileDiff"));

		jLabel1.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setText(res.getString("FileDiffTargetFile"));
		jLabel1.setBounds(new Rectangle(5, 12, 170, 20));
		jTextFieldImportFileName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldImportFileName.setBounds(new Rectangle(180, 9, 650, 25));
		jTextFieldImportFileName.setEditable(false);

		jLabel2.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel2.setText(res.getString("FileDiffTargetSystem"));
		jLabel2.setBounds(new Rectangle(5, 43, 170, 20));
		jTextFieldImportSystemName.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextFieldImportSystemName.setBounds(new Rectangle(180, 40, 650, 25));
		jTextFieldImportSystemName.setEditable(false);

		jScrollPane.setBounds(new Rectangle(9, 73, 822, 458));
		jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		jScrollPane.getViewport().add(jTextArea);
		jTextArea.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jTextArea.setEditable(false);
		jTextArea.setLineWrap(true);
		jTextArea.setBackground(SystemColor.control);
		jTextArea.setBorder(null);

		jProgressBar.setBounds(new Rectangle(30, 540, 640, 30));
		jProgressBar.setStringPainted(true);
		jProgressBar.setVisible(false);

		jButtonStart.setBounds(new Rectangle(30, 540, 640, 30));
		jButtonStart.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonStart.setText(res.getString("Start"));
		jButtonStart.addActionListener(new DialogToListChangesOfFiles_jButtonStart_actionAdapter(this));

		jButtonCancel.setBounds(new Rectangle(700, 540, 110, 30));
		jButtonCancel.setFont(new java.awt.Font(frame_.mainFontName, 0, Editor.MAIN_FONT_SIZE));
		jButtonCancel.setText(res.getString("Close"));
		jButtonCancel.addActionListener(new DialogToListChangesOfFiles_jButtonCancel_actionAdapter(this));

		panelMain.setLayout(null);
		panelMain.setPreferredSize(new Dimension(840, 583));
		panelMain.setBorder(BorderFactory.createEtchedBorder());
		panelMain.add(jLabel1, null);
		panelMain.add(jLabel2, null);
		panelMain.add(jTextFieldImportFileName, null);
		panelMain.add(jTextFieldImportSystemName, null);
		panelMain.add(jScrollPane, null);
		panelMain.add(jProgressBar, null);
		panelMain.add(jButtonStart, null);
		panelMain.add(jButtonCancel, null);

		this.getContentPane().add(panelMain, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(jButtonStart);

		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getPreferredSize();
		this.setLocation((scrSize.width - dlgSize.width)/2 , (scrSize.height - dlgSize.height)/2);
		this.pack();
	}

	public void request(String fileName) {
		try {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new FileInputStream(fileName)));
			domDocumentOld = parser.getDocument();

			NodeList elementList = domDocumentOld.getElementsByTagName("System");
			systemElementOld = (org.w3c.dom.Element)elementList.item(0);
			elementList = frame_.getDomDocument().getElementsByTagName("System");
			systemElementNew = (org.w3c.dom.Element)elementList.item(0);

			float importFileFormat = Float.parseFloat(systemElementOld.getAttribute("FormatVersion"));
			float appliFormat = Float.parseFloat(DialogAbout.FORMAT_VERSION);
			if (importFileFormat > appliFormat) {
				JOptionPane.showMessageDialog(null, "Format versions of files are not the same. Process is canceled.");
			} else {
				jTextFieldImportFileName.setText(fileName);
				jTextFieldImportSystemName.setText(systemElementOld.getAttribute("Name") + " V" + systemElementOld.getAttribute("Version"));
				jTextArea.setText(res.getString("FileDiffMessage"));

				jProgressBar.setValue(0);
				jButtonStart.setEnabled(true);
				this.getRootPane().setDefaultButton(jButtonStart);
				super.setVisible(true);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "XML format of the file is invalid. Process is canceled.");
		}
	}

	void jButtonStart_actionPerformed(ActionEvent e) {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));

			jButtonStart.setVisible(false);
			jProgressBar.setVisible(true);
			setupProgressMaxValue();

			buffer = new StringBuffer();
			countOfChanges = 0;
			
			listChangesOfBasicInfo();
			listChangesOfMenu();
			listChangesOfSubsystem();
			listChangesOfTable();
			listChangesOfFunction();

		} finally {
			jTextArea.setText(countOfChanges + res.getString("FileDiffMessage10") + buffer.toString());
			jTextArea.setCaretPosition(0);

			jProgressBar.setVisible(false);
			jButtonStart.setVisible(true);
			jButtonStart.setEnabled(false);
			this.getRootPane().setDefaultButton(jButtonCancel);

			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	void setupProgressMaxValue() {
		NodeList workElementList;
		int countOfElementsProcessed = 0;

		workElementList = systemElementOld.getElementsByTagName("SubDB");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("SubDB");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementOld.getElementsByTagName("PrintFont");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("PrintFont");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementOld.getElementsByTagName("MaintenanceLog");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("MaintenanceLog");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();

		workElementList = systemElementOld.getElementsByTagName("Menu");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Menu");

		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementOld.getElementsByTagName("Subsystem");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Subsystem");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();

		workElementList = systemElementOld.getElementsByTagName("Table");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Table");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementOld.getElementsByTagName("Field");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Field");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementOld.getElementsByTagName("Key");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Key");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementOld.getElementsByTagName("Refer");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Refer");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementOld.getElementsByTagName("Script");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Script");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();

		workElementList = systemElementOld.getElementsByTagName("Function");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();
		workElementList = systemElementNew.getElementsByTagName("Function");
		countOfElementsProcessed = countOfElementsProcessed + workElementList.getLength();

		jProgressBar.setMaximum(countOfElementsProcessed + 3); //System info counts 3//
	}

	void listChangesOfBasicInfo() {
		NodeList newElementList, oldElementList;
		String tagName;
		ArrayList<String> attrList = new ArrayList<String>();
		int workCount = countOfChanges;
		String label = res.getString("System");
		buffer.append("\n\n<" + res.getString("SystemInfo") + ">");

		/////////////////////////
		// System Descriptions //
		/////////////////////////
		compareNewAndOldElements(systemElementNew, systemElementOld, "Name", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "Version", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "Remarks", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "FormatVersion", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "DatabaseName", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "DatabaseUser", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "DatabasePassword", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "DBCP", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "AppServerName", label);
		jProgressBar.setValue(jProgressBar.getValue() + 1);
		jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
		compareNewAndOldElements(systemElementNew, systemElementOld, "VariantsTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "UserTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "UserFilterValueTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "NumberingTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "UserVariantsTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SessionTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SessionDetailTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "TaxTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "CalendarTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "CurrencyTable", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "CurrencyDetailTable", label);
		jProgressBar.setValue(jProgressBar.getValue() + 1);
		jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
		compareNewAndOldElements(systemElementNew, systemElementOld, "AutoConnectToEdit", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "EditorUser", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "EditorUserPassword", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "DriverVMOptions", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SkipPreload", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SkipModuleCheck", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SmtpHost", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SmtpPort", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SmtpUser", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SmtpPassword", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SmtpAdminEmail", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "ImageFileFolder", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "OutputFolder", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "SystemFont", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "HashFormat", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "WelcomePageURL", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "DateFormat", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "LoginScript", label);
		compareNewAndOldElements(systemElementNew, systemElementOld, "ScriptFunctions", label);
		jProgressBar.setValue(jProgressBar.getValue() + 1);
		jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());

		///////////
		// SubDB //
		///////////
		tagName = "SubDB";
		attrList.clear();
		attrList.add("Name");
		attrList.add("User");
		attrList.add("Password");
		attrList.add("DBCP");
		attrList.add("Description");
		oldElementList = systemElementOld.getElementsByTagName(tagName);
		newElementList = systemElementNew.getElementsByTagName(tagName);
		checkIfNewElementChangedOrAdded(newElementList, oldElementList, attrList, tagName, "Description");
		checkIfNewElementDeleted(newElementList, oldElementList, tagName, "Description");

		////////////////
		// Print Font //
		////////////////
		tagName = "PrintFont";
		attrList.clear();
		attrList.add("FontName");
		attrList.add("PDFFontName");
		attrList.add("PDFEncoding");
		oldElementList = systemElementOld.getElementsByTagName(tagName);
		newElementList = systemElementNew.getElementsByTagName(tagName);
		checkIfNewElementChangedOrAdded(newElementList, oldElementList, attrList, tagName, "FontName");
		checkIfNewElementDeleted(newElementList, oldElementList, tagName, "FontName");

		/////////////////////
		// Maintenance Log //
		/////////////////////
		tagName = "MaintenanceLog";
		attrList.clear();
		attrList.add("Headder");
		attrList.add("Descriptions");
		oldElementList = systemElementOld.getElementsByTagName(tagName);
		newElementList = systemElementNew.getElementsByTagName(tagName);
		checkIfNewElementChangedOrAdded(newElementList, oldElementList, attrList, tagName, "Headder");
		checkIfNewElementDeleted(newElementList, oldElementList, tagName, "Headder");

		///////////////////////////////////////////////////
		// Set no changes message if with no differences //
		///////////////////////////////////////////////////
		if (countOfChanges == workCount) {
			buffer.append("\n" + res.getString("FileDiffNone"));
		}
	}

	void listChangesOfMenu() {
		NodeList newElementList, oldElementList;
		String tagName;
		ArrayList<String> attrList = new ArrayList<String>();
		int workCount = countOfChanges;
		buffer.append("\n\n<" + res.getString("Menu") + ">");

		///////////
		// Menus //
		///////////
		tagName = "Menu";
		attrList.clear();
		attrList.add("Name");
		attrList.add("CrossCheckersToBeLoaded");
		attrList.add("FunctionsToBeLoaded");
		attrList.add("HelpURL");
		oldElementList = systemElementOld.getElementsByTagName(tagName);
		newElementList = systemElementNew.getElementsByTagName(tagName);
		checkIfNewElementChangedOrAdded(newElementList, oldElementList, attrList, tagName, "ID");
		checkIfNewElementDeleted(newElementList, oldElementList, tagName, "ID");

		///////////////////////////////////////////////////
		// Set no changes message if with no differences //
		///////////////////////////////////////////////////
		if (countOfChanges == workCount) {
			buffer.append("\n" + res.getString("FileDiffNone"));
		}
	}

	void listChangesOfSubsystem() {
		NodeList newElementList, oldElementList;
		String tagName;
		ArrayList<String> attrList = new ArrayList<String>();
		int workCount = countOfChanges;
		buffer.append("\n\n<" + res.getString("Subsystem") + ">");

		////////////////
		// Subsystems //
		////////////////
		tagName = "Subsystem";
		attrList.clear();
		attrList.add("Name");
		attrList.add("Remarks");
		oldElementList = systemElementOld.getElementsByTagName(tagName);
		newElementList = systemElementNew.getElementsByTagName(tagName);
		checkIfNewElementChangedOrAdded(newElementList, oldElementList, attrList, tagName, "ID");
		checkIfNewElementDeleted(newElementList, oldElementList, tagName, "ID");

		///////////////////////////////////////////////////
		// Set no changes message if with no differences //
		///////////////////////////////////////////////////
		if (countOfChanges == workCount) {
			buffer.append("\n" + res.getString("FileDiffNone"));
		}
	}

	void listChangesOfTable() {
		NodeList newElementList, oldElementList;
		String tagName;
		ArrayList<String> attrList = new ArrayList<String>();
		int workCount = countOfChanges;
		buffer.append("\n\n<" + res.getString("Table") + ">");

		////////////
		// Tables //
		////////////
		tagName = "Table";
		attrList.clear();
		attrList.add("Name");
		attrList.add("DB");
		attrList.add("ModuleID");
		attrList.add("Remarks");
		attrList.add("SubsystemID");
		attrList.add("UpdateCounter");
		attrList.add("DetailRowNumberAuto");
		oldElementList = systemElementOld.getElementsByTagName(tagName);
		newElementList = systemElementNew.getElementsByTagName(tagName);
		checkIfNewElementChangedOrAdded(newElementList, oldElementList, attrList, tagName, "ID");
		checkIfNewElementDeleted(newElementList, oldElementList, tagName, "ID");

		///////////////////////////////////////////////////
		// Set no changes message if with no differences //
		///////////////////////////////////////////////////
		if (countOfChanges == workCount) {
			buffer.append("\n" + res.getString("FileDiffNone"));
		}
	}

	void listChangesOfFunction() {
		NodeList newElementList, oldElementList;
		String tagName;
		ArrayList<String> attrList = new ArrayList<String>();
		int workCount = countOfChanges;
		buffer.append("\n\n<" + res.getString("FunctionDefinition") + ">");

		///////////////
		// Functions //
		///////////////
		tagName = "Function";
		attrList.clear();
		attrList.add("Name");
		attrList.add("Type");
		attrList.add("SubsystemID");
		oldElementList = systemElementOld.getElementsByTagName(tagName);
		newElementList = systemElementNew.getElementsByTagName(tagName);
		checkIfNewElementChangedOrAdded(newElementList, oldElementList, attrList, tagName, "ID");
		checkIfNewElementDeleted(newElementList, oldElementList, tagName, "ID");

		///////////////////////////////////////////////////
		// Set no changes message if with no differences //
		///////////////////////////////////////////////////
		if (countOfChanges == workCount) {
			buffer.append("\n" + res.getString("FileDiffNone"));
		}
	}

	void compareNewAndOldElements(org.w3c.dom.Element elementNew, org.w3c.dom.Element elementOld, String attribute, String elementLabel) {
		if (!elementNew.getAttribute(attribute).equals(elementOld.getAttribute(attribute))) {
			String attrName = getElementNameByTagName(attribute);
			countOfChanges++;
			if (attribute.equals("Remarks")
					|| attribute.equals("Descriptions")
					|| attribute.equals("Text")
					|| attribute.contains("Script")) {
				buffer.append("\n" + countOfChanges + "."
						+ elementLabel + res.getString("FileDiffMessage02")
						+ attrName + res.getString("FileDiffMessage01"));
			} else {
				buffer.append("\n" + countOfChanges + "."
						+ elementLabel + res.getString("FileDiffMessage02")
						+ attrName + res.getString("FileDiffMessage03")
						+ elementOld.getAttribute(attribute) + res.getString("FileDiffMessage04")
						+ elementNew.getAttribute(attribute) + res.getString("FileDiffMessage05"));
			}
		}
	}

	void checkIfNewElementChangedOrAdded(NodeList newElementList, NodeList oldElementList, ArrayList<String> attrList, String tagName, String nameAttr) {
		org.w3c.dom.Element newElement, oldElement;
		boolean isNotFound;
		String elementLabel;
		String elementName = getElementNameByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			newElement = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElement = (org.w3c.dom.Element)oldElementList.item(j);
				if (oldElement.getAttribute(nameAttr).equals(newElement.getAttribute(nameAttr))) {
					for (int k = 0; k < attrList.size(); k++) {
						elementLabel = elementName + res.getString("FileDiffMessage06")
								+ newElement.getAttribute(nameAttr) + res.getString("FileDiffMessage07");
						compareNewAndOldElements(newElement, oldElement, attrList.get(k), elementLabel);
					}
					isNotFound = false;
					if (tagName.equals("Table")) {
						checkTableDetails(oldElement, newElement);
					}
					if (tagName.equals("Function")) {
						checkFunctionDetails(oldElement, newElement);
					}
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ elementName + res.getString("FileDiffMessage06")
						+ newElement.getAttribute(nameAttr)
						+ res.getString("FileDiffMessage07")
						+ res.getString("FileDiffMessage08"));
			}
		}
	}

	void checkIfNewElementDeleted(NodeList newElementList, NodeList oldElementList, String tagName, String nameAttr) {
		org.w3c.dom.Element newElement, oldElement;
		boolean isNotFound;
		String elementName = getElementNameByTagName(tagName);
		for (int i = 0; i < oldElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			oldElement = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElement = (org.w3c.dom.Element)newElementList.item(j);
				if (oldElement.getAttribute(nameAttr).equals(newElement.getAttribute(nameAttr))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ elementName + res.getString("FileDiffMessage06")
						+ oldElement.getAttribute(nameAttr)
						+ res.getString("FileDiffMessage07")
						+ res.getString("FileDiffMessage09"));
			}
		}
	}

	void checkTableDetails(org.w3c.dom.Element oldElement, org.w3c.dom.Element newElement) {
		NodeList oldElementList, newElementList;
		ArrayList<String> attrList = new ArrayList<String>();
		org.w3c.dom.Element newElementWork, oldElementWork;
		boolean isNotFound;
		String tagName, elementName, elementLabel;
		String tableLabel = res.getString("Table")
					+ res.getString("FileDiffMessage06")
					+ newElement.getAttribute("ID") + res.getString("FileDiffMessage07");

		////////////
		// Fields //
		////////////
		tagName = "Field";
		elementName = getElementNameByTagName(tagName);
		attrList.clear();
		attrList.add("Name");
		attrList.add("ColumnName");
		attrList.add("Order");
		attrList.add("Remarks");
		attrList.add("Type");
		attrList.add("Size");
		attrList.add("Decimal");
		attrList.add("Nullable");
		attrList.add("NoUpdate");
		attrList.add("TypeOptions");
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (oldElementWork.getAttribute("ID").equals(newElementWork.getAttribute("ID"))) {
					elementLabel = tableLabel+ res.getString("FileDiffMessage02")
							+ elementName + res.getString("FileDiffMessage06")
							+ newElementWork.getAttribute("ID")
							+ " " + newElementWork.getAttribute("Name")
							+ res.getString("FileDiffMessage07");
					for (int k = 0; k < attrList.size(); k++) {
						compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
					}
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ tableLabel+ res.getString("FileDiffMessage02")
						+ elementName + res.getString("FileDiffMessage06")
						+ newElementWork.getAttribute("ID") + " " + newElementWork.getAttribute("Name")
						+ res.getString("FileDiffMessage07") + res.getString("FileDiffMessage08"));
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (oldElementWork.getAttribute("Name").equals(newElementWork.getAttribute("Name"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ tableLabel+ res.getString("FileDiffMessage02")
						+ elementName + res.getString("FileDiffMessage06")
						+ oldElementWork.getAttribute("ID") + " " + oldElementWork.getAttribute("Name")
						+ res.getString("FileDiffMessage07") + res.getString("FileDiffMessage09"));
			}
		}

		//////////
		// Keys //
		//////////
		tagName = "Key";
		elementName = getElementNameByTagName(tagName);
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (newElementWork.getAttribute("Type").equals("PK") && oldElementWork.getAttribute("Type").equals("PK")) {
					if (!oldElementWork.getAttribute("Fields").equals(newElementWork.getAttribute("Fields"))) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ tableLabel+ res.getString("FileDiffMessage02")
								+ elementName + "[PK]" + res.getString("FileDiffMessage03")
								+ oldElementWork.getAttribute("Fields") + res.getString("FileDiffMessage04")
								+ newElementWork.getAttribute("Fields") + res.getString("FileDiffMessage05"));
					}
					isNotFound = false;
					break;
				} else {
					if (oldElementWork.getAttribute("Type").equals(newElementWork.getAttribute("Type"))
						&& oldElementWork.getAttribute("Fields").equals(newElementWork.getAttribute("Fields"))) {
						isNotFound = false;
						break;
					}
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ tableLabel+ res.getString("FileDiffMessage02")
						+ elementName + "[" + newElementWork.getAttribute("Type") + "]"
						+ "{" + newElementWork.getAttribute("Fields") + "}"
						+ res.getString("FileDiffMessage08"));
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (oldElementWork.getAttribute("Type").equals(newElementWork.getAttribute("Type"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ tableLabel+ res.getString("FileDiffMessage02")
						+ elementName + "[" + oldElementWork.getAttribute("Type") + "]"
						+ "{" + oldElementWork.getAttribute("Fields") + "}"
						+ res.getString("FileDiffMessage09"));
			}
		}

		/////////////////
		// Join Tables //
		/////////////////
		tagName = "Refer";
		elementName = getElementNameByTagName(tagName);
		attrList.clear();
		attrList.add("Order");
		attrList.add("ToKeyFields");
		attrList.add("WithKeyFields");
		attrList.add("Fields");
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (newElementWork.getAttribute("ToTable").equals(oldElementWork.getAttribute("ToTable"))
						&& newElementWork.getAttribute("TableAlias").equals(oldElementWork.getAttribute("TableAlias"))) {
					if (newElementWork.getAttribute("TableAlias").equals("")) {
						elementLabel = tableLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("ToTable")
								+ res.getString("FileDiffMessage07");
					} else {
						elementLabel = tableLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("TableAlias")
								+ res.getString("FileDiffMessage07");
					}
					for (int k = 0; k < attrList.size(); k++) {
						compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
					}
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				if (newElementWork.getAttribute("TableAlias").equals("")) {
					buffer.append("\n" + countOfChanges + "."
							+ tableLabel+ res.getString("FileDiffMessage02")
							+ elementName + res.getString("FileDiffMessage06")
							+ newElementWork.getAttribute("ToTable")
							+ res.getString("FileDiffMessage07")
							+ res.getString("FileDiffMessage08"));
				} else {
					buffer.append("\n" + countOfChanges + "."
							+ tableLabel+ res.getString("FileDiffMessage02")
							+ elementName + res.getString("FileDiffMessage06")
							+ newElementWork.getAttribute("TableAlias")
							+ res.getString("FileDiffMessage07")
							+ res.getString("FileDiffMessage08"));
				}
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (newElementWork.getAttribute("ToTable").equals(oldElementWork.getAttribute("ToTable"))
						&& newElementWork.getAttribute("TableAlias").equals(oldElementWork.getAttribute("TableAlias"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				if (oldElementWork.getAttribute("TableAlias").equals("")) {
					buffer.append("\n" + countOfChanges + "."
							+ tableLabel+ res.getString("FileDiffMessage02")
							+ elementName + res.getString("FileDiffMessage06")
							+ oldElementWork.getAttribute("ToTable")
							+ res.getString("FileDiffMessage07")
							+ res.getString("FileDiffMessage08"));
				} else {
					buffer.append("\n" + countOfChanges + "."
							+ tableLabel+ res.getString("FileDiffMessage02")
							+ elementName + res.getString("FileDiffMessage06")
							+ oldElementWork.getAttribute("TableAlias")
							+ res.getString("FileDiffMessage07")
							+ res.getString("FileDiffMessage08"));
				}
			}
		}

		/////////////
		// Scripts //
		/////////////
		tagName = "Script";
		elementName = getElementNameByTagName(tagName);
		attrList.clear();
		attrList.add("Order");
		attrList.add("EventP");
		attrList.add("EventR");
		attrList.add("Hold");
		attrList.add("Text");
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (newElementWork.getAttribute("Name").equals(oldElementWork.getAttribute("Name"))) {
					elementLabel = tableLabel+ res.getString("FileDiffMessage02")
							+ elementName + res.getString("FileDiffMessage06")
							+ newElementWork.getAttribute("Name")
							+ res.getString("FileDiffMessage07");
					for (int k = 0; k < attrList.size(); k++) {
						compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
					}
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ tableLabel+ res.getString("FileDiffMessage02")
						+ elementName + res.getString("FileDiffMessage06")
						+ newElementWork.getAttribute("Name")
						+ res.getString("FileDiffMessage07")
						+ res.getString("FileDiffMessage08"));
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			jProgressBar.setValue(jProgressBar.getValue() + 1);
			jProgressBar.paintImmediately(0,0,jProgressBar.getWidth(),jProgressBar.getHeight());
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (newElementWork.getAttribute("Name").equals(oldElementWork.getAttribute("Name"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				countOfChanges++;
				buffer.append("\n" + countOfChanges + "."
						+ tableLabel+ res.getString("FileDiffMessage02")
						+ elementName + res.getString("FileDiffMessage06")
						+ oldElementWork.getAttribute("Name")
						+ res.getString("FileDiffMessage07")
						+ res.getString("FileDiffMessage08"));
			}
		}
	}

	void checkFunctionDetails(org.w3c.dom.Element oldElement, org.w3c.dom.Element newElement) {
		NodeList oldElementList, newElementList;
		String tagName, elementName, elementLabel;
		org.w3c.dom.Element newElementWork, oldElementWork;
		ArrayList<String> attrList = new ArrayList<String>();
		boolean isNotFound;
		String functionLabel = res.getString("FunctionDefinition")
				+ res.getString("FileDiffMessage06")
				+ newElement.getAttribute("ID") + res.getString("FileDiffMessage07");

		////////////////////////////////////////
		// Do checking if type is not changed //
		////////////////////////////////////////
		if (newElement.getAttribute("Type").equals(oldElement.getAttribute("Type"))) {

			/////////////////////////
			// Function Type XF000 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF000")) {
				attrList.clear();
				attrList.add("TimerOption");
				attrList.add("TimerMessage");
				attrList.add("ServiceName");
				attrList.add("Script");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			//////////////////////////////////
			// Function Type XF100 or XF110 //
			//////////////////////////////////
			if (newElement.getAttribute("Type").equals("XF100")
					|| newElement.getAttribute("Type").equals("XF110")) {
				attrList.clear();
				attrList.add("PrimaryTable");
				attrList.add("KeyFields");
				attrList.add("OrderBy");
				attrList.add("FixedWhere");
				attrList.add("InitialListing");
				attrList.add("Size");
				attrList.add("InitialReadCount");
				attrList.add("InitialMsg");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			/////////////////////////
			// Function Type XF100 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF100")) {
				attrList.clear();
				attrList.add("DetailFunction");
				attrList.add("ParmType");
				attrList.add("ParmAdditional");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			/////////////////////////
			// Function Type XF110 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF110")) {
				attrList.clear();
				attrList.add("BatchTable");
				attrList.add("BatchKeyFields");
				attrList.add("BatchWithKeyFields");
				attrList.add("BatchRecordFunction");
				attrList.add("BatchRecordFunctionMsg");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			/////////////////////////
			// Function Type XF200 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF200")) {
				attrList.clear();
				attrList.add("PrimaryTable");
				attrList.add("KeyFields");
				attrList.add("FixedWhere");
				attrList.add("FunctionAfterInsert");
				attrList.add("Size");
				attrList.add("UpdateOnly");
				attrList.add("InitialMsg");
				attrList.add("DataName");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			/////////////////////////
			// Function Type XF290 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF290")) {
				attrList.clear();
				attrList.add("PrimaryTable");
				attrList.add("KeyFields");
				attrList.add("PageSize");
				attrList.add("Direction");
				attrList.add("Margins");
				attrList.add("WithPangeNumber");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			/////////////////////////
			// Function Type XF300 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF300")) {
				attrList.clear();
				attrList.add("HeaderTable");
				attrList.add("HeaderKeyFields");
				attrList.add("FixedWhere");
				attrList.add("HeaderFunction");
				attrList.add("HeaderParmAdditional");
				attrList.add("Size");
				attrList.add("StructureTable");
				attrList.add("StructureUpperKeys");
				attrList.add("StructureChildKeys");
				attrList.add("StructureOrderBy");
				attrList.add("StructureRootText");
				attrList.add("StructureNodeText");
				attrList.add("StructureNodeDefaultIcon");
				attrList.add("StructureNodeIconsFieldID");
				attrList.add("StructureNodeIconsFieldValues");
				attrList.add("StructureNodeIcons");
				attrList.add("StructureViewWidth");
				attrList.add("StructureViewTitle");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			/////////////////////////
			// Function Type XF310 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF310")) {
				attrList.clear();
				attrList.add("HeaderTable");
				attrList.add("HeaderKeyFields");
				attrList.add("HeaderFixedWhere");
				attrList.add("DetailTable");
				attrList.add("DetailKeyFields");
				attrList.add("DetailFixedWhere");
				attrList.add("DetailOrderBy");
				attrList.add("Size");
				attrList.add("StartInAddMode");
				attrList.add("InitialMsg");
//				attrList.add("AddRowListTable");
//				attrList.add("AddRowListWithFields");
//				attrList.add("AddRowListWithHeaderFields");
//				attrList.add("AddRowListOrderBy");
//				attrList.add("AddRowListWhere");
//				attrList.add("AddRowListInitialMsg");
//				attrList.add("AddRowListReturnDataSources");
//				attrList.add("AddRowListReturnToDetailDataSources");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			/////////////////////////
			// Function Type XF390 //
			/////////////////////////
			if (newElement.getAttribute("Type").equals("XF390")) {
				attrList.clear();
				attrList.add("HeaderTable");
				attrList.add("HeaderKeyFields");
				attrList.add("HeaderFixedWhere");
				attrList.add("DetailTable");
				attrList.add("DetailKeyFields");
				attrList.add("DetailFixedWhere");
				attrList.add("DetailOrderBy");
				attrList.add("PageSize");
				attrList.add("Direction");
				attrList.add("Margins");
				attrList.add("WithPageNumber");
				for (int i = 0; i < attrList.size(); i++) {
					compareNewAndOldElements(newElement, oldElement, attrList.get(i), functionLabel);
				}
			}

			///////////////////////////////////////////////////
			// Sub-Elements : Columns of XF100, XF110, XF310 //
			///////////////////////////////////////////////////
			if ((newElement.getAttribute("Type").equals("XF100") && newElement.getAttribute("PrimaryTable").equals(oldElement.getAttribute("PrimaryTable")))
					|| (newElement.getAttribute("Type").equals("XF110") && newElement.getAttribute("PrimaryTable").equals(oldElement.getAttribute("PrimaryTable")))
					|| (newElement.getAttribute("Type").equals("XF310") && newElement.getAttribute("DetailTable").equals(oldElement.getAttribute("DetailTable")))) {
				tagName = "Column";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Order");
				attrList.add("FieldOptions");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("DataSource")
									+ res.getString("FileDiffMessage07");
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ oldElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			////////////////////////////////////////////
			// Sub-Elements : Filters of XF100, XF110 //
			////////////////////////////////////////////
			if ((newElement.getAttribute("Type").equals("XF100") && newElement.getAttribute("PrimaryTable").equals(oldElement.getAttribute("PrimaryTable")))
					|| (newElement.getAttribute("Type").equals("XF110") && newElement.getAttribute("PrimaryTable").equals(oldElement.getAttribute("PrimaryTable")))) {
				tagName = "Filter";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Order");
				attrList.add("FieldOptions");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("DataSource")
									+ res.getString("FileDiffMessage07");
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ oldElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			//////////////////////////////////////////
			// Sub-Elements : Batch Fields of XF110 //
			//////////////////////////////////////////
			if (newElement.getAttribute("Type").equals("XF110") && newElement.getAttribute("BatchTable").equals(oldElement.getAttribute("BatchTable"))) {
				tagName = "BatchField";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Order");
				attrList.add("FieldOptions");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("DataSource")
									+ res.getString("FileDiffMessage07");
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ oldElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			//////////////////////////////////////////////////
			// Sub-Elements : Fields of XF200, XF300, XF310 //
			//////////////////////////////////////////////////
			if ((newElement.getAttribute("Type").equals("XF200") && newElement.getAttribute("PrimaryTable").equals(oldElement.getAttribute("PrimaryTable")))
				|| (newElement.getAttribute("Type").equals("XF300") && newElement.getAttribute("HeaderTable").equals(oldElement.getAttribute("HeaderTable")))
				|| (newElement.getAttribute("Type").equals("XF310") && newElement.getAttribute("HeaderTable").equals(oldElement.getAttribute("HeaderTable")))) {
				tagName = "Field";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Order");
				attrList.add("FieldOptions");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("DataSource")
									+ res.getString("FileDiffMessage07");
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ oldElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			////////////////////////////////////////
			// Sub-Elements : Tab Fields of XF200 //
			////////////////////////////////////////
			if (newElement.getAttribute("Type").equals("XF200") && newElement.getAttribute("PrimaryTable").equals(oldElement.getAttribute("PrimaryTable"))) {
				tagName = "Tab";
				elementName = getElementNameByTagName(tagName);
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("Caption").equals(newElementWork.getAttribute("Caption"))) {
							if (!oldElementWork.isEqualNode(newElementWork)) {
								countOfChanges++;
								buffer.append("\n" + countOfChanges + "."
										+ functionLabel+ res.getString("FileDiffMessage02")
										+ elementName + res.getString("FileDiffMessage06")
										+ newElementWork.getAttribute("Caption") + res.getString("FileDiffMessage07")
										+ res.getString("FileDiffMessage11"));
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("Caption") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("Caption").equals(newElementWork.getAttribute("Caption"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ oldElementWork.getAttribute("Caption") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			//////////////////////////////////////////////////////////
			// Sub-Elements : Buttons of XF100, XF110, XF200, XF310 //
			//////////////////////////////////////////////////////////
			if (newElement.getAttribute("Type").equals("XF100")
					|| newElement.getAttribute("Type").equals("XF110")
					|| newElement.getAttribute("Type").equals("XF200")
					|| newElement.getAttribute("Type").equals("XF310")) {	
				tagName = "Button";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Position");
				attrList.add("Caption");
				attrList.add("Action");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
//						if (oldElementWork.getAttribute("Number").equals(newElementWork.getAttribute("Number"))
//								&& oldElementWork.getAttribute("Action").equals(newElementWork.getAttribute("Action"))) {
						if (oldElementWork.getAttribute("Number").equals(newElementWork.getAttribute("Number"))) {
							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ "F" + newElementWork.getAttribute("Number")
									+ res.getString("FileDiffMessage07");
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ "F" + newElementWork.getAttribute("Number") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("Number").equals(newElementWork.getAttribute("Number"))
								&& oldElementWork.getAttribute("Action").equals(newElementWork.getAttribute("Action"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ "F" + oldElementWork.getAttribute("Number") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			/////////////////////////////////////
			// Sub-Elements : Phrases of XF290 //
			/////////////////////////////////////
			if ((newElement.getAttribute("Type").equals("XF290")) && (newElement.getAttribute("PrimaryTable").equals(oldElement.getAttribute("PrimaryTable")))) {
				tagName = "Phrase";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Block");
				attrList.add("Order");
				attrList.add("Alignment");
				attrList.add("AlignmentMargin");
				attrList.add("SpacingAfter");
				attrList.add("FontID");
				attrList.add("FontSize");
				attrList.add("FontStyle");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("Value").equals(newElementWork.getAttribute("Value"))) {
							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("Value")
									+ res.getString("FileDiffMessage07");
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("Value") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("Value").equals(newElementWork.getAttribute("Value"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ oldElementWork.getAttribute("Value") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			////////////////////////////////////////////
			// Sub-Elements : Header Phrases of XF390 //
			////////////////////////////////////////////
			if ((newElement.getAttribute("Type").equals("XF390")) && (newElement.getAttribute("HeaderTable").equals(oldElement.getAttribute("HeaderTable")))) {
				tagName = "HeaderPhrase";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Block");
				attrList.add("Order");
				attrList.add("Alignment");
				attrList.add("AlignmentMargin");
				attrList.add("SpacingAfter");
				attrList.add("FontID");
				attrList.add("FontSize");
				attrList.add("FontStyle");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("Value").equals(newElementWork.getAttribute("Value"))) {
							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("Value")
									+ res.getString("FileDiffMessage07");
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ newElementWork.getAttribute("Value") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage08"));
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("Value").equals(newElementWork.getAttribute("Value"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						buffer.append("\n" + countOfChanges + "."
								+ functionLabel+ res.getString("FileDiffMessage02")
								+ elementName + res.getString("FileDiffMessage06")
								+ oldElementWork.getAttribute("Value") + res.getString("FileDiffMessage07")
								+ res.getString("FileDiffMessage09"));
					}
				}
			}

			/////////////////////////////////////////
			// Sub-Elements : Detail Tabs of XF300 //
			/////////////////////////////////////////
			if (newElement.getAttribute("Type").equals("XF300") && newElement.getAttribute("HeaderTable").equals(oldElement.getAttribute("HeaderTable"))) {
				tagName = "Detail";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Caption");
				attrList.add("Order");
				attrList.add("HeaderKeyFields");
				attrList.add("OrderBy");
				attrList.add("FixedWhere");
				attrList.add("DetailFunction");
				attrList.add("ParmType");
				attrList.add("ParmAdditional");
				attrList.add("InitialReadCount");
				attrList.add("InitialListing");
				attrList.add("InitialMsg");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("Table").equals(newElementWork.getAttribute("Table"))
								&& oldElementWork.getAttribute("KeyFields").equals(newElementWork.getAttribute("KeyFields"))) {
							if (newElementWork.getAttribute("KeyFields").equals("")) {
								elementLabel = functionLabel+ res.getString("FileDiffMessage02")
										+ elementName + res.getString("FileDiffMessage06")
										+ newElementWork.getAttribute("Table") + "{PK}"
										+ res.getString("FileDiffMessage07");
							} else {
								elementLabel = functionLabel+ res.getString("FileDiffMessage02")
										+ elementName + res.getString("FileDiffMessage06")
										+ newElementWork.getAttribute("Table")
										+ "{" + newElementWork.getAttribute("KeyFields") + "}"
										+ res.getString("FileDiffMessage07");
							}
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							compareSubElementsOfXF300Details(newElementWork, oldElementWork, elementLabel);
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						if (newElementWork.getAttribute("KeyFields").equals("")) {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("Table") + "{PK}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage08"));
						} else {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("Table")
									+ "{" + newElementWork.getAttribute("KeyFields") + "}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage08"));
						}
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("Table").equals(newElementWork.getAttribute("Table"))
								&& oldElementWork.getAttribute("KeyFields").equals(newElementWork.getAttribute("KeyFields"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						if (oldElementWork.getAttribute("KeyFields").equals("")) {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ oldElementWork.getAttribute("Table") + "{PK}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage09"));
						} else {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ oldElementWork.getAttribute("Table")
									+ "{" + oldElementWork.getAttribute("KeyFields") + "}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage09"));
						}
					}
				}
			}

//			////////////////////////////////////////////////
//			// Sub-Elements : AddRowList Columns of XF310 //
//			////////////////////////////////////////////////
//			if ((newElement.getAttribute("Type").equals("XF310")) && (newElement.getAttribute("AddRowListTable").equals(oldElement.getAttribute("AddRowListTable")))) {
//				tagName = "AddRowListColumn";
//				elementName = getElementNameByTagName(tagName);
//				attrList.clear();
//				attrList.add("Order");
//				attrList.add("FieldOptions");
//				oldElementList = oldElement.getElementsByTagName(tagName);
//				newElementList = newElement.getElementsByTagName(tagName);
//				for (int i = 0; i < newElementList.getLength(); i++) {
//					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
//					isNotFound = true;
//					for (int j = 0; j < oldElementList.getLength(); j++) {
//						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
//						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
//							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
//									+ elementName + res.getString("FileDiffMessage06")
//									+ newElementWork.getAttribute("DataSource")
//									+ res.getString("FileDiffMessage07");
//							for (int k = 0; k < attrList.size(); k++) {
//								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
//							}
//							isNotFound = false;
//							break;
//						}
//					}
//					if (isNotFound) {
//						countOfChanges++;
//						buffer.append("\n" + countOfChanges + "."
//								+ functionLabel+ res.getString("FileDiffMessage02")
//								+ elementName + res.getString("FileDiffMessage06")
//								+ newElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
//								+ res.getString("FileDiffMessage08"));
//					}
//				}
//				for (int i = 0; i < oldElementList.getLength(); i++) {
//					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
//					isNotFound = true;
//					for (int j = 0; j < newElementList.getLength(); j++) {
//						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
//						if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
//							isNotFound = false;
//							break;
//						}
//					}
//					if (isNotFound) {
//						countOfChanges++;
//						buffer.append("\n" + countOfChanges + "."
//								+ functionLabel+ res.getString("FileDiffMessage02")
//								+ elementName + res.getString("FileDiffMessage06")
//								+ oldElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
//								+ res.getString("FileDiffMessage09"));
//					}
//				}
//			}
//
//			////////////////////////////////////////////////
//			// Sub-Elements : AddRowList Buttons of XF310 //
//			////////////////////////////////////////////////
//			if ((newElement.getAttribute("Type").equals("XF310")) && (newElement.getAttribute("AddRowListTable").equals(oldElement.getAttribute("AddRowListTable")))) {
//				tagName = "AddRowListButton";
//				elementName = getElementNameByTagName(tagName);
//				attrList.clear();
//				attrList.add("Position");
//				attrList.add("Caption");
//				oldElementList = oldElement.getElementsByTagName(tagName);
//				newElementList = newElement.getElementsByTagName(tagName);
//				for (int i = 0; i < newElementList.getLength(); i++) {
//					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
//					isNotFound = true;
//					for (int j = 0; j < oldElementList.getLength(); j++) {
//						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
//						if (oldElementWork.getAttribute("Number").equals(newElementWork.getAttribute("Number"))
//								&& oldElementWork.getAttribute("Action").equals(newElementWork.getAttribute("Action"))) {
//							elementLabel = functionLabel+ res.getString("FileDiffMessage02")
//									+ elementName + res.getString("FileDiffMessage06")
//									+ "F" + newElementWork.getAttribute("Number")
//									+ res.getString("FileDiffMessage07");
//							for (int k = 0; k < attrList.size(); k++) {
//								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
//							}
//							isNotFound = false;
//							break;
//						}
//					}
//					if (isNotFound) {
//						countOfChanges++;
//						buffer.append("\n" + countOfChanges + "."
//								+ functionLabel+ res.getString("FileDiffMessage02")
//								+ elementName + res.getString("FileDiffMessage06")
//								+ "F" + newElementWork.getAttribute("Number") + res.getString("FileDiffMessage07")
//								+ res.getString("FileDiffMessage08"));
//					}
//				}
//				for (int i = 0; i < oldElementList.getLength(); i++) {
//					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
//					isNotFound = true;
//					for (int j = 0; j < newElementList.getLength(); j++) {
//						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
//						if (oldElementWork.getAttribute("Number").equals(newElementWork.getAttribute("Number"))
//								&& oldElementWork.getAttribute("Action").equals(newElementWork.getAttribute("Action"))) {
//							isNotFound = false;
//							break;
//						}
//					}
//					if (isNotFound) {
//						countOfChanges++;
//						buffer.append("\n" + countOfChanges + "."
//								+ functionLabel+ res.getString("FileDiffMessage02")
//								+ elementName + res.getString("FileDiffMessage06")
//								+ "F" + oldElementWork.getAttribute("Number") + res.getString("FileDiffMessage07")
//								+ res.getString("FileDiffMessage09"));
//					}
//				}
//			}

			/////////////////////////////////////////
			// Sub-Elements : Detail Tabs of XF390 //
			/////////////////////////////////////////
			if (newElement.getAttribute("Type").equals("XF390") && newElement.getAttribute("HeaderTable").equals(oldElement.getAttribute("HeaderTable"))) {
				tagName = "Detail";
				elementName = getElementNameByTagName(tagName);
				attrList.clear();
				attrList.add("Caption");
				attrList.add("CaptionFontSize");
				attrList.add("CaptionFontStyle");
				attrList.add("Order");
				attrList.add("HeaderKeyFields");
				attrList.add("OrderBy");
				attrList.add("FixedWhere");
				attrList.add("FontID");
				attrList.add("FontSize");
				attrList.add("FontStyle");
				oldElementList = oldElement.getElementsByTagName(tagName);
				newElementList = newElement.getElementsByTagName(tagName);
				for (int i = 0; i < newElementList.getLength(); i++) {
					newElementWork = (org.w3c.dom.Element)newElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < oldElementList.getLength(); j++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
						if (oldElementWork.getAttribute("Table").equals(newElementWork.getAttribute("Table"))
								&& oldElementWork.getAttribute("KeyFields").equals(newElementWork.getAttribute("KeyFields"))) {
							if (newElementWork.getAttribute("KeyFields").equals("")) {
								elementLabel = functionLabel+ res.getString("FileDiffMessage02")
										+ elementName + res.getString("FileDiffMessage06")
										+ newElementWork.getAttribute("Table") + "{PK}"
										+ res.getString("FileDiffMessage07");
							} else {
								elementLabel = functionLabel+ res.getString("FileDiffMessage02")
										+ elementName + res.getString("FileDiffMessage06")
										+ newElementWork.getAttribute("Table")
										+ "{" + newElementWork.getAttribute("KeyFields") + "}"
										+ res.getString("FileDiffMessage07");
							}
							for (int k = 0; k < attrList.size(); k++) {
								compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
							}
							compareSubElementsOfXF390Details(newElementWork, oldElementWork, elementLabel);
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						if (newElementWork.getAttribute("KeyFields").equals("")) {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("Table") + "{PK}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage08"));
						} else {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("Table")
									+ "{" + newElementWork.getAttribute("KeyFields") + "}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage08"));
						}
					}
				}
				for (int i = 0; i < oldElementList.getLength(); i++) {
					oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
					isNotFound = true;
					for (int j = 0; j < newElementList.getLength(); j++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(j);
						if (oldElementWork.getAttribute("Table").equals(newElementWork.getAttribute("Table"))
								&& oldElementWork.getAttribute("KeyFields").equals(newElementWork.getAttribute("KeyFields"))) {
							isNotFound = false;
							break;
						}
					}
					if (isNotFound) {
						countOfChanges++;
						if (oldElementWork.getAttribute("KeyFields").equals("")) {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ oldElementWork.getAttribute("Table") + "{PK}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage09"));
						} else {
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ oldElementWork.getAttribute("Table")
									+ "{" + oldElementWork.getAttribute("KeyFields") + "}"
									+ res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage09"));
						}
					}
				}
			}

			////////////////////////////////////////////////////////
			// Sub-Elements : Columns of XF390(if without Detail) //
			////////////////////////////////////////////////////////
			if ((newElement.getAttribute("Type").equals("XF390") && newElement.getAttribute("DetailTable").equals(oldElement.getAttribute("DetailTable")))) {
				oldElementList = oldElement.getElementsByTagName("Detail");
				newElementList = newElement.getElementsByTagName("Detail");
				if (newElementList.getLength() == 0 && newElementList.getLength() == 0) {
					tagName = "Column";
					elementName = getElementNameByTagName(tagName);
					attrList.clear();
					attrList.add("Order");
					attrList.add("Width");
					attrList.add("Alignment");
					attrList.add("FieldOptions");
					oldElementList = oldElement.getElementsByTagName(tagName);
					newElementList = newElement.getElementsByTagName(tagName);
					for (int i = 0; i < newElementList.getLength(); i++) {
						newElementWork = (org.w3c.dom.Element)newElementList.item(i);
						isNotFound = true;
						for (int j = 0; j < oldElementList.getLength(); j++) {
							oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
							if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
								elementLabel = functionLabel+ res.getString("FileDiffMessage02")
										+ elementName + res.getString("FileDiffMessage06")
										+ newElementWork.getAttribute("DataSource")
										+ res.getString("FileDiffMessage07");
								for (int k = 0; k < attrList.size(); k++) {
									compareNewAndOldElements(newElementWork, oldElementWork, attrList.get(k), elementLabel);
								}
								isNotFound = false;
								break;
							}
						}
						if (isNotFound) {
							countOfChanges++;
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ newElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage08"));
						}
					}
					for (int i = 0; i < oldElementList.getLength(); i++) {
						oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
						isNotFound = true;
						for (int j = 0; j < newElementList.getLength(); j++) {
							newElementWork = (org.w3c.dom.Element)newElementList.item(j);
							if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))) {
								isNotFound = false;
								break;
							}
						}
						if (isNotFound) {
							countOfChanges++;
							buffer.append("\n" + countOfChanges + "."
									+ functionLabel+ res.getString("FileDiffMessage02")
									+ elementName + res.getString("FileDiffMessage06")
									+ oldElementWork.getAttribute("DataSource") + res.getString("FileDiffMessage07")
									+ res.getString("FileDiffMessage09"));
						}
					}
				}
			}
		}
	}

	private void compareSubElementsOfXF300Details(org.w3c.dom.Element newElement, org.w3c.dom.Element oldElement, String elementLabel) {
		NodeList oldElementList, newElementList;
		String tagName;
		org.w3c.dom.Element newElementWork, oldElementWork;
		boolean isNotFound, isAnyDifferent;

		isAnyDifferent = false;
		tagName = "Column";
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))
						&& oldElementWork.getAttribute("Order").equals(newElementWork.getAttribute("Order"))
						&& oldElementWork.getAttribute("FieldOptions").equals(newElementWork.getAttribute("FieldOptions"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))
						&& oldElementWork.getAttribute("Order").equals(newElementWork.getAttribute("Order"))
						&& oldElementWork.getAttribute("FieldOptions").equals(newElementWork.getAttribute("FieldOptions"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		if (isAnyDifferent) {
			countOfChanges++;
			buffer.append("\n" + countOfChanges + "." + elementLabel+ res.getString("FileDiffMessage11"));
		}

		isAnyDifferent = false;
		tagName = "Filter";
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))
						&& oldElementWork.getAttribute("Order").equals(newElementWork.getAttribute("Order"))
						&& oldElementWork.getAttribute("FieldOptions").equals(newElementWork.getAttribute("FieldOptions"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))
						&& oldElementWork.getAttribute("Order").equals(newElementWork.getAttribute("Order"))
						&& oldElementWork.getAttribute("FieldOptions").equals(newElementWork.getAttribute("FieldOptions"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		if (isAnyDifferent) {
			countOfChanges++;
			buffer.append("\n" + countOfChanges + "." + elementLabel+ res.getString("FileDiffMessage12"));
		}

		isAnyDifferent = false;
		tagName = "Button";
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (oldElementWork.getAttribute("Position").equals(newElementWork.getAttribute("Position"))
						&& oldElementWork.getAttribute("Number").equals(newElementWork.getAttribute("Number"))
						&& oldElementWork.getAttribute("Caption").equals(newElementWork.getAttribute("Caption"))
						&& oldElementWork.getAttribute("Action").equals(newElementWork.getAttribute("Action"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (oldElementWork.getAttribute("Position").equals(newElementWork.getAttribute("Position"))
						&& oldElementWork.getAttribute("Number").equals(newElementWork.getAttribute("Number"))
						&& oldElementWork.getAttribute("Caption").equals(newElementWork.getAttribute("Caption"))
						&& oldElementWork.getAttribute("Action").equals(newElementWork.getAttribute("Action"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		if (isAnyDifferent) {
			countOfChanges++;
			buffer.append("\n" + countOfChanges + "." + elementLabel+ res.getString("FileDiffMessage11"));
		}
	}

	private void compareSubElementsOfXF390Details(org.w3c.dom.Element newElement, org.w3c.dom.Element oldElement, String elementLabel) {
		NodeList oldElementList, newElementList;
		String tagName;
		org.w3c.dom.Element newElementWork, oldElementWork;
		boolean isNotFound, isAnyDifferent;

		isAnyDifferent = false;
		tagName = "Column";
		oldElementList = oldElement.getElementsByTagName(tagName);
		newElementList = newElement.getElementsByTagName(tagName);
		for (int i = 0; i < newElementList.getLength(); i++) {
			newElementWork = (org.w3c.dom.Element)newElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < oldElementList.getLength(); j++) {
				oldElementWork = (org.w3c.dom.Element)oldElementList.item(j);
				if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))
						&& oldElementWork.getAttribute("Order").equals(newElementWork.getAttribute("Order"))
						&& oldElementWork.getAttribute("Width").equals(newElementWork.getAttribute("Width"))
						&& oldElementWork.getAttribute("Alignment").equals(newElementWork.getAttribute("Alignment"))
						&& oldElementWork.getAttribute("FieldOptions").equals(newElementWork.getAttribute("FieldOptions"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		for (int i = 0; i < oldElementList.getLength(); i++) {
			oldElementWork = (org.w3c.dom.Element)oldElementList.item(i);
			isNotFound = true;
			for (int j = 0; j < newElementList.getLength(); j++) {
				newElementWork = (org.w3c.dom.Element)newElementList.item(j);
				if (oldElementWork.getAttribute("DataSource").equals(newElementWork.getAttribute("DataSource"))
						&& oldElementWork.getAttribute("Order").equals(newElementWork.getAttribute("Order"))
						&& oldElementWork.getAttribute("FieldOptions").equals(newElementWork.getAttribute("FieldOptions"))) {
					isNotFound = false;
					break;
				}
			}
			if (isNotFound) {
				isAnyDifferent = true;
				break;
			}
		}
		if (isAnyDifferent) {
			countOfChanges++;
			buffer.append("\n" + countOfChanges + "." + elementLabel+ res.getString("FileDiffMessage11"));
		}
	}

	private String getElementNameByTagName(String tagName) {
		String elementName = "";
		try {
			elementName = res.getString(tagName);
		} catch (Exception e) {
			elementName = tagName;
		}
		if (tagName.equals("MaintenanceLog")) {
			elementName = res.getString("SystemMaintenanceLog");
		}
		if (tagName.equals("Function")) {
			elementName = res.getString("FunctionDefinition");
		}
		return elementName;
	}

	void jButtonCancel_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}

class DialogToListChangesOfFiles_jButtonStart_actionAdapter implements java.awt.event.ActionListener {
	DialogToListChangesOfFiles adaptee;

	DialogToListChangesOfFiles_jButtonStart_actionAdapter(DialogToListChangesOfFiles adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonStart_actionPerformed(e);
	}
}

class DialogToListChangesOfFiles_jButtonCancel_actionAdapter implements java.awt.event.ActionListener {
	DialogToListChangesOfFiles adaptee;

	DialogToListChangesOfFiles_jButtonCancel_actionAdapter(DialogToListChangesOfFiles adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonCancel_actionPerformed(e);
	}
}