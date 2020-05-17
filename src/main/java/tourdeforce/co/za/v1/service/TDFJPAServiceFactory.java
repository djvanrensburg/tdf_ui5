package tourdeforce.co.za.v1.service;

import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialException;

import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.OnJPAWriteContent;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.hsqldb.jdbc.JDBCBlob;
import org.hsqldb.jdbc.JDBCClob;


public class TDFJPAServiceFactory extends CustomJPAServiceFactory {//ODataJPAServiceFactory {
	private static final String PERSISTENCE_UNIT_NAME = "TourDeForce_olingo_jpa_v1";

	private DataSource ds;
	private EntityManagerFactory emf;

	@Override
	public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {

		Connection connection = null;
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/tdfapp_db");

			Map<String, String> properties = new HashMap();
			this.setDBproperties(properties);
//			properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);

			ODataJPAContext oDatJPAContext = this.getODataJPAContext();
			oDatJPAContext.setDefaultNaming(false);

			oDatJPAContext.setEntityManagerFactory(emf);
			oDatJPAContext.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
			oDatJPAContext.setJPAEdmExtension((JPAEdmExtension)new CustomAnnotationProcessor());
//	            oDatJPAContext.setJPAEdmExtension((JPAEdmExtension) new GetDistFunctImportExt());
			return oDatJPAContext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	 public void setDBproperties(Map<String, String> properties) throws URISyntaxException{
		  properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/tdfapp_db" );
		  properties.put("javax.persistence.jdbc.user", "root" );
		  properties.put("javax.persistence.jdbc.password", "tdfadmin" );
		  properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
		  properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");	
		  
		 
	  }
	 
//	 @Override
//	 public ODataService createODataSingleProcessorService(EdmProvider provider, ODataSingleProcessor processor) {
//	     // TODO Auto-generated method stub
//	     ODataSingleProcessorService service = (ODataSingleProcessorService) super.createODataSingleProcessorService(
//	             provider, processor);
//
//	     return service;
//	 }
//
//	 public class OnDBWriteContent implements OnJPAWriteContent {
//
//	     @Override
//	     public Blob getJPABlob(byte[] binaryData) throws ODataJPARuntimeException {
//	         try {
//	             return new JDBCBlob(binaryData);
//	         } catch (SerialException e) {
//	             ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
//	         } catch (SQLException e) {
//	             ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
//	         }
//	         return null;
//	     }
//
//	     @Override
//	     public Clob getJPAClob(char[] characterData) throws ODataJPARuntimeException {
//	         try {
//	             return new JDBCClob(new String(characterData));
//	         } catch (SQLException e) {
//	             ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
//	         }
//	         return null;
//	     }
//	 }
}
