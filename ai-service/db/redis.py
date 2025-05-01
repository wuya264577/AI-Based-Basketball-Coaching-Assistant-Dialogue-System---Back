import redis

def get_redis_connection():
    REDIS_HOST = 'localhost'
    REDIS_PORT = 6379
    REDIS_DB = 0
    return redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=REDIS_DB)

# 测试连接
# try:
#     r = get_redis_connection()
#     r.ping()
#     print("Redis 连接成功!")
# except redis.ConnectionError as e:
#     print("无法连接到 Redis", e)