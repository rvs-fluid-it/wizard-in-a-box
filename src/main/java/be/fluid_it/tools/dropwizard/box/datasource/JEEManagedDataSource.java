package be.fluid_it.tools.dropwizard.box.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import io.dropwizard.db.ManagedDataSource;

/**
 * A {@link DataSource} configured in JEE Container available through JNDI.
 */
public class JEEManagedDataSource implements ManagedDataSource {
    private DataSource jeeDatasource;

    public JEEManagedDataSource(String datasourcesJndiKey, String dataSourceName) throws NamingException {
        Context envCtx = null;
        if (datasourcesJndiKey != null) {
          InitialContext ic = new InitialContext();
          envCtx = (Context) ic.lookup(datasourcesJndiKey);
        } else {
          Hashtable<String, String> env = new Hashtable<String, String>();
          if (runningOnWeblogic()) {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
          }
          envCtx = new InitialContext(env);
        }
        jeeDatasource = (DataSource) envCtx.lookup(dataSourceName);
    }

  private boolean runningOnWeblogic() {
    try {
      Class<?> weblogicServerClass = Class.forName("weblogic.Server");
      return weblogicServerClass != null;
    } catch (ClassNotFoundException e) {
      return false;
    }
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
