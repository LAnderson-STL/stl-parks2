package org.launchcode.stlparks2.models.data;


import org.launchcode.stlparks2.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {

 public User findByUserName(String userName);


}