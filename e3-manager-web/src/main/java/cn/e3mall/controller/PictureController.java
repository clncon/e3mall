package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.comm.util.FastDFSClient;
import cn.e3mall.comm.util.JsonUtils;

@Controller
public class PictureController {

	 @Value("${IMAGE_SERVER_URL}")
	 private String IMAGE_SERVER_URL;
	 
	 @RequestMapping("/pic/upload")
	 @ResponseBody
	 public String fileUpload(MultipartFile uploadFile){
		 
		 try {
			 //1.取得文件的扩展名
			 String originalFilename = uploadFile.getOriginalFilename();
			 String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			 //2.创建一个FastDFS的客户端
			 FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			 //3.执行上传处理
			 String path = fastDFSClient.uploadFile(uploadFile.getBytes(),extName);
			 //4.拼接返回的url地址和ip地址，拼接成完整的url
			 String url = IMAGE_SERVER_URL+path;
			 //5.返回map
			 Map result = new HashMap<>();
			 result.put("error", 0);
			 result.put("url", url);
			 return JsonUtils.objectToJson(result);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//5.返回map
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message","图片上传失败");
			return JsonUtils.objectToJson(result);
		}

		 
	 }
}
