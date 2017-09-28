package org.transitime.avl;

import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.transitime.avl.PollUrlAvlModule;
import org.transitime.db.structs.AvlReport;
import org.transitime.modules.Module;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONTokener;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.HTTPProxyData;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.select.Elements;

public class GlobalTeamAvlModule extends PollUrlAvlModule {

	private static String avlURL="http://jsonplaceholder.typicode.com/posts/1";
//      private static String avlURL="http://www.rozklady.kiedybus.pl/kombus/dane.json";

        public static String positionsURL="http://iplaner.pl:8081/api/positions/";

        public GlobalTeamAvlModule(String agencyId) {
                super(agencyId);
        }

        @Override
        protected String getUrl() {

                return avlURL;
        }


				  public void SSHClient(String serverIp,int serverPort, String command, String usernameString,String password) throws IOException{
				          System.out.println("inside the ssh function");
				          try
				          {
				              Connection conn = new Connection(serverIp,serverPort);
				              conn.connect();
				              boolean isAuthenticated = conn.authenticateWithPassword(usernameString, password);
				              if (isAuthenticated == false)
				                  throw new IOException("Authentication failed.");
				              ch.ethz.ssh2.Session sess = conn.openSession();
				              sess.execCommand(command);
				              InputStream stdout = new StreamGobbler(sess.getStdout());
				              BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
				              System.out.println("the output of the command is");
				              while (true)
				              {
				                  String line = br.readLine();
				                  if (line == null)
				                      break;
				                  System.out.println(line);
				              }
				              System.out.println("ExitCode: " + sess.getExitStatus());
				              sess.close();
				              conn.close();
				          }
				          catch (IOException e)
				          {
				              e.printStackTrace(System.err);

				          }
				      }
        @Override
        protected void processData(InputStream inp) throws Exception {

	ArrayList<AvlReport> avlReports = new ArrayList<AvlReport>();

	GlobalTeamAvlModule r = new GlobalTeamAvlModule("777");
	r.SSHClient(Credentials.ip,Credentials.port,Credentials.path,Credentials.login,Credentials.pass);
	try {

File fXmlFile = new File("gps.xml");
DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
Document doc = dBuilder.parse(fXmlFile);

//optional, but recommended
//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
doc.getDocumentElement().normalize();

System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

NodeList nList = doc.getElementsByTagName("LocationRecord");

System.out.println("----------------------------");

for (int temp = 0; temp < nList.getLength(); temp++) {

	Node nNode = nList.item(temp);

	System.out.println("\nCurrent Element :" + nNode.getNodeName());

	if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		Element eElement = (Element) nNode;

		System.out.println("Lat : " + eElement.getElementsByTagName("Latitude").item(0).getTextContent());
		System.out.println("Long : " + eElement.getElementsByTagName("Longtitude").item(0).getTextContent());
		System.out.println("TimeOfRecord : " + eElement.getElementsByTagName("TimeOfRecord").item(0).getTextContent());
		System.out.println("Current orient : " + eElement.getElementsByTagName("CurrentOrientation").item(0).getTextContent());
		System.out.println("Vehicle ID: " + eElement.getElementsByTagName("VehicleId").item(0).getTextContent());

		String vehicleId = eElement.getElementsByTagName("VehicleId").item(0).getTextContent();


		Double latitude = Double.parseDouble(eElement.getElementsByTagName("Latitude").item(0).getTextContent());
		Double longitude = Double.parseDouble(eElement.getElementsByTagName("Longtitude").item(0).getTextContent());
		float heading=Float.NaN;
		float speed = Float.NaN;
		String time = eElement.getElementsByTagName("TimeOfRecord").item(0).getTextContent();

						//2016-09-07 17:02:48
		SimpleDateFormat dateformater=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

						Date timestamp=dateformater.parse(time.replace( "T" , " " ));
		Calendar cal = new GregorianCalendar();
		cal.setTime(timestamp);
		cal.add(Calendar.HOUR_OF_DAY, 2);
		//System.out.println("data:"+vehicleId+ timestamp.getTime()+ latitude+longitude+ heading+ speed);

			AvlReport avlReport =
									 new AvlReport("GTavltest", cal.getTime().getTime(), latitude,
																		longitude, heading, speed, "GTavltest");
			processAvlReport(avlReport);
			avlReports.add(avlReport);
	}
}
	} catch (Exception e) {
e.printStackTrace();
	}


return;
}

        /**
         * Just for debugging
         */
        public static void main(String[] args) {
                // Create a WexfordCoachAvlModule for testing
                Module.start("org.transitime.custom.kombus.KombusAvlModule");
        }
}
/*class Device {
	public String id;
	public String name;
}
class Position {
	public String deviceId;
	public String vehicleId;
	public Double latitude;
	public Double longitude;
	public float heading;
	public float speed;
	public Date timestamp;
}
class Credentials {
	public String login;
	public String password;
}*/
