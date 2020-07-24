package web_selenium.actions.db;

import web_selenium.base.TestAction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * An action that executes a SQL update statement against a database using JDBC.
 */
public class JdbcUpdate extends TestAction {

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
            int updateCount = statement.executeUpdate(sql);

            this.writeOutput("updateCount", updateCount);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
