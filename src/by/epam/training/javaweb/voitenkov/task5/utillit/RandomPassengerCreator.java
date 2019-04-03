package by.epam.training.javaweb.voitenkov.task5.utillit;

import java.util.Random;

import by.epam.training.javaweb.voitenkov.task5.model.entity.Passenger;
import by.epam.training.javaweb.voitenkov.task5.model.entity.Train;
import by.epam.training.javaweb.voitenkov.task5.model.entity.directiontype.Direction;
import by.epam.training.javaweb.voitenkov.task5.utillit.utillintarface.PassengerCretator;

/**
 * @author Sergey Voitenkov
 *
 *         Mar 31, 2019
 */
public class RandomPassengerCreator implements PassengerCretator {

	@Override
	public Passenger getPassenger(int passengerID, Train train) {

		Random random = new Random();

		return new Passenger(random.nextInt(5),
				Direction.values()[random.nextInt(4)], passengerID, train);
	}
}
