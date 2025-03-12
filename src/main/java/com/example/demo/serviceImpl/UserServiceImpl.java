package com.example.demo.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.dto.EmailRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.AccountStatus;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ExistDataException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import com.example.demo.util.Validation;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Validation validation;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Boolean registration(UserDto userDto, String url) throws Exception {
        validation.userValidation(userDto);
        User user = modelMapper.map(userDto, User.class);

        setRole(userDto, user);

        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        if (!ObjectUtils.isEmpty(savedUser)) {
            // Send mail
            emailSend(savedUser, url);
            return true;
        }

        return false;
    }

    private void emailSend(User savedUser, String url) throws Exception {
        String message = "Hi,<b> [[username]] </b>"
                + "<br> Your account registerd successfully"
                + "<br> Click the below link to verify and active your account"
                + "<br> <a href='[[url]]'>Click here</a> <br><br>"
                + "Thanks , <br>Enotes.com";

        message = message.replace("[[username]]", savedUser.getFirstName());
        message = message.replace("[[url]]",
                url + "/api/v1/home/verify?uid=" + savedUser.getId() + "&&code="
                        + savedUser.getStatus().getVerificationCode());
        EmailRequest emailRequest = EmailRequest.builder()
                .to(savedUser.getEmail())
                .title("Account creating confirmation")
                .subject("Account created success")
                .message(message)
                .build();
        emailService.sendEmail(emailRequest);

    }

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (authenticate.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
            String token = "dfgdgjbdfgrwejfdssw";
            LoginResponse loginResponse = LoginResponse.builder()
                    .user(modelMapper.map(customUserDetails.getUser(), UserDto.class))
                    .token(token)
                    .build();
            return loginResponse;
        }
        return null;
    }

}
