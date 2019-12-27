package com.pachira.tts.client;

import java.util.HashMap;
import java.util.Map;

public class Test01 {
	private static long temp = 0;
	
	public static void main(String[] args) {
		args = new String[] {
			"-h","172.22.144.135",
			"-p","8181",
			"-pt","json",
			"-text","你好小意",
			"-t","socket"
		};
		if(args == null || args.length==0) {
			Utils.tip();
			return;
		}
		Map<String,String> param = new HashMap<String,String>();
		Utils.initArgs(param);
		Utils.setParams(param, args);
		
		MyReq req = null;
		String t = param.get("-t");
		
		if("httpclient".equals(t)){
			req = new ReqByHttpUtils(param) {
				@Override
				public void callback(byte[] buff, int offset, int len) {
					long tmp = System.currentTimeMillis();
					if(temp == 0) {
						temp = getBeforeReqTime();
					}
					System.out.println("buffTime="+ (tmp - temp) +"; buffSize="+len);
					temp = System.currentTimeMillis();
				}
			};
		} else if("socket".equals(t)) {
			req = new ReqBySocket(param) {
				@Override
				public void callback(byte[] buff, int offset, int len) {
					long tmp = System.currentTimeMillis();
					if(temp == 0) {
						temp = getBeforeReqTime();
					}
					System.out.println("buffTime="+ (tmp - temp) +"; buffSize="+len);
					temp = System.currentTimeMillis();
				}
			};
		}
		req.connection();
		System.out.println("all time: " + (temp - req.getBeforeReqTime()));
		
	}
	
	
	
}
