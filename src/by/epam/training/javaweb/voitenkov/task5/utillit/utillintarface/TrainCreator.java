/**
 * 
 */
package by.epam.training.javaweb.voitenkov.task5.utillit.utillintarface;

import by.epam.training.javaweb.voitenkov.task5.model.entity.Platform;
import by.epam.training.javaweb.voitenkov.task5.model.entity.Train;

/**
 * @author Sergey Voitenkov
 *
 * Mar 31, 2019
 */
public interface TrainCreator {
	public Train getTrain(int trainID, Platform platform);
}
