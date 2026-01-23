package pl.studia.teletext.teletext_backend.common.utils.bool;

public final class BooleanUtils {

  public static boolean parseStrictBoolean(String value) {
    value = value.trim();
    if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
      return true;
    } else if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
      return false;
    } else {
      throw new IllegalArgumentException("Value must be either true or false");
    }
  }
}
