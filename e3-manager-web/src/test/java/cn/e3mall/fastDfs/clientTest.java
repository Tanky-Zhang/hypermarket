package cn.e3mall.fastDfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.FastDfs.FastDFSClient;

public class clientTest {
	@Test
	public void upload() throws Exception{
	ClientGlobal.init("F:/source/e3-manager-web/src/main/resources/conf/client.conf");
	TrackerClient track=new TrackerClient();
	TrackerServer trackserver =track.getConnection();
	StorageServer stor=null;
	StorageClient sto=new StorageClient(trackserver, stor);
	String[] strings =sto.upload_file("E:/Javaweb学习资料/【阶段16】商城项目/e3商城_day01/黑马32期/01.教案-3.0/01.参考资料/广告图片/a283677a1dfb4deee151421c054baead.jpg", "jpg", null);
	for(String string:strings){
		System.out.println(string);
	}
	
	}
	@Test
	public void fastTest() throws Exception{
	FastDFSClient fast=new FastDFSClient("F:/source/e3-manager-web/src/main/resources/conf/client.conf");
	String string = fast.uploadFile("C:/Users/Administrator/Desktop/报告图片/1F0295DAB05C5A66BF2C0A6F805A6DFB.png");
	System.out.println(string);
	
	}
}
