import spark.Request
import spark.Response
import spark.Spark.*
import spark.servlet.SparkApplication
import controllers.*

val userController = UserController()

class Router : SparkApplication {
    override fun init() {
        port(8080)

        routes()
    }

    fun routes() {
        get("/") { req : Request , res: Response ->
            "Hello world!"
        }
        path("/users"){
            post("", userController.createUser)
            get("/:userId", userController.getById)
        }
    }
}