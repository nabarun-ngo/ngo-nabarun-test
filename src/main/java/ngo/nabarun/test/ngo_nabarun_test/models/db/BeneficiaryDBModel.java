package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("beneficiaries")
public class BeneficiaryDBModel {
    private String id;
    private String projectId;
    private String name;
    private String type;
    private String gender;
    private Integer age;
    private Date dateOfBirth;
    private String contactNumber;
    private String email;
    private String address;
    private String location;
    private String category;
    private Date enrollmentDate;
    private Date exitDate;
    private String status;
    private List<String> benefitsReceived;
    private String notes;
    private Map<String, Object> metadata;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
