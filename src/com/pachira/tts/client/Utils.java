package com.pachira.tts.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Map;

public class Utils {
	public static void runMethod(String methodName,Object... objs){
		if(objs == null){
			return;
		}
		for(Object obj:objs){
			if(obj == null){
				return;
			}
			try {
				Method m = obj.getClass().getMethod(methodName);
				m.invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Object... objs){
		if(objs == null){
			return;
		}
		for(Object obj:objs){
			if(obj == null){
				return;
			}
			try {
				if(obj instanceof Closeable){
					((Closeable) obj).close();
				}else if(obj instanceof AutoCloseable){
					((AutoCloseable) obj).close();
				}else {
					runMethod("close", obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void tip() {
		StringBuilder sb = new StringBuilder();
		sb.append("java -jar ttsTestClient.jar [-param paramVal]\n")
			.append("\t-t:\t").append("调用类型。").append("默认：httpclient ").append("可选参数：httpclient/socket\n")
			.append("\t-h:\t").append("IP地址。").append("默认：192.168.128.49\n")
			.append("\t-p:\t").append("端口。").append("默认：8888\n")
			.append("\t-m:\t").append("请求类型POST/GET。").append("默认：POST\n")
			.append("\t-pt:\t").append("POST请求时传入方式:").append("url/json").append("默认：url\n")
			.append("\t-o:\t").append("输出目录。").append("不设置时不保存音频\n")
			.append("\t-buff:\t").append("缓冲大小。").append("默认：1024 ").append("当调用类型为httpclient时该参数有效\n")
			.append("\t-volume:\t").append("音量。").append("默认：1\n")
			.append("\t-speed:\t").append("语速。").append("默认：1\n")
			.append("\t-pitch:\t").append("音调。").append("默认：1\n")
			.append("\t-voice_name:\t").append("发音人。").append("默认：xiaoqing\n")
			.append("\t-sample_rate:\t").append("采样率。").append("默认：16000\n")
			.append("\t-bit:\t").append("采样位数。").append("默认：16\n")
			.append("\t-tag_mode:\t").append("是否使用SSML。").append("默认：1\n")
			.append("\t-eng_mode:\t").append("英文读法。").append("默认：0\n")
			.append("\t-format:\t").append("输出音频格式。").append("默认：pcm\n")
			.append("\t-start_byte:\t").append("开始位置。").append("默认：0\n")
			.append("\t-text:\t").append("TTS文本。 ").append("复杂文本可使用参数-text_file指定TTS文本\n")
			.append("\t-text_file:\t").append("TTS文本文件。\n")
			.append("\t-thread:\t").append("并发数。").append("默认：1\n");
			;
		System.out.println(sb);
	}
	
	public static void paramSet(Map<String,String> args) {
		String type = args.get("-t");
		String ip = args.get("-h");
		String port = args.get("-p");
		String method = args.get("-m");
		String buff = args.get("-buff");
		String volume = args.get("-volume");
		String speed = args.get("-speed");
		String pitch = args.get("-pitch");
		String voice_name = args.get("-voice_name");
		String sample_rate = args.get("-sample_rate");
		String bit = args.get("-bit");
		String tag_mode = args.get("-tag_mode");
		String eng_mode = args.get("-eng_mode");
		String format = args.get("-format");
		String language = args.get("-language");
		String text = args.get("-text");
		String text_file = args.get("-text_file");
		String thread = args.get("-thread");
		
		type = (type==null?"httpclient" : type);
		ip = (ip==null?"192.168.128.49" : ip);
		port = (port==null?"8888" : port);
		method = (method==null?"post" : method);
		buff = (buff==null?"1024":buff);
		volume = (volume==null?"1" : volume);
		speed = (speed==null?"1" : speed);
		pitch = (pitch==null?"1" : pitch);
		thread = (thread==null?"1" : thread);
		voice_name = (voice_name==null?"xiaoqing" : voice_name);
		sample_rate = (sample_rate==null?"16000" : sample_rate);
		bit = (bit==null?"16" : bit);
		tag_mode = (tag_mode==null?"1" : tag_mode);
		eng_mode = (eng_mode==null?"0" : eng_mode);
		format = (format==null?"pcm" : format);
		language = (language==null?"zh-cmn" : language);
		if(text == null) {
			if(text_file == null) {
				text = "测试";
			}
			File textFile = new File(text_file);
			if(!textFile.isFile()) {
				text = "测试";
			}else {
				text = readFile(textFile);
				if("".equals(text)) {
					text = "测试";
				}
			}
		}
		args.put("-t", type);
		args.put("-h", ip);
		args.put("-p", port);
		args.put("-m", method);
		args.put("-buff", buff);
		args.put("-volume", volume);
		args.put("-speed", speed);
		args.put("-pitch", pitch);
		args.put("-voice_name", voice_name);
		args.put("-sample_rate", sample_rate);
		args.put("-bit", bit);
		args.put("-tag_mode", tag_mode);
		args.put("-eng_mode", eng_mode);
		args.put("-format", format);
		args.put("-language", language);
		args.put("-text", text);
		args.put("-thread", thread);
	}
	
	
	public static String readFile(File file) {
		BufferedReader br = null;
		FileReader fr = null;
		StringBuilder sb = new StringBuilder();
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = null;
			while((line=br.readLine()) != null) {
				sb.append("\n").append(line);
			}
			if(sb.length() == 0) {
				return "";
			} else {
				return sb.substring(1).trim();
			}
		} catch (Exception e) {
			return "";
		} finally {
			if(br != null) {try{br.close();}catch (Exception e) {}}
			if(fr != null) {try{fr.close();}catch (Exception e) {}}
		}
	}
	
	
	public static void initArgs(Map<String,String> map) {
		map.put("-t", "httpclient");
		map.put("-h", "192.168.128.49");
		map.put("-p", "8888");
		map.put("-m", "POST");
		map.put("-buff", "1024");
		map.put("-volume", "1");
		map.put("-speed", "1");
		map.put("-pitch", "1");
		map.put("-voice_name", "xiaoqing");
		map.put("-sample_rate", "16000");
		map.put("-bit", "16");
		map.put("-tag_mode", "1");
		map.put("-eng_mode", "0");
		map.put("-format", "pcm");
		map.put("-thread", "1");
		map.put("-start_byte", "0");
		map.put("-pt", "url");
	}
	
	public static void setParams(Map<String,String> map, String[] args) {
		String key = null;
		for(String arg : args) {
			if (key == null) {
				if(arg.startsWith("-")) {
					key = arg.toLowerCase();
				}
			}else {
				map.put(key, arg);
				key = null;
			}
		}
	}
	
}
