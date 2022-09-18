package com.niranjan.androidtutorials.kotlin

class KotlinFunctionConcepts {

    fun returningUnitFunc() : Unit{
        println("Hi there! I return Unit.")
    }

    // every FUNCTION bloc performs a specific tasks
    fun functionBloc(
        // arguments passed
        defaultValue : String = "Default", // Default arguments
        requiredParams : String // Required parameters
    ) : Boolean // Return Type
    {
        println("$defaultValue $requiredParams")
        return true
    }

    // LAMBDA is an expression that makes a function that has no name
    val lambdaFunctionValue: (Int) -> Int =
        { level: Int -> // Parameters and Type
            level / 2  // executed code
        }

    // HIGHER ORDER FUNCTIONS
    // function that takes another function as arguments or return a function
    fun higherOrderFunction(
        msg: String,
        transform: (String) -> String
    ): String {
        return transform(msg)
    }

    // Usecase
    val myTransform: (String) -> String = { input ->
        input.reversed()
    }

    fun namedFunctionNotLambda(input: String): String = input.drop(2)

    fun usingHighOrderFunction(){
        val transformMessage = higherOrderFunction(
            msg = "This is my message i want to transform.",
            myTransform
        )
        val tranformUsingReference = higherOrderFunction(
            msg = "This message i want to drop",
            ::namedFunctionNotLambda
        )
        println(transformMessage)
        println(tranformUsingReference)
    }

    // EXTENSION FUNCTION
    fun String.dropLastWithReverse(){
        this.reversed().dropLast(2)
    }
    fun Int.isEven(): Boolean{
        return  this % 2 == 0
    }
}