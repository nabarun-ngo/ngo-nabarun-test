package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("document_mappings")
public class DocumentMappingDBModel {
    private String id;
    private String documentReferenceId;
    private String entityType;
    private String entityId;
    private Date createdAt;
}
