package com.niranjan.androidtutorials.kotlin

/***
 * #######################################
 * KOTLIN OBJECT ORIENTED CONCEPTS TUTORIAL
 * #######################################
 */


// Interface

fun main() {

}

interface MyInterface {
    // contract property and methods
    val color: String
    val value: Int
    fun isExpensive(): Boolean
}

class MyClassWithInterface : MyInterface{

    override val color: String = "Blue"

    override val value: Int = 123000

    override fun isExpensive(): Boolean {
       if (value> 100000){
           return true
           println("Oh its expensive!")
       }else {
           return false
           println("Its cheap")
       }
    }

}
