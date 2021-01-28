library multi_media_text_field;

import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

part 'multi_media_text_controller.dart';

typedef void OnCreatedCallback(MultiMediaTextController controller);
typedef void OnTextChangedCallback(String value);
typedef void OnGifChangedCallback(String url);
typedef void OnStickerChangedCallback(String path);


class MultiMediaTextField extends StatefulWidget {
  final OnCreatedCallback onCreated;
  final OnTextChangedCallback onTextChanged;
  final OnGifChangedCallback onGifChanged;
  final OnStickerChangedCallback onStickerChanged;

  MultiMediaTextField({
    Key key,
    this.onCreated,
    this.onTextChanged,
    this.onGifChanged,
    this.onStickerChanged,
  }) : super(key: key);

  @override
  _MultiMediaTextFieldState createState() => _MultiMediaTextFieldState();
}

class _MultiMediaTextFieldState extends State<MultiMediaTextField> {
  MultiMediaTextController _controller;
  String _textValue = "";
  String _imageUriValue = "";
  String _stickerPathValue = "";
  ValueNotifier _textNotifier;
  ValueNotifier _imageNotifier;
  ValueNotifier _stickerNotifier;

  @override
  void initState() {
    // Add notifier for text change
    _textNotifier =  ValueNotifier(_textValue);
    _textNotifier.addListener(() {
      print("We are in listener... things have changed!");
      widget.onTextChanged(_textValue);
    });

    // Add notifier for image change
    _imageNotifier =  ValueNotifier(_imageUriValue);
    _imageNotifier.addListener(() {
      print("We are in listener... things have changed!");
      widget.onGifChanged(_imageUriValue);
    });

    // Add notifier for sticker change
    _stickerNotifier =  ValueNotifier(_stickerPathValue);
    _stickerNotifier.addListener(() {
      print("We are in listener... things have changed!");
      widget.onStickerChanged(_stickerPathValue);
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'plugins.mindfulcode/supertextfield',
        onPlatformViewCreated: _onPlatformViewCreated,
      );
    }
    return Text(
        '$defaultTargetPlatform is not yet supported by the text_view plugin');
  }


  void _onPlatformViewCreated(int id) {
    _controller = new MultiMediaTextController._(id);
    // Start listening to changes
    print("We started listening");
    _controller._channel.setMethodCallHandler(_myListenHandler);
    if (widget.onCreated != null) {
      widget.onCreated(_controller);
    }

  }

  // Listen to channel
  Future<dynamic> _myListenHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'changeText':
        _textValue = methodCall.arguments;
        print("Text value = " + _textValue);
        _textNotifier.value = _textValue;
        return;
      case 'changeGif':
        _imageUriValue = methodCall.arguments;
        _imageNotifier.value = _imageUriValue;
        return;
      case 'changeSticker':
        _stickerPathValue = methodCall.arguments;
        _stickerNotifier.value = _stickerPathValue;
        return;
      default:
        throw MissingPluginException('notImplemented');
    }
  }

}


