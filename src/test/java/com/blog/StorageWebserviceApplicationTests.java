package com.blog;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StorageWebserviceApplication.class)
@WebAppConfiguration
public class StorageWebserviceApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void testUpload() throws Exception {
		MockMultipartFile testFile = new MockMultipartFile("file", "testFile.txt", "text/plain", "test".getBytes());
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(MockMvcRequestBuilders.fileUpload("http://localhost:8080/api/storage/upload").file(testFile))
				.andExpect(status().is(200));
	}

	@Test
	public void testDownload() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/storage/download?fileName=testFile.txt"))
				.andExpect(status().is(200)).andExpect(content().contentType("application/octet-stream"));
	}

}
