/**
 * 
 */
package com.blog.blogservice.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.security.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.blog.blogservice.exception.AdminUserException;
import com.blog.blogservice.exception.UserNotFoundException;
import com.blog.blogservice.model.Address;
import com.blog.blogservice.model.AdminUser;
import com.blog.blogservice.model.Geo;
import com.blog.blogservice.model.User;
import com.blog.blogservice.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
 

/**
 * @author suresh
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure = false)
@Import(SecurityConfig.class)
public class AdminControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AdminService adminService;
	
	@InjectMocks
	private AdminController adminController;
	
	@MockBean
	private User user;
	
	@MockBean
	private AdminUser admUser;
	
	private List<User> users = new ArrayList<>();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		 MockitoAnnotations.initMocks(this);
		 mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
		 user = new User();
		 Geo geo = new Geo();
		 Address addr = new Address();
		 geo.setLat("-37.3159");
		 geo.setLng("81.1496");
		 addr.setCity("Gwenborough");
		 addr.setGeo(geo);
		 addr.setStreet("Kulas Light");
		 addr.setSuite("Apt.556");
		 addr.setZipcode("92998-3874");
		 user.setEmail("Sincere@april.biz");
		 user.setName("Leanne Graham");
		 user.setPhone("1-770-736-8031 x56442");
		 user.setUsername("Bret");
		 user.setWebsite("hildegard.org");
		 user.setAddress(addr);
		 users.add(user);
		 
		 admUser = new AdminUser();
		 admUser.setRole("admin");
		 admUser.setUserPassword("admin");
		 admUser.setUserName("testadmin");
		 
		 
	}

	/**
	 * Test method for {@link com.blog.blogservice.controller.AdminController#getUserInfo(java.lang.String)}.
	 */
	@Test
	public final void testGetUserInfo() throws Exception {
		Mockito.when(adminService.getUserById(user.getId())).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.get("/blog/users/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
	}

	@Test
	public final void testInvalidUserInfo() throws Exception {
		Mockito.when(adminService.getUserById("1000")).thenThrow(UserNotFoundException.class);
		mockMvc.perform(MockMvcRequestBuilders.get("/blog/users/1000").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());
	}
	@Test
	public final void testGetAllUserInfoWithTheirPosts() throws Exception {
		Mockito.when(adminService.getAllUserinfoWithPost()).thenReturn(users);
		mockMvc.perform(MockMvcRequestBuilders.get("/blog/users/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
	}
	/**
	 * Test method for {@link com.blog.blogservice.controller.AdminController#adminLogin(com.blog.blogservice.model.AdminUser)}.
	 */
	/*@Test
	public final void testLogin() throws Exception{
		Mockito.when(adminService.isAdminUser(admUser)).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.post("/blog/login").contentType(MediaType.APPLICATION_JSON).content(jsonToString(admUser)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public final void testFailureLogin() throws Exception{
		
		Mockito.when(adminService.isAdminUser(admUser)).thenThrow(AdminUserException.class);
		mockMvc.perform(MockMvcRequestBuilders.post("/blog/login").contentType(MediaType.APPLICATION_JSON).content(jsonToString(admUser)))
        .andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());
	}*/
	// Parsing String format data into JSON format
    private static String jsonToString(final Object obj) throws JsonProcessingException {
        String result;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            result = jsonContent;
            
        } catch (JsonProcessingException e) {
            result = "Json processing error";
        }
        return result;
    }

}
