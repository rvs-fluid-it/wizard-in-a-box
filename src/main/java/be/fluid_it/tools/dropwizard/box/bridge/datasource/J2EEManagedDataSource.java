package be.fluid_it.tools.dropwizard.box.bridge.datasource;

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
 * A {@link DataSource} configured in J2EE Container available through JNDI
 * under java:comp/env context.
 */
public class J2EEManagedDataSource implements ManagedDataSource {
    private DataSource j2eeDatasource;

    public J2EEManagedDataSource(String dataSourceName) throws NamingException {
        InitialContext ic = new InitialContext();
        Context envCtx = (Context) ic.lookup("java:comp/env");
        j2eeDatasource = (DataSource) envCtx.lookup(dataSourceName);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return j2eeDatasource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return j2eeDatasource.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return j2eeDatasource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        j2eeDatasource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        j2eeDatasource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return j2eeDatasource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return j2eeDatasource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return j2eeDatasource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return j2eeDatasource.isWrapperFor(iface);
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
