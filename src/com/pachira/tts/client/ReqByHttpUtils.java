package com.pachira.tts.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public abstract class ReqByHttpUtils extends MyReq {
	private Map<String, String> map;
	private long beforeReqTime = 0;
	
	public ReqByHttpUtils(Map<String, String> map) {
		super(map);
		this.map = map;
	}
	
	@Override
	public void connection() {
		InputStream in = null;
		try {
			String m = map.get("-m").toUpperCase();
			HttpResponse resp = null;
			int buffLen = Integer.parseInt(map.get("-buff"));
			if("GET".equals(m)) {
				resp = new HttpUtils() {
					public void beforeReq() {
						beforeReqTime = System.currentTimeMillis();
					};
				}.doGet(getUrl() + "?" + getGetParamStr(), new HashMap<String,String>());
			} else if("POST".equals(m)) {
				String pt = map.get("-pt");
				if("json".equals(pt)) {
					resp = new HttpUtils() {
						public void beforeReq() {
							beforeReqTime = System.currentTimeMillis();
						};
					}.doPost(getUrl(), new HashMap<String,String>(), null, getGetParamJson());
				} else {
					resp = new HttpUtils() {
						public void beforeReq() {
							beforeReqTime = System.currentTimeMillis();
						};
					}.doPost(getUrl(), new HashMap<String,String>(), null, getGetParamStr());
				}
			}
			HttpEntity entry = resp.getEntity();
			in = entry.getContent();
			byte[] buff = new byte[buffLen];
			int len = 0;
			while((len=in.read(buff)) != -1) {
				callback(buff, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Utils.close(in);
		}
	}

	public long getBeforeReqTime() {
		return beforeReqTime;
	}

}
