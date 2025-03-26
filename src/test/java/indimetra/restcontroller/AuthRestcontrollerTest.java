package indimetra.restcontroller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import indimetra.modelo.dto.LoginRequestDto;
import indimetra.modelo.dto.UserRequestDto;
import indimetra.modelo.dto.UserResponseDto;
import indimetra.modelo.entity.Role;
import indimetra.modelo.entity.User;
import indimetra.modelo.service.IRoleService;
import indimetra.modelo.service.IUserService;
import jakarta.transaction.Transactional;
import indimetra.exception.GlobalExceptionHandler;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthRestcontrollerTest {

    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @Mock
    private IRoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthRestcontroller authRestController;

    private User mockUser;
    private Role userRole;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authRestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Simulación del role de usuario
        userRole = new Role();
        userRole.setId(1L);
        userRole.setName(Role.RoleType.ROLE_USER);

        // Creación del usuario mock
        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedpassword")
                .roles(Collections.singleton(userRole))
                .build();

        // Simulación del DTO recibido en la solicitud de registro
        userRequestDto = UserRequestDto.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("password123")
                .profileImage("https://example.com/image.jpg")
                .country("España")
                .roles(Set.of("ROLE_USER"))
                .socialLinks("https://github.com/newuser")
                .build();
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto("testuser", "password");

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password", "encodedpassword")).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login exitoso"))
                .andExpect(jsonPath("$.user").value("testuser"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        LoginRequestDto loginDto = new LoginRequestDto("notFound", "password");

        when(userService.findByUsername("notFound")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado: notFound"));
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        when(userService.findByUsername(userRequestDto.getUsername())).thenReturn(Optional.empty());
        when(userService.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.empty());
        when(roleService.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encodedpassword");

        // Simular la conversión de DTO a User con ModelMapper
        User mappedUser = User.builder()
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .password("encodedpassword")
                .profileImage(userRequestDto.getProfileImage())
                .country(userRequestDto.getCountry())
                .roles(Collections.singleton(userRole))
                .socialLinks(userRequestDto.getSocialLinks())
                .build();

        when(modelMapper.map(userRequestDto, User.class)).thenReturn(mappedUser);
        when(userService.create(any(User.class))).thenReturn(mappedUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    void testRegisterUserUsernameAlreadyExists() throws Exception {
        when(userService.findByUsername(userRequestDto.getUsername())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El nombre de usuario ya está en uso"));
    }

    @Test
    void testRegisterUserEmailAlreadyExists() throws Exception {
        when(userService.findByUsername(userRequestDto.getUsername())).thenReturn(Optional.empty());
        when(userService.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El email ya está en uso"));
    }

    @Test
    void testRegisterUserRoleNotFound() throws Exception {
        when(userService.findByUsername(userRequestDto.getUsername())).thenReturn(Optional.empty());
        when(userService.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.empty());
        when(roleService.findByName("ROLE_USER")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Rol no encontrado"));
    }
}
