package be.fluid_it.tools.dw.wiz2war.hooks;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.LinkedList;
import java.util.List;

public class ServletContextHook implements ServletContextListener {
    private static ServletContext theServletContext;
    private final List<Destroyable> destroyables = new LinkedList<Destroyable>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        theServletContext = sce.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        synchronized (destroyables) {
            for (Destroyable destroyable : destroyables) {
                destroyable.destroy();
            }
        }
    }

    public void registerDestroyable(Destroyable destroyable) {
        synchronized (destroyables) {
            destroyables.add(destroyable);
        }
    }
}
