package com.nolookblog.config;

import com.nolookblog.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description 推荐实现 WebMvcConfigurer接口
 */

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 拦截器配置
	 *
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor())
				// 表示对所有请求已/user开头的请求进行拦截
				.addPathPatterns("/user/**")
				// 表示排除/emp/toLogin","/emp/login","/js/**","/css/**","/images/** 拦截
				.excludePathPatterns("/emp/toLogin", "/emp/login", "/js/**", "/css/**", "/images/**");

		// TODO 当spring boot版本升级为2.x时，访问静态资源就会被HandlerInterceptor拦截,网上有很多处理办法都是如下写法
		// .excludePathPatterns("/index.html","/","/user/login","/static/**");
	}

	/**
	 * 视图跳转控制器(页面跳转)
	 *
	 * @param registry
	 * @tips:
	 * 		如果需要访问一个页面，必须要写Controller类，然后再写一个方法跳转到页面，感觉好麻烦，其实重写WebMvcConfigurer中的addViewControllers方法即可达到效果了
	 *		直接访问http://localhost:8080/toLogin就跳转到login.jsp页面了
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/toLogin").setViewName("login");
//		registry.addViewController("/index").setViewName("index");
	}

	/**
	 * 静态资源处理, 配置类优先于application.yaml
	 *
	 * @param registry
	 * @tips: 自定义静态资源映射目录, 访问: http://localhost:8080/my/index.html
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		// addResourceHandler添加映射路径
//		registry.addResourceHandler("/**")
//				// addResourceLocations指定路径
//				.addResourceLocations("classpath:/my/");
	}

	/**
	 * 默认静态资源处理器
	 *
	 * @param configurer
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// 没有找到合适的Handler来处理请求时, 就会交给DefaultServletHttpRequestHandler 来处理
		// 这里的静态资源是放置在web根目录下, 而非WEB-INF 下
		configurer.enable();
		configurer.enable("defaultServletName");
	}

	/**
	 * 配置视图解析器
	 *
	 * @param registry
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// 启用内容裁决视图解析器
		// 作用:
		// 		1、该方法会创建一个内容裁决解析器ContentNegotiatingViewResolver,
		// 		2、该解析器不进行具体视图的解析, 而是管理你注册的所有视图解析器, 所有的视图会先经过它进行解析,
		// 		3、然后由它来决定具体使用哪个解析器进行解析, 具体的映射规则是根据请求的 media types来决定的
		registry.enableContentNegotiation();

		// 作用:
		// 		1、该方法会注册一个内部资源视图解析器InternalResourceViewResolver 显然访问的所有jsp都是它进行解析的
		// 		2、该方法参数用来指定路径的前缀和文件后缀
		registry.jsp("/WEB-INF/jsp/", ".jsp");

		// 作用:
		// 		1、将视图名称解析成对应的bean
		//		2、举例:
		// 				假如返回的视图名称是example, 它会到spring容器中找有没有一个叫example的bean,
		// 				并且这个bean是View.class类型的？如果有, 返回这个bean。
		registry.beanName();

		// 作用: 用来注册各种各样的视图解析器的, 包括自己定义的
		// registry.viewResolver();
	}

	/**
	 * 配置内容裁决的一些选项
	 *
	 * @param configurer
	 * @tips: 内容裁决解析器, 专门用来配置内容裁决的一些参数的
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// 是否通过请求Url的扩展名来决定media type
		configurer.favorPathExtension(true)
				// 不检查Accept请求头
				.ignoreAcceptHeader(true)
				.parameterName("mediaType")
				// 设置默认的media type
				.defaultContentType(MediaType.TEXT_HTML)
				// 请求以.html结尾的会被当成MediaType.TEXT_HTML
				.mediaType("html", MediaType.TEXT_HTML)
				// 请求以.json结尾的会被当成MediaType.APPLICATION_JSON
				.mediaType("json", MediaType.APPLICATION_JSON);
	}
}
