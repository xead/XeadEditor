package xeadEditor;

/*
 * Copyright (c) 2019 WATANABE kozo <qyf05466@nifty.com>,
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
	public static final String COPYRIGHT = "Copyright 2019 DBC Ltd.";
	public static final String URL_DBC = "http://dbc.in.coocan.jp/";
	public static final String FULL_VERSION  = "V1.R3.M26";
	//1.3.26
	//�敪�l���X�g���t�B�[���h��`��ŕҏW�ł���悤�ɂ���
	//�̔ԃ��R�[�h���t�B�[���h��`��Œǉ��ł���悤�ɂ���
	//�EXF100,300�̖��׍s�Ń����N�@�\���g����悤�ɂ���
	//�EXF300�̌��o���悩�烌�C�A�E�g�C���|�[�g����ƁA�s�v��LINKED_CALL�܂ŃR�s�[���������C������
	//�E�t�B���^�[�����̃��C�A�E�g�`�F�b�N�̎d�l��ύX����
	//
	//1.3.25
	//�E�t�B�[���h�����N�@�\���@�\�ʎg�r�ɋ������Ă��Ȃ����������C������
	//�EID�E���x���̑Ή��\���t�@�C���o�͂ł���悤�ɂ���
	//�E�t�B���^�[�����́u�[���Ȃ�Ζ����v���͂�����
	//
	//1.3.24
	//�E�X�N���v�g�ł̃t�B�[���h�v���p�e�B���Ƃ���warning��ǉ�����
	//�E�V�X�e����`�̃C���|�[�g�_�C�A���O�Ńe�[�u���f�[�^���C���|�[�g�ł���悤�ɂ���
	//
	//1.3.23
	//�E�����E�u���_�C�A���O�ŃL���v�V�����l���u���ł��Ȃ��o�O���C������
	//�E�u���̑��̐ݒ�v�ɁA���O�t�@�C���t�H���_�ƃ��O�ő�T�C�Y��݂���
	//
	//1.3.22
	//�EXF100,300�ŋ@�\�{�^���̃A�N�V�����Ɂu�I���s��Ԃ��v��ǉ�����
	//�E�X�N���v�g�G�f�B�^�Ńe�[�u����`�̃R�����g��\��t������悤�ɂ���
	//�E�@�\�ǉ����̋@�\���̎����ݒ胍�W�b�N�̃o�O���C������
	//�EXF110,200,300,310�̃t�B�[���h�ݒ�ɃL���v�V��������ǉ�����
	//
	//1.3.21
	//�E�e�[�u���̎Q�ƒ�`�ǉ��_�C�A���O�ŁA�G�C���A�X�̃t�H�[�J�X���ɑS�I��������悤�ɂ���
	//�E�f�[�^�ێ烆�[�e�B���e�B�̃C���|�[�g�����ŁA�������R�[�h�ɂ��Ă͖��������̂ł͂Ȃ��X�V�����悤�ɂ���
	//�EXF110,200,300,310�̃t���b�g�p�l���ɂ����āA�t�B�[���h�̃A���C�������g���w��ł���悤�ɂ���
	//
	//1.3.20
	//�E�e�[�u����`��XF000�ɑ΂���g�r�����̃o�O���C��
	//
	//1.3.19
	//�E�@�\��`�̒ǉ��p�_�C�A���O�ŋ@�\���������ݒ�ł���悤�ɂ���
	//
	//1.3.18
	//�EAssistList��session.getTaxAmount(...)��session.getTaxAmout(...)�ɂȂ��Ă����~�X���C��
	//�E�f�[�^�ێ烆�[�e�B���e�B��Where������OrderBy�������t�H�[�J�X����ΑS�I�������悤�ɂ���
	//�E�f�[�^�ێ烆�[�e�B���e�B�ł̃��R�[�h�������[�����ł��G�N�Z���o�͂ł���悤�ɂ���
	//�E�f�[�^�ێ烆�[�e�B���e�B�ŃC���|�[�g�@�\�ŁA�������ڂ��u�����N�Ȃ�΃[�����Z�b�g����悤�ɂ���
	//�EXF300�̖��׃^�u�Łu�������������v���w��ł���悤�ɂ���
	//
	//1.3.17
	//�E���f���̃C���|�[�g�d�l�����P����
	//�E�����e�[�u���̒ǉ��_�C�A���O�̃e�[�u��ID�ɃA�V�X�g�@�\��g�ݍ���
	//�E�e�[�u���m�[�h�̃G���[�A�C�R���̑���Ɋւ���s����C������
	//�E�X�N���v�g�̌����u���e�L�X�g�w��t�B�[���h���t�H�[�J�X���ꂽ�Ƃ��S�I�������悤�ɂ���
	//
	//1.3.16
	//�E�e�[�u����`�̃��f���C���|�[�g�ɂ�����G�C���A�X�̈��������P����
	//�EDB�ڑ��ƃ��W���[���`�F�b�N���s�Ƃ̊֌W�����P����
	//�E�e�[�u��ID�̓��͍��ڂɃA�V�X�g�@�\��g�ݍ���
	//�Evarchar���ڂɏ���T�C�Y�`�F�b�N��g�ݍ���
	//
	//1.3.15
	//�E���C�A�E�g�`�F�b�N�̃��W�b�N���C������
	//�E���W���[���`�F�b�N�ł�postgresql�����̃o�O���C������
	//�E���𑜓x���ɍ��킹�ă��C���t�H���g�̕����T�C�Y�̏ȗ��l��16����18�ɕύX����
	//�Eproperties�t�@�C���Ń��C���t�H���g�T�C�Y��ݒ�ł���悤�ɂ���
	//
	//1.3.14
	//�E�f�[�^�ێ烆�[�e�B���e�B�̃��b�Z�[�W�n���h�����O�����P����
	//
	//1.3.13
	//�E�@�\��`���炻�̋@�\�݂̂��N�����邽�߂̃{�^����g�ݍ���
	//
	//1.3.12
	//�E�ҏW�R�[�h��ύX���Ă��ύX����Ȃ��ꍇ������o�O���C������
	//�E�폜���[�`���̎����ݒ�̎d�l�����P����
	//�E������DB�ڑ������݂���ꍇ�ł��A���p�\��DB�ڑ������ŁuSQL�R���\�[���v���g����悤�ɂ���
	//�E�f�[�^�ێ烆�[�e�B���e�B�ł́u�ҏW�Ȃ��v�̐����t�B�[���h�̕\���d�l��ύX����
	//�E�X�N���v�g�G�f�B�^�̌����c�[���ɒu���@�\��g�ݍ���
	//
	//1.3.11
	//�EXF310�̍s�ǉ��p�@�\�̌����t�B�[���h�Ɋւ��闘�p�������W�b�N�̕s��������
	//�E�X�N���v�g�A�V�X�g�̕\���̈悪�X�N���[�����͂ݏo�Ȃ��悤�ɂ���
	//�E�����̃e�[�u�����W���[������t�B�[���h��`�����łȂ��L�[��`���⊮�ł���悤�ɂ���
	//�E�X�N���v�g�G�f�B�^��NAME���[�h����������
	//
	//1.3.10
	//�E�����ҏW�t�B�[���h���܂ރe�[�u���f�[�^���G�N�Z���o�͂���ƃG���[�ɂȂ�����C������
	//�EXF100,110,300�Łu�~���v�̕��я����w�肷��ƁA�e�[�u���̍����ւ����ł��Ȃ��Ȃ�����C������
	//�E�f�[�^�ێ烆�[�e�B���e�B�̃C���|�[�g�����ŁA�[����u�����N�������t�B�[���h�ł���΃C���|�[�g�f�[�^�Ɋ܂߂Ȃ��Ă����������悤�ɂ���
	//
	//1.3.9
	//�EXF300�Ŗ��׃^�u��V�K�ǉ������Ƃ��̒ǉ��{�^���ʒu�̏����l���C������
	//�EXF300�Ŗ��׃^�u��20�ȏ�ǉ��ł��Ȃ��悤�ɂ���
	//�E�e�[�u���L�[�̃t�B�[���h�\����ҏW���邽�߂̃_�C�A���O�̎g����������P����
	//�E�Z�������pWEB�T�[�r�X�̂��߂̃v���L�V�ݒ��g�ݍ���
	//
	//1.3.8
	//�E�e�[�u���̎g�r�v�f�̈ꗗ���W�b�N����TableEvaluator�Ɋւ���l���������Ă��������C������
	//�E�f�[�^�ێ烆�[�e�B���e�B�ł̃C���|�[�g�����̓��������P����
	//�E�y���ŗ��ւ̑Ή��ɂƂ��Ȃ��āA�ŗ��e�[�u���ւ̃��R�[�h�̎����o�^�X�e�b�v���͂�����
	//�E�X�N���v�g�̕ҏW�p�l���ɁA���\�b�h�����ꗗ���邽�߂̎x���@�\��g�ݍ���
	//
	//1.3.7
	//�Eaccdb�����̓��������ƕ⊮�����̕s����C������ƂƂ��ɁADB�h���C�o���X�V����
	//�E�f�[�^�ێ烆�[�e�B���e�B�̎g����������コ����
	//�E���f���̃C���|�[�g�@�\���C���E���P����
	//
	//1.3.6
	//�EXF310�́u�s�ǉ��ݒ�v��p�~����
	//
	//1.3.5
	//�EXF310�ōs�ǉ��p�@�\���N�����邽�߂̋@�\�{�^����ǉ��ł���悤�ɂ���
	//�EXF310�Ɂu���׍s��0���Ȃ�Βǉ���������J�n�v�̑�����g�ݍ���
	//�EXF300�ɖ��׃^�u��V�K�ǉ����Ă��u�ύX����v�ɂȂ�Ȃ������o�O���C������
	//�EXF300�ɖ��׃^�u��V�K�ǉ������ہA�u�N�����Ɉꗗ�\������v���`�F�b�N�����悤�ɂ���
	//
	//1.3.4
	//�EXF310�Ō��o�����R�[�h�폜�̂��߂̋@�\�{�^����ǉ��ł���悤�ɂ���
	//�EXF310�ōs�ǉ��p�@�\���N�����邽�߂̋@�\�{�^����ǉ��ł���悤�ɂ���
	//�E�u�f�[�^�x�[�X�ݒ�v�Ɂu���[�U�t�B���^�[�l�v�̍��ڂ�ǉ�����
	//�E�Œ�l�̒ǉ��p�����[�^�̎w��ɂƂ��Ȃ��@�\�֘A�������ł̕s����C������
	//�EMac�Ńt�@�C�����w��p�_�C�A���O�̓��삪�s����ł�����ɑΉ�����
	//�E���f���̃C���|�[�g�@�\��IO�e�[�u���̃|�W�V�����ɑΉ�����
	//
	//1.3.3
	//�EXF100,XF200,XF300�ł̋@�\�N���̐ݒ��INSTANCE_MODE���̌Œ�l�̒ǉ��p�����[�^���w��ł���悤�ɂ���
	//�E��L�̉��P�ɂƂ��Ȃ��āAXF200�̍X�V��p�ݒ��p�~����
	//�E�����񑖍��̃o�O���C������
	//
	//1.3.2
	//�E�C���|�[�g�@�\�ƃt�@�C�����ٕ��͂ŁA�����ΏۂƂȂ鑮���𐮗�����
	//�E�p�ꃊ�\�[�X�̕\�����ꕔ���P����
	//�Emac�������̔��������{����
	//
	//1.3.1
	//�E�_���폜�̎������@�\��p�~����
	//�EXF100�ŁA�������ʂ��P�������̏ꍇ�ɖ��׏����������N�����邽�߂̃I�v�V������݂���
	//
	//1.3.0
	//�E�e�[�u����`�͈̔�KEY��`����������
	//�E���j���[��`�̃N���X�`�F�b�J�[�̃��[�h��`����������
	//�Exead�t�@�C���̃C���|�[�g�����ŁA�e�[�u�����o�͂�Position�����ɒǐ�����悤�ɂ���
	//�E�폜�O�̃e�[�u���X�N���v�g�̏����l��ݒ�ł���悤�ɂ���
	//�EUndo/Redo�p���j���[�A�C�e���̕\�L�����P����
	//�E�u���̑��̐ݒ�v�Ɂu�n�b�V�������v��g�ݍ���
	//�E�u���̑��̐ݒ�v��Admin Email��g�ݍ���
	//
	//1.2.22
	//�Exeaf�̃C���|�[�g�����ŁA���׍s�̕��я���(D)���܂܂�Ă���ƃC���|�[�g�ł��Ȃ������C������
	//
	//1.2.21
	//�E�敪�w�肳��Ă���t�B�[���h���i�������Ƃ��Ēǉ����ꂽ�Ƃ��ɂ́A�u�ȗ����X�g�v�Ƃ��ď����ݒ肳���悤�ɂ���
	//
	//1.2.20
	//�E���f���̃C���|�[�g�@�\�ŁA��`�v�f�̐V�K�ǉ������łȂ��A�����v�f�̍X�V���ł���悤�ɂ���
	//�Eproperties�̐ݒ�ɂ��X�V�O�̎����o�b�N�A�b�v�@�\��݂���
	//
	//1.2.19
	//�EXF310�̍s�ǉ��_�C�A���O�̃��C�A�E�g���A�ꍇ�ɂ���Ă͊m�F�ł��Ȃ������C������
	//�EAccess��YESNO,BYTE,BOOLEAN�^�ɑΉ�����
	//�E�t�B�[���h�́u�u�[���l�v��ݒ肵�Ă������l��"T;F"��ύX�ł��Ȃ��Ȃ��Ă��������C������
	//�E�t�@�C���Ԃ̍��ٕ\���̂��߂̃I�v�V�������t�@�C�����j���[�ɐ݂���
	//
	//1.2.18
	//�E�L�[�̃t�B�[���h�\���ҏW�p�p�l���ŁA�C���f�b�N�X�̏��~�ݒ肪�o���Ȃ��Ȃ��Ă��������C������
	//�E���f���̃C���|�[�g�����Ńe�[�u���̃C���f�b�N�X��`����荞�ނ悤�ɂ���
	//�EXF390�̖��׃e�[�u����ύX�ł��Ȃ��Ȃ��Ă��������C������
	//�E�N�x�ƌ����̃t�B�[���h�̃��C�A�E�g�`�F�b�N���s���m�����������C������

	//�E�f�[�^�ێ烆�[�e�B���e�B���g���₷������
	//�E���W���[���`�F�b�N��ALTER���ɓ��͕K�{���ڂ��f�[�^�ڍs����Ȃ������C������
	//�EXF300�̍\���e�[�u����`�����Z�b�g�ł��Ȃ����������C������
	//
	//1.2.17
	//�Eaccdb�̃I�[�g�C���N�������g�̃L�[�����W���[����ł�Nullable�Ƃ��Đݒ肳���d�l�ɑΉ�����
	//�E�摜�t�B�[���h�̍����ݒ肪�w��s���ɐ��m�ɏ]���Ă��Ȃ����������C������
	//�E�t�B�[���h�̎����̔Ԃ�敪�̑����Ɋւ��Đݒ�̑��쐫�����コ����
	//�E���C�A�E�g�m�F�@�\���t�B�[���h��`�̃J�������ɒǐ����Ȃ������C������
	//�E���C�A�E�g�m�F�@�\�Ł��A���t�B�[���h�̃{�^�����s�K�v�ɕ\�����������C������
	//�E�f�[�^�ێ烆�[�e�B���e�B��Ł��A���Z���������t�B�[���h�Ƃ݂Ȃ���Ă��Ȃ����������C������
	//�E�摜�t�B�[���h�̍ĕ\���{�^���̕������߂ăA�C�R����ݒ肵��
	//�EPostgreSQL�����ɑΉ��f�[�^�^����������ƂƂ��ɁAJDBC�h���C�o���A�b�v�O���[�h����
	//
	//1.2.16
	//�E�t�H���g�ɂ���Ă͈ꕔ�̍��ڂ̃��x�����B�������C������
	//�E�ύX������Undo����Ȃ������C������
	//�E�t�B�[���h��`�Ɂu�J�������v��݂��A�ꗗ�`���ł̃L���v�V���������l�Ƃ���
	//�E�N�����̃X�v���b�V���̌`�������P����
	//�EMySQL�����ɑΉ��f�[�^�^����������
	//�E�C���|�[�g��̍ĕ\���̍ۂɈُ�I����������C������
	//
	//1.2.15
	//�E�c�[������X-TEA�@Editor�ɕύX����
	//�EFloat�^�������̍ő�l���X�ɐݒ肵��
	//�EH2 Database Engine��MS Access�ɑΉ�����
	//�ESQL Server��IMAGE�^�ɑΉ�����
	//�EDialogCheckTableModule�̃f�[�^�^�̃}�b�s���O�����t�@�N�^�����O����
	//�EJava1.8�Ή��ɂƂ��Ȃ��āAxead�t�@�C����xeaf�t�@�C���̃C���|�[�g�������o���Ȃ��Ȃ��Ă��������C��
	//�EJava1.8�Ή��ɂƂ��Ȃ��āA�f�[�^�ێ烆�[�e�B���e�B�̑��샍�O�������Ȃ��Ȃ��Ă��������C��
	//�EJava1.8�Ή��ɂƂ��Ȃ��āA�Q�ƃe�[�u���Ɩ��׃e�[�u���̒ǉ��p�_�C�A���O�������Ȃ��Ȃ��Ă��������C��
	//
	//1.2.14
	//�EJava1.8�ɑΉ����邽�߂ɁAsort������comparator����comparable�x�[�X�ɏC������
	//
	//1.2.13
	//�EXF110,200,310�̌��o����t�B�[���h�̐����ʒu�ݒ�ƊԊu�𓯎��ɕύX����Ɣ��f����Ȃ��o�O���C������
	//�E�t�B�[���h�^�C�v��BYTEA���w�肵�ĕύX����ƕҏW�p�l���̃��C�A�E�g���ꕔ�����o�O���C������
	//
	//1.2.12
	//�EXF290,XF390�̒ǉ�����у��f������̃C���|�[�g�ɂ��āA���[�^�C�g���ݒ�����������
	//�E�V�X�e���m�[�h��undo/redo�Ɋւ���ׂ����o�O���C������
	//�E�t�B�[���h�����Ƃ��āu�X�V�s�v�̃`�F�b�N�{�b�N�X��g�ݍ���
	//�ELinux�ŗ��p���邽�߂ɁA�c���[�r���[�̏������W�b�N�����Driver�̋N���l�������P����
	//�E�f�[�^�ێ烆�[�e�B���e�B�ŁA�s�I�����Ă��Ȃ���Ԃ�Del�L�[���g����o�O���C������
	//
	//1.2.11
	//�EXF290,XF390�ɂ��āA�قȂ�e�[�u������������@�\��`��������C�A�E�g���C���|�[�g�ł���悤�ɂ���
	//�E���C�A�E�g�m�F�p�l���ł̃t�B�[���h�́u�R�����g�v�ɂ��ĕ\���l�������������
	//�EXF390�ŕ����̖��׃e�[�u����������悤�ɂ���
	//�E�t�@�C���X�V���ɑ����[�U�ɂ��X�V���`�F�b�N����悤�ɂ���
	//
	//1.2.10
	//�EPK�̍ŏI�L�[�t�B�[���h�����������łȂ��ꍇ�ɂ́uAUTO�s�ԂƂ��ď�������v�̃`�F�b�N���\���ɂ���悤�ɂ���
	//
	//1.2.9
	//�EPK�Ɋ܂܂��t�B�[���h�̃f�[�^�^�C�v��ύX�s�ɂ���
	//�EPK�̍ŏI�L�[�t�B�[���h�����������łȂ��ꍇ�ɂ́uAUTO�s�ԂƂ��ď�������v�̃`�F�b�N�𖳌�������悤�ɂ���
	//�E�e�[�u���̎��f�[�^���ꗗ���đI�������Ƃ��ɁA������0�Ƃ��Ă���varchar�̃f�[�^��������Ȃ������C������
	//�E�����t�B�[���h�����Ɂu����(HH:MM)�v�̕ҏW�^�C�v��݂���
	//
	//1.2.8
	//�E�������̋@�\�^�C�v�̃t�B�[���h�ꗗ�̃R���e�L�X�g���j���[�ɁA���@�\��`����̃C���|�[�g������g�ݍ���
	//�E�ύX���O���L�^���邽�߂̃��W�b�N���C������
	//�E�X�N���v�g�ҏW���NAME���[�h�ŁA�ҏW�s�̃��b�Z�[�W���o���悤�ɂ���
	//
	//1.2.7
	//�EXF300�̍\���c���[�p�̃^�C�g����݂���
	//
	//1.2.6
	//�EgetFormatted4ByteString(int)��Modeler�ɍ��킹��static�ɕύX����
	//�E�u��`�̕⊮�v�Ńt�B�[���h�̕��я��̐ݒ肪�����Ă��������C������
	//�E�璷�ȕۊǊm�F�̃X�e�b�v�𐮗�����
	//�E�e�[�u��ID�̍Œ������Q�O������S�O���ɕύX
	//
	//1.2.5
	//�E������������уe�[�u���X�N���v�g�G�f�B�^�̖}�ᗓ�ɁAvalueList�Ɋւ���������M����
	//
	//1.2.4
	//�E�N������Java�̃o�[�W�������`�F�b�N����悤�ɂ���
	//�E�e�[�u���̃L�[��`���P�������݂��Ȃ��ꍇ�A�L�[�ҏW�{�^���𖳌��ɂ���悤�ɂ���
	//�E�L�[��`�p�̃_�C�A���O��݂���
	//�E�e�[�u���X�N���v�g���̃f�[�^�\�[�X�I�u�W�F�N�g��enabled�̃v���p�e�B��ǉ�����
	//�E�e�[�u���X�N���v�g�̃G�f�B�^�_�C�A���O���J���Ȃ��Ȃ邱�Ƃ���������C������
	//�ESQL Server�ł�int�^�̕�����L�[�̈����ɍl�����ă��W���[���`�F�b�N�̃��W�b�N�����P����
	//�ESQL Server��float�^��tiny int�^�ɑΉ�����ƂƂ��ɁADATETIME�̃f�[�^�^�𓱓�����
	//�ESQL Server�����̃��W���[���쐬��nchar�^��TEXT�^�ւ̑Ή��������Ă��������C������
	//�EPostgreSql������bytea�̃f�[�^�^�C�v��݂���
	//�E�V�X�e����`�́u���̑��̐ݒ�v�ɁuEditor�N�����Ƀ��W���[���`�F�b�N���X�L�b�v�v��g�ݍ���
	//�E���o�����׌n�̋@�\�^�C�v�ɂ����āA���׃e�[�u����؂�ւ����Ƃ��ɐ���I�����Ă��G���[���b�Z�[�W�������������C������
	//�E�ꕔ�̃V�X�e������e�[�u���̃��W���[�����쐬�����ۂɁA�Z�b�V���������グ�ɕK�v�ȃ��R�[�h�������ǉ�����悤�ɂ���
	//�E�ҏW�r���ŕۊǂ�����ł��A�ύX�����̒ǉ����ɕۊǈȑO�̃A�N�V�������܂߂ď����ݒ肳���悤�ɂ���
	//
	//1.2.3
	//�EDB�Ƃ̐ڑ����؂�Ă���ꍇ�ɍĐڑ����K�C�h����悤�ɂ���
	//�ESQL�R���\�[�����Q��g����DB��ID���X�g���N���A����Ă��܂������C������
	//�E�X�N���v�g�G�f�B�^��ŁACtrl+N��ID���[�h�Ɩ��̃��[�h��؂�ւ�����悤�ɂ���
	//
	//1.2.2
	//�EDriver�p�t�H���g��ύX����΁u���C�A�E�g�m�F�v�̃_�C�A���O�ɂ������ɔ��f�����悤�ɂ���
	//�E�ꕔ�̎g�r�ꗗ����e�[�u���X�N���v�g�ɃW�����v�ł��Ȃ����������C��
	//�E�g�k�ꗗ����̃W�����v�悪XF000�ł���΁A�X�N���v�g�ҏW����t�H�[�J�X����悤�ɂ���
	//�E�J���������255�̐������Ȃ������߂ɁA�f�[�^�ꗗ��EXCEL�o�͂ɂ���xls����xlsx�`���ɕύX����
	//�E�@�\�ւ̃t�B�[���h�ǉ��p�_�C�A���O�Ɂu�S�I���v�̃{�^����u����
	//�E���C�A�E�g�`�F�b�N�@�\�ŋ敪�n�t�B�[���h�̕��ݒ胍�W�b�N��Driver�ƈ���Ă��������C������
	//�E�X�N���v�g�̃C���f���g�����̃��W�b�N�����P����
	//
	//1.2.1
	//�EXF100,110�ɍő�\���s�����͂�����
	//�E�t�B�[���h�����_�C�A���O�ł̃��b�Z�[�W���C������
	//
	//1.2.0
	//�E�t�H���g�T�C�Y��12p����16p�ɕύX����ƂƂ��ɁA�e���`�p�l���̃f�U�C�������P����
	//�Exeadedt.properties�Ńt�H���g����X�N���v�g�G�f�B�^�̔z�F���w��ł���悤�ɂ���
	//�EDB�ڑ��G���[�̃n���h�����O����у��b�Z�[�W�\�����@�����P����
	//�EDriver�̕\���t�H���g��ݒ肷�邽�߂̍��ڂ��V�X�e����`�ɑg�ݍ���
	//�ESQL Server�ɑΉ�����
	//�EXF100,110�ɍő�\���s����g�ݍ���
	//�E�������[���ɂ��邱�ƂŌ����w��Ȃ���VARCHAR�^�ɑΉ�������悤�ɂ���
	//
	//29
	//�EDB�����Ƃ��āuDBCP�I�v�V�����v��ǉ�����
	//�E���j���[�h�c�̍Œ������Z�b�V�������O�̃��j���[�h�c�ɍ��킹�ĂQ�ɐ�������
	//
	//28
	//�EDB�ڑ����X�g�̐ݒ�Ɋւ��ă��W�b�N�𐮗�����
	//�E���W���[���`�F�b�N�̃p�l���̃{�^���z�u�����P����
	//�E�p�l�����C�A�E�g�`�F�b�J�[�ɂ�����敪�t�B�[���h�̕��ݒ�̃��W�b�N��Driver�ɍ��킹��
	//
	//27
	//�E���W���[���`�F�b�N�̃p�l���ɁA���݂̃��W���[���ɂ��ƂÂ��ăt�B�[���h��`�������⊮���邽�߂̃{�^����݂���
	//�ESQL�R���\�[���̃p�l���ɁA���݂̃��W���[�����ꗗ���邽�߂̃{�^����݂���
	//
	//26
	//�E���j���[�ꗗ��Ƀv�����[�h�̗���ǉ�����
	//�E�V�X�e����`�Ɂu�v�����[�h���X�L�b�v�v�̃`�F�b�N�{�b�N�X��݂���
	//�E�e�[�u���ꗗ��ɃN���X�`�F�b�N�Ώۂ��ǂ����̗���ǉ�����
	//�E�����e�[�u���ꗗ��ɃI�v�V���i�����ǂ����̗���ǉ�����
	//�E�ꕔ�̈ꗗ��CSV�o�͂ł��Ȃ����������C������
	//
	//25
	//�E�t�@�C���I���_�C�A���O�̊g���q�ݒ�����P����
	//�E���W���[���`�F�b�N�Ńt�B�[���h���ύX�Ɏ��s���邱�Ƃ̂���o�O�����P����
	//�E���C�A�E�g�m�F�_�C�A���O�ł̓����t�B�[���h���ݒ�̃~�X���C������
	//�E�����t�B�[���h�̎g�r�v�f�Ɋ֐������t�B�[���h���܂߂�悤�ɂ���
	//�E�֐������t�B�[���h�̃o���f�[�V���������P����
	//�EXF100,XF300�̖��׃t�B�[���h�Ɂu��\���v�̃I�v�V������݂���
	//�E�e�[�u����`�́u�폜����v�Ɓu�L���sWhere�v�Ƃ��X�Ɏw��ł���悤�ɂ����i����܂ł͓����Ɏw�肵�Ȃ���Δ��f����Ȃ������j
	//�E�������j���[�́u�t�B�[���h��`�̑����v�̕\���O�ɑO��̌������ʂ��N���A����悤�ɂ���
	//
	//24
	//�EXF300�̖��׃e�[�u���������ւ����ꍇ�A���������̃e�[�u��ID���u��������悤�ɂ���
	//�E�e�[�u����`�ɂP���ڂ̌����e�[�u����`��ǉ�����ƃG���[�ɂȂ�����C������
	//�E���f������XF100���C���|�[�g�����ꍇ�A�N���@�\���ݒ肳��Ă��Ȃ����"XXXXX"�ł͂Ȃ�"NONE"���w�肷��悤�ɂ���
	//�ELONG VARCHAR��Oracle��LONG��Ή�������ƂƂ��ɁAALTER����Oracle�ɍ��킹��
	//
	//23
	//�E�������j���[�Ɂu�t�B�[���h��`�̑����v��ǉ�����
	//�E�p��̌��ꃊ�\�[�X�̔���������
	//�EXF390�̗p�������̏����l��LANDSCAPE�ɕύX����
	//�E�e�[�u����`���C�������Ƃ��A�I�𒆂̃t�B�[���h��`�̎g�r�v�f�������ɍX�V�����悤�ɂ���
	//�E�G���[���O�̖`���Ƀo�[�W���������o�͂���悤�ɂ���
	//�EOracle�ɑΉ�����
	//
	//22
	//�EXF100,110,200,300,310�Ńe�[�u���h�c��u�������ꍇ�̃t�B�[���h��`��FieldOptions�Ɋ܂܂��e�[�u���h�c��u������X�e�b�v�̃o�O���C��
	//�E�}�E�X���쒆�ɈӐ}���Ȃ�Drag&Drop���삪�F������Ĉُ�I�����邱�Ƃ̂�������C��
	//�E�e�[�u����`��Copy&Paste����ɂ����āA�����e�[�u����X�N���v�g���R�s�[�����悤�ɂ���
	//�E�e�[�u���X�N���v�g���_�C�A���O�ŕҏW��������Ɂu�ύX����v��\������悤�ɂ���
	//�E�e�[�u���X�N���v�g�Ɂu�ꎞ�ۗ��v�̐ݒ��ǉ�����
	//�E�e��X�N���v�g���Ctrl+/�������΁A�I������Ă���s���R�����g�������悤�ɂ���
	//�E�����e�[�u����`�̍폜�ɂ����āA�X�N���v�g�̎��s�^�C�~���O�ɑg�ݍ��܂�Ă��Ȃ����`�F�b�N����悤�ɂ���
	//�E�ǂ̌����t�B�[���h�����̒�`�v�f�ɗ��p����Ă��Ȃ���΁A�����e�[�u����`��������悤�ɂ���
	//�E���j���[�Ɂu�v�����[�h�w��@�\�v�̑�����ǉ�����
	//�E���j���[�́u�v�����[�h�w��@�\�v�Ɓu�v�����[�h�w��N���X�`�F�b�J�[�v�ɂ��āA�@�\�֘A�������̑ΏۂɊ܂߂�
	//�EPostgreSQL�̃f�[�^�^bigserial�ɑΉ�����
	//�EPostgreSQL�� NULL����̒ǉ��Ə����ɑΉ�����
	//�E�@�\ID�w���Menu�I�v�V������ǉ����邽�߂̃R���e�L�X�g���j���[��ǉ�����
	//�E�f�[�^�ێ烆�[�e�B���e�B�ŁA�C���|�[�g�f�[�^�̌`����CSV����u�^�u��؁v�̃e�L�X�g�t�@�C��(.txt)�v�ɕύX����
	//�E�f�[�^�ێ烆�[�e�B���e�B�ŁA�C���|�[�g�f�[�^�̂P�s�ڂ��t�B�[���hID�ł��t�B�[���h���ł������ł���悤�ɂ���
	//�E���l�t�B�[���h�����������Ƃ��Ēǉ����ꂽ�ꍇ�A�u�[���Ȃ�Ζ����v�̃`�F�b�N�������l�ɂ���悤�ɂ���
	//�E21�́u�C���|�[�g��X�L���������̌��...�v�����ɖ߂����i�ĕ`�悵�Ȃ��Ə������e�����f����Ă��܂����߁j
	//
	//21
	//�E�C���|�[�g��X�L���������̌�Ɂu�ۑ����Ȃ��v��I�񂾂Ƃ��́A�c���[�r���[���ĕ`�悵�Ȃ��悤�ɂ���
	//�E�p�l���T�C�Y�̎w��l�̏����1200����2000�Ɋg������
	//�EXF100,110,200,300,310�Ńe�[�u���h�c��u�������ꍇ�A�t�B�[���h��`��FieldOptions�Ɋ܂܂��e�[�u���h�c���u������悤�ɂ���
	//�E�e�[�u���̎g�r�v�f�ꗗ����e�[�u����`�ɃW�����v�����ۂɁA�g�r�v�f�̒�`���׍s���I�������悤�ɂ���
	//�EXF390�Ŗ��׃t�B�[���h��ǉ������ۂɁA���l�t�B�[���h�ł���΃A���C�������gRIGHT�Ƃ��Đݒ肷��悤�ɂ���
	//�E�e�[�u�����ɃN���X�`�F�b�N�̏��O���w��ł���悤�ɂ���
	//
	//20
	//�EPostgreSQL�̊֐��C���f�b�N�X�����W���[���`�F�b�N�̑ΏۊO�Ƃ���
	//
	//19
	//�E�e�[�u�����W���[�������݂���ꍇ�ł�PK�̕ύX�{�^����L���ɂ��āAPK���ύX�s�ł��郁�b�Z�[�W��\������悤�ɂ���
	//�E���W���[���`�F�b�N�ł̂o�j�Ⴂ�̃��b�Z�[�W���s���S�����������C��
	//�EXF390�̍s�ԕ����[���ɂł��Ȃ����������C������
	//�E�N���X�`�F�b�J�[�̃��O�C�������[�h�ݒ�����j���[��`�ɑg�ݍ���
	//�EUndo�̉񐔂𖳐����ɂ���
	//�E�ێ痚����݂��AUNDO�����g���ė���ǉ����̏����l��ݒ肷��悤�ɂ���
	//�E�e�[�u����`��UNDO/REDO���ɃG���[�󋵂��Đݒ肷��悤�ɂ���
	//�EPostgreSQL�Ŋ����t�B�[���h�ŃC���f�b�N�X�����ƃ��W���[���`�F�b�N�����������삵�Ȃ������C��
	//�EPostgreSQL�Ńt�B�[���h�h�c�ɑS�p�p�������������ꍇ�Ƀ��W���[���`�F�b�N�����������삵�Ȃ������C��
	//�EPostgreSQL�̃f�[�^�^character varying��VARCHAR�ɑΉ�������
	//�EXF100,110,300�̌��������̃v�����v�g�I�v�V�����Ƃ��āu��⃊�X�g�v��݂���
	//�E�@�\��`�ւ̃t�B�[���h�g�ݍ��݂̍ۂ̌x�����b�Z�[�W�Ƀf�[�^�\�[�X����g�ݍ���
	//�E�@�\�̎g�r�ꗗ�ɂ����āA���������̃v�����v�^�����̎g�r�������Ă��������C��
	//�EXF000�Ŏ��������l�̎w����e�L�X�g�t�B�[���h�łł���悤�ɂ���
	//
	//18
	//�Exead�t�@�C����XF300�^�C�v�̋@�\��`�̃C���|�[�g�ɂ����āA���o���^���׃e�[�u���̕��̓��W�b�N�����P����
	//�Exead�t�@�C���̃e�[�u����`�̃C���|�[�g�ɂ����āA�P�e�[�u�����ł̃t�B�[���hID�̏d����������Ă����o�O���C������
	//�E�v�����v�^�̌����t�B�[���h�ݒ�_�C�A���O�ŁA�R�s�[�{�^���Ń_�C�A���O�����悤�ɂ���
	//�E�i���ݏ�����VALUE:�̎w����ȗ������ꍇ�A�i���ݏ����̈ꗗ��ŏ����l�Ƃ��Ď�����Ȃ��o�O���C������
	//�E�c���[�r���[��̋@�\��`�̏�Ƀ}�E�X�|�C���^��u���΋@�\�^�C�v���q���g�\�������悤�ɂ���
	//�EXF310�̍s�ǉ����X�g�̌����t�B�[���h�ݒ�_�C�A���O�̕����L����
	//
	//17
	//�E���z�t�B�[���h�̏ꍇ�A�t�B�[���h�ꗗ��Ńt�B�[���hID�݂̂��J�b�R�t������悤�ɂ���
	//�EXF100,110,300�ɂ��āA�����\���I�v�V������g�ݍ���
	//�EXF310�̍s�ǉ����X�g�̎w��łO�̌��������������悤�ɂ���
	//�Exead�t�@�C���̃C���|�[�g�ɂ����āA�e�[�u���̓񎟎��ʎq��`����荞�ނ悤�ɂ���
	//�Exead�t�@�C���̃C���|�[�g�ɂ����āA�e�[�u���̌p����������荞�܂Ȃ��悤�ɂ����i�����t�B�[���h�Ƃ��Ď��ƂŒ�`����邽�߁j
	//�E�v�����v�^�֐��̃p�����[�^�ݒ�_�C�A���O�̕����L����
	//�E���C�A�E�g�m�F�_�C�A���O�ɂ����ē��t�t�B�[���h�̘g���\���ݒ�����P����
	//�EPostgreSQL��bytea�^��BLOB�^�ɑΉ�������悤�ɂ���
	//�E�f�[�^�ێ烆�[�e�B���e�B��BLOB�^��ǂݏ������Ȃ��悤�ɂ���
	//�E�f�[�^�ێ烆�[�e�B���e�B�ŃV���O���N�H�[�e�[�V�������܂ރf�[�^��������悤�ɂ���
	//�E�f�[�^�ێ烆�[�e�B���e�B�łۗ̕����̃R�~�b�g����̃`�F�b�N���W�b�N�����P����
	//�E�����e�[�u���̒ǉ��ݒ�_�C�A���O�̃��C�A�E�g�����P����
	//�E�����e�[�u���̒ǉ���Ɂu������KEY���ځv��ύX�ł���悤�ɂ���
	//�E�e�[�u���X�N���v�g�̎��s�^�C�~���O�̑I���ŁA�����e�[�u�������������ɏ]���Ĉꗗ�����悤�ɂ���
	//�E�c���[�r���[��� Ctrl+C �� Ctrl+V ���g����悤�ɂ���ƂƂ��ɁA�����m�[�h�𓯎��I���ł��Ȃ��悤�ɂ���
	//�E�V�X�e���̋N�������SQL�R���\�[���̎��s�̂��߂̋@�\�L�[��F9����F5�ɕύX����
	//�EXF310�̍s�ǉ����X�g�̕��я��w��ł̃o�O���C������
	//�EXF110�̃o�b�`�t�B�[���h�ɂ��ă��C�A�E�g�\���ł��Ȃ����Ƃ���������C������
	//�E�X�N���v�g�̂P�s���ɕ�����'{'��'}'���܂܂�Ă���Ɛ������C���f���g��������Ȃ������C������
	//�E�E�y�C���Œl��ύX�����Ctrl+S�������Ă��㏑������Ȃ����������C��
	//�E�����t�B�[���h�̎g�r���ɂ��āAXF110�ł̗��p�󋵂��s���m�Ɏ�����Ă��������C������

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
