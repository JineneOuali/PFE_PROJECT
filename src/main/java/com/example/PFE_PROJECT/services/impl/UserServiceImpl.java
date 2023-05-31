package com.example.PFE_PROJECT.services.impl;


import com.example.PFE_PROJECT.dao.IUser;
import com.example.PFE_PROJECT.models.User;
import com.example.PFE_PROJECT.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private IUser userRepository;

    @Override
   public User findByUsername(String username ) throws UsernameNotFoundException {
        User u = userRepository.findByUsername( username );
        return u;
    }


    public List<User> findAll() throws AccessDeniedException {
        List<User> result = userRepository.findAll();
        return result;
    }
    
    public User save(User u)
    {
    	return userRepository.save(u);
    }



	/*@Override
	public Page<Utilisateur> chercher(String mc, Pageable pageable) {
		return userRepository.chercher(mc, pageable);
	}*/
}
