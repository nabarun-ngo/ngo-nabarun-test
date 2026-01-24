package ngo.nabarun.test.ngo_nabarun_test.models.db;

import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("phone_numbers")
public class PhoneNumberDBModel {
    private String id;
    private String phoneCode;
    private String phoneNumber;
    private Boolean hidden;
    private Boolean primary;
    private String userId;
    private Integer version;
}
