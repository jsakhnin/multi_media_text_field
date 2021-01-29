package org.mindfulcode.multi_media_text_field;

import io.flutter.plugin.common.PluginRegistry.Registrar;

/** MultiMediaTextFieldPlugin */
public class MultiMediaTextFieldPlugin {
  public static void registerWith(Registrar registrar) {
    registrar
            .platformViewRegistry()
            .registerViewFactory(
                    "plugins.mindfulcode/multimediatextfield", new MultiMediaTextFieldFactory(registrar.messenger()));
  }
}