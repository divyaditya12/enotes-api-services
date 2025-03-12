package com.example.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AccountStatus;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.exception.SuccessException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Boolean verifyAccount(Integer id, String code) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Invalid user id"));

        if (user.getStatus().getVerificationCode() == null) {
            throw new SuccessException("Account already verified");
        }
        if (user.getStatus().getVerificationCode().equals(code)) {
            AccountStatus status = user.getStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
