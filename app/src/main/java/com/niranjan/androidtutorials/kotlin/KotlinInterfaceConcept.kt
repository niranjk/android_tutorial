package com.niranjan.androidtutorials.kotlin

// INTERFACE
interface Fruit {
    // Interface provides the contract properties and methods
    val color: String
    val weight: Double
    val price : Int
    fun isExpensive(): Boolean
}
// Interface can also extend other interfaces
interface Market: Business {
    val quantity: Int
    fun isSold(): Boolean
}
interface Business {
    fun isProfit(): Boolean
}
// a class can implement multiple interfaces
class Apple : Fruit, Market {
    // each class implementing interface must implement all contact methods and properties
    override val color: String = "red"
    override val weight: Double = 2.0
    override val price: Int = 5
    override fun isExpensive() : Boolean = price > 10
    override val quantity: Int = 20
    override fun isSold() = quantity==0
    override fun isProfit() = isSold()
}