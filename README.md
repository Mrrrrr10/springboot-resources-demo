#### 关于springboot访问静态文件的几种方法

##### 1、方法一：

描述：static目录下的文件直接访问

路径：resources/static/index.html

实例：

![1576900724239](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576900724239.png)

访问：<http://localhost:8080/index.html>

##### 2、方法二：

描述：controller层进行路径跳转

实例：

```
@Controller
public class IndexController {

	@GetMapping("/static")
	public String getStaticIndex(){
		return "index.html";
	}
}
```

访问：<http://localhost:8080/static>

##### 3、方法三：

描述：<font color=red>**templates下面存放的动态页面不能通过url中输入.html的方式直接访问，需要通过请求服务器，在转到动态画面。**</font>官方建议的是使用Thymeleaf来做动态界面，这里我使用的是freemaker，springboot也是集成了freemaker的。

实例：

![1576901818595](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576901818595.png)

依赖：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

配置：

```
spring:
  freemarker:
    suffix: .html
```

访问：

1、直接访问是访问不到的

![1576902024838](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576902024838.png)

2、需要通过请求服务器，在转到动态画面。 **<font color=red>注意这里的return不可以带.html，前面我们对freemaker进行的配置中给他添加了后缀名为.html。</font>**

```
@GetMapping("/templates")
public String getTemplatesIndex(){
	return "index";
}
```



![1576902110953](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576902110953.png)

##### 4、方法四：

描述：springboot默认的配置

简介：springboot资源目录下默认提供的静态资源访问路径

- /META-INF/resources
- resources
- static
- public

实例：

![1576902479926](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576902479926.png)

他们的根路径都是“/”

什么意思呢？就是说在访问这些目录下面文件的时候，他们的访问目录和resources下面文件的访问目录是一样的，就比如我们之前访问的目录都没有加static之类的，**<font color=red>无需加上文件夹名前缀</font>**。

那么就大有可为了，我们给不同的目录分配不同的功能，使代码看起来更加清晰明了

既然如此，万一这些目录下面的文件重名了会如何执行呢？他们之间是有先后顺序的，<font color=red>**这个顺序默认是/META-INF/resources>resources>static>public**</font>

![1576902721357](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576902721357.png)

##### 5、方法五：

描述：自定义静态资源访问路径

配置：

```
spring:
  mvc:
    static-path-pattern: /**
    resources:
      static-locations: classpath:/mystatic/, classpath:/static/js/, classpath:/META-INF/resources/, classpath:/resources/, classpath:/static/, classpath:/public/
```

- static-path-pattern: /**：告诉springboot应该以什么样的方式去寻找资源。默认配置为 /\*。换句话说，只有静态资源满足什么样的匹配条件，Spring Boot才会处理静态资源请求，我们也可配置成 /user/**,这样我们必须输入/user/index.html才能访问到这些目录下的资源。
- resources：这个配置项是告诉springboot去哪找资源，根据前后关系确定优先级，也就是说如果/META-INF/resources/目录和/resources都有一个index.html，那么根据默认的优先级，会去访问/mystatic/下的资源。

实例：

![1576902903337](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576902903337.png)



访问：

![1576903569618](C:\Users\10475\AppData\Roaming\Typora\typora-user-images\1576903569618.png)

##### 6、方法六：

描述：使用配置类的方式进行配置

实例：

```
package com.nolookblog.config;

import com.nolookblog.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description 推荐实现 WebMvcConfigurer接口
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 静态资源处理
	 *
	 * @param registry
	 * @tips: 自定义静态资源映射目录
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/mystatic/**").addResourceLocations("classpath:/my/");
	}
}

```





















