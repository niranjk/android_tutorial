package com.niranjan.androidtutorials.kotlin

class KotlinBasics {

    fun kotlinOperators(){
        // Mathematical operators  + - * / %
        // Increment operator ++
        // Decrement operator --
        // Comparison operator < <= > >=
        // Assignment operator =
        // Equality operator == !=
        // Safe call operator ?
        // !! Bang Bang Operator
        // Elvis operator ?:
        // Lazy disjunction ||
        // Lazy conjunction &&
        // Negation !
    }

    fun kotlinDataTypes(){
        // Integer Types
        val bytes : Byte = 123.toByte() // 8 bits (I am using Type Casting Here .toByte())
        val hexBytes = 0xff_ec_de_fe
        var int_v : Int = 1_000_000_000 // 32 bits
        var short_v : Short = 10_000 // 16 bits
        var long_v : Long = 1_000_000_000_000_000_000 // 64 bits
        // Floating-point Types
        var double_v : Double = 1212.00 // 64 bits
        var float_v : Float = 32F // 32 bits
        // Numeric Types
        var char_v : Char = 'a' // 16 bit unicode character
        var boolean_v : Boolean = true // 8 bits
        // Strings
        val s1 = "Hello Niran!"
        val string_concatination = s1 + "How are you doing?"
        print("$s1")  // String Template
        print("${s1 + double_v}") // String Template Expressions
    }

    fun kotlinVariables(){
        // Type Inferance : is when the compiler automatically infers the Type
        val name = "Niran" // Immutable Variable
        var age = 20 // Mutable Variable
        // In Kotlin null variables are not allowed by default
        // we should use the Safe Call operator (?) to assign null values
    }

    fun kotlinConditionals(){
        // if else
        val age = 20
        if ( age < 13) println("childrens")
        else if (age in 13..19) println("teenage") //I am using Ranges here..
        else println("adult")
        // when statements -- Start
        when(age){
            in 0..12 -> println("childrens")
            in 13..19 -> println("teens")
            else -> println("adults")
        }
        // when statement -- end
        // for loops
        val cars = arrayOf("audi", "fiat", "ferrari", "toyota", "mercedez")
        cars.forEach {
            println(it)
        }
        for (i in 1..100) print(i)
        for (i in 5 downTo 1) print(i)
        for (i in 2..20 step 3) print(i)
        // do - while loops
        var apples = 20
        do {
            apples --
            println(apples)
        }while (apples<10)
        // repeat loops
        repeat(2){
            println("Niran")
        }
    }

    data class Player(val name: String, val club: String, val number: Int)
    fun kotlinCollections(){
        // LIST - ordered
        val playerList = listOf(Player("Ronaldo","RealMadrid",7), Player("Messi", "Barcelona",10),
        Player("Ronaldinio", "ACMilan",10), Player("Neymar","Barcelona",9))
        // associateBy and groupBy builds Map associated by the specified KeySelector
        val player10 = playerList.associateBy { it.number } // uses last suitable element as value
        val playerGroup10 = playerList.groupBy( Player::club, Player::name ) // builds list of all suitable elements and put in value
        val playerFiltered = playerList.filter { it.name.length==6 }.size // Filter collections with conditions true
        val findM = playerList.find { it.name.startsWith('M') } // returns first elements that matches
        val findR = playerList.findLast { it.name.contains('R') } // returns last element that matches
        var first = playerList.first()
        var firstOrNull = playerList.firstOrNull { it.name.contains('N') }
        var countPlayers = playerList.count { it.name.contains('R') } // returns total no. of elements
        var last = playerList.last()
        var numbers = listOf(1, 2, 3, -1, 4, -2, -4, -2)
        val (positive, negative) = numbers.partition { it>0 } // split the original collection into 2 with the conditions
        var mutableFruitsList = mutableListOf("apple","orange","kiwi")
        mutableFruitsList.add(2,"banana")
        mutableFruitsList.remove("kiwi")
        val combinedList = listOf(numbers,mutableFruitsList)
        val singleflatMapList = combinedList.flatMap { it } // transforms into iterable object and create a single list
        // ARRAY
        var instruments = arrayOf("guitar","drums")
        var multiTypeArrays = arrayOf(2, "one", false, null)
        // SET - unordered - no duplicates
        var bookSet = setOf("book1","book2")
        // MAP
        var map = mapOf<Int, String>( 1 to "one", 2 to "two")
        var mutableMap = mutableMapOf( 'a' to "apple", 'b' to "banana")
        mutableMap.put('b', "ball")
        if (mutableMap.containsKey('a')) print("the map contains the value")
        mutableMap.forEach { if (it.key=='b') print(it.value) }
        mutableMap.map { "K:"+it.key } // apply transformation to all elements in collection
        val getOrElseResult = mutableMap.getOrElse('c'){"Carrot"} // returns a default value specified
        // HASH MAP
        var hashMap = hashMapOf(Pair("001", "Rome"), Pair("002", "Milan"))
        val any1 = hashMap.any { it.key=="001" } // returns true if collection contains at least one
        val all1 = hashMap.all { it.key=="001" } // returns true if all elements match in collection
        val none1 = hashMap.none { it.key == "001" } // returns true if no elements match in collection
    }

    fun kotlinNullSafety(){
        // In kotlin, null variables are not allowed by default
        // So, we use Safe call operator after the type then we can assign null to variables
        var totalAmount : Int? = null
        // Checking for null
        if (totalAmount != null) println("your amount is: $totalAmount")
        // !! operator
        val booksArray = arrayOf(null, "book2")
        val size = booksArray[0]!!.length // throws the NullPointerException if the value of array in index is null
        // Elvis operator
        val size_elvis = booksArray[0]?.length?:0  // elvis chain null tests : so returns default value in case of null
    }
}