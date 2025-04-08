package indimetra.modelo.helpers;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Validators {

    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;

        String trimmedEmail = email.trim();

        // No debe terminar en punto
        if (trimmedEmail.endsWith("."))
            return false;

        String emailRegex = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(trimmedEmail);

        try {
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Email no válido según el patrón.");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
