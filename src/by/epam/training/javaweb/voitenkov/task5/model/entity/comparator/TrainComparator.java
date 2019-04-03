/**
 * 
 */
package by.epam.training.javaweb.voitenkov.task5.model.entity.comparator;

import java.util.Comparator;

import by.epam.training.javaweb.voitenkov.task5.model.entity.Train;

/**
 * @author sergey
 *
 */
public class TrainComparator implements Comparator<Train> {

	@Override
	public int compare(Train o1, Train o2) {
		return o1.getDirection().compareTo(o2.getDirection());
	}

}
