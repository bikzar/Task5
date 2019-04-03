package by.epam.training.javaweb.voitenkov.task5.model.entity;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epam.training.javaweb.voitenkov.task5.model.entity.directiontype.Direction;

/**
 * @author Sergey Voitenkov Mar 26, 2019
 */

public class Passenger implements Runnable {

	private static final int DEFAULT_PASSENGER_SPEED = 3;

	private static Logger LOGGER;

	private int passengerSpeed = DEFAULT_PASSENGER_SPEED;
	private int passengerID;

	private Direction passengerDirection;
	private Train train;

	private Thread thread;

	static {
		LOGGER = LogManager.getLogger();
	}

	public Passenger(int passengerSpeed, Direction passengerDirection,
			int passengerID, Train train) {

		if (passengerDirection != null) {
			this.passengerDirection = passengerDirection;
		}

		if (passengerSpeed > 0) {
			this.passengerSpeed = passengerSpeed;
		}

		if (passengerID >= 0) {
			this.passengerID = passengerID;
		}

		if (train != null) {
			this.train = train;
		}

		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();

	}

	public void start() {
		thread.start();
	}

	@Override
	public void run() {

		try {
			train.getCountDownLatch().await();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		if (passengerDirection != train.getDirection()) {

			LOGGER.debug("Passenger: " + passengerID + " " + " (from train "
					+ train.getTrainNumber() + " passenger direct: "
					+ passengerDirection + ") "
					+ " try to seat to another train.");

			Platform platform = train.getPlatform();
			Train newDirectionTrain = null;

			while (train.getDepartureTime() > platform.getPlatformTime()) {

				for (Train nextTrain : platform.getTrainList()) {
					if (passengerDirection == nextTrain.getDirection()) {
						newDirectionTrain = nextTrain;
					}
				}

				if (newDirectionTrain != null) {

					LOGGER.debug("Passenger: " + passengerID + " "
							+ " (from train " + train.getTrainNumber()
							+ " passenger direct: " + passengerDirection + ")"
							+ ", my new train is: "
							+ newDirectionTrain.getTrainNumber()
							+ " (freeseats: " + newDirectionTrain.getFreeSeats()
							+ ")");

					while ((train.getDepartureTime()
							- platform.getPlatformTime()) > passengerSpeed
							&& newDirectionTrain.getDepartureTime() - platform
									.getPlatformTime() > passengerSpeed
							&& newDirectionTrain.getFreeSeats() > 0) {

						LOGGER.debug("Passenger: " + passengerID + " "
								+ " (from train " + train.getTrainNumber()
								+ " passenger direct: " + passengerDirection
								+ ") " + " my train depart at: "
								+ train.getDepartureTime()
								+ " my nextTrain depart at: "
								+ newDirectionTrain.getDepartureTime()
								+ " my speed: " + passengerSpeed);

						if (newDirectionTrain != null
								&& newDirectionTrain.addPassenger(this)) {

							train.removePassenger(this);

							LOGGER.debug("Passenger: " + passengerID + " "
									+ " (from train " + train.getTrainNumber()
									+ " passenger direct: " + passengerDirection
									+ ") " + " removed from train: "
									+ train.getTrainNumber()
									+ " with direction: " + train.getDirection()
									+ " and seat in train: "
									+ newDirectionTrain.getTrainNumber()
									+ " with direction: "
									+ newDirectionTrain.getDirection());
							break;
						}
					}
					break;
				} else {
					LOGGER.debug("Passenger: " + passengerID + " "
							+ " (from train " + train.getTrainNumber()
							+ " passenger direct: " + passengerDirection + ") "
							+ " can't find new train");
					try {
						LOGGER.debug("Passenger: " + passengerID + " "
								+ " (from train " + train.getTrainNumber()
								+ " passenger direct: " + passengerDirection
								+ ") " + " I'm sleeping");
						TimeUnit.MILLISECONDS.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void setTrain(Train train) {
		if (train != null) {
			this.train = train;
		}
	}

	public int getPassengerID() {
		return passengerID;
	}

	public Train getTrain() {
		return train;
	}

	public int getPassengerSpeed() {
		return passengerSpeed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + passengerSpeed;
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
		Passenger other = (Passenger) obj;
		if (passengerSpeed != other.passengerSpeed) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Passenger [passengerSpeed=" + passengerSpeed + ", passengerID="
				+ passengerID + ", passengerDirection=" + passengerDirection
				+ " train ID: " + train.getTrainNumber() + "]";
	}
}
