library multi_media_text_field;

import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

part 'multi_media_text_controller.dart';
part 'input_parameters.dart';

typedef void OnTextChangedCallback(String value);
typedef void OnGifChangedCallback(String url);
typedef void OnStickerChangedCallback(String path);

class MultiMediaTextField extends StatefulWidget {
  /// Whether or not to use auto correction for input text
  ///
  /// Defaults to true. Cannot be null
  final bool autocorrect;

  /// List of auto fill hints
  final List<String> autofillHints;

  /// Whether this text field should focus itself if nothing else is already focused.
  ///
  /// If true, the keyboard will open as soon as this text field obtains focus. Otherwise, the keyboard is only shown after the user taps the text field.
  ///
  /// Defaults to false. Cannot be null.
  final bool autofocus;

  /// Listens to changes in input text
  final OnTextChangedCallback onTextChanged;

  /// Listens to changes in gif input and returns URL
  final OnGifChangedCallback onGifChanged;

  /// Listens to sticker input and returns URI (may be local)
  final OnStickerChangedCallback onStickerChanged;

  /// The style used for the text being edited
  final TextStyle style;

  MultiMediaTextField({
    Key key,
    this.autocorrect = true,
    this.autofillHints,
    this.autofocus = false,
    this.onTextChanged,
    this.onGifChanged,
    this.onStickerChanged,
    this.style = const TextStyle(),
  }) : super(key: key);

  @override
  _MultiMediaTextFieldState createState() => _MultiMediaTextFieldState();
}

class _MultiMediaTextFieldState extends State<MultiMediaTextField> {
  InputParameters _inputParameters;
  NativeTextController _controller;
  String _textValue = "";
  String _imageUriValue = "";
  String _stickerPathValue = "";
  ValueNotifier _textNotifier;
  ValueNotifier _imageNotifier;
  ValueNotifier _stickerNotifier;

  @override
  void initState() {
    // Initialize parameters
    _inputParameters = InputParameters.fromWidget(widget);
    // Add notifier for text change
    _textNotifier = ValueNotifier(_textValue);
    _textNotifier.addListener(() {
      print("We are in listener... things have changed!");
      widget.onTextChanged(_textValue);
    });

    // Add notifier for image change
    _imageNotifier = ValueNotifier(_imageUriValue);
    _imageNotifier.addListener(() {
      print("We are in listener... things have changed!");
      widget.onGifChanged(_imageUriValue);
    });

    // Add notifier for sticker change
    _stickerNotifier = ValueNotifier(_stickerPathValue);
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
        viewType: 'plugins.mindfulcode/multimediatextfield',
        onPlatformViewCreated: _onPlatformViewCreated,
        creationParams: _inputParameters.toJson(),
        creationParamsCodec: const StandardMessageCodec(),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: 'plugins.mindfulcode/multimediatextfield',
      );
    }
    return Text(
        '$defaultTargetPlatform is not yet supported by the text_view plugin');
  }

  void _onPlatformViewCreated(int id) {
    _controller = new NativeTextController._(id);
    // Start listening to changes
    print("We started listening");
    _controller._channel.setMethodCallHandler(_myListenHandler);
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
