package org.swe.business;

//import org.swe.core.DAO.ConcreteUserDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.CreateUserDTO;
import org.swe.core.DTO.LoginDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.UnauthorizedException;
//import org.swe.core.utils.JWTUtility;
import org.swe.core.validation.MyValidationResult;
import org.swe.core.validation.MyValidator;
import org.swe.model.User;

//import io.jsonwebtoken.Claims;

public class UserController {

    private final AuthService authService;
    private final UserDAO userDAO;

    public UserController(AuthService authService, UserDAO userDAO) {
        this.authService = authService;
        this.userDAO = userDAO;
    }


    public String whoami(String token) {
        User user = authInterceptor(token);
        return user.getName() + " " + user.getSurname();
    }

    public String login(LoginDTO dto) {
        validationInterceptor(dto);

        User user = userDAO.getUserByEmail(dto.email);
        if(user == null){
            throw new BadRequestException("User not found");
        }

        return authService.authenticateWithPassword(user, dto.password);
    }

    public String signup(CreateUserDTO dto) {
        validationInterceptor(dto);
        try {
            User newUser = userDAO.createUser(dto.getName(), dto.getSurname(), dto.getPassword(), dto.getEmail());
            if(newUser == null){
                throw new BadRequestException("User already exists");
            }
            return authService.generateAccessToken(newUser);
        }
        catch (Exception e) {
            System.out.println(e);
            throw new BadRequestException("User already exists");
        }
    }

    protected User authInterceptor(String token) {
        Integer userId = authService.validateAccessToken(token);
        if (userId == null) throw new UnauthorizedException("userId is invalid.");
        User user = userDAO.getUserById(userId);
        if (user == null) throw new UnauthorizedException("User not found.");
        return user;
    }

    protected void validationInterceptor(Object dto) {
        MyValidator<Object> validator = new MyValidator<>();
        MyValidationResult<Object> result = validator.validate(dto);
        if (result.hasErrors()) {
            throw new BadRequestException(result.getErrorMessage());
        }
    }
}
