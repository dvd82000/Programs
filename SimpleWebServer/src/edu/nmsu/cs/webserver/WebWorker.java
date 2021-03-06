package edu.nmsu.cs.webserver;

/**
 * Web worker: an object of this class executes in its own new thread to receive and respond to a
 * single HTTP request. After the constructor the object executes on its "run" method, and leaves
 * when it is done.
 *
 * One WebWorker object is only responsible for one client connection. This code uses Java threads
 * to parallelize the handling of clients: each WebWorker runs in its own thread. This means that
 * you can essentially just think about what is happening on one client at a time, ignoring the fact
 * that the entirety of the webserver execution might be handling other clients, too.
 *
 * This WebWorker class (i.e., an object of this class) is where all the client interaction is done.
 * The "run()" method is the beginning -- think of it as the "main()" for a client interaction. It
 * does three things in a row, invoking three methods in this class: it reads the incoming HTTP
 * request; it writes out an HTTP header to begin its response, and then it writes out some HTML
 * content for the response content. HTTP requests and responses are just lines of text (in a very
 * particular format).
 * 
 * @author Jon Cook, Ph.D.
 *
 **/

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Scanner;

public class WebWorker implements Runnable
{

	private Socket socket;

	/**
	 * Constructor: must have a valid open socket
	 **/
	public WebWorker(Socket s)
	{
		socket = s;
	}

	/**
	 * Worker thread starting point. Each worker handles just one HTTP request and then returns, which
	 * destroys the thread. This method assumes that whoever created the worker created it with a
	 * valid open socket object.
	 **/
	public void run()
	{
		String file;
		System.err.println("Handling connection...");
		try
		{
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			file = readHTTPRequest(is);
			writeContent(os, file);
			os.flush();
			socket.close();
		}
		catch (Exception e)
		{
			System.err.println("Output error: " + e);
		}
		System.err.println("Done handling connection.");
		return;
	}

	/**
	 * Read the HTTP request header.
	 **/
	private String readHTTPRequest(InputStream is)
	{
		String line;
		String file = "";
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		while (true)
		{
			try
			{
				while (!r.ready())
					Thread.sleep(1);
				line = r.readLine();
				
				if(line.contains("GET")) {
					String line_parts[] = line.split(" ");
					file = line_parts[1];
				}
				
				System.err.println("Request line: (" + line + ")");
				if (line.length() == 0)
					break;
			}
			catch (Exception e)
			{
				System.err.println("Request error: " + e);
				break;
			}
		}
		return file;
	}

	/**
	 * Write the HTTP header lines to the client network connection.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param contentType
	 *          is the string MIME content type (e.g. "text/html")
	 **/
	private void writeHTTPHeader(OutputStream os, String contentType, String responseCode) throws Exception
	{
		Date d = new Date();
		DateFormat df = DateFormat.getDateTimeInstance();
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String httpCode = "HTTP/1.1 " + responseCode + "\n";
		os.write(httpCode.getBytes());
		os.write("Date: ".getBytes());
		os.write((df.format(d)).getBytes());
		os.write("\n".getBytes());
		os.write("Server: Jon's very own server\n".getBytes());
		// os.write("Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\n".getBytes());
		// os.write("Content-Length: 438\n".getBytes());
		os.write("Connection: close\n".getBytes());
		os.write("Content-Type: ".getBytes());
		os.write(contentType.getBytes());
		os.write("\n\n".getBytes()); // HTTP header ends with 2 newlines
		return;
	}

	/**
	 * Write the data content to the client network connection. This MUST be done after the HTTP
	 * header has been written out.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param file
	 * 			is a String representing the file/directory being requested
	 **/
	private void writeContent(OutputStream os, String file) throws Exception
	{
		switch(file) {
			case "/":
				file = "/welcome/welcome.html";
				serveHTML(os, file, "text/html");
				break;
			case "/favicon.ico":
				file = "/images/favicon.ico";
				serveImage(os, file, "image/x-icon");
				break;
			default:
				String fileType = file.substring(file.lastIndexOf('.'), file.length());
				
				switch(fileType) {
					case ".html":
						serveHTML(os, file, "text/html");
						break;
					case ".jpg":
						serveImage(os, file, "image/jpg");
						break;
					case ".gif":
						serveImage(os, file, "image/gif");
						break;
					case ".png":
						serveImage(os, file, "image/png");
						break;
				}
		}
	}
	
	/**
	 * Write the data of an HTML file to the OutputStream or return a 404 error if that file does
	 * not exist.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param file
	 * 			is a String representing the file/directory being requested
	 * @param contentType 
	 * 			is the content type of the file being requested
	 **/
	private void serveHTML(OutputStream os, String file, String contentType ) throws Exception {
		try {
			File fileObj = new File("." + file);
			Scanner fileRead = new Scanner(fileObj);

			writeHTTPHeader(os, contentType, "200 OK");
			while(fileRead.hasNextLine()) {
				String line = fileRead.nextLine();
				
				if(line.contains("<cs371date>")) {
					Date d = new Date();
					DateFormat df = DateFormat.getDateTimeInstance();
					line = line.replace("<cs371date>", df.format(d));
				}
				
				if(line.contains("<cs371server>")) {
					line = line.replace("<cs371server>", "Dawson's first server");
				}
				
				os.write(line.getBytes());
			}
		}
		catch(FileNotFoundException e) {
			writeHTTPHeader(os, "text/html", "404 Not Found");
			
			os.write("<html><head></head><body>\n".getBytes());
			os.write("<h3>404<br />".getBytes());
			os.write("Not found</h3>\n".getBytes());
			os.write("</body></html>\n".getBytes());

			System.out.println(e);
		}
	}
	
	
	/**
	 * Write the data of an image file to the OutputStream or return a 404 error if that file does
	 * not exist.
	 * 
	 * @param os
	 *          is the OutputStream object to write to
	 * @param file
	 * 			is a String representing the file/directory being requested
	 * @param contentType 
	 * 			is the content type of the file being requested
	 **/
	private void serveImage(OutputStream os, String file, String contentType ) throws Exception {
		try {
			File fileObj = new File("." + file);
			Scanner fileRead = new Scanner(fileObj);

			writeHTTPHeader(os, contentType, "200 OK");
			os.write(Files.readAllBytes(fileObj.toPath()));
		}
		catch(FileNotFoundException e) {
			writeHTTPHeader(os, "text/html", "404 Not Found");
			
			os.write("<html><head></head><body>\n".getBytes());
			os.write("<h3>404<br />".getBytes());
			os.write("Not found</h3>\n".getBytes());
			os.write("</body></html>\n".getBytes());

			System.out.println(e);
		}
	}

} // end class
