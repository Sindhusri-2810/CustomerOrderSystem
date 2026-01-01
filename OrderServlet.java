package com.customer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@SuppressWarnings({ "unused", "serial" })
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {

    Connection con;

    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/customerDB",
                "root", // MySQL username
                "root"      // MySQL password
            );
            System.out.println("Database Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            String name = req.getParameter("customer_name");
            String item = req.getParameter("item");
            int quantity = Integer.parseInt(req.getParameter("quantity"));

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO orders(customer_name, item, quantity) VALUES(?,?,?)"
            );

            ps.setString(1, name);
            ps.setString(2, item);
            ps.setInt(3, quantity);

            ps.executeUpdate();

            out.println("<h3>Order Placed Successfully!</h3>");
            out.println("<p>Customer: " + name + "</p>");
            out.println("<p>Item: " + item + "</p>");
            out.println("<p>Quantity: " + quantity + "</p>");
            out.println("<a href='order.html'>Place Another Order</a>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error occurred while placing order");
        }
    }

    public void destroy() {
        try {
            if (con != null) con.close();
            System.out.println("Database Connection Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}