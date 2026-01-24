package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("user_roles")
public class UserRoleDBModel {
    private String id;
    private String roleCode;
    private String roleName;
    private String authRoleCode;
    private Boolean isDefault;
    private String userId;
    private Date createdAt;
    private String createdBy;
    private Date expireAt;
    private Integer version;
}
