package locale;

import java.util.*;

public class LocaleManager {
	private ResourceBundle messages;

	public LocaleManager() {
		Locale selectedLocale = Locale.getDefault();
		messages = ResourceBundle.getBundle("messages", selectedLocale);
	}

	public void chooseLocale(int choice) {
		Locale selectedLocale;
		switch (choice) {
			case 1:
				selectedLocale = new Locale("en");
				break;
			case 2:
				selectedLocale = new Locale("en", "US");
				break;
			default:
				selectedLocale = Locale.getDefault();
		};
		messages = ResourceBundle.getBundle("messages", selectedLocale);
	}

	public String get(String key) {
		return messages.getString(key);
	}
}
