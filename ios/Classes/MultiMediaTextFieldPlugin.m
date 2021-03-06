#import "MultiMediaTextFieldPlugin.h"
#if __has_include(<multi_media_text_field/multi_media_text_field-Swift.h>)
#import <multi_media_text_field/multi_media_text_field-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "multi_media_text_field-Swift.h"
#endif

@implementation MultiMediaTextFieldPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMultiMediaTextFieldPlugin registerWithRegistrar:registrar];
}
@end
