package com.enterprise.messengerinterprocesscommunicationserverapplication

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class MessengerServer {

    companion object{

        public fun handleIncomingRequest(context: Context, message: String?){

            GlobalScope.launch(Dispatchers.IO) {

                val replyMessage = "$message Success"

                MessengerServer.sendMessageToClient(replyMessage)

            }

        }

        public suspend fun sendMessageToClient(message: String?){

            MessengerIPCServerService.messageToSend = message.toString()

            val countDownLatchToWaitSending = CountDownLatch(1)
            MessengerIPCServerService.countDownLatchToWaitSending = countDownLatchToWaitSending

            MessengerIPCServerService.countDownLatchToWaitResponse?.countDown()

            countDownLatchToWaitSending.await()

        }
    }

}