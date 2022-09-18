package com.niranjan.androidtutorials.kotlin

class KotlinClassConcepts {
    // Using Class objects
    var car = Car()
    var bike = Bike(1000, "red")
    var train = Trains(20)

    fun methodOne(){
        car.updateColor("blue")
        println(car.color) // you can access the property of class with . sign
        println(bike.price)
        //println(train.coutch) // we cannot access the coutch paramter
    }

    // Custom Getter And Setter
    var bikeName: String = ""
        get() = "${bike.color} Bike"
        set(value) {
            field = "Bike: $value"
        }
}

class Car {
    // Class is the Blueprint of Objects
    // It consists of methods that operates on their object instances
    // Constructors can contain no parameters
    var color: String = "white"
    val price: Int = 2000
    var forSale: Boolean = false
    /* You can also declare a primary constructor as follows
    constructor(color: String){

    }
     */

    fun updateColor( newColor: String){
        color = newColor
    }

}

class Bike( val price: Int, var color: String = "white"){
    // constructor with parameters
    // if parameters consist of val or var the copy exists in all instance of class
    // Secondary constructors
    constructor(name: String) : this(1000, "blue")
    constructor(length: Int, breadth: Int) : this(length*breadth)
    init {
        println("Bike color ${this.color}")
    }

    // Predefined Data class that store 2 and 3 data respectively
    val pair = Pair(first = 1, second = 2)
    var triple = Triple(first = "a", second = "b", third = "c")
    // Alternative way of declaring Pair using the to keyword
    val alternativePair = 1 to "One"
}

class Trains( coutch: Int){
    // constructor parameter do not contain var or val so the copy exists only with
    // the scope of the constructor
    init {
        // primary constructor
        val length = coutch*10
        // any required code initialization is run in init block
    }

    init {
        // multiple init block is allowed
    }
}

// Abstract Class
abstract class Food {
    abstract val name: String
    abstract val price: Int
    fun eat(){
        println("I am eating $name")
    }
}
class Pasta() : Food(){
    override val name: String = "Pasta"
    override val price: Int = 2
}
class UseCaseAbstractClass{
    val pasta = Pasta()
    fun eating(){
        pasta.eat()
    }
}


// Data class
data class Player(val name: String, val id: Int = 0, val club: String)

// Enum class
enum class FruitsEnum(val color: String){
    APPLE(color = "red"),
    BANANA(color = "yellow"),
    ORANGE(color = "orange"),
    MANGO(color = "green")
}

// Singleton / Object
object Calculator {
    // only 1 instance of this class exists you cannot create more instances..
    fun add( n1: Int, n2: Int) = n1+n2
    fun sub( n3: Int, n4: Int) = n4-n3
}

// Companion Object
class Robot{
    // Share the single instance of set of properties and methods
    companion object RobotConstants{
        const val MEASUREMENT_UNIT = "METERS"
        const val ROBOT_NAME = "NIXY"
        val size = 10.0
        val weight = 2.0
        fun capacity(input: Int, output: Int)= output-input
    }
}