part of 'multi_media_text_field.dart';

class MultiMediaTextController {
  MultiMediaTextController._(int id)
      : _channel = new MethodChannel('plugins.mindfulcode/supertextfield_$id');

  final MethodChannel _channel;


  Future<void> setText(String text) async {
    assert(text != null);
    return _channel.invokeMethod('setText', text);
  }

}