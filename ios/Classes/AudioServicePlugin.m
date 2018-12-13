#import "AudioServicePlugin.h"
#import <audio_service/audio_service-Swift.h>

@implementation AudioServicePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAudioServicePlugin registerWithRegistrar:registrar];
}
@end
