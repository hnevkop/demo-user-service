package com.identity.example.controller;

import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.dto.UserDetailDTO;
import com.identity.example.dto.UserRequestDTO;
import com.identity.example.dto.UserResponseDTO;
import com.identity.example.service.UserService;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{uuid}")
  public @ResponseBody UserResponseDTO getUser(@PathVariable UUID uuid) {
    return userService.getUser(uuid);
  }

  @GetMapping("/{uuid}/detail")
  public @ResponseBody UserDetailDTO getUserDetail(@PathVariable UUID uuid) {
    return userService.getUserDetail(uuid);
  }

  @PostMapping
  public @ResponseBody UserResponseDTO addNewUser(
      @Validated @RequestBody UserRequestDTO userRequestDTO) {
    return userService.createUser(userRequestDTO);
  }

  @PutMapping("/{userUuid}/project")
  public UserDetailDTO assignNewProjectToUser(
      @PathVariable UUID userUuid, @Validated @RequestBody ProjectRequestDTO projectRequestDTO) {
    return userService.assignNewProject(userUuid, projectRequestDTO);
  }

  @PutMapping("/{userUuid}/project/{projectUuid}")
  public UserDetailDTO assignExistingProjectToUser(
      @PathVariable UUID userUuid, @PathVariable UUID projectUuid) {
    return userService.assignExistingProject(userUuid, projectUuid);
  }

  @GetMapping
  public @ResponseBody Iterable<UserResponseDTO> getAllUsers() {
    return userService.getAllUsers();
  }

  @DeleteMapping("/{uuid}")
  public void deleteUser(@PathVariable UUID uuid) {
    userService.deleteUser(uuid);
  }
}
