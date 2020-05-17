package tourdeforce.co.za.v1.service;

import java.io.InputStream;
import java.util.List;

import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmSchemaView;

public class CustomAnnotationProcessor implements JPAEdmExtension {

	@Override
	public void extendWithOperation(JPAEdmSchemaView view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void extendJPAEdmSchema(JPAEdmSchemaView view) {
		// TODO Auto-generated method stub
		Schema edmSchema = view.getEdmSchema();
		List<EntityType> entityTypeList = edmSchema.getEntityTypes();
		for(EntityType entityType : entityTypeList) {
			if(entityType.getName().equals("Player")) {
				entityType.setHasStream(true);
			}
		}
	}

	@Override
	public InputStream getJPAEdmMappingModelStream() {
		// TODO Auto-generated method stub
		return null;
	}

}
