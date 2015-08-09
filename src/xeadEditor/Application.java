package xeadEditor;

/*
 * Copyright (c) 2015 WATANABE kozo <qyf05466@nifty.com>,
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

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.UIManager;
import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;

public class Application {
	private static ResourceBundle res = ResourceBundle.getBundle("xeadEditor.Res");
	boolean packFrame = false;
	private JWindow splashScreen = new JWindow();
	private JLabel splashIcon = new JLabel();
	private JLabel splashText = new JLabel();
	private JProgressBar splashProgressBar = new JProgressBar();

	public Application(String[] args) {
		ImageIcon image = new ImageIcon(xeadEditor.Application.class.getResource("splash.png"));
		splashIcon.setIcon(image);
		splashIcon.setLayout(null);

		splashProgressBar.setBounds(0, 291, 500, 9);
		splashScreen.add(splashProgressBar);

		splashText.setFont(new java.awt.Font("Dialog", 0, 16));
		splashText.setOpaque(false);
		splashText.setBounds(300, 205, 200, 20);
		splashText.setText(res.getString("SplashMessage0"));
		splashScreen.add(splashText);

		splashScreen.getContentPane().add(splashIcon);
		splashScreen.pack();
		splashScreen.setLocationRelativeTo(null);

		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				showSplash();
			}
		});

		Editor frame = new Editor(args, this);
		if (packFrame) {
			frame.pack();
		} else {
			frame.validate();
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.getInstalledLookAndFeels(); 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		new Application(args);
	}
	
	public void showSplash() {
		splashScreen.setVisible(true);
	}
	
	public void setTextOnSplash(String text) {
		splashText.setText(text);
	}
	
	public void setProgressMax(int value) {
		splashProgressBar.setMaximum(value);
	}
	
	public void setProgressValue(int value) {
		splashProgressBar.setValue(value);
	}
	
	public void repaintProgress() {
		splashProgressBar.paintImmediately(0,0,splashProgressBar.getWidth(),splashProgressBar.getHeight());
	}

	public void hideSplash() {
		if (splashScreen != null) {
			splashScreen.setVisible(false);
			splashScreen = null;
			splashText  = null;
			splashIcon  = null;
			splashProgressBar  = null;
		}
	}
}