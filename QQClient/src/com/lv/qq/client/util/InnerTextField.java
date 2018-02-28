package com.lv.qq.client.util;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class InnerTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	
	private String defaltText;
	
	public InnerTextField(String text){
		super(text);
		this.defaltText = text;
		this.setForeground(new Color(174, 174, 174));
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if(getText().trim().equals("")){
					setText(defaltText);
					setForeground(new Color(174, 174, 174));
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(getText().equals(defaltText)){
					setText("");
					setForeground(Color.BLACK);
				}
			}
		});
	}

}
