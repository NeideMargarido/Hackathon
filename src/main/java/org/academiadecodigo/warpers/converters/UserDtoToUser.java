package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link UserDto} to {@link User} type conversion
 */
@Component
public class UserDtoToUser implements Converter<UserDto, User> {

    private UserService userService;

    /**
     * Sets the customer service
     *
     * @param userService the customer service to set
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Converts the customer DTO into a customer model object
     *
     * @param userDto the customer DTO
     * @return the customer
     */
    @Override
    public User convert(UserDto userDto) {

        User user = (userDto.getId() != null ? userService.get(userDto.getId()) : new User());

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(userDto.getPassword());
        user.setCountry(userDto.getCountry());

        return user;
    }
}
