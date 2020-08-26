package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.converters.CustomerDtoToCustomer;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.services.UserService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class CustomerDtoToUserTest {

    private CustomerDtoToCustomer customerDtoToCustomer;
    private UserService userService;

    @Before
    public void setup() {
        userService = mock(UserService.class);

        customerDtoToCustomer = new CustomerDtoToCustomer();
        customerDtoToCustomer.setUserService(userService);
    }

    @Test
    public void testConvert() {

        //setup
        int fakeCustomerId = 9999;
        String fakeFirstName = "Fakey";
        String fakeLastName = "Fake";
        String fakeEmail = "fake@email.com";
        String fakePhone = "912345678";

        Customer fakeCustomer = spy(Customer.class);
        fakeCustomer.setId(fakeCustomerId);

        UserDto fakeUserDto = new UserDto();
        fakeUserDto.setId(fakeCustomerId);
        fakeUserDto.setFirstName(fakeFirstName);
        fakeUserDto.setLastName(fakeLastName);
        fakeUserDto.setEmail(fakeEmail);
        fakeUserDto.setPhone(fakePhone);

        when(userService.get(fakeCustomerId)).thenReturn(fakeCustomer);

        //exercise
        Customer customer = customerDtoToCustomer.convert(fakeUserDto);

        //verify
        verify(userService, times(1)).get(fakeCustomerId);
        verify(fakeCustomer, times(1)).setFirstName(fakeFirstName);
        verify(fakeCustomer, times(1)).setLastName(fakeLastName);
        verify(fakeCustomer, times(1)).setEmail(fakeEmail);
        verify(fakeCustomer, times(1)).setPhone(fakePhone);

        assertTrue(customer.getId() == fakeCustomerId);
        assertTrue(customer.getFirstName().equals(fakeFirstName));
        assertTrue(customer.getLastName().equals(fakeLastName));
        assertTrue(customer.getEmail().equals(fakeEmail));
        assertTrue(customer.getPhone().equals(fakePhone));
    }
}
