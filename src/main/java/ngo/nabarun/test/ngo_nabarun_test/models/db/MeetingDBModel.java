package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("meetings")
public class MeetingDBModel {
    private String id;
    private String extMeetingId;
    private String meetingSummary;
    private String meetingDescription;
    private String meetingLocation;
    private Date meetingStartTime;
    private Date meetingEndTime;
    private String meetingRefId;
    private String meetingType;
    private String status;
    private String meetingRemarks;
    private String meetingRefType;
    private String extVideoConferenceLink;
    private String extHtmlLink;
    private String creatorEmail;
    private String extConferenceStatus;
    private String meetingAgenda;
    private String meetingOutcomes;
    private String attendees;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
