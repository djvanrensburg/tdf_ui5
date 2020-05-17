package tourdeforce.co.za.v1.service;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.apache.olingo.odata2.jpa.processor.api.OnJPAWriteContent;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnDBWriteContent implements OnJPAWriteContent {
	final static Logger LOGGER = LoggerFactory.getLogger(OnDBWriteContent.class);
	
	@Override
	public Blob getJPABlob(byte[] binaryData) throws ODataJPARuntimeException {
		try {

			LOGGER.error("IN getJPABlob");
			return new org.hsqldb.jdbc.JDBCBlob(binaryData);
		} catch (SerialException e) {
			ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
		} catch (SQLException e) {
			ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
		}
		return null;
	}

	@Override
	public Clob getJPAClob(char[] characterData) throws ODataJPARuntimeException {
		try {
			LOGGER.error("IN getJPAClob");
			return new org.hsqldb.jdbc.JDBCClob(new String(characterData));
		} catch (SQLException e) {
			ODataJPARuntimeException.throwException(ODataJPARuntimeException.INNER_EXCEPTION, e);
		}
		return null;
	}

}
