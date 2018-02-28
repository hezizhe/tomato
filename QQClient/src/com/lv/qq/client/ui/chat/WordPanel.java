package com.lv.qq.client.ui.chat;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lv.qq.client.config.PersonalConfig;
import com.lv.qq.client.ui.ImgIcon;
import com.lv.qq.client.util.LinkLable;
import com.lv.qq.client.util.StringTools;
import com.lv.qq.common.vo.ChatWord;

public class WordPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static Desktop DISKTOP = Desktop.getDesktop();
	
	public static int LEFT = 1;
	public static int RIGHT = 2;
	
	private ChatWord chatWord;
	private String word;
	private Font font;
	private int layout;
	private String showTime;
	private String name;
	
	private int contentWidth;
	private int contentHight;
	
	private int x;
	private int y;
	
	public WordPanel(String word, Font font, int layout, String showTime,
			String name) {
		this.word = word;
		this.font = font;
		this.layout = layout;
		this.showTime = showTime;
		this.name = name;
		replaceExpression();
		countWidthAndHight();
		if(layout == LEFT){
			x = 47;
		}else{
			x = 26 + 400 - contentWidth;
		}
		y = 0;
		if(showTime != null)  y += 20;
		if(name != null)  y += 20;
		
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		JLabel text = new JLabel();
		text.setText(this.word);
		text.setFont(font);
		//text.setLineWrap(true);
		//text.setEditable(false);
		if (layout == LEFT) {
			text.setBounds(x + 12, y + 10, contentWidth, contentHight);
			//text.setBackground(new Color(223, 223, 222));
		} else {
			text.setBounds(x + 22, y + 15, contentWidth, contentHight);
			//text.setBackground(new Color(172, 217, 248));
		}
		this.add(text);
		this.setPreferredSize(new Dimension(514, y + contentHight + 28));
	}
	
	public WordPanel(ChatWord chatWord, int layout, String showTime,
			String name) {
		this.chatWord = chatWord;
		this.layout = layout;
		this.showTime = showTime;
		this.name = name;
		// TODO
		contentWidth = 290;
		contentHight = 90;
		if(layout == LEFT){
			x = 47;
		}else{
			x = 26 + 400 - contentWidth;
		}
		y = 0;
		if(showTime != null)  y += 20;
		if(name != null)  y += 20;
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		JPanel filePanel = createFilePanel();
		if (layout == LEFT) {
			filePanel.setBounds(x + 11, y + 11, contentWidth, contentHight);
		} else {
			filePanel.setBounds(x + 16, y + 16, contentWidth, contentHight);
		}
		this.add(filePanel);
		this.setPreferredSize(new Dimension(514, y + contentHight + 28));
	}
	
	private JPanel createFilePanel() {
		JPanel filePanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImgIcon.FILE_LOGO.getImage(), 10, 10, 40, 40, null);
				g.drawImage(ImgIcon.TRANS_SUCCESS.getImage(), 38, 38, 15, 15, null);
				g.setColor(new Color(207, 219, 226));
				g.fillRect(0, 63, contentWidth, 1);
			}
		};
		filePanel.setLayout(null);
		filePanel.setPreferredSize(new Dimension(contentWidth, contentHight));
		filePanel.setBackground(Color.WHITE);
		JLabel fileName = new JLabel(chatWord.getFileName());
		fileName.setBounds(60, 10, 220, 20);
		filePanel.add(fileName);
		JLabel status = new JLabel();
		status.setForeground(new Color(119, 119, 119));
		status.setFont(new Font("宋体", Font.PLAIN, 12));
		if (layout == LEFT) {
			status.setText("成功存至" + chatWord.getFilePath() + "/" + chatWord.getFileName());
		} else {
			status.setText("成功发送文件");
		}
		status.setBounds(60, 36, 220, 14);
		filePanel.add(status);
		LinkLable openFile = new LinkLable("打开");
		openFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File file = new File(chatWord.getFilePath() + "/" + chatWord.getFileName());
				try {
					DISKTOP.open(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		openFile.setBounds(186, 68, 24, 14);
		filePanel.add(openFile);
		LinkLable openFolder = new LinkLable("打开文件夹");
		openFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File folder = new File(chatWord.getFilePath());
				try {
					DISKTOP.open(folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		openFolder.setBounds(220, 68, 60, 14);
		filePanel.add(openFolder);
		return filePanel;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (layout == LEFT) {
			g.drawImage(ImgIcon.CHAT_HEAD_PHOTO.getImage(), 12, y + 2, 42, y + 32, 0, 0, 30, 30, null);
			createWordBg(g, ImgIcon.WORD_BG_L.getImage(), 11, 15, 5, 18, 16, 5);
		} else {
			createWordBg(g, ImgIcon.WORD_BG_R.getImage(), 22, 12, 12, 21, 15, 7);
			g.drawImage(ImgIcon.CHAT_HEAD_PHOTO.getImage(), 465, y + 2, 495, y + 32, 0, 0, 30, 30, null);
		}
	}

	private void createWordBg(Graphics g,Image bg, int w1, int w2, int w3, int h1, int h2, int h3) {
		g.drawImage(bg, x, y, x + w1, y + h1, 0, 0, w1, h1, null);
		g.drawImage(bg, x + w1, y, x + w1 + contentWidth, y + h1, w1, 0, w1 + w2, h1, null);
		g.drawImage(bg, x + w1 + contentWidth, y, x + w1 + contentWidth + w3,  y + h1, w1 + w2, 0, w1 + w2 + w3, h1, null);
		
		g.drawImage(bg, x, y + h1, x + w1, y + h1 + contentHight, 0, h1, w1, h1 + h2, null);
		g.drawImage(bg, x + w1,  y + h1, x + w1 + contentWidth, y + h1 + contentHight, w1, h1, w1 + w2, h1 + h2, null);
		g.drawImage(bg, x + w1 + contentWidth,  y + h1, x + w1 + contentWidth + w3, y + h1 + contentHight, w1 + w2, h1, w1 + w2 + w3, h1 + h2, null);
		
		g.drawImage(bg, x, y + h1 + contentHight, x + w1, y + h1 + contentHight + h3, 0, h1 + h2, w1, h1 + h2 + h3, null);
		g.drawImage(bg, x + w1, y + h1 + contentHight, x + w1 + contentWidth, y + h1 + contentHight + h3, w1, h1 + h2, w1 + w2, h1 + h2 + h3, null);
		g.drawImage(bg, x + w1 + contentWidth, y + h1 + contentHight, x + w1 + contentWidth + w3, y + h1 + contentHight + h3, w1 + w2, h1 + h2, w1 + w2 + w3, h1 + h2 + h3, null);
	}
	
	private void replaceExpression() {
		Pattern p = Pattern.compile("(</.*?>)");
		Matcher m = p.matcher(this.word);
		while (m.find()) {
			String expression = m.group(1);
			String gifName = expression.substring(2, expression.length() - 1);
			this.word = this.word.replace(expression, "<IMG src=\"file:///" + PersonalConfig.EXPRESSION_PATH +"/" + gifName + ".gif\">");
		}
		System.out.println("replaceExpression:" + word);
	}
	
	private void countWidthAndHight(){
		FontMetrics fm = new JLabel().getFontMetrics(font);
		int hight = fm.getHeight();
		String[] paragraphText = word.split("\n");
		contentWidth = 0;
		word = "";
		for (String str : paragraphText) {
			str += " ";
			String[] ss = str.split("(<IMG.*?>)");
			List<String> list = StringTools.getMatch(str, "(<IMG.*?>)");
			String result = "";
			System.out.println("ss.length" + ss.length);
			int lineWidth = 0;
			for (int j = 0; j < ss.length; j++) {
				String s = ss[j];
				for (int i = 0; i < s.length(); i++) {
					int w = fm.stringWidth(String.valueOf(s.charAt(i)));
					lineWidth += w;
					if(lineWidth > 400){
						String left = s.substring(0, i);
						String right = s.substring(i);
						s = left + "<br>" + right;
						lineWidth = 0;
						i += 3;
					}
					System.out.println("lineWidth" + lineWidth);
					if(lineWidth > contentWidth){
						contentWidth = lineWidth;
					}
				}
				if(j != ss.length - 1){
					if(lineWidth > 360){
						s += "<br>";
						lineWidth = 30;
					}else {
						lineWidth += 30;
					}
					System.out.println("lineWidth" + lineWidth);
					if(lineWidth > contentWidth){
						contentWidth = lineWidth;
					}
				}
				if(j < list.size()){
					s += list.get(j);
				}
				result += s;
			}
			str = result;
			String[] lineTexts = str.split("<br>");
			for (String lt : lineTexts) {
				if (hight < 40 && StringTools.getMatch(lt, "(<IMG.*?>)").size() > 0){
						contentHight += 33;
				} else {
					contentHight += hight;
				}
			}
			word += str + "<br>";
		}
		if(word.endsWith("<br>")){
			word = word.substring(0, word.length() - 4);
		}
		word = replaceAngle(word);
		word = "<html><body>" + word + "</body></html>";
		System.out.println(word);
		//contentWidth = 410;
		/*for (int i = 0; i < paragraphText.length; i++) {
			int w = fm.stringWidth(paragraphText[i]);
			widthMax = w > widthMax ? w : widthMax;
			if (w == 0){
				line++;
			} else {
				line += w % 400 > 0 ? w / 400 + 1 : w / 400;
			}
			System.out.println(line);
		}
		contentWidth = widthMax > 400 ? 400 : widthMax;
		ontentHight = hight * line;*/
	}
	
	private String replaceAngle(String str){
		String[] ss = str.split("(<IMG.*?>)");
		List<String> list = StringTools.getMatch(str, "(<IMG.*?>)");
		String s = "";
		for (int i = 0; i < ss.length; i++) {
			ss[i] = ss[i].replace("<", "&lt;").replace(">", "&gt;").replace("&lt;br&gt;", "<br>");
			if(i < list.size()){
				ss[i] += list.get(i);
			}
			s += ss[i];
		}
		return s;
	}
	
}
