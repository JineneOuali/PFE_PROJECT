package com.example.PFE_PROJECT;

import com.example.PFE_PROJECT.dao.IUser;
import com.example.PFE_PROJECT.models.User;
import com.example.PFE_PROJECT.utils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling

public class PfeProjectApplication implements CommandLineRunner {

	@Autowired
	private StorageService storage;

	@Autowired
	private IUser iUser;




	public static void main(String[] args) {
		SpringApplication.run(PfeProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// storage.init();



        /*User user = new User();
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setUsername("Wissem M");
        user.setPassword(hash("wissem123"));


        iUser.save(user);*/


	}

	String hash(String password) {


		String hashedPassword = null;
		int i = 0;
		while (i < 5) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			hashedPassword = passwordEncoder.encode(password);
			i++;
		}

		return hashedPassword;
	}
}
