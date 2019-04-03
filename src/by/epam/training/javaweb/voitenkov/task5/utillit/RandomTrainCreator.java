package by.epam.training.javaweb.voitenkov.task5.utillit;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import by.epam.training.javaweb.voitenkov.task5.model.entity.Passenger;
import by.epam.training.javaweb.voitenkov.task5.model.entity.Platform;
import by.epam.training.javaweb.voitenkov.task5.model.entity.Train;
import by.epam.training.javaweb.voitenkov.task5.model.entity.directiontype.Direction;
import by.epam.training.javaweb.voitenkov.task5.utillit.utillintarface.TrainCreator;

/**
 * @author sergey
 *
 */
public class RandomTrainCreator implements TrainCreator {

	public RandomTrainCreator() {
	}

	@Override
	public Train getTrain(int trainID, Platform platform) {

		Random random = new Random();

		List<Passenger> passengerList = new CopyOnWriteArrayList<Passenger>();

		RandomPassengerCreator passengerCreator = new RandomPassengerCreator();

		Train train = new Train(random.nextBoolean(), random.nextBoolean(),
				Direction.values()[random.nextInt(4)], trainID,
				random.nextInt(20), platform);

		for (int j = 0; j < 10; j++) {
			passengerList.add(passengerCreator.getPassenger(j, train));
		}

		
		train.setPassengerList(passengerList);
		
		return train;
	}
}
