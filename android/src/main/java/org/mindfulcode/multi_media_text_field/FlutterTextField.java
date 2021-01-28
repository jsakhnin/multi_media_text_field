package org.mindfulcode.multi_media_text_field;

import android.content.ClipDescription;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import static io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import static io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public class FlutterTextField implements PlatformView, MethodCallHandler {
    private final EditText editText;
    private final MethodChannel methodChannel;
    private final Context _context;

    FlutterTextField(Context context, BinaryMessenger messenger, int id) {
        methodChannel = new MethodChannel(messenger, "plugins.mindfulcode/supertextfield_" + id);
        methodChannel.setMethodCallHandler(this);
        _context = context;
        editText = new EditText(context) {
            @Override
            public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
                final InputConnection ic = super.onCreateInputConnection(editorInfo);
                EditorInfoCompat.setContentMimeTypes(editorInfo,
                        new String [] {"image/png", "image/gif"});

                final InputConnectionCompat.OnCommitContentListener callback =
                        new InputConnectionCompat.OnCommitContentListener() {
                            @Override
                            public boolean onCommitContent(InputContentInfoCompat inputContentInfo,
                                                           int flags, Bundle opts) {
                                // read and display inputContentInfo asynchronously
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && (flags &
                                        InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                                    try {
                                        inputContentInfo.requestPermission();
                                    }
                                    catch (Exception e) {
                                        return false; // return false if failed
                                    }
                                }

                                handleInput(inputContentInfo);

                                // read and display inputContentInfo asynchronously.
                                // call inputContentInfo.releasePermission() as needed.

                                return true;  // return true if succeeded
                            }

                        };
                return InputConnectionCompat.createWrapper(ic, editorInfo, callback);
            }
        };

        // Listen to changes
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                methodChannel.invokeMethod("changeText", editText.getText().toString());
            }
        });

    }

    @Override
    public View getView() {
        return editText;
    }

    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        switch (methodCall.method) {
            case "setText":
                setText(methodCall, result);
                break;
            default:
                result.notImplemented();
        }

    }

    private void setText(MethodCall methodCall, Result result) {
        String text = (String) methodCall.arguments;
        editText.setText(text);
        result.success(null);
    }

    private void handleInput(InputContentInfoCompat inputContent){
        ClipDescription desc = inputContent.getDescription();
        Log.d("Android Edit Text", "onCommitContent: " + inputContent.getContentUri());

        switch (desc.getMimeType(0)){
            case "image/gif":
                Uri image_uri = inputContent.getLinkUri();
                if (image_uri != null){
                    methodChannel.invokeMethod("changeGif", image_uri.toString());
                }
                break;
            case "image/png":
                // handle png
                break;
        }

    }


    @Override
    public void dispose() {}
}
