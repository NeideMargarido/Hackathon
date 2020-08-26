package org.academiadecodigo.javabank.controller.web;

import org.academiadecodigo.warpers.command.SubscriptionDto;
import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.controller.web.UserController;
import org.academiadecodigo.warpers.converters.AccountToAccountDto;
import org.academiadecodigo.warpers.converters.CustomerDtoToCustomer;
import org.academiadecodigo.warpers.converters.CustomerToCustomerDto;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CustomerToCustomerDto customerToCustomerDto;

    @Mock
    private CustomerDtoToCustomer customerDtoToCustomer;

    @Mock
    private AccountToAccountDto accountToAccountDto;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testListCustomers() throws Exception {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        when(userService.list()).thenReturn(customers);

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(new UserDto());
        userDtos.add(new UserDto());

        when(customerToCustomerDto.convert(customers)).thenReturn(userDtos);

        mockMvc.perform(get("/customer/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        mockMvc.perform(get("/customer/"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        verify(userService, times(3)).list();
        verify(customerToCustomerDto, times(3)).convert(customers);
    }

    @Test
    public void testShowCustomer() throws Exception {

        int fakeId = 999;
        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName("Rui");
        customer.setLastName("Ferrao");
        customer.setEmail("mail@gmail.com");
        customer.setPhone("99999914143");

        when(userService.get(fakeId)).thenReturn(customer);

        UserDto userDto = new UserDto();
        userDto.setId(customer.getId());
        userDto.setEmail(customer.getEmail());
        userDto.setFirstName(customer.getFirstName());
        userDto.setLastName(customer.getLastName());
        userDto.setPhone(customer.getPhone());

        when(customerToCustomerDto.convert(customer)).thenReturn(userDto);

        List<SubscriptionDto> subscriptionDtos = new ArrayList<>();
        subscriptionDtos.add(new SubscriptionDto());
        subscriptionDtos.add(new SubscriptionDto());

        when(accountToAccountDto.convert(ArgumentMatchers.<Account>anyList())).thenReturn(subscriptionDtos);

        mockMvc.perform(get("/customer/" + fakeId))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/show"))
                .andExpect(model().attribute("customer", equalTo(userDto)))
                .andExpect(model().attribute("accounts", equalTo(subscriptionDtos)));

        verify(userService, times(1)).get(fakeId);
        verify(customerToCustomerDto).convert(customer);
        verify(accountToAccountDto).convert(ArgumentMatchers.<Account>anyList());

    }

    @Test
    public void testAddCustomer() throws Exception {

        mockMvc.perform(get("/customer/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/add-update"))
                .andExpect(model().attribute("customer", instanceOf(UserDto.class)));

        verifyZeroInteractions(userService);
    }

    @Test
    public void testEditCustomer() throws Exception {

        int fakeID = 9999;
        Customer customer = new Customer();
        UserDto userDto = new UserDto();
        customer.setId(fakeID);

        when(userService.get(fakeID)).thenReturn(customer);
        when(customerToCustomerDto.convert(customer)).thenReturn(userDto);

        mockMvc.perform(get("/customer/" + fakeID + "/edit/"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/add-update"))
                .andExpect(model().attribute("customer", equalTo(userDto)));

        verify(userService, times(1)).get(fakeID);
    }

    @Test
    public void testSaveCustomer() throws Exception {

        Integer fakeID = 9999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888";
        String email = "mail@gmail.com";

        UserDto userDto = new UserDto();

        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhone(phone);
        userDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(fakeID);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);

        when(customerDtoToCustomer.convert(ArgumentMatchers.any(UserDto.class))).thenReturn(customer);
        when(userService.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/customer")
                //Action parameter to post so spring can use the right method to process this request
                .param("action", "save")
                .param("id", fakeID.toString())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phone", phone))
                // for debugging
                // .andDo(print())
                .andExpect(status().is3xxRedirection());

        //verify properties of bound command object
        ArgumentCaptor<UserDto> boundCustomer = ArgumentCaptor.forClass(UserDto.class);

        verify(customerDtoToCustomer, times(1)).convert(boundCustomer.capture());
        verify(userService, times(1)).save(customer);

        assertEquals(fakeID, boundCustomer.getValue().getId());
        assertEquals(firstName, boundCustomer.getValue().getFirstName());
        assertEquals(lastName, boundCustomer.getValue().getLastName());
        assertEquals(email, boundCustomer.getValue().getEmail());
        assertEquals(phone, boundCustomer.getValue().getPhone());

    }

    @Test
    public void testSaveCustomerCancel() throws Exception {
        mockMvc.perform(post("/customer/")
                //Action parameter to post so spring can use the right method to process this request
                .param("action", "cancel"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testDeleteCustomer() throws Exception {

        int fakeId = 9999;
        Customer customer = new Customer();

        when(userService.get(fakeId)).thenReturn(customer);

        mockMvc.perform(get("/customer/" + fakeId + "/delete/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer"));

        verify(userService, times(1)).delete(fakeId);
    }

}
