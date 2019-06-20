package org.launchcode.stlparks2.models.data;


import org.launchcode.stlparks2.models.Amenity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional

public interface AmenityDao extends CrudRepository<Amenity, Integer> {

    public List<Amenity> findAllByOrderByNameAsc();


}
