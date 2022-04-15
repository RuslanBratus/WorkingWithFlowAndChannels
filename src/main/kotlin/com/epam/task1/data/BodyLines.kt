package com.epam.task1.data

import com.epam.task1.PrepareBodyRequest
import com.epam.task1.PrepareEquipmentRequest
import com.epam.task1.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay

/*
TODO should return actor and contain log("work in bodyLineName") for each bodyLine it is important for test!!!
  it is recommended to use delay inside actor body
 */
// This function launches a new bodyLineOne actor
fun CoroutineScope.createBodyLine(bodyLineName: String): SendChannel<PrepareBodyRequest> {
    return this.actor {
        consumeEach {
            log("work in $bodyLineName")
            delay(300L)
            it.bodyPartsChannel.send(BodyParts(it.chosenBody))
            it.bodyPartsChannel.close()
        }
    }
}
