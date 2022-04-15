package com.epam.task1.data

import com.epam.task1.PrepareEquipmentRequest
import com.epam.task1.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay

/*
TODO should return actor and contain log("work in equipmentLineName") for each equipmentLine it is important for test!!!
  it is recommended to use delay inside actor body
 */
// This function launches a new equipmentLineOne actor
fun CoroutineScope.createEquipmentLine(equipmentLineName: String): SendChannel<PrepareEquipmentRequest> {
        return this.actor {
            consumeEach {
                log("work in $equipmentLineName")
                delay(300L)
                it.equipmentChannel.send(EquipmentParts(it.equipment))
                it.equipmentChannel.close()
            }
        }
}
