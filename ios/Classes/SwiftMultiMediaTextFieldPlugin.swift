import Flutter
import UIKit

public class SwiftMultiMediaTextFieldPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
        let factory = SwiftMultiMediaTextFieldFactory(messenger: registrar.messenger())
        registrar.register(factory, withId: "plugins.mindfulcode/multimediatextfield")
    }
}
