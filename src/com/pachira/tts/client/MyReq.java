package com.pachira.tts.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLEncoder;
import java.util.Map;

public abstract class MyReq {
	private Map<String, String> map;
	private String finalText;
	public MyReq(Map<String, String> map) {
		this.map = map;
		this.finalText = map.get("-text");
		String text_file = map.get("-text_file");
		if(this.finalText == null && text_file != null) {
			BufferedReader br = null;
			FileReader fr = null;
			try {
				fr = new FileReader(text_file);
				br = new BufferedReader(fr);
				String line = null;
				StringBuilder sb = new StringBuilder();
				while((line=br.readLine()) != null) {
					sb.append("\n").append(line);
				}
				this.finalText = sb.substring(1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Utils.close(fr, br);
			}
		}
		
	}
	
	public abstract void callback(byte[] buff, int offset, int len);
	
	public abstract void connection();
	
	public String getUrl() {
		return "http://"+map.get("-h")+":"+map.get("-p")+"/voice/tts";
	}
	
	public String getGetParamStr() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("volume=").append(map.get("-volume")).append("&speed=").append(map.get("-speed"))
			.append("&pitch=").append(map.get("-pitch")).append("&voice_name=").append(map.get("-voice_name"))
			.append("&sample_rate=").append(map.get("-sample_rate")).append("&bit=").append(map.get("-bit"))
			.append("&tag_mode=").append(map.get("-tag_mode")).append("&eng_mode=").append(map.get("-eng_mode"))
			.append("&format=").append(map.get("-format")).append("&start_byte=").append(map.get("-start_byte"))
			.append("-text").append(URLEncoder.encode(finalText, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public String getGetParamJson() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("{\"volume\":").append(map.get("-volume")).append(",\"speed\":").append(map.get("-speed"))
			.append(",\"pitch\":").append(map.get("-pitch")).append(",\"voice_name\":\"").append(map.get("-voice_name"))
			.append("\",\"sample_rate\":").append(map.get("-sample_rate")).append(",\"bit\":").append(map.get("-bit"))
			.append(",\"tag_mode\":").append(map.get("-tag_mode")).append(",\"eng_mode\":").append(map.get("-eng_mode"))
			.append(",\"format\":\"").append(map.get("-format")).append("\",\"start_byte\":").append(map.get("-start_byte"))
			.append(",\"text\":\"").append(finalText.replace("\"", "\\\"")).append("\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public abstract long getBeforeReqTime();
	
}
