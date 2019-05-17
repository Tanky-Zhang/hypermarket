package cn.e3mall.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.FastDfs.FastDFSClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

@Controller
public class pictureController {
	@Value("${URL}")
	private String URL;

	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		try {
			FastDFSClient fast = new FastDFSClient("classpath:conf/client.conf");
			String name = uploadFile.getOriginalFilename();
			String string = fast.uploadFile(uploadFile.getBytes(), name.substring(name.lastIndexOf(".") + 1));
            string=URL+string;
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", string);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
		// return null;
	}
	
}
