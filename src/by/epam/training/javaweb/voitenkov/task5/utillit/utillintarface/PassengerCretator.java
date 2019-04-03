package by.epam.training.javaweb.voitenkov.task5.utillit.utillintarface;

import by.epam.training.javaweb.voitenkov.task5.model.entity.Passenger;
import by.epam.training.javaweb.voitenkov.task5.model.entity.Train;

public interface PassengerCretator {
	public Passenger getPassenger(int passengerID, Train train);
}
