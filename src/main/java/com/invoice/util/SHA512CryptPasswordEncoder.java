package com.invoice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.Crypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Builder;

@Builder
public class SHA512CryptPasswordEncoder implements PasswordEncoder {

  private static final String SHA512_PREFIX = "$6$";

  private static final Pattern SALT_PATTERN =
      Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");

  private static final String CRYPT_PREFIX = "{CRYPT}";

  @Override
  public String encode(CharSequence rawPassword) {

    return CRYPT_PREFIX + Crypt.crypt(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    if (encodedPassword == null || rawPassword == null) {
      return false;
    } else if (encodedPassword.startsWith(CRYPT_PREFIX)) {
      return matches(rawPassword, encodedPassword.substring(CRYPT_PREFIX.length()));
    } else if (!encodedPassword.startsWith(SHA512_PREFIX)) {
      return false;
    } else {
      Matcher m = SALT_PATTERN.matcher(encodedPassword);

      if (m.find()) {
        String encodedSalt = m.group(4);
        String encodedRawPassword = Crypt.crypt(rawPassword.toString(), SHA512_PREFIX + encodedSalt);

        return encodedPassword.equals(encodedRawPassword);
      }
    }

    return false;
  }
 }
