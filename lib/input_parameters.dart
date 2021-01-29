part of 'multi_media_text_field.dart';

class InputParameters{
  final bool autocorrect;
  final List<String> autofillHints;
  final bool autofocus;
  final String backgroundColor;
  final String fontColor;
  final String fontFamily;
  final double fontSize;
  final int fontStyle;

  InputParameters({this.autocorrect, this.autofillHints, this.autofocus, this.backgroundColor, this.fontColor, this.fontFamily, this.fontSize, this.fontStyle,});

  factory InputParameters.fromWidget(MultiMediaTextField widg){
    return InputParameters(
      autocorrect: widg.autocorrect,
      autofillHints: widg.autofillHints,
      autofocus: widg.autofocus,
      backgroundColor: widg.style.backgroundColor != null ? widg.style.backgroundColor.value.toRadixString(16): "FFFFFFFF",
      fontColor: widg.style.color != null ? widg.style.color.value.toRadixString(16): "FF000000",
      fontFamily: widg.style.fontFamily,
      fontSize: widg.style.fontSize ?? 14,
      fontStyle: widg.style.fontStyle != null ? widg.style.fontStyle.index : 0,
    );
  }
  Map<String, dynamic> toJson() {
    return <String, dynamic> {
      'autocorrect': this.autocorrect,
      'autofillHints': this.autofillHints,
      'autofocus': this.autofocus,
      'backgroundColor': this.backgroundColor,
      'fontColor': this.fontColor,
      'fontFamily': this.fontFamily,
      'fontSize': this.fontSize,
      'fontStyle': this.fontStyle,
    };
  }
}