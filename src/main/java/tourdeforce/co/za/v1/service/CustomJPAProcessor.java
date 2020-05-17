package tourdeforce.co.za.v1.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetMediaResourceUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.api.uri.info.PutMergePatchUriInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.core.ODataJPAProcessorDefault;

import tourdeforce.co.za.v1.model.Player;

public class CustomJPAProcessor extends ODataJPAProcessorDefault {
	private static final Charset encoding = UTF_8;
	private static final String PERSISTENCE_UNIT_NAME = "TourDeForce_olingo_jpa_v1";
	private ODataJPAContext oDataJPAContext;

	public CustomJPAProcessor(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
		this.oDataJPAContext = oDataJPAContext;
	}

//	public CustomJPAProcessor(ODataContext oDataContext){
//        setContext(oDataContext);
//}

	@Override
	public ODataResponse readEntitySet(final GetEntitySetUriInfo uriParserResultView, final String contentType)
			throws ODataException {
		List<Object> jpaEntities = jpaProcessor.process(uriParserResultView);
		ODataResponse oDataResponse = responseBuilder.build(uriParserResultView, jpaEntities, contentType);
		return oDataResponse;
	}

	private static BufferedImage convert(BufferedImage src) {
		BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(src, 0, 0, null);
		g2d.dispose();
		return img;
	}

	@Override
	public ODataResponse updateEntityMedia(final PutMergePatchUriInfo uriInfo, final InputStream content,
			final String requestContentType, final String contentType) throws ODataException {
		if (uriInfo.getStartEntitySet().getEntityType().getName().equals("Player")) {
			final FileUploadBase fileUploadBase = new PortletFileUpload();
			final FileItemFactory fileItemFactory = new DiskFileItemFactory();
			fileUploadBase.setFileItemFactory(fileItemFactory);
			fileUploadBase.setHeaderEncoding(encoding.displayName());
			HttpServletRequest req = (HttpServletRequest) this.oDataJPAContext.getODataContext()
					.getParameter(ODataContext.HTTP_SERVLET_REQUEST_OBJECT);
			try {
				final List<FileItem> fileItems = fileUploadBase.parseRequest(req);
				BufferedImage image = ImageIO.read(fileItems.get(0).getInputStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(convert(image), "jpg", out);

				final byte[] photo = out.toByteArray();// EntityProvider.readBinary(content);
				String mimeType = "image/jpeg";
//			try {
//				mimeType = URLConnection.guessContentTypeFromStream(content);
//			}catch(IOException ioe) {
//				mimeType = "images/jpeg";
//			}
				String email = uriInfo.getKeyPredicates().get(0).getLiteral();

				EntityManagerFactory emf = this.oDataJPAContext.getEntityManagerFactory();
				EntityManager em = emf.createEntityManager();
				em.getTransaction().begin();
				Player player = new Player();
				Player foundPlayer = em.find(player.getClass(), email);
				foundPlayer.setPhoto(photo);
				foundPlayer.setPhotoMimeType(mimeType);
				em.merge(foundPlayer);
				em.getTransaction().commit();
				return ODataResponse.newBuilder().eTag("").build();
			} catch (Exception e) {
				return null;
			} finally {
				close();
			}
		}else {
			return super.updateEntityMedia(uriInfo, content, requestContentType, contentType);
		}
	}

	@Override
	public ODataResponse readEntityMedia(final GetMediaResourceUriInfo uriInfo, final String contentType)
			throws ODataException {
		if (uriInfo.getStartEntitySet().getEntityType().getName().equals("Player")) {
			String email = uriInfo.getKeyPredicates().get(0).getLiteral();
			EntityManagerFactory emf = this.oDataJPAContext.getEntityManagerFactory();
			EntityManager em = emf.createEntityManager();
			Player player = new Player();
			Player foundPlayer = em.find(player.getClass(), email);
			final byte[] photo = foundPlayer.getPhoto();
			String mimeType = foundPlayer.getPhotoMimeType();
			return ODataResponse.fromResponse(EntityProvider.writeBinary(mimeType, photo))
					.header("Content-Disposition", "inline; filename=\"entity.jpg\"")
					.header("content-length", Integer.toString(photo.length)).build();
//			return EntityProvider.writeBinary(mimeType, photo);
		} else {
			return super.readEntityMedia(uriInfo, contentType);
		}
	}

	@Override
	public ODataResponse createEntity(final PostUriInfo uriInfo, final InputStream content,
			final String requestContentType, final String contentType) throws ODataException {
		return super.createEntity(uriInfo, content, requestContentType, contentType);
	}
}