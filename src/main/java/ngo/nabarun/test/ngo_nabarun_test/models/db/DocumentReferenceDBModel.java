package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("document_references")
public class DocumentReferenceDBModel {
    private String id;
    private String fileName;
    private String remotePath;
    private String publicToken;
    private String contentType;
    private Integer fileSize;
    private Boolean isPublic;
    private Date createdAt;
    private String uploadedById;
}
