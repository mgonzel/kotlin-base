package services

import redis.clients.jedis.*
import java.text.SimpleDateFormat

class RedisService (
        var jedisPool :JedisPool = JedisPool("redis.local")
){

    private val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val logger = mu.KotlinLogging.logger {}

    private val expirationKeyTime = 84600;

    fun setNewUser(userId: String){
        setValue("user:$userId:logger:active", "true")
    }



    fun setValue(key: String, value: String) {
        jedisPool.resource.use { jedis ->
            logger.debug("Setting value in redis ---> $key = $value")
            jedis.setex(key, expirationKeyTime ,value)
        }
    }

    fun isUserActive(userId: String): Boolean {
        return getValue("user:$userId:logger:active") != null
    }

    fun getStatus() : String {
        return try {
            jedisPool.run{
                "{ jedis_connections : { active:$numActive , idle:$numIdle , waiters:$numWaiters }}"
            }
        } catch (e:Exception){
            "Error accessing Redis: redis.local"
        }
    }

    fun storeDummy(dummy : Any) {
        jedisPool.resource.use { jedis ->
            jedis.set("dummy", dummy.toString())
        }
    }

    fun getDummy() : String {
        jedisPool.resource.use {jedis->
            jedisPool.run{ logger.info (getStatus()) }
            return jedis.get("dummy")
        }
    }

    private fun getValue(key : String) : String? {
        jedisPool.resource.use { jedis ->
            return jedis.get(key)
        }
    }


}