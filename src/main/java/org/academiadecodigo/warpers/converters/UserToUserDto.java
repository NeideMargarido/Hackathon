package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.persistence.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link User} to {@link UserDto} type conversion
 */
@Component
public class UserToUserDto extends AbstractConverter<User, UserDto> {

    /**
     * Converts the user model object into a user DTO
     *
     * @param user the user
     * @return the user DTO
     */
    @Override
    public UserDto convert(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setPassword(user.getPassword());
        userDto.setPhone(user.getPhone());


        return userDto;
    }
}
