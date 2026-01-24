package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("user_profiles")
public class UserDBModel {
    private String id;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String about;
    private String picture;
    private String email;
    private Boolean isPublic;
    private String authUserId;
    private String status;
    private Boolean isTemporary;
    private Boolean isSameAddress;
    private String loginMethods;
    private String panNumber;
    private String aadharNumber;
    private Date donationPauseStart;
    private Date donationPauseEnd;
    private BigDecimal donationAmount;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
