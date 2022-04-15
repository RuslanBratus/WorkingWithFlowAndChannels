package com.epam.task1.factory

import com.epam.task1.PrepareBodyRequest
import com.epam.task1.PrepareEquipmentRequest
import com.epam.task1.combineBody
import com.epam.task1.combineEquipment
import com.epam.task1.data.*
import com.epam.task1.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.system.measureTimeMillis

// producer of completed car orders
/*
TODO should return produce and contain log("Processing order: $order") it is important for test!!!
   has to use functions finalCompose(), getEquipment(), getBody(), preparedEquipment(), prepareBody()
 */
@ExperimentalCoroutinesApi
fun createCar(
    orders: ReceiveChannel<Car>,
    scope: CoroutineScope,
    bodyLineOne: SendChannel<PrepareBodyRequest>,
    bodyLineTwo: SendChannel<PrepareBodyRequest>,
    equipmentLineOne: SendChannel<PrepareEquipmentRequest>,
    equipmentLineTwo: SendChannel<PrepareEquipmentRequest>
): ReceiveChannel<OutPut.FinishedCar> = scope.produce{
        orders.consumeEach {
            log("Processing order: $it")
            val chosenBody = prepareBody(it.body)
            val chosenEquipment = preparedEquipment(it.equipment)
            getBody(chosenBody, bodyLineOne, bodyLineTwo)
            getEquipment(chosenEquipment, equipmentLineOne, equipmentLineTwo)
            send(finalCompose(it, BodyParts(chosenBody), EquipmentParts(chosenEquipment)))

        }
}


suspend fun getEquipment(
    preparedEquipment: ChosenEquipment,
    equipmentLineOne: SendChannel<PrepareEquipmentRequest>,
    equipmentLineTwo: SendChannel<PrepareEquipmentRequest>
) = combineEquipment(preparedEquipment, equipmentLineOne, equipmentLineTwo)

suspend fun getBody(
    preparedBody: ChosenBody,
    bodyLineOne: SendChannel<PrepareBodyRequest>,
    bodyLineTwo: SendChannel<PrepareBodyRequest>
) = combineBody(preparedBody, bodyLineOne, bodyLineTwo)


suspend fun prepareBody(body: Part.Body): ChosenBody {
    log("Preparing car body $body")
    delay(400)
    return ChosenBody(body)
}

suspend fun preparedEquipment(equipment: Part.Equipment): ChosenEquipment {
    log("Preparing car equipment $equipment")
    delay(400)
    return ChosenEquipment(equipment)
}

// composes provided BodyParts and EquipmentParts to FinishedCar
suspend fun finalCompose(
    order: Car,
    bodyParts: BodyParts,
    equipment: EquipmentParts
): OutPut.FinishedCar {
    log("Combining parts $bodyParts, $equipment")
    delay(100)
    return OutPut.FinishedCar(order, bodyParts, equipment)
}