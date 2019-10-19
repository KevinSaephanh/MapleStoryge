package models;

public enum AccountType {
	CHECKING {
		public String toString() {
			return "checking";
		}
	},

	SAVINGS {
		public String toString() {
			return "savings";
		}
	}
}
