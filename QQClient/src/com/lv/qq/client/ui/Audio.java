package com.lv.qq.client.ui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

public class Audio {
	
	public static AudioClip MSG_RING = loadAudio(new File("sound/3424.wav"));
	
	public static AudioClip SYSTEM = loadAudio(new File("sound/system.wav"));
	
	public static AudioClip KNOCK = loadAudio(new File("sound/Global.wav"));
	
	@SuppressWarnings("deprecation")
	private static AudioClip loadAudio(File audio){
		try {
			return Applet.newAudioClip(audio.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
