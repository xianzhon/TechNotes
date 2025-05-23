## Spring 项目中全局异常处理，怎么实现的

在Spring中，**全局异常处理**允许我们集中管理所有Controller层抛出的异常，避免在每个方法中重复编写`try-catch`块。以下是详细的实现方式和最佳实践：

---

### **1. 核心注解**
Spring提供了以下注解实现全局异常处理：
- **`@ControllerAdvice`**：定义全局异常处理类（可限定包或Controller范围）。
- **`@ExceptionHandler`**：标注具体的异常处理方法。

---

### **2. 基础实现步骤**

#### **(1) 创建全局异常处理类**
```java
@RestControllerAdvice // 等同于 @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

    // 处理特定异常
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            "NOT_FOUND",
            ex.getMessage(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 处理通用异常（如参数校验失败）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse(
            "VALIDATION_FAILED",
            errors.toString(),
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 兜底处理其他所有异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "Server error occurred",
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

#### **(2) 定义统一的错误响应体**
```java
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String code;     // 错误码（如 "NOT_FOUND"）
    private String message;  // 错误详情
    private long timestamp;  // 时间戳
}
```

---

### **3. 高级用法**

#### **(1) 限制作用范围**
- **指定包路径**：
  ```java
  @ControllerAdvice("com.example.controllers")
  ```
- **指定注解**：
  ```java
  @ControllerAdvice(annotations = RestController.class)
  ```
- **指定Controller类**：
  ```java
  @ControllerAdvice(assignableTypes = {UserController.class, OrderController.class})
  ```

#### **(2) 处理自定义异常**
```java
// 自定义业务异常
public class BusinessException extends RuntimeException {
    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

// 全局处理
@ExceptionHandler(BusinessException.class)
public ResponseEntity<ErrorResponse> handleBusinessException(
    BusinessException ex) {
    ErrorResponse error = new ErrorResponse(
        ex.getErrorCode(),
        ex.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
```

#### **(3) 参数校验异常精细化处理**
结合`@Valid`和全局异常处理：
```java
@PostMapping("/users")
public User createUser(@Valid @RequestBody UserDTO userDTO) {
    // ...
}

// 处理校验失败异常
@ExceptionHandler(BindException.class)
public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
    Map<String, String> fieldErrors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getDefaultMessage
        ));

    ErrorResponse error = new ErrorResponse(
        "INVALID_INPUT",
        fieldErrors.toString(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
```

---

### **4. 执行优先级规则**
1. **就近原则**：
   Controller内部的`@ExceptionHandler`优先于全局处理。
2. **异常类型匹配**：
   更具体的异常类优先（如`NullPointerException`处理优先于`Exception`）。

---

### **5. 完整工作流程**
1. **请求进入Controller**：
   调用方法时抛出异常（如`throw new ResourceNotFoundException("User not found")`）。
2. **异常捕获**：
   Spring遍历所有`@ExceptionHandler`方法，找到匹配的异常处理器。
3. **生成响应**：
   执行对应的异常处理方法，构造错误响应（JSON/XML等）。
4. **返回客户端**：
   跳过原始Controller逻辑，直接返回错误响应。

---

### **6. 最佳实践**
- **分层处理异常**：
  - 业务异常（如`BusinessException`） → 返回明确错误码。
  - 系统异常（如`NullPointerException`） → 记录日志，返回模糊提示（避免暴露敏感信息）。
- **日志记录**：
  在异常处理器中添加日志（如SLF4J）：
  ```java
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(
      Exception ex, WebRequest request) {
      log.error("Request {} failed: {}", request.getDescription(false), ex.getMessage());
      // ...
  }
  ```
- **Swagger集成**：
  使用`@ApiResponse`标注可能的错误码（增强API文档）：
  ```java
  @ApiResponse(responseCode = "404", description = "Resource not found")
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(...) { ... }
  ```

---

### **7. 对比其他方案**
| 方案                | 优点                          | 缺点                          |
|---------------------|-----------------------------|-----------------------------|
| **全局`@ControllerAdvice`** | 集中管理，代码简洁          | 无法处理Filter层的异常       |
| **`@ExceptionHandler`局部处理** | 灵活性高（针对特定Controller） | 重复代码多                  |
| **`HandlerExceptionResolver`** | 底层控制（自定义响应逻辑）  | 实现复杂                    |
| **AOP拦截异常**      | 非侵入式                    | 无法精确控制HTTP状态码       |

---

通过全局异常处理，可以显著提升代码可维护性，并为客户端提供一致的错误响应格式。