package com.lv.qq.client.util;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class InnerPasswordField extends JPasswordField {

	private static final long serialVersionUID = 1L;
	
	private String defaltText;
	
	public InnerPasswordField(String text){
		super(text);
		this.defaltText = text;
		this.setEchoChar('\0');
		this.setForeground(new Color(174, 174, 174));
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if(String.valueOf(getPassword()).trim().equals("")){
					setText(defaltText);
					setEchoChar('\0');
					setForeground(new Color(174, 174, 174));
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(String.valueOf(getPassword()).equals(defaltText)){
					setText("");
					setEchoChar('¡ñ');
					setForeground(Color.BLACK);
				}
			}
		});
	}

}
