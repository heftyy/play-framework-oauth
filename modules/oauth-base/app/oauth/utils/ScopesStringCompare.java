package oauth.utils;

public final class ScopesStringCompare {
    /**
     * UNUSED
     * Compares two strings describing scopes. Has only simple expressions like * for all, | for or, ! for not.
     *
     * @param arg1 String: First string to compare.
     * @param arg2 String: Second string to compare.
     * @return Boolean: True if strings match, false if they dont.
     */
    public static boolean compareStrings(String arg1, String arg2) {
        String[] arg1Array = arg1.split("/");
        String[] arg2Array = arg2.split("/");
        String simpleRegexArg1;
        String simpleRegexArg2;
        for (int i = 0; i < arg1Array.length; i++) {
            if (arg1Array[i].compareTo(arg2Array[i]) == 0) continue;
            else {
                if ((simpleRegexArg1 = getSimpleRegex(arg1Array[i])) != null) {
                    if (simpleRegexArg1.indexOf('*') != -1) {
                        return true;
                    } else if (simpleRegexArg1.indexOf('|') != -1) {
                        String[] splitOR = arg1Array[i].split("|");
                        for (String aSplitOR : splitOR) {
                            if (aSplitOR.compareTo(arg2Array[i]) == 0) continue;
                        }
                        return false;
                    } else if (simpleRegexArg1.indexOf('!') != -1) {
                        return false;
                    }
                } else if ((simpleRegexArg2 = getSimpleRegex(arg2Array[i])) != null) {
                    if (simpleRegexArg2.indexOf('*') != -1) {
                        return true;
                    } else if (simpleRegexArg2.indexOf('|') != -1) {
                        boolean found = false;
                        String[] splitOR = arg2Array[i].split("|");
                        for (String aSplitOR : splitOR) {
                            if (aSplitOR.compareTo(arg1Array[i]) == 0) {
                                found = true;
                                break;
                            }
                        }
                        if (found) continue;
                        return false;
                    } else if (simpleRegexArg2.indexOf('!') != -1) {
                        return false;
                    }
                } else return false;
            }
        }
        return true;
    }

    /**
     * Compare two strings using regular expressions. If the second string is shorter and
     * ends with /* that means anything after that part is OK.
     *
     * @param scopeRequest    String: First string is the scope asked for.
     * @param scopeRegexMatch String: Second string is the containing regular expressions, its the scope described in properties .xml file.
     * @return Boolean: True if the strings match, false if not.
     */
    public static boolean compareStringsRegex(String scopeRequest, String scopeRegexMatch) {
        String[] scopeRequestArray = scopeRequest.split("/");
        String[] scopeRegexMatchArray = scopeRegexMatch.split("/");
        String simpleRegex;
        if (scopeRequestArray.length < scopeRegexMatchArray.length) {
            return false;
        }
        for (int i = 0; i < scopeRequestArray.length; i++) {
            if (i >= scopeRegexMatchArray.length - 1) {
                if (scopeRegexMatchArray[scopeRegexMatchArray.length - 1].startsWith("*")) {
                    return true;
                }
            }
            if (scopeRequestArray[i].compareTo(scopeRegexMatchArray[i]) == 0) continue;
            else {
                if ((simpleRegex = getRegex(scopeRegexMatchArray[i])) != null) {
                    if (scopeRequestArray[i].matches(simpleRegex)) continue;
                    else return false;
                } else return false;
            }
        }
        return true;
    }

    /**
     * Get the regex between two /. Regex starts with '[' and ends with ']'.
     *
     * @param arg String: String in between two /.
     * @return String: Regex string.
     */
    private static String getRegex(String arg) {
        String simpleRegex = null;
        if (arg.charAt(0) == '[' && arg.charAt(arg.length() - 1) == ']') {
            simpleRegex = arg.substring(1, arg.length() - 1);
        }
        return simpleRegex;
    }

    /**
     * UNUSED
     * Get the regex between two /. Regex starts with '[' and ends with ']'.
     *
     * @param mString: String in between two /.
     * @return String: Regex string.
     */
    private static String getSimpleRegex(String mString) {
        String simpleRegex = null;
        int leftBracket = mString.indexOf('[');
        int rightBracket = mString.indexOf(']');
        if (leftBracket != -1 && rightBracket != -1) {
            simpleRegex = mString.substring(leftBracket + 1, rightBracket);
        }
        return simpleRegex;
    }
}