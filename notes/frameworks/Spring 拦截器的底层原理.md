
## 5w1h 分析 spring 拦截器

以下是使用5W1H分析法对Spring拦截器（Interceptor）的全面解析：

---

### **1. What（什么是Spring拦截器）**
- **定义**：
  Spring拦截器是基于Java反射机制和动态代理的组件，用于在请求处理的不同阶段（如Controller方法执行前后、视图渲染后）插入自定义逻辑。 【底层的原理：java 反射和动态代理？笔记：[java 动态代理](java%20动态代理.md)】
- **核心接口**：
  `HandlerInterceptor`（主要接口）或`AsyncHandlerInterceptor`（异步扩展）。
- **关键方法**：  【HandlerInterceptor 是 Spring WebMVC 包里提供的接口，总共只有下面的 3 个方法】
  - `preHandle()`：请求到达Controller前执行。
  - `postHandle()`：Controller方法执行后、视图渲染前执行。
  - `afterCompletion()`：请求完成后（视图渲染完毕）执行。

---

### **2. Why（为什么使用拦截器）**
- **核心目的**：
  - **解耦横切关注点**：将日志、权限校验、性能监控等与业务逻辑分离。
  - **精细化控制请求流程**：例如拦截未登录用户、参数预处理。【很好！！记住它】
- **优势**：
  - 比Servlet Filter更轻量（仅拦截Spring MVC范围内的请求）。
  - 可获取Spring上下文信息（如Controller对象、方法参数）。

---

### **3. When（何时触发拦截器）**
- **执行时机**：
  - `preHandle` → DispatcherServlet调用Controller前。
  - `postHandle` → Controller执行后，但未返回ModelAndView给视图解析器。
  - `afterCompletion` → 视图渲染完成后（适合资源清理）。
- **典型场景**：
  - 权限验证（`preHandle`中拦截非法请求）。【good！】
  - 接口耗时统计（`preHandle`记录开始时间，`afterCompletion`计算耗时）。【很好！】

---

### **4. Where（拦截器作用范围）**
- **应用层级**：
  Spring MVC框架内，基于DispatcherServlet的请求处理流程。
- **配置方式**：
  ```java
  @Configuration
  public class WebConfig implements WebMvcConfigurer {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(new AuthInterceptor())
                  .addPathPatterns("/api/**")  // 拦截路径
                  .excludePathPatterns("/api/public/**"); // 排除路径
      }
  }
  ```

---

### **5. Who（谁使用拦截器）**
- **使用者角色**：
  - **开发者**：实现业务无关的通用逻辑（如日志、安全）。
  - **架构师**：设计统一的请求处理规范。
- **适用对象**：
  需要精细化控制HTTP请求的Spring MVC/Spring Boot项目。

---

### **6. How（如何实现拦截器）**
- **实现步骤**：
  1. 创建类实现`HandlerInterceptor`接口。
  2. 重写目标方法（如`preHandle`返回`false`则中断请求）。
  3. 注册拦截器并配置拦截规则。
- **示例代码**：
  ```java
  public class LogInterceptor implements HandlerInterceptor {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
          System.out.println("Request URL: " + request.getRequestURL());
          return true; // 继续流程
      }
  }
  ```

---

### **对比与注意事项**
- **vs 过滤器（Filter）**：
  - Filter属于Servlet规范，拦截所有请求；拦截器仅针对Spring MVC的Handler。
  - 拦截器可访问HandlerMethod上下文，Filter只能操作Servlet API。
- **执行顺序**：
  `Filter → DispatcherServlet → Interceptor → Controller`。
- **异步请求**：
  需实现`AsyncHandlerInterceptor`，`afterConcurrentHandlingStarted`方法处理异步场景。

通过5W1H分析，可系统化理解Spring拦截器的定位、价值和实践方式。