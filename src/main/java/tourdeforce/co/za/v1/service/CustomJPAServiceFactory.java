package tourdeforce.co.za.v1.service;

import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataErrorCallback;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPATransaction;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.OnJPAWriteContent;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPAErrorCallback;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAAccessFactory;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAFactory;

public abstract class CustomJPAServiceFactory extends ODataServiceFactory {
	public abstract ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException;
	
	private ODataJPAContext oDataJPAContext;
	private ODataContext oDataContext;
	private boolean setDetailErrors = false;
	private OnJPAWriteContent onJPAWriteContent = null;
	private ODataJPATransaction oDataJPATransaction = null;

//	public CustomJPAServiceFactory(ODataJPAContext oDataJPAContext) {
//	      this.oDataJPAContext = oDataJPAContext;
//	}

	@Override
	public ODataService createService(ODataContext ctx) throws ODataException {
		oDataContext = ctx;
		oDataJPAContext = initializeODataJPAContext();
		validatePreConditions();

		ODataJPAFactory factory = ODataJPAFactory.createFactory();
		ODataJPAAccessFactory accessFactory = factory.getODataJPAAccessFactory();

		if (oDataJPAContext.getODataContext() == null) {
			oDataJPAContext.setODataContext(ctx);
		}
//		OnDBWriteContent onDBWriteContent = new OnDBWriteContent();
//		setOnWriteJPAContent(onDBWriteContent);

		ODataSingleProcessor oDataJPAprocessor = new CustomJPAProcessor(oDataJPAContext);
		EdmProvider edmProvider = accessFactory.createJPAEdmProvider(oDataJPAContext);
		return createODataSingleProcessorService(edmProvider, oDataJPAprocessor);
	}

	/**
	 * @return an instance of type {@link ODataJPAContext}
	 * @throws ODataJPARuntimeException
	 */
	public final ODataJPAContext getODataJPAContext() throws ODataJPARuntimeException {
		if (oDataJPAContext == null) {
			oDataJPAContext = ODataJPAFactory.createFactory().getODataJPAAccessFactory().createODataJPAContext();
		}
		if (oDataContext != null) {
			oDataJPAContext.setODataContext(oDataContext);
		}
		return oDataJPAContext;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ODataCallback> T getCallback(final Class<T> callbackInterface) {
		if (setDetailErrors == true) {
			if (callbackInterface.isAssignableFrom(ODataErrorCallback.class)) {
				return (T) new ODataJPAErrorCallback();
			}
		}
		if (onJPAWriteContent != null) {
			if (callbackInterface.isAssignableFrom(OnJPAWriteContent.class)) {
				return (T) onJPAWriteContent;
			}
		}
		if (oDataJPATransaction != null) {
			if (callbackInterface.isAssignableFrom(ODataJPATransaction.class)) {
				return (T) oDataJPATransaction;
			}
		}
		return null;
	}

	/**
	 * The methods sets the context with a callback implementation for JPA provider
	 * specific content. For details refer to
	 * {@link org.apache.olingo.odata2.jpa.processor.api.OnJPAWriteContent}
	 * 
	 * @param onJPAWriteContent is an instance of type
	 *                          {@link org.apache.olingo.odata2.jpa.processor.api.OnJPAWriteContent}
	 */
	protected void setOnWriteJPAContent(final OnJPAWriteContent onJPAWriteContent) {
		this.onJPAWriteContent = onJPAWriteContent;
	}

	/**
	 * The methods sets the context with a callback implementation for JPA
	 * transaction specific content. For details refer to
	 * {@link ODataJPATransaction}
	 * 
	 * @param oDataJPATransaction is an instance of type
	 *                            {@link org.apache.olingo.odata2.jpa.processor.api.ODataJPATransaction}
	 */
	protected void setODataJPATransaction(final ODataJPATransaction oDataJPATransaction) {
		this.oDataJPATransaction = oDataJPATransaction;
	}

	/**
	 * The method sets the context whether a detail error message should be thrown
	 * or a less detail error message should be thrown by the library.
	 * 
	 * @param setDetailErrors takes
	 *                        <ul>
	 *                        <li>true - to indicate that library should throw a
	 *                        detailed error message</li>
	 *                        <li>false - to indicate that library should not throw
	 *                        a detailed error message</li>
	 *                        </ul>
	 * 
	 */
	protected void setDetailErrors(final boolean setDetailErrors) {
		this.setDetailErrors = setDetailErrors;
	}

	private void validatePreConditions() throws ODataJPARuntimeException {

		if (oDataJPAContext.getEntityManagerFactory() == null) {
			throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.ENTITY_MANAGER_NOT_INITIALIZED,
					null);
		}

	}
}
