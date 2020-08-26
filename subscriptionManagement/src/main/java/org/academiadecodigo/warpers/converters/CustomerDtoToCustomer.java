package org.academiadecodigo.warpers.converters;

import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link UserDto} to {@link Customer} type conversion
 */
@Component
public class CustomerDtoToCustomer implements Converter<UserDto, Customer> {

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
    public Customer convert(UserDto userDto) {

        Customer customer = (userDto.getId() != null ? userService.get(userDto.getId()) : new Customer());

        customer.setFirstName(userDto.getFirstName());
        customer.setLastName(userDto.getLastName());
        customer.setEmail(userDto.getEmail());
        customer.setPhone(userDto.getPhone());

        return customer;
    }
}
