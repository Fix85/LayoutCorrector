package dev.fix85.layoutcorrector;

import java.util.HashMap;
import java.util.Map;

public class LayoutTranslator {
    private static final Map<Character, Character> RU_TO_EN = new HashMap<>();

    static {
        String ru = "йцукенгшщзхъфывапролджэячсмитьбю.ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ,ёЁ\"№;?:";
        String en = "qwertyuiop[]asdfghjkl;'zxcvbnm,./QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?`~@#$;?:";
        for (int i = 0; i < Math.min(ru.length(), en.length()); i++) {
            RU_TO_EN.put(ru.charAt(i), en.charAt(i));
        }
    }

    public static String translateLayout(String input) {
        if (input == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            sb.append(RU_TO_EN.getOrDefault(c, c));
        }
        return sb.toString();
    }

    public static boolean shouldCorrectLayout(String input, String prefix) {
        if (input == null || input.isEmpty() || prefix == null || prefix.isEmpty()) return false;
        if (input.startsWith(prefix) && input.length() > prefix.length()) {
            char next = input.charAt(prefix.length());
            return isRussianLetter(next);
        }
        return false;
    }

    public static boolean isRussianLetter(char c) {
        return (c >= 'а' && c <= 'я') || (c >= 'А' && c <= 'Я') || c == 'ё' || c == 'Ё';
    }

    public static boolean containsRussian(String input) {
        if (input == null) return false;
        for (int i = 0; i < input.length(); i++) {
            if (isRussianLetter(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String translateAliases(String cmd) {
        if (cmd == null || cmd.isEmpty()) return cmd;
        boolean hasSlash = cmd.startsWith("/");
        String raw = hasSlash ? cmd.substring(1) : cmd;

        int spaceIndex = raw.indexOf(' ');
        String commandName = spaceIndex == -1 ? raw : raw.substring(0, spaceIndex);
        String arguments = spaceIndex == -1 ? "" : raw.substring(spaceIndex);

        String targetCommand = Config.get().aliases.get(commandName.toLowerCase());
        if (targetCommand != null) {
            String result = targetCommand + arguments;
            return hasSlash ? "/" + result : result;
        }

        return cmd;
    }
}
