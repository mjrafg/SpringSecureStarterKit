package com.mjrafg.springsecurestarterkit.payload;

import lombok.Getter;
import lombok.Setter;

/**
 * Filter parameters for dynamic query generation.
 */
@Getter
@Setter
public class FilterParams {

    private String columnName;
    private String columnValue;
    private String columnProp = ""; // Default to empty string to avoid null checks
    private String operator;
    private String logic = "and"; // Default logic to "and" unless specified otherwise

    /**
     * Ensures that the provided operator is handled in a case-insensitive manner.
     * @return A lowercase version of the operator for consistent processing.
     */
    public String getOperator() {
        return operator != null ? operator.toLowerCase() : "";
    }

    /**
     * Ensures that the logic is handled in a case-insensitive manner and defaults to "and".
     * @return A lowercase version of the logic for consistent processing.
     */
    public String getLogic() {
        return logic != null ? logic.toLowerCase() : "and";
    }
}
