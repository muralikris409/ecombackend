package com.example.ecombackend.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecombackend.DAO.UserRepository;
import com.example.ecombackend.model.User;
class SessionData {
    private String email;
    private Instant expirationTime;

    public SessionData(String email, Instant expirationTime) {
        this.email = email;
        this.expirationTime = expirationTime;
    }

    public String getEmail() {
        return email;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expirationTime);
    }
}
@Service
public class UserService {
	private Map<String, SessionData> sessionStore = new HashMap<>();
    private static final long SESSION_DURATION = 30 * 60 * 1000;
    @Autowired
    private UserRepository userRepository;

    // Map to store session tokens for users

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    public boolean authenticateUser(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedPassword = hashPassword(rawPassword);
            return user.getPassword().equals(hashedPassword);
        }
        return false;
    }

    public boolean registerUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        User savedUser = userRepository.save(user);
        return savedUser != null && savedUser.getId() != null;
    }

    public boolean isUsernameTaken(String user) {
        Optional<User> opt = userRepository.findByName(user);
        return opt.isPresent();
    }

    // Generate a session key for the user after successful login
    public ApiResponse login(String email, String password) {
        if (authenticateUser(email, password)) {
            String sessionKey = UUID.randomUUID().toString();  // Generate a unique session key
            Instant expirationTime = Instant.now().plusMillis(SESSION_DURATION); // Set session expiration time
            sessionStore.put(sessionKey, new SessionData(email, expirationTime)); 
            System.out.println(sessionStore);// Store session with expiration
            return new ApiResponse(true, sessionKey);  // Return the session key
        }
        return new ApiResponse(false, null);  // Login failed
    }

    // Check if the user has a valid session key
    public boolean isAuthenticated(String sessionKey) {
    	System.out.println(sessionKey);
        SessionData sessionData = sessionStore.get(sessionKey);  // Retrieve session data by session key
System.out.print(sessionStore.get(sessionKey));
System.out.println(sessionStore);
        if (sessionData == null) {
            return false;  // Session key does not exist
        }

        if (sessionData.isExpired()) {
            sessionStore.remove(sessionKey);  // Remove expired session from store
            return false;  // Session has expired
        }

        return true;  // Session is valid and not expired
    }


    // Method to access the dashboard if the user has a valid session
  

    // Optionally, you can provide a logout method to invalidate the session key
    public boolean logout(String sessionKey) {
        sessionStore.remove(sessionKey);  // Remove session key from store
		return true;
    }
}
