package ngo.nabarun.test.ngo_nabarun_test;

import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;

public class TestDemo {
	public static void main(String[] args) {
		DataProvider dataProvider = new DataProvider();
		String firstName = "Leonie";
		String lastName = "Steuber";
		UserDBModel user = dataProvider.findUserByName(firstName, lastName);
		if (user != null) {
			System.out.println("User found: "+user.getId().toString()+" " + user.getFirstName() + " " + user.getLastName());
		} else {
			System.out.println("User not found");
		}
	}
}
