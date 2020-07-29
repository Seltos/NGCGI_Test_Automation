package com.enquero.driverfactory.web_selenium.actions.db;

import com.enquero.driverfactory.web_selenium.base.TestAction;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * An action that executes a SQL query statement against a database using JDBC.
 */
public class JdbcQuery extends TestAction {

    @Override
    public void run() {
        String jdbcUrl = this.readStringArgument("jdbcUrl");
        String user = this.readStringArgument("user", null);
        String password = this.readStringArgument("password", null);
        String sql = this.readStringArgument("sql");

        try {
            Connection conn;

            if (user != null && password != null) {
                conn = DriverManager.getConnection(jdbcUrl, user, password);
            } else {
                conn = DriverManager.getConnection(jdbcUrl);
            }

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsMetaData = resultSet.getMetaData();

            List<Map<String, Object>> rows = new ArrayList<>();
            int columnCount = rsMetaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int columnNo = 1; columnNo <= columnCount; columnNo++) {
                    switch (rsMetaData.getColumnType(columnNo)) {
                        case Types.DATE:
                        case Types.TIME:
                        case Types.TIME_WITH_TIMEZONE:
                        case Types.TIMESTAMP:
                        case Types.TIMESTAMP_WITH_TIMEZONE:
                            row.put(rsMetaData.getColumnName(columnNo), resultSet.getObject(columnNo).toString());
                            break;
                        default:
                            row.put(rsMetaData.getColumnName(columnNo), resultSet.getObject(columnNo));
                    }
                }
                rows.add(row);
            }
            resultSet.close();

            this.writeOutput("rows", rows);
            this.writeOutput("rowCount", rows.size());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}