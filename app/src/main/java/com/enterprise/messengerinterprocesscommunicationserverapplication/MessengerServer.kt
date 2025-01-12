package com.enterprise.messengerinterprocesscommunicationserverapplication

import android.content.Context
import android.content.Intent
import java.util.concurrent.CountDownLatch

class MessengerServer {

    companion object{

        public fun handleIncomingRequest(context: Context, message: String?){

            val intentToLaunch = Intent(context, MainActivity::class.java)

            intentToLaunch.putExtra(AppConstants.MESSAGE_KEY, message)
            intentToLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intentToLaunch)

        }

        public fun sendMessageToClient(message: String?){

            MessengerIPCServerService.messageToSend = message.toString()

            val countDownLatchToWaitSending = CountDownLatch(1)
            MessengerIPCServerService.countDownLatchToWaitSending = countDownLatchToWaitSending

            MessengerIPCServerService.countDownLatchToWaitResponse?.countDown()

            countDownLatchToWaitSending.await()

        }
    }

}