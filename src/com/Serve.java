package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

@SuppressWarnings("serial")
public class Serve extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
            BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
            blobstoreService.serve(blobKey, res);
            res.setHeader("Content-Disposition", "attachment; filename='Google App'");
            
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            String msgBody = "Hi, \n\n I have shared the file with you\n follow the link below\n"
            		+ "https://appengine.google.com/blobstore/detail?app_id=s~alokrai1312&key="+blobKey.getKeyString()+"\n\nThanks & Regards,\n Admin";

            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("cloudatuta@gmail.com", "cloudatuta Admin"));
                msg.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress("deven.demon@gmail.com", "Mr. Deven"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress("divendar.murtadkar@gmail.com", "Mr. User"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress("divendar.murtadak@gmail.com", "Mr. User"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress("rampal.shamlal@gmail.com", "Mr. User"));
                msg.setSubject("Google App Engine File share");
                msg.setText(msgBody);
                Transport.send(msg);

            } catch (AddressException e) {
                // ...
            } catch (MessagingException e) {
                // ...
            }
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            out.println("<h2>email sent to 4 users to access this url</h2>");
            RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
            rd.forward(req, res);
            
        }
}