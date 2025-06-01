
# I. 详细介绍 JWT 权限验证的整个过程

JSON Web Token（JWT）是一种开放标准（RFC 7519），用于在各方之间安全地传输信息作为 JSON 对象。它特别适用于**分布式系统的权限验证**（Authentication & Authorization）。以下是 JWT 权限验证的完整流程，包括生成、传输、验证和实际应用场景。

---

## **1. JWT 基本结构**
一个 JWT 由三部分组成，以 `.` 分隔：
```
Header.Payload.Signature
```
### **（1）Header**
包含令牌类型（`typ: "JWT"`）和签名算法（如 `alg: "HS256"` 或 `RS256`）：
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
→ Base64Url 编码后形成第一部分。

### **（2）Payload（Claims）**
存放实际数据（如用户ID、角色、过期时间等），分为三类：
- **Registered Claims**（预定义字段，可选）：
  ```json
  {
    "sub": "1234567890",  // Subject (用户ID)
    "name": "John Doe",
    "iat": 1516239022,    // Issued At (签发时间)
    "exp": 1516242622     // Expiration Time (过期时间)
  }
  ```
- **Public Claims**（自定义公共字段，如 `role: "admin"`）。
- **Private Claims**（私有字段，用于特定业务）。

→ Base64Url 编码后形成第二部分。

### **（3）Signature**
使用 Header 指定的算法（如 `HS256`）对 `Header + Payload` 进行签名，防止篡改：
```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret_key
)
```
→ 最终 JWT 示例：
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

在线 decode 工具： [JSON Web Tokens - jwt.io](https://jwt.io/)

## **2. JWT 权限验证流程**
### **（1）用户登录，获取 JWT**
1. **客户端**发送登录请求（如 `POST /login`）：
   ```json
   {
     "username": "alice",
     "password": "p@ssw0rd"
   }
   ```
2. **服务端**验证凭据，生成 JWT：
   - 检查用户数据库，确认身份（Authentication）。 【DB 保存密码的加密字符串】
   - 生成 JWT，包含用户角色（如 `"role": "admin"`）和过期时间（如 `exp: 当前时间 + 1小时`）。
   - 使用密钥（如 `HS256`）签名，返回给客户端：
```json
 {
   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### **（2）客户端携带 JWT 访问受保护资源**
- 在后续请求的 `Authorization` Header 中附加 JWT：

```http
  GET /api/protected HTTP/1.1
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### **（3）服务端验证 JWT**
1. **解析 Header**：
   - 检查 `alg` 是否受信任（防止算法替换攻击）。
2. **验证签名**：
   - 使用相同的密钥（如 `HS256`）重新计算签名，对比是否一致。
3. **检查 Claims**：
   - `exp`：令牌是否过期。
   - `iat` / `nbf`：令牌是否已生效。
   - `sub` / `role`：用户是否有权限访问资源（Authorization）。
4. **返回结果**：
   - 验证通过 → 返回请求的资源（如 `200 OK`）。
   - 验证失败 → 返回 `401 Unauthorized` 或 `403 Forbidden`。【什么时候返回 401？验证失败，说明 client 传过来的 jwt 有可能是伪造的】

---

## **3. 关键安全机制**
### **（1）签名算法选择**
- **HS256（对称加密）**：
  使用单一密钥（`secret_key`），适合单体应用。
  **风险**：密钥泄露会导致所有令牌可伪造。
- **RS256（非对称加密）**：
  使用私钥签名，公钥验证，适合微服务（公钥可公开）。
  ```openssl
  # 生成 RSA 密钥对
  openssl genrsa -out private.pem 2048
  openssl rsa -in private.pem -pubout -out public.pem
  ```

### **（2）防止令牌盗用**
- **HTTPS 必选**：避免 JWT 在传输中被截获。
- **短期有效期**：设置较短的 `exp`（如 15分钟），结合 Refresh Token 机制续期。
- **令牌吊销**：
  - 黑名单（需后端存储失效令牌，牺牲无状态性）。
  - 使用无状态方式（如修改密钥，使旧令牌全部失效）。

### **（3）存储方式**
- **浏览器**：
  - `HttpOnly` Cookie（防XSS）。
  - 避免 LocalStorage（易受XSS攻击）。
- **移动端**：安全存储（如 Android Keystore / iOS Keychain）。

---

## **4. 实际应用场景**
### **（1）微服务架构**
```plaintext
客户端 → [API Gateway] → [微服务A] → [微服务B]
```
- **API Gateway** 统一验证 JWT，并转发用户身份（如 `X-User-ID`）给下游服务。
- 微服务无需重复验证签名，只需检查权限（如 `role: "admin"`）。

### **（2）单点登录（SSO）**
- 用户登录后，SSO 系统签发 JWT，多个子系统共享同一公钥验证令牌。

### **（3）无状态 API**
- 服务端无需存储会话，适合横向扩展（如 Kubernetes 集群）。

---

## **5. JWT 的局限性** 【重要！】
- **无法实时吊销**：需结合黑名单或短有效期。
- **Payload 大小限制**：不宜存储过多数据（影响性能）。
- **密钥管理复杂**：RS256 需安全分发公钥/私钥。

---

## **总结**
JWT 权限验证的核心流程：
1. **签发**：服务端用密钥生成签名令牌。
2. **传输**：客户端通过 Header 或 Cookie 携带令牌。
3. **验证**：服务端校验签名、过期时间和权限。
4. **授权**：根据 Payload 中的 Claims 控制资源访问。

**适用场景**：
✅ 分布式系统、无状态API、SSO。
**不适用场景**：
❌ 需要实时吊销令牌、敏感操作（如支付）需二次验证。

**工具推荐**：
- 生成/验证库：`jsonwebtoken`（Node.js）、`jjwt`（Java）、`PyJWT`（Python）。
- API 网关：Kong（JWT 插件）、Apigee、AWS Cognito。