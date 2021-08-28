package com.example.flutterzmq.flutter_zmq

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.zeromq.SocketType
import org.zeromq.ZMQ
import java.util.concurrent.Executors

class MainActivity : FlutterActivity() {

    private val CHANNEL = "com.example.flutter_zmq/zmq"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, _ ->
            if (call.method == "startServer")
                startServer()
            else if (call.method == "startClient")
                startClient()
        }
    }

    private fun startServer() {
        val serverExecutor = Executors.newSingleThreadExecutor()
        serverExecutor.execute {
            val context = ZMQ.context(1)
            val socket = context.socket(SocketType.PUB)

            // connect to address where puller bound itself
            println("Connecting to subscribed clients...")
            socket.bind("tcp://127.0.0.1:5559")

            for (i in 1..20) {
                Thread.sleep(100)
                val plainRequest = "Hello "
                val sent = socket.send(plainRequest, 0)
                println("publishing data $i $plainRequest, published status: $sent")

            }
            socket.close()
        }
    }

    private fun startClient() {
        // infinite loop where we'll be consistently pulling data from a particular socket
        val clientExecutor = Executors.newSingleThreadExecutor()
        clientExecutor.execute {
            val context = ZMQ.context(1)
            val socket = context.socket(SocketType.SUB)

            socket.connect("tcp://127.0.0.1:5559")
            socket.subscribe("")
            while (true) {
                val rawRequest = socket.recvStr(0)
                println("subscribed to some data: $rawRequest")
            }
        }
    }
}
