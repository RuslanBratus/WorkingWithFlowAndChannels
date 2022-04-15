package com.epam.task1

import com.epam.task1.data.Car
import com.epam.task1.data.OutPut
import com.epam.task1.data.createBodyLine
import com.epam.task1.data.createEquipmentLine
import com.epam.task1.factory.createCar
import com.epam.task1.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.channels.onReceiveOrNull as onReceiveOrNullExt

/**
 * Let’s assume that you have a small automated workshop that produces cars on the automation line.
 * But it is not full automated and it should be observed by [CarConstructor.kt].
 * Each machine uses **body line** and **equipment line** to construct a vehicle with [CarFactory.kt].
 * And there is **order desk**, which collects the orders and starts the whole process.

Our workshop must have:
 * car factory ([CarFactory.kt])
 * 2 constructor teams
 * 2 body lines ([BodyLines.kt])
 * 2 equipment lines  ([EquipmentLines.kt])

Our program should
 * Take an order from singe order list
 * Pick Constructor (in parallel)
 * Create body  (in parallel)
 * Create Equipment (in parallel)
 * Combine the body and equipment
 * Provide a car

 * Tips:
 * Please use channels to synchronise this processes.
 * Add time measurement
 */

@kotlinx.coroutines.ExperimentalCoroutinesApi
fun startWorkShopWork(orders: List<Car>) {
    runBlocking(CoroutineName("com.epam.functions.task1.main")) {
        val t = measureTimeMillis {
            val bodyLineOne = this.createBodyLine(BODY_LINE_1)
            val bodyLineTwo = this.createBodyLine(BODY_LINE_2)
            val equipmentLineOne = this.createEquipmentLine(EQUIPMENT_LINE_1)
            val equipmentLineTwo = this.createEquipmentLine(EQUIPMENT_LINE_2)

            val ordersChannel = processOrders(orders)
            val carChannelA = createCar(
                orders = ordersChannel,
                scope = this,
                bodyLineOne = bodyLineOne,
                bodyLineTwo = bodyLineTwo,
                equipmentLineOne = equipmentLineOne,
                equipmentLineTwo = equipmentLineTwo
            )
            val carChannelB = createCar(
                orders = ordersChannel,
                scope = this,
                bodyLineOne = bodyLineOne,
                bodyLineTwo = bodyLineTwo,
                equipmentLineOne = equipmentLineOne,
                equipmentLineTwo = equipmentLineTwo
            )

            performOrderByConstructorTeam(carChannelA, carChannelB)

            val isShotDown = shutdown(
                bodyLineOne = bodyLineOne,
                bodyLineTwo = bodyLineTwo,
                equipmentLineOne = equipmentLineOne,
                equipmentLineTwo = equipmentLineTwo
            )
            log("all channels are shotDown $isShotDown")
        }
        println("Execution time: $t ms")
    }
}

/**
 *  as of right now there's no 'onReceiveOrClosed' operator so we need to track this manually
 * if the carChannel[A|B] was closed, then onReceiveOrNull is fired on each loop rather
 * than suspending
 * this switches on receive from the two constructors, when an order arrives, we print it here
 */
@ExperimentalCoroutinesApi
private suspend fun performOrderByConstructorTeam(
    carChannelA: ReceiveChannel<OutPut.FinishedCar>,
    carChannelB: ReceiveChannel<OutPut.FinishedCar>
) {
    var isConstructorOneActive = true
    var isConstructorTwoActive = true
    while (isConstructorOneActive || isConstructorTwoActive) {
        select<Unit> {
            if (isConstructorOneActive) {
                carChannelA.onReceiveOrNullExt().invoke { v ->
                    if (carChannelA.isClosedForReceive) isConstructorOneActive = false
                    if (v != null) log("$PROVIDED_BY_CONSTRUCTOR_TEAM_1 : $v")
                }
            }
            if (isConstructorTwoActive) {
                carChannelB.onReceiveOrNullExt().invoke { v ->
                    if (carChannelB.isClosedForReceive) isConstructorTwoActive = false
                    if (v != null) log("$PROVIDED_BY_CONSTRUCTOR_TEAM_2 : $v")
                }
            }
        }
    }
}

/**
 * creates ReceiveChannel to emit orders for constructors teams.
 * */
@ExperimentalCoroutinesApi
private fun CoroutineScope.processOrders(orders: List<Car>) =
    produce(CoroutineName(ORDER_DESK)) {
        for (order in orders) send(order)
    }
