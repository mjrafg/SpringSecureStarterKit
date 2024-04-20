package com.mjrafg.springsecurestarterkit.helpers;

public class MariaDBDialectHelper implements DatabaseDialectHelper {
    @Override
    public String getDateFormatFunction() {
        return "DATE_FORMAT";
    }

    @Override
    public String getDateFormatString() {
        return "%Y-%m-%d";
    }
}
