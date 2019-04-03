package by.epam.training.javaweb.voitenkov.task5.model.entity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.training.javaweb.voitenkov.task5.model.entity.directiontype.Direction;
import by.epam.training.javaweb.voitenkov.task5.model.entity.exception.IllegalDepartureTimeExcepion;

/**
 * @author Sergey Voitenkov Mar 26, 2019
 */
public class Train implements Runnable {

	private static final int DEFAULT_WAITING_TIME = 1;
	private static final int CAPACITY = 50;

	private static Logger LOGGER;

	private AtomicInteger freeSeats;
	private int trainNumber;
	private int waitingTime;
	private int departureTime;

	private boolean isClean;
	private boolean isChecked;

	private Lock addLock;
	private Lock removeLock;
	private CountDownLatch countDownLatch;

	private Platform platform;
	private Direction direction;
	private List<Passenger> passengerList;
	private Thread thread;

	static {
		LOGGER = LogManager.getRootLogger();
	}

	{
		passengerList = new CopyOnWriteArrayList<Passenger>();
		addLock = new ReentrantLock();
		removeLock = new ReentrantLock();
		waitingTime = DEFAULT_WAITING_TIME;
		countDownLatch = new CountDownLatch(1);

	}

	public Train(boolean isClean, boolean isChecked, Direction direction,
			int trainNumber, int waitingTime, Platform platform) {

		this.isClean = isClean;
		this.isChecked = isChecked;

		if (direction != null) {
			this.direction = direction;
		}

		this.trainNumber = trainNumber;

		if (waitingTime > 0) {
			this.waitingTime = waitingTime + 7;
		}

		if (platform != null) {
			this.platform = platform;
		}

		thread = new Thread(this, String.valueOf(trainNumber));
	}

	public boolean addPassenger(Passenger passenger) {

		boolean resualt = false;

		if (addLock.tryLock()) {

			try {

				if (passenger != null && freeSeats.get() > 0) {
					freeSeats.decrementAndGet();
					resualt = true;
				}

			} finally {
				addLock.unlock();
			}
		}

		return resualt;
	}

	public boolean removePassenger(Passenger passenger) {

		boolean resualt = false;

		if (removeLock.tryLock()) {

			try {

				if (passenger != null && freeSeats.get() <= CAPACITY) {
					freeSeats.incrementAndGet();
					resualt = true;
				}

			} finally {
				removeLock.unlock();
			}
		}

		return resualt;
	}

	public Direction getDirection() {
		return direction;
	}

	public boolean isClean() {
		return isClean;
	}

	public void setClean(boolean isClean) {
		this.isClean = isClean;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public int getTrainNumber() {
		return trainNumber;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public int getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(int departureTime)
			throws IllegalDepartureTimeExcepion {
		if (departureTime >= waitingTime) {
			this.departureTime = departureTime;
		} else {
			throw new IllegalDepartureTimeExcepion("Incorrect departure time");
		}
	}

	public int getFreeSeats() {
		return freeSeats.get();
	}

	public void setPassengerList(List<Passenger> passengerList) {

		if (passengerList != null) {
			this.passengerList = passengerList;
		}

		freeSeats = new AtomicInteger(CAPACITY - passengerList.size());

	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	@Override
	public void run() {

		while (true) {

			if (isChecked == false) {
				LOGGER.debug(
						"Train " + this.getTrainNumber() + " I'm washing!");
				isChecked = true;
			}

			if (isClean == false) {
				LOGGER.debug(
						"Train " + this.getTrainNumber() + " I'm checking");
				isClean = true;
			}

			if (isChecked == true && isClean == true
					&& platform.arriveToPlatform(this)) {

				departureTime = platform.getPlatformTime() + waitingTime;

				countDownLatch.countDown();

				LOGGER.debug("Train " + trainNumber
						+ " (with direction: " + direction + ")"
						+ " arrived at: " + platform.getPlatformTime()
						+ " train should depart at: " + departureTime);

				break;
			} else {
				try {
					LOGGER.debug("Train " + this.getTrainNumber() + " I sleep");
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {

				}
			}
		}

		while (departureTime > platform.getPlatformTime()) {

		}

		platform.leavePlatform(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + (isChecked ? 1231 : 1237);
		result = prime * result + (isClean ? 1231 : 1237);
		result = prime * result + trainNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Train other = (Train) obj;
		if (direction != other.direction) {
			return false;
		}
		if (isChecked != other.isChecked) {
			return false;
		}
		if (isClean != other.isClean) {
			return false;
		}
		if (trainNumber != other.trainNumber) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Train [isClean=" + isClean + ", isChecked=" + isChecked
				+ ", direction=" + direction + ", trainNumber=" + trainNumber
				+ "]";
	}
}
