package com.logilink.backend.controllers;

import com.logilink.backend.User;
import com.logilink.backend.UserRepository;
import com.logilink.backend.entity.Transporter;
import com.logilink.backend.repository.TransporterRepository;
import com.logilink.backend.payload.request.LoginRequest;
import com.logilink.backend.payload.response.JwtResponse;
import com.logilink.backend.security.jwt.JwtUtils;
import com.logilink.backend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransporterRepository transporterRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                role));
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User userRequest) {
        if (userRepository.findByUsernameIgnoreCase(userRequest.getUsername()).isPresent() ||
                transporterRepository.findByUsernameIgnoreCase(userRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.findByEmailIgnoreCase(userRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setName(userRequest.getName());
        user.setAddress(userRequest.getAddress());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(com.logilink.backend.entity.Role.USER);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/register/transporter")
    public ResponseEntity<?> registerTransporter(@Valid @RequestBody Transporter transporterReq) {
        if (transporterRepository.findByUsernameIgnoreCase(transporterReq.getUsername()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(transporterReq.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        Transporter transporter = new Transporter();
        transporter.setUsername(transporterReq.getUsername());
        transporter.setPassword(encoder.encode(transporterReq.getPassword()));
        transporter.setName(transporterReq.getName());
        transporter.setAddress(transporterReq.getAddress());
        transporter.setMobileNumber(transporterReq.getMobileNumber());
        transporter.setServiceArea(transporterReq.getServiceArea());
        transporter.setRole(com.logilink.backend.entity.Role.TRANSPORTER);
        transporter.setRating(0.0);
        transporter.setReviewCount(0);

        transporterRepository.save(transporter);

        return ResponseEntity.ok("Transporter registered successfully!");
    }
}
