import 'package:flutter/material.dart';
import 'package:multi_media_text_field/multi_media_text_field.dart';

void main() => runApp(MaterialApp(home: MultiMediaTextFieldExample()));

class MultiMediaTextFieldExample extends StatefulWidget {
  @override
  _MultiMediaTextFieldExampleState createState() =>
      _MultiMediaTextFieldExampleState();
}

class _MultiMediaTextFieldExampleState
    extends State<MultiMediaTextFieldExample> {
  String _myText = "";
  String _myImageUri = "";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar:
            AppBar(title: const Text('Flutter MultiMediaTextField example')),
        body: Column(children: [
          Center(
            child: Container(
              padding: EdgeInsets.symmetric(vertical: 30.0),
              width: 130.0,
              height: 100.0,
              child: MultiMediaTextField(
                onCreated: _onTextFieldCreated,
                onTextChanged: (val) {
                  setState(() {
                    print("TEXT CHANGED");
                    _myText = val;
                  });
                },
                onGifChanged: (val) {
                  setState(() {
                    print("IMAGE CHANGED - \n" + _myImageUri);
                    _myImageUri = val;
                  });
                },
              ),
            ),
          ),
          Expanded(
            flex: 1,
            child: Container(
              color: Colors.blue[100],
              child: Center(child: Text(_myText)),
            ),
          ),
          Expanded(
            flex: 3,
            child: _myImageUri != ""
                ? Container(
                    color: Colors.red[100],
                    child: Image.network(_myImageUri),
                  )
                : Container(),
          ),
        ]));
  }

  void _onTextFieldCreated(MultiMediaTextController controller) {
    controller.setText('Hello from Android!');
  }
}
