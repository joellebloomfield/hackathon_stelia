package com.altran.acs.predix.labs.data.jpa.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;

import com.altran.acs.predix.labs.data.jpa.domain.Location;
import com.altran.acs.predix.labs.data.jpa.domain.Sensor;
import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint(value = "/wss/{nodeId}")
public class WebSocketServerEndPoint {

	private static Logger logger = LoggerFactory.getLogger(WebSocketServerEndPoint.class);
	private Connection dbConnection;
	private DataSource dataSource;
	@Autowired  private SensorService sensorService;

	/**
	 * @param nodeId1
	 *            - nodeId for the session
	 * @param session
	 *            - session object
	 * @param ec
	 *            -
	 * @throws SQLException
	 */

	public DataSource getDbConnection() throws SQLException {
			CloudFactory cloudFactory = new CloudFactory();
			Cloud cloud = cloudFactory.getCloud();
			// List<ServiceInfo> serviceInfo = cloud.getServiceInfos();
			// for (ServiceInfo si : serviceInfo) {
			// System.out.println("id: " + si.getId());
			// System.out.println("All: " + si.toString());
			// }

			// Please use postgres Id from manifest.yml file

			return cloud.getServiceConnector("posture-postgres2", DataSource.class, null);

	}

	@PostConstruct
	public void init() throws SQLException {

	}

	@OnOpen
	public void onOpen(@PathParam(value = "nodeId") String nodeId, final Session session, EndpointConfig ec) {
		logger.info("Server: opened... for Node Id : " + nodeId + " : " + session.getId());
		
	}

	@OnMessage
	public void onMessage(@PathParam(value = "nodeId") String nodeId, String message, Session session)
			throws SQLException, IOException {
		logger.info("*** Websocket Message : " + message);

		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			Sensor sensor = objectMapper.readValue(message, Sensor.class);
			
			System.out.println("*** Open Connection");
			
			if (dbConnection == null || dbConnection.isClosed()) {
				dataSource = getDbConnection();
				dbConnection = dataSource.getConnection();
			}
			
			System.out.println("*** Open Connection Done: " + dbConnection.getClientInfo());
			
			//if (sensor.getTmStmp() == null) sensor.setTimestamp(new Date());

			String insertTableSQL = "INSERT INTO sensor(id, deviceId, tmStmp, name, val) values(nextval( 'hibernate_sequence'),"
			       + sensor.getDeviceId()
			       + ", "	+"'" + sensor.getTmStmp()+ "'"
			       + ", " + "'" + sensor.getName() + "'"
			+ ", " + sensor.getVal() +")";
						
			

			//sensorService.createSensor(sensor);

			//System.out.println("*** run statement: " + insertTableSQL);
			Statement statement = dbConnection.createStatement();

			System.out.println(insertTableSQL); 

			// execute insert SQL stetement
			statement.executeUpdate(insertTableSQL);

			System.out.println("*** Record is inserted into Sensor table!");

		} catch (SQLException | IOException e) {

			System.out.println(e.getMessage());
			session.getBasicRemote().sendText(e.getMessage());

		} 
		
		String response = "{\"messageId\": " + nodeId + ",\"statusCode\": 202}"; 
		session.getBasicRemote().sendText(response);

	}

	/**
	 * @param session
	 *            - session object
	 * @param closeReason
	 *            - The reason of close of session
	 */
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info("Server: Session " + session.getId() + " closed because of " + closeReason.toString()); 
	}

	/**
	 * @param session
	 *            - current session object
	 * @param t
	 *            - Throwable instance containing error info
	 */
	@OnError
	public void onError(Session session, Throwable t) {
		logger.error("Server: Session " + session.getId() + " closed because of " + t.getMessage());
	}
}
