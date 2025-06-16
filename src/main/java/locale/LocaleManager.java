package locale;

import java.util.*;

public class LocaleManager {
	private ResourceBundle messages;

	public LocaleManager() {
		Locale selectedLocale = new Locale("en");
		messages = ResourceBundle.getBundle("messages", selectedLocale);
	}

	public void chooseLocale(int choice) {
		Locale selectedLocale;
		switch (choice) {
			case 2:
				selectedLocale = new Locale("en", "US");
				break;
			default:
				selectedLocale = new Locale("en");
				break;
		};
		messages = ResourceBundle.getBundle("messages", selectedLocale);
	}

	public String get(String key) {
		return messages.getString(key);
	}
}
