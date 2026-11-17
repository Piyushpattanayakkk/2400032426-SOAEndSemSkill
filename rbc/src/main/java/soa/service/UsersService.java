package soa.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import soa.model.Users;
import soa.repo.UsersRepo;

@Service
public class UsersService {

    UsersRepo UR;

    public UsersService(UsersRepo UR) {
        this.UR = UR;
    }

    public Object registerUser(Users U) {

        Map<String, Object> response = new HashMap<>();

        try {
            UR.save(U);

            response.put("status", "success");
            response.put("message", "User registered successfully");

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    public Object getAllUsers() {

        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", "success");
            response.put("data", UR.findAll());

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    public Object getUserById(Long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", "success");
            response.put("data", UR.findById(id));

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    public Object deleteUser(Long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            UR.deleteById(id);

            response.put("status", "success");
            response.put("message", "User deleted successfully");

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }
}