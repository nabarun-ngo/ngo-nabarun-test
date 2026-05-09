package ngo.nabarun.test.ngo_nabarun_test.helpers;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.ProjectDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.ExpenseDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.DonationDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.TicketDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.AccountDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.MeetingDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.ProjectTeamMemberDBModel;
import ngo.nabarun.test.ngo_nabarun_test.repository.DonationDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.TicketDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.UserDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.ProjectDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.ExpenseDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.AccountDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.MeetingDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.ProjectTeamMemberDAO;
import ngo.nabarun.test.ngo_nabarun_test.repository.WorkflowDAO;
import ngo.nabarun.test.ngo_nabarun_test.utils.DBUtils;

public class DataProvider {
	private static final Logger logger = LogManager.getLogger(DataProvider.class);

	public UserDBModel findUserByName(String firstName, String lastName) {
		try {
			return DBUtils.getJdbi().withExtension(UserDAO.class, dao -> dao.findByName(firstName, lastName));
		} catch (Exception e) {
			logger.error("Error finding user by name", e);
			return null;
		}
	}

	public List<DonationDBModel> findDonationsBetweenDates(Date startDate, Date endDate, String profileId,
			String type) {
		try {
			return DBUtils.getJdbi().withExtension(DonationDAO.class,
					dao -> dao.findBetweenDates(startDate, endDate, profileId, type));
		} catch (Exception e) {
			logger.error("Error finding donations between dates", e);
			throw new RuntimeException("Error finding donations between dates", e);
		}
	}

	public boolean deleteDonationById(String donationId) {
		try {
			return DBUtils.getJdbi().withExtension(DonationDAO.class, dao -> dao.deleteById(donationId)) > 0;
		} catch (Exception e) {
			logger.error("Error deleting donation by id", e);
			return false;
		}
	}

	public TicketDBModel findOTPDetails(String email) {
		try {
			return DBUtils.getJdbi().withExtension(TicketDAO.class, dao -> dao.findFirstByEmail(email));
		} catch (Exception e) {
			logger.error("Error finding OTP details", e);
			return null;
		}
	}

	public List<UserDBModel> getUsersByRole(String roleCode) {
		try {
			return DBUtils.getJdbi().withExtension(UserDAO.class, dao -> dao.findByRole(roleCode));
		} catch (Exception e) {
			logger.error("Error finding users by role", e);
			return null;
		}
	}

	public List<ProjectDBModel> getProjectsByManagerEmail(String email) {
		try {
			return DBUtils.getJdbi().withExtension(ProjectDAO.class, dao -> dao.findByManagerEmail(email));
		} catch (Exception e) {
			logger.error("Error finding projects by manager email", e);
			return null;
		}
	}

	public List<ExpenseDBModel> getExpensesByProjectCode(String projectCode) {
		try {
			return DBUtils.getJdbi().withExtension(ExpenseDAO.class, dao -> dao.findByProjectCode(projectCode));
		} catch (Exception e) {
			logger.error("Error finding expenses by project code", e);
			return null;
		}
	}

	public List<AccountDBModel> getAccountsByHolderEmail(String email) {
		try {
			return DBUtils.getJdbi().withExtension(AccountDAO.class, dao -> dao.findByHolderEmail(email));
		} catch (Exception e) {
			logger.error("Error finding accounts by holder email", e);
			return null;
		}
	}

	public List<MeetingDBModel> getMeetingsByProjectCode(String projectCode) {
		try {
			return DBUtils.getJdbi().withExtension(MeetingDAO.class, dao -> dao.findByProjectCode(projectCode));
		} catch (Exception e) {
			logger.error("Error finding meetings by project code", e);
			return null;
		}
	}

	public List<ProjectTeamMemberDBModel> getTeamMembersByUserEmail(String email) {
		try {
			return DBUtils.getJdbi().withExtension(ProjectTeamMemberDAO.class, dao -> dao.findByUserEmail(email));
		} catch (Exception e) {
			logger.error("Error finding project team members by user email", e);
			return null;
		}
	}

	public int getAssignmentCountByWorkflowId(String workflowId, String userId) {
		try {
			return DBUtils.getJdbi().withExtension(WorkflowDAO.class,
					dao -> dao.getAssignmentCountByWorkflowId(workflowId, userId));
		} catch (Exception e) {
			logger.error("Error getting assignment count by workflow id", e);
			throw e;
		}
	}

	public UserDBModel getUserByEmail(String email) {
		try {
			return DBUtils.getJdbi().withExtension(UserDAO.class, dao -> dao.findByEmail(email));
		} catch (Exception e) {
			logger.error("Error finding user by email", e);
			return null;
		}
	}

}
