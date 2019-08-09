package masonemobile.restaurantlocator.modules.viewmodels;

import masonemobile.restaurantlocator.modules.models.Business;
import masonemobile.restaurantlocator.modules.repository.Repository;

public class DetailViewModel {

    private Repository repo = Repository.getInstance();
    public Business getBusinuess() {
        return repo.getBusinesses().get(repo.getSelectedBussinuess());
    }
}
