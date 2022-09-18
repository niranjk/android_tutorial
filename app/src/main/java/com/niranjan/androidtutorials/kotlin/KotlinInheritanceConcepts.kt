package com.niranjan.androidtutorials.kotlin

// Inheritance : Extending Classes
// open keyword to allow subclassing and inheritance
open class SuperParentClass{
    // not overridden ones..
    var superResult = 3
    fun updateSuperResult(value: Int) {
        superResult = value
    }
    // overridden properties and methods
    open var overriddenProperty = 4
    open fun overridenMethod(value: Int){
        overriddenProperty = value
    }
}
open class ParentClass : SuperParentClass() {
    var parentResult = 2
    fun updateParentResult(value: Int){
        updateSuperResult(value)
        parentResult = superResult + value
    }
    // overridden
    override var overriddenProperty: Int = 2
    override fun overridenMethod(value: Int) {
        overriddenProperty = value * parentResult
    }
}
// ChildClass is final and cannot be inherited
class ChildClass : ParentClass(){
    // Each class can inherit only one parent class
    var childResult = 1
    fun generateChildResult(){
        updateParentResult(childResult)
        childResult = superResult + parentResult
    }
}
