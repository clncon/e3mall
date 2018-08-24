package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成静态页面测试Controller
 * @author 孔旻
 *
 */
@Controller
public class HtmlGenController {
    @Autowired
	private FreeMarkerConfigurer freemakerConfigurer;
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		
		Configuration configuration = freemakerConfigurer.getConfiguration();
		//加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集
		  Map data = new HashMap<>();
		  data.put("hello", 123456);
		//指定文件输出的路径和文件名
		  FileWriter fileWriter = new FileWriter(new File("D:\\api\\hell2.html"));
		//输出文件
		  template.process(data, fileWriter);
		//关闭流
		  fileWriter.close();
		return "ok";
		
	}
}
