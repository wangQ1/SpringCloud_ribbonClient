package cn.et.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
public class SendController {
	@Autowired
	private RestTemplate template;//Spring提供的用于访问Rest服务的客户端，RestTemplate提供了多种便捷访问远程Http服务的方法
	@Autowired
    private LoadBalancerClient loadBalancer;
    /**
     * 启动多个发布者 端口不一致 程序名相同
     * 使用
     * @LoadBalanced 必须添加
     * @return
     */
	@ResponseBody
    @GetMapping("/choosePub")
    public String choosePub() {
        StringBuffer sb=new StringBuffer();
        for(int i = 0; i < 10; i++) {
            ServiceInstance ss = loadBalancer.choose("sendMail");//从两个idserver中选择一个 这里涉及到选择算法
            sb.append(ss.getUri().toString() + "<br/>");
        }
        return sb.toString();
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/sendClient")
	public String sendMail(String email_to, String email_subject, String email_content){
		//通过注册中心客户端负载均横  获取并调用一个sendMail服务
		try {
			HttpHeaders hh = new HttpHeaders();
			Map map = new HashMap();
			map.put("email_to", email_to);
			map.put("email_subject", email_subject);
			map.put("email_content", email_content);
			HttpEntity he = new HttpEntity(map, hh);
			//getForObject("http://sendMail/sendMail/{id}", String.class, 1);get请求 参数自动填充到uri中
			template.postForEntity("http://sendMail/send", he, String.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			return "redirect:/failed.html";//请求重定向，html默认不支持请求转发
		}
		return "redirect:/suc.html";//请求重定向，html默认不支持请求转发
	}
}
