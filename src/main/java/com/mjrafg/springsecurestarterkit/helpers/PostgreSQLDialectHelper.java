package com.mjrafg.springsecurestarterkit.helpers;

public class PostgreSQLDialectHelper implements DatabaseDialectHelper {
    @Override
    public String getDateFormatFunction() {
        return "TO_CHAR";
    }

    @Override
    public String getDateFormatString() {
        return "YYYY-MM-DD";
    }
}

