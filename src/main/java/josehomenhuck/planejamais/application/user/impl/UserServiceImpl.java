package josehomenhuck.planejamais.application.user.impl;

import josehomenhuck.planejamais.application.jwt.JwtService;
import josehomenhuck.planejamais.domain.user.entity.AccessToken;
import josehomenhuck.planejamais.domain.user.entity.User;
import josehomenhuck.planejamais.domain.user.service.UserService;
import josehomenhuck.planejamais.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void save(User user) {
        var userExists = findByEmail(user.getEmail());

        if (userExists != null) {
            throw new IllegalArgumentException("User already exists");
        }

        encodePassword(user);

        userRepository.save(user);
    }

    @Override
    public AccessToken authenticate(String email, String password) {
        var user = findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(user);
    }

    private void encodePassword(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        user.setPassword(encodedPassword);
    }
}
