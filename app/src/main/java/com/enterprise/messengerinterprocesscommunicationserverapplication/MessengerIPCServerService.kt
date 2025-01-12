package com.enterprise.messengerinterprocesscommunicationserverapplication

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import java.util.concurrent.CountDownLatch

class MessengerIPCServerService : Service() {


    val TAG = "MessengerIPCServerService"

    companion object{

        public var countDownLatchToWaitResponse: CountDownLatch? = null
        public var countDownLatchToWaitSending: CountDownLatch? = null
        public var messageToSend = ""

    }

    // Messenger IPC - Messenger object contains binder to send to client
    private var mMessenger: Messenger? = null
    private var incomingHandler: IncomingHandler? = null


    // Messenger IPC - Message Handler
    internal inner class IncomingHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val receivedBundle = msg.data
            val receivedData = receivedBundle.getString(AppConstants.DATA)

            Log.d(TAG, "Test")

            MessengerServer.handleIncomingRequest(this@MessengerIPCServerService, receivedData)

            countDownLatchToWaitResponse = CountDownLatch(1)

            countDownLatchToWaitResponse?.await()

            val message = Message.obtain(this@IncomingHandler, 0)
            val bundle = Bundle()
            bundle.putString(AppConstants.DATA, messageToSend)
            message.data = bundle
            msg.replyTo.send(message)

            countDownLatchToWaitSending?.countDown()
        }
    }


    override fun onBind(intent: Intent): IBinder? {

        val thread = HandlerThread("MyHandlerThread")
        thread.start()

        incomingHandler = thread.looper?.let { IncomingHandler(it) }
        mMessenger = Messenger(incomingHandler)

        return mMessenger?.binder

    }
}