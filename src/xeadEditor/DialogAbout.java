package xeadEditor;

/*
 * Copyright (c) 2018 WATANABE kozo <qyf05466@nifty.com>,
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

	/**
	 * Application Information and log of changes
	 */
	private static final long serialVersionUID = 1L;
	public static final String APPLICATION_NAME  = "X-TEA Editor 1.3";
	public static final String FORMAT_VERSION  = "1.2";
	public static final String PRODUCT_NAME = "X-TEA Editor";
	public static final String COPYRIGHT = "Copyright 2018 DBC Ltd.";
	public static final String URL_DBC = "http://dbc.in.coocan.jp/";
	public static final String FULL_VERSION  = "V1.R3.M21";
	//1.3.21
	//・テーブルの参照定義追加ダイアログで、エイリアスのフォーカス時に全選択させるようにした
	//・データ保守ユーティリティのインポート処理で、既存レコードについては無視されるのではなく更新されるようにした
	//・XF110,200,300,310のフラットパネルにおいて、フィールドのアラインメントを指定できるようにした
	//・スクリプトエディタでテーブル定義のコメントを貼り付けられるようにした
	//
	//1.3.20
	//・テーブル定義のXF000に対する使途検索のバグを修正
	//
	//1.3.19
	//・機能定義の追加用ダイアログで機能名を自動設定できるようにした
	//
	//1.3.18
	//・AssistListでsession.getTaxAmount(...)がsession.getTaxAmout(...)になっていたミスを修正
	//・データ保守ユーティリティのWhere条件とOrderBy条件をフォーカスすれば全選択されるようにした
	//・データ保守ユーティリティでのレコード件数がゼロ件でもエクセル出力できるようにした
	//・データ保守ユーティリティでインポート機能で、数字項目がブランクならばゼロをセットするようにした
	//・XF300の明細タブで「初期検索件数」を指定できるようにした
	//
	//1.3.17
	//・モデルのインポート仕様を改善した
	//・結合テーブルの追加ダイアログのテーブルIDにアシスト機能を組み込んだ
	//・テーブルノードのエラーアイコンの操作に関する不具合を修正した
	//・スクリプトの検索置換テキスト指定フィールドがフォーカスされたとき全選択されるようにした
	//
	//1.3.16
	//・テーブル定義のモデルインポートにおけるエイリアスの扱いを改善した
	//・DB接続とモジュールチェック実行との関係を改善した
	//・テーブルIDの入力項目にアシスト機能を組み込んだ
	//・varchar項目に上限サイズチェックを組み込んだ
	//
	//1.3.15
	//・レイアウトチェックのロジックを修正した
	//・モジュールチェックでのpostgresql向けのバグを修正した
	//・高解像度環境に合わせてメインフォントの文字サイズの省略値を16から18に変更した
	//・propertiesファイルでメインフォントサイズを設定できるようにした
	//
	//1.3.14
	//・データ保守ユーティリティのメッセージハンドリングを改善した
	//
	//1.3.13
	//・機能定義からその機能のみを起動するためのボタンを組み込んだ
	//
	//1.3.12
	//・編集コードを変更しても変更されない場合があるバグを修正した
	//・削除ルーチンの自動設定の仕様を改善した
	//・無効なDB接続が存在する場合でも、利用可能なDB接続だけで「SQLコンソール」を使えるようにした
	//・データ保守ユーティリティでの「編集なし」の整数フィールドの表示仕様を変更した
	//・スクリプトエディタの検索ツールに置換機能を組み込んだ
	//
	//1.3.11
	//・XF310の行追加用機能の交換フィールドに関する利用検査ロジックの不足を補った
	//・スクリプトアシストの表示領域がスクリーンをはみ出ないようにした
	//・既存のテーブルモジュールからフィールド定義だけでなくキー定義も補完できるようにした
	//・スクリプトエディタのNAMEモードを強化した
	//
	//1.3.10
	//・時刻編集フィールドを含むテーブルデータをエクセル出力するとエラーになる問題を修正した
	//・XF100,110,300で「降順」の並び順を指定すると、テーブルの差し替えができなくなる問題を修正した
	//・データ保守ユーティリティのインポート処理で、ゼロやブランクを許すフィールドであればインポートデータに含めなくても処理されるようにした
	//
	//1.3.9
	//・XF300で明細タブを新規追加したときの追加ボタン位置の初期値を修正した
	//・XF300で明細タブを20個以上追加できないようにした
	//・テーブルキーのフィールド構成を編集するためのダイアログの使い勝手を改善した
	//・住所検索用WEBサービスのためのプロキシ設定を組み込んだ
	//
	//1.3.8
	//・テーブルの使途要素の一覧ロジック中にTableEvaluatorに関する考慮が抜けていた問題を修正した
	//・データ保守ユーティリティでのインポート処理の動きを改善した
	//・軽減税率への対応にともなって、税率テーブルへのレコードの自動登録ステップをはずした
	//・スクリプトの編集パネルに、メソッド候補を一覧するための支援機能を組み込んだ
	//
	//1.3.7
	//・accdb向けの同期処理と補完処理の不具合を修正するとともに、DBドライバを更新した
	//・データ保守ユーティリティの使い勝手を向上させた
	//・モデルのインポート機能を修正・改善した
	//
	//1.3.6
	//・XF310の「行追加設定」を廃止した
	//
	//1.3.5
	//・XF310で行追加用機能を起動するための機能ボタンを追加できるようにした
	//・XF310に「明細行が0件ならば追加処理から開始」の属性を組み込んだ
	//・XF300に明細タブを新規追加しても「変更あり」にならなかったバグを修正した
	//・XF300に明細タブを新規追加した際、「起動時に一覧表示する」がチェックされるようにした
	//
	//1.3.4
	//・XF310で見出しレコード削除のための機能ボタンを追加できるようにした
	//・XF310で行追加用機能を起動するための機能ボタンを追加できるようにした
	//・「データベース設定」に「ユーザフィルター値」の項目を追加した
	//・固定値の追加パラメータの指定にともなう機能関連性検査での不具合を修正した
	//・Macでファイル名指定用ダイアログの動作が不安定である問題に対応した
	//・モデルのインポート機能でIOテーブルのポジションに対応した
	//
	//1.3.3
	//・XF100,XF200,XF300での機能起動の設定でINSTANCE_MODE等の固定値の追加パラメータを指定できるようにした
	//・上記の改善にともなって、XF200の更新専用設定を廃止した
	//・文字列走査のバグを修正した
	//
	//1.3.2
	//・インポート機能とファイル差異分析で、処理対象となる属性を整理した
	//・英語リソースの表現を一部改善した
	//・mac環境向けの微調整を施した
	//
	//1.3.1
	//・論理削除の自動化機構を廃止した
	//・XF100で、検索結果が１件だけの場合に明細処理を自動起動するためのオプションを設けた
	//
	//1.3.0
	//・テーブル定義の範囲KEY定義を除去した
	//・メニュー定義のクロスチェッカーのロード定義を除去した
	//・xeadファイルのインポート処理で、テーブル入出力のPosition属性に追随するようにした
	//・削除前のテーブルスクリプトの初期値を設定できるようにした
	//・Undo/Redo用メニューアイテムの表記を改善した
	//・「その他の設定」に「ハッシュ方式」を組み込んだ
	//・「その他の設定」にAdmin Emailを組み込んだ
	//
	//1.2.22
	//・xeafのインポート処理で、明細行の並び順に(D)が含まれているとインポートできない問題を修正した
	//
	//1.2.21
	//・区分指定されているフィールドが絞込条件として追加されたときには、「省略可リスト」として初期設定されるようにした
	//
	//1.2.20
	//・モデルのインポート機能で、定義要素の新規追加だけでなく、既存要素の更新もできるようにした
	//・propertiesの設定による更新前の自動バックアップ機能を設けた
	//
	//1.2.19
	//・XF310の行追加ダイアログのレイアウトが、場合によっては確認できない問題を修正した
	//・AccessのYESNO,BYTE,BOOLEAN型に対応した
	//・フィールドの「ブール値」を設定しても初期値の"T;F"を変更できなくなっていた問題を修正した
	//・ファイル間の差異表示のためのオプションをファイルメニューに設けた
	//
	//1.2.18
	//・キーのフィールド構成編集用パネルで、インデックスの昇降設定が出来なくなっていた問題を修正した
	//・モデルのインポート処理でテーブルのインデックス定義を取り込むようにした
	//・XF390の明細テーブルを変更できなくなっていた問題を修正した
	//・年度と月序のフィールドのレイアウトチェックが不正確だった問題を修正した

	//・データ保守ユーティリティを使いやすくした
	//・モジュールチェックでALTER時に入力必須項目がデータ移行されない問題を修正した
	//・XF300の構成テーブル定義をリセットできなかった問題を修正した
	//
	//1.2.17
	//・accdbのオートインクリメントのキーがモジュール上ではNullableとして設定される仕様に対応した
	//・画像フィールドの高さ設定が指定行数に正確に従っていなかった問題を修正した
	//・フィールドの自動採番や区分の属性に関して設定の操作性を向上させた
	//・レイアウト確認機能がフィールド定義のカラム名に追随しない問題を修正した
	//・レイアウト確認機能で〒連動フィールドのボタンが不必要に表示される問題を修正した
	//・データ保守ユーティリティ上で〒連動住所が漢字フィールドとみなされていなかった問題を修正した
	//・画像フィールドの再表示ボタンの幅を狭めてアイコンを設定した
	//・PostgreSQL向けに対応データ型を強化するとともに、JDBCドライバをアップグレードした
	//
	//1.2.16
	//・フォントによっては一部の項目のラベルが隠れる問題を修正した
	//・変更履歴がUndoされない問題を修正した
	//・フィールド定義に「カラム名」を設け、一覧形式でのキャプション初期値とした
	//・起動時のスプラッシュの形式を改善した
	//・MySQL向けに対応データ型を強化した
	//・インポート後の再表示の際に異常終了する問題を修正した
	//
	//1.2.15
	//・ツール名をX-TEA　Editorに変更した
	//・Float型小数桁の最大値を９に設定した
	//・H2 Database EngineとMS Accessに対応した
	//・SQL ServerのIMAGE型に対応した
	//・DialogCheckTableModuleのデータ型のマッピングをリファクタリングした
	//・Java1.8対応にともなって、xeadファイルとxeafファイルのインポート処理が出来なくなっていた問題を修正
	//・Java1.8対応にともなって、データ保守ユーティリティの操作ログが動かなくなっていた問題を修正
	//・Java1.8対応にともなって、参照テーブルと明細テーブルの追加用ダイアログが動かなくなっていた問題を修正
	//
	//1.2.14
	//・Java1.8に対応するために、sort処理をcomparatorからcomparableベースに修正した
	//
	//1.2.13
	//・XF110,200,310の見出し域フィールドの水平位置設定と間隔を同時に変更すると反映されないバグを修正した
	//・フィールドタイプにBYTEAを指定して変更すると編集パネルのレイアウトが一部崩れるバグを修正した
	//
	//1.2.12
	//・XF290,XF390の追加およびモデルからのインポートについて、帳票タイトル設定を微調整した
	//・システムノードのundo/redoに関する細かいバグを修正した
	//・フィールド属性として「更新不可」のチェックボックスを組み込んだ
	//・Linuxで利用するために、ツリービューの処理ロジックおよびDriverの起動様式を改善した
	//・データ保守ユーティリティで、行選択していない状態でDelキーが使えるバグを修正した
	//
	//1.2.11
	//・XF290,XF390について、異なるテーブルを処理する機能定義からもレイアウトをインポートできるようにした
	//・レイアウト確認パネルでのフィールドの「コメント」について表示様式を微調整した
	//・XF390で複数の明細テーブルを扱えるようにした
	//・ファイル更新時に他ユーザによる更新をチェックするようにした
	//
	//1.2.10
	//・PKの最終キーフィールドが数字属性でない場合には「AUTO行番として処理する」のチェックを非表示にするようにした
	//
	//1.2.9
	//・PKに含まれるフィールドのデータタイプを変更不可にした
	//・PKの最終キーフィールドが数字属性でない場合には「AUTO行番として処理する」のチェックを無効化するようにした
	//・テーブルの実データを一覧して選択したときに、長さを0としているvarcharのデータが示されない問題を修正した
	//・整数フィールド向けに「時刻(HH:MM)」の編集タイプを設けた
	//
	//1.2.8
	//・いくつかの機能タイプのフィールド一覧のコンテキストメニューに、他機能定義からのインポート処理を組み込んだ
	//・変更ログを記録するためのロジックを修正した
	//・スクリプト編集域のNAMEモードで、編集不可のメッセージを出すようにした
	//
	//1.2.7
	//・XF300の構成ツリー用のタイトルを設けた
	//
	//1.2.6
	//・getFormatted4ByteString(int)をModelerに合わせてstaticに変更した
	//・「定義の補完」でフィールドの並び順の設定が抜けていた問題を修正した
	//・冗長な保管確認のステップを整理した
	//・テーブルIDの最長桁を２０桁から４０桁に変更
	//
	//1.2.5
	//・走査援助およびテーブルスクリプトエディタの凡例欄に、valueListに関する情報を加筆した
	//
	//1.2.4
	//・起動時にJavaのバージョンをチェックするようにした
	//・テーブルのキー定義が１件も存在しない場合、キー編集ボタンを無効にするようにした
	//・キー定義用のダイアログを設けた
	//・テーブルスクリプト中のデータソースオブジェクトにenabledのプロパティを追加した
	//・テーブルスクリプトのエディタダイアログを開けなくなることがある問題を修正した
	//・SQL Serverでのint型の複合主キーの扱いに考慮してモジュールチェックのロジックを改善した
	//・SQL Serverのfloat型とtiny int型に対応するとともに、DATETIMEのデータ型を導入した
	//・SQL Server向けのモジュール作成でnchar型とTEXT型への対応が抜けていた問題を修正した
	//・PostgreSql向けにbyteaのデータタイプを設けた
	//・システム定義の「その他の設定」に「Editor起動時にモジュールチェックをスキップ」を組み込んだ
	//・見出し明細系の機能タイプにおいて、明細テーブルを切り替えたときに正常終了してもエラーメッセージが示される問題を修正した
	//・一部のシステム制御テーブルのモジュールを作成した際に、セッション立ち上げに必要なレコードを自動追加するようにした
	//・編集途中で保管した後でも、変更履歴の追加時に保管以前のアクションも含めて初期設定されるようにした
	//
	//1.2.3
	//・DBとの接続が切れている場合に再接続をガイドするようにした
	//・SQLコンソールを２回使うとDBのIDリストがクリアされてしまう問題を修正した
	//・スクリプトエディタ上で、Ctrl+NでIDモードと名称モードを切り替えられるようにした
	//
	//1.2.2
	//・Driver用フォントを変更すれば「レイアウト確認」のダイアログにただちに反映されるようにした
	//・一部の使途一覧からテーブルスクリプトにジャンプできなかった問題を修正
	//・使徒一覧からのジャンプ先がXF000であれば、スクリプト編集域をフォーカスするようにした
	//・カラム数上限255の制限をなくすために、データ一覧のEXCEL出力についてxlsからxlsx形式に変更した
	//・機能へのフィールド追加用ダイアログに「全選択」のボタンを置いた
	//・レイアウトチェック機能で区分系フィールドの幅設定ロジックがDriverと違っていた問題を修正した
	//・スクリプトのインデント処理のロジックを改善した
	//
	//1.2.1
	//・XF100,110に最大表示行数をはずした
	//・フィールド検索ダイアログでのメッセージを修正した
	//
	//1.2.0
	//・フォントサイズを12pから16pに変更するとともに、各種定義パネルのデザインを改善した
	//・xeadedt.propertiesでフォント名やスクリプトエディタの配色を指定できるようにした
	//・DB接続エラーのハンドリングおよびメッセージ表示方法を改善した
	//・Driverの表示フォントを設定するための項目をシステム定義に組み込んだ
	//・SQL Serverに対応した
	//・XF100,110に最大表示行数を組み込んだ
	//・桁数をゼロにすることで桁数指定なしのVARCHAR型に対応させるようにした
	//
	//29
	//・DB属性として「DBCPオプション」を追加した
	//・メニューＩＤの最長桁をセッションログのメニューＩＤに合わせて２に制限した
	//
	//28
	//・DB接続リストの設定に関してロジックを整理した
	//・モジュールチェックのパネルのボタン配置を改善した
	//・パネルレイアウトチェッカーにおける区分フィールドの幅設定のロジックをDriverに合わせた
	//
	//27
	//・モジュールチェックのパネルに、現在のモジュールにもとづいてフィールド定義を自動補完するためのボタンを設けた
	//・SQLコンソールのパネルに、現在のモジュールを一覧するためのボタンを設けた
	//
	//26
	//・メニュー一覧上にプリロードの欄を追加した
	//・システム定義に「プリロードをスキップ」のチェックボックスを設けた
	//・テーブル一覧上にクロスチェック対象かどうかの欄を追加した
	//・結合テーブル一覧上にオプショナルかどうかの欄を追加した
	//・一部の一覧でCSV出力できなかった問題を修正した
	//
	//25
	//・ファイル選択ダイアログの拡張子設定を改善した
	//・モジュールチェックでフィールド桁変更に失敗することのあるバグを改善した
	//・レイアウト確認ダイアログでの日時フィールド長設定のミスを修正した
	//・結合フィールドの使途要素に関数交換フィールドを含めるようにした
	//・関数交換フィールドのバリデーションを改善した
	//・XF100,XF300の明細フィールドに「非表示」のオプションを設けた
	//・テーブル定義の「削除操作」と「有効行Where」とを個々に指定できるようにした（それまでは同時に指定しなければ反映されなかった）
	//・検索メニューの「フィールド定義の走査」の表示前に前回の検索結果をクリアするようにした
	//
	//24
	//・XF300の明細テーブルを差し替えた場合、検索条件のテーブルIDも置き換えるようにした
	//・テーブル定義に１件目の結合テーブル定義を追加するとエラーになる問題を修正した
	//・モデルからXF100をインポートした場合、起動機能が設定されていなければ"XXXXX"ではなく"NONE"を指定するようにした
	//・LONG VARCHARにOracleのLONGを対応させるとともに、ALTER文もOracleに合わせた
	//
	//23
	//・検索メニューに「フィールド定義の走査」を追加した
	//・英語の言語リソースの抜けを補った
	//・XF390の用紙方向の初期値をLANDSCAPEに変更した
	//・テーブル定義を修正したとき、選択中のフィールド定義の使途要素も即時に更新されるようにした
	//・エラーログの冒頭にバージョン情報を出力するようにした
	//・Oracleに対応した
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

		labelName.setFont(new java.awt.Font(editor.mainFontName, 1, 20));
		labelName.setHorizontalAlignment(SwingConstants.CENTER);
		labelName.setText(PRODUCT_NAME);
		labelName.setBounds(new Rectangle(0, 8, 240, 22));
		labelVersion.setFont(new java.awt.Font(editor.mainFontName, 0, 16));
		labelVersion.setHorizontalAlignment(SwingConstants.CENTER);
		labelVersion.setText(FULL_VERSION);
		labelVersion.setBounds(new Rectangle(0, 32, 240, 20));
		labelCopyright.setFont(new java.awt.Font(editor.mainFontName, 0, 16));
		labelCopyright.setHorizontalAlignment(SwingConstants.CENTER);
		labelCopyright.setText(COPYRIGHT);
		labelCopyright.setBounds(new Rectangle(0, 53, 240, 20));
		labelURL.setFont(new java.awt.Font(editor.mainFontName, 0, 14));
		labelURL.setHorizontalAlignment(SwingConstants.CENTER);
		labelURL.setText("<html><u><font color='blue'>" + URL_DBC);
		labelURL.setBounds(new Rectangle(0, 75, 240, 20));
		labelURL.addMouseListener(new DialogAbout_labelURL_mouseAdapter(this));
		insetsPanel3.setLayout(null);
		insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
		insetsPanel3.setPreferredSize(new Dimension(250, 80));
		insetsPanel3.add(labelName, null);
		insetsPanel3.add(labelVersion, null);
		insetsPanel3.add(labelCopyright, null);
		insetsPanel3.add(labelURL, null);

		buttonOK.setText("OK");
		buttonOK.setFont(new java.awt.Font(editor.mainFontName, 0, 16));
		buttonOK.addActionListener(this);
		insetsPanel1.add(buttonOK, null);

		panel1.add(insetsPanel1, BorderLayout.SOUTH);
		panel1.add(panel2, BorderLayout.NORTH);
		panel2.setPreferredSize(new Dimension(350, 100));
		panel2.add(insetsPanel2, BorderLayout.CENTER);
		panel2.add(insetsPanel3, BorderLayout.EAST);

		this.setTitle("About X-TEA Editor");
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
