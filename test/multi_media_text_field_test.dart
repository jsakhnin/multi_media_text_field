import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:multi_media_text_field/multi_media_text_field.dart';

void main() {
  const MethodChannel channel = MethodChannel('multi_media_text_field');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

}
