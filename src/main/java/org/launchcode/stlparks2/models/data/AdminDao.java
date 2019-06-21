package org.launchcode.stlparks2.models.data;


import org.launchcode.stlparks2.models.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional


public interface AdminDao extends CrudRepository<Admin, Integer> {

    public Optional<Admin> findByUserName(String adminUserName);

}