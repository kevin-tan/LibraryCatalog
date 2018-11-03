package com.soen343.project.database.query;

/**
 * Created by Kevin Tan 2018-09-24
 */


public class QueryBuilder {

    private final static String INSERT_INTO = "INSERT INTO ";
    private final static String SELECT = "SELECT ";
    private final static String ALL = " * ";
    private final static String FROM = " FROM ";
    private final static String VALUES = " VALUES ";
    private final static String WHERE = " WHERE ";
    private final static String ID = " id ";
    private final static String EQUAL = " = ";
    private final static String AND = " AND ";
    private final static String UPDATE = "UPDATE ";
    private final static String SET = " SET ";
    private final static String DELETE = "DELETE ";
    public final static String GET_ID_MOST_RECENT = "SELECT LAST_INSERT_ROWID();";
    public final static int MOST_RECENT_ID_COL = 1;

    private final static String END_QUERY = ";";
    private final static String COMMA = ",";
    private final static String QUOTE = "'";

    private QueryBuilder() { }

    //INSERT INTO User VALUES (‘Big’, ‘Boss’, ‘7582 Rue Concordia’, ‘bigboss@hotmail.com’, ‘514-895-9852’, ‘Admin’);
    public static String createSaveQuery(String table, String... values) {
        StringBuilder query = new StringBuilder(INSERT_INTO);
        query.append(table);
        query.append(VALUES);
        for (int i = 0; i < values.length; i++) {
            query.append(values[i]);
            if (i < values.length - 1) query.append(COMMA);
        }
        query.append(END_QUERY);
        return query.toString();
    }


    public static String createFindAllQuery(String table) {
        return SELECT + ALL + FROM + table + END_QUERY;
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
        return DELETE + FROM + table + WHERE + attribute1 + EQUAL + QUOTE + value1 + QUOTE +
                AND + attribute2 + EQUAL + QUOTE + value2 + QUOTE + END_QUERY;
    }
}
