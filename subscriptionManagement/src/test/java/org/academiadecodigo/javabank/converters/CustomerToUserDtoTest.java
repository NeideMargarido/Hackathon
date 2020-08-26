package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.converters.CustomerToCustomerDto;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class CustomerToUserDtoTest {

    private CustomerToCustomerDto customerToCustomerDto;

    @Before
    public void setup() {
        customerToCustomerDto = new CustomerToCustomerDto();
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
        fakeCustomer.setFirstName(fakeFirstName);
        fakeCustomer.setLastName(fakeLastName);
        fakeCustomer.setEmail(fakeEmail);
        fakeCustomer.setPhone(fakePhone);

        //exercise
        UserDto userDto = customerToCustomerDto.convert(fakeCustomer);

        //verify
        verify(fakeCustomer, times(1)).getId();
        verify(fakeCustomer, times(1)).getFirstName();
        verify(fakeCustomer, times(1)).getLastName();
        verify(fakeCustomer, times(1)).getEmail();
        verify(fakeCustomer, times(1)).getPhone();

        assertTrue(userDto.getId() == fakeCustomerId);
        assertTrue(userDto.getFirstName().equals(fakeFirstName));
        assertTrue(userDto.getLastName().equals(fakeLastName));
        assertTrue(userDto.getEmail().equals(fakeEmail));
        assertTrue(userDto.getPhone().equals(fakePhone));
    }
}
