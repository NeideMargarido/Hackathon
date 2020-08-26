package org.academiadecodigo.javabank.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.academiadecodigo.warpers.command.UserDto;
import org.academiadecodigo.warpers.controller.rest.RestUserController;
import org.academiadecodigo.warpers.converters.CustomerDtoToCustomer;
import org.academiadecodigo.warpers.converters.CustomerToCustomerDto;
import org.academiadecodigo.warpers.exceptions.AssociationExistsException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestUserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CustomerToCustomerDto customerToCustomerDto;

    @Mock
    private CustomerDtoToCustomer customerDtoToCustomer;


    @InjectMocks
    private RestUserController restUserController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restUserController).build();
        objectMapper = new ObjectMapper();

    }

    @Test
    public void testListCustomers() throws Exception {

        Integer fakeID = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "777888999";
        String email = "mail@gmail.com";

        Customer customer = new Customer();
        customer.setId(fakeID);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);

        UserDto userDto = new UserDto();
        userDto.setId(fakeID);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhone(phone);
        userDto.setEmail(email);

        List<Customer> customers = new ArrayList<>();
        customers.add(customer);


        when(userService.list()).thenReturn(customers);
        when(customerToCustomerDto.convert(customer)).thenReturn(userDto);

        mockMvc.perform(get("/api/customer/"))
                .andExpect(jsonPath("$[0].id").value(fakeID))
                .andExpect(jsonPath("$[0].firstName").value(firstName))
                .andExpect(jsonPath("$[0].lastName").value(lastName))
                .andExpect(jsonPath("$[0].email").value(email))
                .andExpect(jsonPath("$[0].phone").value(phone))
                .andExpect(status().isOk());

        verify(userService, times(1)).list();
        verify(customerToCustomerDto, times(1)).convert(customer);
    }

    @Test
    public void testShowCustomer() throws Exception {

        Integer fakeID = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "777888999";
        String email = "mail@gmail.com";

        Customer customer = new Customer();
        customer.setId(fakeID);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);

        UserDto userDto = new UserDto();
        userDto.setId(fakeID);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhone(phone);
        userDto.setEmail(email);


        when(userService.get(fakeID)).thenReturn(customer);
        when(customerToCustomerDto.convert(customer)).thenReturn(userDto);

        mockMvc.perform(get("/api/customer/{id}", customer.getId()))
                .andExpect(jsonPath("$.id").value(fakeID))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phone").value(phone))
                .andExpect(status().isOk());

        verify(userService, times(1)).get(fakeID);
        verify(customerToCustomerDto, times(1)).convert(customer);
    }

    @Test
    public void testShowInvalidCustomer() throws Exception {

        int invalidId = 888;
        int fakeId = 999;
        Customer customer = new Customer();
        customer.setId(fakeId);


        when(userService.get(invalidId)).thenReturn(null);

        mockMvc.perform(get("/api/customer/{id}", invalidId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).get(invalidId);
    }

    @Test
    public void testAddCustomer() throws Exception {

        int fakeId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhone(phone);
        userDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);


        when(customerDtoToCustomer.convert(ArgumentMatchers.any(UserDto.class))).thenReturn(customer);
        when(userService.save(customer)).thenReturn(customer);

        mockMvc.perform(post("/api/customer/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(userDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/api/customer/" + customer.getId())));

        //verify properties of bound command object
        ArgumentCaptor<UserDto> boundCustomer = ArgumentCaptor.forClass(UserDto.class);

        verify(customerDtoToCustomer, times(1)).convert(boundCustomer.capture());
        verify(userService, times(1)).save(customer);

        assertEquals(null, boundCustomer.getValue().getId());
        assertEquals(firstName, boundCustomer.getValue().getFirstName());
        assertEquals(lastName, boundCustomer.getValue().getLastName());
        assertEquals(email, boundCustomer.getValue().getEmail());
        assertEquals(phone, boundCustomer.getValue().getPhone());
    }

    @Test
    public void testAddCustomerWithBadRequest() throws Exception {

        mockMvc.perform(post("/api/customer/"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testEditCustomer() throws Exception {

        int fakeId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setId(fakeId);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhone(phone);
        userDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);


        when(customerDtoToCustomer.convert(ArgumentMatchers.any(UserDto.class))).thenReturn(customer);
        when(userService.save(customer)).thenReturn(customer);
        when(userService.get(fakeId)).thenReturn(customer);

        mockMvc.perform(put("/api/customer/{id}", fakeId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(userDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<UserDto> boundCustomer = ArgumentCaptor.forClass(UserDto.class);

        verify(customerDtoToCustomer, times(1)).convert(boundCustomer.capture());
        verify(userService, times(1)).save(customer);

        assertEquals((Integer) fakeId, boundCustomer.getValue().getId());
        assertEquals(firstName, boundCustomer.getValue().getFirstName());
        assertEquals(lastName, boundCustomer.getValue().getLastName());
        assertEquals(email, boundCustomer.getValue().getEmail());
        assertEquals(phone, boundCustomer.getValue().getPhone());
    }

    @Test
    public void testEditCustomerWithBadRequest() throws Exception {

        int invalidId = 888;
        int fakeId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setId(fakeId);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhone(phone);
        userDto.setEmail(email);


        mockMvc.perform(put("/api/customer/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditInvalidCustomer() throws Exception {

        int invalidId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setId(invalidId);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhone(phone);
        userDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(invalidId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);


        when(customerDtoToCustomer.convert(ArgumentMatchers.any(UserDto.class))).thenReturn(customer);
        when(userService.save(customer)).thenReturn(customer);
        when(userService.get(invalidId)).thenReturn(null);

        mockMvc.perform(put("/api/customer/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(userDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testDeleteCustomer() throws Exception {

        int fakeId = 999;

        mockMvc.perform(delete("/api/customer/{id}", fakeId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(fakeId);
    }

    @Test
    public void testDeleteInvalidCustomer() throws Exception {

        int invalidId = 888;

        doThrow(new CustomerNotFoundException()).when(userService).delete(ArgumentMatchers.any(Integer.class));

        mockMvc.perform(delete("/api/customer/{id}", invalidId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).delete(invalidId);
    }

    @Test
    public void testDeleteCustomerWithOpenAccount() throws Exception {

        int fakeId = 999;

        doThrow(new AssociationExistsException()).when(userService).delete(ArgumentMatchers.any(Integer.class));

        mockMvc.perform(delete("/api/customer/{id}", fakeId))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).delete(fakeId);
    }
}
