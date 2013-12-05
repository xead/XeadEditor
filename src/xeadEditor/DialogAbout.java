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
import java.awt.event.*;
import java.net.URI;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;

public class DialogAbout extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Application Information
	 */
	public static final String APPLICATION_NAME  = "XEAD Editor 1.1";
	public static final String FULL_VERSION  = "V1.R1.M22";
	//
	//22
	//・XF100,110,200,300,310でテーブルＩＤを置換した場合のフィールド定義のFieldOptionsに含まれるテーブルＩＤを置換するステップのバグを修正
	//・マウス操作中に意図しないDrag&Drop操作が認識されて異常終了することのある問題を修正
	//・テーブル定義のCopy&Paste操作において、結合テーブルやスクリプトもコピーされるようにした
	//・テーブルスクリプトをダイアログで編集した直後に「変更あり」を表示するようにした
	//・テーブルスクリプトに「一時保留」の設定を追加した
	//・各種スクリプト上でCtrl+/を押せば、選択されている行がコメント化されるようにした
	//・結合テーブル定義の削除において、スクリプトの実行タイミングに組み込まれていないかチェックするようにした
	//・どの結合フィールドも他の定義要素に利用されていなければ、結合テーブル定義を許可するようにした
	//・メニューに「プリロード指定機能」の属性を追加した
	//・メニューの「プリロード指定機能」と「プリロード指定クロスチェッカー」について、機能関連性検査の対象に含めた
	//・PostgreSQLのデータ型bigserialに対応した
	//・PostgreSQLの NULL制約の追加と除去に対応した
	//・機能ID指定でMenuオプションを追加するためのコンテキストメニューを追加した
	//・データ保守ユーティリティで、インポートデータの形式をCSVから「タブ区切」のテキストファイル(.txt)」に変更した
	//・データ保守ユーティリティで、インポートデータの１行目がフィールドIDでもフィールド名でも処理できるようにした
	//・数値フィールドが検索条件として追加された場合、「ゼロならば無視」のチェックを初期値にするようにした
	//・21の「インポートやスキャン処理の後に...」を元に戻した（再描画しないと処理内容が反映されてしまうため）
	//
	//21
	//・インポートやスキャン処理の後に「保存しない」を選んだときは、ツリービューを再描画しないようにした
	//・パネルサイズの指定値の上限を1200から2000に拡張した
	//・XF100,110,200,300,310でテーブルＩＤを置換した場合、フィールド定義のFieldOptionsに含まれるテーブルＩＤも置換するようにした
	//・テーブルの使途要素一覧からテーブル定義にジャンプした際に、使途要素の定義明細行が選択されるようにした
	//・XF390で明細フィールドを追加した際に、数値フィールドであればアラインメントRIGHTとして設定するようにした
	//・テーブル毎にクロスチェックの除外を指定できるようにした
	//
	//20
	//・PostgreSQLの関数インデックスをモジュールチェックの対象外とした
	//
	//19
	//・テーブルモジュールが存在する場合でもPKの変更ボタンを有効にして、PKが変更不可であるメッセージを表示するようにした
	//・モジュールチェックでのＰＫ違いのメッセージが不完全だった問題を修正
	//・XF390の行番幅をゼロにできなかった問題を修正した
	//・クロスチェッカーのログイン時ロード設定をメニュー定義に組み込んだ
	//・Undoの回数を無制限にした
	//・保守履歴を設け、UNDO情報を使って履歴追加時の初期値を設定するようにした
	//・テーブル定義のUNDO/REDO時にエラー状況を再設定するようにした
	//・PostgreSQLで漢字フィールドでインデックスを作るとモジュールチェックが正しく動作しない問題を修正
	//・PostgreSQLでフィールドＩＤに全角英数字を混ぜた場合にモジュールチェックが正しく動作しない問題を修正
	//・PostgreSQLのデータ型character varyingをVARCHARに対応させた
	//・XF100,110,300の検索条件のプロンプトオプションとして「候補リスト」を設けた
	//・機能定義へのフィールド組み込みの際の警告メッセージにデータソース名を組み込んだ
	//・機能の使途一覧において、検索条件のプロンプタ向けの使途が抜けていた問題を修正
	//・XF000で時刻初期値の指定をテキストフィールドでできるようにした
	//
	//18
	//・xeadファイルのXF300タイプの機能定義のインポートにおいて、見出し／明細テーブルの分析ロジックを改善した
	//・xeadファイルのテーブル定義のインポートにおいて、１テーブル内でのフィールドIDの重複が許されていたバグを修正した
	//・プロンプタの交換フィールド設定ダイアログで、コピーボタンでダイアログを閉じるようにした
	//・絞込み条件でVALUE:の指定を省略した場合、絞込み条件の一覧上で初期値として示されないバグを修正した
	//・ツリービュー上の機能定義の上にマウスポインタを置けば機能タイプがヒント表示されるようにした
	//・XF310の行追加リストの交換フィールド設定ダイアログの幅を広げた
	//
	//17
	//・仮想フィールドの場合、フィールド一覧上でフィールドIDのみをカッコ付けするようにした
	//・XF100,110,300について、初期表示オプションを組み込んだ
	//・XF310の行追加リストの指定で０個の検索条件を許すようにした
	//・xeadファイルのインポートにおいて、テーブルの二次識別子定義も取り込むようにした
	//・xeadファイルのインポートにおいて、テーブルの継承属性を取り込まないようにした（結合フィールドとして手作業で定義されるため）
	//・プロンプタ関数のパラメータ設定ダイアログの幅を広げた
	//・レイアウト確認ダイアログにおいて日付フィールドの枠線表示設定を改善した
	//・PostgreSQLのbytea型をBLOB型に対応させるようにした
	//・データ保守ユーティリティでBLOB型を読み書きしないようにした
	//・データ保守ユーティリティでシングルクォーテーションを含むデータを扱えるようにした
	//・データ保守ユーティリティでの保留中のコミット操作のチェックロジックを改善した
	//・結合テーブルの追加設定ダイアログのレイアウトを改善した
	//・結合テーブルの追加後に「結合元KEY項目」を変更できるようにした
	//・テーブルスクリプトの実行タイミングの選択で、結合テーブルが結合順序に従って一覧されるようにした
	//・ツリービュー上で Ctrl+C と Ctrl+V を使えるようにするとともに、複数ノードを同時選択できないようにした
	//・システムの起動およびSQLコンソールの実行のための機能キーをF9からF5に変更した
	//・XF310の行追加リストの並び順指定でのバグを修正した
	//・XF110のバッチフィールドについてレイアウト表示できないことがある問題を修正した
	//・スクリプトの１行中に複数の'{'や'}'が含まれていると正しくインデント処理されない問題を修正した
	//・右ペインで値を変更直後にCtrl+Sを押しても上書きされなかった問題を修正
	//・結合フィールドの使途情報について、XF110での利用状況が不正確に示されていた問題を修正した
	public static final String FORMAT_VERSION  = "1.1";
	public static final String PRODUCT_NAME = "XEAD[zi:d] Editor";
	public static final String COPYRIGHT = "Copyright 2013 DBC,Ltd.";
	public static final String URL_DBC = "http://homepage2.nifty.com/dbc/";
	/**
	 * Components on dialog
	 */
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel insetsPanel1 = new JPanel();
	private JPanel insetsPanel2 = new JPanel();
	private JPanel insetsPanel3 = new JPanel();
	private JButton buttonOK = new JButton();
	private JLabel imageLabel = new JLabel();
	private JLabel labelName = new JLabel();
	private JLabel labelVersion = new JLabel();
	private JLabel labelCopyright = new JLabel();
	private JLabel labelURL = new JLabel();
	private ImageIcon imageXead = new ImageIcon();
	private HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
	private Desktop desktop = Desktop.getDesktop();
	private Editor editor;

	public DialogAbout(Editor parent) {
		super(parent);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			editor = parent;
			jbInit(parent);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit(Editor parent) throws Exception  {
		imageXead = new ImageIcon(xeadEditor.Editor.class.getResource("title.png"));
		imageLabel.setIcon(imageXead);
		panel1.setLayout(new BorderLayout());
		panel1.setBorder(BorderFactory.createEtchedBorder());
		panel2.setLayout(new BorderLayout());
		insetsPanel2.setLayout(new BorderLayout());
		insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		insetsPanel2.setPreferredSize(new Dimension(75, 52));
		insetsPanel2.add(imageLabel, BorderLayout.EAST);
		//
		labelName.setFont(new java.awt.Font("Serif", 1, 16));
		labelName.setHorizontalAlignment(SwingConstants.CENTER);
		labelName.setText(PRODUCT_NAME);
		labelName.setBounds(new Rectangle(-5, 9, 190, 18));
		labelVersion.setFont(new java.awt.Font("Dialog", 0, 12));
		labelVersion.setHorizontalAlignment(SwingConstants.CENTER);
		labelVersion.setText(FULL_VERSION);
		labelVersion.setBounds(new Rectangle(-5, 32, 190, 15));
		labelCopyright.setFont(new java.awt.Font("Dialog", 0, 12));
		labelCopyright.setHorizontalAlignment(SwingConstants.CENTER);
		labelCopyright.setText(COPYRIGHT);
		labelCopyright.setBounds(new Rectangle(-5, 53, 190, 15));
		labelURL.setFont(new java.awt.Font("Dialog", 0, 12));
		labelURL.setHorizontalAlignment(SwingConstants.CENTER);
		labelURL.setText("<html><u><font color='blue'>" + URL_DBC);
		labelURL.setBounds(new Rectangle(-5, 73, 190, 15));
		labelURL.addMouseListener(new DialogAbout_labelURL_mouseAdapter(this));
		insetsPanel3.setLayout(null);
		insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
		insetsPanel3.setPreferredSize(new Dimension(190, 80));
		insetsPanel3.add(labelName, null);
		insetsPanel3.add(labelVersion, null);
		insetsPanel3.add(labelCopyright, null);
		insetsPanel3.add(labelURL, null);
		//
		buttonOK.setText("OK");
		buttonOK.addActionListener(this);
		insetsPanel1.add(buttonOK, null);
		//
		panel1.add(insetsPanel1, BorderLayout.SOUTH);
		panel1.add(panel2, BorderLayout.NORTH);
		panel2.setPreferredSize(new Dimension(270, 90));
		panel2.add(insetsPanel2, BorderLayout.CENTER);
		panel2.add(insetsPanel3, BorderLayout.EAST);
		//
		this.setTitle("About XEAD Editor");
		this.getContentPane().add(panel1, null);
		this.setResizable(false);
	}

	public void request() {
		insetsPanel1.getRootPane().setDefaultButton(buttonOK);
		Dimension dlgSize = this.getPreferredSize();
		Dimension frmSize = editor.getSize();
		Point loc = editor.getLocation();
		this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		this.pack();
		super.setVisible(true);
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			cancel();
		}
		super.processWindowEvent(e);
	}

	void cancel() {
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonOK) {
			cancel();
		}
	}

	void labelURL_mouseClicked(MouseEvent e) {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			desktop.browse(new URI(URL_DBC));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "The Site is inaccessible.");
		} finally {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	void labelURL_mouseEntered(MouseEvent e) {
		setCursor(htmlEditorKit.getLinkCursor());
	}

	void labelURL_mouseExited(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}

class DialogAbout_labelURL_mouseAdapter extends java.awt.event.MouseAdapter {
	DialogAbout adaptee;
	DialogAbout_labelURL_mouseAdapter(DialogAbout adaptee) {
		this.adaptee = adaptee;
	}
	public void mouseClicked(MouseEvent e) {
		adaptee.labelURL_mouseClicked(e);
	}
	public void mouseEntered(MouseEvent e) {
		adaptee.labelURL_mouseEntered(e);
	}
	public void mouseExited(MouseEvent e) {
		adaptee.labelURL_mouseExited(e);
	}
}
