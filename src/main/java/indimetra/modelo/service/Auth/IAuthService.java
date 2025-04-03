package indimetra.modelo.service.Auth;

import indimetra.modelo.service.Auth.Model.LoginRequestDto;
import indimetra.modelo.service.Auth.Model.LoginResponseDto;
import indimetra.modelo.service.User.Model.UserRequestDto;
import indimetra.modelo.service.User.Model.UserResponseDto;

public interface IAuthService {

    LoginResponseDto authenticateUser(LoginRequestDto loginDto);

    UserResponseDto registerUser(UserRequestDto userDto);
    
    UserResponseDto getAuthenticatedUser(String username);

}
