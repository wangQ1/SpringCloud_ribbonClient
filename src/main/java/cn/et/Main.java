package cn.et;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
//Eureka是一个服务发现框架,其中包含两个组件：Eureka Server和Eureka Client， Eureka Server提供服务注册服务
//各个节点启动后，会在Eureka Server中进行注册。Eureka Client是一个java客户端，用于在注册中心注册服务，简化与Eureka Server的交互
@EnableEurekaClient
@SpringBootApplication
@RibbonClient(value="sendMail")//表示使用ribbon客户端调用其他服务，value表示发布方在注册中心的服务名称
public class Main {
	@LoadBalanced//开始负载均衡
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
