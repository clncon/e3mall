package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerTest {
	@Test
	public void testFreeMarker(){
		//1.创建一个模板文件
		//2.创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		try {
			//3.设置模板文件保存的目录
			configuration.setDirectoryForTemplateLoading(new File("F:\\e3mall\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
			//4.模板文件的编码格式，一般就是utf-8
		     configuration.setDefaultEncoding("UTF-8");
			//5.加载一个模板文件，创建一个模板对象
		     Template template = configuration.getTemplate("student.ftl");
			//6.创建一个数据集。可以是pojo也可以是map,推荐使用map
		     Map map = new HashMap<>();
		     List<Student> list = new ArrayList<>();
		     list.add(new Student(1,"小明",18,"回龙观1"));
		     list.add(new Student(2,"小红",19,"回龙观2"));
		     list.add(new Student(3,"小男",20,"回龙观3"));
		     list.add(new Student(4,"小被",21,"回龙观4"));
		     list.add(new Student(5,"小绿",22,"回龙观5"));
		     list.add(new Student(6,"小器",23,"回龙观6"));
		     list.add(new Student(7,"小王",24,"回龙观7"));
		     Student student = new Student(1,"小明",18,"回龙观");
		     map.put("hello","hello freemarker");
		     map.put("student", student);
		     map.put("studentList",list);
		     map.put("val","345");
		     //添加日期类型
		      map.put("date",new Date());
		     //7.创建一个writer对象，指定输出文件的路劲以及文件名
		     Writer fileWriter = new FileWriter(new File("D:\\api\\student.html"));
		     //8.生成静态页面
		     template.process(map, fileWriter);
		     //9.关闭流
		     fileWriter.close();
		     
		} catch (IOException | TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
