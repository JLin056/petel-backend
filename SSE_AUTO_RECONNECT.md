# SSE 自动重连机制技术文档

## 文档目的
此文档说明后端已实现的 SSE（Server-Sent Events）自动重连机制，供前端开发人员检查是否需要添加相应的实现。

---

## 后端已完成的功能

### 1. SSE 连线配置
**位置:** `NotificationHub.java:31-70`

- ✅ 连线超时：30 分钟
- ✅ 自动重连间隔：**3 秒**
- ✅ 自动清理机制：超时/错误/正常断线时自动清理资源

```java
// 后端在建立连线时会发送
emitter.send(SseEmitter.event()
    .comment("SSE connection established")
    .reconnectTime(3000L));  // 告诉浏览器：断线后每 3 秒重连
```

### 2. 事件补发 API
**API:** `POST /notifications/resend-missed`
**功能:** 重连后补发错过的通知

**Request:**
```json
{
  "MWHEADER": {
    "消息头信息..."
  },
  "TRANRQ": {
    "last_event_time": "2024-01-01T12:00:00+08:00"
  }
}
```

**Response:**
```json
{
  "MWHEADER": {
    "returnCode": "0000",
    "returnDesc": "成功"
  },
  "TRANRS": {
    "resent_count": 5
  }
}
```

---

## 前端需要实现的部分

### 必须实现

#### 1. 记录最后事件时间戳
每次收到通知时，保存 `createdAt` 或使用当前时间：
```typescript
this.lastEventTime = notification.createdAt || new Date().toISOString();
```

#### 2. 重连后调用补发 API
监听 SSE 的 `open` 事件，在重连成功后调用 `NOTIFY006`：
```typescript
eventSource.addEventListener('open', () => {
  this.resendMissedEvents(); // 调用补发 API
});
```

### 自动完成（无需编写代码）

- ✅ 检测断线
- ✅ 等待 3 秒
- ✅ 自动重连

这些由浏览器的 `EventSource` API 原生支持。

---

## 完整前端实现示例

### TypeScript/Angular 版本

```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private eventSource: EventSource | null = null;
  private lastEventTime: string = new Date().toISOString();
  private baseUrl = 'http://localhost:8080';
  private reconnectCount = 0;

  constructor(private http: HttpClient) {}

  /**
   * 建立 SSE 连线
   * @param token JWT access token
   */
  connect(token: string): void {
    if (this.eventSource) {
      console.warn('[SSE] 已存在连线，先断开旧连线');
      this.disconnect();
    }

    const url = `${this.baseUrl}/notifications/subscribe?token=${token}`;
    this.eventSource = new EventSource(url);

    // 监听通知事件
    this.eventSource.addEventListener('notification', (event: MessageEvent) => {
      try {
        const notification = JSON.parse(event.data);
        console.log('[SSE] 收到通知:', notification);

        // 【重要】更新最后事件时间
        this.lastEventTime = notification.createdAt || new Date().toISOString();

        // 显示通知给用户
        this.handleNotification(notification);
      } catch (error) {
        console.error('[SSE] 解析通知失败:', error);
      }
    });

    // 监听连线成功（包括重连成功）
    this.eventSource.addEventListener('open', () => {
      if (this.reconnectCount === 0) {
        console.log('[SSE] 首次连线成功');
      } else {
        console.log(`[SSE] 重连成功（第 ${this.reconnectCount} 次）`);
        // 【重要】重连后补发错过的通知
        this.resendMissedEvents();
      }
      this.reconnectCount++;
    });

    // 监听错误（会自动重连）
    this.eventSource.addEventListener('error', (event) => {
      if (this.eventSource?.readyState === EventSource.CLOSED) {
        console.error('[SSE] 连线已关闭');
      } else {
        console.warn('[SSE] 连线断开，3 秒后自动重连...');
      }
    });
  }

  /**
   * 补发错过的事件
   */
  private resendMissedEvents(): void {
    const token = this.getToken();
    if (!token) {
      console.error('[SSE] 无法补发：找不到 token');
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = {
      MWHEADER: {
        // 根据你的项目填充消息头
      },
      TRANRQ: {
        last_event_time: this.lastEventTime
      }
    };

    this.http.post<any>(`${this.baseUrl}/notifications/resend-missed`, body, { headers })
      .subscribe({
        next: (response) => {
          const count = response.TRANRS?.resent_count || 0;
          console.log(`[SSE] 补发了 ${count} 条错过的通知`);
        },
        error: (error) => {
          console.error('[SSE] 补发通知失败:', error);
        }
      });
  }

  /**
   * 处理收到的通知
   */
  private handleNotification(notification: any): void {
    // 根据通知类型显示不同的提示
    switch (notification.type) {
      case 'ORDER':
        console.log('订单通知:', notification.title, notification.message);
        break;
      case 'PAYMENT':
        console.log('支付通知:', notification.title, notification.message);
        break;
      case 'SYSTEM':
        console.log('系统通知:', notification.title, notification.message);
        break;
      default:
        console.log('通知:', notification.title, notification.message);
    }

    // TODO: 显示 toast/snackbar/弹窗等
  }

  /**
   * 断开连线
   */
  disconnect(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      this.reconnectCount = 0;
      console.log('[SSE] 连线已断开');
    }
  }

  /**
   * 获取 JWT token
   */
  private getToken(): string | null {
    // 从 localStorage、sessionStorage 或其他地方获取
    return localStorage.getItem('access_token');
  }
}
```

### JavaScript/Vanilla 版本

```javascript
class NotificationService {
  constructor(baseUrl = 'http://localhost:8080') {
    this.baseUrl = baseUrl;
    this.eventSource = null;
    this.lastEventTime = new Date().toISOString();
    this.reconnectCount = 0;
  }

  connect(token) {
    if (this.eventSource) {
      this.disconnect();
    }

    const url = `${this.baseUrl}/notifications/subscribe?token=${token}`;
    this.eventSource = new EventSource(url);

    this.eventSource.addEventListener('notification', (event) => {
      const notification = JSON.parse(event.data);
      console.log('[SSE] 收到通知:', notification);

      // 更新最后事件时间
      this.lastEventTime = notification.createdAt || new Date().toISOString();

      this.handleNotification(notification);
    });

    this.eventSource.addEventListener('open', () => {
      if (this.reconnectCount === 0) {
        console.log('[SSE] 首次连线成功');
      } else {
        console.log(`[SSE] 重连成功（第 ${this.reconnectCount} 次）`);
        this.resendMissedEvents();
      }
      this.reconnectCount++;
    });

    this.eventSource.addEventListener('error', () => {
      if (this.eventSource?.readyState === EventSource.CLOSED) {
        console.error('[SSE] 连线已关闭');
      } else {
        console.warn('[SSE] 连线断开，3 秒后自动重连...');
      }
    });
  }

  async resendMissedEvents() {
    const token = this.getToken();
    if (!token) return;

    try {
      const response = await fetch(`${this.baseUrl}/notifications/resend-missed`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          MWHEADER: {},
          TRANRQ: {
            last_event_time: this.lastEventTime
          }
        })
      });

      const result = await response.json();
      const count = result.TRANRS?.resent_count || 0;
      console.log(`[SSE] 补发了 ${count} 条错过的通知`);
    } catch (error) {
      console.error('[SSE] 补发通知失败:', error);
    }
  }

  handleNotification(notification) {
    console.log('通知:', notification.title, notification.message);
    // TODO: 显示通知
  }

  disconnect() {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      this.reconnectCount = 0;
    }
  }

  getToken() {
    return localStorage.getItem('access_token');
  }
}

// 使用示例
const notificationService = new NotificationService();
notificationService.connect('your-jwt-token');
```

---

## 工作流程图

```
┌─────────────────────────────────────────────────────────────┐
│ 前端                                                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. new EventSource("/notifications/subscribe?token=xxx")   │
│     ↓                                                        │
│  2. 收到 "retry: 3000" 配置                                  │
│     ↓                                                        │
│  3. 监听 'notification' 事件                                 │
│     ↓                                                        │
│  4. 每次收到通知 → 更新 lastEventTime                         │
│     ↓                                                        │
│  [正常运行中...]                                              │
│     ↓                                                        │
│  ⚠️ 断线！（网络问题/30分钟超时）                             │
│     ↓                                                        │
│  5. 浏览器自动等待 3 秒                                       │
│     ↓                                                        │
│  6. 浏览器自动重连（无需代码）                                │
│     ↓                                                        │
│  7. 'open' 事件触发                                          │
│     ↓                                                        │
│  8. 【需要实现】调用 POST /notifications/resend-missed        │
│     - 参数: last_event_time                                  │
│     ↓                                                        │
│  9. 后端推送错过的通知到 SSE                                  │
│     ↓                                                        │
│  10. 继续正常运行 ✓                                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 后端                                                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  • GET /notifications/subscribe?token=xxx                   │
│    - 建立 SSE 连线                                           │
│    - 发送 retry: 3000                                        │
│    - 30 分钟后自动断线                                       │
│                                                              │
│  • POST /notifications/resend-missed                         │
│    - 查询 last_event_time 之后的事件                         │
│    - 推送到 SSE 连线                                         │
│    - 返回补发数量                                            │
│                                                              │
│  • NotificationHub.sendTo(...)                              │
│    - 发送即时通知到所有活跃连线                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 断线重连场景

### 场景 1: 网络短暂断线（推荐实现补发）
```
用户在地铁隧道 → WiFi 断线 → 3 秒后出隧道 → 自动重连 → 补发错过的通知
```

### 场景 2: 30 分钟超时（推荐实现补发）
```
用户打开网页 → 去开会 → 30 分钟后超时断线 → 用户回来 → 自动重连 → 补发通知
```

### 场景 3: 服务器重启（推荐实现补发）
```
后端部署新版本 → 所有连线断开 → 每 3 秒重试 → 服务器恢复 → 自动重连 → 补发通知
```

### 场景 4: 移动设备休眠（推荐实现补发）
```
手机锁屏 → 系统关闭连线 → 解锁 → 自动重连 → 补发通知
```

---

## API 规格

### 1. 建立 SSE 连线

**Endpoint:** `GET /notifications/subscribe`

**Query Parameters:**
- `token` (string, required): JWT access token

**Response:** Server-Sent Events stream

**Event Format:**
```
event: notification
id: A00001-1234567890-5678
data: {"id":"N123456789","title":"订单取消","message":"您的订单已取消","type":"ORDER","status":"UNREAD","createdAt":"2024-01-01T12:00:00+08:00","orderId":"O00001"}

retry: 3000
```

**Headers:**
```
Content-Type: text/event-stream
Cache-Control: no-cache
Connection: keep-alive
```

### 2. 补发错过的事件

**Endpoint:** `POST /notifications/resend-missed`

**Headers:**
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "MWHEADER": {
    // 根据项目规范填充
  },
  "TRANRQ": {
    "last_event_time": "2024-01-01T12:00:00+08:00"
  }
}
```

**Response:**
```json
{
  "MWHEADER": {
    "returnCode": "0000",
    "returnDesc": "成功"
  },
  "TRANRS": {
    "resent_count": 5
  }
}
```

**Error Codes:**
- `E001`: 时间格式错误
- `E401`: 未授权

---

## 检查清单

请前端开发人员检查以下项目：

### 必须实现
- [ ] 建立 SSE 连线（使用 EventSource API）
- [ ] 监听 `notification` 事件
- [ ] **记录 `lastEventTime`**（每次收到通知时更新）
- [ ] **监听 `open` 事件**（重连成功时触发）
- [ ] **调用补发 API**（重连后调用 `/notifications/resend-missed`）

### 可选实现
- [ ] 显示重连状态给用户（"连线中断，正在重连..."）
- [ ] 记录重连次数（用于调试）
- [ ] 错误处理和日志记录
- [ ] 断线时的 UI 提示

### 无需实现（浏览器自动）
- [x] 检测断线
- [x] 等待 3 秒
- [x] 自动重连

---

## 常见问题

### Q1: 如果不实现补发机制会怎样？
**A:** 断线期间的通知会丢失。重连后只能收到新的通知。

### Q2: `lastEventTime` 必须精确吗？
**A:** 是的。建议使用通知中的 `createdAt` 字段。如果使用本地时间，可能因为时区或时钟偏差导致漏发或重复。

### Q3: 需要手动处理重连逻辑吗？
**A:** 不需要。浏览器的 `EventSource` API 会自动重连，你只需要监听 `open` 事件并调用补发 API。

### Q4: 3 秒的重连间隔可以修改吗？
**A:** 可以，但需要后端修改 `NotificationHub.java` 中的 `reconnectTime` 值。

### Q5: 如何测试重连机制？
**A:**
1. 打开浏览器开发者工具 → Network
2. 找到 SSE 连线（subscribe）
3. 暂停网络或关闭后端服务器
4. 恢复网络/启动服务器
5. 观察是否自动重连并补发通知

---

## 联系方式

如有疑问，请联系后端开发人员。

**文档版本:** 1.0
**最后更新:** 2025-11-03
**相关代码:**
- `NotificationHub.java`
- `NotificationController.java`
- `NOTIFY005SvcImpl.java`
- `NOTIFY006SvcImpl.java`
