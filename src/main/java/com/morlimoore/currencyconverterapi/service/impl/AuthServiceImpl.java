package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.DTOs.LoginRequestDTO;
import com.morlimoore.currencyconverterapi.DTOs.SignupRequestDTO;
import com.morlimoore.currencyconverterapi.entities.Role;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.UserDetailsImpl;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.payload.JwtResponse;
import com.morlimoore.currencyconverterapi.repositories.RoleRepository;
import com.morlimoore.currencyconverterapi.repositories.UserRepository;
import com.morlimoore.currencyconverterapi.repositories.WalletRepository;
import com.morlimoore.currencyconverterapi.security.JwtUtils;
import com.morlimoore.currencyconverterapi.service.AuthService;
import com.morlimoore.currencyconverterapi.util.RoleEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.createResponse;
import static org.springframework.http.HttpStatus.OK;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Boolean ifUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean ifEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> createUserAccount(SignupRequestDTO signupRequestDTO) {
        User user = modelMapper.map(signupRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        Role userRole = roleRepository.findByName(RoleEnum.valueOf(signupRequestDTO.getRole()))
                .orElseThrow(() -> new CustomException("Error: Role is not found.", HttpStatus.BAD_REQUEST));
        user.setRole(userRole);
        userRepository.save(user);
        Wallet wallet = new Wallet(signupRequestDTO.getMainCurrency());
        wallet.setUser(user);
        wallet.setType("MAIN");
        wallet.setAmount(0L);
        walletRepository.save(wallet);
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(OK);
        response.setMessage("User Registration Successful");
        return createResponse(response);
    }

    @Override
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()).get(0);
        ApiResponse<JwtResponse> response = new ApiResponse<>();
        response.setStatus(OK);
        response.setMessage("Successful");
        response.setResult(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));
        return createResponse(response);
    }
}
