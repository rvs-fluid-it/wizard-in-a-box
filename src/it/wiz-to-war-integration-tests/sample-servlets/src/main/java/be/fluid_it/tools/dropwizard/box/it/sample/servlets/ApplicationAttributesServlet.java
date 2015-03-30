package be.fluid_it.tools.dropwizard.box.it.sample.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name="applicationAttributesServlet", urlPatterns={"/attributes"})
public class ApplicationAttributesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        Enumeration<String> attributeNames = servletContext.getAttributeNames();
        PrintWriter out = response.getWriter();
        out.println("Application attributes :");
        while(attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            out.println(attributeName + "=" + servletContext.getAttribute(attributeName));
        }
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

}