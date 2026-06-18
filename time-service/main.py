"""
Time Service - 提供查询当前时间的 REST 接口，支持 HTTP Basic 认证。

专为 MCP Context Forge 的 Tool 页面配置设计：
- HTTP Basic 认证方式
- GET /current-time 返回当前时间信息

MCP Context Forge Tool 页面配置参考：
  Tool URL:   http://<host>:8000/current-time
  Auth Type:  Basic
  Username:   admin
  Password:   admin123
"""

import secrets
from datetime import datetime, timezone

from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.security import HTTPBasic, HTTPBasicCredentials
from pydantic import BaseModel

# ============================================================
# 配置 - 可通过环境变量覆盖
# ============================================================
import os

BASIC_USERNAME = os.getenv("BASIC_USERNAME", "admin")
BASIC_PASSWORD = os.getenv("BASIC_PASSWORD", "admin123")

# ============================================================
# 应用 & 认证方案
# ============================================================
app = FastAPI(
    title="Time Service",
    description="查询当前时间的 REST 接口，HTTP Basic 认证",
    version="1.0.0",
)

security = HTTPBasic()


# ============================================================
# 数据模型
# ============================================================

class TimeResponse(BaseModel):
    """当前时间响应"""
    success: bool = True
    timestamp: int
    datetime: str
    timezone: str
    iso8601: str


# ============================================================
# 认证逻辑
# ============================================================

def verify_basic_auth(credentials: HTTPBasicCredentials = Depends(security)) -> str:
    """验证 HTTP Basic 认证，校验通过则返回用户名"""
    current_username_bytes = credentials.username.encode("utf8")
    correct_username_bytes = BASIC_USERNAME.encode("utf8")
    is_correct_username = secrets.compare_digest(
        current_username_bytes, correct_username_bytes
    )
    current_password_bytes = credentials.password.encode("utf8")
    correct_password_bytes = BASIC_PASSWORD.encode("utf8")
    is_correct_password = secrets.compare_digest(
        current_password_bytes, correct_password_bytes
    )

    if not (is_correct_username and is_correct_password):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="无效的用户名或密码",
            headers={"WWW-Authenticate": "Basic"},
        )
    return credentials.username


# ============================================================
# 业务接口
# ============================================================

@app.get("/current-time", response_model=TimeResponse)
async def get_current_time(username: str = Depends(verify_basic_auth)):
    """
    获取当前时间。

    需要 HTTP Basic 认证，在请求头中携带：
        Authorization: Basic <base64(username:password)>

    MCP Context Forge 配置说明：
        - 此接口返回标准 JSON 格式，可直接被 MCP Context Forge 解析为 Tool 结果
        - 响应中的字段均为文本/数字类型，无需额外处理
    """
    now = datetime.now(timezone.utc)
    return TimeResponse(
        timestamp=int(now.timestamp()),
        datetime=now.strftime("%Y-%m-%d %H:%M:%S"),
        timezone="UTC",
        iso8601=now.isoformat(),
    )


# ============================================================
# 健康检查
# ============================================================

@app.get("/health")
async def health():
    """健康检查接口（无需认证）"""
    return {"status": "ok", "service": "time-service", "version": "1.0.0"}


# ============================================================
# 入口
# ============================================================

if __name__ == "__main__":
    import uvicorn
    port = int(os.getenv("PORT", "8000"))
    uvicorn.run(app, host="0.0.0.0", port=port)