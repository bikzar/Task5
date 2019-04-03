package by.epam.training.javaweb.voitenkov.task5.model.entity;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.training.javaweb.voitenkov.task5.model.entity.comparator.TrainComparator;
import by.epam.training.javaweb.voitenkov.task5.model.entity.exception.IllegalDepartureTimeExcepion;

/**
 * @author Sergey Voitenkov Mar 26, 2019
 */

public class Platform {

	// rewrite get inform from xml file
	private static AtomicInteger trainNumberNearThePlatform;
	private Set<Train> trainList;
	private volatile int platformTime;

	private static Logger LOGGER;

	static {
		LOGGER = LogManager.getLogger(); // "Platform Logger"
	}

	private Platform() {
		trainNumberNearThePlatform = new AtomicInteger(0);
		trainList = new ConcurrentSkipListSet<Train>(new TrainComparator());
		new PlatformClock();
	}

	private static class InstanceHelper {
		private static Platform instance = new Platform();
	}

	public static Platform getInstance() {
		return InstanceHelper.instance;
	}

	public int getPlatformTime() {
		return platformTime;
	}

	public Set<Train> getTrainList() {
		return trainList; // return copy
	}

	public boolean arriveToPlatform(Train train) {

		boolean resualt = false;

		LOGGER.debug("Train " + train.getTrainNumber() + " (with direction: "
				+ train.getDirection() + ")" + " try to arrived To Platform");

		if (trainList.add(train)) {

			try {
				train.setDepartureTime(platformTime + train.getWaitingTime());
			} catch (IllegalDepartureTimeExcepion e) {
				LOGGER.warn(
						"Try to set illegal time val in arriveToPlatform methood");
			}

			LOGGER.debug("Train " + train.getTrainNumber() + " (with direction: "
					+ train.getDirection() + ")"
					+ " arrived to the platform with direction: "
					+ train.getDirection() + ". It has " + train.getFreeSeats()
					+ " free seats");

			trainNumberNearThePlatform.incrementAndGet();

			resualt = true;
		}

		return resualt;
	}

	public void leavePlatform(Train train) {

		LOGGER.debug("Train " + train.getTrainNumber() + " i leave at: "
				+ platformTime);

		trainList.remove(train);
	}

	private class PlatformClock implements Runnable {

		Thread thread;

		public PlatformClock() {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}

		@Override
		public void run() {
			while (true) {
				try {
					platformTime++;
					LOGGER.debug("Platform time: " + platformTime);
					Thread.sleep(1L);
				} catch (InterruptedException e) {
					LOGGER.warn(
							"Interrupted exception in PlatformClock class. Methood run().",
							e);
				}
			}
		}

	}
}
