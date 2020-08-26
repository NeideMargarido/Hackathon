package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link Customer} to {@link UserDto} type conversion
 */
@Component
public class CustomerToCustomerDto extends AbstractConverter<Customer, UserDto> {

    /**
     * Converts the customer model object into a customer DTO
     *
     * @param customer the customer
     * @return the customer DTO
     */
    @Override
    public UserDto convert(Customer customer) {

        UserDto userDto = new UserDto();
        userDto.setId(customer.getId());
        userDto.setFirstName(customer.getFirstName());
        userDto.setLastName(customer.getLastName());
        userDto.setEmail(customer.getEmail());
        userDto.setPhone(customer.getPhone());

        return userDto;
    }
}
