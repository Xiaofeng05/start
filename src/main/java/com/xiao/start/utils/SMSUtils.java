package com.xiao.start.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;

/**
 * 短信发送工具类
 */
public class SMSUtils {



	@Value("${AccessKeyId}")
	public static String AccessKeyId;

	@Value("${AccessKeySecret}")
	public static String AccessKeySecret;

	@Value("${SignName")
	public static String SignName;

	@Value("${TemplateCode")
	public static String TemplateCode;

	/**
	 * 发送短信
	 * @param signName 签名
	 * @param templateCode 模板
	 * @param phoneNumbers 手机号
	 * @param param 参数
	 */
	public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", AccessKeyId, AccessKeySecret);
		IAcsClient client = new DefaultAcsClient(profile);

		SendSmsRequest request = new SendSmsRequest();
		request.setPhoneNumbers(phoneNumbers);
		request.setSignName(signName);
		request.setTemplateCode(templateCode);
		request.setTemplateParam("{\"code\":\""+param+"\"}");
		try {
			SendSmsResponse response = client.getAcsResponse(request);
			System.out.println("短信发送成功");
		}catch (ClientException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		sendMessage(SignName,TemplateCode,"18838563912","6542");
	}
}
