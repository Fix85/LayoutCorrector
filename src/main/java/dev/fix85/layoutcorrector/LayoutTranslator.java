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

    public static boolean shouldCorrectLayout(String input) {
        if (input == null || input.isEmpty()) return false;
        // Если начинается с точки (которая на русской раскладке является слэшем)
        if (input.charAt(0) == '.') {
            if (input.length() > 1) {
                char next = input.charAt(1);
                return isRussianLetter(next);
            }
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

    /**
     * Пытается перевести команду согласно пользовательским алиасам в Config.
     * Например, "/спавн" -> "/spawn" или "/ах 10" -> "/ah 10".
     * Входная строка cmd может начинаться как с "/", так и быть без неё (для sendCommand).
     */
    public static String translateAliases(String cmd) {
        if (cmd == null || cmd.isEmpty()) return cmd;
        boolean hasSlash = cmd.startsWith("/");
        String raw = hasSlash ? cmd.substring(1) : cmd;

        // Разделяем на команду и аргументы
        int spaceIndex = raw.indexOf(' ');
        String commandName = spaceIndex == -1 ? raw : raw.substring(0, spaceIndex);
        String arguments = spaceIndex == -1 ? "" : raw.substring(spaceIndex);

        // Проверяем наличие алиаса
        String targetCommand = Config.get().aliases.get(commandName.toLowerCase());
        if (targetCommand != null) {
            String result = targetCommand + arguments;
            return hasSlash ? "/" + result : result;
        }

        return cmd;
    }
}
