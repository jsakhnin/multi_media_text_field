package org.mindfulcode.multi_media_text_field;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class MultiMediaTextFieldFactory extends PlatformViewFactory {
    private final BinaryMessenger messenger;

    public MultiMediaTextFieldFactory(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }

    @Override
    public PlatformView create(Context context, int id, Object args) {
        final Map<String, Object> creationParams = (Map<String, Object>) args;
        Log.d("CREATION PARAMS: ", creationParams.toString());
        return new FlutterTextField(context, messenger, id, creationParams);
    }

}
