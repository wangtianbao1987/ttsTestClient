package com.pachira.tts.client;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.util.Map;

public abstract class ReqBySocket extends MyReq {
	Map<String, String> map;
	long beforeReqTime = 0;
	
	public ReqBySocket(Map<String, String> map) {
		super(map);
		this.map = map;
	}
	
	@Override
	public void connection() {
		OutputStreamWriter osw = null;
		Socket socket = null;
		InputStream in = null;
		try {
			String url = getUrl();
			URI uri = new URI(url);
			socket = new Socket(uri.getHost(), uri.getPort());
			osw = new OutputStreamWriter(socket.getOutputStream(),"utf-8");
			String m = map.get("-m");
			StringBuffer sb = new StringBuffer();
			String paramStr = null;
			if("POST".equals(m)) {
				sb.append("POST " + uri.getPath() + " HTTP/1.1\r\n");
				String pt = map.get("-pt");
				if("json".equals(pt)) {
					sb.append("Content-Type: application/json\r\n");
					paramStr = getGetParamJson();
				} else {
					sb.append("Content-Type: application/x-www-form-urlencoded\r\n");
					paramStr = getGetParamStr();
				}
				sb.append("Content-Length: "+paramStr.getBytes("UTF-8").length+"\r\n");
			}else {
				sb.append("GET " + uri.getPath() + "?"+getGetParamStr()+" HTTP/1.1\r\n");
			}
			sb.append("Host: " + uri.getHost()+":"+uri.getPort() + "\r\n");
			sb.append("Connection: Keep-Alive\r\n");
			sb.append("\r\n");
			osw.write(sb.toString());
			osw.flush();
			if("POST".equals(m)) {
				osw.write(paramStr);
			}
			osw.flush();
			
			beforeReqTime = System.currentTimeMillis();
			
			in = socket.getInputStream();
			
			
			String line = readLine(in);
			if(!line.contains("200")) {
				System.err.println(line);
				return;
			}
			
			while((!"".equals(readLine(in))));
			
			String trunkStr = null;
			
			while((trunkStr =readLine(in)) != null && !"0".equals(trunkStr)) {
				int trunk = Integer.parseInt(trunkStr, 16);
				byte[] buff = readLen(in, trunk);
				callback(buff, 0, trunk);
				readLen(in, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Utils.close(osw, in, socket);
		}
	}
	
	
	public String readLine(InputStream in) {
		StringBuilder sb = new StringBuilder();
		while(true) {
			byte[] buff = readLen(in, 1);
			if(buff[0] == '\r') {
				break;
			}
			sb.append(new String(buff));
		}
		readLen(in, 1);
		return sb.toString();
	}
	
	public byte[] readLen(InputStream in, int len) {
		byte[] buff = new byte[len];
		try {
			int pos = 0;
			int readLen = 0;
			while(pos < len && ((readLen=in.read(buff, pos, len-pos)) != -1)) {
				pos += readLen;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return buff;
	}
	
	public long getBeforeReqTime() {
		return beforeReqTime;
	}
	

}
