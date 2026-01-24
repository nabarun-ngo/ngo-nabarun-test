package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("notices")
public class NoticeDBModel {
    private String id;
    private String title;
    private String description;
    private String status;
    private Date noticeDate;
    private Date publishDate;
    private Boolean hasMeeting;
    private String creatorId;
    private String creatorRoleCode;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
