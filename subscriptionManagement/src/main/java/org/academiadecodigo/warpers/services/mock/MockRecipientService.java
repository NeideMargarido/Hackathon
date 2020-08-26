package org.academiadecodigo.warpers.services.mock;

import org.academiadecodigo.warpers.persistence.model.Recipient;
import org.academiadecodigo.warpers.services.RecipientService;

/**
 * A mock {@link RecipientService} implementation
 */
public class MockRecipientService extends AbstractMockService<Recipient> implements RecipientService {

    /**
     * @see RecipientService#get(Integer)
     */
    @Override
    public Recipient get(Integer id) {

        return modelMap.get(id);
    }
}
