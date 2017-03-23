package com.ypyproductions.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils
{

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-" +
"Z0-9][a-zA-Z0-9\\-]{0,25})+"
);

    public EmailUtils()
    {
    }

    public static boolean isEmailAddressValid(String s)
    {
        return EMAIL_ADDRESS_PATTERN.matcher(s).matches();
    }

}
