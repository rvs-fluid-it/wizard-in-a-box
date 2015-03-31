package be.fluid_it.tools.dropwizard.box.it.support;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ContextUrlSupport {
    private static String  theContextUrl;

    public static String contextUrl() {
        if (theContextUrl == null) {
            ServiceLoader<ContextUrlProvider> loader = ServiceLoader.load(ContextUrlProvider.class);
            Iterator<ContextUrlProvider> providerIterator = loader.iterator();
            if (providerIterator != null && providerIterator.hasNext()) {
                theContextUrl = providerIterator.next().contextUrl();
            } else {
                throw new IllegalStateException("No ContextUrlProvider found on classpath ...");
            }
        }
        return theContextUrl;
    }
}
