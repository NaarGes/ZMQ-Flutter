import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter ZMQ Demo',
      theme: ThemeData(
        primarySwatch: Colors.deepPurple,
      ),
      home: MyHomePage(title: 'Flutter ZMQ Demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  static const platform = const MethodChannel('com.example.flutter_zmq/zmq');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Running ZMQ through Android Channel',
            ),
            SizedBox(height: 16),
            ElevatedButton(onPressed: _startServer, child: Text('Start Server')),
            SizedBox(height: 16),
            ElevatedButton(onPressed: _startClient, child: Text('Start Client')),
          ],
        ),
      ),
    );
  }

  void _startServer() {
    try {
      platform.invokeMethod('startServer');
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  void _startClient() {
    try {
      platform.invokeMethod('startClient');
    } on PlatformException catch (e) {
      print(e.message);
    }
  }
}
