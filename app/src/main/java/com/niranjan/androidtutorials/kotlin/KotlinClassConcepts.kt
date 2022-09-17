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