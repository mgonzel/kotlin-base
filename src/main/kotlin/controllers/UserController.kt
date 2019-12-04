package controllers

import spark.Request
import spark.Response

class UserController {
    private val users = mutableMapOf<String,String>()

    val getById = { req : Request , res : Response ->
        val user = users.get(req.params("userId"))
        if (user == null){
            res.status(404)
            """{"error":"user not found"}"""
        } else {
            user
        }
    }
    val createUser = { req: Request, res: Response ->
        val userId = getRandomId()
        users.put(userId, req.body().toString())

        res.status(201)
        """{"status":"created", "id":"$userId"}"""
    }

    fun getRandomId(): String {
        return "${System.currentTimeMillis()}"
    }
}
