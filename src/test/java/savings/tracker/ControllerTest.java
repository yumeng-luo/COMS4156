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
          "[{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":42.40669,\"lon\":-71.091868,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":42.4869,\"lon\":-71.2257,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":42.319621,\"lon\":-71.175656,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":42.205051,\"lon\":-71.152857,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":42.301849,\"lon\":-71.385773,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":42.29133,\"lon\":-71.67102,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":43.1557,\"lon\":-76.119,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":9.49,\"store\":\"Wegmans Store\",\"lat\":41.020969,\"lon\":-73.718156,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":43.0687,\"lon\":-76.0877,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House Grnd\",\"barcode\":\"78535783526\",\"price\":8.99,\"store\":\"Wegmans Store\",\"lat\":43.03306,\"lon\":-76.05325,\"sku\":\"399394\",\"tcin\":\"78535783526\",\"image\":\"https://wfmproducts.azureedge.net/images-500/785357835269.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":42.40669,\"lon\":-71.091868,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":42.4869,\"lon\":-71.2257,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":42.319621,\"lon\":-71.175656,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":42.205051,\"lon\":-71.152857,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":42.301849,\"lon\":-71.385773,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":42.29133,\"lon\":-71.67102,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":43.1557,\"lon\":-76.119,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":41.020969,\"lon\":-73.718156,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":43.0687,\"lon\":-76.0877,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"Peet's Coffee House WB\",\"barcode\":\"78535710052\",\"price\":5.92,\"store\":\"Wegmans Store\",\"lat\":43.03306,\"lon\":-76.05325,\"sku\":\"138366\",\"tcin\":\"78535710052\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00785357100527.jpg\"},{\"name\":\"CV Ella Coffee-Luna Drk\",\"barcode\":\"73038517790\",\"price\":9.99,\"store\":\"Wegmans Store\",\"lat\":43.1557,\"lon\":-76.119,\"sku\":\"766609\",\"tcin\":\"73038517790\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00730385177904.jpg\"},{\"name\":\"CV Ella Coffee-Luna Drk\",\"barcode\":\"73038517790\",\"price\":9.99,\"store\":\"Wegmans Store\",\"lat\":43.0687,\"lon\":-76.0877,\"sku\":\"766609\",\"tcin\":\"73038517790\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00730385177904.jpg\"},{\"name\":\"CV Ella Coffee-Luna Drk\",\"barcode\":\"73038517790\",\"price\":9.99,\"store\":\"Wegmans Store\",\"lat\":43.03306,\"lon\":-76.05325,\"sku\":\"766609\",\"tcin\":\"73038517790\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00730385177904.jpg\"},{\"name\":\"CV Ella Coffee-2Maria Med\",\"barcode\":\"73038517792\",\"price\":9.99,\"store\":\"Wegmans Store\",\"lat\":43.1557,\"lon\":-76.119,\"sku\":\"766617\",\"tcin\":\"73038517792\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00730385177928.jpg\"},{\"name\":\"CV Ella Coffee-2Maria Med\",\"barcode\":\"73038517792\",\"price\":9.99,\"store\":\"Wegmans Store\",\"lat\":43.0687,\"lon\":-76.0877,\"sku\":\"766617\",\"tcin\":\"73038517792\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00730385177928.jpg\"},{\"name\":\"CV Ella Coffee-2Maria Med\",\"barcode\":\"73038517792\",\"price\":9.99,\"store\":\"Wegmans Store\",\"lat\":43.03306,\"lon\":-76.05325,\"sku\":\"766617\",\"tcin\":\"73038517792\",\"image\":\"https://wfmproducts.azureedge.net/images-500/00730385177928.jpg\"}]"
            + "")));
  }

  @Test
  public void postSelect() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/select_item")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"Peet's Coffee House Grnd\"}")));
  }
  

  @Test
  public void postSelectFinal() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/select_purchase?upc=78535783526")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"Peet's Coffee House Grnd\"}")));
  }
  

  @Test
  public void getNoAlt() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/no_alternative")
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content()
            .string(equalTo("{\"code\":200,\"message\":\"No Alternative\"}")));
  }

}