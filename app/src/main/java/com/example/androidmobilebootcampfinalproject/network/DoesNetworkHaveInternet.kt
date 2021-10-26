package com.example.androidmobilebootcampfinalproject.network

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory


//Sending a ping to google's primary DNS.
//If successful, that means we have internet.
object DoesNetworkHaveInternet {

    //this should be executed on a background thread, when it's called!!
    fun execute(socketFactory: SocketFactory): Boolean {
        return try{
            Log.d(CM_TAG, "PINGING google.")
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.d(CM_TAG, "PING success.")
            true
        } catch (e: IOException){
            Log.e(CM_TAG, "No internet connection. $e")
            false
        }
    }
}