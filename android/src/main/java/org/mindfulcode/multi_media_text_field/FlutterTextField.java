package org.mindfulcode.multi_media_text_field;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

import java.util.Map;

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
    private final Map<String, Object> _creationParams;

    FlutterTextField(Context context, BinaryMessenger messenger, int id, Map<String, Object> creationParams) {
        methodChannel = new MethodChannel(messenger, "plugins.mindfulcode/supertextfield_" + id);
        methodChannel.setMethodCallHandler(this);
        _context = context;
        _creationParams = creationParams;
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
        // Auto-correct
        boolean _isAutoCorrect = (boolean) _creationParams.get("autocorrect");
        Log.d("VIEWDEBUG", "getView: Autocorrect - " + _isAutoCorrect);
        if (!_isAutoCorrect){
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            Log.d("VIEWDEBUG", "Autocrrect is false");
        }
        // Auto fill hints
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String[] _autofillhints = (String[]) _creationParams.get("autofillHints");
            editText.setAutofillHints(_autofillhints);
        }
        // Auto focus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean _focusable = (boolean) _creationParams.get("autofocus");
            editText.setFocusedByDefault(_focusable);
        }
        // Background color
        String _backgroundColor = "#" + (String) _creationParams.get("backgroundColor");
        Log.d("VIEWDEBUG", "getView: Background color : "+ _backgroundColor);
        editText.setBackgroundColor(Color.parseColor(_backgroundColor));
        // Text color
        String _fontColor = "#" + (String) _creationParams.get("fontColor");
        Log.d("VIEWDEBUG", "getView: Font color : "+ _fontColor);
        editText.setTextColor(Color.parseColor(_fontColor));
        // Font family and style
        int _fontStyle = (int) _creationParams.get("fontStyle");
        String _fontFamily = (String) _creationParams.get("fontFamily");
        Log.d("VIEWDEBUG", "getView: Font style : "+ _fontStyle + "  Font family: " + _fontFamily);
        editText.setTypeface(Typeface.create(_fontFamily, _fontStyle));
        // Font size
        double _fontSize = (double) _creationParams.get("fontSize");
        Log.d("VIEWDEBUG", "getView: Font size : "+ _fontSize);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) _fontSize);
        // Set cursor drawable
        Drawable unwrappedDrawable = ContextCompat.getDrawable(_context, R.drawable.cursor);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.RED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("VIEWDEBUG", "getView: changing drawable!");
            editText.setTextCursorDrawable(wrappedDrawable);
        }
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
