package ngo.nabarun.test.ngo_nabarun_test.models;
import java.util.List;

import lombok.Data;

@Data
public class User {
    private Boolean presentAndPermanentAddressSame;
    private String id;
    private String initials;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String about;
    private String picture;
    private String pictureBase64;
    private List<String> roles;
    private String roleString;
    private String email;
    private String primaryNumber;
    private List<String> phones;
    private List<String> addresses;
    private List<String> socialMediaLinks;
    private String memberSince;
    private String activeContributor;
    private boolean publicProfile;
    private String userId;
    private String status;
    private String loginMethod;
    private List<String> attributes;

}