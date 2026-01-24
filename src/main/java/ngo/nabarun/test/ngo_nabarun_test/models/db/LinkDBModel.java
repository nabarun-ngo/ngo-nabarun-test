package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("links")
public class LinkDBModel {
    private String id;
    private String linkName;
    private String linkType;
    private String linkValue;
    private String userId;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
}
