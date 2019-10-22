package main;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import views.MainMenu;
import views.View;

/*
 * @author Kevin Saephanh
 * */

public class Main {
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		View currentView = new MainMenu();
		while (currentView != null) {
			currentView = currentView.process();
		}
	}
}
