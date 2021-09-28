package com.ringnull.crazytank.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ringnull.crazytank.crazyTank;

public class DesktopLauncher {
// Для компиляции игры надо: в окне Gradle справа выбрать Tasks->other->dist это запустит сборку билда
// Искать .jar тут : C:\project\crazyTank\desktop\build\libs

	protected final static int WINDOW_WIDTH = 1280;
	protected final static int WINDOW_HEIGHT = 720;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;

		new LwjglApplication(new crazyTank(), config);
	}
}
