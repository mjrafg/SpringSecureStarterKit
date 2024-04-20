package com.mjrafg.springsecurestarterkit.utils;

import com.mjrafg.springsecurestarterkit.helpers.DatabaseDialectHelper;
import com.mjrafg.springsecurestarterkit.payload.FilterParams;
import jakarta.persistence.criteria.*;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilterSpecificationUtils {


    // Method to set the database dialect helper based on the detected or configured database type
    @Setter
    private static DatabaseDialectHelper databaseDialectHelper;

    private static Object getCompareValue(String value){
        if(value.equals("true"))return true;
        if(value.equals("false"))return false;
        return value;
    }
    public static <T> Specification<T> createSpecification(List<FilterParams> filters) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicatesAnd = new ArrayList<>();
            List<Predicate> predicatesOr = new ArrayList<>();
            for (FilterParams filter : filters) {
                String value = filter.getColumnValue();
                String operator = filter.getOperator();
                String columnName = filter.getColumnName();
                String columnProp = filter.getColumnProp();
                if (CommonUtils.isNotEmpty(value) && CommonUtils.isNotEmpty(columnName)) {
                    Predicate predicate = switch (operator) {
                        case "contains" ->
                                criteriaBuilder.like(criteriaBuilder.lower(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName)), "%" + value.toLowerCase() + "%");
                        case "startsWith" ->
                                criteriaBuilder.like(criteriaBuilder.lower(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName)), value.toLowerCase() + "%");
                        case "endsWith" ->
                                criteriaBuilder.like(criteriaBuilder.lower(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName)), "%" + value.toLowerCase());
                        case "equals", "=" ->
                                criteriaBuilder.equal(criteriaBuilder.lower(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName)), value.toLowerCase());
                        case ">" -> criteriaBuilder.greaterThan(root.get(columnName), value);
                        case "<" -> criteriaBuilder.lessThan(root.get(columnName), value);
                        case ">=" -> criteriaBuilder.greaterThanOrEqualTo(root.get(columnName), value);
                        case "<=" -> criteriaBuilder.lessThanOrEqualTo(root.get(columnName), value);
                        case "!=" -> criteriaBuilder.notEqual(root.get(columnName), value);
                        case "isEmpty" -> criteriaBuilder.isEmpty(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName));
                        case "isNotEmpty" -> criteriaBuilder.isNotEmpty(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName));
                        case "isAnyOf" -> {
                            CriteriaBuilder.In<Object> inClause = criteriaBuilder.in(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName));
                            Arrays.stream(value.split(",")).forEach(val -> inClause.value(val.trim()));
                            yield inClause;
                        }
                        case "is" ->
                                criteriaBuilder.equal(CommonUtils.isNotEmpty(columnProp) ? root.get(columnName).get(columnProp) : root.get(columnName), getCompareValue(value));
                        case "isDate" ->
                                criteriaBuilder.equal(criteriaBuilder.function(databaseDialectHelper.getDateFormatFunction(), String.class, root.get(columnName), criteriaBuilder.literal(databaseDialectHelper.getDateFormatString())),value);
                        case "not" -> {
                            yield criteriaBuilder.notEqual(criteriaBuilder.function(databaseDialectHelper.getDateFormatFunction(), String.class, root.get(columnName), criteriaBuilder.literal(databaseDialectHelper.getDateFormatString())),value);
                        }
                        case "after" -> {
                            yield criteriaBuilder.greaterThan(criteriaBuilder.function(databaseDialectHelper.getDateFormatFunction(), String.class, root.get(columnName), criteriaBuilder.literal(databaseDialectHelper.getDateFormatString())),value);
                        }
                        case "before" -> {
                            yield criteriaBuilder.lessThan(criteriaBuilder.function(databaseDialectHelper.getDateFormatFunction(), String.class, root.get(columnName), criteriaBuilder.literal(databaseDialectHelper.getDateFormatString())),value);
                        }
                        case "between" -> {
                            String[] dates = value.split(",");
                            Expression<String> columnExpression = criteriaBuilder.function(
                                    databaseDialectHelper.getDateFormatFunction(),
                                    String.class, root.get(columnName),
                                    criteriaBuilder.literal(databaseDialectHelper.getDateFormatString()));
                            yield criteriaBuilder.between(columnExpression, dates[0], dates[1]);
                        }
                        default -> null;
                        // Add more cases as needed
                    };

                    if (predicate != null) {
                        if ("or".equalsIgnoreCase(filter.getLogic())) {
                            predicatesOr.add(predicate);
                        } else {
                            predicatesAnd.add(predicate);
                        }
                    }
                }

            }

            Predicate finalPredicate = null;
            if (!predicatesAnd.isEmpty()) {
                finalPredicate = criteriaBuilder.and(predicatesAnd.toArray(new Predicate[0]));
            }
            if (!predicatesOr.isEmpty()) {
                Predicate orPredicate = criteriaBuilder.or(predicatesOr.toArray(new Predicate[0]));
                finalPredicate = (finalPredicate == null) ? orPredicate : criteriaBuilder.and(finalPredicate, orPredicate);
            }
            return finalPredicate;
        };
    }
}

