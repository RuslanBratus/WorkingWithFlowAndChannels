package com.epam.task1

import com.epam.task1.data.Car
import com.epam.task1.data.Part
import com.epam.task1.utils.log
import com.sun.source.tree.Scope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
        println("last $i")
        delay(1000)
        println("last again $i")
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")



//    val channelActor = Channel<Int>()
//    MainScope().launch {
//        (1..10).forEach {
//            channelActor.send(it)
//        }
//    }
//
//
//    MainScope().launch {
//         log("We receive ${channelActor.receive()}!")
//    }




//    printOrders(orders)
//    startWorkShopWork(orders)
}

fun doSmting(): ReceiveChannel<Int> {
    val array = listOf(1, 5, 6, 9, 7, 8)
    val channelActor = Channel<Int>()
    MainScope().launch {
        (1..10).forEach {
            channelActor.send(it)
        }
    }

    return channelActor
}




val orders = listOf(
    Car(Part.Body.Sedan, Part.Equipment.Premium),
    Car(Part.Body.SportCar, Part.Equipment.Family),
    Car(Part.Body.Sedan, Part.Equipment.LowCost),
    Car(Part.Body.Van, Part.Equipment.Premium),
    Car(Part.Body.Sedan, Part.Equipment.LowCost),
    Car(Part.Body.Van, Part.Equipment.LowCost),
    Car(Part.Body.Van, Part.Equipment.LowCost)
)

fun printOrders(orders: List<Car>) {
    orders.forEach {
        log(it)
    }
}