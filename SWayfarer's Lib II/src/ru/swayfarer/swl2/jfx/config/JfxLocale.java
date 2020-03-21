package ru.swayfarer.swl2.jfx.config;

import ru.swayfarer.swl2.observable.property.ObservableProperty;

public class JfxLocale {

	public static JfxLocale instance = new JfxLocale(); //JsonUtils.loadFromJson(RLUtils.pkg("locale.json").toSingleString(), JfxLocale.class);
	
	public ObservableProperty<String> infoDialogTitle = new ObservableProperty<>("Info");
	public ObservableProperty<String> warningDialogTitle = new ObservableProperty<>("Warning!");
	public ObservableProperty<String> errorDialogTitle = new ObservableProperty<>("Error!");
}
