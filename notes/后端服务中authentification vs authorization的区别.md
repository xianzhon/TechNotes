# 基本概念，两者的区别
## 1. 后端开发中，**Authentication（认证）** 和 **Authorization（授权）**的区别

### 1.1. **Authentication（认证）**
- **是什么**：验证用户身份（确认“你是谁”）。
- **核心问题**：用户是否真的是其所声称的身份？
- **实现方式**：
  - 用户名/密码校验。
  - 多因素认证（短信/邮箱验证码、生物识别等）。
  - OAuth、JWT（Token 校验）。
  - Session-Cookie 机制。
- **示例**：
  - 用户登录时输入密码，系统验证密码是否正确。
  - 通过 JWT 的签名验证 Token 是否合法。

---

### 1.2. **Authorization（授权）**
- **是什么**：验证用户是否有权限执行操作（确认“你能做什么”）。
- **核心问题**：用户是否有权限访问特定资源或执行特定操作？
- **实现方式**：
  - 角色权限模型（RBAC，如 `admin`、`user` 角色）。
  - 权限列表（ACL，如 `read:file`、`delete:file`）。
  - OAuth 的 Scope（如 GitHub API 的 `repo:read` 权限）。
- **示例**：
  - 管理员可以删除用户，普通用户不行。
  - API 接口要求用户必须有 `write:article` 权限才能发帖。

---

### 1.3. 关键区别
| 维度       | Authentication（认证）    | Authorization（授权）  |
| -------- | --------------------- | ------------------ |
| **目的**   | 验证身份                  | 验证权限               |
| **时机**   | 通常在登录时完成              | 在认证之后，访问资源时触发      |
| **数据依赖** | 校验凭证（密码、Token 等）      | 校验权限（角色、Scope 等）   |
| **错误响应** | 401 Unauthorized（未认证） | 403 Forbidden（无权限） |

---

### 1.4. 实际场景中的协作
1. **用户登录**：输入密码 → 系统验证（Authentication）→ 返回 Token。
2. **访问 API**：携带 Token 请求 `/admin/dashboard` → 系统先验证 Token（Authentication），再检查用户角色是否为 `admin`（Authorization）。

---

### 1.5. 代码示例（Node.js）
```javascript
// 中间件示例：先认证，再授权
app.get('/admin/dashboard',
  authenticate, // 认证：检查 JWT
  authorize('admin'), // 授权：检查角色
  (req, res) => { res.send('Admin Dashboard'); }
);

// 认证中间件
function authenticate(req, res, next) {
  const token = req.headers.authorization;
  if (!validToken(token)) return res.status(401).send('Unauthorized');
  next();
}

// 授权中间件
function authorize(role) {
  return (req, res, next) => {
    if (req.user.role !== role) return res.status(403).send('Forbidden');
    next();
  };
}
```

---

### 1.6. 常见混淆点
- **401 vs 403**：
  - `401 Unauthorized`：认证失败（如 Token 无效）。
  - `403 Forbidden`：认证成功但权限不足（如普通用户访问管理员接口）。
- **OAuth 中的术语**：OAuth 的 `access_token` 实际用于认证（证明身份），而 `scope` 参数用于授权（限制权限）。

---

总结：**认证是敲门，授权是进门后的权限检查**。两者协同工作，构成后端安全的基石。

# 2. Spring 中如何实现

在 Spring 项目中，**Authentication（认证）** 和 **Authorization（授权）** 通常通过 **Spring Security** 模块实现。以下是具体实现方式和代码示例：

---

## 1. **Authentication（认证）**
Spring Security 提供了多种认证方式，常见的有 **表单登录**、**JWT** 和 **OAuth2**。

### 1.1. **方式 1：基于用户名/密码的认证（表单登录）**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated() // 所有请求都需要认证
            )
            .formLogin(form -> form
                .loginPage("/login") // 自定义登录页
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 内存存储用户（生产环境用数据库）
        UserDetails user = User.withUsername("user")
            .password("{noop}password") // {noop} 表示明文密码（仅测试用）
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }
}
```
- **关键点**：
  - `formLogin()` 启用表单登录。
  - `UserDetailsService` 提供用户信息（生产环境应连接数据库）。
  - `{noop}password` 表示不加密密码（仅测试用，生产环境需用 `BCryptPasswordEncoder`）。

---

### 1.2. **方式 2：JWT 认证**
适用于 REST API，使用 `JwtAuthenticationFilter` 拦截请求并验证 Token。
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用 CSRF（REST API 不需要）
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
```
**JwtAuthenticationFilter** 示例：
```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = extractToken(request);
        if (token != null && validateToken(token)) {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                getUsernameFromToken(token), null, getAuthoritiesFromToken(token));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
```
- **关键点**：
  - 从 `Authorization: Bearer <token>` 提取 JWT。
  - 验证 Token 并设置 `SecurityContextHolder`。

---

## 2. **Authorization（授权）**
授权通过 **角色（Role）** 或 **权限（Authority）** 控制，常用注解：
- `@PreAuthorize`（方法级权限控制）
- `@Secured`（基于角色的控制）
- `@RolesAllowed`（JSR-250 标准）

### 2.1. **方式 1：基于角色的授权**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 启用方法级安全控制
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN") // 仅 ADMIN 可访问
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(); // 启用表单登录

        return http.build();
    }
}
```
在 Controller 中使用 `@PreAuthorize`：
```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // 必须具有 ADMIN 角色
    public String adminOnly() {
        return "Admin Dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('READ_PROFILE')") // 必须具有 READ_PROFILE 权限
    public String userProfile() {
        return "User Profile";
    }
}
```

---

### 2.2. **方式 2：自定义权限校验**
如果需要复杂逻辑（如“仅允许操作自己的数据”），可使用 `@PostAuthorize` 或 `@PreAuthorize` + SpEL：
```java
@PreAuthorize("#userId == authentication.principal.id") // 只能操作自己的数据
@GetMapping("/users/{userId}")
public User getUser(@PathVariable Long userId) {
    return userService.findById(userId);
}
```

---

## 3. **认证与授权的协作流程**
1. **用户登录**：提交用户名/密码 → Spring Security 调用 `UserDetailsService` 验证 → 生成 `Authentication` 对象。
2. **访问受保护资源**：
   - 先通过 `AuthenticationManager` 认证（如 JWT Filter）。
   - 再通过 `AccessDecisionManager` 授权（检查 `@PreAuthorize`）。

---

## 4. **常见配置项**
| 功能                | 配置方式                                  |
|---------------------|------------------------------------------|
| **禁用 CSRF**       | `.csrf(csrf -> csrf.disable())`          |
| **允许匿名访问**    | `.requestMatchers("/public/**").permitAll()` |
| **内存用户存储**    | `InMemoryUserDetailsManager`              |
| **数据库用户存储**  | 自定义 `UserDetailsService`              |
| **密码加密**        | `BCryptPasswordEncoder`                  |

---

## 5. 总结
- **认证（Authentication）**：
  - 表单登录：`formLogin()` + `UserDetailsService`。
  - JWT：自定义 Filter + `SecurityContextHolder`。
- **授权（Authorization）**：
  - URL 级别：`.authorizeHttpRequests()`。
  - 方法级别：`@PreAuthorize` + SpEL。

Spring Security 的灵活性允许你根据需求组合不同的认证和授权策略。


# SpringBoot 项目中如何实现


在 Spring Boot 项目中，**Authentication（认证）** 和 **Authorization（授权）** 的实现主要通过 **Spring Security** 模块完成。以下是详细的分步实现方案，涵盖常见场景（表单登录、JWT、数据库存储用户、RBAC 权限控制等）。

---

## 1. 基础配置
### 1.1. 添加依赖
```xml
<!-- pom.xml -->
<dependencies>
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- Web 支持 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- 数据库（如使用 JPA） -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <!-- Lombok（可选） -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

## 2. 认证（Authentication）
### 2.1. 内存用户认证（快速开始）
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password("{noop}password") // {noop} 表示明文密码（生产环境需加密）
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password("{noop}admin123")
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
```

### 2.2. 数据库用户认证
#### 2.2.1. 实体类
```java
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password; // 存储加密后的密码
    private String role; // 如 "ROLE_USER", "ROLE_ADMIN"
}
```

#### 2.2.2. 实现 `UserDetailsService`
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().replace("ROLE_", ""))
            .build();
    }
}
```

#### 2.2.3. 密码加密
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 使用 BCrypt 加密
}
```

#### 2.2.4. 注册和登录逻辑
```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER"); // 默认角色
        userRepository.save(user);
        return ResponseEntity.ok("注册成功");
    }
}
```

---

## 3. 授权（Authorization）
### 3.1. URL 级别授权
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults());

        return http.build();
    }
}
```

### 3.2. 方法级别授权
启用方法级安全控制：
```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true) // 启用 @PreAuthorize
public class MethodSecurityConfig {
}
```

在 Controller 或 Service 中使用注解：
```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // 仅 ADMIN 可访问
    public String adminEndpoint() {
        return "Admin Data";
    }

    @GetMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.name") // 只能访问自己的数据
    public String userProfile(@PathVariable String username) {
        return "Profile: " + username;
    }
}
```

---

## 4. JWT 认证（REST API 场景）
### 4.1. 添加 JWT 依赖
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

### 4.2. JWT 工具类
```java
@Component
public class JwtUtils {
    private final String SECRET_KEY = "your-secret-key";
    private final long EXPIRATION_TIME = 864_000_000; // 10天

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

### 4.3. JWT 认证过滤器
```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String token = extractToken(request);
            if (token != null && jwtUtils.validateToken(token)) {
                String username = jwtUtils.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token 无效");
            return;
        }
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
```

### 4.4. 配置 Spring Security
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用 CSRF（API 场景不需要）
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态会话
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
```

### 4.5. 登录接口
```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(token);
    }
}
```

---

## 5. 关键点总结
| 功能                | 实现方式                                                                 |
|---------------------|--------------------------------------------------------------------------|
| **认证（Authentication）** | 表单登录、JWT、OAuth2、数据库用户存储 + `UserDetailsService`             |
| **授权（Authorization）** | URL 级别（`.authorizeHttpRequests()`）、方法级别（`@PreAuthorize`）      |
| **密码加密**         | `BCryptPasswordEncoder`                                                 |
| **JWT 集成**        | 自定义 Filter + 无状态会话（`SessionCreationPolicy.STATELESS`）          |
| **角色/权限控制**   | `hasRole()`、`hasAuthority()`、SpEL 表达式                               |

通过 Spring Security 的模块化设计，可以灵活组合认证和授权策略，适应从单体应用到微服务的各种场景。