import Flutter
import UIKit

public class SwiftMultiMediaTextFieldPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "multi_media_text_field", binaryMessenger: registrar.messenger())
    let instance = SwiftMultiMediaTextFieldPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
