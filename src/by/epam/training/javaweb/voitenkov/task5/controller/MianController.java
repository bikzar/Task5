package by.epam.training.javaweb.voitenkov.task5.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.epam.training.javaweb.voitenkov.task5.model.entity.Platform;
import by.epam.training.javaweb.voitenkov.task5.utillit.RandomTrainCreator;
import by.epam.training.javaweb.voitenkov.task5.utillit.utillintarface.TrainCreator;

/**
 * @author Sergey Voitenkov Mar 26, 2019
 */
public class MianController {

	public static void main(String[] args) {

		TrainCreator tc = new RandomTrainCreator();

		Platform platform = Platform.getInstance();

		ExecutorService executorService = Executors.newCachedThreadPool();

		for (int i = 0; i < 20; i++) {
			executorService.execute(tc.getTrain(i, platform));
		}

		executorService.shutdown();

	}
}
