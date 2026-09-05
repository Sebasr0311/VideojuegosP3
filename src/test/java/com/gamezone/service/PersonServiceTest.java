package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class PersonServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private PersonRepository personRepository;
    private PersonService personService;

    @Before
    public void setUp() throws Exception {
        File dataDir = temporaryFolder.newFolder("test-data");
        personRepository = new PersonRepository(dataDir.getAbsolutePath());
        personService = new PersonService(personRepository);
    }

    @Test
    public void testPreloadedSellers() {
        List<Seller> sellers = personService.getAllSellers();
        assertNotNull(sellers);
        assertTrue("Must preload at least 3 sellers", sellers.size() >= 3);
        assertNotNull(personService.findSellerByCode("VEN001"));
    }

    @Test
    public void testRegisterCustomerSuccess() {
        Customer c = personService.registerCustomer("1001", "Maria Perez", "3112223344", "maria@mail.com");
        assertNotNull(c);
        assertEquals("1001", c.getId());
        assertEquals("Maria Perez", c.getFullName());

        Customer retrieved = personService.findCustomerById("1001");
        assertNotNull(retrieved);
        assertEquals("maria@mail.com", retrieved.getEmail());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterCustomerDuplicateIdThrowsException() {
        personService.registerCustomer("1002", "Pedro Gomez", "3120000000", "pedro@mail.com");
        personService.registerCustomer("1002", "Pedro Otro", "3120000000", "pedro2@mail.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCustomerInvalidEmailThrowsException() {
        personService.registerCustomer("1003", "Ana Gomez", "3130000000", "invalid-email");
    }
}
