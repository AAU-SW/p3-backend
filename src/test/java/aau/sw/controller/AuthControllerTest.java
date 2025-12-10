package aau.sw.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import aau.sw.dto.LoginReq;
import aau.sw.dto.RegisterReq;
import aau.sw.model.User;
import aau.sw.repository.UserRepository;
import aau.sw.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @Mock
  UserRepository userRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  AuthenticationManager authManager;

  @Mock
  JwtService jwtService;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  String testEncryptedPassword = "encrypted-password";
  String testPassword = "password";
  String testEmail = "test@mail.com";
  String testName = "test name";
  String testRole = "admin";

  @BeforeEach
  void setUp() {
    AuthController controller = new AuthController(
      userRepository,
      passwordEncoder,
      authManager,
      jwtService
    );
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    objectMapper = new ObjectMapper();
  }

  @Nested
  @DisplayName("Register User Tests")
  class RegisterUserTests {

    @Test
    @DisplayName(
      "POST /api/auth/register creates a user and returns 201 + body"
    )
    void registerUser_success() throws Exception {
      // Arrange
      RegisterReq input = new RegisterReq(
        testEmail,
        testPassword,
        testName,
        testRole
      );

      User expected = new User();
      expected.setEmail(testEmail);
      expected.setEncryptedPassword(testEncryptedPassword);

      when(passwordEncoder.encode(any(String.class))).thenReturn(
        testEncryptedPassword
      );
      when(userRepository.save(any(User.class))).thenReturn(expected);

      // Act & Assert
      mockMvc
        .perform(
          post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.role").value(testRole))
        .andExpect(jsonPath("$.email").value(testEmail));

      verify(userRepository).save(any(User.class));
      verify(passwordEncoder).encode(any(String.class));
    }

    @Test
    @DisplayName("POST /api/auth/register with existing email returns 409")
    void registerUser_emailExists_conflict() throws Exception {
      // Arrange
      RegisterReq input = new RegisterReq(
        testEmail,
        testPassword,
        testName,
        testRole
      );

      when(userRepository.findByEmail(testEmail)).thenReturn(
        java.util.Optional.of(new User())
      );
      // Act & Assert
      mockMvc
        .perform(
          post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error").value("Email already in use"));

      verify(userRepository).findByEmail(testEmail);
    }

    @Test
    @DisplayName("POST /api/auth/register with invalid input returns 400")
    void registerUser_invalidInput_badRequest() throws Exception {
      // Arrange
      RegisterReq input = new RegisterReq("", "", "", "");
      // Act & Assert
      mockMvc
        .perform(
          post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isBadRequest());

      verify(userRepository, never()).save(any(User.class));
    }
  }

  @Nested
  @DisplayName("Login User Tests")
  class LoginUserTests {

    @Test
    @DisplayName("POST /api/auth/login with valid input returns 200 + tokens")
    void loginUser_success() throws Exception {
      // Arrange
      LoginReq input = new LoginReq(testEmail, testPassword);
      String testAccessToken = "access-token";
      String testRefreshToken = "refresh-token";

      when(jwtService.issue(any(String.class))).thenReturn(testAccessToken);
      when(jwtService.issueRefresh(any(String.class))).thenReturn(
        testRefreshToken
      );

      // Act & Assert
      mockMvc
        .perform(
          post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(testAccessToken))
        .andExpect(jsonPath("$.refreshToken").value(testRefreshToken));

      verify(authManager).authenticate(any());
      verify(jwtService).issue(any(String.class));
      verify(jwtService).issueRefresh(any(String.class));
    }

    @Test
    @DisplayName("POST /api/auth/login with invalid input returns 400")
    void loginUser_invalidInput_badRequest() throws Exception {
      // Arrange
      LoginReq input = new LoginReq("", "");
      // Act & Assert
      mockMvc
        .perform(
          post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isBadRequest());

      verify(authManager, never()).authenticate(any());
    }

    @Test
    @DisplayName("POST /api/auth/login with wrong credentials returns 401")
    void loginUser_wrongCredentials_unauthorized() throws Exception {
      // Arrange
      LoginReq input = new LoginReq(testEmail, testPassword);
      doThrow(new RuntimeException("Bad credentials"))
        .when(authManager)
        .authenticate(any());
      // Act & Assert
      mockMvc
        .perform(
          post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(input))
        )
        .andExpect(status().isUnauthorized());

      verify(authManager).authenticate(any());
    }
  }
}
