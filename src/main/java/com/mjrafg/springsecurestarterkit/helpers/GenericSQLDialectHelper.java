package com.mjrafg.springsecurestarterkit.helpers;

public class GenericSQLDialectHelper implements DatabaseDialectHelper {
    @Override
    public String getDateFormatFunction() {
        // Placeholder, adjust based on actual support
        return "FORMAT_DATE";
    }

    @Override
    public String getDateFormatString() {
        // Placeholder, adjust accordingly
        return "%Y-%m-%d";
    }
}

