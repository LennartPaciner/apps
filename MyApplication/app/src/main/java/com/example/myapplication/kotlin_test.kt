package com.example.myapplication

fun main(){
    val userName : String = "Bojack"
    var age : Int = 800

    var arr : MutableList<String> = ArrayList()
    arr.add("xd")
    arr.add("ddd")
    //println(arr[0])

    for (name in arr) {
        if (arr[0] == "xd") {
            println("stimmt")
        }
        else {
            println("XDDD")
        }
        println(name)
    }

    println("Hello world $userName. $age alt")
    test("waldmann")
    sayHelloKotlin()
}

fun test(xd : String){
    println(xd)
}


fun sayHelloKotlin() {
    println("Hello")
}




