package com.lv.qq.client.ui;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImgIcon {
	//公用图片
	public static ImageIcon BTN_CLOSE = new ImageIcon("image/btnClose.png");
	public static ImageIcon BTN_MINIMIZE = new ImageIcon("image/btnMinimize.png");
	public static ImageIcon BTN_SETTING = new ImageIcon("image/btnSetting.png");
	public static ImageIcon HEAD_PHOTO = new ImageIcon("image/defaltHeadDog.png");
	//public static ImageIcon HEAD_PHOTO_MINI = new ImageIcon("image/defaltHeadMini.png");
	public static ImageIcon HEAD_PHOTO_MEDIUM = new ImageIcon(HEAD_PHOTO.getImage().getScaledInstance(60, 60,  
            Image.SCALE_DEFAULT)); 
	public static ImageIcon HEAD_PHOTO_MINI = new ImageIcon(HEAD_PHOTO.getImage().getScaledInstance(40, 40,  
            Image.SCALE_DEFAULT)); 
	public static ImageIcon HEAD_PHOTO_OUTLINE = new ImageIcon("image/defaltHeadOutLineDog.png");
	public static ImageIcon HEAD_PHOTO_OUTLINE_MEDIUM = new ImageIcon(HEAD_PHOTO_OUTLINE.getImage().getScaledInstance(60, 60,  
            Image.SCALE_DEFAULT)); 
	public static ImageIcon HEAD_PHOTO_OUTLINE_MINI = new ImageIcon(HEAD_PHOTO_OUTLINE.getImage().getScaledInstance(40, 40,  
            Image.SCALE_DEFAULT)); 
	public static ImageIcon TITLE_BG = new ImageIcon("image/titleBg.png");
	public static ImageIcon LOADING = new ImageIcon("image/loading.gif");
	
	//登陆界面图片
	public static ImageIcon BG_LOGIN = new ImageIcon("image/login/bgLoginOO.png");
	public static ImageIcon BG_LOGIN_FAIL = new ImageIcon("image/login/loginFail.png");
	public static ImageIcon BTN_LOGIN = new ImageIcon("image/login/btnLogin.png");
	public static ImageIcon BTN_CANCER = new ImageIcon("image/login/btnCancer.png");
	
	//主菜单界面图片
	public static ImageIcon BG_CLIENT = new ImageIcon("image/client/bg.png");
	public static ImageIcon ARROW_OPEN = new ImageIcon("image/client/arrowOpen.png");
	public static ImageIcon ARROW_CLOSE = new ImageIcon("image/client/arrowClose.png");
	public static ImageIcon BTN_ADD = new ImageIcon("image/client/add.png");
	public static ImageIcon BTN_MENU = new ImageIcon("image/client/menu.png");
	public static ImageIcon NULL = new ImageIcon("image/client/null.png");
	public static ImageIcon INFORM = new ImageIcon("image/client/inform.png");
	
	//对话窗口界面图片
	public static ImageIcon BTN_ICON_SEND = new ImageIcon("image/chat/btnSend.png");
	public static ImageIcon BTN_ICON_CLOSE = new ImageIcon("image/chat/btnClose.png");
	public static ImageIcon SHOW_MAN = new ImageIcon("image/chat/man.png");
	public static ImageIcon SHOW_WOMAN = new ImageIcon("image/chat/woman.png");
	public static ImageIcon SHOW_SECRET = new ImageIcon("image/chat/secret.png");
	public static ImageIcon SHOW_GAY = new ImageIcon("image/chat/gay.png");
	public static ImageIcon SHOW_LES = new ImageIcon("image/chat/lesbian.png");
	public static ImageIcon BTN_CLOSE_GRAY = new ImageIcon("image/chat/closeG.png");
	public static ImageIcon BTN_CLOSE_RED = new ImageIcon("image/chat/closeR.png");
	//public static ImageIcon CHAT_BTN_CLOSE = new ImageIcon("image/chat/chatClose.png");
	//public static ImageIcon CHAT_BTN_MINIMIZE = new ImageIcon("image/chat/chatMinimize.png");
	public static ImageIcon CHAT_TITLE_BG = new ImageIcon("image/chat/titleBg.png");
	public static ImageIcon CHAT_BG = new ImageIcon("image/chat/chatBg.png");
	public static ImageIcon CHAT_HEAD_PHOTO = new ImageIcon(HEAD_PHOTO.getImage().getScaledInstance(30, 30,  
            Image.SCALE_DEFAULT)); 
	public static ImageIcon WORD_BG_R = new ImageIcon("image/chat/wordBgR.png");
	public static ImageIcon WORD_BG_L = new ImageIcon("image/chat/wordBgL.png");
	public static ImageIcon DOT_RED_S = new ImageIcon("image/chat/dotRed.png");
	public static ImageIcon DOT_RED_M = new ImageIcon(DOT_RED_S.getImage().getScaledInstance(22, 20,  
            Image.SCALE_DEFAULT));
	public static ImageIcon DOT_RED_L = new ImageIcon(DOT_RED_S.getImage().getScaledInstance(26, 22,  
            Image.SCALE_DEFAULT));
	public static ImageIcon FILE_LOGO = new ImageIcon("image/chat/fileLogo.png");
	public static ImageIcon FILE_UPLOAD = new ImageIcon("image/chat/fileUpload.png");
	public static ImageIcon FILE_DOWNLOAD = new ImageIcon("image/chat/fileDownload.png");
	public static ImageIcon TRANS_SUCCESS = new ImageIcon("image/chat/transSuccess.png");
	public static ImageIcon WRONG_MSG_BG = new ImageIcon("image/chat/wrongMsgBg.png");
	public static ImageIcon EXPRESSION = new ImageIcon("image/chat/expression.png");
	
	//查找好友界面图片
	public static ImageIcon ADD_USER_TITLE_BG = new ImageIcon("image/adduser/titleBg.png");
	public static ImageIcon ADD_USER_FINGER = new ImageIcon("image/adduser/finger.png");
	public static ImageIcon BTN_ADD_USER = new ImageIcon("image/adduser/addUser.png");
	public static ImageIcon BTN_SEARCH = new ImageIcon("image/adduser/btnSearch.png");
	public static ImageIcon SUCCESS_LOGO = new ImageIcon("image/adduser/success.png");
	
	//注册界面图片
	public static ImageIcon BTN_REGISTER = new ImageIcon("image/register/btnRegister.png");
}
