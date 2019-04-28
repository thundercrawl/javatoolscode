package bg.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;



public class Tools
{

   public static void sentMail(String to, String msg)
   {

      // Sender's email ID needs to be mentioned
      String from = "admin@cttl1.com";

      // Assuming you are sending email from localhost
      String host = "localhost";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try
      {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("This is a reset account mail, Donot respond it");

         // Now set the actual message
         message.setText(msg);

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
         Logger.getLogger(Tools.class).info("send mail to ,addr=" + to);
      }
      catch (MessagingException mex)
      {
         mex.printStackTrace();
      }
   }

   public static Map<String, String> getFileSizeMbytesInDirectory(String path)
   {
      Map<String, String> contents = new HashMap<String, String>();
      File dir = new File(path);
      if (!dir.exists()) return contents;

      File[] files = dir.listFiles();
      for (File f : files)
      {
         if (f.isDirectory()) continue;

         contents.put(f.getName(), new Float(f.length() / 1024 / 1024).toString());
      }

      return contents;

   }

   public static Process WaitForProcessExit(String pathtoexe)
   {
      // Create ProcessBuilder.
      ProcessBuilder p = new ProcessBuilder();

      Process ps = null;
      p = p.command(pathtoexe);
      try
      {
         ps = p.start();
         Thread.sleep(2000);
         return ps;
      }
      catch (IOException e2)
      {
         Logger.getLogger(Tools.class).error("failed to get the process path=" + pathtoexe);
         e2.printStackTrace();
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      catch (Exception e)
      {
         Logger.getLogger(Tools.class).error("failed to create process, path=" + pathtoexe + " error="
               + e.getMessage());
      }

      return null;
   }

   public static String getHostName()
   {

      InetAddress netAddress;
      String host = null;
      try
      {
         netAddress = InetAddress.getLocalHost();
         host = netAddress.getHostName();
      }
      catch (UnknownHostException e)
      {
         Logger.getLogger(Tools.class).error("Unknown host got ,set to host name");
         host = "UnKnownHost";
      }
      finally
      {

         return host;

      }
   }

   public static String getIP()
   {
      InetAddress netAddress;
      String ip = null;
      try
      {
         netAddress = InetAddress.getLocalHost();
         ip = netAddress.getHostAddress();
      }
      catch (Exception e)
      {
         Logger.getLogger(Tools.class).error("Unknown host ip got ,set to host name");
         ip = "UnKnownHostip";
      }
      finally
      {
         return ip;

      }

   }

   public static String createHttpHostURL()
   {
      String header = "http://";
      String ip = getIP();

      return header + ip + "/";
   }

   public static String createURLByIP(String ip)
   {
      String header = "http://";
      return header + ip + "/";
   }

   public static void clearFileContent(File f)
   {
      PrintWriter writer;
      try
      {
         writer = new PrintWriter(f);
         writer.print("");
         writer.close();
         Logger.getLogger(Tools.class).info("delete contents finish");
      }
      catch (FileNotFoundException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   public static String getRandomString(int length)
   {

      String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
      StringBuilder salt = new StringBuilder();
      Random rnd = new Random();
      while (salt.length() < length)
      { // length of the random string.
         int index = (int) (rnd.nextFloat() * SALTCHARS.length());
         salt.append(SALTCHARS.charAt(index));
      }
      String saltStr = salt.toString();

      return saltStr;

   }

   public static String getCurrentDir()
   {
      return System.getProperty("user.dir");
   }

   public static void main(String[] args)
   {
      sentMail(args[0], args[1]);
   }

}
