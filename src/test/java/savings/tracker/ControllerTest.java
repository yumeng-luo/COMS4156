package savings.tracker;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void getFrontend() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/frontend")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string(equalTo("Placeholder for frontend")));
  }

  @Test
  public void postSearch() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/search")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string(equalTo(
            "[{\"name\":\"Good Coffee\",\"barcode\":\"123456789\",\"price\":5.39,"
            + "\"store\":\"McDonalds\",\"lat\":45.0,\"lon\":23.0,\"sku\":null,"
            + "\"tcin\":\"123456789\"},{\"name\":\"Okay Coffee\",\"barcode\":\"111111111\","
            + "\"price\":3.29,\"store\":\"Tim Hortons\",\"lat\":54.0,\"lon\":13.0,"
            + "\"sku\":null,\"tcin\":\"111111111\"},"
            + "{\"name\":\"Bad Coffee\",\"barcode\":\"555555555\",\"price\":1.39,"
            + "\"store\":\"Walmart\",\"lat\":4.0,\"lon\":17.0,\"sku\":null,"
            + "\"tcin\":\"555555555\"}]")));
  }

  @Test
  public void postSelect() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/select_item")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"Good Coffee\"}")));
  }

  @Test
  public void postSelectFinal() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?tcin=111111111")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"Okay Coffee\"}")));
  }

  @Test
  public void getNoAlt() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/no_alternative")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"No Alternative\"}")));
  }

}