package com.soen343.project.database.query;

import java.util.Date;
import java.util.Map;

/**
 * Created by Kevin Tan 2018-09-24
 */


public class QueryBuilder {

    private final static String INSERT_INTO = "INSERT INTO ";
    private final static String SELECT = "SELECT ";
    private final static String ALL = " * ";
    private final static String TABLE_ALL = ".*";
    private final static String FROM = " FROM ";
    private final static String VALUES = " VALUES ";
    private final static String WHERE = " WHERE ";
    private final static String ID = " id ";
    private final static String TABLE_ID = ".id";
    private final static String EQUAL = " = ";
    private final static String AND = " AND ";
    private final static String UPDATE = "UPDATE ";
    private final static String SET = " SET ";
    private final static String DELETE = "DELETE ";
    private final static String LIKE = " LIKE ";
    private final static String INNER_JOIN = " INNER JOIN ";
    private final static String ON = " ON ";
    private final static String DOT = ".";
    public final static String GET_ID_MOST_RECENT = "SELECT LAST_INSERT_ROWID();";
    public final static int MOST_RECENT_ID_COL = 1;

    private final static String END_QUERY = ";";
    private final static String QUOTE = "'";
    private final static String PERCENTAGE = "%";
    private final static String COMMA = ", ";

    private QueryBuilder() { }

    //INSERT INTO User VALUES (‘Big’, ‘Boss’, ‘7582 Rue Concordia’, ‘bigboss@hotmail.com’, ‘514-895-9852’, ‘Admin’);
    public static String createSaveQuery(String table, String values) {
        StringBuilder query = new StringBuilder(INSERT_INTO);
        query.append(table);
        query.append(VALUES);
        query.append(values);
        query.append(END_QUERY);
        return query.toString();
    }

    public static String createDoubleTableQuery(String table1, String table2, String idMatcher, String attribute, String attributeValue) {
        return SELECT + table1 + TABLE_ALL + FROM + table1 + COMMA + table2 + WHERE + idMatcher + EQUAL + table2 + TABLE_ID + AND + table2 +
               DOT + attribute + EQUAL + QUOTE + attributeValue + QUOTE + END_QUERY;
    }

    public static String createFindAllQuery(String table) {
        return SELECT + ALL + FROM + table + END_QUERY;
    }

    public static String createFindAllQuery(String table1, String table2, String id, String foreign_id) {
        return SELECT + ALL + FROM + table1 + INNER_JOIN + table2 + ON + table2 + DOT + id + EQUAL + table1 + DOT + foreign_id + END_QUERY;
    }

    public static String createFindByIdQuery(String table, Long id) {
        return SELECT + ALL + FROM + table + WHERE + ID + EQUAL + id + END_QUERY;
    }

    public static String createFindByIdQuery(String table, String attribute, String value) {
        return SELECT + ALL + FROM + table + WHERE + attribute + EQUAL + QUOTE + value + QUOTE + END_QUERY;
    }

    public static String createFindByAttributeQuery(String table, String attribute, String value) {
        return SELECT + ALL + FROM + table + WHERE + attribute + EQUAL + QUOTE + value + QUOTE + END_QUERY;
    }

    public static String createSearchByAttributeQuery(String table, String attribute, String value) {
        return SELECT + ALL + FROM + table + WHERE + attribute + LIKE + QUOTE + PERCENTAGE + value + PERCENTAGE + QUOTE + END_QUERY;
    }

    public static String createSearchByAttributeQuery(String table, String attribute, Date value) {
        return SELECT + ALL + FROM + table + WHERE + attribute + LIKE + QUOTE + PERCENTAGE + value + PERCENTAGE + QUOTE + END_QUERY;
    }

    public static String createSearchByAttributeQuery(String table, String attribute, Long value) {
        return SELECT + ALL + FROM + table + WHERE + attribute + LIKE + QUOTE + PERCENTAGE + value + PERCENTAGE + QUOTE + END_QUERY;
    }

    public static String createSearchByAttributesQuery(String table, Map<String, String> attributeValue) {
        StringBuilder query = new StringBuilder(SELECT + ALL + FROM + table + WHERE);

        int counter = 0;
        for (String attribute : attributeValue.keySet()) {
            if (counter != 0) {
                query.append(AND);
            }
            query.append(attribute);
            query.append(LIKE + QUOTE + PERCENTAGE);
            query.append(attributeValue.get(attribute));
            query.append(PERCENTAGE + QUOTE);
            counter++;
        }
        query.append(END_QUERY);

        return query.toString();
    }


    public static String createUpdateQuery(String table, String updatedValues, Long id) {
        return UPDATE + table + SET + updatedValues + WHERE + ID + EQUAL + id + END_QUERY;
    }

    public static String createDeleteQuery(String table, Long id) {
        return DELETE + FROM + table + WHERE + ID + EQUAL + id + END_QUERY;
    }

    public static String createDeleteQuery(String table, String attribute, String value) {
        return DELETE + FROM + table + WHERE + attribute + EQUAL + QUOTE + value + QUOTE + END_QUERY;
    }

    public static String createDeleteQuery(String table, String attribute1, String value1, String attribute2, String value2) {
        return DELETE + FROM + table + WHERE + attribute1 + EQUAL + QUOTE + value1 + QUOTE + AND + attribute2 + EQUAL + QUOTE + value2 +
               QUOTE + END_QUERY;
    }

}
