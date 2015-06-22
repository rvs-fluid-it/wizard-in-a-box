package be.fluid_it.tools.dropwizard.box.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import io.dropwizard.db.ManagedDataSource;

/**
 * A {@link DataSource} configured in JEE Container available through JNDI
 * under java:comp/env context.
 */
public class JEEManagedDataSource implements ManagedDataSource {
    private DataSource jeeDatasource;

    public JEEManagedDataSource(String dataSourceName) throws NamingException {
        InitialContext ic = new InitialContext();
        Context envCtx = (Context) ic.lookup("java:comp/env");
        jeeDatasource = (DataSource) envCtx.lookup(dataSourceName);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return jeeDatasource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return jeeDatasource.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return jeeDatasource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        jeeDatasource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        jeeDatasource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return jeeDatasource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return jeeDatasource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return jeeDatasource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return jeeDatasource.isWrapperFor(iface);
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        // We can't close DataSource here.
        // Any good container should support closing Resource objects when not
        // required
        // In Tomcat define closeMethod="close" in Resource configuration
    }

}
