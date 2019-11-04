package enterprise.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import enterprise.SpringBootWebApplication;
import enterprise.dto.DepartmentDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootWebApplication.class)
@AutoConfigureMockMvc
public class RESTcontrollersTests
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void create() throws Exception
    {
        DepartmentDTO headDep = new DepartmentDTO("Дирекция");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(headDep);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/departments/create")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals("Created successfully!", result.getResponse().getContentAsString());

        //perform get by name
        requestBuilder = MockMvcRequestBuilders
                .get("/departments/findbyname")
                .param("name", headDep.getName())
                .accept(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertFalse(result.getResponse().getContentAsString().isEmpty());

        JSONAssert.assertEquals(json,
                result.getResponse()
                        .getContentAsString(),
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("id", (o1, o2) -> true)));
        
        DepartmentDTO headDepFetched = mapper.readValue(result.getResponse()
                        .getContentAsString(), DepartmentDTO.class);
        assertNotNull(headDepFetched.getId());

        //create with parent
        DepartmentDTO subDep1 = new DepartmentDTO(headDep, "Бухгалтерия");

        json = mapper.writeValueAsString(subDep1);

        requestBuilder = MockMvcRequestBuilders
                .post("/departments/create")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals("Created successfully!", result.getResponse().getContentAsString());

        requestBuilder = MockMvcRequestBuilders
                .get("/departments/findbyname")
                .param("name", subDep1.getName())
                .accept(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertFalse(result.getResponse().getContentAsString().isEmpty());
        System.out.println(result.getResponse().getContentAsString());

        JSONAssert.assertEquals(json,
                result.getResponse()
                        .getContentAsString(),
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("parent.id", (o1, o2) -> true),
                        new Customization("id", (o1, o2) -> true)));

        DepartmentDTO subDep1Fetched = mapper.readValue(result.getResponse()
                .getContentAsString(), DepartmentDTO.class);
        assertNotNull(subDep1Fetched.getId());
        assertEquals(headDepFetched, subDep1Fetched.getParent());
    }

}
