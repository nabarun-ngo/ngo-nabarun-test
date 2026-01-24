package ngo.nabarun.test.ngo_nabarun_test.models.db;

import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("addresses")
public class AddressDBModel {
    private String id;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String hometown;
    private String zipCode;
    private String state;
    private String district;
    private String country;
    private String addressType;
    private String userId;
    private Integer version;
}
