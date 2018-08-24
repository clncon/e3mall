package cn.e3mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.comm.util.FastDFSClient;

public class FastDfsTest {

	@Test
	public void testUpload()throws Exception{
		
		//创建配置文件。文件名任意。内容就是tracker服务器的地址
		//使用全局对象加载配置文件
	     ClientGlobal.init("F:/e3mall/e3-manager-web/src/main/resources/conf/client.conf");
		//创建一个TrackerClient对象
	     TrackerClient trackerClient = new TrackerClient();
		//通过TrackerClient获取一个TrackerServer对象
	     TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StrorageServer的引用，可以是null
	     StorageServer storageServer= null;
		//创建一个StorageClient,参数需要TrackerServer和StrorageServer
	     StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用StorageClient上传文件
	    String[] strs= storageClient.upload_file("C:/Users/孔旻/Pictures/IQIYISnapShot/[www.loldytt.com]LEG[00-18-43][20180415-212022883].jpg", "jpg",null);
	    
	    for(String str:strs){
	    	System.out.println(str);
	    }
	}
	
	@Test
	public void testFastDfsClient() throws Exception{
		  
		FastDFSClient fastDFSClient = new FastDFSClient("F:/e3mall/e3-manager-web/src/main/resources/conf/client.conf");
		
		String result = fastDFSClient.uploadFile("C:/Users/孔旻/Pictures/IQIYISnapShot/[Kisssub][Himouto! U[00-00-22][20180103-231146456].jpg");
		System.out.println("result:"+result);
	}
}
